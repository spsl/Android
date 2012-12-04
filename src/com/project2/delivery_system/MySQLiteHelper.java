package com.project2.delivery_system;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is responsible for interacting with database provided
 * by android
 * @author deyuandeng
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "fooditems.db";
	public static final String TABLE_FOODITMES = "fooditems";
	public static final String TABLE_ORDERS = "orders";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ITEMNAME = "item_name";
	public static final String COLUMN_ITEMPRICE = "item_price";
	public static final String COLUMN_ITEMPICTURE = "item_picture";
	public static final String COLUMN_ORDERSTATUS = "order_status";
	public static final String COLUMN_ORDERUSER = "order_user";
	public static final String GET_ALL_ORDER_BY = COLUMN_ID + " DESC";	

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_LIST_CREATE = 
			"create table " + TABLE_FOODITMES + "(" +
			COLUMN_ID + " text primary key, " +
			COLUMN_ITEMNAME + " text not null, " + 
			COLUMN_ITEMPRICE + " text not null, " + 
			COLUMN_ITEMPICTURE + " blob);";
	private static final String DATABASE_ORDER_CREATE = 
			"create table " + TABLE_ORDERS + "(" +
			COLUMN_ID + " text primary key, " +
			COLUMN_ORDERSTATUS + " text not null, " + 
			COLUMN_ORDERUSER + " text not null);";

	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_LIST_CREATE);
		database.execSQL(DATABASE_ORDER_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODITMES);
		onCreate(db);
	}
}