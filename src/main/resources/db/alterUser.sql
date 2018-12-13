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