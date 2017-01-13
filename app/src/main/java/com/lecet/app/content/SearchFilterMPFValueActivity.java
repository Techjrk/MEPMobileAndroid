package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpflocationBinding;
import com.lecet.app.databinding.ActivitySearchFilterMpfvalueBinding;
import com.lecet.app.viewmodel.SearchFilterMPFValueViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterMPFValueActivity extends AppCompatActivity {
    SearchFilterMPFValueViewModel sfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchFilterMpfvalueBinding sfilter =   DataBindingUtil.setContentView(this,R.layout.activity_search_filter_mpfvalue);

        // get Value Extras
        Intent intent = getIntent();
        String min = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_VALUE_MIN);
        String max = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_VALUE_MAX);

        SearchFilterMPFValueViewModel viewModel =  new SearchFilterMPFValueViewModel(this);
        viewModel.setMin(min);
        viewModel.setMax(max);
        sfilter.setViewModel(viewModel);
    }
}
