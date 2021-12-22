<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>照会文书导入</title>
<%@ include file="../tool.jsp"%>
<link rel="stylesheet" href="${ctx }/css/layui/css/layui.css">
<script type="text/javascript" src="${ctx }/css/layui/layui.js"></script>
<script type="text/javascript" src="${ctx }/js/validate.js"></script>
<script type="text/javascript" src="${ctx }/view/component/allComponent.js"></script>
<style type="text/css">
a{
	color: #08c;
    text-decoration: underline;
}
a:hover {
    color: #005580;
    text-decoration: underline;
}
</style>
</head>
<body>
	<div id="app" class="right_content">
		<template>
			<div v-show="!showDetail" class="right_content_all">
				<div class="right_content_all_top">
					<span>导入压缩包</span>
				</div>
				<div class="right_content_table">
					<form id="saveForm" method="post" enctype="multipart/form-data" style="display: inline-flex;">
						<div>
							<input style="width:180px;" dataType="Require" msg="请选择要导入的外交照会压缩包！" type="file" id="file" name="file"/>
						</div>
						<div @click="upload()" class="right_content_btnbox_btn right_content_btnbox_add">
							<span>导入</span>
						</div>
					</form>
				</div>
			</div>
			<div v-show="showDetail" class="right_content_all">
				<note-detail :note="note"></note-detail>
				<div class="right_content_all_top">
					<span>飞行计划</span>
				</div>
				<div class="right_content_table">
					<p>
						{{note.flightBodys}}
					</p>
				</div>
			</div>
			<div class="right_content_btnbox">
				<div onclick="parent.closeDetailLayer()" 
					class="right_content_btnbox_btn right_content_btnbox_return " style="height: 20px;float: right;">
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
			showDetail: false,
			note: {}
		},
		mounted: function(){
		},
		methods: {
			upload: function(){
				if(!Validator.Validate($('#saveForm')[0],3)){
					return;
				}
				openLoading();
				$("#saveForm").ajaxSubmit({
					url:ctx+'/note/upload.action',
					type:'post',
					dataType:'json',
					success:(data)=>{
						if(data.errCode=='1'){
							vm.note = data.data;
							vm.showDetail = true;
							parent.vm.init();
						}
						showMsg(data.errCode,data.errMsg);
						closeLoading();
					}
				});
			}
		}
	});
	
</script>
</html>