<template>
  <div class="header gradient-custom">
    <div class="row">
      <div class="col-lg-5"></div>
      <div class="col-lg-5"></div>
      <div class="col-lg-2">
        <router-link to="/login">
          <input class="btn btn-primary" type="button" @click="goToHome()" value="Login">
        </router-link>
        <router-link to="/register">
          <button type="button" class="btn btn-secondary">Register</button>
        </router-link>
      </div>
      </div>
  </div>
</template>

<script>
export default {
  name: "HomeHeader",
  methods:{
    goToHome(){
      this.$router.push('/home');
    }
  }
}
</script>

<style scoped>
h2{
  margin:0;
}
.gradient-custom {
  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to bottom right, #000428, #2c3e50);

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to bottom right, #000428, #2c3e50);
}
</style>