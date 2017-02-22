package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemDashboardProjectBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.viewmodel.DashboardProjectItemViewModel;
import com.lecet.app.viewmodel.MainViewModel;
import com.lecet.app.viewmodel.RecentBidItemViewModel;

import java.util.List;

import io.realm.RealmObject;

import static com.lecet.app.R.string.google_api_key;

/**
 * File: DashboardRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @MainViewModel.DashboardPosition
    private int adapterType;

    private List<RealmObject> data;
    private AppCompatActivity activity;
    private BidDomain bidDomain;

    public DashboardRecyclerViewAdapter(AppCompatActivity activity, List<RealmObject> data, @MainViewModel.DashboardPosition int adapterType, BidDomain bidDomain) {

        this.activity = activity;
        this.data = data;
        this.adapterType = adapterType;
        this.bidDomain = bidDomain;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {

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
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final String mapsApiKey = activity.getBaseContext().getResources().getString(google_api_key);

        if (holder instanceof MBRViewHolder) {

            MBRViewHolder viewHolder = (MBRViewHolder) holder;
            viewHolder.getBinding().setViewModel(new RecentBidItemViewModel(bidDomain, (Bid) data.get(position), mapsApiKey, activity));

        } else if (holder instanceof MHSViewHolder) {

            MHSViewHolder viewHolder = (MHSViewHolder) holder;
            viewHolder.getBinding().setViewModel(new DashboardProjectItemViewModel((Project) data.get(position), mapsApiKey));

        } else if (holder instanceof MRAViewHolder) {

            MRAViewHolder viewHolder = (MRAViewHolder) holder;
            viewHolder.getBinding().setViewModel(new DashboardProjectItemViewModel((Project) data.get(position), mapsApiKey));
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

}
