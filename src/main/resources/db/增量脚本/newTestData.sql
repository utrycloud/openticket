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