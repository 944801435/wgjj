package com.brilliance.base.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.brilliance.base.util.spacial.Coord;
import com.brilliance.base.util.spacial.GeometryCalc;
import com.brilliance.base.util.spacial.Jwd;
import com.brilliance.base.util.spacial.MathTool;
import com.brilliance.base.util.spacial.Point;
import com.brilliance.base.util.spacial.PolylineBuffer;

@SuppressWarnings({ "unchecked"})
public class SpaceUtil {
	private static final Logger log = Logger.getLogger(SpaceUtil.class);
	// public static final double RADIUS_OF_AIRPORT_ARC = 7070;// 机场建筑物限制面弧线半径
	public static final double earthRadius = 6378137.0;// 地球半径
	private static final int circleStepCount = 10;
	public static String[] maxAllowGrids = null;// 全部的海南全省的30级网格

	/**
	 * 获取各种空域构型投影之后的多边形坐标点's
	 * 多边形
	 * 
	 * @Title: getPolygonProjPoints
	 * @author maqian
	 * @date 2018年12月11日 上午11:05:06
	 * @param jwdsWgs84
	 * @return
	 */
	public static Point[] getPolygonProjPoints(Jwd[] jwdsWgs84) {
		Point[] points = new Point[jwdsWgs84.length];
		Coord coord = new Coord();
		for (int i = 0; i < jwdsWgs84.length; i++)
			points[i] = coord.lQtoXy(jwdsWgs84[i].getJd(), jwdsWgs84[i].getWd());
		return points;
	}

	/**
	 * 多边形-经纬度坐标
	 * 
	 * @Title: getPolygonProjJwds
	 * @author maqian
	 * @date 2018年12月11日 上午11:05:28
	 * @param jwdReqs
	 * @return
	 */
	public static Jwd[] getPolygonProjJwds(Jwd[] jwdReqs) {
		Point[] points = getPolygonProjPoints(jwdReqs);
		Coord coord = new Coord();
		Jwd[] jwds = new Jwd[points.length];
		for (int i = 0; i < points.length; i++)
			jwds[i] = coord.xYtoLq(points[i].x, points[i].y);
		return jwds;
	}

	/**
	 * 圆形
	 * 
	 * @Title: getCircleProjPoints
	 * @author maqian
	 * @date 2018年12月11日 上午11:05:36
	 * @param centreWgs84
	 * @param radius
	 * @return
	 */
	public static Point[] getCircleProjPoints(Jwd centreWgs84, double radius) {
		Coord coord = new Coord();
		Point point = coord.lQtoXy(centreWgs84.getJd(), centreWgs84.getWd());
		GeometryCalc util = new GeometryCalc();
		List<Point> pointList = util.shapeToPointArray(point.getX(), point.getY(), radius, radius, 0, 360, "圆形");
		Point[] points = new Point[pointList.size()];
		for (int i = 0; i < pointList.size(); i++)
			points[i] = pointList.get(i);
		return points;
	}

	/**
	 * 圆形-经纬度坐标
	 * 
	 * @Title: getCircleProjJwds
	 * @author maqian
	 * @date 2018年12月11日 上午11:07:20
	 * @param centre
	 * @param radius
	 * @return
	 */
	public static Jwd[] getCircleProjJwds(Jwd centre, double radius) {
		Point[] points = getCircleProjPoints(centre, radius);
		Coord coord = new Coord();
		Jwd[] jwds = new Jwd[points.length];
		for (int i = 0; i < points.length; i++)
			jwds[i] = coord.xYtoLq(points[i].x, points[i].y);
		return jwds;
	}

	/**
	 * 扇形
	 * 
	 * @Title: getSectorProjPoints
	 * @author maqian
	 * @date 2018年12月11日 上午11:07:29
	 * @param centreWgs84
	 * @param radius
	 * @param nStartA
	 * @param nEndA
	 * @return
	 */
	public static Point[] getSectorProjPoints(Jwd centreWgs84, double radius, double nStartA, double nEndA) {
		Coord coord = new Coord();
		Point point = coord.lQtoXy(centreWgs84.getJd(), centreWgs84.getWd());
		GeometryCalc util = new GeometryCalc();
		List<Point> pointList = util.shapeToPointArray(point.getX(), point.getY(), radius, radius, nStartA, nEndA, "扇形");
		Point[] points = new Point[pointList.size()];
		for (int i = 0; i < pointList.size(); i++)
			points[i] = pointList.get(i);
		return points;
	}

	/**
	 * 扇形-经纬度坐标
	 * 
	 * @Title: getSectorProjJwds
	 * @author maqian
	 * @date 2018年12月11日 上午11:07:38
	 * @param centre
	 * @param radius
	 * @param nStartA
	 * @param nEndA
	 * @return
	 */
	public static Jwd[] getSectorProjJwds(Jwd centre, double radius, double nStartA, double nEndA) {
		Point[] points = getSectorProjPoints(centre, radius, nStartA, nEndA);
		Coord coord = new Coord();
		Jwd[] jwds = new Jwd[points.length];
		for (int i = 0; i < points.length; i++)
			jwds[i] = coord.xYtoLq(points[i].x, points[i].y);
		return jwds;
	}

	/**
	 * 线缓冲区-投影坐标
	 * 
	 * @Title: getLineBufferPoints
	 * @author maqian
	 * @date 2018年12月11日 上午11:07:50
	 * @param jwdsWgs84
	 * @param width
	 * @return
	 */
	public static Point[] getLineBufferPoints(Jwd[] jwdsWgs84, double width) {
		// 经纬度转投影坐标
		Coord coord = new Coord();
		double[] data = new double[jwdsWgs84.length * 2];
		Point point = null;
		for (int i = 0; i < jwdsWgs84.length; i++) {
			point = coord.lQtoXy(jwdsWgs84[i].getJd(), jwdsWgs84[i].getWd());
			data[2 * i] = point.getX();
			data[2 * i + 1] = point.getY();
		}

		// 依据投影坐标计算线缓冲区
		String str = PolylineBuffer.GetBufferEdgeCoords(data, width / 2);
		String[] strs = str.split(",");
		Point[] points = new Point[strs.length / 2];
		for (int i = 0; i < strs.length / 2; i++) {
			points[i] = new Point(Double.parseDouble(strs[2 * i]), Double.parseDouble(strs[2 * i + 1]));
		}

		return points;
	}

	/**
	 * 线缓冲区-经纬度坐标
	 * 
	 * @Title: getLineBufferJwds
	 * @author maqian
	 * @date 2018年12月11日 上午11:08:10
	 * @param jwdsWgs84
	 * @param width
	 * @return
	 */
	public static Jwd[] getLineBufferJwds(Jwd[] jwdsWgs84, double width) {
		Point[] points = getLineBufferPoints(jwdsWgs84, width);

		// 投影坐标转经纬度
		Coord coord = new Coord();
		Jwd[] jwds = new Jwd[points.length];
		for (int i = 0; i < points.length; i++)
			jwds[i] = coord.xYtoLq(points[i].getX(), points[i].getY());

		return jwds;
	}

	/**
	 * 机场障碍物限制面-经纬度坐标 
	 * 
	 * @Title: getAirportJwds
	 * @author maqian
	 * @date 2018年12月11日 上午11:08:17
	 * @param a1Wgs84
	 * @param a2Wgs84
	 * @param c2Wgs84
	 * @param radiusC2B2
	 * @param b2Wgs84
	 * @param b3Wgs84
	 * @param radiusB3C3
	 * @param c3Wgs84
	 * @param a3Wgs84
	 * @param a4Wgs84
	 * @param c4Wgs84
	 * @param radiusC4B4
	 * @param b4Wgs84
	 * @param b1Wgs84
	 * @param radiusB1C1
	 * @param c1Wgs84
	 * @return
	 */
	public static Jwd[] getAirportJwds(Jwd a1Wgs84, Jwd a2Wgs84, Jwd c2Wgs84, double radiusC2B2, Jwd b2Wgs84, Jwd b3Wgs84, double radiusB3C3,
			Jwd c3Wgs84, Jwd a3Wgs84, Jwd a4Wgs84, Jwd c4Wgs84, double radiusC4B4, Jwd b4Wgs84, Jwd b1Wgs84, double radiusB1C1, Jwd c1Wgs84) {
		List<Jwd> jwdList = new LinkedList<Jwd>();
		Jwd[] jwds = null;

		jwdList.add(a1Wgs84);// a1
		jwdList.add(a2Wgs84);// a2
		jwdList.add(c2Wgs84);// c2
		// 求c2b2弧
		jwds = getArcJwds(c2Wgs84, b2Wgs84, radiusC2B2);
		for (int i = 0; i < jwds.length; i++)
			jwdList.add(jwds[i]);// c2b2
		jwdList.add(b2Wgs84);// b2
		jwdList.add(b3Wgs84);// b3
		// 求b3c3弧
		jwds = getArcJwds(b3Wgs84, c3Wgs84, radiusB3C3);
		for (int i = 0; i < jwds.length; i++)
			jwdList.add(jwds[i]);// b3c3
		jwdList.add(c3Wgs84);// c3
		jwdList.add(a3Wgs84);// a3
		jwdList.add(a4Wgs84);// a4
		jwdList.add(c4Wgs84);// c4
		// 求c4b4弧
		jwds = getArcJwds(c4Wgs84, b4Wgs84, radiusC4B4);
		for (int i = 0; i < jwds.length; i++)
			jwdList.add(jwds[i]);// c4b4
		jwdList.add(b4Wgs84);// b4
		jwdList.add(b1Wgs84);// b1
		// 求b1c1弧
		jwds = getArcJwds(b1Wgs84, c1Wgs84, radiusB1C1);
		for (int i = 0; i < jwds.length; i++)
			jwdList.add(jwds[i]);// b1c1
		jwdList.add(c1Wgs84);// c1

		jwds = new Jwd[jwdList.size()];
		for (int i = 0; i < jwdList.size(); i++) {
			jwds[i] = jwdList.get(i);
		}

		return jwds;
	}

	/**
	 * 已知两点求弧线 
	 * 
	 * @Title: getArcJwds
	 * @author maqian
	 * @date 2018年12月11日 上午11:08:28
	 * @param p1
	 * @param p2
	 * @param dRadius
	 * @return
	 */
	private static Jwd[] getArcJwds(Jwd p1, Jwd p2, double dRadius) {
		// 经纬度转弧度
		double lng1Rad = degreeToRadians(p1.getJd());
		double lat1Rad = degreeToRadians(p1.getWd());
		double lng2Rad = degreeToRadians(p2.getJd());
		double lat2Rad = degreeToRadians(p2.getWd());

		// 计算bearing
		double bearing = bearing(lat1Rad, lng1Rad, lat2Rad, lng2Rad);

		// 取p1、p2中点
		double[] cenRad = midpoint(lat1Rad, lng1Rad, lat2Rad, lng2Rad);
		double lngCenRad = cenRad[0];
		double latCenRad = cenRad[1];

		// 求圆心
		bearing = bearing + Math.PI / 2;
		// 计算中心点到圆心的距离
		double distance = distance(lat1Rad, lng1Rad, latCenRad, lngCenRad, earthRadius);
		distance = Math.sqrt(dRadius * dRadius - distance * distance);
		// 根据bearing和距离计算圆心
		cenRad = destination(latCenRad, lngCenRad, bearing, distance, earthRadius);
		lngCenRad = cenRad[0];
		latCenRad = cenRad[1];

		// 计算圆心到p1、p2的bearing
		double bearingP1 = bearing(latCenRad, lngCenRad, lat1Rad, lng1Rad);
		double bearingP2 = bearing(latCenRad, lngCenRad, lat2Rad, lng2Rad);
		if (bearingP1 > 0 && bearingP2 > 0) {

		} else if (bearingP1 > 0 && bearingP2 < 0) {
			bearingP2 += 2 * Math.PI;
		} else if (bearingP1 < 0 && bearingP2 < 0) {
			bearingP1 += 2 * Math.PI;
			bearingP2 += 2 * Math.PI;
		} else if (bearingP1 < 0 && bearingP2 > 0) {

		}

		// 计算弧度步长
		double step = (bearingP2 - bearingP1) / circleStepCount;
		double[] tmpRad = null;
		double lngRad = 0;
		double latRad = 0;
		Jwd tmpJwd = null;
		List<Jwd> jwdList = new LinkedList<Jwd>();
		bearing = bearingP1 + step;
		while (bearing < bearingP2) {
			// 根据步长逐个计算点
			tmpRad = destination(latCenRad, lngCenRad, bearing, dRadius, earthRadius);
			lngRad = tmpRad[0];
			latRad = tmpRad[1];

			// 弧度转经纬度
			tmpJwd = new Jwd(radiansToDegree(lngRad), radiansToDegree(latRad));
			jwdList.add(tmpJwd);

			bearing += step;
		}

		Jwd[] jwds = new Jwd[jwdList.size()];
		for (int i = 0; i < jwds.length; i++) {
			jwds[i] = jwdList.get(i);
		}
		return jwds;
	}

