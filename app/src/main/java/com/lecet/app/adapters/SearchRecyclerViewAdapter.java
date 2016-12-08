package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.databinding.ListItemSearchSavedViewBinding;

import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchItemSavedSearchViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;

/**
 * File: SearchRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int adapterType;
    private List data = Collections.emptyList();
    private AppCompatActivity activity;
    public static boolean isMSE2SectionVisible;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Object item);
    }

    public SearchRecyclerViewAdapter(List data) {

        this.data = data;
    }

    /**
     * Old code - for reference
     * public SearchRecyclerViewAdapter(AppCompatActivity activity, List data) {
     * this.activity = activity;
     * this.data = data;
     * }
     */
    public SearchRecyclerViewAdapter(AppCompatActivity activity, List data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
        this.activity = activity;
    }

    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case SearchViewModel.SEARCH_ADAPTER_TYPE_RECENT:
                com.lecet.app.databinding.ListItemSearchRecentViewBinding recentItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_recent_view, parent, false);
                viewHolder = new RecentViewHolder(recentItemsBinding);
                break;

            case SearchViewModel.SEARCH_ADAPTER_TYPE_PROJECTS:
                ListItemSearchSavedViewBinding projectsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_saved_view, parent, false);
                viewHolder = new ProjectSavedViewHolder(projectsBinding);
                break;

            case SearchViewModel.SEARCH_ADAPTER_TYPE_COMPANIES:
                ListItemSearchSavedViewBinding companiesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_saved_view, parent, false);
                viewHolder = new CompanySavedViewHolder(companiesBinding);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Recent item
        if (holder instanceof RecentViewHolder) {
            RecentViewHolder viewHolder = (RecentViewHolder) holder;
//  old code - viewHolder.getBinding().setViewModel(new SearchItemRecentViewModel(((SearchResult)data.get(position)).getProject(), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
            viewHolder.getBinding().setViewModel(new SearchItemRecentViewModel(activity, ((SearchResult) data.get(position)).getProject(), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
            viewHolder.setItemClickListener((SearchResult) data.get(position), listener);
        }

        // Project
        else if (holder instanceof ProjectSavedViewHolder) {
            ProjectSavedViewHolder viewHolder = (ProjectSavedViewHolder) holder;
            viewHolder.getBinding().setViewModel(new SearchItemSavedSearchViewModel((SearchSaved) data.get(position)));

        }

        // Company
        else if (holder instanceof CompanySavedViewHolder) {
            CompanySavedViewHolder viewHolder = (CompanySavedViewHolder) holder;
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
     * View Holders - Recent Items, Project, and Company
     * TODO - consolidate into one?
     **/

    public class RecentViewHolder extends RecyclerView.ViewHolder {

        private final com.lecet.app.databinding.ListItemSearchRecentViewBinding binding;

        public RecentViewHolder(com.lecet.app.databinding.ListItemSearchRecentViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public com.lecet.app.databinding.ListItemSearchRecentViewBinding getBinding() {
            return binding;
        }

        /**
         * setItemClickListener - used to display the MSE 2.0 layout
         */
        public void setItemClickListener(final SearchResult item, final OnItemClickListener listener) {
            final LinearLayout mapviewsection = (LinearLayout) itemView.findViewById(R.id.mapsectionview);
            final ScrollView mse1section = binding.getViewModel().getMSE1SectionView();
            final ScrollView mse2section = binding.getViewModel().getMSE2SectionView();

            mapviewsection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isMSE2SectionVisible = !isMSE2SectionVisible;
                    if (isMSE2SectionVisible) {
                        mse1section.setVisibility(View.GONE);
                        mse2section.setVisibility(View.VISIBLE);
                    } else {
                        mse1section.setVisibility(View.VISIBLE);
                        mse2section.setVisibility(View.GONE);
                        listener.onItemClick(item.getProject());
                    }
                }
            });
        }
    }

    public class ProjectSavedViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchSavedViewBinding binding;

        public ProjectSavedViewHolder(ListItemSearchSavedViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemSearchSavedViewBinding getBinding() {
            return binding;
        }
    }

    public class CompanySavedViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchSavedViewBinding binding;

        public CompanySavedViewHolder(ListItemSearchSavedViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemSearchSavedViewBinding getBinding() {
            return binding;
        }
    }
}
