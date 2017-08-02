package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterStageBinding;
import com.lecet.app.viewmodel.SearchFilterStageViewModel;

/**
 * Activity for Search Filter: Stage
 */
public class SearchFilterStageActivity extends AppCompatActivity {
    private SearchFilterStageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterStageBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_stage);
        viewModel = new SearchFilterStageViewModel(this);
        sfilter.setViewModel(viewModel);
        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
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
