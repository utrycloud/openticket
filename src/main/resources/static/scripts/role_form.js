//加载表格
var DatatableRemoteAjaxDemo = {
    init: function () {
        //表格头
        var data = [{
            field: 'id',
            title: '编号'
        }, {
            field: 'name',
            title: '角色名称'
        }, {
            field: 'roleTypeName',
            title: '角色类型'
        },{
            field: 'description',
            title: '描述'
        },{
            "field":"Actions",
            "width":110,
            "title":"操作",
            "template":function() {
                return '<a href="#setPermissionModal" onclick="permissionManageModal(this)" data-toggle="modal" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="查看权限"><i class="la la-pencil" ></i></a>'+
                    '<a href="#updateRoleModal" onclick="roleInfo2Modal(this)" data-toggle="modal" class="m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit" onclick="javascript:;"></i></a>'+
                    '<a href="#" onclick="deleteRole(this)" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash" ></i></a>';
            }
        }
        ];
        var t;
        t = $("#roleTable").mDatatable({
            data: {
                type: "remote",
                source: {
                    read: {
                        url: "/openticket/role/list2",
                        map: function (t) {
                            var e = t;
                            return void 0 !== t.data && (e = t.data),
                                e
                        }
                    }
                },
                pageSize: 10,
                serverPaging: !0,
                serverFiltering: !0,
                serverSorting: !0
            },
            layout: {
                scroll: !1,
                footer: !1
            },
            sortable: !0,
            pagination: !0,
            toolbar: {
                items: {
                    pagination: {
                        pageSizeSelect: [10, 20, 30, 50, 100]
                    }
                }
            },
            search: {
                input: $("#generalSearch")
            },
            columns: data
        });
            $("#m_form_status").on("change",
                function () {
                    t.search($(this).val(), "Status")
                });
            $("#m_form_type").on("change",
                function () {
                    t.search($(this).val(), "Type")
                });
            $("#m_form_status, #m_form_type").selectpicker()

    }
};
jQuery(document).ready(function () {
    DatatableRemoteAjaxDemo.init();
});


//添加角色
function addRole() {
    //检验表单
    if($("#roleName").val()===""){
        alert("名称不能为空");
        return;
    }

    var data=$("#addRoleForm").serialize();
    data = decodeURIComponent(data,true);
    //data = encodeURI(encodeURI(data));
    $.ajax({
        type:"post",
        dataType:"json",
        url:"/openticket/role/save",
        data:data,
        success:function(data) {
            if (data.code == 200) {
                alert("添加成功！");
                window.location.href = "/openticket/role";
            } else {
                alert("添加失败！" + data.msg);
            }
        },
        error:function(data){
            alert("添加失败！");
        }
    });
}

//删除用户
function deleteRole(obj) {
    var tr = $(obj).parents("tr");
    var id = tr.find("td").eq(0).text();
    $.ajax({
        type:"post",
        dataType:"json",
        url:"/openticket/role/delete",
        data:{id:id},
        success:function(data) {
            if (data.code == 200) {
                alert("删除成功！");
                tr.remove();
            } else {
                alert("删除失败！" + data.msg);
            }
        },
        error:function(data){
            alert("删除失败！");
        }
    });
}

//编辑用户弹窗数据填充
function roleInfo2Modal(obj) {
    //先清空
    $("#u_id").val("");
    $("#u_name").val("");
    $("#u_description").val("");
    //获取要编辑的user信息
    var tr = $(obj).parents("tr");
    var id = tr.find("td").eq(0).text();
    var name=tr.find("td").eq(1).text();
    var desc=tr.find("td").eq(3).text();
    $("#u_id").val(id);
    $("#u_name").val(name);
    $("#u_description").val(desc);
}

//更新user信息
function updateRole() {
    //校验
    if($("#u_name").val()==""){
        alert("请输入角色名称")
        return ;
    }
    var data=$("#updateRoleForm").serialize();
    data = decodeURIComponent(data,true);
    //data = encodeURI(encodeURI(data));
    $.ajax({
        type:"post",
        dataType:"json",
        url:"/openticket/role/update",
        data:data,
        success:function(data) {
            if (data.code == 200) {
                alert("修改成功！");
                window.location.href = "/openticket/role";
            } else {
                alert("修改失败！" + data.msg);
            }
        },
        error:function(data){
            alert("修改失败！");
        }
    });
}

//显示角色拥有权限
function permissionManageModal(obj) {
    var tr = $(obj).parents("tr");
    var id = tr.find("td").eq(0).text();
    var name=tr.find("td").eq(1).text();
    //清空
    $("#permissionTree").html("");

    $("#s_role_name").html(name);
    $("#s_id").val(id);
/*    //获取角色拥有的权限并显示
    $.ajax({
        type: "post",
        dataType: "json",
        data:{roleId:id},
        url: "/openticket/permission/getByRoleId",
        success: function (data) {
            if (data.code == 200) {
                var list=data.data;
                for(var r in list){
                    $("#permissionDiv").append("<span>·"+list[r].name+"&nbsp;&nbsp;&nbsp;"+list[r].uri+"</span>");
                }
            }
        },
        error: function (data) {
            alert("服务器出错！");
        }
    });*/

    //加载权限树
    $.ajax({
        type: "Get",
        url: "/openticket/permission/getTree",
        dataType: "json",
        success: function (result) {
            //先清除
            $('#permissionTree').jstree("destroy");
            //
            $("#permissionTree").jstree({
                // 引入插件
                'plugins': ['checkbox', 'types', 'themes'],
                'types': {
                    'default': {
                        'icon': false  // 删除默认图标
                    },
                },
                'checkbox': {  // 去除checkbox插件的默认效果
                    'tie_selection': false,
                    'keep_selected_style': false,
                    'whole_node': false
                },
                'core': {
                    'multiple': true,  // 可否多选
                    'data': result.data,
                    'dblclick_toggle': true   //允许tree的双击展开
                }
            }).on("loaded.jstree",function(event,data){
                //当前角色拥有的权限
                $.ajax({
                    type: "Get",
                    url: "/openticket/permission/getByRoleId",
                    dataType: "json",
                    data:{roleId:id},
                    success:function (result) {
                        var list=result.data;
                        for(var p in list){
                            $("#permissionTree").jstree('check_node',list[p].id);
                        }
                    }
                });
            });
        },
        error: function () {
            alert("加载失败！")
        }
    });
}

//更新角色权限
function updateRolePermission() {
    var id=$("#s_id").val();
    var list=$('#permissionTree').jstree("get_checked");
    var params=[];
    for (var p in list){
        params.push(list[p]);
    }
    $.ajax({
        type:"post",
        dataType:"json",
        data:{roleId:id,ids:params.toString()},
        url:"/openticket/role/setRolePermission",
        success:function (result) {
            if(result.code==200){
                alert("保存成功！");
            }else{
                alert("保存失败！"+result.msg);
            }
        },
        error:function () {
            alert("保存失败");
        }
    });
}
