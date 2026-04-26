<template>
  <div class="app-container">
    <el-alert title="通道号要填写正确，用户名/密码就是后台登陆的用户名/密码" type="success"
              style="margin-bottom: 10px;"/>

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
      <el-form-item label="摄像头名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入摄像头名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备厂商" prop="firm">
        <el-select v-model="queryParams.firm" placeholder="请选择设备厂商" clearable style="width: 180px;">
          <el-option
              v-for="dict in rtsp_manufacturer"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
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
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['rtsp:RtspDevice:add']"
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
            v-hasPermi="['rtsp:RtspDevice:edit']"
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
            v-hasPermi="['rtsp:RtspDevice:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['rtsp:RtspDevice:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="RtspDeviceList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="所属部门" align="center" prop="deptName"/>
      <el-table-column label="ip" align="center" prop="ip"/>
      <el-table-column label="摄像头名称" align="center" prop="name"/>
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
      <el-table-column label="通道号" align="center" prop="channel"/>
      <el-table-column label="设备厂商" align="center" prop="firm">
        <template #default="scope">
          <dict-tag :options="rtsp_manufacturer" :value="scope.row.firm"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="400">
        <template #default="scope">
          <el-button link type="primary" icon="Avatar" @click="handleAI(scope.row)"
                     v-hasPermi="['rtsp:RtspDevice:Avatar']">AI播放
          </el-button>
          <el-button link type="primary" icon="AlarmClock" @click="handleAlarmClock(scope.row)"
                     v-hasPermi="['rtsp:RtspDevice:AlarmClock']">历史播放
          </el-button>
          <el-button link type="primary" icon="View" @click="handleView(scope.row)"
                     v-hasPermi="['rtsp:RtspDevice:view']">播放
          </el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['rtsp:RtspDevice:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['rtsp:RtspDevice:remove']">删除
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

    <!-- 添加或修改rtsp设备对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="RtspDeviceRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属部门" prop="deptId">
          <el-tree-select v-model="form.deptId" :data="enabledDeptOptions" :props="{ value: 'id', label: 'label', children: 'children' }" value-key="id" placeholder="请选择归属部门" check-strictly />
        </el-form-item>
        <el-form-item label="ip" prop="ip">
          <el-input v-model="form.ip" placeholder="请输入ip"/>
        </el-form-item>
        <el-form-item label="摄像头名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入摄像头名称"/>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="通道号" prop="channel">
          <el-input v-model="form.channel" placeholder="请输入通道号"/>
        </el-form-item>
        <el-form-item label="设备厂商" prop="firm">
          <el-select v-model="form.firm" placeholder="请选择设备厂商">
            <el-option
                v-for="dict in rtsp_manufacturer"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
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

    <!-- 播放弹窗 对话框 -->
    <el-dialog :title="title" v-model="showPaly" width="835px">
      <div>
        <Hikvision :rtsp="rtspURL" v-if="showPaly"/>
      </div>
    </el-dialog>

    <el-dialog title="选择时间" v-model="showAlarmClock" width="505px" append-to-body>
      <el-form :model="alarmClockOptions" :rules="rulesAlarm" ref="alarmClockFormRef" label-width="100px">
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
              v-model="alarmClockOptions.startTime"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
              v-model="alarmClockOptions.endTime"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFormAlarmClock">确 定</el-button>
          <el-button @click="showAlarmClock = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="选择ai模式" v-model="showAI" width="505px">
      <div>
        <el-button type="primary" style="width: 100%;" @click="handlDetection('detection')">目标检测</el-button>
      </div>
      <div style="margin-top: 10px;">
        <el-button type="primary" style="width: 100%;">车牌检测</el-button>
      </div>
    </el-dialog>

    <el-dialog title="目标检测算法" v-model="showAIPaly" width="835px" @close="closeAI">
      <div style="width: 800px; height: 600px;">
        <el-row>
          <el-col :span="24">
            <div class="selectVideo">
              <CusPlayer ref="cusPlayer" v-if="cusPlayerShow"></CusPlayer>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="RtspDevice">
import {
  listRtspDevice,
  getRtspDevice,
  delRtspDevice,
  addRtspDevice,
  updateRtspDevice,
  addDetection,
  stopDetection,
  alarmClockRtspDevice
} from "@/api/rtsp/RtspDevice";
import Hikvision from "@/components/Hikvision/index.vue";
import CusPlayer from "@/components/flv/CusPlayer.vue";
import {ElLoading} from "element-plus";
import {deptTreeSelect} from "@/api/system/user";

const {proxy} = getCurrentInstance();
const {rtsp_manufacturer} = proxy.useDict('rtsp_manufacturer');

const RtspDeviceList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const cusPlayerShow = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const rtspURL = ref("");
const showPaly = ref(false);
const showAIPaly = ref(false);
const showAI = ref(false);
const showAlarmClock = ref(false);
const streamInfo = ref(null);
const cusPlayer = ref(null);
const alarmClockFormRef = ref(null);

const deptOptions = ref(undefined);
const enabledDeptOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    deptId: null,
    ip: null,
    name: null,
    firm: null,
  },
  rules: {
    deptId: [{ required: true, message: "请选择所属部门", trigger: 'blur' }],
    ip: [
      {required: true, message: "ip不能为空", trigger: "blur"}
    ],
    name: [
      {required: true, message: "摄像头名称不能为空", trigger: "blur"}
    ],
    userName: [
      {required: true, message: "用户名不能为空", trigger: "blur"}
    ],
    password: [
      {required: true, message: "密码不能为空", trigger: "blur"}
    ],
    channel: [
      {required: true, message: "通道号不能为空", trigger: "blur"}
    ],
    firm: [
      {required: true, message: "设备厂商不能为空", trigger: "change"}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);


const passwordVisibility = ref({});

const rulesAlarm = ref({
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ]
});

