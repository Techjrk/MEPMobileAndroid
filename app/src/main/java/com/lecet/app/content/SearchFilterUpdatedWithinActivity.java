package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterUpdatedWithinBinding;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchFilterUpdatedWithinViewModel;

public class SearchFilterUpdatedWithinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterUpdatedWithinBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_updated_within);
        SearchFilterUpdatedWithinViewModel viewModel = new SearchFilterUpdatedWithinViewModel(this);

        // get passed data from Intent if returning to Activity with existing user selection
        Intent intent = getIntent();
        String displayStr = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_UPDATED_WITHIN_DISPLAY_STR);
        String daysInt = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_UPDATED_WITHIN_DAYS_INT);
        if (displayStr != null && daysInt != null) {
            viewModel.setUpdatedWithinData(displayStr, daysInt);
        }
        sfilter.setViewModel(viewModel);
        setupToolbar();
    }
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
        }
    }
}
