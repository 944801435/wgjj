package com.uav.base.util.bdgrid;

import java.util.Date;

import cn.hutool.core.date.DateUtil;

public class Tester {
	public static void main(String[] args){
		long time=1622261812436l;
		Date d=new Date(time);
		System.out.println(DateUtil.formatTime(d));
		
//		int level=6;
//		
////		System.out.println("东北半球：116.31260278,39.99316111,68.17");
////		BdGrid baseBd=BdGrid.withBitPrecision(39.99316111, 116.31260278, 68.17, level);
////		System.out.println("西北半球：-116.31260278,39.99316111,68.17");
////		BdGrid baseBd=BdGrid.withBitPrecision(39.99316111, -116.31260278, 68.17, level);
////		System.out.println("东南半球：116.31260278,-39.99316111,68.17");
////		BdGrid baseBd=BdGrid.withBitPrecision(-39.99316111, 116.31260278, 68.17, level);
//		System.out.println("西南半球：-116.31260278,-39.99316111,68.17");
//		BdGrid baseBd=BdGrid.withBitPrecision(-39.99316111, -116.31260278, 68.17, level);
//		BoundingBox box=baseBd.getBoundingBox();
//		System.out.println(baseBd.toBinaryString());
//		System.out.println(box.getMinLon()+","+box.getMinLat()+" - "+box.getMaxLon()+","+box.getMaxLat());
//		System.out.println(SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMaxLon(), box.getMinLat())
//				+"*"
//				+SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMinLon(), box.getMaxLat()));
//		System.out.println(box.isThreeDim()+" - "+box.getMinHei()+" - "+box.getMaxHei());
//		
////		System.out.println("从编码还原");
////		baseBd=BdGrid.fromBinaryString(true, "S011J0047050390B8021");
////		box=baseBd.getBoundingBox();
////		System.out.println(baseBd.toBinaryString());
////		System.out.println(box.getMinLon()+","+box.getMinLat()+" - "+box.getMaxLon()+","+box.getMaxLat());
////		System.out.println(SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMaxLon(), box.getMinLat())
////				+"*"
////				+SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMinLon(), box.getMaxLat()));
////		System.out.println(box.isThreeDim()+" - "+box.getMinHei()+" - "+box.getMaxHei());
//		
//		System.out.println("东");
//		BdGrid bd=baseBd.getEasternNeighbour();
//		box=bd.getBoundingBox();
//		System.out.println(bd.toBinaryString());
//		System.out.println(box.getMinLon()+","+box.getMinLat()+" - "+box.getMaxLon()+","+box.getMaxLat());
//		System.out.println(SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMaxLon(), box.getMinLat())
//				+"*"
//				+SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMinLon(), box.getMaxLat()));
//		System.out.println(box.isThreeDim()+" - "+box.getMinHei()+" - "+box.getMaxHei());
//		
//		System.out.println("南");
//		bd=baseBd.getSouthernNeighbour();
//		box=bd.getBoundingBox();
//		System.out.println(bd.toBinaryString());
//		System.out.println(box.getMinLon()+","+box.getMinLat()+" - "+box.getMaxLon()+","+box.getMaxLat());
//		System.out.println(SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMaxLon(), box.getMinLat())
//				+"*"
//				+SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMinLon(), box.getMaxLat()));
//		System.out.println(box.isThreeDim()+" - "+box.getMinHei()+" - "+box.getMaxHei());
//		
//		System.out.println("西");
//		bd=baseBd.getWesternNeighbour();
//		box=bd.getBoundingBox();
//		System.out.println(bd.toBinaryString());
//		System.out.println(box.getMinLon()+","+box.getMinLat()+" - "+box.getMaxLon()+","+box.getMaxLat());
//		System.out.println(SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMaxLon(), box.getMinLat())
//				+"*"
//				+SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMinLon(), box.getMaxLat()));
//		System.out.println(box.isThreeDim()+" - "+box.getMinHei()+" - "+box.getMaxHei());
//		
//		System.out.println("北");
//		bd=baseBd.getNorthernNeighbour();
//		box=bd.getBoundingBox();
//		System.out.println(bd.toBinaryString());
//		System.out.println(box.getMinLon()+","+box.getMinLat()+" - "+box.getMaxLon()+","+box.getMaxLat());
//		System.out.println(SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMaxLon(), box.getMinLat())
//				+"*"
//				+SpaceUtil.distance(box.getMinLon(), box.getMinLat(), box.getMinLon(), box.getMaxLat()));
//		System.out.println(box.isThreeDim()+" - "+box.getMinHei()+" - "+box.getMaxHei());
	}
}
