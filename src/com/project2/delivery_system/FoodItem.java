package com.project2.delivery_system;


/**
 * Represent one food item in application
 * @author deyuandeng
 */
public class FoodItem {
	private long id;	// `id` is added by database
	private long createAt;
	private String name;
	private long price;

	public FoodItem() {
		
	}
	
	public FoodItem(long createAt, String name, long price) {
		this.name = name;
		this.price = price;
		this.createAt = createAt;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreateAt() {
		return createAt;
	}
	
	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name + " " + price;
	}
}
