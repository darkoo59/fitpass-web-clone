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
  <div ref="mapDiv" style="width: 50%; height: 50%"/>
</template>

<script>
import axios from "axios"
import { Loader } from '@googlemaps/js-api-loader'
import { onMounted, ref } from "vue";
import { useRoute } from 'vue-router'

export default {
  name: "OpenedFacility",
  setup() {
    const route = useRoute()
    let facility = []
    let center = {
      lat: 0.0,
      lng: 0.0
    }
    const loader = new Loader({ apiKey: 'AIzaSyBx8GVH-2qbiuswKuukDTH5bIbh9XZwSoI'})
    const mapDiv = ref(null)

    onMounted(async () => {
      let resp = await axios.get('http://localhost:8081/getSportFacility', {
        params: {
          id: route.params.id
        }
      })
      facility = resp.data

      center.lat = facility.location.latitude
      center.lng = facility.location.longitude

      await loader.load()
      let map = new google.maps.Map(mapDiv.value, {
        center: center,
        zoom: 18
      })

      new google.maps.Marker({
        position: center,
        map,
        title: facility.name,
      });
    })

    return { facility, center, mapDiv}
  }
}
</script>

<style scoped>

</style>