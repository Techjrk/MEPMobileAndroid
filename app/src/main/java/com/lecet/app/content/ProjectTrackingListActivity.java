package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectTrackingListBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.enums.LacetFont;
import com.lecet.app.utility.TextViewUtility;
import com.lecet.app.viewmodel.ProjectTrackingListViewModel;

import java.lang.reflect.Field;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ProjectTrackingListActivity
 * Created by jasonm on 11/2/16.
 */

public class ProjectTrackingListActivity extends NavigationBaseActivity {

    private final String TAG = "ProjectTrackingListAct";

    private ProjectTrackingListViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        long listItemPosition = getIntent().getIntExtra("listItemPosition", -1);
        long listItemId = getIntent().getLongExtra("listItemId", -1);

        // TODO - accomodate company list also
        final TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        RealmResults<ProjectTrackingList> allProjectLists = trackingListDomain.fetchUserProjectTrackingList();
        ProjectTrackingList projectList = allProjectLists.get((int) listItemPosition - 1);  // get just the one list of projects we need

        setupBinding(projectList);
        setupToolbar();

        //setContentView(R.layout.activity_project_tracking_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    private void setupBinding(ProjectTrackingList projectList) {
        ActivityProjectTrackingListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_tracking_list);

        BidDomain bidDomain = new BidDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        viewModel = new ProjectTrackingListViewModel(this, projectList, bidDomain, projectDomain);
        binding.setViewModel(viewModel);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.dashboard_projects);
        TextView toolbarTitle = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            toolbarTitle = (TextView) f.get(toolbar);
            TextViewUtility.changeTypeface(toolbarTitle, LacetFont.LATO_REGULAR);
            toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }

}
