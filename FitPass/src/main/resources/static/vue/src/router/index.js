import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home'
import Register from '../views/Register'
import Login from "../views/Login"
import AdministratorCreateProfiles from "../views/AdministratorCreateProfiles";
import AllProfiles from "../views/AllProfiles";
import CreateFacility from "../views/CreateFacility";

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
    },
    {
        path: '/allProfiles',
        name: 'AllProfiles',
        component: AllProfiles
    },
    {
        path: '/createFacility',
        name: 'CreateFacility',
        component: CreateFacility
    }

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router