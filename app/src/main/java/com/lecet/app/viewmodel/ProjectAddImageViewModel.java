package com.lecet.app.viewmodel;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.BR;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.ProjectImageChooserActivity;
import com.lecet.app.data.models.PhotoPost;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.domain.ProjectDomain;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectImageChooserActivity.PROJECT_REPLACE_IMAGE_EXTRA;
import static com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel.REQUEST_CODE_ASK_PERMISSIONS;
import static com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel.REQUEST_CODE_REPLACE_IMAGE;

/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectAddImageViewModel extends BaseObservable {

    private static final String TAG = "ProjectAddImageVM";

    private final int MAX_IMAGE_DATA_SIZE = 100000;
    private final int MIN_IMAGE_W = 1000;
    private final int MIN_IMAGE_H = 1000;

    private AppCompatActivity activity;
    private AlertDialog alert;
    private long projectId;
    private boolean replaceImage;
    private long photoID;
    private static Bitmap bitmap;   //TODO - does this need to be static?
    private String imagePath;
    private Uri uri;
    private String title;
    private String body = "";
	private ProjectDomain projectDomain;
    private int newTitleLength = 0;
    private Target picassoTarget;


    public ProjectAddImageViewModel(AppCompatActivity activity, long projectId, boolean replaceImage, long photoID, String title, String body, String imagePath, ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectId = projectId;
        this.replaceImage = replaceImage;
        this.photoID = photoID;
        this.title = title;
        this.body = body;
        this.imagePath = imagePath;
		this.projectDomain = projectDomain;

        Log.d(TAG, "Constructor 1: projectId: " + projectId);
        Log.d(TAG, "Constructor 1: replaceImage: " + replaceImage);
        Log.d(TAG, "Constructor 1: photoID: " + photoID);
        Log.d(TAG, "Constructor 1: title: " + title);
        Log.d(TAG, "Constructor 1: body: " + body);
        Log.d(TAG, "Constructor 1: imagePath: " + imagePath);

        this.bitmap = BitmapFactory.decodeFile(imagePath);  //TODO - access of static var

    }

    public ProjectAddImageViewModel(AppCompatActivity activity, long projectId, boolean replaceImage, long photoID, String title, String body, Uri uri, ProjectDomain projectDomain, final float neededRotation) {
        this.activity = activity;
        this.projectId = projectId;
        this.replaceImage = replaceImage;
        this.photoID = photoID;
        this.title = title;
        this.body = body;
        this.uri = uri;
        this.projectDomain = projectDomain;

        Log.d(TAG, "Constructor 2: projectId: " + projectId);
        Log.d(TAG, "Constructor 2: replaceImage: " + replaceImage);
        Log.d(TAG, "Constructor 2: photoID: " + photoID);
        Log.d(TAG, "Constructor 2: title: " + title);
        Log.d(TAG, "Constructor 2: body: " + body);
        Log.d(TAG, "Constructor 2: uri: " + uri);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        }
        catch (IOException e){
            Log.e(TAG, "IOException. Error converting URI to bitmap: " + e.getMessage());

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
        catch (RuntimeException e1) {
            Log.e(TAG, "ProjectAddImageViewModel: RuntimeException. Error converting URI to bitmap: " + uri);
        }
        if(bitmap != null){
            bitmap = rotateImage(bitmap, neededRotation);
        }
    }

    private void startProjectDetailActivity() {
        Log.d(TAG, "startProjectDetailActivity");

        Intent intent = new Intent(activity, ProjectDetailActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        activity.startActivity(intent);
    }

    public void onClickCancel(View view) {
        Log.d(TAG, "onClickCancel");
        activity.setResult(RESULT_CANCELED);
        activity.finish();
    }

    public void onClickReplaceImage(View view) {
        Log.d(TAG, "onClickReplaceImage");
        if (canSetup()) {
            Intent intent = new Intent(activity, ProjectImageChooserActivity.class);    //TODO - launch Chooser Activity, which immediately launches
            intent.putExtra(PROJECT_ID_EXTRA, projectId);
            intent.putExtra(PROJECT_REPLACE_IMAGE_EXTRA, true);

            activity.startActivityForResult(intent, REQUEST_CODE_REPLACE_IMAGE);
        }
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

        showPostPhotoAlertDialog(view, onClick);
    }

    public void onClickDelete(View view) {
        DialogInterface.OnClickListener onClickDeleteListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deletePhoto();
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

        showDeletePhotoAlertDialog(view, onClickDeleteListener);
    }

    private boolean canSetup(){
        if(Build.VERSION.SDK_INT >= 23) {
            List<String> permissionNeeded = new ArrayList<String>();//list of permissions that aren't allowed

            if(!hasPermission(Manifest.permission.CAMERA)){//has Camera permissions
                permissionNeeded.add(Manifest.permission.CAMERA);
            }

            if(!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                permissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            Log.d(TAG, "canSetup: NeededPermission(s) = " + permissionNeeded.size());

            if(permissionNeeded.size() > 0) {
                String[] tempList = new String[permissionNeeded.size()];//TODO: write actual converter from List<String> to String[]
                ActivityCompat.requestPermissions(activity, permissionNeeded.toArray(tempList), REQUEST_CODE_ASK_PERMISSIONS);
            }else {
                return true;//none were needed
            }

            return false;//if less then 1 then there are no permissions so return true. you can now set up
        }
        return true;
    }

    private boolean hasPermission(String permission){
        if(ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED){
            return false;
        }
        return  true;
    }



    private void postImage(boolean replaceExisting) {
        Log.d(TAG, "postImage: replaceExisting: " + replaceExisting);

        Log.d(TAG, "postImage: encoding to base64...");
        String base64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 70);
        Log.d(TAG, "postImage: encoded length: " + base64Image.length());

        Log.d(TAG, "postImage: compressing...");
        //String compressedImage = compressImageData(base64Image);
        String compressedImage = resizeBase64Image(base64Image);
        Log.d(TAG, "postImage: compressed base64 image length: " + compressedImage.length());

        PhotoPost photoPost = new PhotoPost(title, body, true, compressedImage);

        Call<ProjectPhoto> call;

        // for a new post
        if (!replaceExisting) {
            Log.d(TAG, "postImage: new image post");
            call = projectDomain.postPhoto(projectId, photoPost);
        }
        // for updating an existing post
        else {
            Log.d(TAG, "postImage: update to existing image post");
            call = projectDomain.updatePhoto(photoID, new PhotoPost(title, body, true, compressedImage));
        }

        call.enqueue(new Callback<ProjectPhoto>() {
            @Override
            public void onResponse(Call<ProjectPhoto> call, Response<ProjectPhoto> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "postImage: onResponse: image post successful");
                    activity.setResult(RESULT_OK);
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

    private void deletePhoto() {
        Log.d(TAG, "deletePhoto");

        Call<ProjectPhoto> call = projectDomain.deletePhoto(photoID);

        call.enqueue(new Callback<ProjectPhoto>() {
            @Override
            public void onResponse(Call<ProjectPhoto> call, Response<ProjectPhoto> response) {

                if (response.isSuccessful()) {

                    Log.d(TAG, "deletePhoto: onResponse: photo deletion successful");
                    activity.setResult(RESULT_OK);
                    activity.finish();

                } else {
                    Log.e(TAG, "deletePhoto: onResponse: photo deletion failed");
                    // TODO: Alert HTTP call error
                }
            }

            @Override
            public void onFailure(Call<ProjectPhoto> call, Throwable t) {
                Log.e(TAG, "deletePhoto: onFailure: photo deletion failed");
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

        showPostPhotoAlertDialog(view, onClick);
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    /*private String compressImageData(String imageData) {
        int compressionRate = 90;

        while (imageData.length() > MAX_IMAGE_DATA_SIZE && compressionRate > 0) {
            Log.d(TAG, "compressImageData: compressionRate: " + compressionRate);
            Log.d(TAG, "compressImageData: data length: " + imageData.getBytes().length);
            compressionRate -= 10;
            imageData = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, compressionRate);
        }

        Log.d(TAG, "compressImageData: final jpeg compressionRate: " + compressionRate);
        Log.d(TAG, "compressImageData: final jpeg data length: " + imageData.getBytes().length);
        return imageData;
    }*/

    private String resizeBase64Image(String base64image) {

        Log.d(TAG, "resizeBase64Image: original length: " + base64image.length());

        // if already small enough, don't recompress further
        if(base64image.length() <= MAX_IMAGE_DATA_SIZE) {
            Log.d(TAG, "resizeBase64Image: image already small enough, not resizing further");
            return base64image;
        }

        // decode the passed data
        byte [] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);

        // options for BitmapFactory
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true; //TODO - deprecated in API v21

        // create a decoded bitmap from the passed data
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);

        // create new bitmap at specific scaled-down size
        int compressionRate = 50;
        int h = image.getHeight();
        int w = image.getWidth();
        float aspectRatio = (float)w/(float)h; //Multiply this by the new H to get the new W

        int newH = 400;
        int newW = (int)(newH * aspectRatio);
        Log.d(TAG, "resizeBase64Image: aspectRatio: " + aspectRatio);

        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        byte [] byteArray;
        String compressedBase64Image = null;

        // increase dimensions and quality until just before the image data is too large to post
        for(int i=1; i<20; i++) {
            compressionRate += 1;
            newH += 100;
            newW = (int)(newH * aspectRatio);
            Log.d(TAG, "resizeBase64Image: newWidth: " + newW);
            image = Bitmap.createScaledBitmap(image, newW, newH, false);

            // compress
            image.compress(Bitmap.CompressFormat.JPEG, compressionRate, baos);
            Log.d(TAG, "resizeBase64Image: width:  " + newW);
            Log.d(TAG, "resizeBase64Image: height: " + newH);
            Log.d(TAG, "resizeBase64Image: compressionRate: " + compressionRate);
            //Log.d(TAG, "resizeBase64Image: data length: " + image.getByteCount());

            byteArray = baos.toByteArray();
            System.gc();

            String nextLargerSizeImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            Log.d(TAG, "resizeBase64Image: nextLargerSizeImage length: " + nextLargerSizeImage.length());
            if(nextLargerSizeImage.length() > MAX_IMAGE_DATA_SIZE) {
                Log.d(TAG, "resizeBase64Image: compressedBase64Image length: " + compressedBase64Image.length());
                return compressedBase64Image;
            }
            else compressedBase64Image = nextLargerSizeImage;
        }

        return  compressedBase64Image;
    }

    private void showPostPhotoAlertDialog(View view, DialogInterface.OnClickListener onClick) {
        alert = new AlertDialog.Builder(view.getContext()).create();

        //Required Content of image
        if(getBitmap() == null || getBitmap().getByteCount() == 0) {
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

    private void showDeletePhotoAlertDialog(View view, DialogInterface.OnClickListener onClick) {
        alert = new AlertDialog.Builder(view.getContext()).create();

        //Are you sure?
        alert.setMessage("Are you sure you want to delete this photo?");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", onClick);
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onClick);
        alert.show();
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

    public boolean canDelete() {
        return replaceImage;
    }

    @Bindable
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @BindingAdapter("{imageBitmap}")
    public static void loadImage(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    //Rotates images to the correct perspective. This can make images very big.
    private Bitmap rotateImage(Bitmap image,float angle){
        Log.d(TAG, "rotateImage: angle of rotation:" + angle);
        int w = image.getWidth();
        int h = image.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(angle);

        Bitmap bm = Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);
        Log.d(TAG, "rotateImage: width: " + bm.getWidth());
        Log.d(TAG, "rotateImage: height: " + bm.getHeight());
        return bm;

    }


}

