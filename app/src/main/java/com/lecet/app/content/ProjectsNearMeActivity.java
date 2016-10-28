package com.lecet.app.content;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.lecet.app.R;
import com.lecet.app.content.widget.LacetInfoWindowAdapter;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectsNearMeBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.LocationManager;
import com.lecet.app.viewmodel.ProjectsNearMeViewModel;

import io.realm.Realm;

public class ProjectsNearMeActivity extends AppCompatActivity implements OnMapReadyCallback
        , LocationManager.LocationManagerListener, LacetConfirmDialogFragment.ConfirmDialogListener {

    public static final String EXTRA_ENABLE_LOCATION = "enable_location";

    ActivityProjectsNearMeBinding binding;
    ProjectsNearMeViewModel viewModel;
    LocationManager locationManager;
    boolean enableLocationUpdates;
    Location lastKnowLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableLocationUpdates = false;
        setupBinding();
        setupToolbar();


        setupLocationManager();

//        LacetConfirmDialogFragment.newInstance(getString(R.string.confirm_share_your_location_description)
//                , getString(R.string.confirm_share_your_location), getString(R.string.confirm_cancel)).show(getSupportFragmentManager(), "jfkdlskjfds");

        checkPermissions();
    }

    private void continueSetup() {
        setupMap();
    }

    private void setupLocationManager() {
        locationManager = new LocationManager(this, this);
    }

    private void setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_projects_near_me);
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        viewModel = new ProjectsNearMeViewModel(this, projectDomain);
        binding.setViewModel(viewModel);

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();
            View searchBarView = inflater.inflate(R.layout.projects_near_me_search_bar_layout, null);
            actionBar.setCustomView(searchBarView);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    private void setupMap() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map_container, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setInfoWindowAdapter(new LacetInfoWindowAdapter(this));
        viewModel.setMap(map);

        lastKnowLocation = locationManager.retrieveLastKnownLocation();
        fetchProjects();
    }

    private void fetchProjects() {
        if (lastKnowLocation != null) {
            LatLng location = new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude());
            viewModel.moveMapCamera(location);
            viewModel.fetchProjectsNearMe(location);
        } else {
            enableLocationUpdates = true;
            locationManager.startLocationUpdates();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {

    }

    @Override
    public void onConnectionSuspended() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            enableLocationUpdates = false;
            lastKnowLocation = location;
            locationManager.stopLocationUpdates();
            fetchProjects();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationManager.handleOnStart();
        if (enableLocationUpdates) {
            locationManager.startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.handleOnStop();
        locationManager.stopLocationUpdates();
    }

    private void checkPermissions() {
        if (locationManager.isLocationPermissionEnabled()) {
            continueSetup();
        } else {
            locationManager.requestLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locationManager.LECET_LOCATION_PERMISSION_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        continueSetup();
                    }
                }, 500);
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                finish();
            }
            return;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_ENABLE_LOCATION, enableLocationUpdates);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        enableLocationUpdates = savedInstanceState.getBoolean(EXTRA_ENABLE_LOCATION);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
