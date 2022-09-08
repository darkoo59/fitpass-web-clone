<link href="assets/plugins/global/plugins.bundle.css" rel="stylesheet" type="text/css"/>
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
    <div class="col-md-6 p-4" v-for="trainingHistory in filteredCoachTrainingsHistory" :key="trainingHistory.id">
      <div class="card" style="width: 30rem;">
<!--        <div v-bind:class="getCardClass()">-->
        <div class="card">
          <h5 class="card-title">{{getTrainingName(trainingHistory)}}</h5>
          <p class="card-text">
            <b>Sport facility : </b> {{getFacilityName(trainingHistory)}}<br>
            <b>Training type : </b> {{getTrainingType(trainingHistory)}}<br>
            <b>Application date and time : </b>{{getApplicationDateTime(trainingHistory)}}<br>
            <b>Next training on : </b> {{trainingHistory.nextTrainingDate}}<br>

            <b>Price : </b>{{getTrainingPrice(trainingHistory)}}
          </p>
          <p v-if="getTrainingType(trainingHistory) === 'personalni'">
            <button type="button" v-bind:class="createButtonClassName(trainingHistory)"  @click="cancelPersonalTraining(trainingHistory)">Cancel</button>
          </p>
        </div>
      </div>
    </div>
  </div>
  <div class="item error" v-if="filter.searchInput && !filteredCoachTrainingsHistory.length">
    <p style="color:white;">No results found!</p>
  </div>
</template>

<script>
import axios from "axios";
import moment from "moment"


export default {
  name: "CoachTrainings",
  data: function () {
    return {
      coachTrainingsHistory: [],
      coachTrainings: [],
      filteredCoachTrainingsHistory: [],
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
    let response1 = await axios.get(this.$port.value + '/coachTrainingsHistory', {headers: this.createHeadersWithToken()})
    this.coachTrainingsHistory = response1.data
    this.filteredCoachTrainingsHistory = response1.data
    let response2 = await axios.get(this.$port.value + '/coachTrainingsInfo', {headers: this.createHeadersWithToken()})
    this.coachTrainings = response2.data
    let response3 = await axios.get(this.$port.value + '/sportsFacilities')
    this.allFacilities = response3.data
    this.facilityTypes = this.getFacilityTypesFromAllFacilities(this.allFacilities)
    this.trainingTypes = this.getTrainingTypes(this.coachTrainings)
  },
  methods:
      {
        async searchTrainings() {
          let resp = await axios
              .post(this.$port.value + '/coachTrainings/filter', this.filter, {headers : this.createHeadersWithToken()})
          this.filteredCoachTrainingsHistory = resp.data
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
        }
        ,
        createHeadersWithToken() {
          return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
        },
        createButtonClassName(trainingHistory) {
          let splitedDate = trainingHistory.nextTrainingDate.split('-')
          const dateString = splitedDate[2] + "-" + splitedDate[1] + "-" + splitedDate[0]
          let trainingDate = moment(dateString)
          let twoDaysAgoDate = moment(new Date()).add(2, 'days');
          if (twoDaysAgoDate < trainingDate)
            return 'btn btn-danger'
          else
            return 'btn btn-danger disabled'
        }
        ,
        getTrainingName(trainingHistory) {
          for (const training of this.coachTrainings) {
            if (training.id === trainingHistory.trainingId) {
              return training.name
            }
          }
        }
        ,
        getFacilityName(trainingHistory) {
          for (const training of this.coachTrainings) {
            if (trainingHistory.trainingId === training.id) {
              let facilityId = training.facilityId;
              for (let facility of this.allFacilities) {
                if (facilityId === facility.id)
                  return facility.name;
              }
            }
          }
          return ''
        },
        getTrainingType(trainingHistory) {
          for (const training of this.coachTrainings) {
            if (training.id === trainingHistory.trainingId) {
              return training.type
            }
          }
        },
        getApplicationDateTime(trainingHistory) {
          let date = trainingHistory.applicationDateTime.date
          let time = trainingHistory.applicationDateTime.time
          return date + ' at ' + time
        },
        getTrainingPrice(trainingHistory) {
          for (const training of this.coachTrainings) {
            if (training.id === trainingHistory.trainingId) {
              return training.price + " RSD"
            }
          }
        },
        cancelPersonalTraining(trainingHistory) {
          axios.get(this.$port.value + '/cancelPersonalTraining', {
            params: {id: trainingHistory.trainingId}
          }).then(this.$router.go());
        },
        removeFilters() {
          this.filteredCoachTrainingsHistory = this.coachTrainingsHistory
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