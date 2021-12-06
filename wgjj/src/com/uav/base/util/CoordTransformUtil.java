package com.uav.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.uav.base.common.Constants;
import com.uav.base.util.spacial.Coord;
import com.uav.base.util.spacial.Jwd;
import com.uav.base.util.spacial.Point;

/**
 * 百度坐标，火星坐标和WGS84之间转换Java代码
 * 
 * @Title: CoordTransformUtil.java
 * @author maqian  
 * @date 2018年12月11日 上午10:02:15
 */
public class CoordTransformUtil {

	public static double pi = 3.1415926535897932384626;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;

	/**
	 * 根据覆盖物形状、WGS84坐标，计算多边形投影、WGS84、投影最大最小XY
	 * 
	 * @Title: getPolygonProj
	 * @author maqian
	 * @date 2018年12月11日 上午10:01:25
	 * @param spcShape 形状
	 * @param spcLocWgs84 原始wgs84坐标
	 * @return
	 */
	public static Map<String, Object> getPolygonProj(String spcShape, String spcLocationWgs84) {
		double jdWgs84 = 0;
		double wdWgs84 = 0;
		double r = 0;
		Point point = null;
		StringBuffer polyWgs84ResultBuf = new StringBuffer();
		StringBuffer polyProjResultBuf = new StringBuffer();
		byte[] fenceData = null;// 云标准围栏数据
		Double minx = null;
		Double miny = null;
		Double maxx = null;
		Double maxy = null;
		Double minlng = null;
		Double minlat = null;
		Double maxlng = null;
		Double maxlat = null;
		Jwd jwdWgs84 = null;
		Point[] points = null;
		Coord coord = new Coord();
		if (Constants.space_shape_dbx.equals(spcShape)) {// 多边形：j,w|j,w|j,w
			List<Jwd> jwdWgs84ReqList = new ArrayList<Jwd>();// 请求的所有坐标的集合
			String[] spcLocationsWgs84 = spcLocationWgs84.split("\\|");
			String[] jwWgs84 = null;
			for (String s : spcLocationsWgs84) {
				jwWgs84 = s.split(",");
				jdWgs84 = Double.parseDouble(jwWgs84[0]);
				wdWgs84 = Double.parseDouble(jwWgs84[1]);
				// WGS84坐标转换为投影坐标
				point = coord.lQtoXy(jdWgs84, wdWgs84);
				polyProjResultBuf.append(point.getX()).append(",").append(point.getY()).append("|");
				// 记录最大最小xy
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
				if (minlng == null) {
					minlng = jdWgs84;
					maxlng = jdWgs84;
					minlat = wdWgs84;
					maxlat = wdWgs84;
				} else {
					maxlng = jdWgs84 > maxlng ? jdWgs84 : maxlng;
					minlng = jdWgs84 < minlng ? jdWgs84 : minlng;
					maxlat = wdWgs84 > maxlat ? wdWgs84 : maxlat;
					minlat = wdWgs84 < minlat ? wdWgs84 : minlat;
				}
				// 得到所有的WGS84坐标的经纬度的list
				jwdWgs84ReqList.add(new Jwd(jdWgs84, wdWgs84));
			}
			polyWgs84ResultBuf.append(spcLocationWgs84 + "|");
			fenceData = SpaceUtil.getPolygonRail(jwdWgs84ReqList.toArray(new Jwd[] {}));
		} else if (Constants.space_shape_yx.equals(spcShape)) {// 圆形：j,w,r
			String[] jwr = spcLocationWgs84.split(",");
			jdWgs84 = Double.parseDouble(jwr[0]);
			wdWgs84 = Double.parseDouble(jwr[1]);
			r = Double.parseDouble(jwr[2]);
			// 根据WGS84坐标计算多边形，获得投影坐标数组
			Jwd center = new Jwd(jdWgs84, wdWgs84);
			points = SpaceUtil.getCircleProjPoints(center, r);
			for (int i = 0; i < points.length; i++) {
				polyProjResultBuf.append(points[i].x).append(",").append(points[i].y).append("|");
				// 投影坐标转换为WGS84坐标
				jwdWgs84 = coord.xYtoLq(points[i].x, points[i].y);
				polyWgs84ResultBuf.append(jwdWgs84.getJd()).append(",").append(jwdWgs84.getWd()).append("|");

				// 记录最大最小xy
				if (minx == null) {
					minx = points[i].getX();
					maxx = points[i].getX();
					miny = points[i].getY();
					maxy = points[i].getY();
				} else {
					if (points[i].getX() > maxx)
						maxx = points[i].getX();
					else if (points[i].getX() < minx)
						minx = points[i].getX();
					if (points[i].getY() > maxy)
						maxy = points[i].getY();
					else if (points[i].getY() < miny)
						miny = points[i].getY();
				}
				if (minlng == null) {
					minlng = jwdWgs84.getJd();
					maxlng = jwdWgs84.getJd();
					minlat = jwdWgs84.getWd();
					maxlat = jwdWgs84.getWd();
				} else {
					maxlng = jwdWgs84.getJd() > maxlng ? jwdWgs84.getJd() : maxlng;
					minlng = jwdWgs84.getJd() < minlng ? jwdWgs84.getJd() : minlng;
					maxlat = jwdWgs84.getWd() > maxlat ? jwdWgs84.getWd() : maxlat;
					minlat = jwdWgs84.getWd() < minlat ? jwdWgs84.getWd() : minlat;
				}
			}
			fenceData = SpaceUtil.getCircleRail(center, r);
		} else if (Constants.space_shape_xhcq.equals(spcShape)) {// 线缓冲区：j,w,k|j,w,k|j,w,k|j,w,k
			String[] spcLocations = spcLocationWgs84.split("\\|");
			String k = spcLocations[0].split(",")[2];
			double width = Double.parseDouble(k);
			List<Jwd> jwdList = new LinkedList<Jwd>();
			for (int i = 0; i < spcLocations.length; i++) {
				// WGS84坐标
				String[] jw = spcLocations[i].split(",");
				jdWgs84 = Double.parseDouble(jw[0]);
				wdWgs84 = Double.parseDouble(jw[1]);
				jwdList.add(new Jwd(jdWgs84, wdWgs84));
			}
			// 根据WGS84坐标计算多边形，获得投影坐标数组
			Jwd[] jwds = new Jwd[jwdList.size()];
			for (int i = 0; i < jwdList.size(); i++)
				jwds[i] = jwdList.get(i);
			points = SpaceUtil.getLineBufferPoints(jwds, width);
			for (int i = 0; i < points.length; i++) {
				polyProjResultBuf.append(points[i].x).append(",").append(points[i].y).append("|");
				// 投影坐标转换为WGS84坐标
				jwdWgs84 = coord.xYtoLq(points[i].x, points[i].y);
				polyWgs84ResultBuf.append(jwdWgs84.getJd()).append(",").append(jwdWgs84.getWd()).append("|");
				// 记录最大最小xy
				if (minx == null) {
					minx = points[i].getX();
					maxx = points[i].getX();
					miny = points[i].getY();
					maxy = points[i].getY();
				} else {
					if (points[i].getX() > maxx)
						maxx = points[i].getX();
					else if (points[i].getX() < minx)
						minx = points[i].getX();
					if (points[i].getY() > maxy)
						maxy = points[i].getY();
					else if (points[i].getY() < miny)
						miny = points[i].getY();
				}
				if (minlng == null) {
					minlng = jwdWgs84.getJd();
					maxlng = jwdWgs84.getJd();
					minlat = jwdWgs84.getWd();
					maxlat = jwdWgs84.getWd();
				} else {
					maxlng = jwdWgs84.getJd() > maxlng ? jwdWgs84.getJd() : maxlng;
					minlng = jwdWgs84.getJd() < minlng ? jwdWgs84.getJd() : minlng;
					maxlat = jwdWgs84.getWd() > maxlat ? jwdWgs84.getWd() : maxlat;
					minlat = jwdWgs84.getWd() < minlat ? jwdWgs84.getWd() : minlat;
				}
			}
			fenceData = SpaceUtil.getLineBufferRail(jwds, width);
		} else if (Constants.space_shape_sx.equals(spcShape)) {// 扇形：j,w|r|nStartA|nEndA
			String[] spcLocations = spcLocationWgs84.split("\\|");
			String[] jw = spcLocations[0].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			r = Double.parseDouble(spcLocations[1]);
			double nStartA = Double.parseDouble(spcLocations[2]);
			double nEndA = Double.parseDouble(spcLocations[3]);
			// 根据WGS84坐标计算多边形，获得投影坐标数组
			Jwd center = new Jwd(jdWgs84, wdWgs84);
			points = SpaceUtil.getSectorProjPoints(center, r, nStartA, nEndA);
			for (int i = 0; i < points.length; i++) {
				polyProjResultBuf.append(points[i].x).append(",").append(points[i].y).append("|");
				// 投影坐标转换为WGS84坐标
				jwdWgs84 = coord.xYtoLq(points[i].x, points[i].y);
				polyWgs84ResultBuf.append(jwdWgs84.getJd()).append(",").append(jwdWgs84.getWd()).append("|");
				// 记录最大最小xy
				if (minx == null) {
					minx = points[i].getX();
					maxx = points[i].getX();
					miny = points[i].getY();
					maxy = points[i].getY();
				} else {
					if (points[i].getX() > maxx)
						maxx = points[i].getX();
					else if (points[i].getX() < minx)
						minx = points[i].getX();
					if (points[i].getY() > maxy)
						maxy = points[i].getY();
					else if (points[i].getY() < miny)
						miny = points[i].getY();
				}
				if (minlng == null) {
					minlng = jwdWgs84.getJd();
					maxlng = jwdWgs84.getJd();
					minlat = jwdWgs84.getWd();
					maxlat = jwdWgs84.getWd();
				} else {
					maxlng = jwdWgs84.getJd() > maxlng ? jwdWgs84.getJd() : maxlng;
					minlng = jwdWgs84.getJd() < minlng ? jwdWgs84.getJd() : minlng;
					maxlat = jwdWgs84.getWd() > maxlat ? jwdWgs84.getWd() : maxlat;
					minlat = jwdWgs84.getWd() < minlat ? jwdWgs84.getWd() : minlat;
				}
			}
			fenceData = SpaceUtil.getSectorRail(center, r, nStartA, nEndA);
		} else if (Constants.space_shape_jczaw.equals(spcShape)) {// 机场障碍物限制面：j,w|j,w|j,w|rC2B2|j,w|j,w|rB3C3|j,w|j,w|j,w|j,w|rC4B4|j,w|j,w|rB1C1|j,w
			String[] spcLocations = spcLocationWgs84.split("\\|");

			String[] jw = spcLocations[0].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd a1 = new Jwd(jdWgs84, wdWgs84);

			jw = spcLocations[1].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd a2 = new Jwd(jdWgs84, wdWgs84);

			jw = spcLocations[2].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd c2 = new Jwd(jdWgs84, wdWgs84);

			double radiusC2B2 = Double.parseDouble(spcLocations[3]);

			jw = spcLocations[4].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd b2 = new Jwd(jdWgs84, wdWgs84);

			jw = spcLocations[5].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd b3 = new Jwd(jdWgs84, wdWgs84);

			double radiusB3C3 = Double.parseDouble(spcLocations[6]);

			jw = spcLocations[7].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd c3 = new Jwd(jdWgs84, wdWgs84);

			jw = spcLocations[8].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd a3 = new Jwd(jdWgs84, wdWgs84);

			jw = spcLocations[9].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd a4 = new Jwd(jdWgs84, wdWgs84);

			jw = spcLocations[10].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd c4 = new Jwd(jdWgs84, wdWgs84);

			double radiusC4B4 = Double.parseDouble(spcLocations[11]);

			jw = spcLocations[12].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd b4 = new Jwd(jdWgs84, wdWgs84);

			jw = spcLocations[13].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd b1 = new Jwd(jdWgs84, wdWgs84);

			double radiusB1C1 = Double.parseDouble(spcLocations[14]);

			jw = spcLocations[15].split(",");
			jdWgs84 = Double.parseDouble(jw[0]);
			wdWgs84 = Double.parseDouble(jw[1]);
			Jwd c1 = new Jwd(jdWgs84, wdWgs84);

			Jwd[] jwds = SpaceUtil.getAirportJwds(a1, a2, c2, radiusC2B2, b2, b3, radiusB3C3, c3, a3, a4, c4, radiusC4B4, b4, b1, radiusB1C1, c1);
			for (int i = 0; i < jwds.length; i++) {
				point = coord.lQtoXy(jwds[i].getJd(), jwds[i].getWd());
				polyProjResultBuf.append(point.x).append(",").append(point.y).append("|");
				// 投影坐标转换为Wgs84坐标
				polyWgs84ResultBuf.append(jwds[i].getJd()).append(",").append(jwds[i].getWd()).append("|");
				// 记录最大最小xy
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
				jwdWgs84 = jwds[i];
				if (minlng == null) {
					minlng = jwdWgs84.getJd();
					maxlng = jwdWgs84.getJd();
					minlat = jwdWgs84.getWd();
					maxlat = jwdWgs84.getWd();
				} else {
					maxlng = jwdWgs84.getJd() > maxlng ? jwdWgs84.getJd() : maxlng;
					minlng = jwdWgs84.getJd() < minlng ? jwdWgs84.getJd() : minlng;
					maxlat = jwdWgs84.getWd() > maxlat ? jwdWgs84.getWd() : maxlat;
					minlat = jwdWgs84.getWd() < minlat ? jwdWgs84.getWd() : minlat;
				}
			}
			fenceData = SpaceUtil.getAirportRail(a1, a2, c2, radiusC2B2, b2, b3, radiusB3C3, c3, a3, a4, c4, radiusC4B4, b4, b1, radiusB1C1, c1);
		} else if (Constants.space_shape_wg.equals(spcShape)) {// 网格
			String[] spcLocations = spcLocationWgs84.split("\\|");
			for (String spcLocation : spcLocations) {
				String[] spcLocationsWgs84 = GeoHashUtil.getPolygonOfGh(spcLocation).split("\\|");
				String[] jwWgs84 = null;
				String tempPolyProjResult = "";
				StringBuffer tempPolyProjResultBuf = new StringBuffer();
				String temlPolyWgs84Result = "";
				StringBuffer temlPolyWgs84ResultBuf = new StringBuffer();
				for (String s : spcLocationsWgs84) {
					jwWgs84 = s.split(",");
					jdWgs84 = Double.parseDouble(jwWgs84[0]);
					wdWgs84 = Double.parseDouble(jwWgs84[1]);
					temlPolyWgs84ResultBuf.append(jdWgs84);
					temlPolyWgs84ResultBuf.append(",");
					temlPolyWgs84ResultBuf.append(wdWgs84);
					temlPolyWgs84ResultBuf.append("|");
					// WGS84坐标转换为投影坐标
					point = coord.lQtoXy(jdWgs84, wdWgs84);
					tempPolyProjResultBuf.append(point.getX());
					tempPolyProjResultBuf.append(",");
					tempPolyProjResultBuf.append(point.getY());
					tempPolyProjResultBuf.append("|");
					// 记录最大最小xy
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
					if (minlng == null) {
						minlng = jdWgs84;
						maxlng = jdWgs84;
						minlat = wdWgs84;
						maxlat = wdWgs84;
					} else {
						maxlng = jdWgs84 > maxlng ? jdWgs84 : maxlng;
						minlng = jdWgs84 < minlng ? jdWgs84 : minlng;
						maxlat = wdWgs84 > maxlat ? wdWgs84 : maxlat;
						minlat = wdWgs84 < minlat ? wdWgs84 : minlat;
					}
				}
				if (StringUtils.isNotBlank(tempPolyProjResultBuf)) {
					tempPolyProjResult = tempPolyProjResultBuf.toString().substring(0, tempPolyProjResultBuf.toString().length() - 1);
					polyProjResultBuf.append(tempPolyProjResult);
					polyProjResultBuf.append(";");
				}
				if (StringUtils.isNotBlank(temlPolyWgs84ResultBuf)) {
					temlPolyWgs84Result = temlPolyWgs84ResultBuf.toString().substring(0, temlPolyWgs84ResultBuf.toString().length() - 1);
					polyWgs84ResultBuf.append(temlPolyWgs84Result);
					polyWgs84ResultBuf.append(";");
				}
			}
		}

		if (fenceData == null)
			fenceData = new byte[] { 0 };

		String polyWgs84Result = polyWgs84ResultBuf.toString();
		String polyProjResult = polyProjResultBuf.toString();
		polyWgs84Result = polyWgs84Result.substring(0, polyWgs84Result.length() - 1);
		polyProjResult = polyProjResult.substring(0, polyProjResult.length() - 1);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wgs84Result", spcLocationWgs84);
		map.put("polyWgs84Result", polyWgs84Result);
		map.put("polyProjResult", polyProjResult);
		map.put("fenceData", fenceData);
		map.put("minx", minx);
		map.put("miny", miny);
		map.put("maxx", maxx);
		map.put("maxy", maxy);
		map.put("minlng", minlng);
		map.put("minlat", minlat);
		map.put("maxlng", maxlng);
		map.put("maxlat", maxlat);

		return map;
	}
	
