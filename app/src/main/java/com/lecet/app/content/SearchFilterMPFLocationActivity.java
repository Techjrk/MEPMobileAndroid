package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpflocationBinding;
import com.lecet.app.databinding.ActivitySearchFilterMps30Binding;
import com.lecet.app.viewmodel.SearchFilterMPFLocationViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchFilterMSEViewModel;

public class SearchFilterMPFLocationActivity extends AppCompatActivity {
    SearchFilterMPFViewModel sfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_filter_mpflocation);
        ActivitySearchFilterMpflocationBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpflocation);

        // get Location Extras
        Intent intent = getIntent();
        String city   = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_CITY);
        String state  = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_STATE);
        String county = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_COUNTY);
        String zip    = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_ZIP);

        SearchFilterMPFLocationViewModel viewModel = new SearchFilterMPFLocationViewModel(this);
        viewModel.setCity(city);
        viewModel.setState(state);
        viewModel.setCounty(county);
        viewModel.setZipcode(zip);
        sfilter.setViewModel(viewModel);
    }

}
