/**
 * 	可以对所有覆盖物进行操作 
 */
//清空所有覆盖物
function clearOverlay(){
	var i = overlayArray.length-1;
	for(;i>=0;i--){
		if(overlayArray[i].overlayType=='marker'){//点
			//标牌避让   移除连接线
			if(markerLine.get(overlayArray[i].overlayObj)!=null){
			//	markerLine.get(overlayArray[i].overlayObj).remove();
				map.removeLayer(markerLine.get(overlayArray[i].overlayObj));
				markerLine.delete(overlayArray[i].overlayObj);
			}
			//移除Marker
			overlayArray[i].overlayObj.remove();
			overlayArray.splice(i,1);
		}else if(overlayArray[i].overlayType=='polygon'){//多边形
			//移除多边形
		  	map.removeLayer(overlayArray[i].overlayObj);
		  	overlayArray.splice(i,1);
		}else if(overlayArray[i].overlayType=='heatmap'){//热力图
		  	map.removeLayer(overlayArray[i].overlayObj);
			overlayArray.splice(i, 1);
		}
	}
}

//清除多边形覆盖物
function clearPolygonOverlay(){
	var i = overlayArray.length-1;
	for(;i>=0;i--){
		if(overlayArray[i].overlayType=='polygon'){
		  	map.removeLayer(overlayArray[i].overlayObj);
		  	overlayArray.splice(i,1);
		}
	}
}

//添加指定覆盖物
function addPointOverlay(pointOverlay){
	if(pointOverlay!=null){
		pointOverlay.overlayObj.addTo(map);
		overlayArray.push(pointOverlay);
		if(pointOverlay.overlayType=='marker'){
			isShowLabel();
		}else if(pointOverlay.overlayType=='polygon'){
			isShowPolygonLabel();
		}
	}
}

//清除指定覆盖物
function clearPointOverlay(pointOverlay){
	if(pointOverlay!=null){
		var i = overlayArray.length-1;
		for(;i>=0;i--){
			if(overlayArray[i].overlayObj==pointOverlay.overlayObj){
				if(pointOverlay.overlayType=='marker'){
					//标牌避让   移除连接线
					if(markerLine!=null && markerLine.get(overlayArray[i].overlayObj)!=null){
						//markerLine.get(overlayArray[i].overlayObj).remove();
						map.removeLayer(markerLine.get(overlayArray[i].overlayObj));
						markerLine.delete(overlayArray[i].overlayObj);
					}
					//移除Marker
					overlayArray[i].overlayObj.remove();
					overlayArray.splice(i,1);
				}else if(pointOverlay.overlayType=='polygon'){
					//移除多边形
					map.removeLayer(overlayArray[i].overlayObj);
					overlayArray.splice(i,1);
				}else if(pointOverlay.overlayType = 'layerGroup'){
					map.removeLayer(overlayArray[i].overlayObj);
					overlayArray.splice(i,1);
				}
				break;
			}
		}
	}
}
// 移除多个覆盖物
function clearPointOverlays(pointOverlays){
	if(pointOverlays!=null && pointOverlays.length>0){
		for(var k=0;k<pointOverlays.length;k++){
			var pointOverlay = pointOverlays[k];
			if(pointOverlay!=null){
				var i = overlayArray.length-1;
				for(;i>=0;i--){
					if(overlayArray[i].overlayObj==pointOverlay.overlayObj){
						if(pointOverlay.overlayType=='marker'){
							//标牌避让   移除连接线
							if(markerLine!=null && markerLine.get(overlayArray[i].overlayObj)!=null){
								//markerLine.get(overlayArray[i].overlayObj).remove();
								map.removeLayer(markerLine.get(overlayArray[i].overlayObj));
								markerLine.delete(overlayArray[i].overlayObj);
							}
							//移除Marker
							overlayArray[i].overlayObj.remove();
							overlayArray.splice(i,1);
						}else if(pointOverlay.overlayType=='polygon'){
							//移除多边形
							map.removeLayer(overlayArray[i].overlayObj);
							overlayArray.splice(i,1);
						}else if(pointOverlay.overlayType = 'layerGroup'){
							map.removeLayer(overlayArray[i].overlayObj);
							overlayArray.splice(i,1);
						}
						break;
					}
				}
			}
		}
	}
}

//删除指定的layer
function clearPointLayer(layer){
	if(layer!=null){
		map.removeLayer(layer);
	}
}