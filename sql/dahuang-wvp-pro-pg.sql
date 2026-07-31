/*
 Navicat Premium Data Transfer

 Source Server         : 本地pg
 Source Server Type    : PostgreSQL
 Source Server Version : 150018 (150018)
 Source Host           : localhost:5432
 Source Catalog        : dahuang-wvp-pro
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 150018 (150018)
 File Encoding         : 65001

 Date: 31/07/2026 11:28:35
*/


-- ----------------------------
-- Sequence structure for gen_table_column_column_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gen_table_column_column_id_seq";
CREATE SEQUENCE "public"."gen_table_column_column_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for gen_table_table_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gen_table_table_id_seq";
CREATE SEQUENCE "public"."gen_table_table_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_base_config_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_base_config_config_id_seq";
CREATE SEQUENCE "public"."sys_base_config_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_config_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_config_config_id_seq";
CREATE SEQUENCE "public"."sys_config_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dept_dept_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dept_dept_id_seq";
CREATE SEQUENCE "public"."sys_dept_dept_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dict_data_dict_code_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dict_data_dict_code_seq";
CREATE SEQUENCE "public"."sys_dict_data_dict_code_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dict_type_dict_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dict_type_dict_id_seq";
CREATE SEQUENCE "public"."sys_dict_type_dict_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_job_job_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_job_id_seq";
CREATE SEQUENCE "public"."sys_job_job_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_job_log_job_log_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_log_job_log_id_seq";
CREATE SEQUENCE "public"."sys_job_log_job_log_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_logininfor_info_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_logininfor_info_id_seq";
CREATE SEQUENCE "public"."sys_logininfor_info_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_menu_menu_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_menu_menu_id_seq";
CREATE SEQUENCE "public"."sys_menu_menu_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_notice_notice_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_notice_notice_id_seq";
CREATE SEQUENCE "public"."sys_notice_notice_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_oper_log_oper_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_oper_log_oper_id_seq";
CREATE SEQUENCE "public"."sys_oper_log_oper_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_post_post_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_post_post_id_seq";
CREATE SEQUENCE "public"."sys_post_post_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_role_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_role_id_seq";
CREATE SEQUENCE "public"."sys_role_role_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_user_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_user_id_seq";
CREATE SEQUENCE "public"."sys_user_user_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_cloud_record_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_cloud_record_id_seq";
CREATE SEQUENCE "public"."wvp_cloud_record_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_common_group_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_common_group_id_seq";
CREATE SEQUENCE "public"."wvp_common_group_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_common_region_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_common_region_id_seq";
CREATE SEQUENCE "public"."wvp_common_region_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_device_alarm_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_device_alarm_id_seq";
CREATE SEQUENCE "public"."wvp_device_alarm_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_device_channel_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_device_channel_id_seq";
CREATE SEQUENCE "public"."wvp_device_channel_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_device_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_device_id_seq";
CREATE SEQUENCE "public"."wvp_device_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_device_mobile_position_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_device_mobile_position_id_seq";
CREATE SEQUENCE "public"."wvp_device_mobile_position_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_favorites_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_favorites_id_seq";
CREATE SEQUENCE "public"."wvp_favorites_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_mark_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_mark_id_seq";
CREATE SEQUENCE "public"."wvp_mark_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_platform_channel_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_platform_channel_id_seq";
CREATE SEQUENCE "public"."wvp_platform_channel_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_platform_group_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_platform_group_id_seq";
CREATE SEQUENCE "public"."wvp_platform_group_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_platform_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_platform_id_seq";
CREATE SEQUENCE "public"."wvp_platform_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_platform_region_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_platform_region_id_seq";
CREATE SEQUENCE "public"."wvp_platform_region_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_record_plan_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_record_plan_id_seq";
CREATE SEQUENCE "public"."wvp_record_plan_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_record_plan_item_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_record_plan_item_id_seq";
CREATE SEQUENCE "public"."wvp_record_plan_item_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_resources_tree_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_resources_tree_id_seq";
CREATE SEQUENCE "public"."wvp_resources_tree_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_sip_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_sip_config_id_seq";
CREATE SEQUENCE "public"."wvp_sip_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_stream_proxy_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_stream_proxy_id_seq";
CREATE SEQUENCE "public"."wvp_stream_proxy_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_stream_push_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_stream_push_id_seq";
CREATE SEQUENCE "public"."wvp_stream_push_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wvp_user_api_key_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wvp_user_api_key_id_seq";
CREATE SEQUENCE "public"."wvp_user_api_key_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table";
CREATE TABLE "public"."gen_table" (
  "table_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "table_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "table_comment" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "sub_table_name" text COLLATE "pg_catalog"."default",
  "sub_table_fk_name" text COLLATE "pg_catalog"."default",
  "class_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "tpl_category" text COLLATE "pg_catalog"."default" DEFAULT 'crud'::text,
  "tpl_web_type" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "package_name" text COLLATE "pg_catalog"."default",
  "module_name" text COLLATE "pg_catalog"."default",
  "business_name" text COLLATE "pg_catalog"."default",
  "function_name" text COLLATE "pg_catalog"."default",
  "function_author" text COLLATE "pg_catalog"."default",
  "gen_type" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "gen_path" text COLLATE "pg_catalog"."default" DEFAULT '/'::text,
  "options" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."gen_table"."table_id" IS '编号';
COMMENT ON COLUMN "public"."gen_table"."table_name" IS '表名称';
COMMENT ON COLUMN "public"."gen_table"."table_comment" IS '表描述';
COMMENT ON COLUMN "public"."gen_table"."sub_table_name" IS '关联子表的表名';
COMMENT ON COLUMN "public"."gen_table"."sub_table_fk_name" IS '子表关联的外键名';
COMMENT ON COLUMN "public"."gen_table"."class_name" IS '实体类名称';
COMMENT ON COLUMN "public"."gen_table"."tpl_category" IS '使用的模板（crud单表操作 tree树表操作）';
COMMENT ON COLUMN "public"."gen_table"."tpl_web_type" IS '前端模板类型（element-ui模版 element-plus模版）';
COMMENT ON COLUMN "public"."gen_table"."package_name" IS '生成包路径';
COMMENT ON COLUMN "public"."gen_table"."module_name" IS '生成模块名';
COMMENT ON COLUMN "public"."gen_table"."business_name" IS '生成业务名';
COMMENT ON COLUMN "public"."gen_table"."function_name" IS '生成功能名';
COMMENT ON COLUMN "public"."gen_table"."function_author" IS '生成功能作者';
COMMENT ON COLUMN "public"."gen_table"."gen_type" IS '生成代码方式（0zip压缩包 1自定义路径）';
COMMENT ON COLUMN "public"."gen_table"."gen_path" IS '生成路径（不填默认项目路径）';
COMMENT ON COLUMN "public"."gen_table"."options" IS '其它生成选项';
COMMENT ON COLUMN "public"."gen_table"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."gen_table"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."gen_table"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."gen_table"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."gen_table"."remark" IS '备注';

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table_column";
CREATE TABLE "public"."gen_table_column" (
  "column_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "table_id" int8,
  "column_name" text COLLATE "pg_catalog"."default",
  "column_comment" text COLLATE "pg_catalog"."default",
  "column_type" text COLLATE "pg_catalog"."default",
  "java_type" text COLLATE "pg_catalog"."default",
  "java_field" text COLLATE "pg_catalog"."default",
  "is_pk" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "is_increment" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "is_required" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "is_insert" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "is_edit" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "is_list" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "is_query" char(1) COLLATE "pg_catalog"."default" DEFAULT NULL::bpchar,
  "query_type" text COLLATE "pg_catalog"."default" DEFAULT 'EQ'::text,
  "html_type" text COLLATE "pg_catalog"."default",
  "dict_type" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "sort" int4,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."gen_table_column"."column_id" IS '编号';
COMMENT ON COLUMN "public"."gen_table_column"."table_id" IS '归属表编号';
COMMENT ON COLUMN "public"."gen_table_column"."column_name" IS '列名称';
COMMENT ON COLUMN "public"."gen_table_column"."column_comment" IS '列描述';
COMMENT ON COLUMN "public"."gen_table_column"."column_type" IS '列类型';
COMMENT ON COLUMN "public"."gen_table_column"."java_type" IS 'JAVA类型';
COMMENT ON COLUMN "public"."gen_table_column"."java_field" IS 'JAVA字段名';
COMMENT ON COLUMN "public"."gen_table_column"."is_pk" IS '是否主键（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_increment" IS '是否自增（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_required" IS '是否必填（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_insert" IS '是否为插入字段（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_edit" IS '是否编辑字段（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_list" IS '是否列表字段（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_query" IS '是否查询字段（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."query_type" IS '查询方式（等于、不等于、大于、小于、范围）';
COMMENT ON COLUMN "public"."gen_table_column"."html_type" IS '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';
COMMENT ON COLUMN "public"."gen_table_column"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."gen_table_column"."sort" IS '排序';
COMMENT ON COLUMN "public"."gen_table_column"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."gen_table_column"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."gen_table_column"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."gen_table_column"."update_time" IS '更新时间';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_blob_triggers";
CREATE TABLE "public"."qrtz_blob_triggers" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "blob_data" bytea
)
;
COMMENT ON COLUMN "public"."qrtz_blob_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_blob_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "public"."qrtz_blob_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "public"."qrtz_blob_triggers"."blob_data" IS '存放持久化Trigger对象';

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_calendars";
CREATE TABLE "public"."qrtz_calendars" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "calendar_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "calendar" bytea NOT NULL
)
;
COMMENT ON COLUMN "public"."qrtz_calendars"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_calendars"."calendar_name" IS '日历名称';
COMMENT ON COLUMN "public"."qrtz_calendars"."calendar" IS '存放持久化calendar对象';

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_cron_triggers";
CREATE TABLE "public"."qrtz_cron_triggers" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "cron_expression" text COLLATE "pg_catalog"."default" NOT NULL,
  "time_zone_id" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."qrtz_cron_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_cron_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "public"."qrtz_cron_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "public"."qrtz_cron_triggers"."cron_expression" IS 'cron表达式';
COMMENT ON COLUMN "public"."qrtz_cron_triggers"."time_zone_id" IS '时区';

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_fired_triggers";
CREATE TABLE "public"."qrtz_fired_triggers" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "entry_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "instance_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "fired_time" int8 NOT NULL,
  "sched_time" int8 NOT NULL,
  "priority" int4 NOT NULL,
  "state" text COLLATE "pg_catalog"."default" NOT NULL,
  "job_name" text COLLATE "pg_catalog"."default",
  "job_group" text COLLATE "pg_catalog"."default",
  "is_nonconcurrent" text COLLATE "pg_catalog"."default",
  "requests_recovery" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."entry_id" IS '调度器实例id';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."instance_name" IS '调度器实例名';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."fired_time" IS '触发的时间';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."sched_time" IS '定时器制定的时间';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."priority" IS '优先级';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."state" IS '状态';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."job_group" IS '任务组名';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."is_nonconcurrent" IS '是否并发';
COMMENT ON COLUMN "public"."qrtz_fired_triggers"."requests_recovery" IS '是否接受恢复执行';

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_job_details";
CREATE TABLE "public"."qrtz_job_details" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "job_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "job_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "job_class_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "is_durable" text COLLATE "pg_catalog"."default" NOT NULL,
  "is_nonconcurrent" text COLLATE "pg_catalog"."default" NOT NULL,
  "is_update_data" text COLLATE "pg_catalog"."default" NOT NULL,
  "requests_recovery" text COLLATE "pg_catalog"."default" NOT NULL,
  "job_data" bytea
)
;
COMMENT ON COLUMN "public"."qrtz_job_details"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_job_details"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."qrtz_job_details"."job_group" IS '任务组名';
COMMENT ON COLUMN "public"."qrtz_job_details"."description" IS '相关介绍';
COMMENT ON COLUMN "public"."qrtz_job_details"."job_class_name" IS '执行任务类名称';
COMMENT ON COLUMN "public"."qrtz_job_details"."is_durable" IS '是否持久化';
COMMENT ON COLUMN "public"."qrtz_job_details"."is_nonconcurrent" IS '是否并发';
COMMENT ON COLUMN "public"."qrtz_job_details"."is_update_data" IS '是否更新数据';
COMMENT ON COLUMN "public"."qrtz_job_details"."requests_recovery" IS '是否接受恢复执行';
COMMENT ON COLUMN "public"."qrtz_job_details"."job_data" IS '存放持久化job对象';

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_locks";
CREATE TABLE "public"."qrtz_locks" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "lock_name" text COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."qrtz_locks"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_locks"."lock_name" IS '悲观锁名称';

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_paused_trigger_grps";
CREATE TABLE "public"."qrtz_paused_trigger_grps" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" text COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."qrtz_paused_trigger_grps"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_paused_trigger_grps"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_scheduler_state";
CREATE TABLE "public"."qrtz_scheduler_state" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "instance_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "last_checkin_time" int8 NOT NULL,
  "checkin_interval" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."qrtz_scheduler_state"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_scheduler_state"."instance_name" IS '实例名称';
COMMENT ON COLUMN "public"."qrtz_scheduler_state"."last_checkin_time" IS '上次检查时间';
COMMENT ON COLUMN "public"."qrtz_scheduler_state"."checkin_interval" IS '检查间隔时间';

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_simple_triggers";
CREATE TABLE "public"."qrtz_simple_triggers" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "repeat_count" int8 NOT NULL,
  "repeat_interval" int8 NOT NULL,
  "times_triggered" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."qrtz_simple_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_simple_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "public"."qrtz_simple_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "public"."qrtz_simple_triggers"."repeat_count" IS '重复的次数统计';
COMMENT ON COLUMN "public"."qrtz_simple_triggers"."repeat_interval" IS '重复的间隔时间';
COMMENT ON COLUMN "public"."qrtz_simple_triggers"."times_triggered" IS '已经触发的次数';

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_simprop_triggers";
CREATE TABLE "public"."qrtz_simprop_triggers" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "str_prop_1" text COLLATE "pg_catalog"."default",
  "str_prop_2" text COLLATE "pg_catalog"."default",
  "str_prop_3" text COLLATE "pg_catalog"."default",
  "int_prop_1" int4,
  "int_prop_2" int4,
  "long_prop_1" int8,
  "long_prop_2" int8,
  "dec_prop_1" numeric(13,4) DEFAULT NULL::numeric,
  "dec_prop_2" numeric(13,4) DEFAULT NULL::numeric,
  "bool_prop_1" text COLLATE "pg_catalog"."default",
  "bool_prop_2" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."str_prop_1" IS 'String类型的trigger的第一个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."str_prop_2" IS 'String类型的trigger的第二个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."str_prop_3" IS 'String类型的trigger的第三个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."int_prop_1" IS 'int类型的trigger的第一个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."int_prop_2" IS 'int类型的trigger的第二个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."long_prop_1" IS 'long类型的trigger的第一个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."long_prop_2" IS 'long类型的trigger的第二个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."dec_prop_1" IS 'decimal类型的trigger的第一个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."dec_prop_2" IS 'decimal类型的trigger的第二个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."bool_prop_1" IS 'Boolean类型的trigger的第一个参数';
