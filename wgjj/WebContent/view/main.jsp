<%@page import="com.brilliance.base.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.brilliance.base.model.SysUser,com.brilliance.base.common.Constants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<meta charset="utf-8" />
<title><%=com.brilliance.base.common.Constants.SYS_NAME%></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320">
<!--核心CSS-->
<link href="${ctx }/css/bootstrap.min.css" rel="stylesheet"	type="text/css" />
<link href="${ctx }/css/style-metronic.css" rel="stylesheet"	type="text/css" />
<link href="${ctx }/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx }/css/style-responsive.css" rel="stylesheet"	type="text/css" />
<link href="${ctx }/css/blue.css" id="style_color" type="text/css"	rel="stylesheet">
<link href="${ctx }/css/tree-metronic.css" rel="stylesheet" type="text/css"	/>
<link href="${ctx }/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<!--核心JS-->
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.cookie.min.js" type="text/javascript"></script>
<script src="${ctx }/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx }/js/contextmenu.js" type="text/javascript"></script>
<script src="${ctx }/js/md5.min.js" type="text/javascript"></script>


<!--系统基础JS-->
<script src="${ctx }/js/base.js" type="text/javascript"></script>
<script src="${ctx }/js/head.js" type="text/javascript"></script>
<script src="${ctx }/js/page.js" type="text/javascript"></script>
<script src="${ctx }/js/layer/layer.js" type="text/javascript"></script>
<!--[if lt IE9]> 
    <script src="http://cdn.bootcss.com/html5shiv/r29/html5.min.js"></script>
<![endif]-->
<style type="text/css">
.tl {
	width: 240px;
	height: 193px;
	position: absolute;/*这里一定要设置*/
	z-index: 999999;/*这里是该元素与显示屏的距离，据说越大越好，但是我也没有看到效果，因为没有它也是可以的*/
	margin-top: 20%;
	margin-left: -209px; 
	background-image :url("/ship_three/images/tl.png");
	-webkit-transition: .5s ease-in-out;/* css的transition允许css的属性值在一定的时间内从一个状态平滑的过渡到另一个状态 */
	-moz-transition: .5s ease-in-out;/*这里为了兼容其他浏览器*/
	-o-transition: .5s ease-in-out;
	background-image: url("/ship_three/images/tl.png");
}
ul.page-sidebar-menu > li.active > a {
    background: #4B89DF !important;
}
.tab_bar > li.selected {
    background-color: #4B89DF;
}
.tab_bar > li {
	border-left: none;
    background-color: #3E5371;
    margin-right:5px;
    margin-top: 5px;
    border-radius: 3px !important;
}
.tab_bar .close {
	top: 2px;
}
ul.page-sidebar-menu > li > a {
  border-top: none !important;	
  color: #ffffff !important;
}
ul.page-sidebar-menu > li.active > a i {
    color: #fff;
}

ul.page-sidebar-menu > li:hover > a, 
ul.page-sidebar-menu > li > a:hover,
ul.page-sidebar-menu > li > ul.sub-menu > li > a:hover{
  background: #47618C !important;
}
.portlet.box > .portlet-body {
    padding: 0px !important;
}

