package com.lecet.app.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lecet.app.R;
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
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));

        projectDomain.getProjectsNear(location.latitude, location.longitude, 10, new Callback<ProjectsNearResponse>() {
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
        for (Project project : projects) {
            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_marker))
                    .position(project.getGeocode().toLatLng())
                    .title("Marker"));
        }
    }

}
