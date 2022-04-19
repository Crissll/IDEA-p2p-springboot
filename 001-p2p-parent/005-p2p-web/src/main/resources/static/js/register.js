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


//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
	//给手机输入框添加失去焦点事件
	$("#phone").on("blur",function () {
		var phone = $.trim($("#phone").val())
		if (phone == ""){
			showError("phone","手机号不能为空")
		}else if (!/^1(?:3\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\d|9\d)\d{8}$/.test(phone)){
			showError("phone","手机号码格式错误")
		}else{
			$.ajax({
				url:"/p2p/loan/checkPhone",
				type:"get",
				data:{
					"phone":phone
				},
				success:function(data) {
					if (data.data.success){
						showSuccess("phone")
					}else{
						showError("phone",data.data.message)
					}
				}
			})
			showSuccess("phone")
		}
	})

	//密码框进行非空和格式校验
	$("#loginPassword").on("blur",function () {
		var loginPassword=$.trim($("#loginPassword").val())
		if (loginPassword==""){
			showError("loginPassword","密码不能为空")
		}else if (loginPassword.length<8||loginPassword>20){
			showError("loginPassword","密码长度为8-20位")
		}else if (!/^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,18}$/.test(loginPassword)){
			showError("loginPassword","密码必须由字母、数字组成，区分大小写")
		}else {
			showSuccess("loginPassword")
		}
	})

	//注册按钮单击事件
	$("#btnRegist").on("click",function () {
		//触发手机号框和密码框事件
		$("#phone").blur()
		$("#loginPassword").blur()

		var errorText = $("div[id$='Err']").text()


		if (errorText==""){
			var loginPassword=$.trim($("#loginPassword").val())
			var phone=$.trim($("#phone").val())
			var messageCode =$.trim($("#messageCode").val())
			$("#loginPassword").val($.md5(loginPassword))

			$.ajax({
				url:"/p2p/loan/register",
				type:"post",
				data:{
					"phone":phone,
					"loginPassword":$.md5(loginPassword),
					"messageCode":messageCode
				},
				success:function (data) {
					if (data.success){
						//注册成功跳转
						window.location.href="/p2p/loan/page/realName"
					}else{
						if (data.state==-1){
							showError("messageCode",data.message)
						}else {
							showError("loginPassword","注册失败")
						}

					}
				},
				error:function () {
					//注册失败
					showError("loginPassword","系统繁忙,请稍后再试！")
				}
			})
		}
	})

	//验证码非空校验
	$("#messageCode").on("blur",function () {
		var messageCode=$.trim($("#messageCode").val())
		if (messageCode!=""){
			showSuccess("messageCode")
		}
	})

	//验证码倒计时
	$("#messageCodeBtn").on("click",function () {


		//触发手机号框和密码框事件
		$("#phone").blur()
		$("#loginPassword").blur()
		var errorText = $("div[id$='Err']").text()

		if (errorText==""){

			var phone = $.trim($("#phone").val())
			//请求后台发送验证码
			$.ajax({
				url:"/p2p/user/messageCode",
				type:"get",
				data:{
					"phone":phone
				},
				success:function (data) {
					//验证码是否发送成功,发送成功进行倒计时
					if (data.code==1){
						alert(data.phoneCode)
						if (!$("#messageCodeBtn").hasClass("on")){
							$.leftTime(60,function(d){
								//d.status,值true||false,倒计时是否结束;
								//d.s,倒计时秒;
								if (d.status){
									$("#messageCodeBtn").addClass("on")
									$("#messageCodeBtn").text(d.s==0? "60秒后获取":d.s+"秒后获取")
								}else {
									$("#messageCodeBtn").removeClass("on")
									$("#messageCodeBtn").text("获取验证码")
								}
							});
						}
					}else{
						//验证码发送失败
						showError("messageCode",data.message)
					}
				}
			})


		}
	})
});
