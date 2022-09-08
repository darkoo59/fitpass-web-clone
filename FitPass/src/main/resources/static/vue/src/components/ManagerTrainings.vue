<template>
  <br>
  <div class="container" id="searchFilter">
    <div class="row">
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
        <option value="1">Training price: Low to High</option>
        <option value="2">Training price: High to Low</option>
        <option value="3">Application date: Ascending</option>
        <option value="4">Application date: Descending</option>
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
    <div class="col-md-6 p-4" v-for="trainingHistory in filteredManagerTrainingsHistory" :key="trainingHistory.id">
      <div class="card" style="width: 30rem;">
        <!--        <div v-bind:class="getCardClass()">-->
        <div class="card">
          <h5 class="card-title">{{getTrainingName(trainingHistory)}}</h5>
          <p class="card-text">
            <b>Sport facility : </b> {{getFacilityName(trainingHistory)}}<br>
            <b>Training type : </b> {{getTrainingType(trainingHistory)}}<br>
            <b>Application date and time : </b>{{getApplicationDateTime(trainingHistory)}}<br>

            <b>Price : </b>{{getTrainingPrice(trainingHistory)}}
          </p>
        </div>
      </div>
    </div>
  </div>
  <div class="item error" v-if="filter.searchInput && !filteredManagerTrainingsHistory.length">
    <p style="color:white;">No results found!</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ManagerTrainings",
  data: function () {
    return {
      managerTrainingsHistory: [],
      managerTrainings: [],
      filteredManagerTrainingsHistory: [],
      managerFacilities: [],
      date: null,
      trainingTypes: [],
      filter: {
        searchInput: "",
        trainingType: "Training type",
        price: "Price",
        sort: "Sort by",
        fromApplicationDate: null,
        toApplicationDate: null
      }
    }
  },
  async mounted() {
    let response1 = await axios.get(this.$port.value + '/managerTrainingsHistory', {headers: this.createHeadersWithToken()})
    this.managerTrainingsHistory = response1.data
    this.filteredManagerTrainingsHistory = response1.data
    let response2 = await axios.get(this.$port.value + '/managerTrainingsInfo', {headers: this.createHeadersWithToken()})
    this.managerTrainings = response2.data
    let response3 = await axios.get(this.$port.value + '/managerFacilities',{headers: this.createHeadersWithToken()})
    this.managerFacilities = response3.data
    this.trainingTypes = this.getTrainingTypes(this.managerTrainings)
  },
  methods: {
    async searchTrainings() {
      let resp = await axios
          .post(this.$port.value + '/managerTrainings/filter', this.filter, {headers : this.createHeadersWithToken()})
      this.filteredManagerTrainingsHistory = resp.data
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
    getApplicationDateTime(trainingHistory) {
      let date = trainingHistory.applicationDateTime.date
      let time = trainingHistory.applicationDateTime.time
      return date + ' at ' + time
    },
    createHeadersWithToken() {
      return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
    },
    getTrainingName(trainingHistory) {
      for (const training of this.managerTrainings) {
        if (training.id === trainingHistory.trainingId) {
          return training.name
        }
      }
    },
    getFacilityName(trainingHistory) {
      for (const training of this.managerTrainings) {
        if (trainingHistory.trainingId === training.id) {
          let facilityId = training.facilityId;
          for (let facility of this.managerFacilities) {
            if (facilityId === facility.id)
              return facility.name;
          }
        }
      }
      return ''
    },
    getTrainingType(trainingHistory) {
        for (const training of this.managerTrainings) {
          if (training.id === trainingHistory.trainingId) {
            return training.type
          }
        }
      },
    getTrainingPrice(trainingHistory) {
      for (const training of this.managerTrainings) {
        if (training.id === trainingHistory.trainingId) {
          return training.price + " RSD"
        }
      }
    },
    removeFilters() {
      this.filteredManagerTrainingsHistory = this.managerTrainingsHistory
      this.filter.searchInput = ""
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