<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>空域列表</title>
<%@ include file="../tool.jsp"%>
<style type="text/css">
table>thead>tr,table>tbody>tr>td{
	height: 20px;
}
.table>thead>tr>th,.table>tbody>tr>td{
	word-wrap:break-word;
	word-break:break-all;
	padding: 2px 0 2px 5px;
}
.table{
	margin-bottom : 0
}
table tbody{
	display:block;
	max-height: 150px;
	overflow-y:scroll;
}
table thead,tbody tr{
	display: table;
	width:100%;
	table-layout:fixed;
	cursor: pointer;
}
table thead {
    width: calc( 100% - 0.5rem );
}
div#divTable{
	margin: 0;
	padding: 0; 
	position: absolute;
	bottom: 7px;
	/* opacity:0.6; */
	width: 99%;
	right: 5px;
}
</style>
<script type="text/javascript">
    var mhOrjq='jq';   //显示mh模糊网格 还是jq精确空域
	//空域形状
	var space_shape_wg = "${fns:findKey('space_shape_wg')}";
	var space_shape_dbx = "${fns:findKey('space_shape_dbx')}";
	var space_shape_yx = "${fns:findKey('space_shape_yx')}";
	var space_shape_sx = "${fns:findKey('space_shape_sx')}";
	var space_shape_xhcq = "${fns:findKey('space_shape_xhcq')}";
	var space_shape_jczaw = "${fns:findKey('space_shape_jczaw')}";

	var infoSpaceTypeList = [];// 空域类别
	<c:forEach var="type" items="${infoSpaceTypeList}">
		var infoSpaceType = {
			spcType: '${type.spcType}',
			typeName: '${type.typeName}',
			colorBack: '${type.colorBack}',
			colorBorder: '${type.colorBorder}'
		};
		infoSpaceTypeList.push(infoSpaceType);
	</c:forEach>
	// 根据空域类型标识获得空域类型实体
	function getInfoSpaceType(spcType){
		for(var i=0;i<infoSpaceTypeList.length;i++){
			if(infoSpaceTypeList[i].spcType == spcType){
				return infoSpaceTypeList[i];
			}
		}
		return {};
	}

	var infoSpaceList = [];//存储空域列表
	var overlays_all=new Array();//所有页面上画的覆盖物
	
	//点击查询button
	function search(){
		mapIframe.clearPopup(); 
		getInfoSpaceList();
	}
	
	//重置查询条件
	function reset(){
		$("#searchForm")[0].reset();
		$("#searchForm").find("select").val("");
		$("#searchForm").find("select").select2(); 
	}
	
	//点击分页的按钮触发查询
	function page_submit(pageParam,page){
		sendListAjaxQuery(page);//发送ajax请求
	}
	
	//查询空域列表
	function getInfoSpaceList(){
		var ajaxData = {
			spcName:$("input[name='spcName']").val(),
			spcShape:$("select[name='spcShape']").val(),
			spcType:$("select[name='spcType']").val(),
			spcKind:$("select[name='spcKind']").val()
		};
		$.ajax({
			url:"${pageContext.request.contextPath}/spaceQueryList_.action",
			type:"post",
			data:ajaxData,
			dataType:"json",
			//async:false,
			success:function(data){
				if($("#alert_div")){
					$("#alert_div").remove();
				}
				if(data.errCode=="0"){   //失败
					$(".right_content_all:first").before("<div id=\"alert_div\" class=\"alert alert-success \" style=\"background-color: red;color:#fff;\"><button data-dismiss=\"alert\" class=\"close\" style=\"color: #fff;\">×</button>"+data.errMsg+"</div>");
				}else{ //成功
					infoSpaceList = data.data.infoSpaceList;
					sendListAjaxQuery(1);
					//画空域
					drawInfoSpace();
				}
			}
		});
	}
	
	//获取数组对象的分页数据
	function pagination(pageNo, pageSize, array) {  
		var offset = (pageNo - 1) * pageSize;  
		return (offset + pageSize >= array.length) ? array.slice(offset, array.length) : array.slice(offset, offset + pageSize);  
	}
	
	//查询列表
	function sendListAjaxQuery(curPage){
		var pageSize = 10;
		var page_infoSpaceList = pagination(curPage,pageSize,infoSpaceList);
		$("#listTable tr").not(":eq(0)").remove();//获取表格除第一行以外的所有行并清空
		var addTr = "";
		$(page_infoSpaceList).each(function(index,item){
			addTr = "<tr onclick=\"showOverlay('"+item.spcId+"','"+item.spcShape+"')\">";
			addTr +="	<td style='height:20px;'>"+item.spcName;
			addTr +="	</td>";
			addTr +="	<td style='height:20px;width:10%;'>";
			addTr += getInfoSpaceType(item.spcType).typeName;
			addTr +="	</td>";
			
			addTr +="	<td style='height:20px;width:10%;'>";
			<c:forEach var="spcKindMap" items="${fns:findMap('spc_kind_map') }">
			  	if('${spcKindMap.key}' == item.spcKind){
			    	addTr += '${spcKindMap.value}';
			    }
			</c:forEach>
			addTr +="	</td>";
			
			addTr +="	<td style='height:20px;width:10%;'>";
		  	<c:forEach var="shapeMap" items="${fns:findMap('space_shape_map') }">
			  	if('${shapeMap.key}' == item.spcShape){
			    	addTr += '${shapeMap.value}';
			    }
			</c:forEach>
			addTr +="	</td>";
			addTr +="	<td style='height:20px;width:8%;'>"+item.spcBottom +"</td> ";
			addTr +="	<td style='height:20px;width:8%;'>"+item.spcTop +"</td> ";
			addTr +="</tr>";
			$("#listTable").append(addTr);
		});
		var pager = Pager({
			totalCount : infoSpaceList.length, //总条数
			pageSize : pageSize, //每页显示n条内容，默认10
			buttonSize : 5, //显示6个按钮，默认10
			pageParam : 'curPage', //页码的参数名为'curPage'，默认为'page'
			pageValue : curPage,
			className : 'pagination', //分页的样式
			prevButton : '上一页', //上一页按钮
			nextButton : '下一页', //下一页按钮
			firstButton : '首页', //第一页按钮
			lastButton : '末页', //最后一页按钮
			pageForm : $('form')[0]  //提交的form
		}); 
		$(".pagination").html(pager);//设置分页
		var pagerDesc = "<span>"+((curPage-1)*pageSize+1 )+"</span>-<span>"+curPage*pageSize +"</span>条，共<span>"+infoSpaceList.length+"</span>条";
		$(".pagination_sub").html(pagerDesc);
	}
	
	//画空域
	function drawInfoSpace(){
		mapIframe.clearOverlay();//清空地图上所有覆盖物
		overlays_all = [];
		$(infoSpaceList).each(function(index,item){
			var spaceType = getInfoSpaceType(item.spcType);
			item.spaceType = spaceType;
			var polygonOverlayArry = [];
			if(mhOrjq == 'mh'){
				for(var i=0;i<item.polygonWgs84ToWg.split(";").length;i++){
					var obj=mapIframe.drawCustomColorPolygonNoClear(item.polygonWgs84ToWg.split(";")[i],item.spaceType.colorBack,item.spaceType.colorBorder);
					clickInfoSpacePolygonToShowMsg(obj,item);
					polygonOverlayArry.push(obj);
				}
			}else{
				if(space_shape_wg == item.spcShape){
					for(var i=0;i<item.polygonWgs84.split(";").length;i++){
						var obj=mapIframe.drawCustomColorPolygonNoClear(item.polygonWgs84.split(";")[i],item.spaceType.colorBack,item.spaceType.colorBorder);
						clickInfoSpacePolygonToShowMsg(obj,item);
						polygonOverlayArry.push(obj);
					}
				}else{
					var polygon_wgs84 = item.polygonWgs84;
					if(polygon_wgs84){
						var obj=mapIframe.drawCustomColorPolygonNoClear(polygon_wgs84,item.spaceType.colorBack,item.spaceType.colorBorder);
						clickInfoSpacePolygonToShowMsg(obj,item);
						polygonOverlayArry.push(obj);
					}
				}
			}
			overlays_all.push([item.spcId,polygonOverlayArry,item.spaceType.colorBorder]);
		});
		if(overlays_all.length>0){
			mapIframe.positionSpace();//根据所画的图形定位和缩放地图
		}
	}
	
	//为多边形绑定click事件并提供展示信息内容
	function clickInfoSpacePolygonToShowMsg(polygonOverlay,infoSpaceObj){
		var infoWindowMsg = 
			"<table class='table table-bordered'>"+
				"<tr>"+
					"<td>空域名称</td>"+
					"<td colspan='3' style='max-width: 220px;text-align:left;padding-left:5px;'>"+infoSpaceObj.spcName+"</td>"+
				"</tr>"+
				"<tr>"+
					"<td>空域类型</td>"+
					"<td colspan='3' style='max-width: 220px;'>";
					infoWindowMsg += getInfoSpaceType(infoSpaceObj.spcType).typeName;
					infoWindowMsg+="</td>"+
				"</tr>"+
				"<tr>"+
					"<td>空域分类</td>"+
					"<td colspan='3' style='max-width: 220px;'>";
						<c:forEach var="spcKindMap" items="${fns:findMap('spc_kind_map') }">
						  	if('${spcKindMap.key}' == infoSpaceObj.spcKind){
						  		infoWindowMsg += '${spcKindMap.value}';
						    }
						</c:forEach>
					infoWindowMsg+="</td>"+
				"</tr>"+
				"<tr>"+
					"<td style='width:20%'>形状</td>"+
					"<td style='width:30%'>";
					  	<c:forEach var="shapeMap" items="${fns:findMap('space_shape_map') }">
						  	if('${shapeMap.key}' == infoSpaceObj.spcShape){
						  		infoWindowMsg += '${shapeMap.value}';
						    }
						</c:forEach>
					infoWindowMsg+="</td>"+
					"<td>高度</td>"+
					"<td>"+infoSpaceObj.spcBottom+"-"+infoSpaceObj.spcTop+"</td>"+
				"</tr>";
				infoWindowMsg+="<tr>";
				infoWindowMsg+="<td colspan='4' style='text-align:left;padding-left:5px;'>时段";
				infoWindowMsg+="</td>";
				infoWindowMsg+="</tr>";
				if(infoSpaceObj.infoSpaceTimeList && infoSpaceObj.infoSpaceTimeList.length>0){
					for(var i = 0;i<infoSpaceObj.infoSpaceTimeList.length;i++) {
						infoWindowMsg+="<tr>";
						infoWindowMsg+="<td colspan='4' style='text-align:left;padding-left:5px;'>";
						infoWindowMsg+=infoSpaceObj.infoSpaceTimeList[i].begDatetime+"&nbsp;至&nbsp;"+infoSpaceObj.infoSpaceTimeList[i].endDatetime;
						infoWindowMsg+="</td>";
						infoWindowMsg+="</tr>";
					}
				}else{
					infoWindowMsg+="<tr>";
					infoWindowMsg+="<td colspan='4' style='text-align:left;padding-left:5px;'>";
					infoWindowMsg+="永久有效";
					infoWindowMsg+="</td>";
					infoWindowMsg+="</tr>";
				}
				
			if("${fns:findKey('space_type_bmdky')}" == infoSpaceObj.spcType){
				// 白名单空域，显示其白名单的无人机/定位终端
				infoWindowMsg+="<tr>";
				infoWindowMsg+="<td colspan='4' style='text-align:left;padding-left:5px;'>白名单无人机";
				infoWindowMsg+="</td>";
				infoWindowMsg+="</tr>";
				var whiteUavList = infoSpaceObj.whiteUavList;
				if(whiteUavList!=null && whiteUavList.length>0){
					var whiteUavMsg = "";
					for(var i=0;i<whiteUavList.length;i++){
						var whiteUav = whiteUavList[i];
						var relType = whiteUav.relType;
						var infoUav = whiteUav.infoUav;
						var runTerminal = whiteUav.runTerminal;
						var oneMsg = "";
						if("${fns:findKey('rel_type_uav')}" == relType && infoUav!=null){
							oneMsg = "编号：" + infoUav.uavSn + " 厂商：" + infoUav.vender + " 型号：" + infoUav.model;
						}else if("${fns:findKey('rel_type_tmn')}" == relType && runTerminal!=null){
							oneMsg = "编号：" + runTerminal.tmnCode + " 厂商：" + runTerminal.brand + " 型号：" + runTerminal.model;
						}
						whiteUavMsg += (i+1) + ".<span style='max-width: 264px; word-break: break-all; white-space: break-spaces; margin-left:5px;'>"+ oneMsg +"</span><br>";
					}
					infoWindowMsg+="<tr>";
					infoWindowMsg+="<td colspan='4' style='text-align:left;padding-left:5px;max-width: 264px;'>";
					infoWindowMsg+=whiteUavMsg;
					infoWindowMsg+="</td>";
					infoWindowMsg+="</tr>";
				}else{
					infoWindowMsg+="<tr>";
					infoWindowMsg+="<td colspan='4' style='text-align:left;padding-left:5px;'>";
					infoWindowMsg+="未设置";
					infoWindowMsg+="</td>";
					infoWindowMsg+="</tr>";
				}
			}
		infoWindowMsg+=	"</table>";
		mapIframe.polygonClickOpenMsg(polygonOverlay,infoWindowMsg);
	}

	//放大地图
	function zoomInMap(elem){
		$(".searchDiv").hide();
		$("#legentDiv").css("top","50px");
		var className=$(elem).find('i:eq(0)').attr('class');
		if(className=='icon-angle-up'){
			$(elem).attr('onclick','zoomOutMap(this)');
			$(elem).find('i:eq(0)').attr('class','icon-angle-down');
		}
	}
	
	//缩小地图
	function zoomOutMap(elem){
		$(".searchDiv").show();
		$("#legentDiv").css("top",($(".searchDiv:eq(0)").parent().height()-30+50)+"px");
		var className=$(elem).find('i:eq(0)').attr('class');
		if(className=='icon-angle-down'){
			$(elem).attr('onclick','zoomInMap(this)');
			$(elem).find('i:eq(0)').attr('class','icon-angle-up');
		}
	}
	
	$(function(){
		zoomInMap();
		$("#mapIframe").css({"height" : ($(parent.document.getElementsByTagName("iframe")[1]).height()-52)});//使地图可以全屏显示
		//当窗口变化时重置地图大小
		$(window).resize(function() {
			$("#mapIframe").css({"height" : ($(parent.document.getElementsByTagName("iframe")[1]).height()-52)});//使地图可以全屏显示
		});
		//iframe加载完成后执行的方法
		$("#mapIframe").load(function(){
			setTimeout(function(){
				search();
			},100);//之所以是要延迟，是因为需要等待地图全部显示出来后再赋值，这样才能实现根据覆盖物定位
			
			//比例尺
			mapIframe.setControlPosition(mapIframe.getScaleControl(),mapIframe.TOP_RIGHT);
			//地图类型
			mapIframe.setControlPosition(mapIframe.getMaptypeControl(),mapIframe.TOP_RIGHT);
			//缩放
			mapIframe.setControlPosition(mapIframe.getNavigationControl(),mapIframe.TOP_RIGHT);
			//测量工具
			mapIframe.setControlPosition(mapIframe.getMeasureControl(),mapIframe.TOP_RIGHT);
			//添加自定义控件
			//var myGeoCtrl = mapIframe.addGeoControl(geoControlCallBack,{anchor:mapIframe.TOP_RIGHT,offset:mapIframe.createOffset(105,10)});
			
			$("div#divTable").mouseover(function(){
				$('#divTable tbody').show();
			});	  
			
			$("div#divTable").mouseout(function(){
				$('#divTable tbody').hide();
			});	  
			$('#divTable tbody').hide();
	    }); 
	})
	
	//自定义控件回调方法
	function geoControlCallBack(divId){
		mhOrjq=divId;
		drawInfoSpace();
	}

	//点击列表定位空域
	function showOverlay(spcId,spcShape){
		mapIframe.clearPopup(); 
		var overlayArry=[];
		for(var i=0;i<overlays_all.length;i++){
			overlayArry=overlays_all[i][1];
			if(overlays_all[i][0]==spcId){
				//查询到选中的空域
				if(overlayArry){
					var points = [];
					for(var j=0;j<overlayArry.length;j++){
						points = points.concat(mapIframe.getPolygonPoint(overlayArry[j]));
					}
					//定位空域
					mapIframe.positionPointOverlayArray(overlayArry);
					//打开空域信息框
					var centerPoint = mapIframe.getCenterPointByPointArray(points);
					mapIframe.triggerPointOverlayArryClick(overlayArry[0],{point:centerPoint});
					
				}
				break;
			}
		}
	}
	
