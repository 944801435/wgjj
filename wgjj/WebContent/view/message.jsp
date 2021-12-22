<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
  <head>
  	<%@ include file="tool.jsp"%> 
  </head>
  
  <body>
    <div id="messageDiv" style="height:100px;padding-top: 10px;padding-left: 10px;">
    	
    </div>
    <div id="next_btn" style="text-align: right;padding-right: 10px;">
    	<a onclick="next()" href="javascript:void(0);"><label>下一页</label></a>
    </div>
  </body>
  <script type="text/javascript">
  $(document).ready(function(){
	  	next();
  });
  var message_data=eval(window.parent.message_data);
  var index = window.parent.index;
  var arr2 ="";
  function next(){
	  	  message_data=eval(window.parent.message_data);
	  	  if(message_data.length == 0) {
	  		  return;
	  	  }
		  arr2 = message_data.slice(0,1);//数组的深拷贝,js引用类型是引用拷贝，用此方法可以值拷贝。
		  if(arr2[0].menuUrl == null || arr2[0].menuUrl == ''){
			$("#messageDiv").html(message_data[0].msgText);
		  }else {
			$("#messageDiv").html("<a href='javascript:void(0);'  onclick='openPrentMenu();'>"+message_data[0].msgText+"</a>");
		  }
		 $.ajax({
				url:"${ctx}/messageClose.action",
				type:"post",
				data:{"msgSeq":message_data[0].msgSeq},
				async:false,
				success:function(data){
					message_data.shift();
					window.parent.message_data=message_data;
					if(message_data.length==0){
						$("#next_btn").hide();
					}else {
						$("#next_btn").show();
					}
				}
			});
	}
  
  function openPrentMenu() {
	  
	 var menuUrl = arr2[0].menuUrl;
	 var modelId = arr2[0].modelId;
	 window.parent.openMenu(menuUrl,modelId);
	 next();
  }
		
  	</script>
</html>
