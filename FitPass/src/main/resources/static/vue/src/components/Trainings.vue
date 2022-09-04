<template>
  <h1 class="text-white">{{ trainingsHistory.length }}</h1>

  <h1 class="text-white">{{ trainings.length }}</h1>
  <div class="row">
    <div class="col-md-3"  v-for="trainingHistory in trainingsHistory" :key="trainingHistory.id">
      <div class="card" style="width: 18rem;">
        <div class="card-body">
          <h5 class="card-title">{{getFacilityName(trainingHistory)}}</h5>
          <p class="card-text">
            Sport facility: {{getFacilityName(trainingHistory)}}  <br>
            <ul>
              <li v-for="date in trainingsHistory.trainingDates">
                  <p>Training was at :{{date}}</p> <br>
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

export default {
  name: "Trainings",
  data: function () {
    return {
      trainingsHistory: [],
      trainings: []
    }
  },
  async mounted() {
    let resp = await axios.get('http://localhost:8081/myTrainings', {headers: this.createHeadersWithToken()})
    this.trainingsHistory = resp.data
    let respp = await axios.get('http://localhost:8081/myTrainingsInfo', {headers: this.createHeadersWithToken()})
    this.trainings = respp.data
  },
  methods:
  {
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
    },
    getTrainingName(trainingHistory){
      for (const training of this.trainings){
          if(training.id === trainingHistory.trainingId){
            return training.name
            }
      }
    },
    async getFacilityName(trainingHistory) {
      for (const training of this.trainings) {
        if (training.id === trainingHistory.trainingId) {
          let resp = await axios.get('http://localhost:8081/facilityByTrainingId', {
            params: {
              facilityId: trainingHistory.trainingId
            }
          })
          let facility = resp.data
          return facility.name;
        }
      }
      return ''
    }
  }
}
</script>

<style scoped>

</style>