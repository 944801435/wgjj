package com.uav.base.util;

import com.uav.base.util.spacial.Jwd;
import com.uav.base.util.spacial.Point;

public class GeoHashVo {
	public double swLng=0;
	public double swLat=0;
	public double neLng=0;
	public double neLat=0;
	
	public double minx=0;
	public double miny=0;
	public double maxx=0;
	public double maxy=0;
	
	public Point[] points=null;
	public Jwd[] jwds=null;
}
