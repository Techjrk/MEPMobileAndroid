package com.lecet.app.content;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityLoginBinding;
import com.lecet.app.databinding.ActivityProjectAddNoteBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.viewmodel.ProjectDetailAddNoteViewModel;

import io.realm.Realm;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddNoteActivity extends LecetBaseActivity {

    private static final String TAG = "ProjectDetailAddNoteAct";
    private long projectId; //TODO: use this Id to learn where to post the note to.
    ProjectDetailAddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectId =
        Log.d(TAG, "onCreate");
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        projectId = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);

        setupBinding(projectDomain);
    }

    private void setupBinding(ProjectDomain projectDomain) {
        ActivityProjectAddNoteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_note);
        viewModel = new ProjectDetailAddNoteViewModel(this, projectId, projectDomain);
        binding.setViewModel(viewModel);
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
