<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
     <title>系统配置参数查询</title>	
     <%@ include file="../../tool.jsp"%>
	 <script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
  </head>
  <body>
   	<div class="right_content">
       	<lg:errors/>
		<div class="right_content_all">
			<form action="${ctx }/sysConfigList.action" method="post"></form>
			<div class="right_content_table">
		         <table class="table table-bordered table_list table-striped">
					<thead>
						<tr class="active blue_active_op">
							<th width="20%">参数名</th>
							<th width="20%">参数值</th>
							<th>描述</th>
							<th width="10%">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${sysconfiglist }" varStatus="status">
							<tr>
								<td>${item.cfgId }</td>
								<td>${item.cfgValue }</td>
								<td>${item.cfgDesc }</td>
								<td>
									<a href="${pageContext.request.contextPath }/sysConfigToEdit.action?cfgId=${item.cfgId}">编辑</a>
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
						pageForm:$("form")[0]
	
					}));
				</script>
				</div>
				<span class="pagination_sub">显示<span>${(curPage-1)*pageSize+1 }</span>-<span>${curPage*pageSize }</span>条，共<span>${totalCount }</span>条</span>
			</div>
		</div>
	</div>
  </body>
</html>
