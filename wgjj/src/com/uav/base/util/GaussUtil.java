package com.uav.base.util;

import com.uav.base.util.spacial.Jwd;
import com.uav.base.util.spacial.Point;

public class GaussUtil {
//	//54年北京坐标系参数
//	private static final double a=6378245.0;
//	private static final double iF=1.0/298.3;
//	//80国家大地坐标系参数
//	private static final double a=6378140.0;
//	private static final double iF=1.0/298.257;
	//WGS84坐标系参数
	private static final double a=6378137.0;
	private static final double iF=1.0/298.257223563;
	
	private static final double iPI=0.0174532925199433;//3.1415926535898/180.0;
	private static final int ZoneWide = 3; //3度带宽
	
	//WGS84经纬度坐标转高斯投影坐标
	public static Point jwdToGauss(double longitude, double latitude) {
		int ProjNo = (int) (longitude / ZoneWide);
		double longitude0 = (ProjNo * ZoneWide + ZoneWide / 2) * iPI;
		double longitude1 = longitude * iPI; // 经度转换为弧度
		double latitude1 = latitude * iPI; // 纬度转换为弧度
		double e2 = 2 * iF - iF * iF;
		double ee = e2 / (1.0 - e2);
		double NN = a / Math.sqrt(1.0 - e2 * Math.sin(latitude1) * Math.sin(latitude1));
		double T = Math.tan(latitude1) * Math.tan(latitude1);
		double C = ee * Math.cos(latitude1) * Math.cos(latitude1);
		double A = (longitude1 - longitude0) * Math.cos(latitude1);
		double M = a * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * latitude1
					- (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2 / 1024) * Math.sin(2 * latitude1)
					+ (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * latitude1) 
					- (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * latitude1));
		// 因为是以赤道为Y轴的，与我们南北为Y轴是相反的，所以xy与高斯投影的标准xy正好相反;
		double xval = NN * (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 14 * C - 58 * ee) 
						* A * A * A * A * A / 120);
		double yval = M + NN * Math.tan(latitude1) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61
				- 58 * T + T * T + 270 * C - 330 * ee) * A * A * A * A * A * A / 720);
		double X0 = 1000000L * (ProjNo + 1) + 500000L;
		double Y0 = 0;
		xval = xval + X0;
		yval = yval + Y0;
		return new Point(xval,yval);
	}
	
	//高斯投影坐标转WGS84经纬度坐标
	public static Jwd gaussToJwd(double X, double Y) {
		int ProjNo = (int) (X / 1000000L); // 查找带号
		double longitude0 = ((ProjNo - 1) * ZoneWide + ZoneWide / 2) * iPI;
		double X0 = ProjNo * 1000000L + 500000L;
		double Y0 = 0;
		double xval = X - X0;
		double yval = Y - Y0; // 带内大地坐标
		double e2 = 2 * iF - iF * iF;
		double e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
		double ee = e2 / (1 - e2);
		double M = yval;
		double u = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
		double fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u)
				+ (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32)
				* Math.sin(4 * u) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u)
				+ (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
		double C = ee * Math.cos(fai) * Math.cos(fai);
		double T = Math.tan(fai) * Math.tan(fai);
		double NN = a / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai)); // 字串1
		double R = a * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai))
						* (1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)));
		double D = xval / NN;
		// 计算经度(Longitude) 纬度(Latitude)
		double longitude1 = longitude0 + (D - (1 + 2 * T + C) * D * D * D / 6 + (5 - 2 * C + 28 * T
						- 3 * C * C + 8 * ee + 24 * T * T) * D * D * D * D * D / 120) / Math.cos(fai);
		double latitude1 = fai - (NN * Math.tan(fai) / R) * (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D
						* D * D * D / 24 + (61 + 90 * T + 298 * C + 45 * T * T
						- 256 * ee - 3 * C * C) * D * D * D * D * D * D / 720);
		// 转换为度 DD
		return new Jwd(longitude1 / iPI, latitude1 / iPI);
	}
}