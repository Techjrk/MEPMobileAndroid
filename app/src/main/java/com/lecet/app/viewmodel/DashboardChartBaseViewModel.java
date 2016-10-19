package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jason M on 5/10/2016.
 */

public class DashboardChartBaseViewModel extends BaseObservable implements OnChartValueSelectedListener, View.OnClickListener {

    private final String TAG = "DashboardChartBaseVM";

    private final float CHART_VALUE_HOUSING = 0.0f;
    private final float CHART_VALUE_ENGINEERING = 1.0f;
    private final float CHART_VALUE_BUILDING = 2.0f;
    private final float CHART_VALUE_UTILITIES = 3.0f;

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

    public class CustomValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public CustomValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // no decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            Log.d("MainActivity", "getFormattedValue: " + value);
            //
            return mFormat.format(value) + ""; // suffixes
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d(TAG, "onValueSelected: " + h);

        // update chart values display
        PieData data = pieChartView.getData();
        data.setDrawValues(false);
        IPieDataSet dataSet = data.getDataSetByIndex(h.getDataSetIndex());

        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14.0f);
        dataSet.setValueFormatter(new CustomValueFormatter());
        pieChartView.invalidate(); // refresh

        // highlighting of values
        float highlightX = h.getX();
        View fragmentView = fragment.getView();
        View buttonToSelect = null;

        if (highlightX == CHART_VALUE_HOUSING) {
            buttonToSelect = fragmentView.findViewById(R.id.button_housing);
        }
        else if (highlightX == CHART_VALUE_ENGINEERING) {
            buttonToSelect = fragmentView.findViewById(R.id.button_engineering);
        }
        else if (highlightX == CHART_VALUE_BUILDING) {
            buttonToSelect = fragmentView.findViewById(R.id.button_building);
        }
        else if (highlightX == CHART_VALUE_UTILITIES) {
            buttonToSelect = fragmentView.findViewById(R.id.button_utilities);
        }

        setSelected(buttonToSelect);
    }

    @Override
    public void onNothingSelected() {
        //Log.d(TAG, "onNothingSelected ");

        // hide chart values
        PieData data = pieChartView.getData();
        IPieDataSet dataSet = data.getDataSet();
        data.setDrawValues(false);
        pieChartView.invalidate();

    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");

    }

    public void onHousingButtonClick(View view) {
        //Log.d(TAG, "onHousingButtonClick");
        pieChartView.highlightValue(CHART_VALUE_HOUSING, 0);
        setSelected(view);
    }

    public void onEngineeringButtonClick(View view) {
        //Log.d(TAG, "onEngineeringButtonClick");
        pieChartView.highlightValue(CHART_VALUE_ENGINEERING, 0);
        setSelected(view);
    }

    public void onBuildingButtonClick(View view) {
        //Log.d(TAG, "onBuildingButtonClick");
        pieChartView.highlightValue(CHART_VALUE_BUILDING, 0);
        setSelected(view);
    }

    public void onUtilitiesButtonClick(View view) {
        //Log.d(TAG, "onUtilitiesButtonClick");
        pieChartView.highlightValue(CHART_VALUE_UTILITIES, 0);
        setSelected(view);
    }

    private void setSelected(View view) {
        if (view != null) {
            LinearLayout housingButton     = (LinearLayout) fragment.getView().findViewById(R.id.button_housing);
            LinearLayout engineeringButton = (LinearLayout) fragment.getView().findViewById(R.id.button_engineering);
            LinearLayout buildingButton    = (LinearLayout) fragment.getView().findViewById(R.id.button_building);
            LinearLayout utilitiesButton   = (LinearLayout) fragment.getView().findViewById(R.id.button_utilities);

            housingButton.setSelected(false);
            engineeringButton.setSelected(false);
            buildingButton.setSelected(false);
            utilitiesButton.setSelected(false);

            LinearLayout selectedButton = (LinearLayout) view;
            selectedButton.setSelected(true);
        }
    }
}
