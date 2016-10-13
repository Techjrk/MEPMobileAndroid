package com.lecet.app.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.contentbase.DashboardChartFragmentBase;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardProjectsRecentlyUpdated extends DashboardChartFragmentBase {

    private static final String TAG = "ProjectsRecentlyUpdated";


    public static DashboardProjectsRecentlyUpdated newInstance(int page, String title, String subtitle) {
        Log.d(TAG, "newInstance");
        DashboardProjectsRecentlyUpdated fragmentInstance = new DashboardProjectsRecentlyUpdated();
        Bundle args = new Bundle();
        args.putInt(DashboardChartFragmentBase.ARG_PAGE, page);
        args.putString(DashboardChartFragmentBase.ARG_TITLE, title);
        args.putString(DashboardChartFragmentBase.ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

}
