package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.lecet.app.R;
import com.lecet.app.content.SearchActivity;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ListItemSearchQueryAllProjectBinding;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchViewModel;
import java.util.Collections;
import java.util.List;

import static com.lecet.app.R.string.google_api_key;


/**
 * File: SearchAllProjectRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchAllProjectRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SearchViewModel.SearchAdapterType
    private final int adapterType;
    private SearchActivity activity;

    private List data = Collections.emptyList();

    public SearchAllProjectRecyclerViewAdapter(SearchActivity activity, int adapterType, List data) {
        this.activity = activity;
        this.adapterType = adapterType;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        ListItemSearchQueryAllProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_query_all_project,parent, false);
        viewHolder = new ProjectQuerySearchViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String mapsApiKey = activity.getBaseContext().getResources().getString(google_api_key);
        ProjectQuerySearchViewHolder viewHolder = (ProjectQuerySearchViewHolder) holder;
        SearchItemRecentViewModel vm = new SearchItemRecentViewModel((Project) data.get(position), mapsApiKey, activity.getViewModel());
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
    private class ProjectQuerySearchViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchQueryAllProjectBinding binding;

        private ProjectQuerySearchViewHolder(ListItemSearchQueryAllProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemSearchQueryAllProjectBinding getBinding() {
            return binding;
        }
    }
}
