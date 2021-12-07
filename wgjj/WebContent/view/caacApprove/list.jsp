<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>民航意见</title>	
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
						<div class="span4 right_content_select_box">
							<span class="right_content_select_name">许可证号：</span> <input
								class="right_content_select_ctt right_content_select_cttt"
								placeholder="请输入许可证号" type="text" v-model="queryForm.licenseNo"  />
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
					<div v-if="optFlag" @click="upload()" class="right_content_btnbox_btn right_content_btnbox_add">
						<img src="${ctx}/images/add_btn.png" />
						<span>导入</span>
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
								<th width="">来函单位</th>
								<th width="6%">联系人</th>
								<th width="7%">电话</th>
								<th width="5%">版本</th>
								<th width="8%">操作</th>
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
								<td>{{item.licenseNo }}</td>
								<td>{{item.note.letterUnit }}</td>
								<td>{{item.note.personName }}</td>
								<td>{{item.note.telNo }}</td>
								<td>{{item.appVer }}</td>
								<td>
									<a href="javascript:void(0);" @click="detail(item.caSeq)">查看</a>
									<a href="javascript:void(0);" @click="download(item.caacZipFileId)">下载</a>
								</td>
							</tr>
						</tbody>
					</table>
			    </div>
			    <div class="right_content_pagination">
					<pagination :cur-page.sync="curPage" :page-size="pageSize" :total-count="totalCount" @turn="init" />
				</div>
			</div>
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
				licenseNo:'',
				letterUnit:''
			},
			list:[],
			curPage:1,
			pageSize:10,
			totalCount:0,
			optFlag:'${fns:hasPms(pmsIds,"10202")}'=='true',
			selCaSeq: null
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
					url:ctx+'/caacApprove/list.action',
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
			//导入
			upload: function(){
				var domStr = 
					`<form id="saveForm" method="post" enctype="multipart/form-data" >
			  			<div style="display:inline-flex;margin:20px;width:220px;">
		  					<input style="width:180px;" dataType="Require" msg="请选择要导入的民航意见压缩包！" type="file" id="file" name="file"/>
		  				</div>
			  		</form>`;
				layer.open({
					   type: 1
					  ,title: '导入民航意见压缩包'
					  ,content: domStr
					  ,btn: ['确定','取消']
					  ,yes: function(index,layero){
						if(!Validator.Validate($('#saveForm')[0],2)){
							return;
						}
						openLoading();
						$("#saveForm").ajaxSubmit({
							url:ctx+'/caacApprove/upload.action',
							type:'post',
							dataType:'json',
							success:(data)=>{
								if(data.errCode=='1'){
									vm.init();
								}
								showMsg(data.errCode,data.errMsg);
								closeLoading();
							}
						});
					    layer.close(index);
					}
				});
			},
			// 下载
			download(fileId){
				window.location.href = ctx + "/download.action?fileId=" + fileId;
			},
			detail(caSeq){
				vm.selCaSeq = caSeq;
				layerIndex=layer.open({
					type:2,
					title:'民航意见详情',
					content: ctx + '/view/caacApprove/detail.jsp',
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
		vm.selCaSeq = null;
	}
  </script>
</html>
