package com.uav.base.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询时，封装查询的结果
 * @author Lee
 *
 */
public class PagerVO<T> {
	
	/**
	 * 总记录数
	 */
	private int total;
	
	/**
	 * 分页查询到的数据
	 */
	private List<T> datas = new ArrayList<T>();
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getDatas() {
		return datas;
	}
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
}
