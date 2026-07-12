import fs from 'node:fs'
import path from 'node:path'

const root = process.cwd()
const sourceDir = path.join(root, 'static')
const outputDir = path.join(root, 'dist/build/mp-weixin')
const staticOutput = path.join(outputDir, 'static')
const assetsOutput = path.join(outputDir, 'assets')

fs.mkdirSync(staticOutput, { recursive: true })
fs.cpSync(sourceDir, staticOutput, { recursive: true })

const assetsModule = fs.readFileSync(path.join(outputDir, 'common/assets.js'), 'utf8')
const logoName = assetsModule.match(/\/assets\/(logo\.[a-f0-9]+\.png)/)?.[1]
const avatarName = assetsModule.match(/\/assets\/(default-avatar\.[a-f0-9]+\.png)/)?.[1]

fs.mkdirSync(assetsOutput, { recursive: true })
if (logoName) fs.copyFileSync(path.join(sourceDir, 'logo.png'), path.join(assetsOutput, logoName))
if (avatarName) fs.copyFileSync(path.join(sourceDir, 'default-avatar.png'), path.join(assetsOutput, avatarName))

console.log('Mini program assets copied.')