</script>
</head>
<body>
	<!-- 图例 -->
	<div id="legentDiv" style="position: absolute;left: 10px;top: 50px;z-index: 3;background-color: #ffffff;border-radius: 8px;">
		<table style="max-width: 129px;font-size: 12px;text-align: left;margin: 10px;">
			<tbody style="overflow: hidden; max-height: none;">
				<c:forEach var="item" items="${infoSpaceTypeList }">
					<tr style="height: 25px;">
						<td style="width: 20px;padding-left:7px;"><div style="width: 16px; height:16px; background-color: ${item.colorBack};"></div></td>
						<td style="line-height: 25px; padding: 0; opacity:0.8;"><span style="margin: 0 0x;">${item.typeName }</span></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- 显示鼠标所在经纬度以及当前地图缩放比例 -->
	<div id="currLngLatZoomDiv" style="position: absolute; top: 12px; left: 160px; font-size: small; font-family: serif; color: #4174CD;">
		<div style="float: left; margin-right: 10px;">
			<label style="float:left">经度:</label>
			<label style="float:left" id="currMouseLng"></label>
		</div>
		<div style="float: left; margin-right: 10px;">
			<label style="float:left">纬度:</label>
			<label style="float:left" id="currMouseLat"></label>
		</div>
		<div style="float: left;">
			<label style="float:left">缩放级别:</label>
			<label style="float:left" id="currZoom"></label>
		</div>
	</div>
	<div class="right_content">
		<lg:errors />
		<div class="right_content_all">
			<a href="javascript:void(0);" onclick="zoomOutMap(this)" style="text-decoration: none;">
				<div class="right_content_all_top">
					<span>检索条件</span>
		                <span class="btn btn-xs" style="float:right;line-height: 1.5;border:none; border-left:1px solid #dee5e7;">
		                  <i class="icon-angle-down"></i>
		                </span>
				</div>
            </a>
			<div class="right_content_select searchDiv">
				<form action="${ctx }/spaceQueryList_.action" method="post" id="searchForm">
				    <div class="span4 right_content_select_box">
						<span class="right_content_select_name">空域名称：</span> 
						<input id="spcName" class="right_content_select_ctt right_content_select_cttt" placeholder="请输入" type="text"  name="spcName" value="${infoSpace.spcName }" />
					</div>
					<div class="span4 right_content_select_box">
						<span class="right_content_select_name">空域形状：</span>
						<select class="right_content_select_ctt"  name="spcShape" id="spcShape">
							<option value="">全部</option>
							<c:forEach var="item" items="${fns:findMap('space_shape_map') }">
								<option value="${item.key }" ${infoSpace.spcShape==item.key ? "selected" : "" }>${item.value }</option>
							</c:forEach>
						</select>
					</div>
					<div class="span4 right_content_select_box">
						<span class="right_content_select_name">空域类型：</span> 
						<select class="right_content_select_ctt"  name="spcType" id="spcType">
							<option value="">全部</option>
							<c:forEach var="item" items="${infoSpaceTypeList }">
								<option value="${item.spcType }" ${item.spcType == infoSpace.spcType ? "selected" : "" }>${item.typeName }</option>
							</c:forEach>
						</select>
					</div>
					<div class="span4 right_content_select_box">
						<span class="right_content_select_name">空域分类：</span>
						<select class="right_content_select_ctt"  name="spcKind" id="spcKind">
							<option value="">全部</option>
							<c:forEach var="item" items="${fns:findMap('spc_kind_map') }">
								<option value="${item.key }" ${infoSpace.spcKind==item.key ? "selected" : "" }>${item.value }</option>
							</c:forEach>
						</select>
					</div>
	            </form>	
			</div>
			<div class="right_content_btnbox searchDiv">
				<div onclick="search()" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_search">
					<img src="${pageContext.request.contextPath }/images/search_btn.png"/>
					<span>搜索</span>
				</div>
				<div onclick="reset()" class="right_content_btnbox_btn right_content_btnbox_resize">
					<img src="${ctx }/images/resize_btn.png"/>
					<span>清空</span>
				</div>
			</div>
		</div>
	</div>
	<div class="right_content" style="padding-top: 0;padding-bottom: 0;position: relative;">
		<iframe src="${ctx }/leaflet/leaflet_map.jsp" name="mapIframe" id="mapIframe" width="100%" height="500px" frameborder="0" ></iframe>
		<div class="right_content_all">
			<%--列表展示--%>
			<div class="right_content_table" id="divTable" style="z-index: 99;">
				<div style="height:30px;display: inline-block;width:100%;">
					<div style="float: right;height:30px;width:90%;">
						<span class="pagination_sub" style="padding: 0px;padding-left: 5px;background-color:#fff;margin-top:1px;line-height:28px;border:1px solid #e5e5e5;border-radius: 4px;">
						</span>
						<div class="pagination" style="display: inline-block;margin-top: -7px;float: right;" >
						</div>
					</div>
				</div>
				<div>
					<table class="table table-bordered table_list table-striped" id="listTable">
						<thead>
							<tr class="active blue_active_op">
								<th width="">空域名称</th>
								<th width="10%">空域类型</th>
								<th width="10%">空域分类</th>
								<th width="10%">空域形状</th>
								<th width="8%">底高(米)</th>
								<th width="8%">顶高(米)</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
