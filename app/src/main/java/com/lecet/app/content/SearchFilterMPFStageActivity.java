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

import io.realm.Realm;
import io.realm.RealmResults;

public class SearchFilterMPFStageActivity extends AppCompatActivity {

    RealmResults<SearchFilterStagesMain> realmStages;
    SearchFilterMPFStageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //       setContentView(R.layout.activity_search_filter_mpfstage);
        ActivitySearchFilterMpfstage2Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfstage2);
        viewModel = new SearchFilterMPFStageViewModel(this);
        sfilter.setViewModel(viewModel);
        getStageList();

      initRecycleView(viewModel);
    }

    /**
     * Process the multi-level display item of the Stage with adapter
     */
    public void initRecycleView(SearchFilterMPFStageViewModel viewModel) {
        Log.d("SearchFilterMPFStageAct","stageinit");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        List<StageAdapter.Parent> data = new ArrayList<>();

        List<StageAdapter.Child> children = null;
        for (SearchFilterStagesMain sMain : realmStages) {

            Log.d("SearchFilterMPFStageAct","stagelist"+sMain.getName());
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

        Log.d("SearchFilterMPFStageAct","stagemain"+realmStages.size());
//        Log.d("SearchFilterMPFStageAct","stagemain"+realmStages.size());
        StageAdapter adapter = new StageAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }

    private void getStageList() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmStages = realm.where(SearchFilterStagesMain.class).findAll();
            }
        });
        initRecycleView(viewModel);
    }

}
