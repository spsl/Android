package com.project2.delivery_system;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project2.hardware.GPSActivity;
import com.project2.hardware.NFCActivity;
import com.project2.utilities.Order;

/**
 * View details of an order. 
 */
public class OrderViewActivity extends Activity {
	
	private String orderID;
	private String orderStatus;
	private String orderUser;
	private String orderItem;
	
	private Button actionButton;
	private Button traceButton;
	private DeliveryApplication delivery;
	
    Intent intent;
    int[] trace_location;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderview);
		
		delivery = (DeliveryApplication) getApplication();
		setTitle("DeLiWei Food Factory for: " + delivery.getUser());
		Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderID = bundle.getString("orderID");
            orderStatus = bundle.getString("orderStatus");
            orderUser = bundle.getString("orderUser");
            orderItem = bundle.getString("orderItem");
        }
        // Display details for user.
        TextView text;
        text = (TextView)findViewById(R.id.textView_order_id);
        text.setText("Order ID: " + orderID);
        text = (TextView)findViewById(R.id.textView_order_item);
        text.setText("Order Item: " + orderItem);
        text = (TextView)findViewById(R.id.textView_order_status);
        text.setText(orderStatus);
        text = (TextView)findViewById(R.id.textView_order_user);
        text.setText("Order User: " + orderUser);        
		
        // Set up action button for different user.
		actionButton = (Button)findViewById(R.id.button_action);
		traceButton = (Button)findViewById(R.id.button_trace);

		actionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			    Intent intent;
			    switch(delivery.getIdentity()){
		        case CUSTOMER:
                    // For customer, check if the state of the order is delivery confirmed, 
                    // if yes, start NFC activity for transaction
                    if (OrderViewActivity.this.orderStatus.contentEquals(Order.STATUS_COUR_CONFIRMED)){
		                intent = new Intent(OrderViewActivity.this, NFCActivity.class);
		                Bundle bundle = new Bundle();
                        bundle.putString("orderID", orderID);
                        bundle.putString("orderStatus", orderStatus);
                        bundle.putString("orderUser", orderUser);
                        bundle.putString("orderItem", orderItem);
                        intent.putExtras(bundle);
		                startActivity(intent);
                    }
		            break;
		        case PROVIDER:
		            // Update order status
		        	new Uploader().execute(Order.STATUS_PENDING, orderID);
		            break;
		        case COURIER:
		            if (OrderViewActivity.this.orderStatus.equals(Order.STATUS_COUR_CONFIRMED)) {
		                intent = new Intent(OrderViewActivity.this, NFCActivity.class);
		                startActivity(intent);
		            }
		            else if (OrderViewActivity.this.orderStatus.equals(Order.STATUS_PROV_CONFIRMED)) {
			            // Update order status
			        	new Uploader().execute(Order.STATUS_PROV_CONFIRMED, orderID);
			            break;
		            }
		            break;
		        default:
		            break; 
			    }
			    finish();
			}
		});
		
		traceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Start GPS activity showing the location of the courier                
                new Uploader().execute("Order Trace", orderID);
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
	
	// Enable action buttons if the status of the order is legal
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
            if(orderStatus.contentEquals(Order.STATUS_PROV_CONFIRMED)){
                actionButton.setText(getText(R.string.order_action_confirm));  
                actionButton.setEnabled(true);
                actionButton.setVisibility(View.VISIBLE);
            } else if(orderStatus.contentEquals(Order.STATUS_COUR_CONFIRMED)){
                actionButton.setText(getText(R.string.order_action_transaction));  
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
	    
	    if(orderStatus.contentEquals(Order.STATUS_COUR_CONFIRMED)){
            traceButton.setEnabled(true);
            traceButton.setVisibility(View.VISIBLE);
        }else{
            traceButton.setEnabled(false);
            traceButton.setVisibility(View.INVISIBLE);
        }
	};
	
	/***
	 * Asynchronously posts to server, avoid blocking UI thread.
	 */
	class Uploader extends AsyncTask<String, Integer, String> {

		// doInBackground() is the callback that specifies the actual work to be
		// done on the separate thread, as if it's executing in the background.
		@Override
		protected String doInBackground(String... in) {
			try {
				if (in[0].equals(Order.STATUS_PENDING)) 
					delivery.getWebAccessor().orderProviderConfirm(orderID);
				else if (in[0].equals(Order.STATUS_PROV_CONFIRMED))
					delivery.getWebAccessor().orderDeliveryConfirm(orderID);
				else if (in[0].equals("Order Trace"))
					trace_location = delivery.getWebAccessor().getOrderLocation(orderID);
				return in[0];
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "NONE";
		}

		// onProgressUpdate() is called whenever there's progress in the task
		// execution. The progress should be reported from the doInBackground() call.
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// onPostExecute() is called when our task completes. This is our
		// callback method to update the user interface and tell the user 
		// that the task is done.
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("Order Trace")) {
				intent = new Intent(OrderViewActivity.this, GPSActivity.class);
				
				if (trace_location == null) {
					Toast.makeText(delivery, "No tracing information", Toast.LENGTH_LONG).show();
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString("orderID", orderID);
				bundle.putString("orderStatus", orderStatus);
				bundle.putString("orderUser", orderUser);
				bundle.putInt("locX", trace_location[0]);
				bundle.putInt("locY", trace_location[1]);
				intent.putExtras(bundle);
            
				startActivity(intent);
			}
		}
	}
}
