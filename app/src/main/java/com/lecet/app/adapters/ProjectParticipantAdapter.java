package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Contact;
import com.lecet.app.databinding.ListItemMpdParticipantBinding;
import com.lecet.app.viewmodel.ProjectDetailContactViewModel;

import io.realm.RealmResults;

/**
 * File: ProjectParticipantAdapter Created: 1/26/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectParticipantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RealmResults<Contact> data;

    public ProjectParticipantAdapter(RealmResults<Contact> data) {

        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemMpdParticipantBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mpd_participant, parent, false);
        return new ParticipantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Contact contact = data.get(position);

        ((ParticipantViewHolder) holder).getBinding().setViewModel(new ProjectDetailContactViewModel(contact));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class ParticipantViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMpdParticipantBinding binding;

        public ParticipantViewHolder(ListItemMpdParticipantBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMpdParticipantBinding getBinding() {
            return binding;
        }
    }
}
