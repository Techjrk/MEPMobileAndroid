package com.lecet.app.content;

import com.google.android.gms.common.ConnectionResult;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectAddNoteBinding;
import com.lecet.app.domain.LocationDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.LocationManager;
import com.lecet.app.viewmodel.ProjectAddNoteViewModel;

import io.realm.Realm;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectAddNoteActivity extends LecetBaseActivity  implements LocationManager.LocationManagerListener, LecetConfirmDialogFragment.ConfirmDialogListener {

    public static final String NOTE_ID_EXTRA    = "com.lecet.app.content.ProjectAddNoteActivity.note.id.extra";
    public static final String NOTE_TITLE_EXTRA = "com.lecet.app.content.ProjectAddNoteActivity.note.title.extra";
    public static final String NOTE_BODY_EXTRA  = "com.lecet.app.content.ProjectAddNoteActivity.note.body.extra";
    public static final String NOTE_EXTRA       = "com.lecet.app.content.ProjectAddNoteActivity.note.extra";

    private static final String TAG = "ProjectAddNoteAct";

    private long projectId;
    private long noteId;
    private String noteTitle;
    private String noteBody;
    private LocationManager locationManager;
    ProjectAddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        LocationDomain locationDomain = new LocationDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());

        projectId = getIntent().getLongExtra(PROJECT_ID_EXTRA, -1);

        // in the case of editing an existing note look for its id, title and body
        noteId    = getIntent().getLongExtra(NOTE_ID_EXTRA, -1);
        noteTitle = getIntent().getStringExtra(NOTE_TITLE_EXTRA);
        noteBody  = getIntent().getStringExtra(NOTE_BODY_EXTRA);

        Log.d(TAG, "onCreate: projectId: " + projectId);
        Log.d(TAG, "onCreate: noteId: " + noteId);
        Log.d(TAG, "onCreate: noteTitle: " + noteTitle);
        Log.d(TAG, "onCreate: noteBody: " + noteBody);

        setupLocationManager();
        setupBinding(projectDomain, locationDomain);
        checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        locationManager.startLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        locationManager.handleOnStop();
    }

    private void setupLocationManager() {
        Log.d(TAG, "setupLocationManager() called");
        locationManager = new LocationManager(this, this);
        locationManager.handleOnStart();
        Location m = locationManager.retrieveLastKnownLocation();
        Log.d(TAG, "setupLocationManager() called. Location: " + m);
    }

    private void setupBinding(ProjectDomain projectDomain, LocationDomain locationDomain) {
        Log.d(TAG, "setupBinding() called with: projectDomain = [" + projectDomain + "], locationDomain = [" + locationDomain + "]");
        ActivityProjectAddNoteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_note);
        viewModel = new ProjectAddNoteViewModel(this, projectId, noteId, noteTitle, noteBody, projectDomain, locationDomain);
        binding.setViewModel(viewModel);
    }

    private void checkPermissions() {
        if (locationManager.isLocationPermissionEnabled()) {
            checkGps();
        } else {
            showLocationPermissionRequiredDialog();
        }
    }

    private void checkGps() {
        if (!locationManager.isGpsEnabled()) {
            showLocationEnableRequired();
        }
    }

    private void showLocationPermissionRequiredDialog() {
        //isAskingForPermission = true;
        LecetConfirmDialogFragment dialogFragment = LecetConfirmDialogFragment.newInstance(getString(R.string.confirm_share_your_location_description)
                , getString(R.string.confirm_share_your_location), getString(android.R.string.cancel));

        dialogFragment.setCallbackListener(this);
        dialogFragment.show(getSupportFragmentManager(), LecetConfirmDialogFragment.TAG);
    }


    private void showLocationEnableRequired() {
        LecetConfirmDialogFragment dialogFragment = LecetConfirmDialogFragment.newInstance(getString(R.string.confirm_enable_your_location_description)
                , getString(R.string.confirm_go_to_settings), getString(android.R.string.cancel));

        dialogFragment.setCallbackListener(this);
        dialogFragment.show(getSupportFragmentManager(), LecetConfirmDialogFragment.TAG);
    }


    /*
     * LocationManager Listener methods
     */

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        Log.d(TAG, "onConnected() called with: connectionHint = [" + connectionHint + "]");
        locationManager.startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended() {
        Log.d(TAG, "onConnectionSuspended() called");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called with: connectionResult = [" + connectionResult + "]");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
        viewModel.handleLocationChanged(location);
        //locationManager.stopLocationUpdates();
    }


    //

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        locationManager.requestLocationPermission();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.e(TAG, "onDialogNegativeClick: USER DENIED LOCATION PERMISSIONS. NOTE POSTING STILL VIABLE.");
    }

    @Override
    public void onDialogCancel(DialogFragment dialog) {

    }
}
