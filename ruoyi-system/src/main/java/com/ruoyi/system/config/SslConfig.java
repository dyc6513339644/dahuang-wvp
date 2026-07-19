package com.ruoyi.system.config;

import com.ruoyi.system.domain.SysBaseConfig;
import com.ruoyi.system.mapper.SysBaseConfigMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * SSL/HTTPS动态配置类
 * 从数据库加载配置（延迟加载+超时保护），如果数据库没有配置则使用application.yml中的配置作为默认值
 *
 * @author ruoyi
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "wvp.ssl", ignoreInvalidFields = true)
@Data
public class SslConfig {

    /** 数据库配置加载超时时间（秒） */
    private static final int DB_LOAD_TIMEOUT_SECONDS = 10;

    /** 是否已从数据库加载过配置 */
    private volatile boolean dbLoaded = false;

    @Autowired
    private SysBaseConfigMapper baseConfigMapper;

    /**
     * 是否开启HTTPS访问
     */
    private boolean enabled = false;

    /**
     * 证书路径（classpath: 或绝对路径）
     */
    private String keyStore;

    /**
     * 证书密码
     */
    private String keyStorePassword;

    /**
     * HTTPS端口
     */
    private int port = 443;

    /**
     * HTTP端口（会覆盖 server.port）
     */
    private int httpPort;

    /**
     * 网站域名
     */
    private String accessDomain;

    /**
     * 确保已从数据库加载配置（延迟加载，首次调用时触发）。
     * 在独立线程中执行数据库查询，设置超时保护，
     * 避免因数据库连接池未就绪导致主线程无限阻塞。
     */
    public void ensureLoaded() {
        if (dbLoaded) {
            return;
        }
        synchronized (this) {
            if (dbLoaded) {
                return;
            }
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(this::doLoadFromDb);
            try {
                future.get(DB_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                log.warn("[SSL配置] 数据库加载超时（{}秒），使用application.yml默认配置", DB_LOAD_TIMEOUT_SECONDS);
                future.cancel(true);
            } catch (Exception e) {
                log.error("[SSL配置] 数据库加载异常，使用application.yml默认配置", e);
            } finally {
                executor.shutdownNow();
                dbLoaded = true;
            }
        }
    }

    /**
     * 实际从数据库读取SSL配置并覆盖字段值
     */
    private void doLoadFromDb() {
        try {
            List<SysBaseConfig> sysBaseConfigList= baseConfigMapper.selectList(null);
            if(sysBaseConfigList.size()>0){
                SysBaseConfig dbConfig = sysBaseConfigList.get(0);
                if (dbConfig != null) {
                    if (!ObjectUtils.isEmpty(dbConfig.getSslEnabled())) {
                        this.enabled = "1".equals(dbConfig.getSslEnabled());
                    }
                    if (!ObjectUtils.isEmpty(dbConfig.getSslKeyStore())) {
                        this.keyStore = dbConfig.getSslKeyStore();
                    }
                    if (!ObjectUtils.isEmpty(dbConfig.getSslKeyStorePassword())) {
                        this.keyStorePassword = dbConfig.getSslKeyStorePassword();
                    }
                    if (dbConfig.getSslPort() != null && dbConfig.getSslPort() > 0) {
                        this.port = dbConfig.getSslPort();
                    }
                    if (dbConfig.getHttpPort() != null && dbConfig.getHttpPort() > 0) {
                        this.httpPort = dbConfig.getHttpPort();
                    }
                    if (!ObjectUtils.isEmpty(dbConfig.getAccessDomain())) {
                        this.accessDomain = dbConfig.getAccessDomain();
                    }
                }
            }
        } catch (Exception e) {
            log.error("[SSL配置] 从数据库加载配置失败，使用application.yml中的配置", e);
        }
    }

    /**
     * 重新加载配置（供外部调用，如配置更新后）
     */
    public void reload() {
        dbLoaded = false;
        ensureLoaded();
    }

}
