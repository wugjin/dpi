package com.hfjh.dams.core.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: wugj
 * @date: 2017-05-31 上午10:33:36
 * @instructions: page(页)类对象，实现了一些基本的用于分页的方法
 * @version:
 */
public class Page implements Serializable {

	private static final long serialVersionUID = 1L; // 序列化控制

	public static final Page EMPTY_PAGE = new Page(); // 空页

	public static final int DEFAULT_PAGE_SIZE = 10; // 默认每页记录数

	/**
	 * 总的查询记录数
	 */
	private int totalSize;

	/**
	 * 每页记录数
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 当前页中得第一条记录在数据库中的位置记录
	 */
	private int start;

	/**
	 * 存放与当前页中的、用于返回的结果列表
	 */
	private List<?> result;

	private List<Map<String, Object>> resultMap;

	/**
	 * 构造函数 start：当前页中得第一条记录在数据库中的位置记录 currentPageSize：结果的总页数 totalSize：总的查询记录数
	 * pageSize：每页记录数 data：存放与当前页中的、用于返回的结果列表
	 */
	public Page(int start, int totalSize, int pageSize, List<?> data) {

		this.pageSize = pageSize;
		this.start = start;
		this.totalSize = totalSize;
		this.result = data;
	}

	/**
	 * 构造函数--空页面
	 */
	public Page() {
		this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList<Object>());
	}

	/**
	 * 总的查询记录数
	 */
	public int getTotalSize() {
		return this.totalSize;
	}

	/**
	 * 当前页中的记录
	 */
	public List<?> getResult() {
		return this.result;
	}

	/**
	 * 取每页数据容量
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * 获得当前页中得第一条记录在数据库中的位置
	 */
	public int getStart() {
		return start;
	}


	public void setResult(List<?> result) {
		this.result = result;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public List<Map<String, Object>> getResultMap() {
		return resultMap;
	}

	public void setResultMap(List<Map<String, Object>> resultMap) {
		this.resultMap = resultMap;
	}

	public void setStart(int start) {
		this.start = start;
	}

}
