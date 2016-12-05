package com.lecet.app.viewmodel;

import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchSaved;

/**
 * File: SearchItem1ViewModel Created: 10/17/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchItemSavedSearchViewModel {

    private final SearchSaved saved;
//    private final String mapsApiKey;
 //   private final String code ="this123code";

    public String getTitle(){
         return saved.getTitle();
    }
    public SearchItemSavedSearchViewModel(SearchSaved saved) {
//        this.project = project;
//        this.mapsApiKey = mapsApiKey;
        this.saved = saved;
    }




}
