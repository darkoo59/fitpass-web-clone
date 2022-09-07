<template>
  <div class="card mb-3">
    <h1>MEMBERSHIP REVIEW</h1>
    <div class="card-body">
      <h5 class="card-title">{{ this.existingMembership.name }}</h5>
      <ul class="list-group list-group-flush">
        <li class="list-group-item">Type : {{ this.existingMembership.membershipType }}</li>
        <li class="list-group-item">Daily terms : {{ this.existingMembership.dailyTerms }}</li>
        <li class="list-group-item">Payment date : {{this.paymentDate}} </li>
        <li class="list-group-item">Valid until : {{this.validityDateTime}} </li>
        <li class="list-group-item">Price : {{ this.existingMembership.price }} RSD</li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import moment from "moment";

export default {
  name: "OpenedMembership",
  data: function(){
    return{
      token: window.localStorage.getItem('token'),
      paymentDate: '',
      validityDateTime: '',
      existingMembership: ''
    }
  },
  async mounted()
  {
    let resp = await axios.get('http://localhost:8081/activeMembership',{headers: this.createHeadersWithToken()})
    this.existingMembership = resp.data
    this.paymentDate = moment(new Date())
    if(this.existingMembership.membershipType === '15 dana')
      this.validityDateTime = moment(new Date()).add(15,'days')
    else if(this.existingMembership.membershipType === 'mesecna')
      this.validityDateTime = moment(new Date()).add(1,'M')
    else if(this.existingMembership.membershipType === 'godisnja')
      this.validityDateTime = moment(new Date()).add(1,'years')
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
    }
  }
}
</script>

<style scoped>

</style>