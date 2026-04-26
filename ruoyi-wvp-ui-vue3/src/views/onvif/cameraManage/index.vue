<template>
  <div class="app-container">
    <div style="margin-bottom: 10px;">
      <el-alert title="onvif云台需要咨询厂商确定设备是否支持！ 如遇问题加群咨询。微信1: chenbai0511 备注：ruoyi-wvp；微信2: NYHHDWGZL 备注：ruoyi-wvp" type="success"/>
    </div>

    <el-card>
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
        <el-form-item label="所属部门" prop="deptId">
          <el-tree-select style="width: 202px" v-model="queryParams.deptId" :data="enabledDeptOptions" :props="{ value: 'id', label: 'label', children: 'children' }" value-key="id" placeholder="请选择归属部门" check-strictly />
        </el-form-item>
        <el-form-item label="ip" prop="ip">
          <el-input
              v-model="queryParams.ip"
              placeholder="请输入ip"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input
              v-model="queryParams.name"
              placeholder="请输入名称"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="设备厂商" prop="firm">
          <el-input
              v-model="queryParams.firm"
              placeholder="请输入设备厂商"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="m-1">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAdd"
              v-hasPermi="['onvif:device:add']"
          >新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="success"
              plain
              icon="Edit"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['onvif:device:edit']"
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
              v-hasPermi="['onvif:device:remove']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="warning"
              plain
              icon="Download"
              @click="handleExport"
              v-hasPermi="['onvif:device:export']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange" border>
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="所属部门" align="center" prop="deptName"/>
        <el-table-column label="名称" align="center" prop="name"/>
        <el-table-column label="ip" align="center" prop="ip"/>
        <el-table-column label="设备厂商" align="center" prop="firm" width="150"/>
        <el-table-column label="设备型号" align="center" prop="model" width="200"/>
        <el-table-column label="固件版本" align="center" prop="firmwareVersion" width="200"/>
        <el-table-column label="用户名" align="center" prop="userName" width="100"/>
        <el-table-column label="密码" align="center" prop="password" width="150">
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
        <el-table-column label="默认播放地址" align="center" prop="url" width="300">
          <template #default="scope">
            <el-text style="cursor: pointer;" @click="copyToClipboard(scope.row.url)" type="primary">{{
                scope.row.url
              }}
            </el-text>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary"
                       text
                       icon="Position"
                       @click="viewUrls(scope.row.streamUris)">
              全部地址
            </el-button>
            <el-button link type="primary" icon="View" @click="handleView(scope.row)"
                       v-hasPermi="['onvif:device:play']">播放
            </el-button>
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                       v-hasPermi="['onvif:device:edit']">修改
            </el-button>
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                       v-hasPermi="['onvif:device:remove']">删除
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
    </el-card>
    <!-- 添加或修改onvif 设备对话框 -->
    <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
      <el-form ref="deviceRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <el-tree-select v-model="form.deptId" :data="enabledDeptOptions" :props="{ value: 'id', label: 'label', children: 'children' }" value-key="id" placeholder="请选择归属部门" check-strictly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="userName">
              <el-input v-model="form.name" placeholder="请输入设备名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="ip" prop="ip">
              <el-input v-model="form.ip" placeholder="请输入ip"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入密码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="默认播放地址" prop="url">
              <el-select
                  v-model="form.url"
                  class="m-2"
                  placeholder="请选择直播流地址"
                  size="large"
              >
                <el-option
                    v-for="item in form.streamUris"
                    :key="item"
                    :label="item"
                    :value="item"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备厂商" prop="firm">
              <el-input v-model="form.firm" placeholder="请输入设备厂商" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入设备型号" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="固件版本" prop="firmwareVersion">
              <el-input v-model="form.firmwareVersion" placeholder="请输入固件版本" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看直播流地址 对话框 -->
    <el-dialog :title="title" v-model="showUrl" width="800px" append-to-body>
      <div v-for="(item,index) in urls" :key="index" style="margin-bottom: 10px;">
        <el-text type="primary" style="cursor: pointer;" @click="copyToClipboard(item)">{{ item }}</el-text>
      </div>
    </el-dialog>

    <!-- 播放弹窗 对话框 -->
    <el-dialog :title="title" v-model="showPaly" width="835px" @opened="openedPaly" @close="closePaly">
      <div style="background: #000000">
        <Hikvision :rtsp="playerOptions.rtspURL" v-if="showPaly"/>
      </div>
      <div style="margin-top: 10px;">
        <el-row :gutter="20">
          <el-col :span="24">
            <div style="font-weight: bold;">通道：
              <el-select
                  v-model="token"
                  placeholder="请选择通道"
                  style="width: 240px"
              >
                <el-option
                    v-for="item in optionsToken"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </div>
          </el-col>

          <el-tabs v-model="playTabsName" v-if="token" style="width: 100%;" @tab-click="handleplayTabsClick">
            <el-tab-pane label="云台" name="absolute">
              <el-row :gutter="20">
                <el-col :span="12" style="margin-top: 20px;">
                  <div style="font-weight: bold;">绝对位置移动：</div>
                  <div style="display: flex; align-items: center; margin-top: 10px;">
                    VX
                    <el-input-number @change="handleAbsolute" v-model="absoluteNumX" :precision="2" :step="0.1" :max="1"
                                     style="margin-left: 10px; width: 150px;"></el-input-number>
                  </div>
                  <div style="display: flex; align-items: center; margin-top: 10px;">
                    VY
                    <el-input-number @change="handleAbsolute" v-model="absoluteNumY" :precision="2" :step="0.1" :max="1"
                                     style="margin-left: 10px; width: 150px;"></el-input-number>
                  </div>
                </el-col>
                <el-col :span="12" style="margin-top: 20px;" v-if="token">
                  <div style="font-weight: bold;">连续移动：</div>
                  <div style="display: flex;">
                    <div style="margin-top: 10px;">
                      <div style="display: flex; align-items: center;">
                        VX
                        <el-input-number v-model="continuousNumX" :precision="2" :step="0.1" :max="1"
                                         style="margin-left: 10px; width: 150px;"></el-input-number>
                      </div>
                      <div style="display: flex; align-items: center; margin-top: 10px;">
                        VY
                        <el-input-number v-model="continuousNumY" :precision="2" :step="0.1" :max="1"
                                         style="margin-left: 10px; width: 150px;"></el-input-number>
                      </div>
                    </div>
                    <div style="margin-top: 10px; margin-left: 15px;">
                      <el-button type="success" @click="handleContinuous">开始移动</el-button>
                      <el-button type="danger" @click="handleContinuousStop">停止移动</el-button>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="预置点" name="presets">
              <div style="display: flex; align-items: center;">
                <el-input style="width: 300px; margin-right: 10px;" v-model="presetName" placeholder="请输入预置点名称" />
                <el-button type="primary" @click="presetAdd">添 加</el-button>
              </div>
              <div style="margin-top: 10px;">
                <el-tag v-loading="showPresets" closable @close="closePresets(item)" @click="gotoPresets(item)" style="margin-right: 10px; cursor: pointer;" v-for="item in optionsPresetsToken" :key="item.token">{{item.name}}</el-tag>
              </div>

            </el-tab-pane>
          </el-tabs>


        </el-row>
      </div>
    </el-dialog>

    <el-dialog :title="title" v-model="openAdd" width="800px" append-to-body>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form ref="probeRef" :model="probeForm" :rules="rules" label-width="120px">
            <el-form-item label="所属部门" prop="deptId">
              <el-tree-select v-model="form.deptId" :data="enabledDeptOptions" :props="{ value: 'id', label: 'label', children: 'children' }" value-key="id" placeholder="请选择归属部门" check-strictly />
            </el-form-item>
            <el-form-item label="设备名称" prop="name">
              <el-input v-model="probeForm.name" placeholder="请输入设备名称"/>
            </el-form-item>
            <el-form-item label="ONVIF IP" prop="ip">
              <el-input v-model="probeForm.ip" placeholder="请输入ONVIF IP"/>
            </el-form-item>
            <el-form-item label="ONVIF用户名" prop="username">
              <el-input v-model="probeForm.username" placeholder="请输入ONVIF用户名"/>
            </el-form-item>
            <el-form-item label="ONVIF密码" prop="password">
              <el-input v-model="probeForm.password" placeholder="请输入ONVIF密码"/>
            </el-form-item>
          </el-form>
          <div style="display: flex; justify-content: space-around;">
            <el-button type="primary" @click="submitDetectionForm"  v-hasPermi="['onvif:service:getInfo']">探 测</el-button>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form ref="resultRef" :model="resultForm" :rules="rulesResult" label-width="120px">
            <el-form-item label="厂商" prop="firm">
              <el-input v-model="resultForm.firm" placeholder="请输入厂商" disabled/>
            </el-form-item>
            <el-form-item label="型号" prop="model">
              <el-input v-model="resultForm.model" placeholder="请输入型号" disabled/>
            </el-form-item>
            <el-form-item label="固件版本" prop="firmwareVersion">
              <el-input v-model="resultForm.firmwareVersion" placeholder="请输入固件版本" disabled/>
            </el-form-item>
            <el-form-item label="直播流地址" prop="url">
              <el-select
                  v-model="resultForm.url"
                  class="m-2"
                  placeholder="请选择直播流地址"
                  size="large"
                  :disabled="disabledAdd"
              >
                <el-option
                    v-for="item in resultForm.streamUris"
                    :key="item"
                    :label="item"
                    :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <div style="display: flex; justify-content: space-around;">
              <el-button type="primary" @click="submitResultForm" :disabled="disabledAdd" v-hasPermi="['onvif:device:add']">添 加</el-button>
            </div>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup name="CameraManage">
