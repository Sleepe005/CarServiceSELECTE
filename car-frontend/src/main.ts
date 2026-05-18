import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css' // <-- импортируем стили
import App from './App.vue'

const app = createApp(App)
app.use(ElementPlus)
app.mount('#app')