package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.content.ContactDetailActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.ProjectsNearMeActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.SearchDomain;

import java.io.IOException;
import java.io.Serializable;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 * This code is copyright (c) 2016 Dom & Tom Inc.
 * This View Model is used
 */

public class BidItemViewModel extends BaseObservable {

    private Project project;
    private Company company;
    private Contact contact;
    private String mapsApiKey;
    private boolean isClientLocation2;
    SearchDomain searchDomain;
    ProjectsNearMeViewModel viewModel;

    public BidItemViewModel(Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
        searchDomain= new SearchDomain(LecetClient.getInstance(), Realm.getDefaultInstance());
    //    this.viewModel =viewModel;

    }


    ////////////////////////////////////
    // PROJECT

    public String getTitle() {
        //    if (project == null) return "Unknown";
        return project != null ? project.getTitle() : company != null ? company.getName() : "Unknown";
    }

    public String getProjectName() {
        return project.getTitle();
    }

    public String getClientLocation() {
        return project != null ? project.getCity() + " , " + project.getState() : company != null ? company.getAddress1() : "";
    }

    public String getClientLocation2() {
        if (project != null && project.getAddress2() != null && !project.getAddress2().trim().equals(""))
            setIsClientLocation2(true);
        else setIsClientLocation2(false);
        return project != null ? project.getAddress2() : "";
    }

    @Bindable
    public boolean getIsClientLocation2() {
        return isClientLocation2;
    }

    @Bindable
    public void setIsClientLocation2(boolean clientLocation2) {
        isClientLocation2 = clientLocation2;
    }

    ////////////////////////////////////
    // MAP IMAGE URL - UNIVERSAL

    public String getMapUrl() {

        // project map
        if (project != null) {
            if (project.getGeocode() == null) {
                return null;
            } else
                return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                                "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                        project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
        }

        // company map
        if (company != null) {
            if (company.getCity() == null || company.getState() == null) {
                return null;
            }

            String mapStr = null;
            StringBuilder sb = new StringBuilder();
            sb.append("https://maps.googleapis.com/maps/api/staticmap");
            sb.append("?zoom=16");
            sb.append("&size=200x200");
            sb.append("&markers=color:blue|");
            if (company.getAddress1() != null) sb.append(company.getAddress1() + ",");
            if (company.getAddress2() != null) sb.append(company.getAddress2() + ",");
            if (company.getCity() != null) sb.append(company.getCity() + ",");
            if (company.getState() != null) sb.append(company.getState());
            sb.append("&key=" + mapsApiKey);
            //mapStr = String.format((sb.toString().replace(" ", "+")), null);
            mapStr = sb.toString().replace(" ", "+");
            return mapStr;
        }

        return null;
    }

    ////////////////////////////////////
    // CLICK HANDLERS

/*
    public void onProjectSavedClick(View view) {
        if (viewModel !=null) viewModel.setDetailVisible(true);
        if (project == null) {
            onCompanyClick(view);
            return;
        }
        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
        Log.d("projectsaved","projectsaved");
       // saveRecentlyProject(SearchActivity.USER_ID,LecetSharedPreferenceUtil.getInstance(getContext()));
        view.getContext().startActivity(intent);
    }
*/

//event for clicking the Saved Search Project Detail item
    public void onProjectClick(View view) {
       // if (viewModel !=null) viewModel.setDetailVisible(true);
        if (project == null) return;
        Intent intent = new Intent(view.getContext(), ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
      //  viewModel.setDetailVisible(true);
        view.getContext().startActivity(intent);
    }

}

