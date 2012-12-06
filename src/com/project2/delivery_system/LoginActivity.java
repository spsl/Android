package com.project2.delivery_system;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project2.delivery_system.DeliveryApplication.Identity;

/**
 * Login activity, main activity of our application. 
 * @author deyuandeng
 */
public class LoginActivity extends Activity {
	
	private EditText nameEditText;
	private EditText passwordEditText;
	private Button loginButton;
	private Button signupButton;
	private DeliveryApplication delivery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	
		// Instantiate all variables
		delivery = (DeliveryApplication) getApplication();
		nameEditText = (EditText)findViewById(R.id.editName);
		passwordEditText = (EditText)findViewById(R.id.editPassword);
		loginButton = (Button)findViewById(R.id.buttonLogin);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = nameEditText.getText().toString();
				String password = passwordEditText.getText().toString();

				delivery.setUser(name);		// if login fail, name will be reset next time
				new Uploader().execute(name, password);		// internet connection in background
			}			
		});
		signupButton = (Button)findViewById(R.id.buttonSignup);
		signupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Start sign up activity
				startActivity(new Intent(LoginActivity.this, SignupActivity.class));
			}
		});
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		delivery.getWebAccessor().delete();
	};

	/***
	 * Asynchronously login to server, avoid blocking UI thread.
	 */
	class Uploader extends AsyncTask<String, Integer, String> {

		// doInBackground() is the callback that specifies the actual work to be
		// done on the separate thread, as if it's executing in the background.
		@Override
		protected String doInBackground(String... user) {
			try {
				return delivery.getWebAccessor().login(user[0], user[1]);
			} catch (Exception e) {
				e.printStackTrace();
				return "None";
			}
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
			if (result.contains("error")) {		// stay in login page if something wrong
				Toast.makeText(delivery, result, Toast.LENGTH_LONG).show();
			} else {	// login succeed, set identity and start browse activity
				if (result.equalsIgnoreCase("customer"))
					delivery.setIdentity(Identity.CUSTOMER);
				else if (result.equalsIgnoreCase("provider"))
					delivery.setIdentity(Identity.PROVIDER);
				else if (result.equalsIgnoreCase("courier"))
					delivery.setIdentity(Identity.COURIER);
				startActivity(new Intent(LoginActivity.this, BrowseActivity.class));
			}
		}
	}
}

