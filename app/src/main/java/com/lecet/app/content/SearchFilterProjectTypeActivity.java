package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lecet.app.R;

import com.lecet.app.adapters.SearchFilterProjectTypeAdapter;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.databinding.ActivitySearchFilterProjectTypeBinding;
import com.lecet.app.viewmodel.SearchFilterProjectTypeViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity for Search Filter: Project Type
 */
public class SearchFilterProjectTypeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchFilterProjectTypeBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_project_type);
        SearchFilterProjectTypeViewModel viewModel = new SearchFilterProjectTypeViewModel(this);
        sfilter.setViewModel(viewModel);
    }
}