.fullPage{
    z-index: 100000;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
}
</style>
<script type="text/javascript">
var userbean={
		userId:'${userbean.userId}',
		deptName:'${userbean.deptName}',
		userName:'${userbean.userName}',
		phone:'${userbean.phone}',
		lastIp:'${userbean.lastIp}',
		lastTime:'${userbean.lastTime}',
		initMenuId:'${userbean.initMenuId}'
}
function logout(){
	layer.confirm("您确认要退出当前系统吗？", {
		  icon: 3, 
		  title:'提示',
		},function(index){
			layer.close(index);
			window.location.href="${ctx }/logout.action";
		},function(index){
			layer.close(index);
		});
}
</script>
</head>
<body class="page-header-fixed">
	<!-- 页面主体page-container开始 -->
	<div id="main" class="" style="background: linear-gradient(#192943,#1a294f,#1a2958); background-image: url('${ctx}/images/left_bg.png') !important;background-size:200px 610px;">
		<!-- BEGIN SIDEBAR -->
		<div id="left" class="page-sidebar navbar-collapse collapse" style="position: absolute; z-index: 9999;background-color: transparent;">
			<!-- BEGIN SIDEBAR MENU -->
			<div style="height: 50px; margin-left: 10px; line-height: 50px;">
				<%-- <img id="logo" src="${ctx }/images/logo.png" alt="logo"/> --%>
			</div>
			<ul class="page-sidebar-menu">
				<c:forEach var="item" items="${menuList }">
					<c:if test="${fn:length(item.childMenus)==0 }">
						<li><a url="${ctx}${item.url}" href="#${item.menuId }" menuid="${item.menuId }" menuName="${obj.menuName }"><i class="fa ${item.note }"></i> ${item.menuName }</a></li>
					</c:if>
					<c:if test="${fn:length(item.childMenus)>0 }">
						<li class="">
							<a href="javascript:;"><i class="fa ${item.note }"></i><span class="title">${item.menuName }</span><span class="arrow"></span></a>
							<ul class="sub-menu">
								<c:forEach var="obj" items="${item.childMenus }">
									<li><a url="${ctx}${obj.url}" href="#${obj.menuId }" menuid="${obj.menuId }" menuName="${obj.menuName }">
										<!-- <i class="fa fa-file-text"></i> --> 
										<c:if test="${fn:contains(obj.note,'.')}">
											<img alt="" src="${ctx }/view/icon/${obj.note}" style="width:16px;height:16px;margin-top: -2px;">&nbsp;
										</c:if>
										<c:if test="${!fn:contains(obj.note,'.')}">
											<i class="${obj.note }" style="font-size:16px;"></i>
										</c:if>
										${obj.menuName }
										<c:if test="${obj.menuId==21 || obj.menuId==81}">
											<span id="taskCount" class="badge badge-important"></span>
										</c:if>
										<c:if test="${obj.menuId==24 || obj.menuId==84}">
											<span id="taskCount_fly" class="badge badge-important"></span>
										</c:if>
									</a></li>
										
								</c:forEach>
							</ul>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
		<!-- END SIDEBAR -->


		<!--EditPwd-->
		<div class="page-content">
			<div id="header" class="header navbar navbar-inverse navbar-fixed-top" style="border-bottom: none;background-image: url('${ctx}/images/header.png') !important; width: calc(100% - 200px); margin-left: 200px;">
				<div class="header-inner" style="padding-top:1px;">
					<a class="sidebar-toggler hidden-phone navbar-brand" style="margin-top: 10px;margin-left: 8px; width: 30px;"></a>
					<a class="navbar-brand" href="#" style="width:600px;height:48px;padding: 15px 15px;font-size: 20px;margin-top: 0px;"> 
						<span style="color: #fff;font-size: 20px;">
							<%=Constants.SYS_NAME %>
						</span>
					</a> 
					<a href="javascript:logout()" class="dropdown-toggle" style="float: right;margin-top: 20px;padding-right: 20px;color:#CA1E1D;">退出</a>
					<a href="#basic" onClick="Tool.userInfo()" class="dropdown-toggle" style="float: right;margin-top: 20px;color: #fff;">${userbean.userDesc }(${userbean.userName })&nbsp;&nbsp;|&nbsp;&nbsp;</a>
					<a href="javascript:void(0);" class="dropdown-toggle" style="float: right;margin-top: 20px;padding-right: 20px;"> 
						<img alt="" src="${ctx }/images/time.png" width="14px" height="14px" style="margin-top: -1px;"/>&nbsp;
						<span style="padding-bottom: 100;color: #fff;" id="localtime"></span>
					</a>
					
				</div>
		
			</div>
			<!-- END HEADER -->
			<div class="mainbox" style="padding-top: 50px;">
				<div class="contextMenu" id="tabBarMenu" style="display: none">
					<ul>
						<li id="item_1">刷新标签页</li>
						<li id="item_2">关闭标签页</li>
						<li id="item_3">关闭其他页</li>
						<li id="item_4">关闭全部页</li>
					</ul>
				</div>
				<div id="portlet-title-tab" style="background-color: #1B2E44;height:40px;">
					<div id="tab_wrap" style="height:40px;">
						<ul class="tab_bar">
							<li url="" tabid="L10000" menuid="10000"><a
								onClick="UtilFun.selected(this)" title="首页" href="javascript:"><span><i
										class="fa fa-home"></i> 首页</span></a></li>
						</ul>
					</div>
				</div>
				<div id="main-page-bar">
					<div class="portlet box" id="page_L10000">
						<div class="portlet-body">
							<script type="text/javascript">
								$(function(){
									$(window.rightFrame.document).find("body").css("margin","0");
								})
							</script>
							<iframe name="rightFrame" id="rightFrame" frameborder="0"
								 width="100%" height="100%" src="${ctx }/view/index.jsp"></iframe>
						</div>

					</div>
				</div>
			</div>

		</div>
		<!-- 页面结束 -->
		
	</div>
	<!-- 页面主体page-container结束 -->

	<div class="msg">
		<i class="fa fa-exclamation-triangle" style="color: yellow"></i>&nbsp;&nbsp;<span></span>
	</div>
	<div class="dataTables_processing" id="onProcess">
		<span class="note-success" style="padding: 10px; border-radius: 10px"><i
			class="fa fa-coffee"></i>加载中请等待...</span>
	</div>
	<div aria-hidden="false" role="basic" tabindex="-1" id="basic"
		class="modal fade in" data-width="950">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<a aria-hidden="true" data-dismiss="modal" class="close-dialog"
						type="button" onClick="base.Dialog.hide()">×</a>
					<h4 class="modal-title">
						<span id="modal-title"></span>
					</h4>
				</div>
				<div class="modal-body" id="modal-body-box">
					<div class="body-box"></div>
				</div>
			</div>
		</div>
	</div>

	<div id="loginBox" style="display: none">
		<div
			style="padding:0px; background:url(${ctx }/images/ajax-loading.gif) no-repeat center center #fff;">
			<a href="#basic" data-toggle="modal"></a>
			<iframe class="fn-hide" src="" width="100%" height="220"
				id="loginIframe" frameborder="0" scrolling="no"></iframe>
		</div>
	</div>
	<div class="imgView"></div>
	<script src="${ctx }/js/menu.js"></script>
	<script src="${ctx }/js/app.js"></script>
	<script type="text/javascript" src="${ctx }/js/My97DatePicker/WdatePicker.js"></script>
