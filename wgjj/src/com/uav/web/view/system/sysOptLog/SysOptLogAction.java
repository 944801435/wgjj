package com.uav.web.view.system.sysOptLog;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uav.base.common.BaseAction;
import com.uav.base.common.Constants;
import com.uav.base.common.PagerVO;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.SysOptLog;
import com.uav.base.model.SysUser;

/**
 * 系统操作日志
 * @ClassName: SysOptLogAction
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午2:06:19
 */
@Controller
@LoginAccess
public class SysOptLogAction extends BaseAction{
	protected static final Logger log=Logger.getLogger(SysOptLogAction.class);
	
	@Autowired
	private SysOptLogService sysOptLogService;

	
	/**
	 * 
	 * 描述 获取系统操作日志列表
	 * @Title: findList 
	 * @author 
	 * @Modified By 钟志峰
	 * @Modified Date:  2018年8月27日 下午2:05:28                                    
	 * @Why & What is modified  <修改分页，减少代码冗余>    
	 * @param curPage 当前页
	 * @param sysOptLog 查询参数，实体
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/sysoptlog_list.action")
	public String findList(Integer curPage,SysOptLog sysOptLog,HttpServletRequest request,Model model){
		if(curPage==null){
			curPage=1;
		}
		try {
		String curDeptId =	this.getCurDeptid(request);
		PagerVO pv = sysOptLogService.findList(sysOptLog,curDeptId,curPage,pageSize);
		for(Object temp : pv.getItems()){
			if (temp instanceof SysOptLog) {
				SysOptLog obj = (SysOptLog) temp;
				SysUser sysUser = sysOptLogService.findUserById(obj.getUserId());
				obj.setUserName(sysUser.getUserName());
				obj.setOpState(Constants.getOptSts(obj.getOpState()));
			}
		}
		model.addAttribute("sysoptloglist", pv.getItems());
		model.addAttribute("sysoptlog", sysOptLog);
		model.addAttribute("totalCount", pv.getCounts());
		model.addAttribute("curPage", curPage);
		model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取系统操作日志列表失败！", e);
			this.setMessage(request, "获取系统操作日志列表失败！", "red");
		}
		return "/view/system/log/sysoptlog";
	}

}
