import Vue from "vue"
import 'api/resource'
import App from 'pages/App.vue'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'

Vue.use(Vuetify)

new Vue({
    vuetify: new Vuetify(),
    el: "#app",
    render: a => a(App)
})