package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.interfaces.DashboardChartFetchData;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.interfaces.MRADataSource;
import com.lecet.app.interfaces.MRADelegate;
import com.lecet.app.interfaces.MRUDataSource;
import com.lecet.app.interfaces.MRUDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Jason M on 5/10/2016.
 */

public class BaseDashboardChartViewModel extends BaseObservable implements DashboardChartFetchData, OnChartValueSelectedListener, View.OnClickListener {

    private final String TAG = "DashboardChartBaseVM";

    private final float CHART_HOLE_RADIUS_UNSELECTED = 50.0f;
    private final float CHART_HOLE_RADIUS_SELECTED = 72.0f;
    private final float CHART_SLICE_SPACING = 0.5f;
    private final float CHART_SELECTION_SHIFT = 24.0f;

    protected static final Long RESULT_CODE_HOUSING = 103L;
    protected static final Long RESULT_CODE_ENGINEERING = 101L;
    protected static final Long RESULT_CODE_BUILDING = 102L;
    protected static final Long RESULT_CODE_UTILITIES = 105L;
    protected static final Long CONSOLIDATED_CODE_B = 901L;
    protected static final Long CONSOLIDATED_CODE_H = 902L;

    protected int housingSetSize = 0;
    protected int engineeringSetSize = 0;
    protected int buildingSetSize = 0;
    protected int utilitiesSetSize = 0;
    protected int b_setSize = 0;
    protected int h_setSize = 0;

    protected float b_chartX;
    protected float h_chartX;

    protected Fragment fragment;
    protected String subtitleNum = "";
    protected String subtitle = "";
    protected PieChart pieChartView;

    protected View b_icon = null;
    protected View h_icon = null;

    protected LinearLayout b_button = null;
    protected LinearLayout h_button = null;

    @IntDef({DATA_SOURCE_TYPE_MBR, DATA_SOURCE_TYPE_MHS, DATA_SOURCE_TYPE_MRA, DATA_SOURCE_TYPE_MRU})
    public @interface DashboardDataSource {
    }

    private final int dataSourceType;

    public static final int DATA_SOURCE_TYPE_MBR = 1;   // for Bids Recently Made (PieChart)
    public static final int DATA_SOURCE_TYPE_MHS = 2;   // for Bids Happening Soon (Calendar only)
    public static final int DATA_SOURCE_TYPE_MRA = 3;   // for Projects Recently Added (PieChart)
    public static final int DATA_SOURCE_TYPE_MRU = 4;   // for Projects Recently Updated (PieChart)

    private MBRDataSource dataSourceMBR;
    private MBRDelegate delegateMBR;
    private MRADataSource dataSourceMRA;
    private MRADelegate delegateMRA;
    private MRUDataSource dataSourceMRU;
    private MRUDelegate delegateMRU;

    public BaseDashboardChartViewModel(Fragment fragment, @DashboardDataSource int dataSourceType) {
        this.fragment = fragment;
        this.dataSourceType = dataSourceType;
    }

    public void initializeMBR(View view, String subtitle, MBRDataSource dataSource, MBRDelegate delegate) {
        this.dataSourceMBR = dataSource;
        this.delegateMBR = delegate;
        setReferences();
        setSubtitle(subtitle);
    }

    public void initializeMRA(View view, String subtitle, MRADataSource dataSource, MRADelegate delegate) {
        this.dataSourceMRA = dataSource;
        this.delegateMRA = delegate;
        setReferences();
        setSubtitle(subtitle);
    }

    public void initializeMRU(View view, String subtitle, MRUDataSource dataSource, MRUDelegate delegate) {
        this.dataSourceMRU = dataSource;
        this.delegateMRU = delegate;
        setReferences();
        setSubtitle(subtitle);
    }

    protected void setReferences() {
        if(fragment != null && fragment.getView() != null) {
            View fragmentView = fragment.getView();

            b_icon   = fragmentView.findViewById(R.id.dashboard_icon_b);
            h_icon   = fragmentView.findViewById(R.id.dashboard_icon_h);

            b_button = (LinearLayout) fragmentView.findViewById(R.id.button_b);
            h_button = (LinearLayout) fragmentView.findViewById(R.id.button_h);
        }
    }

