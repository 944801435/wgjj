package com.brilliance.web.view.uavOnline.base;

public class LngLatPoint {
	public LngLatPoint() {
	}

	public LngLatPoint(double lat,double lng)
	{
		this.lat = lat;
		this.lng = lng;
	}

	private double lat;
	private double lng;

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
}
