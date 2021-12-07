<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>飞行计划下发许可函</title>
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
        /*.form-horizontal .right_content_table{*/
            /*height: 100px;*/
        /*}*/
        .form-horizontal .right_content_table .control-group{
            float: left;
        }
        .form-horizontal .right_content_table .control-label{
            width: 100px;
            margin-top: 5px;
        }
        .form-horizontal .controls{
            margin-left: 110px;
        }
	</style>
  </head>
  <body>
   	<div id="app" class="right_content">
	   	<template>
            <div class="right_content_all">
                <!-- 下发许可函 -->
                <div class="right_content_all_top my-collapse" :href="'#'+panelId1">
                    <span>下发许可信息</span>
                </div>
                <form id="inputForm" method="post" enctype="multipart/form-data" class="form-horizontal" style="margin: 0;">
                    <div :id="panelId1" class="right_content_table">
                        <table style="width: 100%;">
                            <tr>
                                <td>
                                    <div class="control-group">
                                        <label class="control-label">通知单位：</label>
                                        <div class="controls">
                                            <div v-for="item in unitList">
                                                <input name="dictId" type="checkbox" :value="item.dictId" checked="checked"/>{{item.unitName}}
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="control-group">
                                        <label class="control-label">承办单位：</label>
                                        <div class="controls">
                                            <input name="cbdw" v-model="appFoot.cbdw" autocomplete="off" type="text" dataType="Limit" len="50" msg="请输入(1~50)个字符的国家！" maxlength="50" class="required" value=""/>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="control-group">
                                        <label class="control-label">联系人：</label>
                                        <div class="controls">
                                            <input name="lxr" v-model="appFoot.lxr" autocomplete="off" type="text" dataType="Limit" len="50" msg="请输入(1~50)个字符的国家！" maxlength="50" class="required" value=""/>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="control-group">
                                        <label class="control-label">电话：</label>
                                        <div class="controls">
                                            <input name="dh" v-model="appFoot.dh" autocomplete="off" type="text" dataType="Limit" len="50" msg="请输入(1~50)个字符的国家！" maxlength="50" class="required" value=""/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>
            <div class="right_content_btnbox ">
                <div @click="preview()" style="cursor:pointer;"
                     class="right_content_btnbox_btn right_content_btnbox_save">
                    <i class="fa fa-eye"></i> <span>预览</span>
                </div>
                <div @click="goSave()" style="cursor:pointer;"
                     class="right_content_btnbox_btn right_content_btnbox_save">
                    <img src="${ctx }/images/save_btn.png" /> <span>下发</span>
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
  <script>
	//Vue
	var vm=new Vue({
		el:'#app',
		data:{
			ctx: ctx,
            planSeq: parent.vm.selNoteSeq,
			unitList:[],
            appFoot:{
                cbdw:'',
                lxr:'',
                dh:'',
                stat: false,
                show: false
            },
            note:{},
            wordVo:{},
            unitList:[],
            rptFoot:[],
            plan:{},
			planFlightList: [],
            panelId1: new Date().getTime()+"_1",
		},
		methods:{
            goSave() {
                Confirm('确认下发许可吗？',()=>{
                    var flightBodySimple = this.planFlightList.map(function(item){
                        return item.flightBodySimple;
                    });
                    var myForm=document.getElementById("inputForm");
                    if(Validator.Validate(myForm,3)){
                        $("#inputForm").ajaxSubmit({
                            url:ctx+'/flyPlan/sendRpt.action',
                            type:'post',
                            data: {
                                planSeq: parent.vm.selNoteSeq,
                                flightBodySimple:flightBodySimple.toString()
                            },
                            dataType:'json',
                                success:(data)=>{
                                if(data.errCode=='1'){
                                    parent.vm.init();
                                }
                                parent.showMsg(data.errCode,data.errMsg);
                                parent.closeDetailLayer();
                            }
                        });
                    }
                });
            },
            preview(){
                layerIndex=layer.open({
                    type:2,
                    title:'飞行计划详情',
                    content: ctx + '/view/audit/preview.jsp',
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
			init(){
				var flightBodySimple = this.planFlightList.map(function(item){
					return item.flightBodySimple;
				});
				$.ajax({
					url:ctx+'/flyPlan/getWordContent.action',
					type:'post',
					data: {
						planSeq: parent.vm.selNoteSeq,
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
                        vm.planFlightList = data.data.planFlightList;
                        console.log(data.data.plan)
					}
				});
			},
			text(item){
				return (item)
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
	}
  </script>
</html>
