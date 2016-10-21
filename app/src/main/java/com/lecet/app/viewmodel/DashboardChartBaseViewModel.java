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
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Jason M on 5/10/2016.
 */

public class DashboardChartBaseViewModel extends BaseObservable implements OnChartValueSelectedListener, View.OnClickListener {

    private final String TAG = "DashboardChartBaseVM";

    private final Long RESULT_CODE_HOUSING = 103L;
    private final Long RESULT_CODE_ENGINEERING = 101L;
    private final Long RESULT_CODE_BUILDING = 102L;
    private final Long RESULT_CODE_UTILITIES = 105L;

    private final float CHART_VALUE_HOUSING = 0.0f;
    private final float CHART_VALUE_ENGINEERING = 1.0f;
    private final float CHART_VALUE_BUILDING = 2.0f;
    private final float CHART_VALUE_UTILITIES = 3.0f;

    private final Fragment fragment;
    private String bidsRecentlyMade = "";
    private String subtitle = "";
    private PieChart pieChartView;

    private View housingIcon = null;
    private View engineeringIcon = null;
    private View buildingIcon = null;
    private View utilitiesIcon = null;

    private LinearLayout housingButton = null;
    private LinearLayout engineeringButton = null;
    private LinearLayout buildingButton = null;
    private LinearLayout utilitiesButton = null;

    private MBRDataSource dataSource;
    private MBRDelegate delegate;

    public DashboardChartBaseViewModel(Fragment fragment, MBRDataSource dataSource, MBRDelegate delegate) {

        this.fragment = fragment;
        this.dataSource = dataSource;
        this.delegate = delegate;
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

    @Bindable
    public String getBidsRecentlyMade() {
        return bidsRecentlyMade;
    }

    public void setBidsRecentlyMade(String bidsRecentlyMade) {
        this.bidsRecentlyMade = bidsRecentlyMade;
        notifyPropertyChanged(BR.bidsRecentlyMade);
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

    public void fetchBids(final PieChart pieChartView) {

        dataSource.refreshRecentlyMadeBids(new LecetCallback<TreeMap<Long, TreeSet<Bid>>>() {

            @Override
            public void onSuccess(TreeMap<Long, TreeSet<Bid>> result) {
                Log.d(TAG, "onSuccess: " + result);

                TreeSet<Bid> housingTreeSet     = result.get(RESULT_CODE_HOUSING);
                TreeSet<Bid> engineeringTreeSet = result.get(RESULT_CODE_ENGINEERING);
                TreeSet<Bid> buildingTreeSet    = result.get(RESULT_CODE_BUILDING);
                TreeSet<Bid> utilitiesTreeSet   = result.get(RESULT_CODE_UTILITIES);


                List<PieEntry> entries = new ArrayList<>();

                int housingTreeSetSize = 0;
                int engineeringTreeSetSize = 0;
                int buildingTreeSetSize = 0;
                int utilitiesTreeSetSize = 0;

                if(housingTreeSet != null)     housingTreeSetSize     = housingTreeSet.size();
                if(engineeringTreeSet != null) engineeringTreeSetSize = engineeringTreeSet.size();
                if(buildingTreeSet != null)    buildingTreeSetSize    = buildingTreeSet.size();
                if(utilitiesTreeSet != null)   utilitiesTreeSetSize   = utilitiesTreeSet.size();

                Log.d(TAG, "onSuccess: housingTreeSet size: " + housingTreeSetSize);
                Log.d(TAG, "onSuccess: engineeringTreeSet size: " + engineeringTreeSetSize);
                Log.d(TAG, "onSuccess: buildingTreeSet size: " + buildingTreeSetSize);
                Log.d(TAG, "onSuccess: utilitiesTreeSet size: " + utilitiesTreeSetSize);

                // total size of tree sets, for text display
                int totalSize = housingTreeSetSize + engineeringTreeSetSize + buildingTreeSetSize + utilitiesTreeSetSize;

                List<Integer> colorsList = new ArrayList<Integer>();

                // for any result category that contains data, add a pie chart Entry and add the corresponding color for the chart segment
                if(housingTreeSetSize > 0) {
                    entries.add(new PieEntry(housingTreeSetSize));       // housing - 103 - light orange
                    colorsList.add(R.color.lecetLightOrange);
                }
                if(engineeringTreeSetSize > 0) {
                    entries.add(new PieEntry(engineeringTreeSetSize));   // engineering - 101 - dark orange
                    colorsList.add(R.color.lecetDarkOrange);
                }
                if(buildingTreeSetSize > 0) {
                    entries.add(new PieEntry(buildingTreeSetSize));      // building - 102 - light blue
                    colorsList.add(R.color.lecetLightBlue);
                }
                if(utilitiesTreeSetSize > 0) {
                    entries.add(new PieEntry(utilitiesTreeSetSize));     // utilities - 105 - medium blue
                    colorsList.add(R.color.lecetMediumBlue);
                }

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
                pieChartView.invalidate(); // refresh

                setBidsRecentlyMade(Integer.toString(totalSize));
                setSubtitle(fragment.getContext().getResources().getString(R.string.dashboard_recently_made));  //TODO - make dynamic
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e(TAG, "onFailure: " + message);
                // TODO - check behavior of chart on no data - currently displays 'no data' text
            }
        });
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
