<template>
  <HomeHeader/>
  <router-view />
</template>

<script>
import HomeHeader from "./components/HomeHeader";


export default {
  el: '#home',
  components: {
    HomeHeader
  }
}
</script>

<style>

</style>
