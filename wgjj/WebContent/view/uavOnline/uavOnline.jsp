<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="fns" prefix="fns" %>
<%@ taglib uri="lg" prefix="lg" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE HTML>

<html>
<head>
<title>无人机飞行态势监视</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="renderer" content="webkit"><meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10" />
<meta http-equiv="Expires" content="0"><meta http-equiv="Cache-Control" content="no-cache"><meta http-equiv="Cache-Control" content="no-store">
<meta name="decorator" content="blank"/>
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="${ctx }/leaflet/basic/jwd_dfm_transform_tool.js" type="text/javascript" ></script>
<script src="${ctx }/js/jquery.cookie.min.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.form.js" type="text/javascript"></script>
<script src="${ctx }/bootstrap/3.3.7/css/dist/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx }/js/bootstrapPager.js" type="text/javascript" ></script>
<script src="${ctx }/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx }/js/layer/layer.js" type="text/javascript"></script>
<script src="${ctx }/css/layui/layui.js" type="text/javascript"></script>
<script src="${ctx }/js/echarts.min.js" type="text/javascript"></script>
<script src="${ctx }/js/vue.min.js" type="text/javascript"></script>

<link href="${ctx }/bootstrap/3.3.7/css/dist/css/bootstrap.min.css" rel="stylesheet" /> 
<link href="${ctx }/css/bootstrap/2.3.1/awesome/font-awesome.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx }/css/index.css" rel="stylesheet" type="text/css"/>
<link href="${ctx }/css/layui/css/layui.css" rel="stylesheet" />
<link href="${ctx }/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

<script src="${ctx }/js/validate.js" type="text/javascript"></script>
<script src="${ctx }/view/uavOnline/uavOnline.js" type="text/javascript"></script>
<style type="text/css">
	body, html {
		width: 100%;
		/* height: 662px; */
		overflow: hidden;
		margin:0px;
		padding:0px;
		font-family:"Arial,Helvetica,sans-serif,Microsoft YaHei";
		z-index: 100;
	}
	.label{
		padding: 2px 3px 2px 7px;
	}
	.table-margin{
		margin: 0px;
	}
	.table{
		border-collapse: collapse;/*将表格边框合并为单一的边框，将相邻变合并*/
		border: none;
	}
	.table>thead>tr>th,.table>tbody>tr>td{
		padding: 1px 0 1px 8px;
		word-wrap:break-word;
		word-break:break-all;
		border: none;
		font-size: 10px;
		line-height: 22px;
	}
	.table>tbody>tr>td{
		font-size: 12px
	}
	input[type="checkbox"]{
		vertical-align: text-top;
		cursor: pointer;
	}
	ul[class="dropdown-menu"]{
		padding: 5px 10px 5px 10px;
	}
	.controls{
		padding-top: 5px;
	}
	/*给列表中的操作按钮赋样式*/
	td>a{
		margin-right: 7px;
		cursor: pointer;
	}
	/*点击按钮背景色*/
	.clickButton{
		background-color: #e0e0e0;
	}
	/*设置layer显示的window样式*/
	.layui-layer-title{
		height: 30px;
		line-height: 30px;
	}
	.layui-layer-setwin{
		top: 9px;
	}
	/*回放时间显示样式*/
	#replayTime{
		font-size: 13px;
	    text-align: center;
	    position: absolute;
	    left: 450px;
	    top: 10px;
	    z-index: 999;
	    width: 230px;
	    height: 32px;
	    background-color: rgb(255, 255, 255);
	    line-height: 32px;
	    color: #000;
	    display: none;
	}
	.btn-default{
		border: 1px solid #f1f1f1;
	}
</style>
<script type="text/javascript">
function Confirm(msg,callback,args){
	parent.layer.confirm(msg, {
	  icon: 3, 
	  title:'提示',
	},function(index){
		parent.layer.close(index);
		callback.apply(this,args);
	},function(index){
		parent.layer.close(index);
	});
}

function Confirm(msg,callback){
	parent.layer.confirm(msg, {
	  icon: 3, 
	  title:'提示',
	},function(index){
		parent.layer.close(index);
		callback();
	},function(index){
		parent.layer.close(index);
	});
} 
window.alert=function(msg){
	parent.layer.msg(msg,{
		time:3000
	});
}

