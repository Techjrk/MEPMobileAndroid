package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemDashboardProjectBinding;
import com.lecet.app.databinding.ListItemRecentViewBinding;
import com.lecet.app.viewmodel.DashboardProjectItemViewModel;
import com.lecet.app.viewmodel.MainViewModel;
import com.lecet.app.viewmodel.RecentBidItemViewModel;
import com.lecet.app.viewmodel.SearchItem1ViewModel;

import java.util.List;

import io.realm.RealmObject;

/**
 * File: Search1RecyclerViewAdapter Created: 10/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class Search1RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @MainViewModel.DashboardPosition
    private int adapterType;

    private List<RealmObject> data;

    public Search1RecyclerViewAdapter(List<RealmObject> data) {

        this.data = data;
    }
    public Search1RecyclerViewAdapter(List<RealmObject> data, @MainViewModel.DashboardPosition int adapterType) {

        this.data = data;
        this.adapterType = adapterType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

      /*  switch (viewType) {

            case MainViewModel.DASHBOARD_POSITION_MBR:
                com.lecet.app.databinding.ListItemRecentBidBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_recent_bid, parent, false);
                viewHolder = new MBRViewHolder(binding);
                break;

            case MainViewModel.DASHBOARD_POSITION_MHS:
                com.lecet.app.databinding.ListItemBidHappSoonBinding bindingMHS = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_bid_happ_soon, parent, false);
                viewHolder = new MHSViewHolder(bindingMHS);
                break;

            case MainViewModel.DASHBOARD_POSITION_MRA:
            case MainViewModel.DASHBOARD_POSITION_MRU:
                ListItemDashboardProjectBinding bindingMRU = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_dashboard_project, parent, false);
                viewHolder = new MRAViewHolder(bindingMRU);
                break;
        }*/

         ListItemRecentViewBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.list_item_recent_view,parent,false);
         viewHolder = new RecentViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecentViewHolder viewHolder = (RecentViewHolder) holder;
        viewHolder.getBinding().setViewModel(new SearchItem1ViewModel((Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
 /*
        if (holder instanceof MBRViewHolder) {

            MBRViewHolder viewHolder = (MBRViewHolder) holder;
            viewHolder.getBinding().setViewModel(new RecentBidItemViewModel((Bid) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));

        } else if (holder instanceof MHSViewHolder) {

            MHSViewHolder viewHolder = (MHSViewHolder) holder;
            viewHolder.getBinding().setViewModel(new DashboardProjectItemViewModel((Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));

        } else if (holder instanceof MRAViewHolder) {

            MRAViewHolder viewHolder = (MRAViewHolder) holder;
            viewHolder.getBinding().setViewModel(new DashboardProjectItemViewModel((Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
        }*/
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
  /*
    class MBRViewHolder extends RecyclerView.ViewHolder {

        private com.lecet.app.databinding.ListItemRecentBidBinding binding;

        public MBRViewHolder(com.lecet.app.databinding.ListItemRecentBidBinding binding) {

            super(binding.getRoot());

            this.binding = binding;
        }

        public com.lecet.app.databinding.ListItemRecentBidBinding getBinding() {
            return binding;
        }
    }


    public class MHSViewHolder extends RecyclerView.ViewHolder {

        private com.lecet.app.databinding.ListItemBidHappSoonBinding binding;

        public MHSViewHolder(com.lecet.app.databinding.ListItemBidHappSoonBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public com.lecet.app.databinding.ListItemBidHappSoonBinding getBinding() {
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
 */
    public class RecentViewHolder extends RecyclerView.ViewHolder {

        private final ListItemRecentViewBinding binding;

        public RecentViewHolder(ListItemRecentViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }


        public ListItemRecentViewBinding getBinding() {
            return binding;
        }
    }
}
