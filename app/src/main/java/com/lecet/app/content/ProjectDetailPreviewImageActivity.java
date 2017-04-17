package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectPreviewImageBinding;
import com.lecet.app.viewmodel.ProjectDetailPreviewImageViewModel;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakePhotoFragment.IMAGE_PATH;
import static com.lecet.app.content.ProjectTakePhotoFragment.FROM_CAMERA;

/**
 * Created by jasonm on 4/11/17.
 */

@Deprecated
public class ProjectDetailPreviewImageActivity extends LecetBaseActivity {

    private static final String TAG = "ProjDetailPreviewImgAct";

    private ProjectDetailPreviewImageViewModel viewModel;
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
        viewModel = new ProjectDetailPreviewImageViewModel(this.getBaseContext(), projectId, fromCamera, imagePath);
        binding.setViewModel(viewModel);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