String.prototype.replaceAll = function (FindText, RepText) { 
	regExp = new RegExp(FindText, "g"); 
	return this.replace(regExp, RepText); 
}
var ctx='${ctx}';
var isEdit = "${fns:hasPms(pmsIds,'3202')}";//是否有管理权限
var maxZIndex = 99;//页面上最大的z-index数
var flyingEventUavPns = [];//所有未处置事件的在飞的pn
var warnUpToEventSeqs = [];//记录有所有升级事件的告警
var haveEventPns = [];//记录所有存在事件的无人机sn码
//获取最大的z-index数
function getMaxZIndex(){
	maxZIndex = maxZIndex + 1;
	return maxZIndex;
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
	wsReconnect=false;
	if(wsConnect)
		closeWebSocket();
}

//判断无人机是否存在未处理的事件
function isHaveEvent(pn){
	if(haveEventPns.length == 0) {
		return false;
	}
	var isFound = false;
	for(var j = 0;j < haveEventPns.length;j++) {
		if(pn == haveEventPns[j]) {
			isFound = true;
			break;
		}
	}
	return isFound;
}

$(function(){
	$("table").each(function(index,item){
		$(item).find("tr:eq(0) th").css({"text-align": "left", "background-color": "#d1e0f3", "background-image": "none"});
	})
	//点击按钮组下拉框内容时不关闭下拉框
	$(".dropdown-menu").on('click', function (e) {
        e.stopPropagation();
    });
})

$(function(){
	$("#currContentDiv").css("left",((parent.getWindowSize()[1]-$("#left",parent.document).width()-330)/2));
	$("#replayTime").css("left",((parent.getWindowSize()[1]-$("#left",parent.document).width()-$("#replayTime").width())/2+50));
	$("#mapIframe").css({"height" : ($(parent.document.getElementsByTagName("iframe")[1]).height())});//使地图可以全屏显示
	//当窗口变化时重置地图大小
	$(window).resize(function() {
		$("#currContentDiv").css("left",((parent.getWindowSize()[1]-$("#left",parent.document).width()-330)/2));
		$("#replayTime").css("left",((parent.getWindowSize()[1]-$("#left",parent.document).width()-$("#replayTime").width())/2+50));
		$("#mapIframe").css({"height" : ($(parent.document.getElementsByTagName("iframe")[1]).height())});//使地图可以全屏显示
	});
	
	//当机构的地图范围不为空时，设置地图的默认显示范围
	function setMapCenter(){
		mapIframe.positionCity();//根据所在城市定位地图的中心
	}
	$("#mapIframe").load(function(){
		window.setTimeout(function(){
			//设置展示的地图类型控件的位置
			mapIframe.setControlOptions(mapIframe.getNavigationControl(),{offset:mapIframe.createOffset(10,50),anchor:mapIframe.TOP_RIGHT});
			//设置展示的比例尺的位置
			mapIframe.setControlOptions(mapIframe.getScaleControl(),{offset:mapIframe.createOffset(10,10),anchor:mapIframe.LEFT_BOTTOM});
		},0);

		pageInit();//地图加载完成后连接websocket
		setMapCenter(); //当机构的地图范围不为空时，设置地图的默认显示范围，如果为空设置显示范围为海南省
	});
})

//关闭所有的面板
function closePanel(){
	closeUavInfo();
	closePlanList();
	closeAdsbList();
}

//打开指定面板
function showPanel(panelFlag,obj){
	closePanel();
	obj.blur();//使重放按钮失去焦点
	if(panelFlag == "One"){
		showWarnList();
	}else if(panelFlag == "Two" ){
		showSpaceList();
	}else if(panelFlag == "Three" ){
		showUavList();
	}else if(panelFlag == "Four" ){
		showPlanList();
	}else if(panelFlag == "Five" ){
		showAdsbList();
	}
	$(".showButton").removeClass("clickButton");
}

