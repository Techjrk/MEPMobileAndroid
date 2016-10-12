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

public class DashboardBidsRecentlyMadeFragment extends DashboardChartFragmentBase {

    private static final String TAG = "BidsRecentlyMadeFrag";

    private String title;
    private int page;

    public static DashboardBidsRecentlyMadeFragment newInstance(int page, String title) {
        Log.d(TAG, "newInstance");
        DashboardBidsRecentlyMadeFragment fragmentInstance = new DashboardBidsRecentlyMadeFragment();
        Bundle args = new Bundle();
        //args.putInt("someInt", page);
        //args.putString("someTitle", title);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        //page = getArguments().getInt("someInt", 0);
        //title = getArguments().getString("someTitle");
    }

}