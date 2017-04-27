package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectAddNoteBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectAddNoteViewModel;

import io.realm.Realm;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectAddNoteActivity extends LecetBaseActivity {

    public static final String NOTE_ID_EXTRA    = "com.lecet.app.content.ProjectAddNoteActivity.note.id.extra";
    public static final String NOTE_TITLE_EXTRA = "com.lecet.app.content.ProjectAddNoteActivity.note.title.extra";
    public static final String NOTE_BODY_EXTRA  = "com.lecet.app.content.ProjectAddNoteActivity.note.body.extra";
    public static final String NOTE_EXTRA       = "com.lecet.app.content.ProjectAddNoteActivity.note.extra";

    private static final String TAG = "ProjectAddNoteAct";

    private long projectId;
    private long noteId;
    private String noteTitle;
    private String noteBody;
    ProjectAddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        projectId = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);

        // in the case of editing an existing note look for its id, title and body
        noteId    = getIntent().getLongExtra(NOTE_ID_EXTRA, -1);    //TODO - MARK - check these are put into Intent
        noteTitle = getIntent().getStringExtra(NOTE_TITLE_EXTRA);
        noteBody  = getIntent().getStringExtra(NOTE_BODY_EXTRA);

        setupBinding(projectDomain);

//        if (noteId != -1) {
//            setupBinding(projectDomain);
//        }
//        else setupBinding(projectDomain, noteId);
    }

    private void setupBinding(ProjectDomain projectDomain) {
        ActivityProjectAddNoteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_note);
        viewModel = new ProjectAddNoteViewModel(this, projectId, noteId, noteTitle, noteBody, projectDomain);
        binding.setViewModel(viewModel);
    }

    //TODO - MARK
    /*private void setupBinding(ProjectDomain projectDomain, long noteId) {
        ActivityProjectAddNoteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_note);
        viewModel = new ProjectAddNoteViewModel(this, projectId, noteId, projectDomain);
        binding.setViewModel(viewModel);
    }*/



    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