	/**
	 * 弧度转经纬度
	 * 
	 * @Title: radiansToDegree
	 * @author maqian
	 * @date 2018年12月11日 上午11:08:42
	 * @param radians
	 * @return
	 */
	private static double radiansToDegree(double radians) {
		return radians * 180 / Math.PI;
	}

	/**
	 * 经纬度转弧度 
	 * 
	 * @Title: degreeToRadians
	 * @author maqian
	 * @date 2018年12月11日 上午11:08:48
	 * @param degree
	 * @return
	 */
	public static double degreeToRadians(double degree) {
		return degree * Math.PI / 180;
	}

	private static double bearing(double lat1Rad, double lon1Rad, double lat2Rad, double lon2Rad) {
		double dLon = (lon2Rad - lon1Rad);
		double y = Math.sin(dLon) * Math.cos(lat2Rad);
		double x = Math.cos(lat1Rad) * Math.sin(lat2Rad) - Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(dLon);
		double brng = Math.atan2(y, x);
		return brng;
	}

	private static double[] midpoint(double lat1Rad, double lon1Rad, double lat2Rad, double lon2Rad) {
		double dLon = (lon2Rad - lon1Rad);

		double cosLat1 = Math.cos(lat1Rad);
		double cosLat2 = Math.cos(lat2Rad);
		double sinLat1 = Math.sin(lat1Rad);
		double sinLat2 = Math.sin(lat2Rad);

		double Bx = cosLat2 * Math.cos(dLon);
		double By = cosLat2 * Math.sin(dLon);
		double out_latRad = Math.atan2(sinLat1 + sinLat2, Math.sqrt((cosLat1 + Bx) * (cosLat1 + Bx) + By * By));
		double out_lonRad = lon1Rad + Math.atan2(By, cosLat1 + Bx);

		return new double[] { out_lonRad, out_latRad };
	}

	private static double[] destination(double lat1Rad, double lon1Rad, double bearingRad, double distance, double radius) {
		double dR = distance / radius;
		double out_latRad = Math.asin(Math.sin(lat1Rad) * Math.cos(dR) + Math.cos(lat1Rad) * Math.sin(dR) * Math.cos(bearingRad));
		double out_lonRad = lon1Rad
				+ Math.atan2(Math.sin(bearingRad) * Math.sin(dR) * Math.cos(lat1Rad), Math.cos(dR) - Math.sin(lat1Rad) * Math.sin(out_latRad));
		return new double[] { out_lonRad, out_latRad };
	}

