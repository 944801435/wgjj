//根据空域类型获得空域类型的相对应颜色
function getSpaceTypeById(spcType){
	for(var i=0;i<infoSpaceTypeList.length;i++){
		if(infoSpaceTypeList[i].spcType == spcType){
			return infoSpaceTypeList[i];
		}
	}
	return {colorBack: 'red', colorBorder: 'red'};
}

//通过页面上所描述的点和形状获得要画在地图上的多边形的坐标
function getPolygonWgs84(spcLocWgs84Val){
	var spcShape = $("#spcShape").val();
	var polygonWgs84 = "";
	$.ajax({
		url:pageContextPath+"/spaceManageGetPolygonWgs84.action",
		type:"post",
		data:{
			spcShape:spcShape,
			spcLocWgs84:spcLocWgs84Val
		},
		dataType:"json",
		async:false,
		success:function(data){
			if(data.errCode == '1'){
				polygonWgs84 = data.data.toString();
			}else{
				alert(data.errMsg);
			}
		}
	});
	return polygonWgs84;
}

//获得百度坐标拼接好的字符串
function getSpcLocWgs84(){
	var spcLocWgs84 = "";
	//获取形状
	var spcShape = $("#spcShape").val();
	var shapeFlag = "";
	if(spcShape == space_shape_wg){
		//网格
		shapeFlag = "0";
	}else if(spcShape == space_shape_dbx){
		//多边形
		shapeFlag = "2";
	}else if(spcShape == space_shape_yx){
		//圆形
		shapeFlag = "3";
	}else if(spcShape == space_shape_sx){
		//扇形
		shapeFlag = "6";
	}else if(spcShape == space_shape_xhcq){
		//线缓冲区
		shapeFlag = "7";
	}else if(spcShape == space_shape_jczaw){
		//机场障碍物限制面
		shapeFlag = "8";
	}
	var jds=[];
	$("input[name='jd_"+shapeFlag+"']").each(function(index,el){
		jds.push($(el).val()); 
	});
	var wds = [];
	$("input[name='wd_"+shapeFlag+"']").each(function(index,el){
		wds.push($(el).val()); 
	});
	//组装数据：spcLocWgs84
	if(spcShape == space_shape_wg){ //0：多边形，网格1编号|网格2编号|网格3编号…网格N编号
		for(var i=0;i<selGrids.length;i++){
			spcLocWgs84 += (selGrids[i] + "|") ;
		}
	}else if(spcShape == space_shape_dbx){ //2：多边形j,w|j,w|j,w
		for(var i=0;i<jds.length;i++){
			spcLocWgs84 += (jds[i] + "," + wds[i] + "|") ;
		}
	}else if(spcShape == space_shape_yx){ //3：圆形j,w,r
		var radius = $("#radius_3").val();
		spcLocWgs84 = (jds[0] + "," + wds[0] + "," + radius +"|") ;
	}else if(spcShape == space_shape_sx){ //6：扇形j,w|r|nStartA|nEndA
		var radius = $("#radius_6").val();
		var nStartA = $("#nStartA_6").val();
		var nEndA = $("#nEndA_6").val();
		spcLocWgs84 = (jds[0] + "," + wds[0] + "|" + radius +"|" + nStartA + "|" + nEndA + "|") ;
	}else if(spcShape == space_shape_xhcq){ //7：线缓冲区j,w,k|j,w,k|j,w,k|j,w,k
		var width = $("#width_7").val();
		for(var i=0;i<jds.length;i++){
			spcLocWgs84 += (jds[i] + "," + wds[i] + "," +width +"|") ;
		}
	}else if(spcShape == space_shape_jczaw){ //8：机场障碍物限制面j,w|j,w|j,w|rC2B2|j,w|j,w|rB3C3|j,w|j,w|j,w|j,w|rC4B4|j,w|j,w|rB1C1|j,w
		var a1_jd = $("#a1_jd").val();
		var a1_wd = $("#a1_wd").val();
		var a2_jd = $("#a2_jd").val();
		var a2_wd = $("#a2_wd").val();
		var c2_jd = $("#c2_jd").val();
		var c2_wd = $("#c2_wd").val();
		var rC2B2 = $("#rC2B2").val();
		var b2_jd = $("#b2_jd").val();
		var b2_wd = $("#b2_wd").val();
		var b3_jd = $("#b3_jd").val();
		var b3_wd = $("#b3_wd").val();
		var rB3C3 = $("#rB3C3").val();
		var c3_jd = $("#c3_jd").val();
		var c3_wd = $("#c3_wd").val();
		var a3_jd = $("#a3_jd").val();
		var a3_wd = $("#a3_wd").val();
		var a4_jd = $("#a4_jd").val();
		var a4_wd = $("#a4_wd").val();
		var c4_jd = $("#c4_jd").val();
		var c4_wd = $("#c4_wd").val();
		var rC4B4 = $("#rC4B4").val();
		var b4_jd = $("#b4_jd").val();
		var b4_wd = $("#b4_wd").val();
		var b1_jd = $("#b1_jd").val();
		var b1_wd = $("#b1_wd").val();
		var rB1C1 = $("#rB1C1").val();
		var c1_jd = $("#c1_jd").val();
		var c1_wd = $("#c1_wd").val();
		spcLocWgs84 = a1_jd+","+a1_wd+"|"+a2_jd+","+a2_wd+"|"+c2_jd+","+c2_wd+"|"+rC2B2+"|"+b2_jd+","+b2_wd+"|"+b3_jd+","+b3_wd+"|"+rB3C3+"|"+c3_jd+","+c3_wd+"|"+a3_jd+","+a3_wd+"|"+a4_jd+","+a4_wd+"|"+c4_jd+","+c4_wd+"|"+rC4B4+"|"+b4_jd+","+b4_wd+"|"+b1_jd+","+b1_wd+"|"+rB1C1+"|"+c1_jd+","+c1_wd+"|";
	}
	spcLocWgs84 = spcLocWgs84.substr(0, spcLocWgs84.length-1);
	return spcLocWgs84;
}

