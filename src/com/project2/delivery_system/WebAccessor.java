package com.project2.delivery_system;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.ToneGenerator;

import com.project2.delivery_system.DeliveryApplication.Identity;

	
public class WebAccessor {

	// for debugging
	//private static String FOOD_ITEM_URI = "http://10.0.2.2:8090/fooditems";
	//private static String ORDER_URI = "http://10.0.2.2:8090/orders";
	//private static String USER_URI = "http://10.0.2.2:8090/users";
	//private static String IMAGE_URI = "http://10.0.2.2:8090/images";
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
	private static String USER_URI = "http://18641datastore.appspot.com/users";
	private static String IMAGE_URI = "http://18641datastore.appspot.com/images";
	private static String LOCATION_URI = "http://18641datastore.appspot.com/location";
	
	
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
	
	public void delete() {
		database.delete(MySQLiteHelper.TABLE_FOODITMES, null, null); 
		database.delete(MySQLiteHelper.TABLE_ORDERS, null, null); 
	}

	/**
	 * Get all web food items from server, update local database, then list view
	 */
	public int getAllFoodItems() {
		try {
			String line;
			int newFoodItems = 0;
			
			// Generate GET request for food items
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(FOOD_ITEM_URI);
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			while ((line = reader.readLine()) != null) {	// read server response
				ContentValues values = new ContentValues();
				String[] itemStrings = line.split(";");
				
				// Get image of an item
			    httpGet = new HttpGet(IMAGE_URI + "?itemID=" + URLEncoder.encode(itemStrings[0]));
			    response = httpClient.execute(httpGet);
			    InputStream inputStream = response.getEntity().getContent();
			    ByteArrayOutputStream out = new ByteArrayOutputStream();
			    byte[] data = new byte[1024];
			    int length = 0;
			    while ((length = inputStream.read(data))!=-1) {		// read from input stream as bytes
			    	out.write(data, 0, length);
			    }

			    // Insert into database
				values.put(MySQLiteHelper.COLUMN_ID, itemStrings[0]);
				values.put(MySQLiteHelper.COLUMN_ITEMNAME, itemStrings[1]);
				values.put(MySQLiteHelper.COLUMN_ITEMPRICE, itemStrings[2]);
				values.put(MySQLiteHelper.COLUMN_ITEMPICTURE, out.toByteArray());
				if (database.insertWithOnConflict(MySQLiteHelper.TABLE_FOODITMES,
						null, values, SQLiteDatabase.CONFLICT_IGNORE) != 0)
					newFoodItems++;
			}

            if (newFoodItems > 0) {
                Intent intent = new Intent(NEW_INFO_INTENT);
                intent.putExtra(NEW_INFO_EXTRA, NEW_ITEM);
                context.sendBroadcast(intent);	// send broadcast message to browse activity
            }
			reader.close();
			
			return DeliveryApplication.GET_ALL_FOODITEMS_SUCCESS;

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return DeliveryApplication.GET_ALL_FOODITEMS_FAIL;
	}

	/**
	 * Get all orders of current user from web, update local database, then
	 * update order view
	 */
	public int getAllWebOrders(String orderUser) {		
		try {
			String line;
			int newOrders = 0;
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(ORDER_URI + "?orderUser=" + orderUser);			
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			while ((line = reader.readLine()) != null) {
				String[] orderStrings = line.split(";");
				ContentValues values = new ContentValues();

				values.put(MySQLiteHelper.COLUMN_ID, orderStrings[0]);
				values.put(MySQLiteHelper.COLUMN_ORDERSTATUS, orderStrings[1]);
				values.put(MySQLiteHelper.COLUMN_ORDERUSER, orderStrings[2]);
				if (database.insertWithOnConflict(MySQLiteHelper.TABLE_ORDERS,
						null, values, SQLiteDatabase.CONFLICT_REPLACE) != 0)
					newOrders++;
			}
			
            if (newOrders > 0) {
                Intent intent = new Intent(NEW_INFO_INTENT);
                intent.putExtra(NEW_INFO_EXTRA, NEW_ORDER);
                context.sendBroadcast(intent);
            }
			reader.close();
			
			return DeliveryApplication.GET_ALL_ORDERS_SUCCESS;

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return DeliveryApplication.GET_ALL_ORDERS_FAIL;
	}

	/**
	 * Add a food item to server, update local database, then update list view
	 */
	public void addFoodItem(FoodItem foodItem, byte[] image) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(FOOD_ITEM_URI);
		ContentValues values = new ContentValues();
		
		try {
			MultipartEntity multipartContent = new MultipartEntity();
			// Post to server
			multipartContent.addPart("itemID", new StringBody(foodItem.getId()));
			multipartContent.addPart("itemName", new StringBody(foodItem.getName()));
			multipartContent.addPart("itemPrice", new StringBody(foodItem.getPrice()));
			multipartContent.addPart("itemPicture", 
					new InputStreamBody(new ByteArrayInputStream(image), foodItem.getFilePath()));
			httppost.setEntity(multipartContent);
			httpclient.execute(httppost);
			
			// Update local database
			values.put(MySQLiteHelper.COLUMN_ID, foodItem.getId());
			values.put(MySQLiteHelper.COLUMN_ITEMNAME, foodItem.getName());
			values.put(MySQLiteHelper.COLUMN_ITEMPRICE, foodItem.getPrice());
			values.put(MySQLiteHelper.COLUMN_ITEMPICTURE, image);
			database.insertWithOnConflict(MySQLiteHelper.TABLE_FOODITMES, null,
					values, SQLiteDatabase.CONFLICT_IGNORE);

			// Update item list
            Intent intent = new Intent(NEW_INFO_INTENT);
            intent.putExtra(NEW_INFO_EXTRA, NEW_ITEM);
            context.sendBroadcast(intent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a new order to server, update local database, then list view
	 */
	public int addOrder(String orderUser) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ORDER_URI);
		ContentValues values = new ContentValues();
		
		try {
			// Post to server
			String orderID = String.valueOf(new Random().nextInt(100000));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("orderID", orderID));
			nameValuePairs.add(new BasicNameValuePair("orderStatus", Order.STATUS_PENDING));
			nameValuePairs.add(new BasicNameValuePair("orderUser", orderUser));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);
			
			// Update local database
			values.put(MySQLiteHelper.COLUMN_ID, orderID);
			values.put(MySQLiteHelper.COLUMN_ORDERSTATUS, "Pending");
			values.put(MySQLiteHelper.COLUMN_ORDERUSER, orderUser);
			database.insertWithOnConflict(MySQLiteHelper.TABLE_ORDERS, null,
					values, SQLiteDatabase.CONFLICT_IGNORE);
			
			// Update order list
            Intent intent = new Intent(NEW_INFO_INTENT);
            intent.putExtra(NEW_INFO_EXTRA, NEW_ORDER);
            context.sendBroadcast(intent);
            
            return DeliveryApplication.ADD_ORDER_SUCCESS;
            
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return DeliveryApplication.ADD_ORDER_FAIL;
	}

    /**
     * Get cursor of database table food item, point to the first row 
     * @return
     */
    public Cursor getFoodItemCursor() {
            return database.query(MySQLiteHelper.TABLE_FOODITMES,
                            null, null, null, null, null, 
                            MySQLiteHelper.GET_ALL_ORDER_BY); 
    }
    
    /**
     * Get cursor of database table order, point to the first row 
     * @return
     */
    public Cursor getOrderCursor(String orderUser, Identity identity) {
    	if (identity == Identity.CUSTOMER)
    		return database.query(MySQLiteHelper.TABLE_ORDERS, null,
                    MySQLiteHelper.COLUMN_ORDERUSER + "=?",
                    new String[] {orderUser}, 
                    null, null, 
                    MySQLiteHelper.GET_ALL_ORDER_BY);
    	else
    		return database.query(MySQLiteHelper.TABLE_ORDERS, null,
                    null,
                    null,
                    null, null, 
                    MySQLiteHelper.GET_ALL_ORDER_BY);
    }
    
    /**
     * Login to application
     * @param name
     * @param password
     * @return server response
     */
    public String login(String name, String password) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(USER_URI + "?name=" + name + "&password=" + password);
		String line = null;

		try {
			
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			line = reader.readLine();
			reader.close();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line;
    }
    

    /**
     * Sign up to application.
     * @param name
     * @param password
     * @return server response, e.g. user already exits
     */
    public String signup(String name, String password, String identity) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(USER_URI);
		String line = null;
		
		try {
			// Post to server
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("name", name));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("identity", identity));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			line = reader.readLine();
			reader.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line;
    }
    
    
    /**
     *  Used for provider to confirm a pending order. If order status is incorrect, return -1
     * @param order
     */
	public int orderProviderConfirm(String orderID) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ORDER_URI);
		ContentValues values = new ContentValues();

		try {
			// Post to server
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("orderID", orderID));
			nameValuePairs.add(new BasicNameValuePair("orderStatus", Order.STATUS_PROV_CONFIRMED));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);

			// Update local database
			values.put(MySQLiteHelper.COLUMN_ID, orderID);
			values.put(MySQLiteHelper.COLUMN_ORDERSTATUS, Order.STATUS_PROV_CONFIRMED);
			database.updateWithOnConflict(MySQLiteHelper.TABLE_ORDERS, values, 
					null, null, SQLiteDatabase.CONFLICT_IGNORE);
			
			// Update order list
            Intent intent = new Intent(NEW_INFO_INTENT);
            intent.putExtra(NEW_INFO_EXTRA, NEW_ORDER);
            context.sendBroadcast(intent);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	/**
     *  Used for courier to confirm a provider confirmed order. If order status is incorrect, return -1
     * @param order
     */
	public int orderDeliveryConfirm(String orderID) {
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ORDER_URI);
		ContentValues values = new ContentValues();

		try {
			// Post to server
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("orderID", orderID));
			nameValuePairs.add(new BasicNameValuePair("orderStatus", Order.STATUS_COUR_CONFIRMED));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);

			// Update local database
			values.put(MySQLiteHelper.COLUMN_ID, orderID);
			values.put(MySQLiteHelper.COLUMN_ORDERSTATUS, Order.STATUS_COUR_CONFIRMED);
			database.updateWithOnConflict(MySQLiteHelper.TABLE_ORDERS, values, 
					null, null, SQLiteDatabase.CONFLICT_IGNORE);
			
			// Update order list
            Intent intent = new Intent(NEW_INFO_INTENT);
            intent.putExtra(NEW_INFO_EXTRA, NEW_ORDER);
            context.sendBroadcast(intent);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
     *  Used for courier to confirm an order has completed. If order status is incorrect, return -1
     * @param order
     */
	public int orderTransactionConfirm(String orderID) {
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ORDER_URI);
		ContentValues values = new ContentValues();

		try {
			// Post to server
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("orderID", orderID));
			nameValuePairs.add(new BasicNameValuePair("orderStatus", Order.STATUS_CLOSED));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);

			// Update local database
			values.put(MySQLiteHelper.COLUMN_ID, orderID);
			values.put(MySQLiteHelper.COLUMN_ORDERSTATUS, Order.STATUS_CLOSED);
			database.updateWithOnConflict(MySQLiteHelper.TABLE_ORDERS, values, 
					null, null, SQLiteDatabase.CONFLICT_IGNORE);
			
			// Update order list
            Intent intent = new Intent(NEW_INFO_INTENT);
            intent.putExtra(NEW_INFO_EXTRA, NEW_ORDER);
            context.sendBroadcast(intent);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
     *  Used for courier to update location
     * @param order
     */
	public int uploadOrderLocation(String orderID, String location_x, String location_y) {
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(LOCATION_URI);

		try {
			// Post to server
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("orderID", orderID));
			nameValuePairs.add(new BasicNameValuePair("location_x", location_x));
			nameValuePairs.add(new BasicNameValuePair("location_y", location_y));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 
	 * @param orderID
	 * @return return location array
	 */
	public int[] getOrderLocation(String orderID) {
		int[] location = new int[2];
		
		try {
			String line;
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(LOCATION_URI + "?orderID=" + orderID);			
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			line = reader.readLine();
			if (line.contains("error"))
				return null;
			String[] locationStrings = line.split(";");			
			location[0] = Double.valueOf(locationStrings[0]).intValue();
			location[1] = Double.valueOf(locationStrings[1]).intValue();

			reader.close();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return location;
	}
}
