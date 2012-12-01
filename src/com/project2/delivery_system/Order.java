package com.project2.delivery_system;


/**
 * Represent one food item in application
 * @author deyuandeng
 */
public class Order {
    
    final int STATUS_INITIAL=0;
    final int STATUS_PROV_CONFIRMED=1;
    final int STATUS_COUR_CONFIRMED=2;
    final int STATUS_CLOSED=3;
    
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
}
