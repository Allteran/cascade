import Vue from 'vue'
import VueRouter from 'vue-router'
import StartPage from 'pages/StartPage.vue'
import AdminPanel from 'pages/admin/AdminPanel.vue'
import RepairDeviceType from 'pages/admin/workshop/DeviceType.vue'
import RepairDeviceStatus from 'pages/admin/workshop/Status.vue'

Vue.use(VueRouter)

const routes = [
    {path: '/', component: StartPage},
    {path:'/adm/panel', component: AdminPanel},
    {path: '/adm/repair/device-type', component: RepairDeviceType},
    {path: '/adm/repair/status', component: RepairDeviceStatus}
]

export default new VueRouter({
    mode: 'history',
    base: '/',
    routes: routes
})