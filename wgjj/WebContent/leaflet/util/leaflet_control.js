/**
 * 包含所有控件control的方法
 */
var scaleControl;//地图比例尺控件
var navigationControl;//地图缩放控件
var measureControl;//测量控件
var mapTypeControl;//地图类型控件

//测量控件
function addMeasureControl(){
    measureControl = L.Control.measureControl().setPosition('bottomright');
    measureControl.addTo(map);
}

//添加比例尺控件
function addScaleControl(){
	scaleControl = L.control.scale({
		metric:true,//是否显示(m/km)
		imperial:false//是否显示(mi/ft)
	}).setPosition('bottomleft');
	scaleControl.addTo(map);
}

//添加缩放控件
function addNavigationControl(){
    navigationControl = L.control.zoom({  
        zoomInTitle: '放大',  
        zoomOutTitle: '缩小'  
    }).setPosition('topright');
    navigationControl.addTo(map);
}

//添加地图类型控件
function addMapTypeControl(){
	var anchor = 'bottomright';
	MapTypeControl = L.Control.extend({
		options: {
			position: anchor //初始位置
		},
		onAdd: function (map) {
			this._container = L.DomUtil.create('div');
			this._container.id = 'dtOrWx';
			this._container.className = 'leaflet-control-maptype leaflet-bar';
			var mhDiv = document.createElement('div');
			mhDiv.id = 'dt';
			mhDiv.innerHTML = '地图';
			mhDiv.className = 'mhDiv';
			this._mhDiv = mhDiv;
			
			var jqDiv = document.createElement('div');
			jqDiv.id = 'wx';
			jqDiv.innerHTML = '卫星';
			jqDiv.className = 'jqDiv';
			this._jqDiv = jqDiv;
			
			this._container.appendChild(this._mhDiv);
			this._container.appendChild(this._jqDiv);
			//注册关闭事件
			L.DomEvent.addListener(this._mhDiv, 'click', this._onClickMhDivControl, this);
			L.DomEvent.addListener(this._jqDiv, 'click', this._onClickJqDivControl, this);
			
			return this._container;
		},
		_onClickMhDivControl: function () {
			$('#dt').css({'background':'#1462C8','font-weight':'normal','color':'#fff'});
			$('#wx').css({'background':'rgb(255, 255, 255)','font-weight':'bold','color':'rgb(0, 0, 0)'});
			showDtLayer();
		},
		_onClickJqDivControl: function () {
			$('#dt').css({'background':'rgb(255, 255, 255)','font-weight':'bold','color':'rgb(0, 0, 0)'});
			$('#wx').css({'background':'#1462C8','font-weight':'normal','color':'#fff'});
			showWxLayer();
		}
	});
	// 创建控件
	mapTypeControl = new MapTypeControl();
	// 添加到地图当中
	map.addControl(mapTypeControl);
	return mapTypeControl;
}

//显示普通地图图层
function showDtLayer(){
	map.removeLayer(JXWYX_layer);
	map.addLayer(BJSL_layer);
}

//显示卫星地图图层
function showWxLayer(){
	map.removeLayer(BJSL_layer);
	map.addLayer(JXWYX_layer);
}

//获得地图比例尺控件
function getScaleControl(){
	return scaleControl;
}

//获得地图比例尺控件的class名称
function getScaleControlClass(){
	return "leaflet-control-scale";
}

//获得地图缩放控件
function getNavigationControl(){
	return navigationControl;
}

//获得地图缩放控件的class名称
function getNavigationControlClass(){
	return "leaflet-control-zoom";
}

//获得地图测量控件
function getMeasureControl(){
	return measureControl;
}

//获得地图测量控件的class名称
function getMeasureControlClass(){
	return "leaflet-control-measure";
}

//获得地图类型控件
function getMaptypeControl(){
	return mapTypeControl;
}

//获得地图类型控件的class名称
function getMaptypeControlClass(){
	return "leaflet-control-maptype";
}

//去掉地图控件
function removeMapControl(control){
	map.removeControl(control);
}

//设置控件位置
function setControlPosition(pointControl,position){
	pointControl.setPosition(position);
}

//设置控件样式
function setControlOptions(pointControl,options){
	if(pointControl!=null){
		if(options.anchor && (pointControl == scaleControl || pointControl == navigationControl)){
			pointControl.setPosition(options.anchor);
		}
	}
}

//=======================线以下暂时无用=================================================================================
//添加精确和模糊自定义控件
function addGeoControl(callbackFun,options){
	var anchor = 'topright';
	if(options){
		if(options.anchor){
			anchor = options.anchor;				
		}
	}
	GeoControl = L.Control.extend({
		options: {
			position: anchor //初始位置
		},
		initialize: function (options) {
			L.Util.extend(this.options, options);
		},
		onAdd: function (map) {
			this._container = L.DomUtil.create('div');
			this._container.id = 'jqOrMhDiv';
			this._container.className = 'jqOrMhDiv';
			var mhDiv = document.createElement('div');
			mhDiv.id = 'mh';
			mhDiv.innerHTML = '网格';
			mhDiv.className = 'mhDiv';
			this._mhDiv = mhDiv;
			
			var jqDiv = document.createElement('div');
			jqDiv.id = 'jq';
			jqDiv.innerHTML = '精确';
			jqDiv.className = 'jqDiv';
			this._jqDiv = jqDiv;
			
			this._container.appendChild(this._mhDiv);
			this._container.appendChild(this._jqDiv);
			//注册关闭事件
			L.DomEvent.addListener(this._mhDiv, 'click', this._onClickMhDivControl, this);
			L.DomEvent.addListener(this._jqDiv, 'click', this._onClickJqDivControl, this);
			
			return this._container;
		},
		_onClickMhDivControl: function () {
			$('#mh').css({'background':'#1462C8','font-weight':'bold','color':'#fff'});
			$('#jq').css({'background':'rgb(255, 255, 255)','font-weight':'normal','color':'rgb(0, 0, 0)'});
			callbackFun.apply(this,['mh']);
		},
		_onClickJqDivControl: function () {
			$('#jq').css({'background':'#1462C8','font-weight':'bold','color':'#fff'});
			$('#mh').css({'background':'rgb(255, 255, 255)','font-weight':'normal','color':'rgb(0, 0, 0)'});
			callbackFun.apply(this,['jq']);
		}
	});
	// 创建控件
	var myGeoCtrl = new GeoControl();
	// 添加到地图当中
	map.addControl(myGeoCtrl);
	return myGeoCtrl;
}