<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
<title>照会信息编辑页</title>
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
ul { list-style:none;margin: 0px;padding: 0px;text-align: left;}
.iprod_tit { margin-top:0px; color:#333; text-align:center; font-size:25px }
.white .iprod_tit { color: #fff }
.white .iprod_tit2 { color: #fff }
.product_ls { width:100%; }
.product_ls li { width:49%; height:390px; margin-left:5px; float:left; }
.product_ls li:hover { background:#fff; box-shadow:0 0 8px #ccc }
.product_ls .icons { width:70px; height:70px; margin:0 auto; background-repeat:no-repeat; background-position:center }
/* .product_ls .icon_1 { background-image:url(../images/picon_iprod01.png) } */
/* .product_ls .icon_2 { background-image:url(../images/picon_iprod02.png) } */
/* .product_ls li:hover .icon_1 { background-image:url(../images/picon_iprod01_active.png) } */
/* .product_ls li:hover .icon_2 { background-image:url(../images/picon_iprod02_active.png) } */
.product_ls h3 { font-size:18px; text-align:center }
.product_ls h4 { font-size:14px; text-align:center }
.product_ls p { margin:40px 40px 0; font-size:14px; line-height:24px; text-indent:2em }
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
									<label class="control-label">来函单位：</label>
									<div class="controls">
									<input id="noteId" name="noteId"
											v-model="notePlanInfo.noteId" type="hidden" class="required"
											value="" />
										<input id="letterUnit" name="letterUnit"
											v-model="notePlanInfo.letterUnit" autocomplete="off"
											type="text" dataType="Require,Limit" len="50"
											msg="请输入(1~50)个字符的来函单位！" maxlength="50" class="required"
											value="" />
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">联系人：</label>
									<div class="controls">
										<input id="personName" name="personName"
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
										<input id="telNo" name="telNo" v-model="notePlanInfo.telNo"
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
										<input id="nationality" name="nationality"
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
										<input id="noteNo" name="noteNo" v-model="notePlanInfo.noteNo"
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
										<input id="operator" name="operator"
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
										<input style="width: 140px;" placeholder="请输入数字"
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
										<input id="model" name="model" v-model="notePlanInfo.model"
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
										<input style="width: 140px;" placeholder="请输入数字"
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
										<input id="callNumber" name="callNumber"
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
										<input id="regNo" name="regNo" v-model="notePlanInfo.regNo"
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
										<input style="width: 93%;" id="mission" name="mission"
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
										<input style="width: 93%;" id="other" name="other"
											v-model="notePlanInfo.other" autocomplete="off" type="text"
											dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的任务目的！"
											maxlength="100" class="required" value="" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div class="control-group">
								<label class="control-label">照会文件：</label>
								<input type="file" id='files' name="file" multiple="multiple" @change='fileChangeback($event)'>
							        <label for="files"></label>
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
						<td width="18%">操作</td>
					</tr>
					<tr v-for="item in noteFilesList">
						<td>{{item.fileNameCn }}</td>
						<td>{{item.fileSize }}<span>MB</span></td>
						<td>{{item.createTime }}</td>
						<td><a @click="ocrDistinguish(item)">识别</a>
						 <a @click="noteTranslate(item.id)">翻译</a></td>
					</tr>
				</table>
			</div>
		</div>
			<div class="right_content_all" style="height:425px">
			<div class="right_content_all_top my-collapse" :href="'#'+panelId4">
				<span>结果展示</span>
			</div>
			<ul class="product_ls">
				<li>
					<h3>OCR识别结果</h3>
					<p id="ocrNoteInfo"></p>
				</li>
				<li>
					<h3>翻译结果</h3>
					<p>联合各律师事务所，搭建一站式法律云服务平台，对接移动公证法律大数据，将法律服务连接智能高效的互联网方式。</p>
				</li>
			</ul>
			</div>
		<div class="right_content_all" >
			<div class="right_content_all_top my-collapse" :href="'#'+panelId3" style="margin-top: 25px;">
				<span>飞行计划</span>
			</div>
			<div :id="panelId3" class="right_content_table">
				<table class="table table-bordered table_list flightTable">
					<tr>
						<td colspan="8"><a @click="addFlight()"
							style="float: right; margin-right: 10px;">添加飞行计划信息</a></td>
					</tr>
					<tr>
						<td width="10%">飞行日期</td>
						<td width="9%">起飞机场</td>
						<td width="9%">降落机场</td>
						<td width="9%">入境点</td>
						<td width="9%">出境点</td>
						<td width="19%">计划航线</td>
						<td width="19%">备用航线</td>
						<td width="8%">操作</td>
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
						<td>
							<!-- 							<a @click="editFlight(item)">编辑</a> --> <a
							@click="delFlight(item)">删除</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="right_content_btnbox ">
			<div @click="goSave()" style="cursor: pointer;"
				class="right_content_btnbox_btn right_content_btnbox_save">
				<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
			</div>
			<div
				onclick="window.location.href='${ctx}/noteInfoManageList.action'"
				style="cursor: pointer;"
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
											<input id="flightTime" name="flightTime" type="text"
												readonly="readonly" v-model="flight.flightTime" v-selected
												dateFormat="yyyy-MM-dd" dataType="Require" msg="请选择飞行日期！">
										</div>
									</div>
								</td>
								<td>
									<div class="control-group">
										<label class="control-label">起飞机场：</label>
										<div class="controls">
											<input id="departureAirport" name="departureAirport"
												v-model="flight.departureAirport" autocomplete="off"
												type="text" dataType="Require,Limit" len="4"
												msg="请输入(1~4)个字符的起飞机场！" maxlength="4" class="required"
												value="" />
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="control-group">
										<label class="control-label">降落机场：</label>
										<div class="controls">
											<input id="landAirport" name="landAirport"
												v-model="flight.landAirport" autocomplete="off" type="text"
												dataType="Require,Limit" len="4" msg="请输入(1~4)个字符的降落机场！"
												maxlength="4" class="required" value="" />
										</div>
									</div>
								</td>
								<td>
									<div class="control-group">
										<label class="control-label">入境点：</label>
										<div class="controls">
											<input id="entryName" name="entryName"
												v-model="flight.entryName" autocomplete="off" type="text"
												dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的入境点！"
												maxlength="50" class="required" value="" />
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="control-group">
										<label class="control-label">出境点：</label>
										<div class="controls">
											<input id="exitName" name="exitName"
												v-model="flight.exitName" autocomplete="off" type="text"
												dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的出境点！"
												maxlength="50" class="required" value="" />
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div class="control-group">
										<label class="control-label">计划航线：</label>
										<div class="controls">
											<textarea id="planRoute" name="planRoute"
												v-model="flight.planRoute" dataType="Require"
												msg="请输入航路，不同航路航点间以空格分割！" rows="3" cols="200"
												style="width: 90%;"></textarea>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div class="control-group">
										<label class="control-label">备用航线：</label>
										<div class="controls">
											<textarea id="alternateRoute" name="alternateRoute"
												v-model="flight.alternateRoute" dataType="Require"
												msg="请输入航路，不同航路航点间以空格分割！" rows="3" cols="200"
												style="width: 90%;"></textarea>
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
			imgsback: [],      // 图片预览地址
			imgfilesback: [],  // 图片原文件，上传到后台的数据
			sizeback: 1 , 
			ctx: ctx,
			notePlanInfo: {},
			flight: {},
			flightList: [],
			noteFilesList: [],
			panelId1: new Date().getTime()+"_1",
			panelId2: new Date().getTime()+"_2",
			panelId3: new Date().getTime()+"_3",
			panelId4: new Date().getTime()+"_4",
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
				Confirm('确认保存照会信息吗？',()=>{
					var formData = new FormData($("#inputForm")[0]);
					var url='${pageContext.request.contextPath }/update_plan_info_flight.action';
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
						    	window.location.href="${pageContext.request.contextPath }/noteInfoManageList.action";
								});
							} else {
								layer.alert('保存失败', {icon : 3});
							}	
						}
					});
				});
			},
			//删除
			delFlight(item){
				this.flight = Object.assign({}, item)
				Confirm('确认删除该飞行计划信息吗？',()=>{
					$.ajax({
						url:ctx+'/del_plan_flight_info.action',
						data:{'id': item.id},
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
				noteIdAc="${noteIdAc}";
				Validator.Validate_onLoad($('#saveForm')[0],3);
				vm.flight = {}
				layer.open({
					type: 1
					,title: '添加飞行计划信息'
					,area: '600px;'
					,content: $("#saveForm")
					,btn: ['确定','取消']
					,yes: function(index,layero){
						if(!Validator.Validate($('#saveForm')[0],2)){
							return;
						}
						openLoading();
						vm.flight.noteId = noteIdAc
						$.ajax({
							url:ctx+'/save_plan_flight_info.action',
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
			ocrDistinguish(item){
				$.ajax({
					url:ctx+'/ocr_distinguish_note_info.action',
					data: item,
					type:'post',
					dataType:'json',
					success:(data)=>{
						if(data.errCode=='1'){
						document.getElementById("ocrNoteInfo").innerHTML=data.data.ocrText;
						}
						alert(data.errMsg);
						closeLoading();
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
			},
			del:function(index) {
				this.imgsback.splice(index, 1);//从哪个位置删除1个元素
			}
		}
	});
	
</script>
</html>