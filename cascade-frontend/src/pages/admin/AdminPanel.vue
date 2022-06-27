<template>
  <v-container>
    <v-flex class="mt-4 mb-4">
      <h3>Управление рабочим окружением</h3>
    </v-flex>
    <v-row>
      <v-col
          :cols="6"
          sm="4"
      >
        <v-card
            class="mx-auto"
        >
          <v-card-text>
            <p class="text-h5 text--primary">
              Пользователи системы
            </p>
            <div class="text--primary">
              Список всех пользователей дилера
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col
          :cols="6"
          sm="4"
      >
        <v-card
            class="mx-auto"
            @click="showRolePage"
        >
          <v-card-text>
            <p class="text-h5 text--primary">
              Роли
            </p>
            <div class="text--primary">
              Список возможных ролей для пользователей системы
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col
          :cols="6"
          sm="4"
      >
        <v-card
            class="mx-auto"
            @click="showPOSPage"
        >
          <v-card-text>
            <p class="text-h5 text--primary">
              Торговые точки
            </p>
            <div class="text--primary">
              Список доступных торговых точек дилера, как активных так и закрытых
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col
          :cols="6"
          sm="4"
      >
        <v-card
            class="mx-auto"
            @click="showPOSTypePage"
        >
          <v-card-text>
            <p class="text-h5 text--primary">
              Типы торговых точек
            </p>
            <div class="text--primary">
              Список типов торговых точек - франшизы (Мегафон, Т2, МТС и т.д.)
            </div>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col
          :cols="6"
          sm="4"
      >
        <v-card
            class="mx-auto"
            @click="showOrganizationPage"
        >
          <v-card-text>
            <p class="text-h5 text--primary">
              Организации
            </p>
            <div class="text--primary">
              Все используемые организации: ООО, ПАО, ЗАО, ИП, самозанятые
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-flex class="mt-4 mb-4">
      <h3>Управление номенклатурой ремонта</h3>
    </v-flex>
    <v-row>
      <v-col
          :cols="6"
          sm="4"
      >
        <v-card
            class="mx-auto"
            @click="showRepairDeviceTypePage"
        >
          <v-card-text>
            <p class="text-h5 text--primary">
              Типы ремонтируемых устройств
            </p>
            <div class="text--primary">
              Список всех типов устройств, которые принимаются в ремонт
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col
          :cols="6"
          sm="4"
      >
        <v-card
            class="mx-auto"
            @click="showRepairStatusPage"
        >
          <v-card-text>
            <p class="text-h5 text--primary">
              Статусы ремонта
            </p>
            <div class="text--primary">
              Список доступных статусов ремонта
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'AdminPanel',
  methods: {
    showRepairDeviceTypePage() {
      this.$router.push('/adm/repair/device-type')
    },
    showRepairStatusPage() {
      this.$router.push('/adm/repair/status')
    },
    showRolePage() {
      this.$router.push('/adm/manage/employee-role')
    },
    showOrganizationPage() {
      this.$router.push('/adm/manage/organization')
    },
    showPOSTypePage() {
      this.$router.push('/adm/manage/pos-type')
    },
    showPOSPage() {
      this.$router.push('/adm/manage/pos')
    }
  }
}
</script>

<style>

</style>