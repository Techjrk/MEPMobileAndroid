package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.content.ProjectDetailAddNoteActivity;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityLoginBinding;
import com.lecet.app.domain.UserDomain;

import io.realm.Realm;
//import com.lecet.app.databinding.ProjectDetailAddHeaderBinding;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

@Deprecated
public class ProjectDetailAddHeaderViewModel extends BaseObservable {
    private static final String TAG = "DEPRECATED";

    public ProjectDetailAddHeaderViewModel() {
        Log.d(TAG, "Constructor");
    }

    public void onClickAddNote(View view){
        Log.e(TAG, "onClickAddNote: Launch Next Activity");
        //Intent intent = new Intent(this, ProjectDetailAddNoteActivity.class);
        //startActivity(intent);
    }

    public void addPhotoOnClick(){
        //TODO - add intent and start Add Image Activity
    }

}
