package com.ruoyi.web.controller.common;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.GroupTree;
import com.ruoyi.wvp.gb28181.bean.RegionTree;
import com.ruoyi.wvp.gb28181.service.IGbChannelService;
import com.ruoyi.wvp.gb28181.service.IGroupService;
import com.ruoyi.wvp.gb28181.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static com.ruoyi.common.core.domain.AjaxResult.success;

/**
 * 地图接口
 * @Author: 陈江灿
 * @CreateTime: 2025-06-25
 */
@RestController
@RequestMapping("/gls")
public class GlsMapController extends BaseController {

    @Autowired
    private IRegionService regionService;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IGbChannelService channelService;

    /**
     * 获取国标通道列表
     * @return
     */
    @GetMapping("/gb/channel/list")
    public AjaxResult gbChannelList(){
        HashMap<String, Object> map = new HashMap<>(16);
        List<RegionTree> regionTrees = regionService.queryForTree("", null, null);
        map.put("regionTrees",regionTrees);
        List<GroupTree> groupTrees = groupService.queryForTree("", null, null);
        map.put("groupTrees",groupTrees);
        return success(map);
    }

    /**
     * 行政区域获取树列表
     * @param query
     * @param parent
     * @param hasChannel
     * @return
     */
    @GetMapping("/region/tree/list")
    public AjaxResult queryForTree(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer parent,
            @RequestParam(required = false) Boolean hasChannel
    ) {
        if (ObjectUtils.isEmpty(query)) {
            query = null;
        }
        return success(regionService.queryForTree(query, parent, hasChannel));
    }

    /**
     * 业务分组获取树列表
     * @param query
     * @param parent
     * @param hasChannel
     * @return
     */
    @GetMapping("/group/tree/list")
    public AjaxResult queryGroupForTree(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer parent,
            @RequestParam(required = false) Boolean hasChannel
    ) {
        if (ObjectUtils.isEmpty(query)) {
            query = null;
        }
        return success(groupService.queryForTree(query, parent, hasChannel));
    }


    /**
     * 根据ParentId获取通道列表
     * @param pageNum
     * @param pageSize
     * @param query
     * @param online
     * @param channelType
     * @param groupDeviceId
     * @return
     */
    @GetMapping("/channel/parent/list")
    public TableDataInfo queryListByParentId(int pageNum, int pageSize,
                                             @RequestParam(required = false) String query,
                                             @RequestParam(required = false) Boolean online,
                                             @RequestParam(required = false) Integer channelType,
                                             @RequestParam(required = false) String groupDeviceId) {
        startPage();
        if (ObjectUtils.isEmpty(query)) {
            query = null;
        }
        List<CommonGBChannel> list = channelService.queryListByParentId(pageNum, pageSize, query, online, channelType, groupDeviceId);
        return getDataTable(list);
    }


    /**
     * 根据行政区域获取通道列表
     * @param pageNum
     * @param pageSize
     * @param query
     * @param online
     * @param channelType
     * @param civilCode
     * @return
     */
    @GetMapping("/civilcode/list")
    public TableDataInfo queryListByCivilCode(int pageNum, int pageSize,
                                              @RequestParam(required = false) String query,
                                              @RequestParam(required = false) Boolean online,
                                              @RequestParam(required = false) Integer channelType,
                                              @RequestParam(required = false) String civilCode) {
        startPage();
        if (ObjectUtils.isEmpty(query)) {
            query = null;
        }
        List<CommonGBChannel> list = channelService.queryListByCivilCode(pageNum, pageSize, query, online, channelType, civilCode);
        return getDataTable(list);
    }



}
