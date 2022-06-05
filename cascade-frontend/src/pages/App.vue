<template>
<v-app>
    <v-app-bar
        elevation="9"
        outlined
        app
    >
      <v-toolbar-title>Title</v-toolbar-title>
      <v-btn text
      >
        Item 1
      </v-btn>
      <v-btn text
      >
        Item 2
      </v-btn>
      <v-btn text
      >
        Item 3
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
        <!-- <router-view></router-view> -->
      </v-container>
    </v-main>

  </v-app>
</template>

<script>
    export default {
    }
</script>

<style>

</style>