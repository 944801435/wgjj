/**
 * 包含线形覆盖物的方法
 */
//画线
function drawLine(jwdWgs84,k,borderColor,borderOpacity){
	var coordinatesArray=[];
	var jwdWgs84s = jwdWgs84.split("|");
	for(var i=0;i<jwdWgs84s.length;i++){
		coordinatesArray.push(wgs84_to_gcj02(jwdWgs84s[i].split(",")[0],jwdWgs84s[i].split(",")[1]));
	}
	if(borderColor == undefined){
		borderColor = "#11539E";			
	}
	if(borderOpacity == undefined){
		borderOpacity = 1;
	}
	var options = {
		stroke:true,//是否显示边线
		opacity:borderOpacity,//边线的透明度
		color: borderColor,//边线的颜色
		weight:k//边线的宽度
	};
	var polyline = L.polyline(coordinatesArray, options).addTo(map);
	var polygonObj = {'overlayType':'polygon','overlayObj':polyline};
    overlayArray.push(polygonObj);
	return polygonObj;
}

//更新线的经纬度
function updateLineJwd(overlayObj,jwdWgs84){
	var coordinatesArray=[];
	var jwdWgs84s = jwdWgs84.split("|");
	for(var i=0;i<jwdWgs84s.length;i++){
		coordinatesArray.push(wgs84_to_gcj02(jwdWgs84s[i].split(",")[0],jwdWgs84s[i].split(",")[1]));
	}
	overlayObj.overlayObj.setLatLngs(coordinatesArray);
}

// 画虚线
function drawDashLine(jwdWgs84,k,borderColor,borderOpacity){
	var coordinatesArray=[];
	var jwdWgs84s = jwdWgs84.split("|");
	for(var i=0;i<jwdWgs84s.length;i++){
		coordinatesArray.push(wgs84_to_gcj02(jwdWgs84s[i].split(",")[0],jwdWgs84s[i].split(",")[1]));
	}
	if(!borderColor){
		borderColor = "#11539E";			
	}
	if(!borderOpacity){
		borderOpacity = 1;
	}
	var options = {
		stroke:true,//是否显示边线
		opacity:borderOpacity,//边线的透明度
		color: borderColor,//边线的颜色
		weight:k,//边线的宽度
		className: "dashLines"
	};
	var polyline = L.polyline(coordinatesArray, options).addTo(map);
	var polygonObj = {'overlayType':'polygon','overlayObj':polyline};
    overlayArray.push(polygonObj);
	return polygonObj;
}
