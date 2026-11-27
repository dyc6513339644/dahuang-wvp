PRAGMA foreign_keys = OFF;

-- Converted from MySQL dump for SQLite

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` INTEGER NOT NULL,
  `table_name` TEXT NULL DEFAULT '',
  `table_comment` TEXT NULL DEFAULT '',
  `sub_table_name` TEXT NULL DEFAULT NULL,
  `sub_table_fk_name` TEXT NULL DEFAULT NULL,
  `class_name` TEXT NULL DEFAULT '',
  `tpl_category` TEXT NULL DEFAULT 'crud',
  `tpl_web_type` TEXT NULL DEFAULT '',
  `package_name` TEXT NULL DEFAULT NULL,
  `module_name` TEXT NULL DEFAULT NULL,
  `business_name` TEXT NULL DEFAULT NULL,
  `function_name` TEXT NULL DEFAULT NULL,
  `function_author` TEXT NULL DEFAULT NULL,
  `gen_type` TEXT NULL DEFAULT '0',
  `gen_path` TEXT NULL DEFAULT '/',
  `options` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`table_id`)
);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` INTEGER NOT NULL,
  `table_id` INTEGER NULL DEFAULT NULL,
  `column_name` TEXT NULL DEFAULT NULL,
  `column_comment` TEXT NULL DEFAULT NULL,
  `column_type` TEXT NULL DEFAULT NULL,
  `java_type` TEXT NULL DEFAULT NULL,
  `java_field` TEXT NULL DEFAULT NULL,
  `is_pk` TEXT NULL DEFAULT NULL,
  `is_increment` TEXT NULL DEFAULT NULL,
  `is_required` TEXT NULL DEFAULT NULL,
  `is_insert` TEXT NULL DEFAULT NULL,
  `is_edit` TEXT NULL DEFAULT NULL,
  `is_list` TEXT NULL DEFAULT NULL,
  `is_query` TEXT NULL DEFAULT NULL,
  `query_type` TEXT NULL DEFAULT 'EQ',
  `html_type` TEXT NULL DEFAULT NULL,
  `dict_type` TEXT NULL DEFAULT '',
  `sort` INTEGER NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`column_id`)
);

-- ----------------------------
-- Table structure for isup_device
-- ----------------------------
DROP TABLE IF EXISTS `isup_device`;
CREATE TABLE `isup_device`  (
  `id` INTEGER NOT NULL,
  `dept_id` INTEGER NULL DEFAULT NULL,
  `device_id` TEXT NOT NULL,
  `name` TEXT NULL DEFAULT NULL,
  `channel` TEXT NULL DEFAULT NULL,
  `play_type` TEXT NULL DEFAULT NULL,
  `url` TEXT NULL DEFAULT NULL,
  `stream_id` TEXT NULL DEFAULT NULL,
  `easy_NTS_url` TEXT NULL DEFAULT NULL,
  `dw_size` INTEGER NULL DEFAULT NULL,
  `dw_net_unit_type` INTEGER NULL DEFAULT NULL,
  `firmware_version` TEXT NULL DEFAULT NULL,
  `ip_address` TEXT NULL DEFAULT NULL,
  `port` INTEGER NULL DEFAULT NULL,
  `device_res` TEXT NULL DEFAULT NULL,
  `dev_type` INTEGER NULL DEFAULT NULL,
  `manufacture` INTEGER NULL DEFAULT NULL,
  `user_name` TEXT NULL DEFAULT NULL,
  `password` TEXT NULL DEFAULT NULL,
  `device_serial` TEXT NULL DEFAULT NULL,
  `reliable_transmission` INTEGER NULL DEFAULT NULL,
  `websocket_transmission` INTEGER NULL DEFAULT NULL,
  `support_redirect` INTEGER NULL DEFAULT NULL,
  `dev_protocol_version` TEXT NULL DEFAULT NULL,
  `session_key` TEXT NULL DEFAULT NULL,
  `res` TEXT NULL DEFAULT NULL,
  `market_type` INTEGER NULL DEFAULT NULL,
  `luser_id` INTEGER NULL DEFAULT NULL,
  `status` TEXT NULL DEFAULT '1',
  `lat` TEXT NULL DEFAULT NULL,
  `lng` TEXT NULL DEFAULT NULL,
  `address_map` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` TEXT NOT NULL,
  `trigger_name` TEXT NOT NULL,
  `trigger_group` TEXT NOT NULL,
  `blob_data` BLOB NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`)
  /* FK removed for SQLite: CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT */
);

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` TEXT NOT NULL,
  `calendar_name` TEXT NOT NULL,
  `calendar` BLOB NOT NULL,
  PRIMARY KEY (`sched_name`, `calendar_name`)
);

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` TEXT NOT NULL,
  `trigger_name` TEXT NOT NULL,
  `trigger_group` TEXT NOT NULL,
  `cron_expression` TEXT NOT NULL,
  `time_zone_id` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`)
  /* FK removed for SQLite: CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT */
);

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` TEXT NOT NULL,
  `entry_id` TEXT NOT NULL,
  `trigger_name` TEXT NOT NULL,
  `trigger_group` TEXT NOT NULL,
  `instance_name` TEXT NOT NULL,
  `fired_time` INTEGER NOT NULL,
  `sched_time` INTEGER NOT NULL,
  `priority` INTEGER NOT NULL,
  `state` TEXT NOT NULL,
  `job_name` TEXT NULL DEFAULT NULL,
  `job_group` TEXT NULL DEFAULT NULL,
  `is_nonconcurrent` TEXT NULL DEFAULT NULL,
  `requests_recovery` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `entry_id`)
);

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` TEXT NOT NULL,
  `job_name` TEXT NOT NULL,
  `job_group` TEXT NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `job_class_name` TEXT NOT NULL,
  `is_durable` TEXT NOT NULL,
  `is_nonconcurrent` TEXT NOT NULL,
  `is_update_data` TEXT NOT NULL,
  `requests_recovery` TEXT NOT NULL,
  `job_data` BLOB NULL,
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`)
);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` TEXT NOT NULL,
  `lock_name` TEXT NOT NULL,
  PRIMARY KEY (`sched_name`, `lock_name`)
);

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` TEXT NOT NULL,
  `trigger_group` TEXT NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_group`)
);

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` TEXT NOT NULL,
  `instance_name` TEXT NOT NULL,
  `last_checkin_time` INTEGER NOT NULL,
  `checkin_interval` INTEGER NOT NULL,
  PRIMARY KEY (`sched_name`, `instance_name`)
);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` TEXT NOT NULL,
  `trigger_name` TEXT NOT NULL,
  `trigger_group` TEXT NOT NULL,
  `repeat_count` INTEGER NOT NULL,
  `repeat_interval` INTEGER NOT NULL,
  `times_triggered` INTEGER NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`)
  /* FK removed for SQLite: CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT */
);

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` TEXT NOT NULL,
  `trigger_name` TEXT NOT NULL,
  `trigger_group` TEXT NOT NULL,
  `str_prop_1` TEXT NULL DEFAULT NULL,
  `str_prop_2` TEXT NULL DEFAULT NULL,
  `str_prop_3` TEXT NULL DEFAULT NULL,
  `int_prop_1` INTEGER NULL DEFAULT NULL,
  `int_prop_2` INTEGER NULL DEFAULT NULL,
  `long_prop_1` INTEGER NULL DEFAULT NULL,
  `long_prop_2` INTEGER NULL DEFAULT NULL,
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL,
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL,
  `bool_prop_1` TEXT NULL DEFAULT NULL,
  `bool_prop_2` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`)
  /* FK removed for SQLite: CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT */
);

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` TEXT NOT NULL,
  `trigger_name` TEXT NOT NULL,
  `trigger_group` TEXT NOT NULL,
  `job_name` TEXT NOT NULL,
  `job_group` TEXT NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `next_fire_time` INTEGER NULL DEFAULT NULL,
  `prev_fire_time` INTEGER NULL DEFAULT NULL,
  `priority` INTEGER NULL DEFAULT NULL,
  `trigger_state` TEXT NOT NULL,
  `trigger_type` TEXT NOT NULL,
  `start_time` INTEGER NOT NULL,
  `end_time` INTEGER NULL DEFAULT NULL,
  `calendar_name` TEXT NULL DEFAULT NULL,
  `misfire_instr` INTEGER NULL DEFAULT NULL,
  `job_data` BLOB NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`)
  /* FK removed for SQLite: CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT */
);
CREATE INDEX IF NOT EXISTS "sched_name" ON "qrtz_triggers" ("sched_name", "job_name", "job_group");

-- ----------------------------
-- Table structure for sys_agreement
-- ----------------------------
DROP TABLE IF EXISTS `sys_agreement`;
CREATE TABLE `sys_agreement`  (
  `agreement_id` INTEGER NOT NULL,
  `agreement_type` TEXT NOT NULL,
  `agreement_name` TEXT NULL DEFAULT NULL,
  `content` TEXT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`agreement_id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_agreement_type" ON "sys_agreement" ("agreement_type");

-- ----------------------------
-- Records of sys_agreement
-- ----------------------------
INSERT INTO `sys_agreement` VALUES (1, 'agreement', '用户协议', '<p>本协议是您与本应用运营方之间关于使用本软件所订立的协议。请您在使用前仔细阅读，使用本软件即视为同意本协议全部条款。</p><p>一、服务内容<br/>1. 本应用为视频监控综合管理平台的移动客户端，提供设备查看、通道预览、云台控制、录像回放、报警消息查看等功能。<br/>2. 部分功能需要配合服务端与流媒体服务使用，具体功能以您所在单位实际部署为准。</p><p>二、账号与使用<br/>1. 账号由所在单位管理员分配，您应妥善保管账号与密码，因个人原因泄露造成的损失由您自行承担。<br/>2. 请勿将账号出借他人使用；发现账号异常应立即修改密码并联系管理员。<br/>3. 您应在授权范围内访问设备与视频资源，不得越权访问他人数据。</p><p>三、使用规范<br/>您承诺不利用本应用从事以下行为：<br/>1. 破解、反向工程、非法抓取平台数据；<br/>2. 传播病毒、恶意代码或干扰平台正常运行；<br/>3. 未经授权录制、复制、传播监控视频内容；<br/>4. 违反法律法规、侵犯他人合法权益的其他行为。</p><p>四、知识产权<br/>本应用及其包含的源代码、界面设计、文档、商标等知识产权归运营方所有，未经书面许可不得复制、修改、传播或用于商业用途。</p><p>五、免责声明<br/>1. 视频监控服务的可用性受网络环境、设备状态、服务器负载等因素影响，我们不保证服务不中断。<br/>2. 因不可抗力（自然灾害、网络攻击、基础电信故障等）导致服务中断或数据损失，我们不承担责任。<br/>3. 本应用提供的监控画面仅供参考，不构成任何法律证据或事实认定依据。</p><p>六、协议变更与终止<br/>1. 我们有权在必要时修订本协议，修订后在应用内公示。<br/>2. 若您违反本协议任一约定，我们有权暂停或终止向您提供服务。</p><p>七、其他<br/>本协议适用中华人民共和国法律。如发生争议，双方应友好协商解决。</p>', 'admin', '2026-09-14 09:56:38', 'admin', '2026-09-14 09:56:38', NULL);
INSERT INTO `sys_agreement` VALUES (2, 'privacy', '隐私政策', '<p>本政策说明本应用如何收集、使用与保护您的个人信息。请您在使用前仔细阅读。</p><p>一、我们收集的信息<br/>1. 账号信息：您在登录时使用的用户名、密码（传输过程加密）等。<br/>2. 设备与日志信息：为保障服务稳定，我们会记录设备型号、系统版本、网络环境及操作日志。<br/>3. 业务数据：您在使用视频监控功能时产生的设备信息、通道信息、报警记录与录像记录。</p><p>二、信息的使用<br/>1. 用于完成身份认证、权限校验与账号安全管理。<br/>2. 用于提供视频点播、云台控制、录像回放、报警推送等核心功能。<br/>3. 用于排查故障、优化性能与改进产品体验。<br/>4. 法律法规要求或经您授权的其他用途。</p><p>三、信息的共享与披露<br/>我们不会向任何第三方出售您的个人信息。仅在以下情形共享：<br/>1. 获得您的明确同意；<br/>2. 为提供服务所必需（如流媒体服务节点、云存储服务）；<br/>3. 依据法律法规或监管部门强制性要求。</p><p>四、信息的存储与安全<br/>1. 您的数据存储于您所在单位部署的服务器中，我们采取加密传输、访问控制、权限分级等措施保护数据安全。<br/>2. 视频数据属于敏感信息，仅授权用户可访问，所有访问行为均有日志记录。<br/>3. 我们会按与您约定的期限保留数据，超期后按策略清理。</p><p>五、您的权利<br/>您有权访问、更正、删除您的个人信息，也有权撤回已授予的权限。可通过本应用「关于我们」中的联系方式或联系系统管理员处理。</p><p>六、未成年人信息保护<br/>本应用面向企业及行业用户，不面向未成年人提供服务。</p><p>七、政策更新<br/>本政策如有更新，我们会在应用内提示。继续使用即视为接受更新后的政策。</p>', 'admin', '2026-09-14 09:56:38', 'admin', '2026-09-14 09:56:38', NULL);

-- ----------------------------
-- Table structure for sys_base_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_base_config`;
CREATE TABLE `sys_base_config`  (
  `config_id` INTEGER NOT NULL,
  `ssl_enabled` TEXT NULL DEFAULT '0',
  `ssl_key_store` TEXT NULL DEFAULT '',
  `ssl_key_store_password` TEXT NULL DEFAULT NULL,
  `ssl_port` INTEGER NULL DEFAULT NULL,
  `http_port` TEXT NULL DEFAULT NULL,
  `access_domain` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`config_id`)
);

