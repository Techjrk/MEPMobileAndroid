package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.databinding.ListItemSearchQuerySummaryCompanyBinding;
import com.lecet.app.databinding.ListItemSearchQuerySummaryContactBinding;
import com.lecet.app.databinding.ListItemSearchQuerySummaryProjectBinding;
import com.lecet.app.databinding.ListItemSearchSavedViewBinding;
import com.lecet.app.viewmodel.SearchItemRecentViewModel;
import com.lecet.app.viewmodel.SearchItemSavedSearchViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Collections;
import java.util.List;

//import de.hdodenhof.circleimageview.CircleImageView;

/**
 * File: SearchRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int adapterType;
    private List data = Collections.emptyList();
    private SearchViewModel viewModel;

    public SearchRecyclerViewAdapter(SearchViewModel viewModel, List data) {
        this.viewModel = viewModel;
        this.data = data;
    }


    public void setData(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case SearchViewModel.SEARCH_ADAPTER_TYPE_RECENT:
                com.lecet.app.databinding.ListItemSearchRecentViewBinding recentItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_recent_view, parent, false);
                viewHolder = new RecentViewHolder(recentItemsBinding);
                break;

            case SearchViewModel.SEARCH_ADAPTER_TYPE_PROJECTS:
                ListItemSearchSavedViewBinding projectsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_saved_view, parent, false);
                viewHolder = new ProjectSavedViewHolder(projectsBinding);
                break;

            case SearchViewModel.SEARCH_ADAPTER_TYPE_COMPANIES:
                ListItemSearchSavedViewBinding companiesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_saved_view, parent, false);
                viewHolder = new CompanySavedViewHolder(companiesBinding);
                break;
            case SearchViewModel.SEARCH_ADAPTER_TYPE_PQS:
                ListItemSearchQuerySummaryProjectBinding PQSBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_query_summary_project,parent, false);
                viewHolder = new PQSViewHolder(PQSBinding);
                break;
            case SearchViewModel.SEARCH_ADAPTER_TYPE_CQS:
                ListItemSearchQuerySummaryCompanyBinding CQSBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_query_summary_company,parent, false);
                viewHolder = new CQSViewHolder(CQSBinding);
                break;
            case SearchViewModel.SEARCH_ADAPTER_TYPE_CONTACTQS:
                ListItemSearchQuerySummaryContactBinding ContactQSBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_search_query_summary_contact,parent, false);
                viewHolder = new ContactQSViewHolder(ContactQSBinding);
                break;        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Recent item
        if (holder instanceof RecentViewHolder) {
            RecentViewHolder viewHolder = (RecentViewHolder) holder;
//  old code - viewHolder.getBinding().setViewModel(new SearchItemRecentViewModel(((SearchResult)data.get(position)).getProject(), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU"));
            SearchItemRecentViewModel searchrecent = new SearchItemRecentViewModel(viewModel, ((SearchResult) data.get(position)).getProject(), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU");
            viewHolder.getBinding().setViewModel(searchrecent);
         //   searchrecent.setItemClickListener((SearchResult) data.get(position), viewHolder.itemView);
        }

        // Project
        if (holder instanceof ProjectSavedViewHolder) {
            ProjectSavedViewHolder viewHolder = (ProjectSavedViewHolder) holder;
            SearchItemSavedSearchViewModel searchsaved = new SearchItemSavedSearchViewModel(viewModel, ((SearchSaved) data.get(position)));
            viewHolder.getBinding().setViewModel(searchsaved);
           // searchsaved.setItemClickListener((SearchSaved) data.get(position), viewHolder.itemView);


        }

        // Company
        if (holder instanceof CompanySavedViewHolder) {
            CompanySavedViewHolder viewHolder = (CompanySavedViewHolder) holder;
            SearchItemSavedSearchViewModel searchsaved = new SearchItemSavedSearchViewModel(viewModel, ((SearchSaved) data.get(position)));
            viewHolder.getBinding().setViewModel(searchsaved);
            //searchsaved.setItemClickListener((SearchSaved) data.get(position), viewHolder.itemView);
        }

        //PQS
        if (holder instanceof PQSViewHolder) {
            PQSViewHolder viewHolder = (PQSViewHolder) holder;
            SearchItemRecentViewModel searchPQS = new SearchItemRecentViewModel(viewModel, (Project) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU");
            viewHolder.getBinding().setViewModel(searchPQS);
   ///         searchPQS.setItemClickListener((Project) data.get(position), viewHolder.itemView);
        }
        //CQS
        if (holder instanceof CQSViewHolder) {
            CQSViewHolder viewHolder = (CQSViewHolder) holder;
            SearchItemRecentViewModel searchCQS = new SearchItemRecentViewModel(viewModel, (Company) data.get(position), "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU");
            viewHolder.getBinding().setViewModel(searchCQS);
    ///        searchCQS.setItemClickListener((Company) data.get(position), viewHolder.itemView);
        }

        //ContactQS
        if (holder instanceof ContactQSViewHolder) {
            ContactQSViewHolder viewHolder = (ContactQSViewHolder) holder;
            SearchItemRecentViewModel searchContactQS = new SearchItemRecentViewModel(viewModel, (Contact) data.get(position));
            viewHolder.getBinding().setViewModel(searchContactQS);
            ///        searchCQS.setItemClickListener((Company) data.get(position), viewHolder.itemView);
        }
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

    public void setAdapterType(int adapterType) {
        this.adapterType = adapterType;
    }

    /**
     * View Holders - Recent Items, Project, and Company
     * TODO - consolidate into one?
     **/

    /* For Project Query Search Summary */
    public class PQSViewHolder extends RecyclerView.ViewHolder {

        private final com.lecet.app.databinding.ListItemSearchQuerySummaryProjectBinding binding;

        public PQSViewHolder(com.lecet.app.databinding.ListItemSearchQuerySummaryProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public com.lecet.app.databinding.ListItemSearchQuerySummaryProjectBinding getBinding() {
            return binding;
        }

    }

    /* For Company Query Search Summary */
    public class CQSViewHolder extends RecyclerView.ViewHolder {

        private final com.lecet.app.databinding.ListItemSearchQuerySummaryCompanyBinding binding;

        public CQSViewHolder(com.lecet.app.databinding.ListItemSearchQuerySummaryCompanyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public com.lecet.app.databinding.ListItemSearchQuerySummaryCompanyBinding getBinding() {
            return binding;
        }
    }

    /* For Contact Query Search Summary */
    public class ContactQSViewHolder extends RecyclerView.ViewHolder {

        private final com.lecet.app.databinding.ListItemSearchQuerySummaryContactBinding binding;

        public ContactQSViewHolder(com.lecet.app.databinding.ListItemSearchQuerySummaryContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public com.lecet.app.databinding.ListItemSearchQuerySummaryContactBinding getBinding() {
            return binding;
        }
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder {

        private final com.lecet.app.databinding.ListItemSearchRecentViewBinding binding;

        public RecentViewHolder(com.lecet.app.databinding.ListItemSearchRecentViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public com.lecet.app.databinding.ListItemSearchRecentViewBinding getBinding() {
            return binding;
        }

    }

    public class ProjectSavedViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchSavedViewBinding binding;

        public ProjectSavedViewHolder(ListItemSearchSavedViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemSearchSavedViewBinding getBinding() {
            return binding;
        }
    }

    public class CompanySavedViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSearchSavedViewBinding binding;

        public CompanySavedViewHolder(ListItemSearchSavedViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemSearchSavedViewBinding getBinding() {
            return binding;
        }
    }
}
