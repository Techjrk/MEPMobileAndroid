package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterOwnerTypeBinding;
import com.lecet.app.viewmodel.SearchFilterOwnerTypeViewModel;

public class SearchFilterOwnerTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterOwnerTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_owner_type);
        SearchFilterOwnerTypeViewModel viewModel = new SearchFilterOwnerTypeViewModel(this);
        sfilter.setViewModel(viewModel);
    }
}
