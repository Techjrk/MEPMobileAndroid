package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterProjectTypeBinding;
import com.lecet.app.viewmodel.SearchFilterProjectTypeViewModel;


/**
 * Activity for Search Filter: Project Type
 */
public class SearchFilterProjectTypeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ActivitySearchFilterProjectTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_project_type);
        SearchFilterProjectTypeViewModel viewModel = new SearchFilterProjectTypeViewModel(this);
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
