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
        <div class="row">
          <div v-if="canLeaveComment">
            <LeaveComment @clicked="onClick"/>
          </div>
        </div>
        <div class="row">
          <CommentsSection :comments="comments" :facility="facility" @deleted="reloadCommentSection" :administrator="administrator"/>
        </div>
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
import LeaveComment from "@/components/LeaveComment"
import { app } from "../main"

export default {
  name: "OpenedFacility",
  components: {
    LeaveComment,
    CommentsSection
  },
  data() {
    return {
      comments: [],
      facilityData: [],
      customerId: '',
      text: '',
      rating: '',
      canLeaveComment: false,
      administrator: false
    }
  },
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
      let res = await axios.get(app.config.globalProperties.$port.value + '/facility', {
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
    let res = await axios.get(this.$port.value + '/facility', {
      params: {
        id: this.$route.params.id
      }
    })
    this.facilityData = res.data


    let role = await axios.get(this.$port.value + '/userRole', {headers : this.createHeadersWithToken()})

    if (role.data === "CUSTOMER") {
      let res2 = await axios.get(this.$port.value + '/facility/' + this.facilityData.id + '/comments/add/allowed', {headers: this.createHeadersWithToken()})
      if (res2.data === true) this.canLeaveComment = true
      else this.canLeaveComment = false
    }

    if (role.data === "ADMINISTRATOR") {
      this.administrator = true
    }

    let res3 = await axios.post(this.$port.value + '/facility/' + this.facilityData.id + '/comments', role.data)

    this.comments = res3.data
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
    },
    async onClick(value) {
      let customerId = await this.getCustomerId()
      this.customerId = customerId.toString()
      this.text = value.text
      this.rating = value.rating.toString()
      let data = [this.customerId, this.text, this.rating]
      await axios.post(this.$port.value + '/facility/' + this.facilityData.id + '/comments/add', data)
    },
    async getCustomerId() {
      let res = await axios
          .get(this.$port.value + '/userId', {headers : this.createHeadersWithToken()})
      return res.data
    },
    reloadCommentSection(id) {
      this.comments = this.comments.filter(comment => comment.id !== id)
    }
  }
}
</script>

<style scoped>

</style>