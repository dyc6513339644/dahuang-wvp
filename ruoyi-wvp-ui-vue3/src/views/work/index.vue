<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            @click="handleSave"
        >保存
        </el-button>
      </el-col>
      <el-button
          type="danger"
          plain
          @click="handleCleanUp"
      >清除
      </el-button>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24" :xs="24" :sm="24" :md="4" :lg="4" :xl="4">
        <el-card>
          <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane label="GB" name="GB">
              <div class="top">
                <div>通道列表</div>
                <div>
                  <el-switch
                      v-model="activeValue"
                      active-text="行政区划"
                      inactive-text="业务分组"
                      @change="onSwitch"
                  />
                </div>
              </div>
              <div>
                <el-tree
                    :data="treeData"
                    :props="defaultProps"
                    lazy
                    :load="loadNode"
                    @node-click="handleNodeClick"/>
              </div>
            </el-tab-pane>

            <el-tab-pane label="ONVIF" name="ONVIF">
              <InfiniteList
                  style="margin-top: 20px"
                  v-if="listDevice.length >0"
                  :data="listDevice"
                  :width="'100%'"
                  :height="'100%'"
                  :itemSize="40"
                  v-slot="{ item, index }"
              >
                <div style="cursor: pointer">
                  <el-tag @click="deviceClick(item)" style="width: 100%;" size="large"
                          :type="selectDeviceId === item.id ? 'success' : ''">
                    {{ item.name }}
                  </el-tag>

                </div>
              </InfiniteList>

              <el-empty v-if="listDevice.length === 0" :image-size="50" description="暂无数据"/>
            </el-tab-pane>

            <el-tab-pane label="RTSP" name="RTSP">
              <InfiniteList
                  style="margin-top: 20px"
                  v-if="listDevice.length >0"
                  :data="listDevice"
                  :width="'100%'"
                  :height="'100%'"
                  :itemSize="40"
                  v-slot="{ item, index }"
              >
                <div style="cursor: pointer">
                  <el-tag @click="deviceClick(item)" style="width: 100%;" size="large"
                          :type="selectDeviceId === item.id ? 'success' : ''">
                    {{ item.name }}
                  </el-tag>

                </div>
              </InfiniteList>

              <el-empty v-if="listDevice.length === 0" :image-size="50" description="暂无数据"/>
            </el-tab-pane>

            <el-tab-pane label="IUSP" name="IUSP">
              <InfiniteList
                  style="margin-top: 20px"
                  v-if="listDevice.length >0"
                  :data="listDevice"
                  :width="'100%'"
                  :height="'100%'"
                  :itemSize="40"
                  v-slot="{ item, index }"
              >
                <div style="cursor: pointer">
                  <el-tag @click="deviceClick(item)" style="width: 100%;" size="large"
                          :type="selectDeviceId === item.deviceId ? 'success' : ''">
                    {{ item.name }}
                  </el-tag>

                </div>
              </InfiniteList>

              <el-empty v-if="listDevice.length === 0" :image-size="50" description="暂无数据"/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
      <el-col :span="24" :xs="24" :sm="24" :md="20" :lg="20" :xl="20">
        <el-card>
          <div class="flex">
            分屏:
            <svg-icon :class="['flex-icon', { active: model === 1 }]"
                      icon-class="splitOne" @click="spiltIndex(1)" class="flex-icon"/>
            <svg-icon :class="['flex-icon', { active: model === 4 }]"
                      icon-class="splitFour" @click="spiltIndex(4)" class="flex-icon"/>
            <svg-icon :class="['flex-icon', { active: model === 6 }]"
                      icon-class="splitSix" @click="spiltIndex(6)" class="flex-icon"/>
            <svg-icon :class="['flex-icon', { active: model === 9 }]"
                      icon-class="splitNine" @click="spiltIndex(9)" class="flex-icon"/>
          </div>

          <div style="display: flex; flex-wrap: wrap; margin-top: 20px;position: relative">
            <div
                :id="'video' + index"
                v-for="(item, index) in splitLayouts[splitShow]"
                :key="index"
                :style="getCellStyle(splitShow)"
                style="border: 3px solid #409EFF;margin: 1px;"
                :class="['player-cell', { active: activePlayerIndex === index }]"
                @click="setActivePlayer(index)">
              <div v-if="item.data" style="position: absolute;z-index: 999;top: 5px;right: 20px;color: #F56C6C;">
                <el-icon @click="deleteVideo(index)"><Delete /></el-icon>
              </div>
              <CusPlayer v-if="item.type === 'GB'" :ref="'video' + index"/>
              <video v-if="item.type === 'ONVIF'" :id="'rtspVideo' + index"
                     muted
                     playsinline
                     controls
                     :style="'width:'+item.data.width+'px;height:'+item.data.height+'px'"></video>
              <video v-if="item.type === 'RTSP'" :id="'rtspVideo' + index"
                     muted
                     playsinline
                     controls
                     :style="'width:'+item.data.width+'px;height:'+item.data.height+'px'"></video>
              <video v-if="item.type === 'IUSP'" :id="'rtspVideo' + index"
                     muted
                     playsinline
                     controls
                     :style="'width:'+item.data.width+'px;height:'+item.data.height+'px'"></video>
            </div>
          </div>

        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="WVPLive">
