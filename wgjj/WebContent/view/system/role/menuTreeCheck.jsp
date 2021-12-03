<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../tool.jsp"%>
<!DOCTYPE HTML>
<html>
  <head>
    
    <title>机构树</title>
    
	 <link rel="stylesheet" href="${pageContext.request.contextPath }/js/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	
	<script src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.core.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx }/js/ztree/jquery.ztree.excheck.js"></script>
	
	<SCRIPT type="text/javascript">
	 	var zNodes=${nodeList};
		var setting = {
			callback:{
				onCheck:onCheck
			},
			check: {
				enable: true
			},
		};
		function onCheck(event,treeId,treeNode){
			var treeObj=$.fn.zTree.getZTreeObj(treeId);
            var nodes=treeObj.getCheckedNodes(true);
            window.parent.checkNodeId=new Array();
            for(var i=0;i<nodes.length;i++){
            	window.parent.checkNodeId.push(nodes[i].id);
            }
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#orgtree"), setting,zNodes);
			 var zTree = $.fn.zTree.getZTreeObj("orgtree");//获取ztree对象
			 //子节点选中让他的父节点也选中
             //调用打开所有节点   
             zTree.expandAll(true);
             var nodes=zTree.getCheckedNodes(true);
             window.parent.checkNodeId=new Array();
             for(var i=0;i<nodes.length;i++){
            	 zTree.checkNode(nodes[i], true, true);
             	 window.parent.checkNodeId.push(nodes[i].id);
             }
		});
		
	</SCRIPT>
	<style>
		#orgtree{
			min-width: 190px;
			display: inline-block;
			vertical-align: top;
		}
		/* .ztree li span.button.ico_open{margin-right:2px; background: url(${ctx}/js/ztree/zTreeStyle/img/diy/1_open.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
		.ztree li span.button.ico_close{margin-right:2px; background: url(${ctx}/js/ztree/zTreeStyle/img/diy/1_close.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
		.ztree li span.button.ico_docu{margin-right:2px; background: url(${ctx}/js/ztree/zTreeStyle/img/diy/1_close.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
	 */
	</style>
  </head>
  <body>
  <form class="form-horizontal">
 		 <div class="right_content">
			<lg:errors />
			<div class="right_content_all">
				<div class="right_content_all_top">
					<span>分配权限</span>
				</div>
  					<ul id="orgtree" class="ztree"></ul>
  			</div>
  		</div>
  	</form>
  </body>

</html>
