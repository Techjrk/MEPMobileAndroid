package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.databinding.ListItemMcdBidBinding;
import com.lecet.app.viewmodel.CompanyDetailBidViewModel;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * File: CompanyProjectBidsAdapter Created: 2/2/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyProjectBidsAdapter extends RecyclerView.Adapter<CompanyProjectBidsAdapter.CompanyBidViewHolder> {

    @Retention(SOURCE)
    @IntDef({NAVIGATION_MODE_UPCOMINNG, NAVIGATION_MODE_PAST})
    public @interface NavigationMode {
    }

    public static final int NAVIGATION_MODE_UPCOMINNG = 0;
    public static final int NAVIGATION_MODE_PAST = 1;

    private final AppCompatActivity appCompatActivity;

    private List<Bid> data;

    public CompanyProjectBidsAdapter(@NonNull AppCompatActivity appCompatActivity, List<Bid> data) {
        this.appCompatActivity = appCompatActivity;
        this.data = data;
    }


    @Override
    public CompanyBidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemMcdBidBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mcd_bid, parent, false);
        return new CompanyBidViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CompanyBidViewHolder holder, int position) {

        CompanyDetailBidViewModel viewModel = new CompanyDetailBidViewModel(appCompatActivity, appCompatActivity.getString(R.string.google_maps_key), data.get(position));
        holder.getBinding().setViewModel(viewModel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class CompanyBidViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMcdBidBinding binding;

        public CompanyBidViewHolder(ListItemMcdBidBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMcdBidBinding getBinding() {
            return binding;
        }
    }
}
