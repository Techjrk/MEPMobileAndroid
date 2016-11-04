package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemBidHappSoonBinding;
import com.lecet.app.databinding.ListItemDashboardProjectBinding;
import com.lecet.app.databinding.ListItemRecentBidBinding;
import com.lecet.app.viewmodel.DashboardProjectItemViewModel;
import com.lecet.app.viewmodel.MainViewModel;
import com.lecet.app.viewmodel.RecentBidItemViewModel;

import java.util.List;

import io.realm.RealmObject;

/**
 * File: DashboardAdapter Created: 10/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @MainViewModel.DashboardPosition
    private int adapterType;

    private List<RealmObject> data;

    public ProjectListRecyclerViewAdapter(List<RealmObject> data, @MainViewModel.DashboardPosition int adapterType) {

        this.data = data;
        this.adapterType = adapterType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {

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
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MBRViewHolder) {

            MBRViewHolder viewHolder = (MBRViewHolder) holder;
            viewHolder.getBinding().setViewModel(new RecentBidItemViewModel((Bid) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));

        } else if (holder instanceof MHSViewHolder) {

            MHSViewHolder viewHolder = (MHSViewHolder) holder;
            viewHolder.getBinding().setViewModel(new DashboardProjectItemViewModel((Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));

        } else if (holder instanceof MRAViewHolder) {

            MRAViewHolder viewHolder = (MRAViewHolder) holder;
            viewHolder.getBinding().setViewModel(new DashboardProjectItemViewModel((Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
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

    class MBRViewHolder extends RecyclerView.ViewHolder {

        private ListItemRecentBidBinding binding;

        public MBRViewHolder(ListItemRecentBidBinding binding) {

            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemRecentBidBinding getBinding() {
            return binding;
        }
    }


    public class MHSViewHolder extends RecyclerView.ViewHolder {

        private ListItemBidHappSoonBinding binding;

        public MHSViewHolder(ListItemBidHappSoonBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemBidHappSoonBinding getBinding() {
            return binding;
        }
    }


    public class MRAViewHolder extends RecyclerView.ViewHolder {

        private final ListItemDashboardProjectBinding binding;

        public MRAViewHolder(ListItemDashboardProjectBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemDashboardProjectBinding getBinding() {
            return binding;
        }
    }

}
