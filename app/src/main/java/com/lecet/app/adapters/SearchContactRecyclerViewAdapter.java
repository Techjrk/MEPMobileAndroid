package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.databinding.ListItemSearchSavedViewBinding;
import com.lecet.app.viewmodel.SearchItemSavedSearchViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;


/**
 * File: SearchContactRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

@Deprecated
public class SearchContactRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int adapterType = SearchViewModel.SEARCH_ADAPTER_TYPE_COMPANIES;  //TODO - CHANGE
    private List data = Collections.emptyList();
    private SearchViewModel viewModel;

    public SearchContactRecyclerViewAdapter(SearchViewModel viewModel, List data) {
        this.viewModel = viewModel;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        ListItemSearchSavedViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_saved_view, parent, false);
        viewHolder = new ContactSavedViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContactSavedViewHolder viewHolder = (ContactSavedViewHolder) holder;
        SearchItemSavedSearchViewModel vm = new SearchItemSavedSearchViewModel(viewModel, ((SearchSaved) data.get(position)));
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
    private class ContactSavedViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchSavedViewBinding binding;

        private ContactSavedViewHolder(ListItemSearchSavedViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemSearchSavedViewBinding getBinding() {
            return binding;
        }
    }
}
