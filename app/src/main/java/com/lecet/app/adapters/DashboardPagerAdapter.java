package com.lecet.app.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.content.DashboardBidsHappeningSoonFragment;
import com.lecet.app.content.DashboardBidsRecentlyMadeFragment;
import com.lecet.app.content.DashboardProjectsRecentlyAddedFragment;
import com.lecet.app.content.DashboardProjectsRecentlyUpdated;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "DashboardPagerAdapter";

    private Context context;
    private static final int NUM_ITEMS = 4;

    public DashboardPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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

        String fragmentTitle;
        String fragmentSubtitle;

        switch (position) {

            // Bids Recently Made
            case 0:
                fragmentTitle    = context.getResources().getString(R.string.dashboard_bids);
                fragmentSubtitle = context.getResources().getString(R.string.dashboard_recently_made);
                return DashboardBidsRecentlyMadeFragment.newInstance(0, fragmentTitle, fragmentSubtitle);

            // Bids Happening Soon
            case 1:
                fragmentTitle    = context.getResources().getString(R.string.dashboard_bids);
                fragmentSubtitle = context.getResources().getString(R.string.dashboard_happening_soon);
                return DashboardBidsHappeningSoonFragment.newInstance(1, fragmentTitle, fragmentSubtitle);

            // Projects Recently Added
            case 2:
                fragmentTitle    = context.getResources().getString(R.string.dashboard_projects);
                fragmentSubtitle = context.getResources().getString(R.string.dashboard_recently_added);
                return DashboardProjectsRecentlyAddedFragment.newInstance(2, fragmentTitle, fragmentSubtitle);

            // Projects Recently Updated
            case 3:
                fragmentTitle    = context.getResources().getString(R.string.dashboard_projects);
                fragmentSubtitle = context.getResources().getString(R.string.dashboard_recently_updated);
                return DashboardProjectsRecentlyUpdated.newInstance(3, fragmentTitle, fragmentSubtitle);

            default:
                Log.w(TAG, "getItem: no such item");
                return null;
        }
    }

    // Returns the page title
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
