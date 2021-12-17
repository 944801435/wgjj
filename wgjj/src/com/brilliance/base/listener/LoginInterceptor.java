package com.brilliance.base.listener;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.brilliance.base.common.Constants;

/**
 * 登录拦截器
 * @author ThinkGem
 * @version 2014-8-19
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		// 当过滤器有AOP代理是也能回去到annotation,而上面的方法获取不到。当没有AOP代理时两个方法效果一样
		LoginAccess annotation = handlerMethod.getBeanType().getAnnotation(LoginAccess.class);// method.getAnnotation(LoginAccess.class);

		if (annotation != null) {
			Object obj = request.getSession().getAttribute(Constants.CURRUSER);
			String path = request.getServletPath();
			if (obj == null) {
				if (!path.equals("/login.action") && !path.equals("/login_out.action")) {
					request.getSession().setAttribute("message", "您尚未登录，请先登录！");
					String loginUrl = "/view/login.jsp";
					Cookie[] cookie = request.getCookies();
					if (cookie != null) {
						for (int i = 0; i < cookie.length; i++) {
							Cookie cook = cookie[i];
							if (cook.getName().equalsIgnoreCase("loginUrl")) { // 获取键
								loginUrl = cook.getValue().toString(); // 获取值
								break;
							}
						}
					}
					response.sendRedirect(request.getContextPath() + loginUrl);
					return false;
				}
			}
			return true;

		}
		// 没有注解通过拦截
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

	}

}