// 控制是否显示正常无人机标牌
function changeUavLabelCheckbox(){
	mapIframe.isShowLabel();
}
</script>
</head>

<body style="overflow-x: hidden;">
	<!-- 音乐播放 -->
	<audio id="ringing" src="${pageContext.request.contextPath }/view/uavOnline/warnSound/ringing.mp3" preload="auto" loop></audio>
	<audio id="speedFast" src="${pageContext.request.contextPath }/view/uavOnline/warnSound/speedFast.mp3" preload="auto" loop></audio>
	<audio id="speedSlow" src="${pageContext.request.contextPath }/view/uavOnline/warnSound/speedSlow.mp3" preload="auto" loop></audio>
	<!-- 显示鼠标所在经纬度以及当前地图缩放比例 -->
	<div id="currContentDiv" style="position: absolute; bottom: 10px; left: 160px; font-size: small; font-family: serif; color: #4174CD;">
		<div style="float: left; margin-right: 10px;">
			<label style="float:left">经度:</label>
			<label style="float:left" id="currMouseLng"></label>
		</div>
		<div style="float: left; margin-right: 10px;">
			<label style="float:left">纬度:</label>
			<label style="float:left" id="currMouseLat"></label>
		</div>
		<div style="float: left;">
			<label style="float:left">缩放级别:</label>
			<label style="float:left" id="currZoom"></label>
		</div>
	</div>
	<!-- loading -->
	<div class="modal fade" id="loading" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop='static'>
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="myModalLabel">提示</h4>
	      </div>
	      <div class="modal-body">
	       		正在生成无人机数据，请稍候。。。
	      </div>
	    </div>
	  </div>
	</div>
	<!-- loading1 -->
	<div class="modal fade" id="loading1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop='static'>
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="myModalLabel">提示</h4>
	      </div>
	      <div class="modal-body">
	       		正在结束重放，请稍候。。。
	      </div>
	    </div>
	  </div>
	</div>
	<!--重放窗口-->
	<div id="replayDiv" style="display: none;">
		<div class="modal-body" style="padding: 5px 0 10px 10px">
			<form id="replayForm" class="form-horizontal">
				<div class="control-group" style="margin: 5px 0px">
					<label class="control-label" style="float: left;">开始时间：</label>
					<div class="controls">
						<input id="replayBegDate" style="width: 140px"  type="text" class="required" name="begDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'replayEndDate\')||\'%y-%M-%d %H:%m\'}'});" readonly="readonly" dataType="Require" msg="请选择"/>
					</div>
				</div>
				<div class="control-group" style="margin: 5px 0px">
					<label class="control-label" style="float: left;">结束时间：</label>
					<div class="controls">
						<input id="replayEndDate" style="width: 140px"  type="text" class="required" name="endDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'replayBegDate\')}', maxDate:'%y-%M-%d %H:%m'});" readonly="readonly" dataType="Require" msg="请选择"/>
					</div>
				</div>
				<div class="control-group" style="margin: 5px 0px">
					<label class="control-label" style="float: left;">重放速度：</label>
					<div class="controls">
						<select id="replaySpeed" style="width: 140px">
							<option value="1" selected> 1倍 </option>
							<option value="2"> 2倍 </option>
							<option value="5"> 5倍 </option>
							<option value="10"> 10倍 </option>
							<option value="20"> 20倍 </option>
							<option value="50"> 50倍 </option>
							<option value="100"> 100倍 </option>
						</select>					
					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer" style="padding: 10px 10px 5px 10px">
			<button id="startReplay" style="display: none;background-color: #609df1;border: none;" type="button" class="btn btn-primary" onclick="startReplay()">
				<div style="float: left;">
					<i class="icon-play" style="color: #fff;"></i>
				</div>
				<div style="float:left;padding-top:1px;padding-left:8px;">开始</div>
			</button>
			<button id="continueReplay" style="display: none;background-color: #609df1;border: none;" type="button" class="btn btn-primary" onclick="continueReplay()">
				<div style="float: left;">
					<i class="icon-play" style="color: #fff;"></i>
				</div>
				<div style="float:left;padding-top:1px;padding-left:8px;">继续</div>
			</button>
			<button id="pauseReplay" style="display: none;background-color: #609df1;border: none;" type="button" class="btn btn-primary" onclick="pauseReplay()">
				<div style="float: left;">
					<i class="icon-pause" style="color: #fff;"></i>
				</div>
				<div style="float:left;padding-top:1px;padding-left:8px;">暂停</div>
			</button>
			<button id="endReplay" style="display: none;" type="button" class="btn btn-default" onclick="endReplay()">
				<div style="float: left;">
					<i class="icon-stop" style="color: #000;"></i>
				</div>
				<div style="float:left;padding-top:1px;padding-left:8px;">结束</div>
			</button>
			<button id="cancelReplay" style="display: none;" type="button" class="btn btn-default" onclick="buttonCancelReplay()">
				<div style="float: left;">
					<i class="icon-remove-sign" style="color: #000;"></i>
				</div>
				<div style="float:left;padding-top:1px;padding-left:8px;">取消</div>
			</button>
		</div>
	</div>
	
	<!--页面工具栏-->
	<div class="btn-group" style="position: absolute;right: 50px;top:10px">
		<button type="button" class="btn btn-default" onclick="openReplay(this)"><div style="float:left"><img class="buttonClass" src="${ctx }/view/uavOnline/image/replay_button.png"></div><div style="float:left;padding-top:1px;padding-left:8px;">重放</div></button>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<div style="float:left">
					<img class="buttonClass" src="${ctx }/view/uavOnline/image/space_button.png">
				</div>
				<div style="float:left;padding-top:1px;padding-left:8px;">
				空域&nbsp;
				<span class="caret"></span>
				</div>
			</button>
			<ul class="dropdown-menu" style="min-width: 147px;">
				<li><input type="checkbox" id="allSpaceType" checked="checked">&nbsp;&nbsp;全部</li>
				<c:forEach var="item" items="${infoSpaceTypeList }">
					<li style="display: inline-flex; line-height: 21px;">
						<input checked="checked" type="checkbox" name="spaceTypes" id="spaceType_${item.spcType }" colorBack="${item.colorBack }" colorBorder="${item.colorBorder }">
						<span style="font-size: 12px;opacity: 0.8;margin-left: 2px;">${item.typeName }</span>
						<div style="width: 10px; height:10px; background-color: ${item.colorBack};margin: 5px 0px 0px 2px;border-radius:10px;"></div>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<div style="float:left">
					<img class="buttonClass" src="${ctx }/view/uavOnline/image/view_button.png">
				</div>
				<div style="float:left;padding-top:1px;padding-left:8px;">
					显示&nbsp;
					<span class="caret"></span>
				</div>
			</button>
			<ul class="dropdown-menu" style="min-width: 125px;">
				<li><input onchange="changeAdsbPlaneCheckbox()" type="checkbox" name="adsbPlaneCheckbox" id="adsbPlaneCheckbox"><span style="font-size: 12px;opacity: 0.8;">&nbsp;&nbsp;ADSB飞机</span></li>
				<li><input onchange="changePlaneLinesCheckbox()" type="checkbox" name="planeLinesCheckbox" id="planeLinesCheckbox"><span style="font-size: 12px;opacity: 0.8;">&nbsp;&nbsp;民航航线</span></li>
				<li><input onchange="changeAdsbPlaneLabelCheckbox()" type="checkbox" name="adsbPlaneLabelCheckbox" id="adsbPlaneLabelCheckbox"><span style="font-size: 12px;opacity: 0.8;">&nbsp;&nbsp;ADSB飞机标牌</span></li>
				<li><input onchange="changePlaneLinesLabelCheckbox()" type="checkbox" name="planeLinesLabelCheckbox" id="planeLinesLabelCheckbox"><span style="font-size: 12px;opacity: 0.8;">&nbsp;&nbsp;民航航线名称</span></li>
				<li class="hide"><input onchange="changeUavLabelCheckbox()" type="checkbox" name="uavLabelCheckbox" id="uavLabelCheckbox"><span style="font-size: 12px;opacity: 0.8;">&nbsp;&nbsp;无人机标牌</span></li>
				<li class="hide"><input checked="checked" onchange="changeRelUavCheckbox()" type="checkbox" name="relUavCheckbox" id="relUavCheckbox"><span style="font-size: 12px;opacity: 0.8;">&nbsp;&nbsp;关联无人机</span></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<div style="float:left">
					<img class="buttonClass" src="${ctx }/view/uavOnline/image/legend_button.png">
				</div>
				<div style="float:left;padding-top:1px;padding-left:8px;">
					图例&nbsp;
					<span class="caret"></span>
				</div>
			</button>
			<ul class="dropdown-menu" style="min-width: 144px;">
				<!-- 图例 -->
				<li>
					<div id="legentDiv">
						<table style="width: 100%;font-size: 12px;text-align: left;">
							<tr style="height: 25px;">
								<td style="width: 20px">
									<img src="${pageContext.request.contextPath }/view/uavOnline/image/adsb.png" style="width: 20px; height: 20px;">
								</td>
								<td style="width: 80px; line-height: 25px; padding: 0; opacity:0.8;"><span style="margin: 0 0px;">飞机</span></td>
							</tr>
							<c:forEach var="item" items="${infoSpaceTypeList }">
								<tr style="height: 25px">
									<td><div style="width: 16px; height:16px; background-color: ${item.colorBack};"></div></td>
									<td style=" line-height: 25px; padding: 0; opacity:0.8;"><span style="margin: 0 0px;">${item.typeName }</span></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<div class="btn-group" style="position: absolute;left: 10px;top:10px;z-index: 999">
		<button type="button" class="btn btn-default showButton hide" onclick="showPanel('One',this)" id="OneShowButton"><div style="float:left"><img class="buttonClass" src="${ctx }/view/uavOnline/image/warn_button.png"></div><div style="float:left;padding-top:1px;padding-left:8px;">告警(<span id="warnNum" style="margin-bottom: 0px;">0</span>)</div></button>
		<button type="button" class="btn btn-default showButton" onclick="showPanel('Two',this)" id="TwoShowButton"><div style="float:left"><img class="buttonClass" src="${ctx }/view/uavOnline/image/space_button.png"></div><div style="float:left;padding-top:1px;padding-left:8px;">空域(<span id="spaceNum" style="margin-bottom: 0px;">0</span>)</div></button>
		<button type="button" class="btn btn-default showButton hide" onclick="showPanel('Three',this)" id="ThreeShowButton"><div style="float:left"><img class="buttonClass" src="${ctx }/view/uavOnline/image/uav_button.png"></div><div style="float:left;padding-top:1px;padding-left:8px;">无人机(<span id="uavNum" style="margin-bottom: 0px;">0</span>)</div></button>
		<button type="button" class="btn btn-default showButton" onclick="showPanel('Four',this)" id="FourShowButton"><div style="float:left"><img class="buttonClass" src="${ctx }/view/uavOnline/image/plan_button.png"></div><div style="float:left;padding-top:1px;padding-left:8px;">计划(<span id="planNum" style="margin-bottom: 0px;">0</span>)</div></button>
		<button type="button" class="btn btn-default showButton" onclick="showPanel('Five',this)" id="FiveShowButton"><div style="float:left"><i class="fa fa-plane" style="font-size: 16px;color: #566e85;vertical-align: middle;"></i></div><div style="float:left;padding-top:1px;padding-left:8px;">有人机(<span id="adsbPlaneNum" style="margin-bottom: 0px;">0</span>)</div></button>
	</div>
	<div id="replayTime">
		<div style="float: left;margin-left: 10px;"><i class="icon-time" style="color: #000; margin-right: 5px;"></i>态势回放：</div>
		<div id="replayTimeStr" style="float: left;"></div>
	</div>
	<iframe src="${pageContext.request.contextPath }/leaflet/leaflet_map.jsp" name="mapIframe" id="mapIframe" width="100%" height="100%" frameborder="0" ></iframe>
	</div>
</body>
<%@include file="uavOnline_uavInfo.html" %>
<%@include file="uavOnline_planList.html" %>
<%@include file="uavOnline_adsbList.html" %>
</html>
