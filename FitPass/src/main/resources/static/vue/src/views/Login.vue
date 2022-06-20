<template>
  <HomeHeader />
  <section class="vh-100 gradient-custom">
<!--  <form v-on:submit.prevent="submitForm" ref="form">-->

    <div :data-show="false">
    </div>
    <form v-on:submit.prevent="submitForm">
    <div class="content">
      <div class="row">
        <div class="col-lg-4">
        </div>
        <div class="col-lg-4">
          <div class="form-group p-3">
            <label for="exampleInputEmail1">Username</label>
            <input type="text" name="username" class="form-control" aria-describedby="emailHelp"
                   v-model="formData.username" placeholder="Enter username" required>
          </div>
          <div class="form-group p-3">
            <label for="exampleInputPassword1">Password</label>
            <input type="password" v-model="formData.password" name="password" class="form-control" placeholder="Password" required>
          </div>
          <button type="submit" class="btn btn-primary">Submit</button>
        </div>
        <div class="col-lg-4">
          </div>
      </div>
    </div>
  </form>

  </section>
</template>

<script>
  import axios from 'axios'
  import HomeHeader from "../components/HomeHeader"
  export default {
    components:{
      HomeHeader
    },
    data: function(){
      return{
        formData: {
          username: '',
          password:''
        },
        responseData: ''
        ,
        user : {
          name: '',
          surname: ''
        },
        modal: false
      }
    },
    methods: {
      submitForm:function()  {
        axios
            .post('http://localhost:8081/login',this.formData)
            .then(response => (this.Redirect(response.data)))
      },
      Redirect:function(responseData)
      {
        if(responseData === null){
          alert('Wrong credentials')
          console.log("uslojee")
        }else {
          this.$router.push('/user')
          console.log("uslojee2")
        }
      }
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