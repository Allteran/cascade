import Vue from 'vue'
import VueRouter from 'vue-router'
import StartPage from 'pages/StartPage.vue'
import AdminPanel from 'pages/admin/AdminPanel.vue'
import RepairDeviceType from 'pages/admin/workshop/DeviceType.vue'
import RepairDeviceStatus from 'pages/admin/workshop/Status.vue'
import OrderList from 'pages/workshop/OrderList.vue'
import CreateOrder from 'pages/workshop/CreateOrder.vue'
import Role from 'pages/admin/manager/Role.vue'
import Organization from 'pages/admin/manager/Organization.vue'
import POSType from 'pages/admin/manager/POSType.vue'
import PointOfSales from 'pages/admin/manager/PointOfSales.vue'
import LoginPage from 'pages/LoginPage.vue'
import NotFound from 'pages/NotFound.vue'

Vue.use(VueRouter)

const routes = [
    {path: '/', component: StartPage},
    {path: '/login', component: LoginPage},
    {path: '/adm/panel', component: AdminPanel},
    {path: '/adm/repair/device-type', component: RepairDeviceType},
    {path: '/adm/repair/status', component: RepairDeviceStatus},
    {path: '/adm/manage/employee-role', component: Role},
    {path: '/adm/manage/organization', component: Organization},
    {path: '/adm/manage/pos-type', component: POSType},
    {path: '/adm/manage/pos', component: PointOfSales},
    {path: '/workshop/order', component: OrderList},
    {path: '/workshop/order/new', component: CreateOrder},
    {path: '/404', component: NotFound},
    {path: '*', redirect: '/404'}
]

export default new VueRouter({
    mode: 'history',
    base: '/',
    routes: routes
})