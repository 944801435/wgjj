<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>编辑操作权限</title>
	<%@ include file="../../tool.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/md5.min.js"></script>
	<script type="text/javascript">
		function goSave() {
			var myForm=document.getElementById("inputForm");
			if(Validator.Validate(myForm,3)){
				$("#inputForm").submit();
			}
		}
		var list = ${menuTreeNodeList}
		function setSecond(value){
			var val = value;
			console.log(list)
			var info;
			$.each(list, function(index,callback){
				info+="Person id:" + list[index].id + " name:" +  list[index].name+"/r/t";
			});
			for (i = 0; i < list.length; i++) {
				// console.log(list[i].id)
				if (list[i].id == val){
					var children = list[i].children;
					var sec = document.getElementById('second');
					sec.options.length=0; //清空二级菜单项
					for (j = 0; j < children.length; j++) {
						sec.options[j] = new Option(children[j].name,children[j].id);
					}
					console.log(sec);
				}
			}
		}
	</script>
</head>
<body>
<form id="inputForm" action="${pageContext.request.contextPath }/pmsEdit.action" method="post" class="form-horizontal">
	<input type="hidden" name="pmsId" value="${sysPms.pmsId }" />
	<input type="hidden" id="menuTreeNodeList" value="${menuTreeNodeList }" />
	<div class="right_content">
		<lg:errors />
		<div class="right_content_all">
			<div class="right_content_all_top">
				<span>编辑操作权限</span>
			</div>
			<div class="right_content_select">
				<div class="control-group">
					<label class="control-label">操作权限名称：</label>
					<div class="controls">
						<input type="hidden" name="menuId" id="menuId" value="${sysPms.menuId }" />
						<input id="pmsname" autocomplete="off" type="text" dataType="Require,Limit" len="50" msg="请输入(1~50)个字符的操作权限名称！" maxlength="50" class="required" name="pmsName" value="${sysPms.pmsName }"/>
					</div>
				</div>
			</div>
			<div class="right_content_btnbox ">
				<div onclick="javascript:goSave();" style="cursor:pointer;"
					 class="right_content_btnbox_btn right_content_btnbox_save">
					<img src="${ctx }/images/save_btn.png" /> <span>保存</span>
				</div>
				<div onclick="window.location.href='${ctx}/pmsList.action?menuId=${sysPms.menuId}'" style="cursor:pointer;"
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
