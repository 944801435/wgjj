package com.uav.web.view.uavOnline.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.uav.base.common.Constants;
import com.uav.base.util.DateUtil;
import com.uav.base.util.PropertiesUtil;
import com.uav.base.util.SpringContextUtil;

import net.sf.json.JSONObject;

public class RecurHandler {
	private static final Logger log = Logger.getLogger(RecurHandler.class);

	private UavOnlineWebSocketServer ws = null;
	private String begTime = null;
	private String endTime = null;
	private String speed = "1";

	private boolean doPush = false;
	private boolean doLoad = false;
	private boolean doPause = false;// 暂停重放
	private boolean doStop = false;
	private long doPauseStartTime = 0;// 暂停开始时间
	private int pauseWaitMaxTime = 3600;// 暂停等待最大时间（秒）
	private boolean loadErr = false;// 加载出错
	private boolean loadCmp = false;// 加载信息完毕
	private static final long maxBufSize = 10;// 队列中元素超出该数量，暂停加载
	Queue<Map<String, Object>> queue = new ArrayDeque<Map<String, Object>>();// 先进先出队列，数据获取线程压入数据，推送线程消费数据
	private Map<String, UavOnlineVo> lastTimeMap = new Hashtable<String, UavOnlineVo>();// 用于记录最后信号时间，以进行消批处理

	// 显控推送等待时长
	public static final String UAV_ONLINE_SLEEP_TIME = PropertiesUtil.getPropertyValue("UAV_ONLINE_SLEEP_TIME", "2");// 单位秒
	
	static {
	}

	public RecurHandler(UavOnlineWebSocketServer ws, String begTime, String endTime, String speed) {
		this.ws = ws;
		this.begTime = begTime;
		this.endTime = endTime;
		this.speed = speed;
	}

	public synchronized void start() {
		// 准备数据
		startLoadDeamon();

		// 发送ready信号
		if (ws.singleClientMessage("recurReady")) {
			// 启动推送进程
			startSenderDeamon();
		} else {
			// 未启动推送进程，不再load数据
			doLoad = false;
		}
	}

	public synchronized void stop() {
		doLoad = false;
		queue = new ArrayDeque<Map<String, Object>>();
		doPush = false;
		doPause = false;
		doStop = true;
		lastTimeMap = new Hashtable<String, UavOnlineVo>();
	}

	public synchronized void reset() {
		doLoad = false;
		queue = new ArrayDeque<Map<String, Object>>();
		doPush = false;
		doPause = false;
		doStop = false;
		lastTimeMap = new Hashtable<String, UavOnlineVo>();
		while (!loadCmp) {
			try {
				wait(50);
			} catch (InterruptedException e) {
			}
		}
		loadCmp = false;
		loadErr = false;// 加载出错
		start();
	}

	public synchronized void pause() {
		doPause = true;
		doPauseStartTime = new Date().getTime();
	}

	public synchronized void toContinue() {
		doPause = false;
		doPauseStartTime = 0;
	}

