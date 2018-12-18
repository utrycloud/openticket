/*
drop table if exists ot_role;
drop table if exists ot_user_role;
drop table if exists ot_permission;
drop table if exists ot_role_type;
drop table if exists ot_role_permission;
*/

/*==============================================================*/
/* Table: ot_role_type                                             */
/*==============================================================*/

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
create table ot_user_role
(
   user_id				int not null comment '用户id',
   role_id				int not null comment '角色id',
   primary key (user_id,role_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*==============================================================*/
/* Table: ot_permission                                              */
/*==============================================================*/
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
create table ot_role_permission
(
   id            		int not null comment '角色权限关系id' AUTO_INCREMENT,
   role_id				int not null comment '角色id',
   permission_id		int not null comment '权限id',
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
