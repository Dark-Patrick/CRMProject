<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script>

		$(function (){

			//设为顶层窗口
			if(window.top != window){
				window.top.location = window.location;
			}

			$("#loginAct").val("")

			//页面加载完毕，自动获得焦点
			$("#loginAct").focus();

			$("#loginBtn").click(function (){
				Login();
			})

			$(window).keydown(function (event){
				if (event.keyCode == 13) {
					Login();
				}
			})
		})
		//普通的自定义的function方法，一定要写在$(function(){})外面
		function Login(){
			let loginAct = $.trim($("#loginAct").val());
			let loginPwd = $.trim($("#loginPwd").val());
			if(loginAct == "" || loginPwd == ""){
				$("#msg").html("用户名和密码不能为空")
				return false;
			}
			//验证登录
			$.post("settings/user/login.do", {"loginAct":loginAct,"loginPwd":loginPwd},
				function (resp){
					/**
					 * resp
					 * 		{"success":true/false,"msg":"错误信息"}
					 */
					if(resp.success){
						window.location.href = 'workbench/index.html';
					}else {
						$("#msg").html(resp.msg);
					}

			}, "json");
		}

	</script>

</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2021&nbsp;华电靓仔</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form  action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>