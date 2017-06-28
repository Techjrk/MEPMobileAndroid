package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
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

public abstract class TrackingListActivity<T extends TrackingListViewModel> extends LecetBaseActivity {

    private final String TAG = "ProjectTrackingListAct";

    // Intent extra names
    public static final String PROJECT_LIST_ITEM_ID = "listItemId";
    public static final String PROJECT_LIST_ITEM_SIZE = "listItemSize";
    public static final String PROJECT_LIST_ITEM_TITLE = "listItemTitle";

    private T viewModel;
    private long listItemId;

    public abstract T buildViewModel(long listItemId);
    public abstract String getActionBarSubtitle(int dataSize);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        // get extras from Intent
        listItemId = getIntent().getLongExtra(PROJECT_LIST_ITEM_ID, -1);
        int listItemSize = getIntent().getIntExtra(PROJECT_LIST_ITEM_SIZE, 0);
        String listItemTitle = getIntent().getStringExtra(PROJECT_LIST_ITEM_TITLE);

        setupBinding(listItemId);
        setupToolbar(listItemTitle, listItemSize);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        viewModel.handleOnActivityResult(requestCode, resultCode, data);
    }

    public T getViewModel() {
        return viewModel;
    }

    public long getListItemId() {
        return listItemId;
    }

    private void setupBinding(long listItemId) {
        ActivityTrackingListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tracking_list);

        viewModel = buildViewModel(listItemId);

        binding.setViewModel(viewModel);
    }

    private void setupToolbar(String title, int listItemSize) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_tracking_list, null);
 //           View tb = inflater.inflate(R.layout.projects_near_me_search_bar_layout, null);
            // subtitle, handle plural or singular
            String subTitle = getActionBarSubtitle(listItemSize);

            viewModel.setToolbar(tb, title, subTitle);

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
