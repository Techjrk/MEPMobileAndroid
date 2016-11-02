package com.lecet.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lecet.app.R;

import java.util.List;

/**
 * File: ProjectDetailAdapter Created: 10/27/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailAdapter extends SectionedAdapter {

    //private static final int

    private static final int headerType1 = 0;
    private static final int headerType2 = 1;
    private static final int sectionType1 = 2;
    private static final int sectionType2 = 3;


    private List<List<String>> data;

    public ProjectDetailAdapter(List<List<String>> data) {

        this.data = data;
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

        if (section == 0 && section == 1) {
            return sectionType1;
        } else {

            return sectionType2;
        }
    }

    @Override
    public int getHeaderViewType(int section) {
        if (section == 0) return headerType1;

        return headerType2;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {


    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == headerType1) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.test_section_1, parent, false);

            return new SectionHeaderVH(v);

        }

        return null;
    }

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


    private class ProjectTitleViewHolder extends RecyclerView.ViewHolder {

        public ProjectTitleViewHolder(View view) {
            super(view);
        }
    }

    private class CallToActionViewHolder extends RecyclerView.ViewHolder {

        public CallToActionViewHolder(View itemView) {
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
