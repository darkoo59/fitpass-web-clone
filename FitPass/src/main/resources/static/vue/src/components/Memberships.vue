<template>
  <br>
  <h2 class="text-white">Active membership : {{this.activeMembership.name}}</h2>
  <h2 class="text-white">Collected points : {{this.customer.collectedPoints}}</h2>
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
import moment from "moment";

export default {
  name: "Memberships",
  data: function () {
    return {
      existingMemberships: [],
      activeMembership: 'None',
      isMembershipValid: '',
      membership: '',
      customer: ''
    }
  },
  async mounted()
  {
    let response2 = await axios.get('http://localhost:8081/getCustomer',{headers: this.createHeadersWithToken()})
    this.customer = response2.data
    let resp = await axios.get('http://localhost:8081/existingMemberships')
    this.existingMemberships = resp.data
    let response = await axios.get(this.$port.value + '/activeMembership', {headers: this.createHeadersWithToken()})
    if(response.data == '')
      this.activeMembership = 'None'
    else {
      this.activeMembership = response.data
    }
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
    }
    ,
    checkValidity(membership)
    {
      alert(membership.name)
      return true
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