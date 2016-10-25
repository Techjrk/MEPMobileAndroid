package com.lecet.app.contentbase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;


/**
 * Created by jasonm on 10/5/16.
 */
public class BaseDashboardChartFragment extends Fragment {

    private static final String TAG = "DashboardChartFragBase";

    public static final String ARG_SUBTITLE = "subtitle";

    protected String subtitle = "Subtitle";
    protected PieChart pieChart;  //TODO - check

    /*public static BaseDashboardChartFragment newInstance(String subtitle) {
        Log.d(TAG, "newInstance");
        BaseDashboardChartFragment fragmentInstance = new BaseDashboardChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subtitle = getArguments().getString(ARG_SUBTITLE);
        }
        Log.d(TAG, "onCreate: " + subtitle);
    }

}