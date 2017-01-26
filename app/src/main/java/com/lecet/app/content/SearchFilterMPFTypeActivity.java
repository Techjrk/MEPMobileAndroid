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
import com.lecet.app.data.models.ProjectType;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.databinding.ActivitySearchFilterMpftype2Binding;
import com.lecet.app.databinding.ActivitySearchFilterMpftypeBinding;
import com.lecet.app.viewmodel.SearchFilterMPFTypeViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

import java.util.ArrayList;
import java.util.List;


public class SearchFilterMPFTypeActivity extends AppCompatActivity {

    //// TODO: 1/5/17 Create nested items for Type layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Type ID Extras
        Intent intent = getIntent();
        String typeId = intent.getStringExtra(SearchFilterMPFViewModel.EXTRA_PROJECT_TYPE_ID);
        String[] typeIds = new String[1];
        typeIds[0] = typeId;

//        ActivitySearchFilterMpftypeBinding viewModel = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpftype);
        ActivitySearchFilterMpftype2Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpftype2);
        SearchFilterMPFTypeViewModel viewModel = new SearchFilterMPFTypeViewModel(this);
        viewModel.setType(typeIds);
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

            Log.d("Typelist","Typelist"+ptMain.getTitle());
            TypeAdapter.Parent parent = new TypeAdapter.Parent();
            parent.setName(ptMain.getTitle());
            parent.setId("" + ptMain.getId());

            children = new ArrayList<>();
           // List<TypeAdapter.GrandChild>  grandChildren = new ArrayList<>();
            for (SearchFilterProjectTypesProjectCategory ptpc : ptMain.getProjectCategories()) {
                if (ptpc != null) {
                    TypeAdapter.Child child = new TypeAdapter.Child();
                    child.setName(ptpc.getTitle());
                    child.setId("" + ptpc.getId());
                    //children.add(child);
                    List<PrimaryProjectType> gchildTypes =     ptpc.getProjectTypes();
                    //
                    List<TypeAdapter.GrandChild>  grandChildren = new ArrayList<>();
                    for (PrimaryProjectType ppType: gchildTypes) {
                        if (ppType != null)  {
                            TypeAdapter.GrandChild gchild = new TypeAdapter.GrandChild();
                            gchild.setName(ppType.getTitle());
                            gchild.setId(""+ppType.getId());
                            grandChildren.add(gchild);
                            //Log.d("SearchDomain:","  Child Type: title:" + cType.getTitle() + " id:" + cType.getId()/* + " parentId:" + cType.getParentId()*/);
                        }
                    }
                    child.setGrandChildren(grandChildren);
                    children.add(child);
                    //
                }
            }
            parent.setChildren(children);
            data.add(parent);
        }
        TypeAdapter adapter = new TypeAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }

}
