<template>
  <Menu/>
  <form id="form">
    <div class="mb-3 row">
      <label for="username" class="col-sm-2 col-form-label">Username:</label>
      <div class="col-sm-4">
        <input type="text" :readonly="!editClicked" class="form-control" id="username" v-model="user.username">
      </div>
    </div>
    <div class="mb-3 row">
      <label for="name" class="col-sm-2 col-form-label">Name:</label>
      <div class="col-sm-4">
        <input type="text" :readonly="!editClicked" class="form-control" id="name" v-model="user.name">
      </div>
    </div>
    <div class="mb-3 row">
      <label for="surname" class="col-sm-2 col-form-label">Surname:</label>
      <div class="col-sm-4">
        <input type="text" :readonly="!editClicked" class="form-control" id="surname" v-model="user.surname">
      </div>
    </div>
    <div class="mb-3 row">
      <label for="sex" class="col-sm-2 col-form-label">Sex:</label>
      <div class="col-sm-4">
        <input type="text" v-show="!editClicked" class="form-control-plaintext" id="sex" v-model="user.gender">
      </div>
      <div v-show="editClicked" class="col-sm-4">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="inlineRadioOptions" id="femaleSex"
                 value="female" required/>
          <label class="form-check-label" for="femaleSex">Female</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="inlineRadioOptions" id="maleSex"
                 value="male" required/>
          <label class="form-check-label" for="maleSex">Male</label>
        </div>
      </div>
    </div>
    <div class="mb-3 row">
      <label for="birthDate" class="col-sm-2 col-form-label">Date of birth:</label>
      <div class="col-sm-4">
        <input type="text" readonly v-show="!editClicked" class="form-control" id="birthDate" v-model="user.birthDate">
      </div>
      <div class="col-sm-4">
        <input type="date" v-show="editClicked" name="date" class="form-control form-control-lg" min="1900-01-01" max="2022-12-31"
               v-model="user.birthDate" required/>
      </div>
    </div>
    <div class="mb-3 row col-1">
      <button type="button" class="btn btn-primary" @click="toggleEdit()" v-if="!editClicked">Edit</button>
    </div>
    <div class="mb-3 row col-1">
      <button type="button" class="btn btn-success" @click="toggleEdit(); submitForm()" v-if="editClicked">Save</button>
      <button type="button" class="btn btn-secondary" @click="toggleEdit(); cancelForm()" v-if="editClicked">Cancel</button>
    </div>
  </form>
</template>

<script>
import axios from 'axios'
import Menu from "../components/Menu"

export default {
  name: "MyProfile",
  components: {
    Menu
  },
  data: function() {
    return {
      user: '',
      editClicked: false,
    }
  },
  methods: {
    async GetUserInfo() {
      let result = await axios
          .get('http://localhost:8081/userInfo', {headers: this.createHeadersWithToken() })
      this.user = result.data
      this.user.birthDate = this.formatDate(this.user.birthDate)
    },
    formatDate(value) {
      if (value) {
        let splitted = value.split('-')

        return `${splitted[2]}-${splitted[1]}-${splitted[0]}`
      }
    },
    createHeadersWithToken() {
      return {"Authorization" : `Bearer ${localStorage.getItem('token')}`}
    },
    toggleEdit() {
      if (this.editClicked) {
        this.editClicked = false
      } else {
        this.editClicked = true
        if (this.user.gender === 'MALE') {
          document.getElementById('maleSex').checked = true
        } else {
          document.getElementById('femaleSex').checked = true
        }
      }
    },
    submitForm() {
      let sex = document.querySelector('input[name="inlineRadioOptions"]:checked').value;
      if (sex === "male") {
        this.user.gender = 'MALE'
      } else {
        this.user.gender = 'FEMALE'
      }
      let postData = this.user
      axios
          .post('http://localhost:8081/userInfo/edit', postData)
      // .then(this.Redirect("/userInfo"))
    },
    cancelForm() {
      this.GetUserInfo();
    }
  },
  async mounted() {
    await this.GetUserInfo()
  }
}
</script>

<style scoped>

</style>