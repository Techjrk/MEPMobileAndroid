package com.lecet.app.content;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.utility.DateUtility;
import com.lecet.app.viewmodel.MainViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MainActivity Created by jasonm on 8/15/16.
 */
public class MainActivity extends NavigationBaseActivity implements MHSDelegate, MHSDataSource, MBRDelegate, MBRDataSource {

    private static final String TAG = "MainActivity";

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BidDomain bidDomain = new BidDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        Calendar calendar = Calendar.getInstance();

        //TODO: Attach Model to layout
        viewModel = new MainViewModel(this, bidDomain, projectDomain, calendar);
    }



    /** MBR Implementation **/

    @Override
    public void refreshRecentlyMadeBids(LecetCallback<TreeMap<Long, TreeSet<Bid>>> callback) {

        viewModel.getBidsRecentlyMade(DateUtility.addDays(-30), callback);
    }

    @Override
    public void bidGroupSelected(@BidDomain.BidGroup int group) {

    }

    /** MHS Implementation **/
    @Override
    public void refreshProjectsHappeningSoon(final LecetCallback<Project[]> callback) {

        viewModel.getProjectsHappeningSoon(callback);
    }

    @Override
    public void calendarSelected(Date selectedDate) {

    }

}
