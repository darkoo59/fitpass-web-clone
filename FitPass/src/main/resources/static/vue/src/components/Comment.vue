<template>
  <div>
    <div class="text-white">
      {{ $props.comment.rating }}
    </div>
    <div class="text-white">
      {{ $props.comment.username}}
    </div>
    <div class="text-white">
      {{ $props.comment.text}}
    </div>
  </div>
</template>

<script>

export default {
  name: 'Comment',
  props: {
    comment: Object,
    username: String
  },
}
</script>