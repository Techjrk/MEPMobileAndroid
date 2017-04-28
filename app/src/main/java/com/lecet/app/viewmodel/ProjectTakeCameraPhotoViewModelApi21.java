package com.lecet.app.viewmodel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.content.ProjectAddImageActivity;
import com.lecet.app.content.ProjectTakeCameraPhotoFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.FROM_CAMERA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;


/*
    CAMERA2 IMPLIMENTATION ONLY TO BE USED IF SDK BUILD LVL IS 21 OR HIGHER

    TODO: CODE IS STILL BEING DISSECTED FROM LINK BELOW
    http://coderzpassion.com/android-working-camera2-api/
 */
@TargetApi(21)
public class ProjectTakeCameraPhotoViewModelApi21 extends BaseObservable {
    private static final String TAG = "CameraPhotoViewModel21";
    private final int MAX_IMAGE_SIZE = 900000;
    private static CameraPreview cameraPreview;
    private static SparseIntArray ORIENTATIONS;
    private long projectId;


    public ProjectTakeCameraPhotoViewModelApi21(Fragment fragment,TextureView textureView, long projectId) {
        cameraPreview = new CameraPreview(textureView, fragment);
        if(ORIENTATIONS == null) {
            ORIENTATIONS = new SparseIntArray();
            ORIENTATIONS.append(Surface.ROTATION_0, 90);
            ORIENTATIONS.append(Surface.ROTATION_90, 0);
            ORIENTATIONS.append(Surface.ROTATION_180, 270);
            ORIENTATIONS.append(Surface.ROTATION_270, 180);
        }
        this.projectId = projectId;

        //Look at this link https://developer.android.com/reference/android/hardware/camera2/package-summary.html
    }


    public void onTakePhotoClick(View view) {
        cameraPreview.getPicture();

    }

    public static void releaseCamera(){
        if(cameraPreview != null){
            cameraPreview.closeCamera();
        }
    }


    public class CameraPreview{
        private final String TAG = "CameraPreviewAPI21";
        /*Sizes for Display*/
        private Size previewSize;
        private Size jpegSizes[] = null;

        private Fragment fragment;
        private TextureView textureView;
        private CameraDevice cameraDevice;
        private CaptureRequest.Builder previewBuilder;
        private CameraCaptureSession previewSession;

        private CameraDevice.StateCallback stateCallback=new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {
                cameraDevice=camera;
                startCamera();
            }


            @Override
            public void onDisconnected(CameraDevice camera) {
            }
            @Override
            public void onError(CameraDevice camera, int error) {

            }
        };

