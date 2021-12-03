/**
 * 包含所有多边形覆盖物的方法
 */
var polygonViewMode=0;//0查看模式；1编辑模式，此时点击polygon不弹出信息窗口
function setPolygonViewMode(pmode){
	polygonViewMode=pmode;
}

//是否显示多边形的label
function isShowPolygonLabel(){
	for (var i = 0; i < overlayArray.length; i++){
		if(overlayArray[i].overlayType=='polygon'){
			if(overlayArray[i].overlayObj.getTooltip()){
				if(map.getZoom() >= showOverlayLabel) {//地图缩放到一定级别在显示覆盖物的label
					overlayArray[i].overlayObj.openTooltip();
				}else {
					//如果是Marker设置的setLabel设置其样式隐藏  
					overlayArray[i].overlayObj.closeTooltip();
				}
			}
		}
	}
}

//向地图上画指定颜色的多边形---不清空其他的多边形加入参数：是否显示label，label的msg内容，msg显示的位置坐标
function drawCustomColorPolygon(polygonWgs84,backColor,borderColor,backOpacity,borderOpacity,showLabelFlag,labelMsg){
	var coordinatesArray=[];
	var polygonWgs84s = polygonWgs84.split("|");
	for(var i=0;i<polygonWgs84s.length;i++){
		coordinatesArray.push(wgs84_to_gcj02(polygonWgs84s[i].split(",")[0],polygonWgs84s[i].split(",")[1]));
	}
	if(backOpacity == undefined || !backOpacity){
		backOpacity = 0.3;
	}
	if(borderOpacity == undefined || !borderOpacity){
		borderOpacity = 1;
	}
	if(showLabelFlag == undefined || showLabelFlag == null){
		showLabelFlag = false;
		labelMsg = "";
	}
	var polygon = L.polygon(coordinatesArray, getCustomColorStyleOptions(backColor,borderColor,backOpacity,borderOpacity))
				.addTo(map);
	var polygonObj = {'overlayType':'polygon','overlayObj':polygon};

	if(showLabelFlag && labelMsg != undefined){
		polygon.bindTooltip(labelMsg,//tooltip的内容
		{
			direction:"right",//tooltip在marker右侧显示
			offset:L.point(8,0),//tooltip相对于marker的偏移量
			permanent:true,//是否常显示tooltip
		}).openTooltip();
	}
	overlayArray.push(polygonObj);
	isShowPolygonLabel();
	return polygonObj;
}
//向地图上画指定颜色的多边形-
function drawCustomOptionPolygon(polygonWgs84,options){
	var coordinatesArray=[];
	var polygonWgs84s = polygonWgs84.split("|");
	for(var i=0;i<polygonWgs84s.length;i++){
		coordinatesArray.push(wgs84_to_gcj02(polygonWgs84s[i].split(",")[0],polygonWgs84s[i].split(",")[1]));
	}
	
	var polygon = L.polygon(coordinatesArray, options)
				.addTo(map);
	var polygonObj = {'overlayType':'polygon','overlayObj':polygon};

	overlayArray.push(polygonObj);
	return polygonObj;
}

//获取指定颜色的样式
function getCustomColorStyleOptions(backColor,borderColor,backOpacity,borderOpacity){
	var options = {
		fill:true,//是否填充颜色
		fillOpacity:backOpacity,//填充颜色的透明度
		fillColor:backColor,//填充的颜色
		stroke:true,//是否显示边线
		opacity:borderOpacity,//边线的透明度
		color: borderColor,//边线的颜色
		weight:2//边线的宽度
	};
	return options;
}

//设置多边形颜色
function setPolygonStyle(g, colorBorder, colorBack, backOpacity, borderOpacity){
	g.overlayObj.setStyle({
		fillOpacity:backOpacity,//填充颜色的透明度
		fillColor:colorBack,//填充的颜色
		opacity:borderOpacity,//边线的透明度
		color: colorBorder//边线的颜色
	});
}

//设置多边形颜色
function setGridColor(g, colorBorder, colorBack, backOpacity, borderOpacity){
	g.overlayObj.setStyle({
		fillOpacity:backOpacity,//填充颜色的透明度
		fillColor:colorBack,//填充的颜色
		opacity:borderOpacity,//边线的透明度
		color: colorBorder//边线的颜色
	});
}

//设置多边形边线和背景色的透明度
function setPolygonOpaciry(g,backOpacity,borderOpacity){
	g.overlayObj.setStyle({
		fillOpacity:backOpacity,//填充颜色的透明度
		opacity:borderOpacity//边线的透明度
	});
}

