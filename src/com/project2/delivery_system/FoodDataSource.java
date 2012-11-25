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

	public long addFoodItem(FoodItem foodItem) {
		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.COLUMN_CREATED_AT, foodItem.getCreateAt());
		values.put(MySQLiteHelper.COLUMN_NAME, foodItem.getName());
		values.put(MySQLiteHelper.COLUMN_PRICE, foodItem.getPrice());
		
		try {
			open();
			// Use `CONFLICT_IGNORE` to ignore conflict
			return database.insertWithOnConflict(MySQLiteHelper.TABLE_FOODITMES,
					null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	public void deleteFoodItem(FoodItem foodItem) {
		long createAt = foodItem.getCreateAt();
		
		open();
		database.delete(MySQLiteHelper.TABLE_FOODITMES, 
				MySQLiteHelper.COLUMN_CREATED_AT + " = " + createAt, null);
	}

	public List<FoodItem> getAllFoodItems() {
		List<FoodItem> foodItems = new ArrayList<FoodItem>();

		open();
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
	
	private FoodItem cursorToFoodItem(Cursor cursor) {
		FoodItem foodItem = new FoodItem();
		
		foodItem.setCreateAt(cursor.getLong(0));
		foodItem.setName(cursor.getString(1));
		foodItem.setPrice(cursor.getLong(2));
		
		return foodItem;
	}
}