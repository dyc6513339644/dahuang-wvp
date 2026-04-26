import request from '@/utils/request'

// 左上--设备内总览
export const countDeviceNum = () => {
    return request({
        url: '/api/large/countDeviceNum',
        method: 'get'
    })
}

// 左上--国标总览
export const countGbNum = () => {
    return request({
        url: '/api/large/countGbNum',
        method: 'get'
    })
}

// 获取服务信息
export const serverInfo = () => {
    return request({
        url: '/api/server/system/info',
        method: 'get'
    })
}
