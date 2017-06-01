package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.content.ProjectViewFullscreenImageActivity;
import com.squareup.picasso.Picasso;

import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_BODY_EXTRA;
import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_TITLE_EXTRA;
import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_URL_EXTRA;
import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;


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
    private boolean canView = false;

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

    public void onWhiteSpaceClick(View view){
        canView = !canView;
        notifyChange();
    }

    public int canView(){
        return canView ? View.VISIBLE : View.INVISIBLE;
    }

    public void onImageClick(View view) {
        Log.d(TAG, "onImageClick");

        if(!canView){
            onWhiteSpaceClick(view);
        }else {
            Intent intent = new Intent(activity, ProjectViewFullscreenImageActivity.class);
            intent.putExtra(PROJECT_ID_EXTRA, projectId);
            intent.putExtra(IMAGE_TITLE_EXTRA, title);
            intent.putExtra(IMAGE_BODY_EXTRA, body);
            intent.putExtra(IMAGE_URL_EXTRA, imageUrl);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public void onCancelClick(View view){
        activity.finish();
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
