<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>飞行架次详情</title>
<%@ include file="../tool.jsp"%>
<link rel="stylesheet" href="${ctx }/css/layui/css/layui.css">
<!-- 引入 ECharts 文件 -->
<script type="text/javascript" src="${ctx }/js/echarts.min.js"></script>
<script type="text/javascript" src="${ctx }/view/common/space_common.js"></script>
<script type="text/javascript" src="${ctx }/view/bizfly/cesiumView.js"></script>
<style type="text/css">
.specialColor{
	color: red !important;
}
</style>
<script type="text/javascript">
	var uavDataList = [];
	//解析无人机轨迹
	var uavAllGuijiArry = [];
	<c:forEach var="list" items="${uavDataList}" varStatus="j">
		var uavGuijiArry = [];
		<c:forEach var="item" items="${list}" varStatus="i">
			uavDataList.push({
				lng: "${item.lng}",
				lat: "${item.lat}",
				hei: "${item.hei}",
				spe: "${item.spe}",
				time: "${item.time}",
				className: "${(j.index!=0 && i.index==0) ? 'specialColor' : ''}"
			});
			uavGuijiArry.push('${item.lng}'+","+'${item.lat}'+","+'${item.hei}');
		</c:forEach>
		uavAllGuijiArry.push(uavGuijiArry);
	</c:forEach>
	//解析计划空域
	var planSpaceArry = [];
	<c:forEach var="space" items="${bizSpaceReq.map.spaceList}">
		planSpaceArry.push('${space.polygonWgs84}');
	</c:forEach>
	$(function(){
		$("#currContentDiv").css("left",((parent.parent.getWindowSize()[1]-$("#left",parent.document).width()-330)/2));
		//画曲线图
		drawEcharts();
		$("#mapIframe").css({"height" : parent.parent.getWindowSize()[0]});//使地图可以全屏显示
		//当窗口变化时重置地图大小
		$(window).resize(function() {
			$("#currContentDiv").css("left",((parent.parent.getWindowSize()[1]-$("#left",parent.document).width()-330)/2));
			$("#mapIframe").css({"height" : parent.parent.getWindowSize()[0]});//使地图可以全屏显示
		});
		//iframe加载完成后执行的方法
		$("#mapIframe").load(function(){
			
			setTimeout(function(){
				var lineWidth = 3;
				//画线
				if(uavAllGuijiArry.length>0){
					for(var j=0;j<uavAllGuijiArry.length;j++){
						var uavGuijiArry = uavAllGuijiArry[j];
						//解析无人机轨迹
						var jwdWgs84 = "";
						for(var i=0;i<uavGuijiArry.length;i++){
							if(i>0){
								jwdWgs84 += "|" ;
							}
							jwdWgs84 += uavGuijiArry[i];

							var pointSize = 4;
							// 中间点
							var firstUav = uavGuijiArry[i].split(",");
							var html = "<i style='width:"+pointSize+"px; height:"+pointSize+"px; line-height:"+pointSize+"px;" +
							"border-radius:50%;background-color:#00B8FF;"+
							"display: block;font-style:unset;color:white;text-align:center;'></i>";
							var pointOption = {"width":pointSize,"height":pointSize,"showLabel":"false","className":"zIndex400"};
							mapIframe.drawClickMarker(firstUav[0],firstUav[1],{html:html}, pointOption, null);

						}
						//画无人机轨迹
						mapIframe.drawLine(jwdWgs84, lineWidth);

						var pointSize = 30;
						if(j==0){
							// 起点
							var firstUav = uavGuijiArry[0].split(",");
							var html = "<i style='width:"+pointSize+"px; height:"+pointSize+"px; line-height:"+pointSize+"px;" +
							"border-radius:50%;background-color:#4a02de;"+
							"display: block;font-style:unset;color:white;text-align:center;'>起</i>";
							var pointOption = {"width":pointSize,"height":pointSize,"showLabel":"false","className":"zIndex800"};
							//画起点
							mapIframe.drawClickMarker(firstUav[0],firstUav[1],{html:html}, pointOption, null);
						}
						if(j == uavAllGuijiArry.length-1){
							// 终点
							var firstUav = uavGuijiArry[uavGuijiArry.length-1].split(",");
							var html = "<i style='width:"+pointSize+"px; height:"+pointSize+"px; line-height:"+pointSize+"px;" +
							"border-radius:50%;background-color:#0035FF;"+
							"display: block;font-style:unset;color:white;text-align:center;'>止</i>";
							var pointOption = {"width":pointSize,"height":pointSize,"showLabel":"false","className":"zIndex500"};
							//画起点
							mapIframe.drawClickMarker(firstUav[0],firstUav[1],{html:html}, pointOption, null);
						}
					}
					if(uavAllGuijiArry.length>1){
						var dashJwdArry = [];
						for(var j=0;j<uavAllGuijiArry.length-1;j++){
							var uavGuijiArry = uavAllGuijiArry[j];
							var nextUavGuijiArry = uavAllGuijiArry[j+1];
							var jwdArry = [uavGuijiArry[uavGuijiArry.length-1], nextUavGuijiArry[0]];
							dashJwdArry.push(jwdArry);
						}
						if(dashJwdArry.length>0){
							for(var i=0;i<dashJwdArry.length;i++){
								// 画虚线
								mapIframe.drawDashLine(dashJwdArry[i].join("|"), lineWidth,"red");
								var firstJwd = dashJwdArry[i][0];
								var endJwd = dashJwdArry[i][1];
								// 画点
								var pointSize = 5;
								var html = "<i style='width:"+pointSize+"px; height:"+pointSize+"px; line-height:"+pointSize+"px;" +
								"border-radius:50%;background-color:red;display: block;'></i>";
								var pointOption={"width":pointSize,"height":pointSize,"showLabel":"false","className":"zIndex1000"};
								mapIframe.drawClickMarker(firstJwd.split(",")[0],firstJwd.split(",")[1],{html:html}, pointOption, null);
								mapIframe.drawClickMarker(endJwd.split(",")[0],endJwd.split(",")[1],{html:html}, pointOption, null);
							}
						}
					}
				}
				
				//画计划空域
				for(var i=0;i<planSpaceArry.length;i++){
					var polygonWgs84s = planSpaceArry[i].split(';');
					for(var k=0;k<polygonWgs84s.length;k++){
						mapIframe.drawCustomColorPolygonNoClear(polygonWgs84s[k],'#00FF00','#00FF00');
					}
				}
				mapIframe.positionSpace();//根据所画的图形定位和缩放地图
			},100);//之所以是要延迟，是因为需要等待地图全部显示出来后再赋值，这样才能实现根据覆盖物定位
		});
		
		var vm = new Vue({
			el:'#app',
			data:{
				list:uavDataList
			},
			mounted: function(){
				$("#listDiv").css({"max-height": (parent.parent.getWindowSize()[0] - 330)});
			},
			filters:{
				timeFilter: function(value){
					if(value == null || value == ""){
						return "";
					}
	                return value.split(" ")[1].split(".")[0];
				}
			}
		});
	});
	function drawEcharts(){
	    var echartsDiv=echarts.init(document.getElementById('echartsDiv'));
	    
	    var dateList = uavDataList.map(function(item){
	        return item.time;
	    });
	    var heiList = uavDataList.map(function(item){
	        return item.hei;
	    });
	    var speList = uavDataList.map(function(item){
	    	return item.spe;
	    });
	    
	    var option = {
	   		tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'cross',
		            animation: false,
		            label: {
		                backgroundColor: '#505765'
		            }
		        }
		    },
		    grid: {
		    	bottom: 25,
		    	right: 40
		    },
		    legend: {
		        data: ['高度', '速度'],
		        right: 10,
		    	top: 5
		    },
		    xAxis: [
		        {
		            type: 'category',
		            data: dateList.map(function (str) {
		                return str.split(" ")[1].split(".")[0];
		            })
		        }
		    ],
		    yAxis: [
		        {
		            name: '高度(m)',
		            type: 'value'
		        },
		        {
		            name: '速度(m/s)',
		            type: 'value'
		        }
		    ],
		    series: [
		        {
		            name: '高度',
		            type: 'line',
		            smooth: true,
		            data: heiList
		        },
		        {
		            name: '速度',
		            type: 'line',
		            yAxisIndex: 1,
		            smooth: true,
		            data: speList
		        }
		    ]
		};
	    echartsDiv.setOption(option);
	}
