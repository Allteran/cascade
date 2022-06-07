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
  }
}
</script>

<style>

</style>