//设置Wgs84坐标到相应字段
function setSpcLocation(){
	//获取形状
	var spcShape = $("#spcShape").val();
	var shapeFlag = "";
	if(spcShape == space_shape_wg){
		//网格
		shapeFlag = "0";
	}else if(spcShape == space_shape_dbx){
		//多边形
		shapeFlag = "2";
	}else if(spcShape == space_shape_yx){
		//圆形
		shapeFlag = "3";
	}else if(spcShape == space_shape_sx){
		//扇形
		shapeFlag = "6";
	}else if(spcShape == space_shape_xhcq){
		//线缓冲区
		shapeFlag = "7";
	}else if(spcShape == space_shape_jczaw){
		//机场障碍物限制面
		shapeFlag = "8";
	}
	var myForm=document.getElementById("form_shape_"+shapeFlag);
	if(!Validator.Validate(myForm,3)){
		return false;
	}
	var spcLocWgs84Val = getSpcLocWgs84();
	$("#spcLocWgs84").val(spcLocWgs84Val);
	return true;
}

var selectJwdTr = null;//要选点的tr

$(function(){
	//添加经纬度：多边形和线缓冲区会用到该方法
	$(".addJwd").click(function(){
		var table = $(this.parentNode.parentNode.parentNode.parentNode).find("table");
		var trLength = table.find("tr").length;
		//获取形状
		var spcShape = $("#spcShape").val();
		var shapeFlag = "";
		if(spcShape == space_shape_wg){
			//网格
			shapeFlag = "0";
		}else if(spcShape == space_shape_dbx){
			//多边形
			shapeFlag = "2";
		}else if(spcShape == space_shape_yx){
			//圆形
			shapeFlag = "3";
		}else if(spcShape == space_shape_sx){
			//扇形
			shapeFlag = "6";
		}else if(spcShape == space_shape_xhcq){
			//线缓冲区
			shapeFlag = "7";
		}else if(spcShape == space_shape_jczaw){
			//机场障碍物限制面
			shapeFlag = "8";
		}
		var addTrStr = getAddTrStr("","",shapeFlag);
		table.append(addTrStr);
		var myForm=document.getElementById("form_shape_"+shapeFlag);
		Validator.Validate_onLoad(myForm,3);
	});
	//iframe加载完成后执行的方法
	$("#mapIframe").load(function(){ 
		//当机构的地图范围不为空时，设置地图的默认显示范围
		function setMapCenter(){
			mapIframe.positionCity();//根据所在城市定位地图的中心
		}
		
		//如果是新增模式的话
		if(opt=='add'){
			 setMapCenter();
		}else if(opt=='view'){ //如果是查看模式的话
			var viewPolygonWgs84 = $("#polygonWgs84").val();
			if(viewPolygonWgs84){
				//在地图上展示
				//mapIframe.drawOverlay(viewPolygonWgs84);//在地图上画出多边形
				var infoSpaceTypeId = $("#spcTypeId").val() || $("#spcType").val();
				var infoSpaceTypeObj = getSpaceTypeById(infoSpaceTypeId);
				mapIframe.drawCustomColorPolygonNoClear(viewPolygonWgs84,infoSpaceTypeObj.colorBack,infoSpaceTypeObj.colorBorder);
				mapIframe.positionSpace();//根据所画多边形的位置设置地图的中心和缩放比例
			}
		}else if(opt=='update'){//如果是编辑模式的话
			var updatePolygonWgs84 = $("#polygonWgs84").val();
			var updateSpcShape = $("#spcShape").val();
			var updateSpcLocation = $("#spcLocWgs84").val();
			var shapeFlag = "";
			if(spcShape == space_shape_wg){
				//网格
				shapeFlag = "0";
			}else if(spcShape == space_shape_dbx){
				//多边形
				shapeFlag = "2";
			}else if(spcShape == space_shape_yx){
				//圆形
				shapeFlag = "3";
			}else if(spcShape == space_shape_sx){
				//扇形
				shapeFlag = "6";
			}else if(spcShape == space_shape_xhcq){
				//线缓冲区
				shapeFlag = "7";
			}else if(spcShape == space_shape_jczaw){
				//机场障碍物限制面
				shapeFlag = "8";
			}
			$("#div_shape_"+shapeFlag).show();//展示相关形状的输入框

			if(updatePolygonWgs84){
				//在地图上展示
				//mapIframe.drawOverlay(updatePolygonWgs84);//在地图上画出多边形
				var infoSpaceTypeId = $("#spcTypeId").val() || $("#spcType").val();
				var infoSpaceTypeObj = getSpaceTypeById(infoSpaceTypeId);
				mapIframe.drawCustomColorPolygonNoClear(updatePolygonWgs84,infoSpaceTypeObj.colorBack,infoSpaceTypeObj.colorBorder);
				mapIframe.positionSpace();//根据所画多边形的位置设置地图的中心和缩放比例
			}
			
			//解析数据：spcLocWgs84
			if(updateSpcShape == space_shape_dbx){ //2：多边形j,w|j,w|j,w
				var updateSpcLocations = updateSpcLocation.split("|");
				var table_shape_2 = $("#table_shape_2");
				for(var i=0;i<updateSpcLocations.length;i++){
					var jw = updateSpcLocations[i].split(",");
					if(i<3){
						table_shape_2.find("tr").eq(i).find("input").eq(0).val(jw[0]);
						table_shape_2.find("tr").eq(i).find("input").eq(1).val(jw[1]);
					}else{
						var addTrStr = getAddTrStr(jw[0],jw[1],shapeFlag);
						table_shape_2.append(addTrStr);
					}
					mapIframe.drawMarker(jw[0],jw[1]);
				}
				if(updateSpcLocations.length>3){
					var myForm=document.getElementById("form_shape_2");
					Validator.Validate_onLoad(myForm,3);
				}
			}else if(updateSpcShape == space_shape_yx){ //3：圆形j,w,r
				var updateSpcLocations = updateSpcLocation.split(",");
				$("#radius_3").val(updateSpcLocations[2]);
				var table_shape_3 = $("#table_shape_3");
				table_shape_3.find("tr").eq(0).find("input").eq(0).val(updateSpcLocations[0]);
				table_shape_3.find("tr").eq(0).find("input").eq(1).val(updateSpcLocations[1]);
				mapIframe.drawMarker(updateSpcLocations[0],updateSpcLocations[1]);
			}else if(updateSpcShape == space_shape_sx){ //6：扇形j,w|r|nStartA|nEndA
				var updateSpcLocations = updateSpcLocation.split("|");
				$("#radius_6").val(updateSpcLocations[1]);
				$("#nStartA_6").val(updateSpcLocations[2]);
				$("#nEndA_6").val(updateSpcLocations[3]);
				var jw = updateSpcLocations[0].split(",");
				var table_shape_6 = $("#table_shape_6");
				table_shape_6.find("tr").eq(0).find("input").eq(0).val(jw[0]);
				table_shape_6.find("tr").eq(0).find("input").eq(1).val(jw[1]);
				mapIframe.drawMarker(jw[0],jw[1]);
			}else if(updateSpcShape == space_shape_xhcq){ //7：线缓冲区j,w,k|j,w,k|j,w,k|j,w,k
				var updateSpcLocations = updateSpcLocation.split("|");
				$("#width_7").val(updateSpcLocations[0].split(",")[2]);
				var table_shape_7 = $("#table_shape_7");
				for(var i=0;i<updateSpcLocations.length;i++){
					var jw = updateSpcLocations[i].split(",");
					if(i<2){
						table_shape_7.find("tr").eq((i+1)).find("input").eq(0).val(jw[0]);
						table_shape_7.find("tr").eq((i+1)).find("input").eq(1).val(jw[1]);
					}else{
						var addTrStr = getAddTrStr(jw[0],jw[1],shapeFlag);
						table_shape_7.append(addTrStr);
					}
					mapIframe.drawMarker(jw[0],jw[1]);
				}
				if(updateSpcLocations.length>2){
					var myForm=document.getElementById("form_shape_7");
					Validator.Validate_onLoad(myForm,3);
				}
			}else if(updateSpcShape == space_shape_jczaw){ //8：机场障碍物限制面j,w|j,w|j,w|rC2B2|j,w|j,w|rB3C3|j,w|j,w|j,w|j,w|rC4B4|j,w|j,w|rB1C1|j,w
				var updateSpcLocations = updateSpcLocation.split("|");
				$("#a1_jd").val(updateSpcLocations[0].split(",")[0]);
				$("#a1_wd").val(updateSpcLocations[0].split(",")[1]);
				mapIframe.drawMarker($("#a1_jd").val(),$("#a1_wd").val());
				$("#a2_jd").val(updateSpcLocations[1].split(",")[0]);
				$("#a2_wd").val(updateSpcLocations[1].split(",")[1]);
				mapIframe.drawMarker($("#a2_jd").val(),$("#a2_wd").val());
				$("#c2_jd").val(updateSpcLocations[2].split(",")[0]);
				$("#c2_wd").val(updateSpcLocations[2].split(",")[1]);
				mapIframe.drawMarker($("#c2_jd").val(),$("#c2_wd").val());
				$("#rC2B2").val(updateSpcLocations[3]);
				$("#b2_jd").val(updateSpcLocations[4].split(",")[0]);
				$("#b2_wd").val(updateSpcLocations[4].split(",")[1]);
				mapIframe.drawMarker($("#b2_jd").val(),$("#b2_wd").val());
				$("#b3_jd").val(updateSpcLocations[5].split(",")[0]);
				$("#b3_wd").val(updateSpcLocations[5].split(",")[1]);
				mapIframe.drawMarker($("#b3_jd").val(),$("#b3_wd").val());
				$("#rB3C3").val(updateSpcLocations[6]);
				$("#c3_jd").val(updateSpcLocations[7].split(",")[0]);
				$("#c3_wd").val(updateSpcLocations[7].split(",")[1]);
				mapIframe.drawMarker($("#c3_jd").val(),$("#c3_wd").val());
				$("#a3_jd").val(updateSpcLocations[8].split(",")[0]);
				$("#a3_wd").val(updateSpcLocations[8].split(",")[1]);
				mapIframe.drawMarker($("#a3_jd").val(),$("#a3_wd").val());
				$("#a4_jd").val(updateSpcLocations[9].split(",")[0]);
				$("#a4_wd").val(updateSpcLocations[9].split(",")[1]);
				mapIframe.drawMarker($("#a4_jd").val(),$("#a4_wd").val());
				$("#c4_jd").val(updateSpcLocations[10].split(",")[0]);
				$("#c4_wd").val(updateSpcLocations[10].split(",")[1]);
				mapIframe.drawMarker($("#c4_jd").val(),$("#c4_wd").val());
				$("#rC4B4").val(updateSpcLocations[11]);
				$("#b4_jd").val(updateSpcLocations[12].split(",")[0]);
				$("#b4_wd").val(updateSpcLocations[12].split(",")[1]);
				mapIframe.drawMarker($("#b4_jd").val(),$("#b4_wd").val());
				$("#b1_jd").val(updateSpcLocations[13].split(",")[0]);
				$("#b1_wd").val(updateSpcLocations[13].split(",")[1]);
				mapIframe.drawMarker($("#b1_jd").val(),$("#b1_wd").val());
				$("#rB1C1").val(updateSpcLocations[14]);
				$("#c1_jd").val(updateSpcLocations[15].split(",")[0]);
				$("#c1_wd").val(updateSpcLocations[15].split(",")[1]);
				mapIframe.drawMarker($("#c1_jd").val(),$("#c1_wd").val());
			}
		}else {
			 setMapCenter();//默认根据机构定位，如果机构不存在就直接定位海南省
		}
    }); 
});