/**
加载三级联动地区,在要用的地方加上 id="areaBar" 就可以自动显示出来地区了 
data-admin="1" 表示；只显示当前用户的所在地区
data-areaid="001" 表示；只显示当前001的地区
data-disable="false"
用法：<div class="col-md-10" id="areaBar" data-admin="0"></div>
**/
var areaObject = new Object();
areaObject.init = function(page_obj){
	var areaBar = page_obj.find('#areaBar');
	var areaId = areaBar.attr('data-areaid') ? areaBar.attr('data-areaid'):0; //如果不为空则代表编辑数据
	var admin_areaid = areaBar.attr('data-admin') ? areaBar.attr('data-admin'):0; //如果不为空则代表只查询当前所属地区
	if(areaBar.length>0){
		areaBar.append('<img src="/admin/img/loading.gif" id="loading">');
		if(parseInt(areaId) == 0){
			areaObject.getArea(0,0);
		}else{
			//根据当前id获取父区域
			areaObject.initEdit(areaId,admin_areaid);
		}
	}
}
areaObject.initEdit = function(areaid,admin_areaid){
	var areaBar =  $('#'+PAGE_ID).find('#areaBar');
	var disabled = areaBar.attr('data-disable') == 'true'?true:false; //如果禁用
	$.post('/admin/index/getParentArea',{id:areaid,admin_areaid:admin_areaid},function(res){
		$('#loading').remove();
		var data = $.parseJSON(res); 
		for(var i=0;i<data.html.length; i++){
			var html = '';
			html += '<div class="col-md-2" style="padding-left:0">';	
			html += data.html[i];
			html += '</div>';		
			areaBar.append(html);	
		}
		//是否初始化禁用
		if(disabled){;
			areaBar.find('select').prop('disabled', true);
		}
	});	
	return false;		
}
areaObject.delOld = function(rank){
	var areaBar =  $('#'+PAGE_ID).find('#areaBar');
	if(rank==1){
		areaBar.find('#oneArea').parent().remove();areaBar.find('#twoArea').parent().remove();areaBar.find('#threeArea').parent().remove();
	}else if(rank==2){
		areaBar.find('#twoArea').parent().remove();areaBar.find('#threeArea').parent().remove();
	}else if(rank==3){
		areaBar.find('#threeArea').parent().remove();
	}
}
areaObject.getArea = function(areaid,rank,admin_areaid){
	var areaBar =  $('#'+PAGE_ID).find('#areaBar');
	var disabled = areaBar.attr('data-disable') == 'true'?true:false; //如果禁用
	areaObject.delOld(rank);
	$.post('/admin/index/getArea',{id:areaid, rank:rank,admin_areaid:admin_areaid},function(res){
		$('#loading').remove();
		var data = $.parseJSON(res);
		var html = '';
		html += '<div class="col-md-2" style="padding-left:0">';	
		html += data.html;
		html += '</div>';	
		
		data.html ? areaBar.append(html):'';
		//是否初始化禁用
		if(disabled){;
			areaBar.find('select').prop('disabled', true);
		}
	});	
	return false;		
}
//修改多次查询ajax bug
areaObject.change = function(obj, rank){
	var areaBar =  $(obj).parents('#areaBar');
	var areaId = areaBar.attr('data-areaid') ? areaBar.attr('data-areaid'):0; //如果不为空则代表编辑数据
	var admin_areaid = areaBar.attr('data-admin') ? areaBar.attr('data-admin'):0; //如果不为空则代表只查询当前所属地区
	var disabled = areaBar.attr('data-disable') == 'true'?true:false; //如果禁用
	var showrank = areaBar.attr('data-rank') ? areaBar.attr('data-rank') : 4;//显示1 2 3 4 共4级
	
	if($(obj).val() !=-1){
		if(rank<showrank){
			areaBar.append('<img src="/admin/img/loading.gif" id="loading">');
			areaObject.getArea($(obj).val(),rank);
		}
	}else{
		if(rank==1){
			areaBar.find('select[name=oneArea]').parent().remove();
			areaBar.find('select[name=twoArea]').parent().remove();
			areaBar.find('select[name=threeArea]').parent().remove();				
		}else if(rank==2){
			areaBar.find('select[name=twoArea]').parent().remove();
			areaBar.find('select[name=threeArea]').parent().remove();				
		}else if(rank==3){
			areaBar.find('select[name=threeArea]').parent().remove();	
		}
	}
	return false;		
}

