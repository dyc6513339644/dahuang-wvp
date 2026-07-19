package com.ruoyi.common.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用 Date TypeHandler —— 读写统一走 VARCHAR 通道，
 * 兼容 MySQL / SQLite / 达梦 DM 三种数据库。
 * <p>
 * SQLite 没有原生 DATETIME 类型（存 TEXT），
 * 默认 DateTypeHandler 调用 ResultSet.getTimestamp() 会抛 Error parsing time stamp。
 * 本 Handler 改用 getString() + 显式格式解析，三种 DB 的 JDBC 驱动均稳定支持。
 *
 * @author ruoyi
 */
public class UniversalDateTypeHandler extends BaseTypeHandler<Date> {

    private static final String[] FORMATS = {
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss.SSS",
        "yyyy-MM-dd"
    };

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date date, JdbcType jdbcType) throws SQLException {
        ps.setString(i, new SimpleDateFormat(FORMATS[0]).format(date));
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return s == null ? null : parse(s);
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return s == null ? null : parse(s);
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return s == null ? null : parse(s);
    }

    private Date parse(String s) {
        // 纯数字 → Unix 时间戳（毫秒级13位，秒级10位）
        if (s.matches("\\d+")) {
            long ts = Long.parseLong(s);
            return new Date(ts > 9999999999L ? ts : ts * 1000);
        }
        // 尝试多种日期格式
        for (String fmt : FORMATS) {
            try {
                return new SimpleDateFormat(fmt).parse(s);
            } catch (ParseException ignored) {
            }
        }
        throw new RuntimeException("日期解析失败: " + s);
    }
}
