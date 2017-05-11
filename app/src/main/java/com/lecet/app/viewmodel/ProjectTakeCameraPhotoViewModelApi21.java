package com.lecet.app.viewmodel;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
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
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

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

import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.FROM_CAMERA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;


/*
    CAMERA2 IMPLIMENTATION ONLY TO BE USED IF SDK BUILD LVL IS 21 OR HIGHER
    //Reference: https://developer.android.com/reference/android/hardware/camera2/package-summary.html
 */
@TargetApi(21)
public class ProjectTakeCameraPhotoViewModelApi21 extends BaseObservable {

    private static final String TAG = "CameraTakePhotoVM21";

    private final int MAX_IMAGE_SIZE = 700000;
    private final int REDUCED_IMAGE_AMT = 10;

    private static List<String> rotatedCameraManufacturers = new ArrayList<String>(Arrays.asList("samsung"));//A list of all manufacturers that have non-standard camera implimentations. Used to decide camera rotation.
    private static CameraPreview cameraPreview;
    private static SparseIntArray ORIENTATIONS;

    /* THIS MAKES IT HAPPEN ONCE IN MEMORY! GOOD FOR SETUP OF A SPARSE ARRAY*/
    static{
        ORIENTATIONS = new SparseIntArray();
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    public ProjectTakeCameraPhotoViewModelApi21(Fragment fragment,TextureView textureView) {
        cameraPreview = new CameraPreview(textureView, fragment);
    }

    public void onSwapCameraClick(View view) {
        Log.d(TAG, "onSwapCameraClick");

        cameraPreview.swapCamera();
    }

    public void onFlashClick(View view) {
        Log.d(TAG, "onFlashClick");
    }

    public void onTakePhotoClick(View view) {
        cameraPreview.getPicture();
    }

    //Releases the camera so it won't mess with any other classes, activities, or apps. static for use in any fragments/activites
    public static void releaseCamera(){
        if(cameraPreview != null){
            Log.d(TAG, "releaseCamera");
            cameraPreview.closeCamera();
        }
    }

    public int canSwap(){
        return cameraPreview.isSwapableCamera ? View.VISIBLE : View.INVISIBLE;
    }

    public  int canFlash(){
        return cameraPreview.isFlashable ? View.VISIBLE : View.INVISIBLE;
    }


    /**
     * CameraPreview: Inner Class that holds all the code that is needed for the camera preview and picture taking.
     */
    private class CameraPreview{
        private final String TAG = "CameraPreviewAPI21";

        /*Sizes for Display*/
        private Size previewSize;//The current size of the preview
        private Size jpegSizes[] = null;//the possible sizes of the jpegOutput. smallest is selected

        private int sensorOrientation;//orientation of sensor. Default is 90.
        private Fragment fragment;
        private int cameraIdInUse = 0;
        private Boolean isFlashable = false;
        private boolean isSwapableCamera = false;
        private TextureView textureView;//The textureView that is holding the camera preview
        private CameraDevice cameraDevice;//This is the actual camera that is used. it does not hold its own characteristics. us CameraCharacteristics class for that
        private CaptureRequest.Builder previewBuilder;//This creates the preview, it is called repetatively to update the visual effect
        private CameraCaptureSession previewSession;//The class that handles the capture of an image that the CameraDevice percieves

        private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {//Callback used with most camera actions.
            @Override
            public void onOpened(CameraDevice camera) {
                cameraDevice = camera;
                startCamera();
            }


            @Override
            public void onDisconnected(CameraDevice camera) {
            }

            @Override
            public void onError(CameraDevice camera, int error) {

            }
        };

        private TextureView.SurfaceTextureListener surfaceTextureListener = //Listens for changes to the SurfaceTexture which is attached to the Texture View. Lets you know when you can open the camera.
                new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openCamera(cameraIdInUse);
                previewRotation(textureView.getWidth(), textureView.getHeight());
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                openCamera(cameraIdInUse);
                previewRotation(textureView.getWidth(), textureView.getHeight());
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


        private CameraPreview(TextureView textureView, Fragment fragment){
            this.textureView = textureView;
            this.fragment = fragment;
            textureView.setSurfaceTextureListener(surfaceTextureListener);


            CameraManager cameraManager = (CameraManager)fragment.getActivity()
                    .getSystemService(Context.CAMERA_SERVICE);
            try {
                if(cameraManager.getCameraIdList().length > 1) {//Checks if there is more then one camera
                    Log.d(TAG, "CameraPreview: camera list: " + cameraManager.getCameraIdList());
                    isSwapableCamera = true;
                }
            } catch (CameraAccessException e) {
                Log.e(TAG, "CameraPreview: " + e.getMessage());
            }
        }

        private void getPicture(){//Takes the picture but also does a lot of attachement of the callbacks and listeners
            if(cameraDevice == null){
                Log.e(TAG, "getPicture: No Camera Device");
                return;
            }

            CameraManager cameraManager = (CameraManager)fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);

            try{
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraDevice.getId());

                jpegSizes = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);


                int width = 640, height = 480;//Size just in case one isn't provided
                if(jpegSizes != null && jpegSizes.length > 0){
                    width = jpegSizes[0].getWidth();
                    height = jpegSizes[0].getHeight();
                }

                ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);