/*全站统一函数库初始化左侧栏目*/
var UtilFun = {
	initPage:function(page_obj){
		//检测区域
		areaObject.init(page_obj);
	},
	selected:function(obj){
		var li  = $(obj).parent('li'),url = li.attr('url'),tabid = li.attr('tabid'),menuid=li.attr('menuid'); 
		var isDrop = li.parent('ul').attr('role');
		var page_id = 'page_'+tabid;
		//设置全局变量
		MENU_ID = menuid;
		TAB_ID  = tabid;
		PAGE_ID = 'page_'+tabid;
		PAGE_URL = url;
		li.addClass('selected').siblings().removeClass();
		if(isDrop=='menu'){
			$('.tab_bar li').removeClass();
			$('.tab_bar li').each(function(i, el) { 
                if($(el).attr('menuid') == menuid){ $(el).addClass('selected')};return;
            });
		}
		$('#'+PAGE_ID).show().siblings().hide();
		$('.pageload').remove();
		var iframe = $("iframe");
        iframe.scrollWidth;
        iframe.height(iframe.height()+1);
        iframe.height(iframe.height()-1);
	},
	closed:function(obj){
		var li  = $(obj).parent('li'),ul = $(obj).parents('ul'),url = li.attr('url'),tabid = li.attr('tabid'),preli = li.prev(); 
		var dropGroup = $('#drop-group');
		var page_id = 'page_'+tabid;
		var pre_page_id = 'page_'+preli.attr('tabid');
		var has_select = false;
		//新增tab判断li个数
		var $tab_bar    = $('#portlet-title-tab .tab_bar'),$tab_wrap=$('#tab_wrap');
		var mleft = parseInt($tab_bar.css('marginLeft')),currtli_w = li.width();
		var mw = (mleft+currtli_w) > 0 ? 0: (mleft+currtli_w);
		$tab_bar.animate({marginLeft: mw+'px'}, "fast");
		
		li.remove();
		$('#'+page_id).remove();
		
		ul.find('li').each(function(index, element) { 
			if($(this).hasClass('selected')){
				has_select = true;
				return;
			}
		});
		if(!has_select){
			preli.addClass('selected');
			$('#'+pre_page_id).show();
		}
		//删除右侧菜单元素
		dropGroup.find('li').each(function(index, element) {
            if($(element).attr('tabid')==tabid){$(element).remove()}
        });
		
	},
	paseRespose:function(str){},
	checkFull:function(obj,tabid,text,uri){
		var dropli_html = '<li tabid="L'+tabid+'" menuid="'+tabid+'" url="'+uri+'"><a href="javascript:;" title="'+text+'" onClick="UtilFun.selected(this)">'+text+'</a></li>';
		var dropGroup = $('#drop-group'), aliwidth=fullwidth=0;
		
		fullwidth = obj.parent().width();
		aliwidth  = obj.width();

		//dropGroup.find('ul').append(dropli_html);
	}
};
$(function(){
	var $mainpage   = $('#main-page-bar'); 
	var $tab_bar    = $('#portlet-title-tab .tab_bar'),$parentBar = $('#portlet-title-tab'),$tab_wrap=$('#tab_wrap');
	var pageload_html = '';
	
	$mainpage.find('a').live('click',function(e){
		var url = $(this).attr('href'),isblank = $(this).attr('target'); 
		if(url == undefined || url.indexOf('#')==-1){
			//$('body').append(pageload_html);
			if(isblank!='_blank' && url !='javascript:;'){
				$('#'+PAGE_ID).load(url,function(res){
					PAGE_URL = url;
					$('.pageload').remove();
					$('#'+PAGE_ID).attr('loadurl',url);
					//初始化页面
					UtilFun.initPage($('#'+PAGE_ID));
				});
				//阻止href跳转
				e.preventDefault();
			}
		}
	});
		
	$('.page-sidebar-menu a').live('click',function(e,a){
		var $uri = a!=undefined?a:$(this).attr('url'), $tabid = $(this).attr('menuid'), $text=$(this).attr('menuName') ?  $(this).attr('menuName') : $(this).text();
		if($tabid == undefined){ 
			return false;
		}
		
		var li_html = '<li tabid="L'+$tabid+'" menuid="'+$tabid+'" url="'+$uri+'">'
		+'<a href="javascript:" title="'+$text+'" onClick="UtilFun.selected(this)">'
		+'<span>'+$text+'</span></a><a class="close" href="javascript:;" onClick="UtilFun.closed(this)">close</a></li>';		
		
		var page_id = 'page_L'+$tabid;
		
		//设置全局变量
		MENU_ID = $tabid;
		TAB_ID  = 'L'+$tabid;
		PAGE_ID = 'page_'+TAB_ID;
		PAGE_URL = $uri;

		//页面
		if($('#'+PAGE_ID).length>0){
		}else{
			$mainpage.append('<div class="portlet box" id="'+page_id+'"><div class="portlet-body"><iframe frameborder="0" width="100%" height="'+iframeHeight+'" src="'+PAGE_URL+'"></iframe></div></div>');
			
			UtilFun.checkFull($tab_bar,$tabid,$text,$uri);
			$tab_bar.append(li_html);
			
			
			//新增tab判断li个数
			var mleft = $tab_bar.css('marginLeft'),currtli_w = $tab_bar.find('li').last().width();
			if($tab_bar.width()>=$tab_wrap.width()){
				$tab_bar.animate({marginLeft: parseInt(mleft)-currtli_w+'px'}, "fast");
			}
		}	
	
		$tab_bar.find('li').each(function(index, element) {
			if($(this).attr('tabid') == ('L'+$tabid)){
				$(this).addClass('selected').siblings().removeClass();
			}
		});		
		
		$('body').append(pageload_html);
		/*$('#'+page_id).load($uri,function(res){
			$('.pageload').remove();
			$('#'+page_id).attr('loadurl',$uri);
			UtilFun.initPage($('#'+PAGE_ID));
		});*/
		$('#'+page_id).show().siblings().hide();
		
		//绑定tab事件
		UtilFun.initMenuEvent();
		//返回顶部
		$('html,body').animate({
                scrollTop:0
            }, 'slow');
		//e.preventDefault();
	});
	
	//默认首页加载
	$('body').append(pageload_html);
	//$mainpage.append('<div class="portlet box" id="page_L10000" loadurl="/admin/index/home"></div>');
	//$('#page_L10000').load('/admin/index/home',function(){$('.pageload').remove();});
	$tab_bar.find('li').eq(0).addClass('selected');
});

