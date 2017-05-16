package com.lecet.app.utility;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import com.lecet.app.service.GeofenceTransitionsIntentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * File: LocationManager Created: 8/26/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int LECET_LOCATION_PERMISSION_REQUEST = 1293;

    private final AppCompatActivity context;
    private final GoogleApiClient apiClient;
    private final LocationManagerListener listener;

    private LocationRequest mLocationRequest;
    private List<Geofence> mGeofences;

    public LocationManager(AppCompatActivity context, LocationManagerListener listener) {

        this.context = context;
        this.listener = listener;

        apiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Must be called in the Activity onStart
    public void handleOnStart() {
        if (!apiClient.isConnected() && !apiClient.isConnecting()) {
            apiClient.connect();
        }
    }

    // Must be called in the Activity onStop
    public void handleOnStop() {
        if (apiClient.isConnected() || apiClient.isConnecting()) {
            apiClient.disconnect();
        }
    }

    // GoogleApiClient.ConnectionCallbacks

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        listener.onConnected(connectionHint);
    }

    // GoogleApiClient.OnConnectionFailedListener

    @Override
    public void onConnectionSuspended(int i) {
        listener.onConnectionSuspended();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        listener.onConnectionFailed(connectionResult);
    }

    // LocationListener
    @Override
    public void onLocationChanged(Location location) {
        listener.onLocationChanged(location);
    }

    // Public

    /**
     * Permissions
     **/

    public boolean isLocationPermissionEnabled() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission() {

        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LECET_LOCATION_PERMISSION_REQUEST);
    }

    /** Reverse Address
     */

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(context, Locale.US);
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

    /**
     * Location
     **/
    public Location retrieveLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        return LocationServices.FusedLocationApi.getLastLocation(apiClient);
    }

    public void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mLocationRequest == null) {
            createLocationRequest();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        if (apiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    apiClient, this);
        }
    }

    public boolean isGpsEnabled() {
        android.location.LocationManager manager
                = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    /**
     * Geofencing
     **/

    private PendingIntent getGeofencePendingIntent() {

        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    protected void addGeofence(String requestKey, double latitude, double longitude,
                               long expirationInMilliseconds, float radius, ResultCallback<Status> callback) {

        if (mGeofences == null) {

            mGeofences = new ArrayList<>();
        }

        mGeofences.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(requestKey)
                .setCircularRegion(
                        latitude,
                        longitude,
                        radius
                )
                .setExpirationDuration(expirationInMilliseconds)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    protected void startGeofenceMonitoring(ResultCallback<Status> callback) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                apiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(callback);
    }

    protected void removeGeofence(PendingIntent pendingIntent, ResultCallback<Status> callback) {

        LocationServices.GeofencingApi.removeGeofences(
                apiClient,
                // This is the same pending intent that was used in addGeofences().
                pendingIntent
        ).setResultCallback(callback); // Result processed in onResult().
    }

    // Private

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofences);
        return builder.build();
    }

    public interface LocationManagerListener {

        void onConnected(@Nullable Bundle connectionHint);

        void onConnectionSuspended();

        void onConnectionFailed(@NonNull ConnectionResult connectionResult);

        void onLocationChanged(Location location);
    }

}
