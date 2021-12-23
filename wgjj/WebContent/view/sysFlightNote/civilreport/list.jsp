<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>民航信息管理</title>
<%@ include file="../../tool.jsp"%>
<script type="text/javascript">
	function search(){
		$("#myNoteform").submit();
	}

	function reset(){
		$("#myNoteform")[0].reset();
		$("#myNoteform input[type='text']").val("");
		$('#status').val(0).trigger('change');
	}
	function upload(){
		$("#uploadFile").trigger("click");
		$("#uploadFile").change(function(){
			let file=$(this)[0].files[0];
			if(!file){
				window.alert("文件不能为空!")
				return
			}
			let formData = new FormData();
			formData.append('zipFile',file); //上传压缩包
			$.ajax({
				url : "${ctx}/civilReport/uploadZipFile.action",
				type : 'POST',
				data : formData,
				cache : false,
				processData : false,
				contentType : false,
				dataType: 'json',
				success : function(result) {
					let resp =eval(result);
					window.alert(resp.message)
					if(resp.code=10001){
						search();
					}
				}
			});
		});
	}
	$(function(){
		<%--$("#status").val("${civilAviationParam.status}");--%>
		console.info("status value:"+$("#status").val()+"，civilReportParam：${civilReportParam.status}");
		var status='${civilReportParam.status}';
		if(status){
			$('#status').val('${civilReportParam.status}').trigger('change');
		}

	});
</script>
</head>
<style>
	.right_content_table tbody tr td{
		text-align: center!important;
	}
	.right_content_table thead th{
		text-align: center!important;
	}
