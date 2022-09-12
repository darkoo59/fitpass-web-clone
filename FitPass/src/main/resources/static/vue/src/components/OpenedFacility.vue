<template>
  <div style="text-align: center">
    <img style="width: 10%; height: 10%" v-bind:src="'../'+img">
    <h4 class="text-white">{{facilityData.name}}</h4>
    <p class="text-white">Type : {{facilityData.type}}</p>
    <p class="text-white">Status : {{ this.getFacilityStatus() }} </p>
    <p class="text-white">Average rating : {{facilityData.averageRating}}</p>
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
      administrator: false,
      img: '',
      status: ''
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
  async beforeMount() {
    let res = await axios.get(this.$port.value + '/facility', {
      params: {
        id: this.$route.params.id
      }
    })
    this.facilityData = res.data
    this.getImageLocation()
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
    },
    getImageLocation() {
      this.img = require('../assets/images/' + this.facilityData.logo)
    },
    getFacilityStatus() {
      if (this.facilityData.status === '0')
        return 'WORKING'
      else
        return 'NOT WORKING'
    }
  }
}
</script>

<style scoped>

</style>