<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../tool.jsp"%>
<!DOCTYPE HTML>
<%
String roleId=request.getParameter("roleId");
request.setAttribute("roleId", roleId);
%>
<html>
  <head>
    <title>编辑角色</title>
	<script type="text/javascript">
	 	var checkNodeId=new Array();
	</script>
  </head>
  	<frameset cols="70%,*" frameborder="no" border="0" framespacing="0">
  		<frame id="rightFrame" name="rightFrame" src="${ctx }/to_role_edit.action?roleId=${roleId}" />
  		<frame name="leftFrame" src="${ctx }/MenuTreeCheckJson.action?roleId=${roleId }" />
  	</frameset>
  	<body></body>
</html>