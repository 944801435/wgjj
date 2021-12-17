package com.brilliance.base.listener;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.brilliance.base.common.Constants;

public class AdminRoleFilter implements Filter {

	protected Object sb = null;
	protected String url = null;
	protected FilterConfig filterConfig = null;
	private String contextPath = null;
	private String[] urlList = null;

	public void destroy() {
		this.url = null;
		this.filterConfig = null;
		this.sb = null;
		this.urlList = null;

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		String uri = ((HttpServletRequest) req).getRequestURI();
		HttpSession session = ((HttpServletRequest) req).getSession();
		this.sb = session.getAttribute(Constants.CURRUSER);
		if (this.sb == null) {
			boolean flag = false;
			for (String str : urlList) {
				str = ((HttpServletRequest) req).getContextPath() + str;
				if (str.equals(uri) || contextPath.equals(uri)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				((HttpServletRequest) req).getSession().setAttribute("message", "您尚未登录或登录失效，请重新登录！");
				// 获取request里面的cookie cookie里面存值方式也是 键值对的方式
				String loginUrl = urlList[0];
				Cookie[] cookie = ((HttpServletRequest) req).getCookies();
				if (cookie != null) {
					for (int i = 0; i < cookie.length; i++) {
						Cookie cook = cookie[i];
						if (cook.getName().equalsIgnoreCase("loginUrl")) { // 获取键
							loginUrl = cook.getValue().toString(); // 获取值
							break;
						}
					}
				}
				((HttpServletResponse) res).sendRedirect(((HttpServletRequest) req).getContextPath() + loginUrl);
				return;
			}

		}
		fc.doFilter(req, res);
	}

	public void init(FilterConfig fc) throws ServletException {
		this.filterConfig = fc;
		this.url = filterConfig.getInitParameter("url");
		this.urlList = this.url.split(",");
		this.sb = null;

		ServletContext sc = fc.getServletContext();
		String ctxPath = sc.getContextPath();
		if (!ctxPath.equals("/"))//
			ctxPath = ctxPath + "/";//
		this.contextPath = ctxPath;
	}
}