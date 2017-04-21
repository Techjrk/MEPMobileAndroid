package com.lecet.app.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.content.LecetConfirmDialogFragment;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.models.BindableString;
import com.lecet.app.data.models.PhotoPost;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.domain.ProjectDomain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectAddImageViewModel extends BaseObservable {

    private static final String TAG = "ProjectAddImageVM";

    private AppCompatActivity activity;
    private BindableString noteTitle;
    private AlertDialog alert;
	private ProjectDomain projectDomain;
    private long projectId;
    private Bitmap bitmap;
    private String imagePath;
    private String title;
    private String body = "";
    private int titleSize;

    public String getTitleSize() {
        return String.valueOf(titleSize);
    }


    public ProjectAddImageViewModel(AppCompatActivity activity, long projectId, String imagePath, ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectId = projectId;
		this.projectDomain = projectDomain;
        this.imagePath = imagePath;
        this.bitmap = BitmapFactory.decodeFile(imagePath);


        Log.d(TAG, "ProjectAddImageViewModel: imagePath: " + imagePath);

        if(imagePath != null && !imagePath.isEmpty()) {
            //TODO - switch on image path depending on whether from camera or library
        }
    }

    public ProjectAddImageViewModel(AppCompatActivity activity, long projectId, Uri uri , ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectId = projectId;
        this.projectDomain = projectDomain;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        }catch (IOException e){
            Log.e(TAG, "ProjectAddImageViewModel: " + e.getMessage());
        }

        Log.d(TAG, "ProjectAddImageViewModel: imagePath: " + imagePath);

        if(imagePath != null && !imagePath.isEmpty()) {
            //TODO - switch on image path depending on whether from camera or library
        }
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
                        Log.d(TAG, "onClick: posting image");
                        int compressionRate = 70;
                        String base64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, compressionRate);
                        while(base64Image.length() > 90000 && compressionRate > 0){
                            compressionRate -= 10;
                            base64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, compressionRate);
                        }
                        Log.e(TAG, "onClick: compressionRate: " + compressionRate );
                        PhotoPost photoPost = new PhotoPost(title, body, false, base64Image);
                        Call<ProjectPhoto> call = projectDomain.postPhoto(projectId, photoPost);
                        call.enqueue(new Callback<ProjectPhoto>() {
                            @Override
                            public void onResponse(Call<ProjectPhoto> call, Response<ProjectPhoto> response) {

                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse: image post successful");
                                    startProjectDetailActivity();
                                    //activity.setResult(RESULT_OK);
                                    activity.finish();

                                } else {
                                    Log.e(TAG, "onResponse: image post failed");

                                    // TODO: Alert HTTP call error
                                }
                            }

                            @Override
                            public void onFailure(Call<ProjectPhoto> call, Throwable t) {
                                Log.e(TAG, "onResponse: failure");

                                //TODO: Display alert noting network failure
                            }
                        });

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

        alert = new AlertDialog.Builder(view.getContext()).create();
        if(body.equals("")){//Required Content of post
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", onClick);
            alert.setMessage("Post Needs Body Text. Express yourself.");
            alert.show();
        }else {//Are you sure?
            alert.setMessage("You are about to post a public photo.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Post Photo", onClick);
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onClick);
            alert.show();
        }
    }


    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleSize = title.length();
        notifyChange();
    }

    @Bindable
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
