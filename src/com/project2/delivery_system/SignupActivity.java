package com.project2.delivery_system;

import com.project2.delivery_system.DeliveryApplication.Identity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
	Spinner userIdentitySpinner;
	DeliveryApplication delivery;
	private boolean passwordDisplay = false;  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		// Instantiate all variables
		delivery = (DeliveryApplication)getApplication();
		userNameEditText = (EditText)findViewById(R.id.editSignupName);
		passwordEditText= (EditText)findViewById(R.id.editSignupPassword);
		passwordConfirmEditText= (EditText)findViewById(R.id.editSignupPasswordConfirm);
		userIdentitySpinner = (Spinner)findViewById(R.id.spinnerIdentity);
		signupButton= (Button)findViewById(R.id.buttonSignup);
		signupButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String name = userNameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				String passwordConfirm = passwordConfirmEditText.getText().toString();
				String identity = (String)userIdentitySpinner.getSelectedItem();

				// hide password, display "."  
				passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());  
				passwordDisplay = !passwordDisplay;  
				passwordEditText.postInvalidate();  

				if (name.equals("")) {					// no user name is entered
					Toast.makeText(delivery, "please enter user name", Toast.LENGTH_LONG).show();
				} else if (password.equals("")) {		// no password is entered
					Toast.makeText(delivery, "please enter valid password", Toast.LENGTH_LONG).show();
				} else if (!password.equals(passwordConfirm)) {	// if password doesn't equal
					Toast.makeText(delivery, "password does not match", Toast.LENGTH_LONG).show();
				} else {
					delivery.setUser(name);
					if (identity.equalsIgnoreCase("customer"))
						delivery.setIdentity(Identity.CUSTOMER);
					else if (identity.equalsIgnoreCase("provider"))
						delivery.setIdentity(Identity.PROVIDER);
					else if (identity.equalsIgnoreCase("courier"))
						delivery.setIdentity(Identity.COURIER);
					new Uploader().execute(name, password, identity);
				}
			}
		});
	}


	/***
	 * Asynchronously posts to server, avoid blocking UI thread.
	 */
	class Uploader extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... user) {
			try {
				String result = delivery.getWebAccessor().signup(user[0], user[1], user[2]);
				// Display message from server.
				Toast.makeText(delivery, result, Toast.LENGTH_LONG).show();
				if (result.contains("error")) {
					return DeliveryApplication.SIGNUP_FAIL;
				} else {
					return DeliveryApplication.SIGNUP_SUCCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return DeliveryApplication.WEB_ERROR;
		}

		// onProgressUpdate() is called whenever there�s progress in the task
		// execution. The progress should be reported from the doInBackground() call.
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// onPostExecute() is called when our task completes. This is our
		// callback method to update the user interface and tell the user 
		// that the task is done.
		@Override
		protected void onPostExecute(Integer result) {
			if (result == DeliveryApplication.SIGNUP_SUCCESS) {
				startActivity(new Intent(SignupActivity.this, BrowseActivity.class));	
			}
		}
	}
}