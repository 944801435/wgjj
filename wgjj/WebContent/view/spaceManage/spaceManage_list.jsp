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
	padding: 2px 0 2px 4px;
}
.table{
	margin-bottom : 0
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
.control-group{
	padding-bottom: 0;
}
.form-horizontal{
	margin: 5px;
}
.form-horizontal .control-group{
	margin-bottom: 0;
}
.form-horizontal .control-label{
	width: 80px;
}
.form-horizontal .controls{
	margin-left: 80px;
}
.addJwd{
	cursor: pointer;
}
.shortInput{
	width: 125px;
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
ul>.disabled>a{
	background-color: #fff !important;
}
</style>
<script type="text/javascript" src="${ctx }/js/vue.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/validate.js"></script>
<script type="text/javascript">
	//空域形状
	var space_shape_wg = "${fns:findKey('space_shape_wg')}";
	var space_shape_dbx = "${fns:findKey('space_shape_dbx')}";
	var space_shape_yx = "${fns:findKey('space_shape_yx')}";
	var space_shape_sx = "${fns:findKey('space_shape_sx')}";
	var space_shape_xhcq = "${fns:findKey('space_shape_xhcq')}";
	var space_shape_jczaw = "${fns:findKey('space_shape_jczaw')}";
	
	var isSpaceNeed="${userType}"=="0";   //是否空域需求0需求1划设
	
	var gridDftColorBack='#A8A8A8';
	var gridDftColorBorder='#A8A8A8';
	var gridSelColorBack='#00FCFF';
	var gridSelColorBorder='#00FCFF';
	var drag_start=false;
	var curGrids=[];
	var newGrids=[];
	var selGrids=[];
	var isGrid=false;//是否是网格
	var gridMapLvl=13;
	var gridBackOpacity=0.4;
	var gridBorderOpacity=0.4;
	var isInEdit=false;
	var editCacheGridOverlay=[];
	var infoSpaceList = [];//存储空域列表
	var overlays_all=new Array();//所有页面上画的覆盖物
	var selectJwdTr = null;//要选点的tr
	
	//点击查询button
	function search(){
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
			userType:"${userType}",
			spcName:$("input[name='spcName']").val(),
			spcShape:$("select[name='spcShape']").val(),
			spcType:$("select[name='spcType']").val(),
			spcKind: $("select[name='spcKind']").val(),
			spcSts: $("select[name='spcSts']").val()
		};
		$.ajax({
			url:"${pageContext.request.contextPath}/spaceManageList_.action",
			type:"post",
			data:ajaxData,
			dataType:"json",
			//async:false,
			success:function(data){
				if($("#alert_div")){
					$("#alert_div").remove();
				}
				if(data.errCode=="0"){   //失败
					$(".right_content_all").before("<div id=\"alert_div\" class=\"alert alert-success \" style=\"background-color: red;color:#fff;\"><button data-dismiss=\"alert\" class=\"close\" style=\"color: #fff;\">×</button>"+data.errMsg+"</div>");
				}else{ //成功
					infoSpaceList = data.data.infoSpaceList;
					sendListAjaxQuery(1);
					mapIframe.clearOverlay();//清空地图上所有覆盖物
					overlays_all = [];
					var polygon_wgs84 = "";
					$(infoSpaceList).each(function(index,item){
						var polygonOverlayArry = [];
						for(var i=0;i<item.polygonWgs84.split(";").length;i++){
							var obj=mapIframe.drawCustomColorPolygonNoClear(item.polygonWgs84.split(";")[i],item.spaceType.colorBack,item.spaceType.colorBorder,gridBackOpacity,gridBorderOpacity);
							clickInfoSpacePolygonToShowMsg(obj,item);
							//可修改
							if((isSpaceNeed && (item.spcRevSts == "${fns:findKey('app_rst_wait')}" || item.spcRevSts == "${fns:findKey('app_rst_no')}") && "${fns:hasPms(pmsIds,'10601')}"=="true") || (!isSpaceNeed && item.spcSts != "${fns:findKey('spc_sts_yfb')}" && "${fns:hasPms(pmsIds,'1101')}"=="true")){
								doubleClickInfoSpacePolygonToOpenEditWin(obj,item.spcId);
							}
							polygonOverlayArry.push(obj);
						}
						overlays_all.push([item.spcId,polygonOverlayArry]);
					});

					if(overlays_all.length>0){
						mapIframe.positionSpace();//根据所画的图形定位和缩放地图
					}
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

			//是否包含空域管理的发布权限
			if("${fns:hasPms(pmsIds,'1102')}"=="true" && !isSpaceNeed){
				//审批页面审批通过的空域可以进行发布
				addTr +="   <td style='height:20px;width:3%'><input type='checkbox' name='listSpcId' value='"+item.spcId+"'/></td>";
			}
			addTr +="	<td style='height:20px;'>"+item.spcName;
			addTr +="	</td>";
			addTr +="	<td style='height:20px;width:10%;'>"+item.spaceType.typeName;
			addTr +="	</td>";
			
			addTr +="	<td style='height:20px;width:8%;'>";
			<c:forEach var="spcKindMap" items="${fns:findMap('spc_kind_map') }">
			  	if('${spcKindMap.key}' == item.spcKind){
			    	addTr += '${spcKindMap.value}';
			    }
			</c:forEach> 
			addTr +="	</td>";
			if(!isSpaceNeed){
				addTr +="	<td style='height:20px;width:5%;'>";
			    <c:forEach var="spcStsMap" items="${fns:findMap('spc_sts_map') }">
				  	if('${spcStsMap.key}' == item.spcSts){
				    	addTr += '${spcStsMap.value}';
				    }
				</c:forEach> 
				addTr +="	</td>";
			}
			
			addTr +="	<td style='height:20px;width:13%;'>"+item.crtTime +"</td> ";
			if(isSpaceNeed){
				addTr +="	<td style='height:20px;width:6%;'>";
			    <c:forEach var="spcRevStsMap" items="${fns:findMap('app_rst_all_map') }">
				  	if('${spcRevStsMap.key}' == item.spcRevSts){
				    	addTr += '${spcRevStsMap.value}';
				    }
				</c:forEach> 
				addTr += "</td> ";
				addTr +="	<td style='height:20px;width:13%;'>"+(item.revTime == null ? "" : item.revTime) +"</td> ";
				addTr +="	<td style='height:20px;width:20%;'>"+(item.revMsg == null ? "" : item.revMsg) +"</td> ";
			}
			
			addTr +="<td style='height:20px;width:10%;'>";
			if((isSpaceNeed && (item.spcRevSts == "${fns:findKey('app_rst_wait')}" || item.spcRevSts == "${fns:findKey('app_rst_no')}") && "${fns:hasPms(pmsIds,'10601')}"=="true") || (!isSpaceNeed && item.spcSts != "${fns:findKey('spc_sts_yfb')}" && "${fns:hasPms(pmsIds,'1101')}"=="true")){
				addTr += "<a style='cursor:pointer;' onclick='javascript:deleteInfoSpace(\""+item.spcId+"\");'>删除</a>&nbsp;";
				addTr += "<a style='cursor:pointer;' onclick='javascript:openEditWin(\""+item.spcId+"\");'>编辑</a>&nbsp;";
			}
			//空域需求可以提交
			if((item.spcRevSts == "${fns:findKey('app_rst_wait')}" || item.spcRevSts == "${fns:findKey('app_rst_no')}") && isSpaceNeed && "${fns:hasPms(pmsIds,'10601')}"=="true"){
				addTr += "<a style='cursor:pointer;' onclick='javascript:submitInfoSpace(\""+item.spcId+"\");'>提交</a>&nbsp;";
			}
			//空域划设可以进行发布
			if("${fns:hasPms(pmsIds,'1102')}"=="true" && !isSpaceNeed){
				if(item.spcSts == "${fns:findKey('spc_sts_yfb')}"){
					addTr +="<a style='cursor:pointer;' onclick='javascript:repealInfoSpace(\""+item.spcId+"\");'>撤销</a>&nbsp;";
				}else if(item.spcSts == "${fns:findKey('spc_sts_ycx')}" || item.spcSts == "${fns:findKey('spc_sts_wfb')}"){
					addTr +="<a style='cursor:pointer;' onclick='javascript:publishInfoSpace(\""+item.spcId+"\");'>发布</a>&nbsp;";
				}
			}
			//是否包含审批权限，是否是审批模块，是否是未审批的
			if(isSpaceNeed && "${fns:hasPms(pmsIds,'10602')}"=="true" && item.spcRevSts == "${fns:findKey('app_rst_in')}"){
				addTr +="<a style='cursor:pointer;' onclick='javascript:openAuditWin(\""+item.spcId+"\");'>审批</a>&nbsp;";
			}
			addTr +="<a style='cursor:pointer;' onclick='javascript:openViewWin(\""+item.spcId+"\");'>查看</a>";
			addTr += "</td> ";
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
	//提交空域需求申请
	function submitInfoSpace(spcId){
		if(spcId){
			Confirm("你确定要提交该条空域信息吗？",function(){
				$.ajax({
					url:"${pageContext.request.contextPath}/submitInfoSpace.action",
					type:"post",
					data:{'spcId':spcId},
					dataType:"json",
					async:false,
					success:function(data){
						alert(data.errMsg);
						closeAllWin();
						if(data.errCode=="1"){   //成功
							search();
						}
					}
				});
			},[spcId]);
		}else{
			alert("请选择一条要提交的空域信息！");
		}
	}
	//发布空域
	function publishInfoSpace(spcId){
		if(spcId){
			Confirm("你确定要发布该条空域信息吗？",function(){
				parent.openLoading();
				$.ajax({
					url:"${pageContext.request.contextPath}/publishInfoSpace.action",
					type:"post",
					data:{'spcId':spcId},
					dataType:"json",
					success:function(data){
						parent.closeLoading();
						alert(data.errMsg);
						closeAllWin();
						if(data.errCode=="1"){   //成功
							search();
						}
					}
				});
			},[spcId]);
		}else{
			alert("请选择一条要发布的空域信息！");
		}
	}
	//撤销空域
	function repealInfoSpace(spcId){
		if(spcId){
			Confirm("你确定要撤销该条空域信息吗？",function(){
				parent.openLoading();
				$.ajax({
					url:"${pageContext.request.contextPath}/repealInfoSpace.action",
					type:"post",
					data:{'spcId':spcId},
					dataType:"json",
					success:function(data){
						parent.closeLoading();
						alert(data.errMsg);
						closeAllWin();
						if(data.errCode=="1"){   //成功
							search();
						}
					}
				});
			},[spcId]);
		}else{
			alert("请选择一条要撤销的空域信息！");
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
						<c:forEach var="type" items="${infoSpaceTypeList}">
							if(infoSpaceObj.spcType == '${type.spcType}'){
								infoWindowMsg += '${type.typeName}';
							}
						</c:forEach>
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
					"<td style='width:20%'>高度</td>"+
					"<td style='width:30%'>"+infoSpaceObj.spcBottom+"-"+infoSpaceObj.spcTop+"</td>"+
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
		infoWindowMsg+=	"</table>";
		mapIframe.polygonClickOpenMsg(polygonOverlay,infoWindowMsg);
	}
	
	//双击空域弹出编辑框
	function doubleClickInfoSpacePolygonToOpenEditWin(polygonOverlay,spcId){
// 		mapIframe.polygonDoubleClickOpenEditWin(polygonOverlay,spcId);
		mapIframe.polygonBindDbClick(polygonOverlay,()=>{
			openEditWin(spcId);
		},spcId)
	}
	
	//放大地图
	function zoomInMap_list(elem){
		$(".searchDiv").hide();
		$("#legentDiv").css("top","50px");
		var className=$(elem).find('i:eq(0)').attr('class');
		if(className=='icon-angle-up'){
			$(elem).attr('onclick','zoomOutMap_list(this)');
			$(elem).find('i:eq(0)').attr('class','icon-angle-down');
		}
	}
	
	//缩小地图
	function zoomOutMap_list(elem){
		$(".searchDiv").show();
		$("#legentDiv").css("top",($(".searchDiv:eq(0)").parent().height()-30+50)+"px");
		var className=$(elem).find('i:eq(0)').attr('class');
		if(className=='icon-angle-down'){
			$(elem).attr('onclick','zoomInMap_list(this)');
			$(elem).find('i:eq(0)').attr('class','icon-angle-up');
		}
	}
	
	$(function(){
		zoomInMap_list();
		$("#mapIframe").css({"height" : ($(parent.document.getElementsByTagName("iframe")[1]).height()-49)});//使地图可以全屏显示
		//当窗口变化时重置地图大小
		$(window).resize(function() {
			$("#mapIframe").css({"height" : ($(parent.document.getElementsByTagName("iframe")[1]).height()-49)});//使地图可以全屏显示
		});
		//iframe加载完成后执行的方法
		$("#mapIframe").load(function(){
			//比例尺
			mapIframe.setControlPosition(mapIframe.getScaleControl(),mapIframe.TOP_RIGHT);
			//地图类型
			mapIframe.setControlPosition(mapIframe.getMaptypeControl(),mapIframe.TOP_RIGHT);
			//缩放
			mapIframe.setControlPosition(mapIframe.getNavigationControl(),mapIframe.TOP_RIGHT);
			//测量工具
			mapIframe.setControlPosition(mapIframe.getMeasureControl(),mapIframe.TOP_RIGHT);
			setTimeout(function(){
				search();
			},100);//之所以是要延迟，是因为需要等待地图全部显示出来后再赋值，这样才能实现根据覆盖物定位

			//地图点击事件
			mapIframe.mapBindClick((jd,wd)=>{
				setJwdToTr(jd,wd);
				drawMarker(jd,wd);
			});
			mapIframe.map.addEventListener("dragstart", function () { 
				drag_start=true; 
			});
			mapIframe.map.addEventListener("dragend", function () { 
				if(drag_start){
					drag_start=false;
					//判断是否是网格
					if(isGrid){
						showGrid(true); 
					} 
				}
			});
			mapIframe.map.addEventListener("zoomend", function() { 
				//判断是否是网格
				if(isGrid) {
					showGrid(true);
				}
			});
			mapIframe.map.addEventListener("resize", function() { 
				//判断是否是网格
				if(isGrid){
					showGrid(true);
				}
			});
		}); 
		$("div#divTable > div:gt(0)").mouseover(function(){
			$('#divTable tbody').show();
		});	  
		$("div#divTable").mouseleave(function(){
			$('#divTable tbody').hide();
		}); 
		$('#divTable tbody').hide();
	})

	var infoSpaceTypeList = [];//空域类型列表
	<c:forEach var="item" items="${infoSpaceTypeList }">
		var infoSpaceType = {
			spcType: '${item.spcType }',
			colorBack: '${item.colorBack }',
			colorBorder: '${item.colorBorder }',
			typeName: '${item.typeName}'
		};
		infoSpaceTypeList.push(infoSpaceType);
	</c:forEach>
</script>
<script type="text/javascript">
$(function(){
	//删除经纬度
	$("body").on("click",".delListJwd",function(){  
		event.preventDefault();
		var tr = $(this.parentNode.parentNode);
		clearMarker($(tr)[0]);
		tr.remove();
		if(showInMapOverlays){
			mapIframe.clearPointOverlay(showInMapOverlays);
		}
	}); 
	//在地图上选点
	$("body").on("click",".selectJwd",function(){  
		$('html, body').animate({
            scrollTop: $("#mapIframe").offset().top
        }, 1000);
        if(selectJwdTr!=null){
        	 $(selectJwdTr).find('input').css('border-color','#ccc');
        }
        selectJwdTr =$(this).parent().parent();
        $(selectJwdTr).find('input').css('border-color','red');
	});

	//添加经纬度：多边形和线缓冲区会用到该方法
	$(".addJwd").click(function(){
		var table = $(this.parentNode.parentNode.parentNode.parentNode).find("table");
		var trLength = table.find("tr").length;
		//获取形状
		var spcShape = $("#spcShape").val();
		var shapeFlag = "";
		if(spcShape == space_shape_wg){
			//网格
			shapeFlag = "0";
		}else if(spcShape == space_shape_dbx){
			//多边形
			shapeFlag = "2";
		}else if(spcShape == space_shape_yx){
			//圆形
			shapeFlag = "3";
		}else if(spcShape == space_shape_sx){
			//扇形
			shapeFlag = "6";
		}else if(spcShape == space_shape_xhcq){
			//线缓冲区
			shapeFlag = "7";
		}else if(spcShape == space_shape_jczaw){
			//机场障碍物限制面
			shapeFlag = "8";
		}
		var addTrStr = getAddTrStr("","",shapeFlag);
		table.append(addTrStr);
		var myForm=document.getElementById("form_shape_"+shapeFlag);
		Validator.Validate_onLoad(myForm,3);
	});
});
//移除对应tr的标注  如果tr为null 清空所有标注
function clearMarker(tr){
	if(tr!=null){
		mapIframe.clearPointOverlay(markerMap.get(tr));
		markerMap.delete(tr);
	}else{
		markerMap.forEach(function (value, key, map) {
			mapIframe.clearPointOverlay(value);
		});
		markerMap.clear();
	}
	
}

//返回table的tr
function getAddTrStr(jd,wd,shapeFlag){
	var addTrStr = 
		"<tr>                                                                                                           "+
		"	<td style=\"width: 45%\">                                                                                   "+
		"		经度：<input value=\""+jd+"\" type=\"text\" dataType=\"Require,Currency\" msg=\"请输入经度！\" class=\"required shortInput\" name=\"jd_"+shapeFlag+"\" /> "+
		"	</td>                                                                                                        "+
		"	<td style=\"width: 45%\">                                                                                      "+
		"		纬度：<input value=\""+wd+"\" type=\"text\" dataType=\"Require,Currency\" msg=\"请输入纬度！\" class=\"required shortInput\" name=\"wd_"+shapeFlag+"\" />  "+
		"	</td>                                                                                                        "+
		"	<td style=\"width: 10%\">                                                                                      "+
		"		<a class=\"selectJwd\" style=\"cursor: pointer;\" title=\"编辑\"><i class=\"icon-pencil\"></i></a>&nbsp;                                       "+
		"		<a class=\"delListJwd\" style=\"cursor: pointer;color:red;\">✖</a>                                          "+
		"	</td>                                                                                                        "+
		"</tr>                                                                                                           ";
	return addTrStr;
}

function goSave() {
	editCacheGridOverlay=[];
	var myForm=document.getElementById("inputForm");
	if(Validator.Validate(myForm,3)){
		//验证时段是否为空
		if($("#spcType").val()=="${fns:findKey('space_type_lsgbky')}"){//临时关闭空域
			var isNoTime = true;
			$('#time input').each(function(){
				if($.trim($(this).val()).length==0){
					isNoTime = false;
				}
			});
			if(!isNoTime){
				alert('时段不能为空！');
				return;
			}
		}else{
			var isHasOneTime = false;
			$('#time div.controls').each(function(){
				var begTime = $(this).find("input").eq(0);
				var endTime = $(this).find("input").eq(1);
				if($.trim(begTime.val()).length==0&&$.trim(endTime.val()).length!=0){
					isHasOneTime = true;
				}else if($.trim(begTime.val()).length!=0&&$.trim(endTime.val()).length==0){
					isHasOneTime = true;
				}
			});
			if(isHasOneTime){
				alert('时段不能为空！');
				return;
			}
		}
		var setSpcLocationResult = setSpcLocation();//设置spcLocWgs84数值
		var spcTop = parseInt($("#spcTop").val());
		var spcBottom = parseInt($("#spcBottom").val());
		if(spcTop == 0){
			alert("输入有误：顶高不能等于0，请重新输入！");
			return;
		}
		if(spcTop<spcBottom){
			alert("输入有误：顶高不能小于底高，请重新输入！");
			return;
		}
		//获取形状
		if(!setSpcLocationResult){
			return;
		}
		var spcShape = $("#spcShape").val();
		if(spcShape == space_shape_jczaw){//如果是机场障碍物限制面的话，需要验证输入的半径和相关两点是否能够画出弧线
			var spcLocWgs84 = $("#spcLocWgs84").val();
			$.ajax({
				url:"${pageContext.request.contextPath}/spaceManageValidateJcSpcLocation.action",
				type:"post",
				data:{'spcLocWgs84':spcLocWgs84},
				dataType:"json",
				async:false,
				success:function(data){
					if(data.errCode == '1'){
						saveInputForm();
					}else{
						alert(data.errMsg);
					}
				}
			});
		}else{
			if(spcShape == space_shape_wg){//网格需要验证是否已经选取网格
				if(selGrids.length==0){
					alert("输入有误：请在地图中选取网格！");
					return;
				}
			}
			saveInputForm();
		}
	}
}

function saveInputForm(){
	showInMap();
	$("#inputForm").ajaxSubmit({
		url:"${ctx }/spaceQueryManageModifySave.action?userType=${userType}",
		type:"post",
		dataType:"json",
		async:false,
		success:function(data){
			alert(data.errMsg);
			if(data.errCode == '1'){
				closeAddWin();
				search();
				showInMapOverlays = null;
			}
		}
	});
}

function openAddWin(){
	closeAllWin();
	isInEdit=true;
	clearAddWin();
	$("#addSpaceDiv").show();
}

function closeAddWin(){
	isInEdit=false;
	$("#addSpaceDiv").hide();
	clearAddWin();
	
	//如果是退出网格编辑，需要重新画网格对应的空域
	if(editCacheGridOverlay.length>0){
		for(var i=0;i<editCacheGridOverlay.length;i++){
			mapIframe.addPointOverlay(editCacheGridOverlay[i]);
		}
		editCacheGridOverlay=[];
	}
}
//清除数据
function clearAddWin(){
	//清除预览的空域
	if(showInMapOverlays){
		mapIframe.clearPointOverlay(showInMapOverlays);
	}

	//移除所有的标注物
	markerMap.forEach(function (value, key, map) {
		mapIframe.clearPointOverlay(value);
	});
	markerMap=new Map();
	
	var spcShape = $("#spcShape").val();
	$("#addSpaceDiv").find('input').val('');
	$("#addSpaceDiv").find("select").val(""); 
	$("#addSpaceDiv").find("select").select2(); 
	
	changeSpcShape();
	
	//关闭编辑框时把所有空域边框宽度设置为1
	for(var i=0;i<overlays_all.length;i++){
		for(var j=0;j<overlays_all[i][1].length;j++){
			mapIframe.setPolygonStrokeWeight(overlays_all[i][1][j],1);
		}
	}
	//清除所有动态增加的文本框
	$('a.delListJwd').each(function(){
		$(this).parent().parent().remove();
		
	});
	$('a.delTime').each(function(){
		$(this).parent().parent().remove();
	});
}

//画点
var markerMap=new Map();
function drawMarker(jd,wd){
	//判断是否是网格
	if(isGrid){
		return;
	}
	var spcShape = $("#spcShape").val();
	var shapeFlag = "";
	if(spcShape == space_shape_wg){
		//网格
		shapeFlag = "0";
	}else if(spcShape == space_shape_dbx){
		//多边形
		shapeFlag = "2";
	}else if(spcShape == space_shape_yx){
		//圆形
		shapeFlag = "3";
	}else if(spcShape == space_shape_sx){
		//扇形
		shapeFlag = "6";
	}else if(spcShape == space_shape_xhcq){
		//线缓冲区
		shapeFlag = "7";
	}else if(spcShape == space_shape_jczaw){
		//机场障碍物限制面
		shapeFlag = "8";
	}
	if(spcShape!=null && spcShape!=''){
		if(selectJwdTr==null){
			var inputTr=null;
			var isTr=false;
			$('#table_shape_'+shapeFlag).find('tr').each(function(){
				if($(this).find('td:eq(0)>input').val()=='' && $(this).find('td').length>1){
					$(this).find('td:eq(0)>input').val(jd);
					$(this).find('td:eq(1)>input').val(wd);
					inputTr=$(this)[0];
					isTr=true;
					return false;
				}
			});
			if((spcShape==space_shape_dbx || spcShape==space_shape_xhcq) && !isTr){   //多边形  线缓冲区
				$('#table_shape_'+shapeFlag).append(getAddTrStr(jd,wd,shapeFlag));
				inputTr=$('#table_shape_'+shapeFlag).find('tr:last')[0];
			}
			if(!isTr && spcShape!=space_shape_dbx && spcShape!=space_shape_xhcq){
				return;
			} 
			var marker = mapIframe.drawMarker(jd,wd);
			markerMap.set(inputTr,marker);
		}else{
			$(selectJwdTr).find('input').css('border-color','#ccc');
			$(selectJwdTr).find('td:eq(0)>input').val(jd);
			$(selectJwdTr).find('td:eq(1)>input').val(wd);
			var marker = mapIframe.drawMarker(jd,wd);
			markerMap.set($(selectJwdTr)[0],marker);
			selectJwdTr=null;
		}
	}
	
}

var showInMapOverlays;

//在地图上展示效果
function showInMap(){
	//获取形状
	var spcShape = $("#spcShape").val();
	if(spcShape==space_shape_wg)
		return;
	
	if(spcShape == null || spcShape == ""){
		alert("请选择形状");
		return;
	}
	var formFlag = "";
	if(spcShape == space_shape_wg){
		//网格
		formFlag = "0";
	}else if(spcShape == space_shape_dbx){
		//多边形
		formFlag = "2";
	}else if(spcShape == space_shape_yx){
		//圆形
		formFlag = "3";
	}else if(spcShape == space_shape_sx){
		//扇形
		formFlag = "6";
	}else if(spcShape == space_shape_xhcq){
		//线缓冲区
		formFlag = "7";
	}else if(spcShape == space_shape_jczaw){
		//机场障碍物限制面
		formFlag = "8";
	}
	var myForm=document.getElementById("form_shape_"+formFlag);
	if(!Validator.Validate(myForm,3)){
		return;
	}
	var spcLocWgs84Val = getSpcLocWgs84();
	var polygonWgs84 = getPolygonWgs84(spcLocWgs84Val);
	if(polygonWgs84){
		//在地图上展示
		if(showInMapOverlays){
			mapIframe.clearPointOverlay(showInMapOverlays);
		}
		var infoSpaceTypeId = $("#spcTypeId").val() || $("#spcType").val();
		var infoSpaceTypeObj = getSpaceTypeById(infoSpaceTypeId);
		showInMapOverlays = mapIframe.drawCustomColorPolygonNoClear(polygonWgs84,infoSpaceTypeObj.colorBack,infoSpaceTypeObj.colorBorder);
	}
}

//改变所画图形的形状触发的事件
function changeSpcShape(){
	selGrids=[];
	
	var spcShape = $("#spcShape").val();
	$(".div_shape").hide();//隐藏所有输入框
	
	if(spcShape==''){
		showGrid(false);
		$("#div_preview").show();
		return;
	}
	
	if(spcShape == space_shape_wg){
		//网格
		$("#div_shape_0").show();//显示change的输入框
	}else if(spcShape == space_shape_dbx){
		//多边形
		$("#div_shape_2").show();//显示change的输入框
	}else if(spcShape == space_shape_yx){
		//圆形
		$("#div_shape_3").show();//显示change的输入框
	}else if(spcShape == space_shape_sx){
		//扇形
		$("#div_shape_6").show();//显示change的输入框
	}else if(spcShape == space_shape_xhcq){
		//线缓冲区
		$("#div_shape_7").show();//显示change的输入框
	}else if(spcShape == space_shape_jczaw){
		//机场障碍物限制面
		$("#div_shape_8").show();//显示change的输入框
	}
	
	if(spcShape!=space_shape_wg){
		showGrid(false);
		$("#div_preview").show();
	}else{
		showGrid(true);
		$("#div_preview").hide();
	}
	
	if(selectJwdTr!=null){
		$(selectJwdTr).find('input').css('border-color','#ccc');
		selectJwdTr = null;//将选点置空，否则点击地图的时候会修改上一个tr的内容
	}
	//移除标注
	clearMarker(null);
	//移除预览的图
	if(showInMapOverlays){
		mapIframe.clearPointOverlay(showInMapOverlays);
	}
	$('.div_shape').find('input').val('');
}

//查询并显示网格
function showGrid(isShow){
	if(isShow){
		var zoom=mapIframe.getMapZoom();
		//设置地图级别为13
		mapIframe.lockMapZoom(zoom, gridMapLvl);
		if(zoom<gridMapLvl){
			waitZoom();
			function waitZoom(){
				if(mapIframe.getMapZoom()<gridMapLvl){
					setTimeout(function(){
						waitZoom();
					}, 50);
				}else{
					queryGrid();
				}
			}
		}else{
			queryGrid();
		}
		
		function queryGrid(){
			newGrids=[];
			var bs = mapIframe.getMapBounds();
			var bssw = bs.getSouthWest();
			var bsne = bs.getNorthEast();
			var ajaxData = {
				minX:bssw.lng,
				minY:bssw.lat,
				maxX:bsne.lng,
				maxY:bsne.lat,
				mapZoom:gridMapLvl
			};
			$.ajax({
				url:"${pageContext.request.contextPath}/spaceManageQueryGrid.action",
				type:"post",
				data:ajaxData,
				dataType:"json",
				async:false,
				success:function(data){
					if($("#alert_div")){
						$("#alert_div").remove();
					}
					if(data.errCode=="0"){   //失败
						$(".right_content_all").before("<div id=\"alert_div\" class=\"alert alert-success \" style=\"background-color: red;color:#fff;\"><button data-dismiss=\"alert\" class=\"close\" style=\"color: #fff;\">×</button>"+data.errMsg+"</div>");
					}else{ //成功
						var ghList = data.data.ghList;
						var grid;
						var g;
						var found=false;
						var color=getGridSelColor();
						$(ghList).each(function(index,item){
							//判断网格是否在之前的网格中，如果在则不再重画
							found=false;
							for(var i=0;i<curGrids.length;i++){
								if(curGrids[i].ghCode==item.ghCode){
									newGrids.push(curGrids[i]);
									found=true;
									break;
								}
							}
							
							if(!found){
								//不在之前的网格中，画网格
								g=mapIframe.drawCustomColorPolygonNoClear(item.ghPolygon,gridDftColorBack,gridDftColorBorder,gridBackOpacity,gridBorderOpacity);
								grid={
									ghCode:item.ghCode,
									polygon:g
								};
								newGrids.push(grid);
								
								//如果该网格之前被选中，设置选中颜色
								for(var i=0;i<selGrids.length;i++){
									if(selGrids[i]==grid.ghCode){
										mapIframe.setGridColor(grid.polygon, color.colorBorder, color.colorBack, gridBackOpacity, gridBorderOpacity);
										break;
									}
								}
							}
						});
					}
				}
			});
			clearGrid();
			isGrid=true;
		}
	}else{
		clearGrid();
		mapIframe.unlockMapZoom();
		isGrid=false;
	}
}

function clearGrid(){
	//针对原网格迭代，如果在新网格中，则不删除多边形，否则删除
	var found=false;
	for(var i=0;i<curGrids.length;i++){
		found=false;
		for(var j=0;j<newGrids.length;j++){
			if(newGrids[j].ghCode==curGrids[i].ghCode){
				found=true;
				break;
			}
		}
		if(!found){
			//执行删除
			mapIframe.clearPointOverlay(curGrids[i].polygon);
		}
	}

	curGrids=newGrids;
	newGrids=[];
}

function selectGrid(jd,wd){
	for(var i=0;i<curGrids.length;i++){
		if(mapIframe.gridContainsPoint(curGrids[i].polygon, jd, wd)){
			//找到点击的网格
			var oldSel=false;
			for(var j=0;j<selGrids.length;j++){
				if(selGrids[j]==curGrids[i].ghCode){
					//原来选中
					oldSel=true;
					selGrids.splice(j,1);
					break;
				}
			}
			if(oldSel){
				//原来选中，现在取消选中
				mapIframe.setGridColor(curGrids[i].polygon, gridDftColorBorder, gridDftColorBack, gridBackOpacity, gridBorderOpacity);
			}else{
				//原来未选中，现在选中
				var color=getGridSelColor();
				mapIframe.setGridColor(curGrids[i].polygon, color.colorBorder, color.colorBack, gridBackOpacity, gridBorderOpacity);
				selGrids.push(curGrids[i].ghCode);
			}
			break;
		}
	}
}

//改变空域类型触发的事件
function changeSpcType(){
	if(!isGrid)
		return;
	
	var color=getGridSelColor();
	for(var i=0;i<curGrids.length;i++){
		for(var j=0;j<selGrids.length;j++){
			if(curGrids[i].ghCode==selGrids[j]){
				//被选中的网格，需要改变颜色
				mapIframe.setGridColor(curGrids[i].polygon, color.colorBorder, color.colorBack, gridBackOpacity, gridBorderOpacity);
				break;
			}
		}
	}
}

function getGridSelColor(){
	//获取颜色
	var colorBorder;
	var colorBack;
	var infoSpaceTypeId = $("#spcTypeId").val() || $("#spcType").val();
	if(infoSpaceTypeId && infoSpaceTypeId!=''){
		var infoSpaceTypeObj = getSpaceTypeById(infoSpaceTypeId);
		colorBorder=infoSpaceTypeObj.colorBorder;
		colorBack=infoSpaceTypeObj.colorBack;
	}else{
		colorBorder=gridSelColorBorder;
		colorBack=gridSelColorBack;
	}
	
	return {colorBorder:colorBorder,colorBack:colorBack};
}

function setJwdToTr(jd,wd){
	if(isGrid){
		selectGrid(jd,wd);
	}else if(selectJwdTr){
		clearMarker($(selectJwdTr)[0]);
		selectJwdTr.find("input").eq(0).val(jd);
		selectJwdTr.find("input").eq(1).val(wd);
		return false;//返回table
	}else{
		return false;
	}
}
</script>
</head>
<body>
	<!-- 图例 -->
	<div id="legentDiv" style="position: absolute;left: 10px;top: 50px;z-index: 3;background-color: #ffffff;border-radius: 8px;">
		<table style="max-width: 129px;font-size: 12px;text-align: left;margin: 6px;">
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
		<div class="right_content_all">
			<a href="javascript:void(0);" onclick="zoomOutMap_list(this)" style="text-decoration: none;">
				<div class="right_content_all_top">
					<span>检索条件</span>
	                <span class="btn btn-xs" style="float:right;line-height: 1.5;border:none; border-left:1px solid #dee5e7;">
	                  <i class="icon-angle-down"></i>
	                </span>
				</div>
			</a>
			<div class="right_content_select searchDiv">
				<form method="post" id="searchForm">
					<input type="hidden" name="pageSize" id="pageSize" value="10">
				    <div class="span4 right_content_select_box">
						<span class="right_content_select_name">空域名称：</span> 
						<input class="right_content_select_ctt right_content_select_cttt" placeholder="请输入" type="text"  name="spcName" value="${infoSpace.spcName }" />
					</div>
					<div class="span4 right_content_select_box">
						<span class="right_content_select_name">空域形状：</span>
						<select class="right_content_select_ctt"  name="spcShape" >
							<option value="">全部</option>
							<c:forEach var="item" items="${fns:findMap('space_shape_map') }">
								<option value="${item.key }" ${infoSpace.spcShape==item.key ? "selected" : "" }>${item.value }</option>
							</c:forEach>
						</select>
					</div>
					
					<div class="span4 right_content_select_box">
						<span class="right_content_select_name">空域类型：</span> 
						<select class="right_content_select_ctt"  name="spcType">
							<option value="">全部</option>
							<c:forEach var="item" items="${infoSpaceTypeList }">
								<option value="${item.spcType }" ${item.spcType == infoSpace.spcType ? "selected" : "" }>${item.typeName }</option>
							</c:forEach>
						</select>
					</div>
					<div class="span4 right_content_select_box">
						<span class="right_content_select_name">空域分类：</span> 
						<select class="right_content_select_ctt"  name="spcKind">
							<option value="">全部</option>
							<c:forEach var="item" items="${fns:findMap('spc_kind_map') }">
								<option value="${item.key }" ${infoSpace.spcKind==item.key ? "selected" : "" }>${item.value }</option>
							</c:forEach>
						</select>
					</div>
					<c:if test="${userType=='1' }">
						<div class="span4 right_content_select_box">
							<span class="right_content_select_name">发布状态：</span> 
							<select class="right_content_select_ctt"  name="spcSts">
								<option value="">全部</option>
								<c:forEach var="item" items="${fns:findMap('spc_sts_map') }">
									<option value="${item.key }" ${infoSpace.spcSts==item.key ? "selected" : "" }>${item.value }</option>
								</c:forEach>
							</select>
						</div>
					</c:if>
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
		<iframe src="${ctx }/leaflet/leaflet_map.jsp" name="mapIframe" id="mapIframe" width="100%" frameborder="0" ></iframe>
		<div class="right_content_all">
			<%--列表展示--%>
			<div class="right_content_table" id="divTable" style="z-index: 99;">
				<div style="height:30px;display: inline-block;width:100%;">
					<c:if test="${fns:hasPms(pmsIds,'1101') || fns:hasPms(pmsIds,'10601')}">
						<div onclick="openAddWin()" class="right_content_btnbox_btn right_content_btnbox_add" style="cursor:pointer;padding-top:0px;margin-top: 0.5px;margin-left:0px;line-height: 25px;">
							<img src="${pageContext.request.contextPath }/images/add_btn.png" />
							<span>添加</span>
						</div>
					</c:if>
					<c:if test="${userType=='1' && fns:hasPms(pmsIds,'1102')}">
						<div onclick="publishSpaceAll()" class="right_content_btnbox_btn right_content_btnbox_save" style="cursor:pointer;padding-top:0px;margin-top: 0.5px;margin-left:0px;line-height: 25px;">
							<img src="${pageContext.request.contextPath }/images/save_btn.png" />
							<span>发布</span>
						</div>
						<div onclick="repealSpaceAll()" class="right_content_btnbox_btn right_content_btnbox_save" style="cursor:pointer;padding-top:0px;margin-top: 0.5px;margin-left:0px;line-height: 25px;background-color: #ff877a;">
							<img src="${pageContext.request.contextPath }/images/save_btn.png" />
							<span>撤销</span>
						</div>
					</c:if>
					<div style="float: right;height:30px;width:80%;">
						<span class="pagination_sub" style="padding: 0px;padding-left: 5px;background-color:#fff;margin-top:1px;line-height:28px;border:1px solid #e5e5e5;border-radius: 4px;">
						</span>
						<div class="pagination" style="display: inline-block;margin-top: -7px;float: right;" >
						</div>
					</div>
				</div>
				<div >
					<table class="table table-bordered table_list table-hover table-striped" id="listTable">
						<thead>
							<tr class="active blue_active_op">
								<c:if test="${fns:hasPms(pmsIds,'1102') && !(userType=='0')}">
									<th width="3%"><input type="checkbox" onchange="checkAll(this)" id="allListSpcIdCheckbox"/></th>
								</c:if>
								<th >空域名称</th>
								<th width="10%">空域类型</th>
								<th width="8%">空域分类</th>
								<c:if test="${userType=='1' }">
									<th width="5%">发布状态</th>
								</c:if>
								<th width="13%">创建时间</th>
								<c:if test="${userType=='0' }">
									<th width="6%">审批状态</th>
									<th width="13%">审批时间</th>
									<th width="20%">审批意见</th>
								</c:if>
								<th width="10%">操作</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- 空域编辑 start -->
		<div class="right_content_all" id="addSpaceDiv" style="display: none;position: absolute;right: 0;top: 0; background-color: #fdfdfd;width:550px;">
			<div class="right_content_select" id="inputDiv" style="padding: 0">
				<form id="inputForm" class="form-horizontal" action="${ctx }/spaceQueryManageModifySave.action?userType=${userType}">
					<input type="hidden" name="spcId" id="spcId" >
					<input type="hidden" value="${infoSpace.polygonWgs84}" id="polygonWgs84">
				 	<input class="required" type="hidden" id="spcLocWgs84" name="spcLocWgs84" value="${infoSpace.spcLocWgs84 }">
					<div class="control-group">
						<label class="control-label">名称：</label>
						<div class="controls">
							<c:if test="${opt!='view' }">
								<input id="spcName" name="spcName" type="text" dataType="Require,Limit,CEN_" len="100" msg="请输入(1~100)个只包含中文、英文、下划线字符的名称！" maxlength="100" class="required" value="${infoSpace.spcName }"/>
							</c:if>
							<c:if test="${opt=='view' }">
								<label class="control-label" style="text-align: left;">${infoSpace.spcName }</label>
							</c:if>
						</div>
					</div>
					<div id="time" style="max-height: 100px;overflow: auto;">
						<div class="control-group">
							<label class="control-label">时段：</label>
							<div class="controls">
								<input type="text" id="timestart_1" name="startTime" style="width:150px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'timeend_1\');}'})"/>&nbsp;至
								<input type="text" id="timeend_1" name="endTime" style="width:150px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'timestart_1\');}'})"/>
								 
								<a href="javascript:void('0');" id="addTime" title="添加" onclick="addTime(this)" style="float: right;padding-right: 5px;text-align: center;line-height: 30px;"><i class="icon-plus"></i></a>
							</div>
						</div>
					</div>
					<table style="width: 100%;">
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">底高（米）：</label>
									<div class="controls">
										<c:if test="${opt!='view' }">
											<input type="text" dataType="Require,Int" msg="请输入底高！" class="required shortInput" name="spcBottom" id="spcBottom" value="${infoSpace.spcBottom }"/>
										</c:if>
										<c:if test="${opt=='view' }">
											<label class="control-label" style="text-align: left;">${infoSpace.spcBottom }</label>
										</c:if>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">顶高（米）：</label>
									<div class="controls">
										<c:if test="${opt!='view' }">
											<input type="text" dataType="Require,Int" msg="请输入顶高！" class="required shortInput" name="spcTop" id="spcTop" value="${infoSpace.spcTop }"/>
										</c:if>
										<c:if test="${opt=='view' }">
											<label class="control-label" style="text-align: left;">${infoSpace.spcTop }</label>
										</c:if>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">形状：</label>
									<div class="controls">
										<c:if test="${opt!='view' }">
											<select style="width: 140px" dataType="Require" msg="请选择形状！" class="required" name="spcShape" id="spcShape" onchange="changeSpcShape()">
												<option value="">全部</option>
												<c:forEach var="item" items="${fns:findMap('space_shape_map') }">
													<option value="${item.key }" ${item.key==infoSpace.spcShape ? "selected" : ""} >${item.value }</option>
												</c:forEach>
											</select>
										</c:if>
										<c:if test="${opt=='view' }">
											<label class="control-label" style="text-align: left;">${fns:findValue('space_shape_map',infoSpace.spcShape ) }</label>
										</c:if>
									</div>
								</div>
							</td>
							<td>
								<div class="control-group">
									<label class="control-label">类型：</label>
									<div class="controls">
										<c:if test="${opt!='view' }">
											<select style="width: 140px" dataType="Require" msg="请选择类型！" class="required" name="spcType" id="spcType" onchange="changeSpcType()">
												<option value="">全部</option>
												<c:forEach var="item" items="${infoSpaceTypeList }">
													<option value="${item.spcType }" ${item.spcType==infoSpace.spcType ? "selected" : ""} >${item.typeName }</option>
												</c:forEach>
											</select>
										</c:if>
										<c:if test="${opt=='view' }">
											<c:forEach var="item" items="${infoSpaceTypeList }">
												<c:if test="${item.spcType==infoSpace.spcType }">
													<label class="control-label" style="text-align: left;">${item.typeName }</label>
												</c:if>
											</c:forEach>
										</c:if>
									</div>
								</div>	
							</td>
						</tr>
						<tr>
							<td>
								<div class="control-group">
									<label class="control-label">分类：</label>
									<div class="controls">
										<c:if test="${opt!='view' }">
											<select style="width: 140px" class="required" name="spcKind" id="spcKind">
												<option value="">全部</option>
												<c:forEach var="item" items="${fns:findMap('spc_kind_map') }">
													<option value="${item.key }" ${item.key==infoSpace.spcKind ? "selected" : ""} >${item.value }</option>
												</c:forEach>
											</select>
										</c:if>
										<c:if test="${opt=='view' }">
											<label class="control-label" style="text-align: left;">${fns:findValue('spc_kind_map',infoSpace.spcKind ) }</label>
										</c:if>
									</div>
								</div>
							</td>
							<td></td>
						</tr>
					</table>
				</form>
				<c:if test="${opt!='view' }">
					<form id="form_shape_0" class="form-horizontal">
						<div id="div_shape_0" style="display: none;" class="div_shape">
							<div class="right_content_all">
								<div class="right_content_all_top">
									<span>设置 <b style="color: red">网格</b><span style="color: red;font-size: 60%;">&nbsp;&nbsp;可在地图上选择网格</span></span>
								</div>
							</div>
						</div>
					</form>
					<form id="form_shape_2" class="form-horizontal">
						<div id="div_shape_2" style="display: none;" class="div_shape">
							<div class="right_content_all">
								<div class="right_content_all_top">
									<span>设置 <b style="color: red">多边形</b><span style="color: red;font-size: 60%;">&nbsp;&nbsp;可输入坐标或选取坐标点</span></span>
									<span style="float: right; margin-right: 20px">
										<a class="addJwd">添加经纬度</a>
									</span>
								</div>
							</div>
							<table class="table table-bordered table_list" id="table_shape_2">
								<tr>
									<td style="width: 45%">
										经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="jd_2" />
									</td>
									<td style="width: 45%">
										纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="wd_2" />
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="jd_2" />
									</td>
									<td style="width: 45%">
										纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="wd_2" />
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="jd_2" />
									</td>
									<td style="width: 45%">
										纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="wd_2" />
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
							</table>
						</div>
					</form>
					<form id="form_shape_3" class="form-horizontal">
						<div id="div_shape_3" style="display: none;" class="div_shape">
							<div class="right_content_all">
								<div class="right_content_all_top">
									<span>设置 <b style="color: red">圆形</b><span style="color: red;font-size: 60%;">&nbsp;&nbsp;可输入坐标或选取坐标点</span></span>
								</div>
							</div>
							<table class="table table-bordered table_list">
								<tr>
									<td>
										半径：<input type="text" dataType="Require,Int" msg="请输入半径！" class="required shortInput" name="radius_3" id="radius_3"/>
									</td>
								</tr>
							</table>
							<table class="table table-bordered table_list" id="table_shape_3">
								<tr>
									<td style="width: 45%">
										经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="jd_3" />
									</td>
									<td style="width: 45%">
										纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="wd_3" />
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
							</table>
						</div>
					</form>
					<form id="form_shape_6" class="form-horizontal">
						<div id="div_shape_6" style="display: none;" class="div_shape">
							<div class="right_content_all">
								<div class="right_content_all_top">
									<span>设置 <b style="color: red">扇形</b><span style="color: red;font-size: 60%;">&nbsp;&nbsp;可输入坐标或选取坐标点</span></span>
								</div>
							</div>
							<table class="table table-bordered table_list">
								<tr>
									<td>
										扇形半径：<input type="text" dataType="Require,Int" msg="请输入半径！" class="required shortInput" name="radius_6" id="radius_6"/>
									</td>
								</tr>
							</table>
							<table class="table table-bordered table_list">
								<tr>
									<td style="width: 45%">
										初始角度：<input type="text" dataType="Require,Angle,Range" min="0" max="360" msg="请输入角度！" class="required shortInput" name="nStartA_6" id="nStartA_6"/>
									</td>
									<td>
										终止角度：<input type="text" dataType="Require,Angle,Range" min="0" max="360" msg="请输入角度！" class="required shortInput" name="nEndA_6" id="nEndA_6"/>
									</td>
								</tr>
							</table>
							<table class="table table-bordered table_list" id="table_shape_6">
								<tr>
									<td style="width: 45%">
										圆心经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="jd_6" />
									</td>
									<td style="width: 45%">
										圆心纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="wd_6" />
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
							</table>
						</div>
					</form>
					<form id="form_shape_7" class="form-horizontal">
						<div id="div_shape_7" style="display: none;" class="div_shape">
							<div class="right_content_all">
								<div class="right_content_all_top">
									<span>设置 <b style="color: red">线缓冲区</b><span style="color: red;font-size: 60%;">&nbsp;&nbsp;可输入坐标或选取坐标点</span></span>
									<span style="float: right; margin-right: 20px">
										<a class="addJwd">添加经纬度</a>
									</span>
								</div>
							</div>
							<table class="table table-bordered table_list" id="table_shape_7">
								<tr>
									<td colspan="3">
										宽度：<input type="text" dataType="Require,Int,Range" min="1" msg="请输入宽度！" class="required shortInput" name="width_7" id="width_7"/>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="jd_7" />
									</td>
									<td style="width: 45%">
										纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="wd_7" />
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="jd_7" />
									</td>
									<td style="width: 45%">
										纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="wd_7" />
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
							</table>
						</div>
					</form>
					<form id="form_shape_8" class="form-horizontal">
						<div id="div_shape_8" style="display: none;" class="div_shape">
							<div class="right_content_all">
								<div class="right_content_all_top">
									<span>设置 <b style="color: red">机场障碍物限制面</b><span style="color: red;font-size: 60%;">&nbsp;&nbsp;可输入坐标或选取坐标点</span></span>
								</div>
							</div>
							<table class="table table-bordered table_list" id="table_shape_8">
								<tr>
									<td style="width: 45%">
										A1经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="a1_jd" id="a1_jd"/>
									</td>
									<td style="width: 45%">
										A1纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="a1_wd" id="a1_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										A2经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="a2_jd" id="a2_jd"/>
									</td>
									<td style="width: 45%">
										A2纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="a2_wd" id="a2_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										C2经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="c2_jd" id="c2_jd"/>
									</td>
									<td style="width: 45%">
										C2纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="c2_wd" id="c2_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										C2B2半径：<input type="text" dataType="Require,Int" msg="请输入半径！" class="required shortInput" name="rC2B2" id="rC2B2"/>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										B2经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="b2_jd" id="b2_jd"/>
									</td>
									<td style="width: 45%">
										B2纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="b2_wd" id="b2_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										B3经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="b3_jd" id="b3_jd"/>
									</td>
									<td style="width: 45%">
										B3纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="b3_wd" id="b3_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										B3C3半径：<input type="text" dataType="Require,Int" msg="请输入半径！" class="required shortInput" name="rB3C3" id="rB3C3"/>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										C3经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="c3_jd" id="c3_jd"/>
									</td>
									<td style="width: 45%">
										C3纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="c3_wd" id="c3_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										A3经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="a3_jd" id="a3_jd"/>
									</td>
									<td style="width: 45%">
										A3纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="a3_wd" id="a3_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										A4经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="a4_jd" id="a4_jd"/>
									</td>
									<td style="width: 45%">
										A4纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="a4_wd" id="a4_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										C4经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="c4_jd" id="c4_jd"/>
									</td>
									<td style="width: 45%">
										C4纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="c4_wd" id="c4_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										C4B4半径：<input type="text" dataType="Require,Int" msg="请输入半径！" class="required shortInput" name="rC4B4" id="rC4B4"/>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										B4经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="b4_jd" id="b4_jd"/>
									</td>
									<td style="width: 45%">
										B4纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="b4_wd" id="b4_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										B1经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="b1_jd" id="b1_jd"/>
									</td>
									<td style="width: 45%">
										B1纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="b1_wd" id="b1_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										B1C1半径：<input type="text" dataType="Require,Int" msg="请输入半径！" class="required shortInput" name="rB1C1" id="rB1C1"/>
									</td>
								</tr>
								<tr>
									<td style="width: 45%">
										C1经度：<input type="text" dataType="Require,Currency" msg="请输入经度！" class="required shortInput" name="c1_jd" id="c1_jd"/>
									</td>
									<td style="width: 45%">
										C1纬度：<input type="text" dataType="Require,Currency" msg="请输入纬度！" class="required shortInput" name="c1_wd" id="c1_wd"/>
									</td>
									<td style="width: 10%">
										<a class="selectJwd" style="cursor: pointer;" title="编辑"><i class="icon-pencil"></i></a>
									</td>
								</tr>
							</table>
						</div>
					</form>
				</c:if>
			</div>
			<div class="right_content_btnbox ">
				<c:if test="${opt!='view' }">
					<div onclick="javascript:goSave();" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_save">
						<img src="${ctx }/images/save_btn.png" /> 
						<span>保存</span>
					</div>
					<div onclick="closeAddWin()" style="cursor: pointer;" class="right_content_btnbox_btn right_content_btnbox_return">
						<img src="${pageContext.request.contextPath }/images/return_btn.png" />
						<span>取消</span>
					</div>
					<div id="div_preview" onclick="showInMap()" style="cursor:pointer;background-color: #0088CC " class="right_content_btnbox_btn">
						<span>预览</span>
					</div>
				</c:if>
			</div>
		</div>
	    <!-- 空域编辑  end -->
	    <!-- 审批 -->
		<div class="right_content_all" id="auditSpaceDiv" style="display: none;position: absolute;right: 0;top: 0; background-color: #fdfdfd;width:550px;">
			<template>
				<div class="right_content_select" style="padding: 0">
					<form id="form_rev" class="form-horizontal">
						<input type="hidden" value="" id="auditSpcId">
						<div class="right_content_all">
							<div class="right_content_all_top">
								<span>空域详情</span>
							</div>
						</div>
						<table class="table table-bordered table_list">
							<tbody style="max-height: 200px;">
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">空域名称：</label>
											<div class="controls">
												{{auditSpace.spcName}}
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">时间范围：</label>
											<div class="controls">
												<span v-for="(time,index) in auditSpace.infoSpaceTimeList"><br v-if="index!=0">{{time.begDatetime}} - {{time.endDatetime}}</span>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">高度范围：</label>
											<div class="controls">
												{{auditSpace.spcBottom}} - {{auditSpace.spcTop}} 米
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">空域类型：</label>
											<div class="controls">
												{{auditSpace.spcType | spcTypeFilter}}
											</div>
										</div>
									</td>
								</tr>
								<tr v-if="auditSpace.fileList!=null && auditSpace.fileList!='' && auditSpace.fileList.length>0">
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">文件资料：</label>
											<div class="controls">
												<a v-bind:style="index!=0?'margin-left:10px':''" v-for="(sysFile,index) in auditSpace.fileList" v-bind:href="ctx+'/downloadFile.action?fileId='+sysFile.fileId">{{sysFile.fileName}}</a>
												<!-- <a v-bind:style="index!=0?'margin-left:10px':''" v-for="(sysFile,index) in auditSpace.fileList" v-bind:href="ctx+'/downLoadFile.action?fileId='+sysFile.fileId">{{sysFile.fileName}}</a>
 -->											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
						<div id="div_rev">
							<div class="right_content_all">
								<div class="right_content_all_top">
									<span>审批信息</span>
								</div>
							</div>
							<table class="table table-bordered table_list" >
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">审批状态：</label>
											<div class="controls">
												<select style="width: 140px" dataType="Require" msg="请选择！" class="required" name="spcRevSts" id="spcRevSts" >
													<c:forEach var="item" items="${fns:findMap('app_rst_2_map') }">
														<option value="${item.key }">${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">审批意见：</label>
											<div class="controls">
												<textarea style="max-height: 100px;max-width: 400px;" class="form-control" rows="3" name="revMsg" id="revMsg"></textarea>
											</div>
										</div>
									</td>
								</tr>
							</table>
						</div>
					</form>
				</div>
			</template>
			<div class="right_content_btnbox ">
				<div onclick="javascript:goSaveAudit();" style="cursor:pointer;" class="right_content_btnbox_btn right_content_btnbox_save">
					<img src="${ctx }/images/save_btn.png" /> 
					<span>保存</span>
				</div>
				<div onclick="closeAuditWin()" style="cursor: pointer;" class="right_content_btnbox_btn right_content_btnbox_return">
					<img src="${pageContext.request.contextPath }/images/return_btn.png" />
					<span>取消</span>
				</div>
			</div>
		</div>
		 <!-- 详情 -->
		<div class="right_content_all" id="viewSpaceDiv" style="display: none;position: absolute;right: 0;top: 0; background-color: #fdfdfd;width:550px;">
			<template>
				<div class="right_content_select" style="padding: 0">
					<form class="form-horizontal">
						<div class="right_content_all">
							<div class="right_content_all_top">
								<span>空域详情</span>
							</div>
						</div>
						<table class="table table-bordered table_list">
							<tbody style="max-height: 200px;">
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">空域名称：</label>
											<div class="controls">
												{{viewSpace.spcName}}
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">时间范围：</label>
											<div class="controls">
												<span v-for="(time,index) in viewSpace.infoSpaceTimeList"><br v-if="index!=0">{{time.begDatetime}} - {{time.endDatetime}}</span>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">高度范围：</label>
											<div class="controls">
												{{viewSpace.spcBottom}} - {{viewSpace.spcTop}} 米
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">空域类型：</label>
											<div class="controls">
												{{viewSpace.spcType | spcTypeFilter}}
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">空域分类：</label>
											<div class="controls">
												{{viewSpace.spcKind | spcKindFilter}}
											</div>
										</div>
									</td>
								</tr>
								<tr v-if="viewSpace.fileList!=null && viewSpace.fileList!='' && viewSpace.fileList.length>0">
									<td>
										<div class="control-group" style="border-bottom: unset;">
											<label class="control-label">文件资料：</label>
											<div class="controls">
												<a v-bind:style="index!=0?'margin-left:10px':''" v-for="(sysFile,index) in viewSpace.fileList" v-bind:href="ctx+'/downloadFile.action?fileId='+sysFile.fileId">{{sysFile.fileName}}</a>
 											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
						<c:if test="${userType=='0' }">
							<div id="div_rev">
								<div class="right_content_all">
									<div class="right_content_all_top">
										<span>审批信息</span>
									</div>
								</div>
								<table class="table table-bordered table_list" >
									<tr>
										<td>
											<div class="control-group" style="border-bottom: unset;">
												<label class="control-label">审批状态：</label>
												<div class="controls">
													{{viewSpace.spcRevSts | spcRevStsFilter}}
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div class="control-group" style="border-bottom: unset;">
												<label class="control-label">审批时间：</label>
												<div class="controls">
													{{viewSpace.revTime }}
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div class="control-group" style="border-bottom: unset;">
												<label class="control-label">审批意见：</label>
												<div class="controls">
													{{viewSpace.revMsg }}
												</div>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</c:if>
						
					</form>
				</div>
			</template>
			<div class="right_content_btnbox ">
				<div onclick="closeViewWin()" style="cursor: pointer;" class="right_content_btnbox_btn right_content_btnbox_return">
					<img src="${pageContext.request.contextPath }/images/return_btn.png" />
					<span>取消</span>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
function checkAll(elem){
	if(elem.checked){
		$("input[name='listSpcId']").prop("checked",true);
	}else{
		$("input[name='listSpcId']").prop("checked",false);
	}
}

//批量发布
function publishSpaceAll(){
	if($("input[name='listSpcId']:checked").length<1){
		alert('请选择要发布的空域信息！');
		return;
	}
	var spcIds=new Array();
	$("input[name='listSpcId']:checked").each(function(){
		spcIds.push($(this).val());
	});

	Confirm("你确定要发布这些空域吗？",function(){
		$.ajax({
			url:"${pageContext.request.contextPath}/publishSpaceAll.action",
			type:"post",
			data:{'spcIds':spcIds.join(',')},
			dataType:"json",
			async:false,
			success:function(data){
				alert(data.errMsg);
				if(data.errCode=="1"){   //成功
					search();
					$("#allListSpcIdCheckbox").prop("checked",false);
				}
			}
		});
	},[spcIds]);
}
//批量撤销
function repealSpaceAll(){
	if($("input[name='listSpcId']:checked").length<1){
		alert('请选择要撤销的空域信息！');
		return;
	}
	var spcIds=new Array();
	$("input[name='listSpcId']:checked").each(function(){
		spcIds.push($(this).val());
	});

	Confirm("你确定要撤销这些空域吗？",function(){
		$.ajax({
			url:"${pageContext.request.contextPath}/repealSpaceAll.action",
			type:"post",
			data:{'spcIds':spcIds.join(',')},
			dataType:"json",
			async:false,
			success:function(data){
				alert(data.errMsg);
				if(data.errCode=="1"){   //成功
					search();
					$("#allListSpcIdCheckbox").prop("checked",false);
				}
			}
		});
	},[spcIds]);
}
//关闭所有的打开窗口
function closeAllWin(){
	closeAddWin();
	closeAuditWin();
	closeViewWin();

	Validator.Validate_onLoad($("#inputForm")[0],3);
}
var auditVm = new Vue({
	el:"#auditSpaceDiv",
	data:{
		auditSpace:{},
		ctx:'${ctx}'
	},
	filters: {
		spcTypeFilter: function(value){
			if(value == null || value == ''){
				return "";
			}
			for(var i=0;i<infoSpaceTypeList.length;i++){
				var infoSpaceTypeObj = infoSpaceTypeList[i];
				if(infoSpaceTypeObj.spcType == value){
					return infoSpaceTypeObj.typeName;
				}
			}
			return "";
		}
	}
});
var spcRevStsList = [];
<c:forEach var="item" items="${fns:findMap('app_rst_2_map') }">
	var tempSpcRevSts = {
		key: '${item.key }',
		value: '${item.value }'
	};
	spcRevStsList.push(tempSpcRevSts);
</c:forEach>
var spcKindList = [];
<c:forEach var="item" items="${fns:findMap('spc_kind_map') }">
var tempSpcKind = {
	key: '${item.key }',
	value: '${item.value }'
};
spcKindList.push(tempSpcKind);
</c:forEach>
var viewVm = new Vue({
	el:"#viewSpaceDiv",
	data:{
		viewSpace:{},
		ctx:'${ctx}'
	},
	filters: {
		spcTypeFilter: function(value){
			if(value == null || value == ''){
				return "";
			}
			for(var i=0;i<infoSpaceTypeList.length;i++){
				var infoSpaceTypeObj = infoSpaceTypeList[i];
				if(infoSpaceTypeObj.spcType == value){
					return infoSpaceTypeObj.typeName;
				}
			}
			return "";
		},
		spcRevStsFilter: function(value){
			if(value == null || value == ''){
				return "";
			}
			for(var i=0;i<spcRevStsList.length;i++){
				if(spcRevStsList[i].key == value){
					return spcRevStsList[i].value;
				}
			}
			return "";
		},
		spcKindFilter: function(value){
			if(value == null || value == ''){
				return "";
			}
			for(var i=0;i<spcKindList.length;i++){
				if(spcKindList[i].key == value){
					return spcKindList[i].value;
				}
			}
			return "";
		}
	}
});
//审批空域信息
function openAuditWin(spcId){
	closeAllWin();
	$("#auditSpcId").val(spcId);
	//获取空域详情
	$.ajax({
		url: "${ctx}/getAuditInfoSpaceDetail.action",
		data: {
			spcId: spcId
		},
		dataType: 'json',
		async: false,
		success: function(data){
			if(data.errCode!='1'){
				alert(data.errMsg);
				return;
			}
			auditVm.auditSpace = data.data;
			$("#auditSpaceDiv").show();
		}
	});
}
//空域详情
function openViewWin(spcId){
	closeAllWin();
	//获取空域详情
	$.ajax({
		url: "${ctx}/getAuditInfoSpaceDetail.action",
		data: {
			spcId: spcId
		},
		dataType: 'json',
		async: false,
		success: function(data){
			if(data.errCode!='1'){
				alert(data.errMsg);
				return;
			}
			viewVm.viewSpace = data.data;
			$("#viewSpaceDiv").show();
		}
	});
}
//关闭审批空域信息框
function closeAuditWin(){
	$("#spcRevSts").val("${fns:findKey('app_rst_yes')}");
	$("#spcRevSts").select2(); 
	$("#revMsg").val("");
	$("#auditSpcId").val("");
	auditVm.auditSpace = {};
	$("#auditSpaceDiv").hide();
}
//关闭空域信息框
function closeViewWin(){
	viewVm.viewSpace = {};
	$("#viewSpaceDiv").hide();
}
//保存审批空域信息
function goSaveAudit(){
	var myForm=document.getElementById("form_rev");
	if(Validator.Validate(myForm, 3)){
		$.ajax({
			url:"${pageContext.request.contextPath}/revInfoSpace.action",
			type:"post",
			data:{
				'spcRevSts':$("#spcRevSts").val(),
				'revMsg':$("#revMsg").val(),
				'spcId':$("#auditSpcId").val()
			},
			dataType:"json",
			async:false,
			success:function(data){
				alert(data.errMsg);
				if(data.errCode == '1'){
					closeAuditWin();
					search();
				}
			}
		});
	}	
}
//删除空域信息
function deleteInfoSpace(spcId){
	if(spcId){
		Confirm("你确定要删除该条信息吗？",function(){
			$.ajax({
				url:"${pageContext.request.contextPath}/spaceManageDeleteInfoSpace.action",
				type:"post",
				data:{'spcId':spcId},
				dataType:"json",
				async:false,
				success:function(data){
					alert(data.errMsg);
					closeAddWin();
					if(data.errCode=="1"){   //成功
						search();
					}
				}
			});
		},[spcId]);
	}else{
		alert("请选择一条要删除的信息！");
	}
}
//选中高亮显示空域
function showOverlay1(spcId,spcShape){
	var overlayArry=[];
	for(var i=0;i<overlays_all.length;i++){
		overlayArry=overlays_all[i][1];
		if(overlays_all[i][0]==spcId){
			if(overlayArry){
				if(spcShape!=space_shape_wg){
					for(var j=0;j<overlayArry.length;j++){
						mapIframe.setPolygonStrokeWeight(overlayArry[j],2);
					}
					mapIframe.positionPointOverlayArray(overlayArry);
					break;
				}else{
					mapIframe.positionPointOverlayArray(overlayArry);
					for(var j=0;j<overlayArry.length;j++){
						mapIframe.clearPointOverlay(overlayArry[j]);
					}
					editCacheGridOverlay = editCacheGridOverlay.concat(overlayArry);
				}
			}
		}else{
			for(var j=0;j<overlayArry.length;j++){
				mapIframe.setPolygonStrokeWeight(overlayArry[j],1);
			}
		}
	}
}

function showOverlay(spcId,spcShape){
	if(isInEdit)
		return;
	var overlayArry=[];
	for(var i=0;i<overlays_all.length;i++){
		overlayArry=overlays_all[i][1];
		if(overlays_all[i][0]==spcId){
			if(overlayArry){
				if(spcShape!=space_shape_wg){
					for(var j=0;j<overlayArry.length;j++){
						mapIframe.setPolygonStrokeWeight(overlayArry[j],2);
					}
					mapIframe.positionPointOverlayArray(overlayArry);
					break;
				}else{
					for(var j=0;j<overlayArry.length;j++){
						mapIframe.setPolygonStrokeWeight(overlayArry[j],2);
					}
					mapIframe.positionPointOverlayArray(overlayArry);
				}
			}
		}else{
			for(var j=0;j<overlayArry.length;j++){
				mapIframe.setPolygonStrokeWeight(overlayArry[j],1);
			}
		}
	}
}

//编辑空域信息
function openEditWin(spcId){
	closeAllWin();
	var obj = null;
	isInEdit=true;
	editCacheGridOverlay=[];
	for(var i=0;i<infoSpaceList.length;i++){
		if(infoSpaceList[i].spcId == spcId){
			obj = infoSpaceList[i];
			break;
		}
	}
	if(obj == null){
		console.info("程序出现异常，未找到spcId="+spcId+"的空域");
		return;
	}
	//清除input数据
	$('#addSpaceDiv').find('input').val('');
	//清除所有动态增加的文本框
	$('a.delListJwd').each(function(){
		$(this).parent().parent().remove();
	});
	$('a.delTime').each(function(){
		$(this).parent().parent().remove();
	});
	var timeList=obj.infoSpaceTimeList;
	for(var i = 0;i<timeList.length;i++) {
		if($('#time>div:eq('+i+')').length>0){
			$('#time>div:eq('+i+') input:eq(0)').val(timeList[i].begDate+' '+timeList[i].begTime);
			$('#time>div:eq('+i+') input:eq(1)').val(timeList[i].endDate+' '+timeList[i].endTime);
		}else{ 
			addTimeVal(timeList[i].begDate+' '+timeList[i].begTime,timeList[i].endDate+' '+timeList[i].endTime);
		} 
	}
	//先移动到空域所在位置
	showOverlay1(spcId,obj.spcShape);
	
	$('#spcId').val(obj.spcId);
	$("#polygonWgs84").val(obj.polygonWgs84);
	$("#spcLocWgs84").val(obj.spcLocWgs84);
	$('#spcName').val(obj.spcName);
	$('#spcBottom').val(obj.spcBottom);
	$('#spcTop').val(obj.spcTop);
	$('#spcShape').val(obj.spcShape);
	$('#spcShape').select2();
	changeSpcShape();
	$('#spcType').val(obj.spcType);
	$('#spcType').select2();
	$('#spcKind').val(obj.spcKind);
	$('#spcKind').select2();
	
	var updateSpcShape = $("#spcShape").val();
	var updateSpcLocation = $("#spcLocWgs84").val();
	//解析数据：spcLocWgs84
	if(updateSpcShape == space_shape_dbx){ //2：多边形j,w|j,w|j,w
		var updateSpcLocations = updateSpcLocation.split("|");
		var table_shape_2 = $("#table_shape_2");
		for(var i=0;i<updateSpcLocations.length;i++){
			var jw = updateSpcLocations[i].split(",");
			if(i<3){
				table_shape_2.find("tr").eq(i).find("input").eq(0).val(jw[0]);
				table_shape_2.find("tr").eq(i).find("input").eq(1).val(jw[1]);
			}else{
				var addTrStr = getAddTrStr(jw[0],jw[1],"2");
				table_shape_2.append(addTrStr);
			}
		}
		if(updateSpcLocations.length>3){
			var myForm=document.getElementById("form_shape_2");
			Validator.Validate_onLoad(myForm,3);
		}
	}else if(updateSpcShape == space_shape_yx){ //3：圆形j,w,r
		var updateSpcLocations = updateSpcLocation.split(",");
		$("#radius_3").val(updateSpcLocations[2]);
		var table_shape_3 = $("#table_shape_3");
		table_shape_3.find("tr").eq(0).find("input").eq(0).val(updateSpcLocations[0]);
		table_shape_3.find("tr").eq(0).find("input").eq(1).val(updateSpcLocations[1]);
	}else if(updateSpcShape == '${fns:findKey('space_shape_sx')}'){ //6：扇形j,w|r|nStartA|nEndA
		var updateSpcLocations = updateSpcLocation.split("|");
		$("#radius_6").val(updateSpcLocations[1]);
		$("#nStartA_6").val(updateSpcLocations[2]);
		$("#nEndA_6").val(updateSpcLocations[3]);
		var jw = updateSpcLocations[0].split(",");
		var table_shape_6 = $("#table_shape_6");
		table_shape_6.find("tr").eq(0).find("input").eq(0).val(jw[0]);
		table_shape_6.find("tr").eq(0).find("input").eq(1).val(jw[1]);
	}else if(updateSpcShape == '${fns:findKey('space_shape_xhcq')}'){ //7：线缓冲区j,w,k|j,w,k|j,w,k|j,w,k
		var updateSpcLocations = updateSpcLocation.split("|");
		$("#width_7").val(updateSpcLocations[0].split(",")[2]);
		var table_shape_7 = $("#table_shape_7");
		for(var i=0;i<updateSpcLocations.length;i++){
			var jw = updateSpcLocations[i].split(",");
			if(i<2){
				table_shape_7.find("tr").eq((i+1)).find("input").eq(0).val(jw[0]);
				table_shape_7.find("tr").eq((i+1)).find("input").eq(1).val(jw[1]);
			}else{
				var addTrStr = getAddTrStr(jw[0],jw[1],"7");
				table_shape_7.append(addTrStr);
			}
		}
		if(updateSpcLocations.length>2){
			var myForm=document.getElementById("form_shape_7");
			Validator.Validate_onLoad(myForm,3);
		}
	}else if(updateSpcShape == space_shape_jczaw){ //8：机场障碍物限制面j,w|j,w|j,w|rC2B2|j,w|j,w|rB3C3|j,w|j,w|j,w|j,w|rC4B4|j,w|j,w|rB1C1|j,w
		var updateSpcLocations = updateSpcLocation.split("|");
		$("#a1_jd").val(updateSpcLocations[0].split(",")[0]);
		$("#a1_wd").val(updateSpcLocations[0].split(",")[1]);
		$("#a2_jd").val(updateSpcLocations[1].split(",")[0]);
		$("#a2_wd").val(updateSpcLocations[1].split(",")[1]);
		$("#c2_jd").val(updateSpcLocations[2].split(",")[0]);
		$("#c2_wd").val(updateSpcLocations[2].split(",")[1]);
		$("#rC2B2").val(updateSpcLocations[3]);
		$("#b2_jd").val(updateSpcLocations[4].split(",")[0]);
		$("#b2_wd").val(updateSpcLocations[4].split(",")[1]);
		$("#b3_jd").val(updateSpcLocations[5].split(",")[0]);
		$("#b3_wd").val(updateSpcLocations[5].split(",")[1]);
		$("#rB3C3").val(updateSpcLocations[6]);
		$("#c3_jd").val(updateSpcLocations[7].split(",")[0]);
		$("#c3_wd").val(updateSpcLocations[7].split(",")[1]);
		$("#a3_jd").val(updateSpcLocations[8].split(",")[0]);
		$("#a3_wd").val(updateSpcLocations[8].split(",")[1]);
		$("#a4_jd").val(updateSpcLocations[9].split(",")[0]);
		$("#a4_wd").val(updateSpcLocations[9].split(",")[1]);
		$("#c4_jd").val(updateSpcLocations[10].split(",")[0]);
		$("#c4_wd").val(updateSpcLocations[10].split(",")[1]);
		$("#rC4B4").val(updateSpcLocations[11]);
		$("#b4_jd").val(updateSpcLocations[12].split(",")[0]);
		$("#b4_wd").val(updateSpcLocations[12].split(",")[1]);
		$("#b1_jd").val(updateSpcLocations[13].split(",")[0]);
		$("#b1_wd").val(updateSpcLocations[13].split(",")[1]);
		$("#rB1C1").val(updateSpcLocations[14]);
		$("#c1_jd").val(updateSpcLocations[15].split(",")[0]);
		$("#c1_wd").val(updateSpcLocations[15].split(",")[1]);
	}else if(updateSpcShape == space_shape_wg){
		//网格形状的处理
		var spcLocStr=obj.spcLocWgs84;
		var ghCodes = spcLocStr.split("|");
		var color=getGridSelColor();
		for(var i=0;i<ghCodes.length;i++){
			selGrids.push(ghCodes[i]);
			for(var j=0;j<curGrids.length;j++){
				if(curGrids[j].ghCode==ghCodes[i]){
					mapIframe.setGridColor(curGrids[j].polygon, color.colorBorder, color.colorBack, gridBackOpacity, gridBorderOpacity);
					break;
				}
			}
		}
	}
	
	$("#addSpaceDiv").show(); 
}

//添加时段
var timeId=$('#time>div').length;
function addTime(elem){
	timeId++;
	$(elem).parent().parent().after('<div class="control-group"><div class="controls">'+
			'<input type="text" id="timestart_'+timeId+'" name="startTime" style="width:150px;" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm\',maxDate:\'#F{$dp.$D(\\\'timeend_'+timeId+'\\\');}\'})"/>&nbsp;至&nbsp;'+
			'<input type="text" id="timeend_'+timeId+'" name="endTime" style="width:150px;" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm\',minDate:\'#F{$dp.$D(\\\'timestart_'+timeId+'\\\');}\'})"/>'+
			'<a href="javascript:void(0);" class="delTime" title="删除" onclick="delTime(this)" style="float: right;padding-right: 5px;text-align: center;line-height: 30px;color:red;"><i class="icon-remove"></i></a>'+
		'</div></div>');
}
function addTimeVal(startTime,endTime){
	timeId++;
	$('#time').append('<div class="control-group"><div class="controls">'+
			'<input type="text" id="timestart_'+timeId+'" name="startTime" style="width:150px;" value="'+startTime+'" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm\',maxDate:\'#F{$dp.$D(\\\'timeend_'+timeId+'\\\');}\'})"/>&nbsp;至&nbsp;'+
			'<input type="text" id="timeend_'+timeId+'" name="endTime" style="width:150px;" value="'+endTime+'" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm\',minDate:\'#F{$dp.$D(\\\'timestart_'+timeId+'\\\');}\'})"/>'+
			'<a href="javascript:void(0);" class="delTime" title="删除" onclick="delTime(this)" style="float: right;padding-right: 5px;text-align: center;line-height: 30px;color:red;"><i class="icon-remove"></i></a>'+
		'</div></div>');
}

//删除时段
function delTime(elem){
	$(elem).parent().parent().remove();	
}
//根据空域类型获得空域类型的相对应颜色
function getSpaceTypeById(spcType){
	for(var i=0;i<infoSpaceTypeList.length;i++){
		if(infoSpaceTypeList[i].spcType == spcType){
			return infoSpaceTypeList[i];
		}
	}
	return {colorBack: 'red', colorBorder: 'red'};
}

//通过页面上所描述的点和形状获得要画在地图上的多边形的坐标
function getPolygonWgs84(spcLocWgs84Val){
	var spcShape = $("#spcShape").val();
	var polygonWgs84 = "";
	$.ajax({
		url:pageContextPath+"/spaceManageGetPolygonWgs84.action",
		type:"post",
		data:{
			spcShape:spcShape,
			spcLocWgs84:spcLocWgs84Val
		},
		dataType:"json",
		async:false,
		success:function(data){
			if(data.errCode == '1'){
				polygonWgs84 = data.data.toString();
			}else{
				alert(data.errMsg);
			}
		}
	});
	return polygonWgs84;
}

//获得百度坐标拼接好的字符串
function getSpcLocWgs84(){
	var spcLocWgs84 = "";
	//获取形状
	var spcShape = $("#spcShape").val();
	var shapeFlag = "";
	if(spcShape == space_shape_wg){
		//网格
		shapeFlag = "0";
	}else if(spcShape == space_shape_dbx){
		//多边形
		shapeFlag = "2";
	}else if(spcShape == space_shape_yx){
		//圆形
		shapeFlag = "3";
	}else if(spcShape == space_shape_sx){
		//扇形
		shapeFlag = "6";
	}else if(spcShape == space_shape_xhcq){
		//线缓冲区
		shapeFlag = "7";
	}else if(spcShape == space_shape_jczaw){
		//机场障碍物限制面
		shapeFlag = "8";
	}
	var jds=[];
	$("input[name='jd_"+shapeFlag+"']").each(function(index,el){
		jds.push($(el).val()); 
	});
	var wds = [];
	$("input[name='wd_"+shapeFlag+"']").each(function(index,el){
		wds.push($(el).val()); 
	});
	//组装数据：spcLocWgs84
	if(spcShape == space_shape_wg){ //0：多边形，网格1编号|网格2编号|网格3编号…网格N编号
		for(var i=0;i<selGrids.length;i++){
			spcLocWgs84 += (selGrids[i] + "|") ;
		}
	}else if(spcShape == space_shape_dbx){ //2：多边形j,w|j,w|j,w
		for(var i=0;i<jds.length;i++){
			spcLocWgs84 += (jds[i] + "," + wds[i] + "|") ;
		}
	}else if(spcShape == space_shape_yx){ //3：圆形j,w,r
		var radius = $("#radius_3").val();
		spcLocWgs84 = (jds[0] + "," + wds[0] + "," + radius +"|") ;
	}else if(spcShape == space_shape_sx){ //6：扇形j,w|r|nStartA|nEndA
		var radius = $("#radius_6").val();
		var nStartA = $("#nStartA_6").val();
		var nEndA = $("#nEndA_6").val();
		spcLocWgs84 = (jds[0] + "," + wds[0] + "|" + radius +"|" + nStartA + "|" + nEndA + "|") ;
	}else if(spcShape == space_shape_xhcq){ //7：线缓冲区j,w,k|j,w,k|j,w,k|j,w,k
		var width = $("#width_7").val();
		for(var i=0;i<jds.length;i++){
			spcLocWgs84 += (jds[i] + "," + wds[i] + "," +width +"|") ;
		}
	}else if(spcShape == space_shape_jczaw){ //8：机场障碍物限制面j,w|j,w|j,w|rC2B2|j,w|j,w|rB3C3|j,w|j,w|j,w|j,w|rC4B4|j,w|j,w|rB1C1|j,w
		var a1_jd = $("#a1_jd").val();
		var a1_wd = $("#a1_wd").val();
		var a2_jd = $("#a2_jd").val();
		var a2_wd = $("#a2_wd").val();
		var c2_jd = $("#c2_jd").val();
		var c2_wd = $("#c2_wd").val();
		var rC2B2 = $("#rC2B2").val();
		var b2_jd = $("#b2_jd").val();
		var b2_wd = $("#b2_wd").val();
		var b3_jd = $("#b3_jd").val();
		var b3_wd = $("#b3_wd").val();
		var rB3C3 = $("#rB3C3").val();
		var c3_jd = $("#c3_jd").val();
		var c3_wd = $("#c3_wd").val();
		var a3_jd = $("#a3_jd").val();
		var a3_wd = $("#a3_wd").val();
		var a4_jd = $("#a4_jd").val();
		var a4_wd = $("#a4_wd").val();
		var c4_jd = $("#c4_jd").val();
		var c4_wd = $("#c4_wd").val();
		var rC4B4 = $("#rC4B4").val();
		var b4_jd = $("#b4_jd").val();
		var b4_wd = $("#b4_wd").val();
		var b1_jd = $("#b1_jd").val();
		var b1_wd = $("#b1_wd").val();
		var rB1C1 = $("#rB1C1").val();
		var c1_jd = $("#c1_jd").val();
		var c1_wd = $("#c1_wd").val();
		spcLocWgs84 = a1_jd+","+a1_wd+"|"+a2_jd+","+a2_wd+"|"+c2_jd+","+c2_wd+"|"+rC2B2+"|"+b2_jd+","+b2_wd+"|"+b3_jd+","+b3_wd+"|"+rB3C3+"|"+c3_jd+","+c3_wd+"|"+a3_jd+","+a3_wd+"|"+a4_jd+","+a4_wd+"|"+c4_jd+","+c4_wd+"|"+rC4B4+"|"+b4_jd+","+b4_wd+"|"+b1_jd+","+b1_wd+"|"+rB1C1+"|"+c1_jd+","+c1_wd+"|";
	}
	spcLocWgs84 = spcLocWgs84.substr(0, spcLocWgs84.length-1);
	return spcLocWgs84;
}

//设置Wgs84坐标到相应字段
function setSpcLocation(){
	//获取形状
	var spcShape = $("#spcShape").val();
	var shapeFlag = "";
	if(spcShape == space_shape_wg){
		//网格
		shapeFlag = "0";
	}else if(spcShape == space_shape_dbx){
		//多边形
		shapeFlag = "2";
	}else if(spcShape == space_shape_yx){
		//圆形
		shapeFlag = "3";
	}else if(spcShape == space_shape_sx){
		//扇形
		shapeFlag = "6";
	}else if(spcShape == space_shape_xhcq){
		//线缓冲区
		shapeFlag = "7";
	}else if(spcShape == space_shape_jczaw){
		//机场障碍物限制面
		shapeFlag = "8";
	}
	var myForm=document.getElementById("form_shape_"+shapeFlag);
	if(!Validator.Validate(myForm,3)){
		return false;
	}
	var spcLocWgs84Val = getSpcLocWgs84();
	$("#spcLocWgs84").val(spcLocWgs84Val);
	return true;
}
</script>
</html>
