package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterBuildingOrHighwayBinding;
import com.lecet.app.viewmodel.SearchFilterBuildingOrHighwayViewModel;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;

public class SearchFilterBuildingOrHighwayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterBuildingOrHighwayBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_building_or_highway);

        // get B/H Extras
        Intent intent = getIntent();
        String[] bhArr = intent.getStringArrayExtra(SearchFilterAllTabbedViewModel.EXTRA_BUILDING_OR_HIGHWAY);

        SearchFilterBuildingOrHighwayViewModel viewModel = new SearchFilterBuildingOrHighwayViewModel(this);
        if(bhArr != null) {
         //   viewModel.setBh(bhArr);
            viewModel.setStrBH(bhArr[0]);
            viewModel.setTagBH(bhArr[1]);
        }
        sfilter.setViewModel(viewModel);

    }
}