	public static double distance(double lat1Rad, double lon1Rad, double lat2Rad, double lon2Rad, double radius) {
		double dLat = (lat2Rad - lat1Rad);
		double dLon = (lon2Rad - lon1Rad);
		double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);
		double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
		double d = radius * c;
		return d;
	}

	// 获取符合云系统规范的围栏数据
	// 多边形
	public static byte[] getPolygonRail(Jwd[] jwds) {
		return new byte[] { 0 };
	}

	// 圆形-云系统标准围栏数据
	public static byte[] getCircleRail(Jwd centre, double radius) {
		return new byte[] { 0 };
	}

	// 扇形-云系统标准围栏数据
	public static byte[] getSectorRail(Jwd centre, double radius, double nStartA, double nEndA) {
		return new byte[] { 0 };
	}

	// 线缓冲区-云系统标准围栏数据
	public static byte[] getLineBufferRail(Jwd[] jwds, double width) {
		return new byte[] { 0 };
	}

	// 机场障碍物限制面-云系统标准围栏数据
	public static byte[] getAirportRail(Jwd a1, Jwd a2, Jwd c2, double radiusC2B2, Jwd b2, Jwd b3, double radiusB3C3, Jwd c3, Jwd a3, Jwd a4, Jwd c4,
			double radiusC4B4, Jwd b4, Jwd b1, double radiusB1C1, Jwd c1) {
		return new byte[] { 0 };
	}

	/**
	 * 判断经纬度的点是否在多边形内（jwd的多边形）
	 * 
	 * @Title: isJwdInPolygon
	 * @author maqian
	 * @date 2020年3月19日 下午3:14:04
	 * @param jwd
	 * @param plgWgs84
	 * @return
	 */
	public static boolean isJwdInPolygon(Jwd jwd, String plgWgs84) {
		Coord coord = new Coord();
		return isPointInPolygon(coord.lQtoXy(jwd.getJd(), jwd.getWd()), CoordTransformUtil.getPolygonProj(plgWgs84));
	}

	/**
	 * 判断点是否在多边形内
	 * 
	 * @Title: isPointInPolygon
	 * @author maqian
	 * @date 2018年12月11日 上午11:31:57
	 * @param point
	 * @param polygonProj
	 * @return
	 */
	public static boolean isPointInPolygon(Point point, String polygonProj) {
		boolean in = false;
		int loc = polygonProj.indexOf(";");
		String[] polygonProjs = null;
		if (loc > 0) {
			polygonProjs = polygonProj.split(";");
		} else {
			polygonProjs = new String[] { polygonProj };
		}

		String[] pointStr = null;
		Point[] points = null;
		GeometryCalc util = new GeometryCalc();
		for (int i = 0; i < polygonProjs.length; i++) {
			pointStr = polygonProjs[i].split("\\|");
			points = new Point[pointStr.length];
			for (int j = 0; j < pointStr.length; j++) {
				loc = pointStr[j].indexOf(",");
				points[j] = new Point(Double.parseDouble(pointStr[j].substring(0, loc)), Double.parseDouble(pointStr[j].substring(loc + 1)));
			}

			if (util.isPointInPolygon(point, points)) {
				in = true;
				break;
			}
		}

		return in;
	}

	/**
	 * 判断多边形是否与空域相交 
	 * 
	 * @Title: isPolygonIntersect
	 * @author maqian
	 * @date 2018年12月11日 上午11:32:13
	 * @param srcPoints
	 * @param tarPoints
	 * @return
	 */
	public static boolean isPolygonIntersect(Point[] srcPoints, Point[] tarPoints) {
		GeometryCalc util = new GeometryCalc();
		return util.IsPolygonIntersect(srcPoints, srcPoints.length, tarPoints, tarPoints.length);
	}

	public static boolean isPolygonIntersect(String polygonWgs841, String polygonWgs842) {
		List<Jwd[]> srcPointsList = polygonWgs84ToJwdList(polygonWgs841);
		List<Jwd[]> tarPointsList = polygonWgs84ToJwdList(polygonWgs842);
		for (Jwd[] srcPoints : srcPointsList) {
			for (Jwd[] tarPoints : tarPointsList) {
				if (isPolygonIntersect2(srcPoints, tarPoints)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断多边形是否与空域相交 ，为了修正计算适飞空域时右上角少一块儿的问题
	 * 
	 * @Title: isPolygonIntersect2
	 * @author jiaotw
	 * @date 2020年04月12日 上午11:32:13
	 * @param srcPoints
	 * @param tarPoints
	 * @return
	 */
	public static boolean isPolygonIntersect2(Jwd[] srcPoints, Jwd[] tarPoints) {
		Point[] points1 = new Point[srcPoints.length];
		for (int i = 0; i < srcPoints.length; i++)
			points1[i] = new Point(srcPoints[i].getJd(), srcPoints[i].getWd());
		Point[] points2 = new Point[tarPoints.length];
		for (int i = 0; i < tarPoints.length; i++) {
			points2[i] = new Point(tarPoints[i].getJd(), tarPoints[i].getWd());
		}

		GeometryCalc util = new GeometryCalc();
		// 首先判断多边形1的各个点是否在多边形2内
		for (int i = 0; i < points1.length; i++) {
			if (util.insidepolygon(points2.length, points2, points1[i]) < 2) {
				return true;
			}
		}
		// 首先判断多边形1的各个点是否在多边形2内
		for (int i = 0; i < points2.length; i++) {
			if (util.insidepolygon(points1.length, points1, points2[i]) < 2) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断多边形是否与空域相交 
	 * 
	 * @Title: isPolygonIntersect
	 * @author maqian
	 * @date 2018年12月11日 上午11:32:27
	 * @param points
	 * @param polygonProjList
	 * @return
	 */
	public static boolean isPolygonIntersect(Point[] points, List<String> polygonProjList) {
		boolean isIntersect = false;

		List<Point[]> pointsList = SpaceUtil.getPointsList(polygonProjList);
		Point[] targetPoints = null;
		for (int k = 0; k < pointsList.size(); k++) {
			targetPoints = pointsList.get(k);
			if (isPolygonIntersect(points, targetPoints)) {
				isIntersect = true;
				break;
			}
		}

		return isIntersect;
	}

	/**
	 * 将投影坐标转换为Point数组的列表，网格的列表元素可能大于1，其他形状列表元素等于1 
	 * 
	 * @Title: getPointsList
	 * @author maqian
	 * @date 2018年12月11日 上午11:32:38
	 * @param polygonProjList
	 * @return
	 */
	private static List<Point[]> getPointsList(List<String> polygonProjList) {
		Point[] points = null;
		int loc = 0;
		String xStr = null;
		String yStr = null;
		// String polygonProj=null;
		String[] polygonProjs = null;
		List<Point[]> pointsList = new LinkedList<Point[]>();
		String[] locStrs = null;
		for (String polygonProj : polygonProjList) {
			loc = polygonProj.indexOf(";");
			if (loc > 0) {
				polygonProjs = polygonProj.split(";");
			} else {
				polygonProjs = new String[] { polygonProj };
			}

			for (int i = 0; i < polygonProjs.length; i++) {
				polygonProj = polygonProjs[i];

				locStrs = polygonProj.split("\\|");
				points = new Point[locStrs.length];
				for (int j = 0; j < locStrs.length; j++) {
					loc = locStrs[j].indexOf(",");
					xStr = locStrs[j].substring(0, loc);
					yStr = locStrs[j].substring(loc + 1);
					points[j] = new Point(Double.parseDouble(xStr), Double.parseDouble(yStr));
				}
				pointsList.add(points);
			}
		}

		return pointsList;
	}

	// 计算飞行轨迹覆盖面积，幅宽作为参数之一，用于植保作业面积计算
	// 每两个航迹点之间的连线根据幅宽生成线缓冲区，视为两点覆盖的面积
	// 采用栅格微分方法，剔除重叠面积，即：将包围盒微分为很多栅格，设置每个线缓冲区覆盖的栅格标志为1，最后计算所有标志为1的栅格面积
	// 返回：
	// objs[0] String 面积（亩）
	// objs[1] Jwd[n][4] 相交的网格
	public static Object[] calFlyArea(Jwd[] jwdsWgs84, double width) {
		long begTimes = new Date().getTime();

		if (jwdsWgs84 == null || jwdsWgs84.length == 0)
			return new Object[] { "0.0", new Jwd[0][0] };

		// 0、转换航迹点为投影坐标
		Point[] points = getPolygonProjPoints(jwdsWgs84);
		// 1、计算包围盒
		Double minx = null;
		Double miny = null;
		Double maxx = null;
		Double maxy = null;
		for (Point point : points) {
			if (minx == null) {
				minx = point.getX();
				maxx = point.getX();
				miny = point.getY();
				maxy = point.getY();
			} else {
				if (point.getX() > maxx)
					maxx = point.getX();
				else if (point.getX() < minx)
					minx = point.getX();
				if (point.getY() > maxy)
					maxy = point.getY();
				else if (point.getY() < miny)
					miny = point.getY();
			}
		}
		// 2、包围盒根据幅宽进行扩大
		minx -= width / 2;
		maxx += width / 2;
		miny -= width / 2;
		maxy += width / 2;

//		//3、根据指定粒度生成栅格，返回栅格数据组，每个栅格用四个定点的投影坐标描述
//		double step=1;//栅格大小为1*1平方米
//		Point[][] grids=getGrids(minx,miny,maxx,maxy,step);
//		int[] gridMarks=new int[grids.length];
//		for (int i = 0; i < gridMarks.length; i++)
//			gridMarks[i]=0;
//		//4、逐个处理每个线段
//		Point begPoint=null;
//		Point endPoint=null;
//		Point[] linePoints=null;
//		int intersectGridCount=0;
//		for (int i = 0; i < (points.length-1); i++) {
//			begPoint=points[i];
//			endPoint=points[i+1];
//			
//			//4.1计算线缓冲区，返回其投影坐标表示的多边形
//			linePoints=getLineBufferPoints(new Point[]{begPoint,endPoint}, width);
//			//4.2计算线缓冲区与栅格的相交情况
//			for (int j = 0; j < grids.length; j++) {
//				if(gridMarks[j]==1)
//					continue;
//				else if(isPolygonIntersect(grids[j], linePoints)){
//					gridMarks[j]=1;
//					intersectGridCount++;
//				}
//			}
//		}
//		//5合计所有相交标志为1的栅格面积，生成所有相交的网格数组
//		double area=0;
//		Jwd[][] intersectGrids = new Jwd[intersectGridCount][4];
//		int idx=0;
//		Coord coord = new Coord();
//		for (int i = 0; i < gridMarks.length; i++) {
//			if(gridMarks[i]==1){
//				area+=step*step;
//				
//				intersectGrids[idx][0] = coord.xYtoLq(grids[i][0].x, grids[i][0].y);
//				intersectGrids[idx][1] = coord.xYtoLq(grids[i][1].x, grids[i][1].y);
//				intersectGrids[idx][2] = coord.xYtoLq(grids[i][2].x, grids[i][2].y);
//				intersectGrids[idx][3] = coord.xYtoLq(grids[i][3].x, grids[i][3].y);
//				idx++;
//			}
//		}

		// 3、根据指定粒度生成栅格，返回栅格数据组，每个栅格用四个定点的投影坐标描述
		int step = 1;// 栅格大小为1*1平方米
		Object[] objs = getGridsOpt(minx, miny, maxx, maxy, step);
		List<GridVo> rootGrids = (List<GridVo>) objs[0];
		int xidx = (int) objs[1];
		int yidx = (int) objs[2];
		int[][] gridMarks = new int[xidx][yidx];
		for (int i = 0; i < xidx; i++)
			for (int j = 0; j < yidx; j++)
				gridMarks[i][j] = 0;

		// 4、逐个处理每个线段
		Point begPoint = null;
		Point endPoint = null;
		Point[] linePoints = null;
		List<GridVo> intersectGridList = new LinkedList<GridVo>();
		for (int i = 0; i < (points.length - 1); i++) {
			begPoint = points[i];
			endPoint = points[i + 1];

			// 4.1计算线缓冲区，返回其投影坐标表示的多边形
			linePoints = getLineBufferPoints(new Point[] { begPoint, endPoint }, width);
			// 4.2计算线缓冲区与栅格的相交情况
			intersectGridList.addAll(getIntersectGrid4(rootGrids, linePoints, gridMarks));
		}

		// 5合计所有相交标志为1的栅格面积，生成所有相交的网格数组
		double area = 0;
		Coord coord = new Coord();
		Point[] gridPoints = null;
		Jwd[][] intersectGrids = new Jwd[intersectGridList.size()][4];
		for (int i = 0; i < intersectGridList.size(); i++) {
			area += step * step;

			gridPoints = intersectGridList.get(i).getPoints();
			intersectGrids[i][0] = coord.xYtoLq(gridPoints[0].x, gridPoints[0].y);
			intersectGrids[i][1] = coord.xYtoLq(gridPoints[1].x, gridPoints[1].y);
			intersectGrids[i][2] = coord.xYtoLq(gridPoints[2].x, gridPoints[2].y);
			intersectGrids[i][3] = coord.xYtoLq(gridPoints[3].x, gridPoints[3].y);
		}

		// 打印耗时
		long useMilSeconds = new Date().getTime() - begTimes;
		log.debug("计算用时：" + useMilSeconds);

		// 平方米转换为亩，小数点后取1位
		DecimalFormat fmt = new DecimalFormat();
		fmt.applyPattern("#0.0");
		return new Object[] { fmt.format(area / 666.66), intersectGrids };
	}

	// 根据指定粒度生成栅格，返回栅格数据组，每个栅格用四个定点的投影坐标描述
	public static Point[][] getGrids(double minx, double miny, double maxx, double maxy, double step) {
		double begx = minx;
		double begy = miny;
		double endx = minx + step;
		double endy = miny + step;
		Point[] grid = new Point[4];
		grid[0] = new Point(begx, begy);
		grid[1] = new Point(endx, begy);
		grid[2] = new Point(endx, endy);
		grid[3] = new Point(begx, endy);
		List<List<Point[]>> rows = new LinkedList<List<Point[]>>();

		// y取最小值,x从小到大，计算一行栅格
		List<Point[]> firstLine = new LinkedList<Point[]>();
		firstLine.add(grid);
		while (endx < maxx) {
			begx = endx;
			endx = begx + step;

			grid = new Point[4];
			grid[0] = new Point(begx, begy);
			grid[1] = new Point(endx, begy);
			grid[2] = new Point(endx, endy);
			grid[3] = new Point(begx, endy);
			firstLine.add(grid);
		}
		rows.add(firstLine);
		// y从小到大，计算每一行栅格
		List<Point[]> nextLine = null;
		Point[] baseGrid = null;
		while (endy < maxy) {
			begy = endy;
			endy = begy + step;

			nextLine = new LinkedList<Point[]>();
			for (int i = 0; i < firstLine.size(); i++) {
				baseGrid = firstLine.get(i);

				grid = new Point[4];
				grid[0] = new Point(baseGrid[0].getX(), begy);
				grid[1] = new Point(baseGrid[1].getX(), begy);
				grid[2] = new Point(baseGrid[1].getX(), endy);
				grid[3] = new Point(baseGrid[0].getX(), endy);
				nextLine.add(grid);
			}
			rows.add(nextLine);
		}

		// 汇总所有栅格
		Point[][] grids = new Point[rows.size() * firstLine.size()][4];
		int idx = 0;
		for (int i = 0; i < rows.size(); i++) {
			List<Point[]> line = rows.get(i);
			for (int j = 0; j < firstLine.size(); j++) {
				grids[idx] = line.get(j);
				idx++;
			}
		}
		return grids;
	}

	// 优化的栅格算法
	// obj[0] List<GridVo> 根Grid列表
	// obj[1] xidx
	// obj[2] yidx
	// obj[3] 所有4级网格
	private static Object[] getGridsOpt(double minx, double miny, double maxx, double maxy, int step) {
		double begx = minx;
		double endx4 = begx;
		double endx3 = 0;
		double endx2 = 0;
		double endx1 = 0;
		double begy = miny;
		double endy4 = begy;
		double endy3 = 0;
		double endy2 = 0;
		double endy1 = 0;
		int yidx = 0;
		int lastYidx = 0;
		int xidx = 0;
		GridVo grid1 = null;
		GridVo grid2 = null;
		GridVo grid3 = null;
		GridVo grid4 = null;
		List<GridVo> grid1List = new LinkedList<GridVo>();
		List<GridVo> preColGrid3List = null;
		List<GridVo> preColGrid2List = null;
		List<GridVo> preColGrid1List = null;

		while (endx4 < maxx) {
			begx = endx4;
			endx4 = begx + step;

			if (xidx % 2 == 0) {
				preColGrid3List = new LinkedList<GridVo>();
				if (xidx % 4 == 0) {
					preColGrid2List = new LinkedList<GridVo>();
					if (xidx % 8 == 0) {
						preColGrid1List = new LinkedList<GridVo>();
					}
				}
			}

			while (endy4 < maxy) {
				begy = endy4;
				endy4 = begy + step;

				// 创建四级网格
				grid4 = new GridVo(4, 4);
				grid4.setPoint(0, new Point(begx, begy));
				grid4.setPoint(1, new Point(endx4, begy));
				grid4.setPoint(2, new Point(endx4, endy4));
				grid4.setPoint(3, new Point(begx, endy4));
				grid4.xidx = xidx;
				grid4.yidx = yidx;

				if (xidx % 2 != 0) {
					// 使用前一列的3级网格
					grid3 = preColGrid3List.get(yidx / 2);
				}
				if (xidx % 4 != 0) {
					// 使用前一列的2级网格
					grid2 = preColGrid2List.get(yidx / 4);
				}
				if (xidx % 8 != 0) {
					// 使用前一列的1级网格
					grid1 = preColGrid1List.get(yidx / 8);
				}

				if (yidx % 2 == 0 && xidx % 2 == 0) {
					// yidx、xidx除2后余数为零，则创建三级网格
					endx3 = begx + step * 2;
					endy3 = begy + step * 2;
					grid3 = new GridVo(3, 4);
					grid3.setPoint(0, new Point(begx, begy));
					grid3.setPoint(1, new Point(endx3, begy));
					grid3.setPoint(2, new Point(endx3, endy3));
					grid3.setPoint(3, new Point(begx, endy3));
					preColGrid3List.add(grid3);

					if (yidx % 4 == 0 && xidx % 4 == 0) {
						// yidx、xidx除4后余数为零，则创建二级网格
						endx2 = begx + step * 4;
						endy2 = begy + step * 4;
						grid2 = new GridVo(2, 4);
						grid2.setPoint(0, new Point(begx, begy));
						grid2.setPoint(1, new Point(endx2, begy));
						grid2.setPoint(2, new Point(endx2, endy2));
						grid2.setPoint(3, new Point(begx, endy2));
						preColGrid2List.add(grid2);

						if (yidx % 8 == 0 && xidx % 8 == 0) {
							// yidx、xidx除8后余数为零，则创建一级网格
							endx1 = begx + step * 8;
							endy1 = begy + step * 8;
							grid1 = new GridVo(1, 4);
							grid1.setPoint(0, new Point(begx, begy));
							grid1.setPoint(1, new Point(endx1, begy));
							grid1.setPoint(2, new Point(endx1, endy1));
							grid1.setPoint(3, new Point(begx, endy1));
							preColGrid1List.add(grid1);
							grid1List.add(grid1);
						}

						grid1.addChild(grid2);
					}

					grid2.addChild(grid3);
				}

				grid3.addChild(grid4);
				yidx++;
			}
			endy4 = miny;
			lastYidx = yidx;
			yidx = 0;
			xidx++;
		}

		return new Object[] { grid1List, xidx, lastYidx };
	}

	private static List<GridVo> getIntersectGrid4(List<GridVo> rootGrids, Point[] linePoints, int[][] gridMarks) {
		List<GridVo> rtnGrids = new LinkedList<GridVo>();
		List<GridVo> grids2 = null;
		List<GridVo> grids3 = null;
		List<GridVo> grids4 = null;
		GridVo grid4 = null;
		int calCount = 0;
		for (int i = 0; i < rootGrids.size(); i++) {
			// 首先判断是否与1级网格相交
			calCount++;
			if (!isPolygonIntersect(linePoints, rootGrids.get(i).getPoints()))
				continue;

			// 判断是否与2级网格相交
			grids2 = rootGrids.get(i).getChildGrids();
			for (int j = 0; j < grids2.size(); j++) {
				calCount++;
				if (!isPolygonIntersect(linePoints, grids2.get(j).getPoints()))
					continue;

				// 判断是否与3级网格相交
				grids3 = grids2.get(j).getChildGrids();
				for (int k = 0; k < grids3.size(); k++) {
					calCount++;
					if (!isPolygonIntersect(linePoints, grids3.get(k).getPoints()))
						continue;

					// 判断是否与4级网格相交
					grids4 = grids3.get(k).getChildGrids();
					for (int l = 0; l < grids4.size(); l++) {
						grid4 = grids4.get(l);

						if (gridMarks[grid4.xidx][grid4.yidx] == 1)
							continue;

						calCount++;
						if (isPolygonIntersect(linePoints, grid4.getPoints())) {
							gridMarks[grid4.xidx][grid4.yidx] = 1;
							rtnGrids.add(grid4);
						}
					}
				}
			}
		}
		log.debug("计算线缓冲区与栅格的相交情况：calCount数量=" + calCount);
		return rtnGrids;
	}

	public static Point[] getLineBufferPoints(Point[] linePoints, double width) {
		double[] data = new double[linePoints.length * 2];
		for (int i = 0; i < linePoints.length; i++) {
			data[2 * i] = linePoints[i].getX();
			data[2 * i + 1] = linePoints[i].getY();
		}
		// 依据投影坐标计算线缓冲区
		String str = PolylineBuffer.GetBufferEdgeCoords(data, width / 2);

//		// 依据投影坐标计算线缓冲区
//		List<Point> coords=new LinkedList<Point>();
//		coords.add(linePoints[0]);
//		coords.add(linePoints[1]);
//		String str = GetBufferEdgeCoordsMine(coords, width / 2);

		String[] strs = str.split(",");
		Point[] points = new Point[strs.length / 2];
		for (int i = 0; i < strs.length / 2; i++)
			points[i] = new Point(Double.parseDouble(strs[2 * i]), Double.parseDouble(strs[2 * i + 1]));

		return points;
	}

	public static String GetBufferEdgeCoordsMine(List<Point> coords, double radius) {
		String leftBufferCoords = GetLeftBufferEdgeCoordsMine(coords, radius);
		Collections.reverse(coords);
		String rightBufferCoords = GetLeftBufferEdgeCoordsMine(coords, radius);
		return leftBufferCoords + "," + rightBufferCoords;
	}

	private static String GetLeftBufferEdgeCoordsMine(List<Point> coordsProj, double radius) {
		double alpha = 0.0;
		StringBuilder strCoords = new StringBuilder();
		double startRadian = 0.0;
		double endRadian = 0.0;

		alpha = MathTool.GetQuadrantAngle(coordsProj.get(0), coordsProj.get(1));
		startRadian = alpha + Math.PI;
		endRadian = alpha + (3 * Math.PI) / 2;
		List<String> coordList = GetBufferCoordsByRadianMine(coordsProj.get(0), startRadian, endRadian, radius);
		strCoords.append(coordList.get(coordList.size() - 1));// 取最后一个点

		startRadian = alpha + (3 * Math.PI) / 2;
		endRadian = alpha + 2 * Math.PI;
		coordList = GetBufferCoordsByRadianMine(coordsProj.get(1), startRadian, endRadian, radius);
		strCoords.append(",");
		strCoords.append(coordList.get(0));// 取第一个点

		return strCoords.toString();
	}

	private static List<String> GetBufferCoordsByRadianMine(Point center, double startRadian, double endRadian, double radius) {
		double gamma = Math.PI / 6;

		double x = 0.0, y = 0.0;
		List<String> coordList = new LinkedList<String>();
		for (double phi = startRadian; phi <= endRadian + 0.000000000000001; phi += gamma) {
			x = center.x + radius * Math.cos(phi);
			y = center.y + radius * Math.sin(phi);
			coordList.add(x + "," + y);
		}
		return coordList;
	}

	/**
	 * 将多边形转成Jwd的点
	 */
	private static List<Jwd[]> polygonWgs84ToJwdList(String polygonWgs84) {
		String[] locStrs = null;
		Jwd[] jwds = null;
		List<Jwd[]> jwdsList = new LinkedList<Jwd[]>();
		String[] polygonWgs84s = polygonWgs84.split(";");
		for (int i = 0; i < polygonWgs84s.length; i++) {
			polygonWgs84 = polygonWgs84s[i];
			locStrs = polygonWgs84.split("\\|");
			jwds = new Jwd[locStrs.length];
			for (int j = 0; j < locStrs.length; j++) {
				String[] jwdArry = locStrs[j].split(",");
				jwds[j] = new Jwd(Double.parseDouble(jwdArry[0]), Double.parseDouble(jwdArry[1]));
			}
			jwdsList.add(jwds);
		}
		return jwdsList;
	}

	// 判断两个包围盒是否相交
	public static boolean isCross(Double spc1Minx, Double spc1Miny, Double spc1Maxx, Double spc1Maxy, Double spc2Minx, Double spc2Miny,
			Double spc2Maxx, Double spc2Maxy) {
		if (((spc1Minx >= spc2Minx && spc1Minx <= spc2Maxx) || (spc1Maxx >= spc2Minx && spc1Maxx <= spc2Maxx)
				|| (spc1Minx <= spc2Minx && spc1Maxx >= spc2Maxx) || (spc1Minx >= spc2Minx && spc1Maxx <= spc2Maxx))
				&& ((spc1Miny >= spc2Miny && spc1Miny <= spc2Maxy) || (spc1Maxy >= spc2Miny && spc1Maxy <= spc2Maxy)
						|| (spc1Miny <= spc2Miny && spc1Maxy >= spc2Maxy) || (spc1Miny >= spc2Miny && spc1Maxy <= spc2Maxy)))
			return true;
		else
			return false;
	}

	/**
	 * 将普通网格合并成大网格
	 * 
	 * @Title: getBigWgLoc
	 * @author maqian
	 * @date 2019年11月5日 上午9:25:10
	 * @param loc 只能接收网格类型
	 * @return
	 */
	public static String getBigWgLoc(String loc) {
		List<String> binaryList = Arrays.asList(loc.split("\\|"));
		List<String> resultList = new ArrayList<String>();
		while (binaryList.size() > 0) {
			binaryList = getBigWgList(binaryList, resultList);
		}
		return String.join("|", resultList);
	}

	// 将小网格合并成大网格
	private static List<String> getBigWgList(List<String> oldList, List<String> resultList) {
		Map<String, List<String>> binaryMap = new HashMap<String, List<String>>();
		for (String binary : oldList) {
			String key = binary.substring(0, binary.length() - 2);
			List<String> value = new ArrayList<String>();
			if (binaryMap.containsKey(key)) {
				value = binaryMap.get(key);
			}
			value.add(binary);
			binaryMap.put(key, value);
		}

		List<String> binaryList = new ArrayList<String>();
		Iterator<String> iter = binaryMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (binaryMap.get(key).size() == 4) {
				binaryList.add(key);
			} else {
				List<String> value = binaryMap.get(key);
				resultList.addAll(value);
			}
		}
		System.out.println("剩余未合并小网格数量：" + binaryList.size());
		return binaryList;
	}

	// 多边形扩展或收缩
	private static Point pointAdd(Point left, Point right) {
		return new Point(left.x + right.x, left.y + right.y);
	}

	private static Point pointSub(Point left, Point right) {
		return new Point(left.x - right.x, left.y - right.y);
	}

	private static double pointMult(Point left, Point right) {
		return left.x * right.x + left.y * right.y;
	}

	private static Point pointMult(Point left, double value) {
		return new Point(left.x * value, left.y * value);
	}

	private static double pointMultMult(Point left, Point right) {
		return left.x * right.y - left.y * right.x;
	}

	public static Jwd[] expand(Jwd[] jwdsWgs84, double expand) {
		expand = 0 - expand;
		List<Point> pList = new LinkedList<Point>();// 原始顶点坐标，
													// 在initPList函数当中初始化赋值
		Coord coord = new Coord();
		for (int i = 0; i < jwdsWgs84.length; i++) {
			pList.add(coord.lQtoXy(jwdsWgs84[i].getJd(), jwdsWgs84[i].getWd()));
		}

		List<Point> dpList = new LinkedList<Point>();// 边向量dpList［i＋1］－
														// dpLIst［i］ 在
														// initDPList函数当中计算后赋值
		Point point = null;
		for (int i = 0; i < pList.size(); i++) {
			if (i == (pList.size() - 1)) {
				point = pList.get(0);
			} else {
				point = pList.get(i + 1);
			}
			point = pointSub(point, pList.get(i));
			dpList.add(point);
		}

		// print("开始计算ndpList")
		List<Point> ndpList = new LinkedList<Point>();// 单位化的边向量，
														// 在initNDPList函数当中计算后肤质，实际使用的时候，完全可以用dpList来保存他们
		for (int i = 0; i < dpList.size(); i++) {

			double value = 1.0 / Math.sqrt(pointMult(dpList.get(i), dpList.get(i)));
			point = pointMult(dpList.get(i), value);
			ndpList.add(point);
		}

		List<Point> newList = new LinkedList<Point>();// newList
		int count = pList.size();
		int startIndex = 0;
		int endIndex = 0;
		double length = 0;
		for (int i = 0; i < count; i++) {
			startIndex = ((i == 0) ? count - 1 : i - 1);
			endIndex = i;
			length = expand / pointMultMult(ndpList.get(startIndex), ndpList.get(endIndex));
			point = pointSub(ndpList.get(endIndex), ndpList.get(startIndex));
			point = pointMult(point, length);
			point = pointAdd(pList.get(i), point);

			newList.add(point);
		}

		Jwd[] rstJwds = new Jwd[newList.size()];
		for (int i = 0; i < newList.size(); i++) {
			rstJwds[i] = coord.xYtoLq(newList.get(i).x, newList.get(i).y);
		}
		return rstJwds;
	}

	// 处理鹿回头
	public static void luhuitouPolygon() {
		double scCenterLng = 109.49722222;
		double scCenterLat = 18.22694444;
		double scRadius = 600;
		Jwd jwd = null;
		Coord coord = new Coord();
		// 求小圆的圆心投影坐标
		Point scCenterPoint = coord.lQtoXy(scCenterLng, scCenterLat);
		// 求小圆的投影多边形（逆时针）
		Jwd scCenterJwd = new Jwd(scCenterLng, scCenterLat);
		Point[] scPoints = SpaceUtil.getCircleProjPoints(scCenterJwd, scRadius);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < scPoints.length; i++) {
			sb.append(scPoints[i].x).append(",").append(scPoints[i].y).append("|");
		}
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);
		System.out.println(str);
		// 计算平行于多边形左上边并且穿过小圆圆心的延长线
		double p11x = 3.753409199964018E7;
		double p11y = 2005322.3221215273;
		double p12x = 3.757250170490414E7;
		double p12y = 2021691.1113396403;
		double pcx = scCenterPoint.x;
		double pcy = scCenterPoint.y;
		double angle = (p12y - p11y) / (p12x - p11x);
		double p21x = 3.752624626622146E7;
		double p21y = pcy - angle * (pcx - p21x);// angle*(p2.x - p0.x) + p0.y
		Point p21 = new Point(p21x, p21y);
		jwd = coord.xYtoLq(p21x, p21y);
		System.out.println(jwd.getJd() + "," + jwd.getWd());
		double p22x = p12x;
		double p22y = angle * (p22x - p21x) + p21y;
		Point p22 = new Point(p22x, p22y);
		jwd = coord.xYtoLq(p22x, p22y);
		System.out.println(jwd.getJd() + "," + jwd.getWd());
		// 计算延长线与小圆的交点
		Point p31 = null, p32 = null;
		Object[] objs = null;
		boolean isIntersect = false;
		Point point = null;
		for (int i = 0; i < scPoints.length; i++) {
			if (i == 0) {
				p31 = scPoints[scPoints.length - 1];
				p32 = scPoints[0];
			} else {
				p31 = scPoints[i - 1];
				p32 = scPoints[i];
			}
			objs = crossPoint(p21, p22, p31, p32);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("小圆交点：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点所在线段：" + p31.x + "," + p31.y + " - " + p32.x + "," + p32.y);
			}
		}
		// 计算延长线与多边形交点
		String polygonStr = "3.752624626622146E7,2023615.1503347408|3.756461880176331E7,2039969.2667274387|3.757250170490414E7,2021691.1113396403|3.753409199964018E7,2005322.3221215273";
		String[] polygonStrs = polygonStr.split("\\|");
		Point[] polygonPoints = new Point[polygonStrs.length];
		double x = 0, y = 0;
		int loc = 0;
		for (int i = 0; i < polygonStrs.length; i++) {
			loc = polygonStrs[i].indexOf(",");
			x = Double.parseDouble(polygonStrs[i].substring(0, loc));
			y = Double.parseDouble(polygonStrs[i].substring(loc + 1));
			polygonPoints[i] = new Point(x, y);
		}
		for (int i = 0; i < polygonPoints.length; i++) {
			if (i == 0) {
				p31 = polygonPoints[polygonPoints.length - 1];
				p32 = polygonPoints[0];
			} else {
				p31 = polygonPoints[i - 1];
				p32 = polygonPoints[i];
			}
			objs = crossPoint(p21, p22, p31, p32);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("多边形交点：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点所在线段：" + p31.x + "," + p31.y + " - " + p32.x + "," + p32.y);
			}
		}
	}

	public static void luhuitouCircle() {
		// 根据两个圆（大圆套小圆）的圆心，计算其延长线与两个圆的交点
		double bcCenterLng = 109.478056;
		double bcCenterLat = 18.211944;
		double bcRadius = 8000;
		double scCenterLng = 109.49722222;
		double scCenterLat = 18.22694444;
		double scRadius = 600;
		Jwd jwd = null;
		Coord coord = new Coord();
		// 求大圆的圆心投影坐标
		Point bcCenterPoint = coord.lQtoXy(bcCenterLng, bcCenterLat);
		System.out.println(bcCenterPoint.x + "," + bcCenterPoint.y);
		// 求大圆的投影多边形（逆时针）
		Jwd bcCenterJwd = new Jwd(bcCenterLng, bcCenterLat);
		Point[] bcPoints = SpaceUtil.getCircleProjPoints(bcCenterJwd, bcRadius);
		// 求小圆的圆心投影坐标
		Point scCenterPoint = coord.lQtoXy(scCenterLng, scCenterLat);
		System.out.println(scCenterPoint.x + "," + scCenterPoint.y);
		// 求大圆的投影多边形（顺时针）
		Jwd scCenterJwd = new Jwd(scCenterLng, scCenterLat);
		Point[] scPoints = SpaceUtil.getCircleProjPoints(scCenterJwd, scRadius);
		// 重新排序为顺时针
		Point[] pnts = new Point[scPoints.length];
		int idx = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = scPoints.length - 1; i >= 0; i--) {
			pnts[idx] = scPoints[i];
			idx++;

			sb.append(scPoints[i].x).append(",").append(scPoints[i].y).append("|");
		}
		scPoints = pnts;
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);
		System.out.println(str);
		// 大圆圆心到小圆圆心连线的延长线
		double angle = (scCenterPoint.y - bcCenterPoint.y) / (scCenterPoint.x - bcCenterPoint.x);// (p1.y-p0.y)/(p1.x-p0.x)
		double x = bcCenterPoint.x - (scCenterPoint.x - bcCenterPoint.x) * 4;
		double y = angle * (x - bcCenterPoint.x) + bcCenterPoint.y;// angle*(p2.x
																	// - p0.x) +
																	// p0.y
