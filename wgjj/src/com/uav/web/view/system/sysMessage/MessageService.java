package com.uav.web.view.system.sysMessage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uav.base.common.Constants;
import com.uav.base.model.SysMessage;
import com.uav.base.util.DateUtil;

/**
 * 
 * @ClassName: MessageService
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午1:36:51
 */
@Service
@Transactional(rollbackFor={RuntimeException.class,Exception.class})
public class MessageService {
	@Autowired
	private MessageDao messageDao;
	
	/**
	 * 
	 * 描述 查询所有消息
	 * @Title: findList 
	 * @author 
	 * @Modified By 钟志峰
	 * @param obj 查询参数
	 * @return    
	 * List<SysMessage> 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<SysMessage> findList(SysMessage obj){
		String hql="from SysMessage where rcvType=? and rcvId=? and readSts=? and expTime>=? order by msgTime desc";
		return messageDao.findList(hql, new Object[]{obj.getRcvType(),obj.getRcvId(),Constants.sys_default_no,DateUtil.getNowFullTimeString()});
	}
	
	/**
	 * 
	 * 描述 关闭消息
	 * @Title: closeMessage 
	 * @author 
	 * @Modified By 钟志峰
	 * @Modified Date:  2018年8月27日 下午1:37:23                                    
	 * @param msgSeq     消息ID
	 * void 返回类型 
	 * @throws
	 */
	public void closeMessage(String msgSeq){
		messageDao.executeHql("update SysMessage set readSts=? where msgSeq=?", new Object[]{Constants.sys_default_yes,msgSeq});
	}

}
