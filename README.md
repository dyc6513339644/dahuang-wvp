<p align="center">
	<img width="150" alt="logo" src="https://ry-wvp-1315132478.cos.ap-guangzhou.myqcloud.com/md/扣1.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">RuoYi-Wvp</h1>
<h4 align="center">基于ruoyi-vue的流媒体平台，开箱即用、完全开源、使用MIT许可协议</h4>
<p align="center">
	<a href="https://gitee.com/xiaochemgzi/RuoYi-Wvp/stargazers"><img src="https://gitee.com/xiaochemgzi/RuoYi-Wvp/badge/star.svg?theme=dark"></a>
	<a href="https://gitee.com/xiaochemgzi/RuoYi-Wvp"><img src="https://img.shields.io/badge/RuoYi_Wvp-v1.7.0-blue.svg"></a>
	<a href="https://gitee.com/y_project/RuoYi-Vue/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>

## 平台介绍

ruoyi-wvp是基于ruoyi-vue和wvp框架的全部开源的GB/T 28181-2016标准流媒体平台,支持gb28181-2016协议、gb28181-2022协议、onvif协议、rtsp协议、isup协议、大华sdk、海康sdk、宇视sdk、天地伟业sdk、萤石云、乐橙云、非标转国标、gat1400协议。

## 概述

