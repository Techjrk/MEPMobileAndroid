package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectViewFullscreenImageBinding;
import com.lecet.app.utility.Log;
import com.lecet.app.viewmodel.ProjectViewFullscreenImageViewModel;

import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_BODY_EXTRA;
import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_TITLE_EXTRA;
import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_URL_EXTRA;
import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectViewFullscreenImageActivity extends LecetBaseActivity {

    private static final String TAG = "ProjectFullViewImageAct";

    private ProjectViewFullscreenImageViewModel viewModel;
    private long projectId;
    private String title;
    private String body;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get project ID and image data for passing to the viewmodel
        Bundle extras = getIntent().getExtras();
        projectId  = extras.getLong(PROJECT_ID_EXTRA);
        title = extras.getString(IMAGE_TITLE_EXTRA);
        body = extras.getString(IMAGE_BODY_EXTRA);
        url = extras.getString(IMAGE_URL_EXTRA);

        Log.d(TAG, "onCreate: projectId: " + projectId + ", url: " + url);

        setupBinding();
    }

    private void setupBinding() {
        ActivityProjectViewFullscreenImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_view_fullscreen_image);
        viewModel = new ProjectViewFullscreenImageViewModel(this, projectId, title, body, url);
        binding.setViewModel(viewModel);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
