const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: '0.0.0.0',
    port: process.env.VUE_APP_PORT || 5173,
    proxy: {
      '^/': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass(req) {
          // 不代理前端静态资源和页面访问
          const url = req.url
          if (req.headers.accept?.includes('text/html')) return url
          if (url.match(/\.(js|css|png|jpg|svg|ico|woff|ttf|map|json)(\?.*)?$/)) return url
          if (url.startsWith('/__webpack') || url.startsWith('/sockjs')) return url
          if (url === '/') return url
        }
      }
    }
  }
})