* ruoyi-wvp 是基于GB/T 28181-2016标准全部开源的流媒体平台,依托优秀的开源流媒体服务[ZLMediaKit](https://github.com/ZLMediaKit/ZLMediaKit) ,实现了高效、稳定的流媒体处理功能。
* 感谢goview开源项目，ruoyi-wvp前端基于goview进行开发自定义大屏。
* 整合了优秀的开源框架 ruoyi-vue，提供了高效率的开发体验，通过代码生成器，用户可以一键生成前后端代码，极大地提升了开发效率。
* 支持通过支持gb28181-2016协议、gb28181-2022协议、onvif协议、rtsp协议、isup协议、大华sdk、海康sdk、宇视sdk、天地伟业sdk、萤石云、乐橙云、非标转国标、gat1400协议将各类摄像头和录像机轻松接入平台，实现视频流的在线观看与分发。
* 支持加载动态权限菜单，多方式轻松权限控制,支持多终端认证系统。

## 技术栈

* 前端基于Vue3与Element-Plus构建高效优雅前端界面。
* 后端运用Spring Boot构建基础，结合Spring Security保障安全，辅以Redis缓存与Jwt认证，打造稳健高效服务。
* 数据库采用MySQL，利用MyBatis高效操作数据库，同时支持多种数据库。

## 开源版、付费版、商业版功能区别

| 功能                | 开源版 | 付费版 | 商业版                        |
|-------------------| ------ |-----|----------------------------|
| 原若依全部功能           | 支持   | 支持  | 支持                         |
| 国标设备              | 支持   | 支持  | 支持;全面支持2022协议、2022协议对讲；以及mp3循环对讲 |
| 云端录像              | 支持   | 支持  | 支持                         |
| 推流列表              | 支持   | 支持  | 支持                         |
| 拉流列表              | 支持   | 支持  | 支持                         |
| 国标级联              | 支持   | 支持  | 支持                         |
| 录像计划              | 支持   | 支持  | 支持                         |
| 行政分组              | 支持   | 支持  | 支持                         |
| 通道管理              | 支持   | 支持  | 支持                         |
| 节点管理              | 支持   | 支持  | 支持                         |
| 分屏监控              | 支持   | 支持  | 支持                         |
| onvif协议           | 不支持 | 支持  | 全面支持注册国标平台;实现国标协议调用云台、预置点    |
| 工作台               | 不支持 | 支持  | 支持                         |
| 海康isup协议          | 不支持 | 支持  | 全面支持注册国标平台;实现国标协议调用云台、预置点    |
| 海康SDK协议           | 不支持 | 不支持  | 全面支持注册国标平台;实现国标协议调用云台、预置点    |
| rtsp协议            | 支持   | 支持  | 支持                         |
| 大华sdk             | 不支持 | 支持  | 全面支持注册国标平台;实现国标协议调用云台、预置点    |
| 宇视sdk             | 不支持 | 不支持  | 全面支持注册国标平台;实现国标协议调用云台、预置点    |
| 天地伟业sdk           | 不支持 | 不支持  | 全面支持注册国标平台;实现国标协议调用云台、预置点    |
| 萤石云协议             | 不支持 | 不支持 | 全面支持注册国标平台;实现国标协议调用云台、预置点    |
| 乐橙云协议             | 不支持 | 不支持 | 支持                         |
| gat1400协议         | 不支持 | 不支持 | 支持                         |
| 大屏可视化             | 不支持 | 支持  | 支持                         |
| 小程序+app           | 不支持 | 支持  | 全面支持安卓、ios、鸿蒙、微信小程序          |
| zml和ruoyi-wvp分开部署 | 不支持 | 支持  | 支持                         |
| 新首页ui             | 不支持 | 支持  | 支持                         |
| rtsp获取设备录像        | 不支持 | 支持  | 支持                         |
| 设备电子地图            | 不支持 | 支持  | 支持                         |
| goview自定义大屏       | 不支持 | 不支持 | 支持                         |
| 内网推拉流             | 不支持 | 不支持 | 支持                         |

## 其他开源

基于ruoyi-cloud的nvr系统，开箱即用、完全开源、使用MIT许可协议。
* [ruoyi-qs-nvr](https://gitee.com/tangwenzhaoaini/ruoyi-qs-nvr) 后端地址
* [ruoyi-qs-nvr-ui](https://gitee.com/tangwenzhaoaini/ruoyi-qs-nvr-ui) 前端地址

## QQ群

QQ群： [![加入QQ群](https://img.shields.io/badge/695642294-blue.svg)]()

## 在线体验

文档地址：http://1.12.55.122:85/

## 演示

<table>
    <tr>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/1.png"/></td>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/2.png"/></td>
    </tr>
    <tr>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/11.png"/></td>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/12.png"/></td>
    </tr>
    <tr>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/13.png"/></td>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/14.png"/></td>
    </tr>
    <tr>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/15.png"/></td>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/16.png"/></td>
    </tr>
    <tr>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/17.png"/></td>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/18.png"/></td>
    </tr>
    <tr>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/19.png"/></td>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/20.png"/></td>
    </tr>
    <tr>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/21.png"/></td>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/22.png"/></td>
    </tr>
    <tr>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/23.png"/></td>
        <td><img src="https://ruoyi-wvp-1308576884.cos.ap-guangzhou.myqcloud.com/ruoyi-wvp/upload/24.png"/></td>
    </tr>
</table>

## 授权协议

本项目自有代码使用宽松的MIT协议，在保留版权信息的情况下可以自由应用于各自商用、非商业的项目。 但是本项目也零碎的使用了一些其他的开源代码，在商用的情况下请自行替代或剔除； 由于使用本项目而产生的商业纠纷或侵权行为一概与本项目及开发者无关，请自行承担法律风险。 在使用本项目代码时，也应该在授权协议中同时表明本项目依赖的第三方库的协议。

## 联系方式

* 微信1: ruoyi-wvp888 备注：ruoyi-wvp
* 微信2: shaojiaoyan520  备注：ruoyi-wvp

## 付费社群-299永久

## 特别致谢

- 感谢作者[夏楚](https://github.com/xia-chu) 开源了这么棒流媒体服务框架。
- 感谢作者[wvp](https://github.com/648540858/wvp-GB28181-pro) 开源了这么棒国标服务器框架。
- 感谢作者[若依](https://ruoyi.vip/) 开源了这么棒快速开发框架。
- 感谢作者[go-view](https://gitee.com/dromara/go-view) 开源了这么棒的大屏应用框架。