<template>
  <section class="vh-100 gradient-custom">
    <HomeHeader />
    <SportsFacilities />
  </section>
</template>

<script>
import SportsFacilities from "../components/SportsFacilities";
import HomeHeader from "../components/HomeHeader";

export default {
  el: '#home',
  components: {
    SportsFacilities,
    HomeHeader
  }
}

</script>

<style scoped>
.gradient-custom {
  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to bottom right, #000428, #004e92);

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to bottom right, #000428, #004e92);
}
</style>