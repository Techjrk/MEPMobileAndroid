package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityLoginBinding;
import com.lecet.app.databinding.ActivityProjectAddNoteBinding;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.viewmodel.ProjectDetailAddNoteViewModel;

import io.realm.Realm;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddNoteActivity extends LecetBaseActivity {
    private long projectId; //TODO: use this Id to learn where to post the note to.
    ProjectDetailAddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle extras = getIntent().getExtras();
        //projectId = extras.getLong("projectId");//Get the projectId for posting note to proper file.

        setupBinding();
    }

    private void setupBinding() {
        ActivityProjectAddNoteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_note);
        viewModel = new ProjectDetailAddNoteViewModel();
        binding.setViewModel(viewModel);
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
