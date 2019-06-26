<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>咿呀网 - 用户登录</title>
		<%@include file="/view/resource.jsp" %>
    	<link rel="stylesheet" type="text/css" href="${msUrl}/css/main.css">
    	<link rel="stylesheet" type="text/css" href="${msUrl}/css/user_regester.css">
	</head>
	<form id="loginForm" action="toRegester.do" method="post">
	<body id=userlogin_body>
		<div></div>
		<div id=user_login>
			<dl>
				
				<dd id=user_main>
					<ul>
						<li class=user_main_l></li>
						<li class=user_main_c>
							<div class=user_main_box>
								<ul>
									<li class=user_main_text>
										用户名：
									</li>
									<li class=user_main_input>
										<input class="txtusernamecssclass easyui-validatebox"  data-options="required:true,validType:'email',missingMessage:'邮箱不能为空.',invalidMessage:'邮箱格式不正确'" name="email" id="email" onblur="checkName()"  value="" maxlength="20">
										
									</li>
									<li>
									    <span id="msg" ></span>
									</li>
									
								</ul>
								<ul>
									<li class=user_main_text>
										密 码：
									</li>
									<li class=user_main_input>
										<input class="txtpasswordcssclass easyui-validatebox"   data-options="required:true,missingMessage:'密码不能为空.'" type="text" name="pwd" >
									</li>
								</ul>
								<ul>
									<li class=user_main_text>
										确认密 码：
									</li>
									<li class=user_main_input>
										<input class="txtpasswordcssclass easyui-validatebox"   data-options="required:true,missingMessage:'密码不能为空.'" type="text" name="supwd" >
									</li>
								</ul>
								<ul>
									<li class=user_main_text>
										验证码：
									</li>
									<li class=user_main_input>
										<img class="vc-pic" width="65" height="23" title="点击刷新验证码" src="ImageServlet?time=new Date().getTime()">
										<input class="vc-text easyui-validatebox" data-options="required:true,missingMessage:'验证码不能为空.'" maxlength="4" type="text" name="verifyCode">
									</li>
									
								</ul>
								<ul>
								
								  <li class=user_main_input>
						            <input class="txtpasswordcssclass easyui-validatebox"    type="submit" name="submit" value="注册" >
						              </li>
						              <li>已有账号，</li><a href="login.shtml">请登录</a>
								</ul>
								
							</div>
						</li>
						
					</ul>
			</dl>
		</div>
		<div></div>
		</form><br>
       <script type="text/javascript" src="${msUrl}/js/ux/regester.js"></script>
	</body>
</html>
