package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.content.SearchActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.databinding.ListItemSearchQueryAllCompanyBinding;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;

import static com.lecet.app.R.string.google_api_key;


/**
 * File: SearchAllCompanyRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchAllCompanyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SearchViewModel.SearchAdapterType
    private final int adapterType;
   // private AppCompatActivity appCompatActivity;
    private SearchActivity activity;
    private List data = Collections.emptyList();

    public SearchAllCompanyRecyclerViewAdapter(SearchActivity activity, int adapterType, List data) {
 //       public SearchAllCompanyRecyclerViewAdapter(AppCompatActivity appCompatActivity, int adapterType, List data) {
       // this.appCompatActivity = appCompatActivity;
        this.adapterType = adapterType;
        this.data = data;
        this.activity = activity;
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

        final String mapsApiKey = activity.getBaseContext().getResources().getString(google_api_key);
//        final String mapsApiKey = appCompatActivity.getBaseContext().getResources().getString(google_api_key);

        CompanyQuerySearchViewHolder viewHolder = (CompanyQuerySearchViewHolder) holder;
        SearchItemRecentViewModel vm = new SearchItemRecentViewModel((Company) data.get(position), mapsApiKey, activity.getViewModel());
//        SearchItemRecentViewModel vm = new SearchItemRecentViewModel((Company) data.get(position), mapsApiKey);
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
