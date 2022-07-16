<template>
  <v-form ref="form" v-model="valid" lazy-validation>
    <v-container>
      <v-row
          justify="space-between"
      >
        <v-col>
          <v-flex class="mt-4" >
            <h2>ЗАЯВКА НА РЕМОНТ [РЕДАКТИРОВАНИЕ]</h2>
          </v-flex>
        </v-col>
        <v-col
            md="4"
        >
          <v-flex class="mt-4" >
            <h2>Цена: {{ order.totalPrice }} р.</h2>
          </v-flex>
        </v-col>
      </v-row>
      <v-flex class="mt-4 mb-2">
        <h3>Основная информация</h3>
      </v-flex>
      <v-row>
        <v-col
            cols="auto"
            md="4"
        >
          <v-menu
              ref="creationDatePicker"
              v-model="creationDatePicker"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                  v-model="creationDate"
                  label="Дата приема"
                  persistent-hint
                  prepend-icon="mdi-calendar"
                  v-bind="attrs"
                  v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
                v-model="creationDate"
                no-title
                @input="creationDatePicker = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col
            cols="auto"
            md="4"
        >
          <v-menu
              ref="issueDatePicker"
              v-model="issueDatePicker"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                  v-model="issueDate"
                  label="Дата выдачи"
                  persistent-hint
                  prepend-icon="mdi-calendar"
                  v-bind="attrs"
                  v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
                v-model="issueDate"
                no-title
                @input="issueDatePicker = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col
            cols="auto"
            md="4"
        >
          <v-select
              v-model="selectedStatus"
              :items="repairStatusList"
              item-text="name"
              item-value="id"
              label="Статус ремонта"
              prepend-icon="mdi-check"
              persistent-hint
              return-object
              single-line
          ></v-select>
        </v-col>
      </v-row>
      <v-flex class="mt-4 mb-2">
        <h3>Заказчик</h3>
      </v-flex>
      <v-row>
        <v-col
            cols="6"
            md="6"
        >
          <v-text-field
              v-model="order.customerName"
              :rules="inputFieldRules"
              label="Заказчик"
              prepend-icon="mdi-account"
              required
          ></v-text-field>
        </v-col>
        <v-col
            cols="6"
            md="6"
        >
          <v-text-field
              v-model="order.customerPhone"
              :rules="phoneRules"
              :counter="11"
              label="Контактный номер телефона"
              prepend-icon="mdi-phone"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-flex class="mt-4 mb-2">
        <h3>Устройство</h3>
      </v-flex>
      <v-row>
        <v-col
            cols="8"
            md="4"
        >
          <v-select
              v-model="selectedDeviceType"
              :items="deviceTypeList"
              item-text="name"
              item-value="id"
              label="Тип устройства"
              prepend-icon="mdi-cellphone"
              persistent-hint
              return-object
              single-line
          ></v-select>
        </v-col>
        <v-col
            cols="8"
            md="4"
        >
          <v-text-field
              v-model="order.deviceName"
              :rules="inputFieldRules"
              label="Модель устройства"
              prepend-icon="mdi-cellphone-text"
              required
          ></v-text-field>
        </v-col>
        <v-col
            cols="8"
            md="4"
        >
          <v-text-field
              v-model="order.serialNumber"
              :rules="inputFieldRules"
              label="Серийный номер (IMEI)"
              prepend-icon="mdi-numeric"
              required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="12"
        >
          <v-text-field
              v-model="order.defect"
              :rules="inputFieldRules"
              label="Описание неисправности"
              prepend-icon="mdi-comment-text-outline"
              required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="auto"
            md="6"
        >
          <v-text-field
              v-model="order.appearance"
              :rules="inputFieldRules"
              label="Состояние устройства"
              prepend-icon="mdi-cellphone-information"
              required
          ></v-text-field>
        </v-col>
        <v-col
            cols="auto"
            md="6"
        >
          <v-text-field
              v-model="order.warranty"
              :rules="inputFieldRules"
              label="Гарантийный срок"
              prepend-icon="mdi-alert-circle"
              required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-flex class="mt-4 mb-2">
        <h3>Выполненные работы и цены</h3>
      </v-flex>
      <v-row>
        <v-col
            cols="auto"
            md="4"
        >
          <v-select
              v-model="selectedEngineer"
              :items="engineerList"
              item-value="id"
              :item-text="engineerName"
              label="Исполнитель"
              persistent-hint
              prepend-icon="mdi-shield-account"
              return-object
              single-line
          >
          </v-select>
        </v-col>
        <v-col
            cols="auto"
            md="4"
        >
          <v-text-field
              v-model="order.performedActions"
              :rules="inputFieldRules"
              label="Проведенные работы"
              prepend-icon="mdi-cog-outline"
              required
          ></v-text-field>
        </v-col>
        <v-col
            cols="auto"
            md="4"
        >
          <v-text-field
              v-model="order.preliminaryPrice"
              label="Предварительная цена"
              :rules="inputFieldRules"
              type="number"
              prepend-icon="mdi-cash"
              required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="auto"
            md="4"
        >
          <v-text-field
              v-model="order.componentPrice"
              label="Стоимость запчастей"
              :rules="inputFieldRules"
              type="number"
              prepend-icon="mdi-cash"
              required
          ></v-text-field>
        </v-col>
        <v-col
            cols="auto"
            md="4"
        >
          <v-text-field
              v-model="order.marginPrice"
              label="Маржа"
              :rules="inputFieldRules"
              type="number"
              prepend-icon="mdi-cash"
              required
          ></v-text-field>
        </v-col>
        <v-col
            cols="auto"
            md="4"
        >
          <v-text-field
              v-model="order.totalPrice"
              label="Итоговая цена"
              :rules="[ totalPriceRule ]"
              type="number"
              prepend-icon="mdi-cash"
              required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="auto"
            md="4"
        >
          <v-text-field
              v-model="order.authorName"
              label="Автор заявки"
              prepend-icon="mdi-clipboard-account"
              readonly
          ></v-text-field>
        </v-col>
        <v-col
            cols="auto"
            md="4"
        >
          <v-select
              v-model="selectedPOS"
              :items="posList"
              item-text="shortName"
              item-value="id"
              label="Торговая точка"
              persistent-hint
              prepend-icon="mdi-map-marker"
              return-object
              single-line
          ></v-select>
        </v-col>
        <v-col
            cols="auto"
            md="4"
        >
          <v-text-field
              v-model="order.comment"
              label="Комментарий"
              prepend-icon="mdi-comment"
          ></v-text-field>
        </v-col>
      </v-row>

      <v-row>
        <v-col
            cols="8"
            md="4"
        >
          <v-dialog
              v-model="discardChangesDialog"
              persistent
              max-width="400"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  block
                  depressed
                  large
                  v-bind="attrs"
                  v-on="on"
              >
                Отменить изменения
              </v-btn>
            </template>
            <v-card>
              <v-card-title class="text-h5">
                Выйти?
              </v-card-title>
              <v-card-text>Уверены, что хотите выйти? Все внесенные и не сохраненные изменения будут утрачены</v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                    color="green darken-1"
                    text
                    @click="discardChangesDialog = false"
                >
                  Отменить
                </v-btn>
                <v-btn
                    color="green darken-1"
                    text
                    @click="discardChanges"
                >
                  Выйти без сохранения
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-col>
        <v-col
            cols="8"
            md="4"
        >
          <v-btn
              block
              depressed
              color="green"
              large
              @click="printRepairCertificate"
          >
            Распечатать акт работ
          </v-btn>
        </v-col>
        <v-col
            cols="8"
            md="4"
        >
          <v-dialog
              v-model="saveChangesDialog"
              persistent
              max-width="400"
          >
            <template v-slot:activator="{ on, attrs }">
            </template>
            <v-card>
              <v-card-title class="text-h5">
                Отлично!
              </v-card-title>
              <v-card-text v-text="notificationMessage"></v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                    color="green darken-1"
                    text
                    @click="redirectToOrderList"
                >
                  OK
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-btn
              block
              depressed
              color="primary"
              large
              @click="saveChanges"
          >
            Сохранить изменения
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-form>
</template>

