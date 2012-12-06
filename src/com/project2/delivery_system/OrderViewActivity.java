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
	private String orderUser;
	
	private Button actionButton;
	private Button traceButton;
	private DeliveryApplication delivery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderview);
		
		delivery = (DeliveryApplication) getApplication();
		Bundle bundle = getIntent().getExtras();
        if(bundle != null){
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
		traceButton = (Button)findViewById(R.id.button_trace);

		actionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			    Intent intent;
			    switch(delivery.getIdentity()){
		        case CUSTOMER:
                    // for customer, check if the state of the order is delivery confirmed, 
                    // if yes, start NFC activity for transaction
                    if(OrderViewActivity.this.orderStatus.contentEquals(Order.STATUS_COUR_CONFIRMED)){
		                intent = new Intent(OrderViewActivity.this, NFCActivity.class);
		                Bundle bundle = new Bundle();
                        bundle.putString("orderID", orderID);
                        bundle.putString("orderStatus", orderStatus);
                        bundle.putString("orderUser", orderUser);
                        intent.putExtras(bundle);
		                startActivity(intent);
                    }
		            break;
		        case PROVIDER:
		            // update order status
		        	delivery.getWebAccessor().orderProviderConfirm(orderID);
		            break;
		        case COURIER:
		            if(OrderViewActivity.this.orderStatus.equals(Order.STATUS_COUR_CONFIRMED)) {
		                intent = new Intent(OrderViewActivity.this, NFCActivity.class);
		                startActivity(intent);
		            }
		            else if(OrderViewActivity.this.orderStatus.equals(Order.STATUS_PROV_CONFIRMED)) {
		                // send update command to change status;
		            }
		            break;
		        default:
		            break; 
			    }
			}
		});
		
		traceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                
                // start GPS activity showing the location of the courior
                Intent intent;
                intent = new Intent(OrderViewActivity.this, GPSActivity.class);
               
                int trace_location_x, trace_location_y;
                
                //TODO get order location info
                // dummy location: 
                trace_location_x = 19240000;
                trace_location_y = -99120000;
                Bundle bundle = new Bundle();
                bundle.putString("orderID", orderID);
                bundle.putString("orderStatus", orderStatus);
                bundle.putString("orderUser", orderUser);
                bundle.putInt("locX", trace_location_x);
                bundle.putInt("locY", trace_location_y);
                intent.putExtras(bundle);
                
                startActivity(intent);
            }
        });
		
		setupButtons();
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
        
        setupButtons();
        
	    super.onNewIntent(intent);
	}
	
	// enable action buttons if the status of the order is legal
	private void setupButtons(){
	    switch(delivery.getIdentity()) {
        case CUSTOMER:{
            actionButton.setText(getText(R.string.order_action_transaction));
            if(orderStatus.contentEquals(Order.STATUS_COUR_CONFIRMED)){
                actionButton.setEnabled(true);
                actionButton.setVisibility(View.VISIBLE);
            }  
            else{
                actionButton.setEnabled(false);
                actionButton.setVisibility(View.INVISIBLE);
            }
        }break;
        case PROVIDER:{
            actionButton.setText(getText(R.string.order_action_confirm));
            if(orderStatus.contentEquals(Order.STATUS_PENDING)){
                actionButton.setEnabled(true);
                actionButton.setVisibility(View.VISIBLE);
            }  
            else{
                actionButton.setEnabled(false);
                actionButton.setVisibility(View.INVISIBLE);
            }
        }break;
        case COURIER:{
            actionButton.setText(getText(R.string.order_action_confirm));   
            if(orderStatus.contentEquals(Order.STATUS_PROV_CONFIRMED)){
                actionButton.setEnabled(true);
                actionButton.setVisibility(View.VISIBLE);
            }  
            else{
                actionButton.setEnabled(false);
                actionButton.setVisibility(View.INVISIBLE);
            }
        }break;
        default:
            break;
        }
	};
}
