package com.uav.base.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

public class ErrorsTag extends TagSupport {
	private static final long serialVersionUID = 7360999465421674384L;
	private static final Logger log = Logger.getLogger(ErrorsTag.class);
	
	public int doStartTag() throws JspException {
    	String msg=(String)((javax.servlet.http.HttpServletRequest)pageContext.getRequest()).getAttribute("message");
    	String color=(String)((javax.servlet.http.HttpServletRequest)pageContext.getRequest()).getAttribute("message_color");
        
    	if(msg==null || msg.trim().length()==0){
        	msg=(String)((javax.servlet.http.HttpServletRequest)pageContext.getRequest()).getSession().getAttribute("message");
        	color=(String)((javax.servlet.http.HttpServletRequest)pageContext.getRequest()).getSession().getAttribute("message_color");
        	((javax.servlet.http.HttpServletRequest)pageContext.getRequest()).getSession().removeAttribute("message");
        	((javax.servlet.http.HttpServletRequest)pageContext.getRequest()).getSession().removeAttribute("message_color");
        	if(msg==null || msg.trim().length()==0){
            	return (EVAL_BODY_INCLUDE);
            }
        }
        
        StringBuffer results=new StringBuffer();
        if(msg.contains("失败") || "red".equals(color)){
        	results.append("<div class=\"alert alert-success \" style=\"background-color: red;color:#fff;\"><button data-dismiss=\"alert\" class=\"close\" style=\"color: #fff;\">×</button>"+msg+"</div>");
        }else{
        	results.append("<div class=\"alert alert-success \" ><button data-dismiss=\"alert\" class=\"close\" >×</button>"+msg+"</div>");
        }
        
        
        try {
        	String str=StringEscapeUtils.escapeHtml4(results.toString());
        	pageContext.getOut().print(StringEscapeUtils.unescapeHtml4(str));
        	
		} catch (Exception e) {
			log.error("获取错误信息失败！");
		}
        
        return (EVAL_BODY_INCLUDE);
    }
	
}