-- ----------------------------
-- Records of sys_base_config
-- ----------------------------
INSERT INTO `sys_base_config` VALUES (1, '0', 'classpath:httpsKey.p12', '123456', 444, '28081', '', 'admin', '2026-06-29 20:39:15', 'admin', '2026-08-03 19:34:15', NULL);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` INTEGER NOT NULL,
  `config_name` TEXT NULL DEFAULT '',
  `config_key` TEXT NULL DEFAULT '',
  `config_value` TEXT NULL DEFAULT '',
  `config_type` TEXT NULL DEFAULT 'N',
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`config_id`)
);

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2025-04-03 09:03:39', 'admin', '2026-10-19 21:38:00', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO `sys_config` VALUES (19, '高德地图KEY ', 'gaode_map_key', '88f28edf21df134b8b4c45ae6b87761a', 'Y', 'admin', '2026-06-05 22:21:50', 'admin', '2026-10-09 21:25:19', NULL);
INSERT INTO `sys_config` VALUES (20, '高德地图安全秘钥', 'gaode_map_security_code', '082ed5b08a0c94f565964ce946e260dd', 'Y', 'admin', '2026-06-05 22:21:50', 'admin', '2026-10-09 21:25:19', NULL);
INSERT INTO `sys_config` VALUES (21, '高德地图中心点坐标', 'gaode_map_center', '40.09,116.24', 'Y', 'admin', '2026-06-05 22:25:38', 'admin', '2026-10-09 21:25:19', NULL);
INSERT INTO `sys_config` VALUES (22, '网站名称', 'plat_name', '大荒安防', 'Y', 'admin', '2026-06-22 17:37:07', '', NULL, NULL);
INSERT INTO `sys_config` VALUES (23, '演示信息开关', 'sys.demo.enabled', 'true', 'Y', 'admin', '2026-07-16 18:05:33', 'admin', '2026-07-16 18:26:50', NULL);
INSERT INTO `sys_config` VALUES (24, '用户管理-初始密码修改策略', 'sys.account.initPasswordModify', '1', 'Y', 'admin', '2026-08-05 16:55:03', '', '2026-08-05 16:55:03', '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
INSERT INTO `sys_config` VALUES (25, '用户管理-账号密码更新周期', 'sys.account.passwordValidateDays', '0', 'Y', 'admin', '2026-08-05 16:55:31', 'admin', '2026-08-05 20:58:19', '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
INSERT INTO `sys_config` VALUES (26, '后台登录页Logo', 'site_logo_login', '', 'Y', 'admin', '2026-08-06 20:45:43', 'admin', '2026-08-06 21:19:15', NULL);
INSERT INTO `sys_config` VALUES (27, '后台小Logo', 'site_logo_square', '', 'Y', 'admin', '2026-08-06 20:45:43', 'admin', '2026-08-06 21:19:15', NULL);
INSERT INTO `sys_config` VALUES (28, '后台大Logo', 'site_logo_lefttop', '', 'Y', 'admin', '2026-08-06 20:45:43', 'admin', '2026-08-06 21:19:15', NULL);
INSERT INTO `sys_config` VALUES (29, '移动端登录Logo', 'mobile_login_logo', '', 'Y', 'admin', '2026-08-06 20:45:43', 'admin', '2026-08-06 21:19:15', NULL);
INSERT INTO `sys_config` VALUES (30, '系统ICO图标', 'site_favicon', '', 'Y', 'admin', '2026-08-06 20:45:43', 'admin', '2026-08-06 21:19:15', NULL);
INSERT INTO `sys_config` VALUES (31, '地图类型', 'map_type', 'tianditu', 'Y', 'admin', '2026-10-09 21:25:19', '', '2026-10-09 21:25:19', NULL);
INSERT INTO `sys_config` VALUES (32, '天地图KEY', 'tianditu_map_key', '67ffe2f1dc1f3fcf743bbc9e6396de0e', 'Y', 'admin', '2026-10-09 21:25:19', '', '2026-10-09 21:25:19', NULL);
INSERT INTO `sys_config` VALUES (33, '天地图安全秘钥', 'tianditu_map_security_code', '67ffe2f1dc1f3fcf743bbc9e6396de0e', 'Y', 'admin', '2026-10-09 21:25:19', '', '2026-10-09 21:25:19', NULL);
INSERT INTO `sys_config` VALUES (34, '天地图中心点坐标', 'tianditu_map_center', '40.09,116.24', 'Y', 'admin', '2026-10-09 21:25:19', '', '2026-10-09 21:25:19', NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` INTEGER NOT NULL,
  `parent_id` INTEGER NULL DEFAULT 0,
  `ancestors` TEXT NULL DEFAULT '',
  `dept_name` TEXT NULL DEFAULT '',
  `order_num` INTEGER NULL DEFAULT 0,
  `leader` TEXT NULL DEFAULT NULL,
  `phone` TEXT NULL DEFAULT NULL,
  `email` TEXT NULL DEFAULT NULL,
  `status` TEXT NULL DEFAULT '0',
  `del_flag` TEXT NULL DEFAULT '0',
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
);

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '监控平台', 0, 'ry-wvp', '15888888888', 'dhwvp@qq.com', '0', '0', 'admin', '2025-04-03 09:03:39', 'admin', '2026-08-02 21:44:01', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` INTEGER NOT NULL,
  `dict_sort` INTEGER NULL DEFAULT 0,
  `dict_label` TEXT NULL DEFAULT '',
  `dict_value` TEXT NULL DEFAULT '',
  `dict_type` TEXT NULL DEFAULT '',
  `css_class` TEXT NULL DEFAULT NULL,
  `list_class` TEXT NULL DEFAULT NULL,
  `is_default` TEXT NULL DEFAULT 'N',
  `status` TEXT NULL DEFAULT '0',
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`dict_code`)
);

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (34, 1, '本地', '1', 'play_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-21 05:10:05', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (35, 2, '推流', '2', 'play_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-21 05:10:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (36, 3, 'EasyNTS', '3', 'play_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-21 05:10:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (39, 1, '在线', '1', 'yingshi_online_status', NULL, 'success', 'N', '0', 'admin', '2025-07-31 15:20:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (40, 0, '离线', '0', 'yingshi_online_status', NULL, 'danger', 'N', '0', 'admin', '2025-07-31 15:20:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (41, 1, '高清（主码流）', '1', 'yingshi_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:23:02', 'admin', '2025-07-31 17:23:23', NULL);
INSERT INTO `sys_dict_data` VALUES (42, 2, '流畅（子码流）', '2', 'yingshi_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:23:12', 'admin', '2025-07-31 17:23:31', NULL);
INSERT INTO `sys_dict_data` VALUES (43, 1, '预览', '1', 'yingshi_address_type', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:23:45', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (44, 2, '本地录像回放', '2', 'yingshi_address_type', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:23:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (45, 3, '云存储录像回放', '3', 'yingshi_address_type', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:24:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (46, 0, '不在线', '0', 'yingshi_monitoring_status', NULL, 'default', 'N', '0', 'admin', '2025-07-31 18:09:48', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (47, 1, '在线', '1', 'yingshi_monitoring_status', NULL, 'success', 'N', '0', 'admin', '2025-07-31 18:09:57', 'admin', '2025-07-31 18:10:02', NULL);
INSERT INTO `sys_dict_data` VALUES (48, 0, '隐藏', '0', 'yingshi_camera_is_add', NULL, 'default', 'N', '0', 'admin', '2025-07-31 18:10:48', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (49, 1, '显示', '1', 'yingshi_camera_is_add', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:11:00', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (50, 0, '流畅', '0', 'yingshi_camera_video_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:12:00', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (51, 1, '均衡', '1', 'yingshi_camera_video_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:12:08', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (52, 2, '高清', '2', 'yingshi_camera_video_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:12:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (53, 3, '超清', '3', 'yingshi_camera_video_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:12:27', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (54, 0, '不加密', '0', 'yingshi_camera_is_encrypt', NULL, 'default', 'N', '0', 'admin', '2025-07-31 18:13:23', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (55, 1, '加密', '1', 'yingshi_camera_is_encrypt', NULL, 'default', 'N', '0', 'admin', '2025-07-31 18:13:29', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (61, 4, 'SDK播放', '4', 'play_type', NULL, 'primary', 'N', '0', 'admin', '2025-09-22 12:44:21', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (62, 0, '报警输入（134）', '134', 'filter_channel_type', NULL, NULL, 'N', '0', 'admin', '2026-08-09 21:06:11', '', '2026-08-09 21:06:11', NULL);
INSERT INTO `sys_dict_data` VALUES (63, 1, '报警输出（135）', '135', 'filter_channel_type', NULL, NULL, 'N', '0', 'admin', '2026-08-09 21:06:35', '', '2026-08-09 21:06:35', NULL);
INSERT INTO `sys_dict_data` VALUES (64, 2, '语音输入（136）', '136', 'filter_channel_type', NULL, NULL, 'N', '0', 'admin', '2026-08-09 21:06:52', '', '2026-08-09 21:06:52', NULL);
INSERT INTO `sys_dict_data` VALUES (65, 4, '语音输出（137）', '137', 'filter_channel_type', NULL, NULL, 'N', '0', 'admin', '2026-08-09 21:07:15', '', '2026-08-09 21:07:15', NULL);
INSERT INTO `sys_dict_data` VALUES (66, 5, '中心信令（200）', '200', 'filter_channel_type', NULL, NULL, 'N', '0', 'admin', '2026-08-09 21:07:36', '', '2026-08-09 21:07:36', NULL);
INSERT INTO `sys_dict_data` VALUES (67, 6, '业务分组（215）', '215', 'filter_channel_type', NULL, NULL, 'N', '0', 'admin', '2026-08-09 21:07:57', '', '2026-08-09 21:07:57', NULL);
INSERT INTO `sys_dict_data` VALUES (68, 7, '虚拟组织（216）', '216', 'filter_channel_type', NULL, NULL, 'N', '0', 'admin', '2026-08-09 21:08:19', '', '2026-08-09 21:08:19', NULL);
INSERT INTO `sys_dict_data` VALUES (69, 1, '视频丢失', 'video_loss', 'alarm_type', NULL, 'danger', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '紧急');
INSERT INTO `sys_dict_data` VALUES (70, 2, '设备防拆/视频遮挡', 'tamper', 'alarm_type', NULL, 'danger', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '紧急');
INSERT INTO `sys_dict_data` VALUES (71, 3, '存储磁盘满', 'disk_full', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (72, 4, '存储设备故障', 'disk_fault', 'alarm_type', NULL, 'danger', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '紧急');
INSERT INTO `sys_dict_data` VALUES (73, 5, '设备高温', 'temp_high', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (74, 6, '设备低温', 'temp_low', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (75, 7, '移动侦测', 'motion', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (76, 8, '区域入侵', 'intrusion', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (77, 9, '越界检测', 'cross_line', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (78, 10, '物品遗留', 'object_leave', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (79, 11, '物品移除', 'object_remove', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (80, 12, '逆行检测', 'retrograde', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (81, 13, '徘徊检测', 'loiter', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (82, 14, '流量统计', 'traffic', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (83, 15, '密度检测', 'density', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (84, 16, '视频异常', 'video_abnormal', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (85, 17, '快速移动', 'fast_move', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (86, 18, '人脸检测', 'face', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (87, 19, '人脸抓拍', 'face_capture', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (88, 20, '人体检测', 'person', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (89, 21, '车辆检测', 'vehicle', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (90, 22, '行为分析', 'behavior', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (91, 23, '物品检测', 'object', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (92, 24, '人群密度', 'crowd', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (93, 25, '目标跟踪', 'track', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (94, 26, '人工报警', 'manual', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (95, 27, '门禁报警', 'access', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (96, 28, 'GPS报警', 'gps', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (97, 29, '设备故障', 'device_fault', 'alarm_type', NULL, 'danger', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '紧急');
INSERT INTO `sys_dict_data` VALUES (98, 30, '录像异常', 'record_abnormal', 'alarm_type', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '重要');
INSERT INTO `sys_dict_data` VALUES (99, 31, '外来报警', 'external', 'alarm_type', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '次要');
INSERT INTO `sys_dict_data` VALUES (100, 32, '其他报警', 'other', 'alarm_type', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '提示');
INSERT INTO `sys_dict_data` VALUES (132, 1, '紧急', '1', 'alarm_level', NULL, 'danger', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (133, 2, '重要', '2', 'alarm_level', NULL, 'warning', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (134, 3, '次要', '3', 'alarm_level', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (135, 4, '提示', '4', 'alarm_level', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (139, 1, '未处理', '0', 'alarm_handle_status', NULL, 'danger', 'Y', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (140, 2, '已处理', '1', 'alarm_handle_status', NULL, 'success', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (141, 3, '已忽略', '2', 'alarm_handle_status', NULL, 'info', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (142, 1, '国标', 'GB28181', 'alarm_protocol', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', 'admin', '2026-11-26 16:14:05', NULL);
INSERT INTO `sys_dict_data` VALUES (143, 2, '海康ISUP', 'ISUP', 'alarm_protocol', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (144, 3, 'ONVIF', 'ONVIF', 'alarm_protocol', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (145, 4, '推流', 'PUSH', 'alarm_protocol', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (146, 5, '拉流', 'PULL', 'alarm_protocol', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (147, 6, '其它', 'OTHER', 'alarm_protocol', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (149, 1, '国标设备上报', 'GB_DEVICE', 'alarm_source', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (150, 2, '国标上级平台', 'GB_PLATFORM', 'alarm_source', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (151, 3, '海康ISUP回调', 'ISUP_SDK', 'alarm_source', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (152, 4, '平台手工录入', 'MANUAL', 'alarm_source', NULL, 'primary', 'N', '0', 'admin', '2026-11-26 11:33:28', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` INTEGER NOT NULL,
  `dict_name` TEXT NULL DEFAULT '',
  `dict_type` TEXT NULL DEFAULT '',
  `status` TEXT NULL DEFAULT '0',
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`dict_id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "dict_type" ON "sys_dict_type" ("dict_type");

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (12, '播放类型', 'play_type', '0', 'admin', '2025-06-21 05:08:11', '', NULL, '播放类型');
INSERT INTO `sys_dict_type` VALUES (14, '设备在线状态', 'yingshi_online_status', '0', 'admin', '2025-07-31 15:19:45', '', NULL, '设备在线状态');
INSERT INTO `sys_dict_type` VALUES (15, '视频清晰度', 'yingshi_quality', '0', 'admin', '2025-07-31 17:22:20', '', NULL, '视频清晰度');
INSERT INTO `sys_dict_type` VALUES (16, '地址类型', 'yingshi_address_type', '0', 'admin', '2025-07-31 17:22:43', '', NULL, '地址类型');
INSERT INTO `sys_dict_type` VALUES (17, '通道在线状态', 'yingshi_monitoring_status', '0', 'admin', '2025-07-31 18:09:27', '', NULL, '通道在线状态');
INSERT INTO `sys_dict_type` VALUES (18, '通道是否显示', 'yingshi_camera_is_add', '0', 'admin', '2025-07-31 18:10:27', '', NULL, '通道是否显示');
INSERT INTO `sys_dict_type` VALUES (19, '通道视频质量', 'yingshi_camera_video_quality', '0', 'admin', '2025-07-31 18:11:49', 'admin', '2025-07-31 18:13:06', '通道视频质量');
INSERT INTO `sys_dict_type` VALUES (20, '通道是否加密', 'yingshi_camera_is_encrypt', '0', 'admin', '2025-07-31 18:12:59', 'admin', '2025-07-31 18:13:10', '通道是否加密');
INSERT INTO `sys_dict_type` VALUES (21, '过滤通道类型', 'filter_channel_type', '0', 'admin', '2026-08-09 21:05:40', '', '2026-08-09 21:05:40', NULL);
INSERT INTO `sys_dict_type` VALUES (22, '报警过滤类型', 'alarm_filter', '0', 'admin', '2026-11-24 15:44:46', 'admin', '2026-11-24 16:30:55', '国标报警过滤类型');
INSERT INTO `sys_dict_type` VALUES (23, 'ISUP报警过滤类型', 'isup_alarm_filter', '0', 'admin', '2026-11-24 16:30:41', '', '2026-11-24 16:30:41', NULL);
INSERT INTO `sys_dict_type` VALUES (24, '统一报警类型', 'alarm_type', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '归一化报警类型，dict_value 为编码，dict_label 为展示名称');
INSERT INTO `sys_dict_type` VALUES (25, '报警级别', 'alarm_level', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '1紧急 2重要 3次要 4提示');
INSERT INTO `sys_dict_type` VALUES (26, '报警处理状态', 'alarm_handle_status', '0', 'admin', '2026-11-26 11:33:28', '', NULL, '0未处理 1已处理 2已忽略');
INSERT INTO `sys_dict_type` VALUES (27, '报警接入协议', 'protocol_type', '0', 'admin', '2026-11-26 11:33:28', '', NULL, 'GB28181/ISUP/ONVIF/PUSH/PULL/OTHER');
INSERT INTO `sys_dict_type` VALUES (28, '报警来源', 'alarm_source', '0', 'admin', '2026-11-26 11:33:28', '', NULL, 'GB_DEVICE/GB_PLATFORM/ISUP_SDK/MANUAL');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` INTEGER NOT NULL,
  `job_name` TEXT NOT NULL DEFAULT '',
  `job_group` TEXT NOT NULL DEFAULT 'DEFAULT',
  `invoke_target` TEXT NOT NULL,
  `cron_expression` TEXT NULL DEFAULT '',
  `misfire_policy` TEXT NULL DEFAULT '3',
  `concurrent` TEXT NULL DEFAULT '1',
  `status` TEXT NULL DEFAULT '0',
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT '',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`)
);

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` INTEGER NOT NULL,
  `job_name` TEXT NOT NULL,
  `job_group` TEXT NOT NULL,
  `invoke_target` TEXT NOT NULL,
  `job_message` TEXT NULL DEFAULT NULL,
  `status` TEXT NULL DEFAULT '0',
  `exception_info` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`job_log_id`)
);

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` INTEGER NOT NULL,
  `user_name` TEXT NULL DEFAULT '',
  `ipaddr` TEXT NULL DEFAULT '',
  `login_location` TEXT NULL DEFAULT '',
  `browser` TEXT NULL DEFAULT '',
  `os` TEXT NULL DEFAULT '',
  `status` TEXT NULL DEFAULT '0',
  `msg` TEXT NULL DEFAULT '',
  `login_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`info_id`)
);
CREATE INDEX IF NOT EXISTS "idx_sys_logininfor_s" ON "sys_logininfor" ("status");
CREATE INDEX IF NOT EXISTS "idx_sys_logininfor_lt" ON "sys_logininfor" ("login_time");



