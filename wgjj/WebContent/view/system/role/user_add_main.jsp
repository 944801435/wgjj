<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../tool.jsp"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>添加用户</title>
	<script type="text/javascript">
	 	var checkNodeId=new Array();
	</script>
  </head>
  	<frameset cols="70%,*" frameborder="yes" border="0" framespacing="0">
  		<frame id="rightFrame" name="rightFrame" src="${ctx }/to_add.action?userId=${userId}" />
  		<frame name="leftFrame" src="${ctx }/MenuTreeCheckJson.action" />
  	</frameset>
  	<body></body>
</html>