	// 启动重演数据加载进程
	public synchronized void startLoadDeamon() {
		doLoad = true;
		loadCmp = false;

		new Thread(new Runnable() {
			@Override
			public void run() {
				Session sess = null;
				PreparedStatement pstmt1 = null;
				ResultSet rs = null;
				try {
					SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
					sess = sessionFactory.openSession();
					@SuppressWarnings("deprecation")
					Connection conn = sess.connection();
					/*
						`SN` varchar(16),	-- 无人机编号
						`SRC` varchar(16),		-- 数据源 ADSB|EBOX|LY|SIM|FIMS
						`PLANID` varchar(16),	-- 计划ID
						`LON` double,	-- 经度（度）
						`LAT` double,	-- 纬度（度）
						`ALT` double,	-- 高度（米）
						`HEI` double,	-- 真高（米）
						`SPE` double,	-- 速度（米/秒）
						`DIR` int(4),	-- 航向（度）,以正北为0度的顺时针角度
						`HDOP` double,	-- 水平定位精度
						`LVL` varchar(4),	-- 无人机类型，1微型、2轻型、3小型、4中型、5大型
						`TYPE` varchar(4),	-- 无人机类别，1固定翼、2单旋翼、3多旋翼、4飞艇、5其它
						`OWNTYPE` varchar(64),	-- 用户性质：1个人用户；2单位用户
						`OWNNAME` varchar(64),	-- 用户名称
						`PHONE` varchar(64),	-- 用户电话
						`WRN` varchar(256),	-- 告警信息，NN:未登记飞机飞行,NP:无计划飞行,LC:计划飞行偏航,AS-[AID]:侵入禁飞区域,VC:超出飞行高度,PLC:计划飞行偏航预警,PVC:超出飞行高度预警,PAS30-[AID]:30秒侵入禁飞区域预警,PAS60-[AID]:60秒侵入禁飞区域预警
						`TIME` datetime DEFAULT CURRENT_TIMESTAMP,
						`LTIME` datetime DEFAULT CURRENT_TIMESTAMP,
					 */
					pstmt1 = conn.prepareStatement(
							"select SN,PLANID,LON,LAT,ALT,HEI,SPE,DIR,'3',TIME,MCU,WRN,FLY_SEQ,REL_SN,SRC,MODEL from T_FLYDATA where TIME>=? and TIME<? order by TIME,SN");

					Date cur = DateUtil.parseStringToFullTime(begTime + ":00");// 2018-01-01
																				// 10:00
					Date next = null;
					long nextSecond = 0;
					Date end = DateUtil.parseStringToFullTime(endTime + ":59");
					int secondStep = Integer.valueOf(UAV_ONLINE_SLEEP_TIME) * Integer.valueOf(speed);// 时间跨度，单位：秒

					String pn = null;
					String planid = null;
					String lng = null;
					String lat = null;
					String alt = null;
					String hei = null;
					String spe = null;
					String dir = null;
					String tol = null;
					String lvl = null;
					String type = null;
					String owntype = null;
					String ownname = null;
					String phone = null;
					String time = null;
					String mcu = null;
					String wrn = null;
					String flySeq = null;
					String relSn = null;
					String src = null;
					String model = null;
					long rcvTimeSecond = 0;
					Map<String, UavOnlineVo> uavMap = null;
					Iterator<String> iter = null;
					List<String> pnList = null;
					Map<String, Object> map = null;
					DecimalFormat decimalFormat = new DecimalFormat("#0.000000");
					String curStr = null;
					String nextStr = null;
					while (doLoad && cur.compareTo(end) <= 0) {
						// 根据时间跨度计算该批次截止时间
						next = DateUtil.getNextSecond(cur, secondStep);

						curStr = DateUtil.parseFullTimeToString(cur);
						nextStr = DateUtil.parseFullTimeToString(next);
						log.info("cur:" + curStr + ",next:" + nextStr);

						// 获取>=cur and <next的飞行数据
						pstmt1.setString(1, curStr);
						pstmt1.setString(2, nextStr);
						rs = pstmt1.executeQuery();
						uavMap = new HashMap<String, UavOnlineVo>();
						UavOnlineVo vo = null;
						while (rs.next()) {
							pn = rs.getString(1);
							planid = rs.getString(2);
							lng = rs.getString(3);
							lat = rs.getString(4);
							alt = Long.toString(Math.round(rs.getDouble(5)));
							hei = Long.toString(Math.round(rs.getDouble(6)));
							spe = Long.toString(Math.round(rs.getDouble(7)));
							dir = rs.getString(8);
							tol = rs.getString(9);
							time = rs.getString(10);
							mcu = rs.getString(11);
							wrn = rs.getString(12);
							flySeq = rs.getString(13);
							relSn = rs.getString(14);
							src = rs.getString(15);
							model = rs.getString(16);
							planid = (planid == null) ? Constants.STR_BLANK : planid;
							hei = (hei == null) ? Constants.STR_ZERO : hei;

							ownname = "";
							phone = "";
							rcvTimeSecond = DateUtil.parseStringToFullTime(time).getTime() / 1000;

							lng = decimalFormat.format(Double.parseDouble(lng));
							lat = decimalFormat.format(Double.parseDouble(lat));

							if (uavMap.containsKey(pn)) {
								vo = uavMap.get(pn);
								vo.setPlanid(planid);
								vo.setLng(lng);
								vo.setLat(lat);
								vo.setAlt(hei);
								vo.setSpe(spe);
								vo.setDir(dir);
								vo.setTol(tol);
								vo.setWrn(wrn);
								vo.setTime(time);
								vo.setTimeSecond(rcvTimeSecond);
							} else {
								vo = new UavOnlineVo(pn, planid, lng, lat, alt, hei, spe, dir, tol, lvl, type, owntype, ownname, phone, wrn, "", mcu,
										time);
								vo.setTimeSecond(rcvTimeSecond);
								uavMap.put(pn, vo);
							}

							vo.setRelSn(relSn);
							vo.setSrc(src);
							vo.setModel(model);
							vo.setBizFlyId(flySeq);
							// 记录最后信号时间
							lastTimeMap.put(pn, vo);
						}

						// 遍历lastTimeMap，看看到next时间为止，哪些已经超时需要消批
						nextSecond = next.getTime() / 1000;
						iter = lastTimeMap.keySet().iterator();
						pnList = new LinkedList<String>();
						while (iter.hasNext())
							pnList.add(iter.next());
						for (String key : pnList) {
							vo = lastTimeMap.get(key);
							if ((nextSecond - vo.getTimeSecond()) > 30) {
								// 超时无信号，需要消批
								lastTimeMap.remove(key);

								// 插入消批信号
								vo.setTol(Constants.UAV_FLY_DOWN);
								uavMap.put(key, vo);

								log.debug("消批：pn=" + vo.getPn() + "，lastTime=" + vo.getTime() + "，nowTime=" + DateUtil.parseFullTimeToString(next));
							}
						}
						// 将结果压入队列
						map = new HashMap<String, Object>();
						map.put("time", nextStr);
						map.put("data", uavMap);
						queue.add(map);

						cur = next;

						// 如果超出数量限制，等待
						while (queue.size() > maxBufSize) {
							try {
								Thread.sleep(100);
							} catch (Exception e) {
							}
						}
					}

					loadCmp = true;
				} catch (Exception e) {
					log.error("重演数据加载失败！", e);
					loadErr = true;
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (Exception e2) {
						log.error("重演数据加载进程异常", e2);
					}
					try {
						if (pstmt1 != null)
							pstmt1.close();
					} catch (Exception e2) {
						log.error("重演数据加载进程异常", e2);
					}
					try {
						if (sess != null)
							sess.close();
					} catch (Exception e2) {
						log.error("重演数据加载进程异常", e2);
					}
				}

				log.info("重演数据加载进程结束，sessionId=" + ws.getSessionId());
			}
		}).start();

		log.info("重演数据加载进程启动成功，sessionId=" + ws.getSessionId());
	}

