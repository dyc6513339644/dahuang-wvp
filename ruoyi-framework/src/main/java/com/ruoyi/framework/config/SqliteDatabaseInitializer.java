package com.ruoyi.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * SQLite 内置库引导
 * <p>本类在容器刷新最开始（任何 Bean 实例化之前、连接池尚未打开文件）执行：
 * 解析出 SQLite 实际落盘的绝对路径，若文件不存在或为空，从 classpath 的内置种子库复制一份过去。
 * 这样相对路径可以放心使用，同时首次启动即自带完整表结构和初始数据。
 * @author ruoyi
 */
@Component
public class SqliteDatabaseInitializer implements BeanFactoryPostProcessor, EnvironmentAware
{
    private static final Logger log = LoggerFactory.getLogger(SqliteDatabaseInitializer.class);

    /** 主库数据源 URL 配置项 */
    private static final String MASTER_URL_KEY = "spring.datasource.druid.master.url";

    /** SQLite JDBC URL 前缀 */
    private static final String SQLITE_PREFIX = "jdbc:sqlite:";

    /** 内置种子库（打包进 jar / classpath） */
    private static final String SEED_RESOURCE = "wvp.db";

    /** 容器环境，用于读取已合并 profile 的配置 */
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment)
    {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        String url = resolveSqliteUrl(beanFactory);
        if (url == null)
        {
            return;
        }

        Path dbPath = resolveDbPath(url);
        if (dbPath == null)
        {
            return;
        }

        if (isUsableDatabase(dbPath))
        {
            log.debug("[SQLite] 使用数据库文件: {}", dbPath);
            return;
        }

        seedDatabase(dbPath);
    }

    /**
     * 获取当前生效的 SQLite 连接串，非 SQLite 或未配置时返回 null。
     * 优先从 Environment 读取（已合并 profile 配置），拿不到再回退到直接查容器。
     */
    private String resolveSqliteUrl(ConfigurableListableBeanFactory beanFactory)
    {
        String url = null;
        if (environment != null)
        {
            url = environment.getProperty(MASTER_URL_KEY);
        }
        if (url == null)
        {
            try
            {
                Environment beanFactoryEnv = beanFactory.getBean(Environment.class);
                url = beanFactoryEnv.getProperty(MASTER_URL_KEY);
            }
            catch (Exception e)
            {
                log.error("[SQLite] 无法获取 Environment，跳过内置库初始化", e);
            }
        }

        if (url == null)
        {
            return null;
        }
        url = url.trim();
        return url.startsWith(SQLITE_PREFIX) ? url : null;
    }

    /**
     * 把 SQLite URL 转成绝对文件路径；内存库、URI 形式或空路径返回 null。
     */
    private Path resolveDbPath(String url)
    {
        String raw = url.substring(SQLITE_PREFIX.length());
        // 去掉 ?busy_timeout=... 之类的驱动参数
        int queryIndex = raw.indexOf('?');
        if (queryIndex >= 0)
        {
            raw = raw.substring(0, queryIndex);
        }
        raw = raw.trim();

        if (raw.isEmpty() || ":memory:".equalsIgnoreCase(raw) || raw.startsWith("file:"))
        {
            return null;
        }

        try
        {
            // 相对路径按进程工作目录解析，与 SQLite 驱动自身的行为保持一致
            return Paths.get(raw).toAbsolutePath().normalize();
        }
        catch (Exception e)
        {
            log.error("[SQLite] 无法解析数据库路径: {}", raw, e);
            return null;
        }
    }

    /**
     * 文件存在且非空才认为是可用库；0 字节文件视为损坏的空库。
     */
    private boolean isUsableDatabase(Path dbPath)
    {
        try
        {
            return Files.isRegularFile(dbPath) && Files.size(dbPath) > 0L;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * 从 classpath 种子库复制到目标位置。
     */
    private void seedDatabase(Path dbPath)
    {
        ClassPathResource seed = new ClassPathResource(SEED_RESOURCE);
        if (!seed.exists())
        {
            log.warn("[SQLite] 库文件不存在且未找到内置种子库 {}，将由驱动创建空库", SEED_RESOURCE);
            return;
        }

        try
        {
            Path parent = dbPath.getParent();
            if (parent != null)
            {
                Files.createDirectories(parent);
            }
            try (InputStream in = seed.getInputStream())
            {
                Files.copy(in, dbPath, StandardCopyOption.REPLACE_EXISTING);
            }
            log.debug("[SQLite] 已从内置种子库初始化数据库文件: {}", dbPath);
        }
        catch (Exception e)
        {
            log.error("[SQLite] 初始化数据库文件失败: {}", dbPath, e);
        }
    }
}
