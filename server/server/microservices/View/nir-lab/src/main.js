import Vue from 'vue'
import App from './App.vue'
import * as VeeValidate from 'vee-validate'
import router from './router'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'

// Import Bootstrap an BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import vuetify from './plugins/vuetify';
import VueTableDynamic from 'vue-table-dynamic';
import ToggleButton from 'vue-js-toggle-button';
import draggable from 'vuedraggable';
// dialogs
import VueModal from '@kouts/vue-modal';
import '@kouts/vue-modal/dist/vue-modal.css';
// http petitions
import Axios from 'axios'
import VueAxios from 'vue-axios'
// login
import store from './store';


// Make BootstrapVue available throughout your project
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)

// For use table
Vue.use(VueTableDynamic)

// For use ToggleButtons
Vue.use(ToggleButton)

// For draggable component
Vue.use(draggable)

Vue.use(VeeValidate)

// For dialogs
Vue.use(VueModal)

// For HTTP petitions
Vue.prototype.$http = Axios
Vue.use(VueAxios, Axios)

// For login
const token = localStorage.getItem('token');
if (token) {
    Vue.prototype.$http.defaults.headers.common['Authentication = token']
}

Vue.config.productionTip = false

new Vue({
    router,
    vuetify,
    store,
    render: h => h(App)
}).$mount('#app')