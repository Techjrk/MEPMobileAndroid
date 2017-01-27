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
        initRecycleView(viewModel);
    }

    /**
     * Process the multi-level display item of the Stage with adapter
     */
    public void initRecycleView(SearchFilterStageViewModel viewModel) {
        Log.d("SearchFilterMPFStageAct", "stageinit");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        List<SearchFilterStageAdapter.Parent> data = new ArrayList<>();

        List<SearchFilterStageAdapter.Child> children = null;
        for (SearchFilterStagesMain parentStage : viewModel.getRealmStages()) {

            Log.d("SearchFilterMPFStageAct", "stagelist: name: " + parentStage.getName());
            SearchFilterStageAdapter.Parent parent = new SearchFilterStageAdapter.Parent();
            parent.setId(parentStage.getId());
            parent.setName(parentStage.getName());

            children = new ArrayList<>();
            for (SearchFilterStage childStage : parentStage.getStages()) {
                if (childStage != null) {
                    SearchFilterStageAdapter.Child child = new SearchFilterStageAdapter.Child();
                    child.setId(childStage.getId());
                    child.setName(childStage.getName());
                    children.add(child);
                }
            }
            parent.setChildren(children);
            data.add(parent);
        }

        Log.d("SearchFilterMPFStageAct", "stagemain size: "+viewModel.getRealmStages().size());
        SearchFilterStageAdapter adapter = new SearchFilterStageAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }

}
