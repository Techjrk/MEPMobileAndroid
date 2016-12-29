package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpfownerTypeBinding;
import com.lecet.app.databinding.ActivitySearchFilterMpfworkTypeBinding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterMPFWorkTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterMpfworkTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfwork_type);
        SearchFilterMPFViewModel viewModel = new SearchFilterMPFViewModel(this);
        sfilter.setViewModel(viewModel);
    }
}
