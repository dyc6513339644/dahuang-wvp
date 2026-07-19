package com.ruoyi.wvp.streamProxy.dao.provider;

import com.ruoyi.wvp.common.enums.ChannelDataType;

import java.util.Map;

public class StreamProxyProvider {

    /**
     * 基础查询：从 wvp_device + wvp_device_channel 查询拉流代理
     */
    public String getBaseSelectSql() {
        return "SELECT " +
                " de.*, " +
                ChannelDataType.STREAM_PROXY.value + " as data_type, " +
                " de.id as data_device_id, " +
                " wdc.*, " +
                " wdc.id as gb_id " +
                " FROM wvp_device de " +
                " LEFT JOIN wvp_device_channel wdc " +
                " ON wdc.data_type = " + ChannelDataType.STREAM_PROXY.value +
                " AND de.id = wdc.data_device_id " +
                " WHERE de.protocol_type = 'STREAM_PROXY' ";
    }

    public String select(Map<String, Object> params) {
        return getBaseSelectSql() + " AND de.id = " + params.get("id");
    }

    public String selectForPushingInMediaServer(Map<String, Object> params) {
        return getBaseSelectSql() + " AND de.media_server_id = #{mediaServerId} ORDER BY de.create_time DESC";
    }

    public String selectOneByAppAndStream(Map<String, Object> params) {
        return getBaseSelectSql() + String.format(
                " AND de.device_id = '%s' ORDER BY de.create_time DESC",
                params.get("app"));
    }

    public String selectAll(Map<String, Object> params) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append(getBaseSelectSql());
        if (params.get("query") != null) {
            sqlBuild.append(" AND ")
                    .append(" (")
                    .append(" de.device_id LIKE ").append("'%").append(params.get("query")).append("%' escape '/'")
                    .append(" OR")
                    .append(" de.name LIKE ").append("'%").append(params.get("query")).append("%' escape '/'")
                    .append(" OR")
                    .append(" de.src_url LIKE ").append("'%").append(params.get("query")).append("%' escape '/'")
                    .append(" OR")
                    .append(" wdc.device_id LIKE ").append("'%").append(params.get("query")).append("%' escape '/'")
                    .append(" OR")
                    .append(" wdc.name LIKE ").append("'%").append(params.get("query")).append("%' escape '/'")
                    .append(" )");
        }
        Object pulling = params.get("pulling");
        if (pulling != null) {
            if ((Boolean) pulling) {
                sqlBuild.append(" AND de.on_line = 1 ");
            } else {
                sqlBuild.append(" AND de.on_line = 0 ");
            }
        }
        if (params.get("mediaServerId") != null) {
            sqlBuild.append(" AND de.media_server_id = '").append(params.get("mediaServerId")).append("'");
        }
        sqlBuild.append(" ORDER BY de.create_time DESC");
        return sqlBuild.toString();
    }
}