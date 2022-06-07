<template>
<v-app>
    <v-app-bar
        elevation="9"
        outlined
        app
    >
      <v-toolbar-title class="text-uppercase">
        <span class="font-weight-bold">Cascade</span>
      </v-toolbar-title>
      <v-btn text
      >
        Создание заявки
      </v-btn>
      <v-btn text
      >
        Реестр ремонта
      </v-btn>
      <v-btn text
      >
        Отчеты
      </v-btn>
      <v-btn text
             @click="showAdminPanel"
      >
        Управление
      </v-btn>
      <v-spacer></v-spacer>
      <v-btn
          text
      >
        Profile Name
      </v-btn>
      <v-btn icon href="/logout">
        <v-icon>mdi-exit-to-app</v-icon>
      </v-btn>
    </v-app-bar>
    <v-main>
      <v-container>
        <router-view></router-view>
      </v-container>
    </v-main>

  </v-app>
</template>

<script>
    export default {
      methods: {
        showAdminPanel() {
          this.$router.push('/adm/panel')
        }
      }
    }
</script>

<style>

</style>