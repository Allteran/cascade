import Vue from "vue"
import Vuetify from 'vuetify'
import './api/resource'
import App from './pages/App.vue'

Vue.use(Vuetify)

new Vue({
    vuetify: new Vuetify(),
    el: "#app",
    render: a => a(App)
})