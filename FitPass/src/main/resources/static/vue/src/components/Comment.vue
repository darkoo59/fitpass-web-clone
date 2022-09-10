<template>
  <div>
    <div class="container">
      <div class="text-white row">
        {{ $props.comment.rating }}
      </div>
      <div class="row">
        <div class="text-white col">
          {{ $props.comment.username }}
        </div>
        <div class="text-white col">
          {{ $props.comment.text }}
        </div>
        <div class="row" v-if="pendingComment">
          <button class="btn-success col" @click="acceptComment">Accept</button>
          <button class="btn-secondary col" @click="rejectComment">Reject</button>
        </div>
      </div>
      <div class="row" v-if="$props.administrator" @click="this.delete">
        <button class="btn-close"></button>
      </div>
    </div>
  </div>
</template>

<script>

import axios from "axios";

export default {
  name: 'Comment',
  props: {
    comment: Object,
    administrator: Boolean
  },
  data() {
    return {
      pendingComment: false
    }
  },
  mounted() {
    if (this.comment.status === '0' && this.$props.administrator) {
      this.pendingComment = true
    }
  },
  methods: {
    async delete() {
      await axios.delete(this.$port.value + '/comment/delete/' + this.comment.id)
      this.$emit('deleted', this.comment.id)
    },
    async acceptComment() {
      await axios.put(this.$port.value + '/comment/accept/' + this.comment.id)
      this.pendingComment = false
    },
    async rejectComment() {
      await axios.put(this.$port.value + '/comment/reject/' + this.comment.id)
      this.pendingComment = false
    }
  }
}
</script>