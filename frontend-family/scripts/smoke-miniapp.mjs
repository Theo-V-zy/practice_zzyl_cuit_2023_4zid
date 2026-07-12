import path from 'node:path'
import automator from 'miniprogram-automator'

const projectPath = path.resolve('dist/build/mp-weixin')

function stage(message) {
  console.log(`[smoke] ${message}`)
}

const sleep = milliseconds => new Promise(resolve => setTimeout(resolve, milliseconds))

const wsEndpoint = process.env.WECHAT_AUTOMATION_ENDPOINT
stage(wsEndpoint ? `connecting to ${wsEndpoint}` : 'launching WeChat DevTools')
const miniProgram = wsEndpoint
  ? await automator.connect({ wsEndpoint })
  : await automator.launch({
      cliPath: '/Applications/wechatwebdevtools.app/Contents/MacOS/cli',
      projectPath,
      trustProject: true,
      timeout: 60000
    })

try {
  stage('resetting local session')
  await miniProgram.callWxMethod('clearStorageSync')
  await miniProgram.callWxMethod('setStorageSync', 'familyApiBaseUrl', 'http://127.0.0.1:8080')

  stage('opening login page')
  await miniProgram.evaluate(() => {
    wx.reLaunch({ url: '/pages/login/login' })
    return true
  })
  await sleep(2500)
  let page = await miniProgram.currentPage()
  if (page.path !== 'pages/login/login') throw new Error(`Login page failed to open: ${page.path}`)

  stage('submitting demo account')
  const submitted = await miniProgram.evaluate(() => {
    const pages = getCurrentPages()
    const current = pages[pages.length - 1]
    if (!current || !current.$vm || typeof current.$vm.login !== 'function') return false
    current.$vm.login()
    return true
  })
  if (!submitted) throw new Error('Login page method not found')
  await sleep(2200)

  page = await miniProgram.currentPage()
  if (page.path !== 'pages/home/home') throw new Error(`Login failed, current page: ${page.path}`)
  const homeData = await miniProgram.evaluate(() => getCurrentPages().at(-1)?.$vm?.$data)
  if (!homeData || !Array.isArray(homeData.roomTypes) || homeData.roomTypes.length === 0) {
    throw new Error('Home data failed to load')
  }
  stage('home data loaded')

  for (const [name, route, dataKey] of [
    ['family', '/pages/family/family', 'list'],
    ['service', '/pages/service/service', 'list'],
    ['mine', '/pages/mine/mine', 'user']
  ]) {
    stage(`checking ${name}`)
    await miniProgram.evaluate(url => {
      wx.switchTab({ url })
      return true
    }, route)
    await sleep(1200)
    page = await miniProgram.currentPage()
    if (page.path !== route.slice(1)) throw new Error(`Tab switch failed: ${page.path}`)
    const pageData = await miniProgram.evaluate(() => getCurrentPages().at(-1)?.$vm?.$data)
    if (!pageData || !pageData[dataKey] || (Array.isArray(pageData[dataKey]) && pageData[dataKey].length === 0)) {
      throw new Error(`${name} data failed to load`)
    }
  }

  console.log('Mini program smoke test passed: login, API data and four tab pages are working.')
} finally {
  if (wsEndpoint) {
    miniProgram.disconnect()
  } else {
    await miniProgram.close()
  }
}
