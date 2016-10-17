package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.databinding.ListItemRecentBidBinding;
import com.lecet.app.viewmodel.RecentBidItemViewModel;


/**
 * File: RecentBidsAdapter Created: 10/13/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class RecentBidsAdapter extends RecyclerView.Adapter<RecentBidsAdapter.ViewHolder> {

    private Bid[] data;

    public RecentBidsAdapter(Bid[] data) {

        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemRecentBidBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_recent_bid, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.getBinding().setViewModel(new RecentBidItemViewModel(data[position], "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
    }

    @Override
    public int getItemCount() {

        if (data == null) return 0;

        return data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemRecentBidBinding binding;

        public ViewHolder(ListItemRecentBidBinding binding) {

            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemRecentBidBinding getBinding() {
            return binding;
        }
    }
}
