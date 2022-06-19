<template>
  <div class="card" v-for="facility in facilities" style="width: 18rem;">
<!--    <img src={{f.logo}} class="card-img-top">-->
    <div class="card-body">
      <h5 class="card-title">{{facility.name}}</h5>
      <p class="card-text">{{facility.type}}</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "SportsFacilities",
  data: function () {
    return {
      facilities: null
    }
    },
        mounted()
    {
      axios
          .get('http://localhost:8081/sportsFacilities')
          .then(response => (this.facilities = response.data))
    }
}
</script>

<style scoped>
</style>