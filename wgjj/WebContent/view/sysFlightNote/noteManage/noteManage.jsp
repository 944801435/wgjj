<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>照会文书管理</title>
<%@ include file="../../tool.jsp"%>
<script type="text/javascript">
function search(){
	$("#myNoteInfoform").submit();
}

function reset(){
	$("input[type='text']").val("");
}
	function goDelete() {
		alert(1);
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
		frm.action = '${pageContext.request.contextPath }/noteInfoDel.action';
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
			<form id="myNoteInfoform" action="${pageContext.request.contextPath }/noteInfoManageList.action" method="post">
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">文书编号：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入文书编号" type="text" id="documentNum" name="documentNum" value="${planInfo.documentNum }" />
					</div>
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">&nbsp;&nbsp;&nbsp;国籍：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入国籍" type="text" id="nationality" name="nationality" value="${planInfo.nationality }" />
					</div>
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">机型：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入机型" type="text" id="model" name="model" value="${planInfo.model }" />
					</div>
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">呼号：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入呼号" type="text" id="callNumber" name="callNumber" value="${planInfo.callNumber }" />
					</div>
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">&nbsp;&nbsp;&nbsp;入境点：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入入境点名称" type="text" id="entryName" name="entryName" value="${planInfo.entryName }" />
					</div>
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">出境点：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入出境点名称" type="text" id="exitName" name="exitName" value="${planInfo.exitName }" />
					</div>
					<div style="width: 24%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">状态：</span> 
						<select style="width: 138px;" id="status" name="status" >
							<option value="0">请选择</option>
							<option value="1">待申请</option>
							<option value="2">审核中</option>
							<option value="3">批准</option>
							<option value="4">驳回</option>
						</select>
					</div>
					<%-- <div style="width: 46%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">操作时间：</span> 
						<input style="width: 120px;" class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入起始时间" type="text" id="beginTime" readonly="readonly" name="beginTime" value="${sysoptlog.beginTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							-
						<input style="width: 120px;" class="span3 right_content_select_ctt right_content_select_cttt"
							placeholder="请输入截止时间" type="text" id="endTime" readonly="readonly" name="endTime" value="${sysoptlog.endTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />	
				    </div> --%>
			</div>
			<div class="right_content_btnbox">
				<div onclick="javascript:goDelete()"
					class="right_content_btnbox_btn right_content_btnbox_delete2"
					style="cursor:pointer;">
					<img src="${pageContext.request.contextPath }/images/delete2_btn.png" />
					<span>删除</span>
				</div>
				<div class="right_content_btnbox_btn right_content_btnbox_add"
					style="cursor:pointer;"
					onclick="javascript:window.location.href='${ctx }/to_note_info_add.action?userId=${userId}';">
					<img src="${pageContext.request.contextPath }/images/add_btn.png" />
					<span>添加</span>
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
			<input id="action" name="action" value="2" type="hidden"/>
				<div class="right_content_table">
					<table class="table table-bordered table_list table-striped">
						<thead>
							<tr class="active blue_active_op">
								<th width="4%"></th>
								<th width="12%">照会编号</th>
								<th width="11%">所属单位</th>
								<th width="12%">姓名</th>
								<th width="12%">电话</th>
								<th width="10%">国家</th>
								<th width="12%">照会号</th>
								<th width="9%">机型</th>
								<th width="10%">状态</th>
								<th width="5%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${noteList }">
								<tr>
									<td width="10px">
									<c:if test="${item.status==fns:findKey('sys_default_yes') }">
										<input value="${item.noteId }" name="noteId" type="checkbox">
									</c:if>
									</td>
									<td>${item.documentNum }</td>
									<td>${item.letterUnit }</td>
									<td>${item.personName }</td>
									<td>${item.telNo }</td>
									<td>${item.nationality }</td>
									<td>${item.noteNo }</td>
									<td>${item.model }</td>
									<td>
										<c:if test="${item.status!=null && item.status==1}">待申请</c:if>
										<c:if test="${item.status!=null && item.status==2}">审核中</c:if>
										<c:if test="${item.status!=null && item.status==3}">批准</c:if>
										<c:if test="${item.status!=null && item.status==4}">驳回</c:if>
									</td>
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
</body>
</html>
