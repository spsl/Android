package com.project2.delivery_system;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.view.Menu;

public class MainActivity extends Activity {
	
	DeliveryApplication deliveryApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        deliveryApplication = new DeliveryApplication();
        
        ContentValues values = new ContentValues();
        values.put(FoodDataBase.C_ID, 11); 
        values.put(FoodDataBase.C_CREATED_AT, 10); 
        values.put(FoodDataBase.C_NAME, "chicken");
        values.put(FoodDataBase.C_PRICE, 11.68);
        
        deliveryApplication.getFoodDataBase().insertOrIgnore(values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
