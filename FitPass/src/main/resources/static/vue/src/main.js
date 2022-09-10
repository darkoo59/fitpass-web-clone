import {createApp, ref} from 'vue'
import App from './App.vue'
import router from './router'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import '@vuepic/vue-datepicker/dist/main.css'
import Toaster from "@meforma/vue-toaster";

const app = createApp(App)
app.config.globalProperties.$port = ref('http://localhost:8081')
app.use(router).use(Toaster).mount('#app')
export { app }