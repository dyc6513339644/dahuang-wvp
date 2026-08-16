package com.ruoyi.common.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

/**
 * 数据库方言持有者，根据运行时数据库类型提供方言 SQL 片段。
 * 启动时自动从 DataSource 连接元数据推断数据库类型（不依赖 driverClassName 配置）。
 * <p>
 * 使用场景：Java Provider 类（如 ChannelProvider）中需要根据数据库类型
 * 生成不同的 SQL 语法（如 MySQL LIMIT vs 达梦 FETCH FIRST）。
 * <p>
 * 支持的数据库：MySQL / 达梦(DM) / SQLite / PostgreSQL
 *
 * @author ruoyi
 */
@Component
public final class DatabaseDialectHolder {

    private static volatile boolean dm = false;
    private static volatile boolean sqlite = false;
    private static volatile boolean postgresql = false;

    DatabaseDialectHolder(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            String productName = meta.getDatabaseProductName().toLowerCase();
            if (productName.contains("dm")) {
                dm = true;
            } else if (productName.contains("sqlite")) {
                sqlite = true;
            } else if (productName.contains("postgresql")) {
                postgresql = true;
            }
            // 其余默认 MySQL
        } catch (Exception e) {
            // 连接失败时默认 MySQL
        }
    }

    /**
     * 当前是否为达梦数据库。
     */
    public static boolean isDM() {
        return dm;
    }

    /**
     * 当前是否为 SQLite 数据库。
     */
    public static boolean isSQLite() {
        return sqlite;
    }

    /**
     * 当前是否为 PostgreSQL 数据库。
     */
    public static boolean isPostgreSQL() {
        return postgresql;
    }

    /**
     * 当前是否为 MySQL 数据库。
     */
    public static boolean isMySQL() {
        return !dm && !sqlite && !postgresql;
    }

    // ==================== 数据库类型字符串 ====================

    /**
     * 获取当前数据库类型字符串（用于传递到 MyBatis Mapper XML 的 params 中）。
     *
     * @return "mysql" / "dm" / "sqlite" / "postgresql"
     */
    public static String getDbType() {
        if (dm) return "dm";
        if (sqlite) return "sqlite";
        if (postgresql) return "postgresql";
        return "mysql";
    }

    // ==================== 方言 SQL 片段 ====================

    /**
     * 取一行限制子句。
     * MySQL:  LIMIT 1
     * 达梦:   FETCH FIRST 1 ROWS ONLY
     * SQLite: LIMIT 1
     */
    public static String limitOne() {
        return dm ? " FETCH FIRST 1 ROWS ONLY" : " LIMIT 1";
    }

    /**
     * FIND_IN_SET 等价条件（用于逗号分隔字符串中查找值）。
     * MySQL:  FIND_IN_SET(val, col)
     * 达梦:   INSTR(',' || col || ',', ',' || val || ',') > 0
     * SQLite: INSTR(',' || col || ',', ',' || val || ',') > 0（无 FIND_IN_SET 函数）
     * PostgreSQL: CAST(val AS varchar) = ANY(string_to_array(col, ','))（无 FIND_IN_SET 函数）
     *
     * @param col   列名表达式
     * @param param 参数占位符（如 #{deptId}）或字面量
     */
    public static String findInSet(String col, String param) {
        if (dm || sqlite) {
            return "INSTR(',' || " + col + " || ',', ',' || " + param + " || ',') > 0";
        }
        if (postgresql) {
            return "CAST(" + param + " AS varchar) = ANY(string_to_array(" + col + ", ','))";
        }
        return "FIND_IN_SET(" + param + ", " + col + ")";
    }
}
