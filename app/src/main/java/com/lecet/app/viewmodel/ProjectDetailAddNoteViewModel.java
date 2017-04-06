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

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddNoteViewModel extends AppCompatActivity{

    private Activity activity;
    private static final String TAG = "projDetailAddNoteVM";
    private BindableString noteTitle;
    private AlertDialog alert;

    private String body = "";

    public ProjectDetailAddNoteViewModel(Activity activity) {
        noteTitle = new BindableString("");
        this.activity = activity;

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
        activity.finish();
    }

    public void onClickAdd(View view){
        DialogInterface.OnClickListener onCLick = new DialogInterface.OnClickListener(){//On Click Listener For Dialog
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Bundle extras = activity.getIntent();
                        //TODO: add Functionality to post deployment
                        activity.finish();
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

}
