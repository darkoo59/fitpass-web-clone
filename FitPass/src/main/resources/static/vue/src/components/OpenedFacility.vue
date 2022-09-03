<template>
  <div class="card mb-3">
    <h1>TODO : LOGO IDE OVDE</h1>
    <div class="card-body">
      <h5 class="card-title">{{facility.name}}</h5>
      <ul class="list-group list-group-flush">
        <li class="list-group-item">Type : {{facility.type}}</li>
        <li class="list-group-item">Status : {{facility.status}}</li>
        <li class="list-group-item">Location (TODO : MAPA) : {{facility.location}}</li>
        <li class="list-group-item">Average rating : {{facility.averageRating}}</li>
        <li class="list-group-item">TODO : KOMENTARI</li>
        <li class="list-group-item">TODO : RASPORED TRENINGA KOJE SPORTSKI OBJEKAT NUDI</li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "OpenedFacility",
  data: function () {
    return {
      facility: "",
    }
  },
  async mounted()
  {
    let resp = await axios.get('http://localhost:8081/getSportFacility', {
      params: {
        id: this.$route.path.substring(15)
      }
    })
    this.facility = resp.data
    alert("../../public/images/"+this.facility.logo)
  }
}
</script>

<style scoped>

</style>