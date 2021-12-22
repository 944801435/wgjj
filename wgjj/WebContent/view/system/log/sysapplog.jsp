<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
     <title>APP服务日志</title>	
     <%@ include file="../../tool.jsp"%>
	 <script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
	 <script type="text/javascript">
	 	function search(){
			$("#myform").submit();
		}
		
		function reset(){
			$("input[type='text']").val("");
		}
	 </script>
	 <style>
	 .table>thead>tr>th,.table>tbody>tr>td{
			word-wrap:break-word;
			word-break:break-all;
			padding: 2px 0 2px 5px;
		}
	 </style>
  </head>
  <body>
   	<div class="right_content">
       	<lg:errors/>
		<div class="right_content_all">
			<div class="right_content_all_top">
				<span>检索条件</span>
			</div>
			<div class="right_content_select">
				<form id="myform" action="${pageContext.request.contextPath }/sysapplog_list.action" method="post">
					<div class="span4 right_content_select_box">
						<span class="right_content_select_name">用户名称：</span> <input
							class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入用户名称" type="text" id="userName" name="userName" value="${sysapplog.userName }" />
					</div>
					<div class="span8 right_content_select_box">
						<span class="right_content_select_name">报文时间：</span> 
						<input class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入起始时间" type="text" id="beginTime" readonly="readonly" name="beginTime" value="${sysapplog.beginTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							-
						<input class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入截止时间" type="text" id="endTime" readonly="readonly" name="endTime" value="${sysapplog.endTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />	
				    </div>
	      		</form>	
			</div>
			<div class="right_content_btnbox">
				<div onclick="search()" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_search">
					<img src="${pageContext.request.contextPath }/images/search_btn.png"/>
					<span>搜索</span>
				</div>
				<div onclick="reset()" class="right_content_btnbox_btn right_content_btnbox_resize">
					<img src="${ctx }/images/resize_btn.png"/>
					<span>清空</span>
				</div>
			</div>
			<div class="right_content_table">
		         <table class="table table-bordered table_list table-striped">
					<thead>
						<tr class="active blue_active_op">
							<th width="14%">报文时间</th>
							<th width="8%">用户名称</th>
							<th width="8%">操作状态</th>
							<th width="15%">操作模块</th>
							<!-- <th width="14%">请求类别</th> -->
							<th>请求报文</th>
							<th>响应报文</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${sysapploglist }" varStatus="status">
							<tr>
								<td>${item.optTime }</td>
								<td>${item.userName }</td>
								<td>${item.opState }</td>
								<td>${item.optObj }</td>
								<%-- <td>${item.optType }</td> --%>
								<td>${item.reqMsg }</td>
								<td>${item.rspMsg }</td>
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
