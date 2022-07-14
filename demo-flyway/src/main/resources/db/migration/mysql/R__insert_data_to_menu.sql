-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('自定义应用', '1', '1', 'customMenu', 'audit/customMenu/index', 1, 0, 'C', '0', '0', 'audit:customMenu:list', '#', 'admin', sysdate(), '', null, '自定义应用菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('自定义应用查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'audit:customMenu:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('自定义应用新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'audit:customMenu:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('自定义应用修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'audit:customMenu:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('自定义应用删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'audit:customMenu:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('自定义应用导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'audit:customMenu:export',       '#', 'admin', sysdate(), '', null, '');


insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('人才画像', '1175', '1', 'talentPortrait', 'audit/talentPortrait/index', 1, 0, 'C', '0', '0', 'audit:talentPortrait:list', '#', 'admin', sysdate(), '', null, '人才画像菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('人才画像查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'audit:talentPortrait:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('人才画像新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'audit:talentPortrait:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('人才画像修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'audit:talentPortrait:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('人才画像删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'audit:talentPortrait:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('人才画像导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'audit:talentPortrait:export',       '#', 'admin', sysdate(), '', null, '');
