package com.uav.base.util;

import java.util.List;

import com.uav.web.view.uavOnline.base.LngLatPoint;

public class MapUtil {
	
	//格式30.4555, 114.1121
	public static double convertToDecimal(double duFenMiao)
	{	
		//度
		double d0 = (int)duFenMiao;
		//小数部分
		double  d12= duFenMiao - (int)duFenMiao;
		//分
		double d1 =(int) (d12*100);
		//秒
		double d2 = d12*10000 - d1*100;
		
		return MapUtil.convertToDecimal(d0,d1,d2);
	}
	
	// 经纬度度分秒转换为小数
	public static double convertToDecimal(double du, double fen, double miao) {
		if (du < 0) {
			return -(Math.abs(du) + (Math.abs(fen) + (Math.abs(miao) / 60)) / 60);
		}
		return Math.abs(du) + (Math.abs(fen) + (Math.abs(miao) / 60)) / 60;
	}
	
	//获取多边形中心点，还可以使用平均值算法x=x1+x2+x3+...+xn/n;
	public static double[] getCenterOfPolygon(List<LngLatPoint> polygon) {
		double X = 0;
		double Y = 0;
		double Z = 0;
		for (LngLatPoint point : polygon) {
			double lat1 = point.getLat();
			double lon1 = point.getLng();
			lat1 = lat1 * Math.PI / 180;
			lon1 = lon1 * Math.PI / 180;
			X += Math.cos(lat1) * Math.cos(lon1);
			Y += Math.cos(lat1) * Math.sin(lon1);
			Z += Math.sin(lat1);
		}

		double Lon = Math.atan2(Y, X);
		double Hyp = Math.sqrt(X * X + Y * Y);
		double Lat = Math.atan2(Z, Hyp);
		Lat = Lat * 180 / Math.PI;
		Lon = Lon * 180 / Math.PI;
		return new double[] { Lat, Lon };
	}

}
