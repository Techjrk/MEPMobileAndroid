package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.content.SearchFilterMPFJurisdictionActivity2;
import com.lecet.app.databinding.ListItemMpfjurisdictionChildBinding;
import com.lecet.app.databinding.ListItemMpfjurisdictionGrandchildBinding;
import com.lecet.app.databinding.ListItemMpfjurisdictionParentBinding;
import com.lecet.app.viewmodel.SearchFilterMPFJurisdictionViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * File: SubtypeExampleAdapter Created: 1/11/17 Author: domandtom
 * <p>
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class JurisdictionAdapter extends SectionedAdapter {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PARENT_VIEW_TYPE, CHILD_VIEW_TYPE, GRAND_CHILD_VIEW_TYPE})
    @interface ViewTypes {
    }

    private static final int PARENT_VIEW_TYPE = 0;
    private static final int CHILD_VIEW_TYPE = 1;
    private static final int GRAND_CHILD_VIEW_TYPE = 2;

    private List<Parent> data;
    private SearchFilterMPFJurisdictionViewModel viewModel;
    private List<Integer> expandedParents; // Keep track of expanded parents
    private Map<Integer, TreeMap<Integer, Integer>> expandedChildren; // Key maps to section, Value maps to a TreeMap which keeps track of selected child position and grandchildren count.

    public JurisdictionAdapter(List<Parent> data, SearchFilterMPFJurisdictionViewModel viewModel) {

        this.data = data;
        this.viewModel = viewModel;
        expandedParents = new ArrayList<>();

        // Expanded grandChildrens, we need to keep track of section and then subtype position
        expandedChildren = new HashMap<>();
/*
        ListItemMpfjurisdictionParentBinding bindParent = DataBindingUtil.setContentView(activity, R.layout.list_item_mpfjurisdiction_parent);
        ListItemMpfjurisdictionChildBinding bindChild = DataBindingUtil.setContentView(activity, R.layout.list_item_mpfjurisdiction_child);
        ListItemMpfjurisdictionGrandchildBinding bindGrandChild = DataBindingUtil.setContentView(activity, R.layout.list_item_mpfjurisdiction_grandchild);
        bindParent.setViewModel(viewModel);
        bindChild.setViewModel(viewModel);
        bindGrandChild.setViewModel(viewModel);*/
    }

    @Override
    public int getSectionCount() {

        if (data == null) return 0;

        return data.size();
    }

    @Override
    public int getItemCountForSection(int section) {

        // Expanded scenario, where subtype and subsub are expanded
        Parent parent = data.get(section);
        List<Child> children = parent.getChildren();

        if (children == null) return 0;

        // Do we need to expand, if so check children size and if a child has been selected
        // if so we need to display the grand children too.
        if (expandedParents.contains(section)) {

            int childrenSize = children.size();

            if (expandedChildren.containsKey(section)) {

                int grandChildrenSize = 0;

                TreeMap<Integer, Integer> expandedChilrenPositions = expandedChildren.get(section);

                for (Map.Entry<Integer, Integer> entry : expandedChilrenPositions.entrySet()) {
                    Integer size = entry.getValue();
                    grandChildrenSize = grandChildrenSize + size;
                }

                Log.d("SubTypeExample", "section = " + section + ", children = " + childrenSize + ", grandChildren = " + grandChildrenSize);

                return childrenSize + grandChildrenSize;
            }

            return childrenSize;
        }

        return 0;
    }

    @Override
    public int getItemViewType(int section, int position) {

        return isChild(section, position) ? CHILD_VIEW_TYPE : GRAND_CHILD_VIEW_TYPE;
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
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int section, final int position) {

        Parent parent = data.get(section);
        boolean isChild = isChild(section, position);

        if (holder instanceof ChildViewHolder && isChild) {

            Integer truePosition = childPositionInIndex(section, position);
            final Child child = parent.getChildren().get(truePosition);

            final ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            childViewHolder.checkView.setText(child.name);
            childViewHolder.checkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        viewModel.setJurisdictionExtraName(childViewHolder.checkView.getText().toString());
                      //  Log.d("check","check"+childViewHolder.checkView.getText().toString());
                    }
                }
            });