	/**
	 * 根据多边形WGS84坐标转成投影多边形
	 * @author q
	 * @date 2020年2月19日
	 * @param spcLocationWgs84
	 * @return
	 */
	public static String getPolygonProj(String spcLocationWgs84) {
		List<String> returnStrList=new ArrayList<String>();
		String[] spcLocationWgs84_array=spcLocationWgs84.split(";");
		for(String spcLocationWgs84Item : spcLocationWgs84_array) {
			String[] spcLocations = spcLocationWgs84Item.split("\\|");
			String[] jwWgs84 = null;
			double jd = 0;
			double wd = 0;
			Point point = null;
			Coord coord = new Coord();
			StringBuffer polyProjResult = new StringBuffer();
			for (int i = 0; i < spcLocations.length; i++) {
				String s = spcLocations[i];
				jwWgs84 = s.split(",");
				jd = Double.parseDouble(jwWgs84[0]);
				wd = Double.parseDouble(jwWgs84[1]);
				// WGS84坐标转换为投影坐标
				point = coord.lQtoXy(jd, wd);
				if (i > 0) {
					polyProjResult.append("|");
				}
				polyProjResult.append((point.getX() + "," + point.getY()));
			}
			returnStrList.add(polyProjResult.toString());
			
		}
		
		return StringUtils.join(returnStrList, ";");
	}

