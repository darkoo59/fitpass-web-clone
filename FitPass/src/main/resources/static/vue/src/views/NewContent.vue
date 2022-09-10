<template>
  <section class="vh-100 gradient-custom">
    <HomeHeader/>
    <Menu v-if="token != null"/>
    <AddNewContent />
  </section>
</template>

<script>
import AddNewContent from "@/components/AddNewContent";
import HomeHeader from "../components/HomeHeader";
import Menu from "../components/Menu"

export default {
  name: 'NewContent',
  components: {
    AddNewContent,
    HomeHeader,
    Menu
  },
  data: function(){
    return{
      token: window.localStorage.getItem('token'),
      userRole: window.localStorage.getItem('userRole')
    }
  },
  beforeMount() {
    if(localStorage.getItem('showMenu')){
      this.$router.go()
      localStorage.removeItem('showMenu')
    }
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