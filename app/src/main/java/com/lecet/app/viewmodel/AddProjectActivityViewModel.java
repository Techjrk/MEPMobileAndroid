package com.lecet.app.viewmodel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.AddProjectActivity;
import com.lecet.app.content.SearchFilterProjectTypeActivity;
import com.lecet.app.content.SearchFilterStageActivity;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.models.Geocode;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectPost;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.ClickableMapInterface;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.lecet.app.R.string.google_api_key;

/**
 * Created by jasonm on 5/15/17.
 */

public class AddProjectActivityViewModel extends BaseObservableViewModel implements ClickableMapInterface {

    private static final String TAG = "AddProjectActivityVM";

    private AppCompatActivity activity;
    private final String mapsApiKey;
    private ProjectDomain projectDomain;
    private AlertDialog alert;

    // values for posting to API or which can be displayed as native value
    private ProjectPost projectPost;
    private double latitude;
    private double longitude;
    private String targetStartDate;

    // values for display only
    private String typeSelect;
    private String stageSelect;
    private Calendar calendar;


    public AddProjectActivityViewModel(AppCompatActivity appCompatActivity, String address, double latitude, double longitude, ProjectDomain projectDomain) {
        super(appCompatActivity);

        Log.d(TAG, "Constructor: address: " + address);
        Log.d(TAG, "Constructor: latitude: " + latitude);
        Log.d(TAG, "Constructor: longitude: " + longitude);

        this.activity = appCompatActivity;
        this.projectDomain = projectDomain;
        this.latitude = latitude;
        this.longitude = longitude;

        mapsApiKey = activity.getResources().getString(google_api_key);

        // create the new projectPost obj
        projectPost = new ProjectPost(latitude, longitude);

        // add address if it has been passed
        if (address != null && address.isEmpty()) {
            projectPost.setAddress1(address);   //TODO - this only handles address line 1
        }

        // add lat and long in the form of Geocode obj if they have been passed
        if (projectPost.getGeocode() == null) {
            Geocode geocode = new Geocode();
            geocode.setLat(latitude);
            geocode.setLng(longitude);
            projectPost.setGeocode(geocode);
        }

        initMapImageView((AddProjectActivity) appCompatActivity, getMapUrl(projectPost));

    }

    public String getMapUrl(ProjectPost projectPost) {

        if (projectPost.getGeocode() == null) {

            String mapStr;

            String generatedAddress = generateCenterPointAddress(projectPost);

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
                        "markers=color:blue|%.6f,%.6f&key=%s", projectPost.getGeocode().getLat(), projectPost.getGeocode().getLng(),
                projectPost.getGeocode().getLat(), projectPost.getGeocode().getLng(), mapsApiKey);
    }

    private void initMapImageView(AddProjectActivity activity, String mapUrl) {

        ImageView imageView = (ImageView) activity.findViewById(R.id.parallax_image_view);
        Picasso.with(imageView.getContext()).load(mapUrl).fit().into(imageView);
    }

    private String generateCenterPointAddress(ProjectPost projectPost) {

        StringBuilder stringBuilder = new StringBuilder();

        if (projectPost.getAddress1() != null) {
            stringBuilder.append(projectPost.getAddress1());
            stringBuilder.append(",");
        }

        if (projectPost.getAddress2() != null) {
            stringBuilder.append(projectPost.getAddress2());
            stringBuilder.append(",");
        }

        if (projectPost.getCity() != null) {
            stringBuilder.append(projectPost.getCity());
            stringBuilder.append(",");
        }

        if (projectPost.getState() != null) {
            stringBuilder.append(projectPost.getState());
        }

        if (projectPost.getZipPlus4() != null) {
            stringBuilder.append(",");
            stringBuilder.append(projectPost.getZipPlus4());
        }


        return stringBuilder.toString();
    }

