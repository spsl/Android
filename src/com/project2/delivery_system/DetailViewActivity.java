package com.project2.delivery_system;

import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
import android.os.AsyncTask;
=======
>>>>>>> e1c7ca6f97a5e28b900ebce1c1f022299caa2ae3
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
		
<<<<<<< HEAD
		orderButton = (Button)findViewById(R.id.buttonOrder);
		orderButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Uploader().execute();
				startActivity(new Intent(DetailViewActivity.this, BrowseActivity.class));
			}
		});
		
		Toast.makeText(DetailViewActivity.this, itemID + " " + itemName+ " " + itemPrice + " ", 
				Toast.LENGTH_SHORT).show();
	}
	
	/***
	 * Asynchronously posts to server, avoid blocking UI thread. The first data
	 * type is used by doInBackground, the second by onProgressUpdate, and the
	 * third by onPostExecute.
	 */
	class Uploader extends AsyncTask<String, Integer, String> {

		// doInBackground() is the callback that specifies the actual work to be
		// done on the separate thread, as if itÕs executing in the background.
		@Override
		protected String doInBackground(String... fooditem) {
			try {
				delivery.getWebAccessor().addOrder(delivery.getUser());
			} catch (Exception e) {
				e.printStackTrace();
				return "Failed to order";
			}
			return "Successfully ordered";
		}

		// onProgressUpdate() is called whenever thereÕs progress in the task
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
			// using a Toast feature of the Android UI to display a quick
			// message on the screen.
			Toast.makeText(delivery, result, Toast.LENGTH_LONG).show();
		}
	}

=======
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
>>>>>>> e1c7ca6f97a5e28b900ebce1c1f022299caa2ae3
}
