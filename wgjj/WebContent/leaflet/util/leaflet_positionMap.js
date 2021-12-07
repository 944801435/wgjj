/**
 * 对地图进行定位的所有方法
 */
//根据点定位
function positionPoint(latlng){
	map.panTo(latlng);
	map.setZoom(dftZoom);
}

function positionJwd(jd,wd){
	map.panTo(wgs84_to_gcj02(jd,wd));
}

//设置地图定位和缩放
function setCenterAndZoom(latlng,zoom){
	if(map.getZoom()!=zoom){
		map.setZoom(zoom);
		waitZoom();
		function waitZoom(){
			if(map.getZoom()!=zoom){
				setTimeout(function(){
					waitZoom();
				}, 50);
			}else{
				map.panTo(latlng);
			}
		}
	}else{
		map.panTo(latlng);
	}
}

//根据城市定位
function positionCity(){
	map.fitBounds(maxBounds_default);
}
	
//设置地图默认显示区域
function setMapCenter(latlngs){
	map.fitBounds(latlngs);
}

//根据所有覆盖物定位
function positionSpace(){
	var all_points=[];
	for(var i=0;i<overlayArray.length;i++){
		if(overlayArray[i].overlayType=='marker'){
			all_points.push(overlayArray[i].overlayObj.getLatLng());
		}else if(overlayArray[i].overlayType=='polygon'){
			all_points.push(overlayArray[i].overlayObj.getLatLngs());
		}else if(overlayArray[i].overlayType=='layerGroup'){
			var groupLayers = overlayArray[i].overlayObj.getLayers();
			for(var j=0;j<groupLayers.length;j++){
				var objKeysArry = Object.keys(groupLayers[j]);
				if(objKeysArry.indexOf("_latlng")!=-1){
					all_points.push(groupLayers[j].getLatLng());
				}else if(objKeysArry.indexOf("_latlngs")!=-1){
					all_points.push(groupLayers[j].getLatLngs());
				}
			}
		}
	}
	if(all_points.length == 0) {//如果没有任何覆盖物，定位到默认值。
		positionCity();
		return;
	}
	map.fitBounds(all_points);
}

//定位指定的覆盖物
function positionPointOverlayArray(overlays,zoomNum){
	var all_points=[];
	for(var i=0;i<overlays.length;i++){
		if(overlays[i].overlayType=='marker'){
			all_points.push(overlays[i].overlayObj.getLatLng());
		}else if(overlays[i].overlayType=='polygon'){
			all_points.push(overlays[i].overlayObj.getLatLngs());
		}
	}
	if(all_points.length==1){
		if(zoomNum != undefined){
			setCenterAndZoom(all_points[0],zoomNum);
		}else{
			positionPoint(all_points[0]);
		}
	}else{
		map.fitBounds(all_points);
		if(zoomNum != undefined){
			map.setZoom(zoomNum);
		}
	}
}

//定位指定的覆盖物
function positionPointOverlay(overlay){
	if(overlay.overlayType=='marker'){
		positionPoint(overlay.overlayObj.getLatLng());
	}else if(overlay.overlayType=='polygon'){
		map.fitBounds(overlay.overlayObj.getLatLngs());
	}
}
