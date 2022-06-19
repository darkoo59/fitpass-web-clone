<template>
  <div v-for="f in facilities" class="card" style="width: 18rem;">
    <img src={{f.logo}} class="card-img-top">
    <div class="card-body">
      <h5 class="card-title">{{f.name}}</h5>
      <p class="card-text">{{f.averageRatins}}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "SportsFacilities"
}
</script>

<style scoped>

</style>