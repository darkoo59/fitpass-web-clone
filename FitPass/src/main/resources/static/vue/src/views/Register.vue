<template>
  <HomeHeader />
  <section class="vh-100 gradient-custom">
    <div class="container py-5 h-100">
      <div class="row justify-content-center align-items-center h-100">
        <div class="col-12 col-lg-9 col-xl-7">
          <div class="card shadow-2-strong card-registration" style="border-radius: 15px;">
            <div class="card-body p-4 p-md-5">
              <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Become a member of the FitPass community</h3>
              <form @submit.prevent="register">

                <div class="row">
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label class="form-label" for="firstName">Name</label>
                      <input type="text" v-model="name" id="firstName" name="name" class="form-control form-control-lg" required/>
                    </div>

                  </div>
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label class="form-label" for="lastName">Surname</label>
                      <input type="text" v-model="surname" id="lastName" name="surname" class="form-control form-control-lg" required/>
                    </div>

                  </div>
                </div>

                <div class="row">
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label for="username" class="form-label">Username</label>
                      <input type="text" v-model="username" id="username" name="username" class="form-control form-control-lg" required/>
                    </div>

                  </div>

                  <div class="col-md-6 mb-4">
                    <div class="form-outline">
                      <label for="password" class="form-label">Password</label>
                      <input type="password" v-model="password" id="password" name="password" class="form-control form-control-lg" required/>
                    </div>
                  </div>

                </div>

                <div class="row">
                  <div class="col-md-6 mb-4 pb-2">

                    <label class="form-label" for="datepicker">Date of birth</label>
                    <input type="date" v-model="date" name="date" class="form-control form-control-lg" min="1900-01-01" max="2022-12-31" required/>

                  </div>

                  <div class="col-md-6 mb-4">

                    <h6 class="mb-2 pb-1">Sex</h6>

                    <div class="col-md-12 mb-12 pb-2">
                      <div class="form-check form-check-inline">
                        <input class="form-check-input" v-model="sex" type="radio" name="inlineRadioOptions" id="femaleSex"
                               value="female" required/>
                        <label class="form-check-label" for="femaleSex">Female</label>
                      </div>

                      <div class="form-check form-check-inline">
                        <input class="form-check-input" v-model="sex" type="radio" name="inlineRadioOptions" id="maleSex"
                               value="male" required/>
                        <label class="form-check-label" for="maleSex">Male</label>
                      </div>
                    </div>

                    <span v-show="passwordError" class="text-danger">Password must be at least 8 characters long</span>

                  </div>
                </div>

                <div class="mt-4 pt-2">
                  <button class="btn btn-primary btn-lg" type="submit">Submit</button>
                </div>

              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import HomeHeader from "@/components/HomeHeader";
import axios from "axios";
export default {
  components: {HomeHeader},
  data() {
    return {
      name: '',
      surname: '',
      username: '',
      password: '',
      date: '',
      sex: '',
      passwordError: false
    }
  },
  methods: {
    async register() {
      this.passwordError = false
      if (this.validate()) {
        let data = [ this.name, this.surname, this.username, this.password, this.date, this.sex]
        let res = await axios.post(this.$port.value + '/register', data)
        if (res.data === 'USER_EXISTS') {
          this.$toast.error("User with this username already exists", {position: 'top', duration: 4000, maxToasts: 1})
        } else {
          this.$toast.success("Successful registration", {position: 'top', duration: 4000, maxToasts: 1})
          await this.$router.push('/')
        }
      }
    },
    validate() {
      if (this.name && this.surname && this.username && this.validatePassword() && this.date && this.sex)
        return true
      else
        return false
    },
    validatePassword() {
      if (this.password.trim().length < 8) {
        this.passwordError = true
        return false;
      }
      return true
    }
  }
}
</script>

<style scoped>
.gradient-custom {
  background: -webkit-linear-gradient(to bottom right, #000428, #004e92);
  background: linear-gradient(to bottom right, #000428, #004e92);
}

.card-registration .select-input.form-control[readonly]:not([disabled]) {
  font-size: 1rem;
  line-height: 2.15;
  padding-left: .75em;
  padding-right: .75em;
}
.card-registration .select-arrow {
  top: 13px;
}
</style>