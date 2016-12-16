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
 * This View Model is used
 */

public class SearchItemRecentViewModel {

    private Project project;
    private Company company;
    private Contact contact;
    private String mapsApiKey;
    private SearchViewModel viewModel;

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
        this.viewModel = viewModel;
    }



    ////////////////////////////////////
    // PROJECT


    public String getTitle() {
        if (project == null) return "Unknown";
        return project.getTitle();
    }

    public String getProjectName() {
        return project.getTitle();
    }

    public String getClientLocation() {
        return project.getCity() + " , " + project.getState();
    }


    ////////////////////////////////////
    // COMPANY

    public String getCompanyTitle() {
        if (company == null) return "--";
        return company.getName();
    }

    /**
     * Return 1st line street address e.g. 7215 NW 7th St
     */
    public String getCompanyAddress1() {
        if (company == null) return "--";
        return company.getAddress1();
    }

    /**
     * Return 2nd line city, state & zip+4 address e.g. Freemont, CA 10054-1234
     */
    public String getCompanyAddress2() {
        if (company == null) return "--";

        StringBuilder sb = new StringBuilder();

        // city
        if (company.getCity() != null) {
            sb.append(company.getCity());
        }

        // state
        if (company.getState() != null) {
            sb.append(", ");
            sb.append(company.getState());
        }

        // zip + 4
        if (company.getZipPlus4() != null) {
            sb.append(" ");
            String zip = company.getZipPlus4();
            if(zip.length() > 5) {
                sb.append(zip.substring(0, 5));
                sb.append("-");
                sb.append(zip.substring(5, 9));
            }
            else sb.append(company.getZipPlus4());
        }

        return sb.toString();
    }


    ////////////////////////////////////
    // CONTACT

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
    // MAP IMAGE URL - UNIVERSAL

    public String getMapUrl() {

        // project map
        if (project != null) {
            if (project.getGeocode() == null) {
                return null;
            }

            else return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                            "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                        project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
        }

        // company map
        if (company != null) {
            if (company.getCity() == null || company.getState() == null) {
                return null;
            }

            //TODO - implement
            /*else return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                            "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                        project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);*/
        }

        return null;
    }


    ////////////////////////////////////
    // CLICK HANDLERS

    public void onProjectSavedClick(View view) {
        Toast.makeText(viewModel.getActivity(), "onClick: \nProject saved detail section", Toast.LENGTH_SHORT).show();
    }

    public void onProjectClick(View view) {
        //TODO - connect to the project detail section
        Toast.makeText(viewModel.getActivity(), "onClick: \nProject detail section", Toast.LENGTH_SHORT).show();

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

