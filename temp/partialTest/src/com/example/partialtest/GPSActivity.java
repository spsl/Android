package com.example.partialtest;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.drawable.Drawable;
import java.util.List;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MyLocationOverlay;

public class GPSActivity extends MapActivity {
    
    MyLocationOverlay myLoc;
    OverlayItem myLocItem=null;
    MapView mapView;
    GPSItemizedOverlay itemizedoverlay;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setBuiltInZoomControls(true);
        
        myLoc=new MyLocationOverlay(this,mapView);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        itemizedoverlay = new GPSItemizedOverlay(drawable, this);
        
        GeoPoint point = new GeoPoint(19240000,-99120000);
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");

        GeoPoint point2 = new GeoPoint(35410000, 139460000);
        OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!", "I'm in Japan!");
        
        mapOverlays.add(itemizedoverlay);
        mapOverlays.add(myLoc);
        itemizedoverlay.addOverlay(overlayitem);
        itemizedoverlay.addOverlay(overlayitem2);
        
        }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        myLoc.disableMyLocation();
        myLoc.disableCompass();
        
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        myLoc.enableMyLocation();
        myLoc.enableCompass();

        super.onResume();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gps, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        return super.onOptionsItemSelected(item);
    }
    
}
