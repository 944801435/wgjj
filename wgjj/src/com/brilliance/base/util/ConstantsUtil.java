package com.brilliance.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.brilliance.web.view.system.sysOptLog.SysOptLogDao;

/**
 * 读取数据库常量
 * @author qiaocy
 *
 */
@SuppressWarnings("unchecked")
public class ConstantsUtil {

	// 系统参数
	public static Map<String, String> sysConfigMap = new LinkedHashMap<String, String>();
	// SysApp的Map
	public static Map<String, String> sysAppMap = new LinkedHashMap<String, String>();
	// 节假日
	public static List<String> holiday = new ArrayList<String>();

	static {
		Map<String, String> configFile = PropertiesUtil.getPropertyMap(PropertiesUtil.DEFAULT_PROP_FILE);
		for (Map.Entry<String, String> entry : configFile.entrySet()) {
			sysConfigMap.put(entry.getKey(), entry.getValue());
		}
	}

	public static void readData() {
		SysOptLogDao sysOptLogDao = (SysOptLogDao) SpringContextUtil.getBean("sysOptLogDao");
		Map<String, String> redisMap = new HashMap<String, String>();
		// 读取系统参数
		List<Object[]> list = sysOptLogDao.findList("select o.cfgId,o.cfgValue from SysConfig o ");
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = list.get(i);
			sysConfigMap.put((String) objs[0], (String) objs[1]);
			redisMap.put((String) objs[0], (String) objs[1]);
		}
		boolean setFlag = RedisClientUtil.hmset(RedisClientUtil.REDIS_CACHE_SYS_CONF, redisMap);
		if (setFlag) {
			// 发送SysConfig变更通知
			RedisClientUtil.publish(RedisClientUtil.REDIS_TOPIC_CONF, "conf");
		}
	}

}
