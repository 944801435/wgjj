package com.uav.base.util.spacial;

//import com.csxy.gis.entity.Jwd;
import com.uav.base.util.GaussUtil;

public class Coord {
	/*
	 * public class JWD { public double JD; public double WD; }
	 */
	/*
	 * public class XY { public double x; public double y; }
	 */
	/*
	 * public class Coord {
	 */
	static double PI = Math.PI; // 3.14159265358979323846;

	static double DE = 0.081813334;

	static double r = 6378.245;

	static double B2 = 0.0033560695588;

	static double B4 = 0.0000065700353;

	static double B6 = 0.0000065700353;

	static double B8 = 0.0000065700353;

	static double RADIAN = PI / 180.0;

	static double DEGREE = 180.0 / PI;

	// 坐标转换类参数的初始化
	static double Up_Latitude = 27.0; // 上纬线(度)

	static double Dn_Latitude = 5.0; // 下纬线(度)

	static double Low_Latitude = 20.0; // 最低纬线(度)//最低（最南端）纬度

	static double Center_Longitude = 110.0; // 中央经线(度)

	double a;

	double k;

	double ps;

	double mapLeft = 0.0;

	double mapTop = 0.0;

	double mapRight = 0.0;

	double mapBottom = 0.0;

	double mapWidth = 0.0;

	double mapHeight = 0.0;

	public Coord() {
		myinit();
	}

	public Coord(double left, double top, double right, double bottom, double width, double height) {
		mapLeft = left;
		mapTop = top;
		mapRight = right;
		mapBottom = bottom;
		mapWidth = width;
		mapHeight = height;
	}

	/**
	 *  度 分 秒 ->带小数的度 经度
	 * 
	 * @Title: jdMinuteSecondToDotDegree
	 * @date 2018年12月25日 下午4:51:18
	 * @param degree
	 * @return
	 */
	public float jdMinuteSecondToDotDegree(String degree) {
		float d = Float.parseFloat(degree.substring(0, 3));
		float m = Float.parseFloat(degree.substring(3, 5)) / 60;
		float s = Float.parseFloat(degree.substring(5, 7)) / 3600;
		float ret = d + m + s;
		return ret;
	}

	/**
	 * 度 分 秒 ->带小数的度 纬度
	 * 
	 * @Title: wdMinuteSecondToDotDegree
	 * @date 2018年12月25日 下午4:51:25
	 * @param degree
	 * @return
	 */
	public float wdMinuteSecondToDotDegree(String degree) {
		float d = Float.parseFloat(degree.substring(0, 2));
		float m = Float.parseFloat(degree.substring(2, 4)) / 60;
		float s = Float.parseFloat(degree.substring(4, 6)) / 3600;
		float ret = d + m + s;

		return ret;
	}

	public Point eNtoXy(String strNE) {
		// strNE=ENFormat(strNE);

		String degree_t = strNE;
		String wd = degree_t.substring(0, 6);
		String jd = strNE.substring(7, 14);
		// Array JWD = new Array(2);
		float dJD = jdMinuteSecondToDotDegree(jd);
		float dWD = wdMinuteSecondToDotDegree(wd);

		if (strNE.charAt(14) == 'S') {
			dWD = -dWD;
		}

		Point ptRet = lQtoXy(dJD, dWD);
		return ptRet;
	}

	public String eNStrToXyStr(String strNE) {
		StringBuilder sb = new StringBuilder();
		int nLen = strNE.length() / 15;

		for (int i = 0; i < nLen; i++) {
			String strT = strNE.substring(i * 15, (i + 1) * 15);
			Point pt = eNtoXy(strT);
			sb.append((int) pt.x);
			sb.append(",");
			sb.append((int) pt.y);
			sb.append(";");
		}

		String strRet = sb.toString();

		return strRet.substring(0, strRet.length() - 1);
	}

	/**
	 * 计算投影参数 "u"， tempWd为弧度
	 * 
	 * @Title: calcu
	 * @date 2018年12月25日 下午4:51:35
	 * @param tempWd
	 * @return
	 */
	private double calcu(double tempWd) {
		double Temp1, Temp2, Temp3;
		Temp3 = Math.asin(DE * Math.sin(tempWd)); // ψ=Arcsin(esin(φ))
		Temp1 = Math.tan(PI / 4.0 + tempWd / 2.0); // tg(45°+φ/2)
		Temp2 = Math.pow((Math.tan(PI / 4.0 + Temp3 / 2.0)), DE); // (tg(45°+ψ/2))的e次幂
		Temp3 = tempWd * DEGREE;
		double retValue = Temp1 / Temp2; // 求得U(与纬度有关)

		return retValue;
	}

