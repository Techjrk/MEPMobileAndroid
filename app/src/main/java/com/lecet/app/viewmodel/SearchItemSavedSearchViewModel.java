package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonElement;
import com.lecet.app.BR;
import com.lecet.app.data.models.SearchFilter;
import com.lecet.app.data.models.SearchSaved;

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

//TODO: Filter Parsing Process

    public void onClick(View view) {
        String query = searchSaved.getQuery();
       // viewModel.setFilterSearchSaved(searchSaved.getFilter());
        Log.d("savef","savef"+searchSaved.getFilter().toString());
        String saveString = searchSaved.getFilter().toString();
        //Processing the extraction of the search filter from the saved search project
        if (saveString.contains("projectLocation")){
            viewModel.setProjectSearchFilter(searchSaved.getFilter().toString());
            JsonElement jelement = searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("projectLocation");
           if (jelement !=null) {
               String parseFilter = searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("projectLocation").toString();
               String st = "\"companyLocation\":" + parseFilter;
               viewModel.setCompanySearchFilter(st);
               Log.d("savec","savec"+searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("projectLocation").toString());
               Log.d("savet","savet"+st);
           }

        } else {
            //Processing the extraction of the search filter from the saved search company
            viewModel.setCompanySearchFilterComplete(searchSaved.getFilter().toString());
            JsonElement jelement = searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("companyLocation");
            if (jelement !=null) {
                String parseFilter = searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("companyLocation").toString();
                String st = "\"projectLocation\":" + parseFilter;
                viewModel.setProjectSearchFilterOnly(st);
                Log.d("saves","saves"+searchSaved.getFilter().get("searchFilter").getAsJsonObject().get("companyLocation").toString());
            }
        }


        //Setting the query will refresh the display of the summary section for projects, companies and contacts
        if (query != null && query.length() > 0) {
            viewModel.setQuery(searchSaved.getQuery());
            /*viewModel.setIsMSE1SectionVisible(false);
            viewModel.setIsMSE2SectionVisible(true);*/
        } else {
            viewModel.setQuery("");
        }

        //Log.d("searchfilter","searchfilter:"+searchSaved.getFilter().getSearchFilter());
        viewModel.setIsMSE1SectionVisible(false);
        viewModel.setIsMSE2SectionVisible(true);
    }

}
