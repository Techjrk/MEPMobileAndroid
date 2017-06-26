package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterOwnerTypeBinding;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchFilterOwnerTypeViewModel;

public class SearchFilterOwnerTypeActivity extends AppCompatActivity {
    private SearchFilterOwnerTypeViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterOwnerTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_owner_type);
        Intent intent = getIntent();
        String sdata = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_OWNER_TYPE);
        String sdataId = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_OWNER_TYPE_ID);
         viewModel = new SearchFilterOwnerTypeViewModel(this);

        if (sdata.equalsIgnoreCase(SearchFilterAllTabbedViewModel.ANY)) {
            Log.d("any","any");
            viewModel.setLastChecked(null);
        }

            if (sdataId != null && !sdataId.isEmpty())
            {
                //Log.d("ownertypeid","ownertypeid"+sdataId);
                viewModel.setLastChecked((CheckBox)findViewById(Integer.valueOf(sdataId)));
            }

        sfilter.setViewModel(viewModel);
        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // SearchFilterOwnerTypeViewModel.lastChecked=null;
        viewModel.setLastChecked(null);
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
