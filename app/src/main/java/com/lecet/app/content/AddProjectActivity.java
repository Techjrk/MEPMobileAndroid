package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityAddProjectBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.AddProjectActivityViewModel;

import io.realm.Realm;

import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_ADDRESS;
import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LATITUDE;
import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LONGITUDE;

/**
 * Created by jasonm on 5/15/17.
 */

public class AddProjectActivity extends AppCompatActivity {

    private static final String TAG = "AddProjectActivity";

    private AddProjectActivityViewModel viewModel;
    private String address;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityAddProjectBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_project);

        address   = getIntent().getStringExtra(EXTRA_MARKER_ADDRESS);
        latitude  = getIntent().getDoubleExtra(EXTRA_MARKER_LATITUDE, -1);
        longitude = getIntent().getDoubleExtra(EXTRA_MARKER_LONGITUDE, -1);

        Log.d(TAG, "onCreate: address: " + address);
        Log.d(TAG, "onCreate: latitude: " + latitude);
        Log.d(TAG, "onCreate: longitude: " + longitude);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        viewModel = new AddProjectActivityViewModel(this, address, latitude, longitude, projectDomain);

        binding.setViewModel(viewModel);
    }

}
