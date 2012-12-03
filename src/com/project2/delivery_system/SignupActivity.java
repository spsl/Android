package com.project2.delivery_system;

import com.project2.delivery_system.DeliveryApplication.Identity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Sign up activity, called for a new user to sign up.
 * @author deyuandeng
 */
public class SignupActivity extends Activity { 
	
	Button signupButton;
	EditText userNameEditText;
	EditText passwordEditText;
	EditText passwordConfirmEditText;
	DeliveryApplication delivery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		// Instantiate all variables
		delivery = (DeliveryApplication)getApplication();
		userNameEditText = (EditText)findViewById(R.id.editSignupName);
		passwordEditText= (EditText)findViewById(R.id.editSignupPassword);
		passwordConfirmEditText= (EditText)findViewById(R.id.editSignupPasswordConfirm);
		signupButton= (Button)findViewById(R.id.buttonSignup);
		signupButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String name = userNameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				String passwordConfirm = passwordConfirmEditText.getText().toString();
				
				delivery.setUser(name);
				delivery.setIdentity(Identity.CUSTOMER);
				if (!password.equals(passwordConfirm)) {	// if password doesn't equal
					Toast.makeText(delivery, "password does not match", Toast.LENGTH_LONG).show();
				} else {
					new Uploader().execute(name, password);
				}
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
		protected String doInBackground(String... userStrings) {
			try {
				return delivery.getWebAccessor().signup(userStrings[0], userStrings[1]);
			} catch (Exception e) {
				e.printStackTrace();
				return "error: failed to sign up";
			}
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
			Toast.makeText(delivery, result, Toast.LENGTH_LONG).show();
			if (!result.contains("error")) {	// error may occur when user name exists
				startActivity(new Intent(SignupActivity.this, BrowseActivity.class));	
			}
		}
	}
}
