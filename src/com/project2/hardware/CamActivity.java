package com.project2.hardware;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.project2.delivery_system.R;

/**
 * Camera activity, used to take photo for providers
 */
public class CamActivity extends Activity implements OnClickListener,
		PictureCallback, AutoFocusCallback, SurfaceHolder.Callback {

	private final String TAG = "Preview";
	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;
	String lastPicFile = null;
	Camera mCamera;
	int numberOfCameras;
	int cameraCurrentlyLocked;
	int defaultCameraId;	// The first rear facing camera

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Do we have a camera?
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.putExtra("RESULT_STRING", lastPicFile);
			setResult(RESULT_OK, intent);
			finish();
		}

		// Hide the window title, set content view.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_cam);

		// Create a RelativeLayout container that will hold a SurfaceView,
		// and set it as the content of our activity.
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView_cam);
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);

		// Find the total number of cameras available
		numberOfCameras = Camera.getNumberOfCameras();

		// Find the ID of the default camera
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraCurrentlyLocked = i;
				defaultCameraId = i;
			}
		}

		// Set up buttons
		Button btnTmp = (Button) findViewById(R.id.button_cam_view);
		btnTmp.setOnClickListener(this);
		btnTmp = (Button) findViewById(R.id.button_cam_shot);
		btnTmp.setOnClickListener(this);
		btnTmp = (Button) findViewById(R.id.button_cam_switch);
		btnTmp.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Open the default i.e. the first rear facing camera.
		mCamera = Camera.open(cameraCurrentlyLocked);
		mCamera.setDisplayOrientation(90);
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
		parameters.setRotation(90);
		parameters.setPictureSize(640, 480);	// limit picture size
		mCamera.setParameters(parameters);
		

		try {
			// Set surface view to display camera preview.
			mCamera.setPreviewDisplay(mHolder);
		} catch (Exception e) {
		}

		mCamera.startPreview();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * Called when any of the buttons in CamActivity is pressed.
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.button_cam_view) {
			if (lastPicFile != null) {		// browse nearest token file
				Intent intentBrowseFiles = new Intent(Intent.ACTION_VIEW);
				File file = new File(lastPicFile);

				intentBrowseFiles.setDataAndType(Uri.fromFile(file), "image/*");
				startActivity(intentBrowseFiles);
			}
		} else if (v.getId() == R.id.button_cam_shot) {
			if (mCamera != null) {
				mCamera.autoFocus(this);	// focus and take picture
			}
		} else if (v.getId() == R.id.button_cam_switch) {	// switch to alternative camera
			if (mCamera != null) {
				if (cameraCurrentlyLocked >= 0
						&& cameraCurrentlyLocked == numberOfCameras - 1)
					cameraCurrentlyLocked = 0;
				else
					cameraCurrentlyLocked = cameraCurrentlyLocked + 1;

				onPause();		// restart camera and preview
				onResume();
			}
		}
	}

	/**
	 * Called when camera auto focus completed, then call takePicture() method.
	 */
	public void onAutoFocus(boolean success, Camera camera) {
		camera.takePicture(null, null, this);
	}

	/**
	 * Called when image is available after picture is taken.
	 */
	public void onPictureTaken(byte[] data, Camera camera) {
		File pictureFileDir = getDir();

		// Validate file dir
		if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
			Toast.makeText(this, "Can't create directory to save image.",
					Toast.LENGTH_LONG).show();
			return;
		}

		// Create actual file to store picture.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		String photoFile = "Picture_" + date + ".jpg";
		String filename = pictureFileDir.getPath() + File.separator + photoFile;
		File pictureFile = new File(filename);
		lastPicFile = filename;

		// Save picture.
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			fos.write(data);
			fos.close();
			Toast.makeText(this, "New Image saved:" + photoFile, Toast.LENGTH_LONG).show();
		} catch (Exception error) {
			Toast.makeText(this, "Image could not be saved.", Toast.LENGTH_LONG).show();
		}
		
		Intent intent = new Intent();
		intent.putExtra("RESULT_IMG", data);
		intent.putExtra("RESULT_STRING", lastPicFile);
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * Get place to store temporary picture.
	 * @return File object
	 */
	private File getDir() {
		File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		return new File(sdDir, "temp");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where to draw.
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
			}
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin the preview.
		mCamera.stopPreview();

		int temp;
		temp = w;
		w = h;
		h = temp;

		Camera.Size result = null;

		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setRotation(90);
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= w && size.height <= h) {
				if (result == null)
					result = size;
				else {
					int resultArea = result.width * result.height;
					if (w * h < resultArea)
						result = size;
				}
			}
		}
		
		parameters.setPreviewSize(result.width, result.height);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}
}