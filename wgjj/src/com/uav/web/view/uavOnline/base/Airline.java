package com.uav.web.view.uavOnline.base;

import java.util.List;

public class Airline {
	private String name;
	private List<LngLatPoint> points;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<LngLatPoint> getPoints() {
		return points;
	}

	public void setPoints(List<LngLatPoint> points) {
		this.points = points;
	}
}
