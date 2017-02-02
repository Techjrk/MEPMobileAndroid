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

import io.realm.RealmResults;

/**
 * File: HiddenProjectsAdapter Created: 1/31/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class HiddenProjectsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ProjectDomain projectDomain;
    private final RealmResults<Project> data;
    private final String mapsAPIKey;

    public HiddenProjectsAdapter(ProjectDomain projectDomain, RealmResults<Project> data, String mapsAPIKey) {

        this.projectDomain = projectDomain;
        this.data = data;
        this.mapsAPIKey = mapsAPIKey;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemHiddenProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_hidden_project, parent, false);
        return new ProjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Project project = data.get(position);
        ((ProjectViewHolder) holder).getBinding().setViewModel(new ListItemHiddenProjectViewModel(holder.itemView.getContext(), projectDomain, project, mapsAPIKey));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class ProjectViewHolder extends RecyclerView.ViewHolder {

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
