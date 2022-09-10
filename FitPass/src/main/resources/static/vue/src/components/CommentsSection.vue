<template>
  <div>
    <h4 class="text-white">Comments and ratings ({{ numberOfRatings }})</h4>
    <div>
      <div class="row">
        <p class="text-white col-auto"> {{ Math.round( averageRating * 100 + Number.EPSILON ) / 100  || ''}} </p>
        <Rating class="col"
            :read-only="true"
            :increment="0.01"
            :rating="averageRating"
            :star-size="30"
            :show-rating="false"/>
      </div>
      <Comment :comment="comment" :administrator="administrator" @deleted="deleteComment" v-for="comment in comments" :key="comment.id"></Comment>
    </div>
  </div>

</template>

<script>
import Comment from "@/components/Comment";
import Rating from "vue-star-rating"

export default {
  name: 'CommentsSection',
  props: {
    facility: Object,
    comments: Array,
    administrator: Boolean
  },
  components: {
    Comment,
    Rating
  },
  data() {
    return {
      averageRating: 0.0,
      numberOfRatings: 0
    }
  },
  beforeUpdate() {
    let avg = 0
    for (let comment of this.comments) {
      avg += +comment.rating
    }
    this.averageRating = avg / this.comments.length
    this.numberOfRatings = this.comments.length
  },
  methods: {
    deleteComment(id) {
      this.$emit('deleted', id)
    }
  }
}
</script>