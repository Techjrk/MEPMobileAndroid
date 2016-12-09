package com.lecet.app.content;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityModifyProjectTrackingListBinding;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.TrackingListObject;
import com.lecet.app.viewmodel.ModifyTrackingListViewModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;

/**
 * Created by Josué Rodríguez on 12/11/2016.
 */

public abstract class ModifyTrackingListActivity<T extends RealmObject & TrackingListObject> extends LecetBaseActivity {

    public static final String EXTRA_PROJECT_LIST_ITEM_ID = "listItemId";
    public static final String EXTRA_PROJECT_LIST_ITEM_TITLE = "listItemTitle";
    public static final String EXTRA_PROJECT_LIST_ITEM_SIZE = "listItemSize";
    public static final String EXTRA_SORT_BY = "sortBy";

    private ModifyTrackingListViewModel viewModel;
    private long listItemId;

    public static Intent intentForResult(Activity requester, Class requestee, long listItemId, String listItemTitle, int listItemSize, @ModifyTrackingListViewModel.TrackingSort int sort) {
        Intent intent = new Intent(requester, requestee);
        intent.putExtra(EXTRA_PROJECT_LIST_ITEM_ID, listItemId);
        intent.putExtra(EXTRA_PROJECT_LIST_ITEM_TITLE, listItemTitle);
        intent.putExtra(EXTRA_PROJECT_LIST_ITEM_SIZE, listItemSize);
        intent.putExtra(EXTRA_SORT_BY, sort);
        return intent;
    }


    public abstract T getTrackingList(TrackingListDomain trackingListDomain, long listItemId);

    public abstract ModifyTrackingListViewModel getModifyListViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain, T trackingList, @ModifyTrackingListViewModel.TrackingSort int sort);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItemId = getIntent().getLongExtra(EXTRA_PROJECT_LIST_ITEM_ID, -1);
        String listItemTitle = getIntent().getStringExtra(EXTRA_PROJECT_LIST_ITEM_TITLE);
        int listItemSize = getIntent().getIntExtra(EXTRA_PROJECT_LIST_ITEM_SIZE, 0);

        setupBinding();
        setupToolbar(listItemTitle, getString(R.string.mtm_menu_number_projects, listItemSize));
    }

    private void setupBinding() {
        ActivityModifyProjectTrackingListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_project_tracking_list);

        TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance(), new RealmChangeListener() {
            @Override
            public void onChange(Object element) {

            }
        });

        @ModifyTrackingListViewModel.TrackingSort int sort = getIntent().getIntExtra(EXTRA_SORT_BY, ModifyTrackingListViewModel.SORT_VALUE_HIGH);

        T trackingList = getTrackingList(trackingListDomain, listItemId);
        viewModel = getModifyListViewModel(this, trackingListDomain, trackingList, sort);
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

            View tb = inflater.inflate(R.layout.include_app_bar_layout_tracking_list, null);
            viewModel.setToolbar(tb, title, subtitle);

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
