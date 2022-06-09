import Vue from 'vue'
// import 'es6-promise/auto' //to support old browsers
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)
const workshopUrl = 'http://localhost:9090/api/v1/workshop'

export default new Vuex.Store({
    state: {
        orderStatusList:[],
        deviceTypeList:[],
        repairStatusList:[],
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
        }

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
    }
})