</body>
<style>
#left{
	max-height:1000px !important;
}
.userDiv{
	position:fixed;
    bottom:0px;
    text-align: center;
    
}
.userDiv a{
	text-decoration: none;
	color:#fff;
}
.userDiv img{
	 opacity:0.6;
}
</style>
<script type="text/javascript"> 
    var leftWidth = $('.page-sidebar').width(); // 左侧菜单窗口大小
    var iframeHeight=500;
	var htmlObj = $("html,body");
	var headerObj = $("#header");
	function wSize(){
		var frameObj = $("#left,iframe");
		var minWidth=900;
		var minHeight=500;
		var strs = getWindowSize().toString().split(",");
		htmlObj.css({"overflow-x" : (strs[1]<minWidth ? "auto" : "hidden"),"overflow-y" : (strs[0]<minHeight ? "auto" : "hidden")});
		iframeHeight=strs[0]- headerObj.height()-50;
		iframeHeight = iframeHeight + 12;
		frameObj.height(iframeHeight);
		changeAllBodyHeight(iframeHeight);
		$("#main").css("background-size",leftWidth+"px "+(strs[0])+"px");
		$('#userDiv').css({"width":leftWidth+"px",'height':'50px'});
	}
	//改变iframe的高度同时改变iframe中body的高度
	function changeAllBodyHeight(iframeHeight){
		$("#main-page-bar").find("iframe").each(function(index,item){
			$(item).contents().find("body").height(iframeHeight);
		});
	}
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
	$(window).resize(function() {
		$(document).scrollTop(0);
		wSize();
	});
	wSize();

	//获取我的消息记录
	var message_data="";
    //关闭窗口使用
    var index='';
	function message(){
		if(message_data==null || message_data.length==0){
			$.ajax({
				url:"${ctx}/messagelist.action",
				type:"post",
				dataType:"json",
				async:false,
				success:function(data){
					if(data.length>0){
						message_data=data;
						
						if(index == '') {
							index = layer.open({
								skin: 'demo-class',
								type: 2,
								title:'您有新消息',
								area: ['300px', '200px'],
								offset: 'rb',
								content: ['${ctx}/view/message.jsp', 'no'],
								shade: 0,     //不显示遮罩
								end:function(){
									message_data="";
								}
							});
						}
					}
					
				},
				
			});
		}
		setTimeout(message,10000);
	}
	
	$(document).ready(function(){
		message();
		//初始化首页
		var initMenuId=userbean.initMenuId;
		if(initMenuId==null || initMenuId==''){
			initMenuId='${userbean.sysDept.initMenuId}';
		} 
		if($('a[menuid='+initMenuId+']').length>0){
			$('a[menuid='+initMenuId+']')[0].click();
		}
		
		if (window.navigator.userAgent.indexOf("MSIE")>=1){
			$('a[class=navbar-toggle]')[0].click();
			$('a[class=navbar-toggle]').addClass('hide');  
		}
	});
	
	
	function closeLayer() {
		if(index != '') {
			layer.close(index);
			index='';
		}
	}
	
	/**
		系统消息直接打开页面
	**/
	function openMenu(menuUrl,modelId) {
		 if(menuUrl!=null && menuUrl!='' && modelId != null && modelId!= ''){
			 menuUrl = '${ctx}'+ menuUrl;
			$("a").each(function(){
				 	var url = $(this).attr("url");
				 	if(url) {
					 	//如果找到了目标菜单
					 	if(menuUrl == url) {
					 		try {
						 		$('.open').removeClass();
						 		$('.sub-menu').css("display","none");
						 		$(this).parent().parent().parent().addClass("active open");
						 		$(this).parent().parent().css("display","block");
						 		$(this).parent().parent().prev().find("span[class=arrow]").addClass("arrow open"); 
					 		}catch(err) {};
					 		//点击菜单
					 		$(this).parent().prev().trigger("click");
					 		
					 		var resUrl = menuUrl + "?modelId=" + modelId;
					 		if(menuUrl.indexOf("?") != -1) {
					 			resUrl = menuUrl + "&modelId=" + modelId;
					 		}
					 		
					 		//重新加载tab页面
					 		var pageMenuid = $(this).attr("menuid");
					 		var pageMen = $("li[menuid='"+pageMenuid+"']");
					 		if(pageMen) {
					 			var thisClose = pageMen.children(".close");
					 			if(thisClose && thisClose.attr("class") && thisClose.attr("class") == "close"){
						 			$(thisClose).trigger("click");
					 			}
					 		}
					 		var pageUrl = $("li[menuid='"+pageMenuid+"']").attr("url");
					 		$(this).attr("url",resUrl);
					 		//点击子菜单，根据参数调整查询
					 		$(this).trigger("click");
					 		$(this).attr("url",url);
					 		if(message_data.length == 0) {
								 closeLayer();
							  }
					 	}
				 	}
				});
		} 
	}
	
	//显示当前时间
	function showLocale(objD){
		var yy = objD.getYear();
		if(yy<1900) yy = yy+1900;
		var MM = objD.getMonth()+1;
		if(MM<10) MM = '0' + MM;
		var dd = objD.getDate();
		if(dd<10) dd = '0' + dd;
		var hh = objD.getHours();
		if(hh<10) hh = '0' + hh;
		var mm = objD.getMinutes();
		if(mm<10) mm = '0' + mm;
		var ss = objD.getSeconds();
		if(ss<10) ss = '0' + ss;
		return (yy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss);
	}
	function tick(){
		var today;
		today = new Date();
		document.getElementById("localtime").innerHTML = showLocale(today);
		window.setTimeout("tick()", 1000);
	}
	tick();
	
	var loading;
	function openLoading(){
		loading = layer.load(2, {
		  shade: [0.2,'#fff'] //0.1透明度的白色背景
		});
	}
	function closeLoading(){
		if(loading){
			layer.close(loading);
		}
	}
</script>
</html>
