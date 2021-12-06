package com.uav.base.util.spacial;

import java.util.ArrayList;
import java.util.Map;

public class Test {
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
//		double lng=110.224834;
//		double lat=19.935685;
		
//		//计算经纬度投影
//		Coord coord=new Coord();
//		Point point=coord.lQtoXy(lng, lat);
//		
//		//左右平移1000米后计算经纬度
//		Jwd jwd=coord.xYtoLq(point.getX()+1000, point.getY());
//		System.out.println(jwd.getJd()+","+jwd.getWd());
		
		//经纬度转换为投影坐标
//		Coord coord=new Coord();
//		Jwd jwd=coord.xYtoLq(582238.00, 2264930.23);
//		System.out.println(jwd.getJd()+","+jwd.getWd());
//		Point point=coord.lQtoXy(jwd.getJd(), jwd.getWd());
//		System.out.println(point.getX()+","+point.getY());
//		Point point=coord.lQtoXy(116.326046, 39.996018);
//		System.out.println(point.getX()+","+point.getY());
		
		ArrayList<Point> points=new ArrayList<Point>();
		points.add(new Point(0,0));
		points.add(new Point(2,1));
		points.add(new Point(3,3));
		points.add(new Point(5,0));
		Map<String,Object> map=new GeometryCalc().genBuffer(points, 0.2);
		//缓冲区ArrayList<BufferLineEntity> arrLine
		//包线ArrayList<Point> arrRet
		ArrayList<Point> arrRet=(ArrayList<Point>)map.get("包线");
		Point point=null;
		for (int i = 0; i < arrRet.size(); i++) {
			point=arrRet.get(i);
			System.out.println(point.x+","+point.y);
		}
	}
}
