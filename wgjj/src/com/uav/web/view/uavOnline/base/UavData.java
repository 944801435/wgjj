package com.uav.web.view.uavOnline.base;

import lombok.Data;

/**
 * 态势数据，航迹点使用
 * 
 * @Title: UavData.java
 */
@Data
public class UavData {

	private double lng;// 经度
	private double lat;// 纬度
	private double hei;// 高度
	private double spe;// 速度
	private String time;// 时间
	private double dir; // 航向
	private String wrn;// 告警

	public UavData() {
	}

	public UavData(double lng, double lat, double hei, double spe, String time) {
		super();
		this.lng = lng;
		this.lat = lat;
		this.hei = hei;
		this.spe = spe;
		this.time = time;
	}

	public UavData(double lng, double lat, double hei, double spe, String time, double dir, String wrn) {
		super();
		this.lng = lng;
		this.lat = lat;
		this.hei = hei;
		this.spe = spe;
		this.time = time;
		this.dir = dir;
		this.wrn = wrn;
	}
}
