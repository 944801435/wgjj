package com.uav.adsb;

public class FlightTarget {
	private String id;
	private String at;
	private double sp;//速度，公里/小时
	private double ht;//高度，米
	private double ang;//航向
	private double lat;
	private double lng;
	private String ti;//编号
	private String sc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public double getSp() {
		return sp;
	}

	public void setSp(double sp) {
		this.sp = sp;
	}

	public double getHt() {
		return ht;
	}

	public void setHt(double ht) {
		this.ht = ht;
	}

	public double getAng() {
		return ang;
	}

	public void setAng(double ang) {
		this.ang = ang;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getTi() {
		return ti;
	}

	public void setTi(String ti) {
		this.ti = ti;
	}

	public String getSc() {
		return sc;
	}

	public void setSc(String sc) {
		this.sc = sc;
	}
}
