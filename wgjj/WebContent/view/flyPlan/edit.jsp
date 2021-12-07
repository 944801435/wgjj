<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>飞行计划编辑</title>
<%@ include file="../tool.jsp"%>
<script type="text/javascript" src="${ctx }/view/component/allComponent.js"></script>
</head>
<body>
	<div id="app" class="right_content">
		<template>
			<div class="right_content_all">
				<!-- 飞行计划基础信息 -->
				<note-detail :note="note"></note-detail>
			</div>
			<div class="right_content_all">
				<!-- 照会文书原始航线 -->
				<flight :flightlist="flightList" title="照会文书原始航线"></flight>
			</div>
			<div class="right_content_all" v-for="item in caacList">
				<!-- 民航调整航线 -->
				<flight :caac="item" :flightlist="item.flightList" title="民航调整航线"></flight>
			</div>
			<div class="right_content_all">
				<!-- 历史计划航线 -->
				<flight :flightlist="oldPlanFlightList" title="历史计划航线"></flight>
			</div>
			<div class="right_content_all">
				<!-- DD调整航线 -->
				<flight :flightlist="planFlightList" title="飞行计划航线" updateflight="true" @turn="update"></flight>
				<div id="mapDiv" style="display: none; height: 100%;">
					<view-map style="height:80%;"></view-map>
					计划航路：<textarea v-model="flightBody" rows="3" style="width: 80%;"></textarea>
					<a href="javascript:void(0);" @click="showFlight()">显示</a>
					<a href="javascript:void(0);" @click="saveFlight()">保存</a>
				</div>
			</div>
			<div class="right_content_btnbox">
				<div onclick="parent.closeDetailLayer()" 
					class="right_content_btnbox_btn right_content_btnbox_return " style="float: right;">
					<img src="${pageContext.request.contextPath }/images/return_btn.png" />
					<span>返回</span>
				</div>
			</div>
		</template>
	</div>
</body>
<script type="text/javascript">
	var tempFlightLine = null;
	var layerIndex = null;
	var vm = new Vue({
		el: "#app",
		data: {
			ctx: ctx,
			note: {},
			flightList: [],
			caacList: [],
			oldPlanFlightList: [],
			hisPlanList: [],
			planFlightList: [],
			noteFileList: [],
			flightObj: {},
			flightBody: ''
		},
		mounted: function(){
			this.init();
		},
		methods: {
			init(){
				$.ajax({
					url: ctx + "/flyPlan/detail.action",
					data: {
						planSeq: parent.vm.selNoteSeq
					},
					type: "post",
					dataType: "json",
					success: (data)=>{
						if(data.errCode!='1'){
							alert(data.errMsg);
							return;
						}
						this.note = data.data.note;
						this.flightList = data.data.noteFlightList;
						this.caacList = data.data.caacList;
						this.oldPlanFlightList = data.data.oldPlanFlightList;
						this.hisPlanList = data.data.hisPlanList;
						this.planFlightList = data.data.planFlightList;
						this.noteFileList = data.data.noteFileList;
					}
				});
			},
			update(flightObj){
				this.flightObj = flightObj;
				vm.flightBody = flightObj.flightBody
				layerIndex = layer.open({
					type:1,
					title:'调整计划航线',
					content: $("#mapDiv"),
					shadeClose: false,    //开启遮罩关闭
					shade: false,
					area:['800px','500px'],
					maxmin: true,
					//title: true, //标题不显示
					closeBtn: 1,// 不显示关闭按钮
					isOutAnim: false,// 关闭关闭图层动画效果
					anim: -1,// 关闭打开图层动画效果
					success:function(layero,index){
						$("#mapIframe").load(function(){
							// 画航路
							if(tempFlightLine!=null){
								removeOverlay(tempFlightLine);
								tempFlightLine = null;
							}
							var wayList = vm.flightObj.wayList;
							if(wayList!=null && wayList.length>0){
								tempFlightLine = drawFlightLine(wayList, "blue");
								positionOverlay(tempFlightLine);
							}
						});
					}
				});
			},
			showFlight(){
				Vue.delete(this.flightObj,'wayList');
				let params = Object.assign({}, this.flightObj);
				params.flightBody = vm.flightBody;
				$.ajax({
					url: ctx + "/flyPlan/getFlightWayList.action",
					data: params,
					type: "post",
					dataType: "json",
					success: (data)=>{
						if(data.errCode!='1'){
							alert(data.errMsg);
							return;
						}
						if(tempFlightLine!=null){
							removeOverlay(tempFlightLine);
							tempFlightLine = null;
						}
						// 画航路
						var wayList = data.data;
						if(wayList!=null && wayList.length>0){
							tempFlightLine = drawFlightLine(wayList, "blue");
							positionOverlay(tempFlightLine);
						}
					}
				});
			},
			saveFlight(){
				Confirm('确认保存该飞行计划航路吗？',()=>{
					Vue.delete(vm.flightObj,'wayList');
					let params = Object.assign({}, vm.flightObj);
					params.flightBody = vm.flightBody;
					$.ajax({
						url: ctx + "/flyPlan/update.action",
						data: params,
						type: "post",
						dataType: "json",
						success: (data)=>{
							alert(data.errMsg);
							if(data.errCode!='1'){
								return;
							}
							if(tempFlightLine!=null){
								removeOverlay(tempFlightLine);
								tempFlightLine = null;
							}
							layer.close(layerIndex);
							vm.init();
						}
					});
				});
			}
		},
		watch: {
		}
	});
	
</script>
</html>