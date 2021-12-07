<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>飞行计划建议稿预览</title>
	<%@ include file="../tool.jsp"%>
	<script type="text/javascript" src="${ctx }/js/validate.js"></script>
	  <script type="text/javascript" src="${ctx }/view/component/allComponent.js"></script>
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
   	<div id="app">
	   	<template>
	   		<!-- 表格 -->
			<audit-form :note="note" :plan="plan" :wordvo="wordVo" :appfoot="appFoot" :unitlist="unitList" :rptfoot="appFoot" title="审批表"></audit-form>
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
			note:{},
			wordVo:{},
			appFoot:{
				cbdw:'',
				lxr:'',
				dh:'',
				stat: true,
				show: true
			},
			unitList:[],
			rptFoot:[],
			plan:{},
			planSeq: null,
			planFlightList: parent.vm.planFlightList
		},
		methods:{
			init(){
				var flightBodySimple = this.planFlightList.map(function(item){
					return item.flightBodySimple;
				});
				$.ajax({
					url:ctx+'/flyPlan/getWordContent.action',
					type:'post',
					data: {
						planSeq: parent.vm.planSeq,
						flightBodySimple:flightBodySimple.toString()
					},
					dataType:'json',
					success:function(data){
						if(data.errCode=='0'){
							showMsg(data.errCode,data.errMsg);
							return;
						}
						vm.note=data.data.note;
						vm.wordVo=data.data.wordVo;
						vm.rptFoot=data.data.rptFoot;
						vm.unitList=data.data.unitList;
						vm.plan=data.data.plan;
					}
				});
			},
			text(item){
				return (item)
			}
		},
		created(){
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
