package com.lecet.app.adapters;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.lecet.app.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File: SubtypeExampleAdapter Created: 1/11/17 Author: domandtom
 * <p>
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class JurisdictionAdapter extends SectionedAdapter {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PARENT_VIEW_TYPE, SUB_VIEW_TYPE, SUB_SUB_VIEW_TYPE})
    @interface ViewTypes {
    }

    private static final int PARENT_VIEW_TYPE = 0;
    private static final int SUB_VIEW_TYPE = 1;
    private static final int SUB_SUB_VIEW_TYPE = 2;

    private List<Parent> data;
    private List<Integer> expandedParents;
    private Map<Integer, List<Integer>> expandedSubTypes;

    public JurisdictionAdapter(List<Parent> data) {

        this.data = data;

        // We will expand the parents in position 0, 3. In a real example
        // we will need to keep track of clicks and position.
        expandedParents = new ArrayList<>();
        // Expanded subtypes, we need to keep track of section and then subtype position
        expandedSubTypes = new HashMap<>();

        for (int i = 0; i < data.size(); i++) {
            expandedParents.add(i);
            List<Subtype> subtype = data.get(i).getSubtypes();
            if (subtype != null) {
                int subtypesize = subtype.size();
                if (subtypesize > 0) {
                    //  List subsubtype = subtype.get(i).se
                    List expandedSubtypeSection = new ArrayList();

                    /**
                     * TODO: Out Of Memory occurs when it implemented the commented codes below that I created.
                     */
/*                    for (int k = 0; k < subtype.size(); k++){
                        Log.d("subtypesize", "subtypesize " + subtypesize);
                    List<SubSubtype> subsubtype = subtype.get(k).getSubtypes();
                    if (subsubtype != null) {

                      //  List expandedSubtypeSection = new ArrayList();
                        for (int j = 0; j < subtypesize; i++) {
                            expandedSubtypeSection.add(j);
                        }
                    }
                    expandedSubTypes.put(k, expandedSubtypeSection);
                    }
                    */
                }
            }
        }
//        expandedParents.add(0);
//        expandedParents.add(2);

/**
 * TODO: Using the given code sample below does not display the subsubtype items... Is there any missing here?
 */
        List expandedSubtypeSectionZero = new ArrayList();
        //   expandedSubtypeSectionZero.add(2);

        List expandedSubtypeSectionTwo = new ArrayList();
        //   expandedSubtypeSectionTwo.add(1);

        // expandedSubTypes.put(0, expandedSubtypeSectionZero);
        // expandedSubTypes.put(2, expandedSubtypeSectionTwo);
    }

    @Override
    public int getSectionCount() {

        if (data == null) return 0;

        Log.d("SectionCount", " section count" + data.size());  //To check the section count returns;
        return data.size();
    }

    /**
     * I temporarily convert the subSubTypeSize to instance variable and assign a fixed value so I could check the 3rd-level display item.
     */
    int subSubTypeSize = 12;

    @Override
    public int getItemCountForSection(int section) {

        // Expanded scenario, where subtype and subsub are expanded
        Parent parent = data.get(section);
        List<Subtype> subtypes = parent.getSubtypes();

        if (subtypes == null) return 0;

        // Do we need to expand, if so check subtype size and if subsubtypes have been selected
        // if so we need to display them too.
        if (expandedParents.contains(section)) {

            int subTypeSize = subtypes.size();

            //MODIFIED
            //  int subSubTypeSize = 2;
/**
 * Note: I think the code below should be changed in order to display the 3rd-level items correctly.
 */
         /*   if (expandedSubTypes.containsKey(section)) {

                List<Integer> expandedSubSubPositions = expandedSubTypes.get(section);

                for (Integer pos : expandedSubSubPositions) {

                    Subtype subtype = subtypes.get(pos);
                    subSubTypeSize = subSubTypeSize + (subtype.getSubtypes() != null ? subtype.getSubtypes().size() : 0);
                }
            }*/
/**
 *  I cannot figure this out how it works. I observed that the last 3rd-level items (subsubtype) will only be displayed at the end of the last 2nd-level (subtype) section,
 *  not below on each 2nd-level items where it belongs.
 */
            return subTypeSize + subSubTypeSize;
        }

        return 0;
    }

    @Override
    public int getItemViewType(int section, int position) {

        // Check and see the section and size of subtypes. If we
        // position is greater than the size of the subtypes, then we
        // are assuming that it is a subsubtype view

        Parent parent = data.get(section);
        List<Subtype> subtype = parent.getSubtypes();

        int subtTypeSize = parent.getSubtypes() != null ? parent.getSubtypes().size() : 0;

        Log.d("ItemViewType", "ItemViewType: section" + section + " position:" + position + " subtype size:" + subtTypeSize); //To monitor how often it will call this method

//**??? The 3rd level item (subsubtype) only appears at the end of the section. I think the condition below should be modified.
        if (position < subtTypeSize) {
            return SUB_VIEW_TYPE;
        }

        return SUB_SUB_VIEW_TYPE;
    }
    @Override
    public int getHeaderViewType(int section) {
        return PARENT_VIEW_TYPE;
    }

    @Override
    public int getFooterViewType(int section) {
        return 0;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        Subtype subtype = null;
        if (holder instanceof SubTypeViewHolder) {

            Parent parent = data.get(section);

            // SubType position is denoted by position
            subtype = parent.getSubtypes().get(position);

            SubTypeViewHolder subTypeViewHolder = (SubTypeViewHolder) holder;
            subTypeViewHolder.checkView.setText(subtype.getName());
//            subTypeViewHolder.textView.setText(subtype.getName());


        } else if (holder instanceof SubSubTypeViewHolder) {

            //MODIFY2
            Parent parent = data.get(section);
            // SubType position is denoted by position
            List<Subtype> listSubType = parent.getSubtypes();

            /**
             * Using the code below, I was able to display the name in the 3rd-level item.
             */
            int subTypeCount = listSubType.size();
            //    int subSubtypePos = subTypeCount - position;
            int subSubtypePos = position - subTypeCount;
            Log.d("subsubsec", "subsubsec " + section + " position:" + position + " typepos:" + subSubtypePos + " count:" + subTypeCount);
            //SubSubtype subSubtype = data.get(section).getSubtypes().get(subSubtypePos);
            // Subtype subtype1 = data.get(section).getSubtypes().get(subSubtypePos);
            Subtype subtype1 = listSubType.get(subTypeCount - 1);
            List<SubSubtype> list2xSubType = subtype1.getSubtypes();
            SubSubTypeViewHolder subSubTypeViewHolder = (SubSubTypeViewHolder) holder;
            if (list2xSubType != null && list2xSubType.size() > subSubtypePos && subSubtypePos >= 0) {
                SubSubtype subSubtype = list2xSubType.get(subSubtypePos);
                //  if (subtype!=null){
                //    SubSubtype subSubtype = subtype.getSubtypes().get(position);
                subSubTypeViewHolder.checkView.setText(subSubtype.getName());
//               subSubTypeViewHolder.checkView.setText("position"+position+ " section"+section);
                //}
            }
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        ParentViewHolder parentViewHolder = (ParentViewHolder) holder;

        // Parent denoted by section number
        Parent parent = data.get(section);
//        parentViewHolder.textView.setText(parent.getName());
        parentViewHolder.checkView.setText(parent.getName());
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == SUB_VIEW_TYPE) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_mpfjurisdiction_local, parent, false);
            SubTypeViewHolder vh = new SubTypeViewHolder(v);
            return vh;

        } else if (viewType == SUB_SUB_VIEW_TYPE) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_mpfjurisdiction_district, parent, false);
            SubSubTypeViewHolder vh = new SubSubTypeViewHolder(v);
            return vh;

        } else {

            // create a parent layout
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_mpfjurisdiction_parent, parent, false);
            ParentViewHolder vh = new ParentViewHolder(v);
            return vh;
        }
    }

    /* We Use the Parent as the Header */
    @Override
    public boolean enableHeaderForSection(int section) {
        return true;
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {

        //  public TextView textView;
        public CheckBox checkView;

        public ParentViewHolder(View itemView) {
            super(itemView);
            checkView = (CheckBox) itemView.findViewById(R.id.j_name);
//            textView = (TextView) itemView.findViewById(R.id.name_text_view);

        }
    }

    public class SubTypeViewHolder extends RecyclerView.ViewHolder {

        //        public TextView textView;
        public CheckBox checkView;

        public SubTypeViewHolder(View itemView) {
            super(itemView);
            checkView = (CheckBox) itemView.findViewById(R.id.j_local_name);
            //    textView = (TextView) itemView.findViewById(R.id.name_text_view);
        }
    }

    public class SubSubTypeViewHolder extends RecyclerView.ViewHolder {

        //   public TextView textView;
        public CheckBox checkView;

        public SubSubTypeViewHolder(View itemView) {
            super(itemView);
            checkView = (CheckBox) itemView.findViewById(R.id.j_district_name);
            //  textView = (TextView) itemView.findViewById(R.id.name_text_view);
        }
    }

    public static class Parent {

        private String name;
        private List<Subtype> subtypes;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSubtypes(List<Subtype> subtypes) {
            this.subtypes = subtypes;
        }

        public List<Subtype> getSubtypes() {
            return subtypes;
        }
    }

    public static class Subtype {

        private String name;
        private List<SubSubtype> subtypes;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSubtypes(List<SubSubtype> subtypes) {
            this.subtypes = subtypes;
        }

        public List<SubSubtype> getSubtypes() {
            return subtypes;
        }
    }

    public static class SubSubtype {

        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
}


/**
 * On final note. I think there's a need to add or modify codes in the SectionedAdapter in order to work the 3rd-level multi-item selection.
 */