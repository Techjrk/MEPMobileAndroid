package com.lecet.app.interfaces;


import com.github.mikephil.charting.charts.PieChart;

/**
 * Created by Josué Rodríguez on 18/10/2016.
 */

public interface DashboardChartFetchData {

    void fetchData(final PieChart pieChartView);
    void notifyDelegateOfSelection(Long category);
}