	// 启动重演数据推送进程
	public synchronized void startSenderDeamon() {
		doPush = true;

		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				int millSecondStep = Integer.valueOf(UAV_ONLINE_SLEEP_TIME) * 1000;
				Map<String, Object> map = null;// 要推送的目标数据
				long curTime = 0;
				long nextTime = 0;
				JSONObject jsonObject = null;
				String msg = null;
				// 如果队列中没有数据，等待
				map = queue.poll();
				while (map == null && !loadErr && !loadCmp) {
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						log.error("重演数据加载进程异常", e);
					}
					map = queue.poll();
				}
				if (map != null) {
					// 获取到待推送数据，开始推送，记录开始时间
					nextTime = new Date().getTime();

					while (doPush) {
						if (doPause) {// 暂停
							if ((new Date().getTime() / 1000 - doPauseStartTime / 1000) > pauseWaitMaxTime) {
								break;
							}
							try {
								Thread.sleep(50);
							} catch (Exception e) {
								log.error("重演数据加载进程异常", e);
							}
							nextTime = new Date().getTime();
							continue;
						}
						// 判断是否到该推送的时间，没到则等待
						curTime = new Date().getTime();
						while (curTime < nextTime) {
							try {
								Thread.sleep(50);
							} catch (Exception e) {
								log.error("重演数据加载进程异常", e);
							}
							curTime = new Date().getTime();
						}
						nextTime += millSecondStep;// 下次可以开始推送的时间

						try {
							// 执行推送
							jsonObject = new JSONObject();
							jsonObject.put("time", (String) map.get("time"));
							jsonObject.put("uav", ((Map<String, UavOnlineVo>) map.get("data")).values().toArray(new UavOnlineVo[] {}));
							msg = jsonObject.toString();
							log.info("重演数据推送消息：" + msg);
							ws.singleClientMessage(msg);
						} catch (Exception e) {
							log.error("重演数据推送失败！", e);
						}

						// 获取下一个待推送数据
						map = queue.poll();
						while (map == null && !loadErr && !loadCmp) {
							try {
								Thread.sleep(100);
							} catch (Exception e) {
								log.error("重演数据加载进程异常", e);
							}
							map = queue.poll();
						}
						if (map == null)
							break;
					}
				}
				if (doPause) {
					log.info("重演暂停时间超长数据推送进程被迫结束，sessionId=" + ws.getSessionId());
					// 发送recurPauseToStop信号
					ws.singleClientMessage("recurPauseToStop");
				} else if (doStop) {
					log.info("重演数据推送进程被前端结束，sessionId=" + ws.getSessionId());
					// 发送recurStop信号
					ws.singleClientMessage("recurStop");
				} else {
					log.info("重演数据推送进程结束，sessionId=" + ws.getSessionId());
					// 发送finish信号
					ws.singleClientMessage("recurFinish");
				}
			}
		}).start();

		log.info("重演数据推送进程启动成功，sessionId=" + ws.getSessionId());
	}

	public static void main(String[] args) {
		RecurHandler handler = new RecurHandler(new UavOnlineWebSocketServer(), "2018-06-06 09:00", "2018-06-06 09:05", "1");
		handler.start();
	}
}