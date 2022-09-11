<template>
  <div class="content">
    <div class="row">
      <div class="col-lg-4">
      </div>
      <div class="col-lg-4">
          <div class="form-group p-3">
            <label class="form-label text-white" for="exampleInputEmail1">Name</label>
            <input type="text" name="name" class="form-control" aria-describedby="emailHelp"
                   v-model="contentData.name" placeholder="Enter name (required)" required>
          </div>
          <div class="form-group p-3">
            <label class="form-label text-white" for="exampleInputEmail1">Type</label>
            <input type="text" name="type" class="form-control" aria-describedby="emailHelp"
                   v-model="contentData.type" placeholder="Enter type (required)" required>
          </div>
          <div class="form-group p-3">
            <label class="form-label text-white" for="image">Image</label>
            <input type="file" @change="onPhotoSelected" name="image" accept="image/png, image/gif, image/jpeg" class="form-control form-control-lg" required/>
          </div>
          <div class="form-group p-3">
            <label class="form-label text-white" for="exampleInputEmail1">Description</label>
            <input type="text" name="type" class="form-control" aria-describedby="emailHelp"
                   v-model="contentData.description" placeholder="Enter description (optional)">
          </div>
          <div class="form-group p-3">
            <label class="form-label text-white" for="exampleInputEmail1">Duration</label>
            <input type="text" name="duration" class="form-control" aria-describedby="emailHelp"
                   v-model="contentData.duration" placeholder="Enter duration in minutes (optional)">
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
  name: "AddNewContent",
  data: function() {
    return {
      contentData: {
        name: '',
        type: '',
        image: '',
        description: '',
        duration: '',
      },
      selectedPhoto: null,
      responseData: ''
    }
  },
  methods:{
    createHeadersWithToken() {
      return {"Authorization": `Bearer ${localStorage.getItem('token')}`}
    },
    async submitForm() {
      const formData = new FormData();
      formData.append('photo', this.selectedPhoto)

      let resp = await axios
          .post(this.$port.value + "/addNewContent/logo/" + this.contentData.name, formData)
      this.contentData.image = resp.data
      await axios
          .post(this.$port.value + "/addNewContent", this.contentData, {headers : this.createHeadersWithToken(),
            params:{
              name: this.contentData.name,
              type: this.contentData.type,
              image: this.contentData.image,
              description: this.contentData.description,
              duration: this.contentData.duration
            }
          })

      await this.$router.push('/')
    },
    onPhotoSelected(event) {
      this.selectedPhoto = event.target.files[0]
    }
  }
}
</script>

<style scoped>

</style>