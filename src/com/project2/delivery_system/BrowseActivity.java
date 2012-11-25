package com.project2.delivery_system;

import java.util.List;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * This is the browse activity of our application. 
 * @author deyuandeng
 */
public class BrowseActivity extends ListActivity {
		
	private DeliveryApplication deliveryApplication;
	private IntentFilter filter = new IntentFilter(UpdateService.NEW_FOODITEM_INTENT);
	private BroadcastReceiver receiver = new BrowseReceiver();
	private ArrayAdapter<FoodItem> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		deliveryApplication = (DeliveryApplication)getApplication();
	    refreshListView();
	    
	    // Start service to fetch new food items from web server
	    if (deliveryApplication.isServiceRunning() == false)
	    	startService(new Intent(this, UpdateService.class));
	}
	
	@Override
	protected void onResume() {
		deliveryApplication.getFoodDataSource().open();
		super.registerReceiver(receiver, filter, null, null);
		super.onResume();
	}

	@Override
	protected void onPause() {
		deliveryApplication.getFoodDataSource().close();
		super.unregisterReceiver(receiver);
		super.onPause();
	}
	
	private void refreshListView() {
		// Get all food items from local database, and use the SimpleCursorAdapter
		// to show the elements in a ListView
		List<FoodItem> foodItems = deliveryApplication.getFoodDataSource().getAllFoodItems();
	    adapter = new ArrayAdapter<FoodItem>(this, android.R.layout.simple_list_item_1, foodItems);
	    setListAdapter(adapter);
	}
	
	class BrowseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			refreshListView();
			/* below implementation prompt "illegal statement in fill window"
			// re-query to refresh cursor
			cursor.requery();
			// notify adapter that underlying data has changed
			adapter.notifyDataSetChanged();
			 */
		}
	}
}