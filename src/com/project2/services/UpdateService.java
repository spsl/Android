package com.project2.services;

import com.project2.delivery_system.DeliveryApplication;
import com.project2.delivery_system.DeliveryApplication.Identity;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.IBinder;



/**
 * This class periodically get content from web server, with
 * the help of WebAccessor class.
 * @author deyuandeng
 */
public class UpdateService extends Service {
	
	private static int DELAY = 30000;	// 1min period
	private DeliveryApplication delivery;
	private Updater updater;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		delivery = (DeliveryApplication)getApplication();
		updater = new Updater();
	}

	/**
	 * Unused in our application.
	 */
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
			try {
				while (delivery.isServiceRunning()) {	
					boolean statusUpdated = false;
					
					delivery.getWebAccessor().getAllFoodItems();
					// Get all food items, and all orders of current user. Callee methods
					// will update database and list view if needed.
					if (delivery.getIdentity() == Identity.COURIER || delivery.getIdentity() == Identity.PROVIDER)
						statusUpdated = delivery.getWebAccessor().getAllWebOrders(
								DeliveryApplication.GET_ALL_ORDERS, 
								delivery.getIdentity());
					else if (delivery.getIdentity() == Identity.CUSTOMER)
						statusUpdated = delivery.getWebAccessor().getAllWebOrders(delivery.getUser(),
								delivery.getIdentity());

					if (statusUpdated == true) {
					    try {
					        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					        Ringtone r = RingtoneManager.getRingtone(delivery, notification);
					        r.play();
							final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
							tg.startTone(ToneGenerator.TONE_PROP_BEEP);
					    } catch (Exception e) {
					    	
					    }		
					}
					Thread.sleep(DELAY);
				}
			} catch (InterruptedException ex) {
					delivery.setServiceRunning(false);
			}
		}
	}
}