	/**
	 * 计算投影参数 "a" ,UpWd、DnWd为弧度 
	 * 
	 * @Title: calca
	 * @date 2018年12月25日 下午4:51:43
	 * @param UpWd
	 * @param DnWd
	 * @return
	 */
	private double calca(double UpWd, double DnWd) {
		double r1, r2, u1, u2;
		r2 = r * Math.cos(UpWd) / Math.sqrt(1 - DE * DE * Math.sin(UpWd) * Math.sin(UpWd)) * 1000;
		r1 = r * Math.cos(DnWd) / Math.sqrt(1 - DE * DE * Math.sin(DnWd) * Math.sin(DnWd)) * 1000;

		u2 = calcu(UpWd);
		u1 = calcu(DnWd);

		double retValue = (Math.log(r1) - Math.log(r2)) / (Math.log(u2) - Math.log(u1));

		return retValue;
	}

	/**
	 * 计算投影参数 "k"，UpWd为弧度
	 * 
	 * @Title: calck
	 * @date 2018年12月25日 下午4:51:48
	 * @param UpWd
	 * @return
	 */
	private double calck(double UpWd) {
		double r1, u1;
		r1 = r * Math.cos(UpWd) / Math.sqrt(1 - DE * DE * Math.sin(UpWd) * Math.sin(UpWd));
		u1 = calcu(UpWd);

		double retValue = (r1 * Math.pow(u1, a)) / a;

		return retValue;
	}

	/**
	 * 计算投影参数 "ps"， tempWd为弧度
	 * 
	 * @Title: calcp
	 * @date 2018年12月25日 下午4:51:54
	 * @param tempWd
	 * @return
	 */
	private double calcp(double tempWd) {
		double u;
		u = calcu(tempWd);

		double retValue = k / Math.pow(u, a);

		return retValue;
	}

	/**
	 * 计算投影直角坐标 "x"， tempJd、tempWd为弧度
	 * 
	 * @Title: calcy
	 * @date 2018年12月25日 下午4:51:59
	 * @param tempJd
	 * @param tempWd
	 * @return
	 */
	public double calcy(double tempJd, double tempWd) {
		double tempp;
		tempp = calcp(tempWd);

		double retValue = tempp * Math.sin(a * (tempJd - Center_Longitude * RADIAN));

		return retValue * 1000;
	}

	/**
	 * 计算投影直角坐标 "y" ， tempJd、tempWd为弧度
	 * 
	 * @Title: calcx
	 * @date 2018年12月25日 下午4:52:08
	 * @param tempJd
	 * @param tempWd
	 * @return
	 */
	public double calcx(double tempJd, double tempWd) {
		double tempp, temp;
		tempp = calcp(tempWd);
		temp = Math.cos(a * (tempJd - Center_Longitude * RADIAN));
		double retValue = ps - tempp * temp;

		return retValue * 1000;
	}

	/**
	 * 类CoordSysConversion初始化
	 * 
	 * @Title: myinit
	 * @date 2018年12月25日 下午4:52:13
	 */
	private void myinit() {

		// 变成弧度参加运算
		double tempu = Up_Latitude * RADIAN;
		double templ = Dn_Latitude * RADIAN;
		double tempw0 = Low_Latitude * RADIAN;

		a = calca(tempu, templ);
		k = calck(tempu);
		ps = calcp(tempw0);

	}

	/**
	 * 计算经度
	 * 
	 * @Title: calcJD
	 * @date 2018年12月25日 下午4:52:19
	 * @param X
	 * @param Y
	 * @return
	 */
	public double calcJD(double X, double Y) {
		double tttt;
		tttt = X / 1000.0;
		X = Y / 1000.0;
		Y = tttt;
		double delta, FL;
		delta = Math.atan(Y / (ps - X));
		FL = delta / a;
		double tempJD = FL * DEGREE + Center_Longitude;

		return tempJD;
	}