COMMENT ON COLUMN "public"."qrtz_simprop_triggers"."bool_prop_2" IS 'Boolean类型的trigger的第二个参数';

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_triggers";
CREATE TABLE "public"."qrtz_triggers" (
  "sched_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "job_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "job_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "next_fire_time" int8,
  "prev_fire_time" int8,
  "priority" int4,
  "trigger_state" text COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_type" text COLLATE "pg_catalog"."default" NOT NULL,
  "start_time" int8 NOT NULL,
  "end_time" int8,
  "calendar_name" text COLLATE "pg_catalog"."default",
  "misfire_instr" int2,
  "job_data" bytea
)
;
COMMENT ON COLUMN "public"."qrtz_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "public"."qrtz_triggers"."trigger_name" IS '触发器的名字';
COMMENT ON COLUMN "public"."qrtz_triggers"."trigger_group" IS '触发器所属组的名字';
COMMENT ON COLUMN "public"."qrtz_triggers"."job_name" IS 'qrtz_job_details表job_name的外键';
COMMENT ON COLUMN "public"."qrtz_triggers"."job_group" IS 'qrtz_job_details表job_group的外键';
COMMENT ON COLUMN "public"."qrtz_triggers"."description" IS '相关介绍';
COMMENT ON COLUMN "public"."qrtz_triggers"."next_fire_time" IS '上一次触发时间（毫秒）';
COMMENT ON COLUMN "public"."qrtz_triggers"."prev_fire_time" IS '下一次触发时间（默认为-1表示不触发）';
COMMENT ON COLUMN "public"."qrtz_triggers"."priority" IS '优先级';
COMMENT ON COLUMN "public"."qrtz_triggers"."trigger_state" IS '触发器状态';
COMMENT ON COLUMN "public"."qrtz_triggers"."trigger_type" IS '触发器的类型';
COMMENT ON COLUMN "public"."qrtz_triggers"."start_time" IS '开始时间';
COMMENT ON COLUMN "public"."qrtz_triggers"."end_time" IS '结束时间';
COMMENT ON COLUMN "public"."qrtz_triggers"."calendar_name" IS '日程表名称';
COMMENT ON COLUMN "public"."qrtz_triggers"."misfire_instr" IS '补偿执行的策略';
COMMENT ON COLUMN "public"."qrtz_triggers"."job_data" IS '存放持久化job对象';

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_base_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_base_config";
CREATE TABLE "public"."sys_base_config" (
  "config_id" int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1
),
  "ssl_enabled" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "ssl_key_store" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "ssl_key_store_password" text COLLATE "pg_catalog"."default",
  "ssl_port" int4,
  "http_port" text COLLATE "pg_catalog"."default",
  "access_domain" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_base_config"."config_id" IS '配置主键';
COMMENT ON COLUMN "public"."sys_base_config"."ssl_enabled" IS '是否开启HTTPS（0关闭 1开启）';
COMMENT ON COLUMN "public"."sys_base_config"."ssl_key_store" IS '证书路径';
COMMENT ON COLUMN "public"."sys_base_config"."ssl_key_store_password" IS '证书密码';
COMMENT ON COLUMN "public"."sys_base_config"."ssl_port" IS 'HTTPS端口';
COMMENT ON COLUMN "public"."sys_base_config"."http_port" IS 'http端口';
COMMENT ON COLUMN "public"."sys_base_config"."access_domain" IS '访问域名';
COMMENT ON COLUMN "public"."sys_base_config"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_base_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_base_config"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_base_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_base_config"."remark" IS '备注';

-- ----------------------------
-- Records of sys_base_config
-- ----------------------------
INSERT INTO "public"."sys_base_config" VALUES (1, '0', 'classpath:httpsKey.p12', '123456', 444, '28081', '', 'admin', '2026-06-29 20:39:15', 'admin', '2026-07-16 21:15:50', NULL);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_config";
CREATE TABLE "public"."sys_config" (
  "config_id" int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1
),
  "config_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "config_key" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "config_value" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "config_type" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_config"."config_id" IS '参数主键';
