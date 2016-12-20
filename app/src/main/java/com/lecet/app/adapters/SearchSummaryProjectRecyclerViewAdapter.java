package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemSearchQuerySummaryProjectBinding;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;


/**
 * File: SearchSummaryProjectRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchSummaryProjectRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int adapterType = SearchViewModel.SEARCH_ADAPTER_TYPE_PROJECT_QUERY_SUMMARY;
    private List data = Collections.emptyList();
    private SearchViewModel viewModel;

    public SearchSummaryProjectRecyclerViewAdapter(SearchViewModel viewModel, List data) {
        this.viewModel = viewModel;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        ListItemSearchQuerySummaryProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_query_summary_project,parent, false);
        viewHolder = new ProjectQuerySearchViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ProjectQuerySearchViewHolder viewHolder = (ProjectQuerySearchViewHolder) holder;
        SearchItemRecentViewModel vm = new SearchItemRecentViewModel(viewModel, (Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU");
        viewHolder.getBinding().setViewModel(vm);
    }

    @Override
    public int getItemCount() {

        if (data == null) return 0;

        return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        return adapterType;
    }

    /**
     * View Holder class
     **/
    private class ProjectQuerySearchViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchQuerySummaryProjectBinding binding;

        private ProjectQuerySearchViewHolder(ListItemSearchQuerySummaryProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemSearchQuerySummaryProjectBinding getBinding() {
            return binding;
        }

    }
}
