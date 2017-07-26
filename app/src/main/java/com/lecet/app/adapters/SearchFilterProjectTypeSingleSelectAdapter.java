package com.lecet.app.adapters;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchFilterProjectTypeViewModel;
import com.lecet.app.viewmodel.SearchFilterStageViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * File: SubtypeExampleAdapter Created: 1/11/17 Author: domandtom
 * <p>
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class SearchFilterProjectTypeSingleSelectAdapter extends SectionedAdapter {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PARENT_VIEW_TYPE, CHILD_VIEW_TYPE, GRAND_CHILD_VIEW_TYPE})
    @interface ViewTypes {
    }
    public static final int NO_TYPE = -1;
    private static final int PARENT_VIEW_TYPE = 0;
    private static final int CHILD_VIEW_TYPE = 1;
    private static final int GRAND_CHILD_VIEW_TYPE = 2;
//    public static boolean customSearch;

    private static int NO_IMAGE = 0;
    private CheckBox cb = null;
    private List<SearchFilterProjectTypeAdapter.Parent> data;
    private SearchFilterProjectTypeViewModel viewModel;
    private List<Integer> expandedParents; // Keep track of expanded parents
    private Map<Integer, TreeMap<Integer, Integer>> expandedChildren; // Key maps to section, Value maps to a TreeMap which keeps track of selected child position and grandchildren count.

    //** for single-item project near me section
    private Bundle selectedParent = new Bundle(); //list of user's selected parent items
    private Bundle selectedChild = new Bundle();  //list of user's selected child item

    public SearchFilterProjectTypeSingleSelectAdapter(List <SearchFilterProjectTypeAdapter.Parent>  data, SearchFilterProjectTypeViewModel viewModel) {

        this.data = data;
        this.viewModel = viewModel;
        expandedParents = new ArrayList<>();

        // Expanded grandChildrens, we need to keep track of section and then subtype position
        expandedChildren = new HashMap<>();

    }

    public void clearLast() {
        viewModel.setLastChecked(null);
        viewModel.setLastFamilyChecked(NO_TYPE);
        viewModel.setLastName("");
        //Additional to clear
        viewModel.setLastChildParentPosition(0);
        viewModel.setLastSection(0);
        viewModel.setLastPosition(0);
    }

    private void checkLastSelect(boolean selected) {

        if (viewModel.getLastFamilyChecked() != NO_TYPE) {
            if (viewModel.getLastFamilyChecked() == GRAND_CHILD_VIEW_TYPE) {
                if (data != null && viewModel.getLastSection() < data.size() && data.get(viewModel.getLastSection()) != null && data.get(viewModel.getLastSection()).getChildren() != null
                        && viewModel.getLastChildParentPosition() < data.get(viewModel.getLastSection()).getChildren().size() && data.get(viewModel.getLastSection()).getChildren().get(viewModel.getLastChildParentPosition()) != null
                        && data.get(viewModel.getLastSection()).getChildren().get(viewModel.getLastChildParentPosition()).getGrandChildren() != null
                        && viewModel.getLastPosition() < data.get(viewModel.getLastSection()).getChildren().get(viewModel.getLastChildParentPosition()).getGrandChildren().size()) {
                    data.get(viewModel.getLastSection()).getChildren().get(viewModel.getLastChildParentPosition()).getGrandChildren().get(viewModel.getLastPosition()).setSelected(selected);

                }

            } else if (viewModel.getLastFamilyChecked() == CHILD_VIEW_TYPE) {
                if (data != null && viewModel.getLastSection() < data.size() && data.get(viewModel.getLastSection()) != null
                        && data.get(viewModel.getLastSection()).getChildren().size() > viewModel.getLastPosition()) {
                    data.get(viewModel.getLastSection()).getChildren().get(viewModel.getLastPosition()).setSelected(selected);
                }

            } else if (viewModel.getLastFamilyChecked() == PARENT_VIEW_TYPE) {
                if (data != null && viewModel.getLastSection() < data.size())
                    data.get(viewModel.getLastSection()).setSelected(selected);
            }
        }
    }

    private void checkLastGChildSelectName(boolean selected, SearchFilterProjectTypeAdapter.GrandChild gcname, GrandChildTypeViewHolder vholder, int section, int grandChildParentIndex, int grandChildIndex) {

        if (viewModel.getLastFamilyChecked() != NO_TYPE) {
            if (viewModel.getLastFamilyChecked() == GRAND_CHILD_VIEW_TYPE) {
                if (data != null && gcname != null && viewModel.getLastName() !=null) {
                    if (viewModel.getLastName().equals(gcname.getName().trim())) {
                        checkLastSelect(false);
                        gcname.setSelected(selected);
                        vholder.checkView.setSelected(selected);
                        viewModel.setLastPosition(Integer.valueOf(grandChildIndex));
                        viewModel.setLastChildParentPosition(Integer.valueOf(grandChildParentIndex));
                        viewModel.setLastSection(section);
                    } else if (gcname.getSelected() == true) gcname.setSelected(false);
                }
            }
        }
    }

    private void checkLastChildSelectName(boolean selected, SearchFilterProjectTypeAdapter.Child cname, ChildViewHolder vholder, int section, int truePosition) {

        if (viewModel.getLastFamilyChecked() != NO_TYPE) {
            if (viewModel.getLastFamilyChecked() == CHILD_VIEW_TYPE) {
                if (data != null && cname != null && viewModel.getLastName() !=null) {
                    if (viewModel.getLastName().equals(cname.getName().trim())) {
                        checkLastSelect(false);
                        cname.setSelected(selected);
                        vholder.checkView.setSelected(selected);
                        viewModel.setLastPosition(Integer.valueOf(truePosition));
                        viewModel.setLastSection(section);
                    } else if (cname.getSelected() == true) cname.setSelected(false);
                }
            }
        }
    }

    private void checkLastParentSelectName(boolean selected, SearchFilterProjectTypeAdapter.Parent pname, ParentViewHolder vholder, int section) {

        if (viewModel.getLastFamilyChecked() != NO_TYPE) {
            if (viewModel.getLastFamilyChecked() == PARENT_VIEW_TYPE) {
                if (data != null && pname != null && viewModel.getLastName() !=null) {
                    if (viewModel.getLastName().equals(pname.getName().trim())) {
                        checkLastSelect(false);
                        pname.setSelected(selected);
                        checkLastSelect(true);
                        vholder.checkView.setSelected(selected);
                        viewModel.setLastSection(section);
                    } else if (pname.getSelected() == true) pname.setSelected(false);
                }

            }
        }
    }

    @Override
    public int getSectionCount() {

        if (data == null) return 0;

        return data.size();
    }

    @Override
    public int getItemCountForSection(int section) {
        // Expanded scenario, where subtype and subsub are expanded
        SearchFilterProjectTypeAdapter.Parent parent = data.get(section);
        List<SearchFilterProjectTypeAdapter.Child> children = parent.getChildren();
        if (viewModel.getCustomSearch())  {
            if (!expandedParents.contains(section)) {
                expandedParents.add(section);   ///*** expanded
                parent.isExpanded = true;
            }
        }
        if (children == null) return 0;
        if (viewModel.getCustomSearch()) {
            TreeMap<Integer, Integer> expanded = new TreeMap<>();
            for (SearchFilterProjectTypeAdapter.Child child: children) {
                if (children.get(0).getGrandChildren() !=null) {
                    children.get(0).isExpanded=true;
                    expanded.put(0, children.get(0).getGrandChildren().size());
                    expandedChildren.put(section, expanded);
                }
            }
        }
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

                return childrenSize + grandChildrenSize;
            } else { //for single-select PNM
                /**
                 * When the parent is no longer expanded, the children of this parent should be also in the mode of not be expanded.
                 */
                int k = 0;
                if (children != null)
                    for (SearchFilterProjectTypeAdapter.Child child : children) {
                        children.get(k).isExpanded = false;
                        k++;
                    }
            }  //end of for single-select PNM

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

        final SearchFilterProjectTypeAdapter.Parent parent = data.get(section);
        boolean isChild = isChild(section, position);

        if (holder instanceof ChildViewHolder && isChild) {

            final Integer truePosition = childPositionInIndex(section, position);
            final SearchFilterProjectTypeAdapter.Child child = parent.getChildren().get(truePosition);
            final ChildViewHolder childViewHolder = (ChildViewHolder) holder;

            childViewHolder.imgView.setVisibility(View.GONE);
            childViewHolder.checkView.setChecked(false);
            if (child.getGrandChildren() != null)
                childViewHolder.imgView.setVisibility(View.VISIBLE);
            childViewHolder.checkView.setText(child.getName());

            checkLastChildSelectName(true, child, childViewHolder, section, truePosition);

            //Note: generates an error on the server if the child has grandchildren is checkable. Only the bottom (grandchild) should be checkable)...
            if ( child.getGrandChildren() !=null) {
                childViewHolder.checkView.setButtonDrawable(null);
            } else {
                childViewHolder.checkView.setChecked(child.getSelected());
                childViewHolder.checkView.setTag(Integer.valueOf(truePosition));
            }

        if (!parent.isExpanded) childViewHolder.imgView.setImageResource(R.mipmap.ic_chevron_down_black);

            TreeMap<Integer, Integer> expanded = expandedChildren.get(section);

            if (child.isExpanded && expanded !=null  )
                childViewHolder.imgView.setImageResource(R.mipmap.ic_chevron_up_black);
            else
                childViewHolder.imgView.setImageResource(R.mipmap.ic_chevron_down_black);

            childViewHolder.checkView.setOnClickListener(null);
            childViewHolder.checkView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // customSearch = false;
                            viewModel.setCustomSearch(false);
                            cb = (CheckBox) view;
                            child.setSelected(cb.isChecked());
                            if (cb.isChecked()) {
                                if (viewModel.getLastName() != null) {
                                    checkLastSelect(false);
                                }

                                viewModel.setLastChecked(childViewHolder.checkView);
                                viewModel.setLastFamilyChecked(CHILD_VIEW_TYPE);
                                viewModel.setLastPosition(Integer.valueOf(truePosition));
                                viewModel.setLastSection(section);
                                viewModel.setLastName(cb.getText().toString().trim());
                                viewModel.clearBundle();
                                selectedChild.putInt(child.getName(), truePosition);
                                viewModel.addProjectTypeData(child.getId(), childViewHolder.checkView.getText().toString());
                            } else {
                                clearLast();
                                viewModel.clearBundle();
                                child.setSelected(false);
                                selectedChild.remove(child.getName());
                                viewModel.removeProjectTypeData(child.getId());
                            }
                            notifyDataSetChanged();
                        }

                    }
            );

            childViewHolder.imgView.setOnClickListener(null);
            childViewHolder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //customSearch=false;
                    viewModel.setCustomSearch(false);
                    TreeMap<Integer, Integer> expanded = expandedChildren.get(section);
                    if (expanded == null) {
                        expanded = new TreeMap<>();
                        expandedChildren.put(section, expanded);
                    }

                    // If the position is already in the expanded list, then we need to remove it
                    if (expanded.containsKey(position)) {
                        child.isExpanded = false;
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
                        child.isExpanded = true;
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

            final GrandChildTypeViewHolder grandChildViewHolder = (GrandChildTypeViewHolder) holder;
            final Integer grandChildParentAdapterIndex = grandChildParentIndex(section, position);
            final Integer grandChildParentIndex = childPositionInIndex(section, grandChildParentAdapterIndex);
            final Integer grandChildIndex = grandChildIndexInParent(grandChildParentAdapterIndex, position);
            final SearchFilterProjectTypeAdapter.GrandChild grandChild = data.get(section).getChildren().get(grandChildParentIndex).getGrandChildren().get(grandChildIndex);

            grandChildViewHolder.checkView.setText(grandChild.getName());
            checkLastGChildSelectName(true, grandChild, grandChildViewHolder, section, grandChildParentIndex, grandChildIndex);
            grandChildViewHolder.checkView.setChecked(grandChild.getSelected());

            grandChildViewHolder.checkView.setOnClickListener(null);
            grandChildViewHolder.checkView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // customSearch = false;
                            viewModel.setCustomSearch(false);
                            CheckBox cb = (CheckBox) view;
                            grandChild.setSelected(cb.isChecked());
                            if (cb.isChecked()) {
                                if (viewModel.getLastName() != null) {
                                    checkLastSelect(false);
                                }

                                viewModel.setLastChecked(grandChildViewHolder.checkView);
                                viewModel.setLastFamilyChecked(GRAND_CHILD_VIEW_TYPE);
                                viewModel.setLastPosition(Integer.valueOf(grandChildIndex));
                                viewModel.setLastChildParentPosition(Integer.valueOf(grandChildParentIndex));
                                viewModel.setLastSection(section);
                                viewModel.setLastName(cb.getText().toString().trim());
                                viewModel.clearBundle();
                                viewModel.addProjectTypeData(grandChild.getId(), grandChildViewHolder.checkView.getText().toString());
                            } else {
                                clearLast();
                                viewModel.clearBundle();
                                grandChild.setSelected(false);
                            }

                            grandChild.setSelected(cb.isChecked());

                            notifyDataSetChanged();
                        }

                    }

            );
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, final int section) {
        final ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
        // Parent denoted by section number
        final SearchFilterProjectTypeAdapter.Parent parent = data.get(section);
        if (parent == null) return;
        parentViewHolder.checkView.setChecked(false);
        parentViewHolder.checkView.setText(parent.getName());
        checkLastParentSelectName(true, parent, parentViewHolder, section);
        //Note: generates an error in the server if the parent is checkable. Only the bottom (child) should be checkable)...
        //parentViewHolder.checkView.setChecked(parent.getSelected());
        parentViewHolder.checkView.setButtonDrawable(null);

        if (parent.getChildren() == null) {
            parentViewHolder.imgView.setImageResource(NO_IMAGE);
        } else {
            if (parent.isExpanded)
                parentViewHolder.imgView.setImageResource(R.mipmap.ic_chevron_up_black);
            else
                parentViewHolder.imgView.setImageResource(R.mipmap.ic_chevron_down_black);
        }
        //checkLastParentSelectName(true, parent, parentViewHolder, section);
        parentViewHolder.imgView.setOnClickListener(null);
        parentViewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //customSearch = false;
                viewModel.setCustomSearch(false);
                if (parent.getChildren() != null) {
                    if (expandedParents.contains(section)) {
                        parent.isExpanded = false;
                        expandedParents.remove(Integer.valueOf(section));
                        expandedChildren.remove(Integer.valueOf(section));
                    } else {
                        parent.isExpanded = true;
                        expandedParents.add(section);
                    }
                }
                notifyDataSetChanged();
            }
        });

        parentViewHolder.checkView.setOnClickListener(null);
        parentViewHolder.checkView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //customSearch = false;
                        viewModel.setCustomSearch(false);
                        CheckBox cb = (CheckBox) view;
                        parent.setSelected(cb.isChecked());

                        if (cb.isChecked()) {
                            if (viewModel.getLastName() != null) {
                               // viewModel.getLastChecked().setChecked(false);
                                checkLastSelect(false);
                            }

                            viewModel.setLastChecked(parentViewHolder.checkView);
                            viewModel.setLastFamilyChecked(PARENT_VIEW_TYPE);
                            viewModel.setLastSection(section);
                            viewModel.setLastName(cb.getText().toString().trim());
                            viewModel.clearBundle();
                            selectedParent.putInt(parent.getName(),section);
                            viewModel.addProjectTypeData(parent.getId(), parentViewHolder.checkView.getText().toString());
                        } else {
                            clearLast();
                            viewModel.clearBundle();
                            parent.setSelected(false);
                            selectedParent.remove(parent.getName());
                           // viewModel.removeProjectTypeData(parent.getId());
                        }

                        notifyDataSetChanged();
                    } //end of onClick();
                }
        );


    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == CHILD_VIEW_TYPE) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_sectioned_adapter_child, parent, false);

            ChildViewHolder vh = new ChildViewHolder(v);
            return vh;

        } else if (viewType == GRAND_CHILD_VIEW_TYPE) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_sectioned_adapter_grandchild, parent, false);

            GrandChildTypeViewHolder vh = new GrandChildTypeViewHolder(v);
            return vh;

        } else {

            // create a parent layout
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_sectioned_adapter_parent, parent, false);

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

    //***
    public Integer grandChildParentIndex(Integer section, Integer adapterPosition) {

        if (expandedChildren.get(section).floorKey(adapterPosition) == null)
            throw new IllegalArgumentException(String.format("There is no expanded child at Section: %1d, Position: %2d", section, adapterPosition));

        return expandedChildren.get(section).floorKey(adapterPosition);
    }

    public Integer grandChildIndexInParent(Integer parentPosition, Integer adapterPosition) {
        // If parent is 5, and adapter position is 7. Then we know the grand child is in index position
        // 1.
        return adapterPosition - parentPosition - 1;
    }


    //***
    public class ParentViewHolder extends RecyclerView.ViewHolder {
        //        public TextView textView;
        public CheckBox checkView;
        public ImageView imgView;

        public ParentViewHolder(View itemView) {
            super(itemView);
            checkView = (CheckBox) itemView.findViewById(R.id.j_parent);
            imgView = (ImageView) itemView.findViewById(R.id.j_parent_img);
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkView;
        public ImageView imgView;

        public ChildViewHolder(View itemView) {
            super(itemView);
            checkView = (CheckBox) itemView.findViewById(R.id.j_child);
            imgView = (ImageView) itemView.findViewById(R.id.j_child_img);
        }
    }

    public class GrandChildTypeViewHolder extends RecyclerView.ViewHolder {
        //        public TextView textView;
        public CheckBox checkView;

        public GrandChildTypeViewHolder(View itemView) {
            super(itemView);
            checkView = (CheckBox) itemView.findViewById(R.id.j_grandchild);
        }
    }
}
