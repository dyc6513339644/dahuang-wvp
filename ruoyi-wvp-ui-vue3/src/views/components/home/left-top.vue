<template>
  <div style="width: 100%;height: 350px;" id="leftTopChart"></div>
</template>

<script setup name="LeftTop">
import * as echarts from 'echarts';
import {countDeviceNum} from "../../../api/index.js";

const myChart = ref({})

onMounted(()=>{
  countDeviceNum().then((res)=>{
    nextTick(() => {
      const chartDom = document.getElementById('leftTopChart');
      let chart = echarts.init(chartDom);
      myChart.value = chart
      let option;
      option = {
        tooltip: {
          trigger: 'item'
        },
        toolbox: {
          show: true,
          feature: {
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {

            type: 'pie',
            radius: '50%',
            data: [
              { value: res.data.totalGbNum, name: '国标设备数' },
              { value: res.data.totalIsupNum, name: 'iusp设备数' },
              { value: res.data.totalOnvifNum, name: 'onvif设备数' },
              { value: res.data.totalRtspNum, name: 'rtsp设备数' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      window.addEventListener("resize", resize);

      chart.setOption(option);
    })
  })
})


function resize() {
  myChart.value.resize();
}

onBeforeUnmount(() => {
  window.removeEventListener("resize",resize);
})
</script>

<style scoped>

</style>
