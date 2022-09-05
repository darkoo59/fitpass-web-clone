<template>
  <br>
  <div class="row">
    <div class="col-md-3" v-for="trainingHistory in customerTrainingsHistory" :key="trainingHistory.id">
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
      customerTrainings: [],
      allFacilities: []
    }
  },
  async mounted() {
    let response1 = await axios.get('http://localhost:8081/myTrainings', {headers: this.createHeadersWithToken()})
    this.customerTrainingsHistory = response1.data
    let response2 = await axios.get('http://localhost:8081/myTrainingsInfo', {headers: this.createHeadersWithToken()})
    this.customerTrainings = response2.data
    let response3 = await axios.get('http://localhost:8081/sportsFacilities')
    this.allFacilities = response3.data
  },
  methods:
  {
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
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
    }
  }
}
</script>

<style scoped>

</style>