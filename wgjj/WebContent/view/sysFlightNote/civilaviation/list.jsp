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
	function goDelete() {
		var hasChecked = 0;
		var frm = document.forms[0];
		if (frm.userIds != null) {
			if (frm.userIds.checked) {
				hasChecked = 1;
			} else {
				for ( var i = 0; i < frm.userIds.length; i++) {
					if (frm.userIds[i].checked == 1) {
						hasChecked = 1;
						break;
					}
				}
			}
			if (hasChecked) {
				Confirm("确定要删除选中的信息吗？", doDelete);
			} else {
				alert("请选择要删除的信息！");
			}
		}
	}

	function doDelete(noteId) {
		Confirm("确定删除？",(function(){
			$.ajax( {
				type : "POST",
				url : "${ctx}/civilAviation/deleteById.action?noteId="+noteId,
				dataType:"json",
				cache:false,
				async:false,
				success : function(result) {
					let resp =eval(result);
					window.alert(resp.message)
					if(resp.code=10001){
						search();
					}
				}
			});
		}));
	}
	function doExport(noteId) {
		Confirm("确定导出？",(function(){
			$.ajax( {
				type : "POST",
				url : "${ctx}/civilAviation/exportZip.action?noteId="+noteId,
				dataType:"json",
				cache:false,
				async:false,
				success : function(result) {
					let resp =eval(result);
					window.alert(resp.message)
					if(resp.code=10001){
						window.location="${ctx}/preview.action?path="+resp.data;
					}
				}
			});
		}));
	}
	function doBatchExport() {
		var chekLength=$("input:checkbox[name='noteId']:checked").length;
		if(chekLength<1){
			window.alert("请选择要导出的民航信息！");
			return;
		}
		Confirm("确定导出？",(function(){
				var noteIds = "";
				$('input:checkbox[name=noteId]:checked').each(function(i){
					if(0==i){
						noteIds = $(this).val();
					}else{
						noteIds += (","+$(this).val());
					}
				});
				$.ajax( {
					type : "POST",
					url : "${ctx}/civilAviation/batchExportZip.action?noteIds="+noteIds,
					dataType:"json",
					cache:false,
					async:false,
					success : function(result) {
						let resp =eval(result);
						window.alert(resp.message)
						if(resp.code=10001){
							window.location="${ctx}/preview.action?path="+resp.data;
						}
					}
				});
			}));
	}
	function userCheck(ths) {
		if (ths.checked == false) {
			$("input[name=all]:checkbox").prop('checked', false);
		}
		else {
			var count = $("input[name='noteId']:checkbox:checked").length;
			if (count == $("input[name='noteId']:checkbox").length) {
				$("input[name=all]:checkbox").prop("checked", true);
			}
		}
	}
	$(function(){
		<%--$("#status").val("${civilAviationParam.status}");--%>
		console.info("status value:"+$("#status").val()+"，civilAviation：${civilAviationParam.status}");
		var status='${civilAviationParam.status}';
		if(status){
			$('#status').val('${civilAviationParam.status}').trigger('change');
		}

		//全选,设置chheckbox name='all' tbody id=tb
		$("input[name=all]").click(function () {
			if (this.checked) {
				$("input[name=noteId]").prop("checked", true);
			} else {
				$("input[name=noteId]").prop("checked", false);
			}
		});
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
			<form id="myNoteform" action="${ctx }/civilAviation/list.action" method="post">
					<div style="width: 25%;float: left;" class="span6 right_content_select_box">
						<span class="right_content_select_name">照会号：</span>
						<input style="width: 65%;" class="right_content_select_ctt right_content_select_cttt"
							   placeholder="请输入照会编号" type="text" id="noteNo" name="noteNo" value="${civilAviationParam.noteNo }" />
					</div>
					<div style="width: 25%;float: left;" class="span5 right_content_select_box">
						<span class="right_content_select_name">国家：</span>
						<input style="width: 65%;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入国家名称" type="text" name="nationality" value="${civilAviationParam.nationality }" />
					</div>
					<div style="width: 25%;float: left;" class="span5 right_content_select_box">
						<span class="right_content_select_name">时间：</span>
						<input style="width: 65%;" class="right_content_select_ctt right_content_select_cttt"
							   placeholder="请输入时间" type="text" class="wpc95 Wdate"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true})" name="flightTime" value="${civilAviationParam.flightTime }" />
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
				<c:if test="${'true'==fns:hasPms(pmsIds,'50201')}">
					<div onclick="javascript:doBatchExport()"
						 class="right_content_btnbox_btn right_content_btnbox_delete2"
						 style="cursor:pointer;">
						<img src="${ctx }/images/file_icon.png" />
						<span>导出民航意见</span>
					</div>
				</c:if>
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
								<th width="2%">
									<input type="checkbox" name="all">
								</th>
								<th width="12%">照会号</th>
								<th width="7%">民航许可号</th>
								<th width="10%">航线信息</th>
								<th width="8%">飞行时间</th>
								<th width="10%">国家</th>
								<th width="8%">机型</th>
								<th width="8%">来电来函单位</th>
								<th width="5%">姓名</th>
								<th width="6%">电话</th>
								<th width="6%">照会编号</th>
								<th width="2%">民航是否回复</th>
								<th width="4%">状态</th>

								<th width="11%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${pagerVO.items }">
								<tr  style="text-align: center;">
									<td>
										<c:if test="${item.status!=null && item.status==2 && not empty item.permitNumber }">
											<input type="checkbox" name="noteId" value="${item.noteId }" onclick="userCheck(this)">
										</c:if>
									</td>
									<td>${item.noteNo }</td>
									<td>${item.permitNumber }</td>
									<td>${item.routeInfo }</td>
									<td>${item.flightTime }</td>
									<td>${item.nationality }</td>
									<td>${item.model }</td>
									<td>${item.letterUnit }</td>
									<td>${item.personName }</td>
									<td>${item.telNo }</td>
									<td>${item.documentNum }</td>
									<td>
										<c:choose>
											<c:when test="${empty item.permitNumber}">
												否
											</c:when>
											<c:otherwise>是</c:otherwise>
										</c:choose>
									</td>
									<td>
										<%--<c:if test="${item.status!=null && item.status==1}">待申请</c:if>--%>
										<c:if test="${item.status!=null && item.status==2}">已申请</c:if>
										<c:if test="${item.status!=null && item.status==3}">审核中</c:if>
										<c:if test="${item.status!=null && item.status==4}">批准</c:if>
										<c:if test="${item.status!=null && item.status==5}">驳回</c:if>
									</td>

									<td>
										<a href="${ctx}/civilAviation/detail.action?noteId=${item.noteId }">详情</a>
										<c:if test="${item.status!=null && item.status!=4}">
											<c:if test="${'true'==fns:hasPms(pmsIds,'50202')}">
												<a href="${ctx }/civilAviation/reply.action?noteId=${item.noteId }">回复</a>
											</c:if>
										</c:if>
										<!-- 状态是待申请并且状态已经回复，可以导出 -->
										<%--<c:if test="${item.status!=null && item.status==2 && not empty item.permitNumber}">
											<a href="javascript:void(0);" onclick="doExport('${item.noteId }')">导出</a>
										</c:if>--%>
										<c:if test="${item.status!=null && item.status==2}">
											<a href="javascript:void(0);" onclick="doDelete('${item.noteId }')">删除</a>
										</c:if>

										<%--<div class="btn-group">
											&lt;%&ndash;<button type="button" class="btn btn-default btn-sm" >详情</button>&ndash;%&gt;
											<a class="btn btn-default  btn-sm" href="${ctx}/civilAviation/detail.action?noteId=${item.noteId }">详情</a>
											<button type="button" class="btn btn-default dropdown-toggle btn-sm"
													data-toggle="dropdown">
												<span class="caret"></span>
												<span class="sr-only">切换下拉菜单</span>
											</button>
											<ul class="dropdown-menu" role="menu">
												<c:if test="${item.status!=null && item.status!=4}">
													<li><a href="${ctx }/civilAviation/reply.action?noteId=${item.noteId }">回复</a></li>
												</c:if>
												<!-- 状态是待申请并且状态已经回复，可以导出 -->
												<c:if test="${item.status!=null && item.status==2 && not empty item.permitNumber}">
													<li><a href="javascript:void(0);" onclick="doExport('${item.noteId }')">导出</a></li>
												</c:if>
												<c:if test="${item.status!=null && item.status==2}">
													<li><a href="javascript:void(0);" onclick="doDelete('${item.noteId }')">删除</a></li>
												</c:if>

											</ul>
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
