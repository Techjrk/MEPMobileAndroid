package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterJurisdictionAdapter;
import com.lecet.app.databinding.ActivitySearchFilterJurisdictionBinding;
import com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel;

/**
 * Activity for Search Filter: Jurisdiction
 */
public class SearchFilterJurisdictionActivity extends AppCompatActivity {
    private SearchFilterJurisdictionViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ActivitySearchFilterJurisdictionBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_jurisdiction);
         viewModel = new SearchFilterJurisdictionViewModel(this);
        sfilter.setViewModel(viewModel);
        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewModel.clearLast();
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