COMMENT ON COLUMN "public"."sys_config"."config_name" IS '参数名称';
COMMENT ON COLUMN "public"."sys_config"."config_key" IS '参数键名';
COMMENT ON COLUMN "public"."sys_config"."config_value" IS '参数键值';
COMMENT ON COLUMN "public"."sys_config"."config_type" IS '系统内置（Y是 N否）';
COMMENT ON COLUMN "public"."sys_config"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_config"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_config"."remark" IS '备注';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO "public"."sys_config" VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO "public"."sys_config" VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '初始化密码 123456');
INSERT INTO "public"."sys_config" VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO "public"."sys_config" VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2025-04-03 09:03:39', 'admin', '2026-07-21 15:09:29', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO "public"."sys_config" VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO "public"."sys_config" VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2025-04-03 09:03:39', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO "public"."sys_config" VALUES (19, '高德地图KEY ', 'gaode_map_key', '88f28edf21df134b8b4c45ae6b87761a', 'Y', 'admin', '2026-06-05 22:21:50', 'admin', '2026-06-07 15:39:26', NULL);
INSERT INTO "public"."sys_config" VALUES (20, '高德地图安全秘钥', 'gaode_map_security_code', '082ed5b08a0c94f565964ce946e260dd', 'Y', 'admin', '2026-06-05 22:21:50', 'admin', '2026-06-07 15:43:56', NULL);
INSERT INTO "public"."sys_config" VALUES (21, '高德地图中心点坐标', 'gaode_map_center', '40.09,116.24', 'Y', 'admin', '2026-06-05 22:25:38', 'admin', '2026-06-07 15:51:26', NULL);
INSERT INTO "public"."sys_config" VALUES (22, '网站名称', 'plat_name', '大荒安防', 'Y', 'admin', '2026-06-22 17:37:07', '', NULL, NULL);
INSERT INTO "public"."sys_config" VALUES (23, '演示信息开关', 'sys.demo.enabled', 'true', 'Y', 'admin', '2026-07-16 18:05:33', 'admin', '2026-07-16 18:26:50', NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
  "dept_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "parent_id" int8 DEFAULT 0,
  "ancestors" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "dept_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "order_num" int4 DEFAULT 0,
  "leader" text COLLATE "pg_catalog"."default",
  "phone" text COLLATE "pg_catalog"."default",
  "email" text COLLATE "pg_catalog"."default",
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "del_flag" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_dept"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."sys_dept"."parent_id" IS '父部门id';
COMMENT ON COLUMN "public"."sys_dept"."ancestors" IS '祖级列表';
COMMENT ON COLUMN "public"."sys_dept"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_dept"."order_num" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_dept"."leader" IS '负责人';
COMMENT ON COLUMN "public"."sys_dept"."phone" IS '联系电话';
COMMENT ON COLUMN "public"."sys_dept"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_dept"."status" IS '部门状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_dept"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "public"."sys_dept"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dept"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dept"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dept"."update_time" IS '更新时间';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept" VALUES (100, 0, '0', 'ry-wvp', 0, 'ry-wvp', '15888888888', 'ry-wvp@qq.com', '0', '0', 'admin', '2025-04-03 09:03:39', 'admin', '2025-04-18 10:55:40', NULL);
INSERT INTO "public"."sys_dept" VALUES (101, 100, '0,100', '广州总公司', 1, 'ry-wvp', '15888888888', 'ry-wvp@qq.com', '0', '0', 'admin', '2025-04-03 09:03:39', 'admin', '2025-04-18 10:56:23', NULL);
INSERT INTO "public"."sys_dept" VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2025-04-03 09:03:39', '', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-04-03 09:03:39', '', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2025-04-03 09:03:39', '', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (105, 101, '0,100,101', '测试部门2', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-04-03 09:03:39', 'admin', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2025-04-03 09:03:39', '', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2025-04-03 09:03:39', '', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2025-04-03 09:03:39', '', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2025-04-03 09:03:39', '', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (110, 101, '0,100,101', '测试部门3', 4, NULL, NULL, NULL, '0', '0', 'admin', NULL, '', NULL, NULL);
INSERT INTO "public"."sys_dept" VALUES (111, 110, '0,100,101,110', '额的大所3', 5, '111111', '', '', '0', '0', 'admin', '2026-07-15 18:54:54', 'admin', '2026-07-15 18:55:05', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_data";
CREATE TABLE "public"."sys_dict_data" (
  "dict_code" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "dict_sort" int4 DEFAULT 0,
  "dict_label" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "dict_value" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "dict_type" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "css_class" text COLLATE "pg_catalog"."default",
  "list_class" text COLLATE "pg_catalog"."default",
  "is_default" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_dict_data"."dict_code" IS '字典编码';
COMMENT ON COLUMN "public"."sys_dict_data"."dict_sort" IS '字典排序';
COMMENT ON COLUMN "public"."sys_dict_data"."dict_label" IS '字典标签';
COMMENT ON COLUMN "public"."sys_dict_data"."dict_value" IS '字典键值';
COMMENT ON COLUMN "public"."sys_dict_data"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."sys_dict_data"."css_class" IS '样式属性（其他样式扩展）';
COMMENT ON COLUMN "public"."sys_dict_data"."list_class" IS '表格回显样式';
COMMENT ON COLUMN "public"."sys_dict_data"."is_default" IS '是否默认（Y是 N否）';
COMMENT ON COLUMN "public"."sys_dict_data"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_dict_data"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict_data"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dict_data"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict_data"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dict_data"."remark" IS '备注';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO "public"."sys_dict_data" VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '性别男');
INSERT INTO "public"."sys_dict_data" VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '性别女');
INSERT INTO "public"."sys_dict_data" VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '性别未知');
INSERT INTO "public"."sys_dict_data" VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '显示菜单');
INSERT INTO "public"."sys_dict_data" VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '隐藏菜单');
INSERT INTO "public"."sys_dict_data" VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data" VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '停用状态');
INSERT INTO "public"."sys_dict_data" VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data" VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '停用状态');
INSERT INTO "public"."sys_dict_data" VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '默认分组');
INSERT INTO "public"."sys_dict_data" VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统分组');
INSERT INTO "public"."sys_dict_data" VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统默认是');
INSERT INTO "public"."sys_dict_data" VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统默认否');
INSERT INTO "public"."sys_dict_data" VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '通知');
INSERT INTO "public"."sys_dict_data" VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '公告');
INSERT INTO "public"."sys_dict_data" VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data" VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '关闭状态');
INSERT INTO "public"."sys_dict_data" VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '其他操作');
INSERT INTO "public"."sys_dict_data" VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '新增操作');
INSERT INTO "public"."sys_dict_data" VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '修改操作');
INSERT INTO "public"."sys_dict_data" VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '删除操作');
INSERT INTO "public"."sys_dict_data" VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '授权操作');
INSERT INTO "public"."sys_dict_data" VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '导出操作');
INSERT INTO "public"."sys_dict_data" VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '导入操作');
INSERT INTO "public"."sys_dict_data" VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '强退操作');
INSERT INTO "public"."sys_dict_data" VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '生成操作');
INSERT INTO "public"."sys_dict_data" VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '清空操作');
INSERT INTO "public"."sys_dict_data" VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data" VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '停用状态');
INSERT INTO "public"."sys_dict_data" VALUES (34, 1, '本地', '1', 'play_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-21 05:10:05', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (35, 2, '推流', '2', 'play_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-21 05:10:15', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (36, 3, 'EasyNTS', '3', 'play_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-21 05:10:25', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (39, 1, '在线', '1', 'yingshi_online_status', NULL, 'success', 'N', '0', 'admin', '2025-07-31 15:20:02', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (40, 0, '离线', '0', 'yingshi_online_status', NULL, 'danger', 'N', '0', 'admin', '2025-07-31 15:20:13', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (41, 1, '高清（主码流）', '1', 'yingshi_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:23:02', 'admin', '2025-07-31 17:23:23', NULL);
INSERT INTO "public"."sys_dict_data" VALUES (42, 2, '流畅（子码流）', '2', 'yingshi_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:23:12', 'admin', '2025-07-31 17:23:31', NULL);
INSERT INTO "public"."sys_dict_data" VALUES (43, 1, '预览', '1', 'yingshi_address_type', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:23:45', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (44, 2, '本地录像回放', '2', 'yingshi_address_type', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:23:53', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (45, 3, '云存储录像回放', '3', 'yingshi_address_type', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 17:24:13', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (46, 0, '不在线', '0', 'yingshi_monitoring_status', NULL, 'default', 'N', '0', 'admin', '2025-07-31 18:09:48', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (47, 1, '在线', '1', 'yingshi_monitoring_status', NULL, 'success', 'N', '0', 'admin', '2025-07-31 18:09:57', 'admin', '2025-07-31 18:10:02', NULL);
INSERT INTO "public"."sys_dict_data" VALUES (48, 0, '隐藏', '0', 'yingshi_camera_is_add', NULL, 'default', 'N', '0', 'admin', '2025-07-31 18:10:48', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (49, 1, '显示', '1', 'yingshi_camera_is_add', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:11:00', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (50, 0, '流畅', '0', 'yingshi_camera_video_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:12:00', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (51, 1, '均衡', '1', 'yingshi_camera_video_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:12:08', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (52, 2, '高清', '2', 'yingshi_camera_video_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:12:18', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (53, 3, '超清', '3', 'yingshi_camera_video_quality', NULL, 'primary', 'N', '0', 'admin', '2025-07-31 18:12:27', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (54, 0, '不加密', '0', 'yingshi_camera_is_encrypt', NULL, 'default', 'N', '0', 'admin', '2025-07-31 18:13:23', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (55, 1, '加密', '1', 'yingshi_camera_is_encrypt', NULL, 'default', 'N', '0', 'admin', '2025-07-31 18:13:29', '', NULL, NULL);
INSERT INTO "public"."sys_dict_data" VALUES (61, 4, 'SDK播放', '4', 'play_type', NULL, 'primary', 'N', '0', 'admin', '2025-09-22 12:44:21', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_type";
CREATE TABLE "public"."sys_dict_type" (
  "dict_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "dict_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "dict_type" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_dict_type"."dict_id" IS '字典主键';
COMMENT ON COLUMN "public"."sys_dict_type"."dict_name" IS '字典名称';
COMMENT ON COLUMN "public"."sys_dict_type"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."sys_dict_type"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_dict_type"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict_type"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dict_type"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict_type"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dict_type"."remark" IS '备注';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO "public"."sys_dict_type" VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '用户性别列表');
INSERT INTO "public"."sys_dict_type" VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '菜单状态列表');
INSERT INTO "public"."sys_dict_type" VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统开关列表');
INSERT INTO "public"."sys_dict_type" VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '任务状态列表');
INSERT INTO "public"."sys_dict_type" VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '任务分组列表');
INSERT INTO "public"."sys_dict_type" VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '系统是否列表');
INSERT INTO "public"."sys_dict_type" VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '通知类型列表');
INSERT INTO "public"."sys_dict_type" VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '通知状态列表');
INSERT INTO "public"."sys_dict_type" VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '操作类型列表');
INSERT INTO "public"."sys_dict_type" VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '登录状态列表');
INSERT INTO "public"."sys_dict_type" VALUES (12, '播放类型', 'play_type', '0', 'admin', '2025-06-21 05:08:11', '', NULL, '播放类型');
INSERT INTO "public"."sys_dict_type" VALUES (14, '设备在线状态', 'yingshi_online_status', '0', 'admin', '2025-07-31 15:19:45', '', NULL, '设备在线状态');
INSERT INTO "public"."sys_dict_type" VALUES (15, '视频清晰度', 'yingshi_quality', '0', 'admin', '2025-07-31 17:22:20', '', NULL, '视频清晰度');
INSERT INTO "public"."sys_dict_type" VALUES (16, '地址类型', 'yingshi_address_type', '0', 'admin', '2025-07-31 17:22:43', '', NULL, '地址类型');
INSERT INTO "public"."sys_dict_type" VALUES (17, '通道在线状态', 'yingshi_monitoring_status', '0', 'admin', '2025-07-31 18:09:27', '', NULL, '通道在线状态');
INSERT INTO "public"."sys_dict_type" VALUES (18, '通道是否显示', 'yingshi_camera_is_add', '0', 'admin', '2025-07-31 18:10:27', '', NULL, '通道是否显示');
INSERT INTO "public"."sys_dict_type" VALUES (19, '通道视频质量', 'yingshi_camera_video_quality', '0', 'admin', '2025-07-31 18:11:49', 'admin', '2025-07-31 18:13:06', '通道视频质量');
INSERT INTO "public"."sys_dict_type" VALUES (20, '通道是否加密', 'yingshi_camera_is_encrypt', '0', 'admin', '2025-07-31 18:12:59', 'admin', '2025-07-31 18:13:10', '通道是否加密');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job" (
  "job_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "job_name" text COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::text,
  "job_group" text COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'DEFAULT'::text,
  "invoke_target" text COLLATE "pg_catalog"."default" NOT NULL,
  "cron_expression" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "misfire_policy" text COLLATE "pg_catalog"."default" DEFAULT '3'::text,
  "concurrent" char(1) COLLATE "pg_catalog"."default" DEFAULT '1'::bpchar,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default" DEFAULT ''::text
)
;
COMMENT ON COLUMN "public"."sys_job"."job_id" IS '任务ID';
COMMENT ON COLUMN "public"."sys_job"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_job"."job_group" IS '任务组名';
COMMENT ON COLUMN "public"."sys_job"."invoke_target" IS '调用目标字符串';
COMMENT ON COLUMN "public"."sys_job"."cron_expression" IS 'cron执行表达式';
COMMENT ON COLUMN "public"."sys_job"."misfire_policy" IS '计划执行错误策略（1立即执行 2执行一次 3放弃执行）';
COMMENT ON COLUMN "public"."sys_job"."concurrent" IS '是否并发执行（0允许 1禁止）';
COMMENT ON COLUMN "public"."sys_job"."status" IS '状态（0正常 1暂停）';
COMMENT ON COLUMN "public"."sys_job"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_job"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_job"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_job"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_job"."remark" IS '备注信息';

-- ----------------------------
-- Records of sys_job
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job_log";
CREATE TABLE "public"."sys_job_log" (
  "job_log_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "job_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "job_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "invoke_target" text COLLATE "pg_catalog"."default" NOT NULL,
  "job_message" text COLLATE "pg_catalog"."default",
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "exception_info" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_job_log"."job_log_id" IS '任务日志ID';
COMMENT ON COLUMN "public"."sys_job_log"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_job_log"."job_group" IS '任务组名';
COMMENT ON COLUMN "public"."sys_job_log"."invoke_target" IS '调用目标字符串';
COMMENT ON COLUMN "public"."sys_job_log"."job_message" IS '日志信息';
COMMENT ON COLUMN "public"."sys_job_log"."status" IS '执行状态（0正常 1失败）';
COMMENT ON COLUMN "public"."sys_job_log"."exception_info" IS '异常信息';
COMMENT ON COLUMN "public"."sys_job_log"."create_time" IS '创建时间';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_logininfor";
CREATE TABLE "public"."sys_logininfor" (
  "info_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "user_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "ipaddr" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "login_location" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "browser" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "os" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "msg" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "login_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_logininfor"."info_id" IS '访问ID';
COMMENT ON COLUMN "public"."sys_logininfor"."user_name" IS '用户账号';
COMMENT ON COLUMN "public"."sys_logininfor"."ipaddr" IS '登录IP地址';
COMMENT ON COLUMN "public"."sys_logininfor"."login_location" IS '登录地点';
COMMENT ON COLUMN "public"."sys_logininfor"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."sys_logininfor"."os" IS '操作系统';
COMMENT ON COLUMN "public"."sys_logininfor"."status" IS '登录状态（0成功 1失败）';
COMMENT ON COLUMN "public"."sys_logininfor"."msg" IS '提示消息';
COMMENT ON COLUMN "public"."sys_logininfor"."login_time" IS '访问时间';

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
  "menu_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "menu_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "parent_id" int8 DEFAULT 0,
  "order_num" int4 DEFAULT 0,
  "path" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "component" text COLLATE "pg_catalog"."default",
  "query" text COLLATE "pg_catalog"."default",
  "route_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "is_frame" int4 DEFAULT 1,
  "is_cache" int4 DEFAULT 0,
  "menu_type" char(1) COLLATE "pg_catalog"."default" DEFAULT ''::bpchar,
  "visible" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "perms" text COLLATE "pg_catalog"."default",
  "icon" text COLLATE "pg_catalog"."default" DEFAULT '#'::text,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default" DEFAULT ''::text
)
;
COMMENT ON COLUMN "public"."sys_menu"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "public"."sys_menu"."parent_id" IS '父菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."order_num" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_menu"."path" IS '路由地址';
COMMENT ON COLUMN "public"."sys_menu"."component" IS '组件路径';
COMMENT ON COLUMN "public"."sys_menu"."query" IS '路由参数';
COMMENT ON COLUMN "public"."sys_menu"."route_name" IS '路由名称';
COMMENT ON COLUMN "public"."sys_menu"."is_frame" IS '是否为外链（0是 1否）';
COMMENT ON COLUMN "public"."sys_menu"."is_cache" IS '是否缓存（0缓存 1不缓存）';
COMMENT ON COLUMN "public"."sys_menu"."menu_type" IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN "public"."sys_menu"."visible" IS '菜单状态（0显示 1隐藏）';
COMMENT ON COLUMN "public"."sys_menu"."status" IS '菜单状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_menu"."perms" IS '权限标识';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."sys_menu"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_menu"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_menu"."remark" IS '备注';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu" VALUES (1, '系统管理', 0, 8, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'el-icon-setting', 'admin', '2025-04-03 09:03:39', 'admin', '2026-05-31 15:37:13', '系统管理目录');
INSERT INTO "public"."sys_menu" VALUES (2, '系统监控', 0, 7, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'el-icon-stopwatch', 'admin', '2025-04-03 09:03:39', 'admin', '2026-05-31 15:36:35', '系统监控目录');
INSERT INTO "public"."sys_menu" VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '用户管理菜单');
INSERT INTO "public"."sys_menu" VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '角色管理菜单');
INSERT INTO "public"."sys_menu" VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '菜单管理菜单');
INSERT INTO "public"."sys_menu" VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '部门管理菜单');
INSERT INTO "public"."sys_menu" VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '岗位管理菜单');
INSERT INTO "public"."sys_menu" VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '字典管理菜单');
INSERT INTO "public"."sys_menu" VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '参数设置菜单');
INSERT INTO "public"."sys_menu" VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '通知公告菜单');
INSERT INTO "public"."sys_menu" VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '在线用户菜单');
INSERT INTO "public"."sys_menu" VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '定时任务菜单');
INSERT INTO "public"."sys_menu" VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '服务监控菜单');
INSERT INTO "public"."sys_menu" VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '缓存监控菜单');
INSERT INTO "public"."sys_menu" VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '缓存列表菜单');
INSERT INTO "public"."sys_menu" VALUES (500, '操作日志', 2, 8, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', '#', 'admin', '2025-04-03 09:03:39', 'admin', '2026-05-31 15:59:19', '操作日志菜单');
INSERT INTO "public"."sys_menu" VALUES (501, '登录日志', 2, 7, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', '#', 'admin', '2025-04-03 09:03:39', 'admin', '2026-05-31 15:59:13', '登录日志菜单');
INSERT INTO "public"."sys_menu" VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2000, '基础配置', 0, 6, 'node', 'wvp/mediaServer/index', NULL, 'node', 1, 0, 'C', '0', '0', 'wvp:server:list', 'el-icon-document', 'admin', '2025-04-03 09:08:16', 'admin', '2026-06-20 23:47:32', '');
INSERT INTO "public"."sys_menu" VALUES (2003, '国标设备', 2015, 1, 'device', 'wvp/device/index', NULL, 'device', 1, 0, 'C', '0', '0', 'wvp:device:list', '#', 'admin', '2025-04-09 14:54:29', 'admin', '2025-04-18 09:00:20', '');
INSERT INTO "public"."sys_menu" VALUES (2004, 'ONVIF设备', 2015, 4, 'onvif', 'onvif/index', NULL, 'onvif', 1, 0, 'C', '0', '0', 'onvif:device:list', '#', 'admin', '2025-04-09 17:31:15', 'admin', '2026-06-29 22:17:20', '');
INSERT INTO "public"."sys_menu" VALUES (2006, '云端录像', 2162, 99, 'cloudRecord', 'wvp/cloudRecord/index', NULL, 'cloudRecord', 1, 0, 'C', '0', '0', 'wvp:record:list', '#', 'admin', '2025-04-11 11:31:09', 'admin', '2026-06-20 23:42:51', '');
INSERT INTO "public"."sys_menu" VALUES (2007, '推流设备', 2015, 3, 'streamPush', 'wvp/streamPush/index', NULL, 'streamPush', 1, 0, 'C', '0', '0', 'wvp:push:list', '#', 'admin', '2025-04-11 14:53:58', 'admin', '2026-06-20 23:31:04', '');
INSERT INTO "public"."sys_menu" VALUES (2008, '拉流设备', 2015, 2, 'streamProxy', 'wvp/streamProxy/index', NULL, 'streamProxy', 1, 1, 'C', '0', '0', 'wvp:proxy:list', '#', 'admin', '2025-04-12 09:13:44', 'admin', '2026-06-20 23:30:49', '');
INSERT INTO "public"."sys_menu" VALUES (2012, '录像计划', 2162, 6, 'recordPlan', 'wvp/recordPlan/index', NULL, 'recordPlan', 1, 0, 'C', '0', '0', 'wvp:record:list', '#', 'admin', '2025-04-14 11:02:38', 'admin', '2026-06-20 23:42:37', '');
INSERT INTO "public"."sys_menu" VALUES (2013, '分屏监控', 0, 1, 'wvpLive', 'wvp/live/index', NULL, 'wvpLive', 1, 0, 'C', '0', '0', 'wvp:live:list', 'el-icon-video-camera', 'admin', '2025-04-14 11:03:37', 'admin', '2026-06-20 22:57:28', '');
INSERT INTO "public"."sys_menu" VALUES (2014, '国标级联', 0, 4, 'platform', 'wvp/platform/index', NULL, 'platform', 1, 0, 'C', '0', '0', 'wvp:platform:list', 'el-icon-upload2', 'admin', '2025-04-16 10:06:03', 'admin', '2026-06-20 23:39:38', '');
INSERT INTO "public"."sys_menu" VALUES (2015, '设备接入', 0, 3, 'gbmanger', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'el-icon-video-camera-solid', 'admin', '2025-04-16 14:05:21', 'admin', '2026-06-20 23:29:54', '');
INSERT INTO "public"."sys_menu" VALUES (2016, '删除设备', 2003, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:remove', '#', 'admin', '2025-04-18 09:01:18', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2017, '同步设备', 2003, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:sync', '#', 'admin', '2025-04-18 09:02:25', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2018, '查询设备', 2003, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:query', '#', 'admin', '2025-04-18 09:03:19', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2019, '开启/关闭目录订阅', 2003, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:subscribeCatalog', '#', 'admin', '2025-04-18 09:04:43', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2020, '开启/关闭移动位置订阅', 2003, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:subscribeMobilePosition', '#', 'admin', '2025-04-18 09:05:22', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2022, '更新设备信息', 2003, 7, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:edit', '#', 'admin', '2025-04-18 09:06:43', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2023, '修改数据流传输模式', 2003, 8, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:updateTransport', '#', 'admin', '2025-04-18 09:07:31', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2024, '获取平台配置信息', 2003, 9, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:server:configInfo', '#', 'admin', '2025-04-18 09:09:20', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2025, '获取流媒体服务列表', 2003, 10, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:server:listOnline', '#', 'admin', '2025-04-18 09:10:12', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2026, '报警布防/撤防命令', 2003, 11, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:control:guardApi', '#', 'admin', '2025-04-18 09:12:18', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2027, '设备配置查询', 2003, 12, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:config:cdownloadApi', '#', 'admin', '2025-04-18 09:13:55', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2028, '通道列表', 2003, 13, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:channels', '#', 'admin', '2025-04-18 10:29:46', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2029, '修改通道码流', 2003, 14, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:device:channelStreamIdentification', '#', 'admin', '2025-04-18 10:32:27', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2030, '播放视频', 2003, 15, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:play:start', '#', 'admin', '2025-04-18 10:39:05', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2031, '停止播放', 2003, 16, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:play:stop', '#', 'admin', '2025-04-18 10:40:21', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2032, '查询通道', 2003, 17, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:channel:query', '#', 'admin', '2025-04-18 10:42:04', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2033, '修改通道', 2003, 18, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:channel:edit', '#', 'admin', '2025-04-18 10:43:00', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2034, '录像控制命令', 2003, 19, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:control:recordApi', '#', 'admin', '2025-04-18 10:46:14', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2035, '播放', 2006, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:play', '#', 'admin', '2025-04-18 10:54:11', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2036, '下载', 2006, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:download', '#', 'admin', '2025-04-18 10:55:06', 'admin', '2025-04-18 10:56:03', '');
INSERT INTO "public"."sys_menu" VALUES (2037, '新增', 2007, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:push:add', '#', 'admin', '2025-04-18 10:58:59', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2038, '修改', 2007, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:push:edit', '#', 'admin', '2025-04-18 10:59:31', 'admin', '2025-04-18 10:59:37', '');
INSERT INTO "public"."sys_menu" VALUES (2039, '删除', 2007, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:push:remove', '#', 'admin', '2025-04-18 11:00:31', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2040, '播放', 2007, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:push:play', '#', 'admin', '2025-04-18 11:01:41', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2041, '删除', 2008, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:delete', '#', 'admin', '2025-04-18 11:05:18', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2042, '新增', 2008, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:add', '#', 'admin', '2025-04-18 11:08:59', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2043, '修改', 2008, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:edit', '#', 'admin', '2025-04-18 11:09:18', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2044, '播放', 2008, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:play', '#', 'admin', '2025-04-18 11:09:37', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2045, '停止', 2008, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:proxy:stop', '#', 'admin', '2025-04-18 11:09:51', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2046, '新增', 2014, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:add', '#', 'admin', '2025-04-18 11:12:54', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2047, '编辑', 2014, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:edit', '#', 'admin', '2025-04-18 11:13:06', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2048, '删除', 2014, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:delete', '#', 'admin', '2025-04-18 11:13:22', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2049, '导出', 2004, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:export', '#', 'admin', '2025-04-18 11:14:12', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2051, '修改', 2004, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:edit', '#', 'admin', '2025-04-18 11:14:44', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2052, '获取详情', 2051, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:query', '#', 'admin', '2025-04-18 11:15:02', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2053, '推送通道', 2014, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:push', '#', 'admin', '2025-04-18 11:15:03', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2054, '播放', 2004, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:play', '#', 'admin', '2025-04-18 11:16:28', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2055, '通道共享', 2014, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:channelList', '#', 'admin', '2025-04-18 11:17:23', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2057, '新增设备', 2014, 6, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:channelAdd', '#', 'admin', '2025-04-18 11:18:47', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2058, '删除设备', 2014, 7, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:platform:channelRemove', '#', 'admin', '2025-04-18 11:19:22', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2059, '删除', 2004, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'onvif:device:remove', '#', 'admin', '2025-04-18 11:21:58', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2060, '新塘', 2012, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:add', '#', 'admin', '2025-04-18 11:23:48', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2061, '修改', 2012, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:edit', '#', 'admin', '2025-04-18 11:24:02', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2062, '删除', 2012, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:delete', '#', 'admin', '2025-04-18 11:24:16', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2063, '关联通道', 2012, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:channelList', '#', 'admin', '2025-04-18 11:27:25', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2064, '新增通道关联', 2012, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:channelAdd', '#', 'admin', '2025-04-18 11:29:23', 'admin', '2025-04-18 11:30:41', '');
INSERT INTO "public"."sys_menu" VALUES (2065, '删除通道关联', 2012, 6, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:record:channelDelete', '#', 'admin', '2025-04-18 11:29:48', 'admin', '2025-04-18 11:30:46', '');
INSERT INTO "public"."sys_menu" VALUES (2079, '查看', 2000, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'wvp:wvpMediaServer:view', '#', 'admin', '2025-04-18 11:58:39', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2089, '日志导出', 500, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2026-05-31 16:01:16', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (2161, '电子地图', 0, 2, 'wvpgbMap', 'wvp/gbMap', NULL, 'wvpgbMap', 1, 0, 'C', '0', '0', 'wvp:gbMap:list', 'el-icon-map-location', 'admin', '2026-05-26 07:59:53', 'admin', '2026-06-20 22:57:35', '');
INSERT INTO "public"."sys_menu" VALUES (2162, '录像管理', 0, 5, 'record', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'el-icon-c-scale-to-original', 'admin', '2026-06-20 23:41:43', 'admin', '2026-06-22 10:52:31', '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_notice";
CREATE TABLE "public"."sys_notice" (
  "notice_id" int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1
),
  "notice_title" text COLLATE "pg_catalog"."default" NOT NULL,
  "notice_type" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "notice_content" bytea,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_notice"."notice_id" IS '公告ID';
COMMENT ON COLUMN "public"."sys_notice"."notice_title" IS '公告标题';
COMMENT ON COLUMN "public"."sys_notice"."notice_type" IS '公告类型（1通知 2公告）';
COMMENT ON COLUMN "public"."sys_notice"."notice_content" IS '公告内容';
COMMENT ON COLUMN "public"."sys_notice"."status" IS '公告状态（0正常 1关闭）';
COMMENT ON COLUMN "public"."sys_notice"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_notice"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_notice"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_notice"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_notice"."remark" IS '备注';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_oper_log";
CREATE TABLE "public"."sys_oper_log" (
  "oper_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "title" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "business_type" int4 DEFAULT 0,
  "method" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "request_method" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "operator_type" int4 DEFAULT 0,
  "oper_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "dept_name" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "oper_url" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "oper_ip" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "oper_location" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "oper_param" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "json_result" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "status" int4 DEFAULT 0,
  "error_msg" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "oper_time" timestamp(6),
  "cost_time" int8 DEFAULT 0
)
;
COMMENT ON COLUMN "public"."sys_oper_log"."oper_id" IS '日志主键';
COMMENT ON COLUMN "public"."sys_oper_log"."title" IS '模块标题';
COMMENT ON COLUMN "public"."sys_oper_log"."business_type" IS '业务类型（0其它 1新增 2修改 3删除）';
COMMENT ON COLUMN "public"."sys_oper_log"."method" IS '方法名称';
COMMENT ON COLUMN "public"."sys_oper_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "public"."sys_oper_log"."operator_type" IS '操作类别（0其它 1后台用户 2手机端用户）';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_name" IS '操作人员';
COMMENT ON COLUMN "public"."sys_oper_log"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_url" IS '请求URL';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_ip" IS '主机地址';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_location" IS '操作地点';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_param" IS '请求参数';
COMMENT ON COLUMN "public"."sys_oper_log"."json_result" IS '返回参数';
COMMENT ON COLUMN "public"."sys_oper_log"."status" IS '操作状态（0正常 1异常）';
COMMENT ON COLUMN "public"."sys_oper_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_time" IS '操作时间';
COMMENT ON COLUMN "public"."sys_oper_log"."cost_time" IS '消耗时间';

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_post";
CREATE TABLE "public"."sys_post" (
  "post_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "post_code" text COLLATE "pg_catalog"."default" NOT NULL,
  "post_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "post_sort" int4 NOT NULL,
  "status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_post"."post_id" IS '岗位ID';
COMMENT ON COLUMN "public"."sys_post"."post_code" IS '岗位编码';
COMMENT ON COLUMN "public"."sys_post"."post_name" IS '岗位名称';
COMMENT ON COLUMN "public"."sys_post"."post_sort" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_post"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_post"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_post"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_post"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_post"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_post"."remark" IS '备注';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO "public"."sys_post" VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_post" VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_post" VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_post" VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2025-04-03 09:03:39', '', NULL, '');
INSERT INTO "public"."sys_post" VALUES (5, 'test3', 'test3', 0, '0', 'admin', NULL, 'admin', '2026-07-19 09:35:17', NULL);
INSERT INTO "public"."sys_post" VALUES (6, 'test2', 'test2', 0, '0', 'admin', '2026-07-19 09:35:05', '', '2026-07-19 09:35:05', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
  "role_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "role_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "role_key" text COLLATE "pg_catalog"."default" NOT NULL,
  "role_sort" int4 NOT NULL,
  "data_scope" char(1) COLLATE "pg_catalog"."default" DEFAULT '1'::bpchar,
  "menu_check_strictly" bool DEFAULT true,
  "dept_check_strictly" bool DEFAULT true,
  "status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "del_flag" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_role"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "public"."sys_role"."role_key" IS '角色权限字符串';
COMMENT ON COLUMN "public"."sys_role"."role_sort" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_role"."data_scope" IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';
COMMENT ON COLUMN "public"."sys_role"."menu_check_strictly" IS '菜单树选择项是否关联显示';
COMMENT ON COLUMN "public"."sys_role"."dept_check_strictly" IS '部门树选择项是否关联显示';
COMMENT ON COLUMN "public"."sys_role"."status" IS '角色状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_role"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_role"."remark" IS '备注';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES (1, '超级管理员', 'admin', 1, '1', 't', 't', '0', '0', 'admin', '2025-04-03 09:03:39', '', NULL, '超级管理员');
INSERT INTO "public"."sys_role" VALUES (2, '普通角色', 'common', 2, '2', 't', 't', '0', '0', 'admin', '2025-04-03 09:03:39', 'admin', '2025-09-19 16:35:21', '普通角色');
INSERT INTO "public"."sys_role" VALUES (4, '测试', '111111', 0, '1', 't', 't', '0', '2', 'admin', '2026-07-30 19:58:10', '', '2026-07-30 19:58:10', NULL);
INSERT INTO "public"."sys_role" VALUES (3, '管理员', 'admin_wvp', 0, '1', 't', 't', '0', '0', 'admin', '2026-06-23 16:52:04', 'admin', '2026-07-30 19:59:17', NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_dept";
CREATE TABLE "public"."sys_role_dept" (
  "role_id" int8 NOT NULL,
  "dept_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_role_dept"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_dept"."dept_id" IS '部门ID';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO "public"."sys_role_dept" VALUES (2, 100);
INSERT INTO "public"."sys_role_dept" VALUES (2, 101);
INSERT INTO "public"."sys_role_dept" VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_menu";
CREATE TABLE "public"."sys_role_menu" (
  "role_id" int8 NOT NULL,
  "menu_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_menu"."menu_id" IS '菜单ID';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO "public"."sys_role_menu" VALUES (2, 1);
INSERT INTO "public"."sys_role_menu" VALUES (2, 2);
INSERT INTO "public"."sys_role_menu" VALUES (2, 3);
INSERT INTO "public"."sys_role_menu" VALUES (2, 100);
INSERT INTO "public"."sys_role_menu" VALUES (2, 101);
INSERT INTO "public"."sys_role_menu" VALUES (2, 102);
INSERT INTO "public"."sys_role_menu" VALUES (2, 103);
INSERT INTO "public"."sys_role_menu" VALUES (2, 104);
INSERT INTO "public"."sys_role_menu" VALUES (2, 105);
INSERT INTO "public"."sys_role_menu" VALUES (2, 106);
INSERT INTO "public"."sys_role_menu" VALUES (2, 107);
INSERT INTO "public"."sys_role_menu" VALUES (2, 108);
INSERT INTO "public"."sys_role_menu" VALUES (2, 109);
INSERT INTO "public"."sys_role_menu" VALUES (2, 110);
INSERT INTO "public"."sys_role_menu" VALUES (2, 111);
INSERT INTO "public"."sys_role_menu" VALUES (2, 112);
INSERT INTO "public"."sys_role_menu" VALUES (2, 113);
INSERT INTO "public"."sys_role_menu" VALUES (2, 114);
INSERT INTO "public"."sys_role_menu" VALUES (2, 115);
INSERT INTO "public"."sys_role_menu" VALUES (2, 116);
INSERT INTO "public"."sys_role_menu" VALUES (2, 117);
INSERT INTO "public"."sys_role_menu" VALUES (2, 500);
INSERT INTO "public"."sys_role_menu" VALUES (2, 501);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1000);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1001);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1002);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1003);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1004);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1005);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1006);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1007);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1008);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1009);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1010);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1011);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1012);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1013);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1014);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1015);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1016);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1017);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1018);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1019);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1020);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1021);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1022);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1023);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1024);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1025);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1026);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1027);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1028);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1029);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1030);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1031);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1032);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1033);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1034);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1035);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1036);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1037);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1038);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1039);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1040);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1041);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1042);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1043);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1044);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1045);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1046);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1047);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1048);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1049);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1050);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1051);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1052);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1053);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1054);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1055);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1056);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1057);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1058);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1059);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1060);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2013);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2161);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2015);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2003);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2016);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2017);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2018);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2019);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2020);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2022);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2023);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2024);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2025);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2026);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2027);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2028);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2029);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2030);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2031);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2032);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2033);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2034);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2008);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2041);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2042);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2044);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2043);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2045);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2007);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2037);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2038);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2039);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2040);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2004);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2049);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2059);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2051);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2052);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2054);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2014);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2046);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2047);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2048);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2053);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2055);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2057);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2058);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2162);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2012);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2060);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2061);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2062);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2063);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2064);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2065);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2006);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2035);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2036);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2000);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2079);
INSERT INTO "public"."sys_role_menu" VALUES (3, 109);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1046);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1047);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1048);
INSERT INTO "public"."sys_role_menu" VALUES (3, 113);
INSERT INTO "public"."sys_role_menu" VALUES (3, 114);
INSERT INTO "public"."sys_role_menu" VALUES (3, 501);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1042);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1043);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1044);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1045);
INSERT INTO "public"."sys_role_menu" VALUES (3, 500);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1039);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1040);
INSERT INTO "public"."sys_role_menu" VALUES (3, 2089);
INSERT INTO "public"."sys_role_menu" VALUES (3, 100);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1000);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1001);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1002);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1003);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1004);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1005);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1006);
INSERT INTO "public"."sys_role_menu" VALUES (3, 101);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1007);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1008);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1009);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1010);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1011);
INSERT INTO "public"."sys_role_menu" VALUES (3, 103);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1016);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1017);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1018);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1019);
INSERT INTO "public"."sys_role_menu" VALUES (3, 104);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1020);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1021);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1022);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1023);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1024);
INSERT INTO "public"."sys_role_menu" VALUES (3, 106);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1030);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1031);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1032);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1033);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1034);
INSERT INTO "public"."sys_role_menu" VALUES (3, 107);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1035);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1036);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1037);
INSERT INTO "public"."sys_role_menu" VALUES (3, 1038);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "user_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "dept_id" int8,
  "user_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "nick_name" text COLLATE "pg_catalog"."default" NOT NULL,
  "user_type" text COLLATE "pg_catalog"."default" DEFAULT '00'::text,
  "email" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "phonenumber" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "sex" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "avatar" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "password" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "del_flag" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "login_ip" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "login_date" timestamp(6),
  "push_key" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."sys_user"."user_name" IS '用户账号';
