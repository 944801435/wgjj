package com.brilliance.web.view.system.sysMenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brilliance.base.common.BaseAction;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.listener.LoginAccess;
import com.brilliance.base.model.SysMenu;
import com.brilliance.base.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单管理
 * @ClassName: SysMenuAction
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午1:32:00
 */
@Slf4j
@Controller
@LoginAccess
public class SysMenuAction extends BaseAction {
	@Autowired
	private SysMenuService menuService;

	/**
	 * 同步加载菜单信息(当前用户只能加载自己已有的)
	 * 描述
	 * @Title: bankTreeCheckJson 
	 * @author 
	 * @Modified By 钟志峰
	 * @param request
	 * @param model
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/MenuTreeCheckJson")
	public String bankTreeCheckJson(HttpServletRequest request, Model model) {
		// 当前操作角色ID
		String roleId = request.getParameter("roleId");
		List<String> menuIds = menuService.findListByRoleId(roleId);
		List<MenuTreeNode> nodeList = menuService.findAllChild(menuIds);
		model.addAttribute("nodeList", JsonUtil.listToJson(nodeList));
		return "view/system/role/menuTreeCheck";
	}
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
	@RequestMapping("/menuList_main")
	public String main(HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_sysMenu");
		if (queryMap == null) {
			queryMap = new HashMap<String, Object>(16);
		}
		queryMap.put("curPage", 1);
		request.getSession().setAttribute("query_sysMenu", queryMap);
		return findList(1, null, request, model);
	}
	/**
	 *
	 * 描述 获取操作权限列表
	 * @Title: findList
	 * @param curPage 当前页面
	 * @param sysMenu 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/menuList.action")
	public String findList(Integer curPage, SysMenu sysMenu, HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_sysMenu");
		if (curPage == null) {
			curPage = Integer.parseInt(queryMap.get("curPage") + "");
		}
		queryMap.put("curPage", curPage);
		request.getSession().setAttribute("query_sysMenu", queryMap);

		try {
			if (sysMenu == null) {
				sysMenu = new SysMenu();
			}
			PagerVO pv = menuService.findList(sysMenu, curPage, pageSize);
			model.addAttribute("menuList", pv.getItems());
			model.addAttribute("totalCount", pv.getCounts());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取操作权限列表失败！", e);
			this.setMessage(request, "获取操作权限列表失败！", "red");
		}
		return "/view/system/pms/menu_list";
	}
}
