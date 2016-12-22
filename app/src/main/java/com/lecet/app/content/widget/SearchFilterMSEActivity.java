package com.lecet.app.content.widget;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMseBinding;
import com.lecet.app.viewmodel.SearchFilterMSEViewModel;

public class SearchFilterMSEActivity extends AppCompatActivity {
    SearchFilterMSEViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     setContentView(R.layout.activity_search_filter_mse);
      ActivitySearchFilterMseBinding sfilter =   DataBindingUtil.setContentView(this,R.layout.activity_search_filter_mse);
        SearchFilterMSEViewModel viewModel =  new SearchFilterMSEViewModel(this);
        sfilter.setViewModel(viewModel);
    }
}
