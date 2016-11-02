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

    private SparseIntArray sectionIndexPositionMap; // Maps each section to data source aggregate item position which includes items plus headers.
    private SparseIntArray sectionCountMap; // Maps each sections item count.
    private TreeMap<Integer, Integer> positionToSectionMap; // Maps the position of each section start.


    public SectionedAdapter() {

        positionToSectionMap = new TreeMap<>();
        sectionIndexPositionMap = new SparseIntArray();
        sectionCountMap = new SparseIntArray();
    }


    public abstract int getSectionCount();

    public abstract int getItemCountForSection(int section);

    public abstract int getItemViewType(int section, int position);

    public abstract int getHeaderViewType(int section);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position);

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position, List payloads) {
        onBindItemViewHolder(holder, section, position);
    }

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section);

    /**
     * Implementation of Adapter required methods
     **/

    @Override
    public int getItemCount() {

        // Clear any existing data
        sectionIndexPositionMap.clear();
        sectionCountMap.clear();

        // We will collect the number of sections then call the data source to determine
        // the number of items in section.
        int sections = getSectionCount();
        int count = 0;

        int i;
        for (i = 0; i < sections; i++) {

            int itemsInSection = getItemCountForSection(i);
            sectionCountMap.put(i, itemsInSection);

            // If the first index is zero, header position is zero.
            if (i == 0) {

                sectionIndexPositionMap.put(i, i);
                positionToSectionMap.put(i, i);

            } else {

                int previousSectionStart = sectionIndexPositionMap.get(i - 1);
                int previousSectionLength = sectionCountMap.get(i - 1);

                // Index of the next section header will be intuitively the length of the previous
                // section plus 1.
                int currentSectionStart = previousSectionStart + previousSectionLength + 1;
                sectionIndexPositionMap.put(i, currentSectionStart);
                positionToSectionMap.put(currentSectionStart, i);
            }

            count = count + itemsInSection + 1;
        }

        return count;
    }

    @Override
    public int getItemViewType(int position) {

        if (positionToSectionMap.containsKey(position)) {

            return getHeaderViewType(positionToSectionMap.get(position));
        }

        Integer section = findSectionByPosition(position);

        return getItemViewType(section, findPositionInSection(section, position));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Integer section = findSectionByPosition(position);

        if (positionToSectionMap.containsKey(position)) {

            onBindHeaderViewHolder(holder, section);

        } else {

            Integer positionInSection = findPositionInSection(section, position);
            onBindItemViewHolder(holder, section, positionInSection);
        }
    }


    /**
     * Private
     **/

    private Integer findSectionByPosition(int position) {

        Map.Entry<Integer, Integer> lowKeyMatch = positionToSectionMap.floorEntry(position);

        if (lowKeyMatch == null) {

            throw new SectionAdapterException(String.format("Given position %d, does not have a corresponding section!", position));
        }

        return lowKeyMatch.getValue();
    }


    private Integer findPositionInSection(Integer section, int position) {

        Integer sectionStartPosition = sectionIndexPositionMap.get(section);
        Integer sectionPosition;

        if (sectionStartPosition == null) {

            throw new SectionAdapterException(String.format("Given position %d, does not have a corresponding section!", position));
        }

        // Section zero (0) positions are their natural position.
        if (section == 0) {

            sectionPosition = position;

        } else {

            // We can now with certainty that the modulo remainder is the actual position within the aggregate data
            sectionPosition = position % sectionStartPosition;
        }


        // We then take account for the section header in the aggregate item count to get the position within
        // the subsection of data. Ex Position 15 in data set with 2 sections with the first section comprising of
        // seven (7) items plus the header. Section one (1) header will then be at position nine (9). 15 % 9 would then yield
        // six (6) which is the items in the section plus the header. Subtracting the header element from the data would yield
        // five (5) which is where the item position is within section 1.
        return sectionPosition - 1;
    }


    private class SectionAdapterException extends RuntimeException {

        SectionAdapterException(String message) {
            super(message);
        }
    }
}
