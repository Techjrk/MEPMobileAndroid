package com.lecet.app.viewmodel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.data.api.request.GeocodeRequest;
import com.lecet.app.data.models.NotePost;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.geocoding.GeocodeAddress;
import com.lecet.app.data.models.geocoding.GeocodeResult;
import com.lecet.app.domain.LocationDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.Log;
import com.lecet.app.utility.SimpleLecetDefaultAlert;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.lecet.app.R.string.google_api_key;

/**
 * Created by jasonm on 4/11/17.
 */

public class ProjectAddNoteViewModel extends BaseObservable {

    private static final String TAG = "ProjectAddNoteVM";

    private AppCompatActivity activity;
    private AlertDialog alert;
    private long projectId;
    private long id;
    private String title = "";
    private String body = "";
    private GeocodeRequest geocode;
    private String fullAddress= "";
    private ProjectDomain projectDomain;
    private LocationDomain locationDomain;
    private String mapsApiKey;
    private int newTitleLength = 0;


    public ProjectAddNoteViewModel(AppCompatActivity activity, long projectId, long id, String title, String body, ProjectDomain projectDomain, LocationDomain locationDomain) {
        this.activity = activity;
        this.projectId = projectId;
        this.id = id;
        this.title = title;
        this.body = body;
        this.geocode = new GeocodeRequest();
        this.projectDomain = projectDomain;
        this.locationDomain = locationDomain;

        Log.d(TAG, "Constructor: projectId: " + projectId);
        Log.d(TAG, "Constructor: id: " + id);
        Log.d(TAG, "Constructor: title: " + title);
        Log.d(TAG, "Constructor: body: " + body);
    }

