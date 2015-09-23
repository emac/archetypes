#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page<T> implements Serializable {
	private static final long serialVersionUID = 2592626835485763593L;

	private static final int DEFAULT_PAGE_NO = 1;

	private static final int DEFAULT_PAGE_SIZE = 10;

	private int pageNo;

	private int pageSize;

	private int totalRecords;

	private List<T> results;

	private Map<String, Object> params;

	private boolean isCountRecords = true; // 是否统计总数 默认为true

	public Page() {
		this(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE, true);
	}

	public Page(int pageNo, int pageSize, List<T> results, Map<String, Object> params, boolean isCountRecords) {
		this.pageNo = pageNo < 1 ? 1 : pageNo;
		this.pageSize = pageSize < 1 ? 1 : pageSize;
		this.results = results;
		this.params = params;
		this.setCountRecords(isCountRecords);
	}

	public Page(int pageNo, int pageSize, boolean isCountRecords) {
		this(pageNo, pageSize, new ArrayList<T>(), new HashMap<String, Object>(), isCountRecords);
	}

	public Page(int pageNo, int pageSize, Map<String, Object> params, boolean isCountRecords) {
		this(pageNo, pageSize, new ArrayList<T>(), params, isCountRecords);
	}

	public Page(int pageNo, Map<String, Object> params, boolean isCountRecords) {
		this(pageNo, DEFAULT_PAGE_SIZE, params, isCountRecords);
	}

	public Page(int pageNo, Map<String, Object> params) {
		this(pageNo, DEFAULT_PAGE_SIZE, params, true);
	}

	public Page(Map<String, Object> params, boolean isCountRecords) {
		this(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE, params, isCountRecords);
	}

	public Page<T> addParam(String key, Object value) {
		params = params == null ? new HashMap<String, Object>() : params;
		params.put(key, value);
		return this;
	}

	public boolean isHasPrevious() {
		return pageNo > 1;
	}

	public Page<T> previous() {
		if (isHasPrevious()) {
			return new Page<T>(pageNo - 1, params, isCountRecords);
		}
		return new Page<T>(0, pageSize, isCountRecords); // 第一页
	}

	public boolean isHasNext() {
		return getTotalPage() > pageNo;
	}

	public Page<T> next() {
		if (isHasNext()) {
			return new Page<T>(pageNo + 1, params, isCountRecords);
		}
		return new Page<T>(getTotalPage(), params, isCountRecords);
	}

	public int getResultSize() {
		return results == null ? 0 : results.size();
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public int getTotalPage() {
		if (totalRecords == 0) {
			return 1;
		}
		if (totalRecords % pageSize == 0) {
			return totalRecords / pageSize;
		}
		return (totalRecords / pageSize) + 1;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public boolean isCountRecords() {
		return isCountRecords;
	}

	public void setCountRecords(boolean isCountRecords) {
		this.isCountRecords = isCountRecords;
	}
}
