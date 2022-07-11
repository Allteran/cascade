<template>
  <v-container>
    <v-data-table
        :headers="headers"
        :items="repairOrderList"
        :loading="loading"
        :loading-text="'Загружаем данные'"
        :no-data-text="'Ничено не найдено :('"
        :no-results-text="'Ничено не найдено :('"
        :items-per-page="15"
        @click:row="clickRow"
        fixed-header
        :search="search"
        class="elevation-1"
        :footer-props="{
          'items-per-page-options': [10, 20, 30, 40, 50, 100],
          'items-per-page-text':'Отображаемое количество',
        }"
    >
      <template v-slot:item.creationDate="{ item }">
        <td v-text="dateFormat(item.creationDate)"></td>
      </template>
      <template v-slot:item.issueDate="{ item }">
        <td v-text="dateFormat(item.issueDate)"></td>
      </template>
      <template v-slot:item.statusName="{ item }">
        <v-chip
            :color="getChipColor(item.statusId)"
            dark
        >
          {{ item.statusName }}
        </v-chip>
      </template>
      <template v-slot:top>
        <v-toolbar
            flat
        >
          <v-toolbar-title>Реестр ремонта</v-toolbar-title>
          <v-divider
              class="mx-4"
              inset
              vertical
          ></v-divider>
          <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Поиск заявки"
              single-line
              hide-details
          ></v-text-field>
        </v-toolbar>
      </template>
    </v-data-table>
    <v-btn
        class="mr-10 mb-10"
        fab
        color="accent"
        fixed
        bottom
        right
        large
        @click="showCreateOrderPage()"
    >
      <v-icon>mdi-plus</v-icon>
    </v-btn>
  </v-container>
</template>

<script>
import {mapActions, mapState} from 'vuex'
import moment from 'moment'

export default {
  name: 'OrderList',
  data: () => ({
    headers: [
      {text: 'Устройство', value: 'deviceName'},
      {text: 'Дефект', value: 'defect'},
      {text: 'Дата приема', value: 'creationDate'},
      {text: 'Дата выдачи', value: 'issueDate'},
      {text: 'Статус', value: 'statusName'},
      {text: 'Исполнитель', value: 'engineerName'},
      {text: 'Стоимость', value: 'totalPrice'},
      {text: 'Точка продаж', value: 'posShortName'},
      {text: 'Сотрудник', value: 'authorName'}
    ],
    search: '',
    loading: true,
  }),
  beforeMount() {
    this.getRepairOrderList().then(res => {
      this.loading = false
    }).catch(er => {
      this.loading = false
      this.redirectToLogin()
    })
  },
  computed: {
    ...mapState(['repairOrderList', 'statusIdRepaired', 'statusIdUnrepaired', 'statusIdPaid']),
  },
  methods: {
    ...mapActions(['getRepairOrderList']),
    getChipColor(id) {
      if(id === this.statusIdUnrepaired) return 'red'
      else if(id === this.statusIdPaid) return 'green'
      else if(id === this.statusIdRepaired) return 'yellow'
      else return 'black'
    },
    clickRow(item) {
      this.$router.push({name: 'order-details', params: {id: item.id}})
    },
    dateFormat(date) {
      if(date === '2000-01-01T01:01:00') {
        return 'Не выдан'
      } else {
        return moment(date).format('DD.MM.YYYY')
      }
    },
    showCreateOrderPage() {
      this.$router.push('/workshop/order/new')
    },
    redirectToLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style>

</style>