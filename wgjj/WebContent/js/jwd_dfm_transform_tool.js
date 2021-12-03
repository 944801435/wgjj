//地理坐标系转换成度分秒
function jwd_to_dfm(jwd){
	if(jwd=='' || jwd.length==0){
		return "";
	}
	jwd = parseFloat(jwd);
	var du = parseInt(jwd);
	var fm = jwd - du;
	var ff = fm * 60;
	var fen = parseInt(ff);
	var m = (ff - fen) * 60;
	var miao = parseInt(m);
	var dfm = du + "°" + fen + "′" + miao + "″";
	return dfm;
}

//度分秒转换成地理坐标系
function dfm_to_jwd(dfm){
	if(dfm=='' || dfm.length==0){
		return '';
	}
	var du = parseInt(dfm.split("°")[0]);
	var fen = parseFloat(dfm.split("°")[1].split("′")[0]);
	var miao = parseFloat(dfm.split("°")[1].split("′")[1].split("″")[0]);
	var mf = miao / 60;
	var ff = fen + mf;
	var fm = ff / 60;
	var jwd = du+ fm;
	return jwd;
}