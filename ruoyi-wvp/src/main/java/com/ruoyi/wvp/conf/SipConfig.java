package com.ruoyi.wvp.conf;


import com.ruoyi.wvp.gb28181.bean.SipConfigEntity;
import com.ruoyi.wvp.mapper.SipConfigMapper;
import com.ruoyi.wvp.service.ISipConfigService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * SIP配置类
 * 从数据库加载配置（延迟加载+超时保护），如果数据库没有配置则使用application.yml中的配置作为默认值
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "sip", ignoreInvalidFields = true)
@Data
public class SipConfig {

    /** 数据库配置加载超时时间（秒） */
    private static final int DB_LOAD_TIMEOUT_SECONDS = 10;

    /** 是否已从数据库加载过配置 */
    private volatile boolean dbLoaded = false;

	@Autowired
	private ISipConfigService sipConfigService;

	/**
	 * SIP服务监听的IP地址
	 */
	private String ip;

	/**
	 * 对外展示的IP地址
	 */
	private String showIp;

	/**
	 * SIP服务监听的端口
	 */
	private Integer port;

	/**
	 * 国标域
	 */
	private String domain;

	/**
	 * 国标ID（对应配置文件中的id）
	 */
	private String id;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 云台速度
	 */
	Integer ptzSpeed = 50;

	/**
	 * 注册时间间隔（秒）
	 */
	Integer registerTimeInterval = 120;

	/**
	 * 是否接收报警信息
	 */
	private boolean alarm = false;

	/**
	 * 超时时间（秒）
	 */
	private long timeout = 15;

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
				future.cancel(true);
			} catch (Exception e) {
				log.error("[SIP配置] 数据库加载异常，使用application.yml默认配置", e);
			} finally {
				executor.shutdownNow();
				dbLoaded = true;
			}
		}
	}

	/**
	 * 实际从数据库读取SIP配置并覆盖字段值
	 */
	private void doLoadFromDb() {
		try {
			SipConfigEntity dbConfig = sipConfigService.getSipConfig();
			if (dbConfig != null) {
				if (!ObjectUtils.isEmpty(dbConfig.getIp())) {
					this.ip = dbConfig.getIp();
				}
				if (!ObjectUtils.isEmpty(dbConfig.getShowIp())) {
					this.showIp = dbConfig.getShowIp();
				}
				if (dbConfig.getPort() != null) {
					this.port = dbConfig.getPort();
				}
				if (!ObjectUtils.isEmpty(dbConfig.getGbDomain())) {
					this.domain = dbConfig.getGbDomain();
				}
				if (!ObjectUtils.isEmpty(dbConfig.getServerId())) {
					this.id = dbConfig.getServerId();
				}
				if (!ObjectUtils.isEmpty(dbConfig.getGbPwd())) {
					this.password = dbConfig.getGbPwd();
				}
				if (dbConfig.getPtzSpeed() != null) {
					this.ptzSpeed = dbConfig.getPtzSpeed();
				}
				if (dbConfig.getRegisterTimeInterval() != null) {
					this.registerTimeInterval = dbConfig.getRegisterTimeInterval();
				}
				if (dbConfig.getAlarm() != null) {
					this.alarm = dbConfig.getAlarm();
				}
				if (dbConfig.getTimeout() != null) {
					this.timeout = dbConfig.getTimeout();
				}
			}
		} catch (Exception e) {
			log.error("[SIP配置] 从数据库加载配置失败，使用application.yml中的配置", e);
		}
	}

	/**
	 * 初始化方法（保持兼容），委托到 ensureLoaded()
	 */
	public void init() {
		ensureLoaded();
	}

	/**
	 * 重新加载配置（供外部调用，如配置更新后）
	 */
	public void reload() {
		dbLoaded = false;
		ensureLoaded();
	}

	/**
	 * 将当前配置同步到数据库
	 */
	public void syncToDatabase() {
		ensureLoaded();
		try {
			SipConfigEntity dbConfig = sipConfigService.getSipConfig();
			SipConfigEntity entity = new SipConfigEntity();
			entity.setIp(this.ip);
			entity.setShowIp(this.showIp);
			entity.setPort(this.port);
			entity.setGbDomain(this.domain);
			entity.setServerId(this.id);
			entity.setGbPwd(this.password);
			entity.setPtzSpeed(this.ptzSpeed);
			entity.setRegisterTimeInterval(this.registerTimeInterval);
			entity.setAlarm(this.alarm);
			entity.setTimeout(this.timeout);

			if (dbConfig != null) {
				// 更新
				entity.setId(dbConfig.getId());
				entity.setUpdateTime(java.time.LocalDateTime.now().toString());
                sipConfigService.update(entity);
			} else {
				// 新增
				entity.setCreateTime(java.time.LocalDateTime.now().toString());
				entity.setUpdateTime(entity.getCreateTime());
                sipConfigService.save(entity);
			}
		} catch (Exception e) {
			log.error("[SIP配置] 同步到数据库失败", e);
		}
	}
}
