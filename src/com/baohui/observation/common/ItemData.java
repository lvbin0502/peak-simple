/**
 * 
 */
package com.baohui.observation.common;

/**
 * @author lvbin 2015-8-12
 */
public class ItemData {

	private String plantform;
	private String storeName;
	private String itemId;
	private Double price;
	private Integer sales;

	public String getPlantform() {
		return plantform;
	}

	public void setPlantform(String plantform) {
		this.plantform = plantform;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
	}

	public ItemData() {
	}

	@Override
	public String toString() {
		return "ItemData [itemId=" + itemId + ", plantform=" + plantform
				+ ", price=" + price + ", sales=" + sales + ", storeName="
				+ storeName + "]";
	}

}
