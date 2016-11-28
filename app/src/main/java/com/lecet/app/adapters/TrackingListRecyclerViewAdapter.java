package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemProjectTrackingBinding;
import com.lecet.app.viewmodel.ListItemTrackingViewModel;

import java.util.List;

/**
 * File: TrackingListRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class TrackingListRecyclerViewAdapter extends RecyclerView.Adapter<TrackingListRecyclerViewAdapter.ProjectListViewHolder> {

    public static int ADAPTER_TYPE_PROJECTS = 1;

    private int adapterType;
    private List<Project> data;
    private boolean showUpdates;

    /**
     * Default Constructor
     */
    public TrackingListRecyclerViewAdapter(List<Project> data) {

        this.data = data;
        this.adapterType = ADAPTER_TYPE_PROJECTS;
    }

    /**
     * Alternate Constructor for use with adapter types other than the default
     */
    public TrackingListRecyclerViewAdapter(List<Project> data, int adapterType) {

        this.data = data;
        this.adapterType = adapterType;
    }

    @Override
    public ProjectListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemProjectTrackingBinding projectBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_tracking, parent, false);
        ProjectListViewHolder viewHolder = new ProjectListViewHolder(projectBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectListViewHolder holder, int position) {

        holder.getBinding().setViewModel(new ListItemTrackingViewModel(data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU", showUpdates));
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

    public void setShowUpdates(boolean showUpdates) {
        this.showUpdates = showUpdates;
    }


    /**
     * View Holders
     **/

    public class ProjectListViewHolder extends RecyclerView.ViewHolder {

        private ListItemProjectTrackingBinding binding;

        public ProjectListViewHolder(ListItemProjectTrackingBinding binding) {

            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemProjectTrackingBinding getBinding() {
            return binding;
        }
    }


}
