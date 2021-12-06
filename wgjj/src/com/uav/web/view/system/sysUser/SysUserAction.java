package com.uav.web.view.system.sysUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uav.base.common.BaseAction;
import com.uav.base.common.Constants;
import com.uav.base.common.MessageVo;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.SysDept;
import com.uav.base.model.SysMenu;
import com.uav.base.model.SysRole;
import com.uav.base.model.SysUser;
import com.uav.base.util.DateUtil;
import com.uav.web.view.system.sysMenu.SysMenuService;
import com.uav.web.view.system.sysRole.SysRoleService;

/**
 * 用户管理
 * @ClassName: SysUserAction
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午2:07:20
 */
@Controller
@LoginAccess
@SystemWebLog(menuName = "用户管理")
@SuppressWarnings("unchecked")
public class SysUserAction extends BaseAction {
	protected static final Logger log = Logger.getLogger(SysUserAction.class);

	@Autowired
	private SysUserService userService;
	@Autowired
	private SysMenuService menuService;
	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 用户登录
	 * 描述
	 * @Title: login 
	 * @author 
	 * @Modified By 钟志峰
	 * @param userName 用户名称
	 * @param userPwd 用户密码
	 * @param deptId 用户部门ID
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/login.action")
	@SystemWebLog(methodName = "用户登录")
	public String login(String userName, String userPwd, String deptId, HttpServletRequest request, Model model) {
		String loginUrl = "/view/login.jsp";
		Cookie[] cookie = request.getCookies();
		if (cookie != null) {
			for (int i = 0; i < cookie.length; i++) {
				Cookie cook = cookie[i];
				// 获取键
				if (cook.getName().equalsIgnoreCase("loginUrl")) {
					// 获取值
					loginUrl = cook.getValue().toString();
					break;
				}
			}
		}
		try {
			String pwd = "";
			if (StringUtils.isBlank(userName) || StringUtils.isBlank(userPwd)) {
				this.setMessage(request, "用户名或密码错误！", "red");
				log.info("用户名或密码错误，登录失败！");
				return "redirect:" + loginUrl;
			}
			pwd = userPwd.trim();
			// 读取用户信息
			SysUser obj = null;
			if (StringUtils.isBlank(deptId)) {
				obj = userService.findUserByUserNamePwd(userName, pwd);
			} else {
				obj = userService.findUserByUserNamePwd(userName, pwd, deptId);
			}
			if (obj == null) {
				this.setMessage(request, "用户名或密码错误！", "red");
				log.info("用户名或密码错误，登录失败！");
				return "redirect:" + loginUrl;
			}
			// 查询用户对应机构
			SysDept sysDept = userService.findDeptById(obj.getDeptId());
			obj.setDeptName(sysDept.getDeptName());
			String clientIp = getClientIP(request);
			if (clientIp == null) {
				clientIp = "";
			}
			// 读取用户信息成功后修改用户登录信息
			obj.setLastIp(clientIp);
			obj.setLastTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
			userService.update(obj);

			List<SysMenu> menuList = new ArrayList<SysMenu>();
			List<String> pmsIds = new ArrayList<String>();
			if (Constants.SYS_USER_ID.equals(obj.getUserId())) {
				// 如果是admin就获得所有菜单，所有权限
				menuList = menuService.findMenuList();
				pmsIds = menuService.findPmsList();
			} else {
				// 读取用户相对应的菜单
				menuList = menuService.findMenuListByRoleId(obj.getRoleId());
				// 读取用户所有的权限
				pmsIds = menuService.findListByRoleId(obj.getRoleId());
			}
			request.getSession().setAttribute("menuList", menuList);
			request.getSession().setAttribute("pmsIds", pmsIds);
			request.getSession().setAttribute(Constants.CURRUSER, obj);
			log.info("request.getSession().getId()===" + request.getSession().getId());
			return "redirect:/view/main.jsp";
		} catch (Exception e) {
			this.setMessage(request, "系统异常，请联系管理员！", "red");
			log.error("登录失败！", e);
			return "redirect:" + loginUrl;
		}
	}

