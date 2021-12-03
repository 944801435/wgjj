/**
 * Created by zheng on 14-5-12.
 */
var base = new Object(), MENU_ID = '10000',PAGE_ID = 'page_L10000', TAB_ID='L10000',PAGE_URL='/admin/index/home'; //全站页面通用标示ID
/*简单提示框*/
base.showMsg = function(msg,delay,callback)
{
	if(msg=='xsso_message'){	
		base.showXssoMsg();
	}else{
		$(".msg span").html(msg);
		$(".msg").show();
		setTimeout(base.closemsg,delay?delay:1500);
		if(callback) setTimeout(callback,delay?delay:1500);		
	}
}
base.showXssoMsg= function(){
	base.Dialog('温馨提示：','<p class="xsso_message">对不起，同一用户名已在其他设备上登录，您被强行下线！</p>',300);
}
base.closemsg = function()
{
    $(".msg").hide();
}
base.inArray = function(key,array)
{
    for(var i=0;i<array.length;i++)
    {
        if(key == array[i])
        {
            return true;
        }
    }
    return false;
}
//去空格
base.trim = function(str)
{
    if(str == null) return "";
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
base.addCookie = function(objName,objValue,objHours)
{
    var str = objName + "=" + escape(objValue);
    if(objHours > 0){//为0时不设定过期时间，浏览器关闭时cookie自动消失
        var date = new Date();
        var ms = objHours*3600*1000;
        date.setTime(date.getTime() + ms);
        str += "; expires=" + date.toGMTString() +"; path=/;domain="+document.domain;
    }
    document.cookie = str;
}
base.getCookie = function(objName)
{
    var arrStr = document.cookie.split("; ");
    for(var i = 0;i < arrStr.length;i ++){
        var temp = arrStr[i].split("=");
        if(temp[0] == objName) return temp[1];
    }
}
base.referUrl = function(delay){
	setTimeout(function(){$('.tab_bar > li.selected').find('.close').trigger('click');},delay?delay:1500);
}
/*全站页面跳转*/
base.redirect = function(url){
	if(url!=''){
		document.getElementById("rightFrame").src=url;
	}
	return false;
}
//跳转并关闭当前页
base.redirectTo = function(url)
{
    $('.tab_bar > li.selected').find('.close').trigger('click');
    base.redirect(url);
}
//登录框
base.LoginBox = function(str, width){
	var loginbox = $('#loginBox'), loginframe = $('#loginIframe');
	loginframe.attr('src',str);
	loginbox.find('a').trigger('click');
	base.Dialog("用户登录",loginbox.html(),width);
}
// url:要加载的页面  callback回调函数
base.loadHtml = function(url,callback){
	var pattern=/^\/admin\/\w+\/\w+/;
	var content = "#load_"+MENU_ID, page_id = pattern.exec(url).toString().replace(/\//g,"_");
	var pageObj = $(content).find('#JL'+page_id); 
	if(pageObj.length==0){
		$(content).append('<div id="JL'+page_id+'"></div>');
	}
	$(content).children(':first-child').slideUp(400);
	$(content).find('#JL'+page_id).load(url,function(res){//arguments
		if(typeof callback === "function"){
			//加载的页面ID
			callback('#JL'+page_id);
		}
	}).show();
}
//关闭加载页 返回到上一页
base.closeBack = function(url){
	var pattern=/^\/admin\/\w+\/\w+/;
	var content = "#load_"+MENU_ID, page_id = pattern.exec(url).toString().replace(/\//g,"_");
	var pageObj = $(content).find('#JL'+page_id);
	$(content).children(':first-child').show();
	pageObj.html(''); 	
}
/*
全站页面弹出层
title 层标题 content 层内容
*/
base.Dialog	= function(title,content,width){
	var basicObj = $("#basic"), titBar = basicObj.find('#modal-title'), bodyBar = basicObj.find('#modal-body-box');
	var title = title ? title : '信息提示：';
	var w = width ? width : 700;
	basicObj.find('.modal-dialog').width(w);
	basicObj.fadeIn(100);
	titBar.html(title);
	bodyBar.html(content);
}
base.Dialog.hide = function()
{
    $('#modal-title').html('');
    $('#modal-body-box').html('<div class="body-box"></div>');
	$("#basic").find('.modal-dialog').width(500);
    $("#basic").hide();
    $(".modal-backdrop").remove();
}
base.Dialog.close= function(){
	$("#basic").find(".close-dialog").trigger('click');
}
/*
 * 用于行内处理元素，并支持刷新页面
 */
base.deleteRow = function(data,toUrl,reloadUrl)
{
    var confirm = window.confirm('确认删除？');
    if(!confirm)
    {
        return false;
    }
    $.ajax({
        type:'POST',
        url:toUrl,
        data:data,
        dataType:'json',
        success:function(res)
        {
            base.showMsg(res.msg,1500,function(){
                if(res.status == 0 && reloadUrl)
                    base.redirect(reloadUrl)
            });
        }
    })
};
base.status = function(data,toUrl,reloadUrl)
{
	var confirm = window.confirm('确定要执行该操作？');
    if(!confirm)
    {
        return false;
    }
    $.ajax({
        type:'POST',
        url:toUrl,
        data:data,
        dataType:'json',
        success:function(res)
        {
            base.showMsg(res.msg,1500,function(){
                if(res.status == 0 && reloadUrl)
                    base.redirect(reloadUrl)
            });
        }
    })
};
base.showInfo = function(data,toUrl,reloadUrl, width)
{
	
    $.ajax({
        type:'POST',
        data:data,
        url:toUrl,
        success:function(res)
        {
            base.Dialog('详细信息',res,width?width:800);
        }
    });
    return false;	
};
/*
 * 展示搜索表单
 */
base.showSearch = function(ele,tagId)
{
    var self = $(ele);
    if(self.find('i').hasClass('fa-angle-down'))
    {
        self.find('i').removeClass('fa-angle-down').addClass('fa-angle-up');
        self.find('span').html('隐藏搜索');
        $("#page_L"+tagId+" .dataform").show();
    }
    else
    {
        self.find('i').removeClass('fa-angle-up').addClass('fa-angle-down');
        self.find('span').html('显示搜索');
        $("#page_L"+tagId+" .dataform").hide();
    }
};
//验证是否手机
base.isPhone = function(num)
{
    var preg = /^1[3-7,8]{1}[0-9]{9}$/;
    return preg.test(num);

};
//验证是否固话
base.isTel = function(num)
{
    var preg = /^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
    return preg.test(num);
};
//验证是否邮箱
base.isEmail = function(num)
{
    var preg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    return preg.test(num);
};
//验证是否身份证
base.isIdentity =function(num)
{
    var preg = /^\d{14}(\d{3})?(\d|x)$/;
	var preg2 = /^[a-zA-Z]{1}[0-9]{10}$/; //港澳
	if(num.length>11){
		return preg.test(num)
	}else{
		return preg2.test(num)
	}
};
//验证是否QQ
base.isQQ =function(num)
{
    var preg = /^\d{4,14}$/;
    return preg.test(num);
};
/*
 上传
 */
base.uploadPic = function(ele,key_type,key_id,val_type)
{
    var id = $(ele).parent().parent().find("input[name="+key_type+"_"+val_type+"]").val();
    $.ajaxFileUpload({
        url:'/admin/customer/upload',
        dataType:'json',
        secureuri: false,
        fileElementId:'mainPic_'+val_type,
        data:{'key_id':key_id,'key_type':key_type,'val_type':val_type,'id':id},
        success:function(res)
        {
            base.showMsg(res.msg);
            if(res.status == 0)
            {
                $(ele).parent().parent().find("input[name="+key_type+"_"+val_type+"]").val(res.data.id);
                $('.mainPic_'+val_type).show();
                $('.mainPic_'+val_type).html('<a href="'+res.data.mainPic+'" target="_blank"><img src="'+res.data.mainPic+'"></a>');
            }
        }
    });
    return false;
};
base.isActive = false;
base.showImg = function(ele)
{
    return false;
    var src = $(ele).attr('src');
    var imgContent = $(".imgView");
    if(src && !base.isActive)
    {
        base.isActive = true;
        imgContent.html("<img src='"+src+"' >");
        var wWidth = $(window).width();
        var wHeight = $(window).height();
        imgContent.show(100);
        var iWidth = imgContent.find('img').width()>wWidth?(wWidth-100):imgContent.find('img').width();
        var iHeight = imgContent.find('img').height()>wHeight?(wHeight-100):imgContent.find('img').height();
        imgContent.animate({'top':($(window).height()-iHeight)/2,'left':($(window).width()-iWidth)/2},'500','',function(){
            base.isActive = false;
        });
    }
    return false;
};
base.delPic = function(ele)
{
    var confirm = window.confirm('确定删除此图片?');
    if(confirm)
    {
        var id = $(ele).parent().find("input[type=hidden]").val();
        if(id)
        {
            $.ajax({
                type:'POST',
                url:'/admin/customer/delPic',
                data:'id='+id,
                dataType:'json',
                success:function(res)
                {
                    base.showMsg(res.msg,1000,function(){
                        if(res.status == 0)
                        {
                            $(ele).parent().find(".hidImg").html('');
                        }
                    })
                }
            });
        }
    }
    return false;
};
$(document).ready(function(){
    $(document).click(function()
    {
        var imgContent = $(".imgView");
        imgContent.hide();
        imgContent.html('');
        imgContent.css({'top':0,'left':'30%'});
    });
});
//仅提交按钮事件可用
base.onProcess = function(ele)
{
    $(ele).attr('disabled',true);
    base.showProcess('数据处理中，请稍侯',ele);
};
base.showProcess = function(msg,ele)
{
    $("#onProcess span").html("<i class=\"fa fa-coffee\"></i>"+msg+"...");
    $("#onProcess").show();
    setTimeout(function(){base.delProcess(ele)},10000);
};
base.delProcess = function(ele)
{
    $("#onProcess").hide();
    if(ele)
        $(ele).attr('disabled',false);
};
// 页面工具类
base.refreshTab = function(){
	var pageid=PAGE_ID,loadurl=$('#'+pageid).attr('loadurl'),dataForm = $('#'+pageid).find('.dataform');
	var isDataForm = dataForm.length > 0 ? true : false;
	if(isDataForm){  var pageObj = eval('L_'+MENU_ID);
		if(typeof pageObj == 'object')pageObj.ajaxPost(); 
	}else{
		$('#'+pageid).load(loadurl,function(res){
		   $('.pageload').remove(); UtilFun.initPage($('#'+pageid));
		});				
	}
};
//搜索机构
base.searchAgentBykey = function(obj){
	var pdiv = $(obj).parents('#belong_agent_box');
	var agent_name_keyword = pdiv.find('input[name=agent_name_keyword]').val();
	var all=0;
	if(agent_name_keyword==''){
		all=1;
	}
	$.post('/admin/customer/showmcc',{agent_name_keyword:agent_name_keyword,all:all},function(res){
		if(res.status==0){
			var html ='';
			for(i=0;i<res.data.length; i++){
				html += '<option value="'+res.data[i]['agent_id']+'">'+res.data[i]['agent_name']+'</option>';
			}
			pdiv.find('select[name=belong_agent]').html(html);
		}else{
			base.showMsg(res.msg);
		}
	},'JSON');	
}
//查看图片
base.viewPhoto = function(src,type,isbase)
{
    var str = '';
    if(type != undefined)
    {
        var getStr = "<font style='color:red'>点击图片对应信息页，可提取页面信息</font>";
        if(type == 0 || type == 1 || type == 2)
        {
            var id_name = $("#detail_content input[name=cont_name]").val();
            var id_no = $("#detail_content input[name=identityType_5]").val();
            if(id_no)
            {
                getStr = '姓名：'+id_name+', 身份证号：'+id_no;
            }
        }
        else if(type == 9)
        {
            var cardno = $("#detail_content td[name=cardno]").html();
            var user = $("#detail_content td[name=user]").html();
            var cardBank = $("#detail_content td[name=cardBank]").html();
            if(cardno)
            {
                getStr = '卡号：'+cardno+', 开户名：'+user+', 开户行：'+cardBank;
            }
        }
        str = "<div style='float: right'>" +getStr+"</div>";
    }
    var html= "<div id=\"tool\">"
        + "        <a href=\"javascript:;\" id=\"arr_left\">向左</a>"
        + "        <a href=\"javascript:;\" id=\"arr_right\">向右</a>"
        + "    (滚动滑轮可缩放图片)"+str+"</div>"
        + "    <div id=\"img\" style='text-align: center;width:938px;height: 650px;'>"
        + "    </div>";
    base.Dialog('图片查看',html,960);
    var container = $$("img"),options = {
        onPreLoad: function(){ container.style.backgroundImage = "url('/admin/img/input-spinner.gif')"; },
        onLoad: function(){ container.style.backgroundImage = ""; },
        onError: function(err){ container.style.backgroundImage = ""; alert(err); }
    },it = new ImageTrans( container, options );
    $$("arr_left").onclick = function(){it.left();};
    $$("arr_right").onclick = function(){it.right();};
	if(isbase != 1)
	{
		var b = new Base64();
    	src = b.decode(src);
	}
    it.load(src);
};