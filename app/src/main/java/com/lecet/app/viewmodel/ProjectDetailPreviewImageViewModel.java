package com.lecet.app.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.content.ProjectDetailAddImageActivity;
import com.lecet.app.content.ProjectDetailTakePhotoActivity;
import com.lecet.app.content.ProjectSelectPhotoFragment;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakePhotoFragment.IMAGE_PATH;


/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectDetailPreviewImageViewModel extends BaseObservable {

    private static final String TAG = "ProjDetailPreviewImgVM";
    private Context context;
    private long projectId;
    private Bitmap bitmap;
    private boolean fromCamera;
    private String imagePath;

    public ProjectDetailPreviewImageViewModel(Context context, long projectId, boolean fromCamera, String imagePath) {
        this.context = context;
        this.projectId = projectId;
        this.fromCamera = fromCamera;
        this.imagePath = imagePath;
        this.bitmap = BitmapFactory.decodeFile(imagePath);
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
