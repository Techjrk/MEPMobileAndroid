package com.lecet.app.content;

import android.os.Bundle;
import android.util.Log;

import com.lecet.app.contentbase.BaseDashboardChartFragment;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardProjectsRecentlyAddedFragment extends BaseDashboardChartFragment {

    private static final String TAG = "ProjectsRecentlyAddFrag";


    public static DashboardProjectsRecentlyAddedFragment newInstance(int page, String title, String subtitle) {
        Log.d(TAG, "newInstance");
        DashboardProjectsRecentlyAddedFragment fragmentInstance = new DashboardProjectsRecentlyAddedFragment();
        Bundle args = new Bundle();
        args.putInt(BaseDashboardChartFragment.ARG_PAGE, page);
        args.putString(BaseDashboardChartFragment.ARG_TITLE, title);
        args.putString(BaseDashboardChartFragment.ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}