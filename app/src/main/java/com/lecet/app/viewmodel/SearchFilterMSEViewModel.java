package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lecet.app.R;

/**
 * Created by DomandTom 2016.
 */

public class SearchFilterMSEViewModel extends BaseObservable {

    private static final String TAG = "SearchFilterMSEViewModel";
    private AppCompatActivity activity;

    /**
     * Constructor
     */
    public SearchFilterMSEViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Display error message
     */
    public void errorDisplayMsg(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.error_network_title) + "\r\n" + message + "\r\n");
        Log.e("Error:", "Error " + message);
        builder.setMessage(message);
        builder.setNegativeButton(activity.getString(R.string.ok), null);
        Log.e("onFailure", "onFailure: " + message);
        builder.show();
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * OnClick handlers
     **/

    public void onClicked(View view) {
        Toast.makeText(activity, "Clicked.", Toast.LENGTH_SHORT).show();
/*     switch (view.getId()) {
         case R.id.location:
             break;
     }*/
    }

    public void onFilterClicked(View view) {
//        Toast.makeText(activity, "Filter clicked.", Toast.LENGTH_SHORT).show();
     //   Intent i = new Intent(activity, SearchFilterMSEActivity.class);
    //    activity.startActivityForResult(i, 0);
    }
}
