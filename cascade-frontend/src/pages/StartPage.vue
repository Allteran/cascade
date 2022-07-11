<template>
  <v-app>
    <v-flex h3>
      "START PAGE"
    </v-flex>
  </v-app>
</template>

<script>
export default {
  name: 'StartPage',
  beforeMount() {
    if(this.$store.state.token === null || this.$store.state.token.length <1) {
      this.$store.commit('clearState')
      this.$router.push('/login')
    }
  }
}
</script>

<style>

</style>