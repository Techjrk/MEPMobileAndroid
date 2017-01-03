package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpfjurisdictionBinding;
import com.lecet.app.viewmodel.SearchFilterMPFJurisdictionViewModel;

public class SearchFilterMPFJurisdictionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_search_filter_mpfjurisdiction);
        ActivitySearchFilterMpfjurisdictionBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfjurisdiction);
        SearchFilterMPFJurisdictionViewModel viewModel = new SearchFilterMPFJurisdictionViewModel(this);
        sfilter.setViewModel(viewModel);

    }
}
