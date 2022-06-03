import Vue from "vue"
import 'api/resource'
import App from 'pages/App.vue'
import vuetify from 'plugins/vuetify'


new Vue({
    vuetify,
    el: "#app",
    render: a => a(App)
})