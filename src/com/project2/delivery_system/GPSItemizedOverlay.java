package com.project2.delivery_system;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import android.content.Context;
import android.app.AlertDialog;

public class GPSItemizedOverlay extends ItemizedOverlay {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    Context mContext;
    
    public GPSItemizedOverlay(Drawable defaultMarker, Context context) {
        super(boundCenterBottom(defaultMarker));
        // TODO Auto-generated constructor stub
        
        mContext = context;
    }
    
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }
    
    @Override
    protected OverlayItem createItem(int i) {
      return mOverlays.get(i);
    }

    @Override
    public int size() {
      return mOverlays.size();
    }
    
    @Override
    protected boolean onTap(int index) {
      OverlayItem item = mOverlays.get(index);
      AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
      dialog.setTitle(item.getTitle());
      dialog.setMessage(item.getSnippet());
      dialog.show();
      return true;
    }
    
}
