package com.project2.delivery_system;

import android.app.Application;

public class DeliveryApplication extends Application {

	private boolean serviceRunning;		// indicate whether update is running
	//private FoodDataBase foodDataBase;	// food database for entire application
	
	@Override
	public void onCreate() {
	//	foodDataBase = new FoodDataBase(this);
	}
	
	// Getter of serviceRunning
	public boolean isServiceRunning() {
		return serviceRunning;
	}
	
	// Setter of serviceRunning
	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}
	
	// Getter of foodDataBase
//	public FoodDataBase getFoodDataBase() {
//		return foodDataBase;
//	}
}