//tab右键菜单
$(function(){
 	//所有html元素id为demo2的绑定此右键菜单
   UtilFun.initMenuEvent = function(){ 
	$('.tab_bar > li').contextMenu('tabBarMenu', {
      //菜单样式
      //菜单项样式
      itemStyle: {
        fontFamily : 'verdana',
        backgroundColor : '#fff',
        color: '#333',
        border: 'none',
        padding: '2px 2px 2px 10px',
		fontSize:'12px',
      },
      //菜单项鼠标放在上面样式
      itemHoverStyle: {
        color: '#000',
        backgroundColor: '#eee',
        border: 'none'
      },
      //事件    
      bindings: 
          {
            'item_1': function(t) {
				var taburl = $(t).attr('url'),pageid='page_'+$(t).attr('tabid');
				$('#'+pageid).find('iframe').attr('src',taburl);
				/*var loadurl=$('#'+pageid).attr('loadurl');
				$('#'+pageid).load(loadurl,function(res){
				   $('.pageload').remove(); UtilFun.initPage($('#'+pageid));
				});	*/			
            },
            'item_2': function(t) {
               var li = $('.tab_bar > li'),menuid=$(t).attr('menuid');
			   var limenu = '';
			   for(var i=0;i<li.length;i++){
				    limenu = li.eq(i);
					if(limenu.attr('menuid')== menuid){
						limenu.find('.close').trigger('click');
						break;
					}
			   }
            },
            'item_3': function(t) {
               var li = $('.tab_bar > li'),menuid=$(t).attr('menuid');
			   var limenu = '';
			   for(var i=0;i<li.length;i++){
				    limenu = li.eq(i);
					if(limenu.attr('menuid') != menuid && limenu.attr('menuid') != 10000){
						limenu.find('.close').trigger('click');
					}
			   }
			   $('#tab_wrap .tab_bar').css('marginLeft','0px');
            },
            'item_4': function(t) {
               var li = $('.tab_bar > li'),menuid=$(t).attr('menuid');
			   var limenu = '';
			   for(var i=0;i<li.length;i++){
				    limenu = li.eq(i);
					if(limenu.attr('menuid') != 10000){
						limenu.find('.close').trigger('click');
						$('#tab_wrap .tab_bar').css('marginLeft','0px');
					}
			   }
            }
          }
    });
   } 
   UtilFun.initMenuEvent();
});