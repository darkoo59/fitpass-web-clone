<template>
  <section class="vh-100 gradient-custom">
    <HomeHeader />
    <Menu/>

    <br>
    <input type="text" @input="searchProfiles" v-model="input" placeholder="Search profiles..." />
    <div v-for="prof in filteredProfiles" :key="prof.id">
      <h1 class="text-white">{{prof.username}}</h1>
<!--      <h3>{{}}</h3>-->
<!--      <h2>{{filteredProfiles.username}}</h2>-->
    </div>
    <div id="accordion" class="w-50">

<!--      <li v-for="profile in filteredProfiles" :key="profile.username">-->
<!--        <div class="card">-->
<!--          <div class="card-header">-->
<!--            <a class="btn" data-bs-toggle="collapse" href="#collapseOne">-->
<!--              {{profile.name}} {{profile.surname}}-->
<!--            </a>-->
<!--          </div>-->
<!--          <div id="collapseOne" class="collapse" data-bs-parent="#accordion">-->
<!--            <div class="card-body">-->
<!--              Name : {{profile.name}} <br>-->
<!--              Surname : {{profile.surname}} <br>-->
<!--              Username : {{profile.username}} <br>-->
<!--              Birth date : {{profile.birthDate}} <br>-->
<!--              Role : {{getRoleString(profile.role)}}-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </li>-->

<!--      <div class="card">-->
<!--        <div class="card-header">-->
<!--          <a class="collapsed btn" data-bs-toggle="collapse" href="#collapseTwo">-->
<!--            Collapsible Group Item #2-->
<!--          </a>-->
<!--        </div>-->
<!--        <div id="collapseTwo" class="collapse" data-bs-parent="#accordion">-->
<!--          <div class="card-body">-->
<!--            Lorem ipsum..-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->

<!--      <div class="card">-->
<!--        <div class="card-header">-->
<!--          <a class="collapsed btn" data-bs-toggle="collapse" href="#collapseThree">-->
<!--            Collapsible Group Item #3-->
<!--          </a>-->
<!--        </div>-->
<!--        <div id="collapseThree" class="collapse" data-bs-parent="#accordion">-->
<!--          <div class="card-body">-->
<!--            Lorem ipsum..-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->

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
      input: ""
    }
  },
  async mounted() {
    let resp = await axios.get('http://localhost:8081/allProfiles')
    this.profiles = resp.data
    this.filteredProfiles = resp.data
  },
  methods: {
    getRoleString(role) {
      if(role == 0)
        return "Administrator"
      else if(role == 1)
        return "Manager"
      else if(role == 2)
        return "Coach"
      else
        return "Customer"
    },
    incrementedNum() {
      this.profilesNum = this.profilesNum + 1
      return this.profilesNum.toString()
    },
    searchProfiles() {
      axios.post('http://localhost:8081/sarchProfiles', this.input)
          .then(response => (alert(response.data),
                  this.filteredProfiles = response.data,
                  alert(this.filteredProfiles)))
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