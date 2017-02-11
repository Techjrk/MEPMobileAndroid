package com.lecet.app.adapters;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.content.CompanyAssociatedProjectsActivity;
import com.lecet.app.content.CompanyContactsActivity;
import com.lecet.app.content.CompanyProjectBidsActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ListItemCompHeaderShareBinding;
import com.lecet.app.databinding.ListItemHeaderCompanyBinding;
import com.lecet.app.databinding.ListItemMcdBidBinding;
import com.lecet.app.databinding.ListItemMcdContactBinding;
import com.lecet.app.databinding.ListItemMcdInfoBinding;
import com.lecet.app.databinding.ListItemMcdProjectBinding;
import com.lecet.app.databinding.ListItemMpdFooterBinding;
import com.lecet.app.databinding.ListItemProjectDetailBinding;
import com.lecet.app.databinding.ListItemSectionHeaderBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.CompanyDetailBidViewModel;
import com.lecet.app.viewmodel.CompanyDetailContactViewModel;
import com.lecet.app.viewmodel.CompanyDetailHeaderViewModel;
import com.lecet.app.viewmodel.CompanyDetailInfoViewModel;
import com.lecet.app.viewmodel.CompanyDetailProjectViewModel;
import com.lecet.app.viewmodel.CompanyShareToolbarViewModel;
import com.lecet.app.viewmodel.DetailFooterViewModel;
import com.lecet.app.viewmodel.HeaderViewModel;
import com.lecet.app.viewmodel.ProjDetailItemViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * File: CompanyDetailAdapter Created: 1/23/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyDetailAdapter extends SectionedAdapter {

    private static final String TAG = "ProjectDetailAdapter";

    private static final int HEADER_COMPANY_VIEW_TYPE = 0;
    private static final int HEADER_SECTION_VIEW_TYPE = 1;
    private static final int HEADER_SHARE_VIEW_TYPE = 2;
    private static final int INFO_VIEW_TYPE = 3;
    private static final int DETAIL_VIEW_TYPE = 4;
    private static final int PROJECT_VIEW_TYPE = 5;
    private static final int BID_VIEW_TYPE = 6;
    private static final int CONTACT_VIEW_TYPE = 7;
    private static final int FOOTER_VIEW_TYPE = 8;

    private static final int SECTION_TITLE = 0;
    private static final int SECTION_INFO = 1;
    private static final int SECTION_DETAIL = 2;

    private static final int MIN_SECTIONS = 3;

    private int SECTION_PROJECT = -1;
    private int SECTION_CONTACTS = -1;
    private int SECTION_BIDS = -1;

    private AppCompatActivity appCompatActivity;
    private Company company;
    private ProjectDomain projectDomain;

    List<CompanyDetailInfoViewModel> companyInfo;
    List<ProjDetailItemViewModel> companyDetails;

    private boolean projectsDisplayed;
    private boolean bidsDisplayed;
    private boolean contactsDisplayed;

    public CompanyDetailAdapter(AppCompatActivity appCompatActivity, Company company, ProjectDomain projectDomain) {

        this.appCompatActivity = appCompatActivity;
        this.company = company;
        this.projectDomain = projectDomain;

        // Get a list of the company contact info we need.
        companyInfo = new ArrayList<>();

        if (company.getContacts() != null && company.getContacts().size() > 0) {

            Contact contact = company.getContacts().get(0);

            if (contact.getPhoneNumber() != null) {

                CompanyDetailInfoViewModel viewModel = new CompanyDetailInfoViewModel(appCompatActivity, company.getContacts().get(0).getPhoneNumber(), CompanyDetailInfoViewModel.INFO_PHONE);
                companyInfo.add(viewModel);
            }

            if (contact.getEmail() != null) {

                CompanyDetailInfoViewModel viewModel = new CompanyDetailInfoViewModel(appCompatActivity, company.getContacts().get(0).getEmail(), CompanyDetailInfoViewModel.INFO_EMAIL);
                companyInfo.add(viewModel);
            }
        }

        if (company.getWwwUrl() != null) {

            CompanyDetailInfoViewModel viewModel = new CompanyDetailInfoViewModel(appCompatActivity, company.getWwwUrl(), CompanyDetailInfoViewModel.INFO_WEB);
            companyInfo.add(viewModel);
        }

        // Total valuation is a sum of all projects estLow.
        double totalValuation = 0.00;

        if (company.getProjects() != null && company.getProjects().size() > 0) {

            for (Project project : company.getProjects()) {

                totalValuation = totalValuation + project.getEstLow();
            }
        }

        DecimalFormat formatter = new DecimalFormat("$ #,###");
        String strValuation = formatter.format(totalValuation);

        // Get the company's address and project info.
        companyDetails = new ArrayList<>();

        companyDetails.add(new ProjDetailItemViewModel(appCompatActivity.getString(R.string.address), company.getFullAddress()));
        companyDetails.add(new ProjDetailItemViewModel(appCompatActivity.getString(R.string.total_projects), String.valueOf(company.getProjects() != null ? company.getProjects().size() : 0)));
        companyDetails.add(new ProjDetailItemViewModel(appCompatActivity.getString(R.string.total_valuation), strValuation));

        projectsDisplayed = company.getProjects() != null && company.getProjects().size() > 0;
        bidsDisplayed = company.getBids() != null && company.getBids().size() > 0;
        contactsDisplayed = company.getContacts() != null && company.getContacts().size() > 0;

        if (projectsDisplayed) {

            SECTION_PROJECT = 3;
        }

        if (!projectsDisplayed && contactsDisplayed) {

            SECTION_CONTACTS = 3;

        } else if (projectsDisplayed && contactsDisplayed) {

            SECTION_CONTACTS = 4;
        }

        if (!projectsDisplayed && !contactsDisplayed && bidsDisplayed) {

            SECTION_BIDS = 3;

        } else if (!projectsDisplayed || !contactsDisplayed && bidsDisplayed) {

            SECTION_BIDS = 4;

        } else if (projectsDisplayed || contactsDisplayed && bidsDisplayed) {

            SECTION_BIDS = 5;

        }
    }


    @Override
    public int getSectionCount() {

        // MIN_SECTIONS plus the any additional sections available

        return MIN_SECTIONS + (projectsDisplayed ? 1 : 0) + (contactsDisplayed ? 1 : 0) + (bidsDisplayed ? 1 :  0);
    }

    @Override
    public int getItemCountForSection(int section) {

        if (section == SECTION_TITLE) {

            return 0;

        }  else if (section == SECTION_DETAIL) {

            return 3;
        }
        else if (section == SECTION_INFO) {

            return companyInfo.size();

        } else if (projectsDisplayed && section == SECTION_PROJECT) {

            return company.getProjects().size() <= 3 ? company.getProjects().size() : 3;

        } else if (contactsDisplayed && section == SECTION_CONTACTS) {

            return company.getContacts().size() <= 3 ? company.getContacts().size() : 3;

        } else if (bidsDisplayed && section == SECTION_BIDS) {

            return company.getBids().size() <= 3 ? company.getBids().size() : 3;
        }

        return 0;
    }

    @Override
    public int getItemViewType(int section, int position) {

        if (section == SECTION_DETAIL) {

            return DETAIL_VIEW_TYPE;
        }
        else if (section == SECTION_INFO) {

            return INFO_VIEW_TYPE;

        } else if (projectsDisplayed && section == SECTION_PROJECT) {

            return PROJECT_VIEW_TYPE;

        } else if (contactsDisplayed && section == SECTION_CONTACTS) {

            return CONTACT_VIEW_TYPE;

        } else if (bidsDisplayed && section == SECTION_BIDS) {

            return BID_VIEW_TYPE;
        }

        return INFO_VIEW_TYPE;
    }

    @Override
    public int getHeaderViewType(int section) {
        switch (section) {
            case SECTION_TITLE:
                return HEADER_COMPANY_VIEW_TYPE;
            case SECTION_INFO:
                return HEADER_SHARE_VIEW_TYPE;
            default:
                return HEADER_SECTION_VIEW_TYPE;
        }
    }

    @Override
    public int getFooterViewType(int section) {
        return FOOTER_VIEW_TYPE;
    }

    @Override
    public boolean enableHeaderForSection(int section) {

        return true;
    }

    @Override
    public boolean enableFooterForSection(int section) {

        if (bidsDisplayed && section == SECTION_BIDS) {

            if (company.getBids() == null) return false;

            return company.getBids().size() > 3;
        }

        if (projectsDisplayed && section == SECTION_PROJECT) {

            if (company.getProjects() == null) return false;

            return company.getProjects().size() > 3;
        }

        if (contactsDisplayed && section == SECTION_CONTACTS) {

            if (company.getContacts() == null) return false;

            return company.getContacts().size() > 3;
        }

        return false;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        if (holder instanceof CompanyHeaderViewHolder) {

            ((CompanyHeaderViewHolder) holder).getBinding().setViewModel(new CompanyDetailHeaderViewModel(company));

        } else if (holder instanceof CompanyDetailAdapter.SectionHeaderVH) {

            if (section == SECTION_PROJECT) {

                ((CompanyDetailAdapter.SectionHeaderVH) holder).getBinding().setViewModel(new HeaderViewModel(appCompatActivity.getString(R.string.associated_projects)));

            } else if (section == SECTION_CONTACTS) {

                ((CompanyDetailAdapter.SectionHeaderVH) holder).getBinding().setViewModel(new HeaderViewModel(appCompatActivity.getString(R.string.contacts)));

            } else if (section == SECTION_BIDS) {

                ((CompanyDetailAdapter.SectionHeaderVH) holder).getBinding().setViewModel(new HeaderViewModel(appCompatActivity.getString(R.string.project_bids)));
            }

        } else if (holder instanceof CompanyDetailAdapter.ShareViewHolder) {

            TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(appCompatActivity), Realm.getDefaultInstance(), new RealmChangeListener() {
                @Override
                public void onChange(Object element) {

                }
            }, projectDomain);
            ((CompanyDetailAdapter.ShareViewHolder) holder).getBinding().setViewModel(new CompanyShareToolbarViewModel(appCompatActivity, trackingListDomain, company));
        }
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {

        if (section == SECTION_INFO) {

            CompanyDetailInfoViewModel viewModel = companyInfo.get(position);
            ((CompanyInfoViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (section == SECTION_DETAIL) {

            ProjDetailItemViewModel viewModel = companyDetails.get(position);
            ((CompanyDetailViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (section == SECTION_PROJECT) {

            CompanyDetailProjectViewModel viewModel = new CompanyDetailProjectViewModel(company.getProjects().get(position), appCompatActivity.getString(R.string.google_maps_key));
            ((CompanyProjectViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (section == SECTION_CONTACTS) {

            CompanyDetailContactViewModel viewModel = new CompanyDetailContactViewModel(appCompatActivity, company.getContacts().get(position));
            ((CompanyContactViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (section == SECTION_BIDS) {

            CompanyDetailBidViewModel viewModel = new CompanyDetailBidViewModel(appCompatActivity, appCompatActivity.getString(R.string.google_maps_key), company.getBids().get(position));
            ((CompanyBidViewHolder) holder).getBinding().setViewModel(viewModel);
        }

    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, final int section) {

        if (section == SECTION_CONTACTS) {

            DetailFooterViewModel viewModel = new DetailFooterViewModel(appCompatActivity, String.valueOf(company.getContacts().size()), appCompatActivity.getString(R.string.contacts));
            ((FooterViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (section == SECTION_BIDS) {

            DetailFooterViewModel viewModel = new DetailFooterViewModel(appCompatActivity, String.valueOf(company.getBids().size()), appCompatActivity.getString(R.string.bids));
            ((FooterViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (section == SECTION_PROJECT) {

            DetailFooterViewModel viewModel = new DetailFooterViewModel(appCompatActivity, String.valueOf(company.getProjects().size()), appCompatActivity.getString(R.string.projects));
            ((FooterViewHolder) holder).getBinding().setViewModel(viewModel);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (projectsDisplayed && section == SECTION_PROJECT) {

                    Intent intent = new Intent(appCompatActivity, CompanyAssociatedProjectsActivity.class);
                    intent.putExtra(CompanyAssociatedProjectsActivity.COMPANY_ID_EXTRA, company.getId());
                    appCompatActivity.startActivity(intent);

                } else if (bidsDisplayed && section == SECTION_BIDS) {

                    Intent intent = new Intent(appCompatActivity, CompanyProjectBidsActivity.class);
                    intent.putExtra(CompanyProjectBidsActivity.COMPANY_ID_EXTRA, company.getId());
                    appCompatActivity.startActivity(intent);
                } else if (contactsDisplayed && section == SECTION_CONTACTS) {

                    Intent intent = new Intent(appCompatActivity, CompanyContactsActivity.class);
                    intent.putExtra(CompanyContactsActivity.COMPANY_ID_EXTRA, company.getId());
                    intent.putExtra(CompanyContactsActivity.COMPANY_NAME_EXTRA, company.getName());
                    intent.putExtra(CompanyContactsActivity.COMPANY_CONTACTS_COUNT_EXTRA, company.getContacts().size());
                    appCompatActivity.startActivity(intent);
                }
            }
        });


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case HEADER_COMPANY_VIEW_TYPE: {

                ListItemHeaderCompanyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_header_company, parent, false);
                return new CompanyHeaderViewHolder(binding);
            }

            case HEADER_SECTION_VIEW_TYPE: {

                ListItemSectionHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_section_header, parent, false);
                return new CompanyDetailAdapter.SectionHeaderVH(binding);
            }

            case HEADER_SHARE_VIEW_TYPE: {

                ListItemCompHeaderShareBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_comp_header_share, parent, false);

                return new CompanyDetailAdapter.ShareViewHolder(binding);
            }

            case INFO_VIEW_TYPE: {

                ListItemMcdInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mcd_info, parent, false);
                return new CompanyInfoViewHolder(binding);
            }

            case DETAIL_VIEW_TYPE: {

                ListItemProjectDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_detail, parent, false);
                return new CompanyDetailViewHolder(binding);
            }

            case PROJECT_VIEW_TYPE: {

                ListItemMcdProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mcd_project, parent, false);
                return new CompanyProjectViewHolder(binding);
            }

            case BID_VIEW_TYPE: {

                ListItemMcdBidBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mcd_bid, parent, false);
                return new CompanyBidViewHolder(binding);
            }

            case CONTACT_VIEW_TYPE: {

                ListItemMcdContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mcd_contact, parent, false);
                return new CompanyContactViewHolder(binding);
            }

            case FOOTER_VIEW_TYPE: {

                ListItemMpdFooterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mpd_footer, parent, false);
                return new FooterViewHolder(binding);
            }

            default: {

                ListItemProjectDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_detail, parent, false);
                return new CompanyDetailViewHolder(binding);
            }
        }
    }

    /**
     * Private
     **/

    private class SectionHeaderVH extends RecyclerView.ViewHolder {

        private final ListItemSectionHeaderBinding binding;

        public SectionHeaderVH(ListItemSectionHeaderBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemSectionHeaderBinding getBinding() {
            return binding;
        }
    }

    private class CompanyHeaderViewHolder extends RecyclerView.ViewHolder {

        private final ListItemHeaderCompanyBinding binding;

        public CompanyHeaderViewHolder(ListItemHeaderCompanyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemHeaderCompanyBinding getBinding() {
            return binding;
        }
    }

    private class ShareViewHolder extends RecyclerView.ViewHolder {

        private final ListItemCompHeaderShareBinding binding;

        public ShareViewHolder(ListItemCompHeaderShareBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemCompHeaderShareBinding getBinding() {
            return binding;
        }
    }

    private class CompanyInfoViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMcdInfoBinding binding;

        public CompanyInfoViewHolder(ListItemMcdInfoBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMcdInfoBinding getBinding() {
            return binding;
        }
    }

    private class CompanyDetailViewHolder extends RecyclerView.ViewHolder {

        private final ListItemProjectDetailBinding binding;

        public CompanyDetailViewHolder(ListItemProjectDetailBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemProjectDetailBinding getBinding() {
            return binding;
        }
    }

    private class CompanyProjectViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMcdProjectBinding binding;

        public CompanyProjectViewHolder(ListItemMcdProjectBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMcdProjectBinding getBinding() {
            return binding;
        }
    }

    private class CompanyContactViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMcdContactBinding binding;

        public CompanyContactViewHolder(ListItemMcdContactBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMcdContactBinding getBinding() {
            return binding;
        }
    }

    private class CompanyBidViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMcdBidBinding binding;

        public CompanyBidViewHolder(ListItemMcdBidBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMcdBidBinding getBinding() {
            return binding;
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMpdFooterBinding binding;

        public FooterViewHolder(ListItemMpdFooterBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMpdFooterBinding getBinding() {
            return binding;
        }
    }
}