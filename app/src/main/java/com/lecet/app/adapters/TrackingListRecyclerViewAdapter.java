package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.content.TrackingListActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemTrackingBinding;
import com.lecet.app.viewmodel.ListItemCompanyTrackingViewModel;
import com.lecet.app.viewmodel.ListItemProjectTrackingViewModel;
import com.lecet.app.viewmodel.ListItemTrackingViewModel;

import java.util.List;

import io.realm.RealmObject;

/**
 * File: TrackingListRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class TrackingListRecyclerViewAdapter extends RecyclerView.Adapter<TrackingListRecyclerViewAdapter.TrackingListViewHolder> {

    private String listType;
    private List<RealmObject> data;
    private boolean showUpdates;

    /**
     * Default Constructor
     */
    public TrackingListRecyclerViewAdapter(List<RealmObject> data) {

        this.data = data;
        this.listType = TrackingListActivity.TRACKING_LIST_TYPE_PROJECT;
    }

    /**
     * Alternate Constructor for use with adapter types other than the default
     */
    public TrackingListRecyclerViewAdapter(String listType, List<RealmObject> data) {

        this.listType = listType;
        this.data = data;
    }

    @Override
    public TrackingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemTrackingBinding projectBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_tracking, parent, false);
        TrackingListViewHolder viewHolder = new TrackingListViewHolder(projectBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrackingListViewHolder holder, int position) {

        final String mapsApiKey = "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU";    //TODO - externalize?

        // Project List
        if(this.listType.equals(TrackingListActivity.TRACKING_LIST_TYPE_PROJECT)) {
            holder.getBinding().setViewModel(new ListItemProjectTrackingViewModel((Project) data.get(position), mapsApiKey, showUpdates));
        }

        // Company List
        else if(this.listType.equals(TrackingListActivity.TRACKING_LIST_TYPE_COMPANY)) {
            holder.getBinding().setViewModel(new ListItemCompanyTrackingViewModel((Company) data.get(position), mapsApiKey, showUpdates));
        }
    }


    @Override
    public int getItemCount() {

        if (data == null) return 0;

        return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        //return listType;
        return 1;   //TODO - check
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public void setShowUpdates(boolean showUpdates) {
        this.showUpdates = showUpdates;
    }


    /**
     * View Holders
     **/

    public class TrackingListViewHolder extends RecyclerView.ViewHolder {

        private ListItemTrackingBinding binding;

        public TrackingListViewHolder(ListItemTrackingBinding binding) {

            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemTrackingBinding getBinding() {
            return binding;
        }
    }

}
