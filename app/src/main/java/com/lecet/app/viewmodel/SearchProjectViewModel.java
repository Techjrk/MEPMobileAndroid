package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lecet.app.R;
//import com.lecet.app.adapters.SearchRecyclerViewAdapter;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.domain.SearchDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by getdevsinc on 12/18/16.
 */

public class SearchProjectViewModel {
    final AppCompatActivity activity;
    final SearchViewModel viewModel;
    final SearchDomain sd;

    private List<Project> adapterDataProjectQuerySummary;
    private List<Project> adapterDataProjectQueryAll;


    public SearchProjectViewModel(SearchViewModel viewModel, AppCompatActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.viewModel = viewModel;
        this.sd = sd;
    }

    /**
     * Initialize Saved Projects Adapter
     */
    private void initializeAdapterSavedProject() {
    /*    adapterDataProjectSearchSaved = new ArrayList<SearchSaved>();
        RecyclerView recyclerViewProject = getRecyclerViewById(R.id.recycler_view_project);
        setupRecyclerView(recyclerViewProject, LinearLayoutManager.VERTICAL);
        searchAdapterProject = new SearchRecyclerViewAdapter(this, adapterDataProjectSearchSaved);
        searchAdapterProject.setAdapterType(SEARCH_ADAPTER_TYPE_PROJECTS);
        recyclerViewProject.setAdapter(searchAdapterProject);
  */
    }

}
