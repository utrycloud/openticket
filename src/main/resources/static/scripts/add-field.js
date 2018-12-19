/**
 * @Description : add_field.js
 * @author : LVDING
 * @version : 1.0
 * @date : 2018/07/26
 */
var defaultValue='';

//由是否必要单选框的改变而改变的默认值的样式
$(function(){
    $(":radio").click(function(){
        if($("#optionsRadios4").is(":checked")){
            $("#defaultLabel").css("color","red");
            $("#defaultLabel").text("默认值（*）");
        }else{
            $("#defaultLabel").css("color","black");
            $("#defaultLabel").text("默认值");
        }
    });
});
function check(ticketTypeId) {
    console.log(ticketTypeId);
    window.location.href="/openticket/index?ticketTypeId="+ticketTypeId;
}

//显示用于输入非文本框列的选择值
function showTextarea(obj){
	//输入的类型可能有 文本框，下拉，单选，多选，日期，文本，附件上传 其中文本，日期 大文本，附件 不需要选择值
    var select = $(obj).val();
    if("下拉框" == select||"单选框" == select||"多选框" == select){
        $("#selectTextarea").show();
        $("#optionsRadios4").prop("checked",true);
        $("#optionsRadios5").prop("disabled",true);
    	$("#exampleInputEmail3").prop("value","");
    	$("#exampleInputEmail3").prop("disabled",false);
    }else{
        $("#selectTextarea").hide();
        $("#optionsRadios5").removeProp("disabled");
    	$("#exampleInputEmail3").prop("value","");
    	$("#exampleInputEmail3").prop("disabled",false);
    }
    //如果选择的是日期的话 默认值应该为日期选择框
    if("日期" != select){
    	$("#defaultLabel").show();
    	$("#exampleInputEmail3").show();
    	$("#defaultLabel1").hide();
    	$("#exampleInputEmail31").hide();
    }else{
    	$("#defaultLabel1").show();
    	$("#exampleInputEmail31").show();
    	$("#defaultLabel").hide();
    	$("#exampleInputEmail3").hide();
    }
    //如果是文件选择的话 不需要默认值
    if("文件上传" == select){
    	$("#exampleInputEmail3").prop("value","文件上传");
    	$("#exampleInputEmail3").prop("disabled",true);
    }
}

//进行校验并提交表单
function submitOrder(ticketTypeId){
    console.log("ticketTypeId = ", ticketTypeId);
    var name = $("[name=name]").val();
    var fieldName = $("[name=fieldName]").val();
    var selectType = $("#typeSelect").val();
    defaultValue = $("[name=defaultValue]").val();
    var required = $("[name=optionsRadios]:checked").val();
    var ticketType = $("[name=ticketType]").val();
    var selectValues = $("[name=textareaValue]").val();
    var selectValueList = trimSpace($("[name=textareaValue]").val().split("\n"));
    if($("#exampleInputEmail31").val()!=''){
    	defaultValue=$("#exampleInputEmail31").val();
    }
    if("" != name && "" !=fieldName){
        if($("#optionsRadios4").is(":checked")){
            if("" == defaultValue){
                alert("请输入默认值");
                return;
            }
        }
        if ("下拉框" == selectType||"单选框" == selectType||"多选框" == selectType){
           if ("" == selectValues){
               alert("请输入提供选择的值");
               return;
           }else{
               if (selectValueList.indexOf($("[name=defaultValue]").val()) < 0) {
                   alert("默认值错误");
                   return;
               }
           }
        }
        $.ajax({
            type:"post",
            url:"/openticket/saveField",
            contentType: "application/json",
            //contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            data:JSON.stringify({
                name:name,
                fieldName:fieldName,
                selectType:selectType,
                defaultValue:defaultValue,
                required:required,
                ticketType:ticketType,
                selectValueList:selectValueList
            }),
            success:function(data){
                alert("添加成功！");
                var id = $("[id=p-submit-id]").text();
                console.log(id);
                window.location.href="/openticket/index?ticketTypeId="+id;
            },
            error:function(data){
                alert("添加失败！");
            }
        })
    }else{
        alert("请输入需要添加列的名称或字段名");
    }
}

//数组去空子项
function trimSpace(array) {
    for (var i = 0; i < array.length; i++) {
        if (array[i] == "" || typeof (array[i]) == "undefined") {
            array.splice(i, 1);
            i = i - 1;
        }
    }
    return array;
}