	/**
	 * 
	 * 描述 注销
	 * @Title: logout 
	 * @author 
	 * @Modified By 钟志峰
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/logout.action")
	@SystemWebLog(methodName = "用户注销")
	public String logout(HttpServletRequest request) {
		String loginUrl = "/view/login.jsp";
		Cookie[] cookie = request.getCookies();
		if (cookie != null) {
			for (int i = 0; i < cookie.length; i++) {
				Cookie cook = cookie[i];
				// 获取键
				if (cook.getName().equalsIgnoreCase("loginUrl")) {
					// 获取值
					loginUrl = cook.getValue().toString();
					break;
				}
			}
		}
		try {
			@SuppressWarnings("rawtypes")
			Enumeration e = request.getSession().getAttributeNames();
			while (e.hasMoreElements()) {
				request.getSession().removeAttribute(e.nextElement().toString());
			}
			request.getSession().invalidate();
			log.info("注销成功！");
		} catch (Exception e) {
			log.error("注销失败！", e);
		}
		return "redirect:" + loginUrl;
	}

	/**
	 * 
	 * 描述 修改密码
	 * @Title: modifyPwd 
	 * @author 
	 * @Modified By 钟志峰
	 * @param old_pwd 旧密码
	 * @param new_pwd 新密码
	 * @param com_pwd 确认密码
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * Object 返回类型 
	 * @throws
	 */
	@RequestMapping("/modifyPwd.action")
	@ResponseBody
	@SystemWebLog(methodName = "用户修改密码")
	public Object modifyPwd(String old_pwd, String new_pwd, String com_pwd, HttpServletRequest request, Model model) {
		MessageVo vo = null;
		try {
			SysUser sysUser = (SysUser) this.getCurUser(request);
			if (StringUtils.isBlank(old_pwd)) {
				vo = new MessageVo(MessageVo.FAIL, "原密码不能为空！", null);
				return vo;
			} else {
				if (!old_pwd.equals(sysUser.getUserPwd())) {
					vo = new MessageVo(MessageVo.FAIL, "原密码不正确！", null);
					return vo;
				}
			}
			if (StringUtils.isBlank(new_pwd)) {
				vo = new MessageVo(MessageVo.FAIL, "新密码不能为空！", null);
				return vo;
			}
			if (!new_pwd.equals(com_pwd)) {
				vo = new MessageVo(MessageVo.FAIL, "两次密码输入不一致！", null);
				return vo;
			}
			userService.modifyPwd(sysUser.getUserName(), new_pwd);
			sysUser.setUserPwd(new_pwd);
			request.getSession().setAttribute(Constants.CURRUSER, sysUser);
			vo = new MessageVo(MessageVo.SUCCESS, "修改密码成功！", null);
		} catch (Exception e) {
			log.error("修改密码失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "修改密码失败！", null);
		}
		return vo;
	}

	/**
	 * 
	 * 描述 首次打开页面是调用，防止查询参数丢失
	 * @Title: main 
	 * @author 
	 * @Modified By 钟志峰
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/userList_main")
	public String main(HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_sysUser");
		if (queryMap == null) {
			queryMap = new HashMap<String, Object>(16);
		}
		queryMap.put("curPage", 1);
		request.getSession().setAttribute("query_sysUser", queryMap);
		return findList(1, null, request, model);
	}

	/**
	 * 
	 * 描述 获取用户列表
	 * @Title: findList 
	 * @author 
	 * @Modified By 钟志峰
	 * @param curPage 当前页面
	 * @param sysUser 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/userList.action")
	public String findList(Integer curPage, SysUser sysUser, HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_sysUser");
		if (curPage == null) {
			curPage = Integer.parseInt(queryMap.get("curPage") + "");
		}
		queryMap.put("curPage", curPage);
		request.getSession().setAttribute("query_sysUser", queryMap);

		try {
			if (sysUser == null) {
				sysUser = new SysUser();
			}
			sysUser.setUserId(this.getCurUserid(request));
			sysUser.setDeptId(this.getCurDeptid(request));
			PagerVO pv = userService.findList(sysUser, curPage, pageSize);
			for (Object temp : pv.getDatas()) {
				if (temp instanceof SysUser) {
					SysUser obj = (SysUser) temp;
					if (StringUtils.isNotBlank(obj.getDeptId())) {
						obj.setDeptName(obj.getSysDept().getDeptName());
					}
					if (StringUtils.isNotBlank(obj.getRoleId())) {
						obj.setRoleName(obj.getSysRole().getRoleName());
					}
				}
			}
			model.addAttribute("userList", pv.getDatas());
			model.addAttribute("totalCount", pv.getTotal());
			model.addAttribute("sysUser", sysUser);
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取用户列表失败！", e);
			this.setMessage(request, "获取用户列表失败！", "red");
		}
		return "/view/system/user/user_list";
	}

	/**
	 * 
	 * 描述 删除用户
	 * @Title: del 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysUser
	 * @param userIds
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/userDel.action")
	@SystemWebLog(methodName = "删除用户")
	public String del(SysUser sysUser, String[] userIds, HttpServletRequest request) {
		if (userIds == null) {
			log.error("删除用户失败：参数错误！");
			this.setMessage(request, "删除用户失败：参数错误！", "red");
			return "redirect:/userList.action";
		}
		try {
			userService.delete(userIds);
			this.setMessage(request, "删除用户成功！", "green");
		} catch (Exception e) {
			this.setMessage(request, "删除用户失败！", "red");
		}
		return "redirect:/userList.action";
	}

	/**
	 * 
	 * 描述 跳转新增页面
	 * @Title: toAdd 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysUser
	 * @param model
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/to_add")
	public String toAdd(SysUser sysUser, Model model, HttpServletRequest request) {
		sysUser = userService.findUserById(sysUser.getUserId());
		model.addAttribute("sysUser", sysUser);
		// 查询能够授权的角色
		List<SysRole> roleList = sysRoleService.findList();
		model.addAttribute("roleList", roleList);
		return "/view/system/user/user_add";
	}

	/**
	 * 
	 * 描述  新增用户
	 * @Title: add 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysUser
	 * @param menuIds
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/userAdd.action")
	@SystemWebLog(methodName = "新增用户")
	public String add(SysUser sysUser, HttpServletRequest request) {
		try {
			sysUser.setUserSts(Constants.sys_default_yes);
			sysUser.setDeptId(this.getCurDeptid(request));
			sysUser.setIsAdmin(Constants.sys_default_no);
			String message = userService.save(sysUser);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				return "/view/system/user/user_add";
			}
			this.setMessage(request, "添加用户成功！", "green");
		} catch (Exception e) {
			log.error("添加用户失败！", e);
			this.setMessage(request, "添加用户失败！", "red");
			return "/view/system/user/user_add";
		}
		return "redirect:/userList.action";
	}

	/**
	 * 
	 * 描述 跳转修改页面
	 * @Title: toEdit 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysUser
	 * @param model
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/to_edit")
	public String toEdit(SysUser sysUser, Model model, HttpServletRequest request) {
		sysUser = userService.findUserById(sysUser.getUserId());
		model.addAttribute("sysUser", sysUser);
		// 查询能够授权的角色
		List<SysRole> roleList = sysRoleService.findList();
		model.addAttribute("roleList", roleList);
		return "/view/system/user/user_edit";
	}

	/**
	 * 
	 * 描述 修改用户
	 * @Title: edit 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysUser
	 * @param menuIds
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/userEdit.action")
	@SystemWebLog(methodName = "修改用户页面")
	public String edit(SysUser sysUser, HttpServletRequest request) {
		try {
			String message = userService.update(sysUser);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				return "/view/system/user/user_edit";
			}
			this.setMessage(request, "修改用户成功！", "green");
		} catch (Exception e) {
			log.error("修改用户失败！", e);
			this.setMessage(request, "修改用户失败！", "red");
			return "/view/system/user/user_edit";
		}
		return "redirect:/userList.action";
	}

	/**
	 * 
	 * 描述 跳转修改个人信息
	 * @Title: toModifyMsg 
	 * @author 
	 * @Modified By 钟志峰
	 * @param model
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/to_modify_msg.action")
	public String toModifyMsg(Model model, HttpServletRequest request) {
		SysUser sysUser = userService.findUserById(this.getCurUserid(request));
		model.addAttribute("sysUser", sysUser);
		return "/view/system/user/modify_msg";
	}

	/**
	 * 
	 * 描述 修改个人信息
	 * @Title: modify_msg 
	 * @author 
	 * @Modified By 钟志峰
	 * @param phone
	 * @param old_pwd
	 * @param new_pwd
	 * @param com_pwd
	 * @param request
	 * @param model
	 * @return    
	 * Object 返回类型 
	 * @throws
	 */
	@RequestMapping("/modify_msg.action")
	@ResponseBody
	@SystemWebLog(methodName = "修改改个人信息")
	public Object modifyMsg(String phone, String old_pwd, String new_pwd, String com_pwd, Integer init_menu_id, HttpServletRequest request,
			Model model) {
		MessageVo vo = null;
		try {
			SysUser sysUser = (SysUser) this.getCurUser(request);
			if (!StringUtils.isBlank(old_pwd) || !StringUtils.isBlank(new_pwd) || !StringUtils.isBlank(com_pwd)) {
				if (StringUtils.isBlank(old_pwd)) {
					vo = new MessageVo(MessageVo.FAIL, "请输入原密码！", null);
					return vo;
				} else {
					if (!old_pwd.equals(sysUser.getUserPwd())) {
						vo = new MessageVo(MessageVo.FAIL, "原密码不正确！", null);
						return vo;
					}
				}
				if (StringUtils.isBlank(new_pwd)) {
					vo = new MessageVo(MessageVo.FAIL, "请输入新密码！", null);
					return vo;
				}
				if (!new_pwd.equals(com_pwd)) {
					vo = new MessageVo(MessageVo.FAIL, "新密码和确认密码不一致！", null);
					return vo;
				}
				sysUser.setUserPwd(new_pwd);
			}
			if (init_menu_id != null) {
				sysUser.setInitMenuId(init_menu_id);
			}
			sysUser.setPhone(phone);
			userService.update(sysUser);
			vo = new MessageVo(MessageVo.SUCCESS, "修改个人信息成功！", null);
		} catch (Exception e) {
			log.error("修改个人信息失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "修改个人信息失败！", null);
		}
		return vo;
	}

	/**
	 * 获取用户常用菜单
	 */
	@RequestMapping("/findUserMenuId")
	@ResponseBody
	public Object findUserMenuId(HttpServletRequest request) {
		MessageVo vo = null;
		try {
			Integer[] menuIds = userService.findUserMenuId(this.getCurUserid(request));
			vo = new MessageVo(MessageVo.SUCCESS, null, menuIds);
		} catch (Exception e) {
			log.error("获取用户常用菜单失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "获取用户常用菜单失败！", null);
		}
		return vo;
	}

	/**
	 * 保存用户常用菜单
	 */
	@RequestMapping("/saveUserMenuId")
	@ResponseBody
	@SystemWebLog(methodName = "保存用户常用菜单")
	public Object saveUserMenuId(HttpServletRequest request) {
		MessageVo vo = null;
		try {
			String menuIds = request.getParameter("menuIds");
			userService.saveUserMenuId(this.getCurUserid(request), menuIds.split(","));
			vo = new MessageVo(MessageVo.SUCCESS, "保存用户常用菜单成功！", menuIds);
		} catch (Exception e) {
			log.error("保存用户常用菜单失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "保存用户常用菜单失败！", null);
		}
		return vo;
	}
}