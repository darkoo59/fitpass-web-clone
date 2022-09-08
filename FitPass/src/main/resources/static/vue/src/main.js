import {createApp, ref} from 'vue'
import App from './App.vue'
import router from './router'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import '@vuepic/vue-datepicker/dist/main.css'

const app = createApp(App)
app.config.globalProperties.$port = ref('http://localhost:8081')
app.use(router).mount('#app')
export { app }