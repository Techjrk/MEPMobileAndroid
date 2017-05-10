package com.lecet.app.viewmodel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
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

    private AppCompatActivity activity;
    private long projectId;
    private long noteId;
    private String title = "";
    private String body = "";
    private ProjectDomain projectDomain;
    private int newTitleLength = 0;
    private AlertDialog alert;


    public ProjectAddNoteViewModel(AppCompatActivity activity, long projectId, long noteId, String title, String body, ProjectDomain projectDomain) {
        this.activity = activity;
        this.projectId = projectId;
        this.noteId = noteId;
        this.title = title;
        this.body = body;
        this.projectDomain = projectDomain;

        Log.d(TAG, "Constructor: projectId: " + projectId);
        Log.d(TAG, "Constructor: noteId: " + noteId);
        Log.d(TAG, "Constructor: title: " + title);
        Log.d(TAG, "Constructor: body: " + body);
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

        showAlertDialog(view, onCLick);
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
                    // TODO: Alert HTTP call error
                }
            }

            @Override
            public void onFailure(Call<ProjectNote> call, Throwable t) {
                Log.e(TAG, "postNote: onFailure: note post failed");
                //TODO: Display alert noting network failure
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

        showAlertDialog(view, onClick);
    }

    private void showAlertDialog(View view, DialogInterface.OnClickListener onClick) {
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

