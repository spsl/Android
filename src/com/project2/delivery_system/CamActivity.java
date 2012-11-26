package com.project2.delivery_system;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.view.Window;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import android.os.Environment;

import android.content.Context;
import android.widget.Toast;


import java.util.Date;

import android.util.Log;

import java.io.IOException;

public class CamActivity extends Activity 
implements OnClickListener, 
PictureCallback, 
AutoFocusCallback,
SurfaceHolder.Callback{

    private final String TAG = "Preview";
    
    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    
    String lastPicFile=null;
    
    Camera mCamera;
    int numberOfCameras;
    int cameraCurrentlyLocked;
    
    // The first rear facing camera
    int defaultCameraId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // do we have a camera?
        if (!getPackageManager()
            .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
          Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
              .show();
          
          finish();
        }
        
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_cam);
        
        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mSurfaceView = (SurfaceView)findViewById(R.id.surfaceView_cam);
        
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
                defaultCameraId=i;
            }
        }
        
        Button btnTmp=(Button)findViewById(R.id.button_cam_view);
        btnTmp.setOnClickListener(this);
        btnTmp=(Button)findViewById(R.id.button_cam_shot);
        btnTmp.setOnClickListener(this);
        btnTmp=(Button)findViewById(R.id.button_cam_switch);
        btnTmp.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open(cameraCurrentlyLocked);
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(90);
        mCamera.setParameters(parameters);
        
        try{mCamera.setPreviewDisplay(mHolder);}
        catch(Exception e){}
        
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
    
    public void onClick(View v){
        if(v.getId()==R.id.button_cam_view){
            System.out.println("cam_view"); 
            
            if(lastPicFile!=null){
                Intent intentBrowseFiles = new Intent(Intent.ACTION_VIEW);
                File file = new File(lastPicFile);
                
                intentBrowseFiles.setDataAndType(Uri.fromFile(file),"image/*");
                startActivity(intentBrowseFiles);
            }
        }
        else if(v.getId()==R.id.button_cam_shot){
            System.out.println("cam_shot"); 
            if (mCamera != null) {
                mCamera.autoFocus(this);
            }
        }
        else if(v.getId()==R.id.button_cam_switch){
            System.out.println("cam_switch");  
            
            if(mCamera!=null){
                
                if(cameraCurrentlyLocked>=0&&cameraCurrentlyLocked==numberOfCameras-1)
                    cameraCurrentlyLocked=0;
                else
                    cameraCurrentlyLocked=cameraCurrentlyLocked+1;
                
                onPause();
                onResume();
            }
        }
    }
    
    public void onPictureTaken(byte[] data, Camera camera){
        File pictureFileDir = getDir();

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

          
          Toast.makeText(this, "Can't create directory to save image.",
              Toast.LENGTH_LONG).show();
          return;

        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);
        
        lastPicFile=filename;
        
        try {
          FileOutputStream fos = new FileOutputStream(pictureFile);
          fos.write(data);
          fos.close();
          Toast.makeText(this, "New Image saved:" + photoFile,
              Toast.LENGTH_LONG).show();
        } catch (Exception error) {
        
          Toast.makeText(this, "Image could not be saved.",
              Toast.LENGTH_LONG).show();
        }
    }
    
    private File getDir() {
        File sdDir = Environment
          .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "test");
    }
    
    public void onAutoFocus(boolean success, Camera camera){
        camera.takePicture(null, null, this);
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
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
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        mCamera.stopPreview();
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        int temp;
        temp=w;
        w=h;
        h=temp;
        
        Camera.Size result=null;
        
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(90);
        for(Camera.Size size : parameters.getSupportedPreviewSizes()){
          if(size.width<=w&&size.height<=h){
              if(result==null)
                  result=size;
              else{
                  int resultArea=result.width*result.height;
                  if(w*h<resultArea)
                      result=size;
              }
          }  
        };
        parameters.setPreviewSize(result.width, result.height);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }
}