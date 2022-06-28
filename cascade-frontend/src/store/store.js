import Vue from 'vue'
// import 'es6-promise/auto' //to support old browsers
import Vuex from 'vuex'
import axios from 'axios'
import ConstConfig from 'util/ConstConfig'

Vue.use(Vuex)
const workshopUrl = ConstConfig.url.workshopUrl
const manageUrl = ConstConfig.url.manageUrl

const engineerRoles = [{role: ConstConfig.role.engineer},{role: ConstConfig.role.headEngineer}]

export default new Vuex.Store({
    state: {
        orderStatusList:[],
        deviceTypeList:[],
        repairStatusList:[],
        repairOrderList:[],
        posTypeList:[],
        posList:[],
        engineerList: [],
        roleList:[],
        orgList:[]
    },
    mutations: {
        /**
         * Repair Device Type mutations
         */
        getDeviceTypeListMutation(state, list) {
            state.deviceTypeList = list
        },
        addDeviceTypeListMutation(state, type) {
            state.deviceTypeList = [
                ...state.deviceTypeList,
                type
            ]
        },
        updateDeviceTypeMutation(state, type) {
            const index = state.deviceTypeList.findIndex(item => item.id === type.id)
            if (index > -1) {
                state.deviceTypeList = [
                    ...state.deviceTypeList.slice(0, index),
                    type,
                    ...state.deviceTypeList.slice(index + 1)
                ]
            }
        },
        /**
         * Repair Device Status
         */
        getRepairStatusListMutation(state, list) {
            state.repairStatusList = list
        },
        addRepairStatusMutation(state, status) {
            state.repairStatusList = [
                ...state.repairStatusList,
                status
            ]
        },
        updateRepairStatusMutation(state, status) {
            const index = state.repairStatusList.findIndex(item => item.id === status.id)
            if (index > -1) {
                state.repairStatusList = [
                    ...state.repairStatusList.slice(0, index),
                    status,
                    ...state.repairStatusList.slice(index + 1)
                ]
            }
        },

        /**
         * Repair Device Order mutations
         */
        getRepairOrderListMutation(state, list) {
            state.repairOrderList = list
        },
        addRepairOrderMutation(state, order) {
            state.repairOrderList = [
                ...state.repairOrderList,
                order
            ]
        },
        updateRepairOrderMutation(state, order) {
            const index = state.repairOrderList.findIndex(item => item.id === order.id)
            if (index > -1) {
                state.repairOrderList = [
                    ...state.repairOrderList.slice(0, index),
                    order,
                    ...state.repairOrderList.slice(index + 1)
                ]
            }
        },


        /**
         * Employee Roles mutations
         */
        getRoleListMutation(state, list) {
            state.roleList = list
        },
        addRoleMutation(state, pos) {
            state.roleList = [
                ...state.roleList,
                pos
            ]
        },
        updateRoleMutation(state, pos) {
            const index = state.roleList.findIndex(item => item.id === pos.id)
            if (index > -1) {
                state.roleList = [
                    ...state.roleList.slice(0, index),
                    pos,
                    ...state.roleList.slice(index + 1)
                ]
            }
        },

        /**
         * Employee Engineer mutation
         */
        getEngineerListMutation(state, list) {
            state.engineerList = list
        },

        /**
         * Organization mutations
         */
        getOrganizationListMutation(state, list) {
            state.orgList = list
        },
        addOrganizationMutation(state, org) {
            state.orgList = [
                ...state.orgList,
                org
            ]
        },
        updateOrganizationMutation(state, org) {
            const index = state.orgList.findIndex(item => item.id === org.id)
            if (index > -1) {
                state.orgList = [
                    ...state.orgList.slice(0, index),
                    org,
                    ...state.orgList.slice(index + 1)
                ]
            }
        },

        /**
         * POSType mutations
         */
        getPOSTypeListMutation(state, list) {
            state.posTypeList = list
        },
        addPOSTypeMutation(state, type) {
            state.posTypeList = [
                ...state.posTypeList,
                type
            ]
        },
        updatePOSTypeMutation(state, type) {
            const index = state.posTypeList.findIndex(item => item.id === type.id)
            if (index > -1) {
                state.posTypeList = [
                    ...state.posTypeList.slice(0, index),
                    type,
                    ...state.posTypeList.slice(index + 1)
                ]
            }
        },

        /**
         * POS mutations
         */
        getPOSListMutation(state, list) {
            state.posList = list
        },
        addPOSMutation(state, pos) {
            state.posList = [
                ...state.posList,
                pos
            ]
        },
        updatePOSMutation(state, pos) {
            const index = state.posList.findIndex(item => item.id === pos.id)
            if (index > -1) {
                state.posList = [
                    ...state.posList.slice(0, index),
                    pos,
                    ...state.posList.slice(index + 1)
                ]
            }
        },

    },
    actions: {
        /**
         * Repair Device Type actions
         */
        async getDeviceTypeList({commit}) {
            await axios.get(workshopUrl + '/device-type/list')
                .then(result => {
                    commit('getDeviceTypeListMutation', result.data)
                })
        },

        async addDeviceType({commit}, type) {
            await axios.post(workshopUrl + '/device-type/new', type)
                .then(result => {
                    const index = this.state.deviceTypeList.findIndex(item => item.id === result.data.id)
                    if(index > -1) {
                        commit('updateDeviceTypeMutation', type)
                    } else {
                        commit('addDeviceTypeListMutation', type)
                    }
                })
        },
        async updateDeviceType({commit}, type) {
            await axios.put(workshopUrl + '/device-type/update/' + type.id, type)
                .then(result => {
                    commit('updateDeviceTypeMutation', type)
                })
        },
        /**
         * Repair Device Status actions
         */
        async getRepairStatusList({commit}) {
            await axios.get(workshopUrl + '/status/list')
                .then(result => {
                    commit('getRepairStatusListMutation', result.data)
                })
        },
        async addRepairStatus({commit}, status) {
            await axios.post(workshopUrl + '/status/new', status)
                .then(result => {
                    const index = this.state.repairStatusList.findIndex(item => item.id === result.data.id)
                    if(index > -1) {
                        commit('updateRepairStatusMutation', status)
                    } else {
                        commit('addRepairStatusMutation', status)
                    }
                })
        },
        async updateRepairStatus ({commit}, status) {
            await axios.put(workshopUrl + '/status/update/' + status.id, status)
                .then(result => {
                    commit('updateRepairStatusMutation', status)
                })
        },

        /**
         * Repair Device Order actions
         */
        async getRepairOrderList({commit}) {
            await axios.get(workshopUrl + '/order/list')
                .then(result => {
                    commit('getRepairOrderListMutation', result.data.orderList)
                })
        },
        async addRepairOrder({commit}, order) {
            await axios.post(workshopUrl + '/order/new', order)
                .then(result => {
                    const index = this.state.repairOrderList.findIndex(item => item.id === result.data.id)
                    if(index > -1) {
                        commit('updateRepairOrderMutation', order)
                    } else {
                        commit('addRepairOrderMutation', order)
                    }
                })
        },
        async updateRepairOrder ({commit}, order) {
            await axios.put(workshopUrl + '/order/update/' + order.id, order)
                .then(result => {
                    commit('updateRepairOrderMutation', order)
                })
        },

        /**
         * Organization actions
         */
        async getOrganizationList({commit}) {
            await axios.get(manageUrl + '/organization/list')
                .then(result => {
                    if(result.data.orgList) {
                        commit('getOrganizationListMutation', result.data.orgList)
                    }
                })
        },
        async addOrganization({commit}, org) {
            await axios.post(manageUrl + '/organization/new', org)
                .then(result => {
                    const index = this.state.orgList.findIndex(item => item.id === result.data.orgList[0].id)
                    if(index > -1) {
                        commit('updateOrganizationMutation', org)
                    } else {
                        commit('addOrganizationMutation', org)
                    }
                })
        },
        async updateOrganization ({commit}, org) {
            await axios.put(manageUrl + '/organization/update/' + org.id, org)
                .then(result => {
                    commit('updateOrganizationMutation', org)
                })
        },

        /**
         * POSType actions
         */
        async getPOSTypeList({commit}) {
            await axios.get(manageUrl + '/pos-type/list')
                .then(result => {
                    if(result.data.posTypeList.at(0) != null) {
                        commit('getPOSTypeListMutation', result.data.posTypeList)
                    }
                })
        },
        async addPOSType({commit}, type) {
            await axios.post(manageUrl + '/pos-type/new', type)
                .then(result => {
                    const index = this.state.posTypeList.findIndex(item => item.id === result.data.posTypeList[0].id)
                    if(index > -1) {
                        commit('updatePOSTypeMutation', type)
                    } else {
                        commit('addPOSTypeMutation', type)
                    }
                })
        },
        async updatePOSType ({commit}, type) {
            await axios.put(manageUrl + '/pos-type/update/' + type.id, type)
                .then(result => {
                    commit('updatePOSTypeMutation', type)
                })
        },

        /**
         * POS actions
         */
        async getPOSList({commit}) {
            await axios.get(manageUrl + '/pos/list')
                .then(result => {
                    if(result.data.posList.at(0) != null) {
                        commit('getPOSListMutation', result.data.posList)
                    }
                })
        },
        async addPOS({commit}, pos) {
            await axios.post(manageUrl + '/pos/new', pos)
                .then(result => {
                    const index = this.state.posList.findIndex(item => item.id === result.data.posList[0].id)
                    if(index > -1) {
                        commit('updatePOSMutation', pos)
                    } else {
                        commit('addPOSMutation', pos)
                    }
                })
        },
        async updatePOS ({commit}, pos) {
            await axios.put(manageUrl + '/pos/update/' + pos.id, pos)
                .then(result => {
                    commit('updatePOSMutation', pos)
                })
        },

        /**
         * Employee Role actions
         */
        async getRoleList({commit}) {
            await axios.get(manageUrl + '/employee-role/list')
                .then(result => {
                    commit('getRoleListMutation', result.data.roles)
                })
        },
        async addRole({commit}, role) {
            await axios.post(manageUrl + '/employee-role/new', role)
                .then(result => {
                    const index = this.state.roleList.findIndex(item => item.id === result.data.roles[0].id)
                    if(index > -1) {
                        commit('updateRoleMutation', role)
                    } else {
                        commit('addRoleMutation', role)
                    }
                })
        },
        async updateRole ({commit}, role) {
            await axios.put(manageUrl + '/employee-role/update/' + role.id, role)
                .then(result => {
                    if(result.data !== null) {
                        commit('updateRoleMutation', role)
                    }
                })
        },


        /**
         * Employee Engineer action
         */
        async getEngineerList({commit}) {
            await axios.get(manageUrl + '/employee/search/role/', {
                params: engineerRoles,
                paramsSerializer: params => {
                    return params.map((keyValuePair) => new URLSearchParams(keyValuePair)).join("&")
                }
            })
                .then(result => {
                    commit('getEngineerListMutation', result.data.employeeDTOList)
                })
        },
    }
})