	/**
	 * 计算纬度
	 * 
	 * @Title: calcWD
	 * @date 2018年12月25日 下午4:52:24
	 * @param X
	 * @param Y
	 * @return
	 */
	public double calcWD(double X, double Y) {
		double tttt;
		tttt = X / 1000.0;
		X = Y / 1000.0;
		Y = tttt;

		double rho, phi0;
		double q, FB;

		rho = Math.sqrt((ps - X) * (ps - X) + Y * Y);
		q = (Math.log(k) - Math.log(rho)) / a;

		phi0 = 2 * Math.atan(Math.exp(q)) - PI / 2; // e=2.718281828

		FB = phi0 + B2 * Math.sin(2 * phi0) + B4 * Math.sin(4 * phi0) + B6 * Math.sin(6 * phi0) + B8 * Math.sin(8 * phi0);
		double tempWD = FB * DEGREE;

		return tempWD;
	}

	/**
	 * 经纬度转换到直角坐标XY tempJD、tempWD为度
	 * 
	 * @Title: lQtoXy
	 * @date 2018年12月25日 下午4:52:30
	 * @param tempJD
	 * @param tempWD
	 * @return
	 */
	public Point lQtoXy(double tempJD, double tempWD) {
		// double x = calcx(tempJD * RADIAN, tempWD * RADIAN);
		// double y = calcy(tempJD * RADIAN, tempWD * RADIAN);
		// Point point = new Point();
		// point.setX(y);
		// point.setY(x);
		// return point;
		// 采用高斯投影替换原有投影算法
		return GaussUtil.jwdToGauss(tempJD, tempWD);
	}

	/**
	 * 直角坐标XY转换到经纬度 tempJD、tempWD为度
	 * 
	 * @Title: xYtoLq
	 * @date 2018年12月25日 下午4:52:35
	 * @param X
	 * @param Y
	 * @return
	 */
	public Jwd xYtoLq(double X, double Y) {
		// double jd = calcJD(X, Y);
		// double wd = calcWD(X, Y);
		// Jwd jwd = new Jwd();
		// jwd.setJd(jd);
		// jwd.setWd(wd);
		// return jwd;
		// 采用高斯投影替换原有投影算法
		return GaussUtil.gaussToJwd(X, Y);
	}

	public String dotJDegreeToMinuteSecond(double degree) {
		int m_degree = (int) Math.floor(degree);
		double minute = (degree - m_degree) * 60;
		int m_minute = (int) Math.floor(minute);
		int m_second = (int) Math.round((minute - m_minute) * 60);

		String str_degree = ("000" + Integer.toString(m_degree));
		String str_minute = ("00" + Integer.toString(m_minute));
		String str_second = ("00" + Integer.toString(m_second));

		int dLen = str_degree.length() - 3;
		int mLen = str_minute.length() - 2;
		int sLen = str_second.length() - 2;

		String m_degree_t = str_degree.substring(dLen, dLen + 3);
		String m_minute_t = str_minute.substring(mLen, mLen + 2);
		String m_second_t = str_second.substring(sLen, sLen + 2);
		return m_degree_t + m_minute_t + m_second_t;
	}

	public String dotWDegreeToMinuteSecond(double degree) {
		int m_degree = (int) Math.floor(degree);
		double minute = (degree - m_degree) * 60;
		int m_minute = (int) Math.floor(minute);
		int m_second = (int) Math.round((minute - m_minute) * 60);

		String str_degree = ("00" + Integer.toString(m_degree));
		String str_minute = ("00" + Integer.toString(m_minute));
		String str_second = ("00" + Integer.toString(m_second));

		int dLen = str_degree.length() - 2;
		int mLen = str_minute.length() - 2;
		int sLen = str_second.length() - 2;

		String m_degree_t = str_degree.substring(dLen, dLen + 2);
		String m_minute_t = str_minute.substring(mLen, mLen + 2);
		String m_second_t = str_second.substring(sLen, sLen + 2);
		return m_degree_t + m_minute_t + m_second_t;
	}

	public String dotDegreeToMinuteSecond(double jd, double wd) {
		if (wd < 0) {
			return dotWDegreeToMinuteSecond(Math.abs(wd)) + "S" + dotJDegreeToMinuteSecond(jd) + "E";
		} else {
			return dotWDegreeToMinuteSecond(wd) + "N" + dotJDegreeToMinuteSecond(jd) + "E";
		}
	}