<script>
import {mapState, mapActions} from 'vuex'
import moment from 'moment'
export default {
  name: 'OrderEdit',
  data: () => ({
    inputFieldRules: [
      v => !!v || 'Поле не может быть пустым',
    ],
    phoneRules: [
      v => !!v || 'Номер телефона не может быть пустым',
      v => /^[\d]{11}$/.test(v) || '7XXXXXXXXXX',
    ],
    discardChangesDialog: false,
    saveChangesDialog: false,
    valid: false,
    notificationMessage: '',
    order: [],
    creationDatePicker: false,
    issueDatePicker: false,
    creationDate:[],
    issueDate:[],
    selectedPOS: {
      id: '',
      region: '',
      city: '',
      address: '',
      shortName: '',
      typeId: '',
      typeName: ''
    },
    selectedEngineer: {
      id: '',
      phone: '',
      firstName: '',
      lastName: '',
      password: '',
      passwordConfirm: '',
      newPassword: '',
      organizationId: '',
      organizationName: '',
      active: '',
      creationDate: '',
      hireDate: '',
      dismissalDate: '',
      roles: []
    },
    selectedStatus: {
      id: '',
      name: ''
    },
    selectedDeviceType: {
      id: '',
      name: ''
    }
  }),
  computed: {
    ...mapState(['repairOrderList','deviceTypeList', 'repairStatusList', 'posList', 'engineerList'])
  },
  beforeMount() {
    this.order = this.repairOrderList.find(o => o.id === this.$route.params.id)
    try {
      this.getDeviceTypeList().then(res => {
        this.selectedDeviceType = this.deviceTypeList.find(t => t.id === this.order.deviceTypeId)
      })
      this.getRepairStatusList().then(res => {
        this.selectedStatus = this.repairStatusList.find(s => s.id === this.order.statusId)
      })
      this.getEngineerList().then(res => {
        this.selectedEngineer = this.engineerList.find(e => e.id === this.order.engineerId)
      })
      this.getPOSList().then(res => {
        this.selectedPOS = this.posList.find(p => p.id === this.order.posId)
      })
    } catch (er) {
      this.redirectToLogin()
      return
    }
    this.creationDate = new Date(this.order.creationDate).toISOString().substring(0, 10)
    if(this.order.issueDate === '0001-01-01' || this.order.issueDate === null || this.order.issueDate === []) {
      this.issueDate = []
    } else {
      this.issueDate = new Date(this.order.issueDate).toISOString().substring(0, 10)
    }
  },
  methods: {
    ...mapActions(['getRepairStatusList', 'getDeviceTypeList', 'generateRepairCertificate', 'updateRepairOrder', 'getEngineerList', 'getPOSList']),
    //format date according to LocalDate string format
    dateFormat(date) {
      return moment(date).format('YYYY-MM-DD')
    },
    totalPriceRule(value) {
      const sum = this.order.componentPrice + this.order.marginPrice
      if(sum > value) {
        return 'Итоговая цена не может быть меньше суммы остальных цен'
      } else if(value === '') {
        return 'Поле не может быть пустым'
      } else {
        return true
      }
    },
    engineerName: item => item.firstName + ' ' + item.lastName,
    validate() {
      let priceSum = this.order.componentPrice + this.order.marginPrice
      if(this.$refs.form.validate()) {
        if(priceSum <= this.order.totalPrice) {
          this.order.creationDate = this.creationDate
          if(this.issueDate !== []) {
            this.order.issueDate = this.issueDate
          }
          this.valid = true
        }
      } else {
        this.valid = false
      }
    },
    printRepairCertificate() {
      this.validate()
      if (this.valid) {
        this.notificationMessage = 'Сейчас начнется загрузка акта работ. Все изменения будут сохранены автоматически. Нажмите "ОК" чтобы перейти к реестру заказов'
        this.order.statusName = this.selectedStatus.name
        this.order.statusId = this.selectedStatus.id
        this.generateRepairCertificate(this.order)
        this.saveChangesDialog = true
        this.updateRepairOrder(this.order).catch(er => {
          this.redirectToOrderList()
        })
      }
    },
    saveChanges() {
      this.validate()
      if(this.valid) {
        this.order.statusName = this.selectedStatus.name
        this.order.statusId = this.selectedStatus.id
        this.updateRepairOrder(this.order).catch(er => {
          this.redirectToOrderList()
        })
        this.notificationMessage = 'Все изменения были сохранены. Нажмите "ОК чтобы перейти к реестру заказов'
        this.saveChangesDialog = true
      }
    },
    discardChanges() {
      this.redirectToOrderList()
    },
    redirectToOrderList() {
      this.$router.push('/workshop/order')
    },
    redirectToLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style>
</style>