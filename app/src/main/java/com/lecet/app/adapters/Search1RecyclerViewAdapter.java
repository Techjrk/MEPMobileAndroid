package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.databinding.ListItemSearchProjectSavedViewBinding;
import com.lecet.app.databinding.ListItemSearchRecentViewBinding;

import com.lecet.app.viewmodel.SearchItem1ViewModel;
import com.lecet.app.viewmodel.SearchItemSavedSearchViewModel;

import java.util.Collections;
import java.util.List;

/**
 * File: Search1RecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Search1RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int adapterType;

    private List data = Collections.emptyList();

    public Search1RecyclerViewAdapter(List data) {

        this.data = data;
    }
    public void setData(List data){
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case 0: // 0 for recentview
                ListItemSearchRecentViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_recent_view, parent, false);
                viewHolder = new RecentViewHolder(binding);  //see this class below...
                break;
            case 1:
                ListItemSearchProjectSavedViewBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_project_saved_view, parent, false);
                viewHolder = new ProjectSavedViewHolder(binding1);  //see this class below...
                break;

           case 2:
                ListItemSearchProjectSavedViewBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_project_saved_view, parent, false);
                viewHolder = new ProjectSavedViewHolder(binding2);  //see this class below...
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecentViewHolder) {
            RecentViewHolder viewHolder = (RecentViewHolder) holder;
            viewHolder.getBinding().setViewModel(new SearchItem1ViewModel(((SearchResult)data.get(position)).getProject(), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
        }

        if (holder instanceof ProjectSavedViewHolder) {
            ProjectSavedViewHolder viewHolder = (ProjectSavedViewHolder) holder;
            viewHolder.getBinding().setViewModel(new SearchItemSavedSearchViewModel((SearchSaved) data.get(position)));
        }
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


    public void setAdapterType(int adapterType) {
        this.adapterType = adapterType;
    }

    /**
     * View Holders
     **/

    public class RecentViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchRecentViewBinding binding;

        public RecentViewHolder(ListItemSearchRecentViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }


        public ListItemSearchRecentViewBinding getBinding() {
            return binding;
        }
    }

    public class ProjectSavedViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchProjectSavedViewBinding binding;

        public ProjectSavedViewHolder(ListItemSearchProjectSavedViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemSearchProjectSavedViewBinding getBinding() {
            return binding;
        }
    }
}
