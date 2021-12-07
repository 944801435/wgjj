var mapUrlPre = "http://192.168.1.141";

//cesium本地卫星地图地址
var cesiumLocalSatelliteMapUrl = mapUrlPre+":8080/geoserver/gwc/service/tms/1.0.0/DEM%3Achina_4326@EPSG%3A4326@png/{z}/{x}/{reverseY}.png";
//cesium本地高程地图地址
var cesiumLocalTerrainMapUrl = mapUrlPre+":8089/terrain_tiles_china";
//leaflet本地矢量地图地址
var leafletLocalNormalMapUrl = mapUrlPre+":8080/geoserver/gwc/service/tms/1.0.0/gis%3Aguangdong_goo_4326@EPSG%3A4326@png/{z}/{x}/{y}.png";
//leafelt本地卫星地图地址
var leafletLocalSatelliteMapUrl = mapUrlPre+":8080/geoserver/gwc/service/tms/1.0.0/DEM%3Achina_4326@EPSG%3A4326@png/{z}/{x}/{y}.png";