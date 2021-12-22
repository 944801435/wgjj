<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>飞行计划上报</title>
<%@ include file="../tool.jsp"%>
<script type="text/javascript" src="${ctx }/js/validate.js"></script>
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
				<!-- 飞行航线 -->
				<form id="inputForm" method="post" enctype="multipart/form-data" class="form-horizontal" style="margin: 0;">
					<flight :flightlist="planFlightList" title="飞行航线" updatesimple="true"></flight>
				</form>
			</div>
			<div class="right_content_btnbox">
				<div @click="preview()" style="cursor:pointer;"
					 class="right_content_btnbox_btn right_content_btnbox_save">
					<i class="fa fa-eye"></i> <span>预览</span>
				</div>
				<div @click="goSave()" style="cursor:pointer;"
					 class="right_content_btnbox_btn right_content_btnbox_save">
					<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
				</div>
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
	var vm = new Vue({
		el: "#app",
		data: {
			ctx: ctx,
			planSeq: parent.vm.selNoteSeq,
			note: {},
			flightList: [],
			caacList: [],
			oldPlanFlightList: [],
			hisPlanList: [],
			planFlightList: [],
			noteFileList: []
		},
		mounted: function(){
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
		watch: {
			planFlightList: function(nval){
				this.$nextTick(function(){
					Validator.Validate_onLoad($("#inputForm")[0],3);
				});
			}
		},
		methods: {
			goSave() {
				Confirm('确认上报该飞行计划吗？',()=>{
					var myForm=document.getElementById("inputForm");
					if(Validator.Validate(myForm,3)){
						$("#inputForm").ajaxSubmit({
							url:ctx+'/flyPlan/commit.action',
							type:'post',
							data: {
								planSeq: parent.vm.selNoteSeq
							},
							dataType:'json',
							success:(data)=>{
								if(data.errCode=='1'){
									parent.vm.init();
									parent.closeDetailLayer();
								}
								parent.showMsg(data.errCode,data.errMsg);
							}
						});
					}
				});
			},
			preview(planSeq){
				// vm.planSeq = planSeq;
				layerIndex=layer.open({
					type:2,
					title:'飞行计划详情',
					content: ctx + '/view/flyPlan/preview.jsp',
					shadeClose: true,    //开启遮罩关闭
					shade:false,
					area:['800px','500px'],
					maxmin:false,
					title:false, //标题不显示
					closeBtn:0,// 不显示关闭按钮
					isOutAnim: false,// 关闭关闭图层动画效果
					anim: -1,// 关闭打开图层动画效果
					success:function(layero,index){
					}
				});
				layer.full(layerIndex);
			},
			viewFile(){
				layer.open({
					type:2,
					title:'飞行计划扫描件',
					content: "${ctx}/view/component/viewFile.jsp",
					shadeClose: false,    //开启遮罩关闭
					shade: false,
					area:['800px','500px'],
					maxmin: true,
					//title: true, //标题不显示
					closeBtn: 1,// 不显示关闭按钮
					isOutAnim: false,// 关闭关闭图层动画效果
					anim: -1,// 关闭打开图层动画效果
					success:function(layero,index){
					}
				});
			}
		}
	});
	function closeDetailLayer(){
		if(layerIndex!=null){
			layer.close(layerIndex);
			layerIndex = null;
		}
	}
</script>
</html>