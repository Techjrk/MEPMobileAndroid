package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectTrackingListSortedBinding;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.viewmodel.ProjectTrackingListSortedViewModel;

import io.realm.Realm;

/**
 * Created by Josué Rodríguez on 12/11/2016.
 */

public class ProjectTrackingListSortedActivity extends LecetBaseActivity {

    private ProjectTrackingListSortedViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBinding();
    }

    private void setupBinding() {
        ActivityProjectTrackingListSortedBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_tracking_list_sorted);

        TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());

        viewModel = new ProjectTrackingListSortedViewModel(this, trackingListDomain);
        binding.setViewModel(viewModel);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
