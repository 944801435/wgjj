package com.uav.base.util.bdgrid;

public class BoundingBox {
	private double minLon=0,maxLon=0,minLat=0,maxLat=0;//最小、最大经纬度
	private double minHei=0,maxHei=0;//最小、最大高度
	private boolean threeDim=false;//是否三维
	
	public BoundingBox(double minLon, double minLat, double maxLon, double maxLat){
		this(false, minLon, minLat, maxLon, maxLat, 0, 0);
	}
	public BoundingBox(double minLon, double minLat, double maxLon, double maxLat, double minHei, double maxHei){
		this(true, minLon, minLat, maxLon, maxLat, minHei, maxHei);
	}
	private BoundingBox(boolean threeDim, double minLon, double minLat, double maxLon, double maxLat, double minHei, double maxHei){
		this.threeDim=threeDim;
		this.minLon=minLon;
		this.minLat=minLat;
		this.maxLon=maxLon;
		this.maxLat=maxLat;
		this.minHei=minHei;
		this.maxHei=maxHei;
	}

	public double getMinLon() {
		return minLon;
	}

	public double getMaxLon() {
		return maxLon;
	}

	public double getMinLat() {
		return minLat;
	}

	public double getMaxLat() {
		return maxLat;
	}
	
	public double getMinHei() {
		return minHei;
	}
	
	public double getMaxHei() {
		return maxHei;
	}
	
	public boolean isThreeDim() {
		return threeDim;
	}
}