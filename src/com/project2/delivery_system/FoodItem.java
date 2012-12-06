package com.project2.delivery_system;


/**
 * Represent one food item in application
 * @author deyuandeng
 */
public class FoodItem {
	private String id;
	private String name;
	private String price;
	private String filePath;
	
	public FoodItem() {
	}
	
	public FoodItem(String id, String name, String price, String filePath) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.filePath = filePath;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}	
}
