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
		
		setupActionButtonUsingOrderStatus();
		
		actionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			    Intent intent;
			    switch(delivery.getIdentity()){
		        case CUSTOMER:
                    // for customer, check if the state of the order is delivery confirmed, 
                    // if yes, start nfc activity for transaction
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
		        	delivery.getWebAccessor().orderProviderConfirm(new Order(orderID, orderStatus, orderStatus));
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
        
        setupActionButtonUsingOrderStatus();
        
	    super.onNewIntent(intent);
	}
	
	// enable action buttons if the status of the order is legal
	private void setupActionButtonUsingOrderStatus(){
	    if(delivery.getIdentity() == DeliveryApplication.Identity.CUSTOMER
                && !OrderViewActivity.this.orderStatus.equals(Order.STATUS_COUR_CONFIRMED))
            actionButton.setEnabled(false);
        if(delivery.getIdentity() == DeliveryApplication.Identity.PROVIDER
                &&!OrderViewActivity.this.orderStatus.equals(Order.STATUS_PENDING))
            actionButton.setEnabled(false);
        if(delivery.getIdentity() == DeliveryApplication.Identity.COURIER
                &&!OrderViewActivity.this.orderStatus.equals(Order.STATUS_PROV_CONFIRMED))
            actionButton.setEnabled(false);
        //*************************************************************************************************
        actionButton.setEnabled(true);
	    
	};
}
