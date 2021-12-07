/**
 后台框架核心JS 编辑人:WLJ
**/
var App = function () {

    // IE mode
    var isRTL = false;
    var isIE8 = false;
    var isIE9 = false;
    var isIE10 = false;

    var sidebarWidth = 225;
    var sidebarCollapsedWidth = 35;

    var responsiveHandlers = [];

    // theme layout color set
    var layoutColorCodes = {
        'blue': '#4b8df8',
        'red': '#e02222',
        'green': '#35aa47',
        'purple': '#852b99',
        'grey': '#555555',
        'light-grey': '#fafafa',
        'yellow': '#ffb848'
    };

    var _getViewPort = function () {
        var e = window, a = 'inner';
        if (!('innerWidth' in window)) {
            a = 'client';
            e = document.documentElement || document.body;
        }
        return {
            width: e[a + 'Width'],
            height: e[a + 'Height']
        }
    }

	// Handles the go to top button at the footer
    var handleGoTop = function () {
        /* set variables locally for increased performance */
        jQuery('.footer').on('click', '.go-top', function (e) {
            App.scrollTo();
            e.preventDefault();
        });
    }
	
    var runResponsiveHandlers = function () {
        // reinitialize other subscribed elements
        for (var i in responsiveHandlers) {
            var each = responsiveHandlers[i];
            each.call();
        }
    }
	
    // initializes main settings
    var handleInit = function () {

        if ($('body').css('direction') === 'rtl') {
            isRTL = true;
        }

        isIE8 = !! navigator.userAgent.match(/MSIE 8.0/);
        isIE9 = !! navigator.userAgent.match(/MSIE 9.0/);
        isIE10 = !! navigator.userAgent.match(/MSIE 10.0/);

        if (isIE10) {
            jQuery('html').addClass('ie10'); // detect IE10 version
        }
        
        if (isIE10 || isIE9 || isIE8) {
            jQuery('html').addClass('ie'); // detect IE10 version
        }

        /*
          Virtual keyboards:
          Also, note that if you're using inputs in your modal – iOS has a rendering bug which doesn't 
          update the position of fixed elements when the virtual keyboard is triggered  
        */
        var deviceAgent = navigator.userAgent.toLowerCase();
        if (deviceAgent.match(/(iphone|ipod|ipad)/)) {
            $(document).on('focus', 'input, textarea', function () {
                $('.header').hide();
                $('.footer').hide();
            });
            $(document).on('blur', 'input, textarea', function () {
                $('.header').show();
                $('.footer').show();
            });
        }

    }

    // Helper function to calculate sidebar height for fixed sidebar layout.
    var _calculateFixedSidebarViewportHeight = function () {
        var sidebarHeight = $(window).height() - $('.header').height() + 1;
        if ($('body').hasClass("page-footer-fixed")) {
            sidebarHeight = sidebarHeight - $('.footer').outerHeight();
        }

        return sidebarHeight;
    }	
	
    // Set proper height for sidebar and content. The content and sidebar height must be synced always.
    var handleSidebarAndContentHeight = function () {
        var content = $('.page-content');
        var sidebar = $('.page-sidebar');
        var body = $('body');
		var rightFrame=$('iframe');
        var height;

        if (body.hasClass("page-footer-fixed") === true && body.hasClass("page-sidebar-fixed") === false) {
            var available_height = $(window).height() - $('.footer').outerHeight();
            if (content.height() < available_height) {
                content.attr('style', 'min-height:' + available_height + 'px !important');
            }
        } else {
            if (body.hasClass('page-sidebar-fixed')) {
                height = _calculateFixedSidebarViewportHeight();
            } else {
                height = sidebar.height() + 20;
            }
            if (height >= content.height()) {
                content.attr('style', 'min-height:' + height + 'px !important');
            }
        }
		//rightFrame.attr('height',content.height());
		
    }
	
    // Handle sidebar menu
    var handleSidebarMenu = function () {
        jQuery('.page-sidebar').on('click', 'li > a', function (e) {
            if ($(this).next().hasClass('sub-menu') == false) {
                if ($('.btn-navbar').hasClass('collapsed') == false) {
                    $('.btn-navbar').click();
                }
                return;
            }

            if ($(this).next().hasClass('sub-menu.always-open')) {
                return;
            }

            var parent = $(this).parent().parent();
            var the = $(this);

            parent.children('li.open').children('a').children('.arrow').removeClass('open');
			parent.children('li.open').children('a').children('.fa-folder').removeClass('fa-folder-open-o');//新增
            parent.children('li.open').children('.sub-menu').slideUp(200);
            parent.children('li.open').removeClass('open'); 
			parent.children('li.active').removeClass('active');

            var sub = jQuery(this).next();
            var slideOffeset = -200;
            var slideSpeed = 200;

            if (sub.is(":visible")) {
                jQuery('.arrow', jQuery(this)).removeClass("open");
                jQuery(this).parent().removeClass("open");
				//jQuery(this).find('.selected').remove();
				
                sub.slideUp(slideSpeed, function () {
                    if ($('body').hasClass('page-sidebar-fixed') == false && $('body').hasClass('page-sidebar-closed') == false) {
                        //App.scrollTo(the, slideOffeset);
                    }
                    handleSidebarAndContentHeight();
                });
            } else {
                jQuery('.arrow', jQuery(this)).addClass("open");
				jQuery('.fa-folder', jQuery(this)).addClass("fa-folder-open-o"); //新增
                jQuery(this).parent().addClass("active open");//大栏目
				jQuery(this).append('<span class="selected"></span>');
				
                sub.slideDown(slideSpeed, function () {
                    if ($('body').hasClass('page-sidebar-fixed') == false && $('body').hasClass('page-sidebar-closed') == false) {
                        //App.scrollTo(the, slideOffeset);
                    }
                    handleSidebarAndContentHeight();
                });
            }

            e.preventDefault();
        });
	}


    // Handles sidebar toggler to close/hide the sidebar.
    var handleSidebarToggler = function () {
        var viewport = _getViewPort();
		
        if ($.cookie('sidebar_closed') === '1' && viewport.width >= 992) {
            $('body').addClass('page-sidebar-closed');
			//$("#header").hide();
			//$("#main").css("margin-top","0");
			//$('#userDiv').hide();
            
            $("#header").css({"margin-left":"35px"});
            $("#header").css("width", "calc(100% - "+$("#header").css("margin-left")+")");
            $("#logo").hide();
            wSize();
        }

        // handle sidebar show/hide
        $('.page-sidebar, .header').on('click', '.sidebar-toggler', function (e) {
            var body = $('body');
            var sidebar = $('.page-sidebar');
			//控制tab大小
			var $tab_wrap = $('#tab_wrap'),$parentBar = $('#portlet-title-tab');
			
		
            if ((body.hasClass("page-sidebar-hover-on") && body.hasClass('page-sidebar-fixed')) || sidebar.hasClass('page-sidebar-hovering')) {
                body.removeClass('page-sidebar-hover-on');
                sidebar.css('width', '').hide().show();
                $.cookie('sidebar_closed', '0',{ path: "/"});
                e.stopPropagation();
                runResponsiveHandlers();
                return;
            }

            $(".sidebar-search", sidebar).removeClass("open");

            if (body.hasClass("page-sidebar-closed")) {
                body.removeClass("page-sidebar-closed");
                if (body.hasClass('page-sidebar-fixed')) {
                    sidebar.css('width', '');
                }
                $.cookie('sidebar_closed', '0',{ path: "/"});
            } else {
                body.addClass("page-sidebar-closed");
                $.cookie('sidebar_closed', '1',{ path: "/"});
            }

            if (body.hasClass("page-sidebar-closed")) {
            	$("#header").css({"margin-left":"35px"});
                $("#logo").hide();
            }else{
            	$("#header").css("margin-left", "200px");
                $("#logo").show();
            }
            $("#header").css("width", "calc(100% - "+$("#header").css("margin-left")+")");
            
			$tab_wrap.width($parentBar.width()-35);
			wSize();
            runResponsiveHandlers();
        });
	}
	
    //风格设置JS
    var handleTheme = function () {
		var panel =  $('#themestyle');
        // handle theme colors
        var setColor = function (color) {
            $('#style_color').attr("href", "/admin/css/themes/" + color + ".css");
            $.cookie('style_color', color,{ path: "/"});
        }

        $('#themestyle li').click(function () {
            var color = $(this).attr("data-style");
            $.cookie('style_color', null,{ path: "/"});
            setColor(color);
            $('ul > li', panel).removeClass("current");
            $(this).addClass("current");
        });

        if ($.cookie('style_color')) {
            setColor($.cookie('style_color'));
        }
		
    }

    //* END:CORE HANDLERS *//		
	
	//全站通用表格排序
	var initDataTableHelper =  function() {
		if ($.fn.dataTable) {
			$('[data-provide="datatable"]').each (function () {	
				$(this).addClass ('dataTable-helper');	
				// 初始化配置	
				var defaultOptions = {
						paginate: true,
						search: true,
						info: true,
						lengthChange: true,
						displayRows: 10,
						ajaxUrl:''// ajax从后台获取数据
					},
					dataOptions = $(this).data (),
					helperOptions = $.extend (defaultOptions, dataOptions),
					$thisTable,
					tableConfig = {}; 
					
				//定义配置	
				tableConfig.iDisplayLength = helperOptions.displayRows;
				tableConfig.bFilter = true;
				tableConfig.bSort = true;
				tableConfig.bPaginate = false;
				tableConfig.bLengthChange = false;	
				tableConfig.bInfo = false;
				tableConfig.oLanguage={
					"sProcessing": '<span class="note-success" style="padding:10px;border-radius:10px">&nbsp;加载中请等待...</span>',
					"sEmptyTable":"暂无数据",
					"sInfoEmpty":"当前显示 0  到 0  条 共 0 条记录",
					"sInfo":"当前显示 _START_ 到 _END_ 条,共 _TOTAL_ 条记录",
					"oPaginate":{
						"sFirst":"第一页"	,
						"sPrevious":"上一页",
						"sNext":"下一页"	,
						"sLast":"最后一页"	
					}
				};


				if (helperOptions.paginate) { tableConfig.bPaginate = true; }
				if (helperOptions.lengthChange) { tableConfig.bLengthChange = true; }
				if (helperOptions.info) { tableConfig.bInfo = true; }       
				if (helperOptions.search) { $(this).parent ().removeClass ('datatable-hidesearch'); }	
				
  		

				tableConfig.aaSorting = [];
				tableConfig.aoColumns = [];

				//列属性定义排序
				$(this).find ('thead tr th').each (function (index, value) {
					var sortable = ($(this).data ('sortable') === true) ? true : false;
					tableConfig.aoColumns.push ({ 'bSortable': sortable });

					if ($(this).data ('direction')) {
						tableConfig.aaSorting.push ([index, $(this).data ('direction')]);
					}
				});		

				//是否支持ajax获取数据
				if (helperOptions.ajaxUrl && helperOptions.ajaxUrl !='')
				{ 
					tableConfig.bProcessing = true; //是否显示“正在处理”这个提示信息
					tableConfig.bServerSide = true; //必须设置异步延迟加载
					tableConfig.sAjaxSource = helperOptions.ajaxUrl;
					tableConfig.fnServerData = function (sSource, aoData, fnCallback){
						$.post(sSource,{aoData:JSON.stringify(aoData)},function(resp){
							fnCallback(resp);
						},'JSON');
					}
				
				}  
								
				//开始创建Table
				$thisTable = $(this).dataTable (tableConfig);

				if (!helperOptions.search) {
					$thisTable.parent ().find ('.dataTables_filter').remove ();
				}

				var filterableCols = $thisTable.find ('thead th').filter ('[data-filterable="true"]');

				if (filterableCols.length > 0) {
					var columns = $thisTable.fnSettings().aoColumns,
						$row, th, $col, showFilter;

					$row = $('<tr>', { cls: 'dataTable-filter-row' }).appendTo ($thisTable.find ('thead'));

					for (var i=0; i<columns.length; i++) {
						$col = $(columns[i].nTh.outerHTML);
						showFilter = ($col.data ('filterable') === true) ? 'show' : 'hide';

						th = '<th class="' + $col.prop ('class') + '">';
						th += '<input type="text" class="form-control input-sm ' + showFilter + '" placeholder="' + $col.text () + '">';
						th += '</th>';
						$row.append (th);
					}

					$row.find ('th').removeClass ('sorting sorting_disabled sorting_asc sorting_desc sorting_asc_disabled sorting_desc_disabled');

					$thisTable.find ('thead input').keyup( function () {
						$thisTable.fnFilter( this.value, $thisTable.oApi._fnVisibleToColumnIndex( 
							$thisTable.fnSettings(), $thisTable.find ('thead input[type=text]').index(this) ) );
					});

					$thisTable.addClass ('datatable-columnfilter');
				}
				
			});

			$('.dataTables_filter input').prop ('placeholder', '搜索');
		}
	}	
	
	var initTabBar = function(){
		//控制tab大小
		var $tab_wrap = $('#tab_wrap'),$parentBar = $('#portlet-title-tab');
		$tab_wrap.width($parentBar.width()-35);
	}
    //* END:CORE HANDLERS *//

    return {

        //main function to initiate the theme
        init: function () {

            handleInit(); // initialize core variables
  
            handleSidebarMenu(); // handles main menu
			
            handleSidebarToggler(); // handles sidebar hide/show  
			
			handleGoTop(); //handles scroll to top functionality in the footer
			handleTheme(); //风格设置
			initDataTableHelper ();//数据表
			initTabBar();//初始化tab
			
        },
        // wrapper function to scroll(focus) to an element
        scrollTo: function (el, offeset) {
            pos = (el && el.size() > 0) ? el.offset().top : 0;
            jQuery('html,body').animate({
                scrollTop: pos + (offeset ? offeset : 0)
            }, 'slow');
        },

        // function to scroll to the top
        scrollTop: function () {
            App.scrollTo();
        }
	}

}();

