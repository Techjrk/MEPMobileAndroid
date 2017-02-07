package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterJurisdictionBinding;
import com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel;

/**
 * Activity for Search Filter: Jurisdiction
 */
public class SearchFilterJurisdictionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ActivitySearchFilterJurisdictionBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_jurisdiction);
        SearchFilterJurisdictionViewModel viewModel = new SearchFilterJurisdictionViewModel(this);
        sfilter.setViewModel(viewModel);
    }

}
