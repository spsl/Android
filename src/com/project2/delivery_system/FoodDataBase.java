package com.project2.delivery_system;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;



public class FoodDataBase {

	static final int DB_VERSION = 1; 			// version number of database
	static final String DB_NAME = "food.db"; 	// this is the database name
	static final String TABLE = "fooditem";		// table name of food database
	static final String C_ID = BaseColumns._ID;
	static final String C_CREATED_AT = "created_at";
	static final String C_NAME = "name";
	static final String C_PRICE = "price";
	private static final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";
	private static final String[] MAX_CREATED_AT_COLUMNS = { "max(" + FoodDataBase.C_CREATED_AT + ")" };
	private static final String[] DB_NAME_COLUMNS = { C_NAME };
	
	private final DbHelper dbHelper;
	
	
	// Constructor
	public FoodDataBase(Context context) {
		dbHelper = new DbHelper(context);
	}
	
	// provide other classes the ability to access database
	public SQLiteDatabase getReadableDatabase() {
		return dbHelper.getReadableDatabase();
	}
	
	// provide other classes the ability to access database
	public SQLiteDatabase getWritableDatabase() {
		return dbHelper.getWritableDatabase();
	}
	
	// Close connection to database
	public void close() {
		dbHelper.close();
	}
	
	// Delete items in database
	public void delete() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(TABLE, null, null);
	}

	// Insert values into database, ignore when conflict
	// E.g. ContentValues values = new ContentValues();
	//		for (Status status : statusUpdates) {
	//			long createdAt = status.getCreatedAt().getTime(); 
	//			values.put(StatusData.C_ID, status.getId().longValue());
	//			......
	//			insertOrIgnore(values);
	//		}
	public void insertOrIgnore(ContentValues values) {
		// Open the database to write, the first time we make the call
		// DBHelper will create a new database
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			// Use `CONFLICT_IGNORE` to ignore conflict
			db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally { 
			db.close();
		}
	}
	
	// return the latest food's creation time from database
	public long getLatestStatusCreatedAtTime() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, null, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
			} finally {
				cursor.close();
			}
		} finally {
			db.close(); 
		}
	}
	
	// return status by Id
	public String getStatusTextById(long id) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase(); 
		try {
			Cursor cursor = db.query(TABLE, DB_NAME_COLUMNS, C_ID + "=" + id, null, null, null, null);
		
			try {
				return cursor.moveToNext() ? cursor.getString(0) : null;
			} finally {
				cursor.close(); 
			}
		} finally {
			db.close(); 
		}
	}
	
	// return all items from database
	public Cursor getAllItems() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.query(TABLE, null, null, null, null, null, GET_ALL_ORDER_BY); 
	}
	
	// Helper class to access database
	private class DbHelper extends SQLiteOpenHelper {

		// Constructor
		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		// Called only once, first time the DB is created
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "create table " + TABLE + " (" + 
					C_ID + " int primary key, " + 
					C_CREATED_AT + " int, " + 
					C_NAME + " text, " + 
					C_PRICE+ " text)";
			db.execSQL(sql); 	// execute actual SQL on the object passed into onCreate
		}

		// Called whenever newVersion != oldVersion
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Typically do ALTER TABLE statements, but...we're just in development
			db.execSQL("drop table if exists " + TABLE);	// drops the old database
			onCreate(db); 		// run onCreate to get new database
		}
	}
}