        private TextureView.SurfaceTextureListener surfaceTextureListener =
                new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                openCamera();
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                closeCamera();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };

        public CameraPreview(TextureView textureView, Fragment fragment){
            this.textureView = textureView;
            this.fragment = fragment;
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }

        public void getPicture(){
            if(cameraDevice == null){
                Log.e(TAG, "getPicture: No Camera Device");
                return;
            }

            CameraManager cameraManager = (CameraManager)fragment.getActivity()
                    .getSystemService(Context.CAMERA_SERVICE);

            try{
                CameraCharacteristics cameraCharacteristics
                        = cameraManager.getCameraCharacteristics(cameraDevice.getId());

                jpegSizes = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);


                int width = 640, height = 480;//Size just in case one isn't provided
                if(jpegSizes != null && jpegSizes.length > 0){
                    width = jpegSizes[0].getWidth();
                    height = jpegSizes[0].getHeight();
                }

                ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);

                /*THIS SETS UP WHERE THE IMAGES WILL BE DISPLAYED/SAVED*/
                List<Surface> outputSurfaces = new ArrayList<Surface>();
                outputSurfaces.add(reader.getSurface());

                //ADD SEMAPHORE TO CHECK IF CAMERA IS CLOSED. LOOK AT GOOGLE EXAMPLE
                final CaptureRequest.Builder captureBuilder =
                        cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

                captureBuilder.addTarget(reader.getSurface());
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                int rotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation));

                ImageReader.OnImageAvailableListener  imageAvailableListener = new
                        ImageReader.OnImageAvailableListener() {
                            @Override
                            public void onImageAvailable(ImageReader reader) {
                                Image image = null;
                                try {//THIS IS WHERE YOU SET UP THE IMAGE TO BE SAVED TO STORAGE
                                    image = reader.acquireLatestImage();
                                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                                    byte[] bytes = new byte[buffer.capacity()];
                                    buffer.get(bytes);
                                    save(bytes);

                                }catch (Exception e){

                                } finally {
                                  if(image != null){
                                      image.close();
                                  }
                                }
                            }

                            void save(byte[] data){
                                File file=getOutputMediaFile();
                                OutputStream outputStream=null;
                                try {
                                    outputStream=new FileOutputStream(file);

                                    Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    Bitmap resizedImage = null;

                                    Log.d(TAG, "onPictureTaken: realImage w: " + realImage.getWidth());
                                    Log.d(TAG, "onPictureTaken: realImage h: " + realImage.getHeight());
                                    Log.d(TAG, "onPictureTaken: realImage size: " + realImage.getByteCount());

                                    if(realImage.getByteCount() > MAX_IMAGE_SIZE) {
                                        int resizedWidth  = realImage.getWidth() / 10;
                                        int resizedHeight = realImage.getHeight() / 10;
                                        resizedImage = Bitmap.createScaledBitmap(realImage, resizedWidth, resizedHeight, true);
                                        Log.d(TAG, "onPictureTaken: resizedImage w:  " + resizedWidth);
                                        Log.d(TAG, "onPictureTaken: resizedImage h: " + resizedHeight);
                                        Log.d(TAG, "onPictureTaken: resizedImage size: " + resizedImage.getByteCount());
                                    }

                                    int orientation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
                                    if(Surface.ROTATION_0 == orientation) {
                                        resizedImage = rotateImage(realImage, 90);
                                    }else if(Surface.ROTATION_270 == orientation){
                                        resizedImage = rotateImage(realImage, 180);
                                    }else{
                                        resizedImage = rotateImage(realImage, 0);
                                    }

                                    if(resizedImage == null) {
                                        resizedImage = realImage;
                                    }

                                    boolean writeSuccessful = resizedImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

                                    outputStream.close();

                                    if(writeSuccessful) {
                                        Log.d(TAG, "onPictureTaken: Image Write Success");
                                    }
                                    else {
                                        Log.e(TAG, "onPictureTaken: Image Write Failure");
                                    }

                                    // start next Activity
                                    String imagePath = file.getAbsolutePath();
                                    startProjectDetailAddImageActivity(imagePath);//TODO: DO we go to preview?
                                    fragment.getActivity().finish();
                                } catch (FileNotFoundException e) {
                                    Log.e(TAG, "save: " + e.getMessage());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (outputStream != null)
                                            outputStream.close();
                                    } catch (IOException e) {
                                        Log.e(TAG, "save: " + e.getMessage());
                                    }
                                }
                            }
                        };

                HandlerThread handlerThread = new HandlerThread("takePicture");
                handlerThread.start();

                final Handler handler = new Handler(handlerThread.getLooper());

                reader.setOnImageAvailableListener(imageAvailableListener, handler);


                final CameraCaptureSession.CaptureCallback previewSession
                        = new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureStarted(@NonNull CameraCaptureSession session,
                                                 @NonNull CaptureRequest request, long timestamp,
                                                 long frameNumber) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                    }

                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                   @NonNull CaptureRequest request,
                                                   @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                    }
                };

                //MAKE THE CAMERA SURFACE
                cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {

                        try{
                            session.capture(captureBuilder.build(), previewSession, handler);
                        } catch (CameraAccessException e) {
                            Log.e(TAG, "onConfigured: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, handler);

            } catch (CameraAccessException e) {
                Log.e(TAG, "getPicture: " + e.getMessage());
            } catch (NullPointerException e){
                Log.e(TAG, "getPicture: " + e.getMessage());
            }

        }

        public void openCamera(){
            CameraManager cameraManager = (CameraManager)fragment.getActivity()
                    .getSystemService(Context.CAMERA_SERVICE);
            try{
                String cameraId = cameraManager.getCameraIdList()[0];

                CameraCharacteristics characteristics
                        = cameraManager.getCameraCharacteristics(cameraId);

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                previewSize = map.getOutputSizes(SurfaceTexture.class)[0];
                //noinspection MissingPermission
                cameraManager.openCamera(cameraId, stateCallback, null);


            } catch (CameraAccessException e) {
                Log.e(TAG, "openCamera: " + e.getMessage());
            }
        }

        public void startCamera(){
            if(cameraDevice == null || !textureView.isAvailable() || previewSize == null){
                Log.e(TAG, "startCamera: Missing Required Field");
                if(cameraDevice == null) {
                    openCamera();
                }
                return;
            }

            SurfaceTexture texture = textureView.getSurfaceTexture();
            if(texture == null){
                Log.e(TAG, "startCamera: textureView was empty");
                return;
            }

            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface surface = new Surface(texture);

            try{
                previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            } catch (CameraAccessException e) {
                Log.e(TAG, "startCamera: " + e.getMessage());
            }

            previewBuilder.addTarget(surface);

            try{
                cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        previewSession = session;
                        updatePreviewChange();
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, null);
            } catch (CameraAccessException e) {
                Log.e(TAG, "startCamera: " + e.getMessage());
            }

        }

        public void closeCamera(){
            if(cameraDevice !=  null){
                cameraDevice.close();
                cameraDevice = null;
            }
        }

        public void updatePreviewChange(){
            if(cameraDevice == null){
                Log.e(TAG, "updatePreviewChange: camera not set");
                return;
            }

            previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            HandlerThread thread = new HandlerThread("changedPreview");
            thread.start();
            Handler handler = new Handler(thread.getLooper());

            try{
                previewSession.setRepeatingRequest(previewBuilder.build(), null, handler);
            } catch (CameraAccessException e) {
                Log.e(TAG, "updatePreviewChange: " + e.getMessage());
            }

        }

        private File getOutputMediaFile() {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Lecet");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            return mediaFile;
        }

        private void startProjectDetailAddImageActivity(String imagePath) {
            Intent intent = new Intent(fragment.getContext(), ProjectAddImageActivity.class);
            intent.putExtra(PROJECT_ID_EXTRA, projectId);
            intent.putExtra(FROM_CAMERA, true);
            intent.putExtra(IMAGE_PATH, imagePath);
            fragment.getActivity().startActivity(intent);
        }

        public Bitmap rotateImage(Bitmap image,float angle){
            Log.e(TAG, "rotateImage: angle of rotation:" + angle);
            int w = image.getWidth();
            int h = image.getHeight();

            Matrix mtx = new Matrix();
            mtx.setRotate(angle);

            return Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);
        }

    }
}
