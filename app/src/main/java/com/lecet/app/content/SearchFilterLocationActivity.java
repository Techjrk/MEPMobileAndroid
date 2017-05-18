package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterLocationBinding;
import com.lecet.app.viewmodel.SearchFilterLocationViewModel;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;

public class SearchFilterLocationActivity extends AppCompatActivity {
    SearchFilterAllTabbedViewModel sfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchFilterLocationBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_location);

        // get Location Extras
       /* Intent intent = getIntent();
        String city   = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_LOCATION_CITY);
        String state  = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_LOCATION_STATE);
        String county = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_LOCATION_COUNTY);
        String zip    = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_LOCATION_ZIP);*/

        SearchFilterLocationViewModel viewModel = new SearchFilterLocationViewModel(this);
        /*viewModel.setCity(city);
        viewModel.setState(state);
        viewModel.setCounty(county);
        viewModel.setZipcode(zip);
        */
        sfilter.setViewModel(viewModel);
        setupToolbar();
    }
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();
            //View searchBarView = inflater.inflate(R.layout.projects_near_me_search_bar_layout, null);
            //viewModel.setToolbar(searchBarView);
            //  actionBar.setCustomView(searchBarView);
            //actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowCustomEnabled(false);
        }
    }
}
