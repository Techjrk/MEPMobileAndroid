package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

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
        viewModel.setFilterSearchSaved(searchSaved.getFilter());
        SearchFilter sf = searchSaved.getFilter().getSearchFilter();
        viewModel.setSearchFilterSearchSaved(sf);
        String st ="";
        //Project Filtering process...
        Log.d("bh","bh"+sf.getBuildingOrHighway());
        if (sf.getProjectLocation() != null) {
          //  String st = "{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":{\"projectLocation\":{\"city\":\"Annapolis\"}}}";
            String parseFilter = viewModel.parseSearchFilter(sf.getProjectLocation().toString());
            if (parseFilter !=null) {
                st = "\"projectLocation\":{" + parseFilter + "}";
                viewModel.setProjectSearchFilter2(st);

                //For Company filtering;
                st = "\"companyLocation\":{" + parseFilter + "}";
                viewModel.setCompanySearchFilter(st);
                //end for company filtering
            }
           // viewModel.setProjectSearchFilter2(sf.getProjectLocation().toString());
            Log.d("projectsearchfilter2","projectsearchfilter2"+sf.getProjectLocation().toString());
        }
        else
        if (sf.getCompanyLocation() != null) {
//            viewModel.setCompanySearchFilter(sf.getCompanyLocation().toString());
        //   String st = "{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":{\"projectLocation\":{\"city\":\"Annapolis\"}}}";
            //String companyLocation = "\"city\":\"Annapolis\"";
            //String st = "\"companyLocation\":{" + companyLocation + "}";
            String parseFilter = viewModel.parseSearchFilter(sf.getCompanyLocation().toString());
            Log.d("companyparse","companyparse:"+parseFilter);

            if (parseFilter !=null) {
                /* parse1 = parse1.replace("{", "").replace("}", "");
                Log.d("companysearchfilter2","companysearchfilter2"+parse1);
                String parseCity[] = parse1.split("=");
                if (parseCity == null) return;
                String companyLocation = "\"" + parseCity[0] + "\":" + "\"" + parseCity[1] + "\"";
                st = "\"companyLocation\":{" + companyLocation + "}"; */
                st = "\"companyLocation\":{" + parseFilter + "}";
                viewModel.setCompanySearchFilter(st);
                //For Project filtering
                st = "\"projectLocation\":{" + parseFilter + "}";
                viewModel.setProjectSearchFilter2(st);
                //end for project filtering

            }
          //  Log.d("companysearchfilter","companysearchfilter"+sf.getCompanyLocation().toString());
          //  Log.d("companysearchfilter3","companysearchfilter3"+st);
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
