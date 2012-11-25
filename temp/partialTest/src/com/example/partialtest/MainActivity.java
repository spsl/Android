package com.example.partialtest;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;


public class MainActivity extends Activity 
implements OnClickListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnNFC=(Button)findViewById(R.id.button_nfc);
        btnNFC.setOnClickListener(this);
        
        Button btnCamera=(Button)findViewById(R.id.button_camera);
        btnCamera.setOnClickListener(this);
        
        Button btnGPS=(Button)findViewById(R.id.button_gps);
        btnGPS.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onClick(View v){
        
        if(v.getId()==R.id.button_nfc){
            Intent intent =new Intent(this, NFCActivity.class);
            startActivity(intent);
            
        }
        else if(v.getId()==R.id.button_camera){
            
            Intent intent =new Intent(this, CamActivity.class);
            
            startActivity(intent);
        }
        else if(v.getId()==R.id.button_gps){
            Intent intent =new Intent(this, GPSActivity.class);
            
            startActivity(intent);
        }
        
        return;
    }
}