//设置多边形边线宽度
function setPolygonStrokeWeight(overlay,weightNum){
	overlay.overlayObj.setStyle({weight:weightNum});
}

//获取多边形所有点的经纬度
function getPolygonPoint(overlay){
	return overlay.overlayObj.getLatLngs();
}

//获得多边形的可视范围
function getPolygonBounds(overlay){
	var bs = overlay.overlayObj.getBounds();
    var result = {};
    result.getSouthWest=function getSouthWest(){
    	return bs.getSouthWest();
    }
    result.getNorthEast=function getNorthEast(){
    	return bs.getNorthEast();
    }
    return result;
}

//设置多边形的显示顺序
function setPolygonZIndex(overlay,num){
	overlay.overlayObj.setZIndex(num);
}

//设置多边形的显示顺序
L.Path.prototype.setZIndex = function (index){
    var obj = $(this._container || this._path);
    if (!obj.length) return; // not supported on canvas
    var parent = obj.parent();
    obj.data('order', index).detach();
    var lower = parent.children().filter(function (){
        var order = $(this).data('order');
        if (order == undefined) return false;
        return order <= index;
    });
    if (lower.length){
        lower.last().after(obj);
    } else {
        parent.prepend(obj);
    }
};

//为多边形绑定click事件
function polygonBindClick(overlay,clickCallBackFun,obj){
    //为overlay绑定click事件
	L.DomEvent.on(overlay.overlayObj,"click",function(e){
		clickCallBackFun.apply(this,[obj]);
	});
}

//为多边形绑定click事件并弹出相关信息
function polygonClickOpenMsg(overlay,msg){
    //为overlay绑定click事件
	L.DomEvent.on(overlay.overlayObj,"click",function(e){
		if(polygonViewMode==1)
			return;
		
		openInfoWindow(msg,e.latlng);
	});
}

//为多边形绑定dbclick事件并弹出相关信息
function polygonBindDbClick(overlay,dbClickCallBackFun,obj){
    //为overlay绑定click事件
	L.DomEvent.on(overlay.overlayObj,"dblclick",function(e){
		dbClickCallBackFun.apply(this,[obj]);
	});
}

//更新多边形的经纬度
function updatePolygonJwd(overlayObj,jwdWgs84){
	var coordinatesArray=[];
	var jwdWgs84s = jwdWgs84.split("|");
	for(var i=0;i<jwdWgs84s.length;i++){
		coordinatesArray.push(wgs84_to_gcj02(jwdWgs84s[i].split(",")[0],jwdWgs84s[i].split(",")[1]));
	}
	overlayObj.overlayObj.setLatLngs(coordinatesArray);
}

//获得一组经纬度的最大纬度点:如果最大纬度包含多个点，则返回最大经度的点
function getMsgPointByPolygonWgs84(polygonWgs84){
	var coordinatesArray=[];
	if(polygonWgs84.indexOf(";")!=-1){//网格
		var wgs84 = polygonWgs84.split(';');
		for(var k=0;k<wgs84.length;k++){
			var polygonWgs84s = wgs84[k].split("|");
			for(var i=0;i<polygonWgs84s.length;i++){
				coordinatesArray.push([parseFloat(polygonWgs84s[i].split(",")[0]),parseFloat(polygonWgs84s[i].split(",")[1])]);
			}
		}
	}else{//其他图形
		var polygonWgs84s = polygonWgs84.split("|");
		for(var i=0;i<polygonWgs84s.length;i++){
			coordinatesArray.push([parseFloat(polygonWgs84s[i].split(",")[0]),parseFloat(polygonWgs84s[i].split(",")[1])]);
		}
	}
	var maxLatObjArry = [];
	var maxLat = -90;
	for(var i=0;i<coordinatesArray.length;i++){
		if(maxLat < coordinatesArray[i][1]){
			maxLat = coordinatesArray[i][1];
		}
	}
	for(var i=0;i<coordinatesArray.length;i++){
		if(maxLat == coordinatesArray[i][1]){
			maxLatObjArry.push(coordinatesArray[i]);
		}
	}
	if(maxLatObjArry.length == 1){
		//如果只有一个则返回该坐标
		return wgs84_to_gcj02(maxLatObjArry[0][0],maxLatObjArry[0][1]);
	}else{
		//如果最大纬度包含多个点，则返回最大经度的点
		var maxLng = -180;
		var maxLngObj = [];
		for(var i=0;i<maxLatObjArry.length;i++){
			if(maxLng < maxLatObjArry[i][0]){
				maxLng = maxLatObjArry[i][0];
				maxLngObj = maxLatObjArry[i];
			}
		}
		return wgs84_to_gcj02(maxLngObj[0],maxLngObj[1]);
	}
}


