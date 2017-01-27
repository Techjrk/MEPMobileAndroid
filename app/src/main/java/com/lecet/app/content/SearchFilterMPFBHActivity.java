package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpfbhBinding;
import com.lecet.app.viewmodel.SearchFilterMPFBHViewModel;

public class SearchFilterMPFBHActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter_mpfbh);
        ActivitySearchFilterMpfbhBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfbh);
        SearchFilterMPFBHViewModel viewModel = new SearchFilterMPFBHViewModel(this);
        sfilter.setViewModel(viewModel);

    }
}
