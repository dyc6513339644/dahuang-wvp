<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属部门" prop="deptId">
        <el-tree-select style="width: 202px" v-model="queryParams.deptId" :data="enabledDeptOptions" :props="{ value: 'id', label: 'label', children: 'children' }" value-key="id" placeholder="请选择归属部门" check-strictly />
      </el-form-item>
      <el-form-item label="设备ID" prop="deviceId">
        <el-input
            v-model="queryParams.deviceId"
            placeholder="请输入设备ID"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入设备名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="IP地址" prop="ipAddress">
        <el-input
            v-model="queryParams.ipAddress"
            placeholder="请输入设备的IP地址"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="序列号" prop="deviceSerial">
        <el-input
            v-model="queryParams.deviceSerial"
            placeholder="请输入设备的序列号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择在线状态" style="width: 196px;"
                   default-first-option>
          <el-option label="在线" value="1"></el-option>
          <el-option label="离线" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['isup:lsupDevice:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['isup:lsupDevice:remove']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lsupDeviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="所属部门" align="center" prop="deptName"/>
      <el-table-column label="设备ID" align="center" prop="deviceId"/>
      <el-table-column label="设备名称" align="center" prop="name"/>
      <el-table-column label="通道" align="center" prop="channel"/>
      <el-table-column label="IP地址" align="center" prop="ipAddress"/>
      <el-table-column label="设备类型" align="center" prop="devType"/>
      <el-table-column label="设备的序列号" align="center" prop="deviceSerial"/>
      <el-table-column label="设备协议版本" align="center" prop="devProtocolVersion"/>
      <el-table-column label="用户名" align="center" prop="userName"/>
      <el-table-column label="密码" align="center" prop="password">
        <template #default="scope">
          <div class="password-container">
            <span v-if="!passwordVisibility[scope.row.id]">******</span>
            <span v-else>{{ scope.row.password }}</span>
            <el-icon class="eye-icon" @click="togglePasswordVisibility(scope.row.id)">
              <component :is="passwordVisibility[scope.row.id] ? 'Hide' : 'View'"/>
            </el-icon>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag v-if="scope.row.status === '1'" type="success">在线</el-tag>
          <el-tag v-if="scope.row.status === '0'" type="danger">离线</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleStartPlay(scope.row)"
                     v-hasPermi="['isup:lsupDevice:start']" v-if="scope.row.status === '1'">播放
          </el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['isup:lsupDevice:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['isup:lsupDevice:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改isup设备对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="lsupDeviceRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" disabled placeholder="请输入设备ID"/>
        </el-form-item>
        <el-form-item label="IP地址" prop="ipAddress">
          <el-input v-model="form.ipAddress" disabled placeholder="请输入设备的 IP 地址"/>
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-tree-select v-model="form.deptId" :data="enabledDeptOptions" :props="{ value: 'id', label: 'label', children: 'children' }" value-key="id" placeholder="请选择归属部门" check-strictly />
        </el-form-item>
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入设备名称"/>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" show-password/>
        </el-form-item>
        <el-form-item>
          <template #label>
            <span>
               <el-tooltip content="获取通道必须要输入用户名和密码才能获取" placement="top">
                  <el-icon><question-filled/></el-icon>
               </el-tooltip>
            </span>
          </template>
          <el-button type="primary" @click="getChannel">获取通道</el-button>
        </el-form-item>
        <el-form-item label="通道" prop="channel">
          <el-radio-group v-model="form.channel">
            <el-radio :value="item" v-for="(item,index) in digitalChannelList" :key-="index">通道-{{ item }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="设备协议版本" prop="devProtocolVersion">
          <el-input v-model="form.devProtocolVersion" disabled placeholder="请输入设备协议版本"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="播放视频" v-model="openPlay" width="835px" append-to-body>
      <div>
        <Hikvision :rtsp="videoUrl" v-if="openPlay"/>
      </div>
      <el-tabs v-if="checkPermi(['isup:lsupDevice:ptzCtrl'])" v-model="tabActiveName" type="card" :stretch="true"
               style="margin-top: 10px;">
        <el-tab-pane label="云台控制" name="control">
          <div style="display: grid; grid-template-columns: 240px auto; height: 180px; overflow: auto">
            <!-- 左侧控制区域 -->
            <div style="display: grid; grid-template-columns: 100px auto;">
              <!-- 方向控制 -->
              <div class="control-wrapper">
                <div class="control-btn control-top" @mousedown="ptzCamera(3)">
                  <el-icon class="icon">
                    <CaretTop/>
                  </el-icon>
                  <div class="control-inner-btn control-inner"></div>
                </div>
                <div class="control-btn control-left" @mousedown="ptzCamera(2)">
                  <el-icon class="icon">
                    <CaretLeft/>
                  </el-icon>
                  <div class="control-inner-btn control-inner"></div>
                </div>
                <div class="control-btn control-bottom" @mousedown="ptzCamera(4)">
                  <el-icon class="icon">
                    <CaretBottom/>
                  </el-icon>
                  <div class="control-inner-btn control-inner"></div>
                </div>
                <div class="control-btn control-right" @mousedown="ptzCamera(1)">
                  <el-icon class="icon">
                    <CaretRight/>
                  </el-icon>
                  <div class="control-inner-btn control-inner"></div>
                </div>
                <div class="control-round">
                  <div class="control-round-inner"><i class="fa fa-pause-circle"></i></div>
                </div>
                <!-- 速度控制 -->
                <div class="contro-speed" style="position: absolute; left: 4px; top: 112px; width: 100px;">
                  <el-slider v-model="controSpeed" :max="100"></el-slider>
                </div>
              </div>

              <!--              &lt;!&ndash; 变倍、聚焦、光圈控制 &ndash;&gt;-->
              <!--              <div>-->
              <!--                <div class="ptz-btn-box">-->
              <!--                  <div @mousedown="ptzCamera('zoomin')" @mouseup="ptzCamera('stop')" title="变倍+">-->
              <!--                    <el-icon class="control-zoom-btn" style="font-size: 24px;"><ZoomIn /></el-icon>-->
              <!--                  </div>-->
              <!--                  <div @mousedown="ptzCamera('zoomout')" @mouseup="ptzCamera('stop')" title="变倍-">-->
              <!--                    <el-icon class="control-zoom-btn" style="font-size: 24px;"><ZoomOut /></el-icon>-->
              <!--                  </div>-->
              <!--                </div>-->
              <!--                <div class="ptz-btn-box">-->
              <!--                  <div @mousedown="focusCamera('near')" @mouseup="focusCamera('stop')" title="聚焦+">-->
              <!--                    <i class="iconfont icon-bianjiao-fangda control-zoom-btn" style="font-size: 24px;"></i>-->
              <!--                  </div>-->
              <!--                  <div @mousedown="focusCamera('far')" @mouseup="focusCamera('stop')" title="聚焦-">-->
              <!--                    <i class="iconfont icon-bianjiao-suoxiao control-zoom-btn" style="font-size: 24px;"></i>-->
              <!--                  </div>-->
              <!--                </div>-->
              <!--                <div class="ptz-btn-box">-->
              <!--                  <div @mousedown="irisCamera('in')" @mouseup="irisCamera('stop')" title="光圈+">-->
              <!--                    <i class="iconfont icon-guangquan control-zoom-btn" style="font-size: 24px;"></i>-->
              <!--                  </div>-->
              <!--                  <div @mousedown="irisCamera('out')" @mouseup="irisCamera('stop')" title="光圈-">-->
              <!--                    <i class="iconfont icon-guangquan- control-zoom-btn" style="font-size: 24px;"></i>-->
              <!--                  </div>-->
              <!--                </div>-->
              <!--              </div>-->
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup name="LsupDevice">
import {checkPermi} from "@/utils/permission";
import {delLsupDevice, getLsupDevice, listLsupDevice, updateLsupDevice} from "@/api/isup/lsupDevice";
import {getDigitalChannel, ptzCtrl} from "../../../api/isup/lsupDevice.js";
import Hikvision from "@/components/Hikvision/index.vue";
import {deptTreeSelect} from "@/api/system/user";

const {proxy} = getCurrentInstance();

const lsupDeviceList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);
const daterangeUpdateTime = ref([]);
const passwordVisibility = ref({});

const digitalChannelList = ref([]);

const openPlay = ref(false);
const videoUrl = ref('');

const tabActiveName = ref('control');
const controSpeed = ref(5);

const deptOptions = ref(undefined);
const enabledDeptOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    deptId: null,
    deviceId: null,
    ipAddress: null,
    deviceSerial: null,
    status: null,
    name: null,
  },
  rules: {
    userName: [
      {required: true, message: "用户名不能为空", trigger: "blur"}
    ],
    password: [
      {required: true, message: "密码不能为空", trigger: "blur"}
    ],
    name: [
      {required: true, message: "请输入设备名称", trigger: "blur"}
    ],
    channel: [
      {required: true, message: "请选择通道", trigger: "change"}
    ],
    deptId: [{ required: true, message: "请选择所属部门", trigger: 'blur' }],
  },
});

