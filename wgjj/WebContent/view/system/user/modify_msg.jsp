<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
  <head>
    <title>修改个人信息</title>
    <%@ include file="../../tool.jsp" %>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
	<script src="${pageContext.request.contextPath }/js/md5.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		function save(){
			if(!Validator.Validate(document.getElementById("myform"),3)){
				return;
			}
			var old_pwd=document.getElementById("old_pwd").value;
			var new_pwd=document.getElementById("new_pwd").value;
			var com_pwd=document.getElementById("com_pwd").value;
			if(old_pwd!=''||new_pwd!=''||com_pwd!=''){
				if(old_pwd==''){
					alertMsg(0,"请输入原密码！");
					return;
				}
				if(new_pwd==''){
					alertMsg(0,"请输入新密码！");
					return;
				}
				if(com_pwd==''){
					alertMsg(0,"请输入确认密码！");
					return;
				}
				if(new_pwd!=com_pwd){
					alertMsg(0,"新密码和确认密码不一致！");
					return;
				}
				$("#old_pwd_md5").val(hex_md5($("#userName").val() + $("#old_pwd").val()).toUpperCase());
				$("#new_pwd_md5").val(hex_md5($("#userName").val() + $("#new_pwd").val()).toUpperCase());
				$("#com_pwd_md5").val(hex_md5($("#userName").val() + $("#com_pwd").val()).toUpperCase());
			}
			$("#myform").ajaxSubmit({
				url:"${pageContext.request.contextPath}/modify_msg.action",
				type:"post",
				dataType:"json",
				async:false,
				success:function(data){
					alertMsg(data.errCode,data.errMsg);
				}
			});
		}
		function alertMsg(sts,msg){
			if($("#alert_div")){
				$("#alert_div").remove();
			}
			if(sts=='1'){
				$(".right_content_all").before("<div id=\"alert_div\" class=\"alert alert-success \" ><button data-dismiss=\"alert\" class=\"close\" >×</button>"+msg+"</div>");
			}else{
				$(".right_content_all").before("<div id=\"alert_div\" class=\"alert alert-success \" style=\"background-color: red;color:#fff;\"><button data-dismiss=\"alert\" class=\"close\" style=\"color: #fff;\">×</button>"+msg+"</div>");
			}
			
		}
	</script>
  </head>
  
  <body>
  	<form id="myform" action="" class="form-horizontal">
  		<div class="right_content">
			<div class="right_content_all">
				<div class="right_content_all_top">
					<span>修改个人信息</span>
				</div>
				<div class="right_content_select">
					<div class="control-group">
						<label class="control-label">用户名</label>
						<div class="controls">
							<input type="text" id="userName" name="userName" value="${sysUser.userName }" readonly="readonly" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">手机号</label>
						<div class="controls">
							<input type="text" name="phone" value="${sysUser.phone }" maxlength="11"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">原密码</label>
						<div class="controls">
							<input type="password" id="old_pwd"/>
							<input type="hidden" id="old_pwd_md5" name="old_pwd"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">新密码</label>
						<div class="controls">
							<input type="password" id="new_pwd"/>
							<input type="hidden" id="new_pwd_md5" name="new_pwd"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">密码确认</label>
						<div class="controls">
							<input type="password" id="com_pwd"/>
							<input type="hidden" id="com_pwd_md5" name="com_pwd"/>
						</div>
					</div>
					<div class="">
						<label class="control-label">初始菜单</label>
						<div class="controls">
							<select class="right_content_select_ctt"  name="init_menu_id" style="width: 220px;">
								<option value=""></option>
								<c:forEach var="item" items="${menuList }">
									<c:forEach var="obj" items="${item.childMenus }">
										<option value="${obj.menuId }" ${sysUser.initMenuId==obj.menuId ? "selected" : "" }>${obj.menuName }</option>
									</c:forEach>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<%--按钮结构--%>
				<div class="right_content_btnbox ">
					<div onclick="save()" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_save">
						<img src="${pageContext.request.contextPath }/images/save_btn.png"/>
						<span>保存</span>
					</div>
				</div>
			</div>
		</div>
  	</form>    
  </body>
</html>
