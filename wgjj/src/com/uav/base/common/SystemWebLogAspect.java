package com.uav.base.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.uav.base.model.SysOptLog;
import com.uav.base.model.SysUser;
import com.uav.base.util.DateUtil;
import com.uav.web.view.system.sysOptLog.SysOptLogService;

@Aspect
@Service
public class SystemWebLogAspect {
	@Autowired
	private SysOptLogService sysOptLogService;

	@Around(value = "@annotation(systemWebLog)")
	public Object doAround(ProceedingJoinPoint joinPoint, SystemWebLog systemWebLog) throws Throwable {
		String menuName = joinPoint.getTarget().getClass().getAnnotation(SystemWebLog.class).menuName();
		String message = systemWebLog.methodName();
		Map<String, Object> map = clearNull(getFieldsNameValueMap(joinPoint));
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		SysUser sysUser = (SysUser) session.getAttribute(Constants.CURRUSER);
		Object result = joinPoint.proceed();
		if (sysUser == null) {
			sysUser = (SysUser) session.getAttribute(Constants.CURRUSER);
		}
		String userId = null;
		String deptId = null;
		if (sysUser != null) {
			userId = sysUser.getUserId();
			deptId = sysUser.getDeptId();
		}
		String opState = Constants.sys_default_yes;
		try {
			if (result instanceof MessageVo) {
				MessageVo o = (MessageVo) result;
				opState = o.getErrCode();
			}
		} catch (Exception e) {
			opState = Constants.sys_default_no;
		}
		SysOptLog sysOptLog = new SysOptLog(deptId, userId, DateUtil.getNowFullTimeString(), menuName, message + map.toString(), opState);
		sysOptLogService.addLog(sysOptLog);
		return result;
	}

	/**
	 * 去除null
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private Map<String, Object> clearNull(Map<String, Object> map) throws IllegalArgumentException, IllegalAccessException {
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value instanceof Integer) {
				continue;
			} else if (value instanceof Double) {
				continue;
			} else if (value instanceof Float) {
				continue;
			} else if (value instanceof Long) {
				continue;
			} else if (value instanceof String) {
				continue;
			} else if (value instanceof MultipartFile) {
				continue;
			} else {
				if (value == null) {
					continue;
				}
				Field[] fields = value.getClass().getDeclaredFields();
				Map<String, Object> itemMap = new HashMap<String, Object>();
				for (int i = 0; i < fields.length; i++) {
					if (Modifier.isStatic(fields[i].getModifiers())) {
						continue;
					}
					fields[i].setAccessible(true);
					if (fields[i].get(value) != null && fields[i].get(value) != "") {
						itemMap.put(fields[i].getName(), fields[i].get(value));
					}
				}
				map.put(key, itemMap);
			}
		}
		return map;
	}

	/**
	 * 获取所有参数和参数值
	 */
	private Map<String, Object> getFieldsNameValueMap(JoinPoint joinPoint) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
		Object[] paramValues = joinPoint.getArgs();
		for (int i = 0; i < paramValues.length; i++) {
			// 不解析HttpServletRequest类
			if (paramValues[i] instanceof HttpServletRequest) {
				continue;
			}
			// 不解析HttpServletResponse类
			if (paramValues[i] instanceof HttpServletResponse) {
				continue;
			}
			// 不解析Model类
			if (paramValues[i] instanceof Model) {
				continue;
			}
			map.put(paramNames[i], paramValues[i]);
		}
		return map;
	}

}
