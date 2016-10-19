package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemBidHappSoonBinding;
import com.lecet.app.viewmodel.BidHappeningSoonItemViewModel;

/**
 * File: BidsHappeningSoonAdapter Created: 10/17/16 Author: domandtom
 *
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class BidsHappeningSoonAdapter extends RecyclerView.Adapter<BidsHappeningSoonAdapter.ViewHolder> {

    private Project[] data;

    public BidsHappeningSoonAdapter(Project[] data) {

        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemBidHappSoonBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_bid_happ_soon, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.getBinding().setViewModel(new BidHappeningSoonItemViewModel(data[position], "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
    }

    @Override
    public int getItemCount() {

        if (data == null) return 0;

        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemBidHappSoonBinding binding;

        public ViewHolder(ListItemBidHappSoonBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemBidHappSoonBinding getBinding() {
            return binding;
        }
    }
}
