package com.lecet.app.adapters;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.viewmodel.ProjDetailItemViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * File: ProjectDetailAdapter Created: 10/27/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailAdapter extends SectionedAdapter {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PROJECT_HEADER, SECTION_HEADER, SHARE_HEADER, ALL_VIEW_TYPE, NOTE_VIEW_TYPE})
    @interface ViewTypes {
    }

    private static final int PROJECT_HEADER = 0;
    private static final int SECTION_HEADER = 1;
    private static final int SHARE_HEADER = 2;
    private static final int ALL_VIEW_TYPE = 3;
    private static final int NOTE_VIEW_TYPE = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SECTION_TITLE, SECTION_SHARE, SECTION_NOTES, SECTION_PARTICIPANTS, SECTION_BIDDERS})
    @interface Sections {
    }

    private static final int SECTION_TITLE = 0;
    private static final int SECTION_SHARE = 1;
    private static final int SECTION_NOTES = 2;
    private static final int SECTION_PARTICIPANTS = 3;
    private static final int SECTION_BIDDERS = 4;

    private List<List<ProjDetailItemViewModel>> data;
    private Project project;

    public ProjectDetailAdapter(Project project, Context context) {

        this.project = project;
        this.data = buildDetails(project, context);
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
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {


    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case PROJECT_HEADER: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_header_project, parent, false);

                return new ProjectHeaderViewHolder(v);
            }

            case SECTION_HEADER: {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_section_header, parent, false);

                return new SectionHeaderVH(v);
            }

            case SHARE_HEADER: {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_header_share, parent, false);

                return new ShareViewHolder(v);
            }

            case ALL_VIEW_TYPE: {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_project_detail, parent, false);

                return new LineItemViewHolder(v);
            }

            case NOTE_VIEW_TYPE: {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_project_note, parent, false);

                return new NotesViewHolder(v);
            }
            default: {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_project_detail, parent, false);

                return new LineItemViewHolder(v);
            }
        }
    }

    /** Private **/

    private List<List<ProjDetailItemViewModel>> buildDetails(Project project, Context context) {

        List<List<ProjDetailItemViewModel>> data = new ArrayList<>();

        // First section will only have a header
        List<ProjDetailItemViewModel> section0 = new ArrayList<>();
        data.add(section0);


        // Build Project Details
        List<ProjDetailItemViewModel> section1 = new ArrayList<>();
        data.add(section1);

        section1.add(new ProjDetailItemViewModel(context.getString(R.string.county), project.getCounty()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.project_id), project.getDodgeNumber()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.address), project.getFullAddress()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.project_id), project.getProjectTypes()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.est_low), String.format("$ %,f", project.getEstLow())));

        // Notes
        List<ProjDetailItemViewModel> section2 = new ArrayList<>();

        String notes = "";

        if (project.getProjectNotes() != null) notes = project.getProjectNotes();
        if (project.getStdIncludes() != null) notes = notes + " " + project.getStdIncludes();

        section2.add(new ProjDetailItemViewModel(null, notes));

        return data;
    }


    /** ViewHolders **/

    private class SectionHeaderVH extends RecyclerView.ViewHolder {

        private TextView textView;

        public SectionHeaderVH(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.section_1_text_view);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    private class ProjectHeaderViewHolder extends RecyclerView.ViewHolder {

        public ProjectHeaderViewHolder(View view) {
            super(view);
        }
    }

    private class ShareViewHolder extends RecyclerView.ViewHolder {

        public ShareViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NotesViewHolder extends RecyclerView.ViewHolder {

        public NotesViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class LineItemViewHolder extends RecyclerView.ViewHolder {

        public LineItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