    private void postProject() {
        Log.d(TAG, "postProject: projectPost post: " + projectPost);

        Call<Project> call = projectDomain.postProject(projectPost);

        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {

                if (response.isSuccessful()) {
                    Project createdProject = response.body();
                    Log.d(TAG, "postProject: onResponse: projectPost post successful. Created project ID: " + createdProject.getId());
                    activity.setResult(RESULT_OK);
                    activity.finish();
                }
                else {
                    Log.e(TAG, "postProject: onResponse: projectPost post failed");
                    // TODO: Alert HTTP call error
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                Log.e(TAG, "postProject: onFailure: projectPost post failed");
                //TODO: Display alert noting network failure
            }
        });
    }

    private void updateProject(long projectId) {
        Log.d(TAG, "updateProject: projectPost post: " + projectPost);

        Call<Project> call = projectDomain.updateProject(projectId, projectPost);

        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "updateProject: onResponse: projectPost update successful");
                    activity.setResult(RESULT_OK);
                    activity.finish();
                }
                else {
                    Log.e(TAG, "updateProject: onResponse: projectPost update failed");
                    // TODO: Alert HTTP call error
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                Log.e(TAG, "updateProject: onFailure: projectPost update failed");
                //TODO: Display alert noting network failure
            }
        });
    }

    private void showPostProjectAlertDialog(View view, DialogInterface.OnClickListener onClick) {

        alert = new AlertDialog.Builder(view.getContext()).create();

        // Required content of project post
        if(projectPost == null || projectPost.getGeocode() == null) {
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", onClick);
            alert.setMessage("A location is required.");
            alert.show();
        }

        // Are you sure?
        else {
            alert.setMessage("You are about to save this new project.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Save Project", onClick);
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onClick);
            alert.show();
        }
    }



    /*
     * Clicks 
     */
    public void onClicked(View view) {
        Log.d(TAG, "onClicked: " + view.getContext().getResources().getResourceEntryName(view.getId()));
        Intent i = null;
        int id = view.getId();
        int section = 0;
        switch (id) {
            case R.id.add_project_type:
                section = SearchFilterAllTabbedViewModel.TYPE;
                i = new Intent(activity, SearchFilterProjectTypeActivity.class);
                break;
            case R.id.stage:
                section = SearchFilterAllTabbedViewModel.STAGE;
                i = new Intent(activity, SearchFilterStageActivity.class);
                break;
            case R.id.target_set_date:
                calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                };
                new DatePickerDialog(activity, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            default:
                Log.w(TAG, "onClicked: Warning: Unsupported view id clicked: " + id);
                return;
        }
        activity.startActivityForResult(i, section);
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        setTargetStartDate(sdf.format(calendar.getTime()));
    }
    public void onClickCancel(View view) {
        Log.d(TAG, "onClickCancel");
        activity.setResult(RESULT_CANCELED);
        activity.finish();
    }

    public void onClickSave(View view) {
        Log.d(TAG, "onClickSave");

        DialogInterface.OnClickListener onClickAddListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        postProject();
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        showPostProjectAlertDialog(view, onClickAddListener);
    }

    public void onClickCounty(View view) {
        Log.d(TAG, "onClickCounty");
    }


    @Override
    public void onMapSelected(View view) {
        Log.d(TAG, "onMapSelected");
    }



    /*
     * Bindings for values to be posted to the API or which can be displayed as native values
     */

    @Bindable
    public ProjectPost getProjectPost() {
        return projectPost;
    }

    @Bindable
    public String getTargetStartDate() {
        return targetStartDate;
    }

    public void setTargetStartDate(String targetStartDate) {
        this.targetStartDate = targetStartDate;
        notifyPropertyChanged(BR.targetStartDate);
    }


    /*
     * Bindings for values to be used for visual display only
     */

    @Bindable
    public String getStageSelect() {
        return stageSelect;
    }

    public void setStageSelect(String stageSelect) {
        this.stageSelect = stageSelect;
        notifyPropertyChanged(BR.stageSelect);
    }

    @Bindable
    public String getTypeSelect() {
        return typeSelect;
    }

    public void setTypeSelect(String typeSelect) {
        this.typeSelect = typeSelect;
        notifyPropertyChanged(BR.typeSelect);
    }




}
