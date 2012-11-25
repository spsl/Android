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
	
	public static final String NEW_FOODITEM_INTENT = 
			"com.project2.delivery_system.NEW_FOODITEM";
	public static final String NEW_FOODITEM_EXTRA_COUNT = 
			"com.project2.delivery_system.EXTRA_COUNT";
	public static final String RECEIVE_TIMELINE_NOTIFICATIONS = 
			"com.project2.delivery_system.RECEIVE_TIMELINE_NOTIFICATIONS";
	private static int DELAY = 60000;
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

	// Called each time the service receives startService() intent.
	// A service can receive multiple start service request, each
	// will call this.
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		delivery.setServiceRunning(true);
		updater.start();
		return START_STICKY;
	}

	/**
	 * Create Updater to pull friends' status, run in separate thread
	 * to avoid blocking UI thread.
	 */
	class Updater extends Thread {
		Intent intent;
		
		public Updater() {
			super("UpdateService - Updater");
		}
		
		@Override
		public void run() {
			long newFoodItems = 0;	// count number of new food items
			
			while (delivery.isServiceRunning()) {
				try {
					// Connect to server and put latest statuses into DB
					ArrayList<FoodItem> foodItems = (ArrayList<FoodItem>)webAccessor.getAllWebFoodItems();
					
					for (FoodItem foodItem : foodItems) {
						if (delivery.getFoodDataSource().addFoodItem(foodItem) != 0)
							newFoodItems++;
					}
					
					if (newFoodItems > 0) {
						intent = new Intent(NEW_FOODITEM_INTENT);
						intent.putExtra(NEW_FOODITEM_EXTRA_COUNT, newFoodItems);
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

