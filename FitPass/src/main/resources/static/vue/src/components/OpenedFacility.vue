<template>
  <div class="card mb-3">
    <h1>TODO : LOGO IDE OVDE</h1>
    <div class="card-body">
      <h5 class="card-title">{{facility.name}}</h5>
      <ul class="list-group list-group-flush">
        <li class="list-group-item">Type : {{facility.type}}</li>
        <li class="list-group-item">Status : {{facility.status}}</li>
        <li class="list-group-item">Location : {{facility.location}}</li>
        <li class="list-group-item">Average rating : {{facility.averageRating}}</li>
        <li class="list-group-item">TODO : RASPORED TRENINGA KOJE SPORTSKI OBJEKAT NUDI</li>
      </ul>
    </div>
  </div>
  <div class="container h-50">
    <div class="row h-100">
      <div class="col">
        <div ref="mapDiv" style="width: 100%; height: 100%"/>
      </div>
      <div class="col">
        <CommentsSection :comments="comments" :facility="facility"/>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios"
import { Loader } from '@googlemaps/js-api-loader'
import { onMounted, ref } from "vue";
import { useRoute } from 'vue-router'
import CommentsSection from "./CommentsSection"

export default {
  name: "OpenedFacility",
  components: {
    CommentsSection
  },
  data() {
    return {
      port: 'http://localhost:8081',
      comments: [],
      facilityData: []
    }
  },
  setup() {
    const port = 'http://localhost:8081'
    const route = useRoute()
    let facility = []
    let center = {
      lat: 0.0,
      lng: 0.0
    }
    const loader = new Loader({ apiKey: 'AIzaSyBx8GVH-2qbiuswKuukDTH5bIbh9XZwSoI'})
    const mapDiv = ref(null)

    onMounted(async () => {
      let res = await axios.get(port + '/facility', {
        params: {
          id: route.params.id
        }
      })
      facility = res.data

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

    return { facility, center, mapDiv }
  },
  async mounted() {
    let res = await axios.get(this.port + '/facility', {
      params: {
        id: this.$route.params.id
      }
    })
    this.facilityData = res.data

    let role = await axios.get(this.port + '/userRole', {headers : {"Authorization" : `Bearer ${localStorage.getItem('token')}`}})

    let res2 = await axios.post(this.port + '/facility/' + this.facilityData.id + '/comments', role.data)

    this.comments = res2.data
  }
}
</script>

<style scoped>

</style>