	public double dotJMinuteSecondToDegree(String str_JD) {
		double d_JD_degree = Double.parseDouble(str_JD.substring(0, 3));
		double d_JD_minute = Double.parseDouble(str_JD.substring(3, 5)) / 60;
		double d_JD_second = Double.parseDouble(str_JD.substring(5, 7)) / 3600;
		double d_JD_second_dot = 0.0;
		if (str_JD.length() == 9) {
			d_JD_second_dot = Double.parseDouble(str_JD.substring(7, 9)) / 36000;
		}
		double d_JD = d_JD_degree + d_JD_minute + d_JD_second + d_JD_second_dot;
		return d_JD;
	}

	public double dotWMinuteSecondToDegree(String str_WD) {
		double d_WD_degree = Double.parseDouble(str_WD.substring(0, 2));
		double d_WD_minute = Double.parseDouble(str_WD.substring(2, 4)) / 60;
		double d_WD_second = Double.parseDouble(str_WD.substring(4, 6)) / 3600;
		double d_WD_second_dot = 0.0;
		if (str_WD.length() == 8) {
			d_WD_second_dot = Double.parseDouble(str_WD.substring(6, 8)) / 36000;
		}
		double d_WD = d_WD_degree + d_WD_minute + d_WD_second + d_WD_second_dot;
		try {
			if (str_WD.substring(6, 7) == "S") {
				d_WD = -d_WD;
			}
		} catch (Exception e) {

		}
		return d_WD;
	}

	public int mapToPixelX(double mapX) {
		double pixelX;
		pixelX = mapWidth * (mapX - mapLeft) / (mapRight - mapLeft);
		return (int) (pixelX);
	}

	public int mapToPixelY(double mapY) {
		double pixelY;
		pixelY = mapHeight * (mapTop - mapY) / (mapTop - mapBottom);
		return (int) (pixelY);
	}

	public double pixelToMapX(double pixelX) {
		double mapX;
		mapX = mapLeft + pixelX * (mapRight - mapLeft) / mapWidth;
		return Math.round(mapX * 1000) / 1000;
	}

	double pixelToMapY(double pixelY) {
		double mapY;
		mapY = mapBottom + (mapHeight - pixelY) * (mapTop - mapBottom) / mapHeight;
		return Math.round(mapY * 1000) / 1000;
	}

	public String dmsStr_To_Dash_Split(String strInput) {
		StringBuffer strRet = new StringBuffer();
		if (strInput.length() % 15 == 0) {
			for (int i = 0; i < strInput.length() / 15; i++) {
				String strItem = strInput.substring(i * 15, (i + 1) * 15);

				String strWD = strItem.substring(0, 6);
				String strJD = strItem.substring(7, 14);

				double wd = dotWMinuteSecondToDegree(strWD);
				double jd = dotJMinuteSecondToDegree(strJD);

				strRet.append(Double.toString(jd));
				strRet.append(",");
				strRet.append(Double.toString(wd));
				strRet.append(" ");
			}
		} else if (strInput.length() % 19 == 0) {
			for (int i = 0; i < strInput.length() / 19; i++) {
				String strItem = strInput.substring(i * 19, (i + 1) * 19);

				String strWD = strItem.substring(0, 8);
				String strJD = strItem.substring(9, 18);

				double wd = dotWMinuteSecondToDegree(strWD);
				double jd = dotJMinuteSecondToDegree(strJD);

				strRet.append(Double.toString(jd));
				strRet.append(",");
				strRet.append(Double.toString(wd));
				strRet.append(" ");
			}
		}
		return strRet.toString().trim();
	}

	public String xyArrayToEN(String strXY) {
		if (strXY.indexOf(";") == -1) {
			strXY += ";";
		}

		StringBuffer strRet = new StringBuffer();
		String[] coordA = strXY.split(";");
		for (int i = 0; i < coordA.length - 1; i++) {
			String[] coordB = coordA[i].split(",");
			if (coordB.length != 2) {
				continue;
			}
			String strX = coordB[0];
			String strY = coordB[1];

			Jwd jwd = xYtoLq(Double.parseDouble(strX), Double.parseDouble(strY));
			strRet.append(dotDegreeToMinuteSecond(jwd.getJd(), jwd.getWd()));
		}
		return strRet.toString();
	}
}
