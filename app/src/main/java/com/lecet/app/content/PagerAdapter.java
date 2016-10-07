package com.lecet.app.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by jasonm on 10/5/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DashboardBidsRecentlyMadeFragment();
            case 1:
                return new DashboardBidsHappeningSoonFragment();
            case 2:
                return new DashboardProjectsRecentlyAddedFragment();
            case 3:
                return new DashboardProjectsRecentlyUpdated();
            default:
                Log.w("PagerAdapter", "getItem: no case for position " + position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }


}
