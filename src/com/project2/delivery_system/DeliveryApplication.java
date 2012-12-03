package com.project2.delivery_system;

import android.app.Application;

public class DeliveryApplication extends Application {

	private String user;			// user of current application
	private Identity identity;		// user identity of current application
	private boolean serviceRunning;			// indicate whether update is running
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
