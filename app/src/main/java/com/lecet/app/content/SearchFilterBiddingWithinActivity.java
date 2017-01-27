package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterBiddingWithinBinding;
import com.lecet.app.viewmodel.SearchFilterBiddingWithinViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterBiddingWithinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterBiddingWithinBinding sfilter =   DataBindingUtil.setContentView(this,R.layout.activity_search_filter_bidding_within);

        // get Bidding Within Extras
        Intent intent = getIntent();
        String[] biddingWithinArr = intent.getStringArrayExtra(SearchFilterMPFViewModel.EXTRA_BIDDING_WITHIN);

        SearchFilterBiddingWithinViewModel viewModel =  new SearchFilterBiddingWithinViewModel(this);
        if(biddingWithinArr != null) {
            viewModel.setTime(biddingWithinArr);
        }
        sfilter.setViewModel(viewModel);
    }
}
