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

import java.text.DecimalFormat;

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

    private View housingIcon = null;
    private View engineeringIcon = null;
    private View buildingIcon = null;
    private View utilitiesIcon = null;

    private LinearLayout housingButton = null;
    private LinearLayout engineeringButton = null;
    private LinearLayout buildingButton = null;
    private LinearLayout utilitiesButton = null;

    //private MHSDataSource dataSource;
    //private MHSDelegate delegate;

    public DashboardChartBaseViewModel(Fragment fragment /*, MHSDataSource dataSource, MHSDelegate delegate*/) {

        this.fragment = fragment;
//        this.dataSource = dataSource;
//        this.delegate = delegate;
    }

    public void initialize(View view) {
        View fragmentView = view;

        housingIcon       = fragmentView.findViewById(R.id.dashboard_icon_housing);
        engineeringIcon   = fragmentView.findViewById(R.id.dashboard_icon_engineering);
        buildingIcon      = fragmentView.findViewById(R.id.dashboard_icon_building);
        utilitiesIcon     = fragmentView.findViewById(R.id.dashboard_icon_utilities);

        housingButton     = (LinearLayout) fragmentView.findViewById(R.id.button_housing);
        engineeringButton = (LinearLayout) fragmentView.findViewById(R.id.button_engineering);
        buildingButton    = (LinearLayout) fragmentView.findViewById(R.id.button_building);
        utilitiesButton   = (LinearLayout) fragmentView.findViewById(R.id.button_utilities);
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

    /**
     * Custom ValueFormatter for use in displaying chart values
     */
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
        View buttonToSelect = null;

        if (highlightX == CHART_VALUE_HOUSING) {
            buttonToSelect = housingButton;
        }
        else if (highlightX == CHART_VALUE_ENGINEERING) {
            buttonToSelect = engineeringButton;
        }
        else if (highlightX == CHART_VALUE_BUILDING) {
            buttonToSelect = buildingButton;
        }
        else if (highlightX == CHART_VALUE_UTILITIES) {
            buttonToSelect = utilitiesButton;
        }

        setCategoryButtonState(buttonToSelect);
        setCategoryIcon(h);

        //TODO - notify the activity that a value has been selected so the bottom map fragments can filter
        // expose the group ID that is selected. notify the delegate. see calendar or d's example
    }

    @Override
    public void onNothingSelected() {
        //Log.d(TAG, "onNothingSelected ");

        // hide chart values
        PieData data = pieChartView.getData();
        IPieDataSet dataSet = data.getDataSet();
        data.setDrawValues(false);
        pieChartView.invalidate();

        hideCategoryIcons();
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
    }

    public void onHousingButtonClick(View view) {
        //Log.d(TAG, "onHousingButtonClick");
        pieChartView.highlightValue(CHART_VALUE_HOUSING, 0);
        setCategoryButtonState(view);
    }

    public void onEngineeringButtonClick(View view) {
        //Log.d(TAG, "onEngineeringButtonClick");
        pieChartView.highlightValue(CHART_VALUE_ENGINEERING, 0);
        setCategoryButtonState(view);
    }

    public void onBuildingButtonClick(View view) {
        //Log.d(TAG, "onBuildingButtonClick");
        pieChartView.highlightValue(CHART_VALUE_BUILDING, 0);
        setCategoryButtonState(view);
    }

    public void onUtilitiesButtonClick(View view) {
        //Log.d(TAG, "onUtilitiesButtonClick");
        pieChartView.highlightValue(CHART_VALUE_UTILITIES, 0);
        setCategoryButtonState(view);
    }

    /**
     * Set the pressed state of the selected category button in the button strip
     * @param view
     */
    private void setCategoryButtonState(View view) {
        if (view != null) {
            housingButton.setSelected(false);
            engineeringButton.setSelected(false);
            buildingButton.setSelected(false);
            utilitiesButton.setSelected(false);

            LinearLayout selectedButton = (LinearLayout) view;
            selectedButton.setSelected(true);
        }
    }

    /**
     * Update the category icon in the center of the pie chart
     * @param highlight
     */
    private void setCategoryIcon(Highlight highlight) {

        float highlightX = highlight.getX();
        View iconToShow = null;

        hideCategoryIcons();

        if (highlightX == CHART_VALUE_HOUSING) {
            iconToShow = housingIcon;
        }
        else if (highlightX == CHART_VALUE_ENGINEERING) {
            iconToShow = engineeringIcon;
        }
        else if (highlightX == CHART_VALUE_BUILDING) {
            iconToShow = buildingIcon;
        }
        else if (highlightX == CHART_VALUE_UTILITIES) {
            iconToShow = utilitiesIcon;
        }

        iconToShow.setVisibility(View.VISIBLE);
    }

    private void hideCategoryIcons() {
        housingIcon.setVisibility(View.INVISIBLE);
        engineeringIcon.setVisibility(View.INVISIBLE);
        buildingIcon.setVisibility(View.INVISIBLE);
        utilitiesIcon.setVisibility(View.INVISIBLE);
    }

}
