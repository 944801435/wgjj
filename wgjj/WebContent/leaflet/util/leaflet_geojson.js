//画北京各区的行政划分线路
function drawBeijingRangeLine(){
	var layer = L.geoJSON(beijing_geojson_data, {
	    style: function (feature) {
	    	var options = {
    			//color: feature.properties.color,
	    		color:"#0D2A49",
    			weight:3,
    			fillOpacity:1,
    			fill:false
	    	};
	        return options;
	    }
	}).addTo(map);
	return layer;
}