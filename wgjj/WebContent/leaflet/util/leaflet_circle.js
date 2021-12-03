//画一个圆形
function drawCircle(jd,wd,r,backColor,borderColor,backOpacity,borderOpacity){
	var style = getCustomColorStyleOptions(backColor,borderColor,backOpacity,borderOpacity);
	style.radius = r;
	var circle = L.circle(wgs84_to_gcj02(jd,wd), style).addTo(map);
	var circleObj = {'overlayType':'polygon','overlayObj':circle};
	overlayArray.push(circleObj);
	return circleObj;
}

//移动圆形
function moveCircle(circleObj,jd,wd){
	circleObj.overlayObj.setLatLng(wgs84_to_gcj02(jd,wd));
}