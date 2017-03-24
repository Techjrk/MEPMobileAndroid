package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class ProjectDetailAddNoteViewModel extends AppCompatActivity{

    public ProjectDetailAddNoteViewModel() {
    }

    public void onClickCancel(View view){
        finish();
    }
}
