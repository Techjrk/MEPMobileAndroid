package com.lecet.app.viewmodel;

import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.HiddenProjectsAdapter;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: HiddenProjectsViewModel Created: 1/31/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class HiddenProjectsViewModel extends BaseActivityViewModel {

    private final AppCompatActivity appCompatActivity;
    private final long userID;
    private final ProjectDomain projectDomain;

    private String subTitle;
    private int projectsCount;

    private RecyclerView recyclerView;
    private HiddenProjectsAdapter listAdapter;
    private List<Project> results;

    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private TextView saveButton;

    public HiddenProjectsViewModel(AppCompatActivity appCompatActivity, long userID, ProjectDomain projectDomain) {

        super(appCompatActivity);

        this.appCompatActivity = appCompatActivity;
        this.userID = userID;
        this.projectDomain = projectDomain;

        getHiddenProjects();
    }


    @Bindable
    public String getSubTitle() {

        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        notifyPropertyChanged(BR.subTitle);
    }

    private void updateSubTitle() {
        String title = String.format(appCompatActivity.getString(R.string.hidden_projects_sub_title), projectsCount);
        setSubTitle(title);
    }


    /** Networking **/

    private void getHiddenProjects() {

        showProgressDialog();

        projectDomain.getHiddenProjects(userID, new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                if (response.isSuccessful()) {

                    projectDomain.asyncCopyToRealm(response.body(), new Boolean(true), new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            results = projectDomain.getRealm().copyFromRealm(projectDomain.fetchHiddenProjects());
                            projectsCount = results.size();
                            initializeAdapter(results);
                            updateSubTitle();
                            dismissProgressDialog();

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            dismissProgressDialog();
                            showCancelAlertDialog(appCompatActivity.getString(R.string.error_network_title), error.getMessage());
                        }
                    });

                } else {

                    dismissProgressDialog();

                    showCancelAlertDialog(appCompatActivity.getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

                dismissProgressDialog();

                showCancelAlertDialog(appCompatActivity.getString(R.string.error_network_title), appCompatActivity.getString(R.string.error_network_message));
            }
        });
    }

    /**
     * Adapter Data Management: Project List
     **/

    private void initializeAdapter(List<Project> hiddenProjects) {

        recyclerView = getRecyclerView(R.id.recycler_view);
        setupRecyclerView(recyclerView);
        listAdapter = new HiddenProjectsAdapter(projectDomain, hiddenProjects, appCompatActivity.getString(R.string.google_maps_key));
        recyclerView.setAdapter(listAdapter);
    }

    /**
     * RecyclerView Management
     **/

    public void setupRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    public RecyclerView getRecyclerView(@IdRes int recyclerView) {

        return (RecyclerView) appCompatActivity.findViewById(recyclerView);
    }

    /** Toolbar **/

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        saveButton = (TextView) toolbar.findViewById(R.id.save_text_view);

        backButton.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }

}
