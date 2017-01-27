package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterValueBinding;
import com.lecet.app.viewmodel.SearchFilterValueViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterValueActivity extends AppCompatActivity {
    SearchFilterValueViewModel sfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchFilterValueBinding sfilter =   DataBindingUtil.setContentView(this,R.layout.activity_search_filter_value);

        // get Value Extras
        Intent intent = getIntent();
        String min = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_VALUE_MIN);
        String max = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_VALUE_MAX);

        SearchFilterValueViewModel viewModel =  new SearchFilterValueViewModel(this);
        viewModel.setMin(min);
        viewModel.setMax(max);
        sfilter.setViewModel(viewModel);
    }
}