</style>
<body>
	<div class="right_content">
		<lg:errors />
		<div class="right_content_all">
			<%--列表展示--%>
			<div class="right_content_all_top">
				<span>检索条件</span>
			</div>
			<div class="right_content_select">
			<form id="myNoteform" action="${ctx }/civilReport/list.action" method="post">
				<div style="width: 25%;float: left;" class="span6 right_content_select_box">
					<span class="right_content_select_name">照会号：</span>
					<input style="width: 65%;" class="right_content_select_ctt right_content_select_cttt"
						   placeholder="请输入照会编号" type="text" id="noteNo" name="noteNo" value="${civilReportParam.noteNo }" />
				</div>
				<div style="width: 25%;float: left;" class="span5 right_content_select_box">
					<span class="right_content_select_name">国家：</span>
					<input style="width: 65%;" class="right_content_select_ctt right_content_select_cttt"
						   placeholder="请输入国家名称" type="text" name="nationality" value="${civilReportParam.nationality }" />
				</div>
				<div style="width: 25%;float: left;" class="span5 right_content_select_box">
					<span class="right_content_select_name">时间：</span>
					<input style="width: 65%;" class="right_content_select_ctt right_content_select_cttt"
						   placeholder="请输入时间" type="text" class="wpc95 Wdate"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true})" name="flightTime" value="${civilReportParam.flightTime }" />
				</div>
				<div style="width: 20%;float: left;display: flex" class="span6">
					<span style="width: 80px;margin-top:5px">状态：</span>
					<select class="form-control select"  style="width: 100%;" id="status" name="status">
						<option value="0">请选择</option>
						<option value="2">已申请</option>
						<option value="3">审核中</option>
						<option value="4">批准</option>
						<option value="5">驳回</option>
					</select>
				</div>
					<%--<div style="width: 46%;float: left;" class="span8 right_content_select_box">
						<span class="right_content_select_name">操作时间：</span> 
						<input style="width: 120px;" class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入起始时间" type="text" id="beginTime" readonly="readonly" name="beginTime" value="${sysoptlog.beginTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							-
						<input style="width: 120px;" class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入截止时间" type="text" id="endTime" readonly="readonly" name="endTime" value="${sysoptlog.endTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />	
				    </div>--%>
	      		</form>	
			</div>
			<div class="right_content_btnbox">
				<div onclick="upload();"
					 class="right_content_btnbox_btn right_content_btnbox_delete2"
					 style="cursor:pointer;">
					<img src="${ctx }/images/file_icon.png" />
					<span>导入审批许可</span>
				</div>
				<input type="file" hidden name="zipFile"  id="uploadFile" />
				<%--<div onclick="javascript:goDelete()"
					class="right_content_btnbox_btn right_content_btnbox_delete2"
					style="cursor:pointer;">
					<img src="${ctx}/images/delete2_btn.png" />
					<span>删除</span>
				</div>--%>
				<%--<div class="right_content_btnbox_btn right_content_btnbox_add"
					style="cursor:pointer;"
					onclick="javascript:window.location.href='${ctx }/to_add.action?userId=${userId}';">
					<img src="${ctx }/images/add_btn.png" />
					<span>添加</span>
				</div>--%>
				<div onclick="reset()" class="right_content_btnbox_btn right_content_btnbox_resize">
					<img src="${ctx}/images/resize_btn.png"/>
					<span>清空</span>
				</div>
				<div onclick="search()" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_search">
					<img src="${ctx}/images/search_btn.png"/>
					<span>搜索</span>
				</div>
			</div>
			</div>
				<div class="right_content_table">
					<table class="table table-bordered table_list table-striped">
						<thead>
							<tr class="active blue_active_op">
								<th width="10%">照会号</th>
								<th width="10%">国家</th>
								<th width="9%">机型</th>
								<th width="15%">来电来函单位</th>
								<th width="11%">民航许可号</th>
								<th width="5%">姓名</th>
								<th width="11%">电话</th>
								<th width="6%">照会编号</th>
								<th width="8%">时间</th>
								<th width="5%">状态</th>
								<th width="9%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${pagerVO.items }">
								<tr>
									<td>${item.planInfo.noteNo }</td>
									<td>${item.planInfo.nationality }</td>
									<td>${item.planInfo.model }</td>
									<td>${item.planInfo.letterUnit }</td>
									<td>${item.planInfo.permitNumber }</td>
									<td>${item.planInfo.personName }</td>
									<td>${item.planInfo.telNo }</td>
									<td>${item.planInfo.documentNum }</td>
									<td>${item.planInfo.flightTime }</td>

									<td>
										<c:if test="${item.planInfo.status!=null && item.planInfo.status==2}">已申请</c:if>
										<c:if test="${item.planInfo.status!=null && item.planInfo.status==3}">审核中</c:if>
										<c:if test="${item.planInfo.status!=null && item.planInfo.status==4}">批准</c:if>
										<c:if test="${item.planInfo.status!=null && item.planInfo.status==5}">驳回</c:if>
									</td>
									<td>
										<c:if test="${not empty item.noteReport.filePath}">
											<a  href="${ctx}/onlinePreview.action?path=${item.noteReport.filePath}" target="_blank" >详情</a>
											<a  href="${ctx}/preview.action?path=${item.noteReport.filePath}" target="_blank" >下载</a>
										</c:if>
										<%--<div class="btn-group">
											&lt;%&ndash;<button type="button" class="btn btn-default btn-sm" >详情</button>&ndash;%&gt;
											<c:if test="${not empty item.noteReport.filePath}">
											<a class="btn btn-default  btn-sm" onclick="window.open('${ctx}/onlinePreview.action?path=${item.noteReport.filePath}')">详情</a>
											<a class="btn btn-default  btn-sm" onclick="window.location='${ctx}/preview.action?path=${item.noteReport.filePath}'">下载</a>
											</c:if>
										</div>--%>

									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="right_content_pagination">
					<div class="pagination">
						<script>
							document.write(Pager({
								totalCount : '${pagerVO.counts}', //总条数
								pageSize : '${pagerVO.pageSize}', //每页显示n条内容，默认10
								buttonSize : 6, //显示6个按钮，默认10
								pageParam : 'curPage', //页码的参数名为'curPage'，默认为'page'
								pageValue : '${pagerVO.pageNo}',
								className : 'pagination', //分页的样式
								prevButton : '上一页', //上一页按钮
								nextButton : '下一页', //下一页按钮
								firstButton : '首页', //第一页按钮
								lastButton : '末页', //最后一页按钮
								pageForm : $('form')[0]  //提交的form
	
							}));
						</script>
					</div>
					<%--<span class="pagination_sub">
						显示<span>${pagerVO.pageNo}</span>-<span>${pagerVO.pages}</span>条，共<span>${pagerVO.counts }</span>条
					</span>--%>
				</div>
			</form>
		</div>
	</div>

</body>
</html>
