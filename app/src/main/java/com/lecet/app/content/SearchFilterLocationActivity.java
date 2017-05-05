package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    }

}
