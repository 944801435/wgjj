package com.uav.base.common;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

/**
 * 常量
 */
@SuppressWarnings({ "unchecked", "static-access" })
public class Constants {
	public static String SYS_NAME = readValue("SYS_NAME");// 系统名称
	public static String SYS_NAME_EN = readValue("SYS_NAME_EN");// 系统名称英文
	public static String SYS_COPYRIGHT = readValue("SYS_COPYRIGHT");// 版权信息
	public static String SYS_BEIAN = readValue("SYS_BEIAN");// 备案信息
	public static boolean SYS_SHOW_LOGO = true;// 是否显示LOGO
	static {
		String sysShowLogoStr = readValue("SYS_SHOW_LOGO");
		if (sysShowLogoStr != null && sysShowLogoStr.equals("0"))
			SYS_SHOW_LOGO = false;
	}

	public static final String STR_BLANK = "";
	public static final String STR_ZERO = "0";
	public static final String CONFIG_FILE = "config.properties";

	public static final String CURRUSER = "userbean"; // 当前存储在session中的当前用户
	public static final String SYS_FILE_DB = "d";

	public static final String SYS_DEPT_ID = "0000000000"; // 系统机构ID
	public static final String SYS_USER_ID = "0000000000"; // 系统用户ID（admin）

	// 用户类型
	public static final String user_type_web = "1";
	public static final String user_type_app = "2";

	// 系统缺省标志
	public static final String sys_default_yes = "1"; // 1 是 系统缺省
	public static final String sys_default_no = "0"; // 0 否
	public static final String sys_default_yes_desc = "是"; // 是
	public static final String sys_default_no_desc = "否"; // 否
	public static Map<String, String> sys_defaultMap = new LinkedHashMap<String, String>();
	static {
		sys_defaultMap.put(sys_default_yes, sys_default_yes_desc);
		sys_defaultMap.put(sys_default_no, sys_default_no_desc);
	}

	// 系统配置参数
	public static final String SYS_CONF_MSG_EXP_DAYS = "SYS_CONF_MSG_EXP_DAYS"; // 系统消息过期周期（天）
	public static final String SYS_CONF_UAV_ONLINE_TIMEOUT = "SYS_CONF_UAV_ONLINE_TIMEOUT";// 航空器多久未更新数据就认为其降落
	public static final String SYS_CONF_XIAOPI_LOCK_TIMEOUT = "SYS_CONF_XIAOPI_LOCK_TIMEOUT";// 消批处理抢占锁有效时间（单位：秒）

	// 系统后台任务
	public static final String DEAMON_TASK_STS_NO = "0";// 未处理
	public static final String DEAMON_TASK_STS_IN = "1";// 处理中
	public static final String DEAMON_TASK_STS_SUCC = "2";// 处理成功
	public static final String DEAMON_TASK_STS_FAIL = "3";// 处理失败

	// 有效状态
	public static final String VALID_STS_0 = "0";
	public static final String VALID_STS_1 = "1";
	public static final String VALID_STS_0_DESC = "无效";
	public static final String VALID_STS_1_DESC = "有效";

	public static String getValidSts(String type) {
		if (type == null)
			return STR_BLANK;
		else
			type = type.trim();

		if (type.equals(VALID_STS_0))
			return VALID_STS_0_DESC;
		else if (type.equals(VALID_STS_1))
			return VALID_STS_1_DESC;
		else
			return STR_BLANK;
	}

	// 操作状态
	public static final String OPT_STS_FAIL = "0";
	public static final String OPT_STS_SUCC = "1";
	public static final String OPT_STS_FAIL_DESC = "失败";
	public static final String OPT_STS_SUCC_DESC = "成功";

	public static String getOptSts(String type) {
		if (type == null)
			return STR_BLANK;
		else
			type = type.trim();

		if (type.equals(OPT_STS_FAIL))
			return OPT_STS_FAIL_DESC;
		else if (type.equals(OPT_STS_SUCC))
			return OPT_STS_SUCC_DESC;
		else
			return STR_BLANK;
	}

