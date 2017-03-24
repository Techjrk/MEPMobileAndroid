package com.lecet.app.viewmodel;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.content.ProjectDetailAddNoteActivity;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.databinding.IncludeProjectDetailAddHeaderBinding;

/**
 * Created by ludwigvondrake on 3/24/17.
 */

public class IncludeProjectDetailAddHeaderViewModel extends LecetBaseActivity {
    private static final String TAG = "InclProjDetAddHeadVM";

    public IncludeProjectDetailAddHeaderViewModel() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void onClickAddNote(View view){
        Log.e(TAG, "onClickAddNote: Launched Next Activity");
        Intent intent = new Intent(this, ProjectDetailAddNoteActivity.class);
        startActivity(intent);
    }

    public void addPhotoOnClick(){

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
