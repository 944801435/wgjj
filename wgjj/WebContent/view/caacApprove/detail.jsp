<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>民航意见详情</title>
<%@ include file="../tool.jsp"%>
<script type="text/javascript" src="${ctx }/view/component/allComponent.js"></script>
<style type="text/css">
</style>
</head>
<body>
	<div id="app">
		<template>
			<div class="right_content_all">
				<!-- 照会文书基础信息 -->
				<note-detail :note="note"></note-detail>
			</div>
			<div class="right_content_all">
				<!-- 照会文书原始航线 -->
				<flight :flightlist="noteFlightList" title="照会文书原始航线"></flight>
			</div>
			<div class="right_content_all">
				<!-- 民航调整航线 -->
				<flight :caac="caac" :flightlist="caacFlightList" title="民航调整航线"></flight>
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
			noteFlightList: [],
			noteFileList: [],
			caac: {},
			caacFlightList: [],
			caacFileList: []
		},
		mounted: function(){
			$.ajax({
				url: ctx + "/caacApprove/detail.action",
				data: {
					caSeq: parent.vm.selCaSeq
				},
				type: "post",
				dataType: "json",
				success: (data)=>{
					if(data.errCode!='1'){
						alert(data.errMsg);
						return;
					}
					this.note = data.data.note;
					this.noteFlightList = data.data.noteFlightList;
					this.noteFileList = data.data.noteFileList;
					this.caac = data.data.caac;
					this.caacFlightList = data.data.caacFlightList;
					this.caacFileList = data.data.caacFileList;
				}
			});
		}
	});
	
</script>
</html>