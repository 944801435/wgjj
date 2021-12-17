package com.brilliance.base.common;

public class MessageVo {
	
	public static final String SUCCESS="1";
	public static final String FAIL="0";
	
	
	private String errCode;   //1成功    0失败
	private String errMsg;    
	private Object data;
	
	
	public MessageVo(String errCode, String errMsg, Object data) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.data = data;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

}
