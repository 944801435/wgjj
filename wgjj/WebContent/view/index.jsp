<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="tool.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.content{
	padding-top:2%;
	padding-left: 2%;
}
.item{
	width:84px;
	display: inline-block;
	margin-top: 2%;
	text-align: center;
}
.item_child{
	border: 1px solid #E5E9EF;
	width:60px;
	height: 60px;
	border-radius: 5px;
	cursor: pointer;
	background-color: #fff;
	margin-left: 12px;
}
.item_child img{
	width:32px;
	height:32px;
	margin:14px 14px; 
}
.item_child i{
	font-size: 32px;
	margin:14px 14px;
	color: #4b89df;
}
.item span{
	font-size: 12px;
	white-space:nowrap;
}
.badge{
	float: right;
	margin-top: -65px;
}
</style>
</head>
<body style="background-color: #F5F7FC;">
	<div class="content">
		<div><b>常用功能</b></div>
		<div id="cy">
			<div class="item">
				<div id="edit" class="item_child" operate="edit" onclick="edit(this)">
					<img src="${ctx}/view/icon/add.png" />
					<span>添加</span>
				</div>
			</div>
			
		</div>
		<br/>
		<br/>
		<div><b>快速入口</b></div>
		<div id="ksrk">
			<c:forEach var="item" items="${menuList }">
				<c:forEach var="obj" items="${item.childMenus }">
					<c:if test="${fn:length(obj.note)>0 }">
						<div class="item" style="padding-right:15px;">
							<div class="item_child" menuid="${obj.menuId }" onclick="menuClick(this)">
								<c:if test="${fn:contains(obj.note,'.')}">
									<img src="${ctx}/view/icon/${obj.note}" />
								</c:if>
								<c:if test="${!fn:contains(obj.note,'.')}"> 
									<i class="${obj.note }"></i>
								</c:if>
							</div>
							<span >${obj.menuName }</span>
						</div>
						
					</c:if>
				</c:forEach>
			</c:forEach>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function(){
	//初始获取用户常用菜单
	$.ajax({
		url:'${ctx}/findUserMenuId.action',
		type:'post',
		dataType:'json',
		success:function(data){
			if(data.errCode=='0'){
				alert(data.errMsg);
				return;
			}
			var menuIds=data.data;
			for(var i=0;i<menuIds.length;i++){
				$('#cy').find('div.item:last').before($('#ksrk').find('div[menuid='+menuIds[i]+']:first').parent());
			}
		}
		
	});
});

function menuClick(elem){
	var menuid=$(elem).attr('menuid');
	if(parent.$('a[menuid='+menuid+']').length>0){
		parent.$('a[menuid='+menuid+']')[0].click();
	} 
}

function edit(elem){
	if($(elem).attr('operate')=='edit'){
		$(elem).attr('operate','save');
		$(elem).css('background-color','green');
		$(elem).find('span').html('保存');
		$('.item_child:not(#edit)').attr('onclick','');
		
		$('#cy').find('div.item_child:not(:last)').append('<span onclick="del(this)" class="badge badge-important">×</span>');
		$('#ksrk').find('div.item_child').each(function(){
			var menuid_len=$('#cy').find('div[menuid='+$(this).attr('menuid')+']').length;
			if(menuid_len<1){
				$(this).append('<span onclick="add(this)" class="badge badge-success">√</span>')
			}
		});
		
	}else{
		$(elem).attr('operate','edit');
		$(elem).css('background-color','#fff');
		$(elem).find('span').html('添加');
		$('.item_child:not(#edit)').attr('onclick','menuClick(this)');
		$('.badge').remove();
		var menuIds='';
		$('#cy').find('div.item_child:not(:last)').each(function(){
			if(menuIds.length>0){
				menuIds+=",";
			}
			menuIds+=$(this).attr('menuid');
		});
		//ajax提交保存
		$.ajax({
			url:'${ctx}/saveUserMenuId.action',
			type:'post',
			data:{'menuIds':menuIds},
			dataType:'json',
			success:function(data){
				alert(data.errMsg);
			}
		}); 
	}
}

function del(elem){
	$(elem).attr('onclick','add(this)');
	$(elem).removeClass('badge-important').addClass('badge-success');
	$(elem).html('√');
	$('#ksrk').append($(elem).parent().parent());
}
function add(elem){
	$(elem).attr('onclick','del(this)');
	$(elem).removeClass('badge-success').addClass('badge-important');
	$(elem).html('×');
	$('#cy').find('div.item:last').before($(elem).parent().parent());
}
</script>
</html>