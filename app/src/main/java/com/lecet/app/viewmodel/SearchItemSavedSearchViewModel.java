package com.lecet.app.viewmodel;

import com.google.gson.JsonElement;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.utility.Log;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchItemSavedSearchViewModel extends BaseObservable {
    private SearchSaved searchSaved;
    private String title;
    private SearchViewModel viewModel;

    public SearchItemSavedSearchViewModel(SearchViewModel viewModel, SearchSaved searchSaved) {
        this.viewModel = viewModel;
        this.searchSaved = searchSaved;
        this.setTitle(searchSaved.getTitle());
    }

    /////////////////////////
    // BINDABLE

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    ////////////////////////////////////
    // CLICK HANDLERS

    /**
     * Click event for selecting the Saved Search item
     * @param view
     */
    public void onClick(View view) {
        String query = searchSaved.getQuery();
        // viewModel.setFilterSearchSaved(searchSaved.getFilter());
        Log.d("savef", "savef" + searchSaved.getFilter().toString());
        String saveString = searchSaved.getFilter().toString();

        //Processing the extraction of the search filter from the saved search project
        //Applying directly the whole search filter for the project **new
        viewModel.setProjectSearchFilter(searchSaved.getFilter().toString());

        //If the extracted search filter contains companyLocation, then apply the search filter directly to company
        if ((saveString.contains("companyLocation"))) {
            //Processing the extraction of the search filter from the saved search company
            viewModel.setCompanySearchFilterComplete(searchSaved.getFilter().toString());
            JsonElement jelement = searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("companyLocation");

            //We will also provide a company search filter to the project.
            // If not needed, just delete the whole if-condition below for project
            if (jelement != null) {
                String parseFilter = searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("companyLocation").toString();
                String st = "\"projectLocation\":" + parseFilter;
                viewModel.setProjectSearchFilterOnly(st);
            }
        } else {
            //if the search filter is for project only, then we will provide a project location filter for company.
            // If this is not needed, then just delete the code inside the else statement.
            JsonElement jelement = searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("projectLocation");
            if (jelement != null) {
                String parseFilter = searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("projectLocation").toString();
                String st = "\"companyLocation\":" + parseFilter;
                viewModel.setCompanySearchFilter(st);
            }
        }

        //Setting the query will refresh the display of the summary section for projects, companies and contacts
        if (query != null && query.length() > 0) {
            viewModel.setQuery(searchSaved.getQuery());
        } else {
            viewModel.setQuery("");
        }
        viewModel.setIsMSE1SectionVisible(false);
        viewModel.setIsMSE2SectionVisible(true);
    }

}
