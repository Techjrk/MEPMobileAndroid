package com.lecet.app.content.widget;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.InfoWindowLayoutCreatePinBinding;
import com.lecet.app.viewmodel.MapInfoWindowCreatePinViewModel;

/**
 * Created by Josué Rodríguez on 24/10/2016.
 */

public class LecetInfoWindowCreatePinAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    InfoWindowLayoutCreatePinBinding binding;

    public LecetInfoWindowCreatePinAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.info_window_layout_create_pin, null, false);

        if (marker.getTag() != null && marker.getTag() instanceof Project) {

            final MapInfoWindowCreatePinViewModel viewModel = new MapInfoWindowCreatePinViewModel(context, (Project) marker.getTag());

            /*binding.bidingStatus.setBackgroundResource(viewModel.getBidBackground());
            binding.bidingStatus.setText(viewModel.getBidStatus());
            binding.projectName.setText(viewModel.getProjectTitle());
            binding.location.setText(viewModel.getProjectLocation());
            binding.address1.setText(viewModel.getAddress1());
            binding.address2.setText(viewModel.getAddress2());*/
        }

        return binding.getRoot();
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
