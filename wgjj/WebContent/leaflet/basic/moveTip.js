var markerW=40;
var label_H=40; 
//连接线
var markerLine=new Map();
//标签方向
var labelDir=new Map();
//生成周围偏移数组
var titlePoint_array=[];
titlePoint_array.push(['right',L.point(markerW/2,0)]);  //0
titlePoint_array.push(['right',L.point(markerW/2,14)]);  //1
titlePoint_array.push(['bottom',L.point(0,14)]);  //2
titlePoint_array.push(['left',L.point(0-markerW/2,14)]);   //3
titlePoint_array.push(['left',L.point(0-markerW/2,0)]);   //4
titlePoint_array.push(['left',L.point(0-markerW/2,-14)]);  //5
titlePoint_array.push(['top',L.point(0,-14)]);   //6
titlePoint_array.push(['right',L.point(markerW/2,-14)]);   //7

//移除指定的点的标牌连线
function delPointMarkerTipLine(marker){
	if(markerLine.get(marker)!=null){
		map.removeLayer(markerLine.get(marker));
		markerLine.delete(marker);
	}
}

function moveTip(marker,index,tipLabel,tipClassName){
	//console.info("moveTip");
	
	//当前标签存在时判断其相交情况
	var obj=null;
	var overlap=false;
	var compare=true;
	var lblDirIdx=0;
	if(marker.getTooltip()!=null){
		//console.info(index+":"+$(marker.getTooltip()._container).html());
		
		obj=marker.getTooltip()._container;
		overlap=false;
		compare=true;
		$(obj).siblings().each(
			function(){
				if(!compare)
					return;
				
				if(isRectOverlap($(this),$(obj))){
					overlap=true;
					compare=false;
				}
			}
		);
		if(!overlap){
			//console.info("使用原标签");
			lblDirIdx=labelDir.get(marker);
		}else{
			//console.info("删除原标签");
			//删除原标签
			tipLabel=$(obj).html();
			tipClassName=$(obj).attr('class');
			$(obj).remove();
			//移除tip时移除连线
			if(markerLine.get(marker)!=null){
				map.removeLayer(markerLine.get(marker));
				markerLine.delete(marker);
			}
			//移除label方向
			if(labelDir.get(marker)!=null){
				//console.info("移除labelDir:"+$(marker.getTooltip()._container).html());
				labelDir.delete(marker);
			}
		}
	}
	
	if(overlap){
		//不使用原标签
		for (var i = 0; i < titlePoint_array.length; i++){
			//console.info("i="+i);
			
			//逐个方案判断与其它标牌的相交情况
			marker.bindTooltip(tipLabel,{direction:titlePoint_array[i][0],offset:titlePoint_array[i][1],permanent:true,className:tipClassName});
			
			//如果已经是最后一个方案，只能采用
			obj=marker.getTooltip()._container;
			overlap=false;
			compare=true;
			$(obj).siblings().each(
				function(){
					if(!compare)
						return;
					
					if(isRectOverlap($(this),$(obj))){
						$(obj).remove();
						overlap=true;
						compare=false;
					}
				}
			);
			
			if(!overlap){
				//采用此方案
				break;
			}
		}
		if(i==8) {
			//i=i-1;
			//获得0-7随机的一个数值
			i = parseInt(Math.random()*(7-0+1)+0,10);
			
			marker.bindTooltip(tipLabel,{direction:titlePoint_array[i][0],offset:titlePoint_array[i][1],permanent:true,className:tipClassName});
		}
		//console.info("采用方案"+i);
		marker.openTooltip();
		lblDirIdx=i;
	}
	//console.info("lblDirIdx="+lblDirIdx);
	
	//线无法复用，每次需要重画
	if(markerLine.get(marker)!=null){
		map.removeLayer(markerLine.get(marker));
		markerLine.delete(marker);
	}
	var point=map.latLngToLayerPoint(marker.getLatLng());
	var x=0;
	var y=0;
	if(titlePoint_array[lblDirIdx][0]=='right'){
		x=6;
	}else if(titlePoint_array[lblDirIdx][0]=='bottom'){
		y=6;
	}else if(titlePoint_array[lblDirIdx][0]=='left'){
		x=-6;
	}else if(titlePoint_array[lblDirIdx][0]=='top'){
		y=-6;
	}
	var	aX=point.x+titlePoint_array[lblDirIdx][1].x+x;
	var	aY=point.y+titlePoint_array[lblDirIdx][1].y+y;
	var latLng=map.layerPointToLatLng(L.point(aX,aY));
	var color=$(marker._icon).find('img:first').css('background-color');
	var polyline=L.polyline([[marker.getLatLng().lat,marker.getLatLng().lng],[latLng.lat,latLng.lng]], {color: color,weight:1}).addTo(map);
	markerLine.set(marker,polyline);
	labelDir.set(marker, lblDirIdx);
}

function isRectOverlap(test1,test2){
	var	offsetOne = test1.offset(); 
	var	aX=offsetOne.left;
	var	aY=offsetOne.top;
	var	aW = test1.outerWidth();
	var	aH = label_H;
	var r1 = {
		left:aX,
		top:aY,
		right:aX+aW,
		bottom:aY+aH
	};
	var	offsetTwo = test2.offset(); 
	var	bX=offsetTwo.left;
	var	bY=offsetTwo.top;
	var	bW = test2.outerWidth();
	var	bH = label_H;
	var r2 = {
		left:bX,
		top:bY,
		right:bX+bW,
		bottom:bY+bH
	};
	var flag=!(r1.left >= r2.right || r1.top >= r2.bottom || r2.left >= r1.right || r2.top >= r1.bottom);
	return flag;
}