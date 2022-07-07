<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center" dense>
      <v-col cols="12" sm="8" md="4" lg="4">
        <v-card elevation="0">
          <div class="text-center">
            <h1 class="mb-2">Авторизация | CASCADE</h1>
          </div>
          <v-card-text>
            <v-form ref="form" v-model="valid" lazy-validation>
              <v-text-field
                  v-model="credentials.login"
                  label="79XXXXXXXXX"
                  :rules="phoneRules"
                  prepend-inner-icon="mdi-phone"
                  :counter="11"
                  class="rounded-0" outlined
                  required>
              </v-text-field>
              <v-text-field
                  v-model="credentials.password"
                  label="Пароль"
                  :rules="passwordRules"
                  prepend-inner-icon="mdi-lock"
                  type="password"
                  suffix="| Забыли?"
                  class="rounded-0" outlined>
              </v-text-field>
              <v-btn
                  class="rounded-0"
                  color="#000000"
                  @click="loginAttempt"
                  x-large block dark>Войти</v-btn>
            </v-form>
          </v-card-text>
          <v-card-actions class="ml-6 mr-6 text-center">
            <p>Войдя в систему Вы соглашаетесь с<a href="#" class="pl-2" style="color: #000000">Правовой политикой</a> и <a href="#" class="pl-2" style="color: #000000">Условиями пользования</a></p>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
    <v-dialog
        v-model="dialog"
        persistent
        max-width="400"
    >
      <template v-slot:activator="{ on, attrs }">
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Что-то пошло не так...
        </v-card-title>
        <v-card-text>Логин или пароль были некорректными. Попробуйте еще раз</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="green darken-1"
              text
              @click="closeDialog"
          >
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from 'axios'
import ConstConfig from 'util/ConstConfig'

export default {
  name: 'LoginPage',
  data: () =>({
    dialog: false,
    valid: false,
    phoneRules: [
      v => !!v || 'Номер телефона не может быть пустым',
      v => /^[\d]{11}$/.test(v) || '7XXXXXXXXXX',
    ],
    passwordRules: [
      v => !!v || 'Пароль не может быть пустым',
    ],
    credentials: {
      login: '',
      password: ''
    }
  }),
  methods: {
    loginAttempt() {
      this.valid = this.$refs.form.validate()
      if(this.valid) {
        axios.post(ConstConfig.url.signInUrl, this.credentials)
            .then(response => {
              this.$store.commit('saveToken', response.data.token)
              this.$router.push('/')
            }).catch(error => {
              this.dialog = true
        })
      }
    },

    closeDialog() {
      this.dialog = false
      this.$refs.form.reset()
      this.$refs.form.resetValidation()
    }
  }
}
</script>

<style>

</style>