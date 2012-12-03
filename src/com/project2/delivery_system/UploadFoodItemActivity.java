package com.project2.delivery_system;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UploadFoodItemActivity extends Activity { 
	
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
				// Upload in background
				new Uploader().execute(itemID, itemName, itemPrice);
				startActivity(new Intent(UploadFoodItemActivity.this, BrowseActivity.class));
			}
		});
		takePhotoButton = (Button)findViewById(R.id.button_take_photo);
		takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(UploadFoodItemActivity.this, CamActivity.class));
			}
		});
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
				delivery.getWebAccessor().addFoodItem(new FoodItem(fooditem[0], fooditem[1], fooditem[2]));
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