COMMENT ON COLUMN "public"."sys_user"."nick_name" IS '用户昵称';
COMMENT ON COLUMN "public"."sys_user"."user_type" IS '用户类型（00系统用户）';
COMMENT ON COLUMN "public"."sys_user"."email" IS '用户邮箱';
COMMENT ON COLUMN "public"."sys_user"."phonenumber" IS '手机号码';
COMMENT ON COLUMN "public"."sys_user"."sex" IS '用户性别（0男 1女 2未知）';
COMMENT ON COLUMN "public"."sys_user"."avatar" IS '头像地址';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."status" IS '帐号状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_user"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "public"."sys_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "public"."sys_user"."login_date" IS '最后登录时间';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user"."remark" IS '备注';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$GK9SWq4ExewvcK8d4ImVDO6To51q/zJXU//7avhUxR6jgjeteU97C', '0', '0', '127.0.0.1', '2026-07-29 19:13:10', NULL, 'admin', '2025-04-03 09:03:39', 'admin', '2026-07-29 19:13:10', '测试员');
INSERT INTO "public"."sys_user" VALUES (3, 103, 'ry-wvp', 'ry-wvp', '00', '', '', '2', '', '$2a$10$33PyYoiccR46JxRFMf68eO/AMoAfgeHy0LXaqPkzIcKiAAN9RqjRK', '0', '0', '127.0.0.1', '2025-04-18 11:20:20', NULL, 'admin', '2025-04-18 10:57:08', 'admin', '2025-06-18 00:55:07', NULL);
INSERT INTO "public"."sys_user" VALUES (4, 100, 'admin_wvp', '管理员', '00', '', '18011111111', '0', '', '$2a$10$E46LV8OFOMFQDZ54X3mI2.FhDkmZ9xOQ4mdoByisRBP5fL8p2y5JC', '0', '0', '127.0.0.1', '2026-06-23 17:38:39', NULL, 'admin', '2026-06-23 16:52:55', '', '2026-06-23 17:38:39', NULL);
INSERT INTO "public"."sys_user" VALUES (5, NULL, 'test', 'test', '00', '', '', '0', '', '$2a$10$RSWxVpk9bHZKiFtNKhEaO.sWOlGPM6Oyz./ZJqY0QKm10eH7zCNBa', '0', '0', '', NULL, NULL, 'admin', '2026-07-19 10:20:54', '', '2026-07-19 10:20:54', NULL);
INSERT INTO "public"."sys_user" VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '/profile/avatar/2025/06/17/透明底logo_20250617143004A001.png', '$2a$10$7hGDAACyoyJF9SFe5WHJg.3aqcJaSpQAY.7ar4tpuoPIf1su7zBne', '0', '0', '127.0.0.1', '2026-07-30 19:10:56', '3e80d1762a324d5b0ff636e0bd16f1e3', 'admin', '2025-04-03 09:03:39', '', '2026-07-30 19:10:56', '管理员');
INSERT INTO "public"."sys_user" VALUES (6, NULL, 'ttt', '测试', '00', '', '', '0', '', '$2a$10$KX1a1N1bgeILNEwP3rcCHOP6BgDGAB7uIWNWQ.1mItjmY0XjeYC.i', '0', '0', '', NULL, NULL, 'admin', '2026-07-30 19:57:56', '', '2026-07-30 19:57:56', NULL);

