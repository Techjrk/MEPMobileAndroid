package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterUpdatedWithinBinding;
import com.lecet.app.viewmodel.SearchFilterBiddingWithinViewModel;
import com.lecet.app.viewmodel.SearchFilterUpdatedWithinViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterUpdatedWithinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterUpdatedWithinBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_updated_within);
        SearchFilterUpdatedWithinViewModel viewModel = new SearchFilterUpdatedWithinViewModel(this);

        // get passed data from Intent if returning to Activity with existing user selection
        Intent intent = getIntent();
        String displayStr   = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_UPDATED_WITHIN_DISPLAY_STR);
        String daysInt      = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_UPDATED_WITHIN_DAYS_INT);
        if(displayStr != null && daysInt != null) {
            viewModel.setUpdatedWithinData(displayStr, daysInt);
        }

        sfilter.setViewModel(viewModel);
       /*
        // get Updated Within Extras
        Intent intent = getIntent();
        String[] updatedWithinArr = intent.getStringArrayExtra(SearchFilterMPFViewModel.EXTRA_UPDATED_WITHIN);

        SearchFilterUpdatedWithinViewModel viewModel = new SearchFilterUpdatedWithinViewModel(this);
        if(updatedWithinArr != null) {
           // viewModel.setTime(updatedWithinArr);
        }
        sfilter.setViewModel(viewModel);*/
    }
}
