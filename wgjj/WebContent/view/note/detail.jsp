<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>照会文书详情</title>
<%@ include file="../tool.jsp"%>
<script type="text/javascript" src="${ctx }/view/component/allComponent.js"></script>
</head>
<body>
	<div id="app" class="right_content">
		<template>
			<div class="right_content_all">
				<!-- 照会文书基础信息 -->
				<note-detail :note="note"></note-detail>
			</div>
			<div class="right_content_all">
				<!-- 照会文书原始航线 -->
				<flight :flightlist="flightList" title="照会文书原始航线"></flight>
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
	var vm = new Vue({
		el: "#app",
		data: {
			ctx: ctx,
			note: {},
			flightList: [],
			noteFileList: []
		},
		mounted: function(){
			$.ajax({
				url: ctx + "/note/detail.action",
				data: {
					noteSeq: parent.vm.selNoteSeq
				},
				type: "post",
				dataType: "json",
				success: (data)=>{
					if(data.errCode!='1'){
						alert(data.errMsg);
						return;
					}
					this.note = data.data.note;
					this.flightList = data.data.flightList;
					this.noteFileList = data.data.noteFileList;
				}
			});
		},
		methods: {
			viewFile(){
				layer.open({
					type:2,
					title:'照会文书扫描件',
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
	
</script>
</html>