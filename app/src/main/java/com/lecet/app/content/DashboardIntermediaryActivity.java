package com.lecet.app.content;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.DashboardIntermediaryViewModel;

import io.realm.Realm;

public class DashboardIntermediaryActivity extends LecetBaseActivity {

    private DashboardIntermediaryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_intermediary);

        Log.d("DashboardInterm", "onCreate");

        Realm realm;

        // should catch any cases of Realm not already being inited
        try {
            realm = Realm.getDefaultInstance();
        }
        catch (IllegalStateException e) {
            Realm.init(getApplication());
            realm = Realm.getDefaultInstance();
        }

        LecetClient client = LecetClient.getInstance();
        LecetSharedPreferenceUtil shardPref = LecetSharedPreferenceUtil.getInstance(this);

        viewModel = new DashboardIntermediaryViewModel(this,
                new BidDomain(client, shardPref, realm),
                new ProjectDomain(client, shardPref, realm),
                new SearchDomain(client, shardPref, realm),
                new TrackingListDomain(client, shardPref, realm));
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.cancelDataDownload();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
