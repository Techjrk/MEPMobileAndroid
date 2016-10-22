package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.interfaces.DashboardChartFetchData;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason M on 5/10/2016.
 */

public class BaseDashboardChartViewModel extends BaseObservable implements DashboardChartFetchData, OnChartValueSelectedListener, View.OnClickListener {

    private final String TAG = "DashboardChartBaseVM";

    protected static final Long RESULT_CODE_HOUSING = 103L;
    protected static final Long RESULT_CODE_ENGINEERING = 101L;
    protected static final Long RESULT_CODE_BUILDING = 102L;
    protected static final Long RESULT_CODE_UTILITIES = 105L;

    protected int housingSetSize = 0;
    protected int engineeringSetSize = 0;
    protected int buildingSetSize = 0;
    protected int utilitiesSetSize = 0;

    protected float housingChartX;
    protected float engineeringChartX;
    protected float buildingChartX;
    protected float utilitiesChartX;

    protected Fragment fragment;
    protected String subtitleNum = "";
    protected String subtitle = "";
    protected PieChart pieChartView;

    protected View housingIcon = null;
    protected View engineeringIcon = null;
    protected View buildingIcon = null;
    protected View utilitiesIcon = null;

    protected LinearLayout housingButton = null;
    protected LinearLayout engineeringButton = null;
    protected LinearLayout buildingButton = null;
    protected LinearLayout utilitiesButton = null;

    private MBRDataSource dataSource;
    private MBRDelegate delegate;

    public BaseDashboardChartViewModel(Fragment fragment) {

        this.fragment = fragment;
    }

    public void initialize(View view, MBRDataSource dataSource, MBRDelegate delegate) {
        this.dataSource = dataSource;
        this.delegate = delegate;
        setReferences();
    }

    protected void setReferences() {
        if(fragment != null && fragment.getView() != null) {
            View fragmentView = fragment.getView();
            housingIcon       = fragmentView.findViewById(R.id.dashboard_icon_housing);
            engineeringIcon   = fragmentView.findViewById(R.id.dashboard_icon_engineering);
            buildingIcon      = fragmentView.findViewById(R.id.dashboard_icon_building);
            utilitiesIcon     = fragmentView.findViewById(R.id.dashboard_icon_utilities);

            housingButton     = (LinearLayout) fragmentView.findViewById(R.id.button_housing);
            engineeringButton = (LinearLayout) fragmentView.findViewById(R.id.button_engineering);
            buildingButton    = (LinearLayout) fragmentView.findViewById(R.id.button_building);
            utilitiesButton   = (LinearLayout) fragmentView.findViewById(R.id.button_utilities);
        }
    }

    public void initializeChart(final PieChart pieChartView) {
        Log.d(TAG, "initializeChart");

        this.pieChartView = pieChartView;
        this.pieChartView.setOnClickListener(this);
        this.pieChartView.setOnChartValueSelectedListener(this);

        pieChartView.setTransparentCircleRadius(0);
        pieChartView.setTransparentCircleAlpha(0);
        pieChartView.setHoleRadius(72.0f);
        pieChartView.setHoleColor(ContextCompat.getColor(this.fragment.getContext(), R.color.transparent)); //TODO - update?
        pieChartView.setDrawMarkerViews(false);
        pieChartView.setDrawEntryLabels(false);
        pieChartView.setCenterText("");
        pieChartView.setDescription("");
        pieChartView.setRotationEnabled(false);
        pieChartView.setHighlightPerTapEnabled(true);

        Legend legend = pieChartView.getLegend();
        legend.setEnabled(false);
    }

    public void fetchData(final PieChart pieChartView) {
        // for override
    }

    protected void resetDataSetSizes() {
        housingSetSize = 0;
        engineeringSetSize = 0;
        buildingSetSize = 0;
        utilitiesSetSize = 0;
    }

    protected void handleIncomingData() {
        Log.d(TAG, "handleIncomingData");

        List<PieEntry> entries = new ArrayList<>();

        // total size of data sets, for text display
        int totalSize = housingSetSize + engineeringSetSize + buildingSetSize + utilitiesSetSize;

        List<Integer> colorsList = new ArrayList<Integer>();

        setReferences();

        PieEntry entry;
        float xValue = -1;

        // for any result category that contains data, add a pie chart Entry and add the corresponding color for the chart segment
        if(housingSetSize > 0) {
            entry = new PieEntry(housingSetSize, Long.toString(RESULT_CODE_HOUSING));           // housing - 103 - light orange
            entries.add(entry);
            housingChartX = ++xValue;
            colorsList.add(R.color.lecetLightOrange);
            housingButton.setVisibility(View.VISIBLE);
        }
        else housingButton.setVisibility(View.GONE);

        if(engineeringSetSize > 0) {
            entry = new PieEntry(engineeringSetSize, Long.toString(RESULT_CODE_ENGINEERING));   // engineering - 101 - dark orange
            entries.add(entry);
            engineeringChartX = ++xValue;
            colorsList.add(R.color.lecetDarkOrange);
            engineeringButton.setVisibility(View.VISIBLE);
        }
        else engineeringButton.setVisibility(View.GONE);

        if(buildingSetSize > 0) {
            entry = new PieEntry(buildingSetSize, Long.toString(RESULT_CODE_BUILDING));         // building - 102 - light blue
            entries.add(entry);
            buildingChartX = ++xValue;
            colorsList.add(R.color.lecetLightBlue);
            buildingButton.setVisibility(View.VISIBLE);
        }
        else buildingButton.setVisibility(View.GONE);

        if(utilitiesSetSize > 0) {
            entry = new PieEntry(utilitiesSetSize, Long.toString(RESULT_CODE_UTILITIES));       // utilities - 105 - medium blue
            entries.add(entry);
            utilitiesChartX = ++xValue;
            colorsList.add(R.color.lecetMediumBlue);
            utilitiesButton.setVisibility(View.VISIBLE);
        }
        else utilitiesButton.setVisibility(View.GONE);

        int[] colorsArr = new int[colorsList.size()];
        for(int i=0; i<colorsList.size(); i++) {
            colorsArr[i] = colorsList.get(i);
        }

        // populate pie chart data and set its params
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(0.5f);
        dataSet.setHighlightEnabled(true);
        dataSet.setSelectionShift(24.0f);
        dataSet.setColors(colorsArr, fragment.getContext());

        PieData data = new PieData(dataSet);

        pieChartView.setData(data);
        pieChartView.notifyDataSetChanged();
        pieChartView.invalidate();              // refresh chart display

        setSubtitleNum(Integer.toString(totalSize));
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
            //Log.d(TAG, "getFormattedValue: " + value);
            return mFormat.format(value) + ""; // suffixes
        }
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
        PieEntry pieEntry = (PieEntry) entry;
        String label = pieEntry.getLabel();

