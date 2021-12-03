var bigNum = 10;
function mouseoverMarker(obj){
	var imgWidth = parseInt($(obj).css("width").split("px")[0]);
	var imgHeight = parseInt($(obj).css("height").split("px")[0]);
	$(obj).css({"width":(imgWidth+bigNum)+"px","height":(imgHeight+bigNum)+"px"});
}
function mouseleaveMarker(obj){
	var imgWidth = parseInt($(obj).css("width").split("px")[0]);
	var imgHeight = parseInt($(obj).css("height").split("px")[0]);
	$(obj).css({"width":(imgWidth-bigNum)+"px","height":(imgHeight-bigNum)+"px"});
}

//创建marker
function createNormalMarker(jd,wd,obj,markerData,clickMarkerCallBackFun){
	var divIcon = L.divIcon({
		html: "<img onmouseover='mouseoverMarker(this)' onmouseleave='mouseleaveMarker(this)' style='width:"+markerData.markerIconWidth+"px;height:"+markerData.markerIconHeight+"px;' src='"+markerData.markerIconSrc+"'>",
		iconSize: [markerData.markerIconWidth,markerData.markerIconHeight],
		className: markerData.markerIconClassName
	});
	var marker = L.marker(wgs84_to_gcj02(jd,wd),{icon:divIcon,title:obj.labelMsg});
	if(markerData.showLabel){
		var labelMsg = obj.labelMsg;
		marker.bindTooltip(
				labelMsg,//tooltip的内容
				{
					direction:"right",//tooltip在marker右侧显示
					offset:L.point(12,0),//tooltip相对于marker的偏移量
					permanent:true,//是否常显示tooltip
					className:markerData.markerIconClassName
				}
		).openTooltip();
		if(map.getZoom() >= showLabel) {//地图缩放到一定级别在显示标牌
			marker.openTooltip();
		}else{
			marker.closeTooltip();
		}
	}
    //为marker绑定click事件
	L.DomEvent.on(marker,"click",function(e){
		clickMarkerCallBackFun.apply(this,[obj]);
	});
	return marker;
}

//创建多边形
function createPolygon(polygonWgs84,backColor,borderColor,backOpacity,borderOpacity,showLabelFlag,labelMsg){
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
	var polygon = L.polygon(coordinatesArray, getCustomColorStyleOptions(backColor,borderColor,backOpacity,borderOpacity));
	/*//ERROR 如果不先画多边形到页面上，就不能画tooltip到页面上，而且不能绑定click事件
	 * if(showLabelFlag && labelMsg != undefined){
		polygon.bindTooltip(labelMsg,//tooltip的内容
		{
			direction:"right",//tooltip在marker右侧显示
			offset:L.point(8,0),//tooltip相对于marker的偏移量
			permanent:true,//是否常显示tooltip
		}).openTooltip();
		if(map.getZoom() >= showOverlayLabel) {//地图缩放到一定级别在显示标牌
			polygon.openTooltip();
		}else{
			polygon.closeTooltip();
		}
	}*/
	return polygon;
}

//以分组的形式放入地图中
function drawLayerGroup(markerArry){
	var layerGroup = L.layerGroup(markerArry).addTo(map);
	var layerGroupObj = {"overlayType":"layerGroup","overlayObj":layerGroup};
	overlayArray.push(layerGroupObj);
	return layerGroupObj;
}
