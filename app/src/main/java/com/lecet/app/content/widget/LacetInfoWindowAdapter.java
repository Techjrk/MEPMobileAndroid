package com.lecet.app.content.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.InfoWindowLayoutBinding;

/**
 * Created by Josué Rodríguez on 24/10/2016.
 */

public class LacetInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    InfoWindowLayoutBinding binding;

    public LacetInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.info_window_layout, null, false);

        if (marker.getTag() != null && marker.getTag() instanceof Project) {
            Project project = (Project) marker.getTag();
            binding.bidingStatus.setText(project.getStatusText());
            binding.projectName.setText(project.getTitle());
            binding.location.setText(String.format("%s, %s", project.getCity(), project.getState()));
            binding.address1.setText(project.getAddress1());
            binding.address2.setText(project.getAddress2());
        }

        return binding.getRoot();
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
