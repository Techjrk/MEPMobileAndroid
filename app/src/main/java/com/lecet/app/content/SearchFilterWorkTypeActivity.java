package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterWorkTypeBinding;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchFilterWorkTypeViewModel;

public class SearchFilterWorkTypeActivity extends AppCompatActivity {
    SearchFilterWorkTypeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterWorkTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_work_type);
        Intent intent = getIntent();
        String sdata = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_WORK_TYPE);
        String sdataId = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_WORK_TYPE_ID);
      //  String sdataCB = intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_WORK_TYPE_CB);
        viewModel = new SearchFilterWorkTypeViewModel(this);

        if (sdata.equals(SearchFilterAllTabbedViewModel.ANY)) {
            Log.d("anywork", "anywork");
            // SearchFilterWorkTypeViewModel.lastChecked=null;
            viewModel.setLastChecked(null);
        }

        if (sdataId != null && !sdataId.isEmpty())
        {
           // Log.d("ownertypeid","ownertypeid"+sdataId);
            viewModel.setLastChecked((CheckBox)findViewById(Integer.valueOf(sdataId)));
        }
        sfilter.setViewModel(viewModel);
        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  SearchFilterWorkTypeViewModel.lastChecked=null;
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
