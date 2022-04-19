//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}

var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});

$(function () {
	//账号框校验
	$("#phone").on("blur",function () {
		var phone = $("#phone").val()
		if (phone==""){
			showError("phone","账号不能为空")
		}else if (!/^1(?:3\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\d|9\d)\d{8}$/.test(phone)){
			showError("phone","账号格式错误")
		}else {
			showSuccess("phone")
		}
	})

	//密码框校验
	$("#loginPassword").on("blur",function () {
		var loginPassword = $("#loginPassword").val()
		if (loginPassword==""){
			showError("loginPassword","密码不能为空")
		}else if (loginPassword.length<8){
			showError("loginPassword","密码必须由字母、数字组成，区分大小写，大于8位")
		}else {
			showSuccess("loginPassword")
		}
	})
})
//登入按钮被单击
function Login() {
	$("#phone").blur()
	$("#loginPassword").blur()
	var errorText = $("div[id$='Err']").text()
	if (errorText==""){
		var phone = $("#phone").val()
		var loginPassword = $("#loginPassword").val()
		$.ajax({
			url:"/p2p/loan/login",
			type:"get",
			data:{
				phone:phone,
				loginPassword: $.md5(loginPassword)
			},
			success:function (data) {
				if (data.code==1){
					window.location.href="/p2p/index"
				}else {
					showError("loginPassword",data.message)
				}
			}
		})
	}
}
