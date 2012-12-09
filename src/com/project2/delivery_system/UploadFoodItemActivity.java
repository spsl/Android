package com.project2.delivery_system;


import com.project2.delivery_system.R;
import com.project2.hardware.CamActivity;
import com.project2.utilities.FoodItem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadFoodItemActivity extends Activity { 
	
	byte[] image;
	String filePath;
	Button updateButton;
	Button takePhotoButton;
	EditText itemIDEditText;
	EditText itemNameEditText;
	EditText itemPriceEditText;
	DeliveryApplication delivery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		delivery = (DeliveryApplication)getApplication();
		setTitle("DeLiWei Food Factory for: " + delivery.getUser());
		itemIDEditText = (EditText)findViewById(R.id.editItemID);
		itemNameEditText = (EditText)findViewById(R.id.editItemName);
		itemPriceEditText = (EditText)findViewById(R.id.editItemPrice);
		updateButton = (Button)findViewById(R.id.buttonUpdate);
		updateButton.setOnClickListener(new OnClickListener() {
			@Override			
			public void onClick(View v) {
				String itemID = itemIDEditText.getText().toString();
				String itemName = itemNameEditText.getText().toString();
				String itemPrice = itemPriceEditText.getText().toString();
				
				// Input validation test
				if (itemID.equals("") || itemName.equals("") || itemPrice.equals("") ||
						itemID == null || itemName == null || itemPrice == null || image == null) {
					Toast.makeText(delivery, "Please provide enough information", Toast.LENGTH_LONG).show();
				} else if (itemID.matches("\\d+\\.?\\d*") == false) {
					Toast.makeText(delivery, "Item ID should be number", Toast.LENGTH_LONG).show();
				} else if (itemPrice.matches("\\d+\\.?\\d*") == false) {
					Toast.makeText(delivery, "Item Price should be number", Toast.LENGTH_LONG).show();
				} else {
					// Upload in background
					new Uploader().execute(itemID, itemName, itemPrice, filePath);
					startActivity(new Intent(UploadFoodItemActivity.this, BrowseActivity.class));
				}
			}
		});
		takePhotoButton = (Button)findViewById(R.id.button_take_photo);
		takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(UploadFoodItemActivity.this, CamActivity.class), 0);
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
		        // Use Data to get string
				ImageView jpgView = (ImageView)findViewById(R.id.jpgview);
				TextView txtView = (TextView)findViewById(R.id.textViewNoImage);
		        filePath = data.getStringExtra("RESULT_STRING");
		        image = data.getByteArrayExtra("RESULT_IMG");
		        
		        BitmapDrawable d = new BitmapDrawable(getResources(), filePath);
		        txtView.setVisibility(View.INVISIBLE);
		        jpgView.setImageDrawable(d);
			}
		}
	}
	
	
	/***
	 * Asynchronously posts to server, avoid blocking UI thread. The first data
	 * type is used by doInBackground, the second by onProgressUpdate, and the
	 * third by onPostExecute.
	 */
	class Uploader extends AsyncTask<String, Integer, String> {

		// doInBackground() is the callback that specifies the actual work to be
		// done on the separate thread, as if it’s executing in the background.
		@Override
		protected String doInBackground(String... fooditem) {
			try {
				delivery.getWebAccessor().addFoodItem(
						new FoodItem(fooditem[0], fooditem[1], fooditem[2], fooditem[3]), image);
			} catch (Exception e) {
				e.printStackTrace();
				return "Failed to upload";
			}
			return "Successfully uploaded";
		}

		// onProgressUpdate() is called whenever there’s progress in the task
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
}