</script>
</head>
<body style="position: relative; overflow: hidden;">
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
	<div id="buttonDiv" class="row" style="width:100%;margin-left: 0; position: absolute; text-align: center;">
		<div onclick="parent.closeDetailLayer()" 
			class="right_content_btnbox_btn right_content_btnbox_return " style="float: left;">
			<img src="${pageContext.request.contextPath }/images/return_btn.png" />
			<span>返回</span>
		</div>
		<span id="content" style="font-size: large; font-family: serif; color: #4174CD; height: 30px; line-height: 30px;">
			${bizFly.sn }&nbsp;飞行时间：${bizFly.begTime } 至 ${bizFly.endTime == null || bizFly.endTime =='' ? '至今' : bizFly.endTime}
		</span>
	</div>
	<div class="btn-group" style="position: absolute; top: 5px; left: 90px; ">
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" style="padding-bottom: 0px;">
				空域&nbsp;
				<span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><input type="checkbox" id="allSpaceType">&nbsp;全部</li>
				<div id="infoSpaceTypeDiv"></div>
			</ul>
		</div>
	</div>
	<iframe src="${pageContext.request.contextPath }/leaflet/leaflet_map.jsp" name="mapIframe" id="mapIframe" width="100%" height="100%" frameborder="0" ></iframe>
	<div style="position: absolute; top: 35px; left: 5px;">
		<div id="app" class="right_content_all" style="border: unset;">
			<template>
				<%--列表展示--%>
				<div id="listDiv" class="right_content_table" style="width: 400px; overflow-y: auto; padding: 0px;">
					<table class="table table-bordered table_list table-hover table-striped">
						<thead>
							<tr>
								<th>时间</th>
								<th>经度</th>
								<th>纬度</th>
								<th>高度</th>
								<th>速度</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="item in list">
								<td v-bind:class="item.className">{{item.time | timeFilter}}</td>
								<td v-bind:class="item.className">{{item.lng}}</td>
								<td v-bind:class="item.className">{{item.lat}}</td>
								<td v-bind:class="item.className">{{item.hei}}</td>
								<td v-bind:class="item.className">{{item.spe}}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</template>
		</div>
		<div id="echartsDiv" style="width: 400px; height: 180px; background: #f5f5f5; margin-top: 5px;"></div>
	</div>
</body>
</html>