package com.project2.delivery_system;

import com.project2.delivery_system.DeliveryApplication.Identity;
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
	
	// `ITEM_FROM` and `ITEM_TO` map database to list view for listAdapter
	private static final String[] ITEM_FROM = 
		{ MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_PRICE };
	private static final int[] ITEM_TO = 
		{ R.id.textName, R.id.textPrice };
	// `ORDER_FROM` and `ORDER_TO` map database to list view for orderAdapter
	private static final String[] ORDER_FROM = 
		{ MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_STATUS };
	private static final int[] ORDER_TO = 
		{ R.id.textOrderID, R.id.textStatus };	
	private DeliveryApplication deliveryApplication;
	private IntentFilter filter = new IntentFilter(UpdateService.NEW_INFO_INTENT);
	private BroadcastReceiver receiver = new BrowseReceiver();
	private SimpleCursorAdapter listAdapter;
	private SimpleCursorAdapter orderAdapter;
	private Cursor listCursor;
	private Cursor orderCursor;
	private ListView listView1;
	private ListView listView2;

	/**
	 * Called when browse activity is created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		deliveryApplication = (DeliveryApplication)getApplication();
		listView1 = (ListView)findViewById(R.id.itemlist);
		listView2 = (ListView)findViewById(R.id.orderlist);
		listView1.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	                  int position, long id) {
	        	  SQLiteCursor sqLiteCursor = (SQLiteCursor)listView1.getItemAtPosition(position);
	        	  
	        	  //String product = ((TextView) view).getText().toString();
	        	  Toast.makeText(BrowseActivity.this, sqLiteCursor.getString(0) + " " + sqLiteCursor.getString(1) + " " +
	        			  sqLiteCursor.getString(2) + " ", Toast.LENGTH_SHORT).show();
			}
	     });
		listView2.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	                  int position, long id) {
	        	  SQLiteCursor sqLiteCursor = (SQLiteCursor)listView2.getItemAtPosition(position);
	        	  
	        	  //String product = ((TextView) view).getText().toString();
	        	  Toast.makeText(BrowseActivity.this, sqLiteCursor.getString(0) + " " + sqLiteCursor.getString(1) + " " +
	        			  sqLiteCursor.getString(2) + " ", Toast.LENGTH_SHORT).show();
			}
	     });	
		setupListView();
	    
		//***************this should be set in login activity*************
		deliveryApplication.setUser("ddysher");
		deliveryApplication.setIdentity(Identity.CUSTOMER);
		
	    // Start service ITEM_TO fetch new food items ITEM_FROM web server
	    if (deliveryApplication.isServiceRunning() == false)
	    	startService(new Intent(this, UpdateService.class));
	}
	
	/**
	 * Called when browse activity is resumed, every path
	 * ITEM_TO running state will go through onResume()
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
	 * ITEM_TO stop state will go through onPause()
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
		// Get all food items ITEM_FROM local database, and use the SimpleCursorAdapter
		// ITEM_TO show the elements in a ListView
		listCursor = deliveryApplication.getFoodDataSource().getFoodItemCursor();
		startManagingCursor(listCursor);
		listAdapter = new SimpleCursorAdapter(this, R.layout.list_row, listCursor, ITEM_FROM, ITEM_TO);
		listView1.setAdapter(listAdapter);
		
		orderCursor = deliveryApplication.getFoodDataSource().getOrderCursor();
		startManagingCursor(orderCursor);
		orderAdapter = new SimpleCursorAdapter(this, R.layout.order_row, orderCursor, ORDER_FROM, ORDER_TO);
		listView2.setAdapter(orderAdapter);
	}
	
	/**
	 * Helper class, onReceive() is called whenever there is new food item or new order
	 * @author deyuandeng
	 */
	class BrowseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// re-query ITEM_TO refresh listCursor
			listCursor.requery();
			// notify listAdapter that underlying data has changed
			listAdapter.notifyDataSetChanged();
				
			orderCursor.requery();
			orderAdapter.notifyDataSetChanged();
		}
	}
}