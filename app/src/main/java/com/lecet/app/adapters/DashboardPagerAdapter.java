package com.lecet.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.lecet.app.content.DashboardBidsHappeningSoonFragment;
import com.lecet.app.content.DashboardBidsRecentlyMadeFragment;
import com.lecet.app.content.DashboardProjectsRecentlyAddedFragment;
import com.lecet.app.content.DashboardProjectsRecentlyUpdated;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "DashboardPagerAdapter";

    private static int NUM_ITEMS = 4;

    public DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        //Log.d(TAG, "getItem: " + position);
        switch (position) {
            case 0:
                return DashboardBidsRecentlyMadeFragment.newInstance(0, "DashboardBidsRecentlyMadeFragment");

            case 1:
                return DashboardBidsHappeningSoonFragment.newInstance(1, "DashboardBidsHappeningSoonFragment");

            case 2:
                return DashboardProjectsRecentlyAddedFragment.newInstance(2, "DashboardProjectsRecentlyAddedFragment");

            case 3:
                return DashboardProjectsRecentlyUpdated.newInstance(3, "DashboardProjectsRecentlyUpdated");

            default:
                return null;
        }
    }

    // Returns the page title
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
