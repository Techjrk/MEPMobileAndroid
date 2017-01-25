package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.adapters.StageAdapter;
import com.lecet.app.data.models.SearchFilterStage;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.databinding.ActivitySearchFilterMpfstage2Binding;
import com.lecet.app.viewmodel.SearchFilterMPFStageViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterMPFStageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //       setContentView(R.layout.activity_search_filter_mpfstage);
        ActivitySearchFilterMpfstage2Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfstage2);
        SearchFilterMPFStageViewModel viewModel = new SearchFilterMPFStageViewModel(this);
        sfilter.setViewModel(viewModel);
        initRecycleView(viewModel);
    }

    /**
     * Process the multi-level display item of the Stage with adapter
     */
    public void initRecycleView(SearchFilterMPFStageViewModel viewModel) {
        Log.d("SearchFilterMPFStageAct", "stageinit");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        List<StageAdapter.Parent> data = new ArrayList<>();

        List<StageAdapter.Child> children = null;
        for (SearchFilterStagesMain sMain : viewModel.getRealmStages()) {

            Log.d("SearchFilterMPFStageAct", "stagelist: name: " + sMain.getName());
            StageAdapter.Parent parent = new StageAdapter.Parent();
            parent.setName(sMain.getName());

            children = new ArrayList<>();
            for (SearchFilterStage jlocal : sMain.getStages()) {
                if (jlocal != null) {
                    StageAdapter.Child child = new StageAdapter.Child();
                    child.setName(jlocal.getName());
                    children.add(child);
                }
            }
            parent.setChildren(children);
            data.add(parent);
        }

        Log.d("SearchFilterMPFStageAct", "stagemain size: "+viewModel.getRealmStages().size());
        StageAdapter adapter = new StageAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }

}
