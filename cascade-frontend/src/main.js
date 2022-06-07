import Vue from "vue"
import router from 'router/router'
import store from 'store/store'
import App from 'pages/App.vue'
import Vuetify from 'vuetify'
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/dist/vuetify.min.css'

Vue.use(Vuetify)

new Vue({
    vuetify: new Vuetify(),
    el: "#app",
    store: store,
    router: router,
    render: a => a(App)
})