<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>飞行计划许可函预览</title>
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
			ctx: ctx,
			note:null,
			wordVo:null,
			appFoot:null,
			unitList:null,
			plan:null,
			planSeq: null,
			planFlightList: null
		},
		methods:{
			init(){
				this.note = parent.vm.note;
                this.wordVo = parent.vm.wordVo;
                this.appFoot = parent.vm.appFoot;
                this.unitList = parent.vm.unitList;
                this.plan = parent.vm.plan;
                this.planSeq =  parent.vm.planSeq;
                this.planFlightList =  parent.vm.planFlightList;
                debugger
                console.log(this.appFoot)
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
