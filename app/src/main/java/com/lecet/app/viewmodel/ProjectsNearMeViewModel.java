package com.lecet.app.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lecet.app.R;
import com.lecet.app.content.widget.LacetInfoWindowAdapter;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Josué Rodríguez on 5/10/2016.
 */

public class ProjectsNearMeViewModel extends BaseObservable {

    private static final int DEFAULT_DISTANCE = 5;
    private static final int DEFAULT_ZOOM = 12;

    private final Activity activity;
    private ProjectDomain projectDomain;
    private GoogleMap map;

    public ProjectsNearMeViewModel(Activity activity, ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectDomain = projectDomain;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public void fetchProjectsNearMe(LatLng location) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));

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

    private void populateMap(List<Project> projects) {
        //TODO check if the activity is active
        for (Project project : projects) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .title(Long.toString(project.getId()))
                    .infoWindowAnchor(5.4f, 5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_marker))
                    .position(project.getGeocode().toLatLng()));
            marker.setTag(project);
        }
    }

}
