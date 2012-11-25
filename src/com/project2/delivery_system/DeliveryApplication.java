package com.project2.delivery_system;

import android.app.Application;

public class DeliveryApplication extends Application {

	private boolean serviceRunning;			// indicate whether update is running
	private FoodDataSource foodDataSource;	// food database for entire application
	
	@Override
	public void onCreate() {
		foodDataSource = new FoodDataSource(this);
		foodDataSource.open();		// open database when application is started
	}
	
	// Getter of serviceRunning
	public boolean isServiceRunning() {
		return serviceRunning;
	}
	
	// Setter of serviceRunning
	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}
	
	// Getter of foodDataSource
	public FoodDataSource getFoodDataSource() {
		return foodDataSource;
	}
}
