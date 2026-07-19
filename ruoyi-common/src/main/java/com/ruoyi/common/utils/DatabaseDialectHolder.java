package com.ruoyi.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据库方言持有者，根据运行时数据库类型提供方言 SQL 片段。
 * 启动时自动从 spring.datasource.driver-class-name 推断数据库类型。
 * <p>
 * 使用场景：Java Provider 类（如 ChannelProvider）中需要根据数据库类型
 * 生成不同的 SQL 语法（如 MySQL LIMIT vs 达梦 FETCH FIRST）。
 *
 * @author ruoyi
 */
@Component
public final class DatabaseDialectHolder {

    private static volatile boolean dm = false;

    DatabaseDialectHolder(@Value("${spring.datasource.driver-class-name:}") String driverClassName) {
        dm = "dm".equalsIgnoreCase(resolveDbType(driverClassName));
    }

    /**
     * 当前是否为达梦数据库。
     */
    public static boolean isDM() {
        return dm;
    }

    // ==================== 数据库类型推断 ====================

    /**
     * 根据 JDBC 驱动类名推断数据库类型。
     *
     * @param driverClassName 驱动全限定名（如 com.mysql.cj.jdbc.Driver）
     * @return dbType 字符串（mysql / dm / oracle / postgresql），默认 mysql
     */
    public static String resolveDbType(String driverClassName) {
        if (driverClassName == null || driverClassName.isEmpty()) {
            return "mysql";
        }
        String lower = driverClassName.toLowerCase();
        if (lower.contains("mysql")) return "mysql";
        if (lower.contains("dm")) return "dm";
        if (lower.contains("oracle")) return "oracle";
        if (lower.contains("postgresql")) return "postgresql";
        return "mysql";
    }

    // ==================== 方言 SQL 片段 ====================

    /**
     * 取一行限制子句。
     * MySQL: LIMIT 1
     * 达梦:  FETCH FIRST 1 ROWS ONLY
     */
    public static String limitOne() {
        return dm ? " FETCH FIRST 1 ROWS ONLY" : " LIMIT 1";
    }

    /**
     * FIND_IN_SET 等价条件（用于逗号分隔字符串中查找值）。
     * MySQL: FIND_IN_SET(val, col)
     * 达梦:  INSTR(',' || col || ',', ',' || val || ',') > 0
     *
     * @param col  列名表达式
     * @param param 参数占位符（如 #{deptId}）
     */
    public static String findInSet(String col, String param) {
        if (dm) {
            return "INSTR(',' || " + col + " || ',', ',' || " + param + " || ',') > 0";
        }
        return "FIND_IN_SET(" + param + ", " + col + ")";
    }
}
