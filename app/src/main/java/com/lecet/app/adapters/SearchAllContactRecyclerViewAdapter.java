package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Contact;
import com.lecet.app.databinding.ListItemSearchQueryAllContactBinding;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;


/**
 * File: SearchAllContactRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchAllContactRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SearchViewModel.SearchAdapterType
    private final int adapterType;

    private List data = Collections.emptyList();
    private SearchViewModel viewModel;

    public SearchAllContactRecyclerViewAdapter(int adapterType, SearchViewModel viewModel, List data) {
        this.adapterType = adapterType;
        this.viewModel = viewModel;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        ListItemSearchQueryAllContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_query_all_contact,parent, false);
        viewHolder = new ContactQuerySearchViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContactQuerySearchViewHolder viewHolder = (ContactQuerySearchViewHolder) holder;
        SearchItemRecentViewModel vm = new SearchItemRecentViewModel(viewModel, (Contact) data.get(position));
        viewHolder.getBinding().setViewModel(vm);
    }

    @Override
    public int getItemCount() {

        if (data == null) return 0;

        return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        return adapterType;
    }

    /**
     * View Holder class
     **/
    private class ContactQuerySearchViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchQueryAllContactBinding binding;

        private ContactQuerySearchViewHolder(ListItemSearchQueryAllContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemSearchQueryAllContactBinding getBinding() {
            return binding;
        }
    }
}