import {
  absoluteMove,
  addDevice,
  addPreset,
  continuousMove,
  continuousMoveStop,
  delDevice,
  getChannelToken,
  getDevice,
  getGotoPreset,
  getPresetList,
  listDevice,
  removePreset,
  updateDevice
} from "@/api/onvif/device";
import Hikvision from "@/components/Hikvision/index.vue";
import {ref} from "vue";
import {probe} from "../../../api/onvif/addCamera.js";
import {deptTreeSelect} from "@/api/system/user";

const {proxy} = getCurrentInstance();

const deviceList = ref([]);
const open = ref(false);
const openAdd = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const showPresets = ref(false);
const total = ref(0);
const title = ref("");
const brand = ref("");
const token = ref("");
const playTabsName = ref("absolute");
const presetName = ref("");
const urls = ref({});
const showUrl = ref(false);
const showPaly = ref(false);
const video = ref(null);
const playerOptions = ref({});
const dahuaPlayer = ref(null);
const absoluteNumX = ref(0.00);
const absoluteNumY = ref(0.00);
const continuousNumX = ref(0.00);
const continuousNumY = ref(0.00);
const streamInfo = ref({});
const optionsToken = ref([]);
const optionsPresetsToken = ref([]);

const deptOptions = ref(undefined);
const enabledDeptOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    deptId: null,
    ip: null,
    userName: null,
    password: null,
    url: null,
    firm: null,
    model: null,
    firmwareVersion: null,
    streamUris: null,
  },
  rules: {
    id: [{required: true, message: "编号不能为空", trigger: "blur"}],
    ip: [{required: true, message: "ip不能为空", trigger: "blur"}],
    username: [{required: true, message: "用户名不能为空", trigger: "blur"}],
    password: [{required: true, message: "密码不能为空", trigger: "blur"}],
    deptId: [{ required: true, message: "请选择所属部门", trigger: 'blur' }],
    name: [{ required: true, message: "名称不能为空", trigger: 'blur' }],
  },
  probeForm:{
    name: '',
    ip: '',
    username: '',
    password: '',
  },
  rulesResult:{
    firm: [{required: true, message: "设备厂商不能为空", trigger: "blur"}],
    model: [{required: true, message: "设备型号不能为空", trigger: "blur"}],
    firmwareVersion: [{required: true, message: "固件版本不能为空", trigger: "blur"}],
    url: [{required: true, message: "直播流地址不能为空", trigger: "blur"}],
  }
});

