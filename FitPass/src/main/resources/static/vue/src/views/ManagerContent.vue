<template>
  <section class="vh-100 gradient-custom">
    <HomeHeader/>
    <Menu v-if="token != null"/>
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
              <button
                  type="button"
                  class="btn btn-primary"
                  data-bs-toggle="modal"
                  data-bs-target="#editContent"
                  style="width: 100px;"
                  @click="editContent(cont)">
                Edit
              </button>
          </p>
        </div>
      </div>
    </div>

    <div class="modal fade" id="editContent" data-bs-backdrop="static" tabindex="-1" aria-labelledby="editContentLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div v-if="modalRender" class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editContentLabel">Edit training</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">

            <form>
              <div class="mb-3">
                <label for="name" class="col-form-label">Name:</label>
                <input id="name" type="text" name="name" class="form-control"
                       v-model="contentForEdit.name" placeholder="Enter name (required)" required>
              </div>
              <div class="mb-3">
                <label for="message-text" class="col-form-label">Type:</label>
                <input type="text" name="type" class="form-control"
                       v-model="contentForEdit.type" placeholder="Enter type (required)" required>
              </div>
              <div class="mb-3">
                  <label for="message-text" class="col-form-label">Description:</label>
                  <input v-model="contentForEdit.description" type="text" class="form-control" required/>
              </div>
              <div class="mb-3">
                <label for="message-text" class="col-form-label">Duration:</label>
                <input v-model="contentForEdit.duration" type="text" class="form-control"/>
              </div>
              <div class="mb-3">
                <label for="message-text" class="col-form-label">Image:</label>
                <input type="file" @change="onPhotoSelected" name="image" accept="image/png, image/gif, image/jpeg"
                       class="form-control form-control-lg" required/>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button ref="close" type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary" @click="editSubmit">Save changes</button>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import axios from "axios";
import HomeHeader from "@/components/HomeHeader";
import Menu from "@/components/Menu"

export default {
  components: {
    HomeHeader,
    Menu
  },
  data() {
    return {
      token: window.localStorage.getItem('token'),
      content: [],
      contentForEdit: '',
      modalRender: false,
      selectedPhoto: ''
    }
  },
  async mounted() {
    let res = await axios.get(this.$port.value + '/managerContent', {headers: this.createHeadersWithToken()})
    this.content = res.data
  },
  beforeMount() {
    if(localStorage.getItem('showMenu')){
      this.$router.go()
      localStorage.removeItem('showMenu')
    }
  },
  methods: {
    createHeadersWithToken() {
      return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
    },
    editContent(editContent) {
      this.contentForEdit = { ...editContent }
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
            .put(this.$port.value + "/managerContent/edit/" + this.contentForEdit.name + '/image', formData)

        this.contentForEdit.image = res.data
      }

      let data = [
        this.contentForEdit.id,
        this.contentForEdit.name,
        this.contentForEdit.type,
        this.contentForEdit.description,
        this.contentForEdit.duration,
        this.contentForEdit.image,
      ]

      let res2 = await axios
          .put(this.$port.value + "/managerContent/edit/" + this.contentForEdit.id, data)
      if (res2.data === 'SUCCESS') {
        this.$refs.close.click()
        let res = await axios.get(this.$port.value + '/managerContent', {headers: this.createHeadersWithToken()})
        this.content = res.data
      }
    },
    validateData() {
      return !(this.contentForEdit.name.length < 1 || this.contentForEdit.type.length < 1 ||
          this.contentForEdit.description.length < 1 || this.contentForEdit.duration.length < 1);
    }
  }
}
</script>

<style scoped>
.gradient-custom {
  background: -webkit-linear-gradient(to bottom right, #000428, #004e92);
  background: linear-gradient(to bottom right, #000428, #004e92);
}
</style>