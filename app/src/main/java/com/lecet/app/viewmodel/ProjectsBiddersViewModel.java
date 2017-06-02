package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectBiddersAdapter;
import com.lecet.app.data.models.Bid;
import com.lecet.app.domain.ProjectDomain;

import io.realm.RealmList;
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

        RealmList<Bid> bids = getResortedBids(projectID);

        initRecyclerView(bids);
    }

    private RealmList<Bid> getResortedBids(long projectID) {
        RealmResults<Bid> bids = projectDomain.fetchProjectBids(projectID);
        RealmList<Bid> bidsCopy = new RealmList<>();
        RealmList<Bid> resortedBids = new RealmList<>();
        bidsCopy.addAll(bids);

        // add sorted Bids with value more than zero first
        for(Bid bid : bidsCopy) {
            if(bid.getAmount() > 0) {
                resortedBids.add(bid);
            }
        }

        // add Bids with value of 0 last
        for(Bid bid : bidsCopy) {
            if(bid.getAmount() == 0) {
                resortedBids.add(bid);
            }
        }

        return resortedBids;
    }

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);
        sortButton.setVisibility(View.INVISIBLE);

        //Check the binding in the layout, which is not triggering the button clicks in this VM
 /*       backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick(v);
            }
        });*/

        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }


    /**
     * Adapter Data Management: Project List
     **/

    private void initRecyclerView(RealmList<Bid> data) {

        recyclerView = (RecyclerView) appCompatActivity.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        initializeAdapter(data);
    }

    private void initializeAdapter(RealmList<Bid> data) {

        listAdapter = new ProjectBiddersAdapter(data);
        recyclerView.setAdapter(listAdapter);
    }

    public void onBackButtonClick(View view) {

        appCompatActivity.onBackPressed();
    }
}
