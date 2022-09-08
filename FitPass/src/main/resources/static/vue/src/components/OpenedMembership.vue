<template>
  <div class="card mb-3">
    <h1>MEMBERSHIP REVIEW</h1>
    <div class="card-body">
      <h5 class="card-title">{{ this.existingMembership.name }}</h5>
      <ul class="list-group list-group-flush">
        <li class="list-group-item">Type : {{ this.existingMembership.membershipType }}</li>
        <li class="list-group-item">Daily terms : {{ this.existingMembership.dailyTerms }}</li>
        <li class="list-group-item">Payment date : {{this.membershipData.paymentDate}} </li>
        <li class="list-group-item">Valid until : {{this.membershipData.validityDateTime}} </li>
        <li class="list-group-item">Price : {{ this.existingMembership.price }} RSD</li>
        <li class="list-group-item"><input type="text" name="promoCode" class="form-control-color w-25 "
               v-model="this.promoCode" placeholder="Enter promo code"></li>
        <li class="list-group-item"><input class="btn btn-primary" type="button" @click="confirmMembership()" value="Submit"></li>
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
      membershipData: {
        membershipId : '',
        paymentDate: '',
        validityDateTime:'',
        priceDiscountPercentage: 0.0
      },
      existingMembership: '',
      promoCodes: '',
      promoCode: '',
      selectedPromoCode: ''
    }
  },
  async mounted()
  {
    let resp = await axios.get(this.$port.value + '/membershipById',{headers: this.createHeadersWithToken(),
      params: {
        id: this.$route.path.substring(12)
      }})
    this.existingMembership = resp.data
    this.membershipData.paymentDate = moment(new Date())
    if(this.existingMembership.membershipType === '15 dana')
      this.membershipData.validityDateTime = moment(new Date()).add(15,'days')
    else if(this.existingMembership.membershipType === 'mesecna')
      this.membershipData.validityDateTime = moment(new Date()).add(1,'M')
    else if(this.existingMembership.membershipType === 'godisnja')
      this.membershipData.validityDateTime = moment(new Date()).add(1,'years')
    let resp2 = await axios.get(this.$port.value + '/promoCodes')
    this.promoCodes = resp2.data
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
    },
    confirmMembership() {
      if(this.promoCode == '')
        this.membershipActivation()
      else
      {
        for(const code of this.promoCodes){
          if(this.promoCode === code.code){
              this.existingMembership.price = this.existingMembership.price - ((code.discountPercentage/100)*
                  this.existingMembership.price)
              alert('Price with discount is ' + this.existingMembership.price)
              this.membershipData.priceDiscountPercentage = code.discountPercentage
              this.selectedPromoCode = code
              this.membershipActivation()
              axios
                  .get(this.$port.value + '/decrementPromoCode',{params: {
              id: code.id
            }})
          }
        }
      }
    },
    membershipActivation()
    {
      this.membershipData.membershipId = this.existingMembership.id
      axios
          .post(this.$port.value + '/createMembership',this.membershipData,{headers: this.createHeadersWithToken()})
      this.$router.push('/membership')
    }
  }
}
</script>

<style scoped>

</style>