const {queryParams, form, rules} = toRefs(data);

/** 查询isup设备列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  listLsupDevice(queryParams.value).then(response => {
    lsupDeviceList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 查询部门下拉树结构 */
function getDeptTree() {
  deptTreeSelect().then(response => {
    deptOptions.value = response.data;
    enabledDeptOptions.value = filterDisabledDept(JSON.parse(JSON.stringify(response.data)));
  });
};

/** 过滤禁用的部门 */
function filterDisabledDept(deptList) {
  return deptList.filter(dept => {
    if (dept.disabled) {
      return false;
    }
    if (dept.children && dept.children.length) {
      dept.children = filterDisabledDept(dept.children);
    }
    return true;
  });
};

const togglePasswordVisibility = (id) => {
  passwordVisibility.value[id] = !passwordVisibility.value[id];
};

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    deptId: null,
    deviceId: null,
    name: null,
    channel: null,
    dwSize: null,
    dwNetUnitType: null,
    firmwareVersion: null,
    ipAddress: null,
    port: null,
    deviceRes: null,
    devType: null,
    manufacture: null,
    userName: null,
    password: null,
    deviceSerial: null,
    reliableTransmission: null,
    websocketTransmission: null,
    supportRedirect: null,
    devProtocolVersion: null,
    sessionKey: null,
    res: null,
    marketType: null,
    luserId: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("lsupDeviceRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeUpdateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  digitalChannelList.value = []
  const _id = row.id || ids.value

  getLsupDevice(_id).then(response => {
    form.value = response.data;
    form.value.channel = null
    open.value = true;
    title.value = "修改isup设备";
  });
}

