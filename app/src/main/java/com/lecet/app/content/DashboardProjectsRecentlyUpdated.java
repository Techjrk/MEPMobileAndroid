package com.lecet.app.content;

import android.os.Bundle;
import android.util.Log;

import com.lecet.app.contentbase.BaseDashboardChartFragment;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardProjectsRecentlyUpdated extends BaseDashboardChartFragment {

    private static final String TAG = "ProjectsRecentlyUpdated";


    public static DashboardProjectsRecentlyUpdated newInstance(int page, String title, String subtitle) {
        Log.d(TAG, "newInstance");
        DashboardProjectsRecentlyUpdated fragmentInstance = new DashboardProjectsRecentlyUpdated();
        Bundle args = new Bundle();
        args.putInt(BaseDashboardChartFragment.ARG_PAGE, page);
        args.putString(BaseDashboardChartFragment.ARG_TITLE, title);
        args.putString(BaseDashboardChartFragment.ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

}
