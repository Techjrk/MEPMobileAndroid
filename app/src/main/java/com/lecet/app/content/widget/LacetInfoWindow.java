package com.lecet.app.content.widget;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.lecet.app.data.models.Project;

/**
 * Created by Josué Rodríguez on 24/10/2016.
 */

public class LacetInfoWindow implements GoogleMap.InfoWindowAdapter {

    Context context;
    Project project;

    public LacetInfoWindow(Context context) {
        this.context = context;
        this.project = project;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
