package com.ruoyi.wvp.gb28181.bean;

import com.ruoyi.media.event.hook.HookData;
import com.ruoyi.media.domain.SSRCInfo;
import lombok.Data;

@Data
public class OpenRTPServerResult {

    private SSRCInfo ssrcInfo;
    private HookData hookData;
}
