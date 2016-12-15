package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.RelativeLayout;
import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.SearchSaved;

/**
 * File: SearchItemRecentViewModel Created: 10/17/16 Author: domandtom
 *
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
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    /**
     * setItemClickListener - used to display the MSE 2.0 layout
     */

    public void setItemClickListener(final SearchSaved item, View view) {
        final RelativeLayout viewsavedsection = (RelativeLayout) view.findViewById(R.id.item_saved_searches);

        viewsavedsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewModel.setSearchfield(item.getQuery().trim());
                viewModel.setQuery("");
            }
        });

    }
}
