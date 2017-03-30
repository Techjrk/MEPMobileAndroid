package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.data.models.BindableString;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddNoteViewModel extends AppCompatActivity{

    private static final String TAG = "projDetailAddNoteVM";
    private BindableString noteTitle;

    private String body = "";

    public ProjectDetailAddNoteViewModel() {
        noteTitle = new BindableString("");
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
        finish();
        Log.e(TAG, "onClickCancel: onClickCancel called");
    }
}
