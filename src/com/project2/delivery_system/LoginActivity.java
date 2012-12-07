package com.project2.delivery_system;

import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
import android.graphics.Color;
import android.nfc.NfcAdapter;
=======
>>>>>>> 6056e1f5d8f00de281e0e064ac4506043d5cafe5
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
<<<<<<< HEAD
	@Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
	    if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
	        intent.setClass(this, NFCActivity.class);
	        startActivity(intent);
	    }
	    
        super.onNewIntent(intent);
    }

    private boolean passwordDisplay = false;  
=======
	private boolean passwordDisplay = false;
>>>>>>> 6056e1f5d8f00de281e0e064ac4506043d5cafe5

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
				// hide password, display "."  
				passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());  
				passwordDisplay = !passwordDisplay;  
				passwordEditText.postInvalidate();  
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
	class Uploader extends AsyncTask<String, Integer, Integer> {

		// doInBackground() is the callback that specifies the actual work to be
		// done on the separate thread, as if it's executing in the background.
		@Override
		protected Integer doInBackground(String... user) {
			try {
				String result = delivery.getWebAccessor().login(user[0], user[1]);
				if (result.contains("error")) {
					// Display error message from server.
					Toast.makeText(delivery, result, Toast.LENGTH_LONG).show();
					return DeliveryApplication.LOGIN_FAIL;
				}
				else {
					if (result.equalsIgnoreCase("customer"))
						delivery.setIdentity(Identity.CUSTOMER);
					else if (result.equalsIgnoreCase("provider"))
						delivery.setIdentity(Identity.PROVIDER);
					else if (result.equalsIgnoreCase("courier"))
						delivery.setIdentity(Identity.COURIER);
					return DeliveryApplication.LOGIN_SUCCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return DeliveryApplication.WEB_ERROR;
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
		protected void onPostExecute(Integer result) {
			// login succeed, set identity and start browse activity
			if (result == DeliveryApplication.LOGIN_SUCCESS) {
				startActivity(new Intent(LoginActivity.this, BrowseActivity.class));
			}
		}
	}
}

