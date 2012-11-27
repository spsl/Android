package com.project2.delivery_system;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DetailViewActivity extends Activity {
	
	private String itemID;
	private String itemName;
	private String itemPrice;
	private Button orderButton;
	private DeliveryApplication delivery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailview);
		
		delivery = (DeliveryApplication) getApplication();
		Bundle bundle = getIntent().getExtras();
		itemID = bundle.getString("itemID");
		itemName = bundle.getString("itemName");
		itemPrice = bundle.getString("itemPrice");
		
		orderButton = (Button)findViewById(R.id.buttonOrder);
		orderButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new WebAccessor().uploadOrder(delivery.getUser());
			}
		});
		Toast.makeText(DetailViewActivity.this, itemID + " " + itemName+ " " + itemPrice + " ", Toast.LENGTH_SHORT).show();
	}
}
