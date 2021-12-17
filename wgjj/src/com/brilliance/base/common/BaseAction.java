package com.brilliance.base.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.brilliance.base.model.SysUser;

@SuppressWarnings("unchecked")
public class BaseAction {

	public static final int pageSize = 10; // 每页显示10条

	public void setMessage(HttpServletRequest request, String message, String color) {
		HttpSession session = request.getSession();
		session.setAttribute("message", message);
		session.setAttribute("message_color", color);
	}

	// 获取当前ip
	public String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if ("0:0:0:0:0:0:1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	/**
	 * 验证用户是否有权限
	 */
	public boolean hasPms(String pmsId, HttpServletRequest request) {
		List<String> pmsIds = (List<String>) request.getSession().getAttribute("pmsIds");
		if (pmsIds.contains(pmsId)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前登录的用户
	 * 
	 * @param request
	 * @return
	 */
	public Object getCurUser(HttpServletRequest request) {
		return request.getSession().getAttribute(Constants.CURRUSER);
	}

	/**
	 * 获取当前登录的用户id
	 * 
	 * @param request
	 * @return
	 */
	public String getCurUserid(HttpServletRequest request) {
		Object obj = getCurUser(request);
		if (obj == null) {
			return "";
		}
		if (obj instanceof SysUser) {
			return ((SysUser) obj).getUserId();
		} else {
			return "";
		}
	}

	/**
	 * 获取当前登录的机构id
	 * 
	 * @param request
	 * @return
	 */
	public String getCurDeptid(HttpServletRequest request) {
		Object obj = getCurUser(request);
		if (obj == null) {
			return "";
		}
		if (obj instanceof SysUser) {
			return ((SysUser) obj).getDeptId();
		} else {
			return "";
		}
	}

	/**
	 * 获取当前登录用户的登录名
	 * 
	 * @param request
	 * @return
	 */
	public String getCurUsername(HttpServletRequest request) {
		Object obj = getCurUser(request);
		if (obj == null) {
			return "";
		}
		if (obj instanceof SysUser) {
			return ((SysUser) obj).getUserName();
		} else {
			return "";
		}
	}

	/**
	 * 获取当前登录用户所属机构类型
	 */
	public String getCurDeptType(HttpServletRequest request) {
		Object obj = getCurUser(request);
		if (obj == null) {
			return "";
		}
		if (obj instanceof SysUser) {
			return ((SysUser) obj).getSysDept().getDeptType();
		} else {
			return "";
		}
	}

	/**
	 * 获取当前登录用户所属机构的管控范围
	 */
	public String getCurDeptRange(HttpServletRequest request) {
		Object obj = getCurUser(request);
		if (obj == null) {
			return "";
		}
		if (obj instanceof SysUser) {
			return ((SysUser) obj).getSysDept().getRangeLoc();
		} else {
			return "";
		}
	}

	/**
	 * 获取当前登录用户的所有权限
	 */
	public List<String> getCurPmsIds(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute("pmsIds");
		if (obj != null && obj instanceof ArrayList) {
			return (List<String>) obj;
		}
		return new ArrayList<String>();
	}

}
