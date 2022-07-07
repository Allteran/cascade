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
        orgList:[],
        token: null
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

        saveToken(state, token) {
            state.token = token
        },
        clearToken(state) {
            state.token = null
        }

    },
    actions: {
        /**
         * Repair Device Type actions
         */
        async getDeviceTypeList({commit}) {
            await axios.get(workshopUrl + '/device-type/list', {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('getDeviceTypeListMutation', result.data)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },

        async addDeviceType({commit}, type) {
            await axios.post(workshopUrl + '/device-type/new', type, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    const index = this.state.deviceTypeList.findIndex(item => item.id === result.data.id)
                    if(index > -1) {
                        commit('updateDeviceTypeMutation', type)
                    } else {
                        commit('addDeviceTypeListMutation', type)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)

                })
        },
        async updateDeviceType({commit}, type) {
            await axios.put(workshopUrl + '/device-type/update/' + type.id, type, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('updateDeviceTypeMutation', type)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        /**
         * Repair Device Status actions
         */
        async getRepairStatusList({commit}) {
            await axios.get(workshopUrl + '/status/list', {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('getRepairStatusListMutation', result.data)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async addRepairStatus({commit}, status) {
            await axios.post(workshopUrl + '/status/new', status, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    const index = this.state.repairStatusList.findIndex(item => item.id === result.data.id)
                    if(index > -1) {
                        commit('updateRepairStatusMutation', status)
                    } else {
                        commit('addRepairStatusMutation', status)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async updateRepairStatus ({commit}, status) {
            await axios.put(workshopUrl + '/status/update/' + status.id, status, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('updateRepairStatusMutation', status)
                }).catch(er => {
                    throw new Error('Something went wrokg', er)
                })
        },

        /**
         * Repair Device Order actions
         */
        async getRepairOrderList({commit}) {
            await axios.get(workshopUrl + '/order/list', {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('getRepairOrderListMutation', result.data.orderList)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async addRepairOrder({commit}, order) {
            await axios.post(workshopUrl + '/order/new', order, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    const index = this.state.repairOrderList.findIndex(item => item.id === result.data.id)
                    if(index > -1) {
                        commit('updateRepairOrderMutation', order)
                    } else {
                        commit('addRepairOrderMutation', order)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async updateRepairOrder ({commit}, order) {
            await axios.put(workshopUrl + '/order/update/' + order.id, order, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('updateRepairOrderMutation', order)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },

        /**
         * Organization actions
         */
        async getOrganizationList({commit}) {
            await axios.get(manageUrl + '/organization/list', {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    if(result.data.orgList) {
                        commit('getOrganizationListMutation', result.data.orgList)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async addOrganization({commit}, org) {
            await axios.post(manageUrl + '/organization/new', org, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    const index = this.state.orgList.findIndex(item => item.id === result.data.orgList[0].id)
                    if(index > -1) {
                        commit('updateOrganizationMutation', org)
                    } else {
                        commit('addOrganizationMutation', org)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async updateOrganization ({commit}, org) {
            await axios.put(manageUrl + '/organization/update/' + org.id, org, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('updateOrganizationMutation', org)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },

        /**
         * POSType actions
         */
        async getPOSTypeList({commit}) {
            await axios.get(manageUrl + '/pos-type/list', {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    if(result.data.posTypeList.at(0) != null) {
                        commit('getPOSTypeListMutation', result.data.posTypeList)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async addPOSType({commit}, type) {
            await axios.post(manageUrl + '/pos-type/new', type, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    const index = this.state.posTypeList.findIndex(item => item.id === result.data.posTypeList[0].id)
                    if(index > -1) {
                        commit('updatePOSTypeMutation', type)
                    } else {
                        commit('addPOSTypeMutation', type)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async updatePOSType ({commit}, type) {
            await axios.put(manageUrl + '/pos-type/update/' + type.id, type, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('updatePOSTypeMutation', type)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },

        /**
         * POS actions
         */
        async getPOSList({commit}) {
            await axios.get(manageUrl + '/pos/list', {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    if(result.data.posList.at(0) != null) {
                        commit('getPOSListMutation', result.data.posList)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async addPOS({commit}, pos) {
            await axios.post(manageUrl + '/pos/new', pos, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    const index = this.state.posList.findIndex(item => item.id === result.data.posList[0].id)
                    if(index > -1) {
                        commit('updatePOSMutation', pos)
                    } else {
                        commit('addPOSMutation', pos)
                    }
                }).catch(er=> {
                    throw new Error('Something went wrong', er)
                })
        },
        async updatePOS ({commit}, pos) {
            await axios.put(manageUrl + '/pos/update/' + pos.id, pos, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('updatePOSMutation', pos)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },

        /**
         * Employee Role actions
         */
        async getRoleList({commit}) {
            await axios.get(manageUrl + '/employee-role/list', {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    commit('getRoleListMutation', result.data.roles)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async addRole({commit}, role) {
            await axios.post(manageUrl + '/employee-role/new', role, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    const index = this.state.roleList.findIndex(item => item.id === result.data.roles[0].id)
                    if(index > -1) {
                        commit('updateRoleMutation', role)
                    } else {
                        commit('addRoleMutation', role)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },
        async updateRole ({commit}, role) {
            await axios.put(manageUrl + '/employee-role/update/' + role.id, role, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            })
                .then(result => {
                    if(result.data !== null) {
                        commit('updateRoleMutation', role)
                    }
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },


        /**
         * Employee Engineer action
         */
        async getEngineerList({commit}) {
            await axios.get(manageUrl + '/employee/search/role/', {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                },
                params: engineerRoles,
                paramsSerializer: params => {
                    return params.map((keyValuePair) => new URLSearchParams(keyValuePair)).join("&")
                }
            })
                .then(result => {
                    commit('getEngineerListMutation', result.data.employeeDTOList)
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
        },

        /**
         * Module to generate and download certificates
         */
        async generateAcceptanceCertificate({commit}, order) {
            await axios.post(workshopUrl + "/order/file/acceptance_cert", order, {
                headers: {
                    'Authorization': 'Bearer ' + this.state.token
                }
            }).then(result => {
                axios({
                    url: workshopUrl + "/order/file/acceptance_cert",
                    method: 'GET',
                    responseType: 'blob',
                    headers: {
                        'Authorization': 'Bearer ' + this.state.token
                    }
                }).then((response) => {
                    let fileURL = window.URL.createObjectURL(new Blob([response.data]));
                    let fileLink = document.createElement('a');

                    fileLink.href = fileURL;
                    fileLink.setAttribute('download', 'ACCEPTANCE_CERTIFICATE.xlsx');
                    document.body.appendChild(fileLink);
                    fileLink.click();
                }).catch(er => {
                    throw new Error('Something went wrong', er)
                })
            })
        }
    }
})
