package com.lecet.app.adapters;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.content.ProjectsNearMeActivity;
import com.lecet.app.content.SearchActivity;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemBidAllProjectBinding;
import com.lecet.app.databinding.ListItemSearchQueryAllProjectBinding;
import com.lecet.app.viewmodel.BidItemViewModel;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;

import static com.lecet.app.R.string.google_api_key;


/**
 * File: SearchAllProjectRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class BidProjectListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SearchViewModel.SearchAdapterType
    private final int adapterType;
    ProjectsNearMeActivity activity;
    private List data = Collections.emptyList();

    public BidProjectListRecyclerViewAdapter(ProjectsNearMeActivity activity, int adapterType, List data) {
        this.activity = activity;
        this.adapterType = adapterType;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        ListItemBidAllProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_bid_all_project,parent, false);
        viewHolder = new BidProjectViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        final String mapsApiKey = appCompatActivity.getBaseContext().getResources().getString(google_api_key);
        final String mapsApiKey = activity.getBaseContext().getResources().getString(google_api_key);

        BidProjectViewHolder viewHolder = (BidProjectViewHolder) holder;
        BidItemViewModel vm = new BidItemViewModel((Project) data.get(position), mapsApiKey);
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
    private class BidProjectViewHolder extends RecyclerView.ViewHolder {

        private final ListItemBidAllProjectBinding binding;

        private BidProjectViewHolder(ListItemBidAllProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemBidAllProjectBinding getBinding() {
            return binding;
        }
    }
}