const {queryParams, form, rules,probeForm,rulesResult} = toRefs(data);

const passwordVisibility = ref({});

const url = ref('');
const disabledAdd = ref(true);
const resultForm = ref({});

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

const submitDetectionForm = () => {
  proxy.$refs["probeRef"].validate(async valid => {
    if (valid) {
      const query = {
        ip: probeForm.value.ip,
        username: probeForm.value.username,
        password: probeForm.value.password,
      };
      resultForm.value = {};
      const res = await probe(query);
      resultForm.value.firm = res.data.firm;
      resultForm.value.model = res.data.model;
      resultForm.value.firmwareVersion = res.data.firmwareVersion;
      resultForm.value.streamUris = res.data.streamUris;
      disabledAdd.value = false;
      proxy.$modal.msgSuccess("操作成功");
    }
  });
};

const submitResultForm = () => {
  proxy.$refs["resultRef"].validate(async valid => {
    if (valid) {
      const data = {
        id: null,
        name: probeForm.value.name,
        ip: probeForm.value.ip,
        userName: probeForm.value.username,
        password: probeForm.value.password,
        firm: resultForm.value.firm,
        url: resultForm.value.url,
        model: resultForm.value.model,
        firmwareVersion: resultForm.value.firmwareVersion,
        streamUris: resultForm.value.streamUris,
      };
      await addDevice(data);
      proxy.$modal.msgSuccess("操作成功");
      probeForm.value = {};
      resultForm.value = {};
      openAdd.value = false;
      getList();
    }
  });
}

const presetAdd = async () => {
  const params = {
    ip: streamInfo.value.ip,
    username: streamInfo.value.userName,
    password: streamInfo.value.password,
    profileToken: token.value,
    presetToken: presetName.value,
  }
  await addPreset(params).then(() => {
    getAllPreset();
    proxy.$modal.msgSuccess("操作成功");
  })
}

