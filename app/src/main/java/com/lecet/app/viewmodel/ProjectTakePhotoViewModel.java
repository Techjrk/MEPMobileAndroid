package com.lecet.app.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lecet.app.R;
import com.lecet.app.content.ProjectDetailAddImageActivity;
import com.lecet.app.content.ProjectDetailPreviewImageActivity;
import com.lecet.app.content.ProjectTakePhotoFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakePhotoFragment.FROM_CAMERA;
import static com.lecet.app.content.ProjectTakePhotoFragment.IMAGE_PATH;


/**
 * Created by jasonm on 3/29/17.
 * TODO - replace deprecatd Camera functionality with a more current implementation
 * TODO - make sure Camera is released in all cases (use, cancel, close app, etc)
 */
public class ProjectTakePhotoViewModel extends BaseObservable /*implements Camera.PictureCallback*/ {
    private static Camera camera;
    private static final String TAG = "ProjTakePhotoViewModel";
    private CameraPreview cameraPreview;
    private long projectId;
    private String imagePath;
    private ProjectTakePhotoFragment fragment;


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

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
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

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");

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

    /*@Override
    public void onPictureTaken(byte[] data, Camera camera) {

        File pictureFile = getOutputMediaFile();
        if(pictureFile == null) {
            Log.d(TAG, "onPictureTaken: Error creating media file, check storage permissions.");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Log.d(TAG, "onPictureTaken: Picture saved.");
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "onPictureTaken: File Not Found: " + e.getMessage());
        }
        catch (IOException e) {
            Log.d(TAG, "onPictureTaken: Error accessing file: " + e.getMessage());
        }

    }*/

    /*@Deprecated
    private void startImagePreviewActivity(String imagePath) {
        Intent intent = new Intent(fragment.getContext(), ProjectDetailPreviewImageActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        intent.putExtra(FROM_CAMERA, true);
        intent.putExtra(IMAGE_PATH, imagePath);
        fragment.getActivity().startActivity(intent);
    }*/

    private void startProjectDetailAddImageActivity() {
        Intent intent = new Intent(fragment.getContext(), ProjectDetailAddImageActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        intent.putExtra(FROM_CAMERA, true);
        intent.putExtra(IMAGE_PATH, imagePath);
        fragment.getActivity().startActivity(intent);
    }

    /**
     * Click Events
     */
    public void onClickCancel(View view) {
        Log.e(TAG, "onClickCancel");
        fragment.getActivity().finish();
    }

    public void onTakePhotoClick(View view) {
        Log.e(TAG, "onTakePhotoClick");
        if(camera != null) {
            camera.takePicture(null, null, new CameraPreview(this.fragment.getContext()));
        }

    }

    /**
     * CameraPreview Inner Class
     */
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback {

        private static final String TAG = "CameraPreview";
        private SurfaceHolder mHolder;

        /*private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.e(TAG, "onPictureTaken: PictureTaken");
                //TODO: addFunctionality to this

                File pictureFile = getOutputMediaFile();
                if(pictureFile == null) {
                    Log.d(TAG, "onPictureTaken: Error creating media file, check storage permissions.");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    Log.d(TAG, "onPictureTaken: Picture saved.");
                }
                catch (FileNotFoundException e) {
                    Log.d(TAG, "onPictureTaken: File Not Found: " + e.getMessage());
                }
                catch (IOException e) {
                    Log.d(TAG, "onPictureTaken: Error accessing file: " + e.getMessage());
                }

            }
        };*/

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

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken ***");

            File imageFile = getOutputMediaFile();
            if(imageFile == null) {
                Log.e(TAG, "onPictureTaken ***: Error creating media file, check storage permissions.");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                fos.write(data);
                fos.close();
                Log.d(TAG, "onPictureTaken ***: Picture saved.");

                // start Preview Image Activity
                imagePath = imageFile.getAbsolutePath();
                startProjectDetailAddImageActivity();
                //TODO FINISH WITH RESULT WITH IMAGEPATH
            }
            catch (FileNotFoundException e) {
                Log.e(TAG, "onPictureTaken ***: File Not Found: " + e.getMessage());
            }
            catch (IOException e) {
                Log.e(TAG, "onPictureTaken ***: Error accessing file: " + e.getMessage());
            }
            finally {
                camera.release();
            }

        }
    } // end inner class
}
