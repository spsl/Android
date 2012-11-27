package com.project2.delivery_system;

import java.util.ArrayList;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * This class periodically get content from web server, with
 * the help of WebAccessor class.
 * @author deyuandeng
 */
public class UpdateService extends Service {
	
	public static final String NEW_INFO_INTENT = 
			"com.project2.delivery_system.NEW_INFO";
	public static final String NEW_INFO_EXTRA_COUNT = 
			"com.project2.delivery_system.EXTRA_COUNT";
	private static int DELAY = 15000;
	private DeliveryApplication delivery;
	private Updater updater;
	private WebAccessor webAccessor;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		delivery = (DeliveryApplication)getApplication();
		webAccessor = new WebAccessor();
		updater = new Updater();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Called each time the service receives startService() intent. A service
	 * can receive multiple start service request, each will call this.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		delivery.setServiceRunning(true);
		updater.start();
		return START_STICKY;
	}

	/**
	 * Create Updater to pull all , run in separate thread to avoid blocking
	 * UI thread.
	 */
	class Updater extends Thread {
		Intent intent;
		
		public Updater() {
			super("UpdateService - Updater");
		}
		
		@Override
		public void run() {		
			while (delivery.isServiceRunning()) {
				long newCount = 0;	// count number of new food items or orders
				try {
					ArrayList<FoodItem> foodItems = (ArrayList<FoodItem>)webAccessor.getAllWebFoodItems();	
					ArrayList<Order> orders = (ArrayList<Order>)webAccessor.getAllWebOrders(delivery.getUser());
							
					for (FoodItem foodItem : foodItems) {
						if (delivery.getFoodDataSource().addFoodItem(foodItem) != 0)
							newCount++;
					}
						
					for (Order order : orders) {
						if (delivery.getFoodDataSource().addOrder(order) != 0)
							newCount++;
					}
						
					if (newCount > 0) {
						intent = new Intent(NEW_INFO_INTENT);
						intent.putExtra(NEW_INFO_EXTRA_COUNT, newCount);
						// In delivery application, browse activity will take action to this intent 
						UpdateService.this.sendBroadcast(intent);
					}
					
					Thread.sleep(DELAY);
				} catch (InterruptedException ex) {
					delivery.setServiceRunning(false);
				}
			}
		}
	}
}