    public void initializeChart(final PieChart pieChartView) {
        Log.d(TAG, "initializeChart");

        this.pieChartView = pieChartView;
        this.pieChartView.setOnClickListener(this);
        this.pieChartView.setOnChartValueSelectedListener(this);

        pieChartView.setTransparentCircleRadius(0);
        pieChartView.setTransparentCircleAlpha(0);
        pieChartView.setHoleRadius(CHART_HOLE_RADIUS_UNSELECTED);
        pieChartView.setHoleColor(ContextCompat.getColor(this.fragment.getContext(), R.color.transparent));
        pieChartView.setDrawMarkerViews(false);
        pieChartView.setDrawEntryLabels(false);
        pieChartView.setCenterText("");
        pieChartView.setRotationEnabled(false);
        pieChartView.setHighlightPerTapEnabled(true);

        Description description = pieChartView.getDescription();
        description.setEnabled(false);

        Legend legend = pieChartView.getLegend();
        legend.setEnabled(false);
    }

    /**
     * Note: Requirements state to consolidate all project types into Heavy-Highway and Building in the UI.
     * Heavy-Highway will include Engineering (101) and Utilities (105).
     * Building will include Housing (103) and Building (102).
     */
    public void fetchData() {

        if (this.dataSourceType == DATA_SOURCE_TYPE_MBR) {
            dataSourceMBR.refreshRecentlyMadeBids(new LecetCallback<TreeMap<Long, TreeSet<Bid>>>() {

                @Override
                public void onSuccess(TreeMap<Long, TreeSet<Bid>> result) {
                    Log.d(TAG, "MBR: onSuccess");

                    TreeSet<Bid> housingSet = result.get(RESULT_CODE_HOUSING);
                    TreeSet<Bid> engineeringSet = result.get(RESULT_CODE_ENGINEERING);
                    TreeSet<Bid> buildingSet = result.get(RESULT_CODE_BUILDING);
                    TreeSet<Bid> utilitiesSet = result.get(RESULT_CODE_UTILITIES);

                    resetDataSetSizes();

                    if (housingSet != null) housingSetSize = housingSet.size();
                    if (engineeringSet != null) engineeringSetSize = engineeringSet.size();
                    if (buildingSet != null) buildingSetSize = buildingSet.size();
                    if (utilitiesSet != null) utilitiesSetSize = utilitiesSet.size();

                    Log.d(TAG, "MBR: onSuccess: housingSetSize: " + housingSetSize);
                    Log.d(TAG, "MBR: onSuccess: engineeringSetSize: " + engineeringSetSize);
                    Log.d(TAG, "MBR: onSuccess: buildingSetSize: " + buildingSetSize);
                    Log.d(TAG, "MBR: onSuccess: utilitiesSetSize: " + utilitiesSetSize);

                    handleIncomingData();
                }

                @Override
                public void onFailure(int code, String message) {
                    Log.e(TAG, "MBR: onFailure: " + message);
                }
            });
        }
        else if (this.dataSourceType == DATA_SOURCE_TYPE_MRA) {
            dataSourceMRA.refreshRecentlyAddedProjects(new LecetCallback<TreeMap<Long, TreeSet<Project>>>() {
                @Override
                public void onSuccess(TreeMap<Long, TreeSet<Project>> result) {
                    Log.w(TAG, "MRA: onSuccess");

                    resetDataSetSizes();

                    TreeSet<Project> housing = result.get(Long.valueOf(RESULT_CODE_HOUSING));
                    TreeSet<Project> engineering = result.get(Long.valueOf(RESULT_CODE_ENGINEERING));
                    TreeSet<Project> building = result.get(Long.valueOf(RESULT_CODE_BUILDING));
                    TreeSet<Project> utilities = result.get(Long.valueOf(RESULT_CODE_UTILITIES));

                    if (housing != null) {
                        housingSetSize = housing.size();
                    }

                    if (engineering != null) {
                        engineeringSetSize = engineering.size();
                    }

                    if (building != null) {
                        buildingSetSize = building.size();
                    }

                    if (utilities != null) {
                        utilitiesSetSize = utilities.size();
                    }

                    Log.d(TAG, "MRA: onSuccess: housingSetSize: " + housingSetSize);
                    Log.d(TAG, "MRA: onSuccess: engineeringSetSize: " + engineeringSetSize);
                    Log.d(TAG, "MRA: onSuccess: buildingSetSize: " + buildingSetSize);
                    Log.d(TAG, "MRA: onSuccess: utilitiesSetSize: " + utilitiesSetSize);

                    handleIncomingData();
                }

                @Override
                public void onFailure(int code, String message) {
                    Log.w(TAG, "MRA: onFailure: " + message);
                }
            });

        }
        else if (this.dataSourceType == DATA_SOURCE_TYPE_MRU) {
            dataSourceMRU.refreshRecentlyUpdatedProjects(new LecetCallback<TreeMap<Long, TreeSet<Project>>>() {
                @Override
                public void onSuccess(TreeMap<Long, TreeSet<Project>> result) {
                    Log.w(TAG, "MRU: onSuccess");

                    resetDataSetSizes();

                    TreeSet<Project> housing = result.get(Long.valueOf(RESULT_CODE_HOUSING));
                    TreeSet<Project> engineering = result.get(Long.valueOf(RESULT_CODE_ENGINEERING));
                    TreeSet<Project> building = result.get(Long.valueOf(RESULT_CODE_BUILDING));
                    TreeSet<Project> utilities = result.get(Long.valueOf(RESULT_CODE_UTILITIES));

                    if (housing != null) {
                        housingSetSize = housing.size();
                    }

                    if (engineering != null) {
                        engineeringSetSize = engineering.size();
                    }

                    if (building != null) {
                        buildingSetSize = building.size();
                    }

                    if (utilities != null) {
                        utilitiesSetSize = utilities.size();
                    }

                    Log.d(TAG, "MRU: onSuccess: housingSetSize: " + housingSetSize);
                    Log.d(TAG, "MRU: onSuccess: engineeringSetSize: " + engineeringSetSize);
                    Log.d(TAG, "MRU: onSuccess: buildingSetSize: " + buildingSetSize);
                    Log.d(TAG, "MRU: onSuccess: utilitiesSetSize: " + utilitiesSetSize);

                    handleIncomingData();
                }

                @Override
                public void onFailure(int code, String message) {
                    Log.w(TAG, "MRU: onFailure: " + message);
                }
            });
        }
    }

