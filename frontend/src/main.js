import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './router'
import request from './api/request'

const app = createApp(App)
    .use(router)
    .use(ElementPlus)

// 全局注册图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 挂载全局请求实例
app.config.globalProperties.$request = request

app.mount('#app')
