<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../tool.jsp"%>
<!DOCTYPE HTML>
<%
String userId=request.getParameter("userId");
request.setAttribute("userId", userId);
%>
<html>
  <head>
    <title>编辑用户</title>
	<script type="text/javascript">
	 	var checkNodeId=new Array();
	</script>
  </head>
  	<frameset cols="70%,*" frameborder="no" border="0" framespacing="0">
  		<frame id="rightFrame" name="rightFrame" src="${ctx }/to_edit.action?userId=${userId}" />
  		<frame name="leftFrame" src="${ctx }/MenuTreeCheckJson.action?userId=${userId }" />
  	</frameset>
  	<body></body>
</html>