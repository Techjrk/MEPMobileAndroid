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

    private final Fragment fragment;
    private String subtitle = "99";

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
        pieChartView.setOnClickListener(this);
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
        Log.d(TAG, "onClick " + view.getId());
    }
}
