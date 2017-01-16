package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMpfbiddingWithinBinding;
import com.lecet.app.viewmodel.SearchFilterMPFBiddingWithinViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

public class SearchFilterMPFBiddingWithinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_search_filter_mpfbidding_within);
        ActivitySearchFilterMpfbiddingWithinBinding sfilter =   DataBindingUtil.setContentView(this,R.layout.activity_search_filter_mpfbidding_within);

        // get Bidding Within Extras
        Intent intent = getIntent();
        String[] biddingWithinArr = intent.getStringArrayExtra(SearchFilterMPFViewModel.EXTRA_BIDDING_WITHIN);

        SearchFilterMPFBiddingWithinViewModel viewModel =  new SearchFilterMPFBiddingWithinViewModel(this);
        if(biddingWithinArr != null) {
            viewModel.setTime(biddingWithinArr);
        }
        sfilter.setViewModel(viewModel);
    }
}
