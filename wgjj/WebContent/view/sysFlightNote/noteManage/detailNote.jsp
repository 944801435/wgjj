<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
<title>照会信息详情页</title>
<%@ include file="../../tool.jsp"%>
<script type="text/javascript" src="${ctx }/js/validate.js"></script>
<script type="text/javascript" src="${ctx }/view/component/allComponent.js"></script>
<style type="text/css">
.block {
	border: 1px solid #F1F1F1;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 5px;
	padding-bottom: 5px;
	background: #fff;
}

a {
	color: #08c;
	text-decoration: underline;
}

a:hover {
	color: #005580;
	text-decoration: underline;
}

hr {
	margin: 5px 0;
}

.form-horizontal .control-label {
	width: 140px;
}

.form-horizontal .controls {
	/*width: 180px;*/
	margin-left: 150px;
}

input[type="text"] {
	width: 140px;
}

.table_list tr td {
	text-align: center;
}
/**
弹出框样式
 */
.open_table .control-group {
	float: left;
	padding-bottom: 0;
	margin-bottom: 10px;
}

.open_table .control-label {
	float: left;
	width: 75px;
	margin-top: 5px;
}

.open_table .controls {
	float: left;
}
</style>
</head>
<body>
	<div id="app" class="right_content">
		<template>
		<div class="right_content_all">
			<div class="right_content_all_top my-collapse" :href="'#'+panelId1">
				<span>飞行资料信息</span>
			</div>
			<form id="inputForm" method="post" enctype="multipart/form-data" class="form-horizontal" style="margin: 0;">
				<div :id="panelId1" class="right_content_table">
					<table class="table" style="width: 100%;">
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">来电来函单位：</label>
									<div class="controls">
									<input id="noteId" name="noteId"
											v-model="notePlanInfo.noteId" type="hidden" class="required"
											value="" />
										<input readonly="readonly" id="letterUnit" name="letterUnit"
											v-model="notePlanInfo.letterUnit" autocomplete="off"
											type="text" dataType="Require,Limit" len="50"
											msg="请输入(1~50)个字符的来电来函单位！" maxlength="50" class="required"
											value="" />
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">姓名：</label>
									<div class="controls">
										<input readonly="readonly" id="personName" name="personName"
											v-model="notePlanInfo.personName" autocomplete="off"
											type="text" dataType="Require,Limit" len="100"
											msg="请输入(1~100)个字符的联系人！" maxlength="100" class="required"
											value="" />
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">电话：</label>
									<div class="controls">
										<input readonly="readonly" id="telNo" name="telNo" v-model="notePlanInfo.telNo"
											autocomplete="off" type="text" dataType="Require,Limit"
											len="20" msg="请输入(1~20)个字符的电话！" maxlength="20"
											class="required" value="" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">国家：</label>
									<div class="controls">
										<input readonly="readonly" id="nationality" name="nationality"
											v-model="notePlanInfo.nationality" autocomplete="off"
											type="text" dataType="Require,Limit" len="50"
											msg="请输入(1~50)个字符的国家！" maxlength="50" class="required"
											value="" />
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">照会号：</label>
									<div class="controls">
										<input readonly="readonly" id="noteNo" name="noteNo" v-model="notePlanInfo.noteNo"
											autocomplete="off" type="text" dataType="Require,Limit"
											len="50" msg="请输入(1~50)个字符的照会号！" maxlength="50"
											class="required" value="" />
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">营运方：</label>
									<div class="controls">
										<input readonly="readonly" id="operator" name="operator"
											v-model="notePlanInfo.operator" autocomplete="off"
											type="text" dataType="Require,Limit" len="50"
											msg="请输入(1~50)个字符的营运方！" maxlength="50" class="required"
											value="" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">架数：</label>
									<div class="controls">
										<input readonly="readonly" style="width: 140px;" placeholder="请输入数字"
											id="airNumber" name="airNumber"
											v-model="notePlanInfo.airNumber" autocomplete="off"
											type="number" dataType="Require,Limit" len="11"
											msg="请输入11位数字的架数！" maxlength="11" class="required" value="" />
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">机型：</label>
									<div class="controls">
										<input readonly="readonly" id="model" name="model" v-model="notePlanInfo.model"
											autocomplete="off" type="text" dataType="Require,Limit"
											len="200" msg="请输入(1~200)个字符的机型！" maxlength="200"
											class="required" value="" />
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">机组人数：</label>
									<div class="controls">
										<input readonly="readonly" style="width: 140px;" placeholder="请输入数字"
											id="personNumber" name="personNumber"
											v-model="notePlanInfo.personNumber" autocomplete="off"
											type="number" dataType="Require,Limit" len="11"
											msg="请输入11位数字的架数！" maxlength="11" class="required" value="" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">呼号：</label>
									<div class="controls">
										<input readonly="readonly" id="callNumber" name="callNumber"
											v-model="notePlanInfo.callNumber" autocomplete="off"
											type="text" dataType="Require,Limit" len="100"
											msg="请输入(1~100)个字符的联系人！" maxlength="100" class="required"
											value="" />
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">注册号：</label>
									<div class="controls">
										<input readonly="readonly" id="regNo" name="regNo" v-model="notePlanInfo.regNo"
											autocomplete="off" type="text" dataType="Require,Limit"
											len="20" msg="请输入(1~20)个字符的电话！" maxlength="20"
											class="required" value="" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div class="control-group">
									<label class="control-label">任务目的：</label>
									<div class="controls">
										<input readonly="readonly" style="width: 93%;" id="mission" name="mission"
											v-model="notePlanInfo.mission" autocomplete="off" type="text"
											dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的任务目的！"
											maxlength="100" class="required" value="" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div class="control-group">
									<label class="control-label">其他：</label>
									<div class="controls">
										<input readonly="readonly" style="width: 93%;" id="other" name="other"
											v-model="notePlanInfo.other" autocomplete="off" type="text"
											dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的任务目的！"
											maxlength="100" class="required" value="" />
									</div>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		<div class="right_content_all">
			<div class="right_content_all_top my-collapse" :href="'#'+panelId2">
				<span>照会文书文件</span>
			</div>
			<div :id="panelId2" class="right_content_table">
				<table class="table table-bordered table_list flightTable">
					<tr>
						<td width="18%">文件名</td>
						<td width="18%">文件大小</td>
						<td width="18%">上传时间</td>
					</tr>
					<tr v-for="item in noteFilesList">
						<td>{{item.fileNameCn }}</td>
						<td>{{item.fileSize }}<span>MB</span></td>
						<td>{{item.createTime }}</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="right_content_all">
			<div class="right_content_all_top my-collapse" :href="'#'+panelId3">
				<span>飞行计划</span>
			</div>
			<div :id="panelId3" class="right_content_table">
				<table class="table table-bordered table_list flightTable">
					<tr>
						<td width="10%">飞行日期</td>
						<td width="9%">起飞机场</td>
						<td width="9%">降落机场</td>
						<td width="9%">入境点</td>
						<td width="9%">出境点</td>
						<td width="19%">计划航线</td>
						<td width="19%">备用航线</td>
					</tr>
					<tr v-for="item in flightList">
						<td>{{item.flightTime }}</td>
						<td>{{item.departureAirport }}</td>
						<td>{{item.landAirport }}</td>
						<td>{{item.entryName }}</td>
						<td>{{item.exitName }}</td>
						<td style="text-align: left; padding-left: 3px !important;">{{item.planRoute
							}}</td>
						<td style="text-align: left; padding-left: 3px !important;">{{item.alternateRoute
							}}</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="right_content_btnbox ">
			<div
				onclick="window.location.href='${ctx}/noteInfoManageList.action'"
				style="cursor: pointer;"
				class="right_content_btnbox_btn right_content_btnbox_return">
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
			notePlanInfo: {},
			flight: {},
			flightList: [],
			noteFilesList: [],
			panelId1: new Date().getTime()+"_1",
			panelId2: new Date().getTime()+"_2",
			panelId3: new Date().getTime()+"_3",
			noteIdAc:null
		},
		mounted: function(){
			this.init();
		},
		methods: {
			init(){
				noteIdAc  = "${noteIdAc}";
				$.ajax({
					url: ctx + "/detail_plan_flight_info.action",
					data: {
						noteId: noteIdAc,
					},
					type: "post",
					dataType: "json",
					success: (data)=>{
						if(data.errCode!='1'){
							alert(data.errMsg);
							return;
						}
						this.notePlanInfo = data.data.notePlanInfo;
						this.noteFilesList = data.data.noteFilesList;
						this.flightList = data.data.flightList;
					}
				});
			},
		}
	});
	
</script>
</html>