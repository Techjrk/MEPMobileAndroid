package com.lecet.app.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lecet.app.R;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;

import java.util.HashMap;
import java.util.List;

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

    private final Activity activity;
    private ProjectDomain projectDomain;
    private GoogleMap map;
    private Marker lastMarkerTapped;
    private HashMap<Long, Marker> markers;

    public ProjectsNearMeViewModel(Activity activity, ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectDomain = projectDomain;
        this.markers = new HashMap<>();
    }

    public void setMap(GoogleMap map) {
        this.map = map;
        this.map.setOnMarkerClickListener(this);
        this.map.setOnInfoWindowClickListener(this);
        this.map.setOnInfoWindowCloseListener(this);
        this.map.setOnCameraMoveListener(this);
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
        if (!activity.isFinishing() && !activity.isDestroyed()) {
            for (Project project : projects) {
                if (!markers.containsKey(project.getId())) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .title(Long.toString(project.getId()))
                            .infoWindowAnchor(5.4f, 5f)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_marker))
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
            lastMarkerTapped.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_marker));
        }

        lastMarkerTapped = marker;
        lastMarkerTapped.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_yellow_marker));

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //TODO open something? Campture the get direction tap?
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        if (lastMarkerTapped != null) {
            lastMarkerTapped.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_marker));
        }
        lastMarkerTapped = null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCameraMove() {
        fetchProjectsNearMe(map.getCameraPosition().target);
    }
}
