package com.ruoyi.wvp.conf;


import com.ruoyi.wvp.gb28181.bean.SipConfigEntity;
import com.ruoyi.wvp.mapper.SipConfigMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;

/**
 * SIP配置类
 * 从数据库加载配置，如果数据库没有配置则使用application.yml中的配置作为默认值
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "sip", ignoreInvalidFields = true)
@Order(0)
@Data
public class SipConfig {

	@Autowired
	private SipConfigMapper sipConfigMapper;

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
	 * 初始化方法，从数据库加载配置
	 * 如果数据库中有配置，则覆盖application.yml中的配置
	 */
	@PostConstruct
	public void init() {
		try {
			SipConfigEntity dbConfig = sipConfigMapper.getSipConfig();
			if (dbConfig != null) {
				// 数据库有配置，使用数据库的配置覆盖application.yml的配置
				if (!ObjectUtils.isEmpty(dbConfig.getIp())) {
					this.ip = dbConfig.getIp();
				}
				if (!ObjectUtils.isEmpty(dbConfig.getShowIp())) {
					this.showIp = dbConfig.getShowIp();
				}
				if (dbConfig.getPort() != null) {
					this.port = dbConfig.getPort();
				}
				if (!ObjectUtils.isEmpty(dbConfig.getDomain())) {
					this.domain = dbConfig.getDomain();
				}
				if (!ObjectUtils.isEmpty(dbConfig.getServerId())) {
					this.id = dbConfig.getServerId();
				}
				if (!ObjectUtils.isEmpty(dbConfig.getPassword())) {
					this.password = dbConfig.getPassword();
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
				log.info("[SIP配置] 已从数据库加载SIP配置");
			} else {
				log.info("[SIP配置] 数据库中无SIP配置，使用application.yml中的配置");
			}
		} catch (Exception e) {
			log.error("[SIP配置] 从数据库加载配置失败，使用application.yml中的配置", e);
		}
	}

	/**
	 * 重新加载配置（供外部调用，如配置更新后）
	 */
	public void reload() {
		init();
	}

	/**
	 * 将当前配置同步到数据库
	 */
	public void syncToDatabase() {
		try {
			SipConfigEntity dbConfig = sipConfigMapper.getSipConfig();
			SipConfigEntity entity = new SipConfigEntity();
			entity.setIp(this.ip);
			entity.setShowIp(this.showIp);
			entity.setPort(this.port);
			entity.setDomain(this.domain);
			entity.setServerId(this.id);
			entity.setPassword(this.password);
			entity.setPtzSpeed(this.ptzSpeed);
			entity.setRegisterTimeInterval(this.registerTimeInterval);
			entity.setAlarm(this.alarm);
			entity.setTimeout(this.timeout);

			if (dbConfig != null) {
				// 更新
				entity.setId(dbConfig.getId());
				entity.setUpdateTime(java.time.LocalDateTime.now().toString());
				sipConfigMapper.update(entity);
				log.info("[SIP配置] 已更新到数据库");
			} else {
				// 新增
				entity.setCreateTime(java.time.LocalDateTime.now().toString());
				entity.setUpdateTime(entity.getCreateTime());
				sipConfigMapper.add(entity);
				log.info("[SIP配置] 已保存到数据库");
			}
		} catch (Exception e) {
			log.error("[SIP配置] 同步到数据库失败", e);
		}
	}
}
