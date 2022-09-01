<template>
  <section class="vh-100 gradient-custom">
    <HomeHeader />
    <Menu/>
    <div class="container py-5 h-100">
      <div class="row justify-content-center align-items-center h-100">
        <div class="col-12 col-lg-9 col-xl-7">
          <div class="card shadow-2-strong card-registration" style="border-radius: 15px;">
            <div class="card-body p-4 p-md-5">
              <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Become a member of the FitPass community</h3>
              <form method="post" action="http://localhost:8081/administratorCreateProfile">

                <div class="row">
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label class="form-label" for="firstName">Name</label>
                      <input type="text" id="firstName" name="name" class="form-control form-control-lg" required/>
                    </div>

                  </div>
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label class="form-label" for="lastName">Surname</label>
                      <input type="text" id="lastName" name="surname" class="form-control form-control-lg" required/>
                    </div>

                  </div>
                </div>

                <div class="row">
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label for="username" class="form-label">Username</label>
                      <input type="text" id="username" name="username" class="form-control form-control-lg" required/>
                    </div>

                  </div>

                  <div class="col-md-6 mb-4">
                    <div class="form-outline">
                      <label for="password" class="form-label">Password</label>
                      <input type="password" id="password" name="password" class="form-control form-control-lg" required/>
                    </div>
                  </div>

                </div>

                <div class="row">
                  <div class="col-md-6 mb-4 pb-2">

                    <label class="form-label" for="datepicker">Date of birth</label>
                    <input type="date" name="date" class="form-control form-control-lg" min="1900-01-01" max="2022-12-31" required/>

                  </div>

                  <div class="col-md-6 mb-4">

                    <h6 class="mb-2 pb-1">Sex</h6>

                    <div class="col-md-12 mb-12 pb-2">
                      <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="sexRadioOptions" id="femaleSex"
                               value="female" required/>
                        <label class="form-check-label" for="femaleSex">Female</label>
                      </div>

                      <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="sexRadioOptions" id="maleSex"
                               value="male" required/>
                        <label class="form-check-label" for="maleSex">Male</label>
                      </div>
                    </div>


                  </div>
                </div>

                <div class="row">

                  <div class="col-md-6 mb-4">

                    <h6 class="mb-2 pb-1">Role</h6>

                    <div class="col-md-12 mb-12 pb-2">
                      <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="roleRadioOptions" id="managerRole"
                               value="manager" required/>
                        <label class="form-check-label" for="managerRole">Manager</label>
                      </div>

                      <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="roleRadioOptions" id="trainerRole"
                               value="trainer" required/>
                        <label class="form-check-label" for="trainerRole">Coach</label>
                      </div>
                    </div>


                  </div>
                </div>

                <div class="mt-4 pt-2">
                  <input class="btn btn-primary btn-lg" type="submit" value="Submit" />
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
import Menu from "@/components/Menu";
import axios from "axios";
export default {
  name:'AdministratorCreateProfiles',
  components: {
    Menu
  }
}
</script>

<style scoped>
.gradient-custom {
  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to bottom right, #000428, #004e92);

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
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