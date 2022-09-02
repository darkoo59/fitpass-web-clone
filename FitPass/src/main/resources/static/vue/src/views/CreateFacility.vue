<template>
  <section class="vh-100 gradient-custom">
    <HomeHeader />
    <Menu/>

    <div class="container py-5 h-100">
      <div class="row justify-content-center align-items-center h-100">
        <div class="col-12 col-lg-9 col-xl-7">
          <div class="card shadow-2-strong card-registration" style="border-radius: 15px;">
            <div class="card-body p-4 p-md-5">
              <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Create new sport facility</h3>
              <form method="post" action="http://localhost:8081/createFacility">

                <div class="row">
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label class="form-label" for="name">Name</label>
                      <input type="text" id="name" name="name" class="form-control form-control-lg" required/>
                    </div>

                  </div>
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label class="form-label" for="type">Type</label>
                      <input type="text" id="type" name="type" class="form-control form-control-lg" required/>
                    </div>

                  </div>
                </div>

                <div class="row">
                  <div class="col-md-6 mb-4">

                    <div class="form-outline">
                      <label for="City" class="form-label">City</label>
                      <input type="text" id="city" name="city" class="form-control form-control-lg" required/>
                    </div>

                  </div>

                  <div class="col-md-6 mb-4">
                    <div class="form-outline">
                      <label for="streetAndNumber" class="form-label">Street and number</label>
                      <input type="streetAndNumber" id="streetAndNumber" name="streetAndNumber" class="form-control form-control-lg" required/>
                    </div>
                  </div>

                </div>

                <div class="row">
                  <div class="col-md-6 mb-4 pb-2">

                    <label class="form-label" for="logo">Logo</label>
                    <input type="file" name="logo" accept="image/png, image/gif, image/jpeg" class="form-control form-control-lg" required/>

                  </div>
                  <div class="col-md-6 mb-4 pb-2">
                    <label for="managers">Choose manager:</label>
                    <select name="managers" id="managersSelection">
                        <option v-for="manager in managers" :value="manager">{{manager.name}} {{manager.surname}}</option>
                    </select>
                      <input class="btn btn-primary btn-lg" type="button" @click="createManager()" id="newManagerBtn" value="Create new manager first" />
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
  name:'CreateFacility',
  components: {
    Menu
  },

  data: function () {
    return {
      managers: []
    }
  },
  methods: {
    createManager() {
      window.localStorage.setItem('createManager', 'true')
      this.$router.push('/administratorCreateProfiles')
    }
  }
  ,
  async mounted() {
    let resp = await axios.get('http://localhost:8081/managersForNewFacility')
    this.managers = resp.data
    // if(selection.option)
    //   newManagerBtn.style.visibility = "visible"
    // else
    //   newManagerBtn.style.visibility = "hidden"
    this.$nextTick(function () {
      // Code that will run only after the
      // entire view has been rendered
      let newManagerBtn = document.getElementById('newManagerBtn')
      let selection = document.getElementById('managersSelection');
      if (selection.options.length == 0)
        newManagerBtn.style.visibility = "visible"
      else
        newManagerBtn.style.visibility = "hidden"
    })
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