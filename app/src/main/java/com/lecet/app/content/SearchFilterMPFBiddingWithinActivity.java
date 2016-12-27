package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpfbiddingWithinBinding;
import com.lecet.app.databinding.ActivitySearchFilterMpflocationBinding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterMPFBiddingWithinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_search_filter_mpfbidding_within);
        ActivitySearchFilterMpfbiddingWithinBinding sfilter =   DataBindingUtil.setContentView(this,R.layout.activity_search_filter_mpfbidding_within);
        SearchFilterMPFViewModel viewModel =  new SearchFilterMPFViewModel(this);
        sfilter.setViewModel(viewModel);    }
}
