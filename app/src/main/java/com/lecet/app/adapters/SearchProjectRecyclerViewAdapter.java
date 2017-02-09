package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.databinding.ListItemSearchSavedViewBinding;
import com.lecet.app.viewmodel.SearchItemSavedSearchViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;


/**
 * File: SearchProjectRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchProjectRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SearchViewModel.SearchAdapterType
    private final int adapterType;

    private List data = Collections.emptyList();
    private SearchViewModel viewModel;

    public SearchProjectRecyclerViewAdapter(int adapterType, SearchViewModel viewModel, List data) {
        this.adapterType = adapterType;
        this.viewModel = viewModel;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        ListItemSearchSavedViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_saved_view, parent, false);
        viewHolder = new ProjectSavedViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ProjectSavedViewHolder viewHolder = (ProjectSavedViewHolder) holder;
        SearchItemSavedSearchViewModel vm = new SearchItemSavedSearchViewModel(viewModel, ((SearchSaved) data.get(position)));
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
    private class ProjectSavedViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchSavedViewBinding binding;

        private ProjectSavedViewHolder(ListItemSearchSavedViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemSearchSavedViewBinding getBinding() {
            return binding;
        }
    }
}
