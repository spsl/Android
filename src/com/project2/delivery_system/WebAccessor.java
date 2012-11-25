package com.project2.delivery_system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WebAccessor {

	private static String SERVER_URI = "http://safe-beyond-4124.herokuapp.com/";
	
	// fetch all web food items, return a list of these items
	public List<FoodItem> getAllWebFoodItems() {
		URLConnection connection;
		BufferedReader reader;
		String line;
		ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
		
		try {
			connection = new URL(SERVER_URI).openConnection();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while ((line = reader.readLine()) != null) {
				if (line.contains("itemID")) {		
					int offset1 = line.indexOf("itemID=");
					int offset2 = line.indexOf("foodname=");
					int offset3 = line.indexOf("foodprice=");
					int offset4 = line.indexOf("</a>");
					
					int id = Integer.parseInt(line.substring(offset1 + 7, offset2 - 2));
					String name = line.substring(offset2 + 9, offset3 - 2);
					int price = Integer.parseInt(line.substring(offset3 + 10, offset4));
					FoodItem foodItem = new FoodItem(id, name, price);
					
					foodItems.add(foodItem);
				}
			}
			
			return foodItems;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
