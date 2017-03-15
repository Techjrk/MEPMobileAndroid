package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Contact;
import com.lecet.app.databinding.ListItemMcdContactBinding;
import com.lecet.app.viewmodel.CompanyDetailContactViewModel;

import io.realm.RealmList;

/**
 * File: CompanyContactsAdapter Created: 2/10/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyContactsAdapter extends RecyclerView.Adapter<CompanyContactsAdapter.CompanyContactsViewHolder> {

    private RealmList<Contact> data;
    private AppCompatActivity appCompatActivity;

    public CompanyContactsAdapter(AppCompatActivity appCompatActivity, RealmList<Contact> data) {

        this.appCompatActivity = appCompatActivity;
        this.data = data;
    }

    @Override
    public CompanyContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemMcdContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mcd_contact, parent, false);
        return new CompanyContactsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CompanyContactsViewHolder holder, int position) {

        CompanyDetailContactViewModel viewModel = new CompanyDetailContactViewModel(appCompatActivity, null, data.get(position));   //TODO - check null
        holder.getBinding().setViewModel(viewModel);

    }

    @Override
    public int getItemCount() {
        if (this.data == null) return 0;

        return this.data.size();
    }


    public class CompanyContactsViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMcdContactBinding binding;

        public CompanyContactsViewHolder(ListItemMcdContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemMcdContactBinding getBinding() {
            return binding;
        }
    }
}
