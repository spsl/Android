package com.project2.delivery_system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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
		
		TextView text;
        text = (TextView)findViewById(R.id.textView_item_id);
        text.setText(itemID);
        text = (TextView)findViewById(R.id.textView_item_name);
        text.setText(itemName);
        text = (TextView)findViewById(R.id.textView_item_price);
        text.setText(itemPrice);
        
        orderButton = (Button)findViewById(R.id.buttonOrder);
        
        if(delivery.getIdentity()==DeliveryApplication.Identity.CUSTOMER){
            orderButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    new WebAccessor().uploadOrder(delivery.getUser());
                    
                    Toast.makeText(DetailViewActivity.this, itemID + " " + itemName+ " " + itemPrice + " ", Toast.LENGTH_SHORT).show();
                    // TODO: send new order
                    
                    finish();
                }
            });
        }
        else{
            orderButton.setVisibility(0);
            orderButton.setEnabled(false);
        }
		
		
	}
	
	// on new inent, read extra information
    public void onNewIntent(Intent intent){
        
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            itemID = bundle.getString("ItemID");
            itemName = bundle.getString("ItemName");
            itemPrice = bundle.getString("ItemPrice");
        }
        setIntent(intent);
        TextView text;
        text = (TextView)findViewById(R.id.textView_item_id);
        text.setText(itemID);
        text = (TextView)findViewById(R.id.textView_item_name);
        text.setText(itemName);
        text = (TextView)findViewById(R.id.textView_item_price);
        text.setText(itemPrice);
        
        super.onNewIntent(intent);
    }
}
