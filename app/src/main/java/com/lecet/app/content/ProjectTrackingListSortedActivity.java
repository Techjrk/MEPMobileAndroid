package com.lecet.app.content;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectTrackingListSortedBinding;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.enums.SortBy;
import com.lecet.app.viewmodel.ProjectTrackingListSortedViewModel;

import io.realm.Realm;

/**
 * Created by Josué Rodríguez on 12/11/2016.
 */

public class ProjectTrackingListSortedActivity extends LecetBaseActivity {

    public static final String EXTRA_PROJECT_LIST_ITEM_ID = "listItemId";
    public static final String EXTRA_PROJECT_LIST_ITEM_TITLE = "listItemTitle";
    public static final String EXTRA_PROJECT_LIST_ITEM_SIZE = "listItemSize";
    public static final String EXTRA_SORT_BY = "sortBy";

    private ProjectTrackingListSortedViewModel viewModel;


    public static void startActivityForResult(Activity activity, long listItemId, String listItemTitle, int listItemSize, SortBy sort) {
        Intent intent = new Intent(activity, ProjectTrackingListSortedActivity.class);
        intent.putExtra(EXTRA_PROJECT_LIST_ITEM_ID, listItemId);
        intent.putExtra(EXTRA_PROJECT_LIST_ITEM_TITLE, listItemTitle);
        intent.putExtra(EXTRA_PROJECT_LIST_ITEM_SIZE, listItemSize);
        intent.putExtra(EXTRA_SORT_BY, sort);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long listItemId = getIntent().getLongExtra(EXTRA_PROJECT_LIST_ITEM_ID, -1);
        String listItemTitle = getIntent().getStringExtra(EXTRA_PROJECT_LIST_ITEM_TITLE);
        int listItemSize = getIntent().getIntExtra(EXTRA_PROJECT_LIST_ITEM_SIZE, 0);
        SortBy sort = (SortBy) getIntent().getSerializableExtra(EXTRA_SORT_BY);

        setupBinding();
        setupToolbar(listItemTitle, getString(R.string.mtm_menu_number_projects, Integer.toString(listItemSize)));
    }

    private void setupBinding() {
        ActivityProjectTrackingListSortedBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_tracking_list_sorted);

        TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());

        viewModel = new ProjectTrackingListSortedViewModel(this, trackingListDomain);
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
            actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.activity_bar_background));
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
