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
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.CompanyDomain;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: CompanyAssociatedProjectsViewModel Created: 1/25/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyAssociatedProjectsViewModel extends BaseObservableViewModel {

    /**
     * Tool Bar
     **/
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;

    private final AppCompatActivity appCompatActivity;
    private final CompanyDomain companyDomain;

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

    private long companyId;
    private Company company;
    private Call<Company> companyCall;
    private RealmResults<Project> data;
    private int sortPosition = -1;

    public CompanyAssociatedProjectsViewModel(AppCompatActivity appCompatActivity, CompanyDomain companyDomain, long companyId) {
        super(appCompatActivity);

        this.appCompatActivity = appCompatActivity;
        this.companyDomain = companyDomain;
        this.companyId = companyId;

        refreshData();
    }


    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);


        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortButtonClick(v);
            }
        });
        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }

    // TODO: When returning from a different activity, the data seems to be null
    public void refreshData() {

        showProgressDialog();

        companyCall = companyDomain.getCompanyDetails(companyId, new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {

                if (response.isSuccessful()) {

                    companyDomain.copyToRealmTransaction(response.body());
                    company = companyDomain.fetchCompany(companyId).first();
                    initRecyclerView(company);

                    dismissProgressDialog();

                } else {

                    dismissProgressDialog();
                    showCancelAlertDialog(appCompatActivity.getString(R.string.app_name), response.message());
                }

            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {

                dismissProgressDialog();
                showCancelAlertDialog(appCompatActivity.getString(R.string.error_network_title), appCompatActivity.getString(R.string.error_network_message));
            }
        });

    }

    public void cancelRequest() {

        if (companyCall != null) {
            companyCall.cancel();
        }
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
        if(sortPosition == -1){
            listAdapter = new CompanyAssociatedProjectAdapter(data);
            recyclerView.setAdapter(listAdapter);
        }
        else{
            handleSortSelection(sortPosition);
        }

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
                        sortPosition = position -1;
                        handleSortSelection(sortPosition); // removing title
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

        data = data.sort(filter, sort);
        listAdapter = new CompanyAssociatedProjectAdapter(data);
        recyclerView.setAdapter(listAdapter);
    }

    private RealmResults<Project> defaultSortedData(RealmList<Project> projects) {

        Sort sort = Sort.DESCENDING;
        selectedSort = SORT_BID_DATE;
        filter = "bidDate";

        return projects.sort(filter,sort);
    }
}
