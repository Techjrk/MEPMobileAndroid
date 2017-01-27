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
        initRecycleView(viewModel);
    }

    /**
     * Process the multi-level display item of the Project Type with adapter
     */
    public void initRecycleView(SearchFilterProjectTypeViewModel viewModel) {
        Log.d("typeinit","typeinit");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        List<SearchFilterProjectTypeAdapter.Parent> data = new ArrayList<>();
        List<SearchFilterProjectTypeAdapter.Child> children = null;
        for (SearchFilterProjectTypesMain ptMain : viewModel.getRealmProjectTypes()) {
            SearchFilterProjectTypeAdapter.Parent parent = new SearchFilterProjectTypeAdapter.Parent();
            parent.setName(ptMain.getTitle());
            parent.setId("" + ptMain.getId());
            children = new ArrayList<>();
            for (SearchFilterProjectTypesProjectCategory ptpc : ptMain.getProjectCategories()) {
                if (ptpc != null) {
                    SearchFilterProjectTypeAdapter.Child child = new SearchFilterProjectTypeAdapter.Child();
                    child.setName(ptpc.getTitle());
                    child.setId("" + ptpc.getId());
                    List<PrimaryProjectType> gchildTypes =     ptpc.getProjectTypes();
                    List<SearchFilterProjectTypeAdapter.GrandChild>  grandChildren = new ArrayList<>();
                    for (PrimaryProjectType ppType: gchildTypes) {
                        if (ppType != null)  {
                            SearchFilterProjectTypeAdapter.GrandChild gchild = new SearchFilterProjectTypeAdapter.GrandChild();
                            gchild.setName(ppType.getTitle());
                            gchild.setId(""+ppType.getId());
                            grandChildren.add(gchild);
                        }
                    }
                    child.setGrandChildren(grandChildren);
                    children.add(child);
                }
            }
            parent.setChildren(children);
            data.add(parent);
        }
        SearchFilterProjectTypeAdapter adapter = new SearchFilterProjectTypeAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }
}
