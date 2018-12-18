/*==============================================================*/
/* Table: ot_select_value                                       */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_field_type_value`;
create table ot_field_type_value
(
   id                   int not null comment '选择值编号' AUTO_INCREMENT,
   field_id            int  not null comment '对象编号',
   value                varchar(255)  not null comment '值',
   primary key (id)
);

/*==============================================================*/
/* Table: ot_ticket                                             */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_ticket`;
create table ot_ticket
(
   id            			int not null comment '工单编号' AUTO_INCREMENT,
   create_time          	datetime  not null comment '创建时间',
   create_user_id			int not null comment '创建用户编号',
   ticket_type_id   		int not null comment '工单类型编号',
   primary key (id)
);

/*==============================================================*/
/* Table: ot_user                                               */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_user`;
create table ot_user
(
   id            			int not null comment '用户编号' AUTO_INCREMENT,
   name						varchar(255) not null comment '姓名',
   primary key (id)
);

/*==============================================================*/
/* Table: ot_ticket_field                                       */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_ticket_field`;
create table ot_ticket_field
(
   id                   int not null comment '对象编号' AUTO_INCREMENT,
   ticket_type_id		    int not null comment '工单类型编号',
   field_name           varchar (255) not null comment '对象字段名',
   name                 varchar(255) not null comment '对象名称',
   required             tinyint not null comment '是否必填' default '0', /* 默认为否 */
   default_value        varchar(255) comment '默认值',
   select_id          	int not null comment '选择类型',
   CONSTRAINT ticketTypeId_name UNIQUE (ticket_type_id,name),
   CONSTRAINT ticketTypeId_fieldName UNIQUE (ticket_type_id,field_name),
   primary key (id)
);

/*==============================================================*/
/* Table: ot_ticket_type                                        */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_ticket_type`;
create table ot_ticket_type
(
   id                   int not null comment '工单类型编号' AUTO_INCREMENT,
   name                 varchar(255) not null unique comment '工单类型名称',
   primary key (id)
);

/*==============================================================*/
/* Table: ot_field_type                                         */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_field_type`;
create table ot_field_type
(
   id                   int not null comment '选择类型编号' AUTO_INCREMENT,
   name                 varchar(255) not null comment '选择类型名称',
   primary key (id)
);

/*==============================================================*/
/* Table: ot_ticket_value                                       */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_ticket_value`;
create table ot_ticket_value
(
   id                   int not null comment '数据编号' AUTO_INCREMENT,
   field_id            int not null comment '对象编号',
   ticket_id            int not null comment '工单编号',
   value                varchar(255) comment '值',
   primary key (id)
);

/*==============================================================*/
/* Table: to_attachment                                         */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_attachment`;
create table ot_attachment
(
   id       int not null comment '附件编号' AUTO_INCREMENT,
   ticket_id            int not null comment '工单编号',
   file_name            varchar(255) not null comment '文件名称',
   file_size            int not null comment '文件大小',
   content_type         varchar(255) not null comment '文件类型',
   path                 varchar(255) not null comment '保存路径',
   primary key (id)
);

