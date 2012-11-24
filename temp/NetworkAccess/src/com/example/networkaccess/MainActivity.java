package com.example.networkaccess;

import java.io.BufferedReader;
import org.apache.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//jsoup
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et = (EditText) findViewById(R.id.editText1);
        final Button b = (Button) findViewById(R.id.button1);
        final TextView tv = (TextView) findViewById(R.id.textView1);
        
        
        b.setOnClickListener(new OnClickListener() {
       

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			try{
				URL url = null;
				url = new URL(et.getText().toString());
				URLConnection conn = url.openConnection();
				BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				String name = "";
				String password = "";
				int offset1;
				int offset2 = 0;
				int offset3;
		
				while ((line = read.readLine()) != null){
					if (line.contains("itemID")) {
						offset1 = line.indexOf("itemID=");
						offset2 = line.indexOf("description=");
										
						//get the item ID
						tv.append(line.substring(offset1+7, offset2-1));
						offset3 = line.indexOf("</a", offset2);
										
						//get the item description
						tv.append(line.substring(offset2+12, offset3));
					}
						
					if (line.contains("user=")) {
						    
							offset1 = line.indexOf("user=");
							offset2 = line.indexOf("password=");
							
							// get the user name
							tv.append(line.substring(offset1+5, offset2-1));
							offset3 = line.indexOf("</a", offset2);
							
							// get the user password
							tv.append(line.substring(offset2+9, offset3));
							
			
						}
				}
			}	
			catch (Exception e){
				
			}
		
			}
			});
        }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