	/**
	 * 从WGS84坐标系转成GCJ02火星坐标系
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return
	 */
	public static Jwd wgs84ToGcj02(double lng, double lat) {
		Gps gps = gps84_To_Gcj02(lat, lng);
		if (gps == null) {
			return null;
		}
		return new Jwd(gps.getWgLon(), gps.getWgLat());
	}

	/**
	 * 从GCJ02火星坐标系转成WGS84坐标系
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return
	 */
	public static Jwd gcj02ToWgs84(double lng, double lat) {
		Gps gps = gcj_To_Gps84(lat, lng);
		if (gps == null) {
			return null;
		}
		return new Jwd(gps.getWgLon(), gps.getWgLat());
	}

	/**
	 * 从GCJ02火星坐标系转成BD09百度坐标系
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return
	 */
	public static Jwd gcj02ToBd09(double lng, double lat) {
		Gps gps = gcj02_To_Bd09(lat, lng);
		if (gps == null) {
			return null;
		}
		return new Jwd(gps.getWgLon(), gps.getWgLat());
	}

	/**
	 * 从BD09百度坐标系转成GCJ02火星坐标系
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return
	 */
	public static Jwd bd09ToGcj02(double lng, double lat) {
		Gps gps = bd09_To_Gcj02(lat, lng);
		if (gps == null) {
			return null;
		}
		return new Jwd(gps.getWgLon(), gps.getWgLat());
	}

