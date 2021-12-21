<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>照会文书管理</title>
<%@ include file="../../tool.jsp"%>
 <script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
 <style type="text/css">
	.thead_tr_ths tr th {
		text-align: center;
	}
	.tr_tds td {
		text-align: center;
	}
</style>
<script type="text/javascript">
function search(){
	$("#myNoteInfoform").submit();
}

function reset(){
	$("input[type='text']").val("");
}
	function goDelete() {
		var hasChecked = 0;
		var frm = document.forms[1];
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
		}
		if (hasChecked) {
			Confirm("确定要删除选中的信息吗？", doDelete);
		} else {
			alert("请选择要删除的信息！");
		}
	}
	function goExport() {
	var chekLength=$("input:checkbox[name='noteIds']:checked").length;
	if(chekLength<1){
		window.alert("请选择要导出的信息！");
		return;
	}
	Confirm("确定导出？",(function(){
		var noteIds = "";
		$('input:checkbox[name=noteIds]:checked').each(function(i){
			if(0==i){
				noteIds = $(this).val();
			}else{
				noteIds += (","+$(this).val());
			}
		});
		$.ajax( {
			type : "POST",
			url : "${ctx}/batch_note_exportzip.action?noteIds="+noteIds,
			dataType:"json",
			cache:false,
			async:false,
			success : function(result) {
				let resp =eval(result);
				console.log(resp.data);
				window.alert(resp.message)
				if(resp.code=10001){
					window.location.href="${pageContext.request.contextPath }/preview.action?path="+resp.data;
				}
			}
		});
		}));
	}
	function goApply(id) {
		if (id != null) {
			Confirm('确认申请照会信息吗？',()=>{
				$.ajax( {
					type : "POST",
					url : "${ctx}/noteInfoApply.action?noteId="+id,
					dataType:"json",
					cache:false,
					async:false,
					success : function(result) {
						let resp =eval(result);
						if(resp.code=10001){
						window.alert(resp.message);
						location.reload();
						}
					}
				});
			});
		}else{
			return;
		}
	}
	function doDelete() {
		var frm = document.forms[1];
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
			<form id="myNoteInfoform" action="${pageContext.request.contextPath }/noteInfoManageList.action" method="post">
			<div class="right_content_select">
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">&nbsp;照&nbsp;会&nbsp;号：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入照会号" type="text" id="noteNo" name="noteNo" value="${planInfo.noteNo }" />
					</div>
					<div style="width: 23%;float: left;" class="span4 right_content_select_box">
						<span class="right_content_select_name">&nbsp;&nbsp;&nbsp;国家：</span> 
						<input style="width: 120px;" class="right_content_select_ctt right_content_select_cttt"
							placeholder="请输入国家" type="text" id="nationality" name="nationality" value="${planInfo.nationality }" />
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
						<span class="right_content_select_name">&nbsp;入&nbsp;境&nbsp;点：</span> 
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
							<option value="2">已申请</option>
							<option value="3">审核中</option>
							<option value="4">批准</option>
							<option value="5">驳回</option>
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
			</form>
			<div class="right_content_btnbox">
				<div onclick="javascript:goDelete()"
					class="right_content_btnbox_btn right_content_btnbox_delete2"
					style="cursor:pointer;">
					<img src="${pageContext.request.contextPath }/images/delete2_btn.png" />
					<span>删除</span>
				</div>
				<div onclick="javascript:goExport()"
					class="right_content_btnbox_btn right_content_btnbox_delete2"
					style="cursor:pointer;">
					<img src="${pageContext.request.contextPath }/images/file_icon.png" />
					<span>导出</span>
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
			<form action="">
				<div class="right_content_table">
					<table class="table table-bordered table_list table-striped">
						<thead class="thead_tr_ths">
							<tr class="active blue_active_op" >
								<th width="4%"></th>
								<th width="10%">照会号</th>
								<th width="10%">国家</th>
								<th width="10%">机型</th>
								<th width="11%">来电来函单位</th>
								<th width="10%">姓名</th>
								<th width="11%">电话</th>
								<th width="11%">照会编号</th>
								<th width="10%">状态</th>
								<th width="12%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${noteList }">
								<tr class="tr_tds">
									<td width="10px">
<%-- 									<c:if test="${item.status==fns:findKey('sys_default_yes') }"> --%>
									<input value="${item.noteId }" name="noteIds" type="checkbox">
<%-- 									</c:if> --%>
									</td>
									<td>${item.noteNo }</td>
									<td>${item.nationality }</td>
									<td>${item.model }</td>
									<td>${item.letterUnit }</td>
									<td>${item.personName }</td>
									<td>${item.telNo }</td>
									<td>${item.documentNum }</td>
									<td>
										<c:if test="${item.status!=null && item.status==1}">待申请</c:if>
										<c:if test="${item.status!=null && item.status==2}">已申请</c:if>
										<c:if test="${item.status!=null && item.status==3}">审核中</c:if>
										<c:if test="${item.status!=null && item.status==4}">批准</c:if>
										<c:if test="${item.status!=null && item.status==5}">驳回</c:if>
									</td>
									<td>
										<a href="${pageContext.request.contextPath }/to_edit_Plan.action?noteId=${item.noteId}">编辑</a>
										<a href="${pageContext.request.contextPath }/to_detail_Plan.action?noteId=${item.noteId}">详情</a>
										<c:if test="${item.status!=null && item.status==1}">
										<a onclick="goApply(${item.noteId});">申请</a>
										</c:if>
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