        Log.d(TAG, "onValueSelected: " + label + ", " + highlight);

        // update chart values display
        PieData data = pieChartView.getData();
        data.setDrawValues(false);
        IPieDataSet dataSet = data.getDataSetByIndex(highlight.getDataSetIndex());

        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14.0f);
        dataSet.setValueFormatter(new CustomValueFormatter());
        pieChartView.invalidate(); // refresh

        // highlighting of values (references seem to be null here, so reset them)
        setReferences();

        View buttonToSelect = null;
        View iconToShow = null;

        if (label.equals(Long.toString(RESULT_CODE_HOUSING))) {
            buttonToSelect = housingButton;
            iconToShow = housingIcon;
            notifyDelegateOfSelection(RESULT_CODE_HOUSING);
        }
        else if (label.equals(Long.toString(RESULT_CODE_ENGINEERING))) {
            buttonToSelect = engineeringButton;
            iconToShow = engineeringIcon;
            notifyDelegateOfSelection(RESULT_CODE_ENGINEERING);
        }
        else if (label.equals(Long.toString(RESULT_CODE_BUILDING))) {
            buttonToSelect = buildingButton;
            iconToShow = buildingIcon;
            notifyDelegateOfSelection(RESULT_CODE_BUILDING);
        }
        else if (label.equals(Long.toString(RESULT_CODE_UTILITIES))) {
            buttonToSelect = utilitiesButton;
            iconToShow = utilitiesIcon;
            notifyDelegateOfSelection(RESULT_CODE_UTILITIES);
        }
        else Log.w(TAG, "onValueSelected: ERROR");

        // show the corresponding category button
        setCategoryButtonState(buttonToSelect);

        // show the corresponding center icon
        hideCategoryIcons();
        iconToShow.setVisibility(View.VISIBLE);
    }

    public void notifyDelegateOfSelection(Long category) {
        // for override
    }

    @Override
    public void onNothingSelected() {
        //Log.d(TAG, "onNothingSelected ");

        // hide chart values
        try {
            PieData data = pieChartView.getData();
            //IPieDataSet dataSet = data.getDataSet();
            data.setDrawValues(false);
            pieChartView.invalidate();

            resetButtonStates();
            hideCategoryIcons();
        }
        catch (NullPointerException e) {
            Log.w(TAG, "onNothingSelected: chart data may be null");
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: " + view);
    }

    public void onHousingButtonClick(View view) {
        //Log.d(TAG, "onHousingButtonClick");
        pieChartView.highlightValue(housingChartX, 0);
        setCategoryButtonState(view);
    }

    public void onEngineeringButtonClick(View view) {
        //Log.d(TAG, "onEngineeringButtonClick");
        pieChartView.highlightValue(engineeringChartX, 0);
        setCategoryButtonState(view);
    }

    public void onBuildingButtonClick(View view) {
        //Log.d(TAG, "onBuildingButtonClick");
        pieChartView.highlightValue(buildingChartX, 0);
        setCategoryButtonState(view);
    }

    public void onUtilitiesButtonClick(View view) {
        //Log.d(TAG, "onUtilitiesButtonClick");
        pieChartView.highlightValue(utilitiesChartX, 0);
        setCategoryButtonState(view);
    }

    /**
     * Set the pressed state of the selected category button in the button strip
     * @param view
     */
    private void setCategoryButtonState(View view) {
        if (view != null) {
            resetButtonStates();
            LinearLayout selectedButton = (LinearLayout) view;
            selectedButton.setSelected(true);
        }
    }

    private void resetButtonStates() {
        housingButton.setSelected(false);
        engineeringButton.setSelected(false);
        buildingButton.setSelected(false);
        utilitiesButton.setSelected(false);
    }

    public void hideAllButtons() {
        housingButton.setVisibility(View.INVISIBLE);
        engineeringButton.setVisibility(View.INVISIBLE);
        buildingButton.setVisibility(View.INVISIBLE);
        utilitiesButton.setVisibility(View.INVISIBLE);
    }

    private void hideCategoryIcons() {
        housingIcon.setVisibility(View.INVISIBLE);
        engineeringIcon.setVisibility(View.INVISIBLE);
        buildingIcon.setVisibility(View.INVISIBLE);
        utilitiesIcon.setVisibility(View.INVISIBLE);
    }


    /////////////////////////////////
    // GETTERS / SETTERS

    @Bindable
    public String getSubtitleNum() {
        return subtitleNum;
    }

    public void setSubtitleNum(String subtitleNum) {
        this.subtitleNum = subtitleNum;
        notifyPropertyChanged(BR.subtitleNum);     //TODO - this needs to use subtitle_num or bidsRecentlyMade as generated
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

}
