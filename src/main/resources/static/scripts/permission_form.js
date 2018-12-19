var formData={};

function updatePermission(obj){
	initFrom();
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
        	$('#pid').val(formData.pid);
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
            window.location.href="/openticket/permission";
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
	var pid = $('#pid').val();
	var funcOrder = $('#funcOrder').val();
	var description = $('#description').val();
	
	$.ajax({
        type:"post",
        url:"/openticket/savePermission",
        contentType: "application/json",
        data:JSON.stringify({
        	id:id,
            name:name,
            uri:uri,
            pid:pid,
            funcOrder:funcOrder,
            description:description,

        }),
        success:function(data){
            alert(data.msg);
            window.location.href="/openticket/permission";
        },
        error:function(data){
            alert("添加失败！");
        }
    })
}