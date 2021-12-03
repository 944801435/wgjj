/**
 * 	包含所有的marker点的方法
 */
/*默认的点图标*/
var defaultIcon = L.icon({
	iconUrl: ""+ctx+"/leaflet/images/marker-icon.png",
	iconSize: [15,25]
});

//画点
function drawMarker(jd,wd){
	var labelMsg = jd + "," + wd;
    var marker = L.marker(wgs84_to_gcj02(jd,wd),{icon:defaultIcon}).addTo(map)
	/*.bindTooltip(
		labelMsg,//tooltip的内容
		{
			direction:"right",//tooltip在marker右侧显示
			offset:L.point(8,0),//tooltip相对于marker的偏移量
			permanent:true,//是否常显示tooltip
		}
	).openTooltip()*/;
    var pointObj = {'overlayType':'marker','overlayObj':marker};
    overlayArray.push(pointObj);
    return pointObj;
}

//画可以点击的自定义marker
function drawClickMarker(jd,wd,obj,markerData,clickMarkerCallBackFun){
	var width = markerData.width;
	var height = markerData.height;
	var divIcon = L.divIcon({
		html: obj.html,
		iconSize: [width,height],
		className: markerData.className
	});
	var marker = L.marker(wgs84_to_gcj02(jd,wd),{icon:divIcon}).addTo(map);
	var labelMsg = obj.labelMsg;
	if(markerData.showLabel && labelMsg!=null && labelMsg!=""){
		marker.bindTooltip(
				labelMsg,//tooltip的内容
				{
					direction:"right",//tooltip在marker右侧显示
					offset:L.point(12,0),//tooltip相对于marker的偏移量
					permanent:true,//是否常显示tooltip
					className:""
				}
		).openTooltip();
		if(getMapZoom() >= showLabel) {//地图缩放到一定级别在显示标牌
			marker.openTooltip();
		}else{
			marker.closeTooltip();
		}
	}
	if(clickMarkerCallBackFun!=null){
		//为marker绑定click事件
		L.DomEvent.on(marker,"click",function(e){
			clickMarkerCallBackFun.apply(this,[obj]);
		});
	}
	var pointObj = {'overlayType':'marker','overlayObj':marker};
	overlayArray.push(pointObj);
	return pointObj;
}

//画包含span的marker
function drawSpanMarker(jd,wd,obj,markerData){
	var divIcon = L.divIcon({
		html: "<span>"+obj.spanHtml+"</span>",
		iconSize: [markerData.markerIconWidth,markerData.markerIconHeight],
		className: markerData.markerIconClassName
	});
	var marker = L.marker(wgs84_to_gcj02(jd,wd),{icon:divIcon}).addTo(map);
	var labelMsg = obj.labelMsg;
	if(markerData.showLabel && labelMsg != undefined && labelMsg!=null && labelMsg!=""){
		marker.bindTooltip(
				labelMsg,//tooltip的内容
				{
					direction:(markerData.labelPosition == undefined || markerData.labelPosition == null || markerData.labelPosition == "" )? "right" : markerData.labelPosition,//tooltip在marker右侧显示
					offset:L.point(12,0),//tooltip相对于marker的偏移量
					permanent:true,//是否常显示tooltip
					className:markerData.labelClassName == undefined ? "" : markerData.labelClassName
				}
		).openTooltip();
		if(map.getZoom() >= showLabel) {//地图缩放到一定级别在显示标牌
			marker.openTooltip();
		}else{
			marker.closeTooltip();
		}
	}
	var pointObj = {'overlayType':'marker','overlayObj':marker};
	overlayArray.push(pointObj);
	if(map.getZoom()<markerData.showInMapZoom){
		clearPointOverlay(pointObj);
	}
	return pointObj;
}

