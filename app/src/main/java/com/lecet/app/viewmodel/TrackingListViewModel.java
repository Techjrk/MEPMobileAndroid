package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.ProjectListRecyclerViewAdapter;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * File: TrackingListViewModel
 * Created: 11/2/16
 * Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class TrackingListViewModel extends BaseObservable {

    private final static String TAG = "ProjectTrackingListVM";

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private ProjectListRecyclerViewAdapter projectListAdapter;
    private List<Project> adapterData;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;
    private Switch showUpdatesToggle;
    private boolean showUpdates = true;


    public TrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId, BidDomain bidDomain, ProjectDomain projectDomain) {

        Log.d(TAG, "Constructor");

        this.appCompatActivity = appCompatActivity;
        this.bidDomain = bidDomain;
        this.projectDomain = projectDomain;

        initShowUpdatesSwitch();
        initializeAdapter(listItemId);
    }

    private void initShowUpdatesSwitch() {
        if(this.appCompatActivity != null) {
            showUpdatesToggle = (Switch) appCompatActivity.findViewById(R.id.toggle_button);
        }
    }

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView    = (TextView)  toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView)  toolbar.findViewById(R.id.subtitle_text_view);
        backButton       = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton       = (ImageView) toolbar.findViewById(R.id.sort_menu_button);

        //TODO - check the binding in the layout, which is not triggering the button clicks in this VM
        backButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              onBackButtonClick(v);
                                          }
                                      });

        sortButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              onSortButtonClick(v);
                                          }
                                      });
        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }


    /**
     * Adapter Data Management
     **/

    private void initializeAdapter(long listItemId) {

        adapterData = new ArrayList<>();

        final TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(appCompatActivity), Realm.getDefaultInstance());
        ProjectTrackingList projectList = trackingListDomain.fetchProjectTrackingList(listItemId);

        if(projectList != null) {
            RealmList<Project> projects = projectList.getProjects();
            Project[] data = projects != null ? projects.toArray(new Project[projects.size()]) : new Project[0];

            adapterData.addAll(Arrays.asList(data));
            //projectListAdapter.notifyDataSetChanged();
        }
        else Log.w(TAG, "initializeAdapter: WARNING: projectList is null");

        recyclerView = getProjectRecyclerView(R.id.project_tracking_recycler_view);
        setupRecyclerView(recyclerView);
        projectListAdapter = new ProjectListRecyclerViewAdapter(adapterData);
        recyclerView.setAdapter(projectListAdapter);
    }

    /**
     * RecyclerView Management
     **/

    private void setupRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private RecyclerView getProjectRecyclerView(@IdRes int recyclerView) {

        return (RecyclerView) appCompatActivity.findViewById(recyclerView);
    }

    /**
     * Click handling
     **/

    public void onBackButtonClick(View view) {
        Log.d(TAG, "onBackButtonClick");
        appCompatActivity.onBackPressed();
    }

    public void onSortButtonClick(View view) {
        Log.d(TAG, "onSortButtonClick");
        Toast.makeText(appCompatActivity, "Sort button pressed", Toast.LENGTH_SHORT).show();
    }


    ///////////////////////////////
    // BINDINGS

    @Bindable
    public boolean getShowUpdates() {
        return showUpdates;
    }

    public void setShowUpdates(boolean showUpdates) {
        this.showUpdates = showUpdates;
        notifyPropertyChanged(BR.showUpdates);
        projectListAdapter.setShowUpdates(this.showUpdates);
        projectListAdapter.notifyDataSetChanged();
    }



}
