package com.ruoyi.wvp.streamPush.enent;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.ruoyi.wvp.streamPush.bean.StreamPush;
import com.ruoyi.wvp.streamPush.bean.StreamPushExcelDto;
import com.ruoyi.wvp.streamPush.service.IStreamPushService;
import com.ruoyi.wvp.utils.DateUtil;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamPushUploadFileHandler extends AnalysisEventListener<StreamPushExcelDto> {

    /**
     * 错误数据的回调，用于将错误数据发送给页面
     */
    private final ErrorDataHandler errorDataHandler;

    /**
     * 推流的业务类用于存储数据
     */
    private final IStreamPushService pushService;

    /**
     * 默认流媒体节点ID
     */
    private final String defaultMediaServerId;

    /**
     * 用于存储根据APP+Stream过滤后的数据，存入wvp_device与wvp_device_channel表
     */
    private final Map<String, StreamPush> streamPushItemForSave = new HashMap<>();

    /**
     * 用于存储APP+Stream->国标ID 的数据结构, 数据一一对应，全局判断APP+Stream->国标ID是否存在不对应
     */
    private final BiMap<String,String> gBMap = HashBiMap.create();

    /**
     * 用于存储APP+Stream-> 在数据库中的数据
     */
    private final BiMap<String,String> pushMapInDb = HashBiMap.create();

    /**
     * 记录错误的APP+Stream
     */
    private final List<String> errorStreamList = new ArrayList<>();


    /**
     * 记录错误的国标ID
     */
    private final List<String> errorInfoList = new ArrayList<>();

    /**
     * 读取数量计数器
     */
    private int loadedSize = 0;

    public StreamPushUploadFileHandler(IStreamPushService pushService, String defaultMediaServerId, ErrorDataHandler errorDataHandler) {
        this.pushService = pushService;
        this.defaultMediaServerId = defaultMediaServerId;
        this.errorDataHandler = errorDataHandler;
        // 获取数据库已有的数据，已经存在的则忽略
        List<String> allAppAndStreams = pushService.getAllAppAndStream();
        if (!allAppAndStreams.isEmpty()) {
            for (String allAppAndStream : allAppAndStreams) {
                pushMapInDb.put(allAppAndStream, allAppAndStream);
            }
        }
    }

    public interface ErrorDataHandler{
        void handle(List<String> streams, List<String> gbId);
    }

    @Override
    public void invoke(StreamPushExcelDto streamPushExcelDto, AnalysisContext analysisContext) {
        if (ObjectUtils.isEmpty(streamPushExcelDto.getGbDeviceId())) {
            return;
        }
        Integer rowIndex = analysisContext.readRowHolder().getRowIndex();

        // 自动生成 app 和 stream
        String app = "push" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String stream = app + "01";

        if (gBMap.get(app + stream) == null) {
            try {
                gBMap.put(app + stream, streamPushExcelDto.getGbDeviceId());
            }catch (IllegalArgumentException e) {
                errorInfoList.add("行：" + rowIndex + ", " + streamPushExcelDto.getGbDeviceId() + " 国标ID重复使用");
                return;
            }
        }else {
            if (!gBMap.get(app + stream).equals(streamPushExcelDto.getGbDeviceId())) {
                errorInfoList.add("行：" + rowIndex + ", " + streamPushExcelDto.getGbDeviceId() + " 同样的应用名和流ID使用了不同的国标ID");
                return;
            }
        }

        StreamPush streamPush = new StreamPush();
        streamPush.setApp(app);
        streamPush.setStream(stream);
        streamPush.setName(streamPushExcelDto.getName());
        streamPush.setGbDeviceId(streamPushExcelDto.getGbDeviceId());
        streamPush.setCreateTime(DateUtil.getNow());
        streamPush.setMediaServerId(defaultMediaServerId);
        streamPush.setGbName(streamPushExcelDto.getName());
        streamPush.setGbLongitude(streamPushExcelDto.getLongitude());
        streamPush.setGbLatitude(streamPushExcelDto.getLatitude());
        streamPush.setUpdateTime(DateUtil.getNow());
        streamPushItemForSave.put(app + stream, streamPush);

        loadedSize ++;
        if (loadedSize > 1000) {
            saveData();
            streamPushItemForSave.clear();
            loadedSize = 0;
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        streamPushItemForSave.clear();
        gBMap.clear();
        errorDataHandler.handle(errorStreamList, errorInfoList);
    }

    private void saveData(){
        if (!streamPushItemForSave.isEmpty()) {
            // 向数据库查询是否存在重复的app
            pushService.batchAdd(new ArrayList<>(streamPushItemForSave.values()));
        }
    }
}
