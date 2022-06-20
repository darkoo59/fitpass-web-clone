<template>
  <br>
  <input type="text" @input="searchFacilities" v-model="input" placeholder="Search facilities..." />
  <br>
  <br>
  <div class="row">
    <div class="col-md-3"  v-for="facility in filtered" :key="facility.name">
      <div class="card" style="width: 18rem;">
        <img v-bind:src="require('../../public/images/'+facility.logo)" class="card-img-top">
        <div class="card-body">
          <h5 class="card-title">{{facility.name}}</h5>
          <p class="card-text">
            Type: {{facility.type}} <br>
            Average rating: {{facility.averageRating}} <br>
            Working hours: {{facility.workHour.start.substring(0, facility.workHour.start.length-3)}} -
            {{facility.workHour.end.substring(0, facility.workHour.end.length-3)}} <br>
            Location: {{facility.location.address.streetAndNumber}}, {{facility.location.address.place}}
          </p>
        </div>
      </div>
    </div>
  </div>
  <div class="item error" v-if="input&& !facilities.length">
    <p>No results found!</p>
  </div>

</template>

<script>
import axios from "axios";


export default {
  name: "SportsFacilities",
  data: function () {
    return {
      facilities: [],
      filtered: [],
      input: ""
    }
    },
   async mounted()
    {
      let resp = await axios.get('http://localhost:8081/sportsFacilities')
      this.facilities = resp.data
      this.filtered = resp.data
    },

  methods: {
    searchFacilities() {
      this.filtered = this.facilities.filter((facility) => {
        return facility.name.toLowerCase().includes(this.input.toLowerCase());
      })
    }
  }


}

</script>

<style scoped>
</style>