package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemCompanyAsscProjectBinding;
import com.lecet.app.viewmodel.CompanyDetailProjectViewModel;

import io.realm.RealmResults;

/**
 * File: CompanyAssociatedProjectAdapter Created: 1/25/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyAssociatedProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RealmResults<Project> data;

    public CompanyAssociatedProjectAdapter(RealmResults<Project> data) {

        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemCompanyAsscProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_company_assc_project, parent, false);
        return new CompanyProjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CompanyDetailProjectViewModel viewModel = new CompanyDetailProjectViewModel(data.get(position), holder.itemView.getContext().getString(R.string.google_maps_key));
        ((CompanyProjectViewHolder) holder).getBinding().setViewModel(viewModel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class CompanyProjectViewHolder extends RecyclerView.ViewHolder {

        private final ListItemCompanyAsscProjectBinding binding;

        public CompanyProjectViewHolder(ListItemCompanyAsscProjectBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemCompanyAsscProjectBinding getBinding() {
            return binding;
        }
    }
}
