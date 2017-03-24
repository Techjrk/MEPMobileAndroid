package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.ActivityProjectAddNoteBinding;
import com.lecet.app.viewmodel.ProjectDetailAddNoteViewModel;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddNoteActivity extends LecetBaseActivity {
    private long projectId; //TODO: use this Id to learn where to post the note to.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle extras = getIntent().getExtras();
        //projectId = extras.getLong("projectId");//Get the projectId for posting note to proper file.
        ActivityProjectAddNoteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_note);
        binding.setViewModel(new ProjectDetailAddNoteViewModel());
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