//画online_marker_data.js中的marker
function drawNormalMarker(jd,wd,obj,markerData,clickMarkerCallBackFun){
	var divIcon = L.divIcon({
		html: "<img style='width:"+markerData.markerIconWidth+"px;height:"+markerData.markerIconHeight+"px;' src='"+markerData.markerIconSrc+"'>",
		iconSize: [markerData.markerIconWidth,markerData.markerIconHeight],
		className: markerData.markerIconClassName
	});
	var marker = L.marker(wgs84_to_gcj02(jd,wd),{icon:divIcon}).addTo(map);
	var labelMsg = obj.labelMsg;
	if(markerData.showLabel && labelMsg != undefined && labelMsg!=null && labelMsg!=""){
		marker.bindTooltip(
				labelMsg,//tooltip的内容
				{
					direction:(markerData.labelPosition == undefined || markerData.labelPosition == null || markerData.labelPosition == "" )? "right" : markerData.labelPosition,//tooltip在marker右侧显示
					offset:(markerData.labelOffset == undefined || markerData.labelOffset == null || markerData.labelOffset == "" )? L.point(12,0) : L.point(markerData.labelOffset[0],markerData.labelOffset[1]),//tooltip相对于marker的偏移量
					permanent:true,//是否常显示tooltip
					className:markerData.labelClassName == undefined ? "" : markerData.labelClassName
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
	var pointObj = {'overlayType':'marker','overlayObj':marker};
	overlayArray.push(pointObj);
	if(map.getZoom()<markerData.showInMapZoom){
		clearPointOverlay(pointObj);
	}
	return pointObj;
}

//获取无人的标牌内容
function getUavLabelMsg(uav){
	return uav.pn+"<br>H"+uav.hei+"m V"+uav.spe+"m/s";
}

//无人机标牌默认显示级别
var uavLabelZIndexMap = new Map();
uavLabelZIndexMap.set("nomalUavLabelStyle", 100);
uavLabelZIndexMap.set("earlyWarn60UavLabelStyle", 1000);
uavLabelZIndexMap.set("earlyWarn30UavLabelStyle", 2000);
uavLabelZIndexMap.set("warnUavLabelStyle", 50000);
uavLabelZIndexMap.set("eventUavLabelStyle", 100000);
uavLabelZIndexMap.set("blackUavLabelStyle", 100000);

//画无人机的点
function drawUavMarker(jd,wd,uav,eventFlag){
	var tooltipMsg = getUavLabelMsg(uav);
	var tooltipClassName = "";
	var uavLvl = uav.lvl;
	var folderFlag = "";
	var iconDivClassName = "";
	var imgClassName = "";
	var isShowLabel = false;
	var uavLabelCheckboxObj = parent.document.getElementById("uavLabelCheckbox");
	if(uavLabelCheckboxObj){
		isShowLabel = uavLabelCheckboxObj.checked;
	}
	var isWarnUav = true;
	if(eventFlag){
		tooltipClassName="eventUavLabelStyle";
		folderFlag = "event";
		iconDivClassName = "eventUavDivStyle";
		imgClassName = "eventUavImgStyle";
	}else if(uav.blackFlag == '1'){
		tooltipClassName="blackUavLabelStyle";
		folderFlag = "black";
		iconDivClassName = "blackUavDivStyle";
		imgClassName = "blackUavImgStyle";
	}else if(uav.areaSpecialId!=null&&uav.areaSpecialId!=''){
		tooltipClassName="blackUavLabelStyle";
		folderFlag = "black";
		iconDivClassName = "blackUavDivStyle";
		imgClassName = "blackUavImgStyle";
	}else{
		if(uav.wrn!=null&&uav.wrn!=''){
			var wrns = uav.wrn.split("|");
			var isEarly30 = false;
			var isWarn = false;
			for(var i=0;i<wrns.length;i++){
				if(wrns[i].indexOf("PAS30")==-1&&wrns[i].indexOf("PAS60")==-1){
					isWarn = true;
					break;
				}
			}
			if(!isWarn){
				if(uav.wrn.indexOf("PAS30")!=-1){
					isEarly30 = true;
				}
				if(!isEarly30){
					if(uav.wrn.indexOf("PAS60")!=-1){
						tooltipClassName="earlyWarn60UavLabelStyle";
						folderFlag = "early60";
						iconDivClassName = "earlyWarn60UavDivStyle";
						imgClassName = "earlyWarn60UavImgStyle";
					}
				}else{
					tooltipClassName="earlyWarn30UavLabelStyle";
					folderFlag = "early30";
					iconDivClassName = "earlyWarn30UavDivStyle";
					imgClassName = "earlyWarn30UavImgStyle";
				}
			}else{
				tooltipClassName="warnUavLabelStyle";
				folderFlag = "warn";
				iconDivClassName = "warnUavDivStyle";
				imgClassName = "warnUavImgStyle";
			}
		}else{
			tooltipClassName="nomalUavLabelStyle";
			folderFlag = "normal";
			iconDivClassName = "normalUavDivStyle";
			imgClassName = "normalUavImgStyle";
			isWarnUav = false;
		}
	}
	var divIconHtml = '';
	if(uavLvl==null || uavLvl=='')
		divIconHtml = "<img id='uavImg_"+uav.pn+"' class='"+imgClassName+"' src='"+ctx+"/view/uavOnline/uav/uav_s_0.png'>";
	else
		divIconHtml = "<img id='uavImg_"+uav.pn+"' class='"+imgClassName+"' src='"+ctx+"/view/uavOnline/uav/uav_s_"+uavLvl+".png'>";
	
	divIconHtml += "<div id='uavArrow_"+uav.pn+"' style=\"-webkit-transform:rotate("+uav.dir+"deg);	background:url('"+ctx+"/view/uavOnline/"+folderFlag+"/uav_s_arrow.png');\" class=\"uavArrowStyle\"></div>";
	var uavIconDiv = L.divIcon({
		html:divIconHtml,
		iconSize: [40,40],
		className:"uavImgStyle breatheStyle "+iconDivClassName
	});
	var marker = L.marker(wgs84_to_gcj02(jd,wd),{icon:uavIconDiv,title:uav.pn}).addTo(map);

	//标牌避让
	marker.bindTooltip(tooltipMsg,{direction:titlePoint_array[0][0],offset:titlePoint_array[0][1],permanent:true,className:tooltipClassName});
	labelDir.set(marker,0);
	if(map.getZoom() >= showLabel && (isShowLabel || isWarnUav)) {//地图缩放到一定级别在显示标牌
		// 显示无人机标牌选中或者告警，则展示标牌
		moveTip(marker,0,tooltipMsg,tooltipClassName);
  		marker.openTooltip();
	}else{
		marker.closeTooltip();
	    delPointMarkerTipLine(marker);
	}
    //为marker绑定click事件
	L.DomEvent.on(marker,"click",function(e){
		e.target.setZIndexOffset(new Date().getTime());
		parent.clickUavMarker(uav.pn);
	});
    var markerObj = {'overlayType':'marker','overlayObj':marker};
    overlayArray.push(markerObj);
    $(".uavImgStyle").css("z-index",10000);
    if(uav.spe == '0'){//速度为零时将箭头隐藏
    	$("#uavArrow_"+uav.pn).addClass("hide");
    }else{
    	$("#uavArrow_"+uav.pn).removeClass("hide");
    }
    return markerObj;
}
//更新无人机信息
function updateUavMarker(lastMark,uav,eventFlag){
	var uavLvl = uav.lvl;
	var labelMsg = getUavLabelMsg(uav);
	
	var folderFlag = "";
	var iconDivClassName = "";
	var imgClassName = "";
	var uavLabelClassName = "";
	var isShowLabel = false;
	var uavLabelCheckboxObj = parent.document.getElementById("uavLabelCheckbox");
	if(uavLabelCheckboxObj){
		isShowLabel = uavLabelCheckboxObj.checked;
	}
	var isWarnUav = true;
	//修改tooltip样式
	if(eventFlag){
		uavLabelClassName = "eventUavLabelStyle";
		folderFlag = "event";
		iconDivClassName = "eventUavDivStyle";
		imgClassName = "eventUavImgStyle";
	}else if(uav.blackFlag == '1'){
		uavLabelClassName = "blackUavLabelStyle";
		folderFlag = "black";
		iconDivClassName = "blackUavDivStyle";
		imgClassName = "blackUavImgStyle";
	}else if(uav.areaSpecialId!=null&&uav.areaSpecialId!=''){
		uavLabelClassName = "blackUavLabelStyle";
		folderFlag = "black";
		iconDivClassName = "blackUavDivStyle";
		imgClassName = "blackUavImgStyle";
	}else{
		if(uav.wrn!=null&&uav.wrn!=''){
			var wrns = uav.wrn.split("|");
			var isEarly30 = false;
			var isWarn = false;
			for(var i=0;i<wrns.length;i++){
				if(wrns[i].indexOf("PAS30")==-1&&wrns[i].indexOf("PAS60")==-1){
					isWarn = true;
					break;
				}
			}
			lastMark.overlayObj.openTooltip();
			if(!isWarn){
				if(uav.wrn.indexOf("PAS30")!=-1){
					isEarly30 = true;
				}
				if(!isEarly30){
					if(uav.wrn.indexOf("PAS60")!=-1){
						uavLabelClassName = "earlyWarn60UavLabelStyle";
						folderFlag = "early60";
						iconDivClassName = "earlyWarn60UavDivStyle";
						imgClassName = "earlyWarn60UavImgStyle";
					}
				}else{
					uavLabelClassName = "earlyWarn30UavLabelStyle";
					folderFlag = "early30";
					iconDivClassName = "earlyWarn30UavDivStyle";
					imgClassName = "earlyWarn30UavImgStyle";
				}
			}else{
				uavLabelClassName = "warnUavLabelStyle";
				folderFlag = "warn";
				iconDivClassName = "warnUavDivStyle";
				imgClassName = "warnUavImgStyle";
			}
		}else{
			uavLabelClassName = "nomalUavLabelStyle";
			folderFlag = "normal";
			iconDivClassName = "normalUavDivStyle";
			imgClassName = "normalUavImgStyle";
			isWarnUav = false;
		}
	}
	
	//修改tooltip内容
	lastMark.overlayObj.setTooltipContent(labelMsg);
	$(lastMark.overlayObj.getTooltip()._container)
		.removeClass("nomalUavLabelStyle")
		.removeClass("eventUavLabelStyle")
		.removeClass("earlyWarn60UavLabelStyle")
		.removeClass("earlyWarn30UavLabelStyle")
		.removeClass("blackUavLabelStyle")
		.removeClass("warnUavLabelStyle")
		.addClass(uavLabelClassName);
	if(map.getZoom() >= showLabel && (isShowLabel || isWarnUav)) {//地图缩放到一定级别在显示标牌
  		if(lastMark.overlayObj.getTooltip()!=null && !lastMark.overlayObj.isTooltipOpen()){
     		lastMark.overlayObj.openTooltip();
  		}
	}else{
  		if(lastMark.overlayObj.getTooltip()!=null && lastMark.overlayObj.isTooltipOpen()){
			lastMark.overlayObj.closeTooltip();
     		delPointMarkerTipLine(lastMark.overlayObj);
  		}
	}
	$("#uavImg_"+uav.pn).removeClass("earlyWarn60UavImgStyle").removeClass("earlyWarn30UavImgStyle").removeClass("warnUavImgStyle").removeClass("normalUavImgStyle").removeClass("eventUavImgStyle").removeClass("blackUavImgStyle").addClass(imgClassName);
	$("#uavArrow_"+uav.pn).css({"-webkit-transform":"rotate("+uav.dir+"deg)","background":"url('"+ctx+"/view/uavOnline/"+folderFlag+"/uav_s_arrow.png')"});
	$("#uavImg_"+uav.pn).parent().removeClass("earlyWarn60UavDivStyle").removeClass("earlyWarn30UavDivStyle").removeClass("warnUavDivStyle").removeClass("normalUavDivStyle").removeClass("eventUavDivStyle").removeClass("blackUavDivStyle").addClass(iconDivClassName);
	//移动marker
	lastMark.overlayObj.setLatLng(wgs84_to_gcj02(uav.lng,uav.lat));
	$(".uavImgStyle").css("z-index",1000000);
	if(uav.spe == '0'){//速度为零时将箭头隐藏
		$("#uavArrow_"+uav.pn).addClass("hide");
	}else{
		$("#uavArrow_"+uav.pn).removeClass("hide");
	}
	//标牌避让
	if(lastMark.overlayObj.getTooltip()!=null && lastMark.overlayObj.isTooltipOpen()){
		moveTip(lastMark.overlayObj,0,labelMsg,null);
	} 

	return lastMark;
}

//获得marker的点坐标
function getMarkerPosition(marker){
	return marker.overlayObj.getLatLng();
}

//设置marker位置
function setMarkerPosition(marker,jd,wd){
	var position = wgs84_to_gcj02(jd,wd);
	marker.overlayObj.setLatLng(position);
	return marker;
}

//设置marker的图标
function setMarkerIcon(marker,markerData){
	var divIcon = L.divIcon({
		html: "<img style='width:"+markerData.markerIconWidth+"px;height:"+markerData.markerIconHeight+"px;' src='"+markerData.markerIconSrc+"'>",
		iconSize: [markerData.markerIconWidth,markerData.markerIconHeight],
		className: markerData.markerIconClassName
	});
	marker.overlayObj.setIcon(divIcon);
	return marker;
}

//定位无人机
function positionUavMarker(lastMark){ 
	lastMark.overlayObj.setZIndexOffset(new Date().getTime());
	map.panTo(lastMark.overlayObj.getLatLng());
}

//是否显示标牌
function isShowLabel(){
	var isShowLabel = false;
	var uavLabelCheckboxObj = parent.document.getElementById("uavLabelCheckbox");
	if(uavLabelCheckboxObj){
		isShowLabel = uavLabelCheckboxObj.checked;
	}
	for (var i = 0; i < overlayArray.length; i++){
		if(overlayArray[i].overlayType=='marker'){
			if(overlayArray[i].overlayObj.getTooltip() && $(overlayArray[i].overlayObj._icon).hasClass("uavImgStyle")){
				//地图缩放到一定级别在显示标牌
				if(map.getZoom() >= showLabel && (isShowLabel || !$(overlayArray[i].overlayObj.getTooltip()._container).hasClass("nomalUavLabelStyle"))){
						//标牌避让
						//console.info("isShowLibel");
						overlayArray[i].overlayObj.openTooltip();
						moveTip(overlayArray[i].overlayObj,0,null,null); 
				}else {
					//如果是Marker设置的setLabel设置其样式隐藏  
					overlayArray[i].overlayObj.closeTooltip();
		      		delPointMarkerTipLine(overlayArray[i].overlayObj);
				}
			}
		}
	}
}

function sortUavMarkerTooltip(uavs){
	//var begTime = new Date().getTime();
	if(map.getZoom() >= showLabel) {//地图缩放到一定级别在显示标牌
		for(var i=0;i<uavs.length;i++){
			var lastMark = uavs[i].lastMark;
			var hei = uavs[i].uav.hei;
			hei = parseInt(hei);
			if(lastMark.overlayObj.getTooltip()!=null && lastMark.overlayObj.isTooltipOpen()){
				var zIndex = 1;
				for (var [key, value] of uavLabelZIndexMap) {
					//console.info("key====" + key + "==value===" + value);
					if($(lastMark.overlayObj.getTooltip()._container).hasClass(key)){
						zIndex = value;
						//console.info("===========zIndex=====" + zIndex);
					}
				}
				//console.info("====hei======" + hei);
				//console.info("=======zIndex+hei=====" + (zIndex+hei));
				var cssText = $(lastMark.overlayObj.getTooltip()._container).attr("style") +  ";z-index:"+(zIndex+hei)+" !important;";
				$(lastMark.overlayObj.getTooltip()._container).css("cssText", cssText);
			}
		}
	}
	//var endTime = new Date().getTime();
	//console.info("计算标牌显示优先级耗时：" + (endTime - begTime) + "毫秒");
}

//画点
function drawDivMarker(jd,wd,divIconDom,labelMsg,clickMarkerCallBackFun){
	let width=$(divIconDom).css('width');
	let height=$(divIconDom).css('height');
	var divIcon = L.divIcon({
		html:divIconDom,
		iconSize: [parseInt(width),parseInt(height)],
	});
    var marker = L.marker(wgs84_to_gcj02(jd,wd),{icon:divIcon}).addTo(map);
	marker.bindTooltip(labelMsg,{
			direction:"right",//tooltip在marker右侧显示
			offset:L.point(16,0),//tooltip相对于marker的偏移量
			permanent:true//是否常显示tooltip
		});
	if(map.getZoom() >= showLabel) {//地图缩放到一定级别在显示标牌
		marker.openTooltip();
	}else{
		marker.closeTooltip();
	}
    //为marker绑定click事件
	L.DomEvent.on(marker,"click",function(e){
		e.target.setZIndexOffset(new Date().getTime());
		clickMarkerCallBackFun();
	});
	
    var markerObj = {'overlayType':'marker','overlayObj':marker};
    overlayArray.push(markerObj);
    return markerObj;
}
function updateDivMarker(marker,jd,wd,divIconDom,labelMsg){
	var position = wgs84_to_gcj02(jd,wd);
	let width=$(divIconDom).css('width');
	let height=$(divIconDom).css('height');
	var divIcon = L.divIcon({
		html:divIconDom,
		iconSize: [parseInt(width),parseInt(height)],
	});
	marker.overlayObj.setLatLng(position)
					 .setIcon(divIcon);
	marker.overlayObj.getTooltip().setContent(labelMsg);
	return marker;
}