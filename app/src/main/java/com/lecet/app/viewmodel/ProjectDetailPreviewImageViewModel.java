package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.content.ProjectDetailAddImageActivity;
import com.lecet.app.content.ProjectDetailTakePhotoActivity;
import com.lecet.app.content.ProjectSelectPhotoFragment;

import java.io.IOException;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakePhotoFragment.IMAGE_PATH;


/**
 * Created by jasonm on 4/11/17.
 */

@Deprecated
public class ProjectDetailPreviewImageViewModel extends BaseObservable {

    private static final String TAG = "ProjDetailPreviewImgVM";
    private Context context;
    private long projectId;
    private Bitmap bitmap;
    private boolean fromCamera;
    private String imagePath;
    private Activity activity;

    public ProjectDetailPreviewImageViewModel(Activity activity, long projectId, boolean fromCamera, String imagePath) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.projectId = projectId;
        this.fromCamera = fromCamera;
        this.imagePath = imagePath;
        this.bitmap = BitmapFactory.decodeFile(imagePath);
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Log.e(TAG, "ProjectDetailPreviewImageViewModel: Orientation: " + orientation);
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
        }catch(IOException e){
            Log.e(TAG, "ProjectDetailPreviewImageViewModel:" + e.getMessage());
        }

    }

    public void onUseImageButtonClick(View view) {
        Log.d(TAG, "onUseImageButtonClick");

        // pass the photo data to the MPP 1.1 - Mobile Project Photo Add activity
        Intent intent = new Intent(this.context, ProjectDetailAddImageActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        intent.putExtra(IMAGE_PATH, imagePath);
        this.context.startActivity(intent);
    }

    public void onChangeImageButtonClick(View view) {
        Log.d(TAG, "onChangeImageButtonClick");

        // go back to either the Camera or Library to take or select a different photo
        Intent intent;
        if(this.fromCamera) {
            intent = new Intent(this.context, ProjectDetailTakePhotoActivity.class);
        }
        else intent = new Intent(this.context, ProjectSelectPhotoFragment.class);

        intent.putExtra(PROJECT_ID_EXTRA, projectId);

        this.context.startActivity(intent);
        activity.finish();
    }

    public Bitmap rotateImage(Bitmap image,float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    public boolean isFromCamera() {
        return fromCamera;
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
