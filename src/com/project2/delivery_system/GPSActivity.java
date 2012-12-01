package com.project2.delivery_system;

import android.os.Bundle;
import android.view.MenuItem;
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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setBuiltInZoomControls(true);	// enable zoom in, zoom out
        myLoc = new MyLocationOverlay(this, mapView);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_action_search);
        itemizedoverlay = new GPSItemizedOverlay(drawable, this);
        
        GeoPoint point = new GeoPoint(19240000, -99120000);
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        GeoPoint point2 = new GeoPoint(35410000, 139460000);
        OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!", "I'm in Japan!");
        
        mapOverlays.add(itemizedoverlay);	// add two class (both child of Overlay) to display in map
        mapOverlays.add(myLoc);
        
        itemizedoverlay.addOverlay(overlayitem);	// add two items for interacting
        itemizedoverlay.addOverlay(overlayitem2);
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
    
}
