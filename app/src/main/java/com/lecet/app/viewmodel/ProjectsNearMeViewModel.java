package com.lecet.app.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.Bindable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lecet.app.R;
import com.lecet.app.content.AddProjectActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.SearchFilterMPSActivity;
import com.lecet.app.content.widget.LecetInfoWindowAdapter;
import com.lecet.app.content.widget.LecetInfoWindowCreatePinAdapter;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_ADDRESS;
import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LATITUDE;
import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LONGITUDE;
import static com.lecet.app.viewmodel.SearchViewModel.FILTER_INSTANT_SEARCH;

/**
 * Created by Josué Rodríguez on 5/10/2016.
 */

public class ProjectsNearMeViewModel extends BaseObservableViewModel implements GoogleMap.OnMarkerClickListener
        , GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowCloseListener
        , View.OnClickListener, GoogleMap.OnCameraMoveListener {

    private static final String TAG = "ProjectsNearMeViewModel";

    private static final int DEFAULT_DISTANCE = 3;
    private static final int DEFAULT_ZOOM = 15;
    private static final int REQUEST_FILTER_MPN = 8;
    private AppCompatActivity activity;
    private ProjectDomain projectDomain;
    private GoogleMap map;
    private Marker currLocationMarker;
    private Marker lastMarkerTapped;
    private HashMap<Long, Marker> markers;

    private BitmapDescriptor redMarker;
    private BitmapDescriptor greenMarker;
    private BitmapDescriptor yellowMarker;
    private BitmapDescriptor currentLocationMarker;

    //Toolbar views
    private EditText search;
    private View buttonClear;
    private View buttonSearch;
    private View buttonFilter;

    private Handler timer;

    public ProjectsNearMeViewModel(AppCompatActivity activity, ProjectDomain projectDomain, Handler timer) {
        super(activity);
        this.activity = activity;
        this.projectDomain = projectDomain;
        this.markers = new HashMap<>();
        this.timer = timer;
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
        this.redMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_red_marker);
        this.greenMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_green_marker);
        this.yellowMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_yellow_marker);
        this.currentLocationMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin_marker);
        this.map = map;
        this.map.setOnMarkerClickListener(this);
        this.map.setOnInfoWindowClickListener(this);
        this.map.setOnInfoWindowCloseListener(this);
        this.map.setOnCameraMoveListener(this);

        AppCompatActivity activity = getActivityWeakReference().get();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.map.setMyLocationEnabled(true);
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

        showProgressDialog("", getActivityWeakReference().get().getString(R.string.updating));

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

                    if (projects == null || projects.size() == 0) {

                        showCancelAlertDialog("", getActivityWeakReference().get().getString(R.string.no_projects_found));
                    }

                    placeMapMarker(location);   //TODO - move to method responding to map press. only works on success

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
            if (prebid == null) {
                prebid = new ArrayList<Project>();
                postbid = new ArrayList<Project>();
            } else {
                prebid.clear(); postbid.clear();
            }

            // Clear existing markers
            map.clear();
            markers.clear();

            for (Project project : projects) {
                if (!markers.containsKey(project.getId())) {

                    BitmapDescriptor icon;

                    if (project.getProjectStage() == null) {
                        icon = greenMarker; prebid.add(project);

                    } else {
                        if (project.getProjectStage().getParentId() == 102) {
                            prebid.add(project);
                            icon = greenMarker;
                        } else {
                            postbid.add(project); icon =  redMarker;
                        }
                    }

                    Marker marker = map.addMarker(new MarkerOptions()
                            .infoWindowAnchor(5.4f, 5f)
                            .icon(icon)
                            .position(project.getGeocode().toLatLng()));
                    marker.setTag(project);
                    markers.put(project.getId(), marker);
                }
            }
        }
    }

    private void placeMapMarker(LatLng location) {
        Log.d(TAG, "placeMapMarker");
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getActivityWeakReference().get().getString(R.string.my_location));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = map.addMarker(markerOptions);
    }

    public void onMapClick(View view) {
        Log.d(TAG, "onMapClick: " + view.getId());
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick: " + marker.getTitle());

        BitmapDescriptor icon;

        boolean isMyLocationMarker = false;
        if(marker != null && marker.getTitle() != null) {
            isMyLocationMarker = marker.getTitle().equals(getActivityWeakReference().get().getString(R.string.my_location));
        }

        // my location marker, which uses its own info window adapter and layout
        if (isMyLocationMarker) {
            map.setInfoWindowAdapter(new LecetInfoWindowCreatePinAdapter(activity));
            icon = currentLocationMarker;
            if (lastMarkerTapped != null) {
                lastMarkerTapped.setIcon(icon);
            }
        }

        // fetched project markers
        else {
            map.setInfoWindowAdapter(new LecetInfoWindowAdapter(activity));

            if (lastMarkerTapped != null) {

                Project project = (Project) lastMarkerTapped.getTag();

                if (project.getProjectStage() == null) {
                    icon = greenMarker;
                } else {
                    icon = project.getProjectStage().getParentId() == 102 ? greenMarker : redMarker;
                }

                lastMarkerTapped.setIcon(icon);
            }

            lastMarkerTapped = marker;
            lastMarkerTapped.setIcon(yellowMarker);

        }

        return false;
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

            // TODO - add case for going to New Project Activity here, based on the ProjectDetailActivity layout *********************
            if(marker.getTitle() != null && marker.getTitle().equals(context.getString(R.string.my_location))) {
                Intent intent = new Intent(context, AddProjectActivity.class);
                intent.putExtra(EXTRA_MARKER_ADDRESS, "55 Broadway, New York NY 10006");   //TODO - hardcoded
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

            BitmapDescriptor icon;

            if (project.getProjectStage() == null) {
                icon = greenMarker;
            } else {
                icon = project.getProjectStage().getParentId() == 102 ? greenMarker : redMarker;
            }

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
        } else if (id == R.id.button_search) {
            setProjectFilter("default");
            searchAddress(search.getText().toString());
        } else if (id == R.id.button_filter) {
            search.setText(null);
            setProjectFilter("default");
            Intent intent = new Intent(getActivityWeakReference().get(), SearchFilterMPSActivity.class);
            intent.putExtra(FILTER_INSTANT_SEARCH, false);
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
                Toast.makeText(getActivityWeakReference().get(), R.string.error_fetching_address, Toast.LENGTH_SHORT).show();
            } else {
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
        }
        return p1;
    }
    List<Project> prebid, postbid;

    public List<Project> getPrebid() {
        return prebid;
    }

    public void setPrebid(List<Project> prebid) {
        this.prebid = prebid;
    }

    public List<Project> getPostbid() {
        return postbid;
    }

    public void setPostbid(List<Project> postbid) {
        this.postbid = postbid;
    }

    private boolean tableViewDisplay;

    @Bindable
    public boolean getTableViewDisplay() {
        return tableViewDisplay;
    }

    public void setTableViewDisplay(boolean tableViewDisplay) {
        this.tableViewDisplay = tableViewDisplay;
        notifyPropertyChanged(BR.tableViewDisplay);
    }
}
