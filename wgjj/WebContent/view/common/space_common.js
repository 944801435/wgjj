$(function(){
	//获得空域类型
	getInfoSpaceType();

	//点击按钮组下拉框内容时不关闭下拉框
	$(".dropdown-menu").on('click', function (e) {
        e.stopPropagation();
    });
	
    //给显示全部空域类型checkbox绑定事件
    $("#allSpaceType").change(function(){
        if(this.checked){
            $("input[name='spaceTypes']").each(function(index,item){
                $(item).prop("checked",true);
                checkedSpaceType(item);
            });
        }else{
            $("input[name='spaceTypes']").each(function(index,item){
                $(item).removeAttr("checked");
                uncheckedSpaceType(item);
            });
        }
    });
    //给空域类型checkbox绑定事件
    $("input[name='spaceTypes']").change(function(){
        if(this.checked){
            checkedSpaceType(this);
            if($("input[name='spaceTypes']:checked").length == $("input[name='spaceTypes']").length){
                $("#allSpaceType").prop("checked",true);
            }
        }else{
            uncheckedSpaceType(this);
            $("#allSpaceType").removeAttr("checked");
        }
    });

	//iframe加载完成后执行的方法
	$("#mapIframe").load(function(){
		//获取空域信息
		getInfoSpaceList();
		$("#allSpaceType").prop("checked",true);
		$("#allSpaceType").trigger("change");
	});
});
//初始化空域信息
var infoSpaceList = [];
//所有画的空域
var drawSpaceList = [];
//空域类型
var spcTypeList = [];
var spc_kind_map = {};
var space_shape_map = {};
// 白名单空域类别
var space_type_bmdky = "";
// 关联表标志
var rel_type_uav = "";
var rel_type_tmn = "";
//选中空域类型
function checkedSpaceType(item){
    var spaceType = item.id.split("_")[1];
    var spaceListByType = getInfoSpaceListBySpaceType(spaceType);
    var polygonOverlayList = [];
    for(var i=0;i<drawSpaceList.length;i++){
        if(drawSpaceList[i].spaceType == spaceType){
            polygonOverlayList = drawSpaceList[i].polygonOverlayList;
            break;
        }
    }
    if(polygonOverlayList.length==0){
        for(var i=0;i<spaceListByType.length;i++){
            var polygonWgs84s = spaceListByType[i].polygonWgs84;
            if(polygonWgs84s){
            	for(var j=0;j<polygonWgs84s.split(";").length;j++){
            		var polygonWgs84 = polygonWgs84s.split(";")[j];
            		var polygonOverlay = mapIframe.drawCustomColorPolygonNoClear(polygonWgs84,$(item).attr("colorBack"),$(item).attr("colorBorder"));
            		clickSpaceInfoToShowMsg(polygonOverlay, spaceListByType[i]);
            		var spaceObj={
            				spcId:spaceListByType[i].spcId,
            				polygonOverlay:polygonOverlay
            		};
            		polygonOverlayList.push(spaceObj);
            	}
            }
        }
    }
    var isFind = false;
    for(var i=0;i<drawSpaceList.length;i++){
        if(drawSpaceList[i].spaceType == spaceType){
            isFind = true;
            drawSpaceList[i].polygonOverlayList = polygonOverlayList;
            break;
        }
    }
    if(!isFind){
        var typeArrayObj = {
            spaceType:spaceType,
            polygonOverlayList:polygonOverlayList
        };
        drawSpaceList.push(typeArrayObj);
    }
}
//未选中空域类型
function uncheckedSpaceType(item){
    var spaceType = item.id.split("_")[1];
    var polygonOverlayList = [];
    for(var i=0;i<drawSpaceList.length;i++){
        if(drawSpaceList[i].spaceType == spaceType){
            polygonOverlayList = drawSpaceList[i].polygonOverlayList;
            for(var j=0;j<polygonOverlayList.length;j++){
                mapIframe.clearPointOverlay(polygonOverlayList[j].polygonOverlay);
            }
            drawSpaceList[i].polygonOverlayList = [];
        }
    }
}
//根据空域类型获取空域
function getInfoSpaceListBySpaceType(spaceType){
    var resultList = [];
    for(var i=0;i<infoSpaceList.length;i++){
        var spcType = infoSpaceList[i].spcType;
        if(spcType == spaceType){
            resultList.push(infoSpaceList[i]);
        }
    }
    return resultList;
}

