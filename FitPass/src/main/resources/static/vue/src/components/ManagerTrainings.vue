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
        <div class="card">
          <img style="width: 50%; height: 50%;" v-bind:src="getImage(trainingHistory)" class="card-img-top">
          <h5 class="card-title">{{getTrainingName(trainingHistory)}}</h5>
          <p class="card-text">
            <b>Sport facility : </b> {{getFacilityName(trainingHistory)}}<br>
            <b>Training type : </b> {{getTrainingType(trainingHistory)}}<br>
            <b>Application date and time : </b>{{getApplicationDateTime(trainingHistory)}}<br>
            <b>Price : </b>{{getTrainingPrice(trainingHistory)}}
            <br>
            <button
                type="button"
                class="btn btn-primary"
                data-bs-toggle="modal"
                data-bs-target="#exampleModal"
                style="width: 100px;"
                @click="editTraining(getTraining(trainingHistory))">
              Edit
            </button>
          </p>
        </div>
      </div>
    </div>

    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            ...
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary">Save changes</button>
          </div>
        </div>
      </div>
    </div>


<!--      <div v-if="modalRender" class="modal" id="editTraining" data-bs-backdrop="static" tabindex="-1" aria-labelledby="editTrainingLabel" aria-hidden="true">-->
<!--        <div class="modal-dialog">-->
<!--          <div class="modal-content">-->
<!--            <div class="modal-header">-->
<!--              <h5 class="modal-title" id="editTrainingLabel">Edit training</h5>-->
<!--              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>-->
<!--            </div>-->
<!--            <div class="modal-body">-->

<!--              <form>-->
<!--                <div class="mb-3">-->
<!--                  <label for="name" class="col-form-label">Name:</label>-->
<!--                  <input id="name" type="text" name="name" class="form-control"-->
<!--                         v-model="trainingForEdit.name" placeholder="Enter name (required)" required>-->
<!--                </div>-->
<!--                <div class="mb-3">-->
<!--                  <label for="message-text" class="col-form-label">Type:</label>-->
<!--                  <input type="text" name="type" class="form-control" aria-describedby="emailHelp"-->
<!--                         v-model="trainingForEdit.type" placeholder="Enter type (required)" required>-->
<!--                </div>-->
<!--                <div class="mb-3">-->
<!--                  <div class="row">-->
<!--                    <div class="col">-->
<!--                      <label for="message-text" class="col-form-label">Start : </label>-->
<!--                      <input v-model="trainingForEdit.duration.start" type="time" class="w-auto form-control" id="timeId1"/>-->
<!--                    </div>-->
<!--                    <div class="col">-->
<!--                      <label for="message-text" class="col-form-label">End : </label>-->
<!--                      <input v-model="trainingForEdit.duration.end" type="time" class="w-auto form-control" id="timeId2"/>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                </div>-->
<!--                <div class="mb-3">-->
<!--                  <label for="message-text" class="col-form-label">Choose coach:</label>-->
<!--                  <select class="form-control" v-model="coach" name="coaches" id="coachesSelection">-->
<!--                    <option v-for="coach in coaches" :value="coach" :key="coach">{{coach.name}} {{coach.surname}}</option>-->
<!--                  </select>-->
<!--                </div>-->
<!--                <div class="mb-3">-->
<!--                  <label for="message-text" class="col-form-label">Description:</label>-->
<!--                  <input type="text" name="description" class="form-control" aria-describedby="emailHelp"-->
<!--                         v-model="trainingForEdit.description" placeholder="Enter description (required)" required>-->
<!--                </div>-->
<!--                <div class="mb-3">-->
<!--                  <label for="message-text" class="col-form-label">Price:</label>-->
<!--                  <input type="text" name="price" class="form-control" aria-describedby="emailHelp"-->
<!--                         v-model="trainingForEdit.price" placeholder="Enter price in RSD(required)" required>-->
<!--                </div>-->
<!--                <div class="mb-3">-->
<!--                  <label for="message-text" class="col-form-label">Image:</label>-->
<!--                  <input type="file" @change="onPhotoSelected" name="image" accept="image/png, image/gif, image/jpeg"-->
<!--                         class="form-control form-control-lg" required/>-->
<!--                </div>-->
<!--              </form>-->
<!--            </div>-->
<!--            <div class="modal-footer">-->
<!--              <button ref="close" type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>-->
<!--              <button type="button" class="btn btn-primary" @click="editSubmit">Save changes</button>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
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
      },
      trainingForEdit: null,
      coach: '',
      coaches: [],
      modalRender: false,
      selectedPhoto: ''
    }
  },
  async beforeMount() {
    let res = await axios.get(this.$port.value + '/allCoaches')
    this.coaches = res.data
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
    getTrainingImage(trainingHistory) {
      for (let training of this.managerTrainings) {
        if (training.id === trainingHistory.trainingId) {
          return training.image
        }
      }
    },
    getTraining(trainingHistory) {
      for (const training of this.managerTrainings) {
        if (training.id === trainingHistory.trainingId) {
          return training
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
    },
    editTraining(editTraining) {
      this.trainingForEdit = { ...editTraining }
      setTimeout(() => (this.modalRender = true), 0)
    },
    onPhotoSelected(event) {
      this.selectedPhoto = event.target.files[0]
    },
    async editSubmit() {
      if (!this.validateData()) {
        this.$toast.error("Please complete all fields", {position: 'top', duration: 2500, maxToasts: 1})
        return
      }
      if (this.selectedPhoto.length !== 0) {
        const formData = new FormData();
        formData.append('photo', this.selectedPhoto)

        let res = await axios
            .put(this.$port.value + "/managerTrainings/edit/" + this.trainingForEdit.name + '/logo', formData)

        this.trainingForEdit.image = res.data
      }

      this.trainingForEdit.coachId = this.coach.id

      let data = [
        this.trainingForEdit.id,
        this.trainingForEdit.name,
        this.trainingForEdit.type,
        this.trainingForEdit.image,
        this.trainingForEdit.description,
        this.trainingForEdit.price,
        this.trainingForEdit.duration.start,
        this.trainingForEdit.duration.end,
        this.trainingForEdit.coachId
      ]

      let res2 = await axios
          .put(this.$port.value + "/managerTrainings/edit/" + this.trainingForEdit.id, data)
      if (res2.data === 'SUCCESS') {
        this.$refs.close.click();
      }
    },
    validateData() {
      if (this.trainingForEdit.name.length < 1 || this.trainingForEdit.type.length < 1 ||
          this.trainingForEdit.description.length < 1 || this.trainingForEdit.price.length < 1) {
        return false
      }
      return true
    },
    getImage(trainingHistory) {
      return require('@/assets/images/' + this.getTrainingImage(trainingHistory))
    }
  }
}
</script>

<style scoped>

</style>