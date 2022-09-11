<template>
  <Menu/>
  <section class="vh-100 gradient-custom">
    <form id="form d-flex-col justify-content-center align-items-center">
      <div class="content">
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
            <input type="text" v-show="!editClicked" class="form-control-plaintext text-white" id="sex" v-model="user.gender">
            <div v-show="editClicked">
              <div class="form-check form-check-inline">
                <input class="form-check-input" v-model="user.gender" type="radio" name="inlineRadioOptions" id="femaleSex"
                       value="FEMALE" required/>
                <label class="form-check-label" for="femaleSex">Female</label>
              </div>
              <div class="form-check form-check-inline">
                <input class="form-check-input" v-model="user.gender" type="radio" name="inlineRadioOptions" id="maleSex"
                       value="MALE" required/>
                <label class="form-check-label" for="maleSex">Male</label>
              </div>
            </div>
          </div>
        </div>
        <div class="mb-3 row">
          <label for="birthDate" class="col-sm-2 col-form-label">Date of birth:</label>
          <div class="col-sm-4">
            <input type="text" readonly v-show="!editClicked" class="form-control" id="birthDate" v-model="user.birthDate">
            <input type="date" v-show="editClicked" name="date" class="form-control form-control-lg" min="1900-01-01" max="2022-12-31"
                   v-model="user.birthDate" required/>
          </div>
        </div>
        <div class="mb-3 row col-4">
          <div class="col-1"></div>
          <button type="button" class="btn btn-primary col-2" @click="toggleEdit()" v-if="!editClicked">Edit</button>
          <button type="button" class="btn btn-success col-2" @click="toggleEdit(); submitForm()" v-if="editClicked">Save</button>
          <div class="col-1"></div>
          <button type="button" class="btn btn-secondary row col-2" @click="toggleEdit(); cancelForm()" v-if="editClicked">Cancel</button>
        </div>
        <div class="mb-3 row col-4">
          <div class="col-1"></div>
          <button type="button" class="btn btn-primary col-3" @click="togglePassword">Change password</button>
        </div>
        <div v-show="changePasswordClicked">
          <div class="mb-3 row">
            <label for="oldPassword" class="col-sm-2 col-form-label">Old password:</label>
            <div class="col-sm-4">
              <input type="password" class="form-control" id="oldPassword" v-model="oldPassword">
            </div>
          </div>
          <div class="mb-3 row">
            <label for="newPassword" class="col-sm-2 col-form-label">New password:</label>
            <div class="col-sm-4">
              <input type="password" class="form-control" id="newPassword" v-model="newPassword">
            </div>
          </div>
          <div class="mb-3 row col-4">
            <div class="col-1"></div>
            <button type="button" class="btn btn-success col-2" @click="changePassword">Submit</button>
          </div>
        </div>
      </div>
    </form>
  </section>
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
      changePasswordClicked: false,
      oldPassword: '',
      newPassword: ''
    }
  },
  methods: {
    async getUserInfo() {
      let result = await axios
          .get(this.$port.value + '/userInfo', {headers: this.createHeadersWithToken() })
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
      }
    },
    togglePassword() {
      if (this.changePasswordClicked) {
        this.changePasswordClicked = false
      } else {
        this.changePasswordClicked = true
      }
    },
    submitForm() {
      let postData = [
        this.user.id,
        this.user.username,
        this.user.name,
        this.user.surname,
        this.user.birthDate,
        this.user.gender
      ]
      axios
          .post(this.$port.value + '/userInfo/edit', postData)
    },
    cancelForm() {
      this.getUserInfo()
    },
    async changePassword() {
      if (this.newPassword.length < 8) {
        this.$toast.error("Password must be at least 8 characters long", {position: 'top', duration: 4000, maxToasts: 1})
      } else {
        let passData = [this.user.id, this.oldPassword, this.newPassword]
        let res = await axios
            .put(this.$port.value + '/userInfo/edit/change-password', passData)
        if (res.data === 'PASSWORD_EXCEPTION') {
          this.$toast.error("Wrong password", {position: 'top', duration: 3000, maxToasts: 1})
        } else {
          this.$toast.success("Password successfully changed", {position: 'top', duration: 3000, maxToasts: 1})
          this.oldPassword = ''
          this.newPassword = ''
          this.togglePassword()
        }
      }
    }
  },
  async mounted() {
    await this.getUserInfo()
  }
}
</script>

<style scoped>
label {
  color: white;
}
.gradient-custom {
  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to bottom right, #000428, #004e92);

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to bottom right, #000428, #004e92);
}
</style>