/*==============================================================*/
/* 修改username列名                                     */
/*==============================================================*/
ALTER TABLE ot_user CHANGE  COLUMN `name` `username` VARCHAR(255) NOT NULL;

/*==============================================================*/
/* 增加字段                                   */
/*==============================================================*/
ALTER TABLE ot_user ADD `password`  VARCHAR(50) NOT NULL DEFAULT '0000' COMMENT '密码';
ALTER TABLE ot_user ADD `realName` VARCHAR(20) COMMENT '真实姓名';
ALTER TABLE ot_user ADD `tel`  VARCHAR(20) COMMENT '手机号';
ALTER TABLE ot_user ADD `email` VARCHAR(20) COMMENT '邮箱';
ALTER TABLE ot_user ADD `create_time` DATETIME COMMENT '创建时间';
ALTER TABLE ot_user ADD `update_time` DATETIME COMMENT '更新时间';

/*==============================================================*/
/* 初始化数据                                    */
/*==============================================================*/
INSERT  INTO `ot_user`(`username`,`password`,`realName`,`tel`,`email`,`create_time`,`update_time`)
VALUES ('zhang','123','张三','15868379873','916218211@qq.com','2018-12-11 14:40:39','2018-12-11 14:40:57'),
('zhangsan','123','张三2','15868379873','916218211@qq.com','2018-12-11 14:40:39','2018-12-11 14:40:57'),
('lisi','123','李四2','15868379873','916218211@qq.com','2018-12-11 14:40:39','2018-12-11 14:40:57'),
('wangwu','123','王五2','15868379873','916218211@qq.com','2018-12-11 14:40:39','2018-12-11 14:40:57'),
('xiaojuan','123','小娟','15868379873','916218211@qq.com','2018-12-11 14:40:39','2018-12-11 14:40:57');

-- MH
/*==============================================================*/
/* 增加初始化的属性类型                                    */
/*==============================================================*/
INSERT into ot_field_type (`name`) VALUES('单选框');
INSERT into ot_field_type (`name`) VALUES('多选框');
INSERT into ot_field_type (`name`) VALUES('日期');
INSERT into ot_field_type (`name`) VALUES('大文本');
INSERT into ot_field_type (`name`) VALUES('文件上传');
-- 修改文件记录表 增加一列用于记录一个工单的不同对象
alter table ot_attachment add column ticket_fileld_id varchar(11) not null comment '对象编号';
-- 把这一列放到ticket_id后
alter table ot_attachment modify ticket_fileld_id varchar(11) after ticket_id;
-- 之后就可以增加唯一约束
alter TABLE ot_attachment add UNIQUE(ticket_id,ticket_fileld_id);
-- 第二次修改
-- 修改文件记录表 增加一列 记录用户上传文件的时间
alter table ot_attachment add column file_upload_time datetime not null comment '文件上传时间';
-- 把这一列放到file_size后
alter table ot_attachment modify file_upload_time datetime after file_size;

-- ---------