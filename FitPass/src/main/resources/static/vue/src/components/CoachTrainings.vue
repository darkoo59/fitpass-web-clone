<template>
  <br>
  <div class="container" id="searchFilter">
    <div class="row">
      <div class="col-sm">
        <input type="text" v-model="filter.searchInput" placeholder="Search by name..." />
      </div>
      <select v-model="filter.facilityType" class="form-select col-sm">
        <option v-for="facility in facilityTypes" :key="facility" :value="facility">{{ facility }}</option>
      </select>
      <select v-model="filter.trainingType" class="form-select col-sm">
        <option v-for="type in trainingTypes" :key="type" :value="type">{{ type }}</option>
      </select>
      <div class="col-sm">
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
  </div>
  <br>
  <br>



  <div class="row">
    <div class="col-md-6 p-4" v-for="trainingHistory in coachTrainingsHistory" :key="trainingHistory.id">
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
      trainingTypes: [],
      filter: {
        searchInput: "",
        facilityType: "Facility type",
        trainingType: "Training type"
      }
    }
  },
  async mounted() {
    let response1 = await axios.get('http://localhost:8081/coachTrainingsHistory', {headers: this.createHeadersWithToken()})
    this.coachTrainingsHistory = response1.data
    let response2 = await axios.get('http://localhost:8081/coachTrainingsInfo', {headers: this.createHeadersWithToken()})
    this.coachTrainings = response2.data
    let response3 = await axios.get('http://localhost:8081/sportsFacilities')
    this.allFacilities = response3.data
    this.facilityTypes = this.getFacilityTypesFromAllFacilities(this.allFacilities)
    this.trainingTypes = this.getTrainingTypes(this.coachTrainings)
  },
  methods:
      {
        async searchTrainings() {
          let resp = await axios
              .post('http://localhost:8081/coachTrainings/filter', this.filter, {headers : this.createHeadersWithToken()})
          this.coachTrainingsHistory = resp.data
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
          axios.get('http://localhost:8081/cancelPersonalTraining', {
            params: {id: trainingHistory.trainingId}
          }).then(this.$router.go());
        }
      }
}
</script>

<style scoped>

</style>