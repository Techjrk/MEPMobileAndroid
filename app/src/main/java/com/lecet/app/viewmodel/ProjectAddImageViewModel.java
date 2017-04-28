package com.lecet.app.viewmodel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.BR;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.models.PhotoPost;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.domain.ProjectDomain;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectAddImageViewModel extends BaseObservable {

    private static final String TAG = "ProjectAddImageVM";

    private AppCompatActivity activity;
    private AlertDialog alert;
    private long projectId;
    private long photoID;
    private static Bitmap bitmap;
    private String imagePath;
    private Uri uri;
    private String title;
    private String body = "";
	private ProjectDomain projectDomain;
    private int newTitleLength = 0;
    private Target picassoTarget;


    public ProjectAddImageViewModel(AppCompatActivity activity, long projectId, long photoID, String title, String body, String imagePath, ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectId = projectId;
        this.photoID = photoID;
        this.title = title;
        this.body = body;
        this.imagePath = imagePath;
		this.projectDomain = projectDomain;

        Log.d(TAG, "Constructor 1: projectId: " + imagePath);
        Log.d(TAG, "Constructor 1: photoID: " + photoID);
        Log.d(TAG, "Constructor 1: title: " + title);
        Log.d(TAG, "Constructor 1: body: " + body);
        Log.d(TAG, "Constructor 1: imagePath: " + imagePath);

        this.bitmap = BitmapFactory.decodeFile(imagePath);  //TODO - access of static var
    }

    public ProjectAddImageViewModel(AppCompatActivity activity, long projectId, long photoID, String title, String body, Uri uri, ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectId = projectId;
        this.photoID = photoID;
        this.title = title;
        this.body = body;
        this.uri = uri;
        this.projectDomain = projectDomain;

        Log.d(TAG, "Constructor 2: projectId: " + imagePath);
        Log.d(TAG, "Constructor 2: photoID: " + photoID);
        Log.d(TAG, "Constructor 2: title: " + title);
        Log.d(TAG, "Constructor 2: body: " + body);
        Log.d(TAG, "Constructor 2: imagePath: " + imagePath);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        }
        catch (IOException e){
            Log.e(TAG, "Error converting URI to bitmap: " + e.getMessage());

            picassoTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d(TAG, "onBitmapLoaded");
                    ProjectAddImageViewModel.setBitmapData(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.e(TAG, "onBitmapFailed");

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(activity).load(uri).into(picassoTarget);

        }

        Log.d(TAG, "Constructor 2: uri: " + uri);
    }

    private void startProjectDetailActivity() {
        Log.d(TAG, "startProjectDetailActivity");

        Intent intent = new Intent(activity, ProjectDetailActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        activity.startActivity(intent);
    }

    public void onClickCancel(View view){
        Log.e(TAG, "onClickCancel");
        activity.setResult(RESULT_CANCELED);
        activity.finish();
    }

    public void onClickAdd(View view){
        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        postImage(false);
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        showAlertDialog(view, onClick);
    }

    private void postImage(boolean replaceExisting) {
        Log.d(TAG, "postImage: replaceExisting: " + replaceExisting);

        // TODO - move resizing and compression to helper method
        int compressionRate = 70;
        String base64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, compressionRate);

        // keep shrinking the image until it is small enough to be accepted by the server
        while (base64Image.length() > 70000 && compressionRate > 0) {
            compressionRate -= 10;
            base64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, compressionRate);
        }
        Log.d(TAG, "postImage: compressionRate: " + compressionRate);
        PhotoPost photoPost = new PhotoPost(title, body, false, base64Image);

        Call<ProjectPhoto> call;

        // for a new post
        if (!replaceExisting) {
            Log.d(TAG, "postImage: new image post");
            call = projectDomain.postPhoto(projectId, photoPost);
        }
        // for updating an existing post
        else {
            Log.d(TAG, "postImage: update to existing image post");
            call = projectDomain.updatePhoto(photoID, new PhotoPost(title, body, true, base64Image));
        }

        call.enqueue(new Callback<ProjectPhoto>() {
            @Override
            public void onResponse(Call<ProjectPhoto> call, Response<ProjectPhoto> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "postImage: onResponse: image post successful");
                    startProjectDetailActivity();
                    //activity.setResult(RESULT_OK);
                    activity.finish();
                }
                else {
                    Log.e(TAG, "postImage: onResponse: image post failed");
                    // TODO: Alert HTTP call error
                }
            }

            @Override
            public void onFailure(Call<ProjectPhoto> call, Throwable t) {
                Log.e(TAG, "postImage: onFailure: image post failed");
                //TODO: Display alert noting network failure
            }
        });

    }

    public void onClickUpdate(View view) {
        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        postImage(true);
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        showAlertDialog(view, onClick);
    }


    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    private void showAlertDialog(View view, DialogInterface.OnClickListener onClick) {
        alert = new AlertDialog.Builder(view.getContext()).create();

        //Required Content of image
        if(body.equals("")) {
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", onClick);
            alert.setMessage("Image content is required.");
            alert.show();
        }
        //Are you sure?
        else {
            alert.setMessage("You are about to post a public image.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Post Image", onClick);
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onClick);
            alert.show();
        }
    }

    public static void setBitmapData(Bitmap bmp) {
        Log.d(TAG, "setBitmapData");
        bitmap = bmp;
    }

    @Bindable
    public int getNewTitleLength() {
        return this.newTitleLength;
    }

    public void setNewTitleLength(int newTitleLength) {
        this.newTitleLength = newTitleLength;
        notifyPropertyChanged(BR.newTitleLength);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setNewTitleLength(title.length());
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean getEditMode() {
        return photoID > -1;
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

