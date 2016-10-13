package com.lecet.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.data.models.Bid;

import java.util.TreeSet;


/**
 * File: RecentBidsAdapter Created: 10/13/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class RecentBidsAdapter extends RecyclerView.Adapter {

    private TreeSet<Bid> data;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        if (data == null) return 0;

        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }


    }
}
