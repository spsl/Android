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

	public void addFoodItem(FoodItem foodItem) {
		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.COLUMN_CREATED_AT, foodItem.getCreateAt());
		values.put(MySQLiteHelper.COLUMN_NAME, foodItem.getName());
		values.put(MySQLiteHelper.COLUMN_PRICE, foodItem.getPrice());
		
		database.insert(MySQLiteHelper.TABLE_FOODITMES, null, values);
	}

	public void deleteFoodItem(FoodItem foodItem) {
		long id = foodItem.getId();
		
		database.delete(MySQLiteHelper.TABLE_FOODITMES, 
				MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public List<FoodItem> getAllFoodItems() {
		List<FoodItem> foodItems = new ArrayList<FoodItem>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_FOODITMES,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FoodItem foodItem = cursorToFoodItem(cursor);
			foodItems.add(foodItem);
			cursor.moveToNext();
		}

		cursor.close();
		return foodItems;
	}
	
	public long getLatestCreatedAtTime() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(MySQLiteHelper.TABLE_FOODITMES, 
					MySQLiteHelper.MAX_CREATED_AT_COLUMNS, null, null, null, null, null);
			try {
				// return the first item's created time, which is latest
				return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
			} finally {
				cursor.close();
			}
		} finally {
			db.close(); 
		}
	}
	
	

	private FoodItem cursorToFoodItem(Cursor cursor) {
		FoodItem foodItem = new FoodItem();
		
		foodItem.setId(cursor.getLong(0));
		foodItem.setName(cursor.getString(1));
		foodItem.setPrice(cursor.getLong(2));
		
		return foodItem;
	}
}