                /*THIS SETS UP WHERE THE IMAGES WILL BE DISPLAYED/SAVED*/
                List<Surface> outputSurfaces = new ArrayList<Surface>();
                outputSurfaces.add(reader.getSurface());

                //POSSIBLY ADD SEMAPHORE TO CHECK IF CAMERA IS CLOSED. LOOK AT GOOGLE EXAMPLE
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
                                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();//CONVERTS TO IMAGE TO FORMAT THAT IS NORMAL
                                    byte[] bytes = new byte[buffer.capacity()];
                                    buffer.get(bytes);
                                    save(bytes);//CALLS METHOD ALSO IN THE LISTENER TO ADD TO LIBRARY

                                } finally {
                                  if(image != null){
                                      image.close();
                                  }
                                }
                            }

                            private void save(byte[] data){
                                File file=getOutputMediaFile();
                                OutputStream outputStream=null;
                                try {
                                    outputStream=new FileOutputStream(file);

                                    Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    Bitmap resizedImage;

                                    Log.d(TAG, "onPictureTaken: realImage w: " + realImage.getWidth());
                                    Log.d(TAG, "onPictureTaken: realImage h: " + realImage.getHeight());
                                    Log.d(TAG, "onPictureTaken: realImage size: " + realImage.getByteCount());

                                    if(realImage.getByteCount() > MAX_IMAGE_SIZE) {
                                        int resizedWidth  = realImage.getWidth() / REDUCED_IMAGE_AMT;
                                        int resizedHeight = realImage.getHeight() / REDUCED_IMAGE_AMT;
                                        resizedImage = Bitmap.createScaledBitmap(realImage, resizedWidth, resizedHeight, true);
                                        Log.d(TAG, "onPictureTaken: resizing: resizedImage w:  " + resizedWidth);
                                        Log.d(TAG, "onPictureTaken: resizing: resizedImage h: " + resizedHeight);
                                        Log.d(TAG, "onPictureTaken: resizing: resizedImage size: " + resizedImage.getByteCount());
                                    }


                                    int orientation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
                                    Log.e(TAG, "save: sensorOrientationAngel" + sensorOrientation);
                                    if(Surface.ROTATION_0 == orientation) {
                                        resizedImage = rotateImage(realImage, sensorOrientation);
                                    }
                                    else if(Surface.ROTATION_270 == orientation){
                                        resizedImage = rotateImage(realImage, 180);
                                    }
                                    else{
                                        resizedImage = rotateImage(realImage, 90 + sensorOrientation);
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
                                    //fragment.getActivity().finish();
                                    finishActivityWithResult(imagePath);
                                }
                                catch (FileNotFoundException e) {
                                    Log.e(TAG, "save: " + e.getMessage());
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                finally {
                                    try {
                                        if (outputStream != null)
                                            outputStream.close();//CLOSE FILE LOCATION
                                    } catch (IOException e) {
                                        Log.e(TAG, "save: " + e.getMessage());
                                    }
                                }
                            }
                        };

                HandlerThread handlerThread = new HandlerThread("takePicture");//NAME IS GENERIC LABEL JUST FOR KNOWING IF THIS PROCESS IS RUNNING ANYWHERE ELSE AND SORTING
                handlerThread.start();

                final Handler handler = new Handler(handlerThread.getLooper());

                reader.setOnImageAvailableListener(imageAvailableListener, handler);


                final CameraCaptureSession.CaptureCallback previewSession = new CameraCaptureSession.CaptureCallback() {
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
                            Log.d(TAG, "onConfigured: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, handler);

            } catch (CameraAccessException e) {
                Log.e(TAG, "getPicture: " + e.getMessage());
            }

        }

        private void toggleCameraId() {
            if(cameraIdInUse == 0) {
                cameraIdInUse = 1;
            }
            else cameraIdInUse = 0;
        }

        //SETS THE VALUES FOR cameraDevice AND SETS THE previewSize
        private void openCamera(int id){
            Log.d(TAG, "openCamera: id: " + id);

            CameraManager cameraManager = (CameraManager)fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);

            try{

                String cameraId = cameraManager.getCameraIdList()[id];

                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                isFlashable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if(isFlashable == null){
                    isFlashable = false;

                }
                Integer sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                if(sensorOrientation != null && rotatedCameraManufacturers.contains(Build.MANUFACTURER)){
                    this.sensorOrientation = sensorOrientation;
                }else{
                    this.sensorOrientation = 0;
                }

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                previewSize = map.getOutputSizes(SurfaceTexture.class)[0];

                //noinspection MissingPermission
                cameraManager.openCamera(cameraId, stateCallback, null);


            } catch (CameraAccessException e) {
                Log.e(TAG, "openCamera: " + e.getMessage());
            }
            //notifyAll();  //TODO - removed as this was causing a crash; uncertain of intention
        }

        //SETS UP THE PREVIEW USING THE previewBuilder
        private void startCamera(){
            if(cameraDevice == null || !textureView.isAvailable() || previewSize == null){
                Log.e(TAG, "startCamera: Missing Required Field");
                if(cameraDevice == null) {
                    openCamera(cameraIdInUse);
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

        private void swapCamera() {
            //TODO - add an check for if the camera is already previewing with existing camera

            releaseCamera();
            toggleCameraId();

            //Log.d(TAG, "swapCamera: " + cameraDevice.getId());
            CameraManager cameraManager = (CameraManager)fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics cc0 = null;
            CameraCharacteristics cc1 = null;

            try {
                cc0 = cameraManager.getCameraCharacteristics("0");
                Log.d(TAG, "swapCamera: cc0: " + cc0);
            }
            catch (CameraAccessException e) {
                e.printStackTrace();
            }

            try {
                cc1 = cameraManager.getCameraCharacteristics("1");
                Log.d(TAG, "swapCamera: cc1: " + cc1);
            }
            catch (CameraAccessException e) {
                e.printStackTrace();
            }

            if(cc0 != null && cc1 != null) {
                //boolean camera0isFront = (cc0.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT);
                //boolean camera0isBack  = (cc0.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK);
                //boolean camera1isFront = (cc1.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT);
                //boolean camera1isBack  = (cc1.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK);
                //Log.d(TAG, "swapCamera: cc0 is front? " + camera0isFront);
                //Log.d(TAG, "swapCamera: cc0 is back? " + camera0isBack);
                //Log.d(TAG, "swapCamera: cc1 is front? " + camera1isFront);
                //Log.d(TAG, "swapCamera: cc1 is back? " + camera1isBack);

                closeCamera();

                if (ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera(cameraIdInUse);
                }
                else {
                    Log.e(TAG, "swapCamera: No camera permissions.");
                }
            }

        }

        private void closeCamera(){
            if(cameraDevice != null) {
                Log.d(TAG, "closeCamera");
                cameraDevice.close();
                cameraDevice = null;
            }
        }

        //Updates the preview over and over giving a steady stream of feed from the camera
        private void updatePreviewChange(){
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
                Log.d(TAG, "updatePreviewChange: " + e.getMessage());
            }

        }

        //This rotates the preview of devices that do not support the natural camera architecture
        private void previewRotation(int width, int height){
            if(previewSize == null || textureView == null){
                return;
            }

            Matrix matrix = new Matrix();
            int rotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
            RectF textureRectF = new RectF(0,0, width, height);
            RectF previewRectF = new RectF(0,0, previewSize.getHeight(), previewSize.getWidth());

            float centerX = textureRectF.centerX();
            float centerY = textureRectF.centerY();

            if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
                previewRectF.offset(centerX - previewRectF.centerX(), centerY - previewRectF.centerY());

                matrix.setRectToRect(textureRectF, previewRectF, Matrix.ScaleToFit.FILL);
                float scale = Math.max((float)width / previewSize.getWidth(), (float)height / previewSize.getHeight());

                matrix.postScale(scale, scale, centerX, centerY);
                matrix.postRotate(90* (rotation - 2), centerX, centerY);
                textureView.setTransform(matrix);

            }
        }

        //Gets a new file for the use of a new
        private File getOutputMediaFile() {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Lecet");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("Lecet", "failed to create directory");
                    return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            return mediaFile;
        }


        private void finishActivityWithResult(String imagePath) {
            Intent intent = fragment.getActivity().getIntent();
            intent.putExtra(FROM_CAMERA, true);
            intent.putExtra(IMAGE_PATH, imagePath);
            fragment.getActivity().setResult(Activity.RESULT_OK, intent);
            fragment.getActivity().finish();
        }

        //Rotates images to the correct perspective. This can make images very big.
        private Bitmap rotateImage(Bitmap image,float angle){
            Log.d(TAG, "rotateImage: angle of rotation:" + angle);
            int w = image.getWidth();
            int h = image.getHeight();

            Matrix mtx = new Matrix();
            mtx.setRotate(angle);

            return Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);
        }

    }
}
