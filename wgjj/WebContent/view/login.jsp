<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="UTF-8" />

<title><%=com.brilliance.base.common.Constants.SYS_NAME%></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320">
<!--核心CSS-->

<style type="text/css">
*{ padding:0px; margin:0px;}
body{margin:0px;text-align:center;background-color: #fff;
}
.login{
	width:100%;
	height:100%;
	background:url('${pageContext.request.contextPath}/images/login.png') no-repeat; 
	margin: 0; 
	background-size: 100%,100%; 
}
.title{
	float:right;
	margin-top: 17.5%;
	margin-right: 1.6%;
	text-align: left;
}
.inputbox{
	float:right;
	background-color: #FFF;
	border-radius:2px;
	margin-right: 28%;
	margin-top: 14.5%;
}
.inputbox input{
	width:220px;
	height: 28px;
	background: transparent; 
	border: none;
	border-bottom:1px solid #EAEAEA;
	outline: none;
	padding-left: 10%;
}
#username{background:url('${pageContext.request.contextPath}/images/user.png') no-repeat 0 0;background-position: 1% 50%; }
#password{background:url('${pageContext.request.contextPath}/images/pwd.png') no-repeat 0 0;background-position: 1% 50%; }

.inputbox .btn_sub{
	background: #15243A;
	cursor: pointer;
	border:none;
	color:#fff;
	font-size: 18px;
	font-weight: 500;
}
.login_title{
    /* font-size: 150%; */
    padding-top: 10%;
    text-align: left;
    padding-left: 10%;
}
</style>

<script src="${pageContext.request.contextPath }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath }/js/md5.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		var getWindowSize = function() {
			if (window.navigator.userAgent.indexOf("MSIE")>=1){
				var strs=new Array();
				strs[0]=$(document).height();
				strs[1]=$(document).width();
				return strs;
			}else{
				return ["Height", "Width"].map(function(a) {
					return window["inner" + a] || document.compatMode === "CSS1Compat" && document.documentElement["client" + a] || document.body["client" + a]
				});
			}
			
		};
		//设置登录面板居中
		function setLoginMarginTop(){
			var strs = getWindowSize().toString().split(",");
			$('.login').css('height',strs[0]);
			$('.inputbox').css('width',strs[1]/5);
			$('.inputbox').css('height',strs[0]/1.8);
			$('.login input').css({'height':strs[0]/17,'width':strs[1]/6.5,'margin-left':'5%'});
			$('.login button').css({'height':strs[0]/17,'width':strs[1]/6,'margin-left':'5%'});
			$('.login div[name=lineH]').css({'height':strs[0]/25});
			$('.fontSize').css('font-size',strs[1]/100);
			$('.fontSize1').css('font-size',strs[1]/76);
		}
		setLoginMarginTop();
		$(window).resize(function() {
			setLoginMarginTop();
		});
		$("#username").focus();
	})	
</script>
</head>
<body onkeydown="keyLogin();">
<form method="post" action="${pageContext.request.contextPath }/login.action" id="loginForm" class="login-form">
	<%if(com.brilliance.base.common.Constants.SYS_SHOW_LOGO){%>
	<img src="${pageContext.request.contextPath }/images/logo_big.png" style="position: absolute; top: 26px; left: 58px; width: 142px; height: 47px;">
	<%}%>
	<input type="hidden" name="deptId" value=""/>
	<div class="login">
		<div class="inputbox">
			<div class="login_title fontSize">登&nbsp;&nbsp;录</div>
		 	<div name="lineH"></div>
		 	<div name="lineH"></div>
		 	<input placeholder="请输入登录账号" value="" type="text" maxlength="60" autocomplete="off" name="userName" id="username">
			<div name="lineH"></div>
			<input placeholder="请输入账号密码" value="" maxlength="20" type="password" autocomplete="off" id="password">
			<input type="hidden" name="userPwd" id="md5_password">
			<div name="lineH"></div>
			<div id="msgbar" style="color:red;height:28px;font-size: 12px;">${message }</div>
			<div name="lineH"></div>
			<button onclick="doSubmit()" type="button" class="btn_sub" id="btn_sub">登&nbsp;&nbsp;&nbsp;&nbsp;录</button>
		</div>
		<div class="title" >
			<div class="fontSize1" style="color: #fff;"><%=com.brilliance.base.common.Constants.SYS_NAME%></div><br/>
			<div class="fontSize" style="color:#fff;opacity: 0.6"><%=com.brilliance.base.common.Constants.SYS_NAME_EN%></div>
		</div>
	</div>
	<div style="position: absolute;width:100%;text-align: center;color:#ddd;font-size: 0.8rem; bottom:10px;">
		<span style="padding-right:10px;"><%=com.brilliance.base.common.Constants.SYS_COPYRIGHT%></span>
		<a href="https://beian.miit.gov.cn" target="_blank" style="color:#ddd;text-decoration: none;"><%=com.brilliance.base.common.Constants.SYS_BEIAN%></a>
	</div>
</form>
<%session.removeAttribute("message");%>
<script type="text/javascript">
	    if(top.location!=self.location){
	    	top.location = self.location;
	    } 
         function doSubmit(){
        	 if(document.getElementById("username").value.length==0){
        		 alert("用户名不能为空！");
        		 return;
        	 }
        	 if(document.getElementById("password").value.length==0){
        		 alert("密码不能为空！");
        		 return;
        	 }
        	 
        	 $("#md5_password").val(hex_md5($("#username").val() + $("#password").val()).toUpperCase());
             document.getElementById("loginForm").submit();
         }
         
         function keyLogin(){  
        	 if (event.keyCode==13) {  //回车键的键值为13   
        		 doSubmit();
     		 } 
         }
         
</script>
</body>
</html>