function handleStartPlay(row) {
  videoUrl.value = row.url;
  openPlay.value = true;
}

function getChannel() {
  if (!form.value.ipAddress) {
    proxy.$modal.msgError("请先填写IP地址");
  }
  if (!form.value.userName) {
    proxy.$modal.msgError("请先填写用户名");
  }
  if (!form.value.password) {
    proxy.$modal.msgError("请先填写密码");
  }
  digitalChannelList.value = []
  getDigitalChannel({
    ip: form.value.ipAddress,
    username: form.value.userName,
    password: form.value.password,
  }).then((res) => {
    digitalChannelList.value = res.data.map((item) => {
      return parseInt(item)
    })
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["lsupDeviceRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateLsupDevice(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除isup设备编号为"' + _ids + '"的数据项？').then(function () {
    return delLsupDevice(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/**
 * 云台控制
 */
function ptzCamera(direction) {
  ptzCtrl({direction, controSpeed: controSpeed.value, lUserID: 0})
}

getDeptTree();
getList();
</script>

<style scoped>
::v-deep(.el-icon) {
  height: auto !important;
}

.control-wrapper {
  position: relative;
  width: 100px; /* 6.25rem * 16 = 100px */
  height: 100px; /* 6.25rem * 16 = 100px */
  max-width: 100px; /* 6.25rem * 16 = 100px */
  max-height: 100px; /* 6.25rem * 16 = 100px */
  border-radius: 100%;
  margin-top: 24px; /* 1.5rem * 16 = 24px */
  margin-left: 8px; /* 0.5rem * 16 = 8px */
  float: left;
}

.control-btn {
  display: flex;
  justify-content: center;
  position: absolute;
  width: 44%;
  height: 44%;
  border-radius: 5px;
  border: 1px solid #78aee4;
  box-sizing: border-box;
  transition: all 0.3s linear;
}


.control-btn:hover {
  cursor: pointer;
}

.control-btn .icon {
  width: 100%;
  font-size: 20px;
  color: #78aee4;
  display: flex;
  justify-content: center;
  align-items: center;
}

.control-btn .icon:hover {
  cursor: pointer;
}

.control-zoom-btn:hover {
  cursor: pointer;
}

.control-round {
  position: absolute;
  top: 21%;
  left: 21%;
  width: 58%;
  height: 58%;
  background: #fff;
  border-radius: 100%;
}

.control-round-inner {
  position: absolute;
  left: 13%;
  top: 13%;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 70%;
  height: 70%;
  font-size: 40px;
  color: #78aee4;
  border: 1px solid #78aee4;
  border-radius: 100%;
  transition: all 0.3s linear;
}

.control-inner-btn {
  position: absolute;
  width: 60%;
  height: 60%;
  background: #fafafa;
}

.control-top {
  top: -12px; /* -8% of 100px ≈ -12px */
  left: 27%;
  transform: rotate(-45deg);
  border-radius: 5px 100% 5px 0;
}

.control-top .icon {
  transform: rotate(45deg);
  border-radius: 5px 100% 5px 0;
}

.control-top .control-inner {
  left: -1px;
  bottom: 0;
  border-top: 1px solid #78aee4;
  border-right: 1px solid #78aee4;
  border-radius: 0 100% 0 0;
}

.control-top .fa {
  transform: rotate(45deg) translateY(-7px);
}

.control-left {
  top: 27%;
  left: -12px; /* -8% of 100px ≈ -12px */
  transform: rotate(45deg);
  border-radius: 5px 0 5px 100%;
}

.control-left .icon {
  transform: rotate(-45deg);
}

.control-left .control-inner {
  right: -1px;
  top: -1px;
  border-bottom: 1px solid #78aee4;
  border-left: 1px solid #78aee4;
  border-radius: 0 0 0 100%;
}

.control-left .fa {
  transform: rotate(-45deg) translateX(-7px);
}

.control-right {
  top: 27%;
  right: -12px; /* -8% of 100px ≈ -12px */
  transform: rotate(45deg);
  border-radius: 5px 100% 5px 0;
}

.control-right .icon {
  transform: rotate(-45deg);
}

.control-right .control-inner {
  left: -1px;
  bottom: -1px;
  border-top: 1px solid #78aee4;
  border-right: 1px solid #78aee4;
  border-radius: 0 100% 0 0;
}

.control-right .fa {
  transform: rotate(-45deg) translateX(7px);
}

.control-bottom {
  left: 27%;
  bottom: -12px; /* -8% of 100px ≈ -12px */
  transform: rotate(45deg);
  border-radius: 0 5px 100% 5px;
}

.control-bottom .icon {
  transform: rotate(-45deg);
}

.control-bottom .control-inner {
  top: -1px;
  left: -1px;
  border-bottom: 1px solid #78aee4;
  border-right: 1px solid #78aee4;
  border-radius: 0 0 100% 0;
}

.control-bottom .fa {
  transform: rotate(-45deg) translateY(7px);
}

.trank {
  width: 80%;
  height: 180px;
  text-align: left;
  padding: 0 10%;
  overflow: auto;
}

.trankInfo {
  width: 80%;
  padding: 0 10%;
}

.el-dialog__body {
  padding: 10px 20px;
}

.ptz-btn-box {
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 0 32px; /* 2rem * 16 = 32px */
  height: 48px; /* 3rem * 16 = 48px */
  line-height: 64px; /* 4rem * 16 = 64px */
}
</style>
