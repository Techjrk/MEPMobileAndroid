package com.lecet.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * File: SectionedAdapter Created: 10/26/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public abstract class SectionedAdapter extends RecyclerView.Adapter {

    private SparseIntArray sectionIndexPositionMap; // Tracks each section to data source over all item position which includes items plus headers and footers.
    private SparseIntArray sectionFooterMap; // Tracks each section footer to data source position
    private TreeMap<Integer, Integer> positionToSectionMap; // Maps the position of each section start.


    public SectionedAdapter() {

        positionToSectionMap = new TreeMap<>();
        sectionIndexPositionMap = new SparseIntArray();
        sectionFooterMap = new SparseIntArray();
    }


    public abstract int getSectionCount();

    public abstract int getItemCountForSection(int section);

    public abstract int getItemViewType(int section, int position);

    public abstract int getHeaderViewType(int section);

    public abstract int getFooterViewType(int section);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position);

    @SuppressWarnings("unused")
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position, List payloads) {
        onBindItemViewHolder(holder, section, position);
    }

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section);

    public abstract void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int section);

    @SuppressWarnings("unused")
    public boolean enableHeaderForSection(int section) {

        return false;
    }

    @SuppressWarnings("unused")
    public boolean enableFooterForSection(int section) {

        return false;
    }

    /**
     * Implementation of Adapter required methods
     **/

    @Override
    public int getItemCount() {

        // Maps each sections item count.
        SparseIntArray sectionCountMap; sectionCountMap = new SparseIntArray();

        // Clear any existing data
        sectionFooterMap.clear();
        positionToSectionMap.clear();
        sectionIndexPositionMap.clear();

        // We will collect the number of sections then call the data source to determine
        // the number of items in section.
        int sections = getSectionCount();
        int count = 0;

        int section;
        for (section = 0; section < sections; section++) {

            // Keep track of total count for each section
            int itemsInSection = getItemCountForSection(section);
            sectionCountMap.put(section, itemsInSection);

            // Determine if need a header or footer for this section
            boolean headerEnabled = enableHeaderForSection(section);
            boolean footerEnabled = enableFooterForSection(section);

            // If the first index is zero, header position is zero.
            if (section == 0) {

                sectionIndexPositionMap.put(section, 0);
                positionToSectionMap.put(section, 0);

            } else {

                int previousSection = section - 1;
                int previousSectionStart = sectionIndexPositionMap.get(section - 1);
                int previousSectionLength = sectionCountMap.get(section - 1);

                // Index of the next section header will be intuitively the length of the previous
                // section plus 1. If there is a footer for the section. Then its plus 2
                int currentSectionStart = previousSectionStart + previousSectionLength + (enableHeaderForSection(previousSection) ? 1 : 0) + (enableFooterForSection(previousSection) ? 1 : 0);
                sectionIndexPositionMap.put(section, currentSectionStart);
                positionToSectionMap.put(currentSectionStart, section);

                if (footerEnabled) {
                    sectionFooterMap.put(currentSectionStart + itemsInSection + (headerEnabled ? 1 : 0), section);
                }
            }

            count = count + itemsInSection + (headerEnabled ? 1 : 0) + (footerEnabled ? 1 : 0);
        }

        return count;
    }


    @Override
    public int getItemViewType(int position) {

        if (positionToSectionMap.containsKey(position)) {

            return getHeaderViewType(positionToSectionMap.get(position));

        } else if (sectionFooterMap.get(position, -1) != -1) {

            return getFooterViewType(sectionFooterMap.get(position));
        }

        Integer section = findSectionByPosition(position);

        return getItemViewType(section, findPositionInSection(section, position));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Integer section = findSectionByPosition(position);

        if (positionToSectionMap.containsKey(position)) {

            onBindHeaderViewHolder(holder, section);

        } else if (sectionFooterMap.get(position, -1) != -1) {

            onBindFooterViewHolder(holder, section);

        } else {

            Integer positionInSection = findPositionInSection(section, position);
            onBindItemViewHolder(holder, section, positionInSection);
        }
    }


    /**
     * Private
     **/
    public Integer findSectionByPosition(int position) {

        Map.Entry<Integer, Integer> lowKeyMatch = positionToSectionMap.floorEntry(position);

        if (lowKeyMatch == null)
            throw new SectionAdapterException(String.format("Given position %d, does not have a corresponding section!", position));

        return lowKeyMatch.getValue();
    }


    private Integer findPositionInSection(Integer section, int position) {

        Integer sectionStartPosition = sectionIndexPositionMap.get(section);
        Integer sectionPosition;

        if (sectionStartPosition == null)
            throw new SectionAdapterException(String.format("Given position %d, does not have a corresponding section!", position));

        sectionPosition = position - sectionStartPosition;

        // Subtract one (1) since the header is the first element in the section.
        return sectionPosition - 1;
    }


    private class SectionAdapterException extends RuntimeException {

        SectionAdapterException(String message) {
            super(message);
        }
    }
}
