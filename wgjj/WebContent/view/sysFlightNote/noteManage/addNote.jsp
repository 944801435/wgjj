<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>照会文书新增</title>
<%@ include file="../../tool.jsp"%>
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


.fileBox{
	width: 100%;
	float: left;
}
.file_i{
	width: 200px;
	height: 20px;
	float: left;
	margin-top: -20px;
	background-color: rgb(0 0 0 / 20%);
	position: absolute;
}
.fileBox_item{
	float: left;
	margin-right: 10px;
}
.fileBox img{
	width: 200px;
	height: 200px;
	z-index: -1;
}
.fileBox i{
	float: right;
}
.table td i {
	margin: 4px;
}
.fileBox_item img{
	border: 1px;
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
			<form id="inputForm" method="post" enctype="multipart/form-data" class="form-horizontal" style="margin: 0;">
				<div :id="panelId1" class="right_content_table">
					<table class="table" style="width: 100%;">
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">照会编号：</label>
									<div class="controls">
										<input id="noteSeq" type="text" name="noteSeq" v-model="note.noteSeq" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的照会编号！" maxlength="50" class="required" value=""/>
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
							<td colspan="3">
								<div class="control-group">
									<label class="control-label">任务目的：</label>
									<div class="controls">
										<input style="width: 93%;" id="mission" name="mission" v-model="note.mission" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的任务目的！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div class="control-group">
									<label class="control-label">文书扫描件：</label>
									<input id="file" type="file" name="file" multiple="multiple" @change="getFileOthers">
									<div class="fileBox">
										<div class="fileBox_item" v-for="(item,index) in srcList">
											<img :id="item.id" :src="item.img" alt="" width="100px" height="100px">
											<div>
												<span>{{item.name}}</span>
											</div>
											<div class="file_i"><i class="fa fa-remove" @click="del(index)"></i></div>
										</div>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</form>
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
			selNoteSeq:null,
			srcList:[]
		},
		mounted: function(){

		},
		methods: {
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
					var formData = new FormData($('#inputForm')[0]);
					formData.delete('file');
					// formData.append('planSeq',vm.planSeq);
					for (var i = 0;i<vm.srcList.length;i++){
						formData.append('file',vm.srcList[i].file);
					}
					if(!Validator.Validate($('#inputForm')[0],3)){
						return;
					}
					openLoading();
					$.ajax({
						url:ctx+'/note/add.action',
						type:'post',
						data: formData,
						dataType:'json',
						contentType:false,
						processData:false,
						success:(data)=>{
							alert(data.errMsg);
							if(data.errCode=='1'){
								vm.note = data.data;
								vm.selNoteSeq = data.data.noteSeq;
								layerIndex=layer.open({
									type:2,
									title:'外交照会编辑',
									content: ctx + '/view/note/edit.jsp',
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
								parent.vm.init();
							}
							closeLoading();
						}
					});
				});
			},
			getFileOthers (e) {//附件预览----
				let _this = this
				if(e.target.files.length == 0){
					return;
				}else {
					vm.srcList = [];
					for (var i = 0;i<e.target.files.length;i++){
						var files = e.target.files[i];
						if (files == null){
							return;
						}
						if (!e || !window.FileReader){
							return;  // 看支持不支持FileReader
						}
						let reader = new FileReader();
						reader.file = files;
						reader.readAsDataURL(files); // 这里是最关键的一步，转换就在这里
						reader.onloadend = function()  {
							var obj = {
								img:this.result,
								file:this.file,
								name:this.file.name,
								id:"img_"+vm.srcList.length+i
							};
							vm.srcList.push(obj);
							$("#file").val(null);
						};
					}
				}
			},
			del:function(index) {
				this.srcList.splice(index, 1);//从哪个位置删除1个元素
			}
		}
	});
	function closeDetailLayer(){
		if(layerIndex!=null){
			layer.close(layerIndex);
			layerIndex = null;
		}
		vm.selNoteSeq = null;
		parent.closeDetailLayer();
	}
</script>
</html>