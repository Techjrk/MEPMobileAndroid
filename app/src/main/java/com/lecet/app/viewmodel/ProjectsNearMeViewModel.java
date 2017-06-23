package com.lecet.app.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.Bindable;
import android.graphics.Point;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lecet.app.R;
import com.lecet.app.content.AddProjectActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.ProjectsNearMeActivity;
import com.lecet.app.content.SearchFilterMPSActivity;
import com.lecet.app.content.widget.LecetInfoWindowAdapter;
import com.lecet.app.content.widget.LecetInfoWindowCreatePinAdapter;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.LocationManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LATITUDE;
import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LONGITUDE;
import static com.lecet.app.viewmodel.SearchViewModel.FILTER_INSTANT_SEARCH;

/**
 * Created by Josué Rodríguez on 5/10/2016.
 */

public class ProjectsNearMeViewModel extends BaseObservableViewModel implements GoogleMap.OnMarkerClickListener
        , GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowCloseListener
        , View.OnClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMyLocationChangeListener
        , GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCircleClickListener {

    private static final String TAG = "ProjectsNearMeViewModel";

    private static final int DEFAULT_DISTANCE = 3;
    private static final int DEFAULT_ZOOM = 15;
    private static final int REQUEST_FILTER_MPN = 8;
    private AppCompatActivity activity;
    private ProjectDomain projectDomain;
    private Handler timer;
    private LocationManager locationManager;
    private GoogleMap map;
    private Marker currLocationMarker;
    private Marker lastMarkerTapped;
    private HashMap<Long, Marker> markers;
    private LatLng currentLocation;
    private Circle locationCircle;
    private CircleOptions circleOptions;
    private boolean infoWindowOpen;
    private ArrayList<Project> prebidProjects;
    private ArrayList<Project> postbidProjects;
    private boolean tableViewDisplay;

    //Toolbar views
    private EditText search;
    private View buttonClear;
    private View buttonSearch;
    private View buttonFilter;

    // map marker assets
    private BitmapDescriptor standardMarkerStatic;
    private BitmapDescriptor standardMarkerStaticWithUpdate;
    private BitmapDescriptor standardMarkerPostBid;
    private BitmapDescriptor standardMarkerPostBidWithUpdate;
    private BitmapDescriptor standardMarkerPreBid;
    private BitmapDescriptor standardMarkerPreBidWithUpdate;
    private BitmapDescriptor standardMarkerSelected;
    private BitmapDescriptor standardMarkerSelectedWithUpdate;
    private BitmapDescriptor pinMarkerStatic;
    private BitmapDescriptor pinMarkerStaticWithUpdate;
    private BitmapDescriptor pinMarkerPreBid;
    private BitmapDescriptor pinMarkerPreBidWithUpdate;
    private BitmapDescriptor pinMarkerPostBid;
    private BitmapDescriptor pinMarkerPostBidWithUpdate;
    private BitmapDescriptor pinMarkerSelected;
    private BitmapDescriptor pinMarkerSelectedWithUpdate;


    public ProjectsNearMeViewModel(AppCompatActivity activity, ProjectDomain projectDomain, Handler timer, LocationManager locationManager) {
        super(activity);
        this.activity = activity;
        this.projectDomain = projectDomain;
        this.markers = new HashMap<>();
        this.timer = timer;
        this.locationManager = locationManager;
    }

    public void setProjectFilter(String filter) {
        projectDomain.setFilterMPN(filter);
    }

    public String getProjectFilter() {
        return projectDomain.getFilterMPN();
    }

    public EditText getSearch() {
        return search;
    }

    public void setSearch(EditText search) {
        this.search = search;
    }

    public void setMap(GoogleMap map) {

        initMarkerAssets();

        this.map = map;
        this.map.setOnMarkerClickListener(this);
        this.map.setOnInfoWindowClickListener(this);
        this.map.setOnInfoWindowCloseListener(this);
        this.map.setOnCameraMoveListener(this);
        this.map.setOnMyLocationChangeListener(this);
        this.map.setOnMyLocationButtonClickListener(this);
        this.map.setOnMapClickListener(this);
        this.map.setOnMapLongClickListener(this);
        this.map.setOnCircleClickListener(this);

        AppCompatActivity activity = getActivityWeakReference().get();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.map.setMyLocationEnabled(true);
    }

    private void initMarkerAssets() {
        // standard project markers
        this.standardMarkerStatic             = BitmapDescriptorFactory.fromResource(R.drawable.ic_standard_marker_static);
        this.standardMarkerStaticWithUpdate   = BitmapDescriptorFactory.fromResource(R.drawable.ic_standard_marker_static_update);
        this.standardMarkerPreBid             = BitmapDescriptorFactory.fromResource(R.drawable.ic_standard_marker_pre_bid);
        this.standardMarkerPreBidWithUpdate   = BitmapDescriptorFactory.fromResource(R.drawable.ic_standard_marker_pre_bid_update);
        this.standardMarkerPostBid            = BitmapDescriptorFactory.fromResource(R.drawable.ic_standard_marker_post_bid);
        this.standardMarkerPostBidWithUpdate  = BitmapDescriptorFactory.fromResource(R.drawable.ic_standard_marker_post_bid_update);
        this.standardMarkerSelected           = BitmapDescriptorFactory.fromResource(R.drawable.ic_standard_marker_selected);
        this.standardMarkerSelectedWithUpdate = BitmapDescriptorFactory.fromResource(R.drawable.ic_standard_marker_selected_update);

        // custom (user-created) pin markers
        this.pinMarkerStatic                  = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker_static);
        this.pinMarkerStaticWithUpdate        = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker_static_update);
        this.pinMarkerPreBid                  = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker_pre_bid);
        this.pinMarkerPreBidWithUpdate        = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker_pre_bid_update);
        this.pinMarkerPostBid                 = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker_post_bid);
        this.pinMarkerPostBidWithUpdate       = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker_post_bid_update);
        this.pinMarkerSelected                = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker_selected);
        this.pinMarkerSelectedWithUpdate      = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker_selected_update);
    }

    private void updateLocationCircle(GoogleMap map, LatLng latLng) {
        //Log.d(TAG, "updateLocationCircle: " + latLng);
        circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(25);
        circleOptions.strokeColor(R.color.transparent);
        circleOptions.fillColor(R.color.transparent);

        //remove old circle
        if(locationCircle != null) {
            locationCircle.remove();
            locationCircle = null;
        }
        currentLocation = latLng;
        locationCircle = map.addCircle(circleOptions);
        locationCircle.setCenter(latLng);
        locationCircle.setClickable(true);
        locationCircle.setVisible(false);
    }

    @Override
    public void onMyLocationChange(Location location) {
        //Log.d(TAG, "onMyLocationChange: " + location);
        updateLocationCircle(this.map, new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onCircleClick(Circle circle) {
        Log.d(TAG, "onCircleClick: " + circle);
        if(circle.equals(locationCircle)) {
            Log.d(TAG, "locationCircle: locationCircle clicked");
            //placeMapMarker(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

            if(currLocationMarker != null){
                onMarkerClick(currLocationMarker);
            }
            else{
                placeCustomPin(currentLocation , true);
            }
        }
    }

    private void clearLocationCircle(Circle circle) {
        circle.remove();
        circle = null;
    }

    private void placeCustomPin(LatLng latLng, boolean labelForMyLocation) {
        Log.d(TAG, "placeCustomPin: " + latLng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(pinMarkerStatic);
        if(labelForMyLocation) {
            markerOptions.title(activity.getString(R.string.my_location));
            map.setInfoWindowAdapter(new LecetInfoWindowCreatePinAdapter(activity, true));
        }
        else {
            markerOptions.title("");
            map.setInfoWindowAdapter(new LecetInfoWindowCreatePinAdapter(activity, false));
        }
        clearCurrLocationMarker();
        currLocationMarker = map.addMarker(markerOptions);
        bounceMarker(currLocationMarker);
    }

    private BitmapDescriptor getMarkerIcon(Project project, boolean selected) {

        boolean hasUpdates = projectHasUpdates(project);

        // for standard projects, i.e. with Dodge numbers
        if(project.getDodgeNumber() != null) {

            if(selected) {
                return hasUpdates ? standardMarkerSelectedWithUpdate : standardMarkerSelected;
            }

            if (project.getProjectStage() == null) {
                return hasUpdates ? standardMarkerPreBidWithUpdate : standardMarkerPreBid;
            }

            // style marker for pre-bid or post-bid color
            else {
                if (project.getProjectStage().getParentId() == 102) {
                    return hasUpdates ? standardMarkerPreBidWithUpdate : standardMarkerPreBid;
                }
                else {
                    return hasUpdates ? standardMarkerPostBidWithUpdate : standardMarkerPostBid;
                }
            }
        }

        // for custom (user-created) projects, which have no Dodge number
        else {

            if(selected) {
                return hasUpdates ? pinMarkerSelectedWithUpdate : pinMarkerSelected;
            }

            // pre-bid user-created projects
            if(project.getProjectStage() == null) {
                return hasUpdates ? pinMarkerPreBidWithUpdate : pinMarkerPreBid;
            }

            // post-bid user-created projects
            else {
                if(project.getProjectStage().getParentId() == 102) {
                    return hasUpdates ? pinMarkerPreBidWithUpdate : pinMarkerPreBid;
                }
                else {
                    return hasUpdates ? pinMarkerPostBidWithUpdate : pinMarkerPostBid;
                }
            }
        }

    }

    private boolean projectHasUpdates(Project project) {
        if(project == null) return false;
        if(project.getUserNotes() == null || project.getImages() == null) {
            return false;
        }
        return (project.getImages().size() > 0 || project.getUserNotes().size() > 0);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick: " + latLng);
       /* if (!infoWindowOpen) {
            clearCurrLocationMarker();
            placeCustomPin(latLng, true);
        }
        else infoWindowOpen = false;*/

        clearCurrLocationMarker();
        placeCustomPin(latLng, true);
        updateLocationCircle(map , latLng);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng);
        //clearCurrLocationMarker();
    }

    private void clearCurrLocationMarker() {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
            currLocationMarker = null;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.d(TAG, "onMyLocationButtonClick: *** (this is the UI button, not the blue dot)");
        return false;
    }


    public void setToolbar(View toolbar) {
        search = (EditText) toolbar.findViewById(R.id.search_entry);
        buttonClear = toolbar.findViewById(R.id.button_clear);
        buttonSearch = toolbar.findViewById(R.id.button_search);
        buttonFilter = toolbar.findViewById(R.id.button_filter);

        buttonClear.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonFilter.setOnClickListener(this);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    search.setText(search.getText().toString().trim());
                    onClick(buttonSearch);
                }

                return false;
            }
        });

    }

    public boolean isMapReady() {

        return map != null;
    }

    public void fetchProjectsNearMe(final LatLng location) {

        showProgressDialog();

        projectDomain.getProjectsNear(location.latitude, location.longitude, DEFAULT_DISTANCE, new Callback<ProjectsNearResponse>() {
            @Override
            public void onResponse(Call<ProjectsNearResponse> call, Response<ProjectsNearResponse> response) {

                // Activity dead
                if (!isActivityAlive()) return;

                if (response.isSuccessful()) {

                    List<Project> projects = response.body().getResults();

                    // Clear map markers

                    populateMap(projects);

                    dismissProgressDialog();
                    ((ProjectsNearMeActivity) activity).updateTableViewPager();
                    if (projects == null || projects.size() == 0) {

                        showCancelAlertDialog("", getActivityWeakReference().get().getString(R.string.no_projects_found));
                    }

                    //placeMapMarker(location);   //TODO - move to method responding to map press. only works on success

                } else {

                    dismissAlertDialog();
                    showCancelAlertDialog(getActivityWeakReference().get().getString(R.string.app_name), response.message());
                }

            }

            @Override
            public void onFailure(Call<ProjectsNearResponse> call, Throwable t) {

                // Activity dead
                if (!isActivityAlive()) return;

                dismissProgressDialog();
                showCancelAlertDialog(getActivityWeakReference().get().getString(R.string.error_network_title), getActivityWeakReference().get().getString(R.string.error_network_message));
            }
        });
    }

    public void moveMapCamera(LatLng location) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
    }

    public void animateMapCamera(LatLng location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
    }

    private void populateMap(List<Project> projects) {
        if (isActivityAlive()) {
            if (prebidProjects == null || postbidProjects == null) {
                prebidProjects = new ArrayList<>();
                postbidProjects = new ArrayList<>();
            } else {
                prebidProjects.clear();
                postbidProjects.clear();
            }

            // Clear existing markers
            map.clear();
            markers.clear();

            PointF infoWindowAnchorPos;

            for (Project project : projects) {
                if (!markers.containsKey(project.getId())) {

                    // set the map marker style
                    BitmapDescriptor icon = getMarkerIcon(project, false);

                    // define as pre- or post-bid
                    if (project.getProjectStage() == null) {
                        prebidProjects.add(project);
                    }
                    else {
                        if (project.getProjectStage().getParentId() == 102) {
                            prebidProjects.add(project);
                        } else {
                            postbidProjects.add(project);
                        }
                    }

                    // set the position of the info window
                    infoWindowAnchorPos = getInfoWindowAnchorPosition(project);

                    // add the marker to the map, add the Project as its Tag, and add the marker to a list
                    Marker marker = map.addMarker(new MarkerOptions()
                            .infoWindowAnchor(infoWindowAnchorPos.x, infoWindowAnchorPos.y)
                            .icon(icon)
                            .position(project.getGeocode().toLatLng()));
                    marker.setTag(project);
                    markers.put(project.getId(), marker);
                }
            }
        }
    }

    private PointF getInfoWindowAnchorPosition(Project project) {

        PointF defaultPos = new PointF(5.4f, 5f);
        PointF customPos  = new PointF(8.3f, 6.0f);

        if(project.getDodgeNumber() != null) {
            return defaultPos;
        }
        else return customPos;
    }

    /*private void placeMapMarker(LatLng location) {
        Log.d(TAG, "placeMapMarker");
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getActivityWeakReference().get().getString(R.string.my_location));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = map.addMarker(markerOptions);
    }*/

    public void onMapClick(View view) {
        Log.d(TAG, "onMapClick: " + view.getId());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick: " + marker.getTag());

        BitmapDescriptor icon;
        BitmapDescriptor lastMarkerTappedIcon;
        Project project;

        //boolean isMyLocationMarker = (marker.equals(currLocationMarker));

        // my location marker, which uses its own info window adapter and layout
        if (marker.getTag() == null) {
            map.setInfoWindowAdapter(new LecetInfoWindowCreatePinAdapter(activity, true));
        }

        // fetched project markers
        else {
            map.setInfoWindowAdapter(new LecetInfoWindowAdapter(activity));

            project = (Project) marker.getTag();
            icon = getMarkerIcon(project, true);
            marker.setIcon(icon);

            // reset the last marker selected back to its normal state
            if (lastMarkerTapped != null) {
                project = (Project) lastMarkerTapped.getTag();
                lastMarkerTappedIcon = getMarkerIcon(project, false);
                lastMarkerTapped.setIcon(lastMarkerTappedIcon);
            }

            lastMarkerTapped = marker;
        }
        if(!marker.isInfoWindowShown())
        {
            marker.showInfoWindow();
        }
        return false;
    }

    private void bounceMarker(final Marker marker) {
        Log.d(TAG, "bounceMarker: " + marker.getTitle());

        //Make the marker bounce
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final long duration = 1000;
        final int yOffset = -500;

        Projection proj = map.getProjection();
        final LatLng markerLatLng = marker.getPosition();
        Point startPoint = proj.toScreenLocation(markerLatLng);
        startPoint.offset(0, yOffset);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void onNavigationClicked(View view) {

        //TODO center map on current location
        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s", lastMarkerTapped.getPosition().latitude, lastMarkerTapped.getPosition().longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        getActivityWeakReference().get().startActivity(mapIntent);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(TAG, "onInfoWindowClick: marker: " + marker.getTitle());

        Activity context = getActivityWeakReference().get();

        if (context != null) {

            if(marker.getTitle() != null && marker.getTitle().equals(context.getString(R.string.my_location))) {
                Log.d(TAG, "onInfoWindowClick: marker position: " + marker.getPosition());
                Log.d(TAG, "onInfoWindowClick: marker lat: " + marker.getPosition().latitude);
                Log.d(TAG, "onInfoWindowClick: marker lng: " + marker.getPosition().longitude);
                Log.d(TAG, "onInfoWindowClick: context: " + context);
                Intent intent = new Intent(context, AddProjectActivity.class);
                //intent.putExtra(EXTRA_MARKER_ADDRESS, "55 Broadway, New York NY 10006");
                intent.putExtra(EXTRA_MARKER_LATITUDE, marker.getPosition().latitude);
                intent.putExtra(EXTRA_MARKER_LONGITUDE, marker.getPosition().longitude);
                context.startActivity(intent);
            }

            else {
                Project project = (Project) marker.getTag();
                Intent intent = new Intent(context, ProjectDetailActivity.class);
                intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, project.getId());
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        if (lastMarkerTapped != null) {
            Project project = (Project) lastMarkerTapped.getTag();
            BitmapDescriptor icon = getMarkerIcon(project, false);
            lastMarkerTapped.setIcon(icon);
        }
        lastMarkerTapped = null;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: view id: " + v.getId());

        int id = v.getId();

        if (id == R.id.button_clear) { //the x in the search bar
            search.setText(null);
        }
        else if (id == R.id.button_search) {
            setProjectFilter("default");
            searchAddress(search.getText().toString());
            //  ((ProjectsNearMeActivity) activity).setupViewPager();
        } else if (id == R.id.button_filter) {
            //search.setText(null);
            setProjectFilter("default");
            Intent intent = new Intent(getActivityWeakReference().get(), SearchFilterMPSActivity.class);
            intent.putExtra(FILTER_INSTANT_SEARCH, false);
            intent.putExtra(activity.getString(R.string.using_project_near_me),true);
            Activity activity = getActivityWeakReference().get();
            activity.startActivityForResult(intent, REQUEST_FILTER_MPN);
        }
    }

    @Override
    public void onCameraMove() {
        //timer.removeCallbacks(fetchProjectsOnMapStopMoving);
        //timer.postDelayed(fetchProjectsOnMapStopMoving, 1000); //this is to prevent downloading projects while the user is moving the map
    }

    private Runnable fetchProjectsOnMapStopMoving = new Runnable() {
        @Override
        public void run() {
            fetchProjectsNearMe(map.getCameraPosition().target);
        }
    };

    public void searchAddress(String query) {
        Log.d(TAG, "searchAddress: " + query);
        if (!TextUtils.isEmpty(query)) {
            LatLng location = getLocationFromAddress(query);
            if (location == null) {
                Log.e(TAG, "searchAddress: No location found for: " + query);
                Toast.makeText(getActivityWeakReference().get(), R.string.error_fetching_address, Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "searchAddress: Location found from address. Location: " + location);
                moveMapCamera(location); //this will move and handle the download of projects
                fetchProjectsNearMe(location);
            }
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Log.d(TAG, "getLocationFromAddress: " + strAddress);

        Geocoder coder = new Geocoder(getActivityWeakReference().get(), Locale.US);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null || address.size() == 0) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            Log.e(TAG, "getLocationFromAddress: Error. " + e.getMessage());
        }
        return p1;
    }

    @Bindable
    public ArrayList<Project> getPrebidProjects() {
        return prebidProjects;
    }

    public void setPrebidProjects(ArrayList<Project> prebidProjects) {
        this.prebidProjects = prebidProjects;
    }

    @Bindable
    public ArrayList<Project> getPostbidProjects() {
        return postbidProjects;
    }

    public void setPostbidProjects(ArrayList<Project> postbidProjects) {
        this.postbidProjects = postbidProjects;
    }

    @Bindable
    public boolean getTableViewDisplay() {
        return tableViewDisplay;
    }

    public void setTableViewDisplay(boolean tableViewDisplay) {
        this.tableViewDisplay = tableViewDisplay;
        notifyPropertyChanged(BR.tableViewDisplay);
    }

}
