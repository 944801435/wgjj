<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
  <head>
    <title>修改系统配置参数</title>
    <%@ include file="../../tool.jsp" %>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
    <script type="text/javascript">
		function save(){
			if(Validator.Validate(document.getElementById("myform"),3)){
	    		$("#myform").submit();
	    	}
		}
	</script>
  </head>
  
  <body>
  	<form id="myform" action="${pageContext.request.contextPath }/sysConfigEdit.action" method="post" class="form-horizontal">
  		<div class="right_content">
  		<lg:errors />
			<div class="right_content_all">
				<div class="right_content_all_top">
					<span>修改系统配置参数</span>
				</div>
				<div class="right_content_select">
					<div class="control-group">
						<label class="control-label">参数名</label>
						<div class="controls">
							<label class="lbl">${sysConfig.cfgId }</label>
							<input type="hidden" name="cfgId" value="${sysConfig.cfgId }" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">参数值</label>
						<div class="controls">
							<input type="text" name="cfgValue" value="${sysConfig.cfgValue }" maxlength="50" dataType="Require" msg="请输入参数值！"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">描述</label>
						<div class="controls">
							<input type="text" name="cfgDesc" value="${sysConfig.cfgDesc }" maxlength="50"/>
						</div>
					</div>
				</div>
				<%--按钮结构--%>
				<div class="right_content_btnbox ">
					<div onclick="save()" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_save">
						<img src="${pageContext.request.contextPath }/images/save_btn.png"/>
						<span>保存</span>
					</div>
					<div onclick="window.location.href='${ctx}/sysConfigList.action'" style="cursor:pointer;"
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