	// 计划申请状态
	public static final String BIZ_SPACE_REQ_CREATE = "0";
	public static final String BIZ_SPACE_REQ_AUDIT = "1";
	public static final String BIZ_SPACE_REQ_APPROVE = "2";
	public static final String BIZ_SPACE_REQ_REJECT = "3";
	public static final String BIZ_SPACE_REQ_CREATE_DESC = "未提交";
	public static final String BIZ_SPACE_REQ_AUDIT_DESC = "审核中";
	public static final String BIZ_SPACE_REQ_APPROVE_DESC = "批准";
	public static final String BIZ_SPACE_REQ_REJECT_DESC = "驳回";
	public static Map<String, String> biz_space_req_map = new LinkedHashMap<String, String>();
	static {
		biz_space_req_map.put(BIZ_SPACE_REQ_CREATE, BIZ_SPACE_REQ_CREATE_DESC);
		biz_space_req_map.put(BIZ_SPACE_REQ_AUDIT, BIZ_SPACE_REQ_AUDIT_DESC);
		biz_space_req_map.put(BIZ_SPACE_REQ_APPROVE, BIZ_SPACE_REQ_APPROVE_DESC);
		biz_space_req_map.put(BIZ_SPACE_REQ_REJECT, BIZ_SPACE_REQ_REJECT_DESC);
	}

	// 启用状态
	public static final String USE_STS_ON = "1";
	public static final String USE_STS_OFF = "0";
	public static final String USE_STS_DEL = "2";
	public static final String USE_STS_DEL_DESC = "注销";
	public static final String USE_STS_ON_DESC = "启用";
	public static final String USE_STS_OFF_DESC = "禁用";
	public static Map<String, String> use_sts_map = new LinkedHashMap<String, String>();
	static {
		use_sts_map.put(USE_STS_ON, USE_STS_ON_DESC);
		use_sts_map.put(USE_STS_OFF, USE_STS_OFF_DESC);
	}

	public static String getUseSts(String type) {
		if (type == null)
			return STR_BLANK;
		else
			type = type.trim();

		if (type.equals(USE_STS_ON))
			return USE_STS_ON_DESC;
		else if (type.equals(USE_STS_OFF))
			return USE_STS_OFF_DESC;
		else
			return STR_BLANK;
	}

	// 空域形状
	// 0网格、1多边形、2圆形、3扇形、4线缓冲区、5机场障碍物限制面
	public static final String space_shape_wg = "0";
	public static final String space_shape_dbx = "1";
	public static final String space_shape_yx = "2";
	public static final String space_shape_sx = "3";
	public static final String space_shape_xhcq = "4";
	public static final String space_shape_jczaw = "5";
	public static final String space_shape_wg_desc = "网格";
	public static final String space_shape_dbx_desc = "多边形";
	public static final String space_shape_yx_desc = "圆形";
	public static final String space_shape_sx_desc = "扇形";
	public static final String space_shape_xhcq_desc = "线缓冲区";
	public static final String space_shape_jczaw_desc = "机场障碍物限制面";
	public static Map<String, String> space_shape_map = new LinkedHashMap<String, String>();
	static {
		space_shape_map.put(space_shape_wg, space_shape_wg_desc);
		space_shape_map.put(space_shape_dbx, space_shape_dbx_desc);
		space_shape_map.put(space_shape_yx, space_shape_yx_desc);
		space_shape_map.put(space_shape_sx, space_shape_sx_desc);
		space_shape_map.put(space_shape_xhcq, space_shape_xhcq_desc);
		space_shape_map.put(space_shape_jczaw, space_shape_jczaw_desc);
	}
	public static Map<String, String> run_space_shape_map = new LinkedHashMap<String, String>();
	static {
		run_space_shape_map.put(space_shape_dbx, space_shape_dbx_desc);
		run_space_shape_map.put(space_shape_yx, space_shape_yx_desc);
		run_space_shape_map.put(space_shape_sx, space_shape_sx_desc);
		run_space_shape_map.put(space_shape_xhcq, space_shape_xhcq_desc);
	}

	public static String getSpaceShape(String type) {
		if (type == null)
			return STR_BLANK;
		else
			type = type.trim();

		if (type.equals(space_shape_wg))
			return space_shape_wg_desc;
		else if (type.equals(space_shape_dbx))
			return space_shape_dbx_desc;
		else if (type.equals(space_shape_yx))
			return space_shape_yx_desc;
		else if (type.equals(space_shape_sx))
			return space_shape_sx_desc;
		else if (type.equals(space_shape_xhcq))
			return space_shape_xhcq_desc;
		else if (type.equals(space_shape_jczaw))
			return space_shape_jczaw_desc;
		else
			return STR_BLANK;
	}

