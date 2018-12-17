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
