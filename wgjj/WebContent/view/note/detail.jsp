<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>照会文书详情</title>
<%@ include file="../tool.jsp"%>
<link rel="stylesheet" href="${ctx }/css/layui/css/layui.css">
<script type="text/javascript" src="${ctx }/css/layui/layui.js"></script>
<script type="text/javascript" src="${ctx }/js/validate.js"></script>
<script type="text/javascript" src="${ctx }/view/component/allComponent.js"></script>
<style type="text/css">
.block{
	border:1px solid #F1F1F1;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 5px;
	padding-bottom: 5px;
	background: #fff;
}
a{
	color: #08c;
    text-decoration: underline;
}
a:hover {
    color: #005580;
    text-decoration: underline;
}
hr{
	margin: 5px 0;
}
</style>
</head>
<body style="background-color: #F7F8F9;">
	<div id="app">
		<template>
			<div class="row" style="width:100%;margin-left: 0;">
				<div onclick="parent.closeDetailLayer()" 
					class="right_content_btnbox_btn right_content_btnbox_return " style="height: 20px;float: left;">
					<img src="${pageContext.request.contextPath }/images/return_btn.png" />
					<span>返回</span>
				</div>
			</div>
			<div class="row" style="margin-top: 5px;width:100%;margin-left: -10px;display: inline-flex;">
				<div id="col01" class="span4" style="width: 34%;">
					<!-- 照会文书基础信息 -->
					<note-detail :note="note"></note-detail>
				</div>
				<div id="col02" class="span4" style="width: 33%;">
					<!-- 照会文书原始航线 -->
					<flight :flightlist="flightList" title="照会文书原始航线 "></flight>
				</div>
				<div id="col03" class="span4" style="width: 33%;">
					<!-- 照会文书扫描件 -->
					<view-file :filelist="noteFileList" title="照会文书扫描件"></view-file>
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
		}
	});
	
</script>
</html>