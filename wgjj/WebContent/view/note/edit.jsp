<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>照会文书编辑</title>
<%@ include file="../tool.jsp"%>
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
.form-horizontal .control-label{
	width: 140px;
}
.form-horizontal .controls{
	/*width: 180px;*/
	margin-left: 150px;
}
input[type="text"]{
	width: 140px;
}
.table_list tr td{
	text-align: center;
}
/**
弹出框样式
 */
.open_table .control-group{
	float: left;
	padding-bottom:0;
	margin-bottom:10px;
}
.open_table .control-label{
	float: left;
	width: 75px;
	margin-top: 5px;
}
.open_table .controls{
	float: left;
}
</style>
</head>
<body>
<div id="app" class="right_content">
	<template>
		<div class="right_content_all">
			<div class="right_content_all_top my-collapse" :href="'#'+panelId1">
				<span>基本信息</span>
			</div>
			<form id="inputForm" class="form-horizontal" style="margin: 0;">
				<div :id="panelId1" class="right_content_table">
					<table style="width: 100%;">
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">照会编号：</label>
									<div class="controls">
										<input readonly="readonly" id="noteSeq" type="text" name="noteSeq" v-model="note.noteSeq"/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">国家：</label>
									<div class="controls">
										<input id="nationality" name="nationality" v-model="note.nationality" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的国家！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">照会号：</label>
									<div class="controls">
										<input id="noteNo" name="noteNo" v-model="note.noteNo" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的照会号！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">营运方：</label>
									<div class="controls">
										<input id="operator" name="operator" v-model="note.operator" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的营运方！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">机型：</label>
									<div class="controls">
										<input id="model" name="model" v-model="note.model" autocomplete="off" type="text" dataType="Require,Limit" len="200" msg="请输入(1~200)个字符的机型！" maxlength="200" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">架数：</label>
									<div class="controls">
										<input id="planeNumber" name="planeNumber" v-model="note.planeNumber" autocomplete="off" type="text" dataType="Require,Limit" len="11" msg="请输入11位数字的架数！" maxlength="11" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">呼号：</label>
									<div class="controls">
										<input id="callSign" name="callSign" v-model="note.callSign" autocomplete="off" type="text" dataType="Require,Limit" len="500" msg="请输入(1~500)个字符的呼号！" maxlength="500" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">注册号：</label>
									<div class="controls">
										<input id="regNo" name="regNo" v-model="note.regNo" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的注册号！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">机组人数：</label>
									<div class="controls">
										<input id="personNumber" name="personNumber" v-model="note.personNumber" autocomplete="off" type="text" dataType="Require,Limit" len="11" msg="请输入11位数字的机组人数！" maxlength="11" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">来函单位：</label>
									<div class="controls">
										<input id="letterUnit" name="letterUnit" v-model="note.letterUnit" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的来函单位！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">联系人：</label>
									<div class="controls">
										<input id="personName" name="personName" v-model="note.personName" autocomplete="off" type="text" dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的联系人！" maxlength="100" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">电话：</label>
									<div class="controls">
										<input id="telNo" name="telNo" v-model="note.telNo" autocomplete="off" type="text" dataType="Require,Limit" len="20" msg="请输入(1~20)个字符的电话！" maxlength="20" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div class="control-group">
									<label class="control-label">任务目的：</label>
									<div class="controls">
										<input style="width: 93%;" id="mission" name="mission" v-model="note.mission" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的任务目的！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">文书扫描件：</label>
									<div class="controls" style="margin-top: 3px;">
										<a :href="'${ctx}/download.action?fileId='+note.noteZipFileId">下载</a>
										<a @click="viewFile">查看</a>
										<div id="fileDiv" style="display: none;">
											<view-file :filelist="noteFileList"></view-file>
										</div>
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
				<span>飞行计划</span>
			</div>
			<div :id="panelId2" class="right_content_table">
				<table>
					<tr>
						<td>
							<div>
								<textarea name="flightBodys" rows="4" v-model="note.flightBodys" style="min-width:1000px;overflow: auto;word-break: break-all;"></textarea>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="right_content_all">
			<div class="right_content_all_top my-collapse" :href="'#'+panelId3">
				<span>照会文书原始航线</span>
			</div>
			<div :id="panelId3" class="right_content_table">
				<table class="table table-bordered table_list flightTable">
					<tr>
						<td colspan="8"><a @click="addFlight()" style="float: right;margin-right: 10px;">添加航线</a></td>
					</tr>
					<tr>
						<td width="8%">飞行日期</td>
						<td width="10%">其他日期</td>
						<td width="8%">起飞机场</td>
						<td width="8%">降落机场</td>
						<td width="6%">入境点</td>
						<td width="6%">出境点</td>
						<td>航路</td>
						<td width="8%">操作</td>
					</tr>
					<tr v-for="item in flightList">
						<td>{{item.planDate }}</td>
						<td>{{item.otherDate }}</td>
						<td>{{item.upAirport }}</td>
						<td>{{item.downAirport }}</td>
						<td>{{item.inPointIdent }}</td>
						<td>{{item.outPointIdent }}</td>
						<td style="text-align: left; padding-left: 3px !important;">{{item.flightBody }}</td>
						<td>
							<a @click="editFlight(item)">编辑</a>
							<a @click="delFlight(item)">删除</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="right_content_btnbox ">
			<div @click="goSave()" style="cursor:pointer;"
				 class="right_content_btnbox_btn right_content_btnbox_save">
				<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
			</div>
			<div onclick="parent.closeDetailLayer()" style="cursor:pointer;"
				 class="right_content_btnbox_btn right_content_btnbox_return">
				<img src="${pageContext.request.contextPath }/images/return_btn.png" />
				<span>返回</span>
			</div>
		</div>
		<form id="saveForm" :model="flight" hidden="hidden">
			<div>
				<div class="right_content_all">
					<div class="right_content_table">
						<table class="open_table">
							<tr>
								<td>
									<div class="control-group">
										<label class="control-label">飞行日期：</label>
										<div class="controls">
											<input id="planDate" name="planDate" type="text" readonly="readonly" v-model="flight.planDate" v-selected dateFormat="yyyy-MM-dd" dataType="Require" msg="请选择飞行日期！">
										</div>
									</div>
								</td>
								<td>
									<div class="control-group">
										<label class="control-label">其他日期：</label>
										<div class="controls" style="float: left;">
											<input id="otherDate" name="otherDate" type="text" readonly="readonly" v-model="flight.otherDate" v-selected dateFormat="yyyy-MM-dd">
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="control-group">
										<label class="control-label">起飞机场：</label>
										<div class="controls">
											<input id="upAirport" name="upAirport" v-model="flight.upAirport" autocomplete="off" type="text" dataType="Require,Limit" len="4" msg="请输入(1~4)个字符的起飞机场！" maxlength="4" class="required" value=""/>
										</div>
									</div>
								</td>
								<td>
									<div class="control-group">
										<label class="control-label">降落机场：</label>
										<div class="controls">
											<input id="downAirport" name="downAirport" v-model="flight.downAirport" autocomplete="off" type="text" dataType="Require,Limit" len="4" msg="请输入(1~4)个字符的降落机场！" maxlength="4" class="required" value=""/>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="control-group">
										<label class="control-label">入境点：</label>
										<div class="controls">
											<input id="inPointIdent" name="inPointIdent" v-model="flight.inPointIdent" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的入境点！" maxlength="50" class="required" value=""/>
										</div>
									</div>
								</td>
								<td>
									<div class="control-group">
										<label class="control-label">出境点：</label>
										<div class="controls">
											<input id="outPointIdent" name="outPointIdent" v-model="flight.outPointIdent" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的出境点！" maxlength="50" class="required" value=""/>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div class="control-group">
										<label class="control-label">航路：</label>
										<div class="controls">
											<textarea id="flightBody" name="flightBody" v-model="flight.flightBody" dataType="Require" msg="请输入航路，不同航路航点间以空格分割！" rows="5" cols="200" style="width: 90%;"></textarea>
										</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</form>
	</template>
