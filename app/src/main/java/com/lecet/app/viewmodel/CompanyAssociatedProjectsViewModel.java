package com.lecet.app.viewmodel;

import android.graphics.Point;
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
import com.lecet.app.adapters.CompanyAssociatedProjectAdapter;
import com.lecet.app.adapters.MenuTitleListAdapter;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * File: CompanyAssociatedProjectsViewModel Created: 1/25/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyAssociatedProjectsViewModel {

    /**
     * Tool Bar
     **/
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;

    private final AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private CompanyAssociatedProjectAdapter listAdapter;

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

    private String filter;
    private int selectedSort;

    private final Company company;
    private RealmResults<Project> data;


    public CompanyAssociatedProjectsViewModel(AppCompatActivity appCompatActivity, Company company) {
        this.appCompatActivity = appCompatActivity;
        this.company = company;

        initRecyclerView(this.company);
    }


    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);

        //TODO - check the binding in the layout, which is not triggering the button clicks in this VM
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick(v);
            }
        });

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
     * Adapter Data Management: Project List
     **/

    private void initRecyclerView(Company company) {

        recyclerView = (RecyclerView) appCompatActivity.findViewById(R.id.tracking_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        initializeAdapter(company);
    }

    private void initializeAdapter(Company company) {

        data = defaultSortedData(company.getProjects());
        listAdapter = new CompanyAssociatedProjectAdapter(data);
        recyclerView.setAdapter(listAdapter);
    }

    /** Sort Menu **/

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

        Sort sort = Sort.DESCENDING;

        switch (position) {
            case SORT_BID_DATE:
                selectedSort = SORT_BID_DATE;
                filter = "bidDate";
                break;
            case SORT_DATE_ADDED:
                selectedSort = SORT_DATE_ADDED;
                filter = "firstPublishDate";
                break;
            case SORT_LAST_UPDATE:
                selectedSort = SORT_LAST_UPDATE;
                filter = "lastPublishDate";
                break;
            case SORT_VALUE_HIGH:
                selectedSort = SORT_VALUE_HIGH;
                filter = "estLow";
                break;
            case SORT_VALUE_LOW:
                selectedSort = SORT_VALUE_LOW;
                filter = "estLow";
                sort = Sort.ASCENDING;
                break;
            default:
                selectedSort = SORT_BID_DATE;
                filter = "bidDate";
                break;
        }

        data.sort(filter, sort);
        listAdapter.notifyDataSetChanged();
    }

    private RealmResults<Project> defaultSortedData(RealmList<Project> projects) {

        Sort sort = Sort.DESCENDING;
        selectedSort = SORT_BID_DATE;
        filter = "bidDate";

        return projects.sort(filter,sort);
    }
}