-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` INTEGER NOT NULL,
  `menu_name` TEXT NOT NULL,
  `parent_id` INTEGER NULL DEFAULT 0,
  `order_num` INTEGER NULL DEFAULT 0,
  `path` TEXT NULL DEFAULT '',
  `component` TEXT NULL DEFAULT NULL,
  `query` TEXT NULL DEFAULT NULL,
  `route_name` TEXT NULL DEFAULT '',
  `is_frame` INTEGER NULL DEFAULT 1,
  `is_cache` INTEGER NULL DEFAULT 0,
  `menu_type` TEXT NULL DEFAULT '',
  `visible` TEXT NULL DEFAULT '0',
  `status` TEXT NULL DEFAULT '0',
  `perms` TEXT NULL DEFAULT NULL,
  `icon` TEXT NULL DEFAULT '#',
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT '',
  PRIMARY KEY (`menu_id`)
);

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 8, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'el-icon-setting', 'admin', '2025-04-03 09:03:39', 'admin', '2026-05-31 15:37:13', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 9, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'el-icon-stopwatch', 'admin', '2025-04-03 09:03:39', 'admin', '2026-08-06 16:13:18', '系统监控目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 2, 8, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', '#', 'admin', '2025-04-03 09:03:39', 'admin', '2026-05-31 15:59:19', '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 2, 7, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', '#', 'admin', '2025-04-03 09:03:39', 'admin', '2026-05-31 15:59:13', '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2000, '基础配置', 0, 7, 'node', 'wvp/mediaServer/index', NULL, 'node', 1, 0, 'C', '0', '0', 'wvp:server:list', 'el-icon-document', 'admin', '2025-04-03 09:08:16', 'admin', '2026-08-06 16:12:45', '');
INSERT INTO `sys_menu` VALUES (2003, '国标设备', 2015, 1, 'device', 'wvp/device/index', NULL, 'device', 1, 0, 'C', '0', '0', 'wvp:device:list', '#', 'admin', '2025-04-09 14:54:29', 'admin', '2025-04-18 09:00:20', '');
INSERT INTO `sys_menu` VALUES (2004, 'ONVIF设备', 2015, 4, 'onvif', 'onvif/index', NULL, 'onvif', 1, 0, 'C', '0', '0', 'onvif:device:list', '#', 'admin', '2025-04-09 17:31:15', 'admin', '2026-06-29 22:17:20', '');
INSERT INTO `sys_menu` VALUES (2006, '云端录像', 2162, 99, 'cloudRecord', 'wvp/cloudRecord/index', NULL, 'cloudRecord', 1, 0, 'C', '0', '0', 'wvp:record:list', '#', 'admin', '2025-04-11 11:31:09', 'admin', '2026-06-20 23:42:51', '');
INSERT INTO `sys_menu` VALUES (2007, '推流设备', 2015, 3, 'streamPush', 'wvp/streamPush/index', NULL, 'streamPush', 1, 0, 'C', '0', '0', 'wvp:push:list', '#', 'admin', '2025-04-11 14:53:58', 'admin', '2026-06-20 23:31:04', '');
INSERT INTO `sys_menu` VALUES (2008, '拉流设备', 2015, 2, 'streamProxy', 'wvp/streamProxy/index', NULL, 'streamProxy', 1, 1, 'C', '0', '0', 'wvp:proxy:list', '#', 'admin', '2025-04-12 09:13:44', 'admin', '2026-06-20 23:30:49', '');
INSERT INTO `sys_menu` VALUES (2012, '录像计划', 2162, 6, 'recordPlan', 'wvp/recordPlan/index', NULL, 'recordPlan', 1, 0, 'C', '0', '0', 'wvp:record:list', '#', 'admin', '2025-04-14 11:02:38', 'admin', '2026-06-20 23:42:37', '');
INSERT INTO `sys_menu` VALUES (2013, '分屏监控', 0, 1, 'wvpLive', 'wvp/live/index', NULL, 'wvpLive', 1, 0, 'C', '0', '0', 'wvp:live:list', 'el-icon-video-camera', 'admin', '2025-04-14 11:03:37', 'admin', '2026-06-20 22:57:28', '');
INSERT INTO `sys_menu` VALUES (2014, '国标级联', 0, 6, 'platform', 'wvp/platform/index', NULL, 'platform', 1, 0, 'C', '0', '0', 'wvp:platform:list', 'el-icon-upload2', 'admin', '2025-04-16 10:06:03', 'admin', '2026-08-06 16:12:33', '');
INSERT INTO `sys_menu` VALUES (2015, '设备接入', 0, 4, 'gbmanger', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'el-icon-video-camera-solid', 'admin', '2025-04-16 14:05:21', 'admin', '2026-08-06 16:12:03', '');
INSERT INTO `sys_menu` VALUES (2016, '删除设备', 2003, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:remove', '#', 'admin', '2025-04-18 09:01:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '同步设备', 2003, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:sync', '#', 'admin', '2025-04-18 09:02:25', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2018, '查询设备', 2003, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:query', '#', 'admin', '2025-04-18 09:03:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '更新设备', 2003, 7, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:edit', '#', 'admin', '2025-04-18 09:06:43', 'admin', '2026-11-08 21:55:46', '');
INSERT INTO `sys_menu` VALUES (2028, '通道列表', 2003, 13, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:channels', '#', 'admin', '2025-04-18 10:29:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2030, '播放视频', 2003, 15, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:play:start', '#', 'admin', '2025-04-18 10:39:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2031, '停止播放', 2003, 16, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:play:stop', '#', 'admin', '2025-04-18 10:40:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2032, '查询通道', 2003, 17, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:channel:query', '#', 'admin', '2025-04-18 10:42:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2033, '修改通道', 2003, 18, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:channel:edit', '#', 'admin', '2025-04-18 10:43:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2035, '播放', 2006, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:play', '#', 'admin', '2025-04-18 10:54:11', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2036, '下载', 2006, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:download', '#', 'admin', '2025-04-18 10:55:06', 'admin', '2025-04-18 10:56:03', '');
INSERT INTO `sys_menu` VALUES (2037, '新增', 2007, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:push:add', '#', 'admin', '2025-04-18 10:58:59', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2038, '修改', 2007, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:push:edit', '#', 'admin', '2025-04-18 10:59:31', 'admin', '2025-04-18 10:59:37', '');
INSERT INTO `sys_menu` VALUES (2039, '删除', 2007, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:push:remove', '#', 'admin', '2025-04-18 11:00:31', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2040, '播放', 2007, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:push:play', '#', 'admin', '2025-04-18 11:01:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2041, '删除', 2008, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:delete', '#', 'admin', '2025-04-18 11:05:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '新增', 2008, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:add', '#', 'admin', '2025-04-18 11:08:59', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2043, '修改', 2008, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:edit', '#', 'admin', '2025-04-18 11:09:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2044, '播放', 2008, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:play', '#', 'admin', '2025-04-18 11:09:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2045, '停止', 2008, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:stop', '#', 'admin', '2025-04-18 11:09:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2046, '新增', 2014, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:add', '#', 'admin', '2025-04-18 11:12:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2047, '编辑', 2014, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:edit', '#', 'admin', '2025-04-18 11:13:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2048, '删除', 2014, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:delete', '#', 'admin', '2025-04-18 11:13:22', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2049, '导出', 2004, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:export', '#', 'admin', '2025-04-18 11:14:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2051, '修改', 2004, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:edit', '#', 'admin', '2025-04-18 11:14:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2052, '获取详情', 2051, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:query', '#', 'admin', '2025-04-18 11:15:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2053, '推送通道', 2014, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:push', '#', 'admin', '2025-04-18 11:15:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2054, '播放', 2004, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:play', '#', 'admin', '2025-04-18 11:16:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2055, '通道共享', 2014, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:channelList', '#', 'admin', '2025-04-18 11:17:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2057, '新增设备', 2014, 6, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:channelAdd', '#', 'admin', '2025-04-18 11:18:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2058, '删除设备', 2014, 7, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:channelRemove', '#', 'admin', '2025-04-18 11:19:22', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2059, '删除', 2004, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:remove', '#', 'admin', '2025-04-18 11:21:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2060, '新塘', 2012, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:add', '#', 'admin', '2025-04-18 11:23:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2061, '修改', 2012, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:edit', '#', 'admin', '2025-04-18 11:24:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2062, '删除', 2012, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:delete', '#', 'admin', '2025-04-18 11:24:16', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2063, '关联通道', 2012, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:channelList', '#', 'admin', '2025-04-18 11:27:25', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2064, '新增通道关联', 2012, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:channelAdd', '#', 'admin', '2025-04-18 11:29:23', 'admin', '2025-04-18 11:30:41', '');
INSERT INTO `sys_menu` VALUES (2065, '删除通道关联', 2012, 6, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:channelDelete', '#', 'admin', '2025-04-18 11:29:48', 'admin', '2025-04-18 11:30:46', '');
INSERT INTO `sys_menu` VALUES (2079, '查看', 2000, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:wvpMediaServer:view', '#', 'admin', '2025-04-18 11:58:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2089, '日志导出', 500, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2026-05-31 16:01:16', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2161, '电子地图', 0, 2, 'wvpgbMap', 'wvp/gbMap', NULL, 'wvpgbMap', 1, 0, 'C', '0', '0', 'wvp:gbMap:list', 'el-icon-map-location', 'admin', '2026-05-26 07:59:53', 'admin', '2026-06-20 22:57:35', '');
INSERT INTO `sys_menu` VALUES (2162, '录像管理', 0, 5, 'record', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'el-icon-c-scale-to-original', 'admin', '2026-06-20 23:41:43', 'admin', '2026-06-22 10:52:31', '');
INSERT INTO `sys_menu` VALUES (2163, '云台控制', 2003, 99, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:play:ptz', '#', 'admin', '2026-07-30 20:44:31', '', '2026-07-30 20:44:31', '');
INSERT INTO `sys_menu` VALUES (2164, '基础配置查看', 2000, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'system:baseconfig:query', '#', 'admin', '2026-07-30 20:48:42', '', '2026-07-30 20:48:42', '');
INSERT INTO `sys_menu` VALUES (2165, '基础配置编辑', 2000, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'system:baseconfig:edit', '#', 'admin', '2026-07-30 20:49:11', '', '2026-07-30 20:49:11', '');
INSERT INTO `sys_menu` VALUES (2166, '报警管理', 0, 3, 'gbmanger', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'el-icon-message-solid', 'admin', '2026-08-06 16:11:40', 'admin', '2026-09-06 15:33:51', '');
INSERT INTO `sys_menu` VALUES (2169, '海康ISUP', 2015, 5, 'isup', 'wvp/isup/index', NULL, '', 1, 0, 'C', '0', '0', 'isup:lsupDevice:list', '#', 'admin', '2026-08-11 23:44:39', '', '2026-08-11 23:44:39', '');
INSERT INTO `sys_menu` VALUES (2170, '协议管理', 1, 9, 'agreement', 'system/agreement/index', '', '', 1, 0, 'C', '0', '0', '', '', 'admin', '2026-09-14 10:00:07', 'admin', '2026-09-14 11:08:24', '协议管理菜单');
INSERT INTO `sys_menu` VALUES (2173, '统一报警', 2166, 10, 'alarms', 'wvp/alarms/index', NULL, 'alarms', 1, 0, 'C', '0', '0', 'wvp:alarms:list', 'el-icon-bell', 'admin', '2026-11-23 10:00:00', 'admin', '2026-11-24 15:39:39', '');
INSERT INTO `sys_menu` VALUES (2174, '报警查询', 2170, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:alarms:query', '#', 'admin', '2026-11-23 10:00:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2175, '报警处理', 2170, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:alarms:handle', '#', 'admin', '2026-11-23 10:00:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2176, '报警删除', 2170, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:alarms:remove', '#', 'admin', '2026-11-23 10:00:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2177, '报警清理', 2170, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:alarms:clean', '#', 'admin', '2026-11-23 10:00:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2178, '报警补录', 2170, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:alarms:add', '#', 'admin', '2026-11-23 10:00:00', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` INTEGER NOT NULL,
  `notice_title` TEXT NOT NULL,
  `notice_type` TEXT NOT NULL,
  `notice_content` longblob NULL,
  `status` TEXT NULL DEFAULT '0',
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`notice_id`)
);

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '这是一个公告', '1', X'3C756C3E3C6C693E3C7374726F6E673E3C6120687265663D2268747470733A2F2F7777772E6E6577732E636E2F706F6C69746963732F6C6561646572732F32303236303930382F31363166353935316562346634326432386466366235316663333730363766622F632E68746D6C22207461726765743D225F626C616E6B223EE4B9A0E8BF91E5B9B3E5908CE88BB1E59BBDE9A696E79BB8E4BCAFE7BAB3E5A786E9809AE794B5E8AF9D3C2F613E3C2F7374726F6E673E3C2F6C693E3C6C693E3C693E3C2F693E3C7374726F6E673E3C6120687265663D2268747470733A2F2F7777772E70656F706C656170702E636F6D2F636F6C756D6E2F33303035333039383338372D35303030303736383636353422207461726765743D225F626C616E6B223E3C623EE3808AE696B0E99D92E5B9B4E3808BE7BC96E8BE91E983A8E59B9EE4BFA1EFBC8CE8AFB7E69FA5E694B63C2F623E3C2F613E266E6273703B3C693E266E6273703B3C2F693E3C6120687265663D2268747470733A2F2F7777772E636E722E636E2F61682F6E6577732F32303236303930372F7432303236303930375F3532373830363730362E7368746D6C22207461726765743D225F626C616E6B223E3C623EE591B5E68AA4E6B0B4E6B885E5B2B8E7BBBF20E4BABAE6B19FE5928CE8B090E585B1E7949F3C2F623E3C2F613E3C2F7374726F6E673E3C2F6C693E3C6C693E3C693E3C2F693E3C7374726F6E673E3C6120687265663D2268747470733A2F2F682E78696E687561786D742E636F6D2F76683531322F73686172652F31333237363438313F646F6369643D313332373634383126616D703B6E657773747970653D3130303126616D703B643D3133353238326322207461726765743D225F626C616E6B223EE69D83E5A881E695B0E8AFBBEFBD9CE68891E59BBDE8BF9BE587BAE58FA3E8BF9EE7BBAD34E4B8AAE69C88E4BF9DE68C81E4B8A4E4BD8DE695B0E5A29EE995BF3C2F613E3C2F7374726F6E673E3C2F6C693E3C6C693E3C693E3C2F693E3C7374726F6E673E3C6120687265663D2268747470733A2F2F636F6E74656E742D7374617469632E636374766E6577732E636374762E636F6D2F736E6F772D626F6F6B2F696E6465782E68746D6C3F6974656D5F69643D3939373838373935373338323538383934333426616D703B746F635F7374796C655F69643D66656564735F64656661756C7426616D703B73686172655F746F3D636F70795F75726C26616D703B747261636B5F69643D31373439333632382D623735352D343134322D386436382D64663066316535636230373022207461726765743D225F626C616E6B223EE8A786E9A291E4B8A8E5BDB1E8A786E5B8A6E58AA8E58F96E699AFE59CB0E587BAE59C8820E4B990E5B1B1E38081E893ACE88EB1E8BF8EE69DA5E68993E58DA1E783AD3C2F613E3C2F7374726F6E673E3C2F6C693E3C6C693E3C693E3C2F693E3C7374726F6E673E3C6120687265663D2268747470733A2F2F6469676974616C2E676D772E636E2F323032362D30392F30382F636F6E74656E745F33383938393933302E68746D22207461726765743D225F626C616E6B223EE4BAAEE5BAA6E8B685E5A4AAE998B3E799BEE4BABFE5808DEFBC81E5B081E4B89CE69DA5E999A2E5A3ABE68FADE7A798E2809CE8B685E7BAA758E58589E69CBAE2809D3C2F613E3C2F7374726F6E673E3C2F6C693E3C6C693E3C693E3C2F693E3C7374726F6E673E3C6120687265663D2268747470733A2F2F7777772E73686F627365727665722E636F6D2F73746174696373672F7265732F68746D6C2F7765622F6E65777344657461696C2E68746D6C3F69643D3131373238373326616D703B763D322E3226616D703B7369643D363722207461726765743D225F626C616E6B223EE59CA8E585A8E79083474450E5898DE4BA94E59F8EE5B882E4B8ADEFBC8CE4B88AE6B5B7E4B8BAE4BD95E8A681E59D9AE5AE88E8BF99E4B8AAE2809CE4B88DE59088E7BEA4E2809DE79A84323025EFBC9F3C2F613E3C2F7374726F6E673E3C2F6C693E3C2F756C3E3C756C3E3C6C693E3C6120687265663D2268747470733A2F2F6D2E6A7374762E636F6D2F6E6577732F323032362F392F382F313534363834303033383131393839393133362E68746D6C22207461726765743D225F626C616E6B223EE4BCA0E2809CE7BAA2E7A096E2809DE896AAE781ABEFBC8CE7AB8BE2809CE4BB8EE697A0E588B0E69C89E2809DE4B98BE5BF973C2F613E266E6273703B3C6120687265663D2268747470733A2F2F6F70696E696F6E2E6E65777373632E6F72672F73797374656D2F32303236303930382F3030313633393239312E68746D6C22207461726765743D225F626C616E6B223EE38090E5A5BDE8AF84E4B8ADE59BBDE380913C2F613E3C2F6C693E3C6C693E3C6120687265663D2268747470733A2F2F6D792D68356E6577732E6170702E78696E6875616E65742E636F6D2F68352F61727469636C652E68746D6C3F61727469636C6549643D3230323630393038383964383038313030306239346133666262383933626562333866616661346622207461726765743D225F626C616E6B223EE8B7AFE88194E9809AE5BF83E79BB8E9809AEFBC8CE4B8ADE59089E59088E4BD9CE4B989E588A9E79BB8E89E8D3C2F613E3C2F6C693E3C6C693E3C6120687265663D2268747470733A2F2F6D2E6875616E7169752E636F6D2F61727469636C652F345437505279426334764C22207461726765743D225F626C616E6B223EE697A5E98197E58C96E6ADA6E99480E6AF81E8BF9BE7A88BE4B8A5E9878DE6BB9EE5908EEFBC8CE59BBDE99985E7A4BEE4BC9AE695A6E4BF83E697A5E696B9E58887E5AE9EE5B1A5E7BAA6EFBC8CE7A681E58C96E6ADA6E7BB84E7BB87E7ACAC37E6ACA1E69DA5E58D8EE88083E5AF9F3C2F613E3C2F6C693E3C6C693E3C6120687265663D2268747470733A2F2F636974792E6372692E636E2F32303236303930382F62663437323136622D313531382D343330362D383233332D3063373939366164666333662E68746D6C22207461726765743D225F626C616E6B223EE3809032303236E68993E58DA1E4B8ADE59BBDE38091E59CA8E6B19FE8A5BFE8B5A3E5B79EEFBC8CE79C8BE8A781E4B880E5BAA7E59F8EE79A84E5A49AE5BDA9E58F99E4BA8B3C2F613E3C2F6C693E3C6C693E3C6120687265663D2268747470733A2F2F6261696A696168616F2E62616964752E636F6D2F733F69643D3138373434383835393332313532353039323222207461726765743D225F626C616E6B223EE799BEE5BAA632303236E5B9B438E69C88E4BEB5E69D83E4B8BEE68AA5E58F97E79086E585ACE7A4BA3C2F613E3C2F6C693E3C6C693E3C6120687265663D22687474703A2F2F6261696A696168616F2E62616964752E636F6D2F733F69643D3138373537353237393236303336373230383922207461726765743D225F626C616E6B223EE4BB8AE697A5E8BE9FE8B0A33C2F613E266E6273703B3C6120687265663D22687474703A2F2F7777772E626A6A7562616F2E6F72672E636E2F6E6F64655F313630362E68746D6C22207461726765743D225F626C616E6B223EE58C97E4BAACE7BD91E7AB99E8BE9FE8B0A3E5B9B3E58FB03C2F613E266E6273703B3C6120687265663D2268747470733A2F2F7777772E706979616F2E6F72672E636E2F22207461726765743D225F626C616E6B223EE4BA92E88194E7BD91E88194E59088E8BE9FE8B0A3E5B9B3E58FB03C2F613E3C2F6C693E3C2F756C3E', '0', 'admin', '2026-09-08 22:05:09', '', '2026-09-08 22:05:09', NULL);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` INTEGER NOT NULL,
  `title` TEXT NULL DEFAULT '',
  `business_type` INTEGER NULL DEFAULT 0,
  `method` TEXT NULL DEFAULT '',
  `request_method` TEXT NULL DEFAULT '',
  `operator_type` INTEGER NULL DEFAULT 0,
  `oper_name` TEXT NULL DEFAULT '',
  `dept_name` TEXT NULL DEFAULT '',
  `oper_url` TEXT NULL DEFAULT '',
  `oper_ip` TEXT NULL DEFAULT '',
  `oper_location` TEXT NULL DEFAULT '',
  `oper_param` TEXT NULL DEFAULT '',
  `json_result` TEXT NULL DEFAULT '',
  `status` INTEGER NULL DEFAULT 0,
  `error_msg` TEXT NULL DEFAULT '',
  `oper_time` TEXT NULL DEFAULT NULL,
  `cost_time` INTEGER NULL DEFAULT 0,
  PRIMARY KEY (`oper_id`)
);
CREATE INDEX IF NOT EXISTS "idx_sys_oper_log_bt" ON "sys_oper_log" ("business_type");
CREATE INDEX IF NOT EXISTS "idx_sys_oper_log_s" ON "sys_oper_log" ("status");
CREATE INDEX IF NOT EXISTS "idx_sys_oper_log_ot" ON "sys_oper_log" ("oper_time");


-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` INTEGER NOT NULL,
  `post_code` TEXT NOT NULL,
  `post_name` TEXT NOT NULL,
  `post_sort` INTEGER NOT NULL,
  `status` TEXT NOT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`post_id`)
);

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2025-04-03 09:03:39', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` INTEGER NOT NULL,
  `role_name` TEXT NOT NULL,
  `role_key` TEXT NOT NULL,
  `role_sort` INTEGER NOT NULL,
  `data_scope` TEXT NULL DEFAULT '1',
  `menu_check_strictly` INTEGER NULL DEFAULT 1,
  `dept_check_strictly` INTEGER NULL DEFAULT 1,
  `status` TEXT NOT NULL,
  `del_flag` TEXT NULL DEFAULT '0',
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`)
);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2025-04-03 09:03:39', 'admin', '2025-09-19 16:35:21', '普通角色');
INSERT INTO `sys_role` VALUES (3, '管理员', 'admin_wvp', 0, '1', 1, 1, '0', '0', 'admin', '2026-06-23 16:52:04', 'admin', '2026-11-08 21:55:29', NULL);
INSERT INTO `sys_role` VALUES (4, '演示角色', 'wvp_yans', 4, '1', 1, 1, '0', '0', 'admin', '2026-07-30 20:23:04', 'admin', '2026-11-08 21:55:12', NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` INTEGER NOT NULL,
  `dept_id` INTEGER NOT NULL,
  PRIMARY KEY (`role_id`, `dept_id`)
);

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` INTEGER NOT NULL,
  `menu_id` INTEGER NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`)
);

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);
INSERT INTO `sys_role_menu` VALUES (3, 1);
INSERT INTO `sys_role_menu` VALUES (3, 2);
INSERT INTO `sys_role_menu` VALUES (3, 100);
INSERT INTO `sys_role_menu` VALUES (3, 101);
INSERT INTO `sys_role_menu` VALUES (3, 102);
INSERT INTO `sys_role_menu` VALUES (3, 103);
INSERT INTO `sys_role_menu` VALUES (3, 104);
INSERT INTO `sys_role_menu` VALUES (3, 105);
INSERT INTO `sys_role_menu` VALUES (3, 106);
INSERT INTO `sys_role_menu` VALUES (3, 107);
INSERT INTO `sys_role_menu` VALUES (3, 109);
INSERT INTO `sys_role_menu` VALUES (3, 110);
INSERT INTO `sys_role_menu` VALUES (3, 112);
INSERT INTO `sys_role_menu` VALUES (3, 113);
INSERT INTO `sys_role_menu` VALUES (3, 114);
INSERT INTO `sys_role_menu` VALUES (3, 500);
INSERT INTO `sys_role_menu` VALUES (3, 501);
INSERT INTO `sys_role_menu` VALUES (3, 1000);
INSERT INTO `sys_role_menu` VALUES (3, 1001);
INSERT INTO `sys_role_menu` VALUES (3, 1002);
INSERT INTO `sys_role_menu` VALUES (3, 1003);
INSERT INTO `sys_role_menu` VALUES (3, 1004);
INSERT INTO `sys_role_menu` VALUES (3, 1005);
INSERT INTO `sys_role_menu` VALUES (3, 1006);
INSERT INTO `sys_role_menu` VALUES (3, 1007);
INSERT INTO `sys_role_menu` VALUES (3, 1008);
INSERT INTO `sys_role_menu` VALUES (3, 1009);
INSERT INTO `sys_role_menu` VALUES (3, 1010);
INSERT INTO `sys_role_menu` VALUES (3, 1011);
INSERT INTO `sys_role_menu` VALUES (3, 1012);
INSERT INTO `sys_role_menu` VALUES (3, 1013);
INSERT INTO `sys_role_menu` VALUES (3, 1014);
INSERT INTO `sys_role_menu` VALUES (3, 1015);
INSERT INTO `sys_role_menu` VALUES (3, 1016);
INSERT INTO `sys_role_menu` VALUES (3, 1017);
INSERT INTO `sys_role_menu` VALUES (3, 1018);
INSERT INTO `sys_role_menu` VALUES (3, 1019);
INSERT INTO `sys_role_menu` VALUES (3, 1020);
INSERT INTO `sys_role_menu` VALUES (3, 1021);
INSERT INTO `sys_role_menu` VALUES (3, 1022);
INSERT INTO `sys_role_menu` VALUES (3, 1023);
INSERT INTO `sys_role_menu` VALUES (3, 1024);
INSERT INTO `sys_role_menu` VALUES (3, 1025);
INSERT INTO `sys_role_menu` VALUES (3, 1026);
INSERT INTO `sys_role_menu` VALUES (3, 1027);
INSERT INTO `sys_role_menu` VALUES (3, 1028);
INSERT INTO `sys_role_menu` VALUES (3, 1029);
INSERT INTO `sys_role_menu` VALUES (3, 1030);
INSERT INTO `sys_role_menu` VALUES (3, 1031);
INSERT INTO `sys_role_menu` VALUES (3, 1032);
INSERT INTO `sys_role_menu` VALUES (3, 1033);
INSERT INTO `sys_role_menu` VALUES (3, 1034);
INSERT INTO `sys_role_menu` VALUES (3, 1035);
INSERT INTO `sys_role_menu` VALUES (3, 1036);
INSERT INTO `sys_role_menu` VALUES (3, 1037);
INSERT INTO `sys_role_menu` VALUES (3, 1038);
INSERT INTO `sys_role_menu` VALUES (3, 1039);
INSERT INTO `sys_role_menu` VALUES (3, 1040);
INSERT INTO `sys_role_menu` VALUES (3, 1042);
INSERT INTO `sys_role_menu` VALUES (3, 1043);
INSERT INTO `sys_role_menu` VALUES (3, 1044);
INSERT INTO `sys_role_menu` VALUES (3, 1045);
INSERT INTO `sys_role_menu` VALUES (3, 1046);
INSERT INTO `sys_role_menu` VALUES (3, 1047);
INSERT INTO `sys_role_menu` VALUES (3, 1048);
INSERT INTO `sys_role_menu` VALUES (3, 1049);
INSERT INTO `sys_role_menu` VALUES (3, 1050);
INSERT INTO `sys_role_menu` VALUES (3, 1051);
INSERT INTO `sys_role_menu` VALUES (3, 1052);
INSERT INTO `sys_role_menu` VALUES (3, 1053);
INSERT INTO `sys_role_menu` VALUES (3, 1054);
INSERT INTO `sys_role_menu` VALUES (3, 2000);
INSERT INTO `sys_role_menu` VALUES (3, 2003);
INSERT INTO `sys_role_menu` VALUES (3, 2004);
INSERT INTO `sys_role_menu` VALUES (3, 2006);
INSERT INTO `sys_role_menu` VALUES (3, 2007);
INSERT INTO `sys_role_menu` VALUES (3, 2008);
INSERT INTO `sys_role_menu` VALUES (3, 2012);
INSERT INTO `sys_role_menu` VALUES (3, 2013);
INSERT INTO `sys_role_menu` VALUES (3, 2014);
INSERT INTO `sys_role_menu` VALUES (3, 2015);
INSERT INTO `sys_role_menu` VALUES (3, 2016);
INSERT INTO `sys_role_menu` VALUES (3, 2017);
INSERT INTO `sys_role_menu` VALUES (3, 2018);
INSERT INTO `sys_role_menu` VALUES (3, 2022);
INSERT INTO `sys_role_menu` VALUES (3, 2028);
INSERT INTO `sys_role_menu` VALUES (3, 2030);
INSERT INTO `sys_role_menu` VALUES (3, 2031);
INSERT INTO `sys_role_menu` VALUES (3, 2032);
INSERT INTO `sys_role_menu` VALUES (3, 2033);
INSERT INTO `sys_role_menu` VALUES (3, 2035);
INSERT INTO `sys_role_menu` VALUES (3, 2036);
INSERT INTO `sys_role_menu` VALUES (3, 2037);
INSERT INTO `sys_role_menu` VALUES (3, 2038);
INSERT INTO `sys_role_menu` VALUES (3, 2039);
INSERT INTO `sys_role_menu` VALUES (3, 2040);
INSERT INTO `sys_role_menu` VALUES (3, 2041);
INSERT INTO `sys_role_menu` VALUES (3, 2042);
INSERT INTO `sys_role_menu` VALUES (3, 2043);
INSERT INTO `sys_role_menu` VALUES (3, 2044);
INSERT INTO `sys_role_menu` VALUES (3, 2045);
INSERT INTO `sys_role_menu` VALUES (3, 2046);
INSERT INTO `sys_role_menu` VALUES (3, 2047);
INSERT INTO `sys_role_menu` VALUES (3, 2048);
INSERT INTO `sys_role_menu` VALUES (3, 2049);
INSERT INTO `sys_role_menu` VALUES (3, 2051);
INSERT INTO `sys_role_menu` VALUES (3, 2052);
INSERT INTO `sys_role_menu` VALUES (3, 2053);
INSERT INTO `sys_role_menu` VALUES (3, 2054);
INSERT INTO `sys_role_menu` VALUES (3, 2055);
INSERT INTO `sys_role_menu` VALUES (3, 2057);
INSERT INTO `sys_role_menu` VALUES (3, 2058);
INSERT INTO `sys_role_menu` VALUES (3, 2059);
INSERT INTO `sys_role_menu` VALUES (3, 2060);
INSERT INTO `sys_role_menu` VALUES (3, 2061);
INSERT INTO `sys_role_menu` VALUES (3, 2062);
INSERT INTO `sys_role_menu` VALUES (3, 2063);
INSERT INTO `sys_role_menu` VALUES (3, 2064);
INSERT INTO `sys_role_menu` VALUES (3, 2065);
INSERT INTO `sys_role_menu` VALUES (3, 2079);
INSERT INTO `sys_role_menu` VALUES (3, 2089);
INSERT INTO `sys_role_menu` VALUES (3, 2161);
INSERT INTO `sys_role_menu` VALUES (3, 2162);
INSERT INTO `sys_role_menu` VALUES (3, 2163);
INSERT INTO `sys_role_menu` VALUES (3, 2164);
INSERT INTO `sys_role_menu` VALUES (3, 2165);
INSERT INTO `sys_role_menu` VALUES (4, 1);
INSERT INTO `sys_role_menu` VALUES (4, 2);
INSERT INTO `sys_role_menu` VALUES (4, 100);
INSERT INTO `sys_role_menu` VALUES (4, 101);
INSERT INTO `sys_role_menu` VALUES (4, 103);
INSERT INTO `sys_role_menu` VALUES (4, 104);
INSERT INTO `sys_role_menu` VALUES (4, 105);
INSERT INTO `sys_role_menu` VALUES (4, 106);
INSERT INTO `sys_role_menu` VALUES (4, 107);
INSERT INTO `sys_role_menu` VALUES (4, 109);
INSERT INTO `sys_role_menu` VALUES (4, 112);
INSERT INTO `sys_role_menu` VALUES (4, 113);
INSERT INTO `sys_role_menu` VALUES (4, 114);
INSERT INTO `sys_role_menu` VALUES (4, 500);
INSERT INTO `sys_role_menu` VALUES (4, 501);
INSERT INTO `sys_role_menu` VALUES (4, 1000);
INSERT INTO `sys_role_menu` VALUES (4, 1007);
INSERT INTO `sys_role_menu` VALUES (4, 1016);
INSERT INTO `sys_role_menu` VALUES (4, 1020);
INSERT INTO `sys_role_menu` VALUES (4, 1025);
INSERT INTO `sys_role_menu` VALUES (4, 1030);
INSERT INTO `sys_role_menu` VALUES (4, 1035);
INSERT INTO `sys_role_menu` VALUES (4, 1036);
INSERT INTO `sys_role_menu` VALUES (4, 1037);
INSERT INTO `sys_role_menu` VALUES (4, 1038);
INSERT INTO `sys_role_menu` VALUES (4, 1039);
INSERT INTO `sys_role_menu` VALUES (4, 1040);
INSERT INTO `sys_role_menu` VALUES (4, 1042);
INSERT INTO `sys_role_menu` VALUES (4, 1043);
INSERT INTO `sys_role_menu` VALUES (4, 1044);
INSERT INTO `sys_role_menu` VALUES (4, 1045);
INSERT INTO `sys_role_menu` VALUES (4, 1046);
INSERT INTO `sys_role_menu` VALUES (4, 2000);
INSERT INTO `sys_role_menu` VALUES (4, 2003);
INSERT INTO `sys_role_menu` VALUES (4, 2004);
INSERT INTO `sys_role_menu` VALUES (4, 2006);
INSERT INTO `sys_role_menu` VALUES (4, 2007);
INSERT INTO `sys_role_menu` VALUES (4, 2008);
INSERT INTO `sys_role_menu` VALUES (4, 2012);
INSERT INTO `sys_role_menu` VALUES (4, 2013);
INSERT INTO `sys_role_menu` VALUES (4, 2014);
INSERT INTO `sys_role_menu` VALUES (4, 2015);
INSERT INTO `sys_role_menu` VALUES (4, 2016);
INSERT INTO `sys_role_menu` VALUES (4, 2017);
INSERT INTO `sys_role_menu` VALUES (4, 2018);
INSERT INTO `sys_role_menu` VALUES (4, 2022);
INSERT INTO `sys_role_menu` VALUES (4, 2028);
INSERT INTO `sys_role_menu` VALUES (4, 2030);
INSERT INTO `sys_role_menu` VALUES (4, 2031);
INSERT INTO `sys_role_menu` VALUES (4, 2032);
INSERT INTO `sys_role_menu` VALUES (4, 2033);
INSERT INTO `sys_role_menu` VALUES (4, 2035);
INSERT INTO `sys_role_menu` VALUES (4, 2037);
INSERT INTO `sys_role_menu` VALUES (4, 2038);
INSERT INTO `sys_role_menu` VALUES (4, 2039);
INSERT INTO `sys_role_menu` VALUES (4, 2040);
INSERT INTO `sys_role_menu` VALUES (4, 2041);
INSERT INTO `sys_role_menu` VALUES (4, 2042);
INSERT INTO `sys_role_menu` VALUES (4, 2043);
INSERT INTO `sys_role_menu` VALUES (4, 2044);
INSERT INTO `sys_role_menu` VALUES (4, 2045);
INSERT INTO `sys_role_menu` VALUES (4, 2046);
INSERT INTO `sys_role_menu` VALUES (4, 2047);
INSERT INTO `sys_role_menu` VALUES (4, 2049);
INSERT INTO `sys_role_menu` VALUES (4, 2051);
INSERT INTO `sys_role_menu` VALUES (4, 2052);
INSERT INTO `sys_role_menu` VALUES (4, 2053);
INSERT INTO `sys_role_menu` VALUES (4, 2054);
INSERT INTO `sys_role_menu` VALUES (4, 2055);
INSERT INTO `sys_role_menu` VALUES (4, 2057);
INSERT INTO `sys_role_menu` VALUES (4, 2059);
INSERT INTO `sys_role_menu` VALUES (4, 2064);
INSERT INTO `sys_role_menu` VALUES (4, 2065);
INSERT INTO `sys_role_menu` VALUES (4, 2079);
INSERT INTO `sys_role_menu` VALUES (4, 2089);
INSERT INTO `sys_role_menu` VALUES (4, 2161);
INSERT INTO `sys_role_menu` VALUES (4, 2162);
INSERT INTO `sys_role_menu` VALUES (4, 2163);
INSERT INTO `sys_role_menu` VALUES (4, 2164);
INSERT INTO `sys_role_menu` VALUES (4, 2169);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` INTEGER NOT NULL,
  `dept_id` INTEGER NULL DEFAULT NULL,
  `user_name` TEXT NOT NULL,
  `nick_name` TEXT NOT NULL,
  `user_type` TEXT NULL DEFAULT '00',
  `email` TEXT NULL DEFAULT '',
  `phonenumber` TEXT NULL DEFAULT '',
  `sex` TEXT NULL DEFAULT '0',
  `avatar` TEXT NULL DEFAULT '',
  `password` TEXT NULL DEFAULT '',
  `status` TEXT NULL DEFAULT '0',
  `del_flag` TEXT NULL DEFAULT '0',
  `login_ip` TEXT NULL DEFAULT '',
  `login_date` TEXT NULL DEFAULT NULL,
  `pwd_update_date` TEXT NULL DEFAULT NULL,
  `push_key` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`)
);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '超级系统管理员', '00', 'ry@163.com', '', '1', '/profile/avatar/2025/06/17/透明底logo_20250617143004A001.png', '$2a$10$7hGDAACyoyJF9SFe5WHJg.3aqcJaSpQAY.7ar4tpuoPIf1su7zBne', '0', '0', '127.0.0.1', '2026-11-26 16:20:50', '2026-08-05 20:56:08', '3e80d1762a324d5b0ff636e0bd16f1e3', 'admin', '2025-04-03 09:03:39', '', '2026-11-26 16:20:50', '管理员');
INSERT INTO `sys_user` VALUES (4, 100, 'admin_wvp', '系统管理员', '00', '', '', '0', '/profile/avatar/2025/06/17/透明底logo_20250617143004A001.png', '$2a$10$E46LV8OFOMFQDZ54X3mI2.FhDkmZ9xOQ4mdoByisRBP5fL8p2y5JC', '0', '0', '127.0.0.1', '2026-06-23 17:38:39', NULL, NULL, 'admin', '2026-06-23 16:52:55', '', '2026-06-23 17:38:39', NULL);
INSERT INTO `sys_user` VALUES (6, 100, 'wvp', '演示用户', '00', '', '', '0', '', '$2a$10$By7UFaalJO90qOvYYVPSaeM.HtffdqJQGdwykljAYos18uW8P3jSm', '0', '0', '127.0.0.1', '2026-08-04 23:04:37', NULL, NULL, 'admin', '2026-07-30 20:23:44', 'admin', '2026-08-04 23:04:37', NULL);
INSERT INTO `sys_user` VALUES (7, NULL, 'test', 'test', '00', '', '', '0', '', '$2a$10$YdTLicqkwhEhhjOH..lkxuPOruetGzlEHap3AmBkMsTJKEXIAI9ca', '0', '0', '127.0.0.1', '2026-08-05 20:57:19', '2026-08-01 20:43:22', NULL, 'admin', '2026-08-05 17:01:53', 'admin', '2026-08-05 20:57:19', NULL);

