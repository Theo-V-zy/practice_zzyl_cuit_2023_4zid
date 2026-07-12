import fs from 'node:fs'
import path from 'node:path'
import automator from 'miniprogram-automator'

const projectPath = path.resolve('dist/build/mp-weixin')
const outputDir = '/tmp/zzyl-family-smoke'
fs.mkdirSync(outputDir, { recursive: true })

const miniProgram = await automator.launch({
  cliPath: '/Applications/wechatwebdevtools.app/Contents/MacOS/cli',
  projectPath,
  trustProject: true,
  timeout: 60000
})

try {
  await miniProgram.callWxMethod('clearStorageSync')
  await miniProgram.callWxMethod('setStorageSync', 'familyApiBaseUrl', 'http://127.0.0.1:8080')

  let page = await miniProgram.reLaunch('/pages/login/login')
  await page.waitFor(600)
  const loginButton = await page.$('.login-btn')
  if (!loginButton) throw new Error('Login button not found')
  await loginButton.tap()
  await page.waitFor(1200)

  page = await miniProgram.currentPage()
  if (page.path !== 'pages/home/home') throw new Error(`Login failed, current page: ${page.path}`)
  await miniProgram.screenshot({ path: path.join(outputDir, 'home.png') })

  for (const [name, route] of [
    ['family', '/pages/family/family'],
    ['service', '/pages/service/service'],
    ['mine', '/pages/mine/mine']
  ]) {
    page = await miniProgram.switchTab(route)
    await page.waitFor(800)
    await miniProgram.screenshot({ path: path.join(outputDir, `${name}.png`) })
  }

  console.log(`Mini program smoke test passed. Screenshots: ${outputDir}`)
} finally {
  await miniProgram.close()
}
