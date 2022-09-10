<template>
  <div class="row">
    <div class="col-md-3 p-2 w-25"  v-for="customer in customers" :key="customer.username">
      <div class="card" style="width: 18rem;">
        <div class="card-body">
          <h5 class="card-title">{{ customer.name }} {{customer.surname}}</h5>
          <p class="card-text">
            <b>Username:</b> {{customer.username}} <br>
            <b>Role:</b> Customer <br>
            <b>Birth day:</b> {{ customer.birthDate }} <br>
            <b>Customer type:</b> {{ customer.type.type }} <br>
            <b>Collected points:</b> {{ customer.collectedPoints }} <br>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ShowFacilityCustomers",
  data: function(){
    return{
      customers: [],
      token: window.localStorage.getItem('token'),
      userRole: window.localStorage.getItem('userRole')
    }
  },
  async mounted() {
    let resp = await axios.get(this.$port.value + '/facilityCustomers',{headers : this.createHeadersWithToken()})
    this.customers = resp.data
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