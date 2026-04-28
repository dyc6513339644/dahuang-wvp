<p align="center">
	<img width="200" alt="logo" src="https://www.jhpic.zgdahuan.com/QQ20260428-231217.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Dahuang-Wvp</h1>
<h4 align="center">基于ruoyi和wvp的国标视频管理平台，开箱即用、完全开源、使用MIT许可协议</h4>


## 平台介绍

dahuang-wvp是基于ruoyi-vue和wvp框架的全部开源的GB/T 28181-2016视频平台，保留版权的情况下可以用于商业项目。

## 概述

* dahuang-wvp 是基于GB/T 28181-2016标准全部开源的国标视频平台,依托优秀的开源流媒体服务[ZLMediaKit](https://github.com/ZLMediaKit/ZLMediaKit) ,实现了高效、稳定的流媒体处理功能。
* 整合了优秀的开源框架 ruoyi-vue，提供了高效率的开发体验，通过代码生成器，用户可以一键生成前后端代码，极大地提升了开发效率。
* 支持通过国标 28181 协议将各类摄像头和录像机轻松接入平台，实现视频流的在线观看与分发。
* 支持加载动态权限菜单，多方式轻松权限控制,支持多终端认证系统。



## 技术栈

* 前端基于Vue3与Element-Plus构建高效优雅前端界面。
* 后端运用Spring Boot构建基础，结合Spring Security保障安全，辅以Redis缓存与Jwt认证，打造稳健高效服务。


## 功能特性
* 集成web界面
* 兼容性良好
* 跨平台服务，一次编译多端部署， 可以同时用于x86和arm架构
* 接入设备
* 视频预览
* 支持主码流子码流切换
* 无限制接入路数，能接入多少设备只取决于你的服务器性能
* 云台控制，控制设备转向，拉近，拉远
* 预置位查询，使用与设置
* 查询NVR/IPC上的录像与播放，支持指定时间播放与下载
* 无人观看自动断流，节省流量
* 视频设备信息同步
* 离在线监控
* 支持直接输出RTSP、RTMP、HTTP-FLV、Websocket-FLV、HLS多种协议流地址
* 支持通过一个流地址直接观看摄像头，无需登录以及调用任何接口
* 支持UDP和TCP两种国标信令传输模式
* 支持UDP和TCP两种国标流传输模式
* 支持检索,通道筛选
* 支持通道子目录查询
* 支持过滤音频，防止杂音影响观看
* 支持国标网络校时
* 支持播放H264和H265
* 报警信息处理，支持向前端推送报警信息
* 语音对讲
* 支持业务分组和行政区划树自定义展示以及级联推送
* 支持订阅与通知方法
* 移动位置订阅
* 移动位置通知处理
* 报警事件订阅
* 报警事件通知处理
* 设备目录订阅
* 设备目录通知处理
* 移动位置查询和显示
* 支持手动添加设备和给设备设置单独的密码
* 支持平台对接接入
* 支持国标级联
* 国标通道向上级联
* WEB添加上级平台
* 注册
* 心跳保活
* 通道选择
* 支持通道编号自定义, 支持每个平台使用不同的通道编号
* 通道推送
* 点播
* 云台控制
* 平台状态查询
* 平台信息查询
* 平台远程启动
* 每个级联平台可自定义的虚拟目录
* 目录订阅与通知
* 录像查看与播放
* GPS订阅与通知（直播推流）
* 语音对讲
* 支持同时级联到多个上级平台
* 支持自动配置ZLM媒体服务, 减少因配置问题所出现的问题;
* 支持流媒体节点集群，负载均衡。
* 支持启用udp多端口模式, 提高udp模式下媒体传输性能;
* 支持公网部署；
* 支持wvp与zlm分开部署，提升平台并发能力
* 支持拉流RTSP/RTMP，分发为各种流格式，或者推送到其他国标平台
* 支持推流RTSP/RTMP，分发为各种流格式，或者推送到其他国标平台
* 支持推流鉴权
* 支持接口鉴权
* 云端录像，推流/代理/国标视频均可以录制在云端服务器，支持预览和下载
* 支持打包可执行jar和war
* 支持跨域请求，支持前后端分离部署
* 支持Mysql，Postgresql，金仓等数据库
* 支持录制计划, 根据设定的时间对通道进行录制. 暂不支持将录制的内容转发到国标上级
* 支持国标信令集群
* 新增支持部标808和部标1078，大量新特性不一一列表了。支持作为网关被国标上级调用部标设备
* 支持电子地图。支持展示通道位置，支持在地图上修改通道位置。支持了数据分层抽稀数据能力，百万级数据也可以轻松展示。提供标准的矢量瓦片图层，常见地图引擎都可以直接展示。
## 前端源码

加qq群获取最新前端vue3代码

<p align="center">
 <img width="400" alt="logo" src="https://www.jhpic.zgdahuan.com/qr_code_qq.ef46655e.png">
</p>


## 国标演示图

<table>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/c11404f40b3344719c5e17055972e35b.png"/></td>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/9238d06790764f3b9317f7b0a9abd056.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/749121d82e4844c4827afaaaa5172af2.png"/></td>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/1752defdedd44aea9d4b94440c0a56cd.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/350dcf28536448349a8ab4927a9e192e.png"/></td>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/a383b8369edb421080168fea74e4e4f2.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/34c300506ce74302be2f5bd63fca270f.png"/></td>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/f0c4aa1e694c4282853ebcb304503e28.png"/></td>
    </tr>
	<tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/43d4a7c839a64916a89ce6146acb4db4.png"/></td>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/389dff88a839449483518868154c804c.png"/></td>
    </tr>	 
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/690c87e89f80463298d48136b99998bc.png"/></td>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/b276ace519b541eba6b05e640b450e44.png"/></td>
    </tr>
	<tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/1b0dca2be11a44bab60bed1f6fefb0a7.png"/></td>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/27211331ad6246699cb3019ba9c92a64.png"/></td>
    </tr>
	<tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/a832fa6d9a50413385cbd2ed1b700797.png"/></td>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/17/ae58f01bff6c49a4bad5d622490bede6.png"/></td>
    </tr>
</table>

## onvif演示图

<table>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/05/07/ff16b289ad6c47899da28490ce20dd5c.png"/></td>
    </tr>
</table>

## isup播放示例图
<table>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/24/8ac1339ac5584a2090f529f61da50093.png"/></td>
    </tr>
</table>

## rtsp播放示例图

<table>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/04/24/e35fc83314c54b9a91eb6ddbe2185e7b.png"/></td>
    </tr>
</table>

## 大屏

<table>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/05/07/c0c59e92d0c74a20a9b177af2332e6d3.png"/></td>
    </tr>
</table>

## 工作台

<table>
    <tr>
        <td><img src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/05/07/16ae29a3eaa24471a061d7364b113ab8.png"/></td>
    </tr>
</table>

## 小程序

<table>
    <tr>
        <td><img width="200" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/05/07/7f4b35186a2041b1a04f4c71a534a29d.png"/></td>
        <td><img width="200" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/05/07/fee62e3df1a8458f810a7818561a7530.png"/></td>
    </tr>
    <tr>
        <td><img width="200" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/05/07/c7f62a49119f4f7cb44fc9afb2e77107.png"/></td>
        <td><img width="200" src="https://gdhxkj.oss-cn-guangzhou.aliyuncs.com/2025/05/07/77a2f2dc4fc141c6918fe9f5df129605.png"/></td>
    </tr>
</table>

## 授权协议

本项目自有代码使用宽松的MIT协议，在保留版权信息的情况下可以自由应用于各自商用、非商业的项目。 但是本项目也零碎的使用了一些其他的开源代码，在商用的情况下请自行替代或剔除； 由于使用本项目而产生的商业纠纷或侵权行为一概与本项目及开发者无关，请自行承担法律风险。 在使用本项目代码时，也应该在授权协议中同时表明本项目依赖的第三方库的协议。


## 特别致谢

- 感谢作者[夏楚](https://github.com/xia-chu) 开源了这么棒流媒体服务框架。
- 感谢作者[wvp](https://github.com/648540858/wvp-GB28181-pro) 开源了这么棒国标服务器框架。
- 感谢作者[若依](https://ruoyi.vip/) 开源了这么棒快速开发框架。
- 感谢作者[公安县泉姒禾网络科技工作室](https://gitee.com/xiaochemgzi/RuoYi-Wvp.git) 开源wvp做了集成。

## 赞赏方式
如果你感觉项目对你有帮助,可以扫码进行捐赠
<img width="700" src="https://www.jhpic.zgdahuan.com/QQ20260428-224233.png"/>

