package com.project2.delivery_system;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class UpdateService extends Service {
	
	private DeliveryApplication delivery;
	private static int DELAY = 10000;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		delivery = (DeliveryApplication)getApplication();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Create Updater to pull friends' status, run in separate thread
	 * to avoid blocking UI thread.
	 */
	class Updater extends Thread {
		Intent intent;
		
		public Updater() {
			super("UpdaterService - Updater");
		}
		
		@Override
		public void run() {
			while (delivery.isServiceRunning()) {
				try {
					// connect to server and put latest statuses into DB
					Thread.sleep(DELAY);
				} catch (InterruptedException ex) {
					delivery.setServiceRunning(false);
				}
			}
		}
	}
}
