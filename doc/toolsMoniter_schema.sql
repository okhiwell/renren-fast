CREATE TABLE `toolsMoniter` (
  `tools_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tools_name` varchar(50) NOT NULL COMMENT '工具名',
  `tools_url` varchar(50) DEFAULT NULL COMMENT '工具URL链接',
  `startCommand` varchar(1024) DEFAULT NULL COMMENT '工具启动命令',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态  0：禁用   1：正常',
  `operator` varchar(20) DEFAULT NULL COMMENT '拥有者名字',
  `remark` varchar(300) DEFAULT NULL COMMENT '描述',
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `add_by` bigint(20) DEFAULT NULL COMMENT '拥有者用户id',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改用户id',
  PRIMARY KEY (`tools_id`),
  UNIQUE KEY `tools_name` (`tools_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='三方工具表';





alter table test_api_case add clounm api_typee varchar(20) DEFAULT NULL COMMENT '接口类型';
alter table test_api_case add clounm base_dir` varchar(200) DEFAULT NULL COMMENT '项目根目录





# 用到了firework库中的数据
create database firework;
grant all on firework.* to renren_fast@"%" Identified by "123456ct";
FLUSH PRIVILEGES;