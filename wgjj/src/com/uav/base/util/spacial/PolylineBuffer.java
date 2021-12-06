package com.uav.base.util.spacial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PolylineBuffer {
	public PolylineBuffer() {

	}

	public static String GetBufferEdgeCoords(double coordsProj[], double radius) {
		List<Point> coords = new ArrayList<Point>();
		for (int i = 0; i < coordsProj.length / 2; i++) {
			coords.add(new Point(coordsProj[2 * i], coordsProj[2 * i + 1]));
		}
		String leftBufferCoords = GetLeftBufferEdgeCoords(coords, radius);
		
		Collections.reverse(coords);
		String rightBufferCoords = GetLeftBufferEdgeCoords(coords, radius);
		return leftBufferCoords + "," + rightBufferCoords;
	}

	private static String GetLeftBufferEdgeCoords(List<Point> coordsProj, double radius) {
		if (coordsProj.size() < 1)
			return "";

		double alpha = 0.0;
		double delta = 0.0;
		double l = 0.0;

		StringBuilder strCoords = new StringBuilder();
		double startRadian = 0.0;
		double endRadian = 0.0;
		double beta = 0.0;
		double x = 0.0, y = 0.0;

		alpha = MathTool.GetQuadrantAngle(coordsProj.get(0), coordsProj.get(1));
		startRadian = alpha + Math.PI;
		endRadian = alpha + (3 * Math.PI) / 2;
		String str=GetBufferCoordsByRadian(coordsProj.get(0), startRadian, endRadian, radius);
		strCoords.append(str);

		for (int i = 1; i < coordsProj.size() - 1; i++) {
			alpha = MathTool.GetQuadrantAngle(coordsProj.get(i), coordsProj.get(i + 1));
			delta = MathTool.GetIncludedAngel(coordsProj.get(i - 1), coordsProj.get(i), coordsProj.get(i + 1));
			l = GetVectorProduct(coordsProj.get(i - 1), coordsProj.get(i), coordsProj.get(i + 1));
			if (l > 0) {
				startRadian = alpha + (3 * Math.PI) / 2 - delta;
				endRadian = alpha + (3 * Math.PI) / 2;
				if (strCoords.length() > 0)
					strCoords.append(",");
				str=GetBufferCoordsByRadian(coordsProj.get(i), startRadian, endRadian, radius);
				strCoords.append(str);
			} else if (l < 0) {
				beta = alpha - (Math.PI - delta) / 2;
				x = coordsProj.get(i).x + radius * Math.cos(beta);
				y = coordsProj.get(i).y + radius * Math.sin(beta);
				if (strCoords.length() > 0)
					strCoords.append(",");
				strCoords.append(x + "," + y);
			}
		}

		alpha = MathTool.GetQuadrantAngle(coordsProj.get(coordsProj.size() - 2), coordsProj.get(coordsProj.size() - 1));
		startRadian = alpha + (3 * Math.PI) / 2;
		endRadian = alpha + 2 * Math.PI;
		if (strCoords.length() > 0)
			strCoords.append(",");
		str=GetBufferCoordsByRadian(coordsProj.get(coordsProj.size() - 1), startRadian, endRadian, radius);
		strCoords.append(str);

		return strCoords.toString();
	}

	private static String GetBufferCoordsByRadian(Point center, double startRadian, double endRadian, double radius) {
		double gamma = Math.PI / 6;

		StringBuilder strCoords = new StringBuilder();
		double x = 0.0, y = 0.0;
		for (double phi = startRadian; phi <= endRadian + 0.000000000000001; phi += gamma) {
			x = center.x + radius * Math.cos(phi);
			y = center.y + radius * Math.sin(phi);
			if (strCoords.length() > 0)
				strCoords.append(",");
			strCoords.append(x + "," + y);
		}
		return strCoords.toString();
	}

	private static double GetVectorProduct(Point preCoord, Point midCoord, Point nextCoord) {
		return (midCoord.x - preCoord.x) * (nextCoord.y - midCoord.y) - (nextCoord.x - midCoord.x) * (midCoord.y - preCoord.y);
	}

	public static void main(String[] args) {
		
	}
}