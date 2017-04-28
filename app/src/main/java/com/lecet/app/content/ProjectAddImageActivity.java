package com.lecet.app.content;

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
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.FROM_CAMERA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;
import static com.lecet.app.viewmodel.ProjectImageChooserViewModel.IMAGE_URI;

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
    private String uriExtra;
    private String url;
    private String imagePath;
    private long photoId;
    private String title;
    private String body;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get project ID and image data for passing to the viewmodel
        Bundle extras = getIntent().getExtras();
        projectId  = extras.getLong(PROJECT_ID_EXTRA, -1);
        fromCamera = extras.getBoolean(FROM_CAMERA);
        uriExtra   = extras.getString(IMAGE_URI);
        url        = extras.getString(IMAGE_URL_EXTRA);
        imagePath  = extras.getString(IMAGE_PATH);

        // in the case of editing an existing image look for its id, title and body
        photoId    = extras.getLong(IMAGE_ID_EXTRA, -1);
        title      = extras.getString(IMAGE_TITLE_EXTRA);
        body       = extras.getString(IMAGE_BODY_EXTRA);

        Log.d(TAG, "onCreate: projectId: " + projectId);
        Log.d(TAG, "onCreate: fromCamera: " + fromCamera);
        Log.d(TAG, "onCreate: uriExtra: " + uriExtra);
        Log.d(TAG, "onCreate: url: " + url);
        Log.d(TAG, "onCreate: imagePath: " + imagePath);
        Log.d(TAG, "onCreate: photoId: " + photoId);
        Log.d(TAG, "onCreate: title: " + title);
        Log.d(TAG, "onCreate: body: " + body);


        if(fromCamera) {
            if (imagePath != null) {
                selectedImageUri = Uri.parse(imagePath);
            }
        }
        else {
            if (uriExtra != null) {
                selectedImageUri = Uri.parse(uriExtra);
            }
            else if (url != null) {
                selectedImageUri = Uri.parse(url);
            }
        }

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());

        setupBinding(projectDomain);
    }

    private void setupBinding(ProjectDomain projectDomain) {
        ActivityProjectAddImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_image);
        if(!fromCamera) {
            viewModel = new ProjectAddImageViewModel(this, projectId, photoId, title, body, selectedImageUri, projectDomain);
        }
        else {
            if (imagePath != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, photoId, title, body, imagePath, projectDomain);
            }
            else if (uriExtra != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, photoId, title, body, uriExtra, projectDomain);
            }
            else if (url != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, photoId, title, body, url, projectDomain);
            }
        }
        binding.setViewModel(viewModel);
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
