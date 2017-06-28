package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.MenuTitleListAdapter;
import com.lecet.app.adapters.TrackingListAdapter;
import com.lecet.app.contentbase.BaseObservableViewModel;

import io.realm.RealmResults;

/**
 * File: TrackingListViewModel Created: 11/2/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public abstract class TrackingListViewModel<T extends RealmResults> extends BaseObservableViewModel {

    private final static String TAG = "ProjectTrackingListVM";

    public static final int MODIFY_TRACKING_LIST_REQUEST_CODE = 666;
    public static final String RESULT_EXTRA_ITEMS_EDITED = "result_extra_move_items";
    public static final String RESULT_EXTRA_SELECTED_SORT = "result_extra_selected_sort";

    private AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private TrackingListAdapter listAdapter;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;

    private ListPopupWindow mtmSortMenu;
    private MenuTitleListAdapter mtmSortAdapter;


    private T adapterData;
    private long listItemId;
    private boolean showUpdates = true;

    public TrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.listItemId = listItemId;
        init();
    }

    public abstract String[] sortMenuOptions();

    public abstract void handleSortSelection(int position);

    public abstract TrackingListAdapter recyclerViewAdapter();

    public abstract void handleEditMode();

    public abstract void handleOnActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * Getters && Setters
     **/

    public long getListItemId() {
        return listItemId;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public T getAdapterData() {
        return adapterData;
    }

    public void setAdapterData(T adapterData) {

        this.adapterData = adapterData;
        initializeAdapter();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TrackingListAdapter getListAdapter() {
        return listAdapter;
    }

    /**
     * Initializers
     **/

    private void init() {

        initRecyclerView();
    }

    private void initRecyclerView() {

        recyclerView = getRecyclerView(R.id.tracking_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        initializeAdapter();
    }

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
    //    backButton = (ImageView) toolbar.findViewById(R.id.back_button);
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

    public void updateToolbarSubTitle(int listSize, String title) {

        subtitleTextView.setText(getActionBarSubtitle(listSize, title));
    }

    public String getActionBarSubtitle(int dataSize, String title) {
        // subtitle, handle plural or singular
        StringBuilder subtitleSb = new StringBuilder();
        subtitleSb.append(dataSize);
        subtitleSb.append(" ");
        subtitleSb.append(title);

        return subtitleSb.toString();
    }

    /**
     * Adapter Data Management: Project List
     **/

    private void initializeAdapter() {

        recyclerView = getRecyclerView(R.id.tracking_list_recycler_view);
        setupRecyclerView(recyclerView);
        listAdapter = recyclerViewAdapter();
        recyclerView.setAdapter(listAdapter);
        setShowUpdates(true);
    }

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

    public void onEditClicked(View view) {

        handleEditMode();
    }

    public void onBackButtonClick(View view) {
        Log.d(TAG, "onBackButtonClick");
        appCompatActivity.onBackPressed();
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

    public void onSortButtonClick(View view) {
        toogleMTMSortMenu();
    }

    /** Dialogs **/

}
