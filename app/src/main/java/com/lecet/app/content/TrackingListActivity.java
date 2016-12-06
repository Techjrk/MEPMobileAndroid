package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityTrackingListBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.CompanyTrackingListViewModel;
import com.lecet.app.viewmodel.ProjectTrackingListViewModel;
import com.lecet.app.viewmodel.TrackingListViewModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * TrackingListActivity Created by jasonm on 11/2/16.
 */

public abstract class TrackingListActivity extends NavigationBaseActivity {

    private final String TAG = "ProjectTrackingListAct";

    // Intent extra names
    public static final String TRACKING_LIST_TYPE = "listType";
    public static final String PROJECT_LIST_ITEM_ID = "listItemId";
    public static final String PROJECT_LIST_ITEM_SIZE = "listItemSize";
    public static final String PROJECT_LIST_ITEM_TITLE = "listItemTitle";

    // List types
    public static final String TRACKING_LIST_TYPE_PROJECT = "project";
    public static final String TRACKING_LIST_TYPE_COMPANY = "company";

    private TrackingListViewModel viewModel;
    private String listType; // TODO: Find better solution.

    public abstract TrackingListViewModel buildViewModel(long listItemId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        // get extras from Intent
        listType = getIntent().getStringExtra(TRACKING_LIST_TYPE);
        long listItemId = getIntent().getLongExtra(PROJECT_LIST_ITEM_ID, -1);
        int listItemSize = getIntent().getIntExtra(PROJECT_LIST_ITEM_SIZE, 0);
        String listItemTitle = getIntent().getStringExtra(PROJECT_LIST_ITEM_TITLE);

        setupBinding(listItemId);
        setupToolbar(listItemTitle, listItemSize);
    }

    private void setupBinding(long listItemId) {
        ActivityTrackingListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tracking_list);

        viewModel = buildViewModel(listItemId);

        binding.setViewModel(viewModel);
    }

    private void setupToolbar(String title, int listItemSize) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_tracking_list, null);

            // subtitle, handle plural or singular
            StringBuilder subtitleSb = new StringBuilder();
            subtitleSb.append(listItemSize);
            subtitleSb.append(" ");
            if (listType.equals(TRACKING_LIST_TYPE_PROJECT)) {
                if (listItemSize != 1) {
                    subtitleSb.append(getResources().getString(R.string.projects));
                } else subtitleSb.append(getResources().getString(R.string.project));
            } else if (listType.equals(TRACKING_LIST_TYPE_COMPANY)) {
                if (listItemSize != 1) {
                    subtitleSb.append(getResources().getString(R.string.companies));
                } else subtitleSb.append(getResources().getString(R.string.company));
            }

            viewModel.setToolbar(tb, title, subtitleSb.toString());

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
