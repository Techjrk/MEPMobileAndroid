package com.lecet.app.contentbase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lecet.app.R;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jasonm on 10/5/16.
 */
public class DashboardChartFragmentBase extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "DashboardChartFragBase";

    private String title = "DEFAULT";
    private String subtitle;
    private int page;
    PieChart pieChart;

    public static DashboardChartFragmentBase newInstance(int page, String title, String subtitle) {
        Log.d(TAG, "newInstance");
        DashboardChartFragmentBase fragmentInstance = new DashboardChartFragmentBase();
        Bundle args = new Bundle();
        //args.putInt("someInt", page);
        args.putString("fragmentTitle", title);
        args.putString("fragmentSubtitle", subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("fragmentTitle");
        subtitle = getArguments().getString("fragmentSubtitle");
        Log.d(TAG, "onCreate: " + title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_dashboard_chart_base, container, false);

        TextView fragmentTitleText = (TextView) view.findViewById(R.id.title_text);
        fragmentTitleText.setText(this.title);

        TextView fragmentSubtitleText = (TextView) view.findViewById(R.id.subtitle_text);
        fragmentSubtitleText.setText(this.subtitle);

        initPieChart(view);

        // update pie chart data
        int[] newPieChartData = getNewData();
        updatePieChart(newPieChartData);

        return view;
    }

    private void initPieChart(View view) {
        Log.d(TAG, "initPieChart");
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleRadius(70.0f);
        pieChart.setHoleColor(getResources().getColor(R.color.transparent));
        pieChart.setDrawMarkerViews(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText("");
        pieChart.setDescription("");
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setOnChartValueSelectedListener(this);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
    }

    private void updatePieChart(int[] rawData) {
        Log.d(TAG, "updatePieChart");
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(rawData[0]));       // housing
        entries.add(new PieEntry(rawData[1]));       // engineering
        entries.add(new PieEntry(rawData[2]));       // building
        entries.add(new PieEntry(rawData[3]));       // utilities

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawValues(true);
        dataSet.setSliceSpace(1.0f);
        dataSet.setHighlightEnabled(true);
        dataSet.setSelectionShift(20.0f);
        dataSet.setColors(new int[] { R.color.lecetLightOrange, // housing
                                      R.color.lecetDarkOrange,  // engineering
                                      R.color.lecetLightBlue,   // building
                                      R.color.lecetMediumBlue}, // utilities
                                      this.getContext());

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate(); // refresh
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d(TAG, "onValueSelected: " + e);
        //pieChart.highlightValue(e.getX(), 1);
    }

    @Override
    public void onNothingSelected() {
        Log.d(TAG, "onNothingSelected");
    }

    private int[] getNewData() {
        int[] newPieChartData = new int[4];
        newPieChartData[0] = (int) Math.floor(Math.random()*100);
        newPieChartData[1] = (int) Math.floor(Math.random()*20);
        newPieChartData[2] = (int) Math.floor(Math.random()*5);
        newPieChartData[3] = (int) Math.floor(Math.random()*50);
        return newPieChartData;
    }

}