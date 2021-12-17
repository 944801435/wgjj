package com.brilliance.web.view.uavOnline.base.adsb;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.brilliance.base.common.Constants;
import com.brilliance.base.util.DateUtil;
import com.brilliance.base.util.PropertiesUtil;
import com.brilliance.web.view.uavOnline.base.UavOnlineVo;
import com.brilliance.web.view.uavOnline.base.UavOnlineWebSocketServer;

import net.sf.json.JSONObject;

public class AdsbOnlineUtil {
	private static final Logger log = Logger.getLogger(AdsbOnlineUtil.class);

	public static final long DFT_SYS_CONF_UAV_ONLINE_TIMEOUT = 60;// 秒
	private static final String STR_DOT = ",";

	// 显控推送等待时长
	public static final String UAV_ONLINE_SLEEP_TIME = PropertiesUtil.getPropertyValue(PropertiesUtil.DEFAULT_PROP_FILE, "UAV_ONLINE_SLEEP_TIME",
			"2");// 单位秒

	private static ConcurrentHashMap<String, UavOnlineVo> changeCache = new ConcurrentHashMap<String, UavOnlineVo>();// 无人机以及告警变化信息缓存

	public static void pushData(String message) {
		try {
			// PN,LON,LAT,ALT,SPE,DIR,TI,MUC,TIME       //MUC：信号时间  TIME：FDP接收时间
			// 780969,110.524864,20.766311,611.124,0.0,0.0,cbj5747,2018-11-07 11:22:09.483,2018-11-07 11:22:09
			if (StringUtils.isBlank(message))
				return;

			int loc = 0;
			String str = message;
			List<String> fldList = new LinkedList<String>();
			while ((loc = str.indexOf(STR_DOT)) >= 0) {
				fldList.add(str.substring(0, loc));
				str = str.substring(loc + 1);
			}
			fldList.add(str);
			String[] fields = new String[fldList.size()];
			for (int i = 0; i < fldList.size(); i++)
				fields[i] = fldList.get(i);
			if (fields.length < 8) {
				log.error("接收到的ADSB数据格式错误，message=" + message);
				return;
			}

			String pn = fields[0];// 飞机编号
			if (pn.length() == 0) {
				log.error("接收到的ADSB数据格式错误，PN不能为空，message=" + message);
				return;
			}
			String lng = fields[1];// 经度
			if (lng.length() == 0) {
				log.error("接收到的ADSB数据格式错误，LNG不能为空，message=" + message);
				return;
			}
			String lat = fields[2];// 纬度
			if (lat.length() == 0) {
				log.error("接收到的ADSB数据格式错误，LAT不能为空，message=" + message);
				return;
			}
			String alt = fields[3];// 高度
			String spe = fields[4];// 速度
			String dir = fields[5];// 航向
			String ti = fields[6];// 航班号
			String mcu = fields[7];// 信号时间
			String time = fields[8];// FDP接收时间

			
			long rcvTimeSecond = DateUtil.parseDate(time, "yyyy-MM-dd HH:mm:ss").getTime() / 1000;

			UavOnlineVo uavVo = new UavOnlineVo(pn, null, lng, lat, alt, alt, spe, dir, Constants.UAV_FLY_UP, null, null, null, null, ti, null, null,
					mcu, time);
			uavVo.setTimeSecond(rcvTimeSecond);
			pushChanged(uavVo);
		} catch (Exception e) {
			log.error("ADSB数据处理失败，message=" + message, e);
		}
	}

	// 向变更缓存压入变更
	synchronized public static void pushChanged(UavOnlineVo uavVo) {
		changeCache.put(uavVo.getPn(), uavVo);
	}

	// 从变更缓存提取变更
	synchronized public static Map<String, UavOnlineVo> getChangeCache() {
		Map<String, UavOnlineVo> popUavChg = changeCache;
		changeCache = new ConcurrentHashMap<String, UavOnlineVo>();
		return popUavChg;
	}

	// 启动变更消息发送进程
	public static void startSenderDeamon() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(Integer.parseInt(UAV_ONLINE_SLEEP_TIME) * 1000);
					} catch (Exception e) {
					}

					try {
						Map<String, UavOnlineVo> changeMap = AdsbOnlineUtil.getChangeCache();
						if (changeMap.size() > 0) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("adsb", changeMap.values());
							log.debug("ADSB监视数据推送消息：" + jsonObject.toString());
							UavOnlineWebSocketServer.sendMessage(jsonObject.toString());
						}
					} catch (Exception e) {
						log.error("ADSB监视数据推送处理失败！", e);
					}
				}
			}
		}).start();

		log.info("启动ADSB数据推送进程成功！");
	}
}