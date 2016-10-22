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

    public static final String ARG_PAGE = "page";
    public static final String ARG_TITLE = "title";
    public static final String ARG_SUBTITLE = "subtitle";

    protected int page;
    protected String title = "Title";
    protected String subtitle = "Subtitle";
    protected PieChart pieChart;  //TODO - check

    public static BaseDashboardChartFragment newInstance(int page, String title, String subtitle) {
        Log.d(TAG, "newInstance");
        BaseDashboardChartFragment fragmentInstance = new BaseDashboardChartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_PAGE, 0);
            title = getArguments().getString(ARG_TITLE);
            subtitle = getArguments().getString(ARG_SUBTITLE);
        }
        Log.d(TAG, "onCreate: " + title + " " + subtitle);
    }

}