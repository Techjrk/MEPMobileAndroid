package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpflocationBinding;
import com.lecet.app.databinding.ActivitySearchFilterMpfstageBinding;
import com.lecet.app.viewmodel.SearchFilterMPFStageViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterMPFStageActivity extends AppCompatActivity {
    //// TODO: 1/5/17 Create nested items for Stage layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //       setContentView(R.layout.activity_search_filter_mpfstage);
        ActivitySearchFilterMpfstageBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfstage);
        SearchFilterMPFStageViewModel viewModel = new SearchFilterMPFStageViewModel(this);
        sfilter.setViewModel(viewModel);

    }
}
