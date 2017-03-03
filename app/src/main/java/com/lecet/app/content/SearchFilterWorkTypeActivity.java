package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterWorkTypeBinding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchFilterWorkTypeViewModel;

public class SearchFilterWorkTypeActivity extends AppCompatActivity {
    SearchFilterWorkTypeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterWorkTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_work_type);
        Intent intent = getIntent();
        String sdata = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_WORK_TYPE);

        viewModel = new SearchFilterWorkTypeViewModel(this);
        if (sdata.equals(SearchFilterMPFViewModel.ANY)) {
            Log.d("anywork", "anywork");
            // SearchFilterWorkTypeViewModel.lastChecked=null;
            viewModel.setLastChecked(null);
        }
        sfilter.setViewModel(viewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  SearchFilterWorkTypeViewModel.lastChecked=null;
        viewModel.setLastChecked(null);
    }
}
