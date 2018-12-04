/**
 * @Description : form_future.js
 * @author : LVDING
 * @version : 1.0
 * @date : 2018/07/26
 */
var ticketValueList = new Array();

//进行校验并提交表单
function submitOrder(){
    var ticketType = $("h3").text();
    $(".selfDefine").each(function () {
        var fieldId = $(this).attr('id');
        var value = $(this).val();
        ticketValueList.push({
            "fieldId":fieldId,
            "value":value
        })
    })
    $.ajax({
        type:"post",
        url:"/openticket/saveTicket",
        contentType: "application/json",
        //contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        data:JSON.stringify({
            ticketType:ticketType,
            ticketValueList:ticketValueList
        }),
        success:function(data){
            alert("添加成功！");
            window.location.href="/openticket/index";
        },
        error:function(data){
            alert("添加失败！");
        }
    });
}

