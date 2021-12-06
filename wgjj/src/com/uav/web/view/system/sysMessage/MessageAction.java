package com.uav.web.view.system.sysMessage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uav.base.common.BaseAction;
import com.uav.base.common.Constants;
import com.uav.base.common.MessageVo;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.SysMessage;

/**
 * 消息管理
 * @ClassName: MessageAction
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午1:35:59
 */
@Controller
@LoginAccess
@SystemWebLog(menuName="消息管理")
public class MessageAction extends BaseAction{
	
	@Autowired
	private MessageService messageService;
	
	/**
	 * 
	 * 描述 获取当前用户未读的消息记录
	 * @Title: messagelist 
	 * @author 
	 * @Modified By 钟志峰
	 * @param request
	 * @return    
	 * Object 返回类型 
	 * @throws
	 */
	@RequestMapping("/messagelist")
	@ResponseBody
	public Object messagelist(HttpServletRequest request){
		SysMessage obj=new SysMessage();
		obj.setRcvId(this.getCurUserid(request));
		obj.setRcvType(Constants.user_type_web);
		List<SysMessage> messagelist=messageService.findList(obj);
		return messagelist;
	}
	
	/**
	 * 
	 * 描述 设置当前用户详细已读
	 * @Title: messageClose 
	 * @author 
	 * @Modified By 钟志峰
	 * @param request
	 * @return    
	 * Object 返回类型 
	 * @throws
	 */
	@RequestMapping("/messageClose")
	@ResponseBody
	@SystemWebLog(methodName="设置当前用户详细已读")
	public Object messageClose(HttpServletRequest request){
		String msgSeq=request.getParameter("msgSeq");
		try {
			messageService.closeMessage(msgSeq);
			return new MessageVo(MessageVo.SUCCESS,"",null);
		} catch (NumberFormatException e) {
			return new MessageVo(MessageVo.FAIL,"",null);
		}
	}

}
