<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>飞行架次查询</title>
<%@ include file="../tool.jsp"%>
<style type="text/css">
	.centerSelect{
		width: 51px;
	}
	.centerSelect:first-of-type{
		width: 60px;
	}
	.centerSelect:focus{
		outline: none;
	}
	.centerSpan{
		vertical-align: top;
	}
</style>
</head>
<body>
	<div id="app" class="right_content">
		<template>
			<div class="right_content_all">
				<div class="right_content_all_top">
					<span>检索条件</span>
				</div>
				<div class="right_content_select">
					<form id="queryForm" method="post">
						<div class="right_content_select_box">
							<span class="right_content_select_name">起飞时间：</span> 
							<input class="span3 right_content_select_ctt right_content_select_cttt"
								placeholder="请输入起始时间" type="text" readonly="readonly" name="begTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" style="width:140px;"/>
								-
							<input class="span3 right_content_select_ctt right_content_select_cttt"
								placeholder="请输入截止时间" type="text" readonly="readonly" name="endTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" style="width:140px;"/>	
					  	</div>
						<div class="right_content_select_box">
							<span class="right_content_select_name">登记号：</span> <input
								class="right_content_select_ctt right_content_select_cttt"
								placeholder="请输入登记号" type="text" id="regNo" name="regNo" style="width:160px;"/>
						</div>
						<div class="right_content_select_box">
							<span class="right_content_select_name">序列号：</span> <input
								class="right_content_select_ctt right_content_select_cttt"
								placeholder="请输入序列号" type="text" id="sn" name="sn" style="width:160px;"/>
						</div>
						<div class="span6 right_content_select_box" style="white-space: nowrap;">
							<span class="right_content_select_name">区域中心：</span> 
							<select id="jdD" class="centerSelect" tt=edit>
								<option value=""></option>
								<c:forEach begin="100" end="120" var="i">
									<option value="${i }">${i }</option>
								</c:forEach>
							</select>
							<span class="centerSpan">°</span>
							<select id="jdF" class="centerSelect" tt=edit>
								<option value=""></option>
								<c:forEach begin="0" end="59" var="i">
									<option value="${i }">${i }</option>
								</c:forEach>
							</select>
							<span class="centerSpan">′</span>
							<select id="jdM" class="centerSelect" tt=edit>
								<option value=""></option>
								<c:forEach begin="0" end="59" var="i">
									<option value="${i }">${i }</option>
								</c:forEach>
							</select>
							<span class="centerSpan">″</span>
							<span>,</span>
							<select id="wdD" class="centerSelect" tt=edit>
								<option value=""></option>
								<c:forEach begin="15" end="21" var="i">
									<option value="${i }">${i }</option>
								</c:forEach>
							</select>
							<span class="centerSpan">°</span>
							<select id="wdF" class="centerSelect" tt=edit>
								<option value=""></option>
								<c:forEach begin="0" end="59" var="i">
									<option value="${i }">${i }</option>
								</c:forEach>
							</select>
							<span class="centerSpan">′</span>
							<select id="wdM" class="centerSelect" tt=edit>
								<option value=""></option>
								<c:forEach begin="0" end="59" var="i">
									<option value="${i }">${i }</option>
								</c:forEach>
							</select>
							<span class="centerSpan">″</span>
							<input type="hidden" name="center" id="center">
					  	</div>
						<div class="span4 right_content_select_box">
							<span class="right_content_select_name">半径（米）：</span> <input
								class="right_content_select_ctt right_content_select_cttt"
								placeholder="请输入半径" type="text" id="radius" name="radius" style="width:160px;"/>
						</div>
						<div class="span4 right_content_select_box">
							<span class="right_content_select_name">高度超过：</span> <input
								class="right_content_select_ctt right_content_select_cttt"
								placeholder="请输入高度" type="text" id="maxheiStr" name="maxheiStr" style="width:160px;"/>
					  </div>
		      	</form>	
				</div>
				<div class="right_content_btnbox">
					<div onclick="search()" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_search">
						<img src="${pageContext.request.contextPath }/images/search_btn.png"/>
						<span>搜索</span>
					</div>
					<div onclick="reset()" class="right_content_btnbox_btn right_content_btnbox_resize">
						<img src="${ctx }/images/resize_btn.png"/>
						<span>清空</span>
					</div>
					<div onclick="exportExcel()" class="right_content_btnbox_btn right_content_btnbox_search" style="position: relative;">
						<i class="fa fa-download" style="position: absolute; top: 8px; font-size: 14px;"></i>
						<span style="padding-left: 20px;">导出</span>
					</div>
				</div>
				<%--列表展示--%>
				<div class="right_content_table">
					<table class="table table-bordered table_list table-hover table-striped">
						<thead>
							<tr class="active blue_active_op">
								<th width=8%">ID</th>
								<th width="10%">登记号</th>
								<th width="10%">序列号</th>
								<th width="8%">计划</th>
								<th width="12%">起飞时间</th>
								<th width="12%">起飞坐标</th>
								<th width="12%">降落时间</th>
								<th width="12%">降落坐标</th>
								<th width="5%">最大高度</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="item in list">
								<td><a v-on:click="detail(item.flySeq)" href="javascript:void(0);">{{item.flySeq }}</a></td>
								<td>{{item.regNo }}</td>
								<td>{{item.sn }}</td>
								<td>{{item.planName == null || item.planName == '' ? '无' : '有'  }}</td>
								<td>{{item.begTime }}</td>
								<td>{{item.begLocDfm }}</td>
								<td>{{item.endTime }}</td>
								<td>{{item.endLocDfm }}</td>
								<td>{{item.maxhei }}</td>
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
<script type="text/javascript">
var layerIndex=null;
$(function(){
	init(1);

	$(window).resize(function() {
		if(layerIndex!=null){
			layer.full(layerIndex);
		}
	});
});