const alarmClockOptions = ref({
  id: undefined,
  startTime: undefined,
  endTime: undefined,
});

const submitFormAlarmClock = () => {
  proxy.$refs["alarmClockFormRef"].validate(valid => {
    if (valid) {
      alarmClockRtspDevice(alarmClockOptions.value).then(res => {
        rtspURL.value = res.msg;
        showPaly.value = true;
      }).finally(() => {
        showAlarmClock.value = false;
      });
    }
  });
}

const handleAlarmClock = (row) => {
  resetAlarmClock();
  alarmClockOptions.value.id = row.id;
  showAlarmClock.value = true;
}

const closeAI = () => {
  const params = {
    type: "detection",
    url: streamInfo.value.url,
    mediaId: null,
  }
  stopDetection(params);
}

const handleAI = (row) => {
  streamInfo.value = row;
  showAI.value = true;
}

function resetAlarmClock() {
  alarmClockOptions.value.id = undefined;
  alarmClockOptions.value.startTime = undefined;
  alarmClockOptions.value.endTime = undefined;
}

const handlDetection = async (str) => {
  const loadingService = ElLoading.service({
    lock: true,
    fullscreen: true,
    text: '正在发送指令',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)',
  });
  cusPlayerShow.value = false;
  let mediaId = Math.floor(Math.random() * 900) + 100;
  const params = {
    type: str,
    url: streamInfo.value.url,
    mediaId: mediaId,
  }
  await addDetection(params).then(res => {
    showAI.value = false;
    cusPlayerShow.value = true;
    showAIPaly.value = true;
    setTimeout(() => {
      cusPlayer.value.createPlayer("http://192.168.158.199:7080/live/livestream/" + mediaId + ".flv", true, 0);
      loadingService.close();
    }, 3000);
  }).finally(() => {
    loadingService.close();
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

const handleView = (row) => {
  rtspURL.value = row.url;
  title.value = row.name;
  showPaly.value = true;
}

/** 查询rtsp设备列表 */
function getList() {
  loading.value = true;
  listRtspDevice(queryParams.value).then(response => {
    RtspDeviceList.value = response.rows;
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
    name: null,
    userName: null,
    password: null,
    channel: null,
    url: null,
    firm: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("RtspDeviceRef");
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
  reset();
  open.value = true;
  title.value = "添加rtsp设备";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getRtspDevice(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改rtsp设备";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["RtspDeviceRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateRtspDevice(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addRtspDevice(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除rtsp设备编号为"' + _ids + '"的数据项？').then(function () {
    return delRtspDevice(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('rtsp/RtspDevice/export', {
    ...queryParams.value
  }, `RtspDevice_${new Date().getTime()}.xlsx`)
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