import {queryForTree} from "@/api/wvp/region";
import {queryListByCivilCode, queryListByParentId, sendDevicePush} from "@/api/wvp/channel.js";
import {queryForTree as groupQueryForTree} from "@/api/wvp/group.js";
import CusPlayer from "@/components/flv/CusPlayer.vue";
import {start as playPush} from "@/api/wvp/push.js";
import {start as playProxy} from "@/api/wvp/proxy.js";
import {deviceList as onvifDeviceList} from "../../api/onvif/device.js";
import {rtspDeviceList} from "../../api/rtsp/RtspDevice.js";
import {lsupDeviceList} from "../../api/isup/lsupDevice.js";
import InfiniteList from 'vue3-infinite-list';
import layouts from "./layouts.js";
import {getConfigKey} from "../../api/system/config.js";
import {listWork, updateWork} from "../../api/system/work.js";

const listDevice = ref([])
const activeName = ref('GB')
const selectDeviceId = ref(null);
const videoWidth = ref(null);
const videoHeight = ref(null);

const {proxy} = getCurrentInstance();

const queryParams = ref({
  pageNum: 1,
  pageSize: 200,
})

const video = ref(null);
const webRtcServer = ref();
const treeData = ref([]);
const rtspAddress = ref('');

const defaultProps = {
  children: 'children',
  label: 'name',
  isLeaf: 'leaf'
};

const splitLayouts = ref(JSON.parse(JSON.stringify(layouts)));

async function onSwitch(e) {
  if (activeValue.value) {
    await getTreeData();
  } else {
    await getGroupQueryForTree();
  }
}

const loadNode = async (node, resolve) => {
  if (node.level === 0) {
    return resolve(treeData.value);
  } else if (node.level === 1) {
    return resolve(treeData.value[node.level - 1].children);
  } else if (node.level === 2) {
    if (activeValue.value) {
      queryParams.value.civilCode = node.data.deviceId;
      const response = await queryListByCivilCode(queryParams.value);
      const children = response.rows.map(item => ({
        ...item,
        leaf: true,
        name: item.gbName
      }));
      resolve(children);
    } else {
      queryParams.value.groupDeviceId = node.data.deviceId;
      const response = await queryListByParentId(queryParams.value);
      const children = response.rows.map(item => ({
        ...item,
        leaf: true,
        name: item.gbName
      }));
      resolve(children);
    }
  }
};

const handleNodeClick = async (data) => {
  if (activePlayerIndex.value == null) {
    proxy.$modal.msgError("请先选择一个播放窗口");
    return
  }

  let layoutData = splitLayouts.value[splitShow.value][activePlayerIndex.value]
  if (activeName.value === 'GB') {
    layoutData.type = 'GB'
    selectDeviceId.value = data.deviceId;
    layoutData.data = data
  }

  if (data.dataType === 1) {
    if (data.gbDeviceId && data.gbParentId) {
      const params = {
        deviceId: data.gbParentId,
        channelId: data.gbDeviceId
      }
      const res = await sendDevicePush(params);

      const videoRef = proxy.$refs[`video${activePlayerIndex.value}`];
      if (videoRef && videoRef[0]) {
        videoRef[0].createPlayer(res.data.flv, 0);
      } else {
        proxy.$modal.msgError("请选择播放器");
      }
    } else {
      proxy.$modal.msgError('通道或设备不存在')
    }
  }

  if (data.dataType === 2) {
    const ans = await playPush({id: data.dataDeviceId});
    const videoRef = proxy.$refs[`video${activePlayerIndex.value}`];
    if (videoRef && videoRef[0]) {
      videoRef[0].createPlayer(ans.data.flv, 0);
    } else {
      proxy.$modal.msgError("请选择播放器");
    }
  }

  if (data.dataType === 3) {
    const ans = await playProxy({id: data.dataDeviceId});
    const videoRef = proxy.$refs[`video${activePlayerIndex.value}`];
    if (videoRef && videoRef[0]) {
      videoRef[0].createPlayer(ans.flv, 0);
    } else {
      proxy.$modal.msgError("请选择播放器");
    }
  }
};

