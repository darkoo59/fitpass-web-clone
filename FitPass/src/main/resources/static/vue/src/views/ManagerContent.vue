<template>
    <div class="col-md-6 p-4" v-for="cont in content" :key="cont.id">
      <div class="card" style="width: 30rem;">
        <div class="card">
          <img style="width: 50%; height: 50%;" v-bind:src="require('@/assets/images/' + cont.image)" class="card-img-top">
          <h5 class="card-title"> {{cont.name}} </h5>
          <p class="card-text">
            <b>Type: </b> {{ cont.type }} <br>
            <b>Description: </b> {{ cont.description }} <br>
            <b>Duration: </b> {{ cont.duration }} <br>
            <br>
  <!--            <button-->
  <!--                type="button"-->
  <!--                class="btn btn-primary"-->
  <!--                data-bs-toggle="modal"-->
  <!--                data-bs-target="#editTraining"-->
  <!--                style="width: 100px;"-->
  <!--                @click="editTraining(getTraining(trainingHistory))">-->
  <!--              Edit-->
  <!--            </button>-->
          </p>
        </div>
      </div>
    </div>

</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      content: []
    }
  },
  async mounted() {
    let res = await axios.get(this.$port.value + '/managerContent', {headers: this.createHeadersWithToken()})
    this.content = res.data
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
    }
  }
}
</script>