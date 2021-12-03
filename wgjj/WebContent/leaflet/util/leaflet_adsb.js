//获得adsb飞机的标牌内容
function getAdsbLabelMsg(adsbObj){
	return adsbObj.phone.toUpperCase() + "<br>" + adsbObj.alt + " " + adsbObj.spe + " " + adsbObj.dir;
}

//储存所有ADSB飞机
var adsbPlaneGroup = null;

//画adsb的点
function drawAdsbMarker(adsbObj){
	var jd = adsbObj.lng;
	var wd = adsbObj.lat;
	var dir = adsbObj.dir;
	var adsbIcon = L.divIcon({
		html:"<img id=\"adsb_"+adsbObj.pn+"\" style=\"-webkit-transform: rotate("+dir+"deg);width:26px;height:26px;\" src='${pageContext.request.contextPath }/leaflet/adsb.png'>",
		iconSize: [26,26],
		className:"adsbMarkerStyle"
	});
	var labelMsg = getAdsbLabelMsg(adsbObj);
	var marker = L.marker(wgs84_to_gcj02(jd,wd),{icon:adsbIcon})
	.bindTooltip(
		labelMsg,//tooltip的内容
		{
			direction:"right",//tooltip在marker右侧显示
			offset:L.point(10,0),//tooltip相对于marker的偏移量
			permanent:true,//是否常显示tooltip
			className:"nomalUavLabelStyle"//设置tooltip样式，提供一个classname
		}
	);
	$(".adsbMarkerStyle").css("z-index",(99999999+parseInt(adsbObj.alt)));
	if(adsbPlaneGroup!=null){
		adsbPlaneGroup.addLayer(marker);
	}else{
		adsbPlaneGroup = L.featureGroup([marker]).addTo(map);
	}
	showAdsbPlaneLabel($("#adsbPlaneLabelCheckbox",parent.document)[0].checked);
	return marker;
}

//更新adsb的点
function updateAdsbMarker(marker,adsbObj){
	var labelMsg = getAdsbLabelMsg(adsbObj);
	//修改tooltip内容
	marker.setTooltipContent(labelMsg);
	$("#adsb_" + adsbObj.pn).css("-webkit-transform","rotate("+adsbObj.dir+"deg)");
	//移动marker
	marker.setLatLng(wgs84_to_gcj02(adsbObj.lng,adsbObj.lat));
	$(".adsbMarkerStyle").css("z-index", (99999999+parseInt(adsbObj.alt)));
}

//删除adsb的点
function clearAdsbMarker(marker){
	adsbPlaneGroup.removeLayer(marker);
}
//控制adsb飞机的tooltip显示隐藏
function showAdsbPlaneLabel(flag){
	if(adsbPlaneGroup!=null){
		if(flag){
			adsbPlaneGroup.eachLayer(function (layer) {
			    layer.openTooltip();
			});
		}else{
			adsbPlaneGroup.eachLayer(function (layer) {
			    layer.closeTooltip();
			});
		}
	}
}

//民航航线group
var planeLinesGroup = null;
//显示民航航线
function showPlaneLines(planeLinesList){
	if(planeLinesList.length>0){
		var options = {
			stroke:true,//是否显示边线
			opacity: 1,//边线的透明度
			color: "#11539E",//边线的颜色
			weight: 1//边线的宽度
		};
		var polylineArry = [];
		$.each(planeLinesList, function (index, data) {
			var labelMsg = data.name;
			var jwdWgs84s = data.points;
			var coordinatesArray=[];
			for(var i=0;i<jwdWgs84s.length;i++){
				var lng = jwdWgs84s[i].lng;
				var lat = jwdWgs84s[i].lat;
				coordinatesArray.push(wgs84_to_gcj02(lng,lat));
			}
			var polyline = L.polyline(coordinatesArray, options);
			polyline.bindTooltip(
				labelMsg,
				{
					permanent:true,
					direction:'right',
					className:"planeLinesStyle"
				}
			);
			polylineArry.push(polyline);
		});
		planeLinesGroup = L.featureGroup(polylineArry).addTo(map);
		showPlaneLinesLabel($("#planeLinesLabelCheckbox",parent.document)[0].checked);
	}
}
//控制航线上的tooltip显示隐藏
function showPlaneLinesLabel(flag){
	if(planeLinesGroup!=null){
		if(flag){
			planeLinesGroup.eachLayer(function (layer) {
			    layer.openTooltip();
			});
		}else{
			planeLinesGroup.eachLayer(function (layer) {
			    layer.closeTooltip();
			});
		}
	}
}
//移除民航航线
function removePlaneLines(){
	if(planeLinesGroup!=null){
		planeLinesGroup.clearLayers();
	}
}