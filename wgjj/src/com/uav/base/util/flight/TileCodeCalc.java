package com.uav.base.util.flight;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

public class TileCodeCalc {
	public static BufferedImage getMapImage(double minLon, double minLat, double maxLon, double maxLat, int zoom) throws Exception {
		double lonStep = getLonStep(zoom);
		double latStep = getLatStep(zoom);
		int lonTile = lon2tile(minLon, zoom);
		int latTile = lat2tile(minLat, zoom);
		double lonRange = 0, latRange = 0;
		List<Integer> lonTileList = new LinkedList<Integer>();
		List<Integer> latTileList = new LinkedList<Integer>();
		while (lonRange <= (maxLon + 180)) {
			lonTileList.add(lonTile);
			lonRange = lonStep * (lonTile + 1);
			lonTile++;
		}
		while (latRange <= (maxLat + 90)) {
			latTileList.add(latTile);
			latRange = latStep * (latTile + 1);
			latTile++;
		}

		String url = null;
		BufferedImage srcImg = null, tarImg = null;
		int basex = 0, basey = 0;
		int width = 0, height = 0;
		int[] imageArray = null;
		for (int i = 0; i < lonTileList.size(); i++) {
			lonTile = lonTileList.get(i);
			basey = 0;
			for (int j = latTileList.size(); j > 0; j--) {
				latTile = latTileList.get(j - 1);

				url = "http://192.168.1.141:8080/geoserver/gwc/service/tms/1.0.0/gis%3Aguangdong_goo_4326@EPSG%3A4326@png/" + zoom + "/" + lonTile
						+ "/" + latTile + ".png";
				System.out.println(url);
				srcImg = getImageFromUrl(url);
				if (srcImg == null)
					return null;

				if (tarImg == null) {
					width = srcImg.getWidth();
					height = srcImg.getHeight();
					tarImg = new BufferedImage(width * lonTileList.size(), height * latTileList.size(), BufferedImage.TYPE_INT_RGB);
				}
				imageArray = new int[width * height];
				imageArray = srcImg.getRGB(0, 0, width, height, imageArray, 0, width);
				tarImg.setRGB(basex, basey, width, height, imageArray, 0, width);

				basey += height;
			}
			basex += width;
		}

		return tarImg;
	}

	public static BufferedImage getImageFromUrl(String urlStr) throws Exception {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() == 200)
				return ImageIO.read(conn.getInputStream());
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				conn.disconnect();
			} catch (Exception e) {
			}
		}

		return null;
	}

	public static int lon2tile(double lon, int zoom) {
		double step = getLonStep(zoom);
		lon += 180;
		return (int) Math.round(Math.floor(lon / step));
	}

	public static double getLonStep(int zoom) {
		double step = 180;
		for (int i = 0; i < zoom; i++)
			step = step / 2;
		return step;
	}

	public static int lat2tile(double lat, int zoom) {
		double step = getLatStep(zoom);
		lat += 90;
		return (int) Math.round(Math.floor(lat / step));
	}

	public static double getLatStep(int zoom) {
		double step = 180;
		for (int i = 0; i < zoom; i++)
			step = step / 2;
		return step;
	}

	public static void main(String[] args) {
//		double lon=113.0699334;
//		double lat=23.0797344;
//		int z=11;
//		System.out.println(lon2tile(lon,z));
//		System.out.println(lat2tile(lat,z));

		double minLon = 113.0699334, minLat = 23.0797344;
		double maxLon = 113.4524158, maxLat = 23.2955765;
		int zoom = 11;
		try {
			BufferedImage img = getMapImage(minLon, minLat, maxLon, maxLat, zoom);

			File outFile = new File("D:\\work\\WGJJ\\docs\\all.png");
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			ImageIO.write(img, "png", outFile);
			System.out.println("success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
