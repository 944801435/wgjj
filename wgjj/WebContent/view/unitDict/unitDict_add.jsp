<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>单位字典添加</title>
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
<form id="inputForm" action="${pageContext.request.contextPath }/unitDictAdd.action" method="post" class="form-horizontal">
	<div class="right_content">
		<lg:errors />
		<div class="right_content_all">
			<div class="right_content_all_top">
				<span>添加单位字典</span>
			</div>
			<div class="right_content_select">
				<div class="control-group">
					<label class="control-label">单位名称：</label>
					<div class="controls">
						<input id="unitName" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~20)个字符的单位字典名称！" maxlength="50" class="required" name="unitName" value=""/>
					</div>
				</div>
			</div>
			<div class="right_content_btnbox ">
				<div onclick="javascript:goSave();" style="cursor:pointer;"
					 class="right_content_btnbox_btn right_content_btnbox_save">
					<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
				</div>
				<div onclick="window.location.href='${ctx}/unitDictList.action'" style="cursor:pointer;"
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
