package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectTrackingListBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.ProjectTrackingListViewModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * ProjectTrackingListActivity
 * Created by jasonm on 11/2/16.
 */

public class ProjectTrackingListActivity extends NavigationBaseActivity {

    private final String TAG = "ProjectTrackingListAct";

    public static final String PROJECT_LIST_ITEM_ID = "listItemId";
    public static final String PROJECT_LIST_ITEM_SIZE = "listItemSize";
    public static final String PROJECT_LIST_ITEM_TITLE = "listItemTitle";

    private ProjectTrackingListViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        // get extras from Intent
        long listItemId      = getIntent().getLongExtra(PROJECT_LIST_ITEM_ID, -1);
        int listItemSize     = getIntent().getIntExtra(PROJECT_LIST_ITEM_SIZE, 0);
        String listItemTitle = getIntent().getStringExtra(PROJECT_LIST_ITEM_TITLE);

        setupBinding(listItemId);
        setupToolbar(listItemTitle, new String(listItemSize + " " + getResources().getString(R.string.projects)));
    }

    private void setupBinding(long listItemId) {
        ActivityProjectTrackingListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_tracking_list);

        BidDomain bidDomain = new BidDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance(), new RealmChangeListener() {
            @Override
            public void onChange(Object element) {

            }
        }, projectDomain);

        viewModel = new ProjectTrackingListViewModel(this, listItemId, bidDomain, projectDomain, trackingListDomain);
        binding.setViewModel(viewModel);
    }

    private void setupToolbar(String title, String subtitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_projects_list, null);
            viewModel.setToolbar(tb, title, subtitle);

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
