<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>中和人员列表</title>
<%@ include file="../../tool.jsp"%>
<script type="text/javascript">

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
				Confirm("确定要删除选中的用户吗？", doDelete);
			} else {
				alert("请选择要删除的用户！");
			}
		}
	}

	function doDelete() {
		var frm = document.forms[0];
		frm.action = '${pageContext.request.contextPath }/userDel.action';
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
					onclick="javascript:window.location.href='${pageContext.request.contextPath }/view/system/user/user_add_main.jsp';">
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
			<form action="${pageContext.request.contextPath }/userList.action" method="post">
				<div class="right_content_table">
					<table class="table table-bordered table_list table-striped">
						<thead>
							<tr class="active blue_active_op">
								<th width="4%"></th>
								<th width="12%">登录名</th>
								<th width="12%">用户名</th>
								<th width="14%">手机</th>
								<th>机构</th>
								<th width="14%">最后登录IP</th>
								<th width="14%">最后登录时间</th>
								<th width="12%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${userList }">
								<tr>
									<td width="10px">
										<c:if test="${item.isAdmin==fns:findKey('sys_default_no') }">
											<input value="${item.userId }" name="userIds" type="checkbox">
										</c:if>
									</td>
									<td>${item.userName }</td>
									<td>${item.userDesc }</td>
									<td>${item.phone }</td>
									<td>${item.deptName }</td>
									<td>${item.lastIp }</td>
									<td>${item.lastTime }</td>
									<td>
										<a href="${pageContext.request.contextPath }/view/system/user/user_edit_main.jsp?userId=${item.userId}">编辑</a>
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
