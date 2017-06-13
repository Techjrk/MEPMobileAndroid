package com.lecet.app.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.BR;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectImageChooserViewModel extends BaseObservable {

    private static final String TAG = "ProjectImageChooserVM";
    public static final String IMAGE_URI = "Image_URI";
    public static final String NEEDED_ROTATION = "Needed_Rotation";
    public static final List<String> ROGUE_CAMERA_MANUFACTURERS
            = new ArrayList<String>(Arrays.asList("samsung"));

    private Activity activity;
    private boolean replaceImage;
    private String picturePath;
    private float neededRotation = 0;
    private Uri selectedImageUri;
    private Bitmap bitmap;

    public ProjectImageChooserViewModel(Activity activity, boolean replaceImage){
        this.activity = activity;
        this.replaceImage = replaceImage;

        Log.d(TAG, "replaceImage: " + replaceImage);
    }

    public void setBitmapFromUri(Uri uri) {
        Log.d(TAG, "setBitmapFromUri: uri: " + uri);

        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            selectedImageUri = uri;


            Log.e(TAG, "setBitmapFromUri: URI PATH, " + uri.getHost());
            neededRotation = (float)getImageRotation(activity, selectedImageUri);
            bm = rotateImage(bm, neededRotation);
            setBitmap(bm);
            Log.d(TAG, "setBitmapFromUri: bitmap size: " + bitmap.getByteCount());
        }
        catch (IOException e) {
            Log.e(TAG, "setBitmapFromUri: ERROR CONVERTING URI TO BITMAP: " + uri);
        }
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = 0;
        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(column[0]);
        }else{
            return "null";
        }

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    private String getPath(Context context, Uri uri ) {
        Log.d(TAG, "getPath: Uri: " + uri);
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst() ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onClickCancel(View view){
        Log.e(TAG, "onClickCancel");
        activity.onBackPressed();
    }

    public void onClickUsePhoto(View view){
        Log.d(TAG, "onClickUsePhoto: new post: picturePath: " + picturePath);
        Log.d(TAG, "onClickUsePhoto: new post: selectedImageUri: " + selectedImageUri);

        Intent intent = activity.getIntent();
        intent.putExtra(IMAGE_PATH, picturePath);
        intent.putExtra(IMAGE_URI, selectedImageUri.toString());
        intent.putExtra(NEEDED_ROTATION, neededRotation);
        Log.d(TAG, "onClickUsePhoto: Needed Rotation " + neededRotation);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public boolean getShowImagePreview() {
        Log.d(TAG, "getShowImagePreview:");
        return selectedImageUri != null;
    }

    @Bindable
    public Bitmap getBitmap() {
        Log.d(TAG, "getBitmap");
        return bitmap;
    }

    public Uri getSelectedImageUri() {
        return selectedImageUri;
    }

    public void setBitmap(Bitmap bitmap) {
        Log.d(TAG, "setBitmap");
        this.bitmap = bitmap;
        notifyPropertyChanged(BR.bitmap);
    }

    @BindingAdapter("{imageBitmap}")
    public static void loadImage(ImageView view, Bitmap bitmap) {
        Log.d(TAG, "loadImage");
        view.setImageBitmap(bitmap);
    }

    //Rotates images to the correct perspective. This can make images very big.
    private Bitmap rotateImage(Bitmap image,float angle){
        Log.d(TAG, "rotateImage: angle of rotation:" + angle);
        int w = image.getWidth();
        int h = image.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(angle);

        return Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public int getImageRotation(Context context, Uri uri) {//returns actual degress needed to rotate image
        if(Build.VERSION.SDK_INT >= 19 && ROGUE_CAMERA_MANUFACTURERS.contains(Build.MANUFACTURER)){
            if(DocumentsContract.isDocumentUri(activity, uri)){
                String uriPath = getRealPathFromURI(activity, uri);
                if(uriPath.toLowerCase().contains("camera")){
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface((uriPath));
                    } catch (IOException e) {
                        Log.e(TAG, "getImageRotation: IOEXception " + e.getMessage());
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            return 270;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            return 180;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            return 90;
                    }
                }
            }
        }
        return 0;
    }

    public int getRotationFromMediaStore(Context context, Uri imageUri) {
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
        if (cursor == null) return 0;

        cursor.moveToFirst();

        int orientationColumnIndex = cursor.getColumnIndex(columns[1]);
        Log.d(TAG, "getRotationFromMediaStore: Orientation " + orientationColumnIndex);
        return cursor.getInt(orientationColumnIndex);
    }

    private int exifToDegrees(int exifOrientation) {
        Log.d(TAG, "exifToDegrees: Exif Orientation " + exifOrientation);
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

}
