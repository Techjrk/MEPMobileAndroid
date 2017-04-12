package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.content.ProjectDetailActivity;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectDetailAddImageViewModel extends BaseObservable {

    private static final String TAG = "ProjectDetailAddImageVM";

    private AppCompatActivity activity;
    private long projectId;
    private Bitmap bitmap;
    private String imagePath;
    private String title;
    private String body;


    public ProjectDetailAddImageViewModel(AppCompatActivity activity, long projectId, String imagePath) {
        this.activity = activity;
        this.projectId = projectId;
        this.imagePath = imagePath;
        this.bitmap = BitmapFactory.decodeFile(imagePath);
    }


    public void onAddImageButtonClick(View view) {
        Log.d(TAG, "onAddImageButtonClick: *** needs startActivity() ***");

        Intent intent = new Intent(activity, ProjectDetailActivity.class);  //TODO - go to correct Activity
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        activity.startActivity(intent);
    }

    public void onClickCancel(View view){
        activity.onBackPressed();
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        Log.d(TAG, "setTitle: " + title);   //TODO - remove logging; for testing only
    }

    @Bindable
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        Log.d(TAG, "setBody: " + body);     //TODO - remove logging; for testing only
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
