package com.project2.delivery_system;


import com.project2.delivery_system.R;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Detail view of a food items for user. To start detail view
 * activity, caller must use intent to specify item details.
 * @author deyuandeng
 */
public class DetailViewActivity extends Activity {

	private String itemID;
	private String itemName;
	private String itemPrice;
	private byte[] itemPicture;
	private Button orderButton;
	private DeliveryApplication delivery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailview);
		
		
		// Instantiate all variables
		delivery = (DeliveryApplication) getApplication();
		setTitle("DeLiWei Food Factory for: " + delivery.getUser());
		Bundle bundle = getIntent().getExtras();
		itemID = bundle.getString("itemID");
		itemName = bundle.getString("itemName");
		itemPrice = bundle.getString("itemPrice");
		itemPicture = bundle.getByteArray("itemPicture");

		// Display item details for user
		TextView text;
		text = (TextView)findViewById(R.id.textView_item_id);
		text.setText("ID: " + itemID);
		text = (TextView)findViewById(R.id.textView_item_name);
		text.setText("Name: " + itemName);
		text = (TextView)findViewById(R.id.textView_item_price);
		text.setText("Price: $" + itemPrice);
		ImageView imageView = (ImageView)findViewById(R.id.jpgview_detail);;
		imageView.setImageBitmap(BitmapFactory.decodeByteArray(itemPicture, 0, itemPicture.length));

		// Set order button only for customer
		orderButton = (Button)findViewById(R.id.buttonOrder);
		setupComponentsAccordingToIdentity();
	}

	// set up interfaces according to user identity
	private void setupComponentsAccordingToIdentity(){
		switch(delivery.getIdentity()) {
		case CUSTOMER:{
			orderButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new Uploader().execute();	// place order
					finish();
				}
			});
			orderButton.setVisibility(View.VISIBLE);
			orderButton.setEnabled(true);
		}break;
		case PROVIDER:{
			orderButton.setVisibility(View.INVISIBLE);
			orderButton.setEnabled(false);
		}break;
		case COURIER:{
			orderButton.setVisibility(View.INVISIBLE);
			orderButton.setEnabled(false);
		}break;
		default:
			break;
		}
	}


	/***
	 * Asynchronously posts to server, avoid blocking UI thread.
	 */
	class Uploader extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... order) {
			try {
				return delivery.getWebAccessor().addOrder(delivery.getUser(), itemName);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return DeliveryApplication.ADD_ORDER_FAIL;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Integer result) {
			// using a Toast feature of the Android UI to display a quick
			// message on the screen.
			if (result == DeliveryApplication.ADD_ORDER_SUCCESS)
				Toast.makeText(delivery, "Successfully ordered", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(delivery, "Failed to order", Toast.LENGTH_LONG).show();
		}
	}
}
