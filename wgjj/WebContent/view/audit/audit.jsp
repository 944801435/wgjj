<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>飞行计划审批</title>
<%@ include file="../tool.jsp"%>
<script type="text/javascript" src="${ctx }/js/validate.js"></script>
<script type="text/javascript" src="${ctx }/view/component/allComponent.js"></script>
<style scoped>
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
		margin: 4px 4px;
	}
</style>
</head>
<body>
	<div id="app" class="right_content">
		<template>
			<div class="right_content_all">
				<!-- 飞行计划基础信息 -->
				<div class="right_content">
					<div class="right_content_all">
						<div class="right_content_table">
							<div class="table_title">
								<h5>飞行计划审批意见：</h5>
							</div>
                            <form id="inputForm" method="post" enctype="multipart/form-data" class="form-horizontal" style="margin: 0;">
                                <table class="table table-bordered table_list" style="width: 100%;" align="center">
                                    <tr>
                                        <td style="width: 45px;height: 0px;" v-for="item in 6"></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>审批意见</label>
                                        </td>
                                        <td colspan="5">
                                            <label>
                                                <input name="planSts" type="radio" v-model="audit.planSts" :value="biz_space_req_approve"/>批准
                                                <input name="planSts" type="radio" v-model="audit.planSts" :value="biz_space_req_reject"/>驳回
                                            </label>
                                            <div>
                                                <textarea name="rejectMsg" rows="4" v-model="audit.rejectMsg" style="width: 400px;"></textarea>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label>审批表扫描件</label>
                                        </td>
                                        <td colspan="5">
                                            <%--<span>点击上传</span>--%>
                                            <input id="file" type="file" name="file" multiple="multiple" @change="getFileOthers">
                                            <div class="fileBox">
                                                <div class="fileBox_item" v-for="(item,index) in audit.srcList">
                                                    <img :id="item.id" :src="item.img" alt="123123123" width="100px" height="100px">
                                                    <div>
                                                        <span>{{item.name}}</span>
                                                    </div>
                                                    <div class="file_i"><i class="fa fa-remove" @click="del(index)"></i></div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </form>
						</div>
					</div>
				</div>
				<%--<div class="fileBox-word">--%>
					<%--<span class="fileinput-word">扫描件上传</span>--%>
				<%--</div>--%>
			</div>
			<div class="right_content_btnbox">
				<div @click="goSave()" style="cursor:pointer;"
					 class="right_content_btnbox_btn right_content_btnbox_save">
					<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
				</div>
				<div onclick="parent.closeDetailLayer()" 
					class="right_content_btnbox_btn right_content_btnbox_return " style="float: right;">
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
			srcOthers: null,
			audit:{
				planSeq:parent.vm.selNoteSeq,
				planSts:"${fns:findKey('BIZ_SPACE_REQ_APPROVE')}",
				rejectMsg:null,
                srcList:[]
			},
            biz_space_req_approve: "${fns:findKey('BIZ_SPACE_REQ_APPROVE')}",
            biz_space_req_reject: "${fns:findKey('BIZ_SPACE_REQ_REJECT')}"
		},
		methods: {
			goSave() {
                Confirm('确认审核该飞行计划吗？',()=>{
                    var formData = new FormData($('#inputForm')[0]);
                    formData.delete('file');
                    formData.append('planSeq',vm.audit.planSeq);
                    for (var i = 0;i<vm.audit.srcList.length;i++){
                        formData.append('file',vm.audit.srcList[i].file);
                    }
                    var myForm=document.getElementById("inputForm");
                    if(Validator.Validate(myForm,3)){
                        $.ajax({
                            url:ctx+'/flyPlan/audit.action',
                            type:'post',
                            data: formData,
                            dataType:'json',
                            contentType:false,
                            processData:false,
                            success:(data)=>{
                                if(data.errCode=='1'){
                                    parent.vm.init();
                                    parent.closeDetailLayer();
                                }
                                parent.showMsg(data.errCode,data.errMsg);
                            }
                        });
                    }
                });
			},
			getFileOthers (e) {//附件预览----
				let _this = this
                if(e.target.files.length == 0){
                    return;
                }else {
                    vm.audit.srcList = [];
                    for (var i = 0;i<e.target.files.length;i++){
                        var files = e.target.files[i];
                        if (files == null){
                            return;
                        }
                        if (!e || !window.FileReader) return  // 看支持不支持FileReader
                        let reader = new FileReader()
                        reader.file = files;
                        reader.readAsDataURL(files) // 这里是最关键的一步，转换就在这里
                        reader.onloadend = function () {
                            var obj = {
                                img:this.result,
                                file:this.file,
                                name:this.file.name,
                                id:"img_"+vm.audit.srcList.length+i
                            }
                            vm.audit.srcList.push(obj);
                            $("#file").val(null);
                        }
                    }
                }
			},
			del(id){
				this.audit.srcList.splice(id,1);
			}
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