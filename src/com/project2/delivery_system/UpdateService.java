package com.project2.delivery_system;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * This class periodically get content from web server, with
 * the help of WebAccessor class.
 * @author deyuandeng
 */
public class UpdateService extends Service {
	
	private static int DELAY = 15000;
	private DeliveryApplication delivery;
	private Updater updater;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		delivery = (DeliveryApplication)getApplication();
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
				try {
					delivery.getWebAccessor().getAllFoodItems();
					delivery.getWebAccessor().getAllWebOrders(delivery.getUser());
					
					Thread.sleep(DELAY);
				} catch (InterruptedException ex) {
					delivery.setServiceRunning(false);
				}
			}
		}
	}
}

