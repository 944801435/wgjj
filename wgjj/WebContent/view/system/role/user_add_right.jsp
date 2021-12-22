<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>用户添加</title>
<%@ include file="../../tool.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
<script type="text/javascript">
	function goSave() {
		if(window.parent.checkNodeId.length!=0){
			var menuIds=window.parent.checkNodeId.join(",");
			$("#menuIds").val(menuIds);
		}
		
    	var myForm=document.getElementById("inputForm");
    	if(Validator.Validate(myForm,3)){
    		$("#inputForm").submit();
    	}
	}
	
</script>
</head>
<body>
    <form id="inputForm" target="_parent" action="${pageContext.request.contextPath }/userAdd.action" method="post" class="form-horizontal">
    	<input type="hidden" name="menuIds" id="menuIds" value="">
		<div class="right_content">
			<lg:errors />
			<div class="right_content_all">
				<div class="right_content_all_top">
					<span>添加用户</span>
				</div>
				<div class="right_content_select">	
					<div class="control-group">
						<label class="control-label">登录名：</label>
						<div class="controls">
							<input autocomplete="off" type="text" dataType="Require,Limit,EN_" len="50" msg="请输入(1~50)个字符的登录名,只能输入字母、数字和下划线！" maxlength="50" class="required" name="userName" value=""/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">用户名：</label>
						<div class="controls">
							<input autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的用户名！" maxlength="50" class="required" name="userDesc" value=""/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">密码：</label>
						<div class="controls">
							<input autocomplete="off" type="password" dataType="Require" msg="请输入密码！" class="required"  name="userPwd" value=""/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">手机：</label>
						<div class="controls">
							<input autocomplete="off" type="text" dataType="Mobile" len="11" msg="请输入（11）位的手机！" class="required"  maxlength="11" name="phone" value=""/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">区域范围：<br>（多边形）</label>
						<div class="controls">
							<textarea autocomplete="off" rows="3" cols="100" class="required" name="rangeLoc" style="max-width: 500px;max-height: 150px;"></textarea>
						</div>
					</div>
				</div>
				<div class="right_content_btnbox ">
					<div onclick="javascript:goSave();" style="cursor:pointer;"
						class="right_content_btnbox_btn right_content_btnbox_save">
						<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
					</div>
					<div onclick="window.parent.location.href='${ctx}/userList.action'" style="cursor:pointer;"
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
