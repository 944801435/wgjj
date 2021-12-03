<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>照会文书管理</title>
<%@ include file="../../tool.jsp"%>
<script type="text/javascript">

	function goDelete() {
		var hasChecked = 0;
		var frm = document.forms[0];
		if (frm.noteIds != null) {
			if (frm.noteIds.checked) {
				hasChecked = 1;
			} else {
				for ( var i = 0; i < frm.noteIds.length; i++) {
					if (frm.noteIds[i].checked == 1) {
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

	function doDelete() {
		var frm = document.forms[0];
		frm.action = '${pageContext.request.contextPath }/noteManageDel.action';
		frm.submit();
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
					onclick="javascript:window.location.href='${ctx }/to_add.action?userId=${userId}';">
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
			<form action="${pageContext.request.contextPath }/noteManageList.action" method="post">
			<input id="action" name="action" value="2" type="hidden"/>
				<div class="right_content_table">
					<table class="table table-bordered table_list table-striped">
						<thead>
							<tr class="active blue_active_op">
								<th width="4%"></th>
								<th width="12%">照会编号</th>
								<th width="12%">民航许可号</th>
								<th width="11%">所属单位</th>
								<th width="12%">姓名</th>
								<th width="12%">电话</th>
								<th width="14%">国家/照会号</th>
								<th width="9%">机型</th>
								<th width="10%">状态</th>
								<th width="5%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${noteList }">
								<tr>
									<td width="10px">
										<input value="${item.noteId }" name="noteId" type="checkbox">
									</td>
									<td>${item.documentNum }</td>
									<td>${item.noteNo }</td>
									<td>${item.letterUnit }</td>
									<td>${item.personName }</td>
									<td>${item.telNo }</td>
									<td>${item.nationality }</td>
									<td>${item.noteNo }</td>
									<td>${item.model }</td>
									<td>${item.model }</td>
									<td>
										<a href="${pageContext.request.contextPath }/to_edit.action?userId=${item.noteId}">编辑</a>
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
