<template>
  <div class="content">
    <div class="row">
      <div class="col-lg-4">
      </div>
      <div class="col-lg-4">
        <div class="form-group p-3">
          <label class="form-label text-white" for="exampleInputEmail1">Name</label>
          <input type="text" name="name" class="form-control" aria-describedby="emailHelp"
                 v-model="trainingData.name" placeholder="Enter name(required)" required>
        </div>
        <div class="form-group p-3">
          <label class="form-label text-white" for="exampleInputEmail1">Type</label>
          <input type="text" name="type" class="form-control" aria-describedby="emailHelp"
                 v-model="trainingData.type" placeholder="Enter type(required)" required>
        </div>
        <div class="form-group p-3">
          <label class="form-label text-white">Start : </label>
          <input v-model="trainingData.startTime" type="time" class="w-auto" id="timeId1"/>
          <label class="form-label text-white">End : </label>
          <input v-model="trainingData.endTime" type="time" class="w-auto" id="timeId2"/>
        </div>
        <div class="form-group p-3">
          <label class="form-label text-white" for="coaches">Choose coach:</label>
          <select v-model="coach" name="coaches" id="coachesSelection">
            <option v-for="coach in coaches" :value="coach" :key="coach">{{coach.name}} {{coach.surname}}</option>
          </select>
        </div>
        <div class="form-group p-3">
          <label class="form-label text-white" for="exampleInputEmail1">Description</label>
          <input type="text" name="description" class="form-control" aria-describedby="emailHelp"
                 v-model="trainingData.description" placeholder="Enter description(required)" required>
        </div>
        <div class="form-group p-3">
          <label class="form-label text-white" for="exampleInputEmail1">Price</label>
          <input type="text" name="price" class="form-control" aria-describedby="emailHelp"
                 v-model="trainingData.price" placeholder="Enter price in RSD(required)" required>
        </div>
        <div class="form-group p-3">
          <label class="form-label text-white" for="image">Image</label>
          <input type="file" @change="onPhotoSelected" name="image" accept="image/png, image/gif, image/jpeg" class="form-control form-control-lg" required/>
        </div>
        <div class="m-3">
          <input class="btn btn-primary btn-lg" @click="submitForm" value="Submit" />
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AddNewTraining",
  data: function () {
    return {
      coaches: [],
      coach: '',
      selectedPhoto: '',
      trainingData: {
        name: '',
        type: '',
        startTime: '',
        endTime: '',
        coachId: '',
        description: '',
        price: '',
        image: ''
      },
    }
  },
  async mounted() {
    let resp = await axios.get(this.$port.value + '/allCoaches')
    this.coaches = resp.data
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
    },
    async submitForm() {
      if(!isNaN(this.trainingData.price)) {
        const formData = new FormData();
        formData.append('photo', this.selectedPhoto)

        let resp = await axios
            .post(this.$port.value + "/addNewContent/logo/" + this.trainingData.name, formData)
        this.trainingData.image = resp.data
        this.trainingData.coachId = this.coach.id
        await axios
            .post(this.$port.value + "/addNewTraining", this.trainingData, {
              headers: this.createHeadersWithToken(),
              params: {
                name: this.trainingData.name,
                type: this.trainingData.type,
                image: this.trainingData.image,
                description: this.trainingData.description,
                price: this.trainingData.price,
                startTime: this.trainingData.startTime,
                endTime: this.trainingData.endTime,
                coachId: this.trainingData.coachId
              }
            })

        await this.$router.push('/')
      }else
        alert("Price need to be number")
    },
    onPhotoSelected(event) {
      this.selectedPhoto = event.target.files[0]
    }
  }
}
</script>

<style scoped>

</style>