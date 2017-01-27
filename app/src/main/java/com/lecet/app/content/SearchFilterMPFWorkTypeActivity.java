package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterWorkTypeBinding;
import com.lecet.app.viewmodel.SearchFilterMPFWorkTypeViewModel;

public class SearchFilterMPFWorkTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterWorkTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_work_type);
        SearchFilterMPFWorkTypeViewModel viewModel = new SearchFilterMPFWorkTypeViewModel(this);
        sfilter.setViewModel(viewModel);
    }
}
