package com.lecet.app.viewmodel;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchResult;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchItemRecentViewModel {

    //TODO - support either Project or Company data

    private final String code = "this123code";
    private Project project;
    private String mapsApiKey;
    private Company company;
    private Contact contact;

    private SearchViewModel viewModel;

    public String getTitle() {
        if (project == null) return "Unknown";
        return project.getTitle();
    }

    public String getAddress() {
        if (project == null) return "Unknown";
        return project.getAddress1();
    }

    public String getCode() {
        return code;
    }

    /**
     * old code - for reference
     * public SearchItemRecentViewModel(Project project, String mapsApiKey) {
     * this.project = project;
     * this.mapsApiKey = mapsApiKey;
     * }
     */
    public SearchItemRecentViewModel(SearchViewModel viewModel, Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
        this.viewModel = viewModel;
    }

    public SearchItemRecentViewModel(SearchViewModel viewModel, Company company, String mapsApiKey) {
        this.company = company;
        this.mapsApiKey = mapsApiKey;
        this.viewModel = viewModel;
    }

    public SearchItemRecentViewModel(SearchViewModel viewModel, Contact contact) {
        this.contact = contact;
        this.mapsApiKey = mapsApiKey;
        this.viewModel = viewModel;
    }

    public String getProjectName() {
        return project.getTitle();
    }

    public String getClientLocation() {
        return project.getCity() + " , " + project.getState();
    }

    public String getMapUrl() {

        if (project == null || project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }

    //For Company
    public String getCompanyTitle() {
        if (company == null) return "--";
        return company.getName();
    }

    public String getCompanyAddress() {
        if (company == null) return "--";
        return company.getAddress1();
    }

    //For Contact
    public String getContactName() {
        if (contact == null) return "Unknown";
        if (contact.getName() == null) return "Unknown";
        if (contact.getName().equals("null")) return "";
        return contact.getName();
    }

    public String getContactDetail() {
        if (contact == null) return "Unknown";
        if (contact.getTitle() == null) return "";
        if (contact.getFipsCounty() == null) return "";
        if (contact.getTitle().equals("null")) return "";
        if (contact.getFipsCounty().equals("null")) return "";
        return contact.getTitle() + ", " + contact.getFipsCounty();
    }
    ////////////////////////////////////
    // CLICK HANDLERS

    public void onProjectSavedClick(View view) {
        Toast.makeText(viewModel.getActivity(), "onClick: \nProject saved detail section", Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {
        //TODO - connect to the project detail section
        Toast.makeText(viewModel.getActivity(), "onClick: Project detail section", Toast.LENGTH_SHORT).show();

    }

    public void onCompanyClick(View view) {
        //TODO - connect to the company detail section
        Toast.makeText(viewModel.getActivity(), "onClick: \nCompany detail section", Toast.LENGTH_SHORT).show();

//        Toast.makeText(viewModel.getActivity(), "onClick: \nProject id: " + project.getId(),Toast.LENGTH_SHORT).show();
    }

    public void onContactClick(View view) {
        //TODO - connect to the contact detail section
        Toast.makeText(viewModel.getActivity(), "onClick: Contact detail section", Toast.LENGTH_SHORT).show();
//        Toast.makeText(viewModel.getActivity(), "onClick: \nProject id: " + project.getId(),Toast.LENGTH_SHORT).show();
    }

}

