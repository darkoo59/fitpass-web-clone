<template>
  <br>
  <div class="container" id="searchFilter">
    <div class="row">
      <div class="col-sm">
        <input type="text" v-model="filter.searchInput" placeholder="Search by name..." />
      </div>
      <select v-model="filter.location" class="form-select col-sm">
        <option v-for="city in cities" :key="city" :value="city">{{ city }}</option>
      </select>
      <select v-model="filter.type" class="form-select col-sm">
        <option v-for="type in types" :key="type" :value="type">{{ type }}</option>
      </select>
      <select v-model="filter.rating" class="form-select col-sm">
        <option selected>Rating</option>
        <option value="1">4.5 - 5.0</option>
        <option value="2">4.0 - 4.5</option>
        <option value="3">3.5 - 4.0</option>
        <option value="4">3.0 - 3.5</option>
        <option value="5">&lt;= 3.0</option>
      </select>
      <select v-model="filter.sort" class="form-select col-sm">
        <option selected>Sort by</option>
        <option value="1">Name: Ascending</option>
        <option value="2">Name: Descending</option>
        <option value="3">Avg. rating: Low to High</option>
        <option value="4">Avg. rating: High to Low</option>
      </select>
      <div class="col-sm">
        <button class="btn-primary" @click="searchFacilities()">
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
    <div class="col-md-3"  v-for="facility in filtered" :key="facility.name">
      <div class="card" style="width: 18rem;" @click="openFacility(facility)">
        <img v-bind:src="require('../../public/images/'+facility.logo)" class="card-img-top">
        <div class="card-body">
          <h5 class="card-title">{{ facility.name }}</h5>
          <p class="card-text">
            Type: {{ facility.type }} <br>
            Average rating: {{ facility.averageRating }} <br>
            Working hours: {{ facility.workHour.start.substring(0, facility.workHour.start.length-3) }} -
            {{ facility.workHour.end.substring(0, facility.workHour.end.length-3) }} <br>
            Location: {{ facility.location.address.streetAndNumber}}, {{facility.location.address.place }}
          </p>
        </div>
      </div>
    </div>
  </div>
  <div class="item error" v-if="filter.searchInput && !filtered.length">
    <p style="color:white;">No results found!</p>
  </div>

</template>

<script>
import axios from "axios";


export default {
  name: "SportsFacilities",
  data: function () {
    return {
      facilities: [],
      filtered: [],
      cities: [],
      types: [],
      filter: {
        searchInput: "",
        location: "Location",
        type: "Type",
        rating: "Rating",
        sort: "Sort by"
      }
    }
  },
   async mounted()
    {
      let resp = await axios.get('http://localhost:8081/sportsFacilities')
      this.facilities = resp.data
      this.filtered = resp.data
      this.cities = this.getCitiesFromResponse(resp.data)
      this.types = this.getTypesFromResponse(resp.data)
    },
  methods: {
    openFacility(facility) {
      alert(facility.id)
      this.$router.push({
        name: 'SportFacility',
        params: { id: facility.id }
      })
    },
    async searchFacilities() {
      let resp = await axios
          .post('http://localhost:8081/sportsFacilities/filter', this.filter)
      this.filtered = resp.data
    },
    getCitiesFromResponse(data) {
      let cities = ["Location"]
      for (const element of data) {
        if (cities.indexOf(element.location.address.place) === -1) {
          cities.push(element.location.address.place)
        }
      }
      return cities
    },
    getTypesFromResponse(data) {
      let types = ["Type"]
      for (const element of data) {
        if (types.indexOf(element.type) === -1) {
          types.push(element.type)
        }
      }
      return types
    },
    removeFilters() {
      this.filtered = this.facilities
      this.filter.searchInput = ""
      this.filter.location = "Location"
      this.filter.type = "Type"
      this.filter.rating = "Rating"
      this.filter.sort = "Sort by"
    }
  }
}

</script>

<style scoped>
</style>