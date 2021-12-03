package com.uav.web.view.system.sysPms;

import com.uav.base.common.BaseAction;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.SysPms;
import com.uav.web.view.system.sysMenu.SysMenuService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作权限管理
 * 
 * @Title: SysPmsAction.java
 */
@Slf4j
@Controller
@LoginAccess
@SystemWebLog(menuName = "操作权限管理")
public class SysPmsAction extends BaseAction {

	@Autowired
	private SysPmsService pmsService;
	@Autowired
	private SysMenuService menuService;
	/**
	 *
	 * 描述 首次打开页面是调用，防止查询参数丢失
	 * @Title: main
	 * @author
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/pmsList_main")
	public String main(HttpServletRequest request, SysPms sysPms, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_sysPms");
		if (queryMap == null) {
			queryMap = new HashMap<String, Object>(16);
		}
		queryMap.put("curPage", 1);
		request.getSession().setAttribute("query_sysPms", queryMap);
		return findList(1, sysPms, request, model);
	}
	/**
	 *
	 * 描述 获取操作权限列表
	 * @Title: findList
	 * @param curPage 当前页面
	 * @param sysPms 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/pmsList.action")
	public String findList(Integer curPage, SysPms sysPms, HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_sysPms");
		if (curPage == null) {
			curPage = Integer.parseInt(queryMap.get("curPage") + "");
		}
		queryMap.put("curPage", curPage);
		request.getSession().setAttribute("query_sysPms", queryMap);

		try {
			if (sysPms == null) {
				sysPms = new SysPms();
			}
			PagerVO pv = pmsService.findList(sysPms, curPage, pageSize);
			model.addAttribute("pmsList", pv.getDatas());
			model.addAttribute("menuId", sysPms.getMenuId());
			model.addAttribute("totalCount", pv.getTotal());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取操作权限列表失败！", e);
			this.setMessage(request, "获取操作权限列表失败！", "red");
		}
		return "/view/system/pms/pms_list";
	}
	@RequestMapping("/to_pms_edit")
	public String toEdit(SysPms sysPms, Model model, HttpServletRequest request) {
		sysPms = pmsService.findPmsById(sysPms.getPmsId());
		model.addAttribute("sysPms", sysPms);
		return "/view/system/pms/pms_edit";
	}
	@RequestMapping("/pmsEdit.action")
	@SystemWebLog(methodName = "修改操作权限页面")
	public String edit(SysPms sysPms, String menuIds, Model model,HttpServletRequest request) {
		try {
			String message = pmsService.update(sysPms);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				model.addAttribute("sysPms", sysPms);
				model.addAttribute("menuId", sysPms.getMenuId());
				return "/view/system/pms/pms_edit";
			}
			this.setMessage(request, "修改操作权限成功！", "green");
		} catch (Exception e) {
			log.error("修改操作权限失败！", e);
			this.setMessage(request, "修改操作权限失败！", "red");
			return "/view/system/pms/pms_edit";
		}
		return "redirect:/pmsList.action?menuId="+sysPms.getMenuId();
	}
	@RequestMapping("/pmsDel.action")
	@SystemWebLog(methodName = "删除操作权限")
	public String del(SysPms sysPms, String[] pmsIds, HttpServletRequest request) {
		if (pmsIds == null) {
			log.error("删除操作权限失败：参数错误！");
			this.setMessage(request, "删除操作权限失败：参数错误！", "red");
			return "redirect:/pmsList.action";
		}
		try {
			pmsService.delete(pmsIds);
			this.setMessage(request, "删除操作权限成功！", "green");
		} catch (Exception e) {
			this.setMessage(request, "删除操作权限失败！", "red");
		}
		String menuId = (String) request.getAttribute("menuId");
		return "redirect:/pmsList.action?menuId="+sysPms.getMenuId();
	}
	@RequestMapping("/to_pms_add")
	public String toAdd(SysPms sysPms, Model model, HttpServletRequest request) {
		model.addAttribute("menuId", sysPms.getMenuId());
		return "/view/system/pms/pms_add";
	}
	@RequestMapping("/pmsAdd.action")
	@SystemWebLog(methodName = "新增操作权限")
	public String add(SysPms sysPms, String menuIds, Model model, HttpServletRequest request) {
		try {
			String message = pmsService.save(sysPms);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				model.addAttribute("sysPms", sysPms);
				model.addAttribute("menuId", sysPms.getMenuId());
				return "/view/system/pms/pms_add";
			}
			this.setMessage(request, "添加操作权限成功！", "green");
		} catch (Exception e) {
			log.error("添加操作权限失败！", e);
			this.setMessage(request, "添加操作权限失败！", "red");
			return "/view/system/pms/pms_add";
		}
		return "redirect:/pmsList.action?menuId="+sysPms.getMenuId();
	}
}
