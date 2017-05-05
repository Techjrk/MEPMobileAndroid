package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterBiddingWithinBinding;
import com.lecet.app.viewmodel.SearchFilterBiddingWithinViewModel;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;

/**
 * Activity for Search Filter: Bidding Within
 */
public class SearchFilterBiddingWithinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ActivitySearchFilterBiddingWithinBinding sfilter =   DataBindingUtil.setContentView(this,R.layout.activity_search_filter_bidding_within);

        SearchFilterBiddingWithinViewModel viewModel =  new SearchFilterBiddingWithinViewModel(this);

        // get passed data from Intent if returning to Activity with existing user selection
        Intent intent = getIntent();
        String displayStr   = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_BIDDING_WITHIN_DISPLAY_STR);
        String daysInt      = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_BIDDING_WITHIN_DAYS_INT);
        if(displayStr != null && daysInt != null) {
            viewModel.setBiddingWithinData(displayStr, daysInt);
        }

        sfilter.setViewModel(viewModel);
    }
}
