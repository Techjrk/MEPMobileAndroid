package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lecet.app.BR;
import com.lecet.app.data.models.SearchSaved;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchItemSavedSearchViewModel extends BaseObservable {

    private final SearchSaved searchSaved;
    private String title;
//    private final String mapsApiKey;
 //   private final String code ="this123code";

    public SearchItemSavedSearchViewModel(SearchSaved searchSaved) {
//        this.project = project;
//        this.mapsApiKey = mapsApiKey;
        this.searchSaved = searchSaved;
        this.setTitle(searchSaved.getTitle());
    }

    /////////////////////////
    // BINDABLE

    @Bindable
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
        notifyPropertyChanged(BR.title);
    }



}
