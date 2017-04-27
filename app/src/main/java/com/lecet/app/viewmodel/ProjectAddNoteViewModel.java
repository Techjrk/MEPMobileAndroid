package com.lecet.app.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.data.models.NotePost;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.domain.ProjectDomain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectAddNoteViewModel extends BaseObservable {

    private static final String TAG = "ProjectAddNoteVM";

    private Activity activity;
    private long projectID;
    private long noteId;
    //private BindableString noteTitle;
    private String newTitle = "";
    private int newTitleLength = 0;
    private ProjectDomain projectDomain;
    private AlertDialog alert;

    private String body = "";

    public ProjectAddNoteViewModel(Activity activity, long projectID, long noteId, String title, String body, ProjectDomain projectDomain) {
        /*if(noteTitle == null) {
            this.noteTitle = new BindableString("");
        }
        else this.noteTitle = new BindableString(title);*/

        this.activity = activity;
        this.projectID = projectID;
        this.noteId = noteId;
        this.newTitle = title;
        this.body = body;
        this.projectDomain = projectDomain;

        Log.d(TAG, "ProjectAddNoteViewModel: Constructor: noteId: " + noteId);
        Log.d(TAG, "ProjectAddNoteViewModel: Constructor: title: " + title);
        Log.d(TAG, "ProjectAddNoteViewModel: Constructor: body: " + body);
    }

    /*public BindableString getNoteTitle() {
        return noteTitle;
    }*/

    @Bindable
    public int getNewTitleLength() {
        return this.newTitleLength;
    }

    public void setNewTitleLength(int newTitleLength) {
        this.newTitleLength = newTitleLength;
        notifyPropertyChanged(BR.newTitleLength);
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
        setNewTitleLength(newTitle.length());
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

    public void onClickCancel(View view){
        Log.e(TAG, "onClickCancel");
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
                        Call<ProjectNote> call = projectDomain.postNote(projectID,new NotePost(newTitle, body, false));
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
                                Log.e(TAG, "onClickAdd: onResponse: failure");

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

        showAlertDialog(view, onCLick);
    }

    public void onClickUpdate(View view) {
        DialogInterface.OnClickListener onCLick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Call<ProjectNote> call = projectDomain.updateNote(noteId, new NotePost(noteTitle.getValue(), body, false));
                        Call<ProjectNote> call = projectDomain.updateNote(noteId, new NotePost(newTitle, body, false));
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
                                Log.e(TAG, "onClickUpdate: onResponse: failure");

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

        showAlertDialog(view, onCLick);
    }

    private void showAlertDialog(View view, DialogInterface.OnClickListener onCLick) {
        alert = new AlertDialog.Builder(view.getContext()).create();

        //Required Content of note
        if(body.equals("")) {
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", onCLick);
            alert.setMessage("Note content is required.");
            alert.show();
        }
        //Are you sure?
        else {
            alert.setMessage("You are about to post a public note.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Post Note", onCLick);
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onCLick);
            alert.show();
        }
    }


}

