package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemHiddenProjectBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ListItemHiddenProjectViewModel;

import java.util.List;

import io.realm.RealmResults;

/**
 * File: HiddenProjectsAdapter Created: 1/31/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class HiddenProjectsAdapter extends RecyclerView.Adapter<HiddenProjectsAdapter.ProjectViewHolder> {

    private final ProjectDomain projectDomain;
    private final List<Project> data;
    private final String mapsAPIKey;

    public HiddenProjectsAdapter(ProjectDomain projectDomain, List<Project> data, String mapsAPIKey) {

        this.projectDomain = projectDomain;
        this.data = data;
        this.mapsAPIKey = mapsAPIKey;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemHiddenProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_hidden_project, parent, false);
        return new ProjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {

        Project project = data.get(position);
        holder.getBinding().setViewModel(new ListItemHiddenProjectViewModel(holder.itemView.getContext(), projectDomain, project, mapsAPIKey));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewRecycled(ProjectViewHolder holder) {
        super.onViewRecycled(holder);

        holder.getBinding().getViewModel().removeChangeListener();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final ListItemHiddenProjectBinding binding;

        public ProjectViewHolder(ListItemHiddenProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemHiddenProjectBinding getBinding() {
            return binding;
        }
    }

}
