package com.lecet.app.viewmodel;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.data.models.NotePost;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.geocoding.AddressComponent;
import com.lecet.app.data.models.geocoding.GeocodeAddress;
import com.lecet.app.data.models.geocoding.GeocodeResult;
import com.lecet.app.domain.LocationDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.utility.SimpleLecetDefaultAlert;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.lecet.app.R.string.google_api_key;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectAddNoteViewModel extends BaseObservable {

    private static final String TAG = "ProjectAddNoteVM";

    private AppCompatActivity activity;
    private long projectId;
    private long noteId;
    private String title = "";
    private String body = "";
    private ProjectDomain projectDomain;
    private LocationDomain locationDomain;
    private String mapsApiKey;
    private String formattedAddress;
    private int newTitleLength = 0;
    private AlertDialog alert;


    public ProjectAddNoteViewModel(AppCompatActivity activity, long projectId, long noteId, String title, String body, ProjectDomain projectDomain, LocationDomain locationDomain) {
        this.activity = activity;
        this.projectId = projectId;
        this.noteId = noteId;
        this.title = title;
        this.body = body;
        this.projectDomain = projectDomain;
        this.locationDomain = locationDomain;

        Log.d(TAG, "Constructor: projectId: " + projectId);
        Log.d(TAG, "Constructor: noteId: " + noteId);
        Log.d(TAG, "Constructor: title: " + title);
        Log.d(TAG, "Constructor: body: " + body);

    }

    @Bindable
    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
        notifyPropertyChanged(BR.formattedAddress);
    }

    public void handleLocationChanged(Location location) {
        Log.d(TAG, "handleLocationChanged() called with: location = [" + location + "]");

        mapsApiKey = activity.getResources().getString(google_api_key);
        generateAddress(location.getLatitude(), location.getLongitude(), mapsApiKey);
        Log.d(TAG, "handleLocationChanged() formattedAddress = [" + formattedAddress + "]");

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
                            setFormattedAddress(firstResult.getFormattedAddress());    // the one-line formatted address for display
                            Log.d(TAG, "generateAddress: onResponse: address request successful. formattedAddress: " + formattedAddress);
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




    public void onClickCancel(View view){
        activity.setResult(RESULT_CANCELED);
        activity.finish();
    }

    public void onClickAdd(View view){
        DialogInterface.OnClickListener onClickAddListener = new DialogInterface.OnClickListener(){//On Click Listener For Dialog
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        postNote(false);
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

        showPostNoteAlertDialog(view, onClickAddListener);
    }

    public void onClickDelete(View view) {
        DialogInterface.OnClickListener onClickDeleteListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteNote();
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

        showDeleteNoteAlertDialog(view, onClickDeleteListener);
    }

    private void postNote(boolean replaceExisting) {
        Log.d(TAG, "postNote: replaceExisting: " + replaceExisting);

        Call<ProjectNote> call;

        // for a new post
        if (!replaceExisting) {
            Log.d(TAG, "postNote: new note post");
            call = projectDomain.postNote(projectId, new NotePost(title, body, true));
        }
        // for updating an existing post
        else {
            Log.d(TAG, "postNote: update to existing note post");
            call = projectDomain.updateNote(noteId, new NotePost(title, body, true));
        }

        call.enqueue(new Callback<ProjectNote>() {
            @Override
            public void onResponse(Call<ProjectNote> call, Response<ProjectNote> response) {

                if (response.isSuccessful()) {

                    Log.d(TAG, "postNote: onResponse: note post successful");
                    activity.setResult(RESULT_OK);
                    activity.finish();

                } else {
                    Log.e(TAG, "postNote: onResponse: note post failed");
                    alert = SimpleLecetDefaultAlert.newInstance(activity, SimpleLecetDefaultAlert.HTTP_CALL_ERROR);
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ProjectNote> call, Throwable t) {
                Log.e(TAG, "postNote: onFailure: note post failed");
                alert = SimpleLecetDefaultAlert.newInstance(activity, SimpleLecetDefaultAlert.NETWORK_FAILURE);
                alert.show();
            }
        });
    }

    private void deleteNote() {
        Log.d(TAG, "deleteNote");

        Call<ProjectNote> call = projectDomain.deleteNote(noteId);

        call.enqueue(new Callback<ProjectNote>() {
            @Override
            public void onResponse(Call<ProjectNote> call, Response<ProjectNote> response) {

                if (response.isSuccessful()) {

                    Log.d(TAG, "deleteNote: onResponse: note deletion successful");
                    activity.setResult(RESULT_OK);
                    activity.finish();

                } else {
                    Log.e(TAG, "deleteNote: onResponse: note deletion failed");
                    alert = SimpleLecetDefaultAlert.newInstance(activity, SimpleLecetDefaultAlert.HTTP_CALL_ERROR);
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ProjectNote> call, Throwable t) {
                Log.e(TAG, "deleteNote: onFailure: note deletion failed");
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
                        postNote(true);
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

        showPostNoteAlertDialog(view, onClick);
    }

    private void showPostNoteAlertDialog(View view, DialogInterface.OnClickListener onClick) {
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

    private void showDeleteNoteAlertDialog(View view, DialogInterface.OnClickListener onClick) {
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
        return noteId > -1;
    }

}

