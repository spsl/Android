package com.project2.delivery_system;

import java.util.List;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	
	private FoodDataSource datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		datasource = new FoodDataSource(this);
		datasource.open();
		
		List<FoodItem> foodItems = datasource.getAllFoodItems();

	    // Use the SimpleCursorAdapter to show the elements in a ListView
	    ArrayAdapter<FoodItem> adapter = new ArrayAdapter<FoodItem>(this,
	        android.R.layout.simple_list_item_1, foodItems);
	    setListAdapter(adapter);
	}

	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<FoodItem> adapter = (ArrayAdapter<FoodItem>) getListAdapter();
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}