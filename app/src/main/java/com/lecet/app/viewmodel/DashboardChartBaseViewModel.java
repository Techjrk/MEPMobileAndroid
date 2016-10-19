package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lecet.app.BR;
import com.lecet.app.data.models.Project;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jason M on 5/10/2016.
 */

public class DashboardChartBaseViewModel extends BaseObservable implements OnChartValueSelectedListener, View.OnClickListener {

    private final String TAG = "DashboardChartBaseVM";

    private final float CHART_VALUE_HOUSING     = 0.0f;
    private final float CHART_VALUE_ENGINEERING = 1.0f;
    private final float CHART_VALUE_BUILDING    = 2.0f;
    private final float CHART_VALUE_UTILITIES   = 3.0f;

    private final Fragment fragment;
    private String subtitle = "99";
    private PieChart pieChartView;

    //private MHSDataSource dataSource;
    //private MHSDelegate delegate;

    public DashboardChartBaseViewModel(Fragment fragment /*, MHSDataSource dataSource, MHSDelegate delegate*/) {

        this.fragment = fragment;
//        this.dataSource = dataSource;
//        this.delegate = delegate;
    }

    @Bindable
    public String getSubtitle() {
        return subtitle;
    }

    @Bindable
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        notifyPropertyChanged(BR.subtitle);
    }

    public void initializeChart(final PieChart pieChartView) {
        Log.d(TAG, "initializeChart");
        this.pieChartView = pieChartView;
        this.pieChartView.setOnClickListener(this);
        this.pieChartView.setOnChartValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d(TAG, "onValueSelected: " + e);
    }

    @Override
    public void onNothingSelected() {
        Log.d(TAG, "onNothingSelected ");
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");

    }

    public void onHousingButtonClick(View view) {
        Log.d(TAG, "onHousingButtonClick");
        pieChartView.highlightValue(CHART_VALUE_HOUSING, 0);
    }

    public void onEngineeringButtonClick(View view) {
        Log.d(TAG, "onEngineeringButtonClick");
        pieChartView.highlightValue(CHART_VALUE_ENGINEERING, 0);
    }

    public void onBuildingButtonClick(View view) {
        Log.d(TAG, "onBuildingButtonClick");
        pieChartView.highlightValue(CHART_VALUE_BUILDING, 0);
    }

    public void onUtilitiesButtonClick(View view) {
        Log.d(TAG, "onUtilitiesButtonClick");
        pieChartView.highlightValue(CHART_VALUE_UTILITIES, 0);
    }
}
