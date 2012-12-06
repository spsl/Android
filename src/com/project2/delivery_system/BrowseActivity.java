package com.project2.delivery_system;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

/**
 * This is the browse activity of our application. 
 * @author deyuandeng
 */
public class BrowseActivity extends Activity {
	
	// `ITEM_FROM` and `ITEM_TO` map database to list view for listAdapter
	private static final String[] ITEM_FROM = 
		{ MySQLiteHelper.COLUMN_ITEMNAME, MySQLiteHelper.COLUMN_ITEMPRICE, MySQLiteHelper.COLUMN_ITEMPRICE };
	private static final int[] ITEM_TO = 
		{ R.id.textName, R.id.textPrice, R.id.imageViewFood };
	// `ORDER_FROM` and `ORDER_TO` map database to list view for orderAdapter
	private static final String[] ORDER_FROM = 
		{ MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_ORDERSTATUS };
	private static final int[] ORDER_TO = 
		{ R.id.textOrderID, R.id.textStatus };
	private DeliveryApplication delivery;
	private IntentFilter filter = new IntentFilter(WebAccessor.NEW_INFO_INTENT);
	private BroadcastReceiver receiver = new BrowseReceiver();
	private SimpleCursorAdapter listAdapter;
	private SimpleCursorAdapter orderAdapter;
	private Cursor listCursor;		// cursor that manage item list
	private Cursor orderCursor;		// cursor that manage order list
	private ListView itemListView;	// item list view
	private ListView orderListView;	// order list view
	private Button uploadButton;	// update a food item
	private ProgressDialog progressDialog;
	private ViewBinder VIEW_BINDER = new ViewBinder() {
		// called for each data element that needs to be bound to a particular view
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() == R.id.imageViewFood) {
				ImageView foodImageView = (ImageView) view;
				byte[] image = cursor.getBlob(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ITEMPICTURE));
				if (image != null) {
					Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
					foodImageView.setImageBitmap(bmp);
					return true;
				} else {
					return false;		
				}
			}
			return false;
		}
	};
	
	/**
	 * Called when browse activity is created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		delivery = (DeliveryApplication)getApplication();
		itemListView = (ListView)findViewById(R.id.itemlist);
		orderListView = (ListView)findViewById(R.id.orderlist);

		uploadButton = (Button)findViewById(R.id.buttonUpload);
		uploadButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {		// when upload button is clicked
			    startActivity(new Intent(BrowseActivity.this, UploadFoodItemActivity.class));
			}
		});
		itemListView.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	                  int position, long id) {	// when food item is clicked
	        	  SQLiteCursor sqLiteCursor = (SQLiteCursor)itemListView.getItemAtPosition(position);
	        	  
	        	  Intent intent = new Intent(BrowseActivity.this, DetailViewActivity.class);
	        	  Bundle bundle = new Bundle();
	        	  bundle.putString("itemID", sqLiteCursor.getString(0));
	        	  bundle.putString("itemName", sqLiteCursor.getString(1));
	        	  bundle.putString("itemPrice", sqLiteCursor.getString(2));
	        	  intent.putExtras(bundle);
	        	  startActivity(intent);
			}
	     });
		orderListView.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	                  int position, long id) {	// when order is clicked
 	        	  SQLiteCursor sqLiteCursor = (SQLiteCursor)orderListView.getItemAtPosition(position);
	              
	              Intent intent = new Intent(BrowseActivity.this, OrderViewActivity.class );
	              Bundle bundle = new Bundle();
                  bundle.putString("orderID", sqLiteCursor.getString(0));
                  bundle.putString("orderStatus", sqLiteCursor.getString(1));
                  bundle.putString("orderUser", sqLiteCursor.getString(2));
                  intent.putExtras(bundle);
	              startActivity(intent);
			}
	     });
		
		progressDialog = ProgressDialog.show(BrowseActivity.this, "Processing...", 
				"Loading...", true, false);
		new Uploader().execute();

	    // Start service to fetch new food items from web server
	    if (delivery.isServiceRunning() == false)
	    	startService(new Intent(this, UpdateService.class));
	}
	
	/**
	 * Called when browse activity is resumed, every path
	 * to running state will go through onResume()
	 */
	@Override
	protected void onResume() {
		setupListView();
		super.registerReceiver(receiver, filter, null, null);
		setupComponentsAccordingToIdentity();
		super.onResume();
	}

	/**
	 * Called when browse activity is paused, every path
	 * to stop state will go through onPause()
	 */
	@Override
	protected void onPause() {
		super.unregisterReceiver(receiver);
		super.onPause();
	}
	
	/**
	 * Refresh entire list view in screen
	 */
	private void setupListView() {
		// Get all food items ITEM_FROM local database, and use the SimpleCursorAdapter
		// ITEM_TO show the elements in a ListView
		listCursor = delivery.getWebAccessor().getFoodItemCursor();
		startManagingCursor(listCursor);
		listAdapter = new SimpleCursorAdapter(this, R.layout.list_row, listCursor, ITEM_FROM, ITEM_TO);
		listAdapter.setViewBinder(VIEW_BINDER);
		itemListView.setAdapter(listAdapter);
		
		orderCursor = delivery.getWebAccessor().getOrderCursor(delivery.getUser());
		startManagingCursor(orderCursor);
		orderAdapter = new SimpleCursorAdapter(this, R.layout.order_row, orderCursor, ORDER_FROM, ORDER_TO);
		orderListView.setAdapter(orderAdapter);
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
	
	/***
	 * Asynchronously posts to server, avoid blocking UI thread. The first data
	 * type is used by doInBackground, the second by onProgressUpdate, and the
	 * third by onPostExecute.
	 */
	class Uploader extends AsyncTask<String, Integer, String> {

		// doInBackground() is the callback that specifies the actual work to be
		// done on the separate thread, as if it's executing in the background.
		@Override
		protected String doInBackground(String... user) {
			try {
				delivery.getWebAccessor().getAllFoodItems();
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
			progressDialog.dismiss();
		}
	}
	
    // set up interfaces according to user identity
	private void setupComponentsAccordingToIdentity(){
	    if(delivery.getIdentity() == DeliveryApplication.Identity.PROVIDER){
            uploadButton.setEnabled(true);
            uploadButton.setVisibility(View.VISIBLE);
	    }
	    else{
	        uploadButton.setEnabled(false);
            uploadButton.setVisibility(View.INVISIBLE);
	    } 
	}
}
