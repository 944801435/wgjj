package com.uav.base.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页查询时，封装查询的结果
 * @author Lee
 *
 */
@Data
public class PagerVO<T> {
	
	/**
	 * 总记录数
	 */
	private  int counts;//总个数
	private  int pageSize=10; //每页个数
	private  int pages=1; //总页数
	private  int pageNo=1; //当前页
	/**
	 * 分页查询到的数据
	 */
	private List<T> items ;//数据记录
	/**
	 * 查询参数封装
	 */
	private Map params;
	/**
	 * 实体查询参数
	 */
	private T entiyParam;
}
