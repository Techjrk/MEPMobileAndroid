package com.lecet.app.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.content.LecetConfirmDialogFragment;
import com.lecet.app.data.models.BindableString;
import com.lecet.app.data.models.NotePost;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.domain.ProjectDomain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddNoteViewModel extends AppCompatActivity{

    private Activity activity;
    private static final String TAG = "projDetailAddNoteVM";
    private BindableString noteTitle;
    private AlertDialog alert;
    private long projectID;
    private ProjectDomain projectDomain;

    private String body = "";

    public ProjectDetailAddNoteViewModel(Activity activity, long projectID, ProjectDomain projectDomain) {
        noteTitle = new BindableString("");
        this.activity = activity;
        this.projectID = projectID;
        this.projectDomain = projectDomain;

    }

    public BindableString getNoteTitle() {
        return noteTitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void onClickCancel(View view){
        Log.e(TAG, "onClickCancel: onClickCancel called");
        activity.setResult(RESULT_CANCELED);
        activity.finish();
    }

    public void onClickAdd(View view){
        DialogInterface.OnClickListener onCLick = new DialogInterface.OnClickListener(){//On Click Listener For Dialog
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Bundle extras = activity.getIntent();
                        Call<ProjectNote> call = projectDomain.postNote(projectID,new NotePost(noteTitle.getValue(), body, false));
                        call.enqueue(new Callback<ProjectNote>() {
                            @Override
                            public void onResponse(Call<ProjectNote> call, Response<ProjectNote> response) {

                                if (response.isSuccessful()) {

                                    activity.setResult(RESULT_OK);
                                    activity.finish();

                                } else {

                                    // TODO: Alert HTTP call error
                                }
                            }

                            @Override
                            public void onFailure(Call<ProjectNote> call, Throwable t) {
                                Log.e(TAG, "onResponse: failure");

                                //TODO: Display alert noting network failure
                            }
                        });

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

        alert = new AlertDialog.Builder(view.getContext()).create();
        if(body.equals("")){//Required Content of note
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", onCLick);
            alert.setMessage("Post Needs Body Text. Express yourself.");
            alert.show();
        }else {//Are you sure?
            alert.setMessage("You are about to post a public note.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Post Note", onCLick);
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onCLick);
            alert.show();
        }
    }

    private void postNote(){

    }

}