	/**
	 * 从BD09百度坐标系转成WGS84坐标系
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return
	 */
	public Jwd bd09ToWgs84(double lng, double lat) {
		Gps gps = bd09_To_Gps84(lat, lng);
		if (gps == null) {
			return null;
		}
		return new Jwd(gps.getWgLon(), gps.getWgLat());
	}

	/**
	 * 从WGS84坐标系转成BD09百度坐标系
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return
	 */
	public Jwd wgs84ToBd09(double lng, double lat) {
		Jwd gcj02 = wgs84ToGcj02(lng, lat);
		if (gcj02 == null) {
			return null;
		}
		Jwd bd09 = gcj02ToBd09(gcj02.getJd(), gcj02.getWd());
		return bd09;
	}

	/**
	 * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
	 * 
	 * @param lat
	 * @param lon
	 * @return
	 */
	private static Gps gps84_To_Gcj02(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return null;
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		return new Gps(mgLat, mgLon);
	}

	/**
	 * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	 */
	private static Gps gcj_To_Gps84(double lat, double lon) {
		Gps gps = transform(lat, lon);
		double lontitude = lon * 2 - gps.getWgLon();
		double latitude = lat * 2 - gps.getWgLat();
		return new Gps(latitude, lontitude);
	}

	/**
	 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
	 * 
	 * @param gg_lat
	 * @param gg_lon
	 */
	private static Gps gcj02_To_Bd09(double gg_lat, double gg_lon) {
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new Gps(bd_lat, bd_lon);
	}