-- ----------------------------
-- Table structure for sys_user_channel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_channel`;
CREATE TABLE `sys_user_channel`  (
  `user_id` INTEGER NOT NULL,
  `channel_id` TEXT NOT NULL,
  `device_id` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`, `channel_id`)
);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` INTEGER NOT NULL,
  `post_id` INTEGER NOT NULL,
  PRIMARY KEY (`user_id`, `post_id`)
);

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` INTEGER NOT NULL,
  `role_id` INTEGER NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`)
);

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (4, 3);
INSERT INTO `sys_user_role` VALUES (6, 4);
INSERT INTO `sys_user_role` VALUES (7, 4);

-- ----------------------------
-- Table structure for wvp_alarms
-- ----------------------------
DROP TABLE IF EXISTS `wvp_alarms`;
CREATE TABLE `wvp_alarms`  (
  `id` INTEGER NOT NULL,
  `protocol_type` TEXT NOT NULL DEFAULT 'GB28181',
  `device_id` TEXT NOT NULL,
  `device_name` TEXT NULL DEFAULT NULL,
  `channel_id` TEXT NULL DEFAULT NULL,
  `channel_name` TEXT NULL DEFAULT NULL,
  `alarm_code` TEXT NULL DEFAULT NULL,
  `alarm_type` TEXT NULL DEFAULT 'other',
  `alarm_type_name` TEXT NULL DEFAULT NULL,
  `alarm_level` INTEGER NULL DEFAULT 4,
  `alarm_method` TEXT NULL DEFAULT NULL,
  `alarm_time` TEXT NULL DEFAULT NULL,
  `alarm_desc` TEXT NULL DEFAULT NULL,
  `longitude` REAL NULL DEFAULT NULL,
  `latitude` REAL NULL DEFAULT NULL,
  `image_url` TEXT NULL DEFAULT NULL,
  `handle_status` INTEGER NOT NULL DEFAULT 0,
  `handle_by` TEXT NULL DEFAULT NULL,
  `handle_time` TEXT NULL DEFAULT NULL,
  `handle_remark` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE INDEX IF NOT EXISTS "idx_alarms_time" ON "wvp_alarms" ("alarm_time");
CREATE INDEX IF NOT EXISTS "idx_alarms_device" ON "wvp_alarms" ("device_id");
CREATE INDEX IF NOT EXISTS "idx_alarms_channel" ON "wvp_alarms" ("channel_id");
CREATE INDEX IF NOT EXISTS "idx_alarms_status" ON "wvp_alarms" ("handle_status");
CREATE INDEX IF NOT EXISTS "idx_alarms_type" ON "wvp_alarms" ("alarm_type");
CREATE INDEX IF NOT EXISTS "idx_alarms_protocol" ON "wvp_alarms" ("protocol_type");


-- ----------------------------
-- Table structure for wvp_alarms_raw
-- ----------------------------
DROP TABLE IF EXISTS `wvp_alarms_raw`;
CREATE TABLE `wvp_alarms_raw`  (
  `id` INTEGER NOT NULL,
  `protocol_type` TEXT NOT NULL DEFAULT 'OTHER',
  `device_id` TEXT NULL DEFAULT NULL,
  `channel_id` TEXT NULL DEFAULT NULL,
  `msg_json` TEXT NULL,
  `status` INTEGER NOT NULL DEFAULT 0,
  `lock_owner` TEXT NULL DEFAULT NULL,
  `lock_time` TEXT NULL DEFAULT NULL,
  `retry_count` INTEGER NOT NULL DEFAULT 0,
  `error_msg` TEXT NULL DEFAULT NULL,
  `receive_time` TEXT NULL DEFAULT NULL,
  `create_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE INDEX IF NOT EXISTS "idx_alarms_raw_status" ON "wvp_alarms_raw" ("status", "id");
