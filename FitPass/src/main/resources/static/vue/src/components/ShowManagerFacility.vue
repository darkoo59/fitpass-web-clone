<template>
  <div v-if="isLoaded">
  <div class="card mb-3" style="width: 30rem; margin:auto; padding-top: 5rem;">
    <div class="card-body">
      <img v-bind:src="img" class="card-img-top">
      <h5 class="card-title">{{facility.name}}</h5>
      <ul class="list-group list-group-flush">
        <li class="list-group-item">Type : {{facility.type}}</li>
        <li class="list-group-item">Status : {{this.getStatusByEnum(facility.status)}}</li>
        <li class="list-group-item">Location :
          <ul><li>Longitude : {{facility.location.latitude}}</li>
            <li>Latitude : {{facility.location.latitude}}</li>
            <li>City : {{facility.location.address.place}}</li>
            <li>Street : {{facility.location.address.street}}</li>
            <li>Street number : {{facility.location.address.number}}</li>
            <li>Zip code : {{facility.location.address.zipCode}}</li>

          </ul>
        </li>
        <li class="list-group-item">Average rating : {{facility.averageRating}}</li>
      </ul>
    </div>
  </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ShowManagerFacility",
  data(){
    return {
      facility: [],
      img: '',
      isLoaded: false
    }
  },
  async beforeMount() {
    let res = await axios.get(this.$port.value + '/managerFacility',{headers : this.createHeadersWithToken()}).then(
        (response) => {
          this.isLoaded = true;
          this.facility = response.data;
          this.getImageLocation()
        }
    )
  }
  ,
  methods: {
    createHeadersWithToken() {
      return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
    },
    getImageLocation() {
      this.img = require('../assets/images/' + this.facility.logo)
    },
    getStatusByEnum(status){
      if(status === '0')
        return 'Working'
      else
        return 'Not working'
    },
    getLocation() {
      console.log(this.facility)
      // return this.facility.location.latitude
    }
}
}
</script>

<style scoped>
cardId {
  width: 50px;
  margin: auto;
}
</style>