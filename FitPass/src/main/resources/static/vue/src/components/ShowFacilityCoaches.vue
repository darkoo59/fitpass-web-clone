<template>
  <div class="row">
    <div class="col-md-3 p-2 w-25"  v-for="coach in coaches" :key="coach.username">
      <div class="card" style="width: 18rem;">
        <div class="card-body">
          <h5 class="card-title">{{ coach.name }} {{coach.surname}}</h5>
          <p class="card-text">
            <b>Username:</b> {{coach.username}} <br>
            <b>Role:</b> Coach <br>
            <b>Birth day:</b> {{ coach.birthDate }} <br>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ShowFacilityCoaches",
  data: function(){
    return{
      coaches: [],
      token: window.localStorage.getItem('token'),
      userRole: window.localStorage.getItem('userRole')
    }
  },
  async mounted() {
    let resp = await axios.get(this.$port.value + '/facilityCoaches',{headers : this.createHeadersWithToken()})
    this.coaches = resp.data
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
    },
  }
}
</script>

<style scoped>

</style>