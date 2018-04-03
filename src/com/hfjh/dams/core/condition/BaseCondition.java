package com.hfjh.dams.core.condition;

/**
 * Description:查询条件封装基础类
 * @author wugj
 * @version 2016-6-14 上午11:01:31
 */
public class BaseCondition {
	public static final int INT_IGNORE_VALUE = -1;
    /**
     * 默认分页大小
     */
    private int pageSize= 20;
    
    /**
     * 默认页码
     */
    private int pageNo = 1;


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

}