DROP TABLE IF EXISTS `ot_role_type`;
create table ot_role_type
(
   id            		int not null comment '角色类型编号' AUTO_INCREMENT,
   name					varchar(50) not null comment '角色类型名称',
   description			varchar(255) comment '描述',
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*==============================================================*/
/* Table: ot_role                                               */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_role`;
create table ot_role
(
   id            		int not null comment '角色编号' AUTO_INCREMENT,
   name					varchar(50) not null comment '角色名称',
   role_type_id			int not null comment '角色类型id',
   description			varchar(255) comment '角色描述',
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*==============================================================*/
/* Table: ot_user_role                                             */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_user_role`;
create table ot_user_role
(
   user_id				int not null comment '用户id',
   role_id				int not null comment '角色id',
   primary key (user_id,role_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*==============================================================*/
/* Table: ot_permission                                              */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_permission`;
create table ot_permission
(
   id            		int not null comment '权限id' AUTO_INCREMENT,
   uri			      varchar(50) not null comment '权限请求uri',
   name					varchar(50) not null comment '权限名称',
   pid					int comment '父权限id' default '0',
   func_order			tinyint comment '权限层级' default '1',
   description			varchar(255) comment '权限描述',
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*==============================================================*/
/* Table: ot_role_function                                           */
/*==============================================================*/
DROP TABLE IF EXISTS `ot_role_permission`;
create table ot_role_permission
(
   id            		int not null comment '角色权限关系id' AUTO_INCREMENT,
   role_id				int not null comment '角色id',
   permission_id		int not null comment '权限id',
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


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

insert ot_user(id,username,password,realName) values(100,'admin','123456','管理员甲');
insert ot_user(id,username,password,realName) values(200,'enoch','123456','开发乙');



insert ot_role_type(id,name) values(1,'超级管理员');
insert ot_role_type(id,name) values(2,'团队负责人');
insert ot_role_type(id,name) values(3,'开发');



insert ot_role(id,name,role_type_id) values(1,'管理员',1);
insert ot_role(id,name,role_type_id) values(2,'产品经理',2);
insert ot_role(id,name,role_type_id) values(3,'前端工程师',3);



insert ot_user_role(user_id,role_id) values(100,1);
insert ot_user_role(user_id,role_id) values(200,2);
insert ot_user_role(user_id,role_id) values(200,3);



insert ot_permission(id,uri,name) values(1,'/openticket/addField','添加自定义列');



insert ot_role_permission(role_id,permission_id) values(1,1);


TRUNCATE table ot_field_type;
TRUNCATE table ot_ticket_type;
TRUNCATE table ot_field_type_value;
TRUNCATE table ot_user;
TRUNCATE table ot_ticket_field;

insert into ot_field_type(name) values('文本框');
insert into ot_field_type(name) values('下拉框');
-- insert into ot_field_type(name) values('复选框');
-- insert into ot_field_type(name) values('单选框');

insert into ot_ticket_type(name) values('需求');
insert into ot_ticket_type(name) values('问题');
insert into ot_ticket_type(name) values('bug');

insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'branch','所属分支',1,'云客服',(select id from ot_field_type where name='下拉框'));
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='所属分支'),'自运营');
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='所属分支'),'云客服');

insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'module','产品模块',1,'电话',(select id from ot_field_type where name='下拉框'));
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='产品模块'),'报表');
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='产品模块'),'电话');

insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'weight','权重',1,'1',(select id from ot_field_type where name='下拉框'));
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='权重'),'1');
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='权重'),'2');
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='权重'),'3');
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='权重'),'4');
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='权重'),'5');

insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'customer','客户',0,null,(select id from ot_field_type where name='文本框'));
insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'title','标题',0,null,(select id from ot_field_type where name='文本框'));
insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'zentaoId','禅道编号',0,null,(select id from ot_field_type where name='文本框'));
insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'planOnlineTime','计划上线时间',0,null,(select id from ot_field_type where name='文本框'));
insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'actualOnlineTime','实际上线时间',0,null,(select id from ot_field_type where name='文本框'));
insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='需求'),'status','状态',0,null,(select id from ot_field_type where name='下拉框'));
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='状态'),'开发中');
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='状态'),'计划中');
insert into ot_field_type_value(field_id,value) values((select id from ot_ticket_field where name='状态'),'已完成');
-- insert into ot_ticket(create_time,create_user_id,ticket_type_id) values(SYSDATE(),(select id from ot_user where name='张三'),(select id from ot_ticket_type where name='需求'));
insert into ot_ticket_value(field_id,ticket_id,value) values(1,1,'自运营');
insert into ot_ticket_value(field_id,ticket_id,value) values(2,1,'报表');
insert into ot_ticket_value(field_id,ticket_id,value) values(3,1,'1');
insert into ot_ticket_value(field_id,ticket_id,value) values(4,1,'衣邦人');
insert into ot_ticket_value(field_id,ticket_id,value) values(5,1,'双11数据恢复');
insert into ot_ticket_value(field_id,ticket_id,value) values(6,1,'746');
insert into ot_ticket_value(field_id,ticket_id,value) values(7,1,'2017-01-01');
insert into ot_ticket_value(field_id,ticket_id,value) values(8,1,'2017-03-03');
insert into ot_ticket_value(field_id,ticket_id,value) values(9,1,'开发中');





-- insert into ot_ticket(create_time,create_user_id,ticket_type_id) values(SYSDATE(),2,2);
-- insert into ot_ticket(create_time,create_user_id,ticket_type_id) values(SYSDATE(),3,3);
-- insert into ot_ticket(create_time,create_user_id,ticket_type_id) values(SYSDATE(),1,2);
-- insert into ot_ticket(create_time,create_user_id,ticket_type_id) values(SYSDATE(),2,3);

-- insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='bug'),'testComboBox','测试复选框',1,'1',(select id from ot_field_type where name='复选框'));
-- insert into ot_field_type_value(fields_id,value) values((select id from ot_ticket_field where name='测试复选框'),'1');
-- insert into ot_field_type_value(fields_id,value) values((select id from ot_ticket_field where name='测试复选框'),'2');
-- insert into ot_field_type_value(fields_id,value) values((select id from ot_ticket_field where name='测试复选框'),'3');
--
-- insert into ot_ticket_field(ticket_type_id,field_name,name,required,default_value,select_id) values((select id from ot_ticket_type where name='bug'),'testRadios','测试单选框',1,'1',(select id from ot_field_type where name='单选框'));
-- insert into ot_field_type_value(fields_id,value) values((select id from ot_ticket_field where name='测试单选框'),'1');
-- insert into ot_field_type_value(fields_id,value) values((select id from ot_ticket_field where name='测试单选框'),'2');
-- insert into ot_field_type_value(fields_id,value) values((select id from ot_ticket_field where name='测试单选框'),'3');
