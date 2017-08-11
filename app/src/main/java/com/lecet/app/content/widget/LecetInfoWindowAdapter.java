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
import com.lecet.app.viewmodel.MapInfoWindowViewModel;

/**
 * Created by Josué Rodríguez on 24/10/2016.
 */

public class LecetInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    InfoWindowLayoutBinding binding;

    public LecetInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.info_window_layout, null, false);

        if (marker.getTag() != null && marker.getTag() instanceof Project) {

            final MapInfoWindowViewModel viewModel = new MapInfoWindowViewModel(context, (Project) marker.getTag());

            binding.bidingStatus.setBackgroundResource(viewModel.getBidBackground());
            binding.bidingStatus.setText(viewModel.getBidStatus());
            binding.projectName.setText(viewModel.getProjectTitle());
            binding.address1.setText(viewModel.getAddress1());
           // binding.address2.setText(viewModel.getAddress2());
            binding.location.setText(viewModel.getProjectLocation());
            binding.participant.setText(viewModel.getParticipant());
            binding.companyLocation.setText(viewModel.getCompanyLocation());
        }

        return binding.getRoot();
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
