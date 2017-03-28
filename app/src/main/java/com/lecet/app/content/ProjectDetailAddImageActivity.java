package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectAddImageBinding;
import com.lecet.app.viewmodel.ProjectDetailAddImageViewModel;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddImageActivity extends LecetBaseActivity {
    private long projectId; //TODO: use this Id to learn where to post the image to.
    ProjectDetailAddImageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle extras = getIntent().getExtras();
        //projectId = extras.getLong("projectId");//Get the projectId for posting image to proper file.

        setupBinding();
    }

    private void setupBinding() {
        ActivityProjectAddImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_image);
        viewModel = new ProjectDetailAddImageViewModel();
        binding.setViewModel(viewModel);
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
