package com.project2.delivery_system;

import android.app.Application;

public class DeliveryApplication extends Application {

	public static final int WEB_ERROR = -1;
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAIL = 2;
	public static final int SIGNUP_SUCCESS = 3;
	public static final int SIGNUP_FAIL = 4;
	public static final int GET_ALL_FOODITEMS_SUCCESS = 5;
	public static final int GET_ALL_FOODITEMS_FAIL = 6;
	public static final int GET_ALL_ORDERS_SUCCESS = 7;
	public static final int GET_ALL_ORDERS_FAIL = 8;
	public static final int ADD_ORDER_SUCCESS = 9;
	public static final int ADD_ORDER_FAIL = 10;
	public static final String GET_ALL_ORDERS = "get_all_orders";
	
	private String user;			// user of current application
	private Identity identity;		// user identity of current application
	private boolean serviceRunning;	// indicate whether update is running
	private WebAccessor webAccessor;
	
	enum Identity {
		CUSTOMER,
		PROVIDER,
		COURIER,
	};
	
	@Override
	public void onCreate() {
		webAccessor = new WebAccessor(this);
	}
	
	// Getter of user
	public String getUser() {
		return user;
	}
	
	// Setter of user
	public void setUser(String user) {
		this.user = user;
	}
	
	// Getter of identity
	public Identity getIdentity() {
		return identity;
	}
	
	// Setter of user
	public void setIdentity(Identity identity) {
		this.identity= identity;
	}
	
	// Getter of serviceRunning
	public boolean isServiceRunning() {
		return serviceRunning;
	}
	
	// Setter of serviceRunning
	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}
	
	// Getter of webAccessor
	public WebAccessor getWebAccessor() {
		return webAccessor;
	}
}
