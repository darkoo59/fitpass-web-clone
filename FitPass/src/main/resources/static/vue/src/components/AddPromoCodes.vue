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
          this.$toast.error("Dont choose date from past", {position: 'top', duration: 4000, maxToasts: 1})
        else if(isNaN(this.formData.discountPercentage))
          this.$toast.error("Type number in percentage field", {position: 'top', duration: 4000, maxToasts: 1})
        else if(isNaN(this.formData.maxUsage))
          this.$toast.error("Type number in max usage field", {position: 'top', duration: 4000, maxToasts: 1})
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
        this.$toast.error("Code already exists", {position: 'top', duration: 4000, maxToasts: 1})
      }else if(this.responseData == '0'){
        this.$toast.success("Code successfully added", {position: 'top', duration: 4000, maxToasts: 1})
      }
    }
  }
}
</script>

<style scoped>

</style>