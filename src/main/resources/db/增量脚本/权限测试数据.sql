delete from ot_permission where id=1;
delete from ot_role_permission where permission_id=1;
INSERT INTO `ot_permission` VALUES ('1', '/openticket/**', '最高权限', '0', '1', '拥有所有权限');
INSERT INTO `ot_permission` VALUES ('2', '/*', '工单操作权限', '1', '2', null);
INSERT INTO `ot_permission` VALUES ('3', '/*', '字段管理权限', '1', '2', null);
INSERT INTO `ot_permission` VALUES ('4', '/openticket/addTicket', '新增工单', '2', '3', '包括新增需求，bug等');
INSERT INTO `ot_permission` VALUES ('5', '/openticket/addField', '新增字段', '3', '3', null);
INSERT INTO `ot_role_permission`(role_id,permission_id) VALUES (1,1);
INSERT INTO `ot_role_permission`(role_id,permission_id) VALUES (2,4);