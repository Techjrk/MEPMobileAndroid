package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterBuildingOrHighwayBinding;
import com.lecet.app.viewmodel.SearchFilterBuildingOrHighwayViewModel;

public class SearchFilterBuildingOrHighwayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter_building_or_highway);
        ActivitySearchFilterBuildingOrHighwayBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_building_or_highway);
        SearchFilterBuildingOrHighwayViewModel viewModel = new SearchFilterBuildingOrHighwayViewModel(this);
        sfilter.setViewModel(viewModel);

    }
}
