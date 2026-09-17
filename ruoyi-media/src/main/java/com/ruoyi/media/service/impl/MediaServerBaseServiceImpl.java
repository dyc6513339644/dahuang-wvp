package com.ruoyi.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.media.service.IMediaServerBaseService;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.media.mapper.MediaServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流媒体基础服务实现 —— 媒体服务器节点 CRUD
 */
@Service
public class MediaServerBaseServiceImpl implements IMediaServerBaseService {

    @Autowired
    private MediaServerMapper mediaServerMapper;

    @Override
    public int add(MediaServer mediaServer) {
        return mediaServerMapper.insert(mediaServer);
    }

    @Override
    public int update(MediaServer mediaServer) {
        return  mediaServerMapper.updateById(mediaServer);
    }


    @Override
    public void deleteById(String id) {
        mediaServerMapper.deleteById(id);
    }


    @Override
    public MediaServer getById(String id) {
        return mediaServerMapper.selectById(id);
    }

    @Override
    public List<MediaServer> queryAll() {
        return mediaServerMapper.selectList(null);
    }

    @Override
    public MediaServer queryDefault() {
        QueryWrapper<MediaServer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("default_server", true);
        return mediaServerMapper.selectOne(queryWrapper);
    }

    @Override
    public List<MediaServer> queryAllWithAssistPort() {
        QueryWrapper<MediaServer> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("record_assist_port", 0);
        return mediaServerMapper.selectList(queryWrapper);
    }

    @Override
    public MediaServer queryOneByHostAndPort(String host, int port) {
        QueryWrapper<MediaServer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", host);
        queryWrapper.eq("http_port", port);
        return mediaServerMapper.selectOne(queryWrapper);
    }
}
