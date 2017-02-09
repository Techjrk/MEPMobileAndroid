package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Company;
import com.lecet.app.databinding.ListItemSearchQuerySummaryCompanyBinding;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;


/**
 * File: SearchSummaryCompanyRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchSummaryCompanyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SearchViewModel.SearchAdapterType
    private final int adapterType;
    private List data = Collections.emptyList();


    public SearchSummaryCompanyRecyclerViewAdapter(int adapterType,List data) {
        this.adapterType = adapterType;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        ListItemSearchQuerySummaryCompanyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_query_summary_company,parent, false);
        viewHolder = new CompanyQuerySearchViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CompanyQuerySearchViewHolder viewHolder = (CompanyQuerySearchViewHolder) holder;
        SearchItemRecentViewModel vm = new SearchItemRecentViewModel((Company) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU");
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
    private class CompanyQuerySearchViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchQuerySummaryCompanyBinding binding;

        private CompanyQuerySearchViewHolder(ListItemSearchQuerySummaryCompanyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemSearchQuerySummaryCompanyBinding getBinding() {
            return binding;
        }
    }
}
