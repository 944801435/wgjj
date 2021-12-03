package com.uav.base.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.uav.base.common.Constants;
import com.uav.web.view.uavOnline.base.adsb.RedisAdsbFlyDataListener;
import com.uav.web.view.uavOnline.base.adsb.RedisAdsbFlyStsListener;

import cn.hutool.json.JSONArray;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

@Slf4j
public class RedisClientUtil {

	// 开启订阅Fd redis开关 0：关闭，1：开启
	private static final boolean SUBSCRIBE_FD_REDIS = Constants.sys_default_yes.equals(PropertiesUtil.getPropertyValue("SUBSCRIBE_FD_REDIS", "0"));

	private static final String REDIS_IP_STR = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "REDIS_IP", Constants.STR_BLANK);
	private static final String REDIS_PORT_STR = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "REDIS_PORT", Constants.STR_BLANK);
	// 航迹数据
	public static final String REDIS_QUEUE_MD = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.MD", "uav.md");
	// 态势数据
	public static final String REDIS_TOPIC_FD = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.FD", "uav.fd");
	// 降落数据
	public static final String REDIS_TOPIC_FLY_LAND = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.FLY.LAND",
			"uav.fly.land");
	// 态势入库数据
	public static final String REDIS_QUEUE_FLY_DATA = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.FLY.DATA",
			"uav.fly.data");
	// 架次数据
	public static final String REDIS_QUEUE_FLY_STS = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.FLY.STS",
			"uav.fly.sts");
	// 告警数据
	public static final String REDIS_QUEUE_WARN_STS = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.WARN.STS",
			"uav.warn.sts");
	// 飞行告警广播数据
	public static final String REDIS_TOPIC_WARN = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.WARN", "uav.warn");
	// 空域变更
	public static final String REDIS_TOPIC_SPACE = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.SPACE",
			"uav.space");
	// 白名单空域变更
	public static final String REDIS_TOPIC_SPACE_WHITE = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.SPACE.WHITE",
			"uav.space.white");
	// 反制侦测失败变更
	public static final String REDIS_TOPIC_DEV = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.DEV",
			"uav.dev");
	// 计划变更
	public static final String REDIS_TOPIC_PLAN = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.PLAN", "uav.plan");
	// 系统配置变更
	public static final String REDIS_TOPIC_CONF = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.CONF", "uav.conf");
	// 系统配置缓存key
	public static final String REDIS_CACHE_SYS_CONF = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.SYS.CONF",
			"sys.conf");
	// 无人机信息变更
	public static final String REDIS_TOPIC_UAV = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.UAV", "uav.uav");
	// 态势数据缓存前缀
	private static final String REDIS_FD_PREFIX = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.FD.PREFIX", "uav.data.");
	// 防反设备状态信息缓存前缀
	public static final String REDIS_DEV_PREFIX = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.DEV.PREFIX", "dev.");
	// 系统消批线程抢占前缀
	public static final String REDIS_THREAD_PREFIX = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.THREAD.PREFIX", "fly.");
	
	public static final String REDIS_TOPIC_ED = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.ED",
			Constants.STR_BLANK);
	// 告警通知
	public static final String REDIS_QUEUE_GJTZ = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.GJTZ",
			Constants.STR_BLANK);
	
	//试飞空域变更通知
	public static final String REDIS_TOPIC_RUN_SPACE = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.RUNSPACE","run.space");

	/*无人机反制设备状态信息*/
	public static final String REDIS_TOPIC_DEVSTS = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.DEVSTS", "dev.sts");

	//仿真态势数据Redis.Channel.FZ
	public static final String REDIS_TOPIC_FZ= PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.FZ", "fz.uav.md");
	
	/*adsb*/
	// 航迹数据
	public static final String REDIS_TOPIC_ADSB_MD = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.ADSB.MD", "adsb.md");
	// 态势数据
	public static final String REDIS_TOPIC_ADSB_FD = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.ADSB.FD", "adsb.fd");
	// 降落数据
	public static final String REDIS_TOPIC_ADSB_FLY_LAND = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.ADSB.FLY.LAND",
			"adsb.fly.land");
	// 态势入库数据
	public static final String REDIS_QUEUE_ADSB_FLY_DATA = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.ADSB.FLY.DATA",
			"adsb.fly.data");
	// 架次数据
	public static final String REDIS_QUEUE_ADSB_FLY_STS = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.Channel.ADSB.FLY.STS",
				"adsb.fly.sts");
	//系统消批线程抢占前缀
	public static final String REDIS_THREAD_PREFIX_ADSB = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.THREAD.PREFIX.ADSB", "fly.adsb.");
	// 态势数据缓存前缀
	private static final String REDIS_FD_PREFIX_ADSB = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "Redis.FD.PREFIX.ADSB", "adsb.data.");
		
	
	
	public static JedisPool jedisPool = null;

	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(1024); // 可用连接实例的最大数目,如果赋值为-1,表示不限制.
			config.setMaxIdle(8); // 控制一个Pool最多有多少个状态为idle(空闲的)jedis实例,默认值也是8
			config.setMaxWaitMillis(1000 * 5); // 等待可用连接的最大时间,单位毫秒,默认值为-1,表示永不超时/如果超过等待时间,则直接抛出异常
			config.setTestOnBorrow(true); // 在borrow一个jedis实例时,是否提前进行validate操作,如果为true,则得到的jedis实例均是可用的
			jedisPool = new JedisPool(config, REDIS_IP_STR, Integer.parseInt(REDIS_PORT_STR));
			log.info("RedisClientUtil初始化成功！");
		} catch (Exception e) {
			log.error("RedisClientUtil初始化失败！", e);
		}
	}

	// 发布队列
	public static void lpush(final String topic, final String msg) {
		if(!SUBSCRIBE_FD_REDIS){
			return;
		}
		if (StringUtils.isBlank(topic)) {
			return;
		}
		try {
			// 实现runnable接口，创建多线程并启动
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						log.error("", e);
					}
					Jedis jedis = null;
					try {
						jedis = jedisPool.getResource();
						jedis.lpush(topic, msg);
					} catch (Exception e) {
						log.error("发布队列失败！", e);
					} finally {
						close(jedis);
					}
				}
			}).start();
		} catch (Exception e) {
		}
	}

	// 阻塞式订阅队列
	public static void brpop(final String topic) {
		if(!SUBSCRIBE_FD_REDIS){
			return;
		}
		// Jedis订阅是阻塞式的，必须启动线程进行订阅
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						Jedis jedis = null;
						try {
							jedis = jedisPool.getResource();
							List<String> list = jedis.brpop(0, topic);
							JSONArray json = new JSONArray(list);
							
							log.info("Redis获得队列数据：" + json.toString());
							if (list.size() > 1) {
								// 获得数据，第一个值是key，第二个值是内容
								String key = list.get(0);
								String message = list.get(1);
								if(REDIS_QUEUE_ADSB_FLY_DATA.equals(key)) {
									//处理adsb数据入库
									RedisAdsbFlyDataListener.deal(message);
								}else if(REDIS_QUEUE_ADSB_FLY_STS.equals(key)) {
									//处理adsb架次数据入库
									RedisAdsbFlyStsListener.deal(message);
								}
							}
						} catch (Exception e) {
							log.error("获取queue=" + topic + "数据失败！", e);
						} finally {
							close(jedis);
						}
						try {
							try {
								Thread.sleep(50);
							} catch (Exception e2) {
							}
						} catch (Exception e2) {
						}
					}
				}
			}).start();

		} catch (Exception e) {
		}
	}

	public static void publish(final String topic, final String msg) {
		if(!SUBSCRIBE_FD_REDIS){
			return;
		}
		if (StringUtils.isBlank(topic)) {
			return;
		}
		try {
			// 实现runnable接口，创建多线程并启动
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						log.error("", e);
					}
					Jedis jedis = null;
					try {
						jedis = jedisPool.getResource();
						jedis.publish(topic, msg.toString());
					} catch (Exception e) {
						log.error("发布主题失败！", e);
					} finally {
						close(jedis);
					}
				}
			}) {
			}.start();
		} catch (Exception e) {
		}
	}

	// 订阅Redis主题
	public static boolean subscribeTopic(final String topic, final JedisPubSub listener) {
		if(!SUBSCRIBE_FD_REDIS){
			return true;
		}
		// Jedis订阅是阻塞式的，必须启动线程进行订阅
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						Jedis jedis = null;
						try {
							jedis = jedisPool.getResource();
							jedis.subscribe(listener, topic);
							log.info("Redis主题订阅成功，topic=" + topic);
							break;
						} catch (Exception e) {
							log.error("Redis主题订阅失败，topic=" + topic, e);
							try {
								// 休眠1秒，再次订阅
								try {
									Thread.sleep(1000);
								} catch (Exception e2) {
								}
							} catch (Exception e2) {
							}
						} finally {
							close(jedis);
						}
					}
				}
			}).start();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean hmset(String key, Map<String, String> map) {
		if(!SUBSCRIBE_FD_REDIS){
			return true;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hmset(key, map);
		} catch (Exception e) {
			log.error("向Redis缓存中放入数据失败！", e);
		} finally {
			close(jedis);
		}
		return true;
	}

	public static Map<String, String> hgetAll(String key) {
		if(!SUBSCRIBE_FD_REDIS){
			return null;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Map<String, String> map = jedis.hgetAll(key);
			return map;
		} catch (Exception e) {
			log.error("获取Redis缓存中数据失败！", e);
		} finally {
			close(jedis);
		}
		return null;
	}

	// timeout：秒
	public static boolean setKeyValueTimeout(String key, String value, int timeout) {
		if(!SUBSCRIBE_FD_REDIS){
			return false;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			// NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
			String returnStr = jedis.set(key, value, "NX", "EX", timeout);
			if ("OK".equals(returnStr)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("set " + key + " faild", e);
		} finally {
			close(jedis);
		}
		return false;
	}

	public static String getKeyValue(String key) {
		if(!SUBSCRIBE_FD_REDIS){
			return null;
		}
		String result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.get(key);
		} catch (Exception e) {
			log.error("get cache from redis faild! ", e);
		} finally {
			close(jedis);
		}
		return result;
	}

	/**
	 * 从Redis获取所有飞行数据
	 * IMEI,//IMEI编码 SN,//飞机编号 MCU,//MCU时间 LON,//经度 LAT,//纬度 ALT,//高度 SPE,//速度
	 * DIR,//航向 TOL,//起飞：3 降落：4 PLANID,//计划ID LVL,//飞行等级, TYPE,//飞机类型
	 * OWNTYPE,//所有者类型 OWNNAME,//所有者名称 PHONE,//联系电话
	 * WRN,//告警信息NN|NP|LC|VC|AS-[AirspceID]
	 * 未登记飞机飞行NN,无计划飞行NP,计划飞行偏航LC,侵入禁飞区域AS-[AID],超出飞行高度VC LTIME//服务器时间
	 */
	public static Map<String, Map<String, String>> getAllUavMsgs() throws Exception {
		Map<String, Map<String, String>> allUavMsgs = new HashMap<String, Map<String, String>>();
		if(!SUBSCRIBE_FD_REDIS){
			return allUavMsgs;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> keys = jedis.keys(REDIS_FD_PREFIX + "*");
			Map<String, String> uavMsgMap = null;
			String pnValue = null;
			for (String key : keys) {
				uavMsgMap = jedis.hgetAll(key);
				pnValue = uavMsgMap.get("SN");
				if (StringUtils.isNotBlank(pnValue))
					allUavMsgs.put(pnValue, uavMsgMap);
			}
		} catch (Exception e) {
			log.error("从Redis获取所有飞行数据失败！", e);
			throw e;
		} finally {
			close(jedis);
		}
		return allUavMsgs;
	}

	/**
	 * 获取航空器实时数据
	 */
	public static List<String> getUavMsg(String uavSn, String[] fields) {
		if(!SUBSCRIBE_FD_REDIS){
			return null;
		}
		String key = null;
		Jedis jedis = null;
		try {
			key = REDIS_FD_PREFIX + uavSn;
			jedis = jedisPool.getResource();
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			log.error("get uavmsg from redis faild! ", e);
		} finally {
			close(jedis);
		}
		return null;
	}
	/**
	 * 获取adsb实时数据
	 */
	public static List<String> getAdsbMsg(String uavSn, String[] fields) {
		if(!SUBSCRIBE_FD_REDIS){
			return null;
		}
		String key = null;
		Jedis jedis = null;
		try {
			key = REDIS_FD_PREFIX_ADSB + uavSn;
			jedis = jedisPool.getResource();
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			log.error("get uavmsg from redis faild! ", e);
		} finally {
			close(jedis);
		}
		return null;
	}
	
	/**
	 * 获取防反设备缓存数据
	 */
	public static String getDevMsg(String devId, String field) {
		String key = null;
		Jedis jedis = null;
		try {
			key = REDIS_DEV_PREFIX + devId;
			jedis = jedisPool.getResource();
			return jedis.hget(key, field);
		} catch (Exception e) {
			log.error("get devmsg from redis faild! ", e);
		} finally {
			close(jedis);
		}
		return null;
	}
	
	/**
	 * 删除无人机缓存数据
	 */
	public static long delUavMsg(String sn) {
		String key = null;
		Jedis jedis = null;
		try {
			key = REDIS_FD_PREFIX + sn;
			jedis = jedisPool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			log.error("delete uavmsg from redis faild! ", e);
		} finally {
			close(jedis);
		}
		return 0;
	}
	/**
	 * 删除有人机缓存数据
	 */
	public static long delAdsbMsg(String sn) {
		String key = null;
		Jedis jedis = null;
		try {
			key = REDIS_FD_PREFIX_ADSB + sn;
			jedis = jedisPool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			log.error("delete adsbmsg from redis faild! ", e);
		} finally {
			close(jedis);
		}
		return 0;
	}
	
	/**
	 * 删除防反设备缓存数据
	 */
	public static long delDevMsg(String devId) {
		String key = null;
		Jedis jedis = null;
		try {
			key = REDIS_DEV_PREFIX + devId;
			jedis = jedisPool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			log.error("delete devmsg from redis faild! ", e);
		} finally {
			close(jedis);
		}
		return 0;
	}
	
	public static void close(Jedis jedis) {
		try {
			if (jedis != null)
				jedis.close();
		} catch (Exception e2) {
			log.error("关闭Redis出现异常！", e2);
		}
	}

	public static void main(String[] args) {
//		for (int i = 0; i < 10; i++) {
//			String time = DateUtil.formatDate(new Date(), "yyyyMMddHHmmssSSS");
//			Random random = new Random();
//			Integer r = random.nextInt(100);
//			double cha = r / 25000.00;
//			double lng = 109.76800497299206 - cha;
//			double lat = 19.714122339748457 - cha;
//			double hei = 146.3 - r;
//			String s = "test01," + time + "," + lng + "," + lat + "," + hei + ",1,1,275.2,3,03,";
//			System.out.println(s);
//			lpush(REDIS_QUEUE_MD, s);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		lpush(REDIS_QUEUE_MD, "1231");
//		lpush(REDIS_QUEUE_MD, "1232");
//		lpush(REDIS_QUEUE_MD, "1233");
//		brpop(REDIS_QUEUE_MD);

//		if(setKeyValueTimeout("fly.123.20210309170816", "0", 10)){
//			System.out.println("value==" + getKeyValue("fly.123.20210309170816"));
//		}
		
		//无人机编码,信号时间,经度,纬度,高度,高度类别（1真高、2海高）,速度,航向,起飞降落状态,数据来源,设备标识,厂商架次ID，型号信息
		lpush(REDIS_QUEUE_MD, "8020007462,20210317142053868,116.1064946,40.0747992,15.3,1,4.8,270.6,3,12,,,");
		lpush(REDIS_QUEUE_MD, "8020007463,20210317142053867,116.1061726,40.0704152,15.3,1,4.8,270.6,3,12,,,");
		System.out.println("success");
		
//		String[] srcLocs=new String[]{
//				"116.3665211,40.0795507",
//				"116.3665961,40.0793208",
//				"116.3675711,40.0793771",
//				"116.3676997,40.0793195",
//				"116.3682997,40.0793927",
//				"116.3692962,40.0794409",
//				"116.3693177,40.0790961",
//				"116.3694998,40.0789071",
//				"116.3699071,40.0787261",
//				"116.3704803,40.0786557",
//				"116.3710269,40.0786141",
//				"116.371568,40.0785972",
//				"116.3720824,40.0785638"
//		};
//		String[] tarLocs=new String[]{
//				"116.3662693,40.0794484",
//				"116.366564,40.0793085",
//				"116.3672336,40.0792339",
//				"116.3679622,40.0793972",
//				"116.3688033,40.0793552",
//				"116.3692908,40.0793096",
//				"116.3694784,40.0789318",
//				"116.3697142,40.0788166",
//				"116.3700303,40.0787342",
//				"116.3705125,40.0786639",
//				"116.3709411,40.0786347",
//				"116.3714341,40.0786055",
//				"116.3720717,40.0785597"
//		};
//		String mdStr=null;
//		for (int i = 0; i < srcLocs.length; i++) {
//			mdStr="8020007462,"+DateUtil.formatDate(new java.util.Date(), DateUtil.PATTERN_TIME_WITH_MILSEC_COMPRESS)+","+srcLocs[i]+",15.3,1,4.8,270.6,3,12,,,";
//			lpush(REDIS_QUEUE_MD, mdStr);
//			
//			try {
//				Thread.sleep(100);
//			} catch (Exception e) { }
//			
//			mdStr="TJ01,"+DateUtil.formatDate(new java.util.Date(), DateUtil.PATTERN_TIME_WITH_MILSEC_COMPRESS)+","+tarLocs[i]+",15.3,1,4.8,270.6,3,21,,,";
//			lpush(REDIS_QUEUE_MD, mdStr);
//			
//			try {
//				Thread.sleep(1000);
//			} catch (Exception e) { }
//		}
//		System.out.println("success");
	}
}