package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ListItemHeaderProjectBinding;
import com.lecet.app.databinding.ListItemHeaderShareBinding;
import com.lecet.app.databinding.ListItemProjectDetailBinding;
import com.lecet.app.databinding.ListItemProjectNoteBinding;
import com.lecet.app.databinding.ListItemSectionHeaderBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.HeaderViewModel;
import com.lecet.app.viewmodel.ProjDetailItemViewModel;
import com.lecet.app.viewmodel.ProjectDetailHeaderViewModel;
import com.lecet.app.viewmodel.ProjectShareToolbarViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * File: ProjectDetailAdapter Created: 10/27/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailAdapter extends SectionedAdapter {

    private static final String TAG = "ProjectDetailAdapter";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PROJECT_HEADER, SECTION_HEADER, SHARE_HEADER, ALL_VIEW_TYPE, NOTE_VIEW_TYPE})
    @interface ViewTypes {
    }

    private static final int PROJECT_HEADER = 0;
    private static final int SECTION_HEADER = 1;
    private static final int SHARE_HEADER = 2;
    private static final int ALL_VIEW_TYPE = 3;
    private static final int NOTE_VIEW_TYPE = 4;
    private static final int FOOTER_VIEW_TYPE = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SECTION_TITLE, SECTION_SHARE, SECTION_NOTES, SECTION_PARTICIPANTS, SECTION_BIDDERS})
    @interface Sections {
    }

    private static final int SECTION_TITLE = 0;
    private static final int SECTION_SHARE = 1;
    private static final int SECTION_NOTES = 2;
    private static final int SECTION_PARTICIPANTS = 3;
    private static final int SECTION_BIDDERS = 4;

    private AppCompatActivity appCompatActivity;
    private Project project;
    private List<List<ProjDetailItemViewModel>> data;
    private ProjectDetailHeaderViewModel headerViewModel;
    private ProjectDomain projectDomain;

    public ProjectDetailAdapter(AppCompatActivity appCompatActivity, Project project, List<List<ProjDetailItemViewModel>> data, ProjectDetailHeaderViewModel headerViewModel, ProjectDomain projectDomain) {

        this.appCompatActivity = appCompatActivity;
        this.project = project;
        this.data = data;
        this.headerViewModel = headerViewModel;
        this.projectDomain = projectDomain;
    }

    @Override
    public int getSectionCount() {
        return data.size();
    }

    @Override
    public int getItemCountForSection(int section) {
        return data.get(section).size();
    }

    @Override
    public int getItemViewType(int section, int position) {

        switch (section) {
            case SECTION_NOTES:
                return NOTE_VIEW_TYPE;
            default:
                return ALL_VIEW_TYPE;
        }
    }

    @Override
    public int getHeaderViewType(int section) {
        switch (section) {
            case SECTION_TITLE:
                return PROJECT_HEADER;
            case SECTION_SHARE:
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

        if (section == SECTION_SHARE || section == SECTION_BIDDERS) {
            return true;
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

        if (holder instanceof LineItemViewHolder) {

            ProjDetailItemViewModel viewModel = data.get(section).get(position);
            ((LineItemViewHolder) holder).getBinding().setViewModel(viewModel);

        } else if (holder instanceof NotesViewHolder) {

            ProjDetailItemViewModel viewModel = data.get(section).get(position);
            ((NotesViewHolder) holder).getBinding().setViewModel(viewModel);
        }
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, final int section) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Footer Section: " + section + " Clicked");
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

            case ALL_VIEW_TYPE: {

                ListItemProjectDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_detail, parent, false);
                return new LineItemViewHolder(binding);
            }

            case NOTE_VIEW_TYPE: {

                ListItemProjectNoteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_note, parent, false);
                return new NotesViewHolder(binding);
            }

            case FOOTER_VIEW_TYPE: {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_mpd_footer, parent, false);

                return new FooterViewHolder(v);
            }

            default: {

                ListItemProjectDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_project_detail, parent, false);
                return new LineItemViewHolder(binding);
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

    private class LineItemViewHolder extends RecyclerView.ViewHolder {

        private final ListItemProjectDetailBinding binding;

        public LineItemViewHolder(ListItemProjectDetailBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemProjectDetailBinding getBinding() {
            return binding;
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