-- ----------------------------
-- Table structure for sys_user_channel
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_channel";
CREATE TABLE "public"."sys_user_channel" (
  "user_id" int8 NOT NULL,
  "channel_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "device_id" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of sys_user_channel
-- ----------------------------
INSERT INTO "public"."sys_user_channel" VALUES (2, '34020000002000000996', '34020000002000000996');
INSERT INTO "public"."sys_user_channel" VALUES (2, '34020000002000000997', '34020000002000000997');
INSERT INTO "public"."sys_user_channel" VALUES (2, '34020000002000000999', '34020000002000000999');
INSERT INTO "public"."sys_user_channel" VALUES (2, '34020000002000000998', '34020000002000000998');
INSERT INTO "public"."sys_user_channel" VALUES (2, '34020000002000001000', '34020000002000001000');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_post";
CREATE TABLE "public"."sys_user_post" (
  "user_id" int8 NOT NULL,
  "post_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_post"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_post"."post_id" IS '岗位ID';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO "public"."sys_user_post" VALUES (1, 1);
INSERT INTO "public"."sys_user_post" VALUES (2, 2);
INSERT INTO "public"."sys_user_post" VALUES (3, 4);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role" (
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_role"."role_id" IS '角色ID';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "public"."sys_user_role" VALUES (1, 1);
INSERT INTO "public"."sys_user_role" VALUES (2, 3);
INSERT INTO "public"."sys_user_role" VALUES (3, 2);
INSERT INTO "public"."sys_user_role" VALUES (4, 3);

-- ----------------------------
-- Table structure for wvp_cloud_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_cloud_record";
CREATE TABLE "public"."wvp_cloud_record" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "app" text COLLATE "pg_catalog"."default",
  "stream" text COLLATE "pg_catalog"."default",
  "call_id" text COLLATE "pg_catalog"."default",
  "start_time" int8,
  "end_time" int8,
  "media_server_id" text COLLATE "pg_catalog"."default",
  "file_name" text COLLATE "pg_catalog"."default",
  "folder" text COLLATE "pg_catalog"."default",
  "file_path" text COLLATE "pg_catalog"."default",
  "collect" bool DEFAULT false,
  "file_size" int8,
  "time_len" int8
)
;

-- ----------------------------
-- Records of wvp_cloud_record
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_common_group
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_common_group";
CREATE TABLE "public"."wvp_common_group" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "device_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "name" text COLLATE "pg_catalog"."default" NOT NULL,
  "parent_id" int4,
  "parent_device_id" text COLLATE "pg_catalog"."default",
  "business_group" text COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" text COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" text COLLATE "pg_catalog"."default" NOT NULL,
  "civil_code" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of wvp_common_group
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_common_region
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_common_region";
CREATE TABLE "public"."wvp_common_region" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "device_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "name" text COLLATE "pg_catalog"."default" NOT NULL,
  "parent_id" int4,
  "parent_device_id" text COLLATE "pg_catalog"."default",
  "create_time" text COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" text COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of wvp_common_region
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_device
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_device";
CREATE TABLE "public"."wvp_device" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "dept_id" int8,
  "device_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "name" text COLLATE "pg_catalog"."default",
  "manufacturer" text COLLATE "pg_catalog"."default",
  "db_model" text COLLATE "pg_catalog"."default",
  "firmware" text COLLATE "pg_catalog"."default",
  "transport" text COLLATE "pg_catalog"."default",
  "stream_mode" text COLLATE "pg_catalog"."default",
  "register_time" text COLLATE "pg_catalog"."default",
  "keepalive_time" text COLLATE "pg_catalog"."default",
  "ip" text COLLATE "pg_catalog"."default",
  "create_time" text COLLATE "pg_catalog"."default",
  "update_time" text COLLATE "pg_catalog"."default",
  "port" int4,
  "expires" int4,
  "subscribe_cycle_for_catalog" int4 DEFAULT 0,
  "subscribe_cycle_for_mobile_position" int4 DEFAULT 0,
  "mobile_position_submission_interval" int4 DEFAULT 5,
  "subscribe_cycle_for_alarm" int4 DEFAULT 0,
  "host_address" text COLLATE "pg_catalog"."default",
  "charset" text COLLATE "pg_catalog"."default",
  "ssrc_check" bool DEFAULT false,
  "geo_coord_sys" text COLLATE "pg_catalog"."default",
  "media_server_id" text COLLATE "pg_catalog"."default" DEFAULT 'auto'::text,
  "custom_name" text COLLATE "pg_catalog"."default",
  "sdp_ip" text COLLATE "pg_catalog"."default",
  "local_ip" text COLLATE "pg_catalog"."default",
  "password" text COLLATE "pg_catalog"."default",
  "as_message_channel" bool DEFAULT false,
  "heart_beat_interval" int4,
  "heart_beat_count" int4,
  "position_capability" int4,
  "broadcast_push_after_ack" bool DEFAULT false,
  "lat" text COLLATE "pg_catalog"."default",
  "lng" text COLLATE "pg_catalog"."default",
  "address_map" text COLLATE "pg_catalog"."default",
  "protocol_type" text COLLATE "pg_catalog"."default" DEFAULT 'GB28181'::text,
  "src_url" text COLLATE "pg_catalog"."default",
  "stream_type" text COLLATE "pg_catalog"."default" DEFAULT 'default'::text,
  "ffmpeg_cmd_key" text COLLATE "pg_catalog"."default",
  "rtsp_type" text COLLATE "pg_catalog"."default" DEFAULT '0'::text,
  "enable_audio" bool DEFAULT false,
  "enable_mp4" bool DEFAULT false,
  "enable_remove_none_reader" bool DEFAULT false,
  "enable_disable_none_reader" bool DEFAULT false,
  "stream_key" text COLLATE "pg_catalog"."default",
  "start_offline_push" bool,
  "pushing" bool DEFAULT false,
  "push_time" text COLLATE "pg_catalog"."default",
  "user_name" text COLLATE "pg_catalog"."default",
  "onvif_auth_type" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT 0,
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT 0,
  "remark" text COLLATE "pg_catalog"."default",
  "on_line" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."wvp_device"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."wvp_device"."lat" IS '纬度';
COMMENT ON COLUMN "public"."wvp_device"."lng" IS '经度';
COMMENT ON COLUMN "public"."wvp_device"."address_map" IS '地图定位地址';
COMMENT ON COLUMN "public"."wvp_device"."protocol_type" IS '设备接入协议类型';
COMMENT ON COLUMN "public"."wvp_device"."src_url" IS '拉流源地址';
COMMENT ON COLUMN "public"."wvp_device"."stream_type" IS '代理类型';
COMMENT ON COLUMN "public"."wvp_device"."ffmpeg_cmd_key" IS 'ffmpeg模板KEY';
COMMENT ON COLUMN "public"."wvp_device"."rtsp_type" IS 'RTSP拉流方式';
COMMENT ON COLUMN "public"."wvp_device"."enable_audio" IS '是否启用音频';
COMMENT ON COLUMN "public"."wvp_device"."enable_mp4" IS '是否启用录像';
COMMENT ON COLUMN "public"."wvp_device"."enable_remove_none_reader" IS '无人观看时自动移除';
COMMENT ON COLUMN "public"."wvp_device"."enable_disable_none_reader" IS '无人观看时自动停用';
COMMENT ON COLUMN "public"."wvp_device"."stream_key" IS 'ZLM返回的streamKey';
COMMENT ON COLUMN "public"."wvp_device"."user_name" IS '用户名';
COMMENT ON COLUMN "public"."wvp_device"."onvif_auth_type" IS 'onvif 设备鉴权方式';
COMMENT ON COLUMN "public"."wvp_device"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."wvp_device"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."wvp_device"."remark" IS '备注';

-- ----------------------------
-- Records of wvp_device
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_device_alarm
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_device_alarm";
CREATE TABLE "public"."wvp_device_alarm" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "device_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "channel_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "alarm_priority" text COLLATE "pg_catalog"."default",
  "alarm_method" text COLLATE "pg_catalog"."default",
  "alarm_time" text COLLATE "pg_catalog"."default",
  "alarm_description" text COLLATE "pg_catalog"."default",
  "longitude" float8,
  "latitude" float8,
  "alarm_type" text COLLATE "pg_catalog"."default",
  "create_time" text COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of wvp_device_alarm
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_device_channel
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_device_channel";
CREATE TABLE "public"."wvp_device_channel" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "device_id" text COLLATE "pg_catalog"."default",
  "name" text COLLATE "pg_catalog"."default",
  "manufacturer" text COLLATE "pg_catalog"."default",
  "db_model" text COLLATE "pg_catalog"."default",
  "owner" text COLLATE "pg_catalog"."default",
  "civil_code" text COLLATE "pg_catalog"."default",
  "block" text COLLATE "pg_catalog"."default",
  "address" text COLLATE "pg_catalog"."default",
  "parental" int4,
  "parent_id" text COLLATE "pg_catalog"."default",
  "safety_way" int4,
  "register_way" int4,
  "cert_num" text COLLATE "pg_catalog"."default",
  "certifiable" int4,
  "err_code" int4,
  "end_time" text COLLATE "pg_catalog"."default",
  "secrecy" int4,
  "ip_address" text COLLATE "pg_catalog"."default",
  "port" int4,
  "password" text COLLATE "pg_catalog"."default",
  "status" text COLLATE "pg_catalog"."default" DEFAULT 'OFF'::text,
  "longitude" float8,
  "latitude" float8,
  "ptz_type" int4,
  "position_type" int4,
  "room_type" int4,
  "use_type" int4,
  "supply_light_type" int4,
  "direction_type" int4,
  "resolution" text COLLATE "pg_catalog"."default",
  "business_group_id" text COLLATE "pg_catalog"."default",
  "download_speed" text COLLATE "pg_catalog"."default",
  "svc_space_support_mod" int4,
  "svc_time_support_mode" int4,
  "create_time" text COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" text COLLATE "pg_catalog"."default" NOT NULL,
  "sub_count" int4,
  "stream_id" text COLLATE "pg_catalog"."default",
  "has_audio" bool DEFAULT false,
  "gps_time" text COLLATE "pg_catalog"."default",
  "stream_identification" text COLLATE "pg_catalog"."default",
  "channel_type" int4 NOT NULL DEFAULT 0,
  "gb_device_id" text COLLATE "pg_catalog"."default",
  "gb_name" text COLLATE "pg_catalog"."default",
  "gb_manufacturer" text COLLATE "pg_catalog"."default",
  "gb_model" text COLLATE "pg_catalog"."default",
  "gb_owner" text COLLATE "pg_catalog"."default",
  "gb_civil_code" text COLLATE "pg_catalog"."default",
  "gb_block" text COLLATE "pg_catalog"."default",
  "gb_address" text COLLATE "pg_catalog"."default",
  "gb_parental" int4,
  "gb_parent_id" text COLLATE "pg_catalog"."default",
  "gb_safety_way" int4,
  "gb_register_way" int4,
  "gb_cert_num" text COLLATE "pg_catalog"."default",
  "gb_certifiable" int4,
  "gb_err_code" int4,
  "gb_end_time" text COLLATE "pg_catalog"."default",
  "gb_secrecy" int4,
  "gb_ip_address" text COLLATE "pg_catalog"."default",
  "gb_port" int4,
  "gb_password" text COLLATE "pg_catalog"."default",
  "gb_status" text COLLATE "pg_catalog"."default",
  "gb_longitude" float8,
  "gb_latitude" float8,
  "gb_business_group_id" text COLLATE "pg_catalog"."default",
  "gb_ptz_type" int4,
  "gb_position_type" int4,
  "gb_room_type" int4,
  "gb_use_type" int4,
  "gb_supply_light_type" int4,
  "gb_direction_type" int4,
  "gb_resolution" text COLLATE "pg_catalog"."default",
  "gb_download_speed" text COLLATE "pg_catalog"."default",
  "gb_svc_space_support_mod" int4,
  "gb_svc_time_support_mode" int4,
  "record_plan_id" int4,
  "data_type" int4 NOT NULL,
  "data_device_id" int4 NOT NULL,
  "channel_no" text COLLATE "pg_catalog"."default",
  "src_url" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "remark" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."wvp_device_channel"."channel_no" IS '通道号';
COMMENT ON COLUMN "public"."wvp_device_channel"."src_url" IS '推流地址';
COMMENT ON COLUMN "public"."wvp_device_channel"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."wvp_device_channel"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."wvp_device_channel"."remark" IS '备注';

-- ----------------------------
-- Records of wvp_device_channel
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_device_mobile_position
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_device_mobile_position";
CREATE TABLE "public"."wvp_device_mobile_position" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "device_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "channel_id" text COLLATE "pg_catalog"."default" NOT NULL,
  "device_name" text COLLATE "pg_catalog"."default",
  "time" text COLLATE "pg_catalog"."default",
  "longitude" float8,
  "latitude" float8,
  "altitude" float8,
  "speed" float8,
  "direction" float8,
  "report_source" text COLLATE "pg_catalog"."default",
  "create_time" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of wvp_device_mobile_position
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_favorites
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_favorites";
CREATE TABLE "public"."wvp_favorites" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "favorites_name" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default" DEFAULT ''::text
)
;
COMMENT ON COLUMN "public"."wvp_favorites"."id" IS 'id';
COMMENT ON COLUMN "public"."wvp_favorites"."favorites_name" IS '收藏夹名称';
COMMENT ON COLUMN "public"."wvp_favorites"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."wvp_favorites"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wvp_favorites"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."wvp_favorites"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."wvp_favorites"."remark" IS '备注';

