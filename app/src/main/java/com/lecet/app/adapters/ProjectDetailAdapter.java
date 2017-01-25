package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ListItemHeaderProjectBinding;
import com.lecet.app.databinding.ListItemHeaderShareBinding;
import com.lecet.app.databinding.ListItemMpdBidBinding;
import com.lecet.app.databinding.ListItemMpdFooterBinding;
import com.lecet.app.databinding.ListItemMpdParticipantBinding;
import com.lecet.app.databinding.ListItemProjectDetailBinding;
import com.lecet.app.databinding.ListItemProjectNoteBinding;
import com.lecet.app.databinding.ListItemSectionHeaderBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.DetailFooterViewModel;
import com.lecet.app.viewmodel.HeaderViewModel;
import com.lecet.app.viewmodel.ProjDetailItemViewModel;
import com.lecet.app.viewmodel.ProjectDetailBidViewModel;
import com.lecet.app.viewmodel.ProjectDetailContactViewModel;
import com.lecet.app.viewmodel.ProjectDetailHeaderViewModel;
import com.lecet.app.viewmodel.ProjectShareToolbarViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * File: ProjectDetailAdapter Created: 10/27/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailAdapter extends SectionedAdapter {

    private static final String TAG = "ProjectDetailAdapter";

    private static final int PROJECT_HEADER = 0;
    private static final int SECTION_HEADER = 1;
    private static final int SHARE_HEADER = 2;
    private static final int DETAIL_VIEW_TYPE = 3;
    private static final int NOTE_VIEW_TYPE = 4;
    private static final int PARTICIPANT_VIEW_TYPE = 5;
    private static final int BID_VIEW_TYPE = 6;
    private static final int FOOTER_VIEW_TYPE = 7;

    private static final int SECTION_TITLE = 0;
    private static final int SECTION_DETAILS = 1;
    private static final int SECTION_NOTES = 2;

    // Default values;

    private int SECTION_PARTICIPANTS = -1;
    private int SECTION_BIDDERS = -1;

    private static final int MIN_SECTIONS = 2;

    private static final int DETAILS_DEFAULT_COUNT = 5;

    private AppCompatActivity appCompatActivity;
    private Project project;
    private List<ProjDetailItemViewModel> projectDetails;
    private ProjDetailItemViewModel notes;
    private RealmResults<Bid> projectBids;
    private RealmResults<Contact> projectParticipants;
    private ProjectDetailHeaderViewModel headerViewModel;
    private ProjectDomain projectDomain;

    private boolean detailsExpanded;
    private boolean notesDisplayed;
    private boolean bidsDisplayed;
    private boolean participantsDisplayed;

    public ProjectDetailAdapter(AppCompatActivity appCompatActivity, @NonNull Project project, @NonNull List<ProjDetailItemViewModel> projectDetails, ProjDetailItemViewModel notes, RealmResults<Bid> projectBids, RealmResults<Contact> projectParticipants, ProjectDetailHeaderViewModel headerViewModel, ProjectDomain projectDomain) {

        this.appCompatActivity = appCompatActivity;
        this.project = project;
        this.projectDetails = projectDetails;
        this.notes = notes;
        this.projectBids = projectBids;
        this.projectParticipants = projectParticipants;
        this.headerViewModel = headerViewModel;
        this.projectDomain = projectDomain;

        this.notesDisplayed = notes != null;
        this.bidsDisplayed = projectBids != null && projectBids.size() > 0;
        this.participantsDisplayed = projectParticipants != null && projectParticipants.size() > 0;

        this.SECTION_PARTICIPANTS = notesDisplayed ? 3 : 2;
        if (notesDisplayed && participantsDisplayed) {

            this.SECTION_BIDDERS = 4;

        } else if (!notesDisplayed || !participantsDisplayed) {

            this.SECTION_BIDDERS = 3;
        }
    }

    public void setDetailsExpanded(boolean detailsExpanded) {
        this.detailsExpanded = detailsExpanded;
        notifyDataSetChanged();
    }

    public boolean isDetailsExpanded() {
        return detailsExpanded;
    }

    @Override
    public int getSectionCount() {

        // MIN_SECTIONS plus the any additional sections available

        return MIN_SECTIONS + (notesDisplayed ? 1 : 0) + (bidsDisplayed ? 1 : 0) + (participantsDisplayed ? 1 :  0);
    }

    @Override
    public int getItemCountForSection(int section) {

        if (section == SECTION_DETAILS) {

            if (!isDetailsExpanded()) {

                // Show details up to Est. Low
                return DETAILS_DEFAULT_COUNT;

            } else {

                return projectDetails.size();
            }
        }

        if (notesDisplayed && section == SECTION_NOTES) {

            return  1;
        }

        if (participantsDisplayed && section == SECTION_PARTICIPANTS) {

            return projectParticipants.size() <= 3 ? projectParticipants.size() : 3;
        }

        if (bidsDisplayed && section == SECTION_BIDDERS) {

            return projectBids.size() <= 3 ? projectBids.size() : 3;
        }

        return 0;
    }

    @Override
    public int getItemViewType(int section, int position) {

        if (section == SECTION_NOTES) {

            return NOTE_VIEW_TYPE;
        }

        if (section == SECTION_PARTICIPANTS) {

            return PARTICIPANT_VIEW_TYPE;
        }

        if (section == SECTION_DETAILS) {

            return DETAIL_VIEW_TYPE;
        }

        if (section == SECTION_BIDDERS) {

            return BID_VIEW_TYPE;
        }

        return DETAIL_VIEW_TYPE;
    }

    @Override
    public int getHeaderViewType(int section) {
        switch (section) {
            case SECTION_TITLE:
                return PROJECT_HEADER;
            case SECTION_DETAILS:
                return SHARE_HEADER;
            default:
                return SECTION_HEADER;
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

        if (section == SECTION_DETAILS) {
            return true;
        }

        if (participantsDisplayed && section == SECTION_PARTICIPANTS) {

            return projectParticipants.size() > 3;
        }

        if (bidsDisplayed && section == SECTION_BIDDERS) {

            return projectBids.size() > 3;
        }

        return false;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        if (holder instanceof ProjectHeaderViewHolder) {

            ((ProjectHeaderViewHolder) holder).getBinding().setViewModel(headerViewModel);
        } else if (holder instanceof SectionHeaderVH) {

            if (section == SECTION_NOTES) {

                ((SectionHeaderVH) holder).getBinding().setViewModel(new HeaderViewModel("Notes"));

            } else if (section == SECTION_PARTICIPANTS) {

                ((SectionHeaderVH) holder).getBinding().setViewModel(new HeaderViewModel("Participants"));

            } else if (section == SECTION_BIDDERS) {

                ((SectionHeaderVH) holder).getBinding().setViewModel(new HeaderViewModel("Project Bidders"));

            }
        } else if (holder instanceof ShareViewHolder) {
            Log.d("SHARE", "Section Share");
            TrackingListDomain trackingListDomain =  new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(appCompatActivity), Realm.getDefaultInstance(), new RealmChangeListener() {
                @Override
                public void onChange(Object element) {

                }
            }, projectDomain);
            ((ShareViewHolder) holder).getBinding().setViewModel(new ProjectShareToolbarViewModel(appCompatActivity, trackingListDomain, project));
        }
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {

        if (section == SECTION_DETAILS) {

            ProjDetailItemViewModel viewModel = projectDetails.get(position);
            ((DetailItemViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (section == SECTION_NOTES) {

            ((NotesViewHolder) holder).getBinding().setViewModel(notes);

        } else if (section == SECTION_PARTICIPANTS) {

            Contact contact = projectParticipants.get(position);

            ((ParticipantViewHolder) holder).getBinding().setViewModel(new ProjectDetailContactViewModel(appCompatActivity, contact));

        } else if (section == SECTION_BIDDERS) {

            Bid bid = projectBids.get(position);
            ((BidViewHolder) holder).getBinding().setViewModel(new ProjectDetailBidViewModel(appCompatActivity, bid));
        }
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, final int section) {

        if (section == SECTION_DETAILS) {

            DetailFooterViewModel viewModel = new DetailFooterViewModel(appCompatActivity, "", "");
            ((FooterViewHolder) holder).getBinding().setViewModel(viewModel);
        } else if (section == SECTION_PARTICIPANTS) {

            DetailFooterViewModel viewModel = new DetailFooterViewModel(appCompatActivity, String.valueOf(projectParticipants.size()), appCompatActivity.getString(R.string.participants));
            ((FooterViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (section == SECTION_BIDDERS) {

            DetailFooterViewModel viewModel = new DetailFooterViewModel(appCompatActivity, String.valueOf(projectBids.size()), appCompatActivity.getString(R.string.bidders));
            ((FooterViewHolder) holder).getBinding().setViewModel(viewModel);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (section == SECTION_DETAILS) {
                    setDetailsExpanded(!detailsExpanded);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case PROJECT_HEADER: {

                ListItemHeaderProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_header_project, parent, false);
                return new ProjectHeaderViewHolder(binding);
            }

            case SECTION_HEADER: {

                ListItemSectionHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_section_header, parent, false);
                return new SectionHeaderVH(binding);
            }

            case SHARE_HEADER: {

                ListItemHeaderShareBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_header_share, parent, false);

                return new ShareViewHolder(binding);
            }

            case DETAIL_VIEW_TYPE: {

                ListItemProjectDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_detail, parent, false);
                return new DetailItemViewHolder(binding);
            }

            case NOTE_VIEW_TYPE: {

                ListItemProjectNoteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_note, parent, false);
                return new NotesViewHolder(binding);
            }

            case PARTICIPANT_VIEW_TYPE: {

                ListItemMpdParticipantBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mpd_participant, parent, false);
                return new ParticipantViewHolder(binding);
            }

            case BID_VIEW_TYPE: {

                ListItemMpdBidBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mpd_bid, parent, false);
                return new BidViewHolder(binding);
            }

            case FOOTER_VIEW_TYPE: {

                ListItemMpdFooterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_mpd_footer, parent, false);
                return new FooterViewHolder(binding);
            }

            default: {

                ListItemProjectDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_detail, parent, false);
                return new DetailItemViewHolder(binding);
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

    private class ProjectHeaderViewHolder extends RecyclerView.ViewHolder {

        private final ListItemHeaderProjectBinding binding;

        public ProjectHeaderViewHolder(ListItemHeaderProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemHeaderProjectBinding getBinding() {
            return binding;
        }
    }

    private class ShareViewHolder extends RecyclerView.ViewHolder {

        private final ListItemHeaderShareBinding binding;

        public ShareViewHolder(ListItemHeaderShareBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemHeaderShareBinding getBinding() {
            return binding;
        }
    }

    private class NotesViewHolder extends RecyclerView.ViewHolder {

        private final ListItemProjectNoteBinding binding;

        private NotesViewHolder(ListItemProjectNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ListItemProjectNoteBinding getBinding() {
            return binding;
        }
    }

    private class DetailItemViewHolder extends RecyclerView.ViewHolder {

        private final ListItemProjectDetailBinding binding;

        public DetailItemViewHolder(ListItemProjectDetailBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemProjectDetailBinding getBinding() {
            return binding;
        }
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

    private class BidViewHolder extends RecyclerView.ViewHolder {

        private final ListItemMpdBidBinding binding;

        public BidViewHolder(ListItemMpdBidBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemMpdBidBinding getBinding() {
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
