package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Company;
import com.lecet.app.databinding.ListItemSearchQueryAllCompanyBinding;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;


/**
 * File: SearchAllCompanyRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchAllCompanyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int adapterType = SearchViewModel.SEARCH_ADAPTER_TYPE_COMPANY_QUERY_ALL;
    private List data = Collections.emptyList();
    private SearchViewModel viewModel;

    public SearchAllCompanyRecyclerViewAdapter(SearchViewModel viewModel, List data) {
        this.viewModel = viewModel;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        ListItemSearchQueryAllCompanyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_query_all_company,parent, false);
        viewHolder = new CompanyQuerySearchViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CompanyQuerySearchViewHolder viewHolder = (CompanyQuerySearchViewHolder) holder;
        SearchItemRecentViewModel vm = new SearchItemRecentViewModel(viewModel, (Company) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU");
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

        private final ListItemSearchQueryAllCompanyBinding binding;

        private CompanyQuerySearchViewHolder(ListItemSearchQueryAllCompanyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemSearchQueryAllCompanyBinding getBinding() {
            return binding;
        }
    }
}