CREATE INDEX IF NOT EXISTS "idx_alarms_raw_receive" ON "wvp_alarms_raw" ("receive_time");
CREATE INDEX IF NOT EXISTS "idx_alarms_raw_device" ON "wvp_alarms_raw" ("device_id");

-- ----------------------------
-- Table structure for wvp_cloud_record
-- ----------------------------
DROP TABLE IF EXISTS `wvp_cloud_record`;
CREATE TABLE `wvp_cloud_record`  (
  `id` INTEGER NOT NULL ,
  `app` TEXT NULL DEFAULT NULL,
  `stream` TEXT NULL DEFAULT NULL,
  `call_id` TEXT NULL DEFAULT NULL,
  `start_time` INTEGER NULL DEFAULT NULL,
  `end_time` INTEGER NULL DEFAULT NULL,
  `media_server_id` TEXT NULL DEFAULT NULL,
  `file_name` TEXT NULL DEFAULT NULL,
  `folder` TEXT NULL DEFAULT NULL,
  `file_path` TEXT NULL DEFAULT NULL,
  `collect` INTEGER NULL DEFAULT 0,
  `file_size` INTEGER NULL DEFAULT NULL,
  `time_len` INTEGER NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for wvp_common_group
-- ----------------------------
DROP TABLE IF EXISTS `wvp_common_group`;
CREATE TABLE `wvp_common_group`  (
  `id` INTEGER NOT NULL ,
  `device_id` TEXT NOT NULL,
  `name` TEXT NOT NULL,
  `parent_id` INTEGER NULL DEFAULT NULL,
  `parent_device_id` TEXT NULL DEFAULT NULL,
  `business_group` TEXT NOT NULL,
  `create_time` TEXT NOT NULL,
  `update_time` TEXT NOT NULL,
  `civil_code` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_common_group_device_platform" ON "wvp_common_group" ("device_id");

-- ----------------------------
-- Table structure for wvp_common_region
-- ----------------------------
DROP TABLE IF EXISTS `wvp_common_region`;
CREATE TABLE `wvp_common_region`  (
  `id` INTEGER NOT NULL ,
  `device_id` TEXT NOT NULL,
  `name` TEXT NOT NULL,
  `parent_id` INTEGER NULL DEFAULT NULL,
  `parent_device_id` TEXT NULL DEFAULT NULL,
  `create_time` TEXT NOT NULL,
  `update_time` TEXT NOT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_common_region_device_id" ON "wvp_common_region" ("device_id");

-- ----------------------------
-- Table structure for wvp_device
-- ----------------------------
DROP TABLE IF EXISTS `wvp_device`;
CREATE TABLE `wvp_device`  (
  `id` INTEGER NOT NULL ,
  `dept_id` INTEGER NULL DEFAULT NULL,
  `device_id` TEXT NOT NULL,
  `name` TEXT NULL DEFAULT NULL,
  `manufacturer` TEXT NULL DEFAULT NULL,
  `db_model` TEXT NULL DEFAULT NULL,
  `firmware` TEXT NULL DEFAULT NULL,
  `channel_num` INTEGER NULL DEFAULT 0,
  `transport` TEXT NULL DEFAULT NULL,
  `stream_mode` TEXT NULL DEFAULT NULL,
  `on_line` INTEGER NULL DEFAULT 0,
  `register_time` TEXT NULL DEFAULT NULL,
  `keepalive_time` TEXT NULL DEFAULT NULL,
  `ip` TEXT NULL DEFAULT NULL,
  `create_time` TEXT NULL DEFAULT NULL,
  `update_time` TEXT NULL DEFAULT NULL,
  `port` INTEGER NULL DEFAULT NULL,
  `expires` INTEGER NULL DEFAULT NULL,
  `subscribe_cycle_for_catalog` INTEGER NULL DEFAULT 0,
  `subscribe_cycle_for_mobile_position` INTEGER NULL DEFAULT 0,
  `mobile_position_submission_interval` INTEGER NULL DEFAULT 5,
  `subscribe_cycle_for_alarm` INTEGER NULL DEFAULT 0,
  `host_address` TEXT NULL DEFAULT NULL,
  `charset` TEXT NULL DEFAULT NULL,
  `ssrc_check` INTEGER NULL DEFAULT 0,
  `geo_coord_sys` TEXT NULL DEFAULT NULL,
  `media_server_id` TEXT NULL DEFAULT 'auto',
  `custom_name` TEXT NULL DEFAULT NULL,
  `sdp_ip` TEXT NULL DEFAULT NULL,
  `local_ip` TEXT NULL DEFAULT NULL,
  `password` TEXT NULL DEFAULT NULL,
  `as_message_channel` INTEGER NULL DEFAULT 0,
  `heart_beat_interval` INTEGER NULL DEFAULT NULL,
  `heart_beat_count` INTEGER NULL DEFAULT NULL,
  `position_capability` INTEGER NULL DEFAULT NULL,
  `broadcast_push_after_ack` INTEGER NULL DEFAULT 0,
  `lat` TEXT NULL DEFAULT NULL,
  `lng` TEXT NULL DEFAULT NULL,
  `address_map` TEXT NULL DEFAULT NULL,
  `protocol_type` TEXT NULL DEFAULT 'GB28181',
  `src_url` TEXT NULL DEFAULT NULL,
  `stream_type` TEXT NULL DEFAULT 'default',
  `ffmpeg_cmd_key` TEXT NULL DEFAULT NULL,
  `rtsp_type` TEXT NULL DEFAULT '0',
  `enable_audio` INTEGER NULL DEFAULT 0,
  `enable_mp4` INTEGER NULL DEFAULT 0,
  `enable_remove_none_reader` INTEGER NULL DEFAULT 0,
  `enable_disable_none_reader` INTEGER NULL DEFAULT 0,
  `stream_key` TEXT NULL DEFAULT NULL,
  `start_offline_push` INTEGER NULL DEFAULT NULL,
  `pushing` INTEGER NULL DEFAULT 0,
  `push_time` TEXT NULL DEFAULT NULL,
  `user_name` TEXT NULL DEFAULT NULL,
  `onvif_auth_type` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `update_by` TEXT NULL DEFAULT '',
  `remark` TEXT NULL DEFAULT NULL,
  `channel_type_filter` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_device_device" ON "wvp_device" ("device_id");


-- ----------------------------
-- Table structure for wvp_device_alarm
-- ----------------------------
DROP TABLE IF EXISTS `wvp_device_alarm`;
CREATE TABLE `wvp_device_alarm`  (
  `id` INTEGER NOT NULL ,
  `device_id` TEXT NOT NULL,
  `channel_id` TEXT NOT NULL,
  `alarm_priority` TEXT NULL DEFAULT NULL,
  `alarm_method` TEXT NULL DEFAULT NULL,
  `alarm_time` TEXT NULL DEFAULT NULL,
  `alarm_description` TEXT NULL DEFAULT NULL,
  `longitude` REAL NULL DEFAULT NULL,
  `latitude` REAL NULL DEFAULT NULL,
  `alarm_type` TEXT NULL DEFAULT NULL,
  `create_time` TEXT NOT NULL,
  `channel_name` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for wvp_device_channel
-- ----------------------------
DROP TABLE IF EXISTS `wvp_device_channel`;
CREATE TABLE `wvp_device_channel`  (
  `id` INTEGER NOT NULL ,
  `device_id` TEXT NULL DEFAULT NULL,
  `name` TEXT NULL DEFAULT NULL,
  `manufacturer` TEXT NULL DEFAULT NULL,
  `db_model` TEXT NULL DEFAULT NULL,
  `owner` TEXT NULL DEFAULT NULL,
  `civil_code` TEXT NULL DEFAULT NULL,
  `block` TEXT NULL DEFAULT NULL,
  `address` TEXT NULL DEFAULT NULL,
  `parental` INTEGER NULL DEFAULT NULL,
  `parent_id` TEXT NULL DEFAULT NULL,
  `safety_way` INTEGER NULL DEFAULT NULL,
  `register_way` INTEGER NULL DEFAULT NULL,
  `cert_num` TEXT NULL DEFAULT NULL,
  `certifiable` INTEGER NULL DEFAULT NULL,
  `err_code` INTEGER NULL DEFAULT NULL,
  `end_time` TEXT NULL DEFAULT NULL,
  `secrecy` INTEGER NULL DEFAULT NULL,
  `ip_address` TEXT NULL DEFAULT NULL,
  `port` INTEGER NULL DEFAULT NULL,
  `password` TEXT NULL DEFAULT NULL,
  `status` TEXT NULL DEFAULT 'OFF',
  `on_line` INTEGER NULL DEFAULT 0,
  `longitude` REAL NULL DEFAULT NULL,
  `latitude` REAL NULL DEFAULT NULL,
  `ptz_type` INTEGER NULL DEFAULT NULL,
  `position_type` INTEGER NULL DEFAULT NULL,
  `room_type` INTEGER NULL DEFAULT NULL,
  `use_type` INTEGER NULL DEFAULT NULL,
  `supply_light_type` INTEGER NULL DEFAULT NULL,
  `direction_type` INTEGER NULL DEFAULT NULL,
  `resolution` TEXT NULL DEFAULT NULL,
  `business_group_id` TEXT NULL DEFAULT NULL,
  `download_speed` TEXT NULL DEFAULT NULL,
  `svc_space_support_mod` INTEGER NULL DEFAULT NULL,
  `svc_time_support_mode` INTEGER NULL DEFAULT NULL,
  `create_time` TEXT NOT NULL,
  `update_time` TEXT NOT NULL,
  `sub_count` INTEGER NULL DEFAULT NULL,
  `stream_id` TEXT NULL DEFAULT NULL,
  `has_audio` INTEGER NULL DEFAULT 0,
  `gps_time` TEXT NULL DEFAULT NULL,
  `stream_identification` TEXT NULL DEFAULT NULL,
  `channel_type` INTEGER NOT NULL DEFAULT 0,
  `gb_device_id` TEXT NULL DEFAULT NULL,
  `gb_name` TEXT NULL DEFAULT NULL,
  `gb_manufacturer` TEXT NULL DEFAULT NULL,
  `gb_model` TEXT NULL DEFAULT NULL,
  `gb_owner` TEXT NULL DEFAULT NULL,
  `gb_civil_code` TEXT NULL DEFAULT NULL,
  `gb_block` TEXT NULL DEFAULT NULL,
  `gb_address` TEXT NULL DEFAULT NULL,
  `gb_parental` INTEGER NULL DEFAULT NULL,
  `gb_parent_id` TEXT NULL DEFAULT NULL,
  `gb_safety_way` INTEGER NULL DEFAULT NULL,
  `gb_register_way` INTEGER NULL DEFAULT NULL,
  `gb_cert_num` TEXT NULL DEFAULT NULL,
  `gb_certifiable` INTEGER NULL DEFAULT NULL,
  `gb_err_code` INTEGER NULL DEFAULT NULL,
  `gb_end_time` TEXT NULL DEFAULT NULL,
  `gb_secrecy` INTEGER NULL DEFAULT NULL,
  `gb_ip_address` TEXT NULL DEFAULT NULL,
  `gb_port` INTEGER NULL DEFAULT NULL,
  `gb_password` TEXT NULL DEFAULT NULL,
  `gb_status` TEXT NULL DEFAULT NULL,
  `gb_longitude` REAL NULL DEFAULT NULL,
  `gb_latitude` REAL NULL DEFAULT NULL,
  `gb_business_group_id` TEXT NULL DEFAULT NULL,
  `gb_ptz_type` INTEGER NULL DEFAULT NULL,
  `gb_position_type` INTEGER NULL DEFAULT NULL,
  `gb_room_type` INTEGER NULL DEFAULT NULL,
  `gb_use_type` INTEGER NULL DEFAULT NULL,
  `gb_supply_light_type` INTEGER NULL DEFAULT NULL,
  `gb_direction_type` INTEGER NULL DEFAULT NULL,
  `gb_resolution` TEXT NULL DEFAULT NULL,
  `gb_download_speed` TEXT NULL DEFAULT NULL,
  `gb_svc_space_support_mod` INTEGER NULL DEFAULT NULL,
  `gb_svc_time_support_mode` INTEGER NULL DEFAULT NULL,
  `record_plan_id` INTEGER NULL DEFAULT NULL,
  `data_type` INTEGER NOT NULL,
  `data_device_id` INTEGER NOT NULL,
  `channel_no` TEXT NULL DEFAULT NULL,
  `src_url` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `update_by` TEXT NULL DEFAULT '',
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);



-- ----------------------------
-- Table structure for wvp_device_mobile_position
-- ----------------------------
DROP TABLE IF EXISTS `wvp_device_mobile_position`;
CREATE TABLE `wvp_device_mobile_position`  (
  `id` INTEGER NOT NULL ,
  `device_id` TEXT NOT NULL,
  `channel_id` TEXT NOT NULL,
  `device_name` TEXT NULL DEFAULT NULL,
  `time` TEXT NULL DEFAULT NULL,
  `longitude` REAL NULL DEFAULT NULL,
  `latitude` REAL NULL DEFAULT NULL,
  `altitude` REAL NULL DEFAULT NULL,
  `speed` REAL NULL DEFAULT NULL,
  `direction` REAL NULL DEFAULT NULL,
  `report_source` TEXT NULL DEFAULT NULL,
  `create_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for wvp_favorites
-- ----------------------------
DROP TABLE IF EXISTS `wvp_favorites`;
CREATE TABLE `wvp_favorites`  (
  `id` INTEGER NOT NULL,
  `favorites_name` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT '',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for wvp_favorites_channel
-- ----------------------------
DROP TABLE IF EXISTS `wvp_favorites_channel`;
CREATE TABLE `wvp_favorites_channel`  (
  `channel_id` INTEGER NOT NULL,
  `favorites_id` INTEGER NULL DEFAULT NULL,
  `gb_name` TEXT NULL DEFAULT NULL,
  `gb_parentId` TEXT NULL DEFAULT NULL,
  `gb_deviceId` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT '',
  PRIMARY KEY (`channel_id`)
);

-- ----------------------------
-- Table structure for wvp_mark
-- ----------------------------
DROP TABLE IF EXISTS `wvp_mark`;
CREATE TABLE `wvp_mark`  (
  `id` INTEGER NOT NULL,
  `mark_name` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT '',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for wvp_mark_channel
-- ----------------------------
DROP TABLE IF EXISTS `wvp_mark_channel`;
CREATE TABLE `wvp_mark_channel`  (
  `channel_id` INTEGER NOT NULL,
  `mark_id` INTEGER NULL DEFAULT NULL,
  `gb_name` TEXT NULL DEFAULT NULL,
  `gb_parentId` TEXT NULL DEFAULT NULL,
  `gb_deviceId` TEXT NULL DEFAULT NULL,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT '',
  PRIMARY KEY (`channel_id`)
);

-- ----------------------------
-- Table structure for wvp_media_server
-- ----------------------------
DROP TABLE IF EXISTS `wvp_media_server`;
CREATE TABLE `wvp_media_server`  (
  `id` TEXT NOT NULL,
  `ip` TEXT NULL DEFAULT NULL,
  `hook_ip` TEXT NULL DEFAULT NULL,
  `sdp_ip` TEXT NULL DEFAULT NULL,
  `stream_ip` TEXT NULL DEFAULT NULL,
  `http_port` INTEGER NULL DEFAULT NULL,
  `http_ssl_port` INTEGER NULL DEFAULT NULL,
  `rtmp_port` INTEGER NULL DEFAULT NULL,
  `rtmp_ssl_port` INTEGER NULL DEFAULT NULL,
  `rtp_proxy_port` INTEGER NULL DEFAULT NULL,
  `rtsp_port` INTEGER NULL DEFAULT NULL,
  `rtsp_ssl_port` INTEGER NULL DEFAULT NULL,
  `flv_port` INTEGER NULL DEFAULT NULL,
  `flv_ssl_port` INTEGER NULL DEFAULT NULL,
  `ws_flv_port` INTEGER NULL DEFAULT NULL,
  `ws_flv_ssl_port` INTEGER NULL DEFAULT NULL,
  `auto_config` INTEGER NULL DEFAULT 0,
  `secret` TEXT NULL DEFAULT NULL,
  `type` TEXT NULL DEFAULT 'zlm',
  `rtp_enable` INTEGER NULL DEFAULT 0,
  `rtp_port_range` TEXT NULL DEFAULT NULL,
  `send_rtp_port_range` TEXT NULL DEFAULT NULL,
  `record_assist_port` INTEGER NULL DEFAULT NULL,
  `default_server` INTEGER NULL DEFAULT 0,
  `create_time` TEXT NULL DEFAULT NULL,
  `update_time` TEXT NULL DEFAULT NULL,
  `hook_alive_interval` INTEGER NULL DEFAULT NULL,
  `record_path` TEXT NULL DEFAULT NULL,
  `record_day` INTEGER NULL DEFAULT 7,
  `transcode_suffix` TEXT NULL DEFAULT NULL,
  `nginx_proxy_enabled` INTEGER NULL DEFAULT 0,
  `nginx_http_port` INTEGER NULL DEFAULT NULL,
  `nginx_https_port` INTEGER NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_media_server_unique_ip_http_port" ON "wvp_media_server" ("ip", "http_port");



-- ----------------------------
-- Table structure for wvp_platform
-- ----------------------------
DROP TABLE IF EXISTS `wvp_platform`;
CREATE TABLE `wvp_platform`  (
  `id` INTEGER NOT NULL ,
  `enable` INTEGER NULL DEFAULT 0,
  `name` TEXT NULL DEFAULT NULL,
  `server_gb_id` TEXT NULL DEFAULT NULL,
  `server_gb_domain` TEXT NULL DEFAULT NULL,
  `server_ip` TEXT NULL DEFAULT NULL,
  `server_port` INTEGER NULL DEFAULT NULL,
  `device_gb_id` TEXT NULL DEFAULT NULL,
  `device_ip` TEXT NULL DEFAULT NULL,
  `device_port` TEXT NULL DEFAULT NULL,
  `username` TEXT NULL DEFAULT NULL,
  `password` TEXT NULL DEFAULT NULL,
  `expires` TEXT NULL DEFAULT NULL,
  `keep_timeout` TEXT NULL DEFAULT NULL,
  `transport` TEXT NULL DEFAULT NULL,
  `civil_code` TEXT NULL DEFAULT NULL,
  `manufacturer` TEXT NULL DEFAULT NULL,
  `db_model` TEXT NULL DEFAULT NULL,
  `address` TEXT NULL DEFAULT NULL,
  `character_set` TEXT NULL DEFAULT NULL,
  `ptz` INTEGER NULL DEFAULT 0,
  `rtcp` INTEGER NULL DEFAULT 0,
  `status` INTEGER NULL DEFAULT 0,
  `catalog_group` INTEGER NULL DEFAULT NULL,
  `register_way` INTEGER NULL DEFAULT NULL,
  `secrecy` INTEGER NULL DEFAULT NULL,
  `create_time` TEXT NULL DEFAULT NULL,
  `update_time` TEXT NULL DEFAULT NULL,
  `as_message_channel` INTEGER NULL DEFAULT 0,
  `catalog_with_platform` INTEGER NULL DEFAULT 1,
  `catalog_with_group` INTEGER NULL DEFAULT 1,
  `catalog_with_region` INTEGER NULL DEFAULT 1,
  `auto_push_channel` INTEGER NULL DEFAULT 1,
  `send_stream_ip` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_platform_unique_server_gb_id" ON "wvp_platform" ("server_gb_id");

-- ----------------------------
-- Table structure for wvp_platform_channel
-- ----------------------------
DROP TABLE IF EXISTS `wvp_platform_channel`;
CREATE TABLE `wvp_platform_channel`  (
  `id` INTEGER NOT NULL ,
  `platform_id` INTEGER NULL DEFAULT NULL,
  `device_channel_id` INTEGER NULL DEFAULT NULL,
  `custom_device_id` TEXT NULL DEFAULT NULL,
  `custom_name` TEXT NULL DEFAULT NULL,
  `custom_manufacturer` TEXT NULL DEFAULT NULL,
  `custom_model` TEXT NULL DEFAULT NULL,
  `custom_owner` TEXT NULL DEFAULT NULL,
  `custom_civil_code` TEXT NULL DEFAULT NULL,
  `custom_block` TEXT NULL DEFAULT NULL,
  `custom_address` TEXT NULL DEFAULT NULL,
  `custom_parental` INTEGER NULL DEFAULT NULL,
  `custom_parent_id` TEXT NULL DEFAULT NULL,
  `custom_safety_way` INTEGER NULL DEFAULT NULL,
  `custom_register_way` INTEGER NULL DEFAULT NULL,
  `custom_cert_num` TEXT NULL DEFAULT NULL,
  `custom_certifiable` INTEGER NULL DEFAULT NULL,
  `custom_err_code` INTEGER NULL DEFAULT NULL,
  `custom_end_time` TEXT NULL DEFAULT NULL,
  `custom_secrecy` INTEGER NULL DEFAULT NULL,
  `custom_ip_address` TEXT NULL DEFAULT NULL,
  `custom_port` INTEGER NULL DEFAULT NULL,
  `custom_password` TEXT NULL DEFAULT NULL,
  `custom_status` TEXT NULL DEFAULT NULL,
  `custom_longitude` REAL NULL DEFAULT NULL,
  `custom_latitude` REAL NULL DEFAULT NULL,
  `custom_ptz_type` INTEGER NULL DEFAULT NULL,
  `custom_position_type` INTEGER NULL DEFAULT NULL,
  `custom_room_type` INTEGER NULL DEFAULT NULL,
  `custom_use_type` INTEGER NULL DEFAULT NULL,
  `custom_supply_light_type` INTEGER NULL DEFAULT NULL,
  `custom_direction_type` INTEGER NULL DEFAULT NULL,
  `custom_resolution` TEXT NULL DEFAULT NULL,
  `custom_business_group_id` TEXT NULL DEFAULT NULL,
  `custom_download_speed` TEXT NULL DEFAULT NULL,
  `custom_svc_space_support_mod` INTEGER NULL DEFAULT NULL,
  `custom_svc_time_support_mode` INTEGER NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_platform_gb_channel_platform_id_catalog_id_device_channel_id" ON "wvp_platform_channel" ("platform_id", "device_channel_id");
CREATE UNIQUE INDEX IF NOT EXISTS "uk_platform_gb_channel_device_id" ON "wvp_platform_channel" ("custom_device_id");

-- ----------------------------
-- Table structure for wvp_platform_group
-- ----------------------------
DROP TABLE IF EXISTS `wvp_platform_group`;
CREATE TABLE `wvp_platform_group`  (
  `id` INTEGER NOT NULL ,
  `platform_id` INTEGER NULL DEFAULT NULL,
  `group_id` INTEGER NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_wvp_platform_group_platform_id_group_id" ON "wvp_platform_group" ("platform_id", "group_id");

-- ----------------------------
-- Table structure for wvp_platform_region
-- ----------------------------
DROP TABLE IF EXISTS `wvp_platform_region`;
CREATE TABLE `wvp_platform_region`  (
  `id` INTEGER NOT NULL ,
  `platform_id` INTEGER NULL DEFAULT NULL,
  `region_id` INTEGER NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_wvp_platform_region_platform_id_group_id" ON "wvp_platform_region" ("platform_id", "region_id");

-- ----------------------------
-- Table structure for wvp_record_plan
-- ----------------------------
DROP TABLE IF EXISTS `wvp_record_plan`;
CREATE TABLE `wvp_record_plan`  (
  `id` INTEGER NOT NULL ,
  `snap` INTEGER NULL DEFAULT 0,
  `name` TEXT NOT NULL,
  `create_time` TEXT NULL DEFAULT NULL,
  `update_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for wvp_record_plan_item
-- ----------------------------
DROP TABLE IF EXISTS `wvp_record_plan_item`;
CREATE TABLE `wvp_record_plan_item`  (
  `id` INTEGER NOT NULL ,
  `start` INTEGER NULL DEFAULT NULL,
  `stop` INTEGER NULL DEFAULT NULL,
  `week_day` INTEGER NULL DEFAULT NULL,
  `plan_id` INTEGER NULL DEFAULT NULL,
  `create_time` TEXT NULL DEFAULT NULL,
  `update_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for wvp_resources_tree
-- ----------------------------
DROP TABLE IF EXISTS `wvp_resources_tree`;
CREATE TABLE `wvp_resources_tree`  (
  `id` INTEGER NOT NULL ,
  `is_catalog` INTEGER NULL DEFAULT 1,
  `device_channel_id` INTEGER NULL DEFAULT NULL,
  `gb_stream_id` INTEGER NULL DEFAULT NULL,
  `name` TEXT NULL DEFAULT NULL,
  `parentId` INTEGER NULL DEFAULT NULL,
  `path` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for wvp_sip_black
-- ----------------------------
DROP TABLE IF EXISTS `wvp_sip_black`;
CREATE TABLE `wvp_sip_black`  (
  `id` INTEGER NOT NULL,
  `device_id` TEXT NOT NULL,
  `ip` TEXT NULL DEFAULT NULL,
  `port` INTEGER NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  `create_time` TEXT NULL DEFAULT NULL,
  `update_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_device_id" ON "wvp_sip_black" ("device_id");

-- ----------------------------
-- Table structure for wvp_sip_config
-- ----------------------------
DROP TABLE IF EXISTS `wvp_sip_config`;
CREATE TABLE `wvp_sip_config`  (
  `id` INTEGER NOT NULL,
  `ip` TEXT NULL DEFAULT NULL,
  `show_ip` TEXT NULL DEFAULT NULL,
  `port` INTEGER NOT NULL DEFAULT 8116,
  `gb_domain` TEXT NOT NULL DEFAULT '4101050000',
  `server_id` TEXT NOT NULL DEFAULT '41010500002000000001',
  `gb_pwd` TEXT NULL DEFAULT '12345678',
  `ptz_speed` INTEGER NULL DEFAULT 50,
  `register_time_interval` INTEGER NULL DEFAULT 120,
  `alarm` INTEGER NULL DEFAULT 0,
  `timeout` INTEGER NULL DEFAULT 15,
  `create_time` TEXT NULL DEFAULT NULL,
  `update_time` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of wvp_sip_config
-- ----------------------------
INSERT INTO `wvp_sip_config` VALUES (1, '127.0.0.1', NULL, 15060, '3402000000', '34020000002000000001', 'gbs12345', NULL, NULL, 1, NULL, NULL, '2026-11-25 22:26:11');

-- ----------------------------
-- Table structure for wvp_stream_proxy
-- ----------------------------
DROP TABLE IF EXISTS `wvp_stream_proxy`;
CREATE TABLE `wvp_stream_proxy`  (
  `id` INTEGER NOT NULL ,
  `type` TEXT NULL DEFAULT NULL,
  `app` TEXT NULL DEFAULT NULL,
  `stream` TEXT NULL DEFAULT NULL,
  `src_url` TEXT NULL DEFAULT NULL,
  `timeout` INTEGER NULL DEFAULT NULL,
  `ffmpeg_cmd_key` TEXT NULL DEFAULT NULL,
  `rtsp_type` TEXT NULL DEFAULT NULL,
  `media_server_id` TEXT NULL DEFAULT NULL,
  `enable_audio` INTEGER NULL DEFAULT 0,
  `enable_mp4` INTEGER NULL DEFAULT 0,
  `pulling` INTEGER NULL DEFAULT 0,
  `enable` INTEGER NULL DEFAULT 0,
  `enable_remove_none_reader` INTEGER NULL DEFAULT 0,
  `create_time` TEXT NULL DEFAULT NULL,
  `name` TEXT NULL DEFAULT NULL,
  `update_time` TEXT NULL DEFAULT NULL,
  `stream_key` TEXT NULL DEFAULT NULL,
  `enable_disable_none_reader` INTEGER NULL DEFAULT 0,
  `relates_media_server_id` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_stream_proxy_app_stream" ON "wvp_stream_proxy" ("app", "stream");

-- ----------------------------
-- Table structure for wvp_stream_push
-- ----------------------------
DROP TABLE IF EXISTS `wvp_stream_push`;
CREATE TABLE `wvp_stream_push`  (
  `id` INTEGER NOT NULL ,
  `app` TEXT NULL DEFAULT NULL,
  `stream` TEXT NULL DEFAULT NULL,
  `create_time` TEXT NULL DEFAULT NULL,
  `media_server_id` TEXT NULL DEFAULT NULL,
  `server_id` TEXT NULL DEFAULT NULL,
  `push_time` TEXT NULL DEFAULT NULL,
  `status` INTEGER NULL DEFAULT 0,
  `update_time` TEXT NULL DEFAULT NULL,
  `pushing` INTEGER NULL DEFAULT 0,
  `self` INTEGER NULL DEFAULT 0,
  `start_offline_push` INTEGER NULL DEFAULT 1,
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_stream_push_app_stream" ON "wvp_stream_push" ("app", "stream");

-- ----------------------------
-- Table structure for wvp_user_api_key
-- ----------------------------
DROP TABLE IF EXISTS `wvp_user_api_key`;
CREATE TABLE `wvp_user_api_key`  (
  `id` INTEGER NOT NULL,
  `user_id` INTEGER NOT NULL,
  `access_key` TEXT NOT NULL,
  `secret_key` TEXT NOT NULL,
  `app_name` TEXT NULL DEFAULT NULL,
  `expired_at` TEXT NULL DEFAULT NULL,
  `enable` INTEGER NULL DEFAULT 1,
  `create_by` TEXT NULL DEFAULT '',
  `create_time` TEXT NULL DEFAULT NULL,
  `update_by` TEXT NULL DEFAULT '',
  `update_time` TEXT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT '',
  PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX IF NOT EXISTS "uk_access_key" ON "wvp_user_api_key" ("access_key");
CREATE INDEX IF NOT EXISTS "idx_user_id" ON "wvp_user_api_key" ("user_id");

-- ----------------------------
-- Records of wvp_user_api_key
-- ----------------------------
INSERT INTO `wvp_user_api_key` VALUES (1, 2, 'ak_hOPw-NgBYpFijEzQFWUd6y4n', 'AobdttzX1MiYBNA9sRjvjozh5a8kr7CyttCiF0qSZrI02zjN7f85bZmcquPhgK18', 'test', NULL, 1, 'admin', '2026-07-19 17:30:40', 'admin', '2026-07-19 17:30:40', '');
