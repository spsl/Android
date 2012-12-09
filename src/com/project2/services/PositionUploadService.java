package com.project2.services;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.project2.delivery_system.DeliveryApplication;
import com.project2.utilities.Order;


/**
 * This class periodically get position from google map api and upload
 * the location of the device if the identity is courier and one of the 
 * order is in the state of COURIER_CONFIRMED
 */
public class PositionUploadService extends Service {
    
    private static int SAMPLE_RATE_MS = 30000;	// update location info time interval
    private DeliveryApplication delivery;
    private Location recentLocation;
    private Cursor orderCursor;     // cursor that manage order list
    
    @Override
    public void onCreate() {
        delivery = (DeliveryApplication)getApplication();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(orderCursor!=null){
            orderCursor.close();
        }
        // only courier upload position data
        if(delivery.getIdentity()==DeliveryApplication.Identity.COURIER){
        
            // get the position for the first time
            String gpsProvider = delivery.getLocationManager().getBestProvider(new Criteria(), true);
            
            if (gpsProvider == null) {
                // print error message
                popViewMessage("No GPS device!");
            }
            
            recentLocation = delivery.getLocationManager().getLastKnownLocation(gpsProvider);
            
            updateOrderLocations(recentLocation, orderCursor);
            
            delivery.getLocationManager().requestLocationUpdates(SAMPLE_RATE_MS, 0, new Criteria(),
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                        	if (delivery.isLocationUpdateRunning() == false) {
                        		delivery.getLocationManager().removeUpdates(this);
                        		return;
                        	}
                            orderCursor = delivery.getWebAccessor().getOrderCursor(
                            		DeliveryApplication.GET_ALL_ORDERS, delivery.getIdentity());
                            
                            if (orderCursor == null) {
                                // cannot connect to data base print some error message
                                popViewMessage("No data base cursor!");
                            }
                            else
                                updateOrderLocations(recentLocation,orderCursor);
                        }
                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                        @Override
                        public void onProviderEnabled(String provider) {

                        }
                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            
                        }
                    },
                    null);
        }
        
        return super.onStartCommand(intent, flags, startId);
    }
    
    /**
     * Once binded, listen to location manager 
        check if we need to upload our position
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    public void updateOrderLocations(Location loc, Cursor cursor){
        if (cursor != null && loc != null) {
            int column_status=cursor.getColumnIndex("order_status");
            int column_id=cursor.getColumnIndex("_id");
            
            // have this column
            if (column_status != -1 && column_id != -1) {
                
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    // only update location for orders of STATUS_COUR_CONFIRMED
                    if(cursor.getString(column_status)
                            .contentEquals(Order.STATUS_COUR_CONFIRMED)){
                       
                        new Uploader().execute(cursor.getString(column_id), 
                                String.valueOf(loc.getLatitude()*1e6),
                                String.valueOf(loc.getLongitude()*1e6)
                                );
                    }
                    cursor.moveToNext();
                }
            }
        }     
    }
    
    private void popViewMessage(String message){
        Toast.makeText(delivery, message, Toast.LENGTH_LONG).show();
        return;
    }
    
    class Uploader extends AsyncTask<String, Integer, String> {

        // doInBackground() is the callback that specifies the actual work to be
        // done on the separate thread, as if it's executing in the background.
        @Override
        protected String doInBackground(String... in) {
            try {
                delivery.getWebAccessor().uploadOrderLocation(
                        in[0], 
                        in[1],
                        in[2]
                        );
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;    
        }

        // onProgressUpdate() is called whenever there's progress in the task
        // execution. The progress should be reported from the doInBackground() call.
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        // onPostExecute() is called when our task completes. This is our
        // callback method to update the user interface and tell the user 
        // that the task is done.
        @Override
        protected void onPostExecute(String result) {
        }
    }
}
