package com.project2.delivery_system;


/**
 * Represent one food item in application
 * @author deyuandeng
 */
public class Order {
    
	// order is placed by user, but not confirmed by provider
    public static final String STATUS_PENDING = "Pending";
    // order is confirmed by provider, but not by courier
    public static final String STATUS_PROV_CONFIRMED = "Provider Confirmed";
    // order is confirmed by courier, but not confirmed by user
    public static final String STATUS_COUR_CONFIRMED = "Courier Confirmed";
	// order is completed, namely, user and courier completed transaction
    public static final String STATUS_CLOSED = "Order Completed";
    
	private String id;
	private String user;
	private String status;
	private String itemID;

	public Order() {
		
	}
	
	public Order(String id, String status, String user, String itemID) {
		this.id = id;
		this.user = user;
		this.status = status;
		this.itemID = itemID;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
}
