<template>
  <form v-on:submit.prevent="submitForm">
    <div class="content">
      <div class="row">
        <div class="col-lg-4">
        </div>
        <div class="col-lg-4">
          <div class="form-group p-3">
            <label class="text-white" for="exampleInputEmail1">Code</label>
            <input type="text" name="code" class="form-control" aria-describedby="emailHelp"
                   v-model="formData.code" placeholder="Enter code" required>
          </div>
          <div class="form-group p-3">
            <label class="text-white" for="exampleInputEmail1">Max usage</label>
            <input type="text" name="maxUsage" class="form-control" aria-describedby="emailHelp"
                   v-model="formData.maxUsage" placeholder="Enter max usage" required>
          </div>
          <div class="form-group p-3">
            <label class="text-white" for="exampleInputPassword1">Password</label>
            <input type="datetime-local" v-model="formData.validUntil" name="date" class="form-control" placeholder="Validity date" required>
          </div>
          <div class="form-group p-3">
            <label class="text-white" for="exampleInputEmail1">Code</label>
            <input type="text" name="percentage" class="form-control" aria-describedby="emailHelp"
                   v-model="formData.discountPercentage" placeholder="Enter discount percentage" required>
          </div>
          <button type="submit" class="btn btn-primary m-3">Submit</button>
        </div>
        <div class="col-lg-4">
        </div>
      </div>
    </div>
  </form>
</template>

<script>
import axios from "axios";
import moment from "moment"

export default {
  name: "AddPromoCodes",
  data: function() {
    return {
      formData: {
        code: '',
        validUntil: '',
        discountPercentage: '',
        maxUsage: ''
      },
      responseData: ''
    }
  },
  methods: {
      submitForm:function()  {
        if(moment(this.formData.validUntil) < moment())
          alert("Dont choose date from past")
        else if(isNaN(this.formData.discountPercentage))
          alert('Type number in percentage field')
        else if(isNaN(this.formData.maxUsage))
          alert('Type number in max usage field')
        else {
          axios
              .post(this.$port.value + '/addPromoCode',this.formData,{
                params: {code: this.formData.code,
                  validUntil: this.formData.validUntil,
                  discountPercentage: this.formData.discountPercentage,
                  maxUsage: this.formData.maxUsage
                }
              })
              .then(response => (
                  this.responseData = response.data,
                  this.isResponseGood())
              )
        }
      },
    isResponseGood() {
      if(this.responseData == '1'){
        alert('Code already exist')
      }else if(this.responseData == '0'){
        alert('Code successfully added')
      }
    }
  }
}
</script>

<style scoped>

</style>