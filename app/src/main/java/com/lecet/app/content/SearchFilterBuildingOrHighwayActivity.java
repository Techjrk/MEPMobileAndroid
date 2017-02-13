package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterBuildingOrHighwayBinding;
import com.lecet.app.viewmodel.SearchFilterBuildingOrHighwayViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterBuildingOrHighwayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterBuildingOrHighwayBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_building_or_highway);

        // get B/H Extras
        Intent intent = getIntent();
        String[] bhArr = intent.getStringArrayExtra(SearchFilterMPFViewModel.EXTRA_BUILDING_OR_HIGHWAY);

        SearchFilterBuildingOrHighwayViewModel viewModel = new SearchFilterBuildingOrHighwayViewModel(this);
        if(bhArr != null) {
            viewModel.setBh(bhArr);
        }
        sfilter.setViewModel(viewModel);

    }
}