//获取空域信息
function getInfoSpaceList(){
    $.ajax({
        url: ctx + "/spaceQueryList_.action",
        data:{},
        type:"post",
        dataType:"json",
        //async:false,
        success:function(data){
            if(data.errCode=="0"){   //失败
                //alert(data.errMsg);
            }else{ //成功
                $("input[name='spaceTypes']").each(function(index,item){
                    uncheckedSpaceType(item);
                });
                infoSpaceList = data.data.infoSpaceList;
                //必须这样写，不然画上的覆盖物需要拖动一下地图才能看得到
                setTimeout(function(){
                    $("input[name='spaceTypes']:checked").each(function(index,item){
                        checkedSpaceType(item);
                    });
                },1500);
            }
        }
    });
}

//获得空域类型
function getInfoSpaceType(){
	$.ajax({
		url: ctx + "/getInfoSpaceType.action",
		data: {},
		dataType: "json",
		type: "post",
		async: false,
		success: function(data){
			if(data.errCode !='1'){
				alert(data.errMsg);
				return;
			}
			$("#infoSpaceTypeDiv").empty();
			var typeList = data.data.infoSpaceTypeList;
			spcTypeList = typeList;
			for(var i=0;i<typeList.length;i++){
				var item = typeList[i];
				var typeDom = `
					<li style="display: flex; line-height: 11px;font-size: 12px;margin:5px 0;">
						<input checked="checked" type="checkbox" name="spaceTypes" id="spaceType_${item.spcType }" colorBack="${item.colorBack }" colorBorder="${item.colorBorder }">
						<span style="font-size: 12px;opacity: 0.8;margin-left: 2px;">${item.typeName }</span>
						<div style="width: 10px; height:10px; background-color: ${item.colorBack};margin: 1px 0px 0px 2px;border-radius:10px;"></div>
					</li>
				`;
				$("#infoSpaceTypeDiv").append(typeDom);
			}
            spc_kind_map = JSON.parse(data.data.spc_kind_map);
            space_shape_map = JSON.parse(data.data.space_shape_map);
            space_type_bmdky = data.data.space_type_bmdky;
            rel_type_uav = data.data.rel_type_uav;
            rel_type_tmn = data.data.rel_type_tmn;
            $(".dropdown-menu").css({"min-width":"unset", "padding":"3px 5px"});
		}
	});
}
//为多边形绑定click事件并提供展示信息内容
function clickSpaceInfoToShowMsg(polygonOverlay, infoSpaceObj){
	var spcKind = infoSpaceObj.spcKind!=null && infoSpaceObj.spcKind!='' ? spc_kind_map[infoSpaceObj.spcKind] : "";
	var infoWindowMsg = 
		"<table class='table table-bordered'>"+
			"<tr>"+
				"<td>名称</td>"+
				"<td colspan='3' style='max-width: 220px;text-align:left;'>"+infoSpaceObj.spcName+"</td>"+
			"</tr>"+
			"<tr>"+
				"<td>分类</td>"+
				"<td colspan='3' style='max-width: 220px;'>"+spcKind+"</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='width:20%'>形状</td>"+
				"<td style='width:30%'>"+space_shape_map[infoSpaceObj.spcShape]+"</td>"+
				"<td style='width:20%'>类型</td>"+
				"<td style='width:30%'>";
		for(var i=0;i<spcTypeList.length;i++){
			if(infoSpaceObj.spcType == spcTypeList[i].spcType){
				infoWindowMsg+=spcTypeList[i].typeName;
			}
		}
		infoWindowMsg+="</td>"+
			"</tr>"+
			"<tr>"+
				"<td>底高</td>"+
				"<td>"+infoSpaceObj.spcBottom+"</td>"+
				"<td>顶高</td>"+
				"<td>"+infoSpaceObj.spcTop+"</td>"+
			"</tr>";
	if(infoSpaceObj.infoSpaceTimes && infoSpaceObj.infoSpaceTimes.length > 0){
		infoWindowMsg+="<tr>";
		infoWindowMsg+="<td colspan='4' style='text-align:left;padding-left:5px;'>时段";
		infoWindowMsg+="</td>";
		infoWindowMsg+="</tr>";
		
		for(var i = 0;i<infoSpaceObj.infoSpaceTimes.length;i++) {
			infoWindowMsg+="<tr>";
			infoWindowMsg+="<td colspan='4' style='text-align:left;padding-left:5px;'>";
			infoWindowMsg+=infoSpaceObj.infoSpaceTimes[i].begDatetime+"&nbsp;至&nbsp;"+infoSpaceObj.infoSpaceTimes[i].endDatetime;
			infoWindowMsg+="</td>";
			infoWindowMsg+="</tr>";
		}
	}
		
	if(space_type_bmdky == infoSpaceObj.spcType){
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
				if(rel_type_uav == relType && infoUav!=null){
					oneMsg = "编号：" + infoUav.uavSn + " 厂商：" + infoUav.vender + " 型号：" + infoUav.model;
				}else if(rel_type_tmn == relType && runTerminal!=null){
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
