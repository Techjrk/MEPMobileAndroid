package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.ImageView;


import java.io.IOException;


/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectViewImageViewModel extends BaseObservable {

    private static final String TAG = "ProjectViewImageVM";
    private Context context;
    private long projectId;
    private Bitmap bitmap;
    private String imagePath;
    private Activity activity;

    public ProjectViewImageViewModel(Activity activity, long projectId, String imagePath) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.projectId = projectId;
        this.imagePath = imagePath;
        this.bitmap = BitmapFactory.decodeFile(imagePath);

        // orient the image rotation
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Log.e(TAG, "Constructor: Orientation: " + orientation);
            switch(orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    this.bitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    this.bitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    this.bitmap = rotateImage(bitmap, 270);
                    break;

                default:
                    break;

            }
        }
        catch(IOException e){
            Log.e(TAG, "Constructor: Error: " + e.getMessage());
        }

    }

    public Bitmap rotateImage(Bitmap image,float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    @Bindable
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @BindingAdapter("bind:imageBitmap")
    public static void loadImage(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

}
