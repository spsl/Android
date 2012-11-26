package com.project2.delivery_system;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.content.IntentFilter;
import android.view.View.OnClickListener;

public class NFCActivity extends Activity implements OnClickListener{
    NfcAdapter mNfcAdapter;
    PendingIntent mPendingIntent; 
    TextView textView;
    private IntentFilter[] mFilters;
    
    final String NFC_MESSAGE_MIME="application.com.example.partialTest";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        
        textView=(TextView)findViewById(R.id.textView_nfcDisplay);
     
        Button btnBack=(Button)findViewById(R.id.button_back);
        btnBack.setOnClickListener(this);
        
        mPendingIntent= PendingIntent.getActivity(this, 0, 
                            new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter ndef=new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        
        try{
            ndef.addDataType("*/*");
        }catch (IntentFilter.MalformedMimeTypeException e){
            throw new RuntimeException("fail",e);
        }
        
        mFilters=new IntentFilter[]{ndef,};
        // Check for available NFC Adapter
        
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            System.out.println("NFC on create");
            finish();
            return;
        }
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);
        
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        System.out.println("NFC on new intent");
        String action = intent.getAction();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            
            System.out.println("NDEF Received!");
            
            textView=(TextView)findViewById(R.id.textView_nfcDisplay);
            
            Parcelable[] rawMsgs=intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            System.out.println(rawMsgs.toString());
            NdefMessage msg=(NdefMessage) rawMsgs[0];
            System.out.println(new String(msg.getRecords()[0].getId()));
            
            textView.setText(new String(msg.getRecords()[0].getPayload()));
            
        }

        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        
        // check if NFC is enabled on device
        if(!mNfcAdapter.isEnabled()){
            //TODO enable nfc
            System.out.println("NFC not enabled!");
        }
        
        if(true){
            System.out.println("Start Transmitting..");
            
            String text=("Beam me up, Android! \n\n"+"Beam Time: "+ System.currentTimeMillis());
            
            NdefMessage msg=new NdefMessage(
                    new NdefRecord[]{     
                            new NdefRecord(
                            NdefRecord.TNF_WELL_KNOWN,
                            NdefRecord.RTD_TEXT,
                            NFC_MESSAGE_MIME.getBytes(),
                            text.getBytes() )
                            }
                    );
            
            mNfcAdapter.enableForegroundNdefPush(this, msg);
            mNfcAdapter.enableForegroundDispatch(this,  mPendingIntent, mFilters, null);
        }
        
    }

    
    public void onClick(View v){
        
        if(v.getId()==R.id.button_back){
            finish();
            
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nfc, menu);
        return true;
    }

}
