package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectPreviewImageBinding;
import com.lecet.app.utility.Log;
import com.lecet.app.viewmodel.ProjectPreviewImageViewModel;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.FROM_CAMERA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;

/**
 * Created by jasonm on 4/11/17.
 */

@Deprecated
public class ProjectPreviewImageActivity extends LecetBaseActivity {

    private static final String TAG = "ProjectPreviewImageAct";

    private ProjectPreviewImageViewModel viewModel;
    private long projectId;
    private boolean fromCamera;
    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get project ID and image data for passing to the viewmodel
        Bundle extras = getIntent().getExtras();
        projectId  = extras.getLong(PROJECT_ID_EXTRA);
        imagePath  = extras.getString(IMAGE_PATH);
        fromCamera = extras.getBoolean(FROM_CAMERA);   // true if image was just created by the camera; false if from library

        Log.d(TAG, "onCreate: projectId: " + projectId + ", imagePath: " + imagePath);

        setupBinding();
    }

    private void setupBinding() {
        ActivityProjectPreviewImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_preview_image);
        viewModel = new ProjectPreviewImageViewModel(this, projectId, fromCamera, imagePath);
        binding.setViewModel(viewModel);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
