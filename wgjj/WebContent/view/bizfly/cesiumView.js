// 三维预演
var jsImport=`
<style>
    @import url(`+ctx+`/cesium/basic/widgets.css);
    .cesium-viewer-bottom{
    	display: none;
    }
    .cesium-div{
    	font-size:12px;
    	background: #303336ad;
    	border: 1px solid #444;
    	color: #edffff;
    	fill: #edffff; 
    	border-radius: 4px;
    	width:calc(100% - 6px);
    }
    .cesium-button{
    	font-size:12px;
    	background: #303336;
    	border: 1px solid #444;
    	color: #edffff;
    	fill: #edffff; 
    	border-radius: 4px;
    	padding: 3px 5px;
    }
    #cesium_detialDiv img{
    	width:12px;
    	height:12px;
    }
</style>
<script src="`+ctx+`/cesium/basic/Cesium.js"></script>
<script type="text/javascript" src="`+ctx+`/js/common/mapConfig.js"></script>
<script src="`+ctx+`/cesium/util/CesiumUtil.js" type="text/javascript" ></script>
<script src="`+ctx+`/cesium/util/cesium_base.js" type="text/javascript" ></script>
<script src="`+ctx+`/cesium/util/cesium_line.js" type="text/javascript" ></script>
<script src="`+ctx+`/cesium/util/cesium_point.js" type="text/javascript" ></script>
<script src="`+ctx+`/cesium/util/cesium_polygon.js" type="text/javascript" ></script>
<script src="`+ctx+`/cesium/util/cesium_measureTool.js" type="text/javascript" ></script>
`;
document.write(jsImport);
var cesiumObj={
		pntMap:new Map()                 //航迹点
};

function cesiumView(){
	layer.open({
		  type: 1,
		  maxmin:true,
		  shade:0,
		  title:'三维展现',
		  area: ['60%','80%'],//['800px','500px'], //宽度
		  content: '<div id="cesiumContainer" style="width:100%;height:100%;"></div>',
		  success: function(layero, index){
			 //初始化地图
			 CesiumUtil.init('cesiumContainer');

			 drawGuiji();
			 
			 //初始化测量工具
			 initMeasureTool();
		
		     //加载飞行控制
			 loadClockControll();
		  }
	});
	
}
//加载暂停加速控制
function loadClockControll(){
	var divTemp=document.createElement('div');
	divTemp.style="position: absolute;left: 0px;bottom: 0px;";
	divTemp.className='cesium-button';
	divTemp.innerHTML='暂停';
	divTemp.onclick=function(){
		if(this.innerHTML=='暂停'){
			CesiumUtil.viewer.clock.shouldAnimate = false; 
			this.innerHTML='开始';
		}else{
			CesiumUtil.viewer.clock.shouldAnimate = true; 
			this.innerHTML='暂停';
		}
	}
	$("#cesiumContainer").append(divTemp);
	
	var speedTemp=`
			<div style="position: absolute;left: 40px;bottom: 5px;display:flex;color:#fff;">
				<input id="speedChange" type="range" min="0" max="30" step="1" value="1"/>
				<div id="speedNum">1</div>&nbsp;倍速
			</div>
	`;
	$("#cesiumContainer").append(speedTemp);
	$('#speedChange').on('input propertychange',(el)=>{    //实时事件（获取每个变化的值）
        let num=el.currentTarget.value;
		$('#speedNum').html(num);
		CesiumUtil.viewer.clock.multiplier=num;
    });
}

//测量工具
function initMeasureTool(){
	let htmlTemp=`
		<div style="position:absolute;top: 0px; left: 0px;">
		    <div id="measure"> </div>
		</div>
	`;
	$('#cesiumContainer').append(htmlTemp);
	CesiumUtil.initMeasureTool('measure');
}

//航迹 
function drawGuiji(){
	var pointArray = [];
  	//组装航迹
  	for(let i=0;i<uavAllGuijiArry.length;i++){
 	    var uavGuijiArry=uavAllGuijiArry[i];
		for(var j=0;j<uavGuijiArry.length;j++){
  			var jd = parseFloat(uavGuijiArry[j].split(",")[0]);
	  		var wd = parseFloat(uavGuijiArry[j].split(",")[1]);
	  		var alt = parseFloat(uavGuijiArry[j].split(",")[2]);
	  		pointArray.push([jd,wd,alt,"",""]);
  		}
  	}
    
    if(pointArray.length>0){
    	//绘制航迹点
		pointArray.forEach((item)=>{
			let color='yellow';
	  	    let entity=CesiumUtil.drawPoint(item[0],item[1],item[2],color);
			cesiumObj.pntMap.set(entity._id, item);
		});
	    //绘制航线
	    let polyline=CesiumUtil.drawLine(pointArray,'#ffd700');
	    //无人机飞行模拟
	    CesiumUtil.moveModel(pointArray,ctx+"/cesium/models/Cesium_Air.glb",(text)=>{
		});
	    //定位
	    CesiumUtil.zoomToHPR([polyline],70,-7,findMaxDist(pointArray)/2+3000);
    }
}

//获取最大距离
//[[jd,wd,alt],...]
function findMaxDist(pointArray){
	let maxD=0;
	let end=pointArray[pointArray.length-1];
	for(let i=0;i<pointArray.length-2;i++){
		let cur=pointArray[i];
		let d=CesiumUtil.getDistance(cur[0],cur[1],end[0],end[1]);
		if(maxD<d){
			maxD=d;
		}
	}
//	console.log('maxD:'+maxD);
	return maxD*1000;
}

