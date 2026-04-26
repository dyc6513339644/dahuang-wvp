<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script setup>
const url = ref('http://doc.ry-wvp.xyz');

function goto() {
  window.open(url.value)
}
</script>
