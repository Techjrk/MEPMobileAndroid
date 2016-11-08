package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectListRecyclerViewAdapter;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.LecetCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProjectTrackingListViewModel
 * Created: 11/2/16
 * Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ProjectTrackingListViewModel extends BaseObservable {

    private final static String TAG = "ProjectTrackingListVM";

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private ProjectListRecyclerViewAdapter projectListAdapter;
    private List<RealmObject> adapterData;
    private RealmResults<Project> projectListResults;
    private TextView titleTextView;
    private TextView subtitleTextView;


    public ProjectTrackingListViewModel(AppCompatActivity appCompatActivity, ProjectTrackingList projectList, BidDomain bidDomain, ProjectDomain projectDomain) {

        Log.d(TAG, "Constructor");

        this.appCompatActivity = appCompatActivity;
        this.bidDomain = bidDomain;
        this.projectDomain = projectDomain;

        initializeAdapter();

        // see MTM branch Project Tracking List Domain
        //projectListResults = Realm.getDefaultInstance().where(Project.class).equalTo("id", projectList.getId()).findAll();
        projectListResults = Realm.getDefaultInstance().where(Project.class).lessThan("id", 10000).findAll();  //TODO - temp. uses id range rather than passed list from dashboard menu
        Log.d(TAG, "Constructor: projectListResults: " + projectListResults);
        //RealmList<Project> results = new RealmList<Project>();
        //results.addAll(projectListResults.subList(0, projectListResults.size()));
        setupAdapterWithProjects(projectListResults);
    }

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView    = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);

        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }


    /**
     * Adapter Data Management
     **/

    private void initializeAdapter() {

        adapterData = new ArrayList<>();

        recyclerView = getProjectRecyclerView(R.id.project_tracking_recycler_view);
        setupRecyclerView(recyclerView);
        projectListAdapter = new ProjectListRecyclerViewAdapter(adapterData, 1);  //TODO - allow for multiple init types, default to 1 for now
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

    /*public void getTrackingListProjects(@NonNull final LecetCallback<Project[]> callback) {

        // Check if data has been recently fetched and display those results from Realm
        if (realmResultsMPL == null) {

            getProjects(new Callback<List<Project>>() {
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                    if (response.isSuccessful()) {

                        realmResultsMPL = new RealmResults<Project>();

                        // Store in Realm
                        List<Project> body = response.body();
                        projectDomain.copyToRealmTransaction(body);

                        // Fetch Realm managed Projects
                        realmResultsMPL = fetchProjects();
                        callback.onSuccess(realmResultsMPL != null ? realmResultsMPL.toArray(new Project[realmResultsMPL.size()]) : new Project[0]);

                        fetchProjects(realmResultsMPL);

                    } else {

                        callback.onFailure(response.code(), response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Project>> call, Throwable t) {

                    callback.onFailure(-1, "Network Failure");
                }
            });

        } else {

            // Fetch Realm managed Projects
            realmResultsMPL = fetchProjects();
            callback.onSuccess(realmResultsMPL != null ? realmResultsMPL.toArray(new Project[realmResultsMPL.size()]) : new Project[0]);

            setupAdapterWithProjects(realmResultsMPL);
        }
    }

    private RealmResults<Project> fetchProjects() {

//        return projectDomain.fetchProjects();
        return new RealmResults<Project>();
    }*/


    private void setupAdapterWithProjects(RealmResults<Project> realmResults) {

        Project[] data = realmResults != null ? realmResults.toArray(new Project[realmResults.size()]) : new Project[0];
        adapterData.clear();
        adapterData.addAll(Arrays.asList(data));
        projectListAdapter.setAdapterType(1);       //TODO - needed?
        projectListAdapter.notifyDataSetChanged();
    }

/*
    @Bindable
    public Project[] getProjects() {
        Project project = new Project();
        Project[] projects = new Project[1];
        projects[1] = project;
        return projects;
    }*/
/*
    @BindingAdapter({"bind:projects"})
    public static void entries(RecyclerView recyclerView, Project[] projects) {
        //TODO - set RecyclerView adapter
    }*/

}
