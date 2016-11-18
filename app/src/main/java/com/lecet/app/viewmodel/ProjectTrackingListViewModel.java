package com.lecet.app.viewmodel;

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
import com.lecet.app.adapters.ProjectListRecyclerViewAdapter;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.content.ModifyProjectTrackingListActivity;
import com.lecet.app.content.ProjectTrackingListActivity;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.enums.SortBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * File: ProjectTrackingListViewModel
 * Created: 11/2/16
 * Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ProjectTrackingListViewModel extends BaseObservable {

    private final static String TAG = "ProjectTrackingListVM";

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private ProjectListRecyclerViewAdapter projectListAdapter;
    private List<Project> adapterData;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;
    private Switch showUpdatesToggle;
    private boolean showUpdates = true;
    private ListPopupWindow mtmSortMenu;
    private MenuTitleListAdapter mtmSortAdapter;


    public ProjectTrackingListViewModel(AppCompatActivity appCompatActivity, long listItemId, BidDomain bidDomain, ProjectDomain projectDomain) {

        Log.d(TAG, "Constructor");

        this.appCompatActivity = appCompatActivity;
        this.bidDomain = bidDomain;
        this.projectDomain = projectDomain;

        initShowUpdatesSwitch();
        initializeAdapter(listItemId);
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
     * Adapter Data Management
     **/

    private void initializeAdapter(long listItemId) {

        adapterData = new ArrayList<>();

        final TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(appCompatActivity), Realm.getDefaultInstance());
        ProjectTrackingList projectList = trackingListDomain.fetchProjectTrackingList(listItemId);

        if(projectList != null) {
            RealmList<Project> projects = projectList.getProjects();
            Project[] data = projects != null ? projects.toArray(new Project[projects.size()]) : new Project[0];

            adapterData.addAll(Arrays.asList(data));
            //projectListAdapter.notifyDataSetChanged();
        }
        else Log.w(TAG, "initializeAdapter: WARNING: projectList is null");

        recyclerView = getProjectRecyclerView(R.id.project_tracking_recycler_view);
        setupRecyclerView(recyclerView);
        projectListAdapter = new ProjectListRecyclerViewAdapter(adapterData);
        recyclerView.setAdapter(projectListAdapter);
    }

    /**
     * RecyclerView Management
     **/

    private void setupRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private RecyclerView getProjectRecyclerView(@IdRes int recyclerView) {

        return (RecyclerView) appCompatActivity.findViewById(recyclerView);
    }

    /**
     * Click handling
     **/

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
                    , appCompatActivity.getResources().getStringArray(R.array.mobile_tracking_list_sort_menu));

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
                        SortBy sortBy = SortBy.values()[position - 1];//removing the title
                        long listItemId = appCompatActivity.getIntent().getLongExtra(ProjectTrackingListActivity.PROJECT_LIST_ITEM_ID, -1);
                        String listItemTitle = appCompatActivity.getIntent().getStringExtra(ProjectTrackingListActivity.PROJECT_LIST_ITEM_TITLE);
                        int listItemSize = appCompatActivity.getIntent().getIntExtra(ProjectTrackingListActivity.PROJECT_LIST_ITEM_SIZE, 0);
                        ModifyProjectTrackingListActivity.startActivityForResult(appCompatActivity, listItemId, listItemTitle, listItemSize, sortBy);
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
        projectListAdapter.setShowUpdates(this.showUpdates);
        projectListAdapter.notifyDataSetChanged();
    }


    public void onSortButtonClick(View view) {
        toogleMTMSortMenu();
    }

}
