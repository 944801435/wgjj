<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>违规信息列表</title>
<%@ include file="../tool.jsp"%>
<script type="text/javascript">

	function goDelete() {
		var hasChecked = 0;
		var frm = document.forms[0];
		if (frm.vioIds != null) {
			if (frm.vioIds.checked) {
				hasChecked = 1;
			} else {
				for ( var i = 0; i < frm.vioIds.length; i++) {
					if (frm.vioIds[i].checked == 1) {
						hasChecked = 1;
						break;
					}
				}
			}
			if (hasChecked) {
				Confirm("确定要删除选中的违规信息吗？", doDelete);
			} else {
				alert("请选择要删除的违规信息！");
			}
		}
	}

	function doDelete() {
		var frm = document.forms[0];
		frm.action = '${pageContext.request.contextPath }/violationDel.action';
		frm.submit();
	}
	function goValidSts(vioId) {
		Confirm("确定要更改状态吗？", doValidSts(vioId));
	}
	function doValidSts(vioId) {
		//ajax提交保存
		$.ajax({
			url:'${ctx}/violationStsEdit.action',
			type:'post',
			data:{'vioId':vioId},
			dataType:'json',
			success:function(data){
				alert(data.errMsg);
				window.location.reload(true);
			}
		});
	}
</script>
</head>
<body>
	<div class="right_content">
		<lg:errors />
		<div class="right_content_all">
			<%--列表展示--%>
			<div class="right_content_btnbox">
				<div class="right_content_btnbox_btn right_content_btnbox_add"
					 style="cursor:pointer;"
					 onclick="javascript:window.location.href='${pageContext.request.contextPath }/to_violation_add.action';">
					<img src="${pageContext.request.contextPath }/images/add_btn.png" />
					<span>添加</span>
				</div>
				<div onclick="javascript:goDelete()"
					class="right_content_btnbox_btn right_content_btnbox_delete2"
					style="cursor:pointer;">
					<img src="${pageContext.request.contextPath }/images/delete2_btn.png" />
					<span>删除</span>
				</div>
			</div>
			<form action="${pageContext.request.contextPath }/violationList.action" method="post">
				<div class="right_content_table">
					<table class="table table-bordered table_list table-striped">
						<thead>
							<tr class="active blue_active_op">
								<th width="4%"></th>
								<th width="10%">国家</th>
								<th width="10%">机型</th>
								<th width="10%">呼号</th>
								<th>违规信息</th>
								<th width="15%">违规时间</th>
								<th width="10%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${ violationList }">
								<tr>
									<td width="10px">
										<input value="${item.vioId }" name="vioIds" type="checkbox">
									</td>
									<td>${item.nationality }</td>
									<td>${item.model }</td>
									<td>${item.callSign }</td>
									<td>${item.info }</td>
									<td>${item.planDate }</td>
									<td>
										<a href="${pageContext.request.contextPath }/to_violation_edit.action?vioId=${item.vioId}">编辑</a>
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
								totalCount : '${totalCount}', //总条数
								pageSize : '${pageSize}', //每页显示n条内容，默认10
								buttonSize : 5, //显示6个按钮，默认10
								pageParam : 'curPage', //页码的参数名为'curPage'，默认为'page'
								pageValue : '${curPage}',
								className : 'pagination', //分页的样式
								prevButton : '上一页', //上一页按钮
								nextButton : '下一页', //下一页按钮
								firstButton : '首页', //第一页按钮
								lastButton : '末页', //最后一页按钮
								pageForm : $('form')[0]  //提交的form
	
							}));
						</script>
					</div>
					<span class="pagination_sub">显示<span>${(curPage-1)*pageSize+1
							}</span>-<span>${curPage*pageSize }</span>条，共<span>${totalCount }</span>条
					</span>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
