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
  <GoogleMap api-key="AIzaSyBx8GVH-2qbiuswKuukDTH5bIbh9XZwSoI"
             style="width: 70%; height: 500px" :center="this.center" :zoom="18">
    <Marker :options="this.marker"/>
  </GoogleMap>
</template>

<script>
import axios from "axios"
import { GoogleMap, Marker } from 'vue3-google-map'

export default {
  components: {
    GoogleMap,
    Marker
  },
  name: "OpenedFacility",
  setup() {
    let facility = ''
    let center = {
      lat: 0.0,
      lng: 0.0
    }
    let marker = {
      position: {
        lat: 0.0,
        lng: 0.0
      },
      label: "",
      title: ""
    }
    return { facility, center, marker }
  },
  async mounted()
  {
    let resp = await axios.get('http://localhost:8081/getSportFacility', {
      params: {
        id: this.$route.path.substring(15)
      }
    })
    this.facility = resp.data
    this.mapsSetup()
  },
  methods: {
    mapsSetup() {
      this.center.lat = this.facility.location.latitude
      this.center.lng = this.facility.location.longitude
      this.marker.position.lat = this.center.lat
      this.marker.position.lng = this.center.lng
      this.marker.label = this.facility.type
      this.marker.title = this.facility.name
    }
  }
}
</script>

<style scoped>

</style>