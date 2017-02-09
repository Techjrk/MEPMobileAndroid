package com.lecet.app.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.content.DashboardBidsHappeningSoonFragment;
import com.lecet.app.content.DashboardBidsRecentlyMadeFragment;
import com.lecet.app.content.DashboardProjectsRecentlyAddedFragment;
import com.lecet.app.content.DashboardProjectsRecentlyUpdatedFragment;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "DashboardPagerAdapter";

    private Context context;
    public static final int NUM_ITEMS = 4;

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

        switch (position) {

            // Bids Recently Made
            case 0:
                return DashboardBidsRecentlyMadeFragment.newInstance(context.getResources().getString(R.string.dashboard_recently_made));

            // Bids Happening Soon
            case 1:
                return DashboardBidsHappeningSoonFragment.newInstance(context.getResources().getString(R.string.dashboard_happening_soon));

            // Projects Recently Added
            case 2:
                return DashboardProjectsRecentlyAddedFragment.newInstance(context.getResources().getString(R.string.dashboard_recently_added));

            // Projects Recently Updated
            case 3:
                return DashboardProjectsRecentlyUpdatedFragment.newInstance(context.getResources().getString(R.string.dashboard_recently_updated));

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
