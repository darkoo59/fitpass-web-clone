import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home'
import Register from '../views/Register'
import Login from "../views/Login"
import AdministratorCreateProfiles from "../views/AdministratorCreateProfiles";
import MyProfile from "@/views/MyProfile";
import AllProfiles from "../views/AllProfiles";
import CreateFacility from "../views/CreateFacility";
import SportFacility from "../views/SportFacility";

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
        path: '/userInfo',
        name: 'MyProfile',
        component: MyProfile
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
    },
    {
        path: '/sportFacility/:id',
        name: 'SportFacility',
        component: SportFacility
    }

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router