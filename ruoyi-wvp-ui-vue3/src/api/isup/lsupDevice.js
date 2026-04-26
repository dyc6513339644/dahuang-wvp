import request from '@/utils/request'

// 查询isup设备列表
export function listLsupDevice(query) {
  return request({
    url: '/isup/lsupDevice/list',
    method: 'get',
    params: query
  })
}

// 查询isup设备列表
export function lsupDeviceList(query) {
  return request({
    url: '/isup/lsupDevice/lsupDeviceList',
    method: 'get',
    params: query
  })
}


// 查询isup设备详细
export function getLsupDevice(id) {
  return request({
    url: '/isup/lsupDevice/' + id,
    method: 'get'
  })
}

// 新增isup设备
export function addLsupDevice(data) {
  return request({
    url: '/isup/lsupDevice',
    method: 'post',
    data: data
  })
}

// 修改isup设备
export function updateLsupDevice(data) {
  return request({
    url: '/isup/lsupDevice',
    method: 'put',
    data: data
  })
}

// 删除isup设备
export function delLsupDevice(id) {
  return request({
    url: '/isup/lsupDevice/' + id,
    method: 'delete'
  })
}

// 获取数字通道
export function getDigitalChannel(query) {
  return request({
    url: '/onvif/service/getDigitalChannel',
    method: 'get',
    params: query
  })
}

// 云台控制
export function ptzCtrl(query) {
  return request({
    url: '/isup/lsupDevice/ptzCtrl',
    method: 'get',
    params: query
  })
}