	// 无人机飞行信息
	public static final String UAV_FLY_UP = "3";
	public static final String UAV_FLY_DOWN = "4";
	public static final String UAV_FLY_UP_DESC = "起飞";
	public static final String UAV_FLY_DOWN_DESC = "降落";

	public static String getUavFlyMsg(String type) {
		if (type == null)
			return STR_BLANK;
		else
			type = type.trim();

		if (type.equals(UAV_FLY_UP))
			return UAV_FLY_UP_DESC;
		else if (type.equals(UAV_FLY_DOWN))
			return UAV_FLY_DOWN_DESC;
		else
			return STR_BLANK;
	}

	// 部门类别X系统，D大队，Z作战局
	public static final String DEPT_TYPE_DD = "D";// 大队
	public static final String DEPT_TYPE_ZZJ = "Z";// 作战局
	public static final String DEPT_TYPE_DD_DESC = "大队";// 大队
	public static final String DEPT_TYPE_ZZJ_DESC = "作战局";// 作战局
	public static Map<String, String> dept_type_map = new LinkedHashMap<String, String>();
	static {
		dept_type_map.put(DEPT_TYPE_DD, DEPT_TYPE_DD_DESC);
		dept_type_map.put(DEPT_TYPE_ZZJ, DEPT_TYPE_ZZJ_DESC);
	}

	// 生成WORD文档航线字符串类型
	public static final String WORD_HX_TYPE1 = "1";// 普通文本
	public static final String WORD_HX_TYPE2 = "2";// 加下划线
	public static final String WORD_HX_TYPE3 = "3";// 输出"↑"符号
	public static final String WORD_HX_TYPE4 = "4";// 输出"↓"符号

	// 审批数据包类别
	public static final String APP_DATA_TYPE_WJB = "1";// 外交部
	public static final String APP_DATA_TYPE_MH = "2";// 民航
	public static final String APP_DATA_TYPE_JN = "3";// 军内
	public static final String APP_DATA_TYPE_WJB_DESC = "外交部";// 外交部
	public static final String APP_DATA_TYPE_MH_DESC = "民航";// 民航
	public static final String APP_DATA_TYPE_JN_DESC = "军内";// 军内

	public static boolean hasPms(List<String> pmsIds, String pmsId) {
		if (pmsIds == null) {
			return false;
		}
		if (pmsIds.contains(pmsId)) {
			return true;
		}
		return false;
	}

	/**
	 * 读取common.properties文件
	 * 
	 * @param key
	 * @return
	 */
	public static String readValue(String key) {
		Properties props = new Properties();
		InputStream in = null;
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource(CONFIG_FILE);// 获得配置文件地址
			in = url.openStream();// 获得配置文件流
			props.load(in);
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
	}

	// 获取本机IP
	public static String getLocalhostIP() {
		InetAddress ia = null;
		try {
			ia = ia.getLocalHost();
			String localip = ia.getHostAddress();
			return localip;
		} catch (Exception e) {
			e.printStackTrace();
			return "127.0.0.1";
		}
	}

	public static String findKey(String key) {
		if (StringUtils.isBlank(key)) {
			return "";
		}
		key = key.trim();
		try {
			return (String) Constants.class.getField(key).get(null);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (NoSuchFieldException e3) {
			e3.printStackTrace();
		} catch (SecurityException e4) {
			e4.printStackTrace();
		}
		return "";
	}

	public static Map<String, String> findMap(String mapName) {
		try {
			return (Map<String, String>) Constants.class.getField(mapName).get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, String>();
	}

	public static String findJSONMap(String mapName) {
		try {
			Map<String, String> map = (Map<String, String>) Constants.class.getField(mapName).get(null);
			return JSONObject.toJSONString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONObject().toString();
	}

	public static String findValue(String mapName, String key) {
		try {
			Map<String, String> map = (Map<String, String>) Constants.class.getField(mapName).get(null);
			if (map != null) {
				return map.get(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getMapJson(String mapName) {
		try {
			Map<String, Object> map = (Map<String, Object>) Constants.class.getField(mapName).get(null);
			return JSONObject.toJSONString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