-- ----------------------------
-- Records of wvp_favorites
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_favorites_channel
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_favorites_channel";
CREATE TABLE "public"."wvp_favorites_channel" (
  "channel_id" int8 NOT NULL,
  "favorites_id" int8,
  "gb_name" text COLLATE "pg_catalog"."default",
  "gb_parentid" text COLLATE "pg_catalog"."default",
  "gb_deviceid" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default" DEFAULT ''::text
)
;
COMMENT ON COLUMN "public"."wvp_favorites_channel"."channel_id" IS '国标通道id';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."favorites_id" IS '收藏夹id';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."gb_name" IS '通道名称';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."gb_parentid" IS '设备id';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."gb_deviceid" IS '通道id';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."wvp_favorites_channel"."remark" IS '备注';

-- ----------------------------
-- Records of wvp_favorites_channel
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_mark
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_mark";
CREATE TABLE "public"."wvp_mark" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "mark_name" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default" DEFAULT ''::text
)
;
COMMENT ON COLUMN "public"."wvp_mark"."id" IS 'id';
COMMENT ON COLUMN "public"."wvp_mark"."mark_name" IS '标记照片';
COMMENT ON COLUMN "public"."wvp_mark"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."wvp_mark"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wvp_mark"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."wvp_mark"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."wvp_mark"."remark" IS '备注';

-- ----------------------------
-- Records of wvp_mark
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_mark_channel
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_mark_channel";
CREATE TABLE "public"."wvp_mark_channel" (
  "channel_id" int8 NOT NULL,
  "mark_id" int8,
  "gb_name" text COLLATE "pg_catalog"."default",
  "gb_parentid" text COLLATE "pg_catalog"."default",
  "gb_deviceid" text COLLATE "pg_catalog"."default",
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default" DEFAULT ''::text
)
;
COMMENT ON COLUMN "public"."wvp_mark_channel"."channel_id" IS '国标通道id';
COMMENT ON COLUMN "public"."wvp_mark_channel"."mark_id" IS '标记id';
COMMENT ON COLUMN "public"."wvp_mark_channel"."gb_name" IS '通道名称';
COMMENT ON COLUMN "public"."wvp_mark_channel"."gb_parentid" IS '设备id';
COMMENT ON COLUMN "public"."wvp_mark_channel"."gb_deviceid" IS '通道id';
COMMENT ON COLUMN "public"."wvp_mark_channel"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."wvp_mark_channel"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wvp_mark_channel"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."wvp_mark_channel"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."wvp_mark_channel"."remark" IS '备注';

-- ----------------------------
-- Records of wvp_mark_channel
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_media_server
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_media_server";
CREATE TABLE "public"."wvp_media_server" (
  "id" text COLLATE "pg_catalog"."default" NOT NULL,
  "ip" text COLLATE "pg_catalog"."default",
  "hook_ip" text COLLATE "pg_catalog"."default",
  "sdp_ip" text COLLATE "pg_catalog"."default",
  "stream_ip" text COLLATE "pg_catalog"."default",
  "http_port" int4,
  "http_ssl_port" int4,
  "rtmp_port" int4,
  "rtmp_ssl_port" int4,
  "rtp_proxy_port" int4,
  "rtsp_port" int4,
  "rtsp_ssl_port" int4,
  "flv_port" int4,
  "flv_ssl_port" int4,
  "ws_flv_port" int4,
  "ws_flv_ssl_port" int4,
  "auto_config" bool DEFAULT false,
  "secret" text COLLATE "pg_catalog"."default",
  "type" text COLLATE "pg_catalog"."default" DEFAULT 'zlm'::text,
  "rtp_enable" bool DEFAULT false,
  "rtp_port_range" text COLLATE "pg_catalog"."default",
  "send_rtp_port_range" text COLLATE "pg_catalog"."default",
  "record_assist_port" int4,
  "default_server" bool DEFAULT false,
  "create_time" text COLLATE "pg_catalog"."default",
  "update_time" text COLLATE "pg_catalog"."default",
  "hook_alive_interval" int4,
  "record_path" text COLLATE "pg_catalog"."default",
  "record_day" int4 DEFAULT 7,
  "transcode_suffix" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of wvp_media_server
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_platform
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_platform";
CREATE TABLE "public"."wvp_platform" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "enable" bool DEFAULT false,
  "name" text COLLATE "pg_catalog"."default",
  "server_gb_id" text COLLATE "pg_catalog"."default",
  "server_gb_domain" text COLLATE "pg_catalog"."default",
  "server_ip" text COLLATE "pg_catalog"."default",
  "server_port" int4,
  "device_gb_id" text COLLATE "pg_catalog"."default",
  "device_ip" text COLLATE "pg_catalog"."default",
  "device_port" text COLLATE "pg_catalog"."default",
  "username" text COLLATE "pg_catalog"."default",
  "password" text COLLATE "pg_catalog"."default",
  "expires" text COLLATE "pg_catalog"."default",
  "keep_timeout" text COLLATE "pg_catalog"."default",
  "transport" text COLLATE "pg_catalog"."default",
  "civil_code" text COLLATE "pg_catalog"."default",
  "manufacturer" text COLLATE "pg_catalog"."default",
  "db_model" text COLLATE "pg_catalog"."default",
  "address" text COLLATE "pg_catalog"."default",
  "character_set" text COLLATE "pg_catalog"."default",
  "ptz" bool DEFAULT false,
  "rtcp" bool DEFAULT false,
  "status" bool DEFAULT false,
  "catalog_group" int4,
  "register_way" int4,
  "secrecy" int4,
  "create_time" text COLLATE "pg_catalog"."default",
  "update_time" text COLLATE "pg_catalog"."default",
  "as_message_channel" bool DEFAULT false,
  "catalog_with_platform" int4 DEFAULT 1,
  "catalog_with_group" int4 DEFAULT 1,
  "catalog_with_region" int4 DEFAULT 1,
  "auto_push_channel" bool DEFAULT true,
  "send_stream_ip" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of wvp_platform
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_platform_channel
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_platform_channel";
CREATE TABLE "public"."wvp_platform_channel" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "platform_id" int4,
  "device_channel_id" int4,
  "custom_device_id" text COLLATE "pg_catalog"."default",
  "custom_name" text COLLATE "pg_catalog"."default",
  "custom_manufacturer" text COLLATE "pg_catalog"."default",
  "custom_model" text COLLATE "pg_catalog"."default",
  "custom_owner" text COLLATE "pg_catalog"."default",
  "custom_civil_code" text COLLATE "pg_catalog"."default",
  "custom_block" text COLLATE "pg_catalog"."default",
  "custom_address" text COLLATE "pg_catalog"."default",
  "custom_parental" int4,
  "custom_parent_id" text COLLATE "pg_catalog"."default",
  "custom_safety_way" int4,
  "custom_register_way" int4,
  "custom_cert_num" text COLLATE "pg_catalog"."default",
  "custom_certifiable" int4,
  "custom_err_code" int4,
  "custom_end_time" text COLLATE "pg_catalog"."default",
  "custom_secrecy" int4,
  "custom_ip_address" text COLLATE "pg_catalog"."default",
  "custom_port" int4,
  "custom_password" text COLLATE "pg_catalog"."default",
  "custom_status" text COLLATE "pg_catalog"."default",
  "custom_longitude" float8,
  "custom_latitude" float8,
  "custom_ptz_type" int4,
  "custom_position_type" int4,
  "custom_room_type" int4,
  "custom_use_type" int4,
  "custom_supply_light_type" int4,
  "custom_direction_type" int4,
  "custom_resolution" text COLLATE "pg_catalog"."default",
  "custom_business_group_id" text COLLATE "pg_catalog"."default",
  "custom_download_speed" text COLLATE "pg_catalog"."default",
  "custom_svc_space_support_mod" int4,
  "custom_svc_time_support_mode" int4
)
;

-- ----------------------------
-- Records of wvp_platform_channel
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_platform_group
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_platform_group";
CREATE TABLE "public"."wvp_platform_group" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "platform_id" int4,
  "group_id" int4
)
;

-- ----------------------------
-- Records of wvp_platform_group
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_platform_region
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_platform_region";
CREATE TABLE "public"."wvp_platform_region" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "platform_id" int4,
  "region_id" int4
)
;

-- ----------------------------
-- Records of wvp_platform_region
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_record_plan
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_record_plan";
CREATE TABLE "public"."wvp_record_plan" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "snap" bool DEFAULT false,
  "name" text COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" text COLLATE "pg_catalog"."default",
  "update_time" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of wvp_record_plan
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_record_plan_item
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_record_plan_item";
CREATE TABLE "public"."wvp_record_plan_item" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "start" int4,
  "stop" int4,
  "week_day" int4,
  "plan_id" int4,
  "create_time" text COLLATE "pg_catalog"."default",
  "update_time" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of wvp_record_plan_item
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_resources_tree
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_resources_tree";
CREATE TABLE "public"."wvp_resources_tree" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "is_catalog" bool DEFAULT true,
  "device_channel_id" int4,
  "gb_stream_id" int4,
  "name" text COLLATE "pg_catalog"."default",
  "parentid" int4,
  "path" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of wvp_resources_tree
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_sip_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_sip_config";
CREATE TABLE "public"."wvp_sip_config" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "ip" text COLLATE "pg_catalog"."default",
  "show_ip" text COLLATE "pg_catalog"."default",
  "port" int4 NOT NULL DEFAULT 8116,
  "gb_domain" text COLLATE "pg_catalog"."default" NOT NULL DEFAULT '4101050000'::text,
  "server_id" text COLLATE "pg_catalog"."default" NOT NULL DEFAULT '41010500002000000001'::text,
  "gb_pwd" text COLLATE "pg_catalog"."default" DEFAULT '12345678'::text,
  "ptz_speed" int4 DEFAULT 50,
  "register_time_interval" int4 DEFAULT 120,
  "alarm" bool DEFAULT false,
  "timeout" int8 DEFAULT 15,
  "create_time" text COLLATE "pg_catalog"."default",
  "update_time" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."wvp_sip_config"."id" IS '主键ID';
COMMENT ON COLUMN "public"."wvp_sip_config"."ip" IS 'SIP服务监听的IP地址，多个IP用逗号分隔';
COMMENT ON COLUMN "public"."wvp_sip_config"."show_ip" IS '对外展示的IP地址';
COMMENT ON COLUMN "public"."wvp_sip_config"."port" IS 'SIP服务监听的端口';
COMMENT ON COLUMN "public"."wvp_sip_config"."gb_domain" IS '国标域';
COMMENT ON COLUMN "public"."wvp_sip_config"."server_id" IS '国标服务器ID';
COMMENT ON COLUMN "public"."wvp_sip_config"."gb_pwd" IS '设备认证密码';
COMMENT ON COLUMN "public"."wvp_sip_config"."ptz_speed" IS '云台速度';
COMMENT ON COLUMN "public"."wvp_sip_config"."register_time_interval" IS '注册时间间隔（秒）';
COMMENT ON COLUMN "public"."wvp_sip_config"."alarm" IS '是否接收报警信息';
COMMENT ON COLUMN "public"."wvp_sip_config"."timeout" IS '超时时间（秒）';
COMMENT ON COLUMN "public"."wvp_sip_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wvp_sip_config"."update_time" IS '更新时间';

-- ----------------------------
-- Records of wvp_sip_config
-- ----------------------------
INSERT INTO "public"."wvp_sip_config" VALUES (1, '192.168.228.134', NULL, 15060, '3402000000', '34020000002000000001', 'gbs12345', NULL, NULL, 'f', NULL, NULL, '2026-07-30 19:24:01');

