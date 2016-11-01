package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lecet.app.R;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Josué Rodríguez on 5/10/2016.
 */

public class ProjectsNearMeViewModel extends BaseObservable implements GoogleMap.OnMarkerClickListener
        , GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowCloseListener
        , View.OnClickListener, GoogleMap.OnCameraMoveListener {

    private static final int DEFAULT_DISTANCE = 3;
    private static final int DEFAULT_ZOOM = 12;

    private WeakReference<Activity> activity;
    private ProjectDomain projectDomain;
    private GoogleMap map;
    private Marker lastMarkerTapped;
    private HashMap<Long, Marker> markers;
    private ImageButton navigationButton;

    private BitmapDescriptor redMarker;
    private BitmapDescriptor greenMarker;
    private BitmapDescriptor yellowMarker;

    //Toolbar views
    private EditText search;
    private View buttonClear;
    private View buttonSearch;

    private Handler timer;

    public ProjectsNearMeViewModel(Activity activity, ProjectDomain projectDomain, Handler timer) {
        this.activity = new WeakReference<>(activity);
        this.projectDomain = projectDomain;
        this.markers = new HashMap<>();
        this.timer = timer;
    }

    public void setNavigationButton(ImageButton navigationButton) {
        this.navigationButton = navigationButton;
    }

    public void setMap(GoogleMap map) {
        this.redMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_red_marker);
        this.greenMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_green_marker);
        this.yellowMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_yellow_marker);
        this.map = map;
        this.map.setOnMarkerClickListener(this);
        this.map.setOnInfoWindowClickListener(this);
        this.map.setOnInfoWindowCloseListener(this);
        this.map.setOnCameraMoveListener(this);
    }

    public void setToolbar(View toolbar) {
        search = (EditText) toolbar.findViewById(R.id.search_entry);
        buttonClear = toolbar.findViewById(R.id.button_clear);
        buttonSearch = toolbar.findViewById(R.id.button_search);

        buttonClear.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);

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

    public void fetchProjectsNearMe(LatLng location) {
        projectDomain.getProjectsNear(location.latitude, location.longitude, DEFAULT_DISTANCE, new Callback<ProjectsNearResponse>() {
            @Override
            public void onResponse(Call<ProjectsNearResponse> call, Response<ProjectsNearResponse> response) {
                populateMap(response.body().getResults());
            }

            @Override
            public void onFailure(Call<ProjectsNearResponse> call, Throwable t) {

            }
        });
    }

    public void moveMapCamera(LatLng location) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
    }

    private void populateMap(List<Project> projects) {
        if (activity.get() != null && !activity.get().isFinishing() && !activity.get().isDestroyed()) {
            for (Project project : projects) {
                if (!markers.containsKey(project.getId())) {
                    BitmapDescriptor icon = project.getProjectStage().getId() == 102 ? redMarker : greenMarker;
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (lastMarkerTapped != null) {
            BitmapDescriptor icon = ((Project) marker.getTag()).getProjectStage().getId() == 102 ? redMarker : greenMarker;
            lastMarkerTapped.setIcon(icon);
        }

        lastMarkerTapped = marker;
        lastMarkerTapped.setIcon(yellowMarker);

        navigationButton.setVisibility(View.VISIBLE);

        return false;
    }

    public void onNavigationClicked(View view) {
        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s"
                , lastMarkerTapped.getPosition().latitude, lastMarkerTapped.getPosition().longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        activity.get().startActivity(mapIntent);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //TODO open something? Campture the get direction tap?
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        if (lastMarkerTapped != null) {
            BitmapDescriptor icon = ((Project) marker.getTag()).getProjectStage().getId() == 102 ? redMarker : greenMarker;
            lastMarkerTapped.setIcon(icon);
        }
        navigationButton.setVisibility(View.GONE);
        lastMarkerTapped = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_clear) { //the x in the search bar
            search.setText(null);
        } else if (id == R.id.button_search) {
            searchAddress(search.getText().toString());
        }
    }

    @Override
    public void onCameraMove() {
        timer.removeCallbacks(fetchProjectsOnMapStopMoving);
        timer.postDelayed(fetchProjectsOnMapStopMoving, 1000); //this is to prevent downloading projects while the user is moving the map
    }

    private Runnable fetchProjectsOnMapStopMoving = new Runnable() {
        @Override
        public void run() {
            fetchProjectsNearMe(map.getCameraPosition().target);
        }
    };

    public void searchAddress(String query) {
        if (!TextUtils.isEmpty(query)) {
            LatLng location = getLocationFromAddress(query);
            if (location == null) {
                Toast.makeText(activity.get(), R.string.error_fetching_address, Toast.LENGTH_SHORT).show();
            } else {
                moveMapCamera(location); //this will move and handle the download of projects
                fetchProjectsNearMe(location);
            }
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(activity.get(), Locale.US);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
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
}
