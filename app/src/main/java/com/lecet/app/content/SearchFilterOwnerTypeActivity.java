package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterOwnerTypeBinding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchFilterOwnerTypeViewModel;

public class SearchFilterOwnerTypeActivity extends AppCompatActivity {
    private SearchFilterOwnerTypeViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterOwnerTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_owner_type);
        Intent intent = getIntent();
        String sdata = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_OWNER_TYPE);
         viewModel = new SearchFilterOwnerTypeViewModel(this);

        if (sdata.equals(SearchFilterMPFViewModel.ANY)) {
            Log.d("any","any");
            viewModel.setLastChecked(null);
        }

        sfilter.setViewModel(viewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // SearchFilterOwnerTypeViewModel.lastChecked=null;
        viewModel.setLastChecked(null);
    }
}
