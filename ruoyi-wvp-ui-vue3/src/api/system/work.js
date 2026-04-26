import request from '@/utils/request'

// 获取工作台列表
export function listWork() {
    return request({
        url: '/system/work/list',
        method: 'get',
    })
}

// 修改工作台
export function updateWork(data) {
    return request({
        url: '/system/work',
        method: 'put',
        data,
    })
}
