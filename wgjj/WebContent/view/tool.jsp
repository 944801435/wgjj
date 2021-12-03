<%@ page language="java" import="java.util.*,com.uav.base.common.Constants,com.uav.base.util.ConstantsUtil" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="fns" prefix="fns" %>
<%@ taglib uri="lg" prefix="lg" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="renderer" content="webkit"><meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10" />
<meta http-equiv="Expires" content="0"><meta http-equiv="Cache-Control" content="no-cache"><meta http-equiv="Cache-Control" content="no-store">
<meta name="decorator" content="blank"/>

<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.form.js" type="text/javascript"></script>
<script src="${ctx }/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx }/js/select2/select2.min.js" type="text/javascript"></script>
<script src="${ctx }/js/bootstrapPager.js" type="text/javascript" ></script>
<script src="${ctx }/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx }/js/layer/layer.js" type="text/javascript"></script>
<script src="${ctx }/js/vue.min.js" type="text/javascript"></script>
<script src="${ctx }/js/vuePageComponent.js" type="text/javascript"></script>
<script src="${ctx }/js/vueDirectives.js" type="text/javascript"></script>

<link href="${ctx }/css/bootstrap/2.3.1/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx }/css/bootstrap/2.3.1/awesome/font-awesome.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx }/js/select2/select2.min.css" rel="stylesheet" />
<link href="${ctx }/css/jeesite.css" type="text/css" rel="stylesheet" />
<link href="${ctx }/css/index.css" rel="stylesheet" type="text/css"/>
<link href="${ctx }/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<script>
layer.prompt = function (options, yes, cancel) {
    options = options || {};
    if (typeof options === 'function') yes = options;
    var prompt, content = options.formType == 2 ? '<textarea class="layui-layer-input">' + (options.value || '') + '</textarea>' : function () {
        return '<input type="' + (options.formType == 1 ? 'password' : 'text') + '" class="layui-layer-input" value="' + (options.value || '') + '">';
    }();
    return layer.open($.extend({
        btn: ['确定', '取消'],
        content: content,
        skin: 'layui-layer-prompt',
        success: function (layero) {
            prompt = layero.find('.layui-layer-input');
            prompt.focus();
        },
        btn1: function (index) {
            var value = prompt.val();
            if (value.length > (options.maxlength || 500)) {
                layer.tips('最多输入' + (options.maxlength || 500) + '个字数', prompt, {tips: 1});
            } else {
                yes && yes(value, index, prompt);
            }
        },
        btn2: function (index) {
            var value = prompt.val();
            if (value.length > (options.maxlength || 500)) {
                layer.tips('最多输入' + (options.maxlength || 500) + '个字数', prompt, {tips: 1});
            } else {
                cancel && cancel(value, index, prompt);
            }
        },
    }, options));
};
function Confirm(msg,callback,args){
	parent.layer.confirm(msg, {
	  icon: 3, 
	  title:'提示',
	},function(index){
		parent.layer.close(index);
		callback.apply(this,args);
	},function(index){
		parent.layer.close(index);
	});
}

function Confirm(msg,callback){
	parent.layer.confirm(msg, {
	  icon: 3, 
	  title:'提示',
	},function(index){
		parent.layer.close(index);
		callback();
	},function(index){
		parent.layer.close(index);
	});
} 

/*window.alert=function(msg){
	layer.alert(msg, {
		title:'提示',
		icon:1,
	    closeBtn: 0,
	    shade: 0 //不显示遮罩
	  });
}*/
window.alert=function(msg){
	parent.layer.msg(msg,{
		time:3000
	});
}
$(function(){
	$('select:not([tt=edit])').select2();
	
	// 为了阻止form表单中只有一个文本框按回车自动提交
	$("input").keydown(function(e){
		if (e.keyCode == 13) {
			return false;
		}
	});
	
	// 关闭文本框自动补全功能
	$("input").attr("autocomplete","off");

	//设置时间轴的样式
	$(".layui-timeline-content>h3").css("line-height","22px");
	$(".layui-timeline-content>p").css("margin-top","10px");
});

function jsonToMap(obj){
	var map=new Map();
	for(var key in obj){
		map.set(key,obj[key]);
	}
	return map;
}
//显示信息
function showMsg(errCode,msg){
	if($("#alert_div")){
		$("#alert_div").remove();
	}
	if(errCode=="0"){   //失败
		$(".right_content:visible>div:first").before("<div id=\"alert_div\" class=\"alert alert-success \" style=\"background-color: red;color:#fff;\"><button data-dismiss=\"alert\" class=\"close\" style=\"color: #fff;\">×</button>"+msg+"</div>");
	}else{                   //成功
		$(".right_content:visible>div:first").before("<div id=\"alert_div\" class=\"alert alert-success \" ><button data-dismiss=\"alert\" class=\"close\" >×</button>"+msg+"</div>")
		setTimeout(function(){
			removeMsg();
		},15000);
	}
}
//移除信息
function removeMsg(){
	if($("#alert_div")){
		$("#alert_div").remove();
	}
}

var loadingLayer = null;
//打开等待框
function openLoading(){
	loadingLayer = layer.load(2, {
		shade: [0.1,'#fff'], //0.1透明度的白色背景
	});
}
//关闭等待框
function closeLoading(){
	if(loadingLayer){
		layer.close(loadingLayer);
		loadingLayer = null;
	}
}

var pageContextPath = '${pageContext.request.contextPath}';
var ctx = '${ctx }';
</script>
<style>
form{
	margin: 0 0 0;
}
.right_content_select_box{
	margin-top: 0;
}
.right_content_all_top {
	height: 30px;
}
.right_content_all_top span {
	line-height: 30px;
}

.table-striped{
 	border:1px solid #F1F1F1 !important;
 }
.table-striped th{
 	background-color: #DFEBF5 !important;
 	border: none !important;
 }
.table-striped td{
 	border: none !important;
 }

.right_content_btnbox_search,
.right_content_btnbox_save,
.right_content_btnbox_common,
.right_content_btnbox_add {
    background-color: #609df1;
    border-radius: 3px;
}
.right_content_btnbox_delete2,
.right_content_btnbox_return,
.right_content_btnbox_resize{
	background-color: #fff;
	color: #000;
	border: 1px solid #dcd8d8;
	border-radius: 3px;
}

.hide{
	display: none !important;
}

a{
	cursor: pointer;
}
</style>
