// 地图组件
var mapPageTemp = `
	<div>
		<iframe src="${ctx }/leaflet/leaflet_map.jsp" name="mapIframe" id="mapIframe" width="100%" height="100%" frameborder="0" ></iframe>
		
	</div>
`;
Vue.component('view-map',{
	template:mapPageTemp,
    props: {
    },
    data() {
      return {
      }
    },
    computed: {
    },
    methods: {
    },
    mounted(){
    }
});

//画自定义航路
function drawFlightLine(wayList, color){
	var overlays = [];
	var jwdWgs84 = wayList.map(function(item){
		var jd = item.lonx;
		var wd = item.laty;
		var obj = {
			spanHtml: `<div style="border-radius:5px;width:5px;height:5px;background:${color}"></div>`
		};
		var markerData = {
			markerIconWidth: 5,
			markerIconHeight: 5,
			showInMapZoom: 1
		};
		overlays.push(mapIframe.drawSpanMarker(jd,wd,obj,markerData));
		return jd+","+wd;
	}).join("|");
	overlays.push(mapIframe.drawLine(jwdWgs84, "1" , color, 1));
	return overlays;
}

// 移除指定内容
function removeOverlay(overlays){
	if(Array.isArray(overlays)){
		mapIframe.clearPointOverlays(overlays);
	}else{
		mapIframe.clearPointOverlay(overlays);
	}
}

// 定位指定内容
function positionOverlay(overlays){
	if(Array.isArray(overlays)){
		mapIframe.positionPointOverlayArray(overlays);
	}else{
		mapIframe.positionPointOverlay(overlays);
	}
}