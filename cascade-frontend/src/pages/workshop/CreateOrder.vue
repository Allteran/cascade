<template>
  <v-form ref="form" v-model="valid" lazy-validation>
    <v-container>
      <v-flex class="mt-4 mb-4">
        <h2>ЗАЯВКА НА РЕМОНТ</h2>
      </v-flex>
      <v-row>
        <v-col
            cols="8"
            md="4"
        >
          <v-select
              v-model="selectedType"
              :items="deviceTypeList"
              item-text="name"
              item-value="id"
              label="Тип устройства"
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
              label="Серийный номер"
              required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="6"
            md="6"
        >
          <v-text-field
              v-model="order.customerName"
              :rules="inputFieldRules"
              label="Заказчик"
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
              label="79XXXXXXXXX"
              :counter="11"
              required
          ></v-text-field>
        </v-col>

      </v-row>
      <v-row>
        <v-col
            cols="8"
            md="4"
        >
          <v-text-field
              v-model="order.defect"
              :rules="inputFieldRules"
              label="Заявленная неисправность"
              required
          ></v-text-field>
        </v-col>
        <v-col
            cols="8"
            md="4"
        >
          <v-text-field
              v-model="order.equipSet"
              :rules="inputFieldRules"
              label="Комплектация устройства"
              required
          ></v-text-field>
        </v-col>
        <v-col
            cols="8"
            md="4"
        >
          <v-text-field
              v-model="order.appearance"
              :rules="inputFieldRules"
              label="Состояние устройства"
              required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="8"
            md="4"
        >
          <v-select
              v-model="selectedPOS"
              :items="posList"
              item-text="shortName"
              item-value="id"
              label="Точка продаж"
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
              v-model="order.preliminaryPrice"
              :rules="inputFieldRules"
              label="Предварительная цена"
              type="number"
              required
          ></v-text-field>
        </v-col>

        <v-col
            cols="8"
            md="4"
        >
          <v-select
              v-model="selectedEngineer"
              :items="engineerList"
              item-text="lastName"
              item-value="id"
              label="Исполнитель"
              persistent-hint
              return-object
              single-line
          ></v-select>
        </v-col>
      </v-row>
      <v-dialog
          v-model="certificateDialog"
          persistent
          max-width="400"
      >
        <template v-slot:activator="{ on, attrs }">
        </template>
        <v-card>
          <v-card-title class="text-h5">
            Отлично!
          </v-card-title>
          <v-card-text>Отлично! Сейчас начнется скачивание акта. Не забудьте сохранить заявку</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                color="green darken-1"
                text
                @click="closeCertificateDialog"
            >
              OK
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog
          v-model="saveOrderDialog"
          persistent
          max-width="400"
      >
        <template v-slot:activator="{ on, attrs }">
        </template>
        <v-card>
          <v-card-title class="text-h5">
            Отлично!
          </v-card-title>
          <v-card-text>Вы успешно сохранили заявку. Сейчас вы будете перенаправлены на страницу реестра</v-card-text>
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
    </v-container>
    <v-speed-dial
        v-model="fab"
        class="mr-10 mb-10"
        transition="slide-y-reverse-transition"
        right
        bottom
        fixed
    >
      <template v-slot:activator>
        <v-btn
            v-model="fab"
            color="blue darken-2"
            dark
            fab
        >
          <v-icon v-if="fab">
            mdi-close
          </v-icon>
          <v-icon v-else>
            mdi-pencil
          </v-icon>
        </v-btn>
      </template>
      <v-btn
          fab
          dark
          color="indigo"
          @click="printAcceptanceCertificate"
      >
        <v-icon>mdi-printer-outline</v-icon>
      </v-btn>
      <v-btn
          fab
          dark
          color="red"
          @click="saveOrder"
      >
        <v-icon>mdi-content-save</v-icon>
      </v-btn>
    </v-speed-dial>
  </v-form>
</template>

<script>
import {mapActions, mapState} from 'vuex'

export default {
  name: 'CreateOrder',
  data: () =>({
    inputFieldRules: [
      v => !!v || 'Поле не может быть пустым',
    ],
    phoneRules: [
      v => !!v || 'Номер телефона не может быть пустым',
      v => /^[\d]{11}$/.test(v) || '7XXXXXXXXXX',
    ],
    order: {
      id: '',
      deviceName: '',
      deviceTypeId: '',
      deviceTypeName: '',
      statusId: '',
      statusName: '',
      serialNumber: '',
      defect: '',
      equipSet: '',
      appearance: '',
      warranty: '',
      performedActions: '',
      creationDate: '',
      issueDate: '',
      preliminaryPrice: '',
      servicePrice: '',
      componentPrice: '',
      marginPrice: '',
      totalPrice: '',
      engineerRate: '',
      directorProfit: '',
      headEngineerProfit: '',
      managerProfit: '',
      employeeProfit: '',
      customerName: '',
      customerPhone: '',
      posId: '',
      posShortName: '',
      authorId: '',
      authorName: '',
      engineerId: '',
      engineerName: '',
      engineerRoles: []

    },
    valid: false,
    certificateDialog: false,
    saveOrderDialog: false,
    selectedType: {
      id: '',
      name: '',
    },
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
    direction: 'top',
    fab: false,
  }),
  computed: {
    ...mapState(['deviceTypeList', 'posList', 'engineerList'])
  },
  beforeMount() {
    try {
      this.getDeviceTypeList()
      this.getPOSList()
      if (this.engineerList.length === 0) {
        this.getEngineerList()
      }
    } catch (er) {
      this.redirectToLogin()
    }
  },
  methods: {
    ...mapActions(['getDeviceTypeList', 'getPOSList', 'addRepairOrder', 'getEngineerList', 'generateAcceptanceCertificate']),
    validate() {
      this.valid = this.$refs.form.validate()
    },
    prepareOrder() {
      this.order.posId = this.selectedPOS.id
      this.order.posShortName = this.selectedPOS.shortName

      this.order.deviceTypeId = this.selectedType.id
      this.order.deviceTypeName = this.selectedType.name

      this.order.engineerId = this.selectedEngineer.id
      this.order.engineerName = this.selectedEngineer.firstName + " " +  this.selectedEngineer.lastName
      this.order.engineerRoles = this.selectedEngineer.roles

      //TODO: DONT FORGET TO REPLACE THIS LINE WITH VALID CODE
      this.order.authorId = 'ff80818181a5579d0181a55840580000'
      this.order.issueDate = '2000-01-01T01:01:00'
      this.order.creationDate = new Date().toISOString()
    },
    printAcceptanceCertificate() {
      this.validate()
      if(this.valid) {
        this.prepareOrder()
        this.generateAcceptanceCertificate(this.order)
        this.certificateDialog = true;
      }
    },
    saveOrder() {
      this.validate()
      try {
        if(this.valid) {
          this.prepareOrder()
          this.addRepairOrder(this.order)
          this.saveOrderDialog = true;
        }
      } catch (er) {
        this.redirectToLogin()
      }
    },
    closeCertificateDialog() {
      this.certificateDialog = false;
    },
    redirectToOrderList() {
      this.certificateDialog = false;
      this.saveOrderDialog = false;
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