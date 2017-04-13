package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectAddImageBinding;
import com.lecet.app.viewmodel.ProjectDetailAddImageViewModel;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectTakePhotoFragment.IMAGE_PATH;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddImageActivity extends LecetBaseActivity {

    private static final String TAG = "ProjectDetailAddNoteAct";

    private ProjectDetailAddImageViewModel viewModel;
    private long projectId;
    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get project ID and image data for passing to the viewmodel
        Bundle extras = getIntent().getExtras();
        projectId = extras.getLong(PROJECT_ID_EXTRA, -1);
        imagePath = extras.getString(IMAGE_PATH);

        Log.d(TAG, "onCreate: projectId: " + projectId + ", imagePath: " + imagePath);

        setupBinding();
    }

    private void setupBinding() {
        ActivityProjectAddImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_image);
        viewModel = new ProjectDetailAddImageViewModel(this, projectId, imagePath);
        binding.setViewModel(viewModel);
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
