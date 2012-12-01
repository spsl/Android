package com.project2.delivery_system;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import android.content.Context;
import android.app.AlertDialog;

/**
 * GPS item overlay, display customized marker in map.
 */
@SuppressWarnings("rawtypes")
public class GPSItemizedOverlay extends ItemizedOverlay {

	// mOverlays list stores each of the OverlayItem objects placed on the map
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;

	public GPSItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));

		mContext = context;
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate(); // read each of the OverlayItem objects and prepare them to be drawn.
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i); // called by populate(), return OverlayItem.
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