    protected void resetDataSetSizes() {
        housingSetSize = 0;
        engineeringSetSize = 0;
        buildingSetSize = 0;
        utilitiesSetSize = 0;
    }

    protected void consolidateDataSetSizes() {
        b_setSize = buildingSetSize + housingSetSize;
        h_setSize = engineeringSetSize + utilitiesSetSize;
    }

    protected void handleIncomingData() {
        Log.d(TAG, "handleIncomingData");

        consolidateDataSetSizes();

        List<PieEntry> entries = new ArrayList<>();

        // total size of data sets, for text display
        int totalSize = h_setSize + b_setSize;

        List<Integer> colorsList = new ArrayList<Integer>();

        setReferences();

        PieEntry entry;
        float xValue = -1;

        // for any result category that contains data, add a pie chart Entry and add the corresponding color for the chart segment
        if(b_button != null) {
            if (b_setSize > 0) {
                entry = new PieEntry(b_setSize, Long.toString(CONSOLIDATED_CODE_B));      // B - light blue
                entries.add(entry);
                b_chartX = ++xValue;
                colorsList.add(R.color.lecetLightBlue);
                b_button.setVisibility(View.VISIBLE);
            }
            else b_button.setVisibility(View.GONE);
        }

        if(h_button != null) {
            if (h_setSize > 0) {
                entry = new PieEntry(h_setSize, Long.toString(CONSOLIDATED_CODE_H));      // H - dark orange
                entries.add(entry);
                h_chartX = ++xValue;
                colorsList.add(R.color.lecetDarkOrange);
                h_button.setVisibility(View.VISIBLE);
            }
            else h_button.setVisibility(View.GONE);
        }

        int[] colorsArr = new int[colorsList.size()];
        for(int i=0; i<colorsList.size(); i++) {
            colorsArr[i] = colorsList.get(i);
        }

        // populate pie chart data and set its params
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(CHART_SLICE_SPACING);
        dataSet.setHighlightEnabled(true);
        dataSet.setSelectionShift(CHART_SELECTION_SHIFT);
        dataSet.setColors(colorsArr, fragment.getContext());

        PieData data = new PieData(dataSet);

        pieChartView.setData(data);
        pieChartView.notifyDataSetChanged();
        pieChartView.invalidate();              // refresh chart display

        setSubtitleNum(Integer.toString(totalSize));
    }

    /**
     * Inner Class: Custom Marker View
     * For displaying dynamic custom value views in chart segments.
     */
    public class CustomMarkerView extends MarkerView {

