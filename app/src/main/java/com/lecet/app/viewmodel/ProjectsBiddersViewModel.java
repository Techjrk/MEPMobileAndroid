package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectBiddersAdapter;
import com.lecet.app.data.models.Bid;
import com.lecet.app.domain.ProjectDomain;

import io.realm.RealmResults;

/**
 * File: ProjectsBiddersViewModel Created: 1/25/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectsBiddersViewModel {

    /**
     * Tool Bar
     **/
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;

    private final AppCompatActivity appCompatActivity;
    private final ProjectDomain projectDomain;

    private RecyclerView recyclerView;
    private ProjectBiddersAdapter listAdapter;


    public ProjectsBiddersViewModel(AppCompatActivity appCompatActivity, ProjectDomain projectDomain, long projectID) {
        this.appCompatActivity = appCompatActivity;
        this.projectDomain = projectDomain;

        RealmResults<Bid> bids = projectDomain.fetchProjectBids(projectID);
        initRecyclerView(bids);
    }


    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);
        sortButton.setVisibility(View.INVISIBLE);

        //TODO - check the binding in the layout, which is not triggering the button clicks in this VM
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick(v);
            }
        });

        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }


    /**
     * Adapter Data Management: Project List
     **/

    private void initRecyclerView(RealmResults<Bid> data) {

        recyclerView = (RecyclerView) appCompatActivity.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        initializeAdapter(data);
    }

    private void initializeAdapter(RealmResults<Bid> data) {

        listAdapter = new ProjectBiddersAdapter(data);
        recyclerView.setAdapter(listAdapter);
    }

    public void onBackButtonClick(View view) {

        appCompatActivity.onBackPressed();
    }
}
