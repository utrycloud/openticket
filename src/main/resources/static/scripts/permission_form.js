var formData={};
var selectId=$("#currId").val();

var maxOrder=5;//权限最高层级

//获得所有权限
var allList;


//改变父权限时联动权限层级
function changePid(id){
	/*if(selectId=='2'){
		$("#funcOrder").val(2);
		$("#funcOrder").attr("disabled","true");
	}else{
		$("#funcOrder").val(3);
		$("#funcOrder").attr("disabled","true");
	}*/
/*	$.ajax({
		type:"post",
		dataType:"json",
		url:"/openticket/getPermissionById",
		data:{id:selectId},
		success:function (result) {
            $("#funcOrder").val(result.data.funcOrder+1);
        }
	});*/
	for(var i in allList){
		if(id==allList[i].id){
            $("#funcOrder").val(allList[i].funcOrder+1);
		}
	}

}

//加载父权限下拉框
function loadPid1() {
	//获得所有权限
    $.ajax({
        type:"post",
        dataType:"json",
        url:"/openticket/permission/list",
        success:function (result) {
            allList=result;
            for(var i=1;i<=5;i++){
                $("#order"+i).html("");
                $("#order"+i).attr("hidden",true);
            }
            for(var i in allList){
                var order=allList[i].funcOrder;
                var id=allList[i].id;
                var name=allList[i].name;
                var $group=$("#order"+order);
                $group.attr("hidden",false);
                $group.append("<option value=\'"+id+"\'>"+name+"</option>")
            }
            changePid(selectId);
            $("option[value="+selectId+"]").attr("selected",true);
        }
    });

}

//编辑更新时补充弹出框信息
function updatePermission(obj){
	initFrom();
	//$("#pid1").attr("disabled","disabled");
	//$("#funcOrder").attr("disabled","true");
	//alert($('#description').attr("value"));
	var id = $(obj).parents("tr").find("td").eq(0).text();
	$.ajax({
        type:"post",
        url:"/openticket/getPermissionById",
        contentType: "application/json",
        data:id,
        success:function(data){
        	formData=data.data;
        	$('#id').val(formData.id);
        	$('#name').val(formData.name);
        	$('#uri').val(formData.uri);
        	selectId=formData.pid;
        	$("option[value="+selectId+"]").attr("selected",true);
        	$('#funcOrder').val(formData.funcOrder);
        	$('#description').val(formData.description);
        	//alert("formData.description"+formData.description);
        },
        error:function(data){
            alert("添加失败！");
            retrun;
        }
    })
}

function deletePermission(obj){
	var id = $(obj).parents("tr").find("td").eq(0).text();
	$.ajax({
        type:"post",
        url:"/openticket/delPermissionById",
        contentType: "application/json",
        data:id,
        success:function(data){
        	alert(data.msg);
            window.location.href="/openticket/permission?id="+selectId;
        },
        error:function(data){
            alert("添加失败！");
            return ;
        }
    })
}

function initFrom(){
	$('#id').val("");
	$('#name').val("");
	$('#uri').val("");
	$('#pid').val("");
	$('#funcOrder').val("");
	$('#description').val("");
	//加载父权限
    loadPid1();
}

function initFrom2(){
	initFrom();
	$("#pid1").attr("disabled",false);
	$("#funcOrder").attr("disabled","true");
	$("option").each(function (){
		$(this).attr('selected',false);
	})
	selectId=2;
	$("#funcOrder").val(2);
}

function addPermission(){
	var name = $('#name').val();
	if(name==''){
		alert("权限名字不能为空");
		return ;
	}
	var uri = $('#uri').val();
	if(uri==''){
		alert("uri不能为空");
		return ;
	}
	var id=$('#id').attr("value");
	var funcOrder = $('#funcOrder').val();
	var description = $('#description').val();
	if(funcOrder>maxOrder){
		alert("最高层级为"+maxOrder);
	}
	$.ajax({
        type:"post",
        url:"/openticket/savePermission",
        contentType: "application/json",
        data:JSON.stringify({
        	id:id,
            name:name,
            uri:uri,
            pid:selectId,
            funcOrder:funcOrder,
            description:description,

        }),
        success:function(data){
            alert(data.msg);
            window.location.href="/openticket/permission?id="+selectId;
        },
        error:function(data){
            alert("保存失败！");
        }
    })
}