package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lecet.app.R;

import com.lecet.app.adapters.TypeAdapter;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.databinding.ActivitySearchFilterMpftype2Binding;
import com.lecet.app.viewmodel.SearchFilterMPFTypeViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity for Search Filter: Project Type
 */
public class SearchFilterMPFTypeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchFilterMpftype2Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpftype2);
        SearchFilterMPFTypeViewModel viewModel = new SearchFilterMPFTypeViewModel(this);
        sfilter.setViewModel(viewModel);
        initRecycleView(viewModel);
    }

    /**
     * Process the multi-level display item of the Project Type with adapter
     */
    public void initRecycleView(SearchFilterMPFTypeViewModel viewModel) {
        Log.d("typeinit","typeinit");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        List<TypeAdapter.Parent> data = new ArrayList<>();
        List<TypeAdapter.Child> children = null;
        for (SearchFilterProjectTypesMain ptMain : viewModel.getRealmProjectTypes()) {
            TypeAdapter.Parent parent = new TypeAdapter.Parent();
            parent.setName(ptMain.getTitle());
            parent.setId("" + ptMain.getId());
            children = new ArrayList<>();
            for (SearchFilterProjectTypesProjectCategory ptpc : ptMain.getProjectCategories()) {
                if (ptpc != null) {
                    TypeAdapter.Child child = new TypeAdapter.Child();
                    child.setName(ptpc.getTitle());
                    child.setId("" + ptpc.getId());
                    List<PrimaryProjectType> gchildTypes =     ptpc.getProjectTypes();
                    List<TypeAdapter.GrandChild>  grandChildren = new ArrayList<>();
                    for (PrimaryProjectType ppType: gchildTypes) {
                        if (ppType != null)  {
                            TypeAdapter.GrandChild gchild = new TypeAdapter.GrandChild();
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
        TypeAdapter adapter = new TypeAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }
}