    @Bindable
    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
        notifyPropertyChanged(BR.fullAddress);
    }

    public GeocodeRequest getGeocode() {
        return geocode;
    }

    public void setGeocode(GeocodeRequest geocode) {
        this.geocode = geocode;
    }

    public void handleLocationChanged(Location location) {
        Log.d(TAG, "handleLocationChanged() called with: location = [" + location + "]");

        geocode = new GeocodeRequest(location.getLatitude(), location.getLongitude());
        mapsApiKey = activity.getResources().getString(google_api_key);
        generateAddress(location.getLatitude(), location.getLongitude(), mapsApiKey);
        Log.d(TAG, "handleLocationChanged() fullAddress = [" + fullAddress + "]");

    }

    private void generateAddress(double latitude, double longitude, String mapsApiKey) {
        Log.d(TAG, "generateAddress: lat, lng: " + latitude + ", " + longitude);

        Call<GeocodeAddress> call = locationDomain.getAddressFromLocation(latitude, longitude, "street_address", mapsApiKey);

        call.enqueue(new Callback<GeocodeAddress>() {
            @Override
            public void onResponse(Call<GeocodeAddress> call, Response<GeocodeAddress> response) {
                Log.d(TAG, "generateAddress: onResponse: response.body: " + response.body());

                if (response.isSuccessful()) {
                    GeocodeAddress geocodeAddress = response.body();
                    if (geocodeAddress != null && geocodeAddress.getResults().size() > 0) {
                        GeocodeResult firstResult = geocodeAddress.getResults().get(0);
                        if (firstResult != null) {
                            setFullAddress(firstResult.getFormattedAddress());    // the one-line formatted address for display
                            Log.d(TAG, "generateAddress: onResponse: address request successful. fullAddress: " + fullAddress);
                        }
                    }
                } else {
                    Log.e(TAG, "generateAddress: onResponse: get address failed");
                }
            }

            @Override
            public void onFailure(Call<GeocodeAddress> call, Throwable t) {
                Log.e(TAG, "generateAddress: onFailure: get address failed");
            }
        });
    }

    public void onClickCancel(View view) {
        Log.d(TAG, "onClickCancel");
        activity.setResult(RESULT_CANCELED);
        activity.finish();
    }

    public void onClickAdd(View view){
        DialogInterface.OnClickListener onClickAddListener = new DialogInterface.OnClickListener(){//On Click Listener For Dialog
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        post(false);
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

        showPostAlertDialog(view, onClickAddListener);
    }

    public void onClickDelete(View view) {
        DialogInterface.OnClickListener onClickDeleteListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        delete();
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

        showDeleteAlertDialog(view, onClickDeleteListener);
    }

    private void post(boolean replaceExisting) {
        Log.d(TAG, "post: replaceExisting: " + replaceExisting);

        Call<ProjectNote> call;

        // for a new post
        if (!replaceExisting) {
            Log.d(TAG, "post: new note post");
            call = projectDomain.postNote(projectId, new NotePost(title, body, true, geocode, fullAddress));
        }
        // for updating an existing post
        else {
            Log.d(TAG, "post: update to existing note post");
            call = projectDomain.updateNote(id, new NotePost(title, body, true, geocode, fullAddress));
        }

        call.enqueue(new Callback<ProjectNote>() {
            @Override
            public void onResponse(Call<ProjectNote> call, Response<ProjectNote> response) {

                if (response.isSuccessful()) {

                    Log.d(TAG, "post: onResponse: note post successful");
                    activity.setResult(RESULT_OK);
                    activity.finish();

                } else {
                    Log.e(TAG, "post: onResponse: note post failed");
                    alert = SimpleLecetDefaultAlert.newInstance(activity, SimpleLecetDefaultAlert.HTTP_CALL_ERROR);
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ProjectNote> call, Throwable t) {
                Log.e(TAG, "post: onFailure: note post failed");
                alert = SimpleLecetDefaultAlert.newInstance(activity, SimpleLecetDefaultAlert.NETWORK_FAILURE);
                alert.show();
            }
        });
    }

    private void delete() {
        Log.d(TAG, "delete");

        Call<ProjectNote> call = projectDomain.deleteNote(id);

        call.enqueue(new Callback<ProjectNote>() {
            @Override
            public void onResponse(Call<ProjectNote> call, Response<ProjectNote> response) {

                if (response.isSuccessful()) {

                    Log.d(TAG, "delete: onResponse: note deletion successful");
                    activity.setResult(RESULT_OK);
                    activity.finish();

                } else {
                    Log.e(TAG, "delete: onResponse: note deletion failed");
                    alert = SimpleLecetDefaultAlert.newInstance(activity, SimpleLecetDefaultAlert.HTTP_CALL_ERROR);
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ProjectNote> call, Throwable t) {
                Log.e(TAG, "delete: onFailure: note deletion failed");
                alert = SimpleLecetDefaultAlert.newInstance(activity, SimpleLecetDefaultAlert.NETWORK_FAILURE);
                alert.show();
            }
        });
    }

    public void onClickUpdate(View view) {
        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        post(true);
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

        showPostAlertDialog(view, onClick);
    }

    private void showPostAlertDialog(View view, DialogInterface.OnClickListener onClick) {
        alert = new AlertDialog.Builder(view.getContext()).create();

        //Required body content of note
        if(body == null || body.isEmpty()) {
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", onClick);
            alert.setMessage("Note body is required.");
            alert.show();
        }
        //Are you sure?
        else {
            alert.setMessage("You are about to post a public note.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Post Note", onClick);
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onClick);
            alert.show();
        }
    }

    private void showDeleteAlertDialog(View view, DialogInterface.OnClickListener onClick) {
        alert = new AlertDialog.Builder(view.getContext()).create();

        //Are you sure?
        alert.setMessage("Are you sure you want to delete this note?");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", onClick);
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onClick);
        alert.show();
    }

    @Bindable
    public int getNewTitleLength() {
        return this.newTitleLength;
    }

    public void setNewTitleLength(int newTitleLength) {
        this.newTitleLength = newTitleLength;
        notifyPropertyChanged(BR.newTitleLength);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setNewTitleLength(title.length());
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean getEditMode() {
        return id > -1;
    }

}

