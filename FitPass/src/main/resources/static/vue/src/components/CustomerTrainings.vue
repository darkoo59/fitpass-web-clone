<template>
  <br>
  <div class="container" id="searchFilter">
    <div class="row">
      <div class="col-sm">
        <input type="text" v-model="filter.searchInput" placeholder="Search by facility name..." />
      </div>
      <select v-model="filter.facilityType" class="form-select col-sm">
        <option v-for="facility in facilityTypes" :key="facility" :value="facility">{{ facility }}</option>
      </select>
      <select v-model="filter.trainingType" class="form-select col-sm">
        <option v-for="type in trainingTypes" :key="type" :value="type">{{ type }}</option>
      </select>
      <select v-model="filter.price" class="form-select col-sm">
        <option selected>Price</option>
        <option value="1">Free</option>
        <option value="2">0 - 1000</option>
        <option value="3">1000 - 3000</option>
        <option value="4">3000 - 5000</option>
        <option value="5">>= 5000</option>
      </select>
      <div class="col-sm-4">
        <label class="text-white m-1">From : </label>
        <input v-model="filter.fromApplicationDate" type="date" class="w-auto" id="dateId1"/>
        <label class="text-white m-1">To : </label>
        <input v-model="filter.toApplicationDate" type="date" class="w-auto" id="dateId2"/>
      </div>
      <select v-model="filter.sort" class="form-select col-sm">
        <option selected>Sort by</option>
        <option value="1">Sport facility name: Ascending</option>
        <option value="2">Sport facility name: Descending</option>
        <option value="3">Training price: Low to High</option>
        <option value="4">Training price: High to Low</option>
        <option value="5">Application date: Ascending</option>
        <option value="6">Application date: Descending</option>
      </select>
    </div>
    <div class="col-sm-6">
      <button class="btn-primary" @click="searchTrainings()">
        Search
      </button>
    </div>
    <div class="col-sm">
      <button class="btn-secondary" @click="removeFilters()">
        Remove filters
      </button>
    </div>
  </div>
  <br>
  <br>


  <div class="row">
    <div class="col-md-3" v-for="trainingHistory in filteredCustomerTrainingsHistory" :key="trainingHistory.id">
      <div class="card" style="width: 18rem;">
        <div class="card-body">
          <h5 class="card-title">{{getTrainingName(trainingHistory)}}</h5>
          <p class="card-text">
            <b>Sport facility:</b> {{getFacilityName(trainingHistory)}}<br>
            <b>Trainings done on :</b>
            <ul>
              <li v-for="date in trainingHistory.trainingDates">
                  <p v-if="checkDate(date)">{{date}}</p>
              </li>
            </ul>
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
  name: "Trainings",
  data: function () {
    return {
      customerTrainingsHistory: [],
      filteredCustomerTrainingsHistory: [],
      customerTrainings: [],
      allFacilities: [],
      facilityTypes: [],
      date: null,
      trainingTypes: [],
      filter: {
        searchInput: "",
        facilityType: "Facility type",
        trainingType: "Training type",
        price: "Price",
        sort: "Sort by",
        fromApplicationDate: null,
        toApplicationDate: null
      }
    }
  },
  async mounted() {
    let response1 = await axios.get(this.$port.value + '/myTrainings', {headers: this.createHeadersWithToken()})
    this.customerTrainingsHistory = response1.data
    this.filteredCustomerTrainingsHistory = response1.data
    let response2 = await axios.get(this.$port.value + '/myTrainingsInfo', {headers: this.createHeadersWithToken()})
    this.customerTrainings = response2.data
    let response3 = await axios.get(this.$port.value + '/sportsFacilities')
    this.allFacilities = response3.data
    this.facilityTypes = this.getFacilityTypesFromAllFacilities(this.allFacilities)
    this.trainingTypes = this.getTrainingTypes(this.customerTrainings)
  },
  methods:
  {
    async searchTrainings() {
      let resp = await axios
          .post(this.$port.value + '/customerTrainings/filter', this.filter, {headers : this.createHeadersWithToken()})
      this.filteredCustomerTrainingsHistory = resp.data
    },
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
    },
    getTrainingTypes(allTrainings){
      let types = ["Training type"]
      for (const element of allTrainings) {
        if (!types.includes(element.type)) {
          types.push(element.type)
        }
      }
      return types
    },
    getFacilityTypesFromAllFacilities(allFacilities) {
      let facilities = ["Facility type"]
      for (const element of allFacilities) {
        if (!facilities.includes(element.type)) {
          facilities.push(element.type)
        }
      }
      return facilities
    },
    getTrainingName(trainingHistory){
      for (const training of this.customerTrainings){
          if(training.id === trainingHistory.trainingId){
            return training.name
            }
      }
    }
    ,
    getFacilityName(trainingHistory) {
      for (const training of this.customerTrainings) {
        if(trainingHistory.trainingId === training.id)
        {
          let facilityId = training.facilityId;
          for(let facility of this.allFacilities)
          {
            if(facilityId === facility.id)
              return facility.name;
          }
        }
      }
      return ''
    },
    checkDate(date) {
      let splitedDate = date.split('-')
      const dateString = splitedDate[2] + "-" + splitedDate[1] + "-" + splitedDate[0]
      let trainingDate = moment(dateString)
      let lastMonthDate = moment(new Date()).subtract(30,"days")
      if(lastMonthDate < trainingDate)
        return true
      else
        return false
    },
    removeFilters() {
      this.filteredCustomerTrainingsHistory = this.customerTrainingsHistory
      this.filter.searchInput = ""
      this.filter.facilityType = "Facility type"
      this.filter.trainingType = "Training type"
      this.filter.price = "Price"
      this.filter.sort = "Sort by"
      this.fromApplicationDate = null
      this.toApplicationDate = null
      let date1 = document.getElementById('dateId1')
      let date2 = document.getElementById('dateId2')
      date1.value = null
      date2.value = null
    }
  }
}
</script>

<style scoped>

</style>