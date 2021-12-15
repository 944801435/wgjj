package com.uav.web.view.system.sysConfig;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uav.base.common.BaseAction;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.SysConfig;
import com.uav.base.util.ConstantsUtil;

/**
 * 系统配置参数
 * @ClassName: SysConfigQueryAction
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午1:55:00
 */
@Controller
@LoginAccess
@SystemWebLog(menuName="系统配置参数")
@SuppressWarnings("unchecked")
public class SysConfigQueryAction extends BaseAction{
	protected static final Logger log=Logger.getLogger(SysConfigQueryAction.class);
	
	@Autowired
	private SysConfigQueryService sysConfigQueryService;
	
	/**
	 * 
	 * 描述 第一次访问，防止页面参数丢失
	 * @Title: main 
	 * @author 
	 * @Modified By 钟志峰
	 * @param request
	 * @param model
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/sysConfigList_main")
	public String main(HttpServletRequest request,Model model){
		Map<String,Object> queryMap=(Map<String,Object>)request.getSession().getAttribute("query_sysConfig");
		if(queryMap==null){
			queryMap=new HashMap<String,Object>(16);
		}
		queryMap.put("curPage", 1);
		request.getSession().setAttribute("query_sysConfig", queryMap);
		return findList(1,request,model);
	}
	
	/**
	 * 
	 * 描述 列表查询
	 * @Title: findList 
	 * @author 
	 * @Modified By 钟志峰
	 * @Modified Date:  2018年8月27日 下午1:55:34                                    
	 * @Why & What is modified  <修改分页，减少代码冗余>      
	 * @param curPage
	 * @param request
	 * @param model
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/sysConfigList.action")
	public String findList(Integer curPage, HttpServletRequest request, Model model){
		Map<String,Object> queryMap=(Map<String,Object>)request.getSession().getAttribute("query_sysConfig");
		if(curPage==null){
			curPage=Integer.parseInt(queryMap.get("curPage")+"");
		}
		queryMap.put("curPage", curPage);
		request.getSession().setAttribute("query_sysConfig", queryMap);
		try {
			PagerVO pv = sysConfigQueryService.findList(curPage, pageSize);
			model.addAttribute("sysconfiglist", pv.getItems());
			model.addAttribute("totalCount", pv.getCounts());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取系统配置参数列表失败！", e);
			this.setMessage(request, "获取系统配置参数列表失败！", "red");
		}
		return "/view/system/sysconfig/sysconfig_list";
	}
	
	/**
	 * 跳转修改
	 * 描述
	 * @Title: toEdit 
	 * @author 
	 * @Modified By 钟志峰
	 * @param cfgId
	 * @param model
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/sysConfigToEdit")
	public String toEdit(String cfgId, Model model, HttpServletRequest request){
		SysConfig sysConfig = new SysConfig();
		if(!StringUtils.isBlank(cfgId)){
			sysConfig=sysConfigQueryService.findById(cfgId);
		}
		model.addAttribute("sysConfig", sysConfig);
		return "/view/system/sysconfig/sysconfig_edit";
	}
	
	/**
	 * 
	 * 描述 修改
	 * @Title: edit 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysConfig
	 * @param request
	 * @param model
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/sysConfigEdit")
	@SystemWebLog(methodName="修改系统配置参数")
	public String edit(SysConfig sysConfig, HttpServletRequest request,Model model){
		try {
			sysConfigQueryService.edit(sysConfig);
			ConstantsUtil.readData();//重新load一次配置文件内容
			this.setMessage(request, "修改成功！", "green");
		} catch (Exception e) {
			log.error("修改失败！", e);
			this.setMessage(request, "修改失败！", "red");
		}
		return "redirect:/sysConfigList.action";
	}
	
}
