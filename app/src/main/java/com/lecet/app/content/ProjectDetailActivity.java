package com.lecet.app.content;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectDetailAdapter;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ProjectDetailActivity extends AppCompatActivity {

    public static final String PROJECT_ID_EXTRA = "com.lecet.app.content.ProjectDetailActivity.project.id.extra";

    private ProjectDetailAdapter detailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());

        long projectId = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);

        Project project = projectDomain.fetchProjectById(projectId);

        detailAdapter = new ProjectDetailAdapter(project, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailAdapter);
    }

}
