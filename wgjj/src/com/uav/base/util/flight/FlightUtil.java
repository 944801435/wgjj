package com.uav.base.util.flight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uav.base.common.BaseDAO;
import com.uav.base.model.Airport;
import com.uav.base.model.Airway;
import com.uav.base.model.AirwayGeomSegment;
import com.uav.base.model.AirwayView1;
import com.uav.base.model.Waypoint;
import com.uav.base.util.SpaceUtil;

/**
 * 航线相关工具类
 * 
 * @Title: FlightUtil.java
 * @author mq  
 * @date 2021年11月5日 下午4:05:05
 */
@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FlightUtil {

//	private static final double maxDistanceToAirport = 300000;// 与机场最近点的最大距离
//	private static final String specialAirportIdent = "VMMC";// 特殊的机场四字码
	private static final double maxDistanceBetweenWaypoint = 300000;// 两航点的最大距离

	@Autowired
	private BaseDAO baseDAO;

	// 机场数据
	public Map<String, Airport> airportMap;
	// 航点数据
	public Map<String, Map<String, Waypoint>> wptMap;
	// 航路数据
	public Map<String, List<Airway>> airwayViewMap;

//	@PostConstruct
	public void init() {
		// 加载机场数据
		loadAirportData();
		// 加载航点数据
		loadWayPointData();
		// 加载航路数据
		loadAirwayData();
	}

	// 加载机场数据
	public void loadAirportData() {
		List<Airport> airportList = baseDAO.findList("from Airport");
		Map<String, Airport> airportMap = new HashMap<String, Airport>();
		for (Airport obj : airportList) {
			airportMap.put(obj.getIdent(), obj);
		}
		this.airportMap = airportMap;
	}

	// 加载航点数据
	public void loadWayPointData() {
		List<Waypoint> airportList = baseDAO.findList("from Waypoint");
		Map<String, Map<String, Waypoint>> wptMap = new HashMap<String, Map<String, Waypoint>>();
		for (Waypoint obj : airportList) {
			Map<String, Waypoint> tmp = new HashMap<String, Waypoint>();
			if (wptMap.containsKey(obj.getIdent())) {
				tmp = wptMap.get(obj.getIdent());
			}
			tmp.put(obj.getWaypointId(), obj);
			wptMap.put(obj.getIdent(), tmp);
		}
		this.wptMap = wptMap;
	}

	// 加载航路数据
	public void loadAirwayData() {
		List<AirwayView1> airwayViewList = baseDAO.findList("from AirwayView1");
		Map<String, List<Airway>> airwayViewMap = new HashMap<String, List<Airway>>();
		String airwayIdTmp = "$";
		Airway way = null;
		for (AirwayView1 obj : airwayViewList) {
			if (!airwayIdTmp.equals(obj.getAirwayId())) {
				way = new Airway(obj.getAirwayId(), obj.getAirwayName(), obj.getAirwayType(), obj.getAirwayFragmentNo(), obj.getDirection(),
						obj.getAltitudeMode(), obj.getMinimumAltitude(), obj.getMaximumAltitude(), obj.getWidthMode(), obj.getWidth());
				way.setGeomSegList(new ArrayList<AirwayGeomSegment>());
				List<Airway> airwayList = new ArrayList<Airway>();
				if (airwayViewMap.containsKey(obj.getAirwayName())) {
					airwayList = airwayViewMap.get(obj.getAirwayName());
				}
				airwayList.add(way);
				airwayViewMap.put(obj.getAirwayName(), airwayList);
				airwayIdTmp = obj.getAirwayId();
			}
			AirwayGeomSegment seg = new AirwayGeomSegment(obj.getAirwayGeomSegmentId(), obj.getAirwayId(), obj.getFromWaypointId(),
					obj.getToWaypointId(), obj.getSequenceNo(), obj.getSegmentMinimumAltitude(), obj.getSegmentMaximumAltitude(), obj.getWidth1(),
					obj.getWidth2(), obj.getFromIdent(), obj.getFromRegion(), obj.getFromLonx(), obj.getFromLaty(), obj.getToIdent(),
					obj.getToRegion(), obj.getToLonx(), obj.getToLaty());
			if (seg.getMaximumAltitude() == 0 || seg.getMaximumAltitude() > 12500) {
				seg.setMaximumAltitude(12500);
			}
			way.getGeomSegList().add(seg);
		}
		this.airwayViewMap = airwayViewMap;
	}

	/**
	 * 根据航路和起飞机场，获得航路上的所有航点
	 * 
	 * @Title: getFlightWayList
	 * @author mq
	 * @date 2021年11月5日 下午4:31:52
	 * @param startAriportIdent	起飞机场
	 * @param endAirportIdent	降落机场
	 * @param flightBody
	 * @return
	 * @throws FlightException 
	 */
	public List<FlightWayVo> getFlightWayList(String startAriportIdent, String endAirportIdent, String flightBody) throws FlightException {
		Airport startAirport = airportMap.get(startAriportIdent);
		if (startAirport == null) {
			throw new FlightException("起飞机场不存在！");
		}
		Airport endAirport = airportMap.get(endAirportIdent);
		if (endAirport == null) {
			throw new FlightException("降落机场不存在！");
		}
		List<String> formatRouteList = formatFPLRoute(flightBody);
		if (formatRouteList.size() == 0) {
			throw new FlightException("计划航线为空！");
		}
		List<Waypoint> pointList = new ArrayList<Waypoint>();

		String error = getAllPointOfEstRoute(startAirport, formatRouteList, pointList);
		if (StringUtils.isNotBlank(error)) {
			throw new FlightException(error);
		}
		if (pointList.size() == 0) {
			throw new FlightException("未解析出航点！");
		}
//		// 判断第一点与机场距离以及最后一点与机场距离，相距过大则计划舍去
//		Waypoint lastPoint = pointList.get(pointList.size() - 1);
//		double endDistance = SpaceUtil.distance(lastPoint.getLonx(), lastPoint.getLaty(), endAirport.getLonx(), endAirport.getLaty());
//		if (endDistance > maxDistanceToAirport && !specialAirportIdent.equals(endAirport.getIdent())) {
//			String errMsg = "【" + startAriportIdent + "】机场-【" + endAirportIdent + "】机场与最后一点【" + lastPoint.getIdent() + "】距离过大";
//			throw new FlightException(errMsg);
//		}
//		Waypoint firstPoint = pointList.get(0);
//		double beginDistance = SpaceUtil.distance(firstPoint.getLonx(), firstPoint.getLaty(), startAirport.getLonx(), startAirport.getLaty());
//		if (beginDistance > maxDistanceToAirport && !specialAirportIdent.equals(startAirport.getIdent())) {
//			String errMsg = "【" + startAriportIdent + "】机场-【" + endAirportIdent + "】机场与第一点【" + firstPoint.getIdent() + "】距离过大";
//			throw new FlightException(errMsg);
//
//		}
		List<FlightWayVo> flightWayList = new ArrayList<FlightWayVo>();
		for (Waypoint point : pointList) {
			FlightWayVo flightWayVo = new FlightWayVo(point.getIdent(), point.getLonx(), point.getLaty(), point.getAltitude());
			flightWayList.add(flightWayVo);
		}
		return flightWayList;
	}

	private String getAllPointOfEstRoute(Airport startAirport, List<String> formatRouteList, List<Waypoint> pointList) {
		Map<String, Integer> tempPtIdentMap = new HashMap<String, Integer>();// 存放已经解析的航点，key是ident，value是0
		for (int m = 0; m < formatRouteList.size(); m++) {
			String itemname = formatRouteList.get(m);
			if ("...".equals(itemname)) {// CSZ8042 VTSP ZGSZ||ASSAD A202 SIKOU
											// ... LANDA W130 UJ W553 ZUH W23
				// 认为俩航路点
				if (m != 0 && m != formatRouteList.size() - 1) {
					// 前一个航路点
					String error = addWaypoint(startAirport, tempPtIdentMap, pointList, formatRouteList.get(m - 1));
					if (StringUtils.isNotBlank(error)) {
						return error;
					}
					// 下一个航路点
					error = addWaypoint(startAirport, tempPtIdentMap, pointList, formatRouteList.get(m + 1));
					if (StringUtils.isNotBlank(error)) {
						return error;
					}
				}
				continue;
			}
			List<Airway> airwayList = getAirwayList(itemname);
			// 判断是否是航路
			if (airwayList != null) {
				// 允许重名航路
				String startPoint = "", endPoint = "";
				if (m != 0 && m != formatRouteList.size() - 1) {
					startPoint = formatRouteList.get(m - 1);
					endPoint = formatRouteList.get(m + 1);
				}
				List<Waypoint> tmpRoutePoint = new ArrayList<Waypoint>();
				String error = subAirway(airwayList, startPoint, endPoint, tmpRoutePoint);
				if (StringUtils.isNotBlank(error)) {
					return error;
				}
				for (Waypoint entry : tmpRoutePoint) {
					Integer iterPt = tempPtIdentMap.get(entry.getIdent());
					if (iterPt == null) {
						pointList.add(entry);
						tempPtIdentMap.put(entry.getIdent(), 0);
					}
				}
			} else if (itemname == "DCT") {
				if (m != 0 && m != formatRouteList.size() - 1) {
					// 前一个航路点
					String error = addWaypoint(startAirport, tempPtIdentMap, pointList, formatRouteList.get(m - 1));
					if (StringUtils.isNotBlank(error)) {
						return error;
					}
					// 下一点
					error = addWaypoint(startAirport, tempPtIdentMap, pointList, formatRouteList.get(m + 1));
					if (StringUtils.isNotBlank(error)) {
						return error;
					}
				}
			} else if (m > 0) {
				String error = "";
				if (isWaypoint(itemname)) {
					if (isWaypoint(formatRouteList.get(m - 1)) && !isAirway(formatRouteList.get(m - 1))) {
						error = addWaypoint(startAirport, tempPtIdentMap, pointList, formatRouteList.get(m - 1));
						if (StringUtils.isNotBlank(error)) {
							return error;
						}
					}
					error = addWaypoint(startAirport, tempPtIdentMap, pointList, itemname);
					if (StringUtils.isNotBlank(error)) {
						return error;
					}
				} else {
					return "未识别的要素【" + itemname + "】";
				}
			}
		}

		return "";
	}

	// 判断是否是航路点
	private boolean isWaypoint(String ident) {
		Map<String, Waypoint> iter = wptMap.get(ident);
		if (iter != null) {
			return true;
		}
		return false;
	}

	// 解析航路点
	private String subAirway(List<Airway> airwayList, String startPoint, String endPoint, List<Waypoint> waypointList) {
		Airway airway = null;
		boolean isExist = false;
		Map<Airway, Integer> tmpAirwayMap = new HashMap<Airway, Integer>();
		for (Airway airwayPtr : airwayList) {
			Map<Integer, AirwayGeomSegment> tempMap = new HashMap<Integer, AirwayGeomSegment>();
			for (AirwayGeomSegment segment : airwayPtr.getGeomSegList()) {
				tempMap.put(segment.getSequenceNo(), segment);
			}
			// 遍历看是否存在
			boolean isStart = false, isEnd = false;
			for (Entry<Integer, AirwayGeomSegment> entry : tempMap.entrySet()) {
				if (startPoint.equals(entry.getValue().getFromIdent()) || startPoint.equals(entry.getValue().getToIdent())) {
					isStart = true;
					Integer iterTmpAirway = tmpAirwayMap.get(airwayPtr);
					if (iterTmpAirway == null) {
						tmpAirwayMap.put(airwayPtr, 0);
					}
				}
				if (endPoint.equals(entry.getValue().getFromIdent()) || endPoint.equals(entry.getValue().getToIdent())) {
					isEnd = true;
					Integer iterTmpAirway = tmpAirwayMap.get(airwayPtr);
					if (iterTmpAirway == null) {
						tmpAirwayMap.put(airwayPtr, 1);
					}
				}
				if (isStart && isEnd) {
					isExist = true;
					airway = airwayPtr;
					tmpAirwayMap.clear();
					break;
				}
			}
			if (isExist) {
				break;
			}
		}
		if (!isExist) {
			return "点【" + startPoint + "】或【" + endPoint + "】不在【" + airwayList.get(0).getAirwayName() + "】航路上";
		}
		boolean success = subAirway(airway, startPoint, endPoint, waypointList);
		if (!success) {
			return "航线【" + airway.getAirwayName() + "】" + "解析失败！";
		}
		return "";
	}

	// 截取航路--前后两点都在航路上
	private boolean subAirway(Airway airway, String startPoint, String endPoint, List<Waypoint> waypointList) {
		Map<Integer, AirwayGeomSegment> segmentMap = new HashMap<Integer, AirwayGeomSegment>();
		// 默认升序
		for (AirwayGeomSegment segment : airway.getGeomSegList()) {
			segmentMap.put(segment.getSequenceNo(), segment);
		}
		// 组织航路所有点
		List<String> pointList = new ArrayList<String>();// ident
		Map<String, String> segmentPointMap = new HashMap<String, String>();// key=ident,value=id
		int m = 0;
		for (Entry<Integer, AirwayGeomSegment> entry : segmentMap.entrySet()) {
			pointList.add(entry.getValue().getFromIdent());
			segmentPointMap.put(entry.getValue().getFromIdent(), entry.getValue().getFromWaypointId());
			if (m == segmentMap.size() - 1) {
				pointList.add(entry.getValue().getToIdent());
				segmentPointMap.put(entry.getValue().getToIdent(), entry.getValue().getToWaypointId());
			}
			m++;
		}
		// 根据起止点截取航路点
		int startIndex = pointList.indexOf(startPoint);
		int endIndex = pointList.indexOf(endPoint);
		if (startIndex < endIndex) {
			// 正向取点
			for (int n = startIndex; n < endIndex + 1; n++) {
				String tempID = segmentPointMap.get(pointList.get(n));
				Map<String, Waypoint> iterPoint = wptMap.get(pointList.get(n));
				if (iterPoint != null) {
					Waypoint iterTmp = iterPoint.get(tempID);
					if (iterTmp != null) {
						waypointList.add(iterTmp);
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			// 反向取点
			for (int n = startIndex; n > endIndex - 1; n--) {
				String tempID = segmentPointMap.get(pointList.get(n));
				Map<String, Waypoint> iterPoint = wptMap.get(pointList.get(n));
				if (iterPoint != null) {
					Waypoint iterTmp = iterPoint.get(tempID);
					if (iterTmp != null) {
						waypointList.add(iterTmp);
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	// 根据航路名称获取航路
	private List<Airway> getAirwayList(String ident) {
		List<Airway> airwayList = airwayViewMap.get(ident);
		return airwayList;
	}

	// 根据航路名称获取航路
	private boolean isAirway(String ident) {
		List<Airway> airwayList = airwayViewMap.get(ident);
		if (airwayList != null) {
			return true;
		}
		return false;
	}

	private String addWaypoint(Airport startAirport, Map<String, Integer> tempPtIdentMap, List<Waypoint> pointList, String ptIdent) {
		// 前一个航路点
		Integer iterPt = tempPtIdentMap.get(ptIdent);
		if (iterPt == null) {
			// 未存入已解析的点
			Map<String, Waypoint> startPointMap = getWaypointByIdent(ptIdent);
			if (startPointMap.size() == 0) {
				return "未找到航路点：【" + ptIdent + "】";
			}
			if (startPointMap.size() == 1) {
				// 一个航路点
				Waypoint iterTempPoint = startPointMap.values().toArray(new Waypoint[] {})[0];
				if (pointList.size() > 0) {
					Waypoint lastPoint = pointList.get(pointList.size() - 1);
					double directDistance = SpaceUtil.distance(lastPoint.getLonx(), lastPoint.getLaty(), iterTempPoint.getLonx(),
							iterTempPoint.getLaty());
					if (directDistance < maxDistanceBetweenWaypoint) {
						pointList.add(iterTempPoint);
						tempPtIdentMap.put(ptIdent, 0);
					}
				}
			} else {
				// 多个航路点
				double lonx = 0;
				double laty = 0;
				if (tempPtIdentMap.size() == 0) {
					// 找机场最近点
					lonx = startAirport.getLonx();
					laty = startAirport.getLaty();
				} else {
					// 前一个航路点
					Waypoint lastWaypoint = pointList.get(pointList.size() - 1);
					lonx = lastWaypoint.getLonx();
					laty = lastWaypoint.getLaty();
				}
				// 获得最短距离的航点
				Waypoint waypoint = getMinDisWaypoint(lonx, laty, startPointMap);
				if (StringUtils.isNotBlank(waypoint.getIdent())) {
					pointList.add(waypoint);
					tempPtIdentMap.put(waypoint.getIdent(), 0);
				}
			}
		}
		return "";
	}

	// 获得距离某一点最近的航点
	private Waypoint getMinDisWaypoint(Double lonx, Double laty, Map<String, Waypoint> waypointMap) {
		double distance = 999999999;
		Waypoint result = new Waypoint();
		for (Waypoint waypoint : waypointMap.values()) {
			double dis = SpaceUtil.distance(lonx, laty, waypoint.getLonx(), waypoint.getLaty());
			if (dis < distance) {
				BeanUtils.copyProperties(waypoint, result);
				distance = dis;
			}
		}
		if (distance > maxDistanceBetweenWaypoint) {
			result.setIdent("");
		}
		return result;
	}

	// 根据航路点Ident获取航路点--考虑重名点
	private Map<String, Waypoint> getWaypointByIdent(String ptIdent) {
		return wptMap.get(ptIdent);
	}

	// 格式化航路
	private List<String> formatFPLRoute(String route) {
		List<String> formatRouteList = new ArrayList<String>();
		String[] routeArrSplit = route.trim().split(" ");

		if (routeArrSplit.length < 2) {
			return formatRouteList;
		}

		for (int i = 0; i < routeArrSplit.length; i++) {
			if (i == 0 || i == 1 || i == routeArrSplit.length - 1 || i == routeArrSplit.length - 2) {// 第一个或者最后一个是DCT或者是进离港点，则舍掉

				if (routeArrSplit[i] == "DCT") {
					continue;
				}
				// 校验时忽略预计航路中开头/结尾的机场进离港程序和DCT
				// 机场进离港程序代码为2-5个英文字母+1-2个数字+1个英文字母
				// ^[A-Z]{2,5}\d{1,2}[A-Z]$
				if (routeArrSplit[i].matches("^[A-Z]{2,5}\\d{1,2}[A-Z]$")) {
					continue;
				}
			}

			String str = routeArrSplit[i].trim();
			if (str == "VFR" || str == "IFR" || str == "T" || str == "") {// XX航路不能直接扔了listRoute[i].ToString().Trim().StartsWith("XX")
				continue;
			}
			if (str.indexOf("/") != -1) {// 取“/”之前的字符串

				str = str.split("/")[0];
			}
			if (str.indexOf("|") != -1) {// 取“|”之后的字符串
				str = str.substring(str.indexOf("|") + 1);
			}
			formatRouteList.add(str);
		}
		return formatRouteList;
	}

	// TODO 生成航线示意图
	public String createFlightFile(List<FlightWayVo> wayVoList){
		if(wayVoList == null || wayVoList.size() == 0){
			return null;
		}
		// TODO 获取航点周围的重要目标数据、用空活动计划数据

		// TODO 生成航线示意图时，如果出现生成不了的情况，抛出错误信息
		return null;
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		FlightUtil util = (FlightUtil) context.getBean("flightUtil");
		try {
//			List<FlightWayVo> list = util.getFlightWayList("VYNT", "ZLLL", "VYNT ASUMO B463 MDY LSO A599 LINSO GMA MAKUL GULOT SGM W144 DADOL ZAT ENTOV KAKMI G212 FJC JTG B330 CDX OMBON ELPAN BESMI");
			List<FlightWayVo> list = util.getFlightWayList("OPRN", "ZBTJ", "PURPA W112 TUSLI W187 IBANO B215 NUKTI W66 DKO W69 GUVBA");
			System.out.println(list);
		} catch (FlightException e) {
			e.printStackTrace();
		}
	}
}
