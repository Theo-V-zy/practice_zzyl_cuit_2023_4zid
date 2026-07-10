import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './router'



import axios from 'axios';
// 直接修改全局默认配置
//每次发送请求写凭证数据
axios.defaults.withCredentials = true;
// 配置发送请求的url前缀，接口地址的前缀
axios.defaults.baseURL = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
// cookie携带的凭证数据的保存时长
axios.defaults.timeout = 5000;



const app=createApp(App)
    .use(router)
    .use(ElementPlus);


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount('#app');