//            childViewHolder.checkView.setText("ch sect: " + section + ", pos: " + position + ", name:" + child.name);

            childViewHolder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TreeMap<Integer, Integer> expanded = expandedChildren.get(section);
                    if (expanded == null) {

                        expanded = new TreeMap<>();
                        expandedChildren.put(section, expanded);
                    }

                    // If the position is already in the expanded list, then we need to remove it
                    if (expanded.containsKey(position)) {

                        // Keep track of what needs to be added and removed.
                        List<Integer> toBeRemoved = new ArrayList<>();
                        TreeMap<Integer, Integer> toBeAdded = new TreeMap<>();

                        // We need to find any selected positions that will be affected by the
                        // new child selection. If the previously selected child's position is in a position
                        // greater than selected position, we need to decrease previous selection by
                        // selected grand child count.
                        SortedMap<Integer, Integer> tailMap = expanded.tailMap(position, false);

                        // Increase every child position, by the count of the grandchildren in the set. We also need to keep
                        // track of the keys that need to be removed from the expandedChildren.
                        for (Iterator<Map.Entry<Integer, Integer>> it = tailMap.entrySet().iterator(); it.hasNext(); ) {
                            Map.Entry<Integer, Integer> entry = it.next();

                            Integer key = entry.getKey();
                            Integer value = entry.getValue();

                            toBeRemoved.add(key);

                            Integer newKey = key - (child.getGrandChildren() != null ? child.getGrandChildren().size() : 0);
                            toBeAdded.put(newKey, value);
                        }

                        for (Integer integer : toBeRemoved) {
                            expanded.remove(integer);
                        }

                        expanded.remove(Integer.valueOf(position));
                        expanded.putAll(toBeAdded);

                    } else {

                        // Keep track of what needs to be added and removed.
                        List<Integer> toBeRemoved = new ArrayList<>();
                        TreeMap<Integer, Integer> toBeAdded = new TreeMap<>();

                        // We need to find any selected positions that will be affected by the
                        // new child selection. If the previously selected child's position is in a position
                        // greater than selected position, we need to increase previous selection by
                        // selected grand child count.
                        SortedMap<Integer, Integer> tailMap = expanded.tailMap(position);

                        // Increase every child position, by the count of the grandchildren in the set. We also need to keep
                        // track of the keys that need to be removed from the expandedChildren.
                        for (Iterator<Map.Entry<Integer, Integer>> it = tailMap.entrySet().iterator(); it.hasNext(); ) {
                            Map.Entry<Integer, Integer> entry = it.next();

                            Integer key = entry.getKey();
                            Integer value = entry.getValue();

                            toBeRemoved.add(key);

                            Integer newKey = key + (child.getGrandChildren() != null ? child.getGrandChildren().size() : 0);
                            toBeAdded.put(newKey, value);
                        }

                        for (Integer integer : toBeRemoved) {
                            expanded.remove(integer);
                        }

                        expanded.put(position, child.getGrandChildren() != null ? child.getGrandChildren().size() : 0);
                        expanded.putAll(toBeAdded);
                    }

                    notifyDataSetChanged();
                }
            });

        } else if (holder instanceof GrandChildTypeViewHolder && !isChild) {

/**
 * TODO: Display the grandchildren name. There are some blank items displaying especially towards at the end of the child section.
 * Getting the grandchildren's name is erratic.
 */

            Integer truePosition = childPositionInIndex(section, position);
        //    Child child = parent.getChildren().get(truePosition);
        //    if (child.getGrandChildren() == null) return;
            Map.Entry<Integer, Integer> floorEntry = expandedChildren.get(section).floorEntry(position);
            Integer selectedChildPosition = floorEntry.getKey();
            Integer grandChildren = floorEntry.getValue();
            Integer grandChildrenEndPostion = selectedChildPosition + grandChildren;
//           child = parent.getChildren().get(grandChildrenEndPostion-1);
//           child = parent.getChildren().get(truePosition+grandChildren-1);
       //     int gsize = child.getGrandChildren().size();
//            final GrandChild gchild = child.getGrandChildren().get(0);
            //       int compute = position - grandChildrenEndPostion-(grandChildren-1);
        //    int compute = position - selectedChildPosition + grandChildren - 1;
            //    if (compute == -1) compute+=1;
            //      final GrandChild gchild = child.getGrandChildren().get(compute);
            GrandChildTypeViewHolder subTypeViewHolder = (GrandChildTypeViewHolder) holder;
            subTypeViewHolder.checkView.setText("GChild sect:" + section + ", pos:" + position + ", truepos:" + truePosition +
                    " endp:" + grandChildrenEndPostion + " selChPos:" + selectedChildPosition + " gch:" + grandChildren);
//            subTypeViewHolder.checkView.setText("GChild sect:" + section + ", position:" + position);
        }
    }

    // int gsize=0;
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, final int section) {

        final ParentViewHolder parentViewHolder = (ParentViewHolder) holder;

        // Parent denoted by section number
        Parent parent = data.get(section);
        parentViewHolder.checkView.setText(parent.getName());
        parentViewHolder.checkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    viewModel.setJurisdictionExtraName(parentViewHolder.checkView.getText().toString());
                      Log.d("check","check"+parentViewHolder.checkView.getText().toString());
                }
            }
        });
        ((ParentViewHolder) holder).imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (expandedParents.contains(section)) {

                    expandedParents.remove(Integer.valueOf(section));
                    expandedChildren.remove(Integer.valueOf(section));

                } else {

                    expandedParents.add(section);
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == CHILD_VIEW_TYPE) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_mpfjurisdiction_child, parent, false);
            ChildViewHolder vh = new ChildViewHolder(v);
            return vh;

        } else if (viewType == GRAND_CHILD_VIEW_TYPE) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_mpfjurisdiction_grandchild, parent, false);
            GrandChildTypeViewHolder vh = new GrandChildTypeViewHolder(v);
            return vh;

        } else {

            // create a parent layout
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_mpfjurisdiction_parent, parent, false);
/*
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.test_parent_layout, parent, false);
*/
            ParentViewHolder vh = new ParentViewHolder(v);
            return vh;
        }
    }

    /* We Use the Parent as the Header */
    @Override
    public boolean enableHeaderForSection(int section) {
        return true;
    }

    /**
     * Use to check and see if the section and position relates to child position
     * in the data set.
     *
     * @param section  The section in the data set you want to check against.
     * @param position The position in the section.
     * @return true if the section, position in the data set relates to a child. False equates
     * to a grandchild.
     */
    public boolean isChild(int section, int position) {

        // Check to see if the section, position is an expanded position
        if (expandedChildren.containsKey(section)) {

            // Lets check the floor key for the given position. If it returns null then we know
            // that there is no selected child before it and it must be CHILD_VIEW_TYPE.
            if (expandedChildren.get(section).floorKey(position) == null) {

                return true;

            } else {

                // We know there is an expanded child, if the position equals expanded child.
                // then we know that its not a grandchild
                Map.Entry<Integer, Integer> floorEntry = expandedChildren.get(section).floorEntry(position);
                Integer selectedChildPosition = floorEntry.getKey();
                Integer grandChildren = floorEntry.getValue();

                if (selectedChildPosition == position) {

                    return true;

                } else {

                    Integer grandChildrenEndPostion = selectedChildPosition + grandChildren;
                    return position > grandChildrenEndPostion;
                }
            }
        }

        return true;
    }

    /**
     * Use this method to get the true position of child within a data set.
     * Best if you check if the
     *
     * @param section  The section in the adapter
     * @param position The position within the given section.
     * @return The position within the original data set.
     */
    public Integer childPositionInIndex(Integer section, Integer position) {

        // Check to see if the section, position is an expanded position
        if (expandedChildren.containsKey(section)) {

            // Lets check the floor key for the given position. If it returns null then we know
            // that there is no selected child before it and it must be CHILD_VIEW_TYPE.
            if (expandedChildren.get(section).floorKey(position) == null) {

                return position;

            } else {

                // Get a count of all grandchildren visible before this position.
                int grandChildrenSize = 0;

                TreeMap<Integer, Integer> expandedChildrenPositions = expandedChildren.get(section);

                NavigableMap<Integer, Integer> floorMap = expandedChildrenPositions.headMap(position, false);

                for (Map.Entry<Integer, Integer> entry : floorMap.entrySet()) {
                    Integer size = entry.getValue();
                    grandChildrenSize = grandChildrenSize + size;
                }

                return position - grandChildrenSize;
            }
        }

        return position;
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {

        //        public TextView textView;
        public CheckBox checkView;
        public ImageView imgView;

        public ParentViewHolder(View itemView) {
            super(itemView);

//            textView = (TextView) itemView.findViewById(R.id.name_text_view);
            checkView = (CheckBox) itemView.findViewById(R.id.j_parent);
            imgView = (ImageView) itemView.findViewById(R.id.j_parent_img);
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        //        public TextView textView;
        public CheckBox checkView;
        public ImageView imgView;

        public ChildViewHolder(View itemView) {
            super(itemView);

//            textView = (TextView) itemView.findViewById(R.id.name_text_view);
            checkView = (CheckBox) itemView.findViewById(R.id.j_child);
            imgView = (ImageView) itemView.findViewById(R.id.j_child_img);
        }
    }

    public class GrandChildTypeViewHolder extends RecyclerView.ViewHolder {

        //        public TextView textView;
        public CheckBox checkView;

        public GrandChildTypeViewHolder(View itemView) {
            super(itemView);

//            textView = (TextView) itemView.findViewById(R.id.name_text_view);
            checkView = (CheckBox) itemView.findViewById(R.id.j_grandchild);
        }
    }

    public static class Parent {

        private String name;
        private List<Child> children;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setChildren(List<Child> children) {
            this.children = children;
        }

        public List<Child> getChildren() {
            return children;
        }
    }

    public static class Child {

        private String name;
        private List<GrandChild> grandChildren;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setGrandChildren(List<GrandChild> grandChildren) {
            this.grandChildren = grandChildren;
        }

        public List<GrandChild> getGrandChildren() {
            return grandChildren;
        }
    }

    public static class GrandChild {

        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}

