<template>
  <section class="vh-100 gradient-custom">
    <HomeHeader />
    <Menu/>

    <br>
    <div class="container" id="searchFilter">
      <div class="row">
        <div class="col-sm">
          <input type="text" v-model="filter.searchInput" placeholder="Search by name..." />
        </div>
        <select v-model="filter.role" class="form-select col-sm">
          <option selected>Role</option>
          <option value="1">Administrator</option>
          <option value="2">Manager</option>
          <option value="3">Coach</option>
          <option value="4">Customer</option>
        </select>
        <select v-model="filter.type" class="form-select col-sm">
          <option selected>Customer type</option>
          <option value="1">Bronze</option>
          <option value="2">Silver</option>
          <option value="3">Gold</option>
        </select>
        <select v-model="filter.sort" class="form-select col-sm">
          <option selected>Sort by</option>
          <option value="1">Name: Ascending</option>
          <option value="2">Name: Descending</option>
          <option value="3">Surname: Ascending</option>
          <option value="4">Surname: Descending</option>
<!--          <option value="5">Collected points: Ascending</option>-->
<!--          <option value="6">Collected points: Descending</option>-->
        </select>
        <div class="col-sm">
          <button class="btn-primary" @click="searchProfiles()">
            Search
          </button>
        </div>
        <div class="col-sm">
          <button class="btn-secondary" @click="removeFilters()">
            Remove filters
          </button>
        </div>
      </div>
    </div>
    <br>
    <br>



    <div class="row">
      <div class="col-md-3 p-2 w-25"  v-for="profile in filteredProfiles" :key="profile.username">
        <div class="card" style="width: 18rem;">
          <div class="card-body">
            <h5 class="card-title">{{ profile.name }} {{profile.surname}}</h5>
            <p class="card-text">
              <b>Username:</b> {{profile.username}} <br>
              <b>Role:</b> {{this.convertRole(profile.role)}} <br>
              <b>Birth day:</b> {{ profile.birthDate }} <br>
              <div v-if="profile.role === '3'">
                <b>Collected points: </b>{{this.getCollectedPoints(profile)}} <br>
              </div>
            </p>
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
  },
  data: function () {
    return {
      profiles: [],
      filteredProfiles: [],
      profilesNum: 0,
      roles: [],
      customers: [],
      filter: {
        searchInput: "",
        role: "Role",
        type: "Customer type",
        sort: "Sort by"
      }
    }
  },
  async mounted() {
    let resp = await axios.get(this.$port.value + '/allProfiles')
    this.profiles = resp.data
    this.filteredProfiles = resp.data
    let response = await axios.get('http://localhost:8081/getAllCustomers')
    this.customers = response.data
  },
  methods: {
    getCollectedPoints(profile)
    {
      for(const customer of this.customers){
        if(customer.id === profile.id)
          return customer.collectedPoints
      }
      return ''
    },
    incrementedNum() {
      this.profilesNum = this.profilesNum + 1
      return this.profilesNum.toString()
    },
    searchProfiles() {
      axios.post(this.$port.value + '/sarchProfiles', this.input)
          .then(response => (alert(response.data),
                  this.filteredProfiles = response.data,
                  alert(this.filteredProfiles)))
    },
    convertRole(role){
      if(role === '0')
        return 'Administrator'
      else if (role === '1')
        return 'Manager'
      else if (role === '2')
        return 'Coach'
      else if (role ==='3')
        return 'Customer'
      else
        return 'Non defined'
    },
    async searchProfiles() {
      let resp = await axios
          .post('http://localhost:8081/searchProfiles', this.filter)
      this.filteredProfiles = resp.data
    },
    removeFilters() {
      this.filteredProfiles = this.profiles
      this.filter.searchInput = ""
      this.filter.role = "Role"
      this.filter.type = "Customer type"
      this.filter.sort = "Sort by"
    }
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