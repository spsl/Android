package com.project2.delivery_system;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * This is the browse activity of our application. 
 * @author deyuandeng
 */
public class BrowseActivity extends Activity {
	
	// `FROM` and `TO` map database to list view for adapter
	private static final String[] FROM = 
		{ MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_PRICE };
	private static final int[] TO = 
		{ R.id.textName, R.id.textPrice };
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
		listView.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	                  int position, long id) {
	        	  SQLiteCursor sqLiteCursor = (SQLiteCursor)listView.getItemAtPosition(position);
	        	  
	        	  //String product = ((TextView) view).getText().toString();
	        	  Toast.makeText(BrowseActivity.this, sqLiteCursor.getString(0) + " " + sqLiteCursor.getString(1) + " " +
	        			  sqLiteCursor.getString(2) + " ", Toast.LENGTH_SHORT).show();
			}
	     });
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
			
			// re-query to refresh cursor
			cursor.requery();
			// notify adapter that underlying data has changed
			adapter.notifyDataSetChanged();
		}
	}
}