<template>
  <br>
  <br>
  <div class="row">
    <div class="col-md-3"  v-for="facility in filtered" :key="facility.name">
      <div class="card" style="width: 18rem;">
        <img v-bind:src="require('@/assets/images/'+facility.logo)" class="card-img-top">
        <div class="card-body">
          <h5 class="card-title">{{ facility.name }}</h5>
          <p class="card-text">
            Type: {{ facility.type }} <br>
            Average rating: {{ facility.averageRating }} <br>
            Working hours: {{ facility.workHour.start.substring(0, facility.workHour.start.length-3) }} -
            {{ facility.workHour.end.substring(0, facility.workHour.end.length-3) }} <br>
            Location: {{ facility.location.address.street}} {{ facility.location.address.number}}, {{facility.location.address.place }}
          </p>
          <label>Select training : </label>
          <select class="form-select col-sm" v-model="selectedTraining">
            <option v-for="training in getTrainingsForFacility(facility.id)" :value="training" :key="training.name">{{training.name}}-{{training.type}}</option>
          </select>
        </div>
        <input class="btn btn-primary" type="button" @click="facilityClick(facility)" value="Select">
    </div>
  </div>
  </div>
</template>

<script>
import axios from "axios";
import moment from "moment";

export default {
  name: "CustomerTrain",
  data: function () {
    return {
      facilities: [],
      filtered: [],
      cities: [],
      types: [],
      trainings: [],
      selectedTraining: '',
      activeMembership: '',
      todayTermsNum: 0,
      trainingHistory: {
        trainingDateTime: '',
        trainingId: '',
        customerId: '',
        coachId: '',
      }
    }
  },
  async mounted()
  {
    let resp = await axios.get(this.$port.value + '/sportsFacilities')
    this.facilities = resp.data
    this.filtered = resp.data
    let resp2 = await axios.get(this.$port.value + '/myTrainingsInfo')
    this.trainings = resp2.data
    let resp3 = await axios.get(this.$port.value + '/activeMembership', {headers: this.createHeadersWithToken()})
    if(resp3.data == '')
      this.activeMembership = 'None'
    else
      this.activeMembership = resp3.data
    let resp4 = await axios.get(this.$port.value + '/getTermsNum', {headers: this.createHeadersWithToken()})
    this.todayTermsNum = resp4.data
    this.cities = this.getCitiesFromResponse(resp.data)
    this.types = this.getTypesFromResponse(resp.data)
  },
  getCitiesFromResponse(data) {
    let cities = ["Location"]
    for (const element of data) {
      if (cities.indexOf(element.location.address.place) === -1) {
        cities.push(element.location.address.place)
      }
    }
    return cities
  },
  getTypesFromResponse(data) {
    let types = ["Type"]
    for (const element of data) {
      if (types.indexOf(element.type) === -1) {
        types.push(element.type)
      }
    }
    return types
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
    },
    facilityClick() {
      if(this.selectedTraining.name === undefined)
        alert('Please select training first')
      else if(this.activeMembership === 'None')
        alert('Please purchase membership first')
      else if(!this.isTermsNumberValid())
        alert('You dont have enough terms today')
      else {
        alert('You are ready to go')
        //TODO : umanjiti broj preostalih termina(diskutabilno) i dodati u istoriju treninga vezanu za jednog kupca
        axios.get(this.$port.value + '/addTrainingHistory',{headers: this.createHeadersWithToken(),
          params: {
            applicationDateTime: moment().format('yyyy-MM-DD hh:mm'),
            trainingId: this.selectedTraining.id
          }})
        alert('Training successfully added to training history')
        this.$router.push('/trainNow')
      }
    },
    isTermsNumberValid()
    {
      if(this.todayTermsNum === 'neograniceno')
        return true
      else if(parseInt(this.todayTermsNum) > 0)
        return true
      else
        return false
    }
    ,
    getTrainingsForFacility(facilityId) {
      let trainingsToReturn = []
      for (let training of this.trainings) {
        if (training.facilityId === facilityId)
          trainingsToReturn.push(training)
      }
      return trainingsToReturn
    }
  }
}
</script>

<style scoped>

</style>