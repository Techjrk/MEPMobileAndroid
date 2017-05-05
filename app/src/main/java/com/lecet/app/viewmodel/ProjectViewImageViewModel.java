package com.lecet.app.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectViewImageViewModel extends BaseObservable {

    private static final String TAG = "ProjectViewImageVM";
    private Activity activity;
    private long projectId;
    private String title;
    private String body;
    private String imageUrl;

    public ProjectViewImageViewModel(Activity activity, long projectId, String title, String body, String imageUrl) {
        this.activity = activity;
        this.projectId = projectId;
        this.imageUrl = imageUrl;

        this.title = title;
        this.body = body;

        Log.d(TAG, "Constructor: projectId: " + projectId);
        Log.d(TAG, "Constructor: title: " + title);
        Log.d(TAG, "Constructor: body: " + body);
        Log.d(TAG, "Constructor: imageUrl: " + imageUrl);
    }

    public Bitmap rotateImage(Bitmap image,float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @BindingAdapter("bind:projectImageUrl")
    public static void loadImage(ImageView view, String url) {
        Log.d(TAG, "loadImage: url: " + url);

        Picasso.with(view.getContext()).load(url).placeholder(null).into(view);
    }

}
