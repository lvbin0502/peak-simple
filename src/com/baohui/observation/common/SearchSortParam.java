/**
 * 
 */
package com.baohui.observation.common;

/**
 * 查询结果排序参数
 * 
 * @author lvbin 2015-8-12
 */
public enum SearchSortParam {
	
	PRICE_ASC("price-asc"),
	PRICE_DESC("price-desc"),
	PRICE_TOTAL_ASC("total-asc"),
	PRICE_TOTAL_DESC("total-desc"),
	DEFAULT("default"),
	RENQI("renqi-desc"),
	SALES("sale-desc");

	private String param;
	
	private SearchSortParam(String param) {
		this.param = param;
	}
	
	public String getParam() {
		return param;
	}
}
