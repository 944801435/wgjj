<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
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
label{
    position: relative;
}
#fileinp{
    position: absolute;
    left: 0;
    top: 0;
    opacity: 0;
}
#btn{
    margin-right: 5px;
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
			<form id="inputForm"  method="post" class="form-horizontal" style="margin: 0;">
				<div :id="panelId1" class="right_content_table">
					<table class="table" style="width: 100%;">
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">来电来函单位：</label>
									<div class="controls">
										<input id="letterUnit" name="letterUnit" v-model="notePlanInfo.letterUnit" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的来电来函单位！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">姓名：</label>
									<div class="controls">
										<input id="personName" name="personName" v-model="notePlanInfo.personName" autocomplete="off" type="text" dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的联系人！" maxlength="100" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">电话：</label>
									<div class="controls">
										<input id="telNo" name="telNo" v-model="notePlanInfo.telNo" autocomplete="off" type="text" dataType="Require,Limit" len="20" msg="请输入(1~20)个字符的电话！" maxlength="20" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">国家：</label>
									<div class="controls">
										<input id="nationality" name="nationality" v-model="notePlanInfo.nationality" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的国家！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">照会号：</label>
									<div class="controls">
										<input id="noteNo" name="noteNo" v-model="notePlanInfo.noteNo" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的照会号！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">营运方：</label>
									<div class="controls">
										<input id="operator" name="operator" v-model="notePlanInfo.operator" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的营运方！" maxlength="50" class="required" value=""/>
									</div>
								</div>
							</td>
							</tr>
							<tr>
							<td>
								<div class="control-group">
									<label class="control-label">架数：</label>
									<div class="controls">
										<input style="width: 140px;" placeholder="请输入数字" id="airNumber" name="airNumber" v-model="notePlanInfo.airNumber" autocomplete="off" type="number" dataType="Require,Limit" len="11" msg="请输入11位内的数字！" min = 1 maxlength="11" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">机型：</label>
									<div class="controls">
										<input id="model" name="model" v-model="notePlanInfo.model" autocomplete="off" type="text" dataType="Require,Limit" len="200" msg="请输入(1~200)个字符的机型！" maxlength="200" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">机组人数：</label>
									<div class="controls">
										<input style="width: 140px;" placeholder="请输入数字" id="personNumber" name="personNumber" v-model="notePlanInfo.personNumber" autocomplete="off" type="number" dataType="Require,Limit" len="11" msg="请输入11位内的数字！" min = 1 maxlength="11" class="required" value=""/>
									</div>
								</div>
							</td>
							</tr>
							<tr>
							<td>
								<div class="control-group">
									<label class="control-label">呼号：</label>
									<div class="controls">
										<input id="callNumber" name="callNumber" v-model="notePlanInfo.callNumber" autocomplete="off" type="text" dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的联系人！" maxlength="100" class="required" value=""/>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">注册号：</label>
									<div class="controls">
										<input id="regNo" name="regNo" v-model="notePlanInfo.regNo" autocomplete="off" type="text" dataType="Require,Limit" len="20" msg="请输入(1~20)个字符的电话！" maxlength="20" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div class="control-group">
									<label class="control-label">任务目的：</label>
									<div class="controls">
										<input style="width: 93%;" id="mission" name="mission" v-model="notePlanInfo.mission" autocomplete="off" type="text" dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的任务目的！" maxlength="100" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div class="control-group">
									<label class="control-label">其他：</label>
									<div class="controls">
										<input style="width: 93%;" id="other" name="other" v-model="notePlanInfo.other" autocomplete="off" type="text" dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的任务目的！" maxlength="100" class="required" value=""/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div class="control-group">
								<label class="control-label">照会文件：</label>
								<label for="fileinp">
								    <input type="button" id="btn"  value="选择文件"><span id="text">请上传文件(jpg,png,bmp,gif等图片类型)</span>
									<input type="file" title="" id='fileinp' accept="image/*" name="file" multiple="multiple" @change='fileChangeback($event)'>
							        <label for="files"></label>
								 </label>
								<div v-if='imgsback.length>0' class="fileBox">
								    <div class="fileBox_item" v-for="(item, i) in imgsback" >
								    <img :id="item.id" :src="item.img" alt="" width="100px" height="100px">
											<div>
												<span>{{item.name}}</span>
											</div>
<!-- 							          <img :src="item" alt=""  width="100px" height="100px"> -->
							          <div class="file_i"><i class="fa fa-remove" @click="del(i)"></i></div>
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
			<div onclick="window.location.href='${ctx}/noteInfoManageList.action'" style="cursor:pointer;"
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
			imgsback: [],      // 图片预览地址
			imgfilesback: [],  // 图片原文件，上传到后台的数据
			sizeback: 1 , 
			ctx: ctx,
			notePlanInfo: {},
			flight: {},
			flightList: [],
			noteFileList: [],
			panelId1: new Date().getTime()+"_1",
			selNoteId:null,
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
			fileChangeback(e) {  // 加入图片
		        // 图片预览部分
		        var vm = this
		        var event = event || window.event;
		        var file = event.target.files
		        var leng=file.length;
		            var fileName;
		        for(var i=0;i<leng;i++){
		        	var files = event.target.files[i];
		            var reader = new FileReader();    // 使用 FileReader 来获取图片路径及预览效果
		            vm.imgfilesback.push(file[i]);
		            reader.file=files;
		            reader.readAsDataURL(file[i]); 
		            	fileName=file[i].name;
		            reader.onload =function(e){
		            	var obj = {
		            			img:e.target.result,
		            			file:this.file,
								name:this.file.name,
								id:"img_"+vm.imgsback.length+i
							};
		              vm.imgsback.push(obj);   // base 64 图片地址形成预览                                 
		            };                 
		        }
		    },
			goSave() {
				Confirm('确认保存外交照会吗？',()=>{
					var formData = new FormData($("#inputForm")[0]);
					if(!Validator.Validate($('#inputForm')[0],3)){
						return;
					}
					var files = $("file").val();
					if(vm.imgsback.length<=0){
						layer.msg("请上传照会文件!");
						return;
					}
					var url='${pageContext.request.contextPath }/addNote.action';
					$.ajax({
						type : 'POST',
						url : url,
						data : formData,
						dataType : 'json',
						contentType:false,
						processData:false,
						error : function(xhr, status, statusText) {
							layer.msg("网络错误!" + xhr.status);
						},
						success : function(data) {
							if (data.errCode == '1') {
								vm.notePlanInfo = data.data;
								var noteId = data.data.noteId;
								layer.alert(data.errMsg, function(index){
						    	window.location.href="${pageContext.request.contextPath }/to_add_note_flight_jsp.action?noteId="+noteId;
								});
							} else {
								layer.alert('保存失败', {icon : 3});
							}	
						}
					});
					});
			},
			del:function(index) {
				this.imgsback.splice(index, 1);//从哪个位置删除1个元素
			}
		}
	});
	function closeDetailLayer(){
		if(layerIndex!=null){
			layer.close(layerIndex);
			layerIndex = null;
		}
		vm.selNoteId = null;
		parent.closeDetailLayer();
	}
</script>
</html>