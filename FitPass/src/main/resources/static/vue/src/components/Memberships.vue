<template>
  <br>
  <h1 class="text-white">Active membership : {{this.activeMembership.name}}</h1>
  <div class="row">
    <div class="col-md-3 p-2 w-25"  v-for="membership in existingMemberships" :key="membership.id">
      <div class="card" style="width: 18rem;" @click="openMembership(membership)">
        <div class="card-body">
          <h5 class="card-title">{{ membership.name }}</h5>
          <p class="card-text">
            <b>Id:</b> {{membership.id}} <br>
            <b>Type:</b> {{ membership.membershipType }} <br>
            <b>Daily terms:</b> {{ membership.dailyTerms }} <br>
              <b>Price:</b> {{membership.price}} RSD
          </p>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import axios from "axios";

export default {
  name: "Memberships",
  data: function () {
    return {
      existingMemberships: [],
      activeMembership: 'None'
    }
  },
  async mounted()
  {
    let resp = await axios.get(this.$port.value + '/existingMemberships')
    this.existingMemberships = resp.data
    let response = await axios.get(this.$port.value + '/activeMembership', {headers: this.createHeadersWithToken()})
    if(response.data == '')
      this.activeMembership = 'None'
    else
      this.activeMembership = response.data
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
    },
    openMembership(membership) {
      this.$router.push({
        name: 'SelectedMembership',
        params: {id: membership.id}
      })
    }
  }
}
</script>

<style scoped>

</style>