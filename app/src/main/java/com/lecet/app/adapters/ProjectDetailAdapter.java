package com.lecet.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * File: ProjectDetailAdapter Created: 10/27/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailAdapter extends SectionedAdapter {

    private List<List<String>> data;


    @Override
    public int getSectionCount() {
        return data.size();
    }

    @Override
    public int getItemCountForSection(int section) {
        return data.get(section).size();
    }

    @Override
    public int getItemViewType(int section, int position) {
        return 0;
    }

    @Override
    public int getHeaderViewType(int section) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int position, List payloads) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
