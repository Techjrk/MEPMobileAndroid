package com.lecet.app.contentbase;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * File: BaseMapObservableViewModel
 * Created: 5/17/17
 * Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class BaseMapObservableViewModel extends BaseObservableViewModel implements OnMapReadyCallback {

    // Maps
    private GoogleMap map;

    OnBaseViewModelMapReady listener;

    public BaseMapObservableViewModel(AppCompatActivity appCompatActivity, @Nullable GoogleMapOptions googleMapOptions, @IdRes int containerId) {
        super(appCompatActivity);
        setupMap(googleMapOptions, containerId);
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setListener(OnBaseViewModelMapReady listener) {
        this.listener = listener;
    }

    private void setupMap(@Nullable GoogleMapOptions googleMapOptions, @IdRes int containerId) {

        AppCompatActivity activity = getActivityWeakReference().get();
        SupportMapFragment mapFragment;

        if (googleMapOptions != null) {

            mapFragment = SupportMapFragment.newInstance(googleMapOptions);

        } else {

            mapFragment = SupportMapFragment.newInstance();
        }

        activity.getSupportFragmentManager().beginTransaction().add(containerId, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    /**
     * Clear SupportMapFragment's markers.
     */
    public void clearMap() {
        map.clear();
    }

    /**
     * Add a Marker to a Google Map
     *
     * @param drawableResId drawable resource id that will be used to create BitMapDescriptor as an
     *                      icon
     * @param latLng        the latitude and longitude values object as required for Google Maps
     */
    public void addMarker(@DrawableRes int drawableResId, @NonNull LatLng latLng) {

        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(drawableResId))
                .position(latLng));
    }

    /**
     * Add a Marker to a Google Map
     *
     * @param drawableResId drawable resource id that will be used to create BitMapDescriptor as an
     *                      icon
     * @param lat           the latitude coordinates
     * @param lng           the longitude coordinates
     */
    public void addMarker(@DrawableRes int drawableResId, double lat, double lng) {

        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(drawableResId))
                .position(new LatLng(lat, lng)));
    }

    /**
     * Add a Marker to Google Map and move camera position to LatLng with a specified zoom level.
     *
     * @param drawableResId drawable resource id that will be used to create BitMapDescriptor as an
     *                      icon
     * @param latLng the latitude and longitude values object as required for Google Maps
     * @param zoom Google Map zoom level
     */
    public void addMarker(@DrawableRes int drawableResId, @NonNull LatLng latLng, float zoom) {

        addMarker(drawableResId, latLng);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    // OnMapReadyCallback

    @Override
    public void onMapReady(GoogleMap googleMap) {

        AppCompatActivity activity = getActivityWeakReference().get();

        MapsInitializer.initialize(activity);
        map = googleMap;

        if (listener != null) {
            listener.onMapSetup(map);
        }
    }

    /**
     * An interface that should be called by the child view model. It will notify the child view
     * model when the SupportMapFragment has been added to the container and OnMapReadyCallback has
     * been called and additional setup has been completed.
     */
    public interface OnBaseViewModelMapReady {

        void onMapSetup(GoogleMap googleMap);
    }
}
