package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.content.AddProjectActivity;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.models.Geocode;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.ClickableMapInterface;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_CANCELED;
import static com.lecet.app.R.string.google_api_key;

/**
 * Created by jasonm on 5/15/17.
 */

public class AddProjectActivityViewModel extends BaseObservableViewModel implements ClickableMapInterface {

    private static final String TAG = "AddProjectActivityVM";

    private AppCompatActivity activity;
    private final String mapsApiKey;
    private Project project;

    public AddProjectActivityViewModel(AppCompatActivity appCompatActivity, String address, double latitude, double longitude, ProjectDomain projectDomain) {
        super(appCompatActivity);

        Log.d(TAG, "Constructor: address: " + address);
        Log.d(TAG, "Constructor: latitude: " + latitude);
        Log.d(TAG, "Constructor: longitude: " + longitude);

        this.activity = appCompatActivity;

        mapsApiKey = activity.getResources().getString(google_api_key);

        // create the new project obj
        project = new Project();

        // add address if it has been passed
        if(address != null && address.isEmpty()) {
            project.setAddress1(address);   //TODO - this only handles address line 1
        }

        // add lat and long in the form of Geocode obj if they have been passed
        if(project.getGeocode() == null) {
            Geocode geocode = new Geocode();
            geocode.setLat(latitude);
            geocode.setLng(longitude);
            project.setGeocode(geocode);
        }

        initMapImageView((AddProjectActivity) appCompatActivity, getMapUrl(project));

    }

    public String getMapUrl(Project project) {

        if (project.getGeocode() == null) {

            String mapStr;

            String generatedAddress = generateCenterPointAddress(project);

            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://maps.googleapis.com/maps/api/staticmap");
            sb2.append("?center=");
            sb2.append(generatedAddress);
            sb2.append("&zoom=16");
            sb2.append("&size=200x200");
            sb2.append("&markers=color:blue|");
            sb2.append(generatedAddress);
            sb2.append("&key=" + mapsApiKey);
            mapStr = String.format((sb2.toString().replace(' ', '+')), null);

            return mapStr;
        }

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=800x500&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }

    private void initMapImageView(AddProjectActivity activity, String mapUrl) {

        ImageView imageView = (ImageView) activity.findViewById(R.id.parallax_image_view);
        Picasso.with(imageView.getContext()).load(mapUrl).fit().into(imageView);
    }

    private String generateCenterPointAddress(Project project) {

        StringBuilder stringBuilder = new StringBuilder();

        if (project.getAddress1() != null) {
            stringBuilder.append(project.getAddress1());
            stringBuilder.append(",");
        }

        if (project.getAddress2() != null) {
            stringBuilder.append(project.getAddress2());
            stringBuilder.append(",");
        }

        if (project.getCity() != null) {
            stringBuilder.append(project.getCity());
            stringBuilder.append(",");
        }

        if (project.getState() != null) {
            stringBuilder.append(project.getState());
        }

        if (project.getZipPlus4() != null) {
            stringBuilder.append(",");
            stringBuilder.append(project.getZipPlus4());
        }


        return stringBuilder.toString();
    }

    public void onClicked(View view) {
        Log.d(TAG, "onClicked: " + view.getContext().getResources().getResourceEntryName(view.getId()));
    }

    public void onClickCancel(View view) {
        Log.d(TAG, "onClickCancel");
        activity.setResult(RESULT_CANCELED);
        activity.finish();
    }

    public void onClickSave(View view) {
        Log.d(TAG, "onClickSave: posting Project...");  //TODO - post project
    }

    public void onClickCounty(View view) {
        Log.d(TAG, "onClickCounty");
    }



    @Override
    public void onMapSelected(View view) {
        Log.d(TAG, "onMapSelected");
    }
}