</div>
</body>
<script type="text/javascript">
	var vm = new Vue({
		el: "#app",
		data: {
			ctx: ctx,
			note: {},
			flight: {},
			flightList: [],
			noteFileList: [],
			panelId1: new Date().getTime()+"_1",
			panelId2: new Date().getTime()+"_2",
			panelId3: new Date().getTime()+"_3"
		},
		mounted: function(){
			this.init();
		},
		methods: {
			init(){
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
			viewFile(){
				layer.open({
					type:1,
					title:'照会文书扫描件',
					content: $("#fileDiv"),
					shadeClose: false,    //开启遮罩关闭
					shade: false,
					area:['800px','500px'],
					maxmin: true,
					closeBtn: 1,// 不显示关闭按钮
					isOutAnim: false,// 关闭关闭图层动画效果
					anim: -1,// 关闭打开图层动画效果
					success:function(layero,index){
					}
				});
			},
			goSave() {
				Confirm('确认保存外交照会吗？',()=>{
					var myForm=document.getElementById("inputForm");
					if(Validator.Validate(myForm,3)){
						$.ajax({
							url: ctx + "/note/update.action",
							data: this.note,
							type: "post",
							dataType: "json",
							success: (data)=>{
								alert(data.errMsg);
								if(data.errCode=='1'){
									parent.closeDetailLayer();
								}
							}
						});
					}
				});
			},
			//删除
			delFlight(item){
				this.flight = Object.assign({}, item)
				Confirm('确认删除该原始航线信息吗？',()=>{
					$.ajax({
						url:ctx+'/note/delFlight.action',
						data:{'nfSeq': item.nfSeq},
						type:'post',
						dataType:'json',
						success:(data)=>{
							if(data.errCode=='1'){
								this.init();
							}
							alert(data.errMsg);
						}
					});
				});
			},
			addFlight(){
				Validator.Validate_onLoad($('#saveForm')[0],3);
				vm.flight = {}
				layer.open({
					type: 1
					,title: '添加原始航线'
					,area: '600px;'
					,content: $("#saveForm")
					,btn: ['确定','取消']
					,yes: function(index,layero){
						if(!Validator.Validate($('#saveForm')[0],2)){
							return;
						}
						openLoading();
						vm.flight.noteSeq = parent.vm.selNoteSeq
						$.ajax({
							url:ctx+'/note/saveFlight.action',
							data: vm.flight,
							type:'post',
							dataType:'json',
							success:(data)=>{
								if(data.errCode=='1'){
									vm.init();
								}
								alert(data.errMsg);
								closeLoading();
							}
						});
						layer.close(index);
					}
				});
			},
			editFlight(item){
				Validator.Validate_onLoad($('#saveForm')[0],3);
				this.flight = Object.assign({}, item)
				layer.open({
					type: 1
					,title: '编辑原始航线'
					,area: '600px;'
					,content: $("#saveForm")
					,btn: ['确定','取消']
					,yes: function(index,layero){
						if(!Validator.Validate($('#saveForm')[0],2)){
							return;
						}
						openLoading();
						$.ajax({
							url:ctx+'/note/saveFlight.action',
							data: vm.flight,
							type:'post',
							dataType:'json',
							success:(data)=>{
								if(data.errCode=='1'){
									vm.init();
								}
								alert(data.errMsg);
								closeLoading();
							}
						});
						layer.close(index);
					}
				});
			}
		}
	});
	
</script>
</html>