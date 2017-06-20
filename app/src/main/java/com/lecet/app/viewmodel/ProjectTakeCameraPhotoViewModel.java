package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.lecet.app.content.ProjectTakeCameraPhotoFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.FROM_CAMERA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;


/**
 * Created by jasonm on 3/29/17.
 */
public class ProjectTakeCameraPhotoViewModel extends BaseObservable {

    private static final String TAG = "ProjCameraTakePhotoVM";

    private final int MAX_IMAGE_SIZE = 700000;
    private final int REDUCED_IMAGE_AMT = 10;

    private static Camera camera;
    private CameraPreview cameraPreview;
    private static boolean setup = false;
    private String imagePath;
    private ProjectTakeCameraPhotoFragment fragment;
    private FrameLayout frameLayout;
    private OrientationEventListener orientationEventListener = null;


    public ProjectTakeCameraPhotoViewModel(ProjectTakeCameraPhotoFragment fragment, FrameLayout frameLayout) {
        super();
        this.frameLayout = frameLayout;
        this.fragment = fragment;
        getCameraInstance();
        cameraPreview = new CameraPreview(fragment.getActivity());
        frameLayout.addView(cameraPreview);
        orientationEventListener = cameraPreview.createOrientationListener();

    }

    public void resetCamera(){
        if(setup) {
            getCameraInstance();
            frameLayout.removeView(cameraPreview);
            cameraPreview = new CameraPreview(fragment.getActivity());
            frameLayout.addView(cameraPreview);
            if (orientationEventListener == null) {
                orientationEventListener = cameraPreview.createOrientationListener();
            } else {
                orientationEventListener.enable();
            }
        }
    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Lecet");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Lecet", "failed to create directory");
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

    private void finishActivityWithResult(String imagePath) {
        Intent intent = fragment.getActivity().getIntent();
        intent.putExtra(FROM_CAMERA, true);
        intent.putExtra(IMAGE_PATH, imagePath);
        fragment.getActivity().setResult(Activity.RESULT_OK, intent);
        fragment.getActivity().finish();
    }


    /**
     * Click Events
     */
    public void onClickCancel(View view) {
        Log.d(TAG, "onClickCancel");
        fragment.getActivity().finish();
    }

    public void onTakePhotoClick(View view) {
        Log.d(TAG, "onTakePhotoClick");
        camera.takePicture(null, null, cameraPreview);

    }

    /**
     * CameraPreview Inner Class
     */
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback {

        private Configuration configuration;
        private static final String TAG = "CameraPreview";
        private SurfaceHolder mHolder;
        private Camera.CameraInfo cameraInfo;
        private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback(){
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    camera.takePicture(null, null, cameraPreview);
                }
            }
        };

        private OrientationEventListener createOrientationListener() {
            return new OrientationEventListener(fragment.getActivity()) {
                public void onOrientationChanged(int orientation) {
                    try {
                        if (orientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
                            setCameraDisplayOrientation(fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation());
                        }
                    } catch (Exception e) {
                        Log.w(TAG, "Error while onOrientationChanged", e);
                    }
                }
            };
        }

        public CameraPreview(Context context) {
            super(context);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            setup = true;
        }

        public void surfaceCreated(SurfaceHolder holder) {
            if(camera == null) {
                getCameraInstance();
            }
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                orientationEventListener.enable();
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

        public void setCameraDisplayOrientation(int orientation){
            int degrees = 0;
            if(Surface.ROTATION_0 == orientation){
                camera.setDisplayOrientation(90);
            }else if(Surface.ROTATION_270 == orientation){
                camera.setDisplayOrientation(180);
            }else {
                camera.setDisplayOrientation(0);
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

                Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap resizedImage = null;

                Log.d(TAG, "onPictureTaken: realImage w: " + realImage.getWidth());
                Log.d(TAG, "onPictureTaken: realImage h: " + realImage.getHeight());
                Log.d(TAG, "onPictureTaken: realImage size: " + realImage.getByteCount());

                if(realImage.getByteCount() > MAX_IMAGE_SIZE) {
                    int resizedWidth  = realImage.getWidth() / REDUCED_IMAGE_AMT;
                    int resizedHeight = realImage.getHeight() / REDUCED_IMAGE_AMT;
                    resizedImage = Bitmap.createScaledBitmap(realImage, resizedWidth, resizedHeight, true);
                    Log.d(TAG, "onPictureTaken: resizedImage w:  " + resizedWidth);
                    Log.d(TAG, "onPictureTaken: resizedImage h: " + resizedHeight);
                    Log.d(TAG, "onPictureTaken: resizedImage size: " + resizedImage.getByteCount());
                }

                if(resizedImage == null) {
                    resizedImage = realImage;
                }

                int orientation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
                if(Surface.ROTATION_0 == orientation) {
                    resizedImage = rotateImage(resizedImage, 90);
                }
                else if(Surface.ROTATION_270 == orientation){
                    resizedImage = rotateImage(resizedImage, 180);
                }
                else {
                    resizedImage = rotateImage(resizedImage, 0);
                }

                boolean writeSuccessful = resizedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                fos.close();

                if(writeSuccessful) {
                    Log.d(TAG, "onPictureTaken: Image Write Success");
                }
                else {
                    Log.e(TAG, "onPictureTaken: Image Write Failure");
                }

                // finish Activity with the imagePath extra
                imagePath = imageFile.getAbsolutePath();
                finishActivityWithResult(imagePath);
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



        public Bitmap rotateImage(Bitmap image,float angle){
            int w = image.getWidth();
            int h = image.getHeight();

            Matrix mtx = new Matrix();
            mtx.setRotate(angle);

            return Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);
        }

    } // end inner class
}