var vm=new Vue({
	el:'#app',
	data:{
		curPage: 1,
		pageSize: 10,
		list:null,
		totalCount:0
	},
	methods:{
		//空域详情
		detail:function(flySeq){
			layerIndex=layer.open({
				type:2,
				title:'飞行架次详情',
				content:'${ctx }/bizFlyDetail.action?flySeq='+flySeq,
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
	}
});

function closeDetailLayer(){
	if(layerIndex!=null){
		layer.close(layerIndex);
		layerIndex = null;
	}
}

function init(curPage){
	parent.openLoading();
    vm.curPage=curPage;
	$("#queryForm").ajaxSubmit({
		url:'${ctx}/bizFlyList.action',
        data:{
        	curPage: curPage
        },
        type:'post',
        dataType:'json',
        success:function(data){
            if(data.errCode=='0'){
				alert(data.errMsg);
                return;
            }
            vm.list=data.data.list;
            vm.totalCount=data.data.totalCount;
            vm.pageSize=data.data.pageSize;

        	parent.closeLoading();
        }
    });
}

function search(){
	var centerArry = [];
	$(".centerSelect").each(function(){
		var centerVal = $(this).val();
		if(centerVal != ''){
			centerArry.push(centerVal);
		}
	});
	if(centerArry.length == 0){
		//没输入
	}else if(centerArry.length == 6){
		//全都输入了
		var begTime=$('input[name=begTime]:first').val();
		var endTime=$('input[name=endTime]:first').val();
		if(begTime=='' || endTime==''){
			alert('请选择飞行架次时间');
			return;
		}
		var radius = $("#radius").val();
		if(radius == null || radius == ""){
			$("#radius").val(1000);
		}
		//109°29′38″,19°26′50″
		var center = centerArry[0]+"°"+centerArry[1]+"′"+centerArry[2]+"″,"+centerArry[3]+"°"+centerArry[4]+"′"+centerArry[5]+"″";
		$("#center").val(center);
	}else{
		//数据有问题
		alert("输入的区域中心坐标不完全！");
		return;
	}
	init(1);
}

function reset(){
	$("input").val("");
	$("select").val("");
	$('select:not([tt=edit])').select2();
}

function exportExcel(){
	var maxDownCount = 3000;
	var totalCount = vm.totalCount;
	if(totalCount>maxDownCount){
		alert("查询结果数据量超过"+maxDownCount+"，请补充查询条件点击查询后再导出");
		return;
	}
	window.location.href = "${ctx}/bizFlyExport.action";
}

//为查看详情用
var getWindowSize = function() {
	if (window.navigator.userAgent.indexOf("MSIE")>=1){
		var strs=new Array();
		strs[0]=$(document).height();
		strs[1]=$(document).width();
		return strs;
	}else{
		return ["Height", "Width"].map(function(a) {
			return window["inner" + a] || document.compatMode === "CSS1Compat" && document.documentElement["client" + a] || document.body["client" + a]
		});
	}
};

</script>
</html>
