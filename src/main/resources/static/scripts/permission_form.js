var formData={};
var selectId;
function changePid(obj){
	selectId=obj.value;
	if(selectId=='2'){
		$("#funcOrder").val(2);
		$("#funcOrder").attr("disabled","true");
	}else{
		$("#funcOrder").val(3);
		$("#funcOrder").attr("disabled","true");
	}
}

function updatePermission(obj){
	initFrom();
	$("#pid1").attr("disabled","disabled");
	$("#funcOrder").attr("disabled","true");
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
            window.location.href="/openticket/permission";
        },
        error:function(data){
            alert("添加失败！");
        }
    })
}