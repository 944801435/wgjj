<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>飞行计划审批</title>
	<%@ include file="../tool.jsp"%>
	<script type="text/javascript" src="${ctx }/js/validate.js"></script>
	<style type="text/css">
		input[type=color]{
			border: none;
			width: 20px;
			box-shadow: none;
			-webkit-box-shadow:none;
			background-color: transparent;
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
			width: 90px;
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
	   		<!-- 列表 -->
			<div class="right_content_all">
				<div class="right_content_all_top">
					<span>检索条件</span>
				</div>
				<div class="right_content_select">
					<div class="right_content_select_box">
						<span class="right_content_select_name">创建时间：</span>
						<input class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入起始时间" type="text" readonly="readonly" v-model="queryForm.begTime" v-selected dateFormat="yyyy-MM-dd HH:mm:ss"/>
							-
						<input class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入截止时间" type="text" readonly="readonly" v-model="queryForm.endTime" v-selected dateFormat="yyyy-MM-dd HH:mm:ss"/>	
				    </div>
					<div class="span4 right_content_select_box">
						<span class="right_content_select_name">来函单位：</span> <input
							class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入来函单位" type="text" v-model="queryForm.letterUnit"  />
					</div>
					<div>
						<div class="right_content_select_box">
							<span class="right_content_select_name">照会号：</span> <input
								class="right_content_select_ctt right_content_select_cttt"
								placeholder="请输入照会号" type="text" v-model="queryForm.noteSeq" />
						</div>
						<div class="right_content_select_box">
							<span class="right_content_select_name">国家：</span> <input
								class="right_content_select_ctt right_content_select_cttt"
								placeholder="请输入国家" type="text" v-model="queryForm.nationality"  />
						</div>
						<div class="right_content_select_box">
							<span class="right_content_select_name">机型：</span> <input
								class="right_content_select_ctt right_content_select_cttt"
								placeholder="请输入机型" type="text" v-model="queryForm.model"  />
						</div>
						<div class="right_content_select_box">
							<span class="right_content_select_name">计划状态：</span>
                            <select v-model="queryForm.planSts" v-select2 >
                                <option value="">全部</option>
                                <option v-for="(value,key) in biz_space_req_map" :value="key" >{{value }}</option>
                            </select>
						</div>
					</div>
				</div>
				<div class="right_content_btnbox">
					<div @click="search()" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_search">
						<img src="${pageContext.request.contextPath }/images/search_btn.png"/>
						<span>搜索</span>
					</div>
					<div @click="reset()" class="right_content_btnbox_btn right_content_btnbox_resize">
						<img src="${ctx }/images/resize_btn.png"/>
						<span>清空</span>
					</div>
				</div>
				<div class="right_content_table">
			         <table class="table table-bordered table_list table-striped">
						<thead>
							<tr class="active blue_active_op">
								<th width="12%">导入时间</th>
								<th width="10%">照会号</th>
								<th width="8%">国家</th>
								<th width="6%">呼号</th>
								<th width="6%">机型</th>
								<th width="6%">注册号</th>
								<th width="8%">许可证号</th>
								<th width="8%">来函单位</th>
								<th width="6%">联系人</th>
                                <th width="7%">电话</th>
                                <th width="7%">计划状态</th>
								<th width="10%">操作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="item in list">
								<td>{{item.crtTime }}</td>
								<td>{{item.noteSeq }}</td>
								<td>{{item.note.nationality }}</td>
								<td>{{item.note.callSign }}</td>
								<td>{{item.note.model }}</td>
								<td>{{item.note.regNo }}</td>
								<td>{{item.note.licenseNo }}</td>
								<td>{{item.note.letterUnit }}</td>
								<td>{{item.note.personName }}</td>
                                <td>{{item.note.telNo }}</td>
                                <td>{{biz_space_req_map[item.planSts] }}</td>
								<td>
									<a v-if="item.planSts==BIZ_SPACE_REQ_AUDIT" href="javascript:void(0);" @click="edit(item.planSeq)">编辑</a>
									<a v-if="item.planSts==BIZ_SPACE_REQ_AUDIT" href="javascript:void(0);" @click="createForm(item.planSeq)"><br/>生成审批表<br/></a>
									<a v-if="item.appFileId!=null && item.planSts==BIZ_SPACE_REQ_AUDIT" href="javascript:void(0);" @click="downloadForm(item.appFileId)">下载审批表<br/></a>
									<a v-if="item.planSts==BIZ_SPACE_REQ_AUDIT" href="javascript:void(0);" @click="audit(item.planSeq)">审批</a>
									<a href="javascript:void(0);" @click="detail(item.planSeq)">查看</a>
									<a v-if="item.planSts==BIZ_SPACE_REQ_APPROVE" href="javascript:void(0);" @click="sendRpt(item.planSeq)">下发许可<br/></a>
									<a v-if="item.rptPdfFileId!=null" href="javascript:void(0);" @click="downloadRpt(item.rptPdfFileId)">下载许可</a>
								</td>
							</tr>
						</tbody>
					</table>
			    </div>
			    <div class="right_content_pagination">
					<pagination :cur-page.sync="curPage" :page-size="pageSize" :total-count="totalCount" @turn="init" />
				</div>
			</div>
			<form id="saveForm" hidden="hidden">
				<div>
					<div class="right_content_all">
						<div class="right_content_table">
							<table class="open_table">
								<tr>
									<td>
										<div class="control-group">
											<label class="control-label">二次雷达码：</label>
											<div class="controls">
												<input id="ssrCode" name="ssrCode" v-model="note.ssrCode" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的二次雷达码！" maxlength="50" class="required" value=""/>
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
  <script>
  var layerIndex=null;
  $(function(){
  	$(window).resize(function() {
  		if(layerIndex!=null){
  			layer.full(layerIndex);
  		}
  	});
  });
	//Vue
	var vm=new Vue({
		el:'#app',
		data:{
			queryForm:{
				begTime:'',
			    endTime:'',
				noteSeq:'',
				nationality:'',
				model:'',
				callSign:'',
				letterUnit:'',
                planSts:''
			},
			list:[],
			curPage:1,
			pageSize:10,
			totalCount:0,
			optFlag:'${fns:hasPms(pmsIds,"10102")}'=='true',
			selNoteSeq: null,
			note:{
				noteSeq:null,
				ssrCode:null
			},
            BIZ_SPACE_REQ_AUDIT: "${fns:findKey('BIZ_SPACE_REQ_AUDIT')}",
			BIZ_SPACE_REQ_APPROVE: "${fns:findKey('BIZ_SPACE_REQ_APPROVE')}",
            biz_space_req_map: JSON.parse('${fns:findJSONMap("biz_space_req_map")}')
		},
		watch:{
		},
		methods:{
			search(){
				this.curPage=1;
				this.init();
			},
			reset(){
				for(var key in this.queryForm){
					this.queryForm[key]='';
				}
			},
			init(){
				$.ajax({
					url:ctx+'/flyPlan/auditList.action',
					data:Object.assign(this.queryForm,{curPage:this.curPage,pageSize:this.pageSize}),
					type:'post',
					dataType:'json',
					success:function(data){
						if(data.errCode=='0'){
							showMsg(data.errCode,data.errMsg);
							return;
						}
						vm.list=data.data.list;
						vm.totalCount=data.data.totalCount;
					}
				});
			},
			//添加
			add: function(){
				layerIndex=layer.open({
					type:2,
					title:'飞行计划详情',
					content: ctx + '/view/flyPlan/add.jsp',
					shadeClose: true,    //开启遮罩关闭
					shade:false,
					area:'100%',
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
			//删除
			del(planSeq){
				Confirm('确认删除该飞行计划信息吗？',()=>{
					$.ajax({
						url:ctx+'/flyPlan/del.action',
						data:{'planSeq': planSeq},
						type:'post',
						dataType:'json',
						success:(data)=>{
							if(data.errCode=='1'){
								this.init();
							}
							showMsg(data.errCode,data.errMsg);
						}
					});
				});
			},
			// 下载
			download(planSeq){
				window.location.href = ctx + "/flyPlan/downloadAuditResult.action?planSeq=" + planSeq;
			},
			// 下载审批表
			downloadForm(fileId){
				window.location.href = ctx + "/download.action?fileId=" + fileId;
			},
			// 下载许可
			downloadRpt(fileId){
				window.location.href = ctx + "/download.action?fileId=" + fileId;
			},
			detail(planSeq){
				vm.selNoteSeq = planSeq;
				layerIndex=layer.open({
					type:2,
					title:'飞行计划详情',
					content: ctx + '/view/flyPlan/detail.jsp',
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
			edit(planSeq){
				vm.selNoteSeq = planSeq;
				layerIndex=layer.open({
					type:2,
					title:'飞行计划编辑',
					content: ctx + '/view/flyPlan/edit.jsp',
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
			createForm(planSeq){
				vm.selNoteSeq = planSeq;
				layerIndex=layer.open({
					type:2,
					title:'飞行计划审批表生成',
					content: ctx + '/view/audit/create_form.jsp',
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
			sendRpt(planSeq){
				vm.selNoteSeq = planSeq;
				layerIndex=layer.open({
					type:2,
					title:'飞行计划下发许可函',
					content: ctx + '/view/audit/send_rpt.jsp',
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
			audit(planSeq){
				vm.selNoteSeq = planSeq;
				layerIndex=layer.open({
					type:2,
					title:'飞行计划审批',
					content: ctx + '/view/audit/audit.jsp',
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
            reCall(planSeq){
                Confirm('确认撤回该飞行计划吗？',()=>{
                    $.ajax({
                        url:ctx+'/flyPlan/reCall.action',
                        data:{'planSeq': planSeq},
                        type:'post',
                        dataType:'json',
                        success:(data)=>{
                            if(data.errCode=='1'){
                                this.init();
                            }
                            showMsg(data.errCode,data.errMsg);
                        }
                    });
                });
            },
            commit(planSeq){
                vm.selNoteSeq = planSeq;
                layerIndex=layer.open({
                    type:2,
                    title:'飞行计划上报',
                    content: ctx + '/view/flyPlan/commit.jsp',
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
			//填写二次雷达码
			editCode(item){
				Validator.Validate_onLoad($('#saveForm')[0],3);
				vm.note = Object.assign({}, item)
				layer.open({
					type: 1
					,title: '填写二次雷达码'
					,area: '400px;'
					,content: $("#saveForm")
					,btn: ['确定','取消']
					,yes: function(index,layero){
						if(!Validator.Validate($('#saveForm')[0],2)){
							return;
						}
						openLoading();
						$.ajax({
							url:ctx+'/flyPlan/updateSsrCode.action',
							data: {
								noteSeq: vm.note.noteSeq,
								ssrCode: vm.note.ssrCode
							},
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
		},
		mounted(){
			this.init();
		}
		
	});

	function closeDetailLayer(){
		if(layerIndex!=null){
			layer.close(layerIndex);
			layerIndex = null;
		}
		vm.selNoteSeq = null;
	}
  </script>
</html>
