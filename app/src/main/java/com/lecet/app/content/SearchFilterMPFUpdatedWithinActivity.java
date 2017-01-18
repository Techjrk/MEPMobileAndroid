package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpfupdatedWithinBinding;
import com.lecet.app.viewmodel.SearchFilterMPFUpdatedWithinViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterMPFUpdatedWithinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_filter_mpfupdated_within);
        ActivitySearchFilterMpfupdatedWithinBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfupdated_within);

        // get Updated Within Extras
        Intent intent = getIntent();
        String[] updatedWithinArr = intent.getStringArrayExtra(SearchFilterMPFViewModel.EXTRA_UPDATED_WITHIN);

        SearchFilterMPFUpdatedWithinViewModel viewModel = new SearchFilterMPFUpdatedWithinViewModel(this);
        if(updatedWithinArr != null) {
            viewModel.setTime(updatedWithinArr);
        }
        sfilter.setViewModel(viewModel);
    }
}
