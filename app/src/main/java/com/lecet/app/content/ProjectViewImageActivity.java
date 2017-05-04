package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectViewImageBinding;
import com.lecet.app.viewmodel.ProjectViewImageViewModel;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;

/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectViewImageActivity extends LecetBaseActivity {

    private static final String TAG = "ProjectViewImageAct";

    private ProjectViewImageViewModel viewModel;
    private long projectId;
    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get project ID and image data for passing to the viewmodel
        Bundle extras = getIntent().getExtras();
        projectId  = extras.getLong(PROJECT_ID_EXTRA);
        imagePath  = extras.getString(IMAGE_PATH);

        Log.d(TAG, "onCreate: projectId: " + projectId + ", imagePath: " + imagePath);

        setupBinding();
    }

    private void setupBinding() {
        ActivityProjectViewImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_view_image);
        viewModel = new ProjectViewImageViewModel(this, projectId, imagePath);
        binding.setViewModel(viewModel);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
