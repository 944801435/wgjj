package com.uav.base.util.spacial;


public class MathTool {
	public static double GetQuadrantAngle(Point preCoord, Point nextCoord) {
		return GetQuadrantAngle(nextCoord.x - preCoord.x, nextCoord.y - preCoord.y);
	}

	public static double GetQuadrantAngle(double x, double y) {
		double theta = Math.atan(y / x);
		if (x > 0 && y > 0)
			return theta;
		if (x > 0 && y < 0)
			return Math.PI * 2 + theta;
		if (x < 0 && y > 0)
			return theta + Math.PI;
		if (x < 0 && y < 0)
			return theta + Math.PI;
		return theta;
	}

	public static double GetIncludedAngel(Point preCoord, Point midCoord, Point nextCoord) {
		double innerProduct = (midCoord.x - preCoord.x)
				* (nextCoord.x - midCoord.x) + (midCoord.y - preCoord.y)
				* (nextCoord.y - midCoord.y);
		double mode1 = Math.sqrt(Math.pow((midCoord.x - preCoord.x), 2.0)
				+ Math.pow((midCoord.y - preCoord.y), 2.0));
		double mode2 = Math.sqrt(Math.pow((nextCoord.x - midCoord.x), 2.0)
				+ Math.pow((nextCoord.y - midCoord.y), 2.0));
		double rtn=Math.acos(innerProduct / (mode1 * mode2));
		if(Double.isNaN(rtn)){
			rtn=0;
		}
		return rtn;
	}

	public static double GetDistance(Point preCoord, Point nextCoord) {
		return Math.sqrt(Math.pow((nextCoord.x - preCoord.x), 2)
				+ Math.pow((nextCoord.y - preCoord.y), 2));
	}
}