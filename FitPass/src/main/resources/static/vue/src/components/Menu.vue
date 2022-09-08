<template>
  <!--COACH-->
  <div v-if="userRole === 'COACH'" class="header gradient-custom">
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container-fluid">
        <a class="navbar-brand text-white" href="#">FITPASS</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link active text-white" aria-current="page" href="#">Home</a>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/userInfo">
                My Profile
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/coachTrainings">
                My Trainings
              </router-link>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                CoachFeature3
              </a>
              <ul class="dropdown-menu " aria-labelledby="navbarDropdownMenuLink">
                <li><a class="dropdown-item" href="#">CoachFeature3a</a></li>
                <li><a class="dropdown-item" href="#">CoachFeature3b</a></li>
                <li><a class="dropdown-item" href="#">CoachFeature3c</a></li>
              </ul>
            </li>
          </ul>
        </div>
        <router-link to="/">
          <input class="btn btn-primary" type="button" @click="logout()" value="Logout">
        </router-link>
      </div>
    </nav>
  </div>

  <!--ADMINISTRATOR-->
  <div v-if="userRole === 'ADMINISTRATOR'" class="header gradient-custom">
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container-fluid">
        <a class="navbar-brand text-white" href="#">FITPASS</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
          <ul class="navbar-nav">
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/">
                Home
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/administratorCreateProfiles">
                Create profile
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/allProfiles">
                All profiles
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/createFacility">
                Create facility
              </router-link>
            </li>
            <li>
              <router-link class="nav-link text-white" to="/userInfo">
                My Profile
              </router-link>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                AdministratorFeature3
              </a>
              <ul class="dropdown-menu " aria-labelledby="navbarDropdownMenuLink">
                <li><a class="dropdown-item" href="#">AdministratorFeature3a</a></li>
                <li><a class="dropdown-item" href="#">AdministratorFeature3b</a></li>
                <li><a class="dropdown-item" href="#">AdministratorFeature3c</a></li>
              </ul>
            </li>
          </ul>
        </div>
        <router-link to="/">
          <input class="btn btn-primary" type="button" @click="logout()" value="Logout">
        </router-link>
      </div>
    </nav>
  </div>

  <!--MANAGER-->
  <div v-if="userRole === 'MANAGER'" class="header gradient-custom">
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container-fluid">
        <a class="navbar-brand text-white" href="#">FITPASS</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link active text-white" aria-current="page" href="#">Home</a>
            </li>
            <li>
              <router-link class="nav-link text-white" to="/userInfo">
                My Profile
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/managerTrainings">
                Trainings
              </router-link>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                ManagerFeature3
              </a>
              <ul class="dropdown-menu " aria-labelledby="navbarDropdownMenuLink">
                <li><a class="dropdown-item" href="#">ManagerFeature3a</a></li>
                <li><a class="dropdown-item" href="#">ManagerFeature3b</a></li>
                <li><a class="dropdown-item" href="#">ManagerFeature3c</a></li>
              </ul>
            </li>
          </ul>
        </div>
        <router-link to="/">
          <input class="btn btn-primary" type="button" @click="logout()" value="Logout">
        </router-link>
      </div>
    </nav>
  </div>
  <!--CUSTOMER-->
  <div v-if="userRole === 'CUSTOMER'" class="header gradient-custom">
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container-fluid">
        <a class="navbar-brand text-white" href="#">FITPASS</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
          <ul class="navbar-nav">
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/">
                Home
              </router-link>
            </li>
            <li>
              <router-link class="nav-link text-white" to="/userInfo">
                My Profile
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/myTrainings">
                My trainings
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/membership">
                Membership
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link text-white" to="/trainNow">
                Train now
              </router-link>
            </li>
          </ul>
        </div>
        <router-link to="/">
          <input class="btn btn-primary" type="button" @click="logout()" value="Logout">
        </router-link>
      </div>
    </nav>
  </div>
</template>

<script>
export default {
  name: "Menu",
  methods:{
    logout(){
      window.localStorage.removeItem('token')
      window.localStorage.removeItem('userRole')
      if(this.$route.name === 'Home'){
        this.$router.go()
      }
      else
        this.$router.push('')
    }
  },
  data: function(){
    return{
      userRole: window.localStorage.getItem('userRole')
    }
  }
}
</script>

<style scoped>
.gradient-custom {
  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to bottom right, #000428, #2c3e50);

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to bottom right, #000428, #2c3e50);
}
</style>