package com.uav.web.view.system.sysRole;

import com.uav.base.common.PagerVO;
import com.uav.base.model.SysRole;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uav.base.common.BaseAction;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色权限管理
 * 
 * @Title: SysRoleAction.java
 */
@Slf4j
@Controller
@LoginAccess
@SystemWebLog(menuName = "角色权限管理")
public class SysRoleAction extends BaseAction {

	@Autowired
	private SysRoleService roleService;
	/**
	 *
	 * 描述 首次打开页面是调用，防止查询参数丢失
	 * @Title: main
	 * @author
	 * @Modified By ZSF
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/roleList_main")
	public String main(HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_sysRole");
		if (queryMap == null) {
			queryMap = new HashMap<String, Object>(16);
		}
		queryMap.put("curPage", 1);
		request.getSession().setAttribute("query_sysRole", queryMap);
		return findList(1, null, request, model);
	}
	/**
	 *
	 * 描述 获取角色列表
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param curPage 当前页面
	 * @param sysRole 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/roleList.action")
	public String findList(Integer curPage, SysRole sysRole, HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_sysRole");
		if (curPage == null) {
			curPage = Integer.parseInt(queryMap.get("curPage") + "");
		}
		queryMap.put("curPage", curPage);
		request.getSession().setAttribute("query_sysRole", queryMap);

		try {
			if (sysRole == null) {
				sysRole = new SysRole();
			}
			PagerVO pv = roleService.findList(sysRole, curPage, pageSize);
			model.addAttribute("roleList", pv.getDatas());
			model.addAttribute("totalCount", pv.getTotal());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取角色列表失败！", e);
			this.setMessage(request, "获取角色列表失败！", "red");
		}
		return "/view/system/role/role_list";
	}
	/**
	 *
	 * 描述 跳转修改页面
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param sysRole 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/to_role_edit")
	public String toEdit(SysRole sysRole, Model model, HttpServletRequest request) {
		sysRole = roleService.findRoleById(sysRole.getRoleId());
		model.addAttribute("sysRole", sysRole);
		// 查询能够授权的角色
		List<SysRole> roleList = roleService.findList();
		model.addAttribute("roleList", roleList);
		return "/view/system/role/role_edit";
	}
	/**
	 *
	 * 描述 修改角色信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param sysRole 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/roleEdit.action")
	@SystemWebLog(methodName = "修改角色页面")
	public String edit(SysRole sysRole, String menuIds, HttpServletRequest request) {
		try {
			String[] menuId = null;
			if (!StringUtils.isBlank(menuIds)) {
				menuId = menuIds.split(",");
			}
			String message = roleService.update(sysRole,menuId);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				return "/view/system/role/role_edit";
			}
			this.setMessage(request, "修改角色成功！", "green");
		} catch (Exception e) {
			log.error("修改角色失败！", e);
			this.setMessage(request, "修改角色失败！", "red");
			return "/view/system/role/role_edit";
		}
		return "redirect:/roleList.action";
	}
	/**
	 *
	 * 描述 删除角色信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param sysRole 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/roleDel.action")
	@SystemWebLog(methodName = "删除角色")
	public String del(SysRole sysRole, String[] roleIds, HttpServletRequest request) {
		if (roleIds == null) {
			log.error("删除角色失败：参数错误！");
			this.setMessage(request, "删除角色失败：参数错误！", "red");
			return "redirect:/roleList.action";
		}
		try {
			roleService.delete(roleIds);
			this.setMessage(request, "删除角色成功！", "green");
		} catch (Exception e) {
			this.setMessage(request, "删除角色失败！", "red");
		}
		return "redirect:/roleList.action";
	}
	/**
	 *
	 * 描述 跳转新增角色页面
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param sysRole 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/to_role_add")
	public String toAdd(SysRole sysRole, Model model, HttpServletRequest request) {
		sysRole = roleService.findRoleById(sysRole.getRoleId());
		model.addAttribute("sysRole", sysRole);
		// 查询能够授权的角色
		List<SysRole> roleList = roleService.findList();
		model.addAttribute("roleList", roleList);
		return "/view/system/role/role_add";
	}
	/**
	 *
	 * 描述 保存角色信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param sysRole 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/roleAdd.action")
	@SystemWebLog(methodName = "新增角色")
	public String add(SysRole sysRole, String menuIds, HttpServletRequest request) {
		try {
			String[] menuId = null;
			if (!StringUtils.isBlank(menuIds)) {
				menuId = menuIds.split(",");
			}
			String message = roleService.save(sysRole,menuId);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				return "/view/system/role/role_add";
			}
			this.setMessage(request, "添加角色成功！", "green");
		} catch (Exception e) {
			log.error("添加角色失败！", e);
			this.setMessage(request, "添加角色失败！", "red");
			return "/view/system/role/role_add";
		}
		return "redirect:/roleList.action";
	}
}