const gotoPresets = async (row) => {
  const params = {
    ip: streamInfo.value.ip,
    username: streamInfo.value.userName,
    password: streamInfo.value.password,
    profileToken: token.value,
    presetToken: row.token,
  }
  await getGotoPreset(params);
  proxy.$modal.msgSuccess("操作成功");
}

const closePresets = async (row) => {
  const params = {
    ip: streamInfo.value.ip,
    username: streamInfo.value.userName,
    password: streamInfo.value.password,
    profileToken: token.value,
    presetToken: row.token,
  }
  await removePreset(params).then(() => {
    getAllPreset();
    proxy.$modal.msgSuccess("操作成功");
  });
}
const handleplayTabsClick = (tab, event) => {
  if(tab.props.label ==="预置点"){
    presetName.value = "";
    getAllPreset();
  }
}

const getAllPreset = async () => {
  showPresets.value = true;
  const params = {
    ip: streamInfo.value.ip,
    username: streamInfo.value.userName,
    password: streamInfo.value.password,
    profileToken: token.value,
  }
  const res = await getPresetList(params);
  optionsPresetsToken.value = res.data;
  showPresets.value = false;
}

const togglePasswordVisibility = (id) => {
  passwordVisibility.value[id] = !passwordVisibility.value[id];
};


function closePaly() {
  token.value = null;
  absoluteNumX.value = 0.00;
  absoluteNumY.value = 0.00;
  continuousNumX.value = 0.00;
  continuousNumY.value = 0.00;
  showPaly.value = false;
}

const handleContinuousStop = async () => {
  const query = {
    ip: streamInfo.value.ip,
    username: streamInfo.value.userName,
    password: streamInfo.value.password,
    profileToken: token.value,
  }
  await continuousMoveStop(query);
}

const handleContinuous = async () => {
  const query = {
    ip: streamInfo.value.ip,
    username: streamInfo.value.userName,
    password: streamInfo.value.password,
    profileToken: token.value,
    x: continuousNumX.value,
    y: continuousNumY.value,
  }
  await continuousMove(query);
}

const handleAbsolute = async () => {
  const query = {
    ip: streamInfo.value.ip,
    username: streamInfo.value.userName,
    password: streamInfo.value.password,
    profileToken: token.value,
    x: absoluteNumX.value,
    y: absoluteNumY.value,
  }
  await absoluteMove(query);
}
const onGetChannelToken = async () => {
  const res = await getChannelToken(playerOptions.value);
  optionsToken.value = res.data;
}

async function handleView(row) {
  streamInfo.value = row;
  brand.value = row.firm;
  playerOptions.value.ip = row.ip;
  playerOptions.value.rtspURL = row.url;
  playerOptions.value.username = row.userName;
  playerOptions.value.password = row.password;
  title.value = "视频播放";
  await onGetChannelToken();
  showPaly.value = true;
}

function openedPaly() {
  if (brand.value === 'Dahua') {
    if (dahuaPlayer.value) {
      dahuaPlayer.value.playerPlay();
    }
  }
}


function copyToClipboard(text) {
  navigator.clipboard.writeText(text).then(() => {
    proxy.$modal.msgSuccess("复制成功！");
  }).catch((err) => {
    proxy.$modal.msgError("复制失败，请重试！");
  });
}

function viewUrls(row) {
  title.value = "查看直播流地址";
  urls.value = JSON.parse(row);
  showUrl.value = true;
  console.log(JSON.parse(row))
}

/** 查询onvif 设备列表 */
function getList() {
  loading.value = true;
  listDevice(queryParams.value).then(response => {
    deviceList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

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
    ip: null,
    userName: null,
    password: null,
    url: null,
    firm: null,
    model: null,
    firmwareVersion: null,
    streamUris: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("deviceRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  title.value = "添加onvif 设备";
  openAdd.value = true
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getDevice(_id).then(response => {

    form.value = response.data;
    form.value.streamUris = JSON.parse(response.data.streamUris);
    console.log(form.value)
    open.value = true;
    title.value = "修改onvif 设备";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["deviceRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateDevice(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addDevice(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
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
  proxy.$modal.confirm('是否确认删除onvif 设备编号为"' + _ids + '"的数据项？').then(function () {
    return delDevice(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('onvif /device/export', {
    ...queryParams.value
  }, `device_${new Date().getTime()}.xlsx`)
}

getDeptTree();
getList();
</script>

<style scoped>
.selectVideo {
  height: 600px;
  width: 800px;
}
</style>
