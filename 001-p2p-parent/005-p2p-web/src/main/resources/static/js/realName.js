
//同意实名认证协议
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
});
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

$(function () {
	//手机框校验
	$("#phone").on("blur",function () {
		var phone = $.trim($("#phone").val())
		if (phone == ""){
			showError("phone","手机号码不能为空")
		}else if (phone.length!=11){
			showError("phone","手机格式错误")
		}else {
			showSuccess("phone")
		}
	})

	//真实姓名校验
	$("#realName").on("blur",function () {
		var realName = $.trim($("#realName").val())
		if (realName == ""){
			showError("realName","姓名不能为空")
		}else if (!/^([\u4e00-\u9fa5]{1,20}|[a-zA-Z\.\s]{1,20})$/.test(realName)){
			showError("realName","姓名格式错误")
		}else {
			showSuccess("realName")
		}
	})

	//身份证校验
	$("#idCard").on("blur",function () {
		var idCard = $.trim($("#idCard").val())
		if (idCard == ""){
			showError("idCard","身份证号不能为空")
		}else if (!/^([1-6][1-9]|50)\d{4}(18|19|20)\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(idCard)){
			showError("idCard","身份证格式错误")
		}else {
			showSuccess("idCard")
		}
	})

	//验证码失去焦点
	// $("#messageCode").on("blur",function () {
	// 	var messageCode = $.trim($("#messageCode").val())
	// 	if (messageCode == ""){
	// 		showError("messageCode","验证码不能为空")
	// 	} else if(messageCode.length != 6){
	// 		showError("messageCode","验证码格式错误")
	// 	} else {
	// 		showSuccess("messageCode")
	// 	}
	// })

	//获取验证码
	$("#messageCodeBtn").on("click",function () {
		$("#phone").blur()
		$("#realName").blur()
		$("#idCard").blur()
		var errorText = $("div[id$='Err']").text()
		if (errorText == ""){
			//判断是否正在倒计时中
			if (!$("#messageCodeBtn").hasClass("on")){
				var phone = $.trim($("#phone").val())
				$.ajax({
					url:"/p2p/user/messageCode",
					type:"get",
					data:{
						phone:phone
					},
					success:function (data) {
						if (data.code==1){
							alert(data.phoneCode)
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
						}else {
							showError("phone",data.message)
						}
					},
					error:function () {
						showError("messageCode","验证码发送失败")
					}

				})
			}
		}

	})
	//认证被点击
	$("#btnRegist").on("click",function () {
		$("#phone").blur()
		$("#realName").blur()
		$("#idCard").blur()

		//验证码校验
			var messageCode = $.trim($("#messageCode").val())
			if (messageCode == ""){
				showError("messageCode","验证码不能为空")
			} else if(messageCode.length != 6){
				showError("messageCode","验证码格式错误")
			} else {
				showSuccess("messageCode")
			}

		var errorText = $("div[id$='Err']").text()
		if (errorText == ""){

			var phone = $("#phone").val()
			var realName = $("#realName").val()
			var idCard = $("#idCard").val()

			$.ajax({
				url: "/p2p/loan/realName",
				type:"get",
				data:{
					phone: phone,
					realName: realName,
					idCard: idCard,
					messageCode:messageCode
				},
				success:function (data) {
					if (data.code == 1){
						//实名认证成功暂时跳转到首页
						window.location.href="/p2p/index"
					}else {
						showError("messageCode",data.message)
					}
				},
				error:function () {
					showError("messageCode","认证失败")
				}
			})
		}
	})
})

