(function (factory, window) {
  // define an AMD module that relies on 'leaflet'
  if (typeof define === 'function' && define.amd) {
    define(['leaflet'], function (L) {
      factory(L, window.toGeoJSON);
    });

    // define a Common JS module that relies on 'leaflet'
  } else if (typeof exports === 'object') {
    module.exports = function (L) {
      if (L === undefined) {
        if (typeof window !== 'undefined') {
          L = require('leaflet'); // eslint-disable-line import/no-unresolved
        }
      }
      factory(L);
      return L;
    };
  } else if (typeof window !== 'undefined' && window.L) {
    factory(window.L);
  }
}(function (L) {
  L.Polyline.Measure = L.Draw.Polyline.extend({
    addHooks: function () {
      L.Draw.Polyline.prototype.addHooks.call(this);
      if (this._map) {
        this._markerGroup = new L.LayerGroup();
        this._map.addLayer(this._markerGroup);

        this._markers = [];
        this._map.on('click', this._onClick, this);
        this._startShape();
      }
    },

    removeHooks: function () {
      L.Draw.Polyline.prototype.removeHooks.call(this);

      this._clearHideErrorTimeout();

      // !\ Still useful when control is disabled before any drawing (refactor
		// needed?)
      this._map
        .off('pointermove', this._onMouseMove, this)
        .off('mousemove', this._onMouseMove, this)
        .off('click', this._onClick, this);

      this._clearGuides();
      this._container.style.cursor = '';

      this._removeShape();
    },
    _startShape: function () {
      this._drawing = true;
      this._poly = new L.Polyline([], this.options.shapeOptions);
     
      // this is added as a placeholder, if leaflet doesn't recieve
      // this when the tool is turned off all onclick events are removed
      this._poly._onClick = function () {};

      this._container.style.cursor = 'crosshair';

      this._updateTooltip();
      this._map
        .on('pointermove', this._onMouseMove, this)
        .on('mousemove', this._onMouseMove, this);
    },

    _finishShape: function () {
      this._drawing = false;

      this._cleanUpShape();
      this._clearGuides();

      this._updateTooltip();

      this._map
        .off('pointermove', this._onMouseMove, this)
        .off('mousemove', this._onMouseMove, this);

      this._container.style.cursor = '';
      
    },

    _removeShape: function () {
      if (!this._poly) return;
      this._map.removeLayer(this._poly);
      delete this._poly;
      this._markers.splice(0);
      this._markerGroup.clearLayers();
    },

    _onClick: function () {
      if (!this._drawing) {
        this._removeShape();
        this._startShape();
      }else{
    	  var markerLength=this._markers.length;
          if(markerLength>1){
        	  var distanceTemp=this._markers[markerLength-1].getLatLng().distanceTo(this._markers[markerLength-2].getLatLng());
        	  distanceTemp=Math.round(distanceTemp/10)/100;
        	  var calAngleTemp=this._calAngle(this._markers[markerLength-1].getLatLng(),this._markers[markerLength-2].getLatLng());
        	  var markerTemp=this._markers[markerLength-1].bindTooltip(
        			    "距离："+distanceTemp+" km  角度："+calAngleTemp+"°",// tooltip的内容
    					{
    						direction:"right",// tooltip在marker右侧显示
    						offset:L.point(12,0),// tooltip相对于marker的偏移量
    						permanent:true  // 是否常显示tooltip
    					}
    			).openTooltip();
          }
      }
    },
    // 计算两点之间的角度
    _calAngle:function(newLatLng,oldLatLng){
    	var lngStep=newLatLng.lng-oldLatLng.lng;
		var latStep=newLatLng.lat-oldLatLng.lat;
		var len=Math.sqrt(lngStep*lngStep + latStep*latStep);
		var angle=Math.acos(latStep/len);
		if(lngStep<0){
			angle=2*Math.PI - angle;
		}
		var returnTemp= Math.round(angle*180/Math.PI);
		if(isNaN(returnTemp)){
			return 0;
		}else{
			return returnTemp;
		}
    },
    _getTooltipText: function () {
      var labelText = L.Draw.Polyline.prototype._getTooltipText.call(this);
      if (!this._drawing) {
        labelText.text = '';
      }
      return labelText;
    },
    //重写
    _getMeasurementString:function(){
    	var t,e=this._currentLatLng,i=this._markers[this._markers.length-1].getLatLng();
    	t=L.GeometryUtil.isVersion07x()?i&&e&&e.distanceTo?this._measurementRunningTotal+e.distanceTo(i)*(this.options.factor||1):this._measurementRunningTotal||0:i&&e?this._measurementRunningTotal+this._map.distance(e,i)*(this.options.factor||1):this._measurementRunningTotal||0,L.GeometryUtil.readableDistance(t,this.options.metric,this.options.feet,this.options.nautic,this.options.precision)
        return Math.round(t/10)/100+"km &nbsp;&nbsp;&nbsp;&nbsp; "+this._calAngle(e,i)+"°";
    }
  });

  L.Control.MeasureControl = L.Control.extend({

    statics: {
      TITLE: '测距'
    },
    options: {
      position: 'topleft',
      handler: {}
    },

    toggle: function () {
      if (this.handler.enabled()) {
        this.handler.disable.call(this.handler);
      } else {
        this.handler.enable.call(this.handler);
      }
    },

    onAdd: function (map) {
      var link = null;
      var className = 'leaflet-control-draw';

      this._container = L.DomUtil.create('div', 'leaflet-bar leaflet-control-measure');

      this.handler = new L.Polyline.Measure(map, this.options.handler);

      this.handler.on('enabled', function () {
        this.enabled = true;
        L.DomUtil.addClass(this._container, 'enabled');
      }, this);

      this.handler.on('disabled', function () {
        delete this.enabled;
        L.DomUtil.removeClass(this._container, 'enabled');
      }, this);

      link = L.DomUtil.create('a', className + '-measure', this._container);
      link.href = '#';
      link.title = L.Control.MeasureControl.TITLE;

      L.DomEvent
        .addListener(link, 'click', L.DomEvent.stopPropagation)
        .addListener(link, 'click', L.DomEvent.preventDefault)
        .addListener(link, 'click', this.toggle, this);

      return this._container;
    }
  });


  L.Map.mergeOptions({
    measureControl: false
  });


  L.Map.addInitHook(function () {
    if (this.options.measureControl) {
      this.measureControl = L.Control.measureControl().addTo(this);
    }
  });


  L.Control.measureControl = function (options) {
    return new L.Control.MeasureControl(options);
  };
}, window));
