$package('YiYa.login');
YiYa.login = function(){
	return {
		toLogin:function(){
			try{
				var form = $("#loginForm");
				if(form.form('validate')){
					YiYa.progress('Please waiting','Loading...');
					YiYa.submitForm(form,function(data){
						YiYa.closeProgress();
						YiYa.login.loadVrifyCode();//刷新验证码
						if(data.success){
					 		window.location= "login.shtml";
				        }else{
				       	   YiYa.alert('提示',data.msg,'error');  
				        }
					});
				}
			}catch(e){
				
			}
			return false;
		},
		loadVrifyCode:function(){//刷新验证码
			var _url = "ImageServlet?time="+new Date().getTime();
			$(".vc-pic").attr('src',_url);
		},
		init:function(){
			if(window.top != window.self){
				window.top.location =  window.self.location;
			}
			//验证码图片绑定点击事件
			$(".vc-pic").click(YiYa.login.loadVrifyCode);
			var form = $("#loginForm");
			form.submit(YiYa.login.toLogin);
			
		}
	}
}();

$(function(){
	YiYa.login.init();
	
	
	
});	
function checkName() {
var email=$("#email").val();

	$.ajax({
		type : "post",
		url :  "email.shtml",
		//async:false,
		data : {
		"email" : email
		},
		success:function(msg){ 
			
			if ("true" == msg) {
				      // 账号已经存在
				$("#msg").html("<font color='red'>抱歉，" + email + "已被注册，请更换！</font>")
				} else {
				         // 账号不存在
					if(email!=""){
					    $("#msg").html("<font color='green'>恭喜，" + email + " 可以注册！</font>")
					}else{
						$("#msg").html("<font color='red'>抱歉，email 不可为空！</font>")
					}
				}
		}
		});
}


     