<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>操作权限列表</title>
	<%@ include file="../../tool.jsp"%>
	<script type="text/javascript">

	</script>
</head>
<body>
<div class="right_content">
	<lg:errors />
	<div class="right_content_all">
		<%--列表展示--%>
		<form action="${pageContext.request.contextPath }/menuList.action" method="post">
			<div class="right_content_table">
				<table class="table table-bordered table_list table-striped">
					<thead>
					<tr class="active blue_active_op">
						<th width="12%">菜单名称</th>
						<th width="">URL</th>
						<th width="5%">操作</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="item" items="${ menuList }">
						<tr>
							<td>${item.menuName }</td>
							<td>${item.url }</td>
							<td>
								<a href="${pageContext.request.contextPath }/pmsList_main.action?menuId=${item.menuId}">编辑</a>
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
