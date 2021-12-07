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
	$("input[type='text']").val("");
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
			<div class="right_content_all_top">
				<span>检索条件</span>
			</div>
			<div class="right_content_select">
			<form id="myNoteform" action="${pageContext.request.contextPath }/noteReportList.action" method="post">
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">民航许可号：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入民航许可号" type="text" id="permitNumber" name="permitNumber" value="${planInfo.permitNumber }" />
					</div>
					<div style="width: 24%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">状态：</span> 
						<select style="width: 150px;" id="status" name="status" >
							<option value="0">请选择</option>
							<option value="1">待申请</option>
							<option value="2">审核中</option>
							<option value="3">批准</option>
							<option value="4">驳回</option>
						</select>
					</div>
					<div style="width: 46%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">操作时间：</span> 
						<input style="width: 120px;" class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入起始时间" type="text" id="beginTime" readonly="readonly" name="beginTime" value="${sysoptlog.beginTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							-
						<input style="width: 120px;" class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入截止时间" type="text" id="endTime" readonly="readonly" name="endTime" value="${sysoptlog.endTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />	
				    </div>
			</div>
			<div class="right_content_btnbox">
				<div class="right_content_btnbox_btn right_content_btnbox_add"
					style="cursor:pointer;"
					onclick="javascript:window.location.href='${ctx }/to_add.action?userId=${userId}';">
					<img src="${pageContext.request.contextPath }/images/add_btn.png" />
					<span>导入</span>
				</div>
				<div onclick="reset()" class="right_content_btnbox_btn right_content_btnbox_resize">
					<img src="${ctx }/images/resize_btn.png"/>
					<span>清空</span>
				</div>
				<div onclick="search()" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_search">
					<img src="${pageContext.request.contextPath }/images/search_btn.png"/>
					<span>搜索</span>
				</div>
			</div>
			</div>
				<div class="right_content_table">
					<table class="table table-bordered table_list table-striped">
						<thead>
							<tr class="active blue_active_op">
								<th width="4%"></th>
								<th width="12%">照会编号</th>
								<th width="12%">民航许可号</th>
								<th width="10%">呼号</th>
								<th width="10%">国家</th>
								<th width="10%">照会号</th>
								<th width="12%">文件名</th>
								<th width="10%">状态</th>
								<th width="10%">创建时间</th>
								<th width="8%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${noteReportList }">
								<tr>
									<td width="10px">
									<c:if test="${item.status!=null && item.status==4}">
										<input value="${item.noteId }" name="noteId" type="checkbox">
									</c:if>
									</td>
									<td>${item.documentNum }</td>
									<td>${item.permitNumber }</td>
									<td>${item.callNumber }</td>
									<td>${item.nationality }</td>
									<td>${item.noteNo }</td>
									<td>${item.fileName }</td>
									<td>${item.status }</td>
									<td>${item.createTime }</td>
									<td>
										<a href="${pageContext.request.contextPath }/to_edit.action?userId=${item.noteId}">下载</a>
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
</body>
</html>
