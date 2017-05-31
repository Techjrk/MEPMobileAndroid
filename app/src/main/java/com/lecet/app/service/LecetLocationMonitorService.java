package com.lecet.app.service;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.request.ProjectNotifyRequest;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * File: LecetLocationMonitorService
 * Created: 5/15/17
 * Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class LecetLocationMonitorService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String NAME = "LecetLocationMonitorService";
    private static final long FAST_INTERVAL = 60000 * 5; // Five minutes
    private static final long SLOWER_INTERVAL = 6000 * 10; // Ten minutes
    private static final float DISPLACEMENT_METERS = 200.0f;

    private GoogleApiClient apiClient;
    private LocationRequest locationRequest;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LecetLocationMonitorService() {
        super(NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setFastestInterval(FAST_INTERVAL);
        locationRequest.setInterval(SLOWER_INTERVAL);
        locationRequest.setSmallestDisplacement(DISPLACEMENT_METERS);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // Begin requesting location updates. If permissions was terminated between request
        // and services handle intent. Don't do anything.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (apiClient.isConnected() || apiClient.isConnecting()) {
            apiClient.disconnect();
        }
    }

    // GoogleApiClient.ConnectionCallbacks

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    // GoogleApiClient.OnConnectionFailedListener

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Location Changed Listener

    @Override
    public void onLocationChanged(Location location) {

        LecetClient client = LecetClient.getInstance();
        Call<ResponseBody> call = client.getProjectService().projectNotify(new ProjectNotifyRequest(location.getLatitude(), location.getLongitude()));
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
