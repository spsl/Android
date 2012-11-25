package com.project2.delivery_system;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * This is the browse activity of our application. 
 * @author deyuandeng
 */
public class BrowseActivity extends Activity {
		
	private static final String[] FROM = { MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_PRICE };
	private static final int[] TO = { R.id.textName, R.id.textPrice };
	private DeliveryApplication deliveryApplication;
	private IntentFilter filter = new IntentFilter(UpdateService.NEW_FOODITEM_INTENT);
	private BroadcastReceiver receiver = new BrowseReceiver();
	private SimpleCursorAdapter adapter;
	private Cursor cursor;
	private ListView listView;

	/**
	 * Called when browse activity is created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		deliveryApplication = (DeliveryApplication)getApplication();
		listView = (ListView)findViewById(R.id.list);
		//adapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, FROM, TO);
//		listView.setAdapter(adapter);
		
		setupListView();
	    
	    // Start service to fetch new food items from web server
	    if (deliveryApplication.isServiceRunning() == false)
	    	startService(new Intent(this, UpdateService.class));
	}
	
	/**
	 * Called when browse activity is resumed, every path
	 * to running state will go through onResume()
	 */
	@Override
	protected void onResume() {
		deliveryApplication.getFoodDataSource().open();
		setupListView();
		super.registerReceiver(receiver, filter, null, null);
		super.onResume();
	}

	/**
	 * Called when browse activity is paused, every path
	 * to stop state will go through onPause()
	 */
	@Override
	protected void onPause() {
		deliveryApplication.getFoodDataSource().close();
		super.unregisterReceiver(receiver);
		super.onPause();
	}
	
	/**
	 * Refresh entire list view in screen
	 */
	private void setupListView() {
		// Get all food items from local database, and use the SimpleCursorAdapter
		// to show the elements in a ListView

		//List<FoodItem> foodItems = deliveryApplication.getFoodDataSource().getAllFoodItems();
//		adapter = new ArrayAdapter<FoodItem>(this, android.R.layout.simple_list_item_1, foodItems);
//	    setListAdapter(adapter);
		
		// Get the data
		cursor = deliveryApplication.getFoodDataSource().getFoodItemCursor();
		startManagingCursor(cursor);
		// Setup Adapter
		adapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, FROM, TO);
		listView.setAdapter(adapter);
	}
	
	/**
	 * Helper class, onReceive() is called whenever there is new food item
	 * @author deyuandeng
	 *
	 */
	class BrowseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			//refreshListView();
			//below implementation prompt "illegal statement in fill window"
			// re-query to refresh cursor
			cursor.requery();
			// notify adapter that underlying data has changed
			adapter.notifyDataSetChanged();
			//*/
		}
	}
}