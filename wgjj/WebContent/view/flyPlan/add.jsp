<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>飞行计划</title>
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
					<span>外交照会列表</span>
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
								<th width="8%">操作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="item in list">
								<td>{{item.crtTime }}</td>
								<td>{{item.noteSeq }}</td>
								<td>{{item.nationality }}</td>
								<td>{{item.callSign }}</td>
								<td>{{item.model }}</td>
								<td>{{item.regNo }}</td>
								<td>{{item.licenseNo }}</td>
								<td>{{item.letterUnit }}</td>
								<td>{{item.personName }}</td>
								<td>{{item.telNo }}</td>
								<td>
									<a href="javascript:void(0);" @click="detail(item.noteSeq)">查看</a>
									<a href="javascript:void(0);" @click="edit(item.noteSeq)">生成计划</a>
								</td>
							</tr>
						</tbody>
					</table>
			    </div>
			</div>
            <div class="right_content_btnbox ">
                <div onclick="parent.closeDetailLayer()" style="cursor:pointer;"
                     class="right_content_btnbox_btn right_content_btnbox_return">
                    <img src="${pageContext.request.contextPath }/images/return_btn.png" />
                    <span>返回</span>
                </div>
            </div>
		</template>
	</div>
  </body>
  <script>
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
				letterUnit:''
			},
			list:[],
			optFlag:'${fns:hasPms(pmsIds,"10102")}'=='true',
			selNoteSeq: null
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
					url:ctx+'/flyPlan/getNoteList.action',
					type:'post',
					dataType:'json',
					success:function(data){
						if(data.errCode=='0'){
							showMsg(data.errCode,data.errMsg);
							return;
						}
						vm.list=data.data;
					}
				});
			},
			detail(noteSeq){
				vm.selNoteSeq = noteSeq;
				layerIndex=layer.open({
					type:2,
					title:'飞行计划详情',
					content: ctx + '/view/note/detail.jsp',
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
			edit(noteSeq){
				vm.selNoteSeq = noteSeq;
				Confirm('确认生成该飞行计划吗？',()=>{
					$.ajax({
						url:ctx+'/flyPlan/save.action',
						data:{'noteSeq': noteSeq},
						type:'post',
						dataType:'json',
						success:(data)=>{
							if(data.errCode=='1'){
								parent.vm.init();
								parent.closeDetailLayer();
							}
							parent.showMsg(data.errCode,data.errMsg);
						}
					});
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
        vm.init();
	}
  </script>
</html>
