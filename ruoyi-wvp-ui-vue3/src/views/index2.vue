<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>设备总览</span>
            </div>
          </template>
          <LeftTop/>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>国标统计</span>
            </div>
          </template>
          <TopCenter/>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>CPU</span>
            </div>
          </template>
          <RightTop ref="RightTopRef"/>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>网络</span>
            </div>
          </template>
          <LeftBottom ref="LeftBottomRef"/>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>内存</span>
            </div>
          </template>
          <BottomCenter ref="BottomCenterRef"/>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>磁盘</span>
            </div>
          </template>
          <RightBottom ref="RightBottomRef"/>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import LeftTop from "./components/home/left-top.vue"
import TopCenter from "./components/home/top-center.vue"
import RightTop from "./components/home/right-top.vue"
import LeftBottom from "./components/home/left-bottom.vue"
import BottomCenter from "./components/home/bottom-center.vue"
import RightBottom from "./components/home/right-bottom.vue"
import {serverInfo} from "../api/index.js";

const timer = ref({})
const RightTopRef = ref(null)
const LeftBottomRef = ref(null)
const BottomCenterRef = ref(null)
const RightBottomRef = ref(null)

onMounted(()=>{
  nextTick(()=>{
    serverInfoFun()
    timer.value = setInterval(()=>{
      serverInfoFun()
    }, 2000)
  })
})

function serverInfoFun(){
  serverInfo()
      .then((res) => {
        RightTopRef.value.setData(res.data.cpu)
        LeftBottomRef.value.setData(res.data.net)
        BottomCenterRef.value.setData(res.data.mem)
        RightBottomRef.value.setData(res.data.disk)
      })
      .catch((err) => {

      });
}
</script>

<style scoped lang="scss">

</style>

