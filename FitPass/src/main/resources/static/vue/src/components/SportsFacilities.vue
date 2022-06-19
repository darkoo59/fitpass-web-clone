<template>
  <br>
  <div class="row">
    <div class="col-md-3" v-for="facility in facilities" :key="facility.name">
      <div class="card" style="width: 18rem;">
        <img v-bind:src="require('../../public/images/'+facility.logo)" class="card-img-top">
        <div class="card-body">
          <h5 class="card-title">{{facility.name}}</h5>
          <p class="card-text">
            Type: {{facility.type}} <br>
            Average rating: {{facility.averageRating}} <br>
            Working hours: {{facility.workHour.start.hour}}:{{facility.workHour.start.minute}} -
            {{facility.workHour.end.hour}}:{{facility.workHour.end.minute}}
          </p>
        </div>
      </div>
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