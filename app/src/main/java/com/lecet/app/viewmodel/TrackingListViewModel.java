package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.TrackingListRecyclerViewAdapter;
import com.lecet.app.content.TrackingListActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.TrackingListDomain;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * File: TrackingListViewModel Created: 11/2/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public abstract class TrackingListViewModel extends BaseObservable {

    @Retention(SOURCE)
    @IntDef({LIST_TYPE_COMPANY, LIST_TYPE_PROJECT})
    public @interface TrackingListType {}

    public static final int LIST_TYPE_COMPANY = 0;
    public static final int LIST_TYPE_PROJECT = 1;

    @TrackingListType
    private int listType;

    private final static String TAG = "ProjectTrackingListVM";

    private AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private TrackingListRecyclerViewAdapter listAdapter;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;
    private Switch showUpdatesToggle;


    private List<RealmObject> adapterData;
    private long listItemId;
    private boolean showUpdates = true;

    public TrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId, @TrackingListType int trackingListType) {

        this.appCompatActivity = appCompatActivity;
        this.listItemId = listItemId;
        this.listType = trackingListType;
        init(listType);
    }

    /**
     * Getters && Setters
     **/

    public long getListItemId() {
        return listItemId;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public List<RealmObject> getAdapterData() {
        return adapterData;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TrackingListRecyclerViewAdapter getListAdapter() {
        return listAdapter;
    }

    /**
     * Initializers
     **/

    private void init(@TrackingListType int listType) {

        initRecyclerView(listType);
        initShowUpdatesSwitch();
    }

    private void initRecyclerView(@TrackingListType int listType) {

        recyclerView = getRecyclerView(R.id.tracking_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        initializeEmptyAdapter(listType);
    }

    private void initShowUpdatesSwitch() {
        if (this.appCompatActivity != null) {
            showUpdatesToggle = (Switch) appCompatActivity.findViewById(R.id.toggle_button);
        }
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

    private void initializeEmptyAdapter(@TrackingListType int listType) {

        @TrackingListRecyclerViewAdapter.TrackingAdapterType int adapterType = listType == LIST_TYPE_COMPANY ? TrackingListRecyclerViewAdapter.LIST_TYPE_COMPANY : TrackingListRecyclerViewAdapter.LIST_TYPE_PROJECT;

        adapterData = new ArrayList<>();

        recyclerView = getRecyclerView(R.id.tracking_list_recycler_view);
        setupRecyclerView(recyclerView);
        listAdapter = new TrackingListRecyclerViewAdapter(adapterData, appCompatActivity, adapterType);
        recyclerView.setAdapter(listAdapter);
    }

    /**
     * Adapter Data Management: Company List
     **/

//    private void initAdapterWithCompanyTrackingListId(long listItemId) {
//
//        adapterData = new ArrayList<>();
//
//        //TODO - add RealmChangeListener as last argument to resolve deprecation
//        final TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(appCompatActivity), Realm.getDefaultInstance());
//        CompanyTrackingList companyList = trackingListDomain.fetchCompanyTrackingList(listItemId);
//
//        if (companyList != null) {
//            RealmList<Company> companies = companyList.getCompanies();
//            Company[] data = companies != null ? companies.toArray(new Company[companies.size()]) : new Company[0];
//
//            adapterData.addAll(Arrays.asList(data));
//        } else Log.w(TAG, "initAdapterWithCompanyTrackingListId: WARNING: companyList is null");
//
//        recyclerView = getRecyclerView(R.id.tracking_list_recycler_view);
//        setupRecyclerView(recyclerView);
//        listAdapter = new TrackingListRecyclerViewAdapter(TrackingListActivity.TRACKING_LIST_TYPE_COMPANY, adapterData);
//        recyclerView.setAdapter(listAdapter);
//    }

    /**
     * RecyclerView Management
     **/

    public void setupRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    public RecyclerView getRecyclerView(@IdRes int recyclerView) {

        return (RecyclerView) appCompatActivity.findViewById(recyclerView);
    }

    /**
     * Click handling
     **/

    public void onBackButtonClick(View view) {
        Log.d(TAG, "onBackButtonClick");
        appCompatActivity.onBackPressed();
    }

    public void onSortButtonClick(View view) {
        Log.d(TAG, "onSortButtonClick");
        Toast.makeText(appCompatActivity, "Sort button pressed", Toast.LENGTH_SHORT).show();
    }


    ///////////////////////////////
    // BINDINGS

    @Bindable
    public boolean getShowUpdates() {
        return showUpdates;
    }

    public void setShowUpdates(boolean showUpdates) {
        this.showUpdates = showUpdates;
        notifyPropertyChanged(BR.showUpdates);
        listAdapter.setShowUpdates(this.showUpdates);
        listAdapter.notifyDataSetChanged();
    }


}