-- ----------------------------
-- Table structure for wvp_stream_proxy
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_stream_proxy";
CREATE TABLE "public"."wvp_stream_proxy" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "type" text COLLATE "pg_catalog"."default",
  "app" text COLLATE "pg_catalog"."default",
  "stream" text COLLATE "pg_catalog"."default",
  "src_url" text COLLATE "pg_catalog"."default",
  "timeout" int4,
  "ffmpeg_cmd_key" text COLLATE "pg_catalog"."default",
  "rtsp_type" text COLLATE "pg_catalog"."default",
  "media_server_id" text COLLATE "pg_catalog"."default",
  "enable_audio" bool DEFAULT false,
  "enable_mp4" bool DEFAULT false,
  "pulling" bool DEFAULT false,
  "enable" bool DEFAULT false,
  "enable_remove_none_reader" bool DEFAULT false,
  "create_time" text COLLATE "pg_catalog"."default",
  "name" text COLLATE "pg_catalog"."default",
  "update_time" text COLLATE "pg_catalog"."default",
  "stream_key" text COLLATE "pg_catalog"."default",
  "enable_disable_none_reader" bool DEFAULT false,
  "relates_media_server_id" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of wvp_stream_proxy
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_stream_push
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_stream_push";
CREATE TABLE "public"."wvp_stream_push" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "app" text COLLATE "pg_catalog"."default",
  "stream" text COLLATE "pg_catalog"."default",
  "create_time" text COLLATE "pg_catalog"."default",
  "media_server_id" text COLLATE "pg_catalog"."default",
  "server_id" text COLLATE "pg_catalog"."default",
  "push_time" text COLLATE "pg_catalog"."default",
  "status" bool DEFAULT false,
  "update_time" text COLLATE "pg_catalog"."default",
  "pushing" bool DEFAULT false,
  "self" bool DEFAULT false,
  "start_offline_push" bool DEFAULT true
)
;

-- ----------------------------
-- Records of wvp_stream_push
-- ----------------------------

-- ----------------------------
-- Table structure for wvp_user_api_key
-- ----------------------------
DROP TABLE IF EXISTS "public"."wvp_user_api_key";
CREATE TABLE "public"."wvp_user_api_key" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "user_id" int8 NOT NULL,
  "access_key" text COLLATE "pg_catalog"."default" NOT NULL,
  "secret_key" text COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" text COLLATE "pg_catalog"."default",
  "expired_at" timestamp(6),
  "enable" bool DEFAULT true,
  "create_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "create_time" timestamp(6),
  "update_by" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "update_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default" DEFAULT ''::text
)
;
COMMENT ON COLUMN "public"."wvp_user_api_key"."id" IS '主键ID';
COMMENT ON COLUMN "public"."wvp_user_api_key"."user_id" IS '关联用户ID';
COMMENT ON COLUMN "public"."wvp_user_api_key"."access_key" IS 'Access Key (AK)';
COMMENT ON COLUMN "public"."wvp_user_api_key"."secret_key" IS 'Secret Key (SK)';
COMMENT ON COLUMN "public"."wvp_user_api_key"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."wvp_user_api_key"."expired_at" IS '过期时间';
COMMENT ON COLUMN "public"."wvp_user_api_key"."enable" IS '启用状态 (1:启用, 0:停用)';
COMMENT ON COLUMN "public"."wvp_user_api_key"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."wvp_user_api_key"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wvp_user_api_key"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."wvp_user_api_key"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."wvp_user_api_key"."remark" IS '备注';

