<template>
  <v-app>
    <v-app-bar
        elevation="9"
        outlined
        app
        v-if="isLoggedIn"
    >
      <v-toolbar-title class="text-uppercase">
        <span class="font-weight-bold">Cascade</span>
      </v-toolbar-title>
      <v-btn text
             @click="showOrderList"
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
      <v-btn icon
             @click="logout">
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
  data: () => ({
    loggedIn:false,
  }),
  computed: {
    isLoggedIn: function () {
      return this.$store.state.token != null
    }
  },
  beforeMount() {
    if(this.$store.state.token === null || this.$store.state.token.length < 1) {
      this.$router.push('/login')
    }
  },
  methods: {
    showAdminPanel() {
      this.$router.push('/adm/panel')
    },
    showOrderList() {
      this.$router.push('/workshop/order')
    },
    logout(){
      console.log('token before logout', this.$store.state.token)
      this.$router.push('/login')
      console.log('token after logout', this.$store.state.token)
    }
  }
}
</script>

<style>

</style>