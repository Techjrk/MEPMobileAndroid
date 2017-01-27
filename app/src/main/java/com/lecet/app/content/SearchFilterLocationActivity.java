package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterLocationBinding;
import com.lecet.app.viewmodel.SearchFilterLocationViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterLocationActivity extends AppCompatActivity {
    SearchFilterMPFViewModel sfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchFilterLocationBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_location);

        // get Location Extras
        Intent intent = getIntent();
        String city   = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_CITY);
        String state  = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_STATE);
        String county = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_COUNTY);
        String zip    = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_ZIP);

        SearchFilterLocationViewModel viewModel = new SearchFilterLocationViewModel(this);
        viewModel.setCity(city);
        viewModel.setState(state);
        viewModel.setCounty(county);
        viewModel.setZipcode(zip);
        sfilter.setViewModel(viewModel);
    }

}