	/**
	 * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 *
	 * * @param bd_lat * @param bd_lon * @return
	 */
	private static Gps bd09_To_Gcj02(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new Gps(gg_lat, gg_lon);
	}

	/**
	 * (BD-09)-->84
	 * 
	 * @param bd_lat
	 * @param bd_lon
	 * @return
	 */
	private static Gps bd09_To_Gps84(double bd_lat, double bd_lon) {

		Gps gcj02 = CoordTransformUtil.bd09_To_Gcj02(bd_lat, bd_lon);
		Gps map84 = CoordTransformUtil.gcj_To_Gps84(gcj02.getWgLat(), gcj02.getWgLon());
		return map84;

	}

	private static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	private static Gps transform(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return new Gps(lat, lon);
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		return new Gps(mgLat, mgLon);
	}

	private static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}

}

class Gps {
	private double wgLat;
	private double wgLon;

	public Gps(double wgLat, double wgLon) {
		setWgLat(wgLat);
		setWgLon(wgLon);
	}

	public double getWgLat() {
		return wgLat;
	}

	public void setWgLat(double wgLat) {
		this.wgLat = wgLat;
	}

	public double getWgLon() {
		return wgLon;
	}

	public void setWgLon(double wgLon) {
		this.wgLon = wgLon;
	}

	@Override
	public String toString() {
		return wgLat + "," + wgLon;
	}
}