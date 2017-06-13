package com.lecet.app.content;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectsNearMeBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.LocationManager;
import com.lecet.app.viewmodel.ProjectsNearMeViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ProjectsNearMeActivity extends LecetBaseActivity implements OnMapReadyCallback
        , LocationManager.LocationManagerListener, LecetConfirmDialogFragment.ConfirmDialogListener  {

    public static final String EXTRA_MARKER_LATITUDE  = "com.lecet.app.content.ProjectsNearMeActivity.marker.latitude.extra";
    public static final String EXTRA_MARKER_LONGITUDE = "com.lecet.app.content.ProjectsNearMeActivity.marker.longitude.extra";
    public static final String EXTRA_ENABLE_LOCATION = "enable_location";
    public static final String EXTRA_ASKING_FOR_PERMISSION = "asking_for_permission";
    public static final String EXTRA_LOCATION_MANAGER_CONNECTED = "location_manager_connected";
    public static final int REQUEST_FILTER_MPN = 8;
    private static final int REQUEST_LOCATION_SETTINGS = 1;

    ActivityProjectsNearMeBinding binding;
    ProjectsNearMeViewModel viewModel;
    LocationManager locationManager;
    boolean enableLocationUpdates;
    boolean isAskingForPermission;
    boolean isLocationManagerConnected;
    Location lastKnowLocation;
    //Address lastKnownAddress;
    private final String TAG = "ProjectsNearMeActivity";
    ViewPager viewPager;
    TabLayout tabLayout;
    ProjectsNearMeActivity.ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableLocationUpdates = false;
        isAskingForPermission = false;
        isLocationManagerConnected = false;
        setupLocationManager();
        setupBinding();
        setupToolbar();
        checkPermissions();
        viewPager = (ViewPager) findViewById(R.id.view_pager_bid);
// set up TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_bid_detail);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
    }

    private void continueSetup() {
        setupMap();
    }

    private void setupLocationManager() {
        locationManager = new LocationManager(this, this);
        locationManager.handleOnStart();
    }

    private void setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_projects_near_me);
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        viewModel = new ProjectsNearMeViewModel(this, projectDomain, new Handler(), locationManager);
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
            viewModel.setToolbar(searchBarView);
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
        MapsInitializer.initialize(this);
        //map.setInfoWindowAdapter(new LecetInfoWindowAdapter(this));     // TODO - this is where the info_window_layout is set as the default info window for the map
        viewModel.setMap(map);
        updateLocation();
        fetchProjects(false);
    }

    private void updateLocation() {
        lastKnowLocation = locationManager.retrieveLastKnownLocation();
        Log.d(TAG, "updateLocation: lastKnowLocation: " + lastKnowLocation);

        /*if(lastKnowLocation != null) {
            lastKnownAddress = locationManager.getAddressFromLocation(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude());
            Log.d(TAG, "updateLocation: lastKnownAddress: " + lastKnownAddress);
        }*/
    }


    private void fetchProjects(boolean animateCamera) {

        if (!viewModel.isMapReady()) return;
try { //Adding try-catch block for any runtime exception occurred when GoogleApiClient is not connected yet.
    if (lastKnowLocation != null) {
        LatLng location = new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude());
        if (animateCamera) {
            viewModel.animateMapCamera(location);
        } else {
            viewModel.moveMapCamera(location);
        }
        viewModel.fetchProjectsNearMe(location);
    } else {
        enableLocationUpdates = true;
        if (isLocationManagerConnected) {
            locationManager.startLocationUpdates();
        }
    }
} catch (Exception e) {
    Log.d("fetchProject","fetchProject exception"+e.getMessage());
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
        isLocationManagerConnected = true;
        if (enableLocationUpdates) {
            locationManager.startLocationUpdates();
        }
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
            //lastKnownAddress = locationManager.getAddressFromLocation(location.getLatitude(), location.getLongitude());
            Log.d(TAG, "onLocationChanged: lastKnowLocation: " + lastKnowLocation);
            //Log.d(TAG, "onLocationChanged: lastKnownAddress: " + lastKnownAddress);
            locationManager.stopLocationUpdates();
            fetchProjects(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationManager.handleOnStart();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {
        if (isConnected) {
            fetchProjects(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Commenting the code due to producing crashes when going back to the activity.
       /* isLocationManagerConnected = false;
        locationManager.handleOnStop();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (enableLocationUpdates) {
            locationManager.startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.stopLocationUpdates();
    }

    private void checkPermissions() {
        if (locationManager.isLocationPermissionEnabled()) {
            checkGps();
        } else {
            showLocationPermissionRequiredDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//TODO: in future, each filter should have each request code and create a ticket to fogbugz - backlog,lecetv2 android .
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_SETTINGS) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkGps();
                }
            }, 500);
        }

        if (requestCode == REQUEST_FILTER_MPN) {
            Log.d("filtermpn", "filtermpn");
            processFilter(requestCode, data);
        }
    }

    ///*** Begin Filter Processing

    void processFilter(int requestCode, Intent data) {

        if (data == null) return;

        StringBuilder projectsSb = new StringBuilder();

        // PROJECT FILTERS
        String mpnLocation = "";
        // Project Location Filter e.g. {"projectLocation":{"city":"Brooklyn","state":"NY","county":"Kings","zip5":"11215"}}
        String projectLocationFilter = processProjectLocationFilter(data);
        if (projectLocationFilter.length() > 0) {
           // projectsSb.append(projectLocationFilter); //uncomment if mpnLocation will no longer be used.

            mpnLocation = projectLocationFilter.substring(projectLocationFilter.indexOf(':') + 2, projectLocationFilter.lastIndexOf('}'));
            mpnLocation = mpnLocation.replaceAll(":", " ");

            if (mpnLocation.contains("zip5")) {
                mpnLocation = mpnLocation.substring(mpnLocation.indexOf("zip5") - 1);
                mpnLocation = mpnLocation.replaceAll("\"zip5\"", "");
            } else {
                if (mpnLocation.contains("city")) {
                    mpnLocation = mpnLocation.replaceAll("\"city\"", "");
                }
                if (mpnLocation.contains("county")) {
                    mpnLocation = mpnLocation.replaceAll("\"county\"", "");
                }
                if (mpnLocation.contains("state")) {
                    mpnLocation = mpnLocation.replaceAll("\"state\"", "");
                }
            }
            mpnLocation = mpnLocation.replaceAll("\"", "");
            Log.d("mpnLocation", "mpnLocation:" + mpnLocation);
        }
        // Updated Within Filter
        String updatedWithinFilter = processUpdatedWithinFilter(data);
        if (updatedWithinFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(updatedWithinFilter);
        }

        // Stage Filter
        String stageFilter = processStageFilter(data);
        if (stageFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(stageFilter);
        }

        // Building-or-Highway Filter
        String buildingOrHighwayFilter = processBuildingOrHighwayFilter(data);
        if (buildingOrHighwayFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(buildingOrHighwayFilter);
        }

        // Owner Type Filter
        String ownerTypeFilter = processOwnerTypeFilter(data);
        if (ownerTypeFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(ownerTypeFilter);
        }

        // Work Type Filter
        String workTypeFilter = processWorkTypeFilter(data);
        if (workTypeFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(workTypeFilter);
        }


        // SEARCH FILTERS USED IN PROJECT Filter

        // Primary Project Type Filter e.g. {"type": {Engineering}}
        String primaryProjectTypeFilter = processPrimaryProjectTypeFilter(data);
        if (primaryProjectTypeFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(primaryProjectTypeFilter);

        }

        // Project ID Type Filter
        String projectTypeIdFilter = processProjectTypeIdFilter(data);
        if (projectTypeIdFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(projectTypeIdFilter);

        }

        // Value Filter
        String valueFilter = processValueFilter(data);
        if (valueFilter.length() > 0) {
            if ( valueFilter.contains("MAX")) {
                valueFilter = valueFilter.replace(",\"max\":MAX", "");
            }

            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(valueFilter);
        }

        // Jurisdiction Filter
        String jurisdictionFilter = processJurisdictionFilter(data);
        if (jurisdictionFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(jurisdictionFilter);

        }

        // Bidding Within Filter
        String biddingWithinFilter = processBiddingWithinFilter(data);
        if (biddingWithinFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(biddingWithinFilter);

        }

        // prepend searchFilter param if there are any filters used

        // project search
        if (projectsSb.length() > 0) {
            projectsSb.insert(0, ",\"searchFilter\":{");
            projectsSb.append("}");

        }
        //Final search filter result for Project
        String projectsCombinedFilter = projectsSb.toString();
        //String projectsSearchStr = "{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]" + projectsCombinedFilter + "}";
        String filterMPN = "{\"include\":[\"projectStage\",{\"contacts\":[\"company\"]}],\"limit\":200, \"order\":\"id DESC\" " + projectsCombinedFilter + "}";
        viewModel.setProjectFilter(filterMPN);
        String plocation = viewModel.getSearch().getText().toString();
        if (!plocation.isEmpty()) {
            viewModel.searchAddress(plocation);
        } else if (mpnLocation != null && !mpnLocation.isEmpty()) {
            viewModel.searchAddress(mpnLocation);
        } else {
            enableLocationUpdates = true;
            updateLocation();
            if (lastKnowLocation != null) {
                Log.d("mpn2", "mpn2" + lastKnowLocation.getLongitude() + ":" + lastKnowLocation.getLatitude());
                fetchProjects(true);
            }
            locationManager.startLocationUpdates();
        }
    }

    /**
     * Process the Project Location filter data
     * Ex: "projectLocation":{"city":"Brooklyn","county":"Kings","zip5":"11215"}
     */
    private String processProjectLocationFilter(Intent data) {
        String filter = "";
        String projectLocation = data.getStringExtra(SearchViewModel.FILTER_PROJECT_LOCATION);
        if (projectLocation != null && !projectLocation.equals("")) {
            Log.d(TAG, "onActivityResult: projectLocation: " + projectLocation);
            filter = projectLocation;
        }

        return filter;
    }


    /**
     * Process the Primary Project Type filter data
     */
    private String processPrimaryProjectTypeFilter(Intent data) {
        String filter = "";
        String projectType = data.getStringExtra(SearchViewModel.FILTER_PROJECT_TYPE);
        if (projectType != null && !projectType.equals("")) {
            Log.d(TAG, "onActivityResult: projectType: " + projectType);
            filter = projectType;
        }
        return filter;
    }

    /**
     * Process the Project Type IDs filter data
     * Ex: "projectTypeId":{"inq": [501, 502, 503]}
     */
    private String processProjectTypeIdFilter(Intent data) {
        String filter = "";
        String projectTypeIds = data.getStringExtra(SearchViewModel.FILTER_PROJECT_TYPE_ID);
        if (projectTypeIds != null && !projectTypeIds.equals("")) {
            Log.d(TAG, "onActivityResult: projectTypeIds: " + projectTypeIds);
            filter = projectTypeIds;
        }
        return filter;
    }

    /**
     * Process the $ Value filter data
     * Ex: "projectValue":{"min":555,"max":666}
     */
    private String processValueFilter(Intent data) {
        String filter = "";
        String projectValue = data.getStringExtra(SearchViewModel.FILTER_PROJECT_VALUE);
        if (projectValue != null && !projectValue.equals("")) {
            Log.d(TAG, "onActivityResult: projectValue: " + projectValue);
            filter = projectValue;
        }
        return filter;
    }

    /**
     * Process the Updated In Last filter data
     * Ex, using 12 months = 366 days: "updatedInLast":366
     */
    private String processUpdatedWithinFilter(Intent data) {
        String filter = "";
        String projectUpdatedWithin = data.getStringExtra(SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST);
        if (projectUpdatedWithin != null && !projectUpdatedWithin.equals("")) {
            Log.d(TAG, "onActivityResult: projectUpdatedWithin: " + projectUpdatedWithin);
            filter = projectUpdatedWithin;
        }
        return filter;
    }

    /**
     * Process the Jurisdictions filter data
     * Ex: "jurisdictions":{"inq":["Eastern","New Jersey","3"]}
     */
    private String processJurisdictionFilter(Intent data) {
        String filter = "";
        String jurisdiction = data.getStringExtra(SearchViewModel.FILTER_PROJECT_JURISDICTION);
        if (jurisdiction != null && !jurisdiction.equals("")) {
            Log.d(TAG, "onActivityResult: jurisdiction: " + jurisdiction);
            filter = jurisdiction;
        }
        return filter;
    }

    /**
     * Process the Stage filter data
     * Ex: "projectStageId":{"inq":[203, 201, 206]}
     */
    private String processStageFilter(Intent data) {
        String filter = "";
        String stage = data.getStringExtra(SearchViewModel.FILTER_PROJECT_STAGE);
        if (stage != null && !stage.equals("")) {
            Log.d(TAG, "onActivityResult: stage: " + stage);
            filter = stage;
        }
        return filter;
    }

    /**
     * Process the Bidding Within filter data
     * Ex, using 21 days: "biddingInNext":21
     */
    private String processBiddingWithinFilter(Intent data) {
        String filter = "";
        String projectBiddingWithin = data.getStringExtra(SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN);
        if (projectBiddingWithin != null && !projectBiddingWithin.equals("")) {
            Log.d(TAG, "onActivityResult: projectBiddingWithin: " + projectBiddingWithin);
            filter = projectBiddingWithin;
        }
        return filter;
    }

    /**
     * Process the Building-or-Highway filter data
     * Ex: "buildingOrHighway":{"inq":["B","H"]}
     */
    private String processBuildingOrHighwayFilter(Intent data) {
        String filter = "";
        String projectBuildingOrHighwayWithin = data.getStringExtra(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY);
        if (projectBuildingOrHighwayWithin != null && !projectBuildingOrHighwayWithin.equals("")) {
            Log.d(TAG, "onActivityResult: projectBuildingOrHighwayWithin: " + projectBuildingOrHighwayWithin);
            filter = projectBuildingOrHighwayWithin;
        }
        return filter;
    }

    /**
     * Process the Owner Type filter data
     * Ex: "ownerType":{"inq":["Federal"]}
     */
    private String processOwnerTypeFilter(Intent data) {
        String filter = "";
        String ownerType = data.getStringExtra(SearchViewModel.FILTER_PROJECT_OWNER_TYPE);
        if (ownerType != null && !ownerType.equals("")) {
            Log.d(TAG, "onActivityResult: ownerType: " + ownerType);
            filter = ownerType;
        }
        return filter;
    }

    /**
     * Process the Work Type filter data
     * Ex: "workTypeId":{"inq":["2"]}
     */
    private String processWorkTypeFilter(Intent data) {
        String filter = "";
        String workType = data.getStringExtra(SearchViewModel.FILTER_PROJECT_WORK_TYPE);
        if (workType != null && !workType.equals("")) {
            Log.d(TAG, "onActivityResult: workType: " + workType);
            filter = workType;
        }
        return filter;
    }

    ///*** end of Filter Processing

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
                        checkGps();
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
        outState.putBoolean(EXTRA_ASKING_FOR_PERMISSION, isAskingForPermission);
        outState.putBoolean(EXTRA_LOCATION_MANAGER_CONNECTED, isLocationManagerConnected);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        enableLocationUpdates = savedInstanceState.getBoolean(EXTRA_ENABLE_LOCATION);
        isAskingForPermission = savedInstanceState.getBoolean(EXTRA_ASKING_FOR_PERMISSION);
        isLocationManagerConnected = savedInstanceState.getBoolean(EXTRA_LOCATION_MANAGER_CONNECTED);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (isAskingForPermission) {
            isAskingForPermission = false;
            locationManager.requestLocationPermission();
        } else { //is asking to enable location
            openLocationSettings();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogCancel(DialogFragment dialog) {

    }

    private void checkGps() {
        if (locationManager.isGpsEnabled()) {
            continueSetup();
        } else {
            showLocationEnableRequired();
        }
    }

    private void showLocationPermissionRequiredDialog() {
        isAskingForPermission = true;
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

    private void openLocationSettings() {
        Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(gpsOptionsIntent, REQUEST_LOCATION_SETTINGS);
    }

    public void onNavigationPressed(View view) {
        enableLocationUpdates = true;
        updateLocation();
        if (lastKnowLocation != null) {
            fetchProjects(true);
        }
        locationManager.startLocationUpdates();
    }

    public void onBidTableViewPressed(View view) {
        Log.d(TAG, "onBidTableViewPressed");
        viewModel.setTableViewDisplay(!viewModel.getTableViewDisplay());
    }

    public void updateTableViewPager() {
        setupViewPager(viewPager);
        pagerAdapter.notifyDataSetChanged();
    }

    private void setupViewPager(ViewPager viewPager) {
        int preSize = 0;
        int postSize = 0;

        if (viewModel.getPrebidProjects()!= null) {
            preSize = viewModel.getPrebidProjects().size();
        }
        if (viewModel.getPostbidProjects()!= null) {
            postSize = viewModel.getPostbidProjects().size();
        }

        pagerAdapter = new ProjectsNearMeActivity.ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(PreBidFragment.newInstance(viewModel.getPrebidProjects()), getResources().getString(R.string.reg_pre_bid),preSize);
        pagerAdapter.addFragment(PostBidFragment.newInstance(viewModel.getPostbidProjects()), getResources().getString(R.string.reg_post_bid),postSize);
        viewPager.setAdapter(pagerAdapter);
    }
    /**
     * Inner ViewPagerAdapter class
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title, int size) {
            fragmentList.add(fragment);
            fragmentTitleList.add("     "+size+" "+title+"     ");
        }

        public List<Fragment> getFragmentList() {
            return fragmentList;
        }

        public List<String> getFragmentTitleList() {
            return fragmentTitleList;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    public ProjectsNearMeViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ProjectsNearMeViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
