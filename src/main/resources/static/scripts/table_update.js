/**
 * @Description : table_update.js
 * @author : LVDING
 * @version : 1.0
 * @date : 2018/08/01
 */
var ticketValueList = new Array();

//用于存放真正提交的
var inserts={};
//用于存放被放弃提交的文件信息
var giveUp=[];
function UpFile3(form) {
	var formData = new FormData(form);
	$.ajax({
		//接口地址
		url : '/openticket/fileUpload',
		type : 'POST',
		data : formData,
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			//alert(JSON.stringify(inserts));
			var ticketFileldId=data.ticketFileldId;
			//console.log(inserts[ticketFileldId]);
			//console.log(giveUp);
			if(inserts[ticketFileldId]!=null){
				var insert={};
				var path="path";
				insert=inserts[ticketFileldId];
				console.log(insert);
				giveUp.push(insert[path]);
			}
			inserts[ticketFileldId]=data;
			console.log(giveUp);
			//alert(JSON.stringify(inserts));
		},
		error : function(returndata) {
			//alert(returndata);
		}
	});
};
//进行校验并提交表单
function submitOrder() {
	//alert(1);
	var ticketType = $("h3").text();
	var ticketId = $("input[type=hidden]").val();
	$(".selfDefine").each(function() {
		//如果是单选框或者多选框的话 就只选择check的提交
		if ($(this).attr('type') == 'radio' || $(this).attr('type') == 'checkbox') {
			if ($(this).attr('checked') == 'checked') {
				var fieldId = $(this).attr('id');
				var value = $(this).val();
				ticketValueList.push({
					"fieldId" : fieldId,
					"value" : value
				})
			}
		} else {
			var fieldId = $(this).attr('id');
			var value = $(this).val();
			ticketValueList.push({
				"fieldId" : fieldId,
				"value" : value
			})
		}

	})
	$.ajax({
		type : "post",
		url : "/openticket/updateTicket",
		contentType : "application/json",
		//contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		data : JSON.stringify({
			id : ticketId,
			ticketType : ticketType,
			ticketValueList : ticketValueList,
			inserts:inserts,
			giveUpFile:giveUp,
		}),
		success : function(data) {
			alert("修改成功！");
			var ticketId = $("#id-p-ticketTypteId").text();
			window.location.href = "/openticket/index?ticketTypeId="+ticketId;
		},
		error : function(data) {
			alert("修改失败！");
		}
	});
}