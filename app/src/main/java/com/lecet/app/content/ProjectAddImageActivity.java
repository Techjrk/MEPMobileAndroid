package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectAddImageBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectAddImageViewModel;

import io.realm.Realm;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectImageChooserActivity.PROJECT_REPLACE_IMAGE_EXTRA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.FROM_CAMERA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;
import static com.lecet.app.viewmodel.ProjectImageChooserViewModel.IMAGE_URI;
import static com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel.REQUEST_CODE_NEW_IMAGE;
import static com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel.REQUEST_CODE_REPLACE_IMAGE;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectAddImageActivity extends LecetBaseActivity {

    private static final String TAG = "ProjectAddImageAct";

    public static final String IMAGE_ID_EXTRA    = "com.lecet.app.content.ProjectAddImageActivity.image.id.extra";
    public static final String IMAGE_TITLE_EXTRA = "com.lecet.app.content.ProjectAddImageActivity.image.title.extra";
    public static final String IMAGE_BODY_EXTRA  = "com.lecet.app.content.ProjectAddImageActivity.image.body.extra";
    public static final String IMAGE_URL_EXTRA   = "com.lecet.app.content.ProjectAddImageActivity.image.url.extra";

    private ProjectAddImageViewModel viewModel;
    private long projectId;
    private boolean fromCamera;
    private String imageUri;
    private String url;
    private String imagePath;
    private long photoId;
    private String title;
    private String body;
    private Uri selectedImageUri;
    private boolean replaceImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get project ID and image data for passing to the viewmodel
        Bundle extras = getIntent().getExtras();
        projectId = extras.getLong(PROJECT_ID_EXTRA, -1);
        replaceImage = extras.getBoolean(PROJECT_REPLACE_IMAGE_EXTRA, false);
        fromCamera = extras.getBoolean(FROM_CAMERA);
        imagePath = extras.getString(IMAGE_PATH);
        imageUri = extras.getString(IMAGE_URI);
        url = extras.getString(IMAGE_URL_EXTRA);

        // in the case of editing an existing image look for its id, title and body
        photoId = extras.getLong(IMAGE_ID_EXTRA, -1);
        title = extras.getString(IMAGE_TITLE_EXTRA);
        body = extras.getString(IMAGE_BODY_EXTRA);

        Log.d(TAG, "onCreate: projectId: " + projectId);
        Log.d(TAG, "onCreate: replaceImage: " + replaceImage);
        Log.d(TAG, "onCreate: fromCamera: " + fromCamera);
        Log.d(TAG, "onCreate: imagePath: " + imagePath);
        Log.d(TAG, "onCreate: imageUri: " + imageUri);
        Log.d(TAG, "onCreate: url: " + url);
        Log.d(TAG, "onCreate: photoId: " + photoId);
        Log.d(TAG, "onCreate: title: " + title);
        Log.d(TAG, "onCreate: body: " + body);

        parseImageSource();

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        setupBinding(projectDomain);

        // immediately start the image chooser activity
        if (!replaceImage) {
            startChooserActivity();
        }
    }

    private void parseImageSource() {
        if(fromCamera) {
            if (imagePath != null) {
                selectedImageUri = Uri.parse(imagePath);
            }
        }
        else {
            if (imageUri != null) {
                selectedImageUri = Uri.parse(imageUri);
            }
            else if (url != null) {
                selectedImageUri = Uri.parse(url);
            }
        }
    }

    private void startChooserActivity() {
        Log.d(TAG, "startChooserActivity");
        Intent intent = new Intent(this, ProjectImageChooserActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        intent.putExtra(IMAGE_ID_EXTRA, photoId);
        intent.putExtra(PROJECT_REPLACE_IMAGE_EXTRA, replaceImage);
        intent.putExtra(IMAGE_TITLE_EXTRA, title);
        intent.putExtra(IMAGE_BODY_EXTRA, body);
        if (replaceImage) {
            startActivityForResult(intent, REQUEST_CODE_REPLACE_IMAGE);
        }
        else {
            startActivityForResult(intent, REQUEST_CODE_NEW_IMAGE);
        }
    }

    private void setupBinding(ProjectDomain projectDomain) {
        ActivityProjectAddImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_image);
        if(!fromCamera) {
            viewModel = new ProjectAddImageViewModel(this, projectId, replaceImage, photoId, title, body, selectedImageUri, projectDomain);
        }
        else {
            if (imagePath != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, replaceImage, photoId, title, body, imagePath, projectDomain);
            }
            else if (imageUri != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, replaceImage, photoId, title, body, imageUri, projectDomain);
            }
            else if (url != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, replaceImage, photoId, title, body, url, projectDomain);
            }
        }
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: resultCode: " + resultCode);
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode);

        if (resultCode == RESULT_OK) {
            String title       = data.getStringExtra(IMAGE_TITLE_EXTRA);
            String body        = data.getStringExtra(IMAGE_BODY_EXTRA);
            String imagePath   = data.getStringExtra(IMAGE_PATH);
            String imageUri    = data.getStringExtra(IMAGE_URI);
            boolean fromCamera = data.getBooleanExtra(FROM_CAMERA, false);

            Log.d(TAG, "onActivityResult: fromCamera: " + fromCamera);
            Log.d(TAG, "onActivityResult: imagePath: " + imagePath);
            Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
            Log.d(TAG, "onActivityResult: title: " + title);
            Log.d(TAG, "onActivityResult: body: " + body);

            this.imagePath = imagePath;
            this.imageUri = imageUri;
            this.fromCamera = fromCamera;

            // update the image path or URI as nec based on the results
            parseImageSource();

            // reinstantiate the binding and View Model
            ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
            setupBinding(projectDomain);
        }
        else {
            Log.d(TAG, "onActivityResult: canceling add image");
            finish();
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
