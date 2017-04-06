package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.lecet.app.R;
import com.lecet.app.content.ProjectTakePhotoFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jasonm on 3/29/17.
 */
public class ProjectTakePhotoViewModel extends BaseObservable {
    private static Camera camera;
    private static final String TAG = "projTakePhotoViewModel";
    private CameraPreview cameraPreview;
    private long projectId;
    ProjectTakePhotoFragment fragment;

    public ProjectTakePhotoViewModel(ProjectTakePhotoFragment fragment, long projectId, FrameLayout frameLayout) {
        super();
        this.fragment = fragment;
        this.projectId = projectId;
        getCameraInstance();
        cameraPreview = new CameraPreview(fragment.getActivity());
        frameLayout.addView(cameraPreview);
    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");


        return mediaFile;
    }

    public static void getCameraInstance(){
        if(camera != null){
            return;
        }

        for(int i = 0; i < Camera.getNumberOfCameras(); i++) {
            try {

                camera = Camera.open(i); // attempt to get a Camera instance
                return;
            } catch (Exception e) {
                // Camera is not available (in use or does not exist)
            }
        }
        Log.e(TAG, "getCameraInstance: No Usable Camera");
        return;// returns null if camera is unavailable
    }

    public static void releaseCamera() {
        if(camera != null){
            camera.release();
            camera = null;
            Log.w(TAG, "releaseCamera: CameraReleased");
        }
    }


    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
        private static final String TAG = "CameraPreview";
        private SurfaceHolder mHolder;


        private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.e(TAG, "onPictureTaken: PictureTaken");
                //TODO: addFunctionality to this
            }
        };

        public CameraPreview(Context context) {
            super(context);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            if(camera == null) {
                getCameraInstance();
            }
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                camera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                camera.setPreviewDisplay(mHolder);
                camera.startPreview();

            } catch (Exception e){
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            releaseCamera();
        }




    }
}
