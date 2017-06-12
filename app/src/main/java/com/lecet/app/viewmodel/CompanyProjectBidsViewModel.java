package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.adapters.CompanyProjectBidsAdapter;
import com.lecet.app.adapters.MenuTitleListAdapter;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Company;
import com.lecet.app.domain.BidDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import io.realm.Sort;

/**
 * File: CompanyProjectBidsViewModel Created: 2/2/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyProjectBidsViewModel extends BaseObservable {

    private static final String UPCOMING_TAG = "tab.upcoming";
    private static final String PAST_TAG = "tab.past";

    /**
     * Tool Bar
     **/
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;

    private TabLayout tabLayout;

    private final AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private CompanyProjectBidsAdapter listAdapter;
    private List<Bid> data;

    private final Company company;
    private final BidDomain bidDomain;
    @CompanyProjectBidsAdapter.NavigationMode
    private int navigationMode;

    /**
     * Sort Menu
     **/
    private ListPopupWindow mtmSortMenu;
    private MenuTitleListAdapter mtmSortAdapter;

    private static final int SORT_BID_DATE = 0;
    private static final int SORT_LAST_UPDATE = 1;
    private static final int SORT_DATE_ADDED = 2;
    private static final int SORT_VALUE_HIGH = 3;
    private static final int SORT_VALUE_LOW = 4;


    public CompanyProjectBidsViewModel(AppCompatActivity appCompatActivity, Company company, BidDomain bidDomain) {
        this.appCompatActivity = appCompatActivity;
        this.company = company;
        this.bidDomain = bidDomain;

        // Default to create date
        RealmResults<Bid> results = bidDomain.fetchBidsByCompanyGreaterThanDate(company.getId(), "amount", Sort.DESCENDING, new Date());
        Bid[] bids = new Bid[results.size()];
        results.toArray(bids);
        data = new ArrayList<>(Arrays.asList(bids));

        this.navigationMode = CompanyProjectBidsAdapter.NAVIGATION_MODE_PAST;
        initRecyclerView(this.company);
        setupTabLayout(appCompatActivity);
    }

    // We are running into a crash as though the data is lost when an acitvity resumes
    public void refreshData() {

        // TODO: No need to refresh, data is still there. Why is this occuring?
        // Default to create date
        RealmResults<Bid> results = bidDomain.fetchBidsByCompanyGreaterThanDate(company.getId(), "amount", Sort.DESCENDING, new Date());
        Bid[] bids = new Bid[results.size()];
        results.toArray(bids);
        data = new ArrayList<>(Arrays.asList(bids));

        this.navigationMode = CompanyProjectBidsAdapter.NAVIGATION_MODE_PAST;
        initRecyclerView(this.company);

        // Set Tablayout to default position
        tabLayout.getTabAt(0).select();
    }

    /**
     * Toolbar
     **/
    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);

        //Check the binding in the layout, which is not triggering the button clicks in this VM
 /*       backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick(v);
            }
        });*/

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortButtonClick(v);
            }
        });
        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }

    /**
     * Tabs
     **/
    private void setupTabLayout(AppCompatActivity appCompatActivity) {

        tabLayout = (TabLayout) appCompatActivity.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setTag(UPCOMING_TAG).setText(getUpComingTabTitle()), true); // Upcoming, selected by default
        tabLayout.addTab(tabLayout.newTab().setTag(PAST_TAG).setText(getPastTabTitle()), false); // Past
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getTag().equals(PAST_TAG)) {

                    RealmResults<Bid> results = bidDomain.fetchBidsByCompanyLessThanDate(company.getId(), "amount", Sort.DESCENDING, new Date());
                    Bid[] bids = new Bid[results.size()];
                    results.toArray(bids);
                    data.clear();
                    data.addAll(new ArrayList<>(Arrays.asList(bids)));
                    listAdapter.notifyDataSetChanged();

                } else if (tab.getTag().equals(UPCOMING_TAG)) {

                    RealmResults<Bid> results = bidDomain.fetchBidsByCompanyGreaterThanDate(company.getId(), "amount", Sort.DESCENDING, new Date());
                    Bid[] bids = new Bid[results.size()];
                    results.toArray(bids);
                    data.clear();
                    data.addAll(new ArrayList<>(Arrays.asList(bids)));
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Bindable
    public String getUpComingTabTitle() {

        int size = bidDomain.fetchBidsByCompanyGreaterThanDate(company.getId(), "amount", Sort.DESCENDING, new Date()).size();

        return String.format(appCompatActivity.getString(R.string.bid_tab_title_upcoming), size);
    }

    @Bindable
    public String getPastTabTitle() {

        int size = bidDomain.fetchBidsByCompanyLessThanDate(company.getId(), "amount", Sort.DESCENDING, new Date()).size();

        return String.format(appCompatActivity.getString(R.string.bid_tab_title_past), size);
    }


    /**
     * Adapter Data Management: Project List
     **/

    private void initRecyclerView(Company company) {

        recyclerView = (RecyclerView) appCompatActivity.findViewById(R.id.tracking_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        initializeAdapter();
    }

    private void initializeAdapter() {

        listAdapter = new CompanyProjectBidsAdapter(appCompatActivity, data);
        recyclerView.setAdapter(listAdapter);
    }

    /**
     * Sort Menu
     **/

    public void onBackButtonClick(View view) {

        appCompatActivity.onBackPressed();
    }

    public void onSortButtonClick(View view) {
        toogleMTMSortMenu();
    }

    private void toogleMTMSortMenu() {
        if (mtmSortMenu == null) {
            createMTMSortMenu(appCompatActivity.findViewById(R.id.sort_menu_button));
        }
        mtmSortMenu.show();
    }

    private void createMTMSortMenu(View anchor) {
        if (mtmSortMenu == null) {
            mtmSortMenu = new ListPopupWindow(appCompatActivity);

            mtmSortAdapter
                    = new MenuTitleListAdapter(appCompatActivity
                    , appCompatActivity.getResources().getString(R.string.mtm_sort_menu_title)
                    , sortMenuOptions());

            Display display = appCompatActivity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - appCompatActivity.getResources().getDimensionPixelSize(R.dimen.mtm_sort_menu_space);
            int[] coordinates = new int[2];
            anchor.getLocationOnScreen(coordinates);
            int offset = (int) (coordinates[0]
                    - (appCompatActivity.getResources().getDimensionPixelSize(R.dimen.mtm_sort_menu_space) / 2.0));
            mtmSortMenu.setBackgroundDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.overflow_menu_background));
            mtmSortMenu.setAnchorView(anchor);
            mtmSortMenu.setModal(true);
            mtmSortMenu.setWidth(width);
            mtmSortMenu.setHorizontalOffset(-offset);
            mtmSortMenu.setVerticalOffset(anchor.getHeight());
            mtmSortMenu.setAdapter(mtmSortAdapter);
            mtmSortMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        mtmSortMenu.dismiss();

                        handleSortSelection(position - 1); // removing title
                    }
                }
            }); // the callback for when a list item is selected
        }
    }

    private String[] sortMenuOptions() {

        return appCompatActivity.getResources().getStringArray(R.array.mobile_tracking_list_sort_menu);
    }

    private void handleSortSelection(int position) {

        Comparator<Bid> comparator;

        switch (position) {
            case SORT_BID_DATE:
                comparator = new BidDateComparator();
                break;
            case SORT_DATE_ADDED:
                comparator = new BidAddedComparator();
                break;
            case SORT_LAST_UPDATE:
                comparator = new BidLastUpdatedComparator();
                break;
            case SORT_VALUE_HIGH:
                comparator = new BidAmountHighComparator();
                break;
            case SORT_VALUE_LOW:
                comparator = new BidAmountLowComparator();
                break;
            default:
                comparator = new BidDateComparator();
                break;
        }

        Collections.sort(data, comparator);
        listAdapter.notifyDataSetChanged();
    }

    private class BidDateComparator implements Comparator<Bid> {
        @Override
        public int compare(Bid a, Bid b) {
            try {
                return a.getProject().getBidDate().before(b.getProject().getBidDate()) ? 1 : -1;
            }
            catch (NullPointerException e) {
                return -1;
            }
        }
    }

    private class BidAddedComparator implements Comparator<Bid> {
        @Override
        public int compare(Bid a, Bid b) {
            try {
                return a.getProject().getFirstPublishDate().before(b.getProject().getFirstPublishDate()) ? 1 : -1;
            }
            catch (NullPointerException e) {
                return -1;
            }
        }
    }

    private class BidLastUpdatedComparator implements Comparator<Bid> {
        @Override
        public int compare(Bid a, Bid b) {
            try {
                return a.getProject().getLastPublishDate().before(b.getProject().getLastPublishDate()) ? 1 : -1;
            }
            catch (NullPointerException e) {
                return -1;
            }
        }
    }

    private class BidAmountHighComparator implements Comparator<Bid> {
        @Override
        public int compare(Bid a, Bid b) {
            try {
                return a.getAmount() < b.getAmount() ? 1 : -1;
            }
                catch (NullPointerException e) {
                return -1;
            }
        }
    }

    private class BidAmountLowComparator implements Comparator<Bid> {
        @Override
        public int compare(Bid a, Bid b) {
            try {
                return a.getAmount() > b.getAmount() ? 1 : -1;
            }
            catch (NullPointerException e) {
                return -1;
            }
        }
    }
}