-- ----------------------------
-- Records of wvp_user_api_key
-- ----------------------------
INSERT INTO "public"."wvp_user_api_key" VALUES (1, 2, 'ak_hOPw-NgBYpFijEzQFWUd6y4n', 'AobdttzX1MiYBNA9sRjvjozh5a8kr7CyttCiF0qSZrI02zjN7f85bZmcquPhgK18', 'test', NULL, 't', 'admin', '2026-07-19 17:30:40', 'admin', '2026-07-19 17:30:40', '');

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."gen_table_column_column_id_seq"
OWNED BY "public"."gen_table_column"."column_id";
SELECT setval('"public"."gen_table_column_column_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."gen_table_table_id_seq"
OWNED BY "public"."gen_table"."table_id";
SELECT setval('"public"."gen_table_table_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_base_config_config_id_seq"
OWNED BY "public"."sys_base_config"."config_id";
SELECT setval('"public"."sys_base_config_config_id_seq"', 2, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_config_config_id_seq"
OWNED BY "public"."sys_config"."config_id";
SELECT setval('"public"."sys_config_config_id_seq"', 24, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dept_dept_id_seq"
OWNED BY "public"."sys_dept"."dept_id";
SELECT setval('"public"."sys_dept_dept_id_seq"', 112, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dict_data_dict_code_seq"
OWNED BY "public"."sys_dict_data"."dict_code";
SELECT setval('"public"."sys_dict_data_dict_code_seq"', 62, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dict_type_dict_id_seq"
OWNED BY "public"."sys_dict_type"."dict_id";
SELECT setval('"public"."sys_dict_type_dict_id_seq"', 21, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_job_job_id_seq"
OWNED BY "public"."sys_job"."job_id";
SELECT setval('"public"."sys_job_job_id_seq"', 6, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_job_log_job_log_id_seq"
OWNED BY "public"."sys_job_log"."job_log_id";
SELECT setval('"public"."sys_job_log_job_log_id_seq"', 5, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_logininfor_info_id_seq"
OWNED BY "public"."sys_logininfor"."info_id";
SELECT setval('"public"."sys_logininfor_info_id_seq"', 792, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_menu_menu_id_seq"
OWNED BY "public"."sys_menu"."menu_id";
SELECT setval('"public"."sys_menu_menu_id_seq"', 2163, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_notice_notice_id_seq"
OWNED BY "public"."sys_notice"."notice_id";
SELECT setval('"public"."sys_notice_notice_id_seq"', 3, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_oper_log_oper_id_seq"
OWNED BY "public"."sys_oper_log"."oper_id";
SELECT setval('"public"."sys_oper_log_oper_id_seq"', 2142, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_post_post_id_seq"
OWNED BY "public"."sys_post"."post_id";
SELECT setval('"public"."sys_post_post_id_seq"', 7, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_role_role_id_seq"
OWNED BY "public"."sys_role"."role_id";
SELECT setval('"public"."sys_role_role_id_seq"', 4, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_user_id_seq"
OWNED BY "public"."sys_user"."user_id";
SELECT setval('"public"."sys_user_user_id_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_cloud_record_id_seq"
OWNED BY "public"."wvp_cloud_record"."id";
SELECT setval('"public"."wvp_cloud_record_id_seq"', 285, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_common_group_id_seq"
OWNED BY "public"."wvp_common_group"."id";
SELECT setval('"public"."wvp_common_group_id_seq"', 6, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_common_region_id_seq"
OWNED BY "public"."wvp_common_region"."id";
SELECT setval('"public"."wvp_common_region_id_seq"', 7, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_device_alarm_id_seq"
OWNED BY "public"."wvp_device_alarm"."id";
SELECT setval('"public"."wvp_device_alarm_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_device_channel_id_seq"
OWNED BY "public"."wvp_device_channel"."id";
SELECT setval('"public"."wvp_device_channel_id_seq"', 1185, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_device_id_seq"
OWNED BY "public"."wvp_device"."id";
SELECT setval('"public"."wvp_device_id_seq"', 1115, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_device_mobile_position_id_seq"
OWNED BY "public"."wvp_device_mobile_position"."id";
SELECT setval('"public"."wvp_device_mobile_position_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_favorites_id_seq"
OWNED BY "public"."wvp_favorites"."id";
SELECT setval('"public"."wvp_favorites_id_seq"', 3, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_mark_id_seq"
OWNED BY "public"."wvp_mark"."id";
SELECT setval('"public"."wvp_mark_id_seq"', 3, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_platform_channel_id_seq"
OWNED BY "public"."wvp_platform_channel"."id";
SELECT setval('"public"."wvp_platform_channel_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_platform_group_id_seq"
OWNED BY "public"."wvp_platform_group"."id";
SELECT setval('"public"."wvp_platform_group_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_platform_id_seq"
OWNED BY "public"."wvp_platform"."id";
SELECT setval('"public"."wvp_platform_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_platform_region_id_seq"
OWNED BY "public"."wvp_platform_region"."id";
SELECT setval('"public"."wvp_platform_region_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_record_plan_id_seq"
OWNED BY "public"."wvp_record_plan"."id";
SELECT setval('"public"."wvp_record_plan_id_seq"', 2, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_record_plan_item_id_seq"
OWNED BY "public"."wvp_record_plan_item"."id";
SELECT setval('"public"."wvp_record_plan_item_id_seq"', 3, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_resources_tree_id_seq"
OWNED BY "public"."wvp_resources_tree"."id";
SELECT setval('"public"."wvp_resources_tree_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_sip_config_id_seq"
OWNED BY "public"."wvp_sip_config"."id";
SELECT setval('"public"."wvp_sip_config_id_seq"', 2, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_stream_proxy_id_seq"
OWNED BY "public"."wvp_stream_proxy"."id";
SELECT setval('"public"."wvp_stream_proxy_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_stream_push_id_seq"
OWNED BY "public"."wvp_stream_push"."id";
SELECT setval('"public"."wvp_stream_push_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."wvp_user_api_key_id_seq"
OWNED BY "public"."wvp_user_api_key"."id";
SELECT setval('"public"."wvp_user_api_key_id_seq"', 2, false);

-- ----------------------------
-- Auto increment value for gen_table
-- ----------------------------
SELECT setval('"public"."gen_table_table_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table gen_table
-- ----------------------------
ALTER TABLE "public"."gen_table" ADD CONSTRAINT "gen_table_pkey" PRIMARY KEY ("table_id");

-- ----------------------------
-- Auto increment value for gen_table_column
-- ----------------------------
SELECT setval('"public"."gen_table_column_column_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table gen_table_column
-- ----------------------------
ALTER TABLE "public"."gen_table_column" ADD CONSTRAINT "gen_table_column_pkey" PRIMARY KEY ("column_id");

-- ----------------------------
-- Primary Key structure for table qrtz_blob_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_blob_triggers" ADD CONSTRAINT "qrtz_blob_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_calendars
-- ----------------------------
ALTER TABLE "public"."qrtz_calendars" ADD CONSTRAINT "qrtz_calendars_pkey" PRIMARY KEY ("sched_name", "calendar_name");

-- ----------------------------
-- Primary Key structure for table qrtz_cron_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_cron_triggers" ADD CONSTRAINT "qrtz_cron_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_fired_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_fired_triggers" ADD CONSTRAINT "qrtz_fired_triggers_pkey" PRIMARY KEY ("sched_name", "entry_id");

-- ----------------------------
-- Primary Key structure for table qrtz_job_details
-- ----------------------------
ALTER TABLE "public"."qrtz_job_details" ADD CONSTRAINT "qrtz_job_details_pkey" PRIMARY KEY ("sched_name", "job_name", "job_group");

-- ----------------------------
-- Primary Key structure for table qrtz_locks
-- ----------------------------
ALTER TABLE "public"."qrtz_locks" ADD CONSTRAINT "qrtz_locks_pkey" PRIMARY KEY ("sched_name", "lock_name");

-- ----------------------------
-- Primary Key structure for table qrtz_paused_trigger_grps
-- ----------------------------
ALTER TABLE "public"."qrtz_paused_trigger_grps" ADD CONSTRAINT "qrtz_paused_trigger_grps_pkey" PRIMARY KEY ("sched_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_scheduler_state
-- ----------------------------
ALTER TABLE "public"."qrtz_scheduler_state" ADD CONSTRAINT "qrtz_scheduler_state_pkey" PRIMARY KEY ("sched_name", "instance_name");

-- ----------------------------
-- Primary Key structure for table qrtz_simple_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simple_triggers" ADD CONSTRAINT "qrtz_simple_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_simprop_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simprop_triggers" ADD CONSTRAINT "qrtz_simprop_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Indexes structure for table qrtz_triggers
-- ----------------------------
CREATE INDEX "sched_name" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table qrtz_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_triggers" ADD CONSTRAINT "qrtz_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Auto increment value for sys_base_config
-- ----------------------------
SELECT setval('"public"."sys_base_config_config_id_seq"', 2, false);

-- ----------------------------
-- Primary Key structure for table sys_base_config
-- ----------------------------
ALTER TABLE "public"."sys_base_config" ADD CONSTRAINT "sys_base_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Auto increment value for sys_config
-- ----------------------------
SELECT setval('"public"."sys_config_config_id_seq"', 24, false);

-- ----------------------------
-- Primary Key structure for table sys_config
-- ----------------------------
ALTER TABLE "public"."sys_config" ADD CONSTRAINT "sys_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Auto increment value for sys_dept
-- ----------------------------
SELECT setval('"public"."sys_dept_dept_id_seq"', 112, false);

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("dept_id");

-- ----------------------------
-- Auto increment value for sys_dict_data
-- ----------------------------
SELECT setval('"public"."sys_dict_data_dict_code_seq"', 62, false);

-- ----------------------------
-- Primary Key structure for table sys_dict_data
-- ----------------------------
ALTER TABLE "public"."sys_dict_data" ADD CONSTRAINT "sys_dict_data_pkey" PRIMARY KEY ("dict_code");

-- ----------------------------
-- Auto increment value for sys_dict_type
-- ----------------------------
SELECT setval('"public"."sys_dict_type_dict_id_seq"', 21, false);

-- ----------------------------
-- Uniques structure for table sys_dict_type
-- ----------------------------
ALTER TABLE "public"."sys_dict_type" ADD CONSTRAINT "dict_type" UNIQUE ("dict_type");

-- ----------------------------
-- Primary Key structure for table sys_dict_type
-- ----------------------------
ALTER TABLE "public"."sys_dict_type" ADD CONSTRAINT "sys_dict_type_pkey" PRIMARY KEY ("dict_id");

-- ----------------------------
-- Auto increment value for sys_job
-- ----------------------------
SELECT setval('"public"."sys_job_job_id_seq"', 6, false);

-- ----------------------------
-- Primary Key structure for table sys_job
-- ----------------------------
ALTER TABLE "public"."sys_job" ADD CONSTRAINT "sys_job_pkey" PRIMARY KEY ("job_id", "job_name", "job_group");

-- ----------------------------
-- Auto increment value for sys_job_log
-- ----------------------------
SELECT setval('"public"."sys_job_log_job_log_id_seq"', 5, false);

-- ----------------------------
-- Primary Key structure for table sys_job_log
-- ----------------------------
ALTER TABLE "public"."sys_job_log" ADD CONSTRAINT "sys_job_log_pkey" PRIMARY KEY ("job_log_id");

-- ----------------------------
-- Auto increment value for sys_logininfor
-- ----------------------------
SELECT setval('"public"."sys_logininfor_info_id_seq"', 792, true);

-- ----------------------------
-- Indexes structure for table sys_logininfor
-- ----------------------------
CREATE INDEX "idx_sys_logininfor_lt" ON "public"."sys_logininfor" USING btree (
  "login_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sys_logininfor_s" ON "public"."sys_logininfor" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."bpchar_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_logininfor
-- ----------------------------
ALTER TABLE "public"."sys_logininfor" ADD CONSTRAINT "sys_logininfor_pkey" PRIMARY KEY ("info_id");

-- ----------------------------
-- Auto increment value for sys_menu
-- ----------------------------
SELECT setval('"public"."sys_menu_menu_id_seq"', 2163, false);

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu" ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("menu_id");

-- ----------------------------
-- Auto increment value for sys_notice
-- ----------------------------
SELECT setval('"public"."sys_notice_notice_id_seq"', 3, false);

-- ----------------------------
-- Primary Key structure for table sys_notice
-- ----------------------------
ALTER TABLE "public"."sys_notice" ADD CONSTRAINT "sys_notice_pkey" PRIMARY KEY ("notice_id");

-- ----------------------------
-- Auto increment value for sys_oper_log
-- ----------------------------
SELECT setval('"public"."sys_oper_log_oper_id_seq"', 2142, true);

-- ----------------------------
-- Indexes structure for table sys_oper_log
-- ----------------------------
CREATE INDEX "idx_sys_oper_log_bt" ON "public"."sys_oper_log" USING btree (
  "business_type" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sys_oper_log_ot" ON "public"."sys_oper_log" USING btree (
  "oper_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sys_oper_log_s" ON "public"."sys_oper_log" USING btree (
  "status" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_oper_log
-- ----------------------------
ALTER TABLE "public"."sys_oper_log" ADD CONSTRAINT "sys_oper_log_pkey" PRIMARY KEY ("oper_id");

-- ----------------------------
-- Auto increment value for sys_post
-- ----------------------------
SELECT setval('"public"."sys_post_post_id_seq"', 7, false);

-- ----------------------------
-- Primary Key structure for table sys_post
-- ----------------------------
ALTER TABLE "public"."sys_post" ADD CONSTRAINT "sys_post_pkey" PRIMARY KEY ("post_id");

-- ----------------------------
-- Auto increment value for sys_role
-- ----------------------------
SELECT setval('"public"."sys_role_role_id_seq"', 4, true);

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("role_id");

-- ----------------------------
-- Primary Key structure for table sys_role_dept
-- ----------------------------
ALTER TABLE "public"."sys_role_dept" ADD CONSTRAINT "sys_role_dept_pkey" PRIMARY KEY ("role_id", "dept_id");

-- ----------------------------
-- Primary Key structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "public"."sys_role_menu" ADD CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("role_id", "menu_id");

-- ----------------------------
-- Auto increment value for sys_user
-- ----------------------------
SELECT setval('"public"."sys_user_user_id_seq"', 6, true);

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("user_id");

-- ----------------------------
-- Primary Key structure for table sys_user_channel
-- ----------------------------
ALTER TABLE "public"."sys_user_channel" ADD CONSTRAINT "sys_user_channel_pkey" PRIMARY KEY ("user_id", "channel_id");

-- ----------------------------
-- Primary Key structure for table sys_user_post
-- ----------------------------
ALTER TABLE "public"."sys_user_post" ADD CONSTRAINT "sys_user_post_pkey" PRIMARY KEY ("user_id", "post_id");

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role" ADD CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("user_id", "role_id");

-- ----------------------------
-- Auto increment value for wvp_cloud_record
-- ----------------------------
SELECT setval('"public"."wvp_cloud_record_id_seq"', 285, false);

-- ----------------------------
-- Primary Key structure for table wvp_cloud_record
-- ----------------------------
ALTER TABLE "public"."wvp_cloud_record" ADD CONSTRAINT "wvp_cloud_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_common_group
-- ----------------------------
SELECT setval('"public"."wvp_common_group_id_seq"', 6, false);

-- ----------------------------
-- Uniques structure for table wvp_common_group
-- ----------------------------
ALTER TABLE "public"."wvp_common_group" ADD CONSTRAINT "uk_common_group_device_platform" UNIQUE ("device_id");

-- ----------------------------
-- Primary Key structure for table wvp_common_group
-- ----------------------------
ALTER TABLE "public"."wvp_common_group" ADD CONSTRAINT "wvp_common_group_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_common_region
-- ----------------------------
SELECT setval('"public"."wvp_common_region_id_seq"', 7, false);

-- ----------------------------
-- Uniques structure for table wvp_common_region
-- ----------------------------
ALTER TABLE "public"."wvp_common_region" ADD CONSTRAINT "uk_common_region_device_id" UNIQUE ("device_id");

-- ----------------------------
-- Primary Key structure for table wvp_common_region
-- ----------------------------
ALTER TABLE "public"."wvp_common_region" ADD CONSTRAINT "wvp_common_region_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_device
-- ----------------------------
SELECT setval('"public"."wvp_device_id_seq"', 1115, true);

-- ----------------------------
-- Uniques structure for table wvp_device
-- ----------------------------
ALTER TABLE "public"."wvp_device" ADD CONSTRAINT "uk_device_device" UNIQUE ("device_id");

-- ----------------------------
-- Primary Key structure for table wvp_device
-- ----------------------------
ALTER TABLE "public"."wvp_device" ADD CONSTRAINT "wvp_device_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_device_alarm
-- ----------------------------
SELECT setval('"public"."wvp_device_alarm_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table wvp_device_alarm
-- ----------------------------
ALTER TABLE "public"."wvp_device_alarm" ADD CONSTRAINT "wvp_device_alarm_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_device_channel
-- ----------------------------
SELECT setval('"public"."wvp_device_channel_id_seq"', 1185, true);

-- ----------------------------
-- Primary Key structure for table wvp_device_channel
-- ----------------------------
ALTER TABLE "public"."wvp_device_channel" ADD CONSTRAINT "wvp_device_channel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_device_mobile_position
-- ----------------------------
SELECT setval('"public"."wvp_device_mobile_position_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table wvp_device_mobile_position
-- ----------------------------
ALTER TABLE "public"."wvp_device_mobile_position" ADD CONSTRAINT "wvp_device_mobile_position_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_favorites
-- ----------------------------
SELECT setval('"public"."wvp_favorites_id_seq"', 3, false);

-- ----------------------------
-- Primary Key structure for table wvp_favorites
-- ----------------------------
ALTER TABLE "public"."wvp_favorites" ADD CONSTRAINT "wvp_favorites_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wvp_favorites_channel
-- ----------------------------
ALTER TABLE "public"."wvp_favorites_channel" ADD CONSTRAINT "wvp_favorites_channel_pkey" PRIMARY KEY ("channel_id");

-- ----------------------------
-- Auto increment value for wvp_mark
-- ----------------------------
SELECT setval('"public"."wvp_mark_id_seq"', 3, false);

-- ----------------------------
-- Primary Key structure for table wvp_mark
-- ----------------------------
ALTER TABLE "public"."wvp_mark" ADD CONSTRAINT "wvp_mark_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wvp_mark_channel
-- ----------------------------
ALTER TABLE "public"."wvp_mark_channel" ADD CONSTRAINT "wvp_mark_channel_pkey" PRIMARY KEY ("channel_id");

-- ----------------------------
-- Uniques structure for table wvp_media_server
-- ----------------------------
ALTER TABLE "public"."wvp_media_server" ADD CONSTRAINT "uk_media_server_unique_ip_http_port" UNIQUE ("ip", "http_port");

-- ----------------------------
-- Primary Key structure for table wvp_media_server
-- ----------------------------
ALTER TABLE "public"."wvp_media_server" ADD CONSTRAINT "wvp_media_server_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_platform
-- ----------------------------
SELECT setval('"public"."wvp_platform_id_seq"', 2, true);

-- ----------------------------
-- Uniques structure for table wvp_platform
-- ----------------------------
ALTER TABLE "public"."wvp_platform" ADD CONSTRAINT "uk_platform_unique_server_gb_id" UNIQUE ("server_gb_id");

-- ----------------------------
-- Primary Key structure for table wvp_platform
-- ----------------------------
ALTER TABLE "public"."wvp_platform" ADD CONSTRAINT "wvp_platform_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_platform_channel
-- ----------------------------
SELECT setval('"public"."wvp_platform_channel_id_seq"', 1, false);

-- ----------------------------
-- Uniques structure for table wvp_platform_channel
-- ----------------------------
ALTER TABLE "public"."wvp_platform_channel" ADD CONSTRAINT "uk_platform_gb_channel_platform_id_catalog_id_device_channel_id" UNIQUE ("platform_id", "device_channel_id");
ALTER TABLE "public"."wvp_platform_channel" ADD CONSTRAINT "uk_platform_gb_channel_device_id" UNIQUE ("custom_device_id");

-- ----------------------------
-- Primary Key structure for table wvp_platform_channel
-- ----------------------------
ALTER TABLE "public"."wvp_platform_channel" ADD CONSTRAINT "wvp_platform_channel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_platform_group
-- ----------------------------
SELECT setval('"public"."wvp_platform_group_id_seq"', 1, false);

-- ----------------------------
-- Uniques structure for table wvp_platform_group
-- ----------------------------
ALTER TABLE "public"."wvp_platform_group" ADD CONSTRAINT "uk_wvp_platform_group_platform_id_group_id" UNIQUE ("platform_id", "group_id");

-- ----------------------------
-- Primary Key structure for table wvp_platform_group
-- ----------------------------
ALTER TABLE "public"."wvp_platform_group" ADD CONSTRAINT "wvp_platform_group_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_platform_region
-- ----------------------------
SELECT setval('"public"."wvp_platform_region_id_seq"', 1, false);

-- ----------------------------
-- Uniques structure for table wvp_platform_region
-- ----------------------------
ALTER TABLE "public"."wvp_platform_region" ADD CONSTRAINT "uk_wvp_platform_region_platform_id_group_id" UNIQUE ("platform_id", "region_id");

-- ----------------------------
-- Primary Key structure for table wvp_platform_region
-- ----------------------------
ALTER TABLE "public"."wvp_platform_region" ADD CONSTRAINT "wvp_platform_region_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_record_plan
-- ----------------------------
SELECT setval('"public"."wvp_record_plan_id_seq"', 2, false);

-- ----------------------------
-- Primary Key structure for table wvp_record_plan
-- ----------------------------
ALTER TABLE "public"."wvp_record_plan" ADD CONSTRAINT "wvp_record_plan_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_record_plan_item
-- ----------------------------
SELECT setval('"public"."wvp_record_plan_item_id_seq"', 3, false);

-- ----------------------------
-- Primary Key structure for table wvp_record_plan_item
-- ----------------------------
ALTER TABLE "public"."wvp_record_plan_item" ADD CONSTRAINT "wvp_record_plan_item_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_resources_tree
-- ----------------------------
SELECT setval('"public"."wvp_resources_tree_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table wvp_resources_tree
-- ----------------------------
ALTER TABLE "public"."wvp_resources_tree" ADD CONSTRAINT "wvp_resources_tree_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_sip_config
-- ----------------------------
SELECT setval('"public"."wvp_sip_config_id_seq"', 2, false);

-- ----------------------------
-- Primary Key structure for table wvp_sip_config
-- ----------------------------
ALTER TABLE "public"."wvp_sip_config" ADD CONSTRAINT "wvp_sip_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_stream_proxy
-- ----------------------------
SELECT setval('"public"."wvp_stream_proxy_id_seq"', 1, false);

-- ----------------------------
-- Uniques structure for table wvp_stream_proxy
-- ----------------------------
ALTER TABLE "public"."wvp_stream_proxy" ADD CONSTRAINT "uk_stream_proxy_app_stream" UNIQUE ("app", "stream");

-- ----------------------------
-- Primary Key structure for table wvp_stream_proxy
-- ----------------------------
ALTER TABLE "public"."wvp_stream_proxy" ADD CONSTRAINT "wvp_stream_proxy_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_stream_push
-- ----------------------------
SELECT setval('"public"."wvp_stream_push_id_seq"', 1, false);

-- ----------------------------
-- Uniques structure for table wvp_stream_push
-- ----------------------------
ALTER TABLE "public"."wvp_stream_push" ADD CONSTRAINT "uk_stream_push_app_stream" UNIQUE ("app", "stream");

-- ----------------------------
-- Primary Key structure for table wvp_stream_push
-- ----------------------------
ALTER TABLE "public"."wvp_stream_push" ADD CONSTRAINT "wvp_stream_push_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for wvp_user_api_key
-- ----------------------------
SELECT setval('"public"."wvp_user_api_key_id_seq"', 2, false);

-- ----------------------------
-- Indexes structure for table wvp_user_api_key
-- ----------------------------
CREATE INDEX "idx_user_id" ON "public"."wvp_user_api_key" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Uniques structure for table wvp_user_api_key
-- ----------------------------
ALTER TABLE "public"."wvp_user_api_key" ADD CONSTRAINT "uk_access_key" UNIQUE ("access_key");

-- ----------------------------
-- Primary Key structure for table wvp_user_api_key
-- ----------------------------
ALTER TABLE "public"."wvp_user_api_key" ADD CONSTRAINT "wvp_user_api_key_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table qrtz_blob_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_blob_triggers" ADD CONSTRAINT "qrtz_blob_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE RESTRICT ON UPDATE RESTRICT;

-- ----------------------------
-- Foreign Keys structure for table qrtz_cron_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_cron_triggers" ADD CONSTRAINT "qrtz_cron_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE RESTRICT ON UPDATE RESTRICT;

-- ----------------------------
-- Foreign Keys structure for table qrtz_simple_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simple_triggers" ADD CONSTRAINT "qrtz_simple_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE RESTRICT ON UPDATE RESTRICT;

-- ----------------------------
-- Foreign Keys structure for table qrtz_simprop_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simprop_triggers" ADD CONSTRAINT "qrtz_simprop_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE RESTRICT ON UPDATE RESTRICT;

-- ----------------------------
-- Foreign Keys structure for table qrtz_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_triggers" ADD CONSTRAINT "qrtz_triggers_ibfk_1" FOREIGN KEY ("sched_name", "job_name", "job_group") REFERENCES "public"."qrtz_job_details" ("sched_name", "job_name", "job_group") ON DELETE RESTRICT ON UPDATE RESTRICT;
