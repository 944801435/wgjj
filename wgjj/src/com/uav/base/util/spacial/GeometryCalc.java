package com.uav.base.util.spacial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class GeometryCalc {
	public final double INF = 1E200;

	public final double EP = 1E-10;

	public final int MAXV = 300;

	public final double PI = Math.PI;

	private Coord coord = new Coord();

	double max(double p1, double p2) {
		if (p1 >= p2) {
			return p1;
		} else {
			return p2;
		}
	}

	double min(double p1, double p2) {
		if (p1 >= p2) {
			return p2;
		} else {
			return p1;
		}
	}

	/**
	 * 点的基本运算
	 * 
	 * @Title: dist
	 * @date 2018年12月25日 下午4:56:38
	 * @param p1
	 * @param p2
	 * @return 返回两点之间欧氏距离
	 */
	public double dist(Point p1, Point p2) {
		return (Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
	}

	/**
	 * 判断两个点是否重合
	 * 
	 * @Title: equal_point
	 * @date 2018年12月25日 下午4:57:08
	 * @param p1
	 * @param p2
	 * @return
	 */
	boolean equal_point(Point p1, Point p2) {
		return ((Math.abs(p1.x - p2.x) < EP) && (Math.abs(p1.y - p2.y) < EP));
	}

	/***************************************************************************
	 * r=multiply(sp,ep,op),得到(sp-op)*(ep-op)的叉积 r>0:ep在矢量opsp的逆时针方向；
	 * r=0：opspep三点共线； r<0:ep在矢量opsp的顺时针方向
	 **************************************************************************/
	double multiply(Point sp, Point ep, Point op) {
		return ((sp.x - op.x) * (ep.y - op.y) - (ep.x - op.x) * (sp.y - op.y));
	}

	/***************************************************************************
	 * r=dotmultiply(p1,p2,op),得到矢量(p1-op)和(p2-op)的点积，如果两个矢量都非零矢量
	 * r<0:两矢量夹角为锐角；r=0：两矢量夹角为直角；r>0:两矢量夹角为钝角
	 **************************************************************************/
	double dotmultiply(Point p1, Point p2, Point p0) {
		return ((p1.x - p0.x) * (p2.x - p0.x) + (p1.y - p0.y) * (p2.y - p0.y));
	}

	/***************************************************************************
	 * 判断点p是否在线段l上，条件：(p在线段l所在的直线上)&& (点p在以线段l为对角线的矩形内)
	 **************************************************************************/
	boolean online(LineSeg l, Point p) {
		return ((multiply(l.getE(), p, l.getS()) == 0)
				&& (((p.x - l.getS().x) * (p.x - l.getE().x) <= 0) && ((p.y - l.getS().y) * (p.y - l.getE().y) <= 0)));
	}

	/***************************************************************************
	 * 返回点p以点o为圆心逆时针旋转alpha(单位：弧度)后所在的位置
	 **************************************************************************/
	Point rotate(Point o, double alpha, Point p) {
		Point tp = new Point();
		p.setX(p.x - o.x);
		p.setY(p.y - o.y);
		tp.setX(p.x * Math.cos(alpha) - p.y * Math.sin(alpha) + o.x);
		tp.setY(p.y * Math.cos(alpha) + p.x * Math.sin(alpha) + o.y);
		return tp;
	}

	/**
	 * 方位角
	 * 
	 * @Title: calcAngle
	 * @date 2018年12月25日 下午4:57:40
	 * @param ptCenter
	 * @param ptInput
	 * @return
	 */
	public double calcAngle(Point ptCenter, Point ptInput) {
		double angle = 0;

		if (ptInput.x == ptCenter.x && ptInput.y >= ptCenter.y) {
			angle = 90;
		} else if (ptInput.x == ptCenter.x && ptInput.y <= ptCenter.y) {
			angle = 270;
		} else if (ptInput.x > ptCenter.x && ptInput.y == ptCenter.y) {
			angle = 0;
		} else if (ptInput.x < ptCenter.x && ptInput.y == ptCenter.y) {
			angle = 180;
		} else {
			angle = 180 * (Math.atan(Math.abs(((ptInput.y - ptCenter.y) / (ptInput.x - ptCenter.x))))) / Math.PI;

			if (ptInput.x >= ptCenter.x && ptInput.y >= ptCenter.y)// 1
			{

			} else if (ptInput.x <= ptCenter.x && ptInput.y >= ptCenter.y)// 2
			{
				angle = 180 - angle;
			} else if (ptInput.x <= ptCenter.x && ptInput.y <= ptCenter.y)// 3
			{
				angle = 180 + angle;
			} else if (ptInput.x >= ptCenter.x && ptInput.y <= ptCenter.y)// 4
			{
				angle = 360 - angle;
			}
		}

		// angle = (90 - angle);
		if (angle < 0) {
			angle += 360;
		}

		return angle;
	}

	/**
	 * 矩形旋转
	 * 
	 * @Title: pointRotate
	 * @date 2018年12月25日 下午4:57:49
	 * @param x
	 * @param y
	 * @param longAxis
	 * @param shortAxis
	 * @param rotate
	 * @return
	 */
	public Point pointRotate(double x, double y, double longAxis, double shortAxis, double rotate) {
		Point ptRet = new Point();
		ptRet.setX(Math.cos(rotate) * x + Math.sin(rotate) * y + longAxis);
		ptRet.setY(-Math.sin(rotate) * x + Math.cos(rotate) * y + shortAxis);
		return ptRet;
	}

	public Point[] rectRotate(double x, double y, double longAxis, double shortAxis, double rotate) {
		Point[] ptArr = new Point[4];
		ptArr[0] = pointRotate(-longAxis, shortAxis, x, y, rotate);
		ptArr[1] = pointRotate(longAxis, shortAxis, x, y, rotate);
		ptArr[2] = pointRotate(longAxis, -shortAxis, x, y, rotate);
		ptArr[3] = pointRotate(-longAxis, -shortAxis, x, y, rotate);

		return ptArr;
	}

	/**
	 * 返回顶角在o点，起始边为os，终止边为oe的夹角(单位：弧度) 角度小于pi，返回正值 角度大于pi，返回负值 可以用于求线段之间的夹角
	 * 
	 * @Title: angle
	 * @date 2018年12月25日 下午4:57:58
	 * @param o
	 * @param s
	 * @param e
	 * @return
	 */
	double angle(Point o, Point s, Point e) {
		double cosfi, fi, norm;
		double dsx = s.x - o.x;
		double dsy = s.y - o.y;
		double dex = e.x - o.x;
		double dey = e.y - o.y;

		cosfi = dsx * dex + dsy * dey;
		norm = (dsx * dsx + dey * dey) * (dex * dex + dey * dey);
		cosfi /= Math.sqrt(norm);

		if (cosfi >= 1.0)
			return 0;
		if (cosfi <= -1.0)
			return -3.1415926;

		fi = Math.cos(cosfi);
		if (dsx * dey - dsy * dex > 0)
			return fi; // 说明矢量os 在矢量
		// oe的顺时针方向
		return -fi;
	}

	// endregion

	// region 线运算
	/***************************************************************************
	 * ***************************\ * 线段及直线的基本运算 * * \
	 **************************************************************************/

	/**
	 * 判断点与线段的关系,用途很广泛 本函数是根据下面的公式写的，P是点C到线段AB所在直线的垂足
	 * 
	 * AC dot AB r = --------- ||AB||^2 (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay) =
	 * ------------------------------- L^2
	 * 
	 * r has the following meaning:
	 * 
	 * r=0 P = A r=1 P = B r<0 P is on the backward extension of AB r>1 P is on
	 * the forward extension of AB 0<r<1 P is interior to AB
	 */
	double relation(Point p, LineSeg l) {
		LineSeg tl = new LineSeg();
		tl.setS(l.getS());
		tl.setE(p);
		return dotmultiply(tl.getE(), l.getE(), l.getS()) / (dist(l.getS(), l.getE()) * dist(l.getS(), l.getE()));
	}

	/**
	 * 求点C到线段AB所在直线的垂足 P
	 * 
	 * @Title: perpendicular
	 * @date 2018年12月25日 下午4:58:25
	 * @param p
	 * @param l
	 * @return
	 */
	Point perpendicular(Point p, LineSeg l) {

		double r = relation(p, l);
		Point tp = new Point();
		tp.setX(l.getS().x + r * (l.getE().x - l.getS().x));
		tp.setY(l.getS().y + r * (l.getE().y - l.getS().y));
		return tp;
	}

	/**
	 * 求点p到线段l的最短距离,并返回线段上距该点最近的点np 注意：np是线段l上到点p最近的点，不一定是垂足
	 * 
	 * @Title: ptolinesegdist
	 * @author maqian
	 * @date 2018年12月25日 下午4:58:32
	 * @param p
	 * @param l
	 * @param np 为需要保持的输入值
	 * @return
	 */
	Map<String, Object> ptolinesegdist(Point p, LineSeg l, Point np) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		double result = 0;
		Point np_T;

		double r = relation(p, l);
		if (r < 0) {
			np_T = l.getS();
			result = dist(p, l.getS());
			resultMap.put("source", np_T);
			resultMap.put("result", result);
			return resultMap;
		}
		if (r > 1) {
			np_T = l.getE();
			result = dist(p, l.getE());
			resultMap.put("source", np_T);
			resultMap.put("result", result);
			return resultMap;
		}
		np_T = perpendicular(p, l);

		result = dist(p, np_T);
		resultMap.put("source", np);
		resultMap.put("result", result);

		np.setX(np_T.x);
		np.setY(np_T.y);

		return resultMap;
	}

	/**
	 * 求点p到线段l所在直线的距离,请注意本函数与上个函数的区别
	 * 
	 * @Title: ptoldist
	 * @date 2018年12月25日 下午4:59:15
	 * @param p
	 * @param l
	 * @return
	 */
	double ptoldist(Point p, LineSeg l) {
		return Math.abs(multiply(p, l.getE(), l.getS())) / dist(l.getS(), l.getE());
	}

	/**
	 * 计算点到折线集的最近距离,并返回最近点. 
	 * 注意：调用的是ptolineseg()函数
	 * 
	 * @Title: ptopointset
	 * @date 2018年12月25日 下午4:59:22
	 * @param vcount
	 * @param pointset
	 * @param p
	 * @param q 需要状态保持
	 * @return
	 */
	public Map<String, Object> ptopointset(int vcount, Point[] points, Point p, Point q) {
		// 计算多边形少一个边 调整
		vcount = vcount + 1;
		List<Point> pointList = new ArrayList<Point>(Arrays.asList(points));
		pointList.add(points[0]);
		Point[] pointset = pointList.toArray(new Point[pointList.size()]);

		Map<String, Object> sourceMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int i;
		double cd = INF;
		double td = 0;
		LineSeg l = new LineSeg();
		Point tq = new Point();
		Point cq = new Point();

		for (i = 0; i < vcount - 1; i++) {
			if (pointset[i].x == pointset[i + 1].x && pointset[i].y == pointset[i + 1].y) {
				continue;
			}

			l.setS(pointset[i]);

			l.setE(pointset[i + 1]);

			sourceMap = ptolinesegdist(p, l, tq);
			td = (Double) sourceMap.get("result");
			tq = (Point) sourceMap.get("source");
			if (td < cd) {
				cd = td;
				cq = tq;
			}
		}
		q.setX(cq.x);
		q.setY(cq.y);

		resultMap.put("result", cd);
		resultMap.put("source", q);
		return resultMap;
	}

	/**
	 * 判断圆是否在多边形内.ptolineseg()函数的应用2
	 * 
	 * @Title: CircleInsidePolygon
	 * @date 2018年12月25日 下午4:59:40
	 * @param vcount
	 * @param center
	 * @param radius
	 * @param polygon
	 * @return
	 */
	boolean CircleInsidePolygon(int vcount, Point center, double radius, Point[] polygon) {
		Map<String, Object> sourceMap = new HashMap<String, Object>();
		Point q = new Point();
		double d;
		q.setX(0);
		q.setY(0);
		d = 0;
		sourceMap = ptopointset(vcount, polygon, center, q);
		d = (Double) sourceMap.get("result");
		q = (Point) sourceMap.get("source");
		if (d < radius || Math.abs(d - radius) < EP) // fabs
			return true;
		else
			return false;
	}

	/**
	 * 返回两个矢量l1和l2的夹角的余弦(-1 --- 1)
	 * 注意：如果想从余弦求夹角的话，注意反余弦函数的定义域是从 0到pi
	 * 
	 * @Title: cosine
	 * @date 2018年12月25日 下午4:59:56
	 * @param l1
	 * @param l2
	 * @return
	 */
	double cosine(LineSeg l1, LineSeg l2) {
		double t1 = (l1.getE().x - l1.getS().x) * (l2.getE().x - l2.getS().x);
		double t2 = (l1.getE().y - l1.getS().y) * (l2.getE().y - l2.getS().y);
		double t3 = dist(l1.getE(), l1.getS()) * dist(l2.getE(), l2.getS());

		return ((t1 + t2) / (t3));
	}

	/**
	 * 返回线段l1与l2之间的夹角 单位：弧度 范围(-pi，pi)
	 * 
	 * @Title: lsangle
	 * @date 2018年12月25日 下午5:00:27
	 * @param l1
	 * @param l2
	 * @return
	 */
	double lsangle(LineSeg l1, LineSeg l2) {
		Point o = new Point();
		Point s = new Point();
		Point e = new Point();
		;
		o.setX(0);
		o.setY(0);
		s.setX(l1.getE().x - l1.getS().x);
		s.setY(l1.getE().y - l1.getS().y);
		e.setX(l2.getE().x - l2.getS().x);
		e.setY(l2.getE().y - l2.getS().y);
		return angle(o, s, e);
	}

	/**
	 * 如果线段u和v相交(包括相交在端点处)时，返回true
	 * 
	 * @Title: intersect
	 * @date 2018年12月25日 下午5:00:35
	 * @param u
	 * @param v
	 * @return
	 */
	public boolean intersect(LineSeg u, LineSeg v) {
		boolean b0 = (max(u.getS().x, u.getE().x) >= min(v.getS().x, v.getE().x));
		boolean b1 = max(v.getS().x, v.getE().x) >= min(u.getS().x, u.getE().x);
		boolean b2 = max(u.getS().y, u.getE().y) >= min(v.getS().y, v.getE().y);
		boolean b3 = max(v.getS().y, v.getE().y) >= min(u.getS().y, u.getE().y);
		boolean b4 = multiply(v.getS(), u.getE(), u.getS()) * multiply(u.getE(), v.getE(), u.getS()) >= 0;
		boolean b5 = multiply(u.getS(), v.getE(), v.getS()) * multiply(v.getE(), u.getE(), v.getS()) >= 0;

		return b0 && b1 && b2 && b3 && b4 && b5;
	}

	/**
	 * (线段u和v相交)&&(交点不是双方的端点) 时返回true
	 * 
	 * @Title: intersect_A
	 * @date 2018年12月25日 下午5:00:43
	 * @param u
	 * @param v
	 * @return
	 */
	boolean intersect_A(LineSeg u, LineSeg v) {
		boolean b0 = intersect(u, v);
		boolean b1 = !online(u, v.getS());
		boolean b2 = !online(u, v.getE());
		boolean b3 = !online(v, u.getE());
		boolean b4 = !online(v, u.getS());

		return b0 && b1 && b2 && b3 && b4;
	}

	/**
	 * 线段v所在直线与线段u相交时返回true；
	 * 方法：判断线段u是否跨立线段v
	 * 
	 * @Title: intersect_l
	 * @date 2018年12月25日 下午5:00:49
	 * @param u
	 * @param v
	 * @return
	 */
	boolean intersect_l(LineSeg u, LineSeg v) {
		return multiply(u.getS(), v.getE(), v.getS()) * multiply(v.getE(), u.getE(), v.getS()) >= 0;
	}

	/**
	 * 根据已知两点坐标，求过这两点的直线
	 * 解析方程： a*x+b*y+c = 0 (a >= 0)
	 * 
	 * @Title: makeline
	 * @date 2018年12月25日 下午5:01:09
	 * @param p1
	 * @param p2
	 * @return
	 */
	public Line makeline(Point p1, Point p2) {
		Line tl = new Line();
		int sign = 1;
		tl.setA(p2.y - p1.y);
		if (tl.getA() < 0) {
			sign = -1;
			tl.setA(sign * tl.getA());
		}
		tl.setB(sign * (p1.x - p2.x));
		tl.setC(sign * (p1.y * p2.x - p1.x * p2.y));
		return tl;
	}

	/**
	 * 根据直线解析方程返回直线的斜率k,水平线返回 0,竖直线返回 1e200
	 * 
	 * @Title: slope
	 * @date 2018年12月25日 下午5:01:42
	 * @param l
	 * @return
	 */
	double slope(Line l) {
		if (Math.abs(l.getA()) < 1e-20)
			return 0;
		if (Math.abs(l.getB()) < 1e-20)
			return INF;
		return -(l.getA() / l.getB());
	}

	/**
	 * 返回直线的倾斜角alpha ( 0 - pi)
	 * 
	 * @Title: alpha
	 * @date 2018年12月25日 下午5:02:15
	 * @param l
	 * @return
	 */
	double alpha(Line l) {
		if (Math.abs(l.getA()) < EP)
			return 0;
		if (Math.abs(l.getB()) < EP)
			return PI / 2;
		double k = slope(l);
		if (k > 0)
			return Math.atan(k);
		else
			return PI + Math.atan(k);
	}

	/**
	 * 求点p关于直线l的对称点
	 * 
	 * @Title: symmetry
	 * @date 2018年12月25日 下午5:02:30
	 * @param l
	 * @param p
	 * @return
	 */
	Point symmetry(Line l, Point p) {
		Point tp = new Point();
		tp.setX(((l.getB() * l.getB() - l.getA() * l.getA()) * p.x - 2 * l.getA() * l.getB() * p.y - 2 * l.getA() * l.getC())
				/ (l.getA() * l.getA() + l.getB() * l.getB()));
		tp.setY(((l.getA() * l.getA() - l.getB() * l.getB()) * p.y - 2 * l.getA() * l.getB() * p.x - 2 * l.getB() * l.getC())
				/ (l.getA() * l.getA() + l.getB() * l.getB()));
		return tp;
	}

	public Boolean compareEP(double pInput) {
		if (pInput == 0 || Math.abs(pInput) < EP) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 如果两条直线 l1(a1*x+b1*y+c1 = 0), l2(a2*x+b2*y+c2 = 0)相交，返回true，且返回交点p // 是
	 * 
	 * @Title: lineintersect
	 * @date 2018年12月25日 下午5:02:47
	 * @param l1
	 * @param l2
	 * @return
	 */
	public HashMap<String, Object> lineintersect(Line l1, Line l2) {
		HashMap<String, Object> hashRet = new HashMap<String, Object>();

		Boolean bRet = true;
		Point ptRet = new Point();

		double d = l1.a * l2.b - l2.a * l1.b;

		// 不相交
		if (compareEP(d)) {
			bRet = false;
		}

		ptRet.x = (l2.c * l1.b - l1.c * l2.b) / d;
		ptRet.y = (l2.a * l1.c - l1.a * l2.c) / d;

		hashRet.put("是否相交", bRet);
		hashRet.put("交点", ptRet);

		return hashRet;
	}

	public HashMap<String, Object> intersection(LineSeg l1, LineSeg l2) {
		HashMap<String, Object> hashRet = new HashMap<String, Object>();

		try {
			Point inter = new Point();
			Boolean bRet = false;

			Boolean bIsInsect = false;

			Line ll1 = makeline(l1.s, l1.e);
			Line ll2 = makeline(l2.s, l2.e);

			HashMap<String, Object> hashT = lineintersect(ll1, ll2);

			inter = (Point) (hashT.get("交点"));
			bIsInsect = (Boolean) (hashT.get("是否相交"));

			if (bIsInsect) {
				bRet = online(l1, inter) && online(l2, inter);
			} else {
				bRet = false;
			}

			hashRet.put("是否相交", bRet);
			hashRet.put("交点", inter);
		} catch (Exception e) {
		}

		return hashRet;
	}

	// #endregion

	// #region 多边形运算

	/***************************************************************************
	 * ****************************\ * 多边形常用算法模块 * * \
	 **************************************************************************/

	/**
	 * 如果无特别说明，输入多边形顶点要求按逆时针排列
	 * 返回值：输入的多边形是简单多边形，返回true 
	 * 要 求：输入顶点序列按逆时针排序 说 明：简单多边形定义：
	 * 1：循环排序中相邻线段对的交是他们之间共有的单个点 
	 * 2：不相邻的线段不相交 本程序默认第一个条件已经满足
	 */
	public boolean issimple(int vcount, Point[] polygon) {
		int i, cn;
		LineSeg l1 = new LineSeg();
		LineSeg l2 = new LineSeg();
		for (i = 0; i < vcount; i++) {
			l1.setS(polygon[i]);
			l1.setE(polygon[(i + 1) % vcount]);
			cn = vcount - 3;
			while (cn >= 1) {
				l2.setS(polygon[(i + 2) % vcount]);
				l2.setE(polygon[(i + 3) % vcount]);
				if (intersect(l1, l2))
					break;
				cn--;
			}
			if (cn >= 1)
				return false;
		}
		return true;
	}

	/**
	 * 返回值：按输入顺序返回多边形顶点的凸凹性判断，bc[i]=1,iff:第i个顶点是凸顶点
	 * 
	 * @Title: checkconvex
	 * @date 2018年12月25日 下午5:04:04
	 * @param vcount
	 * @param polygon
	 * @param bc
	 */
	void checkconvex(int vcount, Point[] polygon, boolean[] bc) {
		int i, index = 0;
		Point tp = polygon[0];
		// 寻找第一个凸顶点
		for (i = 1; i < vcount; i++) {
			if (polygon[i].y < tp.y || (polygon[i].y == tp.y && polygon[i].x < tp.x)) {
				tp = polygon[i];
				index = i;
				// break;
			}
		}
		int count = vcount - 1;
		bc[index] = true;
		// 判断凸凹性
		while (count >= 1) {
			if (multiply(polygon[(index + 1) % vcount], polygon[(index + 2) % vcount], polygon[index % vcount]) >= 0)
				bc[(index + 1) % vcount] = true;
			else
				bc[(index + 1) % vcount] = false;
			index++;
			count--;
		}
	}

	/**
	 * 返回值：多边形polygon是凸多边形时，返回true
	 * 
	 * @Title: isconvex
	 * @date 2018年12月25日 下午5:05:25
	 * @param vcount
	 * @param polygon
	 * @return
	 */
	public boolean isconvex(int vcount, Point[] polygon) {
		boolean[] bc = new boolean[MAXV];
		checkconvex(vcount, polygon, bc);
		// 逐一检查顶点，是否全部是凸顶点
		for (int i = 0; i < vcount; i++) {
			if (!bc[i])
				return false;
		}
		return true;
	}

	/**
	 * 返回多边形面积(signed)；输入顶点按逆时针排列时，返回正值；否则返回负值
	 * 
	 * @Title: area_of_polygon
	 * @date 2018年12月25日 下午5:05:45
	 * @param vcount
	 * @param polygon
	 * @return
	 */
	public double area_of_polygon(int vcount, Point[] polygon) {
		int i;
		double s;
		if (vcount < 3)
			return 0;
		s = polygon[0].y * (polygon[vcount - 1].x - polygon[1].x);
		for (i = 1; i < vcount; i++)
			s += polygon[i].y * (polygon[(i - 1)].x - polygon[(i + 1) % vcount].x);
		return s / 2;
	}

	/**
	 * 如果输入顶点按逆时针排列，返回true
	 * 
	 * @Title: isconterclock
	 * @date 2018年12月25日 下午5:05:54
	 * @param vcount
	 * @param polygon
	 * @return
	 */
	boolean isconterclock(int vcount, Point[] polygon) {
		return area_of_polygon(vcount, polygon) > 0;
	}

	/**
	 * 另一种判断多边形顶点排列方向的方法
	 * 
	 * @Title: isccwize
	 * @date 2018年12月25日 下午5:06:00
	 * @param vcount
	 * @param polygon
	 * @return
	 */
	boolean isccwize(int vcount, Point[] polygon) {
		int i, index;
		Point a, b, v;
		v = polygon[0];
		index = 0;
		// 找到最低且最左顶点，肯定是凸顶点
		for (i = 1; i < vcount; i++) {
			if (polygon[i].y < v.y || polygon[i].y == v.y && polygon[i].x < v.x) {
				index = i;
			}
		}
		a = polygon[(index - 1 + vcount) % vcount]; // 顶点v的前一顶点
		b = polygon[(index + 1) % vcount]; // 顶点v的后一顶点
		return multiply(v, b, a) > 0;
	}

	/**
	 * 射线法判断点q与多边形polygon的位置关系，
	 * 要求polygon为简单多边形 
	 * 如果点在多边形内： 返回0 
	 * 如果点在多边形边上： 返回1
	 * 如果点在多边形外： 返回2
	 */
	public int insidepolygon(int vcount, Point[] polygon, Point q) {
		// *han*/int vcount = polygon.length;
		if (!isconterclock(vcount, polygon)) {
			Point[] ptT = new Point[polygon.length];

			for (int t = 0; t < polygon.length; t++) {
				ptT[t] = polygon[polygon.length - t - 1];
			}

			polygon = ptT;
		}

		int c = 0, i, n;
		LineSeg l1 = new LineSeg();
		LineSeg l2 = new LineSeg();
		Point sPoint = new Point();
		sPoint.setX(q.x);
		sPoint.setY(q.y);
		l1.setS(sPoint);
		l1.setE(sPoint);
		l1.getE().setX(INF);
		n = vcount;
		for (i = 0; i < vcount; i++) {
			l2.setS(polygon[i]);
			l2.setE(polygon[(i + 1) % n]);
			if (online(l2, q))
				return 1; // 如果点在边上，返回1
			if (intersect_A(l1, l2) || // 相交且不在端点
					(online(l1, polygon[(i + 1) % n]) && // 第二个端点在射线上
							((!online(l1, polygon[(i + 2) % n])) && /*
																	 * 前一个端点和后一个 端点在射线两侧
																	 */
									(multiply(polygon[i], polygon[(i + 1) % n], l1.getS())
											* multiply(polygon[(i + 1) % n], polygon[(i + 2) % n], l1.getS()) > 0)
									|| online(l1, polygon[(i + 2) % n]) && /*
																			 * 下一条边是水平线， 前一个端点和后一个端点在射线两侧
																			 */
											(multiply(polygon[i], polygon[(i + 2) % n], l1.getS())
													* multiply(polygon[(i + 2) % n], polygon[(i + 3) % n], l1.getS()) > 0))))
				c++;
		}
		if (c % 2 == 1)
			return 0;
		else
			return 2;
	}

	/**
	 * 点q是凸多边形polygon内时，返回true；
	 * 注意：多边形polygon一定要是凸多边形
	 * 可用于三角形！
	 * @Title: InsideConvexPolygon
	 * @date 2018年12月25日 下午5:07:31
	 * @param vcount
	 * @param polygon
	 * @param q
	 * @return
	 */
	public boolean InsideConvexPolygon(int vcount, Point[] polygon, Point q) {
		/* hxd begin */
		Point qq = new Point();
		qq.setX(q.x);
		qq.setY(q.y);
		/* hxd end */
		Point p = new Point();
		LineSeg l = new LineSeg();
		int i;
		p.setX(0);
		p.setY(0);
		// 寻找一个肯定在多边形polygon内的点p：多边形顶点平均值
		for (i = 0; i < vcount; i++) {
			p.setX(p.x + polygon[i].x);
			p.setY(p.y + polygon[i].y);
		}
		p.setX(p.x / vcount);
		p.setY(p.y / vcount);

		for (i = 0; i < vcount; i++) {
			l.setS(polygon[i]);
			l.setE(polygon[(i + 1) % vcount]);
			/*
			 * 点p和点q在边l的两侧， 说明点q肯定在多边形外 原为 < 0
			 */
			if (multiply(p, l.getE(), l.getS()) * multiply(qq, l.getE(), l.getS()) < 0) {
				break;
			}
		}
		return (i == vcount);
	}

	/**
	 * 判断 多边形－多边形 是否相交
	 * 
	 * @Title: IsPolygonIntersect
	 * @date 2018年12月25日 下午5:08:16
	 * @param polygon_first
	 * @param count_first
	 * @param polygon_second
	 * @param count_second
	 * @return
	 */
	public boolean IsPolygonIntersect(Point[] polygon_first, int count_first, Point[] polygon_second, int count_second) {
		for (int i = 0; i < count_first; i++) {
			if (insidepolygon(count_second, polygon_second, polygon_first[i]) < 2) {
				return true;
			}
		}

		for (int i = 0; i < count_second; i++) {
			if (insidepolygon(count_first, polygon_first, polygon_second[i]) < 2) {
				return true;
			}
		}

		LineSeg lseg1 = new LineSeg();
		LineSeg lseg2 = new LineSeg();

		for (int i = 0; i < count_first - 1; i++) {
			for (int j = 0; j < count_second - 1; j++) {

				lseg1.setS(polygon_first[i]);
				lseg1.setE(polygon_first[i + 1]);

				lseg2.setS(polygon_second[j]);
				lseg2.setE(polygon_second[j + 1]);

				if (intersect(lseg1, lseg2)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 判断 折线－多边形 是否相交
	 * 
	 * @Title: IsPolygonLineIntersect
	 * @date 2018年12月25日 下午5:08:47
	 * @param polygon
	 * @param line
	 * @return
	 */
	public boolean IsPolygonLineIntersect(Point[] polygon, Point[] line) {
		LineSeg u = new LineSeg();
		LineSeg v = new LineSeg();

		for (int i = 0; i < line.length - 1; i++) {
			for (int j = 0; j < polygon.length - 1; j++) {
				u.setS(line[i]);
				u.setE(line[i + 1]);

				v.setS(polygon[j]);
				v.setE(polygon[j + 1]);

				if (intersect(u, v)) {
					return true;
				}
			}
		}

		for (int i = 0; i < line.length; i++) {
			if (insidepolygon(polygon.length, polygon, line[i]) != 2) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断 折线－折线 是否相交
	 * 
	 * @Title: IsLineIntersect
	 * @date 2018年12月25日 下午5:09:16
	 * @param line_first
	 * @param line_second
	 * @return
	 */
	public boolean IsLineIntersect(Point[] line_first, Point[] line_second) {
		LineSeg u = new LineSeg();
		LineSeg v = new LineSeg();
		for (int i = 0; i < line_first.length - 1; i++) {
			for (int j = 0; j < line_second.length - 1; j++) {
				u.setS(line_first[i]);
				u.setE(line_first[i + 1]);

				v.setS(line_second[j]);
				v.setE(line_second[j + 1]);

				if (intersect(u, v)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 平移
	 * 
	 * @Title: xy_move
	 * @date 2018年12月25日 下午5:09:43
	 * @param x
	 * @param y
	 * @param sx
	 * @param sy
	 * @return
	 */
	public Point xy_move(double x, double y, double sx, double sy) {
		Point ptRet = new Point();
		ptRet.setX(x - sx);
		ptRet.setY(y - sy);
		return ptRet;
	}

	/**
	 * 点先平移后旋转
	 * 
	 * @Title: move_rotate_move
	 * @date 2018年12月25日 下午5:09:51
	 * @param x
	 * @param y
	 * @param sx
	 * @param sy
	 * @param A
	 * @return
	 */
	public Point move_rotate_move(double x, double y, double sx, double sy, double A) {
		x -= sx;
		y -= sy;
		// 旋转、平移
		Point ptRet = new Point();

		double xx = x * Math.cos(A) + y * Math.sin(A);

		if (Math.abs(xx) < 1e-8) {
			xx = 0;
		}
		double yy = -1 * x * Math.sin(A) + y * Math.cos(A);
		if (Math.abs(yy) < 1e-8) {
			yy = 0;
		}

		ptRet.setX(xx + sx);
		ptRet.setY(yy + sy);

		return ptRet;
	}

	/**
	 * 旋转点集合 多边形 折线
	 * 
	 * @Title: rotatePointArr
	 * @date 2018年12月25日 下午5:10:02
	 * @param arrInput
	 * @param centerX
	 * @param centerY
	 * @param angle
	 * @return
	 */
	public ArrayList<Point> rotatePointArr(ArrayList<Point> arrInput, double centerX, double centerY, double angle) {
		ArrayList<Point> ptArrRet = new ArrayList<Point>();

		angle = angle * Math.PI / 180;
		for (int i = 0; i < arrInput.size(); i++) {
			Point ptT = (Point) arrInput.get(i);
			ptArrRet.add(move_rotate_move(ptT.x, ptT.y, centerX, centerY, angle));
		}

		return ptArrRet;
	}

	public double transformEllipseAngle(double a, double b, double angle) {
		if (Math.abs(angle - Math.PI / 2) <= EP || Math.abs(angle - Math.PI * 3 / 2) <= EP || Math.abs(angle + Math.PI * 3 / 2) <= EP
				|| Math.abs(angle + Math.PI * 3 / 2) <= EP) {
			return angle;
		}

		double angleT = Math.atan(a * Math.tan(angle) / b);

		if (angle < -Math.PI / 2 && angle > -Math.PI * 3 / 2) {
			angleT = -Math.PI + angleT;
		}

		if (angle < -Math.PI * 3 / 2) {
			angleT = -Math.PI * 2 + angleT;
		}

		if (angle > Math.PI / 2 && angle < Math.PI * 3 / 2) {
			angleT = Math.PI + angleT;
		}

		if (angle > Math.PI * 3 / 2) {
			angleT = Math.PI * 2 + angleT;
		}

		// trace("transform
		// angle:",angleBK*180/Math.PI,angle*180/Math.PI,angleT*180/Math.PI);

		return angleT;
	}

	public Hashtable<String, Object> calcCurveOuterRect(String strCoord, double nRadiusX, double nRadiusY, double nStartA, double nEndA,
			double nRotateAngle, String strShapeType) {
		Point ptT = coord.eNtoXy(strCoord);
		return calcCurveOuterRect(ptT.x, ptT.y, nRadiusX, nRadiusY, nStartA, nEndA, nRotateAngle, strShapeType);
	}

	public Hashtable<String, Object> calcCurveOuterRect(double nX, double nY, double nRadiusX, double nRadiusY, double nStartA, double nEndA,
			double rotateAngle, String strShapeType) {
		Hashtable<String, Object> dicRet = new Hashtable<String, Object>();

		ArrayList<Point> list = shapeToPointArray(nX, nY, nRadiusX, nRadiusY, nStartA, nEndA, strShapeType);

		list = rotatePointArr(list, nX, nY, rotateAngle);

		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;

		for (int i = 0; i < list.size(); i++) {
			Point pt = (Point) list.get(i);
			if (i == 0) {
				minX = pt.x;
				minY = pt.y;

				maxX = pt.x;
				maxY = pt.y;
				continue;
			}
			if (minX > pt.x) {
				minX = pt.x;
			}
			if (maxX < pt.x) {
				maxX = pt.x;
			}
			if (minY > pt.y) {
				minY = pt.y;
			}
			if (maxY < pt.y) {
				maxY = pt.y;
			}
		}

		dicRet.put("left", minX);
		dicRet.put("top", maxY);
		dicRet.put("right", maxX);
		dicRet.put("bottom", minY);

		return dicRet;
	}

	public Hashtable<String, Object> calcPolygonOuterRect(String strNE) {
		Hashtable<String, Object> dicRet = new Hashtable<String, Object>();

		int nLen = strNE.length() / 15;
		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;

		for (int i = 0; i < nLen; i++) {
			String strT = strNE.substring(i * 15, (i + 1) * 15);
			Point pt = coord.eNtoXy(strT);
			if (i == 0) {
				minX = pt.x;
				minY = pt.y;

				maxX = pt.x;
				maxY = pt.y;
				continue;
			}
			if (minX > pt.x) {
				minX = pt.x;
			}
			if (maxX < pt.x) {
				maxX = pt.x;
			}
			if (minY > pt.y) {
				minY = pt.y;
			}
			if (maxY < pt.y) {
				maxY = pt.y;
			}
		}

		dicRet.put("left", minX);
		dicRet.put("top", maxY);
		dicRet.put("right", maxX);
		dicRet.put("bottom", minY);

		return dicRet;
	}

	public String GetCurveGeoCalcCoord(double nX, double nY, double nRadiusX, double nRadiusY, double nStartA, double nEndA, double rotateAngle,
			String strShapeType) {
		StringBuffer sb = new StringBuffer();
		ArrayList<Point> list = shapeToPointArray(nX, nY, nRadiusX, nRadiusY, nStartA, nEndA, strShapeType);

		list = rotatePointArr(list, nX, nY, rotateAngle);

		for (int i = 0; i < list.size(); i++) {
			Point pt = (Point) list.get(i);
			sb.append((int) pt.x);
			sb.append(",");
			sb.append((int) pt.y);
			if (i != list.size() - 1) {
				sb.append(";");
			}
		}

		return sb.toString();
	}

	public String GetCurveGeoCalcCoord(String strCoord, double nRadiusX, double nRadiusY, double nStartA, double nEndA, double rotateAngle,
			String strShapeType) {
		Point ptCenter = coord.eNtoXy(strCoord);

		return GetCurveGeoCalcCoord(ptCenter.x, ptCenter.y, nRadiusX, nRadiusY, nStartA, nEndA, rotateAngle, strShapeType);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param sx
	 * @param sy
	 * @param A
	 * @return
	 */
	public Point xy_move_xz(double x, double y, double sx, double sy, double A) {
		/* 旋转、平移 */
		Point ptRet = new Point(0, 0);

		double xx = x * Math.cos(A) + y * Math.sin(A);

		if (Math.abs(xx) < 1e-8) {
			xx = 0;
		}
		double yy = -1 * x * Math.sin(A) + y * Math.cos(A);
		if (Math.abs(yy) < 1e-8) {
			yy = 0;
		}

		xx = xx - sx;
		yy = yy - sy;

		ptRet.x = xx;
		ptRet.y = yy;

		return ptRet;
	}

	/**
	 * 
	 * @param arrInputP
	 * @param nWidth
	 * @return
	 */
	public HashMap<String, Object> genBuffer(ArrayList<Point> arrInputP, double nWidth) {
		ArrayList<Point> arrInput = new ArrayList<Point>();
		arrInput.add((Point) arrInputP.get(0));
		for (int t = 1; t < arrInputP.size(); t++) {
			Point ptC = (Point) arrInputP.get(t);
			Point ptP = (Point) arrInputP.get(t - 1);

			if (ptC.x == ptP.x || ptC.y == ptP.y) {
				continue;
			}

			arrInput.add(arrInputP.get(t));
		}

		HashMap<String, Object> dicRet = new HashMap<String, Object>();

		ArrayList<Point> arrRet = new ArrayList<Point>();
		ArrayList<Integer> arrDir = new ArrayList<Integer>();

		for (int i = 0; i < arrInput.size() - 1; i++) {
			double angle = calcAngle((Point) arrInput.get(i), (Point) arrInput.get(i + 1));
			angle += 90;

			if (angle >= 360) {
				angle -= 360;
			}

			if (angle >= 0 && angle < 90) {
				arrDir.add(1);
			} else if (angle >= 90 && angle < 180) {
				arrDir.add(2);
			} else if (angle >= 180 && angle < 270) {
				arrDir.add(3);
			} else if (angle >= 270 && angle < 360) {
				arrDir.add(4);
			}
		}

		BufferLine bufferLine = new BufferLine();
		ArrayList<BufferLineEntity> arrLine = bufferLine.genLineBuffer(arrInput, nWidth);

		ArrayList<LineSeg> arrInner = new ArrayList<LineSeg>();
		ArrayList<LineSeg> arrOuter = new ArrayList<LineSeg>();
		for (int t = 0; t < arrLine.size(); t++) {
			BufferLineEntity bufferLineEntity = (BufferLineEntity) arrLine.get(t);

			int nDir = Integer.parseInt(arrDir.get((int) Math.floor(t)).toString());

			LineSeg linesegInner = new LineSeg();
			LineSeg linesegOuter = new LineSeg();

			if (nDir == 1 || nDir == 4) {
				linesegInner.s = bufferLineEntity.ptFE;// (Point) arrLine.get(t
														// + 1);
				linesegInner.e = bufferLineEntity.ptSE;// (Point) arrLine.get(t
														// + 2);

				linesegOuter.s = bufferLineEntity.ptFS;// (Point)
														// arrLine.get(t);
				linesegOuter.e = bufferLineEntity.ptSS;// (Point) arrLine.get(t
														// + 3);
			} else {
				linesegInner.s = bufferLineEntity.ptFS;// (Point)
														// arrLine.get(t);
				linesegInner.e = bufferLineEntity.ptSS;// (Point) arrLine.get(t
														// + 3);

				linesegOuter.s = bufferLineEntity.ptFE;// (Point) arrLine.get(t
														// + 1);
				linesegOuter.e = bufferLineEntity.ptSE;// (Point) arrLine.get(t
														// + 2);
			}

			arrInner.add(linesegInner);
			arrOuter.add(linesegOuter);
		}

		if (arrInner.size() == 0 || arrOuter.size() == 0) {
			return dicRet;
		}

		ArrayList<Point> arrPtInner = new ArrayList<Point>();
		ArrayList<Point> arrPtOuter = new ArrayList<Point>();

		arrPtInner.add(((LineSeg) arrInner.get(0)).s);
		for (int j = 0; j < arrInner.size() - 1; j++) {
			HashMap<String, Object> dicT = intersectionAsline((LineSeg) arrInner.get(j), (LineSeg) arrInner.get(j + 1));
			if ((Boolean) dicT.get("是否相交")) {
				arrPtInner.add((Point) dicT.get("交点"));
			} else {
				arrPtInner.add(((LineSeg) arrInner.get(j)).e);
			}
		}
		arrPtInner.add(((LineSeg) arrInner.get(arrInner.size() - 1)).e);

		arrPtOuter.add(((LineSeg) arrOuter.get(0)).s);

		for (int k = 0; k < arrOuter.size() - 1; k++) {

			HashMap<String, Object> dicT = intersectionAsline((LineSeg) arrOuter.get(k), (LineSeg) arrOuter.get(k + 1));
			if ((Boolean) dicT.get("是否相交")) {
				arrPtOuter.add((Point) dicT.get("交点"));
			} else {
				arrPtOuter.add((Point) ((LineSeg) arrOuter.get(k)).e);
			}
		}
		arrPtOuter.add((Point) ((LineSeg) arrOuter.get(arrOuter.size() - 1)).e);

		for (int l = 1; l < arrPtInner.size(); l++) {
			arrRet.add((Point) arrPtInner.get(l));
		}

		for (int m = arrPtOuter.size() - 1; m >= 0; m--) {
			arrRet.add((Point) arrPtOuter.get(m));
		}
		arrRet.add((Point) arrPtInner.get(0));

		dicRet.put("缓冲区", arrLine);
		dicRet.put("包线", arrRet);
		dicRet.put("方向", arrDir);

		return dicRet;
	}

	/**
	 * 
	 * @param l1
	 * @param l2
	 * @return
	 */
	public HashMap<String, Object> intersectionAsline(LineSeg l1, LineSeg l2) {
		HashMap<String, Object> hashRet = new HashMap<String, Object>();

		Line ll1 = makeline(l1.s, l1.e);
		Line ll2 = makeline(l2.s, l2.e);
		hashRet = lineintersect(ll1, ll2);

		return hashRet;
	}

	/**
	 * 
	 * @param nX
	 * @param nY
	 * @param nRadiusX
	 * @param nRadiusY
	 * @param nStartA
	 * @param nEndA
	 * @param shapeType
	 * @return
	 */
	public ArrayList<Point> shapeToPointArray(double nX, double nY, double nRadiusX, double nRadiusY, double nStartA, double nEndA,
			String shapeType) {
		ArrayList<Point> arrPtDrawing = new ArrayList<Point>();

		nStartA += 90;
		nEndA += 90;

		double temp = nStartA;
		nStartA = nEndA;
		nEndA = temp;
		nStartA = 180 - nStartA;
		nEndA = 180 - nEndA;

		if (nStartA > nEndA) {
			nStartA = nStartA - 360;
		}

		nStartA = nStartA * Math.PI / 180.0;
		nEndA = nEndA * Math.PI / 180.0;

		int DELTA_COUNT = 100;

		if (nRadiusX >= 0 && nRadiusX <= 25 && nRadiusY >= 0 && nRadiusY <= 25) {
			DELTA_COUNT = 15;
		} else if (nRadiusX >= 25 && nRadiusX <= 50 && nRadiusY >= 25 && nRadiusY <= 50) {
			DELTA_COUNT = 50;
		} else if (nRadiusX >= 50 && nRadiusX <= 100 && nRadiusY >= 50 && nRadiusY <= 100) {
			DELTA_COUNT = 50;
		} else if (nRadiusX >= 100 && nRadiusX <= 250 && nRadiusY >= 100 && nRadiusY <= 250) {
			DELTA_COUNT = 75;
		} else if (nRadiusX >= 250 && nRadiusX <= 500 || nRadiusY >= 250 && nRadiusY <= 500) {
			DELTA_COUNT = 100;
		} else if (nRadiusX >= 500 && nRadiusX <= 750 || nRadiusY >= 500 && nRadiusY <= 750) {
			DELTA_COUNT = 150;
		} else if (nRadiusX >= 750 || nRadiusY >= 750) {
			DELTA_COUNT = 250;
		}

		double deltaA = 2 * Math.PI / DELTA_COUNT;

		Point ptStart = new Point();
		Boolean bFirst = true;

		for (int i = 0; i <= DELTA_COUNT; i++) {
			double angle = nStartA + deltaA * i;
			if (angle + deltaA > nEndA) {
				angle = nEndA;
			}

			double angleT = transformEllipseAngle(nRadiusX, nRadiusY, angle);
			double x = nRadiusX * Math.cos(angleT);
			double y = nRadiusY * Math.sin(angleT);

			Point ptT = xy_move(x, y, -nX, -nY);

			arrPtDrawing.add(ptT);

			if (bFirst) {
				ptStart.x = ptT.x;
				ptStart.y = ptT.y;
				bFirst = false;
			}

			if (angle + deltaA > nEndA) {
				break;
			}
		}

		if ("扇形".equals(shapeType)) {
			arrPtDrawing.add(new Point(nX, nY));
			arrPtDrawing.add(ptStart);

		} else if ("弦".equals(shapeType)) {
			arrPtDrawing.add(ptStart);
		} else if ("弧形".equals(shapeType)) {

		}

		return arrPtDrawing;
	}
	//根据圆心，开始角度、终止角度计算经过的弧线点位
	public ArrayList<Point> shapeToPointArray(double nX, double nY, double nR, double nStartA, double nEndA,long stepCount,boolean clockwise) {
		ArrayList<Point> arrPtDrawing = new ArrayList<Point>();
		nStartA += 90;
		nEndA += 90;
		
		if(clockwise) {
			double temp = nStartA;
			nStartA = nEndA;
			nEndA = temp;
		}
		nStartA = 180 - nStartA;
		nEndA = 180 - nEndA;
		
		if (nStartA > nEndA) {
			nStartA = nStartA - 360;
		}
		nStartA = nStartA * Math.PI / 180.0;
		nEndA = nEndA * Math.PI / 180.0;
		
		double deltaA = 2 * Math.PI / stepCount;
		
		double angle=nStartA;
		while (angle+deltaA<nEndA) {
			angle+= deltaA;
			double angleT = transformEllipseAngle(nR, nR, angle);
			double x = nR * Math.cos(angleT);
			double y = nR * Math.sin(angleT);
			Point ptT = xy_move(x, y, -nX, -nY);
			arrPtDrawing.add(ptT);
		}
		if(clockwise) {
			Collections.reverse(arrPtDrawing);
		}
		return arrPtDrawing;
	}

	/**
	 * 缩放
	 * 
	 * @param x
	 * @param y
	 * @param cx
	 * @param cy
	 * @param iconSizeX
	 * @param iconSizeY
	 * @return
	 */
	public Point resize(double x, double y, double cx, double cy, double iconSizeX, double iconSizeY) {
		Point ptRet = new Point(x, y);

		ptRet.x *= (iconSizeX);
		ptRet.y *= (iconSizeY);

		ptRet.x += (cx - iconSizeX / 2);
		ptRet.y += (cy - iconSizeY / 2);

		return ptRet;
	}

	/**
	 * 
	 * @param arrInput
	 * @return
	 */
	public HashMap<String, Object> getOuterRect(ArrayList<Point> arrInput) {
		HashMap<String, Object> dicRet = new HashMap<String, Object>();
		dicRet.put("left", 0.0);
		dicRet.put("top", 0.0);
		dicRet.put("right", 0.0);
		dicRet.put("bottom", 0.0);

		dicRet.put("left_seq", 0.0);
		dicRet.put("top_seq", 0.0);
		dicRet.put("right_seq", 0.0);
		dicRet.put("bottom_seq", 0.0);

		if (arrInput == null) {
			return dicRet;
		}

		dicRet.put("left", ((Point) arrInput.get(0)).x);
		dicRet.put("top", ((Point) arrInput.get(0)).y);
		dicRet.put("right", ((Point) arrInput.get(0)).x);
		dicRet.put("bottom", ((Point) arrInput.get(0)).y);

		for (int i = 0; i < arrInput.size(); i++) {

			if (((Point) arrInput.get(i)).x < Double.parseDouble(dicRet.get("left").toString())) {
				dicRet.put("left", ((Point) arrInput.get(i)).x);
				dicRet.put("left_seq", i);
			}

			if (((Point) arrInput.get(i)).x > Double.parseDouble(dicRet.get("right").toString())) {
				dicRet.put("right", ((Point) arrInput.get(i)).x);
				dicRet.put("right_seq", i);
			}

			if (((Point) arrInput.get(i)).y < Double.parseDouble(dicRet.get("top").toString())) {
				dicRet.put("top", ((Point) arrInput.get(i)).y);
				dicRet.put("top_seq", i);
			}

			if (((Point) arrInput.get(i)).y > Double.parseDouble(dicRet.get("bottom").toString())) {
				dicRet.put("bottom", ((Point) arrInput.get(i)).y);
				dicRet.put("bottom_seq", i);
			}
		}

		return dicRet;
	}

	/**
	 * 根据线缓冲区的经纬度坐标点集合、宽度，生成线缓冲区包线的经纬度坐标点集合
	 * 
	 * @Title: getBufferLineRectOfJwd
	 * @date 2018年12月25日 下午5:11:00
	 * @param jwdsWgs84
	 * @param width
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Jwd[] getBufferLineRectOfJwd(Jwd[] jwdsWgs84, double width) {
		if (width <= 0 || jwdsWgs84 == null || jwdsWgs84.length < 2)
			return null;

		// 计算投影坐标集合
		ArrayList<Point> points = new ArrayList<Point>();
		Coord coord = new Coord();
		for (int i = 0; i < jwdsWgs84.length; i++) {
			points.add(coord.lQtoXy(jwdsWgs84[i].getJd(), jwdsWgs84[i].getWd()));
		}

		// 根据投影坐标集合计算包线
		Map<String, Object> map = new GeometryCalc().genBuffer(points, width);
		// 缓冲区ArrayList<BufferLineEntity> arrLine
		// 包线ArrayList<Point> arrRet
		ArrayList<Point> arrRet = (ArrayList<Point>) map.get("包线");
		Point point = null;
		for (int i = 0; i < arrRet.size(); i++) {
			point = arrRet.get(i);
			System.out.println(point.x + "," + point.y);
		}

		return null;
	}

	/**
	 * 判断点是否在多边形内
	 * 
	 * @Title: isPointInPolygon
	 * @date 2018年12月25日 下午5:11:10
	 * @param point
	 * @param points
	 * @return
	 */
	public boolean isPointInPolygon(Point point, Point[] points) {
		boolean c = false;

		for (int i = 0, j = points.length - 1; i < points.length; j = i++) {
			if (((points[i].y > point.y) != (points[j].y > point.y))
					&& (point.x < (points[j].x - points[i].x) * (point.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)) {
				c = !c;
			}
		}

		return c;
	}

	public GeometryCalc() {
	}

	public static void main(String[] args) {
//		Point[] points=new Point[]{new Point(0,1),new Point(1,0),new Point(2,1),new Point(3,1),new Point(4,2),new Point(3,3),new Point(1,3)};
//		GeometryCalc util=new GeometryCalc();
//		Point p=null;
//		
//		p=new Point(3.5,2.4);
//		System.out.println(util.isPointInPolygon(p, points));
	}
}
