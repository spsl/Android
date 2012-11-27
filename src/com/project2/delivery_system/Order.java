package com.project2.delivery_system;


/**
 * Represent one food item in application
 * @author deyuandeng
 */
public class Order {
	private String id;
	private String user;
	private String status;

	public Order() {
		
	}
	
	public Order(String id, String status, String user) {
		this.id = id;
		this.user = user;
		this.status = status;
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

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return user + " " + status;
	}
}
