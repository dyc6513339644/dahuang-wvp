<template>
  <div>
    <svg-icon icon-class="large" @click="goto" />
  </div>
</template>

<script setup>
import {getToken} from "../../../utils/auth.js";
import {getConfigKey} from "../../../api/system/config.js";

const url = ref('');

function getConfigKeyFun() {
  getConfigKey("sys_large_address").then(response => {
    url.value = response.msg + '?token='+getToken()
  })
}

function goto() {
  window.open(url.value)
}

getConfigKeyFun()

</script>
