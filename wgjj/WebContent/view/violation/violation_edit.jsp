<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>违规信息权限</title>
	<%@ include file="../tool.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/md5.min.js"></script>
	<script type="text/javascript">
		function goSave() {
			var myForm=document.getElementById("inputForm");
			if(Validator.Validate(myForm,3)){
				$("#inputForm").submit();
			}
		}
	</script>
</head>
<body>
<form id="inputForm" action="${pageContext.request.contextPath }/violationEdit.action" method="post" class="form-horizontal">
	<input type="hidden" name="vioId" value="${violation.vioId }" />
	<div class="right_content">
		<lg:errors />
		<div class="right_content_all">
			<div class="right_content_all_top">
				<span>修改违规信息</span>
			</div>
			<div class="right_content_select">
				<div class="control-group">
					<label class="control-label">国家：</label>
					<div class="controls">
						<input id="nationality" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的国家名称！" maxlength="50" class="required" name="nationality" value="${violation.nationality }"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">机型：</label>
					<div class="controls">
						<input id="model" autocomplete="off" type="text" dataType="Limit" len="200" msg="请输入(1~200)个字符！" maxlength="200" class="required" name="model" value="${violation.model }"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">违规时间：</label>
					<div class="controls">
						<input type="text" id="planDate" name="planDate" style="width:150px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${violation.planDate }"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">呼号：</label>
					<div class="controls">
						<input id="callSign" autocomplete="off" type="text" dataType="Limit" len="20" msg="请输入(1~20)个字符！" maxlength="20" class="required" name="callSign" value="${violation.callSign }"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">违规信息：</label>
					<div class="controls">
						<textarea rows="5" cols="30" id="info" name="info" value="" type="text" dataType="Limit" len="500" msg="请输入(1~500)个字符！" maxlength="500">${violation.info }</textarea>
					</div>
				</div>
			</div>
			<div class="right_content_btnbox ">
				<div onclick="javascript:goSave();" style="cursor:pointer;"
					 class="right_content_btnbox_btn right_content_btnbox_save">
					<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
				</div>
				<div onclick="window.location.href='${ctx}/violationList.action'" style="cursor:pointer;"
					 class="right_content_btnbox_btn right_content_btnbox_return">
					<img src="${pageContext.request.contextPath }/images/return_btn.png" />
					<span>返回</span>
				</div>
			</div>
		</div>
	</div>
</form>
</body>
</html>
