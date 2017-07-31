package com.lecet.app.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.content.widget.ControllableImageView;
import com.lecet.app.content.widget.PannableImageView;
import com.lecet.app.utility.Log;
import com.squareup.picasso.Picasso;


/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectViewPannableImageViewModel extends BaseObservable {

    private static final String TAG = "ProjectViewFullImageVM";
    private Activity activity;
    private long projectId;
    private boolean canView = false;
    private String title;
    private String body;
    private String imageUrl;
    private ControllableImageView phoneLocation;
    private PannableImageView pannableImageView;
    private ImageView mapImage;
    private boolean setBounds = false;

    private PannableImageView.OnDrawListener onDrawListener = new PannableImageView.OnDrawListener(){
        @Override
        public void onDrawCallback(Canvas canvas) {
            if(!setBounds){
                float intrinsicWidth = pannableImageView.getDrawable().getIntrinsicWidth();
                float imageWidth = (intrinsicWidth * ((float)mapImage.getHeight()/ (float)mapImage.getDrawable().getIntrinsicHeight()));
                phoneLocation.setImageWidth(imageWidth/2);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                int height = phoneLocation.getHeight();
                int width = (int)((float)height * ((float)displayMetrics.widthPixels/(float)displayMetrics.heightPixels));
                Log.d(TAG, "onDrawCallback: Width: " + width);

                phoneLocation.setViewBounds(width);
                setBounds = true;
            }
            phoneLocation.setTranslateValues(pannableImageView.getxPosPercentage(), 0);
        }
    };




    public ProjectViewPannableImageViewModel(Activity activity, long projectId, String title, String body, String imageUrl, PannableImageView pannableImageView, ImageView mapImage , ControllableImageView phoneLocation) {
        this.activity = activity;
        this.projectId = projectId;
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
        this.pannableImageView = pannableImageView;
        this.phoneLocation = phoneLocation;
        this.mapImage = mapImage;
        pannableImageView.setOnDrawListener(onDrawListener);



        Log.d(TAG, "ProjectViewFullscreenImageViewModel: ImageViewWidth" );

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

    public void onClickImage(View view){
        canView = !canView;
        notifyChange();
    }

    public void onClickBackButton(View view){
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    public int canView(){
        return canView ? View.VISIBLE : View.INVISIBLE;
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
