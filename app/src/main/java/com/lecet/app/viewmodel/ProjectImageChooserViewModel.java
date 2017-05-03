package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.BR;

import java.io.IOException;

import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectImageChooserViewModel extends BaseObservable {

    private static final String TAG = "ProjectImageChooserVM";
    public static final String IMAGE_URI = "Image_URI";
    private Activity activity;
    private long projectId;
    private String title;
    private String body;
    private boolean replaceImage;
    private String picturePath;
    private Bitmap bitmap;
    private Uri selectedImageUri;
    private boolean showImagePreview;   //TODO - may not be necessary if layout can use bitmap != null instead

    public ProjectImageChooserViewModel(Activity activity, long projectId, String title, String body, boolean replaceImage){
        this.activity = activity;
        this.projectId = projectId;
        this.title = title;
        this.body = body;
        this.replaceImage = replaceImage;

        Log.d(TAG, "projectId: " + projectId);
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "body: " + body);
        Log.d(TAG, "replaceImage: " + replaceImage);
    }

    public void setBitmapFromUri(Uri uri) {
        Log.d(TAG, "setBitmapFromUri: uri: " + uri);

        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            selectedImageUri = uri;
            setBitmap(bm);
            Log.d(TAG, "setBitmapFromUri: bitmap size: " + bitmap.getByteCount());
        }
        catch (IOException e) {
            Log.e(TAG, "setBitmapFromUri: ERROR CONVERTING URI TO BITMAP: " + uri);
        }
    }

    private String getPath(Context context, Uri uri ) {
        Log.d(TAG, "GetPath: Uri, " + uri);
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst() ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onClickCancel(View view){
        Log.e(TAG, "onClickCancel");
        activity.onBackPressed();
    }

    public void onClickUsePhoto(View view){
        Log.d(TAG, "onClickUsePhoto: new post: picturePath: " + picturePath);
        Log.d(TAG, "onClickUsePhoto: new post: selectedImageUri: " + selectedImageUri);

        Intent intent = activity.getIntent();
        intent.putExtra(IMAGE_PATH, picturePath);
        intent.putExtra(IMAGE_URI, selectedImageUri.toString());
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Bindable
    public void setShowImagePreview(boolean showImagePreview) {
        Log.d(TAG, "setShowImagePreview:");
        this.showImagePreview = showImagePreview;
    }

    public boolean getShowImagePreview() {
        Log.d(TAG, "getShowImagePreview:");
        return this.showImagePreview;
    }

    @Bindable
    public Bitmap getBitmap() {
        Log.d(TAG, "getBitmap");
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        Log.d(TAG, "setBitmap");
        this.bitmap = bitmap;
        notifyPropertyChanged(BR.bitmap);
    }

    @BindingAdapter("bind:imageBitmap")
    public static void loadImage(ImageView view, Bitmap bitmap) {
        Log.d(TAG, "loadImage");
        view.setImageBitmap(bitmap);
    }

}
