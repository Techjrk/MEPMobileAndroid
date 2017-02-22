package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterStageAdapter;
import com.lecet.app.data.models.SearchFilterStage;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.databinding.ActivitySearchFilterStageBinding;
import com.lecet.app.viewmodel.SearchFilterStageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Search Filter: Stage
 */
public class SearchFilterStageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterStageBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_stage);
        SearchFilterStageViewModel viewModel = new SearchFilterStageViewModel(this);
        sfilter.setViewModel(viewModel);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SearchFilterStageAdapter.clearLast();
        setResult(RESULT_CANCELED);
    }
}
