import fs from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const __dirname = path.dirname(fileURLToPath(import.meta.url))
const root = path.resolve(__dirname, '..')
const sourceDir = path.join(root, 'static')
const outputDir = path.join(root, 'dist/build/mp-weixin')
const staticOutput = path.join(outputDir, 'static')
const assetsOutput = path.join(outputDir, 'assets')

// 1. Copy all static files to dist/static/
fs.mkdirSync(staticOutput, { recursive: true })
fs.cpSync(sourceDir, staticOutput, { recursive: true })

// 2. Parse compiled assets.js to find hashed image names, then copy originals
const assetsJsPath = path.join(outputDir, 'common/assets.js')
if (!fs.existsSync(assetsJsPath)) {
  console.error('assets.js not found — run uni build first')
  process.exit(1)
}

const assetsModule = fs.readFileSync(assetsJsPath, 'utf8')

// Extract all unique /assets/xxx.[hash].png references
const hashed = new Set()
for (const m of assetsModule.matchAll(/\/assets\/([a-zA-Z0-9_\-]+\.[a-f0-9]+\.png)/g)) {
  hashed.add(m[1])
}

fs.mkdirSync(assetsOutput, { recursive: true })

// Map: hashed name → source filename (strip hash)
for (const hashedName of hashed) {
  // e.g. "logo.ea958e5e.png" → "logo.png"
  const origName = hashedName.replace(/\.[a-f0-9]+(?=\.png$)/, '')
  const srcPath = path.join(sourceDir, origName)
  if (fs.existsSync(srcPath)) {
    fs.copyFileSync(srcPath, path.join(assetsOutput, hashedName))
    console.log(`  ${origName} → ${hashedName}`)
  } else {
    console.warn(`  SKIP: source not found for ${hashedName} (expected ${origName})`)
  }
}

console.log(`Mini program assets copied. (${hashed.size} hashed files)`)

