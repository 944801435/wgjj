//地理坐标系转换成度分秒
function jwd_to_dfm(jwd,flag){
	if(flag != "LNG" && flag != "LAT"){
		console.info("第二个参数需要标识为LNG或LAT，表示要转换的是经度还是纬度");
		return "";
	}
	jwd = parseFloat(jwd);
	var firstStr = "";
	if(flag=='LNG'){//经度
		if(jwd < 0){
			firstStr = "W";
			jwd = jwd * -1;
		}else{
			firstStr = "E";
		}
	}
	if(flag == 'LAT'){//纬度
		if(jwd < 0){
			firstStr = "S";
			jwd = jwd * -1;
		}else{
			firstStr = "N";
		}
	}
	var du = parseInt(jwd);
	var fm = jwd - du;
	var ff = fm * 60;
	var fen = parseInt(ff);
	var m = (ff - fen) * 60;
	var miao = parseInt(m);
	if(fen < 10){
		fen = "0" + fen;
	}
	if(miao < 10){
		miao = "0" + miao;
	}
	if(flag=='LNG'){//经度
		if(du < 10){
			du = "00" + du;
		}else if(du >= 10 && du < 100){
			du = "0" + du;
		}
	}
	if(flag == 'LAT'){//纬度
		if(du < 10){
			du = "0" + du;
		}
	}
	var dfm = firstStr + du + "°" + fen + "′" + miao + "″";
	return dfm;
}

//度分秒转换成地理坐标系
function dfm_to_jwd(dfm){
	if(du.indexOf("E") == -1 && du.indexOf("N") == -1 && du.indexOf("W") == -1 && du.indexOf("S") == -1){
		console.info("经纬度必须以E、N、W、S其中一个开头，表示要转换的是经度还是纬度并且需要确定是正数还是负数");
		return "";
	}
	var du = dfm.split("°")[0];
	var fen = dfm.split("°")[1].split("′")[0];
	var miao = dfm.split("°")[1].split("′")[1].split("″")[0];
	var firstStr = "";
	if(du.indexOf("E")!=-1 || du.indexOf("N")!=-1){
		du = du.substr(0);
	}
	if(du.indexOf("W")!=-1 || du.indexOf("S")!=-1){
		du = du.substr(0);
		firstStr = "-";
	}
	du = parseInt(du);
	fen = parseInt(fen);
	miao = parseInt(miao);
	var mf = miao / 60;
	var ff = fen + mf;
	var fm = ff / 60;
	var jwd = firstStr + du + "." + fm;
	return jwd;
}