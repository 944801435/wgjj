/**
 * 包含热力图的方法
 */
//创建热力图
function drawHeatmap(data){
    //传入的data样式：[{"lng":116.418261,"lat":39.921984,"count":50}, {"lng":116.418261,"lat":39.921984,"count":50}]
    //转换data样式：data=[[39.923959, 116.518189, 1],[39.923959, 116.518189, 1]]
    var heatmapData = [];
    var points = [];
    for(var i=0;i<data.length;i++){
    	var obj = data[i];
    	if(obj.count==0){
    		continue;
    	}
    	var heatmapDataObj = [];
		var gcj02 = wgs84_to_gcj02(obj.lng,obj.lat);
    	heatmapDataObj.push(gcj02.lat);
    	heatmapDataObj.push(gcj02.lng);
    	heatmapDataObj.push(obj.count);
    	heatmapData.push(heatmapDataObj);
    	points.push(gcj02);
    }
	var cfg = {
        "radius": 8
    };
	var heat = L.heatLayer(heatmapData,cfg).addTo(map);
	map.panInsideBounds(points);
    var echartsLayerObj = {'overlayType':'heatmap','overlayObj':heat};
    overlayArray.push(echartsLayerObj);
    return echartsLayerObj;
}