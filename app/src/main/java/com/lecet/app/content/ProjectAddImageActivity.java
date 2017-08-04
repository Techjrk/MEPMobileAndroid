package com.lecet.app.content;

import com.google.android.gms.common.ConnectionResult;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectAddImageBinding;
import com.lecet.app.domain.LocationDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.LocationManager;
import com.lecet.app.viewmodel.ProjectAddImageViewModel;

import io.realm.Realm;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectImageChooserActivity.PROJECT_REPLACE_IMAGE_EXTRA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.FROM_CAMERA;
import static com.lecet.app.content.ProjectTakeCameraPhotoFragment.IMAGE_PATH;
import static com.lecet.app.viewmodel.ProjectImageChooserViewModel.IMAGE_URI;
import static com.lecet.app.viewmodel.ProjectImageChooserViewModel.NEEDED_ROTATION;
import static com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel.REQUEST_CODE_NEW_IMAGE;
import static com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel.REQUEST_CODE_REPLACE_IMAGE;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectAddImageActivity extends LecetBaseActivity implements LocationManager.LocationManagerListener, LecetConfirmDialogFragment.ConfirmDialogListener {

    private static final String TAG = "ProjectAddImageAct";

    public static final String IMAGE_ID_EXTRA    = "com.lecet.app.content.ProjectAddImageActivity.image.id.extra";
    public static final String IMAGE_TITLE_EXTRA = "com.lecet.app.content.ProjectAddImageActivity.image.title.extra";
    public static final String IMAGE_BODY_EXTRA  = "com.lecet.app.content.ProjectAddImageActivity.image.body.extra";
    public static final String IMAGE_URL_EXTRA   = "com.lecet.app.content.ProjectAddImageActivity.image.url.extra";

    private long projectId;
    private long id;
    private String title;
    private String body;
    private boolean fromCamera;
    private String imageUri;
    private float neededRotation = 0;
    private String url;
    private String imagePath;
    private Uri selectedImageUri;
    private boolean replaceImage;
    private LocationManager locationManager;
    private ProjectAddImageViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        LocationDomain locationDomain = new LocationDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());

        // get project ID and image data for passing to the viewmodel
        Bundle extras = getIntent().getExtras();
        projectId = extras.getLong(PROJECT_ID_EXTRA, -1);
        replaceImage = extras.getBoolean(PROJECT_REPLACE_IMAGE_EXTRA, false);

        fromCamera = extras.getBoolean(FROM_CAMERA);

        neededRotation = extras.getFloat(NEEDED_ROTATION);
        imagePath = extras.getString(IMAGE_PATH);
        imageUri = extras.getString(IMAGE_URI);
        url = extras.getString(IMAGE_URL_EXTRA);

        // in the case of editing an existing image look for its id, title and body
        id = extras.getLong(IMAGE_ID_EXTRA, -1);
        title = extras.getString(IMAGE_TITLE_EXTRA);
        body = extras.getString(IMAGE_BODY_EXTRA);

        Log.d(TAG, "onCreate: projectId: " + projectId);
        Log.d(TAG, "onCreate: replaceImage: " + replaceImage);
        Log.d(TAG, "onCreate: fromCamera: " + fromCamera);
        Log.d(TAG, "onCreate: imagePath: " + imagePath);
        Log.d(TAG, "onCreate: imageUri: " + imageUri);
        Log.d(TAG, "onCreate: url: " + url);
        Log.d(TAG, "onCreate: id: " + id);
        Log.d(TAG, "onCreate: title: " + title);
        Log.d(TAG, "onCreate: body: " + body);

        parseImageSource();
        //setupLocationManager();
        setupBinding(projectDomain, locationDomain);

        // immediately start the image chooser activity
        if (!replaceImage) {
            startImageChooserActivity();
        }
    }

    private void parseImageSource() {
        if(fromCamera) {
            if (imagePath != null) {
                selectedImageUri = Uri.parse(imagePath);
            }
        }
        else {
            if (imageUri != null) {
                selectedImageUri = Uri.parse(imageUri);
            }
            else if (url != null) {
                selectedImageUri = Uri.parse(url);
            }
        }
    }

    private void startImageChooserActivity() {
        Log.d(TAG, "startImageChooserActivity");
        Intent intent = new Intent(this, ProjectImageChooserActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, projectId);
        intent.putExtra(IMAGE_ID_EXTRA, id);
        intent.putExtra(PROJECT_REPLACE_IMAGE_EXTRA, replaceImage);
        intent.putExtra(IMAGE_TITLE_EXTRA, title);
        intent.putExtra(IMAGE_BODY_EXTRA, body);
        if (replaceImage) {
            startActivityForResult(intent, REQUEST_CODE_REPLACE_IMAGE);
        }
        else {
            startActivityForResult(intent, REQUEST_CODE_NEW_IMAGE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        //locationManager.startLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        locationManager.handleOnStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        setupLocationManager();
        checkPermissions();
    }

    private void setupLocationManager() {
        Log.d(TAG, "setupLocationManager() called");
        if(locationManager == null ) {
            locationManager = new LocationManager(this, this);
        }
        locationManager.handleOnStart();
        locationManager.startLocationUpdates();
        //Location lastKnownLocation = locationManager.retrieveLastKnownLocation();
        //Log.d(TAG, "setupLocationManager() called. Location: " + lastKnownLocation);
    }

    private void setupBinding(ProjectDomain projectDomain, LocationDomain locationDomain) {
        ActivityProjectAddImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_add_image);
        if(!fromCamera) {
            viewModel = new ProjectAddImageViewModel(this, projectId, replaceImage, id, title, body, selectedImageUri, neededRotation, projectDomain, locationDomain);
        }
        else {
            if (imagePath != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, replaceImage, id, title, body, imagePath, projectDomain, locationDomain);
            }
            else if (imageUri != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, replaceImage, id, title, body, imageUri, projectDomain, locationDomain);
            }
            else if (url != null) {
                viewModel = new ProjectAddImageViewModel(this, projectId, replaceImage, id, title, body, url, projectDomain, locationDomain);
            }
        }
        binding.setViewModel(viewModel);
    }

    private void checkPermissions() {
        if (locationManager.isLocationPermissionEnabled()) {
            if (!locationManager.isGpsEnabled()) {
                showEnableLocationDialog();
            }
        } else {
            showLocationPermissionRequiredDialog();
        }
    }

    private void showLocationPermissionRequiredDialog() {
        LecetConfirmDialogFragment dialogFragment = LecetConfirmDialogFragment.newInstance(getString(R.string.confirm_share_your_location_description)
                , getString(R.string.confirm_share_your_location), getString(android.R.string.cancel));

        dialogFragment.setCallbackListener(this);
        dialogFragment.show(getSupportFragmentManager(), LecetConfirmDialogFragment.TAG);
    }


    private void showEnableLocationDialog() {
        LecetConfirmDialogFragment dialogFragment = LecetConfirmDialogFragment.newInstance(getString(R.string.confirm_enable_your_location_description)
                , getString(R.string.confirm_go_to_settings), getString(android.R.string.cancel));

        dialogFragment.setCallbackListener(this);
        dialogFragment.show(getSupportFragmentManager(), LecetConfirmDialogFragment.TAG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: resultCode: " + resultCode);
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode);

        if (resultCode == RESULT_OK) {
            String title       = data.getStringExtra(IMAGE_TITLE_EXTRA);
            String body        = data.getStringExtra(IMAGE_BODY_EXTRA);
            String imagePath   = data.getStringExtra(IMAGE_PATH);
            String imageUri    = data.getStringExtra(IMAGE_URI);
            boolean fromCamera = data.getBooleanExtra(FROM_CAMERA, false);
            neededRotation     = data.getFloatExtra(NEEDED_ROTATION, 0);

            Log.d(TAG, "onActivityResult: fromCamera: " + fromCamera);
            Log.d(TAG, "onActivityResult: imagePath: " + imagePath);
            Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
            Log.d(TAG, "onActivityResult: title: " + title);
            Log.d(TAG, "onActivityResult: body: " + body);

            this.imagePath = imagePath;
            this.imageUri = imageUri;
            this.fromCamera = fromCamera;

            // update the image path or URI as nec based on the results
            parseImageSource();

            // reinstantiate the binding and View Model
            ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
            LocationDomain locationDomain = new LocationDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
            setupBinding(projectDomain, locationDomain);
        }
        else {
            Log.d(TAG, "onActivityResult: canceling add image");
            finish();
        }

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
        if(location != null) {
            locationManager.stopLocationUpdates();
            viewModel.handleLocationChanged(location);
        }
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
