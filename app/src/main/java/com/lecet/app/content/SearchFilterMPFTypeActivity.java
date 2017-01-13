package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpflocationBinding;
import com.lecet.app.databinding.ActivitySearchFilterMpftypeBinding;
import com.lecet.app.viewmodel.SearchFilterMPFTypeViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchFilterMSEViewModel;

public class SearchFilterMPFTypeActivity extends AppCompatActivity {
    SearchFilterMPFTypeViewModel sfilter;
    //// TODO: 1/5/17 Create nested items for Type layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterMpftypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpftype);

        // get Type ID Extras
        Intent intent = getIntent();
        String typeId = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_PROJECT_TYPE_ID);
        String[] typeIds = new String[1];
        typeIds[0] = typeId;

        SearchFilterMPFTypeViewModel viewModel = new SearchFilterMPFTypeViewModel(this);
        viewModel.setType(typeIds);
        sfilter.setViewModel(viewModel);
    }

}
