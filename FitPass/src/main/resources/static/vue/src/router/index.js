import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home'
import Register from '../views/Register'
import Login from "../views/Login"
import AdministratorCreateProfiles from "../views/AdministratorCreateProfiles";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/register',
        name: 'Register',
        component: Register
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/administratorCreateProfiles',
        name: 'AdministratorCreateProfiles',
        component: AdministratorCreateProfiles
    }

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router