$(function(){
	//改变错误提示
	$('input').bind('click',function(){ 
		$('.alert').slideUp(100)	
	});
	App.init();
});

var isConfirmed = true;
function ajaxDelete(obj){
		var itemid = new Array();
		var url = $(obj).attr('data-url');
		$('input[name=itemid]').each(function(){
				if(this.checked){
					//itemid += this.value+',';
					itemid.push(this.value);
				}	
		});
		if(itemid==''){
			return;
		}
		//isConfirmed =confirm("确定要执行删除操作吗？");
		if(isConfirmed){
			//itemid = itemid.substring(0,itemid.length-1);
			//发送数据
			$.post(url,
			{
			  itemid:itemid
			},
			function(res){
				res  = $.parseJSON(res);
				if(res.r==1){window.location.reload();}else{
					$.howl ({
						type: 'danger'
						, title: '温馨提示：'
						, content: res.html
						, sticky: $(this).data ('sticky')
						, lifetime: 7500
						, iconCls: $(this).data ('icon')
					});
				}
			});	
		}
}

function msg(msg){
	$("#msg").remove();
	var msgStr = '<div id="msg"><a href="#basic" id="msgbtn" style="display: none;" data-toggle="modal" class="btn default">信息提示</a>';
msgStr += '<div aria-hidden="false" role="basic" tabindex="-1" id="basic" class="modal fade in" style="display: none;">';
								msgStr +='<div class="modal-dialog">';
									msgStr +='<div class="modal-content">';
										msgStr +='<div class="modal-header">';
											msgStr +='<button aria-hidden="true" data-dismiss="modal" class="close" type="button"></button>';
											msgStr +='<h4 class="modal-title">信息提示</h4>';
										msgStr +='</div>';
										msgStr +='<div class="modal-body">';
										msgStr +=msg;
										msgStr +='</div>';
										msgStr +='<div class="modal-footer">';
											<!--<button data-dismiss="modal" class="btn default" type="button">Close</button>-->
											msgStr +='<button data-dismiss="modal" class="btn blue" type="button">关闭</button>';
										msgStr +='</div>';
									msgStr +='</div>';
									
								msgStr +='</div>';
								
							msgStr +='</div></div>';
	
	$("body").append(msgStr);
	$("#msgbtn").click();
	return false;
}