const splitShow = ref(1)
const borderWidth = ref(2)
const activePlayerIndex = ref(null);
const model = ref(1);
const activeValue = ref(true);

function getCellStyle(splitMode) {
  model.value = splitMode;
  const style = {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#000000",
    boxSizing: "border-box",
  };

  if (splitMode === 1) {
    style.width = "100%";
    style.height = "600px";
  } else if (splitMode === 4) {
    style.width = "49.5%";
    style.height = "400px";
    style.margin = "-2px";
  } else if (splitMode === 6) {
    style.width = "49.5%";
    style.height = "300px";
    style.margin = "-2px";
  } else if (splitMode === 9) {
    style.width = "33%";
    style.height = "280px";
    style.margin = "-2px";
  }

  return style;
}

function setActivePlayer(index) {
  activePlayerIndex.value = index;

  const container = document.getElementById('video' + index);
  videoWidth.value = container.offsetWidth;
  videoHeight.value = container.offsetHeight;
}

async function getTreeData() {
  const res = await queryForTree();
  let data = [
    {
      name: "根资源组",
      children: []
    }
  ]
  data[0].children = proxy.handleTree(res.data, "id")
  treeData.value = data;
}

async function getGroupQueryForTree() {
  const res = await groupQueryForTree();
  let data = [
    {
      name: "根资源组",
      children: []
    }
  ]
  data[0].children = proxy.handleTree(res.data, "id")
  treeData.value = data;
}

function spiltIndex(index) {
  splitLayouts.value = JSON.parse(JSON.stringify(layouts));
  splitShow.value = index;
  activePlayerIndex.value = null;
  selectDeviceId.value = null;
}

const handleClick = (tab, event) => {
  nextTick(async () => {
    await getList();
  })
}

function getListWork() {
  listWork().then(async (res) => {
    if (res.data && res.data.layoutList) {
      splitLayouts.value = JSON.parse(res.data.layoutList);
      splitLayouts.value[Number(res.data.index)].forEach((item, index) => {
        if (item.type !== '') {
          if (item.type === 'ONVIF') {
            nextTick(() => {
              webRtcServer.value = new WebRtcStreamer('rtspVideo' + index, rtspAddress.value);
              webRtcServer.value.connect(item.data.url)
            })
          } else if (item.type === 'RTSP') {
            nextTick(() => {
              webRtcServer.value = new WebRtcStreamer('rtspVideo' + index, rtspAddress.value);
              webRtcServer.value.connect(item.data.url)
            })
          } else if (item.type === 'IUSP') {
            nextTick(() => {
              webRtcServer.value = new WebRtcStreamer('rtspVideo' + index, rtspAddress.value);
              webRtcServer.value.connect(item.data.url,index)
            })
          } else if (item.type === 'GB') {
            playGB(item.data,index)
          }
        }
      })
    }

    if (res.data && res.data.index) {
      splitShow.value = Number(res.data.index)
    }
  })
}

async function playGB(data,index) {
  if (data.dataType === 1) {
    if (data.gbDeviceId && data.gbParentId) {
      const params = {
        deviceId: data.gbParentId,
        channelId: data.gbDeviceId
      }
      const res = await sendDevicePush(params);
      await nextTick(() => {
        const videoRef = proxy.$refs[`video${index}`];
        if (videoRef && videoRef[0]) {
          videoRef[0].createPlayer(res.data.flv, 0);
        } else {
          proxy.$modal.msgError("请选择播放器");
        }
      })
    } else {
      proxy.$modal.msgError('通道或设备不存在')
    }
  }

  if (data.dataType === 2) {
    const ans = await playPush({id: data.dataDeviceId});
    await nextTick(() => {
      const videoRef = proxy.$refs[`video${index}`];
      if (videoRef && videoRef[0]) {
        videoRef[0].createPlayer(ans.data.flv, 0);
      } else {
        proxy.$modal.msgError("请选择播放器");
      }
    })
  }

  if (data.dataType === 3) {
    const ans = await playProxy({id: data.dataDeviceId});
    await nextTick(() => {
      const videoRef = proxy.$refs[`video${index}`];
      if (videoRef && videoRef[0]) {
        videoRef[0].createPlayer(ans.flv, 0);
      } else {
        proxy.$modal.msgError("请选择播放器");
      }
    })
  }
}

