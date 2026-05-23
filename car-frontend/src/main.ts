import { createApp } from 'vue'
import { createPinia } from 'pinia'  // ← импортируем Pinia
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// Создаём экземпляр Pinia
const pinia = createPinia()

const app = createApp(App)

app.use(pinia)      // ← СНАЧАЛА Pinia
app.use(router)     // ← потом Router
app.use(ElementPlus)

app.mount('#app')