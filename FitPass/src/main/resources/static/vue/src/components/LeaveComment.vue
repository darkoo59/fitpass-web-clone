<template>
  <textarea class="input-comment" v-model="comment.text" placeholder="Enter your comment here" required/>
  <div class="row">
    <Rating
        class="col"
        v-model:rating="comment.rating"
        :increment="1"
        :show-rating="false"
        :star-size="30"/>
    <button class="col-3" @click="onButtonClick">Submit</button>
  </div>
</template>

<script>
import Rating from "vue-star-rating"

export default {
  emits: ['clicked'],
  components: {
    Rating
  },
  data() {
    return {
      comment: {
        text: '',
        rating: 0
      }
    }
  },
  methods: {
    onButtonClick() {
      this.$emit('clicked', this.comment)
    }
  }
}
</script>

<style scoped>
  .input-comment {
    width: 400px;
    height: 100px;
    resize: none;
  }
</style>