async function getList() {
  listDevice.value = []
  if (activeName.value === 'GB') {
    await getTreeData();
  } else if (activeName.value === 'ONVIF') {
    onvifDeviceList().then(res => {
      console.log(res)
      listDevice.value = res.data
    })
  } else if (activeName.value === 'RTSP') {
    rtspDeviceList().then(res => {
      console.log(res)
      listDevice.value = res.data
    })
  } else if (activeName.value === 'IUSP') {
    lsupDeviceList().then(res => {
      console.log(res)
      listDevice.value = res.data
    })
  }
}

function deviceClick(data) {
  if (activePlayerIndex.value == null) {
    proxy.$modal.msgError("请先选择一个播放窗口");
    return
  }

  let layoutData = splitLayouts.value[splitShow.value][activePlayerIndex.value]

  data.width = videoWidth.value;
  data.height = videoHeight.value - 10;

  if (activeName.value === 'ONVIF') {
    layoutData.type = 'ONVIF'
    selectDeviceId.value = data.id;

    nextTick(() => {
      webRtcServer.value = new WebRtcStreamer('rtspVideo' + activePlayerIndex.value, rtspAddress.value);
      webRtcServer.value.connect(data.url)
    })
  } else if (activeName.value === 'RTSP') {
    layoutData.type = 'RTSP'
    selectDeviceId.value = data.id;

    nextTick(() => {
      webRtcServer.value = new WebRtcStreamer('rtspVideo' + activePlayerIndex.value, rtspAddress.value);
      webRtcServer.value.connect(data.url)
    })
  } else if (activeName.value === 'IUSP') {
    layoutData.type = 'IUSP'
    selectDeviceId.value = data.deviceId;

    nextTick(() => {
      webRtcServer.value = new WebRtcStreamer('rtspVideo' + activePlayerIndex.value, rtspAddress.value);
      webRtcServer.value.connect(data.url)
    })
  }

  layoutData.data = data
}

onUnmounted(() => {
  if (webRtcServer.value) {
    webRtcServer.value.disconnect()
    webRtcServer.value = null
  }
})

function getConfigKeyFun() {
  getConfigKey("sys_rtsp_address").then((res) => {
    rtspAddress.value = res.msg
  })
}

function handleSave(){
  updateWork({
    layoutList: JSON.stringify(splitLayouts.value),
    index: splitShow.value
  }).then(()=>{
    proxy.$modal.msgSuccess("保存成功");
  })
}

function handleCleanUp(){
  proxy.$modal.confirm('是否清除显示的分屏？').then(function () {
    splitLayouts.value = JSON.parse(JSON.stringify(layouts));
    return
  }).then(() => {
    handleSave()
  }).catch(() => {
  });

}

function deleteVideo(index){
  proxy.$modal.confirm('是否删除该分屏').then(function () {
    let layoutData = splitLayouts.value[splitShow.value][index]
    layoutData.data = null
    layoutData.type = ''
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

onMounted(async () => {
  await getConfigKeyFun()
  await getList()
  await getListWork()
})

</script>

<style scoped>
.top {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex {
  width: 100%;
  display: flex;
  align-items: center;
}

.player-cell {
  position: relative;
  transition: border-color 0.3s ease;
}

.player-cell:hover {
  cursor: pointer;
}

.player-cell.active {
  border-color:  #67C23A !important;
}

.flex-icon {
  margin-left: 10px;
}

.flex-icon {
  margin-left: 10px;
  cursor: pointer;
  font-size: 20px;
  transition: color 0.3s ease, transform 0.3s ease;
}

.flex-icon.active {
  color: #409EFF;
  transform: scale(1.2);
}
</style>


