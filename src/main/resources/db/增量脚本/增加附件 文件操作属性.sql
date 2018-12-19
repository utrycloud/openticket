/*==============================================================*/
/* 修改文件记录表 增加一列 记录 文件最近一次下载或者更新的时间                                    */
/*==============================================================*/
alter table ot_attachment add column file_used_time datetime not null comment '文件最近一次下载或者更新的时间';
alter table ot_attachment modify file_used_time datetime after file_upload_time;
