package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterValueBinding;
import com.lecet.app.viewmodel.SearchFilterValueViewModel;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;

public class SearchFilterValueActivity extends AppCompatActivity {
    SearchFilterValueViewModel sfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchFilterValueBinding sfilter =   DataBindingUtil.setContentView(this,R.layout.activity_search_filter_value);

        // get Value Extras
        Intent intent = getIntent();
        String min = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_VALUE_MIN);
        String max = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_VALUE_MAX);

        SearchFilterValueViewModel viewModel =  new SearchFilterValueViewModel(this);
        viewModel.setMin(min);
        viewModel.setMax(max);
        sfilter.setViewModel(viewModel);
        setupToolbar();
    }
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
        }
    }
}
