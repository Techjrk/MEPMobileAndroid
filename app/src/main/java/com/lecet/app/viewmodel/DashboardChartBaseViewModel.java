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

    //private final float CHART_VALUE_HOUSING = 0.0f;
    //private final float CHART_VALUE_ENGINEERING = 1.0f;
    //private final float CHART_VALUE_BUILDING = 2.0f;
    //private final float CHART_VALUE_UTILITIES = 3.0f;

    private float housingChartX;
    private float engineeringChartX;
    private float buildingChartX;
    private float utilitiesChartX;

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
        setReferences();
    }

    private void setReferences() {
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

                setReferences();

                PieEntry entry;
                float xValue = -1;

                // for any result category that contains data, add a pie chart Entry and add the corresponding color for the chart segment
                if(housingTreeSetSize > 0) {
                    entry = new PieEntry(housingTreeSetSize, Long.toString(RESULT_CODE_HOUSING));           // housing - 103 - light orange
                    entries.add(entry);
                    housingChartX = ++xValue;
                    colorsList.add(R.color.lecetLightOrange);
                    housingButton.setVisibility(View.VISIBLE);
                }
                else housingButton.setVisibility(View.GONE);

                if(engineeringTreeSetSize > 0) {
                    entry = new PieEntry(engineeringTreeSetSize, Long.toString(RESULT_CODE_ENGINEERING));   // engineering - 101 - dark orange
                    entries.add(entry);
                    engineeringChartX = ++xValue;
                    colorsList.add(R.color.lecetDarkOrange);
                    engineeringButton.setVisibility(View.VISIBLE);
                }
                else engineeringButton.setVisibility(View.GONE);

                if(buildingTreeSetSize > 0) {
                    entry = new PieEntry(buildingTreeSetSize, Long.toString(RESULT_CODE_BUILDING));         // building - 102 - light blue
                    entries.add(entry);
                    buildingChartX = ++xValue;
                    colorsList.add(R.color.lecetLightBlue);
                    buildingButton.setVisibility(View.VISIBLE);
                }
                else buildingButton.setVisibility(View.GONE);

                if(utilitiesTreeSetSize > 0) {
                    entry = new PieEntry(utilitiesTreeSetSize, Long.toString(RESULT_CODE_UTILITIES));       // utilities - 105 - medium blue
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

        float highlightX = highlight.getX();
        View buttonToSelect = null;
        View iconToShow = null;

        if (label.equals(Long.toString(RESULT_CODE_HOUSING))) {
            buttonToSelect = housingButton;
            iconToShow = housingIcon;
        }
        else if (label.equals(Long.toString(RESULT_CODE_ENGINEERING))) {
            buttonToSelect = engineeringButton;
            iconToShow = engineeringIcon;
        }
        else if (label.equals(Long.toString(RESULT_CODE_BUILDING))) {
            buttonToSelect = buildingButton;
            iconToShow = buildingIcon;
        }
        else if (label.equals(Long.toString(RESULT_CODE_UTILITIES))) {
            buttonToSelect = utilitiesButton;
            iconToShow = utilitiesIcon;
        }
        else Log.w(TAG, "onValueSelected: ERROR");

        // show the corresponding category button
        setCategoryButtonState(buttonToSelect);

        // show the corresponding center icon
        hideCategoryIcons();
        iconToShow.setVisibility(View.VISIBLE);

        //TODO - notify the activity that a value has been selected so the bottom map fragments can filter
        // expose the group ID that is selected. notify the delegate. see calendar or d's example
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
        Log.d(TAG, "onClick");
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

    /*private PieEntry getEntryXByResultCode(long resultCode) {
        String resultCodeStr = Long.toString(resultCode);

        PieEntry entry = null;
        float entryX = -1f;
        for(int i=0; i<pieChartView.getData().getDataSet().getEntryCount(); i++) {
            entry = pieChartView.getData().getDataSet().getEntryForIndex(i);
            if(entry.getLabel().equals(resultCodeStr)) {
                //Entry genericEntry = (Entry) entry;
                //entryX = genericEntry.getX();
                break;
            }
        }

        return entry;
    }*/

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

}
