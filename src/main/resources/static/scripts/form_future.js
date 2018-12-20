/**
 * @Description : form_future.js
 * @author : LVDING
 * @version : 1.0
 * @date : 2018/07/26
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
			alert(returndata);
		}
	});
};
//进行校验并提交表单
function submitOrder() {
	var ticketType = $("h3").text();
	//把所有的内容加入ticketValueList中
	$(".selfDefine").each(function() {
		//如果是单选框或者多选框的话
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
	//alert(JSON.stringify(inserts));
	$.ajax({
		type : "post",
		url : "/openticket/saveTicket",
		contentType : "application/json",
		//contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		data : JSON.stringify({
			ticketType : ticketType,
			ticketValueList : ticketValueList,
			inserts:inserts,
			giveUpFile:giveUp,
		}),
		success : function(data) {
			var ticketTypeId = $("p#id-p-ticketTypteId").text();
			var login = $("p#id-p-session-login").text();
			console.log("login", login);
			if(login == null || login == "") {
                alert("提交成功");
                window.location.reload();
                return;
			} else {
                window.location.href = "/openticket/index?ticketTypeId="+ticketTypeId;
			}

		},
		error : function(data) {
			alert("添加失败！");
		}
	});
}