//是否显示覆盖物的label
function isShowOverlayLabel(){
	for (var i = 0; i < overlayArray.length; i++){
		if(overlayArray[i].overlayType=='polygon' && overlayArray[i].labelMark !=null){
			if(overlayArray[i].labelMark.getTooltip()){
				if(map.getZoom() >= showOverlayLabel) {//地图缩放到一定级别在显示覆盖物的label
					overlayArray[i].labelMark.openTooltip();
				}else {
					//如果是Marker设置的setLabel设置其样式隐藏  
					overlayArray[i].labelMark.closeTooltip();
				}
			}
		}
	}
}

//画显示在覆盖物上label的点
function drawOverlayLabelMarker(labelMsg,msgPoint){
	var marker = L.marker(msgPoint,{icon:overlayLabelIcon})
		.bindTooltip(
			labelMsg,//tooltip的内容
			{
				direction:"right",//tooltip在marker右侧显示
				offset:L.point(0,0),//tooltip相对于marker的偏移量
				permanent:true,//是否常显示tooltip
				className:"overlayLabelStyle"//设置tooltip样式，提供一个classname
			}
		).openTooltip()
		.addTo(map);
	return marker;
}

//向地图上画指定颜色的多边形---不清空其他的多边形加入参数：是否显示label，label的msg内容，msg显示的位置坐标
function drawCustomColorPolygonNoClear(polygonWgs84,backColor,borderColor,backOpacity,borderOpacity,showLabelFlag,labelMsg,msgPoint){
	var coordinatesArray=[];
	var polygonWgs84s = polygonWgs84.split("|");
	for(var i=0;i<polygonWgs84s.length;i++){
		coordinatesArray.push(wgs84_to_gcj02(polygonWgs84s[i].split(",")[0],polygonWgs84s[i].split(",")[1]));
	}
	if(!backOpacity){
		backOpacity = 0.3;
	}
	if(!borderOpacity){
		borderOpacity = 1;
	}
	if(showLabelFlag == null){
		showLabelFlag = false;
		labelMsg = "";
		msgPoint = null;
	}
	var polygon = L.polygon(coordinatesArray, getCustomColorStyleOptions(backColor,borderColor,backOpacity,borderOpacity)).addTo(map);
	var polygonObj = {'overlayType':'polygon','overlayObj':polygon,'labelMark':null};
	showLabelFlag=false;
	if(showLabelFlag){
		var labelMark = drawOverlayLabelMarker(labelMsg,msgPoint);
		polygonObj.labelMark = labelMark;
	}
	overlayArray.push(polygonObj);
	isShowOverlayLabel();
	return polygonObj;
}


//向地图上画指定颜色的多边形---不清空其他的多边形加入参数：是否显示label，label的msg内容，msg显示的位置坐标
function drawFlyingPolygon(polygonWgs84,backColor,borderColor,backOpacity,borderOpacity,showLabelFlag,labelMsg,labelClassName){
	var polygonsGroup = null;
	var polygonWgs84Arry = polygonWgs84.split(";");
	if(polygonWgs84Arry.length>0){
		var polygonArry = [];
		for(var j=0;j<polygonWgs84Arry.length;j++) {
			var polygonWgs84s = polygonWgs84Arry[j].split("|");
			var coordinatesArray=[];
			for(var i=0;i<polygonWgs84s.length;i++){
				var lng = polygonWgs84s[i].split(",")[0];
				var lat = polygonWgs84s[i].split(",")[1];
				coordinatesArray.push(wgs84_to_gcj02(lng,lat));
			}
			var polygon = L.polygon(coordinatesArray, getCustomColorStyleOptions(backColor,borderColor,backOpacity,borderOpacity));
			if(showLabelFlag && labelMsg != undefined){
				polygon.bindTooltip(
					".",
					{
						offset: L.point(0,2),
						permanent: true,
						direction: 'center',
						className: "flyingPolygonNameStyle " + labelClassName
					}
				);
			}
			polygonArry.push(polygon);
		}
		polygonsGroup = L.featureGroup(polygonArry).addTo(map);

		var layerGroupObj = {"overlayType":"layerGroup","overlayObj": polygonsGroup};
		overlayArray.push(layerGroupObj);
	}
	return polygonsGroup;
}

function isShowFlyingPolygonLabel(){
	if(getMapZoom()>=showPolygonLabel){
		$(".flying30PolygonNameStyle").html("30");
		$(".flying50PolygonNameStyle").html("50");
		$(".flying60PolygonNameStyle").html("60");
		$(".flying90PolygonNameStyle").html("90");
	}else{
		$(".flyingPolygonNameStyle").html(".");
	}
}
