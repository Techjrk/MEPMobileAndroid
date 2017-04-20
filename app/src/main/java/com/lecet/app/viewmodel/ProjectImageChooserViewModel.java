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
import com.lecet.app.content.ProjectAddImageActivity;

import java.io.IOException;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.FROM_CAMERA;
import static com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel.RESULT_CODE_PROJECT_CAMERA_IMAGE;
import static com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel.RESULT_CODE_PROJECT_LIBRARY_IMAGE;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectImageChooserViewModel extends BaseObservable {

    private static final String TAG = "ProjectImageChooserVM";
    public static final String IMAGE_URI = "Image_URI";
    private Activity activity;
    private long projectId;
    private String picturePath;
    private Bitmap bitmap;
    private Uri selectedImageUri;
    private boolean showImagePreview;   //TODO - may not be necessary if layout can use bitmap != null instead

    public ProjectImageChooserViewModel(Activity activity, long projectId){
        this.activity = activity;
        this.projectId = projectId;
    }

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "handleOnActivityResult: requestCode: " + requestCode);
        Log.e(TAG, "handleOnActivityResult: resultCode:  " + resultCode);

        if(resultCode == RESULT_CODE_PROJECT_CAMERA_IMAGE) {
            Log.d(TAG, "handleOnActivityResult: resultCode: RESULT_CODE_PROJECT_CAMERA_IMAGE, " + resultCode);
            //TODO - maybe exit with result so as to finish the tabbed activity and jump to the preview?
            //TODO - add bitmap via path
        }
        else if(resultCode == RESULT_CODE_PROJECT_LIBRARY_IMAGE) {
            Log.d(TAG, "handleOnActivityResult: resultCode: RESULT_CODE_PROJECT_LIBRARY_IMAGE, " + resultCode);
            //TODO - maybe exit with result so as to finish the tabbed activity and jump to the preview?
            //TODO - add bitmap via path
        }
        else if(resultCode == Activity.RESULT_OK) {
            selectedImageUri = data.getData();
            Log.d(TAG, "handleOnActivityResult: resultCode: RESULT_OK, " + resultCode);
            Log.d(TAG, "handleOnActivityResult: selectedImageUri: " + selectedImageUri);
            Log.d(TAG, "handleOnActivityResult: picturePath, " + picturePath);
            if(selectedImageUri != null && !selectedImageUri.toString().isEmpty()) {
                setBitmapFromUri(selectedImageUri);
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED) {
            Log.d(TAG, "handleOnActivityResult: resultCode: RESULT_CANCELED, " + resultCode);
        }
        else Log.e(TAG, "handleOnActivityResult: resultCode NOT SUPPORTED: " + resultCode);
    }

    private void setBitmapFromUri(Uri uri) {
        //this.bitmap = BitmapFactory.decodeFile(uri.toString());

        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
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
        Log.e(TAG, "onClickUsePhoto");
        Intent intent = new Intent(this.activity, ProjectAddImageActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        intent.putExtra(FROM_CAMERA, false);    //TODO - check
        Log.d(TAG, "Sent Image Path, " + picturePath);
        intent.putExtra(IMAGE_URI, selectedImageUri.toString());
        activity.startActivity(intent);
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
