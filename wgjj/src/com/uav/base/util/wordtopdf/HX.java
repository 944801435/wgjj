package com.uav.base.util.wordtopdf;

import lombok.Data;

@Data
public class HX {
    /**
     * 1、普通文本，2、加下划线，3、↑符号，4、↓符号
     */
    private String type;
    private String value;
    
	public HX() {
		super();
	}
	
	public HX(String type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
}
