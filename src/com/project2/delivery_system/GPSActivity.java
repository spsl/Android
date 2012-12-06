package com.project2.delivery_system;

import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import java.util.List;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MyLocationOverlay;

/**
 * GPS activity for our application.
 */
public class GPSActivity extends MapActivity {
    
    MyLocationOverlay myLoc;
    OverlayItem myLocItem = null;
    MapView mapView;
    GPSItemizedOverlay itemizedoverlay;
    
    private String orderID;
    private String orderStatus;
    private String orderUser;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setBuiltInZoomControls(true);	// enable zoom in, zoom out
        myLoc = new MyLocationOverlay(this, mapView);
        
        
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_action_search);
        itemizedoverlay = new GPSItemizedOverlay(drawable, this);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        mapOverlays.add(itemizedoverlay);   // add two class (both child of Overlay) to display in map
        mapOverlays.add(myLoc);
        
        Bundle bundle = getIntent().getExtras();
        updateLocations(bundle);
       
    }
    
    @Override
    public void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        Bundle bundle = intent.getExtras();
        
        updateLocations(bundle);
        
        setIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
    	myLoc.disableMyLocation();
    	myLoc.disableCompass();
        
        super.onPause();
    }

    @Override
    protected void onResume() {
        myLoc.enableMyLocation();
    	myLoc.enableCompass();

        super.onResume();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;	// no route is displayed in the map
    }

    @Override
    protected boolean isLocationDisplayed() {
        return myLoc.isMyLocationEnabled();
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    
    // update locations in the map
    private void updateLocations(Bundle bundle){

        if(bundle!=null) {
            orderID = bundle.getString("orderID");
            orderStatus = bundle.getString("orderStatus");
            orderUser = bundle.getString("orderUser");
                        
            GeoPoint point = new GeoPoint(bundle.getInt("locX"), bundle.getInt("locY"));
            OverlayItem overlayitem = new OverlayItem(point, "Courier", "Order ID:"+orderID);
            
            itemizedoverlay.clearOverlay();
            itemizedoverlay.addOverlay(overlayitem);  
        }
    }
}
