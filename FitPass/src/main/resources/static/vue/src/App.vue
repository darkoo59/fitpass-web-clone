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
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0px;
}
</style>
