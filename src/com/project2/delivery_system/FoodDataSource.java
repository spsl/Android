package com.project2.delivery_system;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 * This class is high level database class. It maintains the database connection 
 * and supports adding and fetching new food item.  
 * @author deyuandeng
 */
public class FoodDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;

	
	public FoodDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Add one food item into local database
	 * @param foodItem
	 * @return column added, or 0 if conflict
	 */
	public long addFoodItem(FoodItem foodItem) {
		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.COLUMN_ID, foodItem.getId());
		values.put(MySQLiteHelper.COLUMN_NAME, foodItem.getName());
		values.put(MySQLiteHelper.COLUMN_PRICE, foodItem.getPrice());
		
		try {
			// Use `CONFLICT_IGNORE` to ignore conflict
			return database.insertWithOnConflict(MySQLiteHelper.TABLE_FOODITMES,
					null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Add one food item into local database
	 * @param foodItem
	 * @return column added, or 0 if conflict
	 */
	public long addOrder(Order order) {
		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.COLUMN_ID, order.getId());
		values.put(MySQLiteHelper.COLUMN_STATUS, order.getStatus());
		values.put(MySQLiteHelper.COLUMN_USER, order.getUser());
		
		try {
			// Use `CONFLICT_IGNORE` to ignore conflict
			return database.insertWithOnConflict(MySQLiteHelper.TABLE_ORDERS,
					null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	/**
	 * Delete a specific food item
	 * @param foodItem
	 */
	public void deleteFoodItem(FoodItem foodItem) {
		String id = foodItem.getId();
		
		database.delete(MySQLiteHelper.TABLE_FOODITMES, 
				MySQLiteHelper.COLUMN_ID + " = " + id, null);
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
	 * Get a list of food items in database
	 * @return A list of food items
	 */
	public List<FoodItem> getAllFoodItems() {
		List<FoodItem> foodItems = new ArrayList<FoodItem>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_FOODITMES,
				null, null, null, null, null, MySQLiteHelper.GET_ALL_ORDER_BY);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FoodItem foodItem = cursorToFoodItem(cursor);
			foodItems.add(foodItem);
			cursor.moveToNext();
		}

		cursor.close();
		return foodItems;
	}

	/**
	 * Get cursor of database, point to the first row 
	 * @return
	 */
	public Cursor getOrderCursor() {
		
		return database.query(MySQLiteHelper.TABLE_ORDERS,
				null, null, null, null, null, MySQLiteHelper.GET_ALL_ORDER_BY); 
	}
	
	/**
	 * Convert a row in database into a food item object
	 * @param cursor
	 * @return food item object
	 */
	private FoodItem cursorToFoodItem(Cursor cursor) {
		FoodItem foodItem = new FoodItem();
		
		foodItem.setId(cursor.getString(0));
		foodItem.setName(cursor.getString(1));
		foodItem.setPrice(cursor.getString(2));
		
		return foodItem;
	}
}