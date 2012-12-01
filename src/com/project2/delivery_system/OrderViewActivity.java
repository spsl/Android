package com.project2.delivery_system;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;

/**
 * View details of an order. 
 */
public class OrderViewActivity extends Activity {
	
	private String orderID;
	private String orderStatus;
	@SuppressWarnings("unused")
	private String orderUser;
	
	private Button actionButton;
	private DeliveryApplication delivery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderview);
		
		delivery = (DeliveryApplication) getApplication();
		Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            orderID = bundle.getString("orderID");
            orderStatus = bundle.getString("orderStatus");
            orderUser = bundle.getString("orderUser");
        }
        // Display details for user.
        TextView text;
        text = (TextView)findViewById(R.id.textView_order_id);
        text.setText(orderID);
        text = (TextView)findViewById(R.id.textView_order_status);
        text.setText(orderStatus);
		
        // Set up action button for different user.
		actionButton = (Button)findViewById(R.id.button_action);
		switch(delivery.getIdentity()){
		case CUSTOMER:
		    actionButton.setText(getText(R.string.order_action_transaction));
		    break;
		case PROVIDER:
		    actionButton.setText(getText(R.string.order_action_confirm));
            break;
		case COURIER:
		    actionButton.setText(getText(R.string.order_action_confirm));		
            break;
        default:
            break;
		}
		actionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			    Intent intent;
			    switch(delivery.getIdentity()){
		        case CUSTOMER:
		            // intent = new Intent(OrderViewActivity.this, NFCActivity.class);
		        	intent = new Intent(OrderViewActivity.this, GPSActivity.class);
		        	startActivity(intent);
		            break;
		        case PROVIDER:
		            // update order status
		            break;
		        case COURIER:
		            if(OrderViewActivity.this.orderStatus == "STATUS_COUR_CONFIRMED"){
		                intent = new Intent(OrderViewActivity.this, NFCActivity.class);
		                startActivity(intent);
		            }
		            else if(OrderViewActivity.this.orderStatus == "STATUS_PROV_CONFIRMED"){
		                // send update command to change status;
		            }
		            break;
		        default:
		            break; 
			    }
			}
		});
		
		// Disable action button according to different order status.
		if(delivery.getIdentity() == DeliveryApplication.Identity.CUSTOMER
		        &&OrderViewActivity.this.orderStatus != "STATUS_COUR_CONFIRMED")
		    actionButton.setEnabled(false);
		if(delivery.getIdentity() == DeliveryApplication.Identity.PROVIDER
                &&OrderViewActivity.this.orderStatus != "STATUS_INIT")
            actionButton.setEnabled(false);
		if(delivery.getIdentity() == DeliveryApplication.Identity.COURIER
                &&OrderViewActivity.this.orderStatus != "STATUS_PROV_CONFIRMED")
            actionButton.setEnabled(false);
		//*************************************************************************************************
		actionButton.setEnabled(true);
	}
	
	// On new intent, read extra information
	public void onNewIntent(Intent intent) {
	    
	    Bundle bundle = intent.getExtras();
	    if(bundle!=null) {
	        orderID = bundle.getString("orderID");
	        orderStatus = bundle.getString("orderStatus");
	        orderUser = bundle.getString("orderUser");
	    }
	    setIntent(intent);
	    TextView text;
	    text = (TextView)findViewById(R.id.textView_order_id);
	    text.setText(orderID);
	    text = (TextView)findViewById(R.id.textView_order_status);
        text.setText(orderStatus);
        
	    super.onNewIntent(intent);
	}
}
