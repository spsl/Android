package com.project2.delivery_system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

	
public class WebAccessor {

	// for debugging
	//private static String FOOD_ITEM_URI = "http://10.0.2.2:8090/fooditems";
	//private static String ORDER_URI = "http://10.0.2.2:8090/orders";
	//private static String USER_URI = "http://10.0.2.2:8090/users";
	public static final String NEW_INFO_INTENT = 
			"com.project2.delivery_system.NEW_INFO";
	public static final String NEW_INFO_EXTRA = 
			"com.project2.delivery_system.EXTRA";
	public static final String NEW_ITEM = 
			"com.project2.delivery_system.ITEM";
	public static final String NEW_ORDER = 
			"com.project2.delivery_system.ORDER";
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private Context context;
	private static String FOOD_ITEM_URI = "http://18641datastore.appspot.com/fooditems";
	private static String ORDER_URI = "http://18641datastore.appspot.com/orders";
	// private static String USER_URI = "http://18641datastore.appspot.com/users";

	
	public WebAccessor(Context context) {
		dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase();
		this.context = context;
	}
	
	public void open() {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		database.close();
	}

	/**
	 * Get all web food items from server, update local database, then list view
	 */
	public void getAllFoodItems() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(FOOD_ITEM_URI);
		int newFoodItems = 0;

		try {
			String line;
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			while ((line = reader.readLine()) != null) {
				ContentValues values = new ContentValues();
				String[] itemStrings = line.split(";");

				values.put(MySQLiteHelper.COLUMN_ID, itemStrings[0]);
				values.put(MySQLiteHelper.COLUMN_NAME, itemStrings[1]);
				values.put(MySQLiteHelper.COLUMN_PRICE, itemStrings[2]);
				if (database.insertWithOnConflict(MySQLiteHelper.TABLE_FOODITMES,
						null, values, SQLiteDatabase.CONFLICT_IGNORE) != 0)
					newFoodItems++;
			}

            if (newFoodItems > 0) {
                Intent intent = new Intent(NEW_INFO_INTENT);
                intent.putExtra(NEW_INFO_EXTRA, NEW_ITEM);
                // In delivery application, browse activity will take action to this intent
                context.sendBroadcast(intent);
            }
			reader.close();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all orders of current user from web, update local database, then
	 * update order view
	 */
	public void getAllWebOrders(String orderUser) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(ORDER_URI + "?orderUser=" + orderUser);
		int newOrders = 0;

		try {
			String line;
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			while ((line = reader.readLine()) != null) {
				String[] orderStrings = line.split(";");
				ContentValues values = new ContentValues();

				values.put(MySQLiteHelper.COLUMN_ID, orderStrings[0]);
				values.put(MySQLiteHelper.COLUMN_STATUS, orderStrings[1]);
				values.put(MySQLiteHelper.COLUMN_USER, orderStrings[2]);

				if (database.insertWithOnConflict(MySQLiteHelper.TABLE_ORDERS,
						null, values, SQLiteDatabase.CONFLICT_IGNORE) != 0)
					newOrders++;
			}
			
            if (newOrders > 0) {
                Intent intent = new Intent(NEW_INFO_INTENT);
                intent.putExtra(NEW_INFO_EXTRA, NEW_ORDER);
                // In delivery application, browse activity will take action to this intent
                context.sendBroadcast(intent);
            }
			reader.close();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a food item to server, update local database, then update list view
	 */
	public void addFoodItem(FoodItem foodItem) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(FOOD_ITEM_URI);
		ContentValues values = new ContentValues();

		try {
			// Post to server
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("itemID", foodItem
					.getId()));
			nameValuePairs.add(new BasicNameValuePair("itemName", foodItem
					.getName()));
			nameValuePairs.add(new BasicNameValuePair("itemPrice", foodItem
					.getPrice()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);

			// Update local database
			values.put(MySQLiteHelper.COLUMN_ID, foodItem.getId());
			values.put(MySQLiteHelper.COLUMN_NAME, foodItem.getName());
			values.put(MySQLiteHelper.COLUMN_PRICE, foodItem.getPrice());
			database.insertWithOnConflict(MySQLiteHelper.TABLE_FOODITMES, null,
					values, SQLiteDatabase.CONFLICT_IGNORE);

			// Update item list
            Intent intent = new Intent(NEW_INFO_INTENT);
            intent.putExtra(NEW_INFO_EXTRA, NEW_ITEM);
            context.sendBroadcast(intent);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add an order to server, update local database, then list view
	 */
	public void addOrder(String orderUser) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ORDER_URI);
		ContentValues values = new ContentValues();

		try {
			// Post to server
			String orderID = String.valueOf(new Random().nextInt(100000));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("orderID", orderID));
			nameValuePairs.add(new BasicNameValuePair("orderStatue", "Pending"));
			nameValuePairs.add(new BasicNameValuePair("orderUser", orderUser));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);

			// Update local database
			values.put(MySQLiteHelper.COLUMN_ID, orderID);
			values.put(MySQLiteHelper.COLUMN_STATUS, "Pending");
			values.put(MySQLiteHelper.COLUMN_USER, orderUser);
			database.insertWithOnConflict(MySQLiteHelper.TABLE_ORDERS, null,
					values, SQLiteDatabase.CONFLICT_IGNORE);
			
			// Update order list
            Intent intent = new Intent(NEW_INFO_INTENT);
            intent.putExtra(NEW_INFO_EXTRA, NEW_ORDER);
            context.sendBroadcast(intent);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Get cursor of database, point to the first row 
     * @return
     */
    public Cursor getFoodItemCursor() {
            
            return database.query(MySQLiteHelper.TABLE_FOODITMES,
                            null, null, null, null, null, MySQLiteHelper.GET_ALL_ORDER_BY); 
    }
    
    /**
     * Get cursor of database, point to the first row 
     * @return
     */
    public Cursor getOrderCursor() {
            
            return database.query(MySQLiteHelper.TABLE_ORDERS,
                            null, null, null, null, null, MySQLiteHelper.GET_ALL_ORDER_BY); 
    }
    
    
	public void orderProviderConfirm(String orderID) {

	}

	
	public void orderDeliveryConfirm(String orderID) {

	}

	
	public void orderTransactionConfirm(String orderID) {

	}
}
