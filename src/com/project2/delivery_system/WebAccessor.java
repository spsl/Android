package com.project2.delivery_system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class WebAccessor {

	// for debugging
	private static String FOOD_ITEM_URI = "http://10.0.2.2:8090/fooditems";
	private static String ORDER_URI = "http://10.0.2.2:8090/orders";
	private static String USER_URI = "http://10.0.2.2:8090/users";
	
	/*
	private static String FOOD_ITEM_URI = "http://18641datastore.appspot.com/fooditems";
	private static String ORDER_URI = "http://18641datastore.appspot.com/orders";
	private static String USER_URI = "http://18641datastore.appspot.com/users";
	*/
	
	
	// fetch all web food items, return a list of these items
	public List<FoodItem> getAllWebFoodItems() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(FOOD_ITEM_URI);	// no parameter, get all items
		ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
		
		try {
			String line;
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			
			while ((line = reader.readLine()) != null) {
				String[] itemStrings = line.split(";");
				foodItems.add(new FoodItem(itemStrings[0], itemStrings[1], itemStrings[2]));
			}
			
			reader.close();
			return foodItems;
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// fetch all orders of current user
	public List<Order> getAllWebOrders(String orderUser) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(ORDER_URI + "?orderUser=" + orderUser);	// get current user's orders
		ArrayList<Order> orders = new ArrayList<Order>();
	
		try {
			String line;
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
		
			while ((line = reader.readLine()) != null) {
				String[] orderStrings = line.split(";");
				orders.add(new Order(orderStrings[0], orderStrings[1], orderStrings[2]));
		}
		
		reader.close();
		return orders;
		
	} catch (NumberFormatException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
		return null;
	}
	
	// Upload a food item to server
	public void uploadFoodItem(FoodItem foodItem) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(FOOD_ITEM_URI);

	    try {
	        // Post to server
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	        nameValuePairs.add(new BasicNameValuePair("itemID", foodItem.getId()));
	        nameValuePairs.add(new BasicNameValuePair("itemName", foodItem.getName()));
	        nameValuePairs.add(new BasicNameValuePair("itemPrice", foodItem.getPrice()));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        httpclient.execute(httppost);
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	// Upload a order to server
	public void uploadOrder(String user) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(ORDER_URI);

	    try {
	        // Post to server
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("orderUser", user));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        httpclient.execute(httppost);
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	} 
}
