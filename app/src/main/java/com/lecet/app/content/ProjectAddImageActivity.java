package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
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
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectAddImageActivity extends LecetBaseActivity {

    private static final String TAG = "ProjectAddImageAct";

    private ProjectAddImageViewModel viewModel;
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

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());

        setupBinding(projectDomain);
    }

    private void setupBinding(ProjectDomain projectDomain) {
        ActivityProjectAddImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_image);
        viewModel = new ProjectAddImageViewModel(this, projectId, imagePath, projectDomain);
        binding.setViewModel(viewModel);
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
