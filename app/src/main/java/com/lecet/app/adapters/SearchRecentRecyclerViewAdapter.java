package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.content.SearchActivity;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.databinding.ListItemSearchRecentViewBinding;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;

import static com.lecet.app.R.string.google_api_key;


/**
 * File: SearchRecentRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchRecentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SearchViewModel.SearchAdapterType
    private final int adapterType;
    private AppCompatActivity appCompatActivity;
    private List data = Collections.emptyList();
    private SearchActivity activity;

    public SearchRecentRecyclerViewAdapter(AppCompatActivity appCompatActivity, int adapterType, List data) {
        this.appCompatActivity = appCompatActivity;
        this.adapterType = adapterType;
        this.data = data;
        activity = (SearchActivity) appCompatActivity;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        ListItemSearchRecentViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_recent_view, parent, false);
        viewHolder = new RecentViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final String mapsApiKey = appCompatActivity.getBaseContext().getResources().getString(google_api_key);

        RecentViewHolder viewHolder = (RecentViewHolder) holder;
        SearchItemRecentViewModel vm = null;

        if (((SearchResult) data.get(position)).getProject() != null) {
            vm = new SearchItemRecentViewModel( ((SearchResult) data.get(position)).getProject(), mapsApiKey,activity.getViewModel());
        }
        else
            vm = new SearchItemRecentViewModel( ((SearchResult) data.get(position)).getCompany(), mapsApiKey, activity.getViewModel());

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
}
