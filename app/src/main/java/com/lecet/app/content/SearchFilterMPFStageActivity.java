package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lecet.app.R;
import com.lecet.app.adapters.StageAdapter;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.databinding.ActivitySearchFilterMpflocationBinding;
import com.lecet.app.databinding.ActivitySearchFilterMpfstageBinding;
import com.lecet.app.viewmodel.SearchFilterMPFJurisdictionViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFStageViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterMPFStageActivity extends AppCompatActivity {
    //// TODO: 1/5/17 Create nested items for Stage layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //       setContentView(R.layout.activity_search_filter_mpfstage);
        ActivitySearchFilterMpfstageBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfstage);
        SearchFilterMPFStageViewModel viewModel = new SearchFilterMPFStageViewModel(this);
        sfilter.setViewModel(viewModel);
        initRecycleView(viewModel);
   
    }
    public void initRecycleView(SearchFilterMPFStageViewModel viewModel){
        /**
         * Process the multi-level display item of the stage with adapter
         */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

    }
}
