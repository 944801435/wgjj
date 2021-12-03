<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>用户添加</title>
<%@ include file="../../tool.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/md5.min.js"></script>
<script type="text/javascript">
	function goSave() {
    	var myForm=document.getElementById("inputForm");
    	if(Validator.Validate(myForm,3)){
    		$("#userPwd_md5").val(hex_md5($("#username").val() + $("#userPwd").val()).toUpperCase());
    		$("#inputForm").submit();
    	}
	}
</script>
</head>
<body>
    <form id="inputForm" action="${pageContext.request.contextPath }/userAdd.action" method="post" class="form-horizontal">
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
							<input id="username" autocomplete="off" type="text" dataType="Require,Limit,EN_" len="50" msg="请输入(1~50)个字符的登录名,只能输入字母、数字和下划线！" maxlength="50" class="required" name="userName" value=""/>
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
							<input autocomplete="off" type="password" dataType="Require" msg="请输入密码！" class="required"  id="userPwd" value=""/>
							<input name="userPwd" type="hidden" id="userPwd_md5">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">手机：</label>
						<div class="controls">
							<input autocomplete="off" type="text" dataType="Mobile" len="11" msg="请输入（11）位的手机！" class="required"  maxlength="11" name="phone" value=""/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">授权角色：</label>
						<div class="controls">
							<select name="roleId" dataType="Require" msg="请选择授权角色！">
								<c:forEach var="item" items="${roleList }">
									<option value="${item.roleId}">${item.roleName}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="right_content_btnbox ">
					<div onclick="javascript:goSave();" style="cursor:pointer;"
						class="right_content_btnbox_btn right_content_btnbox_save">
						<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
					</div>
					<div onclick="window.location.href='${ctx}/userList.action'" style="cursor:pointer;"
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
