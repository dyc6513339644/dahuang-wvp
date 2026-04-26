<template>
  <div style="background-color: #000000;">
    <RtspVideo :rtspVideo="rtsp" :width="width" :height="height"/>
  </div>
</template>

<script setup name="Hikvision">
import RtspVideo from '@/components/rtspVideo/index.vue';
import {defineProps} from "vue";

const props = defineProps({
  rtsp: {
    type: String,
    required: true,
    default: ''
  },
  width: {
    type: String,
    required: true,
    default: '800'
  },
  height: {
    type: String,
    default: '457'
  },
})
</script>

<style scoped>

</style>
