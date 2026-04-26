<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="10" style="padding-left: 20px">
        <h2>基于ruoyi-vue的流媒体平台，开箱即用、完全开源、使用MIT许可协议</h2>
        <p>
          ruoyi-wvp是基于ruoyi-vue和wvp框架的全部开源的GB/T 28181-2016标准流媒体平台，保留版权的情况下可以用于商业项目。
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
        <p>
          <el-tag type="danger">&yen;免费开源</el-tag>
        </p>
        <p>
          <el-button
              size="mini"
              plain
              @click="goTarget('http://java.ry-wvp.xyz')"
          >在线体验
          </el-button>
          <el-button
              type="primary"
              size="mini"
              plain
              @click="goTarget('https://gitee.com/xiaochemgzi/RuoYi-Wvp')"
          >访问gitee
          </el-button>
          <el-button
              type="primary"
              size="mini"
              plain
              @click="goTarget('https://github.com/cbnbcbnb/RuoYi-Wvp')"
          >访问gitHub
          </el-button>
          <el-button
              type="primary"
              size="mini"
              plain
              @click="goTarget('https://gitcode.com/m0_59771750/RuoYi-Wvp')"
          >访问gitcode
          </el-button>
        </p>
      </el-col>

      <el-col :sm="24" :lg="6" style="padding-left: 20px">
        <el-row>
          <el-col :span="24">
            <h2>技术选型:</h2>
            <h3>感谢ruoyi-vue、wvp、ZLMediaKit</h3>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <h4>支持协议</h4>
            <ul>
              <li>gb28181协议</li>
              <li>onvif协议</li>
              <li>rtsp协议</li>
              <li>isup协议</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
      <el-col :sm="24" :lg="6" style="padding-left: 20px">
        <h2>我的开源项目</h2>
        <div>
          <el-link type="primary" href="https://gitee.com/xiaochemgzi/RuoYi-Wvp" target="_blank">ruoyi-wvp：基于ruoyi-vue的流媒体平台。</el-link>
        </div>
        <div>
          <el-link type="primary" href="https://gitee.com/tangwenzhaoaini/ruoyi-sign" target="_blank">电子签章系统：基于SpringBoot+Vue+Flowable前后端分离的电子签章系统。</el-link>
        </div>
        <div>
          <el-link type="primary" href="https://gitee.com/tangwenzhaoaini/ruo-yi-vue-tenant" target="_blank">RuoYi-Vue-Tenant：基于RuoYi-Vue的多租户管理平台。</el-link>
        </div>
        <div>
          <el-link type="primary" href="https://gitee.com/tangwenzhaoaini/RuoYi-Vue-flowable" target="_blank">RuoYi-Vue-flowable：基于RuoYi-Vue + flowable 的工作流管理平台。</el-link>
        </div>
        <div>
          <el-link type="primary" href="https://gitee.com/tangwenzhaoaini/ruo-yi-vue-flowable-tenant" target="_blank">RuoYi-Vue-Flowable-Tenant：基于RuoYi-Vue + flowable 的多租户工作流管理平台。</el-link>
        </div>
        <div>
          <el-link type="primary" href="https://gitee.com/xiaochemgzi/ruoyi-iot" target="_blank">ruoyi-iot：基于SpringBoot+Vue3前后端分离的Java物联网平台。</el-link>
        </div>
        <div>
          <el-link type="primary" href="https://gitee.com/xiaochemgzi/rtsp-video-analysis-system" target="_blank">rtsp视频分析系统：基于SpringBoot+Vue前后端分离的rtsp视频分析系统。</el-link>
        </div>
        <div>
          <el-link type="primary" href="https://gitee.com/xiaochemgzi/rtsp-ai" target="_blank">口罩分析流媒体服务器：基于SpringBoot+Vue前后端分离的口罩识别系统。</el-link>
        </div>
      </el-col>
    </el-row>
    <el-divider/>
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template #header>
            <div class="card-header">
              <span>前端源码</span>
            </div>
          </template>
          <div class="body">
            加qq群获取最新前端代码

            <p align="center">
              <img width="200" alt="logo" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/7e0678c3f21547f0a427ddca506eab17.jpg">
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template #header>
            <div class="card-header">
              <span>捐赠支持</span>
            </div>
          </template>
          <div class="body">
            <el-row :gutter="20">
              <el-col :xs="24" :sm="24" :md="24" :lg="24">
                <img width="500" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/bcfef20f53ee49e8baf2559f97e85ffb.jpg"/>
              </el-col>
            </el-row>
          </div>
          <span style="display: inline-block; height: 30px; line-height: 30px"
          >你可以请作者喝杯咖啡表示鼓励</span>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template #header>
            <div class="card-header">
              <span>付费社群</span>
            </div>
          </template>
          <div class="body">
            <img style="width: 500px" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/21/d96d8285328e4ecdbd459018d5ab55c5.jpg"/>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-divider/>
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template #header>
            <div class="card-header">
              <span>联系信息</span>
            </div>
          </template>
          <div class="body">
            <p>
              <i class="el-icon-chat-dot-round"></i> 微信1：<a
                href="javascript:;"
            >NYHHDWGZL 备注:RuoYi-Vue-Tenant</a
            >
            </p>

            <p>
              <i class="el-icon-chat-dot-round"></i> 微信2：<a
                href="javascript:;"
            >chenbai0511 备注:RuoYi-Vue-Tenant</a
            >
            </p>
            <p align="center">
              <img width="200" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/a8663f16829b4871a94c9534a6dad894.jpg"/>
              <img width="200" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/4601cea6e8cc4df18124bfc1af715dc7.png"/>
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template #header>
            <div class="card-header">
              <span>更新日志</span>
            </div>
          </template>
          <el-collapse accordion v-model="activeNames">
            <el-collapse-item title="v1.2.0 - 2024-5-7" name="3">
              <ol>
                <li>1.2.0版本正式发布</li>
                <li>支持工作台分屏查看设备录像</li>
                <li>支持大屏可视化</li>
                <li>支持小程序端进行播放</li>
                <li>修复一些已知的bug</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v1.1.0 - 2024-4-24" name="2">
              <ol>
                <li>1.1.0版本正式发布</li>
                <li>支持ISUP协议、云台控制</li>
                <li>支持rtmp协议</li>
                <li>支持gb28181云台控制，和语音控制等功能</li>
                <li>修复一些已知的bug</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v1.0.0 - 2025-4-15" name="1">
              <ol>
                <li>1.0.0版本正式发布</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template #header>
            <div class="card-header">
              <span>授权协议</span>
            </div>
          </template>
          <div class="body">
            本项目自有代码使用宽松的MIT协议，在保留版权信息的情况下可以自由应用于各自商用、非商业的项目。 但是本项目也零碎的使用了一些其他的开源代码，在商用的情况下请自行替代或剔除； 由于使用本项目而产生的商业纠纷或侵权行为一概与本项目及开发者无关，请自行承担法律风险。 在使用本项目代码时，也应该在授权协议中同时表明本项目依赖的第三方库的协议。
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
const version = ref("1.1.0")
const activeNames = ref('3')
function goTarget(url) {
  window.open(url, '__blank')
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

