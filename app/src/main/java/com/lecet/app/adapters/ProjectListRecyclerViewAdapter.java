package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemProjectTrackingBinding;
import com.lecet.app.viewmodel.ListItemProjectTrackingViewModel;

import java.util.List;

import io.realm.RealmObject;

/**
 * File: ProjectListRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    @MainViewModel.TrackingListType
    private int adapterType;

    private List<RealmObject> data;

    public ProjectListRecyclerViewAdapter(List<RealmObject> data, int adapterType/*, @MainViewModel.DashboardPosition int adapterType*/) {

        this.data = data;
        this.adapterType = adapterType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        /*switch (viewType) {

            case MainViewModel.DASHBOARD_POSITION_MBR:
                ListItemRecentBidBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_recent_bid, parent, false);
                viewHolder = new MBRViewHolder(binding);
                break;

            case MainViewModel.DASHBOARD_POSITION_MHS:
                ListItemBidHappSoonBinding bindingMHS = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_bid_happ_soon, parent, false);
                viewHolder = new MHSViewHolder(bindingMHS);
                break;

            case MainViewModel.DASHBOARD_POSITION_MRA:
            case MainViewModel.DASHBOARD_POSITION_MRU:
                ListItemDashboardProjectBinding bindingMRU = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_dashboard_project, parent, false);
                viewHolder = new MRAViewHolder(bindingMRU);
                break;
        }*/

        ListItemProjectTrackingBinding projectBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_tracking, parent, false);
        viewHolder = new ProjectListViewHolder(projectBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        /*if (holder instanceof MBRViewHolder) {

            MBRViewHolder viewHolder = (MBRViewHolder) holder;
            viewHolder.getBinding().setViewModel(new RecentBidItemViewModel((Bid) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));

        } else if (holder instanceof MHSViewHolder) {

            MHSViewHolder viewHolder = (MHSViewHolder) holder;
            viewHolder.getBinding().setViewModel(new ListItemTracking((Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));

        } else if (holder instanceof MRAViewHolder) {

            MRAViewHolder viewHolder = (MRAViewHolder) holder;
            viewHolder.getBinding().setViewModel(new DashboardProjectItemViewModel((Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
        }*/

        ProjectListViewHolder viewHolder = (ProjectListViewHolder) holder;
        viewHolder.getBinding().setViewModel(new ListItemProjectTrackingViewModel((Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));

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

    class ProjectListViewHolder extends RecyclerView.ViewHolder {

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
