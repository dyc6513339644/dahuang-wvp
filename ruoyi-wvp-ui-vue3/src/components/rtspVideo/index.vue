<template>
  <div>
    <video id="rtspVideo" muted playsinline controls :style="'width:'+width+'px;height:'+height+'px'"></video>
  </div>
</template>

<script setup>
import {defineProps} from 'vue';
import {getConfigKey} from "../../api/system/config.js";

const props = defineProps({
  rtspVideo: {
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

const webRtcServer = ref();
const initWebRtcServer = async () => {
  getConfigKey("sys_rtsp_address").then((res) => {
    nextTick(() => {
      webRtcServer.value = new WebRtcStreamer('rtspVideo', res.msg);
      webRtcServer.value.connect(props.rtspVideo)
    })
  })
}

onUnmounted(() => {
  webRtcServer.value.disconnect()
  webRtcServer.value = null
})
initWebRtcServer();
</script>

<style scoped>

</style>
