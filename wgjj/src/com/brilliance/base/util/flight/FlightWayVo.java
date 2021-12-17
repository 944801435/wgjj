package com.brilliance.base.util.flight;

import lombok.Data;

/**
 * 航线生成航点的实体
 * 
 * @Title: FlightWayVo.java
 * @author mq  
 * @date 2021年11月5日 下午4:06:53
 */
@Data
public class FlightWayVo {

	private String ident;// 航点名称

	private Double lonx;// 经度

	private Double laty;// 纬度

	private Double alt;// 高度

	public FlightWayVo(String ident, Double lonx, Double laty, Double alt) {
		super();
		this.ident = ident;
		this.lonx = lonx;
		this.laty = laty;
		this.alt = alt;
	}

	public FlightWayVo() {
		super();
	}
}
