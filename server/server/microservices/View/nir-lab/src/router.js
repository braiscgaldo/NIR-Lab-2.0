import Vue from 'vue'
import Router from 'vue-router'
import Home from './components/Home.vue'
import Login from './components/public/login/login.vue'
import CreateAccount from './components/public/create_account/create_account.vue'
import GitHub from './components/public/github/github.vue'
import MobileApp from './components/public/mobile_app/mobile_app.vue'
import Information from './components/public/information/information.vue'
import Contributers from './components/public/contributers/contributers.vue'
import Acknowledgements from './components/public/acknowledgements/acknowledgements.vue'
import DataTreatment from './components/private/data_treatment/data_treatment.vue'
import Training from './components/private/training/training.vue'
import Logs from './components/private/logs/logs.vue'
import EditProfile from './components/private/edit_profile/edit_profile.vue'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path: '/create_account', //name to wrap the value
            name: 'create_account',
            component: CreateAccount
        },
        {
            path: '/gitgub', //name to wrap the value
            name: 'github',
            component: GitHub
        },
        {
            path: '/mobile_app', //name to wrap the value
            name: 'mobile_app',
            component: MobileApp
        },
        {
            path: '/information', //name to wrap the value
            name: 'information',
            component: Information
        },
        {
            path: '/contributers', //name to wrap the value
            name: 'contributers',
            component: Contributers
        },
        {
            path: '/acknowledgements', //name to wrap the value
            name: 'acknowledgements',
            component: Acknowledgements
        },
        {
            path: '/data_treatment', //name to wrap the value
            name: 'data_treatment',
            component: DataTreatment,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/training', //name to wrap the value
            name: 'training',
            component: Training,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/logs', //name to wrap the value
            name: 'logs',
            component: Logs,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/edit_profile', //name to wrap the value
            name: 'edit_profile',
            component: EditProfile,
            meta: {
                requiresAuth: true
            }
        }
    ]
})