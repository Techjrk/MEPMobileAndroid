package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.databinding.ListItemMpdBidBinding;
import com.lecet.app.viewmodel.ProjectDetailBidViewModel;

import io.realm.RealmResults;

/**
 * File: ProjectBiddersAdapter Created: 1/25/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectBiddersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RealmResults<Bid> data;

    public ProjectBiddersAdapter(RealmResults<Bid> data) {

        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemMpdBidBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mpd_bid, parent, false);
        return new BidViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ProjectDetailBidViewModel viewModel = new ProjectDetailBidViewModel(data.get(position));
        ((BidViewHolder) holder).getBinding().setViewModel(viewModel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class BidViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMpdBidBinding binding;

        public BidViewHolder(ListItemMpdBidBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMpdBidBinding getBinding() {
            return binding;
        }
    }
}
