package com.uav.base.util;

import java.util.Date;
import java.util.List;

import com.uav.base.common.BaseDAO;
import com.uav.base.common.Constants;
import com.uav.base.model.SysMessage;

public class MessageUtil {
	private static final int DFT_SYS_MSG_EXP_DAYS=3;//系统消息过期周期（天），缺省值
	
	/**
	 * 
	 * 描述
	 * @Title: createMessage 
	 * @author 钟志峰
	 * @param @param dao 数据库操作对象
	 * @param @param rcvType 用户类型
	 * @param @param rcvId  用户标识
	 * @param @param msgText 消息文本
	 * @param @param modelId 模块数据的标识
	 * @param @param menuUrl 数据库存储的URL全路径。如：/spaceManageList.action?spcRevSts=0&userType=0
	 * @param @throws Exception    
	 * @return void 返回类型 
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static void createMessage(BaseDAO dao, String rcvType, String[] rcvId, String msgText, String modelId, String menuUrl) throws Exception {
		// 获取系统配置参数“系统消息过期周期（天）”
		int msgExpDays = 0;
		String confStr = ConstantsUtil.sysConfigMap.get(Constants.SYS_CONF_MSG_EXP_DAYS);
		if (confStr != null && confStr.trim().length() > 0) {
			try {
				int val = Integer.parseInt(confStr.trim());
				if (val <= 0)
					msgExpDays = DFT_SYS_MSG_EXP_DAYS;
				else
					msgExpDays = val;
			} catch (Exception e) {
				msgExpDays = DFT_SYS_MSG_EXP_DAYS;
			}
		} else {
			msgExpDays = DFT_SYS_MSG_EXP_DAYS;
		}

		// 计算当前时间和过期时间
		Date nowTime = DateUtil.getNowTime();
		Date expTime = DateUtil.getNextDays(nowTime, msgExpDays);
		String nowTimeStr = DateUtil.parseFullTimeToString(nowTime);
		String expTimeStr = DateUtil.parseFullTimeToString(expTime);

		SysMessage msg = null;
		String isNotify = null;
		if (Constants.user_type_app.equals(rcvType))
			isNotify = "1";
		else
			isNotify = "0";
		for (int i = 0; i < rcvId.length; i++) {
			String hql = "select msg from SysMessage msg where msg.rcvType = ? "
					+ " and  msg.rcvId = ? and msg.menuUrl = ? and msg.modelId = ? and msg.readSts = ? and msg.expTime >= ? ";
			List list = dao.findList(hql,
					new Object[] { rcvType, rcvId[i], menuUrl, modelId, Constants.sys_default_no, DateUtil.getNowFullTimeString() });
			if (list == null || list.size() == 0) {
				msg = new SysMessage();
				msg.setRcvType(rcvType);
				msg.setRcvId(rcvId[i]);
				msg.setMsgTime(nowTimeStr);
				msg.setExpTime(expTimeStr);
				msg.setIsNotify(isNotify);
				msg.setNotifySts("0");
				msg.setMsgText(msgText);
				msg.setReadSts("0");
				msg.setMenuUrl(menuUrl);
				msg.setModelId(modelId);
				dao.save(msg);
			}
		}
	}
}