        private final static float MARKER_PLACEMENT_SHIFT = .20f;

        private TextView markerTextView;
        private float origXPx = 0;
        private float origYPx = 0;

        public CustomMarkerView (Context context, int layoutResource) {
            super(context, layoutResource);
            markerTextView = (TextView) findViewById(R.id.chart_marker_text);
        }

        /**
         * Callbacks everytime the MarkerView is redrawn, can be used to update the UI
         */
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            markerTextView.setText(Integer.toString((int)e.getY())); // set the entry-value as the display text

            if(!Float.isNaN(highlight.getY())) {
                origXPx = highlight.getXPx();
                origYPx = highlight.getYPx();
            }
            else
            {
                Log.w(TAG, "refreshContent: WARNING *** Highlight Y is NaN");   //NOTE - this may be the root cause issues with value display offset
                origXPx = 0;
                origYPx = 0;
            }

            int index = (int) highlight.getX();

            //Log.d(TAG, "refreshContent: index: " +  index);
            int color = pieChartView.getData().getDataSet().getColor(index);
            markerTextView.setTextColor(color);
        }

        /**
         * X Positioning for the marker
         * Known issue where enlarging slices via button strip throws off getXOffset values
         */
        /*@Override
        public int getXOffset(float xpos) {
            int origX = -(getWidth() / 2);
            int chartCenterX = pieChartView.getWidth() / 2;
            int dx = (int) (origXPx - chartCenterX);
            int addedX = (int) (MARKER_PLACEMENT_SHIFT * dx);
            int extendedXOffset = origX + addedX;
            //return origX;             // orig unshifted x
            return extendedXOffset;     // position custom label in center X of default label location
        }*/

        /**
         * Y Positioning for the marker
         * Known issue where enlarging slices via button strip throws off getYOffset values
         */
        /*@Override
        public int getYOffset(float ypos) {
            int origY = -(getHeight() / 2);
            int chartCenterY = pieChartView.getHeight() / 2;
            int dy = (int) (origYPx - chartCenterY);
            int addY = (int) (MARKER_PLACEMENT_SHIFT * dy);
            int extendedYOffset = origY + addY;
            //return origY;             // orig unshifted y
            return extendedYOffset;     // position custom label in center Y of default label location
        }*/
    }


    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
        PieEntry pieEntry = (PieEntry) entry;
        String label = pieEntry.getLabel();

        Log.d(TAG, "onValueSelected: " + label + ", " + highlight);

        // update chart values display
        PieData data = pieChartView.getData();
        data.setDrawValues(false);
//        IPieDataSet dataSet = data.getDataSetByIndex(highlight.getDataSetIndex());

