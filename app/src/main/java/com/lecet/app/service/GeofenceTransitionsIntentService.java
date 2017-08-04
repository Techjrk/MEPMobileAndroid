package com.lecet.app.service;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;

import com.lecet.app.R;
import com.lecet.app.utility.Log;

import java.io.Serializable;
import java.util.List;


public class GeofenceTransitionsIntentService extends IntentService {

    public static final String ACTION_GEO_FENCE_ENTER = "com.lecet.app.service.GeofenceTransitionsIntentService.ENTER";
    public static final String ACTION_GEO_FENCE_EXIT = "com.lecet.app.service.GeofenceTransitionsIntentService.EXIT";

    public static final String EXTRA_LOCATION = "location";
    public static final String EXTRA_GEOFENCE = "geofences";


    private static final String TAG = "GeofenceService";

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {

            String errorMessage = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        geofencingEvent.getTriggeringGeofences();

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            sendGeoBroadcast(ACTION_GEO_FENCE_ENTER, geofencingEvent.getTriggeringGeofences(), geofencingEvent.getTriggeringLocation());

        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            sendGeoBroadcast(ACTION_GEO_FENCE_EXIT, geofencingEvent.getTriggeringGeofences(), geofencingEvent.getTriggeringLocation());
        }
    }

    private void sendGeoBroadcast(String action, List<Geofence> geofences, Location location) {

        Intent intent = new Intent(action);
        intent.putExtra(EXTRA_LOCATION, location);
        intent.putExtra(EXTRA_GEOFENCE, (Serializable) geofences);
        sendBroadcast(intent);
    }

    private String getErrorString(int errorCode) {
        Resources mResources = getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return mResources.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return mResources.getString(R.string.geofence_too_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return mResources.getString(R.string.geofence_too_many_pending_intents);
            default:
                return mResources.getString(R.string.geofence_unknown_error);
        }
    }
}