//				Jwd jwd=coord.xYtoLq(x, y);
//				System.out.println(jwd.getJd()+","+jwd.getWd());
		Point p11 = new Point(x, y);
		x = bcCenterPoint.x + (scCenterPoint.x - bcCenterPoint.x) * 4;
		y = angle * (x - bcCenterPoint.x) + bcCenterPoint.y;// angle*(p2.x -
															// p0.x) + p0.y
//				Jwd jwd=coord.xYtoLq(x, y);
//				System.out.println(jwd.getJd()+","+jwd.getWd());
		Point p12 = new Point(x, y);
		Boolean isIntersect = false;
		Point point = null;
		Object[] objs = null;
		Point p21 = null;
		Point p22 = null;
		// 计算两个圆心连线延长线与大圆的交点
		for (int i = 0; i < bcPoints.length; i++) {
			if (i == 0) {
				p21 = bcPoints[bcPoints.length - 1];
				p22 = bcPoints[0];
			} else {
				p21 = bcPoints[i - 1];
				p22 = bcPoints[i];
			}
			objs = crossPoint(p11, p12, p21, p22);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("大圆交点：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点所在线段：" + p21.x + "," + p21.y + " - " + p22.x + "," + p22.y);
			}
		}
		// 计算两个圆心连线延长线与小圆的交点
		for (int i = 0; i < scPoints.length; i++) {
			if (i == 0) {
				p21 = scPoints[scPoints.length - 1];
				p22 = scPoints[0];
			} else {
				p21 = scPoints[i - 1];
				p22 = scPoints[i];
			}
			objs = crossPoint(p11, p12, p21, p22);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("小圆交点：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点所在线段：" + p21.x + "," + p21.y + " - " + p22.x + "," + p22.y);
			}
		}
	}

	public static void fenghlPolygon1() {
		double scCenterLng = 109.54361111;
		double scCenterLat = 18.26472222;
		double scRadius = 2000;
		Jwd jwd = null;
		Coord coord = new Coord();
		// 求小圆的圆心投影坐标
//		Point scCenterPoint=coord.lQtoXy(scCenterLng, scCenterLat);
		// 求小圆的投影多边形（逆时针）
		Jwd scCenterJwd = new Jwd(scCenterLng, scCenterLat);
		Point[] scPoints = SpaceUtil.getCircleProjPoints(scCenterJwd, scRadius);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < scPoints.length; i++) {
			sb.append(scPoints[i].x).append(",").append(scPoints[i].y).append("|");
		}
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);
		System.out.println(str);
		// 计算平行于多边形左上边并且穿过小圆圆心的延长线
		Point p21 = new Point(3.753301648866485E7, 2007829.9446048273);
		Point p22 = new Point(3.757142115692653E7, 2024196.587239727);
		// 计算延长线与小圆的交点
		Point p31 = null, p32 = null;
		Object[] objs = null;
		boolean isIntersect = false;
		Point point = null;
		for (int i = 0; i < scPoints.length; i++) {
			if (i == 0) {
				p31 = scPoints[scPoints.length - 1];
				p32 = scPoints[0];
			} else {
				p31 = scPoints[i - 1];
				p32 = scPoints[i];
			}
			objs = crossPoint(p21, p22, p31, p32);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("小圆交点：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点所在线段：" + p31.x + "," + p31.y + " - " + p32.x + "," + p32.y);
			}
		}
	}

	public static void fenghlPolygon2() {
		double scCenterLng = 109.54361111;
		double scCenterLat = 18.26472222;
		double scRadius = 2000;
		Jwd jwd = null;
		Coord coord = new Coord();
		// 求小圆的圆心投影坐标
//		Point scCenterPoint=coord.lQtoXy(scCenterLng, scCenterLat);
		// 求小圆的投影多边形（逆时针）
		Jwd scCenterJwd = new Jwd(scCenterLng, scCenterLat);
		Point[] scPoints = SpaceUtil.getCircleProjPoints(scCenterJwd, scRadius);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < scPoints.length; i++) {
			sb.append(scPoints[i].x).append(",").append(scPoints[i].y).append("|");
		}
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);
		System.out.println(str);
		// 计算平行于多边形左上边并且穿过小圆圆心的延长线
		Point p21 = new Point(3.7556373894080445E7, 2023018.5291855435);
		Point p22 = new Point(3.755897842151458E7, 2021040.76073016);
		// 计算延长线与小圆的交点
		Point p31 = null, p32 = null;
		Object[] objs = null;
		boolean isIntersect = false;
		Point point = null;
		for (int i = 0; i < scPoints.length; i++) {
			if (i == 0) {
				p31 = scPoints[scPoints.length - 1];
				p32 = scPoints[0];
			} else {
				p31 = scPoints[i - 1];
				p32 = scPoints[i];
			}
			objs = crossPoint(p21, p22, p31, p32);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("交点一：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点一所在线段：" + p31.x + "," + p31.y + " - " + p32.x + "," + p32.y);
			}
		}
		// 计算平行于多边形左上边并且穿过小圆圆心的延长线
		p21 = new Point(3.75506151286853E7, 2014665.7103364884);
		p22 = new Point(3.755897842151458E7, 2021040.76073016);
		// 计算延长线与小圆的交点
		for (int i = 0; i < scPoints.length; i++) {
			if (i == 0) {
				p31 = scPoints[scPoints.length - 1];
				p32 = scPoints[0];
			} else {
				p31 = scPoints[i - 1];
				p32 = scPoints[i];
			}
			objs = crossPoint(p21, p22, p31, p32);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("交点二：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点二所在线段：" + p31.x + "," + p31.y + " - " + p32.x + "," + p32.y);
			}
		}
		// 计算平行于多边形左上边并且穿过小圆圆心的延长线
		p21 = new Point(3.755897842151458E7, 2021040.76073016);
		p22 = new Point(3.7562761937413506E7, 2018167.7180813784);
		// 计算延长线与小圆的交点
		for (int i = 0; i < scPoints.length; i++) {
			if (i == 0) {
				p31 = scPoints[scPoints.length - 1];
				p32 = scPoints[0];
			} else {
				p31 = scPoints[i - 1];
				p32 = scPoints[i];
			}
			objs = crossPoint(p21, p22, p31, p32);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("交点三：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点三所在线段：" + p31.x + "," + p31.y + " - " + p32.x + "," + p32.y);
			}
		}
	}

	public static void fenghlLuhuitou() {
		double scCenterLng = 109.54361111;
		double scCenterLat = 18.26472222;
		double scRadius = 2000;
		Jwd jwd = null;
		Coord coord = new Coord();
		// 求小圆的圆心投影坐标
//		Point scCenterPoint=coord.lQtoXy(scCenterLng, scCenterLat);
		// 求小圆的投影多边形（逆时针）
		Jwd scCenterJwd = new Jwd(scCenterLng, scCenterLat);
		Point[] scPoints = SpaceUtil.getCircleProjPoints(scCenterJwd, scRadius);
		// 重新排序为顺时针
		Point[] pnts = new Point[scPoints.length];
		int idx = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = scPoints.length - 1; i >= 0; i--) {
			pnts[idx] = scPoints[i];
			idx++;

			sb.append(scPoints[i].x).append(",").append(scPoints[i].y).append("|");
		}
		scPoints = pnts;
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);
		System.out.println(str);
		// 获取多边形点集
		str = "3.7556743730639435E7,2019591.343687423|3.7556732266302705E7,2019605.5617136557|3.755660217140639E7,2019758.8558416897|3.7556468265299365E7,2019908.8322606343|3.7556330632559605E7,2020055.3962421685|3.755618936011889E7,2020198.4552133405|3.755604453720793E7,2020337.9188150375|3.755589625529997E7,2020473.6989590595|3.755574460805306E7,2020605.7098837576|3.755558969125082E7,2020733.8682082023|3.755543160274205E7,2020858.092984849|3.755527044237884E7,2020978.3057506657|3.7555106311953515E7,2021094.4305766935|3.755493931513438E7,2021206.3941160033|3.755476955740019E7,2021314.1256500236|3.7554597145973586E7,2021417.5571332087|3.755442218975332E7,2021516.623236017|3.755424479924547E7,2021611.2613861745|3.755406508649371E7,2021701.4118081972|3.755388316500845E7,2021787.017561146|3.7553699149695195E7,2021868.0245745932|3.755351315678198E7,2021944.3816827722|3.7553325303745896E7,2022016.040656897|3.755313570923896E7,2022082.9562356242|3.7552944493013114E7,2022145.0861536392|3.755275177584464E7,2022202.3911683548|3.755255767945782E7,2022254.8350846951|3.75523623264481E7,2022302.3847779587|3.755216584020462E7,2022345.0102147402|3.75519683448323E7,2022382.6844718999|3.755176996507347E7,2022415.3837535693|3.755157082622901E7,2022443.087406182|3.755137105407931E7,2022465.7779315165|3.7551170774804726E7,2022483.440997751|3.755097011490594E7,2022496.0654485142|3.75505681603605E7,2022506.1697956661|3.75505681603605E7,2022506.1697956661|3.755036711959695E7,2022503.6433099324|3.755016620581506E7,2022496.0654485142|3.7549965545916274E7,2022483.440997751|3.754976526664169E7,2022465.7779315165|3.754956549449199E7,2022443.087406182|3.7549366355647534E7,2022415.3837535693|3.75491679758887E7,2022382.6844718999|3.754897048051638E7,2022345.0102147402|3.75487739942729E7,2022302.3847779587|3.754857864126318E7,2022254.8350846951|3.754838454487636E7,2022202.3911683548|3.754819182770789E7,2022145.0861536392|3.754800061148204E7,2022082.9562356242|3.7547811016975105E7,2022016.040656897|3.754762316393902E7,2021944.3816827722|3.7547437171025805E7,2021868.0245745932|3.754725315571255E7,2021787.017561146|3.754707123422729E7,2021701.4118081972|3.754689152147553E7,2021611.2613861745|3.7546714130967684E7,2021516.623236017|3.7546539174747415E7,2021417.5571332087|3.754636676332081E7,2021314.1256500236|3.7546197005586624E7,2021206.3941160033|3.7546030008767486E7,2021094.4305766935|3.754586587834216E7,2020978.3057506657|3.754570471797895E7,2020858.092984849|3.754554662947018E7,2020733.8682082023|3.754539171266794E7,2020605.7098837576|3.754524006542103E7,2020473.6989590595|3.754509178351307E7,2020337.9188150375|3.754494696060211E7,2020198.4552133405|3.7544805688161395E7,2020055.3962421685|3.7544668055421636E7,2019908.8322606343|3.754453414931461E7,2019758.8558416897|3.7544404054418296E7,2019605.5617136557|3.754427785290341E7,2019449.046700389|3.754415562448154E7,2019289.4096601263|3.754403744635476E7,2019126.7514230444|3.754392339316694E7,2018961.1747275717|3.754381353695648E7,2018792.784155498|3.754370794711095E7,2018621.686065918|3.7543606690323144E7,2018447.9885280523|3.7543509830549024E7,2018271.8012529889|3.754341742896729E7,2018093.2355243864|3.7543329543940775E7,2017912.4041281866|3.754324623097953E7,2017729.4212813755|3.754316754270583E7,2017544.4025598406|3.754309352882085E7,2017357.4648253722|3.754302423607336E7,2017168.72615185|3.754295970823014E7,2016978.3057506657|3.754289998604836E7,2016786.323895426|3.754284510724983E7,2016592.9018459853|3.754279510649718E7,2016398.161771856|3.7542750015371956E7,2016202.2266750426|3.754270986235467E7,2016005.2203123518|3.754267467280684E7,2015807.267117225|3.754264446895493E7,2015608.4921211433|3.754261926987634E7,2015409.020874654|3.7542599091487356E7,2015208.979368072|3.754258394653308E7,2015008.4939519006|3.754257384457938E7,2014807.6912570256|3.754256879200687E7,2014606.698114733|3.754256879200687E7,2014405.6414765993|3.754257384457938E7,2014204.6483343067|3.754258394653308E7,2014003.8456394316|3.7542599091487356E7,2013803.3602232602|3.754261926987634E7,2013603.3187166783|3.754264446895493E7,2013403.847470189|3.754267467280684E7,2013205.0724741071|3.754270986235467E7,2013007.1192789804|3.7542750015371956E7,2012810.1129162896|3.754279510649718E7,2012614.1778194762|3.754284510724983E7,2012419.437745347|3.754289998604836E7,2012226.0156959062|3.754295970823014E7,2012034.0338406665|3.754302423607336E7,2011843.6134394822|3.754309352882085E7,2011654.87476596|3.754316754270583E7,2011467.9370314917|3.754324623097953E7,2011282.9183099568|3.7543329543940775E7,2011099.9354631456|3.754341742896729E7,2010919.1040669458|3.7543509830549024E7,2010740.5383383434|3.7543606690323144E7,2010564.35106328|3.754370794711095E7,2010390.6535254142|3.754381353695648E7,2010219.5554358342|3.754392339316694E7,2010051.1648637606|3.754403744635476E7,2009885.5881682879|3.754415562448154E7,2009722.929931206|3.754427785290341E7,2009563.2928909434|3.754439259010694E7,2009420.9959256195|3.755212791505754E7,2015790.5246818014|3.75521131563007E7,2015809.1461921693|3.755209838472604E7,2015829.477520476|3.7552084477496214E7,2015850.4095840994|3.7552071459009245E7,2015871.9056610868|3.755205935210398E7,2015893.9280400148|3.755204817802004E7,2015916.4380861477|3.755203795636056E7,2015939.3963092156|3.755202870505782E7,2015962.7624326937|3.755202044034174E7,2015986.4954644619|3.755201317671144E7,2016010.5537687177|3.75520069269098E7,2016034.8951390204|3.755200170190108E7,2016059.4768723354|3.755199751085174E7,2016084.2558439493|3.755199436111429E7,2016109.1885831263|3.755199225821446E7,2016134.2313493693|3.755199120584143E7,2016159.3402091567|3.755199120584143E7,2016184.4711130168|3.755199225821446E7,2016209.5799728043|3.755199436111429E7,2016234.6227390473|3.755199751085174E7,2016259.5554782243|3.755200170190108E7,2016284.3344498381|3.75520069269098E7,2016308.9161831532|3.755201317671144E7,2016333.257553456|3.755202044034174E7,2016357.3158577117|3.755202870505782E7,2016381.0488894798|3.755203795636056E7,2016404.415012958|3.755204817802004E7,2016427.373236026|3.755205935210398E7,2016449.8832821588|3.7552071459009245E7,2016471.9056610868|3.7552084477496214E7,2016493.4017380741|3.755209838472604E7,2016514.3338016975|3.75521131563007E7,2016534.6651300043|3.755212876630585E7,2016554.360054936|3.755214518735623E7,2016573.384024902|3.755216239064374E7,2016591.7036653948|3.755218034598796E7,2016609.2868375396|3.755219902188912E7,2016626.102694478|3.755221838558335E7,2016642.1217354822|3.755223840310014E7,2016657.3158577117|3.7552259039321974E7,2016671.6584055128|3.75522802580459E7,2016685.124217183|3.755230202204706E7,2016697.6896691131|3.755232429314401E7,2016709.3327172305|3.755234703226567E7,2016720.0329356724|3.755237019951991E7,2016729.7715526198|3.7552393754263476E7,2016738.5314832292|3.7552417655173354E7,2016746.297359606|3.755244186031922E7,2016753.055557764|3.7552466327237025E7,2016758.7942215272|3.755249101300349E7,2016763.503283329|3.755251587431138E7,2016767.1744818755|3.7552540867545515E7,2016769.8013766364|3.755256594885928E7,2016771.379359146|3.755259107425152E7,2016771.9056610868|3.755261619964375E7,2016771.379359146|3.755264128095752E7,2016769.8013766364|3.7552666274191655E7,2016767.1744818755|3.7552691135499544E7,2016763.503283329|3.755271582126601E7,2016758.7942215272|3.7552740288183816E7,2016753.055557764|3.755276449332968E7,2016746.297359606|3.755278839423956E7,2016738.5314832292|3.7552811948983125E7,2016729.7715526198|3.7552835116237365E7,2016720.0329356724|3.7552857855359025E7,2016709.3327172305|3.755288012645598E7,2016697.6896691131|3.755290189045714E7,2016685.124217183|3.755292310918106E7,2016671.6584055128|3.7552943745402895E7,2016657.3158577117|3.755296376291969E7,2016642.1217354822|3.7552983126613915E7,2016626.102694478|3.7553001802515075E7,2016609.2868375396|3.75530197578593E7,2016591.7036653948|3.75530369611468E7,2016573.384024902|3.755305338219719E7,2016554.360054936|3.755305423329683E7,2016553.2865179905";
		String[] strs = str.split("\\|");
		Point[] polygonPoints = new Point[strs.length];
		double x = 0, y = 0;
		int loc = 0;
		for (int i = 0; i < strs.length; i++) {
			loc = strs[i].indexOf(",");
			x = Double.parseDouble(strs[i].substring(0, loc));
			y = Double.parseDouble(strs[i].substring(loc + 1));
			polygonPoints[i] = new Point(x, y);
		}
		// 计算多边形点集与小圆的交点
		Point p21 = null, p22 = null, p31 = null, p32 = null;
		Object[] objs = null;
		boolean isIntersect = false;
		Point point = null;
		for (int j = 0; j < polygonPoints.length; j++) {
			if (j == 0) {
				p21 = polygonPoints[polygonPoints.length - 1];
				p22 = polygonPoints[0];
			} else {
				p21 = polygonPoints[j - 1];
				p22 = polygonPoints[j];
			}
			for (int i = 0; i < scPoints.length; i++) {
				if (i == 0) {
					p31 = scPoints[scPoints.length - 1];
					p32 = scPoints[0];
				} else {
					p31 = scPoints[i - 1];
					p32 = scPoints[i];
				}
				objs = crossPoint(p21, p22, p31, p32);
				isIntersect = (Boolean) objs[0];
				if (isIntersect) {
					point = (Point) objs[1];
					System.out.println("交点一：" + point.x + "," + point.y);
					jwd = coord.xYtoLq(point.x, point.y);
					System.out.println(jwd.getJd() + "," + jwd.getWd());
					System.out.println("交点一所在线段：     多边形-" + p21.x + "," + p21.y + " - " + p22.x + "," + p22.y + "     原型-" + p31.x + "," + p31.y
							+ " - " + p32.x + "," + p32.y);
				}
			}
		}
		// 3.754439259010694E7,2009420.9959256195|3.7544404054418296E7,2009406.7778776765|3.754453414931461E7,2009253.4837496425|3.7544668055421636E7,2009103.507330698|3.7544805688161395E7,2008956.9433491637|3.754494696060211E7,2008813.8843779918|3.754509178351307E7,2008674.4207762948|3.754524006542103E7,2008538.6406322727|3.754539171266794E7,2008406.6297075746|3.754554662947018E7,2008278.47138313|3.754570471797895E7,2008154.2466064834|3.754586587834216E7,2008034.0338406665|3.7546030008767486E7,2007917.9090146387|3.7546197005586624E7,2007805.945475329|3.754636676332081E7,2007698.2139413087|3.7546539174747415E7,2007594.7824581235|3.7546714130967684E7,2007495.7163553152|3.754689152147553E7,2007401.0782051578|3.754707123422729E7,2007310.927783135|3.754725315571255E7,2007225.3220301862|3.7547437171025805E7,2007144.315016739|3.754762316393902E7,2007067.95790856|3.7547811016975105E7,2006996.2989344352|3.754800061148204E7,2006929.383355708|3.754819182770789E7,2006867.253437693|3.754838454487636E7,2006809.9484229775|3.754857864126318E7,2006757.504506637|3.75487739942729E7,2006709.9548133735|3.754897048051638E7,2006667.329376592|3.75491679758887E7,2006629.6551194324|3.7549366355647534E7,2006596.955837763|3.754956549449199E7,2006569.2521851503|3.754976526664169E7,2006546.5616598157|3.7549965545916274E7,2006528.8985935813|3.755016620581506E7,2006516.274142818|3.755036711959695E7,2006508.6962813998|3.75505681603605E7,2006506.1697956661|3.755076920112405E7,2006508.6962813998|3.755097011490594E7,2006516.274142818|3.7551170774804726E7,2006528.8985935813|3.755137105407931E7,2006546.5616598157|3.755157082622901E7,2006569.2521851503|3.755176996507347E7,2006596.955837763|3.75519683448323E7,2006629.6551194324|3.755216584020462E7,2006667.329376592|3.75523623264481E7,2006709.9548133735|3.755255767945782E7,2006757.504506637|3.755275177584464E7,2006809.9484229775|3.7552944493013114E7,2006867.253437693|3.755313570923896E7,2006929.383355708|3.7553325303745896E7,2006996.2989344352|3.755351315678198E7,2007067.95790856|3.7553699149695195E7,2007144.315016739|3.755388316500845E7,2007225.3220301862|3.755406508649371E7,2007310.927783135|3.755424479924547E7,2007401.0782051578|3.755442218975332E7,2007495.7163553152|3.7554597145973586E7,2007594.7824581235|3.755476955740019E7,2007698.2139413087|3.755493931513438E7,2007805.945475329|3.7555106311953515E7,2007917.9090146387|3.755527044237884E7,2008034.0338406665|3.755543160274205E7,2008154.2466064834|3.755558969125082E7,2008278.47138313|3.755574460805306E7,2008406.6297075746|3.755589625529997E7,2008538.6406322727|3.755604453720793E7,2008674.4207762948|3.755618936011889E7,2008813.8843779918|3.7556330632559605E7,2008956.9433491637|3.7556468265299365E7,2009103.507330698|3.755660217140639E7,2009253.4837496425|3.7556732266302705E7,2009406.7778776765|3.755685846781759E7,2009563.2928909434|3.7556980696239464E7,2009722.929931206|3.755709887436624E7,2009885.5881682879|3.755721292755406E7,2010051.1648637606|3.755732278376452E7,2010219.5554358342|3.755742837361005E7,2010390.6535254142|3.7557529630397856E7,2010564.35106328|3.755762649017198E7,2010740.5383383434|3.755771889175371E7,2010919.1040669458|3.7557806776780225E7,2011099.9354631456|3.755789008974147E7,2011282.9183099568|3.7557968778015174E7,2011467.9370314917|3.755804279190015E7,2011654.87476596|3.755811208464764E7,2011843.6134394822|3.755817661249086E7,2012034.0338406665|3.755823633467264E7,2012226.0156959062|3.755829121347117E7,2012419.437745347|3.755834121422382E7,2012614.1778194762|3.7558386305349045E7,2012810.1129162896|3.755842645836633E7,2013007.1192789804|3.7558461647914164E7,2013205.0724741071|3.755849185176607E7,2013403.847470189|3.755851705084466E7,2013603.3187166783|3.7558537229233645E7,2013803.3602232602|3.7558552374187924E7,2014003.8456394316|3.7558562476141624E7,2014204.6483343067|3.755856752871413E7,2014405.6414765993|3.755856752871413E7,2014606.698114733|3.7558562476141624E7,2014807.6912570256|3.7558552374187924E7,2015008.4939519006|3.7558537229233645E7,2015208.979368072|3.755851705084466E7,2015409.020874654|3.755849185176607E7,2015608.4921211433|3.7558461647914164E7,2015807.267117225|3.755842645836633E7,2016005.2203123518|3.7558386305349045E7,2016202.2266750426|3.755834121422382E7,2016398.161771856|3.755829121347117E7,2016592.9018459853|3.755823633467264E7,2016786.323895426|3.755817661249086E7,2016978.3057506657|3.755811208464764E7,2017168.72615185|3.755804279190015E7,2017357.4648253722|3.7557968778015174E7,2017544.4025598406|3.755789008974147E7,2017729.4212813755|3.7557806776780225E7,2017912.4041281866|3.755771889175371E7,2018093.2355243864|3.755762649017198E7,2018271.8012529889|3.7557529630397856E7,2018447.9885280523|3.755742837361005E7,2018621.686065918|3.755732278376452E7,2018792.784155498|3.755721292755406E7,2018961.1747275717|3.755709887436624E7,2019126.7514230444|3.7556980696239464E7,2019289.4096601263|3.755685846781759E7,2019449.046700389|3.7556743730639435E7,2019591.343687423|3.755305423329683E7,2016553.2865179905|3.7553068992202334E7,2016534.6651300043|3.7553083763776995E7,2016514.3338016975|3.755309767100682E7,2016493.4017380741|3.755311068949379E7,2016471.9056610868|3.755312279639906E7,2016449.8832821588|3.7553133970483E7,2016427.373236026|3.755314419214247E7,2016404.415012958|3.755315344344521E7,2016381.0488894798|3.7553161708161294E7,2016357.3158577117|3.7553168971791595E7,2016333.257553456|3.755317522159324E7,2016308.9161831532|3.755318044660196E7,2016284.3344498381|3.7553184637651294E7,2016259.5554782243|3.755318778738874E7,2016234.6227390473|3.755318989028858E7,2016209.5799728043|3.7553190942661606E7,2016184.4711130168|3.7553190942661606E7,2016159.3402091567|3.755318989028858E7,2016134.2313493693|3.755318778738874E7,2016109.1885831263|3.7553184637651294E7,2016084.2558439493|3.755318044660196E7,2016059.4768723354|3.755317522159324E7,2016034.8951390204|3.7553168971791595E7,2016010.5537687177|3.7553161708161294E7,2015986.4954644619|3.755315344344521E7,2015962.7624326937|3.755314419214247E7,2015939.3963092156|3.7553133970483E7,2015916.4380861477|3.755312279639906E7,2015893.9280400148|3.755311068949379E7,2015871.9056610868|3.755309767100682E7,2015850.4095840994|3.7553083763776995E7,2015829.477520476|3.7553068992202334E7,2015809.1461921693|3.755305338219719E7,2015789.4512672375|3.75530369611468E7,2015770.4272972716|3.75530197578593E7,2015752.1076567788|3.7553001802515075E7,2015734.524484634|3.7552983126613915E7,2015717.7086276957|3.755296376291969E7,2015701.6895866913|3.7552943745402895E7,2015686.4954644619|3.755292310918106E7,2015672.1529166608|3.755290189045714E7,2015658.6871049905|3.755288012645598E7,2015646.1216530604|3.7552857855359025E7,2015634.478604943|3.7552835116237365E7,2015623.7783865011|3.7552811948983125E7,2015614.0397695538|3.755278839423956E7,2015605.2798389443|3.755276449332968E7,2015597.5139625676|3.7552740288183816E7,2015590.7557644097|3.755271582126601E7,2015585.0171006464|3.7552691135499544E7,2015580.3080388445|3.7552666274191655E7,2015576.636840298|3.755264128095752E7,2015574.0099455372|3.755261619964375E7,2015572.4319630275|3.755259107425152E7,2015571.9056610868|3.755256594885928E7,2015572.4319630275|3.7552540867545515E7,2015574.0099455372|3.755251587431138E7,2015576.636840298|3.755249101300349E7,2015580.3080388445|3.7552466327237025E7,2015585.0171006464|3.755244186031922E7,2015590.7557644097|3.7552417655173354E7,2015597.5139625676|3.7552393754263476E7,2015605.2798389443|3.755237019951991E7,2015614.0397695538|3.755234703226567E7,2015623.7783865011|3.755232429314401E7,2015634.478604943|3.755230202204706E7,2015646.1216530604|3.75522802580459E7,2015658.6871049905|3.7552259039321974E7,2015672.1529166608|3.755223840310014E7,2015686.4954644619|3.755221838558335E7,2015701.6895866913|3.755219902188912E7,2015717.7086276957|3.755218034598796E7,2015734.524484634|3.755216239064374E7,2015752.1076567788|3.755214518735623E7,2015770.4272972716|3.755212876630585E7,2015789.4512672375|3.755212791505754E7,2015790.5246818014
		// 获取多边形点集
		str = "3.754439259010694E7,2009420.9959256195|3.7544404054418296E7,2009406.7778776765|3.754453414931461E7,2009253.4837496425|3.7544668055421636E7,2009103.507330698|3.7544805688161395E7,2008956.9433491637|3.754494696060211E7,2008813.8843779918|3.754509178351307E7,2008674.4207762948|3.754524006542103E7,2008538.6406322727|3.754539171266794E7,2008406.6297075746|3.754554662947018E7,2008278.47138313|3.754570471797895E7,2008154.2466064834|3.754586587834216E7,2008034.0338406665|3.7546030008767486E7,2007917.9090146387|3.7546197005586624E7,2007805.945475329|3.754636676332081E7,2007698.2139413087|3.7546539174747415E7,2007594.7824581235|3.7546714130967684E7,2007495.7163553152|3.754689152147553E7,2007401.0782051578|3.754707123422729E7,2007310.927783135|3.754725315571255E7,2007225.3220301862|3.7547437171025805E7,2007144.315016739|3.754762316393902E7,2007067.95790856|3.7547811016975105E7,2006996.2989344352|3.754800061148204E7,2006929.383355708|3.754819182770789E7,2006867.253437693|3.754838454487636E7,2006809.9484229775|3.754857864126318E7,2006757.504506637|3.75487739942729E7,2006709.9548133735|3.754897048051638E7,2006667.329376592|3.75491679758887E7,2006629.6551194324|3.7549366355647534E7,2006596.955837763|3.754956549449199E7,2006569.2521851503|3.754976526664169E7,2006546.5616598157|3.7549965545916274E7,2006528.8985935813|3.755016620581506E7,2006516.274142818|3.755036711959695E7,2006508.6962813998|3.75505681603605E7,2006506.1697956661|3.755076920112405E7,2006508.6962813998|3.755097011490594E7,2006516.274142818|3.7551170774804726E7,2006528.8985935813|3.755137105407931E7,2006546.5616598157|3.755157082622901E7,2006569.2521851503|3.755176996507347E7,2006596.955837763|3.75519683448323E7,2006629.6551194324|3.755216584020462E7,2006667.329376592|3.75523623264481E7,2006709.9548133735|3.755255767945782E7,2006757.504506637|3.755275177584464E7,2006809.9484229775|3.7552944493013114E7,2006867.253437693|3.755313570923896E7,2006929.383355708|3.7553325303745896E7,2006996.2989344352|3.755351315678198E7,2007067.95790856|3.7553699149695195E7,2007144.315016739|3.755388316500845E7,2007225.3220301862|3.755406508649371E7,2007310.927783135|3.755424479924547E7,2007401.0782051578|3.755442218975332E7,2007495.7163553152|3.7554597145973586E7,2007594.7824581235|3.755476955740019E7,2007698.2139413087|3.755493931513438E7,2007805.945475329|3.7555106311953515E7,2007917.9090146387|3.755527044237884E7,2008034.0338406665|3.755543160274205E7,2008154.2466064834|3.755558969125082E7,2008278.47138313|3.755574460805306E7,2008406.6297075746|3.755589625529997E7,2008538.6406322727|3.755604453720793E7,2008674.4207762948|3.755618936011889E7,2008813.8843779918|3.7556330632559605E7,2008956.9433491637|3.7556468265299365E7,2009103.507330698|3.755660217140639E7,2009253.4837496425|3.7556732266302705E7,2009406.7778776765|3.755685846781759E7,2009563.2928909434|3.7556980696239464E7,2009722.929931206|3.755709887436624E7,2009885.5881682879|3.755721292755406E7,2010051.1648637606|3.755732278376452E7,2010219.5554358342|3.755742837361005E7,2010390.6535254142|3.7557529630397856E7,2010564.35106328|3.755762649017198E7,2010740.5383383434|3.755771889175371E7,2010919.1040669458|3.7557806776780225E7,2011099.9354631456|3.755789008974147E7,2011282.9183099568|3.7557968778015174E7,2011467.9370314917|3.755804279190015E7,2011654.87476596|3.755811208464764E7,2011843.6134394822|3.755817661249086E7,2012034.0338406665|3.755823633467264E7,2012226.0156959062|3.755829121347117E7,2012419.437745347|3.755834121422382E7,2012614.1778194762|3.7558386305349045E7,2012810.1129162896|3.755842645836633E7,2013007.1192789804|3.7558461647914164E7,2013205.0724741071|3.755849185176607E7,2013403.847470189|3.755851705084466E7,2013603.3187166783|3.7558537229233645E7,2013803.3602232602|3.7558552374187924E7,2014003.8456394316|3.7558562476141624E7,2014204.6483343067|3.755856752871413E7,2014405.6414765993|3.755856752871413E7,2014606.698114733|3.7558562476141624E7,2014807.6912570256|3.7558552374187924E7,2015008.4939519006|3.7558537229233645E7,2015208.979368072|3.755851705084466E7,2015409.020874654|3.755849185176607E7,2015608.4921211433|3.7558461647914164E7,2015807.267117225|3.755842645836633E7,2016005.2203123518|3.7558386305349045E7,2016202.2266750426|3.755834121422382E7,2016398.161771856|3.755829121347117E7,2016592.9018459853|3.755823633467264E7,2016786.323895426|3.755817661249086E7,2016978.3057506657|3.755811208464764E7,2017168.72615185|3.755804279190015E7,2017357.4648253722|3.7557968778015174E7,2017544.4025598406|3.755789008974147E7,2017729.4212813755|3.7557806776780225E7,2017912.4041281866|3.755771889175371E7,2018093.2355243864|3.755762649017198E7,2018271.8012529889|3.7557529630397856E7,2018447.9885280523|3.755742837361005E7,2018621.686065918|3.755732278376452E7,2018792.784155498|3.755721292755406E7,2018961.1747275717|3.755709887436624E7,2019126.7514230444|3.7556980696239464E7,2019289.4096601263|3.755685846781759E7,2019449.046700389|3.7556743730639435E7,2019591.343687423|3.755305423329683E7,2016553.2865179905|3.7553068992202334E7,2016534.6651300043|3.7553083763776995E7,2016514.3338016975|3.755309767100682E7,2016493.4017380741|3.755311068949379E7,2016471.9056610868|3.755312279639906E7,2016449.8832821588|3.7553133970483E7,2016427.373236026|3.755314419214247E7,2016404.415012958|3.755315344344521E7,2016381.0488894798|3.7553161708161294E7,2016357.3158577117|3.7553168971791595E7,2016333.257553456|3.755317522159324E7,2016308.9161831532|3.755318044660196E7,2016284.3344498381|3.7553184637651294E7,2016259.5554782243|3.755318778738874E7,2016234.6227390473|3.755318989028858E7,2016209.5799728043|3.7553190942661606E7,2016184.4711130168|3.7553190942661606E7,2016159.3402091567|3.755318989028858E7,2016134.2313493693|3.755318778738874E7,2016109.1885831263|3.7553184637651294E7,2016084.2558439493|3.755318044660196E7,2016059.4768723354|3.755317522159324E7,2016034.8951390204|3.7553168971791595E7,2016010.5537687177|3.7553161708161294E7,2015986.4954644619|3.755315344344521E7,2015962.7624326937|3.755314419214247E7,2015939.3963092156|3.7553133970483E7,2015916.4380861477|3.755312279639906E7,2015893.9280400148|3.755311068949379E7,2015871.9056610868|3.755309767100682E7,2015850.4095840994|3.7553083763776995E7,2015829.477520476|3.7553068992202334E7,2015809.1461921693|3.755305338219719E7,2015789.4512672375|3.75530369611468E7,2015770.4272972716|3.75530197578593E7,2015752.1076567788|3.7553001802515075E7,2015734.524484634|3.7552983126613915E7,2015717.7086276957|3.755296376291969E7,2015701.6895866913|3.7552943745402895E7,2015686.4954644619|3.755292310918106E7,2015672.1529166608|3.755290189045714E7,2015658.6871049905|3.755288012645598E7,2015646.1216530604|3.7552857855359025E7,2015634.478604943|3.7552835116237365E7,2015623.7783865011|3.7552811948983125E7,2015614.0397695538|3.755278839423956E7,2015605.2798389443|3.755276449332968E7,2015597.5139625676|3.7552740288183816E7,2015590.7557644097|3.755271582126601E7,2015585.0171006464|3.7552691135499544E7,2015580.3080388445|3.7552666274191655E7,2015576.636840298|3.755264128095752E7,2015574.0099455372|3.755261619964375E7,2015572.4319630275|3.755259107425152E7,2015571.9056610868|3.755256594885928E7,2015572.4319630275|3.7552540867545515E7,2015574.0099455372|3.755251587431138E7,2015576.636840298|3.755249101300349E7,2015580.3080388445|3.7552466327237025E7,2015585.0171006464|3.755244186031922E7,2015590.7557644097|3.7552417655173354E7,2015597.5139625676|3.7552393754263476E7,2015605.2798389443|3.755237019951991E7,2015614.0397695538|3.755234703226567E7,2015623.7783865011|3.755232429314401E7,2015634.478604943|3.755230202204706E7,2015646.1216530604|3.75522802580459E7,2015658.6871049905|3.7552259039321974E7,2015672.1529166608|3.755223840310014E7,2015686.4954644619|3.755221838558335E7,2015701.6895866913|3.755219902188912E7,2015717.7086276957|3.755218034598796E7,2015734.524484634|3.755216239064374E7,2015752.1076567788|3.755214518735623E7,2015770.4272972716|3.755212876630585E7,2015789.4512672375|3.755212791505754E7,2015790.5246818014";
		strs = str.split("\\|");
		polygonPoints = new Point[strs.length];
		for (int i = 0; i < strs.length; i++) {
			loc = strs[i].indexOf(",");
			x = Double.parseDouble(strs[i].substring(0, loc));
			y = Double.parseDouble(strs[i].substring(loc + 1));
			polygonPoints[i] = new Point(x, y);
		}
		// 计算多边形点集与小圆的交点
		for (int j = 0; j < polygonPoints.length; j++) {
			if (j == 0) {
				p21 = polygonPoints[polygonPoints.length - 1];
				p22 = polygonPoints[0];
			} else {
				p21 = polygonPoints[j - 1];
				p22 = polygonPoints[j];
			}
			for (int i = 0; i < scPoints.length; i++) {
				if (i == 0) {
					p31 = scPoints[scPoints.length - 1];
					p32 = scPoints[0];
				} else {
					p31 = scPoints[i - 1];
					p32 = scPoints[i];
				}
				objs = crossPoint(p21, p22, p31, p32);
				isIntersect = (Boolean) objs[0];
				if (isIntersect) {
					point = (Point) objs[1];
					System.out.println("交点二：" + point.x + "," + point.y);
					jwd = coord.xYtoLq(point.x, point.y);
					System.out.println(jwd.getJd() + "," + jwd.getWd());
					System.out.println("交点二所在线段：     多边形-" + p21.x + "," + p21.y + " - " + p22.x + "," + p22.y + "     原型-" + p31.x + "," + p31.y
							+ " - " + p32.x + "," + p32.y);
				}
			}
		}
	}

	public static void fenghlPolygon3() {
		double scCenterLng = 109.54361111;
		double scCenterLat = 18.26472222;
		double scRadius = 2000;
		Jwd jwd = null;
		Coord coord = new Coord();
		// 求小圆的圆心投影坐标
//		Point scCenterPoint=coord.lQtoXy(scCenterLng, scCenterLat);
		// 求小圆的投影多边形（逆时针）
		Jwd scCenterJwd = new Jwd(scCenterLng, scCenterLat);
		Point[] scPoints = SpaceUtil.getCircleProjPoints(scCenterJwd, scRadius);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < scPoints.length; i++) {
			sb.append(scPoints[i].x).append(",").append(scPoints[i].y).append("|");
		}
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);
		System.out.println(str);
		// 计算平行于多边形左上边并且穿过小圆圆心的延长线
		Point p21 = new Point(3.7552957421049364E7, 2016642.0803060753);
		Point p22 = new Point(3.755637736232768E7, 2019248.981931392);
		// 计算延长线与小圆的交点
		Point p31 = null, p32 = null;
		Object[] objs = null;
		boolean isIntersect = false;
		Point point = null;
		for (int i = 0; i < scPoints.length; i++) {
			if (i == 0) {
				p31 = scPoints[scPoints.length - 1];
				p32 = scPoints[0];
			} else {
				p31 = scPoints[i - 1];
				p32 = scPoints[i];
			}
			objs = crossPoint(p21, p22, p31, p32);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("交点一：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点一所在线段：" + p31.x + "," + p31.y + " - " + p32.x + "," + p32.y);
			}
		}
		// 计算平行于多边形左上边并且穿过小圆圆心的延长线
		p21 = new Point(3.755637736232768E7, 2019248.981931392);
		p22 = new Point(3.7560045708373904E7, 2016463.3958583719);
		// 计算延长线与小圆的交点
		for (int i = 0; i < scPoints.length; i++) {
			if (i == 0) {
				p31 = scPoints[scPoints.length - 1];
				p32 = scPoints[0];
			} else {
				p31 = scPoints[i - 1];
				p32 = scPoints[i];
			}
			objs = crossPoint(p21, p22, p31, p32);
			isIntersect = (Boolean) objs[0];
			if (isIntersect) {
				point = (Point) objs[1];
				System.out.println("交点二：" + point.x + "," + point.y);
				jwd = coord.xYtoLq(point.x, point.y);
				System.out.println(jwd.getJd() + "," + jwd.getWd());
				System.out.println("交点二所在线段：" + p31.x + "," + p31.y + " - " + p32.x + "," + p32.y);
			}
		}
	}

	public static Object[] crossPoint(Point p1, Point p2, Point p3, Point p4) {

		double a1 = p2.y - p1.y;
		double b1 = p1.x - p2.x;
		double c1 = p1.x * p2.y - p2.x * p1.y;
		double a2 = p4.y - p3.y;
		double b2 = p3.x - p4.x;
		double c2 = p3.x * p4.y - p4.x * p3.y;
		double det = a1 * b2 - a2 * b1;

		if (det == 0)
			return new Object[] { false, null };

		Point crossPoint = new Point();
		crossPoint.x = (c1 * b2 - c2 * b1) / det;
		crossPoint.y = (a1 * c2 - a2 * c1) / det;

		// 是否要判断线段相交
		if ((Math.abs(crossPoint.x - (p1.x + p2.x) / 2) <= Math.abs(p2.x - p1.x) / 2)
				&& (Math.abs(crossPoint.y - (p1.y + p2.y) / 2) <= Math.abs(p2.y - p1.y) / 2)
				&& (Math.abs(crossPoint.x - (p3.x + p4.x) / 2) <= Math.abs(p4.x - p3.x) / 2)
				&& (Math.abs(crossPoint.y - (p3.y + p4.y) / 2) <= Math.abs(p4.y - p3.y) / 2)) {
			return new Object[] { true, crossPoint };
		}

		return new Object[] { false, null };
	}

	public static double calcGridArea(String[] gridStrs) {
		double areaSum = 0;
		String[] polyStrs = null;
		Point[] points = null;
		int loc = 0;
		int idx = 0;
		GeometryCalc util = new GeometryCalc();
		Coord coord = new Coord();
		for (int i = 0; i < gridStrs.length; i++) {
			polyStrs = GeoHashUtil.getPolygonOfGh(gridStrs[i]).split("\\|");
			points = new Point[polyStrs.length];
			idx = 0;
			for (String polyStr : polyStrs) {
				loc = polyStr.indexOf(",");
				points[idx] = coord.lQtoXy(Double.parseDouble(polyStr.substring(0, loc)), Double.parseDouble(polyStr.substring(loc + 1)));
				idx++;
			}
			areaSum += util.area_of_polygon(points.length, points) / 1000000.0;
		}
		return areaSum;
	}

	/**
	 * 求两个经纬度之间的距离
	 * 
	 * @author maqian
	 * @date 2019年5月7日 下午2:31:26
	 * @param lon1 经度
	 * @param lat1 纬度
	 * @param lon2 经度
	 * @param lat2 纬度
	 * @return
	 */
	public static double distance(double lon1, double lat1, double lon2, double lat2) {
		double lat1Rad = degreeToRadians(lat1);
		double lon1Rad = degreeToRadians(lon1);
		double lat2Rad = degreeToRadians(lat2);
		double lon2Rad = degreeToRadians(lon2);
		double dLat = (lat2Rad - lat1Rad);
		double dLon = (lon2Rad - lon1Rad);
		double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);
		double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
		double d = earthRadius * c;
		return d;
	}

}