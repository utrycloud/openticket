//加载表格
var DatatableRemoteAjaxDemo = {
    init: function () {
        //表格头
        var data = [{
            field: 'id',
            title: '编号'
        }, {
            field: 'username',
            title: '用户名'
        }, {
            field: 'realName',
            title: '真实姓名'
        }, {
            field: 'tel',
            title: '手机号'
        }, {
            field: 'email',
            title: '邮箱'
        }, {
            field: 'role',
            title: '角色'
        }, {
                field: 'createTime',
                title: '创建时间'
        },{
            "field":"Actions",
            "width":110,
            "title":"操作",
            "template":function() {
                return '<a href="#setRoleModal" onclick="roleManageModal(this)" data-toggle="modal" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="分配角色"><i class="la la-pencil" ></i></a>'+
                    '<a href="#updateUserModal" onclick="userInfo2Modal(this)" data-toggle="modal" class="m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit" onclick="javascript:;"></i></a>'+
                    '<a href="#" onclick="deleteUser(this)" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash" ></i></a>';
            }
        }
        ];
        var t;
        t = $("#userTable").mDatatable({
            data: {
                type: "remote",
                source: {
                    read: {
                        url: "/openticket/user/list",
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

//校验表单
function checkForm() {
    if($("#username").val()===""){
        alert("用户名不能为空");
        return false;
    }
    if($("#password").val()===""){
        alert("密码不能为空");
        return false;
    }
    return true;
}

//添加用户
function addUser() {
    //校验不通过
    if(checkForm()==false){
        return;
    }
    var data=$("#addUserForm").serialize();
    data = decodeURIComponent(data,true);
    //data = encodeURI(encodeURI(data));
    $.ajax({
        type:"post",
        dataType:"json",
        url:"/openticket/user/save",
        data:data,
        success:function(data) {
            if (data.code == 200) {
                alert("添加成功！");
                window.location.href = "/openticket/user";
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
function deleteUser(obj) {
    var tr = $(obj).parents("tr");
    var id = tr.find("td").eq(0).text();
    $.ajax({
        type:"post",
        dataType:"json",
        url:"/openticket/user/delete",
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
function userInfo2Modal(obj) {
    //先清空
    $("#u_id").val("");
    $("#u_username").val("");
    $("#u_password").val("");
    $("#u_realName").val("");
    $("#u_tel").val("");
    $("#u_email").val("");
    //获取要编辑的user信息
    var tr = $(obj).parents("tr");
    var id = tr.find("td").eq(0).text();
    $.ajax({
        type: "post",
        dataType: "json",
        url: "/openticket/user/" + id,
        success: function (data) {
            if (data.code == 200) {
                var u=data.data;
                $("#u_id").val(u.id);
                $("#u_username").val(u.username);
                $("#u_password").val(u.password);
                $("#u_realName").val(u.realName);
                $("#u_tel").val(u.tel);
                $("#u_email").val(u.email);
            }
        },
        error: function () {
            alert("操作失败！");
        }
    });
}

//更新user信息
function updateUser() {
    //校验
    if($("#u_password").val()==""){
        alert("请输入密码")
        return ;
    }
    var data=$("#updateUserForm").serialize();
    data = decodeURIComponent(data,true);
    //data = encodeURI(encodeURI(data));
    $.ajax({
        type:"post",
        dataType:"json",
        url:"/openticket/user/update",
        data:data,
        success:function(data) {
            if (data.code == 200) {
                alert("修改成功！");
                window.location.href = "/openticket/user";
            } else {
                alert("修改失败！" + data.msg);
            }
        },
        error:function(data){
            alert("修改失败！");
        }
    });
}

//填充用户角色模态框信息
function roleManageModal(obj) {
    var tr = $(obj).parents("tr");
    var id = tr.find("td").eq(0).text();
    $("#s_id").val(id);
    //清空
    $("#RoleListDiv").html("");
    //获取所有角色信息并显示
    $.ajax({
        type: "post",
        dataType: "json",
        url: "/openticket/role/list",
        success: function (data) {
            if (data.code == 200) {
                var list=data.data;
                for(var r in list){
                    $("#RoleListDiv").append("<input name=\'roles\' type=\'checkbox\' value=\'"+list[r].id+"\'>"+list[r].name);
                }
                //用户角色信息显示
                getUserRole(id);
            }
        },
        error: function (data) {
            alert("服务器出错！");
        }
    });

}


function getUserRole(id) {
    //当前用户拥有角色
    $.ajax({
        type: "post",
        dataType: "json",
        data:{id:id},
        url: "/openticket/user/getRole",
        success: function (data) {
            if (data.code == 200) {
                var list=data.data;
                for(var r in list){
                    $("[name='roles'][value='"+list[r].id+"'").attr('checked',true);
                }
            }
        },
        error: function (data) {
            alert("服务器出错！");
        }
    });
}

//设置角色
function setRole() {
    var roles=[];
    var checks=$("input[name='roles']");
    checks.each(function () {
        if($(this).is(":checked")){
            roles.push($(this).val());
        }
    });

    var userId=$("#s_id").val();
    var params={userId:userId,roles:roles.toString()};
    $.ajax({
        type: "post",
        dataType: "json",
        data:params,
        url: "/openticket/user/setRole",
        success: function (data) {
            if (data.code == 200) {
                alert("保存成功！");
                window.location.href = "/openticket/user";
            }else{
                alert("保存失败！"+data.msg);
            }
        },
        error: function (data) {
            alert("服务器出错！");
        }
    });
}