//        dataSet.setDrawValues(false);
//        dataSet.setValueTextColor(Color.WHITE);
//        dataSet.setValueTextSize(CHART_VALUE_TEXT_SIZE);
//        dataSet.setValueFormatter(new CustomValueFormatter());


        // custom dynamic chart value marker view - NOTE - these next three lines enable/disable the custom value highlighting. iOS does not use it.
        /*CustomMarkerView mv = new CustomMarkerView (fragment.getContext(), R.layout.dashboard_chart_marker_view);
        pieChartView.setDrawMarkerViews(true);
        pieChartView.setMarkerView(mv);*/

        pieChartView.setHoleRadius(CHART_HOLE_RADIUS_SELECTED);
        pieChartView.invalidate(); // refresh

        // highlighting of values (references seem to be null here, so reset them)
        setReferences();

        View buttonToSelect = null;
        View iconToShow = null;

        if (label.equals(Long.toString(CONSOLIDATED_CODE_B))) {
            buttonToSelect = b_button;
            iconToShow = b_icon;
            notifyDelegateOfSelection(CONSOLIDATED_CODE_B);
        }
        else if (label.equals(Long.toString(CONSOLIDATED_CODE_H))) {
            buttonToSelect = h_button;
            iconToShow = h_icon;
            notifyDelegateOfSelection(CONSOLIDATED_CODE_H);
        }
        else Log.w(TAG, "onValueSelected: ERROR");

        // show the corresponding category button
        setCategoryButtonState(buttonToSelect);

        // show the corresponding center icon
        hideCategoryIcons();
        iconToShow.setVisibility(View.VISIBLE);
    }

    public void notifyDelegateOfSelection(Long category) {

        // MBR
        if(dataSourceType == DATA_SOURCE_TYPE_MBR) {
            if(category == RESULT_CODE_HOUSING) {
                delegateMBR.bidGroupSelected(BidDomain.HOUSING);
            }
            else if(category == RESULT_CODE_ENGINEERING) {
                delegateMBR.bidGroupSelected(BidDomain.ENGINEERING);
            }
            else if(category == RESULT_CODE_BUILDING) {
                delegateMBR.bidGroupSelected(BidDomain.BUILDING);
            }
            else if(category == RESULT_CODE_UTILITIES) {
                delegateMBR.bidGroupSelected(BidDomain.UTILITIES);
            }
            else if (category == CONSOLIDATED_CODE_B) {
                delegateMBR.bidGroupSelected(BidDomain.CONSOLIDATED_CODE_B);
            }
            else if (category == CONSOLIDATED_CODE_H) {
                delegateMBR.bidGroupSelected(BidDomain.CONSOLIDATED_CODE_H);
            }
        }

        // MRA
        else if(dataSourceType == DATA_SOURCE_TYPE_MRA) {

            if (category == RESULT_CODE_HOUSING) {
                delegateMRA.mraBidGroupSelected(BidDomain.HOUSING);
            }
            else if (category == RESULT_CODE_ENGINEERING) {
                delegateMRA.mraBidGroupSelected(BidDomain.ENGINEERING);
            }
            else if (category == RESULT_CODE_BUILDING) {
                delegateMRA.mraBidGroupSelected(BidDomain.BUILDING);
            }
            else if (category == RESULT_CODE_UTILITIES) {
                delegateMRA.mraBidGroupSelected(BidDomain.UTILITIES);
            }
            else if (category == CONSOLIDATED_CODE_B) {
                delegateMRA.mraBidGroupSelected(BidDomain.CONSOLIDATED_CODE_B);
            }
            else if (category == CONSOLIDATED_CODE_H) {
                delegateMRA.mraBidGroupSelected(BidDomain.CONSOLIDATED_CODE_H);
            }
        }

        // MRU
        else if(dataSourceType == DATA_SOURCE_TYPE_MRU) {
            if(category == RESULT_CODE_HOUSING) {
                delegateMRU.mruBidGroupSelected(BidDomain.HOUSING);
            }
            else if(category == RESULT_CODE_ENGINEERING) {
                delegateMRU.mruBidGroupSelected(BidDomain.ENGINEERING);
            }
            else if(category == RESULT_CODE_BUILDING) {
                delegateMRU.mruBidGroupSelected(BidDomain.BUILDING);
            }
            else if(category == RESULT_CODE_UTILITIES) {
                delegateMRU.mruBidGroupSelected(BidDomain.UTILITIES);
            }
            else if (category == CONSOLIDATED_CODE_B) {
                delegateMRU.mruBidGroupSelected(BidDomain.CONSOLIDATED_CODE_B);
            }
            else if (category == CONSOLIDATED_CODE_H) {
                delegateMRU.mruBidGroupSelected(BidDomain.CONSOLIDATED_CODE_H);
            }
        }
    }

    @Override
    public void onNothingSelected() {
        //Log.d(TAG, "onNothingSelected ");

        // hide chart values
        try {
            PieData data = pieChartView.getData();
            //IPieDataSet dataSet = data.getDataSet();
            data.setDrawValues(false);
            pieChartView.setHoleRadius(CHART_HOLE_RADIUS_UNSELECTED);
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

    public void onButtonClickH(View view) {
        //Log.d(TAG, "onButtonClickH");
        pieChartView.highlightValue(h_chartX, 0);
        setCategoryButtonState(view);
    }

    public void onButtonClickB(View view) {
        //Log.d(TAG, "onButtonClickB");
        pieChartView.highlightValue(b_chartX, 0);
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
        b_button.setSelected(false);
        h_button.setSelected(false);
    }

    public void hideAllButtons() {
        b_button.setVisibility(View.INVISIBLE);
        h_button.setVisibility(View.INVISIBLE);
    }

    private void hideCategoryIcons() {
        b_icon.setVisibility(View.INVISIBLE);
        h_icon.setVisibility(View.INVISIBLE);
    }


    /////////////////////////////////
    // GETTERS / SETTERS

    @Bindable
    public String getSubtitleNum() {
        return subtitleNum;
    }

    public void setSubtitleNum(String subtitleNum) {
        this.subtitleNum = subtitleNum;
        notifyPropertyChanged(BR.subtitleNum);     
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
