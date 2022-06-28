<template>
  <v-data-table
      :headers="headers"
      :items="orgList"
      :items-per-page="15"
      class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
          flat
      >
        <v-toolbar-title>Организации</v-toolbar-title>
        <v-divider
            class="mx-4"
            inset
            vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog
            v-model="dialog"
            max-width="500px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                color="primary"
                dark
                class="mb-2"
                v-bind="attrs"
                v-on="on"
            >
              Создать
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                      cols="12"
                      sm="12"
                      md="12"
                  >
                    <v-text-field
                        v-model="editedItem.name"
                        label="Наименование"
                        :rules="nameRules"
                    ></v-text-field>
                  </v-col>
                  <v-col
                      cols="12"
                      sm="12"
                      md="12"
                  >
                    <v-text-field
                        v-model="editedItem.inn"
                        label="ИНН организации "
                        :rules="nameRules"
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                  color="blue darken-1"
                  text
                  @click="close"
              >
                Отмена
              </v-btn>
              <v-btn
                  color="blue darken-1"
                  text
                  @click="save"
              >
                Сохранить
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon
          small
          class="mr-2"
          @click="editItem(item)"
      >
        mdi-pencil
      </v-icon>
    </template>
  </v-data-table>
</template>

<script>
import {mapActions, mapState} from 'vuex'

export default {
  name: 'Organization',
  data: () => ({
    headers: [
      {text: 'ID', align: 'start', sortable: false, value: 'id'},
      {text: 'Название', value: 'name'},
      {text: 'ИНН', value: 'inn'},
      {text: 'Действия', value: 'actions', sortable: false},
    ],
    nameRules: [
      v => !!v || 'Поле не может быть пустым',
    ],
    dialog: false,
    editedIndex: -1,
    editedItem: {
      name: '',
      id: '',
      inn: ''
    },
    defaultItem: {
      name: '',
      id: '',
      inn: ''
    },
  }),
  computed: {
    ...mapState(['orgList']),
    formTitle() {
      return this.editedIndex === -1 ? 'Создание' : 'Редактирование'
    },
  },
  beforeMount() {
    if(this.orgList.length === 0) {
      this.getOrganizationList()
    }
  },
  watch: {
    dialog(val) {
      val || this.close()
    },
  },
  methods: {
    ...mapActions(['getOrganizationList', 'addOrganization', 'updateOrganization']),
    editItem(item) {
      this.editedIndex = this.roleList.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },
    save() {
      if (this.editedItem.id) {
        this.updateOrganization(this.editedItem)
      } else {
        this.addOrganization(this.editedItem)
      }
      this.close()
      this.getOrganizationList()
    },
  }
}
</script>

<style>

</style>